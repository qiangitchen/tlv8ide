/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableConstraintColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TableIDXColumn;
import zigen.plugin.db.core.TablePKColumn;
import zigen.plugin.db.preference.SQLFormatPreferencePage;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public class DefaultSQLCreatorFactory extends AbstractSQLCreatorFactory implements ISQLCreatorFactory {

	protected Column[] cols;

	protected TablePKColumn[] pks;

	protected String primaryKeyName = null;

	protected List fks;

	protected List cons;

	protected List uidxs;

	protected List nuidxs;

	protected boolean isVisibleSchemaName = true;

	protected char encloseChar;

	public void setVisibleSchemaName(boolean b) {
		isVisibleSchemaName = b;
	}

	protected DefaultSQLCreatorFactory(ITable table) {
		setTable(table);
	}

	protected void setTable(ITable table) {
		if (table != null) {
			this.table = table;
			this.cols = table.getColumns();
			this.pks = table.getTablePKColumns();

			this.cons = convertTableConstraintColumn(table.getTableConstraintColumns());
			this.fks = convertTableFKColumn(table.getTableFKColumns());
			this.uidxs = convertTableIDXColumn(table.getTableUIDXColumns());

			if(table.getDbConfig() != null){
				this.encloseChar = AbstractStatementFactory.getFactory(table.getDbConfig()).getEncloseChar();
			}
		}
	}

	protected String getTableNameWithSchemaForSQL(ITable table, boolean isVisible){
		if (isVisible) {
			return SQLUtil.encodeQuotation(table.getSqlTableName());
		} else {
			return SQLUtil.enclose(SQLUtil.encodeQuotation(table.getName()), encloseChar);
		}
	}

	public List convertTableIDXColumn(TableIDXColumn[] idxs) {

		if (idxs == null)
			return null;
		List result = new ArrayList();

		String temp = "";
		for (int i = 0; i < idxs.length; i++) {

			TableIDXColumn idx = idxs[i];
			List list = new ArrayList();
			list.add(idx);
			temp = idx.getName();

			for (int k = i + 1; k < idxs.length; k++) {
				TableIDXColumn _idx = idxs[k];
				if (!temp.equals(_idx.getName())) {

					temp = _idx.getName();
					result.add((TableIDXColumn[]) list.toArray(new TableIDXColumn[0]));

					break;
				} else {
					list.add(_idx);
					i++;
				}
			}

			if (i >= idxs.length - 1) {
				result.add((TableIDXColumn[]) list.toArray(new TableIDXColumn[0]));
			}
		}

		return result;
	}

	public List convertTableConstraintColumn(TableConstraintColumn[] cons) {

		if (cons == null)
			return null;
		List result = new ArrayList();

		String temp = "";
		for (int i = 0; i < cons.length; i++) {

			TableConstraintColumn con = cons[i];
			List list = new ArrayList();
			list.add(con);
			temp = con.getName();

			for (int k = i + 1; k < cons.length; k++) {
				TableConstraintColumn _wk = cons[k];
				if (!temp.equals(_wk.getName())) {

					temp = _wk.getName();
					result.add((TableConstraintColumn[]) list.toArray(new TableConstraintColumn[0]));

					break;
				} else {
					list.add(_wk);
					i++;
				}
			}

			if (i >= cons.length - 1) {
				result.add((TableConstraintColumn[]) list.toArray(new TableConstraintColumn[0]));
			}
		}

		return result;
	}

	public List convertTableFKColumn(TableFKColumn[] fks) {

		if (fks == null)
			return null;
		List result = new ArrayList();

		String temp = "";
		for (int i = 0; i < fks.length; i++) {
			TableFKColumn fk = fks[i];
			List list = new ArrayList();
			list.add(fk);
			temp = fk.getName();

			for (int k = i + 1; k < fks.length; k++) {
				TableFKColumn _fkc = fks[k];
				if (!temp.equals(_fkc.getName())) {
					temp = _fkc.getName();
					result.add((TableFKColumn[]) list.toArray(new TableFKColumn[0]));
					break;
				} else {
					list.add(_fkc);
					i++;;
				}
			}

			if (i >= fks.length - 1) {
				result.add((TableFKColumn[]) list.toArray(new TableFKColumn[0]));
			}
		}

		return result;
	}


	public String createDDL() {
		StringBuffer sb = new StringBuffer();

		String folderName = table.getFolderName();

		if ("VIEW".equals(folderName)) { //$NON-NLS-1$
			sb.append(getCreateView());
		} else {
			sb.append(getCreateTableStr());
		}

		sb.append(getTableComment());

		sb.append(getColumnComment());
		return sb.toString();
	}

	protected String getCreateTableStr() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE ");
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));

		sb.append(DbPluginConstant.LINE_SEP);
		sb.append("(");
		sb.append(DbPluginConstant.LINE_SEP);
		sb.append(getColumnDefine());
		sb.append(getConstraints());
		sb.append(")");
		setDemiliter(sb);
		return sb.toString();
	}

	protected boolean hasPrimaryKey() {
		return pks != null && pks.length > 0;
	}

	protected boolean hasForeginKey() {
		return fks != null && fks.size() > 0;
	}

	protected boolean hasUniqueIndexKey() {
		return uidxs != null && uidxs.size() > 0;
	}

	protected boolean hasConstraintOther() {
		return cons != null && cons.size() > 0;
	}

	protected String getColumnDefine() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cols.length; i++) {
			Column col = cols[i];
			TableColumn column = col.getColumn();

			if (i == cols.length - 1) {
				sb.append(getColumnLabel(column));

				if (column.isNotNull()) {
					sb.append(" NOT NULL");
				}

				// if (hasPrimaryKey() || hasForeginKey() || hasUniqueIndexKey()) {
				if (hasPrimaryKey() || hasForeginKey() || hasUniqueIndexKey() || hasConstraintOther()) {
					sb.append(",");
				}

				sb.append(DbPluginConstant.LINE_SEP);

			} else {
				sb.append(getColumnLabel(column));
				if (column.isNotNull()) {
					sb.append(" NOT NULL");
				}
				sb.append(",");
				sb.append(DbPluginConstant.LINE_SEP);
			}
		}
		return sb.toString();
	}

	protected String getColumnLabel(TableColumn column) {

		StringBuffer sb = new StringBuffer();
		sb.append("    ");
		sb.append(StringUtil.padding(column.getColumnName(), 28));

		String typeName = column.getTypeName().toUpperCase();

		sb.append(typeName);

		if (isVisibleColumnSize(typeName)) {
			if (column.getDecimalDigits() == 0) {
				sb.append("(" + column.getColumnSize() + ")");
			} else {
				sb.append("(" + column.getColumnSize() + "," + column.getDecimalDigits() + ")");
			}
		}

		return sb.toString();
	}

	protected String getConstraintPKStr() {
		StringBuffer sb = new StringBuffer();
		if (pks == null || pks.length == 0)
			return null;

		int i = 0;
		for (i = 0; i < pks.length; i++) {
			TablePKColumn pkc = pks[i];
			if (i == 0) {
				primaryKeyName = pkc.getName();
				sb.append("CONSTRAINT ");
				sb.append(pkc.getName());
				sb.append(" PRIMARY KEY ");
				sb.append("(");
				sb.append(pkc.getColumnName());
			} else {
				sb.append(", " + pkc.getColumnName());
			}

		}
		sb.append(")");
		return sb.toString();
	}

	protected String[] getConstraintFKStr() {

		if (fks == null)
			return null;

		List result = new ArrayList();
		for (Iterator iter = fks.iterator(); iter.hasNext();) {
			TableFKColumn[] _fks = (TableFKColumn[]) iter.next();

			StringBuffer sb = new StringBuffer();
			StringBuffer sb2 = new StringBuffer();
			boolean cascade = false;
			for (int i = 0; i < _fks.length; i++) {
				TableFKColumn column = _fks[i];
				cascade = column.isCasucade();

				if (i == 0) {
					sb.append("CONSTRAINT ");
					sb.append(column.getName());
					sb.append(" FOREIGN KEY ");
					sb.append("(");
					sb.append(column.getColumnName());

					// Reference
					sb2.append(" REFERENCES ");

					if (isVisibleSchemaName && column.getPkSchema() != null) {
						sb2.append(column.getPkSchema());
						sb2.append(".");
					} else {

						if (table.getSchemaName() != null && !table.getSchema().getName().equalsIgnoreCase(column.getPkColumnName())) {
							sb2.append(column.getPkSchema());
							sb2.append(".");

						}


					}
					sb2.append(column.getPkTableName());
					sb2.append(" ");
					sb2.append("(");
					sb2.append(column.getPkColumnName());

				} else {

					sb.append(", " + column.getColumnName());
					sb2.append(", " + column.getColumnName());
				}

			}
			sb.append(")");
			sb2.append(")");
			if (cascade) {
				sb2.append(" ON DELETE CASCADE");
			}
			result.add(sb.toString() + sb2.toString());
		}

		return (String[]) result.toArray(new String[0]);

	}

	protected String[] getConstraintOtherStr() {

		if (cons == null)
			return null;

		List result = new ArrayList();

		String name = null;
		String type = null;
		String paramater = null;
		for (Iterator iter = cons.iterator(); iter.hasNext();) {
			TableConstraintColumn[] _cons = (TableConstraintColumn[]) iter.next();

			StringBuffer sb = new StringBuffer();
			int i = 0;
			for (i = 0; i < _cons.length; i++) {
				TableConstraintColumn column = _cons[i];

				if (i == 0) {

					if (column.getColumnName() == null && !"".equals(column.getSearch_condition())) {
						name = column.getName();
						type = "CHECK";

						if (column.getSearch_condition() != null) {
							paramater = "(" + column.getSearch_condition().trim();

						}

						sb.append("CONSTRAINT ");
						sb.append(column.getName());
						sb.append(" ");
						sb.append(type);
						sb.append(paramater);

						break;

					} else {
						name = column.getName();
						if (column.isNonUnique()) {
							type = "NONUNIQUE";
						} else {
							type = "UNIQUE";
						}

						sb.append("CONSTRAINT ");
						sb.append(column.getName());
						sb.append(" ");
						sb.append(type);
						sb.append(" (");
						sb.append(column.getColumnName());
					}

				} else {
					sb.append(", " + column.getColumnName());
				}
			}
			sb.append(")");

			result.add(sb.toString());
		}

		return (String[]) result.toArray(new String[0]);

	}

	protected String[] getConstraintIDXStr() {

		if (uidxs == null)
			return null;

		List result = new ArrayList();
		for (Iterator iter = uidxs.iterator(); iter.hasNext();) {
			TableIDXColumn[] _idxs = (TableIDXColumn[]) iter.next();

			StringBuffer sb = new StringBuffer();
			// StringBuffer sb2 = new StringBuffer();
			// boolean cascade = false;
			for (int i = 0; i < _idxs.length; i++) {
				TableIDXColumn column = _idxs[i];

				if (column.getName().equals(primaryKeyName))
					break;

				if (i == 0) {
					sb.append("CONSTRAINT ");
					sb.append(column.getName());
					sb.append(" UNIQUE ");
					sb.append("(");
					sb.append(column.getColumnName());
				} else {
					sb.append(", " + column.getColumnName());
				}

				if (i == _idxs.length - 1) {
					sb.append(")");
				}

			}
			if (sb.length() != 0) {
				result.add(sb.toString());
			}

		}

		return (String[]) result.toArray(new String[0]);

	}

	protected String getConstraints() {
		StringBuffer sb = new StringBuffer();

		String pks = getConstraintPKStr();
		String[] fks = getConstraintFKStr();
		String[] cons = getConstraintOtherStr();
		String[] idxs = getConstraintIDXStr();

		boolean hasPk = !(pks == null || pks.length() == 0);
		boolean hasFk = !(fks == null || fks.length == 0);
		boolean hasCs = !(cons == null || cons.length == 0);
		boolean hasIx = !(idxs == null || idxs.length == 0);

		if (pks != null) {
			sb.append("    " + pks);

			if (hasFk || hasCs || hasIx) {
				sb.append(",");

			}
			sb.append(DbPluginConstant.LINE_SEP);
		} else {

		}

		if (fks != null) {
			for (int i = 0; i < fks.length; i++) {
				if (i == fks.length - 1) {
					sb.append("    " + fks[i]);
					if (hasCs || hasIx) {
						sb.append(",");
					}
				} else {
					sb.append("    " + fks[i] + ",");
				}
				sb.append(DbPluginConstant.LINE_SEP);

			}
		}

		if (cons != null) {
			for (int i = 0; i < cons.length; i++) {
				if (i == cons.length - 1) {
					sb.append("    " + cons[i]);
					if (hasIx) {
						sb.append(",");
					}
				} else {
					sb.append("    " + cons[i] + ",");
				}
				sb.append(DbPluginConstant.LINE_SEP);

			}
		}

		if (idxs != null) {
			for (int i = 0; i < idxs.length; i++) {
				if (i == idxs.length - 1) {
					sb.append("    " + idxs[i]);
				} else {
					sb.append("    " + idxs[i] + ",");
				}
				sb.append(DbPluginConstant.LINE_SEP);
			}
		}

		return sb.toString();
	}

	public String createSelect(String condition, int limit) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM ");
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));

		if (condition != null && !"".equals(condition.trim())) {
			sb.append(" WHERE " + condition);
		}
		return sb.toString();
	}

	public boolean isSupportPager() {
		return false;
	}

	public String createSelectForPager(String _condition, int offset, int limit) {
		throw new UnsupportedOperationException("createCommentOnColumnDDL Method is not supported.");
	}

	public String createCountAll(String condition) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT COUNT(*) FROM ");
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));

		if (condition != null && !"".equals(condition.trim())) {
			sb.append(" WHERE " + condition);
		}
		return sb.toString();
	}

	public String createCountForQuery(String query) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT COUNT(*) FROM ( "); //$NON-NLS-1$
		sb.append(DbPluginConstant.LINE_SEP);
		sb.append(query);
		sb.append(DbPluginConstant.LINE_SEP);
		sb.append(" ) TBL");
		return sb.toString();
	}

	public String VisibleColumnSizePattern() {
		return ".*CHAR|^VARCHAR.*|^NUMBER|^DECIMAL|.*INT.*|^FLOAT|^DOUBLE|^REAL|^TIMESTAMP|^TIME|.*VARYING";
	}

	public boolean isVisibleColumnSize(String typeName) {
		return typeName.toUpperCase().matches(VisibleColumnSizePattern());
	}

	public String[] getSupportColumnType() {
		return new String[] {"INT", "INTEGER", "DOUBLE", "FLOAT", "VARCHAR", "CHAR", "DECIMAL", "NUMERIC", "BOOLEAN", "BIT", "TINYINT", "SMALLINT", "BIGINT", "REAL", "BINATY",
				"VARBINATY", "LONGBINARY", "DATE", "TIME", "TIMESTAMP", "DATETIME", "OTHER", "OBJECT"};
	}

	public String getTableComment() {
		StringBuffer sb = new StringBuffer();

		if (table.getRemarks() != null && !"".equals(table.getRemarks())) { //$NON-NLS-1$
			sb.append("COMMENT ON TABLE "); //$NON-NLS-1$
			sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));

			sb.append(" IS "); //$NON-NLS-1$
			sb.append("'"); //$NON-NLS-1$
			sb.append(table.getRemarks());
			sb.append("'"); //$NON-NLS-1$

			setDemiliter(sb);
		}

		return sb.toString();
	}

	public String getColumnComment() {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < cols.length; i++) {
			Column col = cols[i];
			TableColumn tCol = col.getColumn();

			if (tCol.getRemarks() != null && !"".equals(tCol.getRemarks())) { //$NON-NLS-1$
				sb.append("COMMENT ON COLUMN "); //$NON-NLS-1$
				sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
				sb.append("."); //$NON-NLS-1$
				sb.append(tCol.getColumnName());
				sb.append(" IS "); //$NON-NLS-1$
				sb.append("'"); //$NON-NLS-1$
				sb.append(tCol.getRemarks());
				sb.append("'"); //$NON-NLS-1$

				setDemiliter(sb);
			}
		}

		return sb.toString();
	}

	protected String getCreateView() {
		StringBuffer wk = new StringBuffer();
		try {
			boolean onPatch = DbPlugin.getDefault().getPreferenceStore().getBoolean(SQLFormatPreferencePage.P_FORMAT_PATCH);
			int type = DbPlugin.getDefault().getPreferenceStore().getInt(SQLFormatPreferencePage.P_USE_FORMATTER_TYPE);

			wk.append("CREATE OR REPLACE VIEW "); //$NON-NLS-1$
			wk.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));

			wk.append(DbPluginConstant.LINE_SEP);

			wk.append("(");
			for (int i = 0; i < cols.length; i++) {
				Column col = cols[i];
				if (i > 0) {
					wk.append(",");
				}
				wk.append(col.getName());
				wk.append(DbPluginConstant.LINE_SEP);
			}

			wk.append(")");
			wk.append(DbPluginConstant.LINE_SEP);
			wk.append("AS"); //$NON-NLS-1$
			wk.append(DbPluginConstant.LINE_SEP);

			wk.append(getViewDDL(table.getDbConfig(), table.getSchemaName(), table.getName()));

			StringBuffer sb = new StringBuffer();
			sb.append(SQLFormatter.format(wk.toString(), type, onPatch));
			setDemiliter(sb);

			return sb.toString();
		} catch (Exception e) {
			DbPlugin.log(e);
		}
		return null;

	}

	public boolean supportsRemarks() {
		return false;
	}

	public boolean supportsModifyColumnType() {
		return false;
	}

	public boolean supportsModifyColumnSize(String columnType) {
		return false;
	}

	public boolean supportsDropColumnCascadeConstraints() {
		return false;
	}

	public boolean supportsRollbackDDL() {
		return false;
	}

	public String createCreateIndexDDL(String indexName, Column[] columns, int indexType) {
		throw new UnsupportedOperationException("createCreateIndexDDL Method is not supported.");
	}

	public String createDropIndexDDL(String indexName) {
		throw new UnsupportedOperationException("createDropIndexDDL Method is not supported.");
	}

	public String createCreateConstraintCheckDDL(String constraintName, String check) {
		throw new UnsupportedOperationException("createCreateConstraintCheckDDL Method is not supported.");
	}

	public String createCreateConstraintFKDDL(String constraintName, Column[] columns, ITable refTable, Column[] refColumns, boolean onDeleteCascade) {
		throw new UnsupportedOperationException("createCreateConstraintFKDDL Method is not supported.");
	}

	public String createCreateConstraintPKDDL(String constraintName, Column[] columns) {
		throw new UnsupportedOperationException("createCreateConstraintPKDDL Method is not supported.");
	}

	public String createCreateConstraintUKDDL(String constraintName, Column[] columns) {
		throw new UnsupportedOperationException("createCreateConstraintUKDDL Method is not supported.");
	}

	public String createDropConstraintDDL(String constraintName, String type) {
		throw new UnsupportedOperationException("createDropConstraintDDL Method is not supported.");
	}

	protected String getViewDDL_SQL(String dbName, String owner, String view) {
		throw new UnsupportedOperationException("getViewDDL_SQL Method is not supported.");
	}

	public String createCommentOnTableDDL(String commnets) {
		throw new UnsupportedOperationException("createCommentOnTableDDL Method is not supported.");
	}

	public String createCommentOnColumnDDL(Column column) {
		throw new UnsupportedOperationException("createCommentOnColumnDDL Method is not supported.");

	}

	public String createRenameTableDDL(String newTableName) {
		throw new UnsupportedOperationException("createRenameTableDDL Method is not supported.");

	}

	public String createRenameColumnDDL(Column from, Column to) {
		throw new UnsupportedOperationException("createRenameColumnDDL Method is not supported.");
	}

	public String[] createAddColumnDDL(Column column) {
		throw new UnsupportedOperationException("createAddColumnDDL Method is not supported.");
	}

	public String[] createModifyColumnDDL(Column from, Column to) {
		throw new UnsupportedOperationException("createModifyColumnDDL Method is not supported.");
	}

	public String[] createDropColumnDDL(Column column, boolean cascadeConstraints) {
		throw new UnsupportedOperationException("createDropColumnDDL Method is not supported.");
	}
}
