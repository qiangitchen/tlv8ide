/*
 * TLV8 DbViewer - Database SQL export utilities
 *
 */
package zigen.plugin.db.export;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.ConnectionManager;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableConstraintColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TableIDXColumn;
import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.core.TablePKColumn;
import zigen.plugin.db.core.TableSearcher;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.rule.AbstractSQLCreatorFactory;
import zigen.plugin.db.core.rule.DefaultColumnSearcherFactory;
import zigen.plugin.db.core.rule.DefaultConstraintSearcherFactory;
import zigen.plugin.db.core.rule.IColumnSearcherFactory;
import zigen.plugin.db.core.rule.IConstraintSearcherFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ext.oracle.internal.OracleIndexSearcher;
import zigen.plugin.db.ext.oracle.internal.OracleSynonymInfoSearcher;
import zigen.plugin.db.ext.oracle.internal.SynonymInfo;
import zigen.plugin.db.ui.editors.exceptions.NotFoundSynonymInfoException;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.OracleColumn;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.Synonym;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.views.TableSearchThread;

/**
 * Utility for exporting tables/views DDL as SQL script.
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ExportSQLUtil {

	/** only TABLE / VIEW folders are exported */
	public static final String TargetFolderPattern = "^TABLE|^VIEW"; //$NON-NLS-1$

	private ExportSQLUtil() {
	}

	/**
	 * Collect all table/view nodes under the selected element. For DataBase and
	 * Schema nodes the tree is lazily populated, so tables are loaded from JDBC
	 * metadata into a detached (cloned) node to avoid touching the UI tree.
	 *
	 * @param element
	 *            DataBase, Schema, Folder or ITable node.
	 * @return List of ITable (tables and views).
	 */
	public static List collectTables(Object element) throws Exception {
		List result = new ArrayList();
		if (element instanceof DataBase) {
			DataBase db = (DataBase) element;
			Connection con = null;
			try {
				con = ConnectionManager.getConnection(db.getDbConfig());
				TreeLeaf[] childrens = db.getChildrens();
				for (int i = 0; i < childrens.length; i++) {
					TreeLeaf child = childrens[i];
					if (child instanceof Schema) {
						Schema schema = (Schema) ((Schema) child).clone();
						schema.setParent(((Schema) child).getParent());
						result.addAll(loadSchemaTables(con, schema));
					} else if (child instanceof Folder) {
						result.addAll(loadFolderTables(con, (Folder) child));
					}
				}
			} finally {
				ConnectionManager.closeConnection(con);
			}
		} else if (element instanceof Schema) {
			Schema src = (Schema) element;
			Schema schema = (Schema) src.clone();
			schema.setParent(src.getParent());
			Connection con = null;
			try {
				con = ConnectionManager.getConnection(schema.getDbConfig());
				result.addAll(loadSchemaTables(con, schema));
			} finally {
				ConnectionManager.closeConnection(con);
			}
		} else if (element instanceof Folder) {
			// folder already exists in the tree, reuse its children
			addTreeTables(((Folder) element).getChildrens(), result);
		} else if (element instanceof ITable) {
			result.add(element);
		}
		return result;
	}

	/**
	 * Load TABLE/VIEW nodes of the given (possibly detached) schema from JDBC
	 * metadata.
	 */
	public static List loadSchemaTables(Connection con, Schema schema) throws Exception {
		List result = new ArrayList();
		IDBConfig config = schema.getDbConfig();
		boolean schemaSupport = schema.getDataBase().isSchemaSupport();
		String[] tableTypes = schema.getDataBase().getTableType();
		for (int i = 0; i < tableTypes.length; i++) {
			String type = tableTypes[i];
			if (!type.toUpperCase().matches(TargetFolderPattern)) {
				continue;
			}
			String schemaPattern = schemaSupport ? schema.getName() : null;
			TableInfo[] tables = TableSearcher.execute(con, schemaPattern, new String[] { type });
			TableSearchThread.addFolderAndTables(con, schema, type, tables);
			TreeLeaf leaf = schema.getChild(type);
			if (leaf instanceof Folder) {
				addTreeTables(((Folder) leaf).getChildrens(), result);
			}
		}
		return result;
	}

	/**
	 * Load table nodes of an existing folder under a DataBase node (non
	 * schema-support DB) from JDBC metadata into a detached folder.
	 */
	private static List loadFolderTables(Connection con, Folder folder) throws Exception {
		List result = new ArrayList();
		String type = folder.getName();
		if (!type.toUpperCase().matches(TargetFolderPattern)) {
			return result;
		}
		TableInfo[] tables = TableSearcher.execute(con, null, new String[] { type });
		TableSearchThread.AddTables(con, null, folder, tables);
		addTreeTables(folder.getChildrens(), result);
		return result;
	}

	private static void addTreeTables(TreeLeaf[] leafs, List result) {
		for (int i = 0; i < leafs.length; i++) {
			if (leafs[i] instanceof ITable) {
				result.add(leafs[i]);
			}
		}
	}

	/**
	 * Ensure that the columns/PK/FK/indexes of the table node are loaded, so
	 * that createDDL() can generate a complete DDL.
	 */
	public static void ensureLoaded(Connection con, ITable table) throws Exception {
		if (table.isExpanded() && !(hasLoadingChild(table))) {
			ensureTableComment(con, table);
			return;
		}
		table.removeChild(table.getChild(DbPluginConstant.TREE_LEAF_LOADING));
		table.setExpanded(true);
		loadColumn(con, table);
		ensureTableComment(con, table);
	}

	/**
	 * Best-effort fallback: if the table node has no remarks (some drivers do
	 * not return table level REMARKS from DatabaseMetaData.getTables), query
	 * the database specific catalog views to fetch the table comment.
	 */
	private static void ensureTableComment(Connection con, ITable table) {
		if (table.getRemarks() != null && !"".equals(table.getRemarks())) { //$NON-NLS-1$
			return;
		}
		Statement st = null;
		ResultSet rs = null;
		try {
			int dbType = DBType.getType(con.getMetaData());
			String tableName = table.getName();
			String schemaName = table.getSchemaName();
			boolean schemaSupport = table.getDataBase() != null && table.getDataBase().isSchemaSupport();
			String sql = null;

			switch (dbType) {
			case DBType.DB_TYPE_ORACLE:
			case DBType.DB_TYPE_DM:
			case DBType.DB_TYPE_KINGBASEES:
				sql = "SELECT COMMENTS FROM USER_TAB_COMMENTS WHERE TABLE_NAME = '" //$NON-NLS-1$
						+ SQLUtil.encodeQuotation(tableName.toUpperCase()) + "'"; //$NON-NLS-1$
				break;
			case DBType.DB_TYPE_MYSQL:
				// use DATABASE() instead of getCatalog(): Connector/J 8 may
				// return null for getCatalog() (nullCatalogMeansCurrent=false)
				sql = "SELECT TABLE_COMMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = '" //$NON-NLS-1$
						+ SQLUtil.encodeQuotation(tableName) + "'"; //$NON-NLS-1$
				break;
			case DBType.DB_TYPE_POSTGRESQL:
				String qualified = (schemaSupport && schemaName != null && schemaName.length() > 0)
						? schemaName + "." + tableName : tableName; //$NON-NLS-1$
				sql = "SELECT obj_description('" + SQLUtil.encodeQuotation(qualified) + "'::regclass) AS COMMENTS"; //$NON-NLS-1$ //$NON-NLS-2$
				break;
			case DBType.DB_TYPE_SQLSERVER:
				String objName = (schemaSupport && schemaName != null && schemaName.length() > 0)
						? schemaName + "." + tableName : tableName; //$NON-NLS-1$
				sql = "SELECT CAST(ep.value AS nvarchar(4000)) AS COMMENTS FROM sys.extended_properties ep WHERE ep.class = 1 AND ep.minor_id = 0 AND ep.name = 'MS_Description' AND ep.major_id = OBJECT_ID('" //$NON-NLS-1$
						+ SQLUtil.encodeQuotation(objName) + "')"; //$NON-NLS-1$
				break;
			case DBType.DB_TYPE_H2:
				sql = "SELECT REMARKS FROM information_schema.TABLES WHERE TABLE_NAME = '" //$NON-NLS-1$
						+ SQLUtil.encodeQuotation(tableName.toUpperCase()) + "'"; //$NON-NLS-1$
				break;
			default:
				return;
			}

			st = con.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				String comments = rs.getString(1);
				if (comments != null && !"".equals(comments.trim())) { //$NON-NLS-1$
					table.setRemarks(comments.trim());
				}
			}
		} catch (Exception e) {
			// best effort only, ignore failures
			DbPlugin.log(e);
		} finally {
			try {
				if (rs != null) rs.close();
			} catch (Exception e) {
			}
			try {
				if (st != null) st.close();
			} catch (Exception e) {
			}
		}
	}

	private static boolean hasLoadingChild(ITable table) {
		return table.getChild(DbPluginConstant.TREE_LEAF_LOADING) != null;
	}

	/**
	 * Generate a dialect specific "drop table if exists" statement that is
	 * executed before the CREATE TABLE statement.
	 *
	 * @param dbType
	 *            DBType constant of the database.
	 * @param table
	 *            table node.
	 * @param schemaSupport
	 *            true if the database supports schemas.
	 * @return the drop statement, or null when not applicable.
	 */
	public static String createDropTableIfExists(int dbType, ITable table, boolean schemaSupport) {
		String schemaName = table.getSchemaName();
		String name = table.getName();
		String plainName = (schemaSupport && schemaName != null && schemaName.length() > 0) ? schemaName + "." + name : name; //$NON-NLS-1$
		switch (dbType) {
		case DBType.DB_TYPE_ORACLE:
			// Oracle has no DROP TABLE IF EXISTS, use an anonymous PL/SQL block
			StringBuffer oracle = new StringBuffer();
			oracle.append("BEGIN").append(DbPluginConstant.LINE_SEP);
			oracle.append("  EXECUTE IMMEDIATE 'DROP TABLE ").append(plainName).append("';").append(DbPluginConstant.LINE_SEP);
			oracle.append("EXCEPTION").append(DbPluginConstant.LINE_SEP);
			oracle.append("  WHEN OTHERS THEN").append(DbPluginConstant.LINE_SEP);
			oracle.append("    IF SQLCODE != -942 THEN RAISE; END IF;").append(DbPluginConstant.LINE_SEP);
			oracle.append("END;").append(DbPluginConstant.LINE_SEP);
			oracle.append("/").append(DbPluginConstant.LINE_SEP);
			return oracle.toString();
		case DBType.DB_TYPE_SQLSERVER:
			StringBuffer sqlserver = new StringBuffer();
			sqlserver.append("IF OBJECT_ID('").append(plainName).append("', 'U') IS NOT NULL").append(DbPluginConstant.LINE_SEP);
			sqlserver.append("DROP TABLE ").append(table.getSqlTableName()).append(";").append(DbPluginConstant.LINE_SEP);
			return sqlserver.toString();
		case DBType.DB_TYPE_DB2:
			// DB2 has no DROP TABLE IF EXISTS either
			StringBuffer db2 = new StringBuffer();
			db2.append("BEGIN").append(DbPluginConstant.LINE_SEP);
			db2.append("  DECLARE CONTINUE HANDLER FOR SQLSTATE '42704' BEGIN END;").append(DbPluginConstant.LINE_SEP);
			db2.append("  EXECUTE IMMEDIATE 'DROP TABLE ").append(plainName).append("';").append(DbPluginConstant.LINE_SEP);
			db2.append("END@").append(DbPluginConstant.LINE_SEP);
			return db2.toString();
		default:
			// MySQL / PostgreSQL / SQLite / H2 / HSQLDB / Derby / DM / Kingbase...
			return "DROP TABLE IF EXISTS " + table.getSqlTableName() + ";"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Generate the DDL of the given table/view.
	 */
	public static String createDDL(IDBConfig config, ITable table, boolean visibleSchemaName) throws Exception {
		ISQLCreatorFactory factory = AbstractSQLCreatorFactory.getFactoryNoCache(config, table);
		factory.setVisibleSchemaName(visibleSchemaName);
		return factory.createDDL();
	}

	/**
	 * Build the whole SQL script text.
	 *
	 * @param dbName
	 *            database name shown in the header.
	 * @param config
	 *            DB configuration.
	 * @param tables
	 *            list of ITable.
	 * @return the SQL script text.
	 */
	public static String buildSQLText(String dbName, IDBConfig config, List tables) throws Exception {
		boolean schemaSupport = false;
		DataBase db = null;
		if (tables != null && !tables.isEmpty()) {
			ITable first = (ITable) tables.get(0);
			db = first.getDataBase();
			if (db != null) {
				schemaSupport = db.isSchemaSupport();
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append("-- =============================================================").append(DbPluginConstant.LINE_SEP);
		sb.append("-- SQL Export (tables & views)").append(DbPluginConstant.LINE_SEP);
		sb.append("-- Database  : ").append(dbName).append(DbPluginConstant.LINE_SEP);
		sb.append("-- Objects   : ").append(tables == null ? 0 : tables.size()).append(DbPluginConstant.LINE_SEP);
		sb.append("-- Exported  : ").append(new Date()).append(DbPluginConstant.LINE_SEP);
		sb.append("-- Tool      : TLV8 DbViewer").append(DbPluginConstant.LINE_SEP);
		sb.append("-- =============================================================").append(DbPluginConstant.LINE_SEP);

		int dbType = DBType.getType(config);
		int count = tables == null ? 0 : tables.size();
		for (int i = 0; i < count; i++) {
			ITable table = (ITable) tables.get(i);
			String type = table.getFolderName();
			sb.append(DbPluginConstant.LINE_SEP);
			sb.append("-- ------------------------------------------------------------").append(DbPluginConstant.LINE_SEP);
			sb.append("-- ").append(type).append(" : ").append(table.getName());
			if (schemaSupport && table.getSchemaName() != null && table.getSchemaName().length() > 0) {
				sb.append(" (schema: ").append(table.getSchemaName()).append(")");
			}
			sb.append(DbPluginConstant.LINE_SEP);
			sb.append("-- ------------------------------------------------------------").append(DbPluginConstant.LINE_SEP);
			try {
				// drop the object if it already exists, then re-create it
				if (!"VIEW".equals(type)) { //$NON-NLS-1$
					String dropSQL = createDropTableIfExists(dbType, table, schemaSupport);
					if (dropSQL != null) {
						sb.append(dropSQL).append(DbPluginConstant.LINE_SEP);
					}
				}
				String ddl = createDDL(config, table, schemaSupport);
				sb.append(ddl);
				if (ddl != null && !ddl.trim().endsWith(";")) { //$NON-NLS-1$
					sb.append(";"); //$NON-NLS-1$
				}
				sb.append(DbPluginConstant.LINE_SEP);
				// non-unique indexes (unique indexes are already inlined as
				// UNIQUE constraints in the CREATE TABLE statement)
				String idxSQL = buildIndexStatements(table);
				if (idxSQL.length() > 0) {
					sb.append(idxSQL);
				}
			} catch (Exception e) {
				sb.append("-- [ERROR] failed to generate DDL: ").append(e.getMessage()).append(DbPluginConstant.LINE_SEP);
				DbPlugin.log(e);
			}
		}
		return sb.toString();
	}

	/**
	 * Generate CREATE INDEX statements for the non-unique indexes of the
	 * table. Unique indexes are skipped because they are already inlined in
	 * the CREATE TABLE statement as UNIQUE constraints (or the PRIMARY KEY).
	 *
	 * @return SQL text, empty when the table has no non-unique index.
	 */
	public static String buildIndexStatements(ITable table) {
		TableIDXColumn[] nonuidxs = table.getTableNonUIDXColumns();
		if (nonuidxs == null || nonuidxs.length == 0) {
			return ""; //$NON-NLS-1$
		}

		// PK-named indexes are skipped (already defined as PRIMARY KEY)
		java.util.Set pkNames = new java.util.HashSet();
		TablePKColumn[] pks = table.getTablePKColumns();
		if (pks != null) {
			for (int i = 0; i < pks.length; i++) {
				if (pks[i].getName() != null) {
					pkNames.add(pks[i].getName());
				}
			}
		}

		// group columns by index name
		java.util.LinkedHashMap groups = new java.util.LinkedHashMap();
		for (int i = 0; i < nonuidxs.length; i++) {
			TableIDXColumn idx = nonuidxs[i];
			String indexName = idx.getName();
			if (indexName == null || "".equals(indexName) || pkNames.contains(indexName)) { //$NON-NLS-1$
				continue;
			}
			List cols = (List) groups.get(indexName);
			if (cols == null) {
				cols = new ArrayList();
				groups.put(indexName, cols);
			}
			cols.add(idx);
		}

		StringBuffer sb = new StringBuffer();
		for (java.util.Iterator iter = groups.entrySet().iterator(); iter.hasNext();) {
			java.util.Map.Entry entry = (java.util.Map.Entry) iter.next();
			String indexName = (String) entry.getKey();
			List cols = (List) entry.getValue();

			// order columns by ordinal position
			java.util.Collections.sort(cols, new java.util.Comparator() {
				public int compare(Object a, Object b) {
					return ((TableIDXColumn) a).getOrdinal_position() - ((TableIDXColumn) b).getOrdinal_position();
				}
			});

			StringBuffer colList = new StringBuffer();
			for (int i = 0; i < cols.size(); i++) {
				if (i > 0) {
					colList.append(", "); //$NON-NLS-1$
				}
				colList.append(((TableIDXColumn) cols.get(i)).getColumnName());
			}

			sb.append("CREATE INDEX ").append(indexName).append(" ON ").append(table.getSqlTableName()).append(" (") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					.append(colList).append(");").append(DbPluginConstant.LINE_SEP); //$NON-NLS-1$
		}
		return sb.toString();
	}

	/**
	 * Generate INSERT statements for all rows of the given table.
	 *
	 * @param con
	 *            open connection.
	 * @param table
	 *            table node (VIEW is not supported).
	 * @param monitor
	 *            optional progress monitor for feedback, may be null.
	 * @return SQL text with INSERT statements, or empty string on failure.
	 */
	public static String buildInsertStatements(Connection con, ITable table, IProgressMonitor monitor) throws Exception {
		StringBuffer sb = new StringBuffer();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM " + table.getSqlTableName()); //$NON-NLS-1$
			ResultSetMetaData meta = rs.getMetaData();
			int colCount = meta.getColumnCount();
			String[] cols = new String[colCount];
			int[] types = new int[colCount];
			for (int i = 1; i <= colCount; i++) {
				cols[i - 1] = meta.getColumnName(i);
				types[i - 1] = meta.getColumnType(i);
			}
			String colList = join(cols, ", "); //$NON-NLS-1$

			int rowCount = 0;
			StringBuffer values = new StringBuffer();
			while (rs.next()) {
				values.setLength(0);
				for (int i = 1; i <= colCount; i++) {
					if (i > 1) {
						values.append(", "); //$NON-NLS-1$
					}
					values.append(toSqlLiteral(rs, i, types[i - 1]));
				}
				sb.append("INSERT INTO ").append(table.getSqlTableName()).append(" (").append(colList).append(") VALUES (") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						.append(values).append(");").append(DbPluginConstant.LINE_SEP); //$NON-NLS-1$
				rowCount++;
				if (monitor != null && rowCount % 500 == 0) {
					monitor.subTask(table.getName() + ": " + rowCount + " rows..."); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			sb.append("-- ").append(table.getName()).append(": ").append(rowCount).append(" rows").append(DbPluginConstant.LINE_SEP); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} catch (Exception e) {
			sb.append("-- [ERROR] failed to export data of ").append(table.getName()).append(": ").append(e.getMessage()) //$NON-NLS-1$ //$NON-NLS-2$
					.append(DbPluginConstant.LINE_SEP);
			DbPlugin.log(e);
		} finally {
			try {
				if (rs != null) rs.close();
			} catch (Exception e) {
			}
			try {
				if (st != null) st.close();
			} catch (Exception e) {
			}
		}
		return sb.toString();
	}

	private static String join(String[] array, String sep) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				sb.append(sep);
			}
			sb.append(array[i]);
		}
		return sb.toString();
	}

	/**
	 * Convert the current column value of the ResultSet into a SQL literal.
	 */
	private static String toSqlLiteral(ResultSet rs, int index, int type) throws Exception {
		Object value = rs.getObject(index);
		if (value == null) {
			return "NULL"; //$NON-NLS-1$
		}
		switch (type) {
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
		case Types.NCHAR:
		case Types.NVARCHAR:
		case Types.LONGNVARCHAR:
		case Types.CLOB:
			return quoteString(value.toString());
		case Types.DATE:
			return quoteString(new SimpleDateFormat("yyyy-MM-dd").format(rs.getDate(index))); //$NON-NLS-1$
		case Types.TIME:
			return quoteString(new SimpleDateFormat("HH:mm:ss").format(rs.getTime(index))); //$NON-NLS-1$
		case Types.TIMESTAMP:
			Object ts = rs.getTimestamp(index);
			return quoteString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(ts)); //$NON-NLS-1$
		case Types.BINARY:
		case Types.VARBINARY:
		case Types.LONGVARBINARY:
		case Types.BLOB:
			if (value instanceof byte[]) {
				byte[] bytes = (byte[]) value;
				if (bytes.length == 0) {
					return "NULL"; //$NON-NLS-1$
				}
				StringBuffer hex = new StringBuffer(bytes.length * 2 + 2);
				hex.append("0x"); //$NON-NLS-1$
				for (int i = 0; i < bytes.length; i++) {
					hex.append(Character.forDigit((bytes[i] >> 4) & 0xF, 16));
					hex.append(Character.forDigit(bytes[i] & 0xF, 16));
				}
				return hex.toString();
			}
			return "NULL"; //$NON-NLS-1$
		default:
			// numeric types (INTEGER/BIGINT/NUMERIC/DECIMAL/FLOAT/DOUBLE/BIT...)
			if (value instanceof BigDecimal || value instanceof Number || value instanceof Boolean) {
				return value.toString();
			}
			// unknown complex types are exported as string if possible
			return quoteString(value.toString());
		}
	}

	private static String quoteString(String s) {
		StringBuffer sb = new StringBuffer(s.length() + 2);
		sb.append('\'');
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\'') {
				sb.append("''"); //$NON-NLS-1$
			} else {
				sb.append(c);
			}
		}
		sb.append('\'');
		return sb.toString();
	}

	/**
	 * Read a text file as string (used to show a preview of small scripts).
	 */
	public static String readFileAsString(String path, String encoding) throws Exception {
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new java.io.FileInputStream(path), encoding));
			char[] buf = new char[4096];
			int len = 0;
			while ((len = reader.read(buf)) > 0) {
				sb.append(buf, 0, len);
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return sb.toString();
	}

	/**
	 * Load columns, PK/FK/constraints/indexes of the table node from JDBC
	 * metadata. (Based on DDLDiffForSchemaJob#loadColumn)
	 */
	private static void loadColumn(Connection con, ITable table) throws Exception {
		TablePKColumn[] pks = null;
		TableFKColumn[] fks = null;
		TableConstraintColumn[] cons = null;
		TableIDXColumn[] uidxs = null;
		TableIDXColumn[] nonuidxs = null;

		IDBConfig config = table.getDbConfig();
		String schemaName = table.getSchemaName();
		String tableName = table.getName();

		switch (DBType.getType(con.getMetaData())) {
		case DBType.DB_TYPE_ORACLE:
			if (table.getName().toUpperCase().indexOf("BIN$") >= 0) { //$NON-NLS-1$
				return;
			}
			if (table instanceof Synonym) {
				Synonym synonym = (Synonym) table;
				schemaName = synonym.getTable_owner();
				tableName = synonym.getTable_name();
			} else if (table instanceof Bookmark) {
				Bookmark bm = (Bookmark) table;
				if (bm.isSynonym()) {
					SynonymInfo info = OracleSynonymInfoSearcher.execute(con, bm.getSchemaName(), bm.getName());
					schemaName = info.getTable_owner();
					tableName = info.getTable_name();
				}
			}
			break;
		}

		IColumnSearcherFactory columnFactory = DefaultColumnSearcherFactory.getFactory(con.getMetaData(), config.isConvertUnicode());
		TableColumn[] columns = columnFactory.execute(con, schemaName, tableName);

		IConstraintSearcherFactory constraintSearcherFactory = DefaultConstraintSearcherFactory.getFactory(config);
		pks = constraintSearcherFactory.getPKColumns(con, schemaName, tableName);
		fks = constraintSearcherFactory.getFKColumns(con, schemaName, tableName);

		if (!table.getFolderName().equals("VIEW")) { //$NON-NLS-1$
			switch (DBType.getType(con.getMetaData())) {
			case DBType.DB_TYPE_ORACLE:
				cons = constraintSearcherFactory.getConstraintColumns(con, schemaName, tableName);
				uidxs = OracleIndexSearcher.getIDXColumns(con, schemaName, tableName, true);
				nonuidxs = OracleIndexSearcher.getIDXColumns(con, schemaName, tableName, false);
				break;
			default:
				uidxs = constraintSearcherFactory.getUniqueIDXColumns(con, schemaName, tableName, true);
				nonuidxs = constraintSearcherFactory.getUniqueIDXColumns(con, schemaName, tableName, false);
				break;
			}
		}

		table.setTablePKColumns(pks);
		table.setTableFKColumns(fks);
		table.setTableConstraintColumns(cons);
		table.setTableUIDXColumns(uidxs);
		table.setTableNonUIDXColumns(nonuidxs);

		for (int i = 0; i < columns.length; i++) {
			TableColumn w_column = columns[i];
			TablePKColumn w_pk = getPKColumn(pks, w_column);
			TableFKColumn[] w_fks = getFKColumns(fks, w_column);
			addChild(con, table, w_column, w_pk, w_fks);
		}
	}

	private static void addChild(Connection con, ITable table, TableColumn w_column, TablePKColumn w_pk, TableFKColumn[] w_fks) throws Exception {
		switch (DBType.getType(con.getMetaData())) {
		case DBType.DB_TYPE_ORACLE:
			table.addChild(new OracleColumn(w_column, w_pk, w_fks));
			break;
		default:
			table.addChild(new Column(w_column, w_pk, w_fks));
			break;
		}
	}

	private static TablePKColumn getPKColumn(TablePKColumn[] pks, TableColumn column) throws Exception {
		TablePKColumn pk = null;
		for (int i = 0; i < pks.length; i++) {
			if (pks[i].getColumnName().equals(column.getColumnName())) {
				pk = pks[i];
				break;
			}
		}
		return pk;
	}

	private static TableFKColumn[] getFKColumns(TableFKColumn[] fks, TableColumn column) throws Exception {
		List list = new ArrayList();
		for (int i = 0; i < fks.length; i++) {
			if (fks[i].getColumnName().equals(column.getColumnName())) {
				list.add(fks[i]);
			}
		}
		return (TableFKColumn[]) list.toArray(new TableFKColumn[0]);
	}

}
