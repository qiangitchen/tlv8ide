/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.rule.AbstractMappingFactory;
import zigen.plugin.db.core.rule.DefaultConstraintSearcherFactory;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.core.rule.IConstraintSearcherFactory;
import zigen.plugin.db.core.rule.IMappingFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.actions.MaxRecordException;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public class TableManager {

	public static TableElement[] invoke(IDBConfig config, ITable table) throws Exception, MaxRecordException {
		int limit = DbPlugin.getDefault().getPreferenceStore().getInt(PreferencePage.P_MAX_VIEW_RECORD);
		return invoke(config, table, null, 0, limit);
	}

	public static TableElement[] invoke(IDBConfig config, ITable table, String condition) throws Exception, MaxRecordException {
		int limit = DbPlugin.getDefault().getPreferenceStore().getInt(PreferencePage.P_MAX_VIEW_RECORD);
		return invoke(config, table, condition, 0, limit);
	}

	public static TableElement[] invoke(IDBConfig config, ITable table, String condition, int offset, int limit) throws Exception, MaxRecordException {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return invoke(con, table, condition, offset, limit);
		} catch (Exception e) {
			throw e;
		}
	}

	public static TableElement[] invoke(Connection con, ITable table) throws Exception, MaxRecordException {
		return invoke(con, table, null);
	}

	public static TableElement[] invoke(Connection con, ITable table, String condition) throws Exception, MaxRecordException {
		int limit = DbPlugin.getDefault().getPreferenceStore().getInt(PreferencePage.P_MAX_VIEW_RECORD);
		return invoke(con, table, condition, 0, limit);
	}

	public static TableElement[] invoke(Connection con, ITable table, String condition, int offset, int limit) throws Exception, MaxRecordException {
		ResultSet rs = null;
		Statement stmt = null;
		TablePKColumn[] pks = null;
		TableFKColumn[] fks = null;
		TableIDXColumn[] uidxs = null;
		try {
			pks = table.getTablePKColumns();
			if (pks == null) {
				// pks = ConstraintSearcher.getPKColumns(con, table.getSchemaName(), table.getName());
				IConstraintSearcherFactory factory = DefaultConstraintSearcherFactory.getFactory(table.getDbConfig());
				pks = factory.getPKColumns(con, table.getSchemaName(), table.getName());
				if (pks == null) {
					uidxs = ConstraintUtil.getFirstUniqueIndex(table.getTableUIDXColumns());
				}
			}
			fks = table.getTableFKColumns();
			stmt = con.createStatement();

			IDBConfig config = table.getDbConfig();
			ISQLCreatorFactory factory = DefaultSQLCreatorFactory.getFactory(config, table);
			String sql = getSQL(factory, condition, offset, limit);
			rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			List list = new ArrayList();
			TableColumn[] columns = getTableColumns(meta, table);
			TableElement header;
			if (pks != null && pks.length > 0) {
				header = createHeaderElement(rs, table, columns, pks, fks);
			} else {
				header = createHeaderElement(rs, table, columns, uidxs, fks);
			}
			if (pks == null || pks.length == 0) {
				header.setCanModify(checkModify(table.getDbConfig(), columns));
			} else {
				header.setCanModify(true);
			}
			list.add(header);

			int recordNo = 0;
			int addCount = 0;
			while (rs.next()) {

				recordNo++;

				TableElement elements;

				if (offset > 0 && factory.isSupportPager()) {
					int no = recordNo + offset - 1;
					if (pks != null && pks.length > 0) {
						elements = createElement(rs, table, columns, pks, no);
					} else {
						elements = createElement(rs, table, columns, uidxs, no);
					}
					list.add(elements);
				} else {
					/*
					if (limit > 0 && recordNo > limit) {
						String msg = Messages.getString("TableManager.0"); //$NON-NLS-1$
						throw new MaxRecordException(msg, (TableElement[]) list.toArray(new TableElement[0]));
					}
					int no = recordNo;
					if (pks != null && pks.length > 0) {
						elements = createElement(rs, table, columns, pks, no);
					} else {
						elements = createElement(rs, table, columns, uidxs, no);
					}
					*/
					
					if (recordNo >= offset && addCount < limit) {
						int no = recordNo;
						if (pks != null && pks.length > 0) {
							elements = createElement(rs, table, columns, pks, no);
						} else {
							elements = createElement(rs, table, columns, uidxs, no);
						}
						list.add(elements);
						addCount++;
					}
					
				}

				//list.add(elements);
			}

			return (TableElement[]) list.toArray(new TableElement[0]);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(stmt);
		}

	}
	
	
	public static TableElement[] invokeForPager(IDBConfig config, ITable table) throws Exception, MaxRecordException {
		int limit = DbPlugin.getDefault().getPreferenceStore().getInt(PreferencePage.P_MAX_VIEW_RECORD);
		return invokeForPager(config, table, null, 0, limit);
	}

	public static TableElement[] invokeForPager(IDBConfig config, ITable table, String condition) throws Exception, MaxRecordException {
		int limit = DbPlugin.getDefault().getPreferenceStore().getInt(PreferencePage.P_MAX_VIEW_RECORD);
		return invokeForPager(config, table, condition, 0, limit);
	}

	public static TableElement[] invokeForPager(IDBConfig config, ITable table, String condition, int offset, int limit) throws Exception, MaxRecordException {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return invokeForPager(con, table, condition, offset, limit);
		} catch (Exception e) {
			throw e;
		}
	}

	public static TableElement[] invokeForPager(Connection con, ITable table) throws Exception, MaxRecordException {
		return invokeForPager(con, table, null);
	}

	public static TableElement[] invokeForPager(Connection con, ITable table, String condition) throws Exception, MaxRecordException {
		int limit = DbPlugin.getDefault().getPreferenceStore().getInt(PreferencePage.P_MAX_VIEW_RECORD);
		return invoke(con, table, condition, 0, limit);
	}
	
	public static TableElement[] invokeForPager(Connection con, ITable table, String condition, int offset, int limit) throws Exception, MaxRecordException {
		ResultSet rs = null;
		Statement stmt = null;
		TablePKColumn[] pks = null;
		TableFKColumn[] fks = null;
		TableIDXColumn[] uidxs = null;
		try {
			pks = table.getTablePKColumns();
			if (pks == null) {
				// pks = ConstraintSearcher.getPKColumns(con, table.getSchemaName(), table.getName());
				IConstraintSearcherFactory factory = DefaultConstraintSearcherFactory.getFactory(table.getDbConfig());
				pks = factory.getPKColumns(con, table.getSchemaName(), table.getName());
				if (pks == null) {
					uidxs = ConstraintUtil.getFirstUniqueIndex(table.getTableUIDXColumns());
				}
			}
			fks = table.getTableFKColumns();
			stmt = con.createStatement();

			IDBConfig config = table.getDbConfig();
			ISQLCreatorFactory factory = DefaultSQLCreatorFactory.getFactory(config, table);
			String sql = getSQL(factory, condition, offset, limit);
			rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			List list = new ArrayList();
			TableColumn[] columns = getTableColumns(meta, table);
			TableElement header;
			if (pks != null && pks.length > 0) {
				header = createHeaderElement(rs, table, columns, pks, fks);
			} else {
				header = createHeaderElement(rs, table, columns, uidxs, fks);
			}
			if (pks == null || pks.length == 0) {
				header.setCanModify(checkModify(table.getDbConfig(), columns));
			} else {
				header.setCanModify(true);
			}
			list.add(header);

			int recordNo = 0;
			int addCount = 0;
			int size = meta.getColumnCount();
			while (rs.next()) {

				recordNo++;

				TableElement elements = null;

				if (offset > 0 && factory.isSupportPager()) {
					int no = recordNo + offset - 1;
					if (pks != null && pks.length > 0) {
						elements = createElement(rs, table, columns, pks, no);
					} else {
						elements = createElement(rs, table, columns, uidxs, no);
					}
					list.add(elements);
				} else {
					if (recordNo >= offset && addCount < limit) {
						int no = recordNo;
						if (pks != null && pks.length > 0) {
							elements = createElement(rs, table, columns, pks, no);
						} else {
							elements = createElement(rs, table, columns, uidxs, no);
						}
						list.add(elements);
						addCount++;
					}
				}
			}

			return (TableElement[]) list.toArray(new TableElement[0]);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(stmt);
		}

	}
	private static String getSQL(ISQLCreatorFactory factory, String condition, int offset, int limit) {
		/*
		if (offset == 0 || !factory.isSupportPager()) {
			return factory.createSelect(condition, limit);
		} else {
			return factory.createSelectForPager(condition, offset, limit);
		}
		*/
		if (offset == 0) {
			return factory.createSelect(condition, limit);
		}else if(!factory.isSupportPager()){
			return factory.createSelect(condition, 0);	// noLimit
		} else {
			return factory.createSelectForPager(condition, offset, limit);
		}
	}

	public static String getSQLForCSV(ITable table) {
		IDBConfig config = table.getDbConfig();
		ISQLCreatorFactory factory = DefaultSQLCreatorFactory.getFactory(config, table);
		return getSQL(factory, null, 0, 0);
	}

	public static String getSQLForCSV(ITable table, String condition) {
		IDBConfig config = table.getDbConfig();
		ISQLCreatorFactory factory = DefaultSQLCreatorFactory.getFactory(config, table);
		return getSQL(factory, condition, 0, 0);
	}

	static boolean checkModify(IDBConfig config, TableColumn[] columns) {

		IMappingFactory factory = AbstractMappingFactory.getFactory(config);

		for (int i = 0; i < columns.length; i++) {
			TableColumn col = columns[i];
			if (!factory.canModifyDataType(col.getDataType())) {
				// if(!JDBCMapping2.canModifyDataType(col.getDataType())){
				return false;
			}
		}

		return true;
	}

	static TableColumn[] getTableColumns(ResultSetMetaData meta, ITable table) throws SQLException {

		try {

			synchronized (table) {
				if (table.isExpanded()) {

					int count = meta.getColumnCount();
					Column[] cols = table.getColumns();

					TableColumn[] columns = new TableColumn[cols.length];
					for (int i = 0; i < count; i++) {
						// for (int i = 0; i < cols.length; i++) {
						TableColumn column = new TableColumn();
						column.setColumnName(meta.getColumnName(i + 1));
						column.setTypeName(meta.getColumnTypeName(i + 1));
						column.setDataType(meta.getColumnType(i + 1));
						column.setColumnSize(meta.getColumnDisplaySize(i + 1));
						column.setDecimalDigits(meta.getScale(i + 1));
						// column.setNotNull(cols[i].isNotNull());
						if (ResultSetMetaData.columnNoNulls == meta.isNullable(i + 1)) {
							column.setNotNull(true);
						} else {
							column.setNotNull(false);
						}

						if (cols != null && cols.length > 0) {
							column.setDefaultValue(cols[i].getDefaultValue());
						}

						if (columns != null && columns.length > 0) {
							columns[i] = column;
						}
					}

					return columns;

				} else {
					// for test
					int count = meta.getColumnCount();
					TableColumn[] columns = new TableColumn[count];
					for (int i = 0; i < count; i++) {
						TableColumn column = new TableColumn();
						column.setColumnName(meta.getColumnName(i + 1));
						column.setTypeName(meta.getColumnTypeName(i + 1));
						column.setDataType(meta.getColumnType(i + 1));
						column.setColumnSize(meta.getColumnDisplaySize(i + 1));
						column.setDecimalDigits(meta.getScale(i + 1));
						// column.setNotNull(cols[i].isNotNull());
						if (ResultSetMetaData.columnNoNulls == meta.isNullable(i + 1)) {
							column.setNotNull(true);
						} else {
							column.setNotNull(false);
						}
						// column.setDefaultValue(cols[i].getDefaultValue());
						columns[i] = column;
					}

					return columns;

				}
			}

		} catch (SQLException e) {
			DbPlugin.log(e);
			throw e;
		}

	}

	static TableElement createHeaderElement(ResultSet rs, ITable table, TableColumn[] columns, TablePKColumn[] pks, TableFKColumn[] fks) throws Exception {
		TableElement elements = null;
		int size = rs.getMetaData().getColumnCount();
		List pkColumnList = new ArrayList();

		for (int i = 0; i < size; i++) {
			if (ConstraintUtil.isPKColumn(pks, columns[i].getColumnName())) {
				pkColumnList.add(columns[i]);
			}
		}
		if (pks.length == 0) {
			TableColumn[] uniqueColumns = new TableColumn[columns.length];
			System.arraycopy(columns, 0, uniqueColumns, 0, columns.length);
			elements = new TableHeaderElement(table, columns, uniqueColumns);
			elements.setCanModify(checkModify(table.getDbConfig(), columns));
		} else {
			elements = new TableHeaderElement(table, columns, (TableColumn[]) pkColumnList.toArray(new TableColumn[0]));
			elements.setCanModify(true);
			elements.setTablePKColumn(pks);
		}

		if (fks != null && fks.length > 0) {
			elements.setTableFKColumn(fks);
		}

		return elements;
	}

	static TableElement createElement(ResultSet rs, ITable table, TableColumn[] columns, TablePKColumn[] pks, int recordNo) throws Exception {
		TableElement elements = null;
		int size = rs.getMetaData().getColumnCount();
		Object[] items = new Object[size];
		List pkColumnList = new ArrayList();
		List pkItemList = new ArrayList();
		IMappingFactory factory = AbstractMappingFactory.getFactory(table.getDbConfig());
		for (int i = 0; i < size; i++) {
			items[i] = factory.getObject(rs, i + 1);
			if (ConstraintUtil.isPKColumn(pks, columns[i].getColumnName())) {
				pkColumnList.add(columns[i]);
				pkItemList.add(items[i]);
			}

		}

		TableColumn[] uniqueColumns;
		Object[] uniqueItems;
		if (pks.length == 0) {
			uniqueColumns = columns;
			uniqueItems = new Object[size];
			System.arraycopy(items, 0, uniqueItems, 0, size);
			elements = new TableElement(table, recordNo, columns, items, uniqueColumns, uniqueItems);
			elements.setCanModify(checkModify(table.getDbConfig(), columns));
		} else {
			uniqueColumns = (TableColumn[]) pkColumnList.toArray(new TableColumn[0]);
			uniqueItems = new Object[pks.length];
			System.arraycopy(pkItemList.toArray(), 0, uniqueItems, 0, pks.length);
			elements = new TableElement(table, recordNo, columns, items, uniqueColumns, uniqueItems);
			elements.setCanModify(true);
		}

		return elements;
	}

	static TableElement createHeaderElement(ResultSet rs, ITable table, TableColumn[] columns, TableIDXColumn[] idxs, TableFKColumn[] fks) throws Exception {
		TableElement elements = null;
		int size = rs.getMetaData().getColumnCount();

		List uniqueColumnList = new ArrayList();
		// for (int i = 0; i < size; i++) {
		for (int i = 0; i < columns.length; i++) {
			if (ConstraintUtil.isUniqueIDXColumn(idxs, columns[i].getColumnName())) {
				uniqueColumnList.add(columns[i]);
			}
		}

		if (idxs == null || idxs.length == 0) {
			TableColumn[] uniqueColumns = new TableColumn[columns.length];
			System.arraycopy(columns, 0, uniqueColumns, 0, columns.length);
			elements = new TableHeaderElement(table, columns, uniqueColumns);
			elements.setCanModify(checkModify(table.getDbConfig(), columns));
		} else {
			elements = new TableHeaderElement(table, columns, (TableColumn[]) uniqueColumnList.toArray(new TableColumn[0]));
			elements.setCanModify(true);
		}

		if (fks != null && fks.length > 0) {
			elements.setTableFKColumn(fks);
		}
		return elements;
	}

	static TableElement createElement(ResultSet rs, ITable table, TableColumn[] columns, TableIDXColumn[] idxs, int recordNo) throws Exception {
		TableElement elements = null;
		int size = rs.getMetaData().getColumnCount();
		Object[] items = new Object[size];
		List uniqueColumnList = new ArrayList();
		List uniqueItemList = new ArrayList();
		IMappingFactory factory = AbstractMappingFactory.getFactory(table.getDbConfig());
		for (int i = 0; i < size; i++) {
			items[i] = factory.getObject(rs, i + 1);

			if (ConstraintUtil.isUniqueIDXColumn(idxs, columns[i].getColumnName())) {
				uniqueColumnList.add(columns[i]);
				uniqueItemList.add(items[i]);
			}
		}
		TableColumn[] uniqueColumns;
		Object[] uniqueItems;
		if (idxs == null || idxs.length == 0) {
			uniqueColumns = columns;
			uniqueItems = new Object[size];
			System.arraycopy(items, 0, uniqueItems, 0, size);
			elements = new TableElement(table, recordNo, columns, items, uniqueColumns, uniqueItems);
			elements.setCanModify(checkModify(table.getDbConfig(), columns));
		} else {
			uniqueColumns = (TableColumn[]) uniqueColumnList.toArray(new TableColumn[0]);
			uniqueItems = new Object[idxs.length];
			System.arraycopy(uniqueItemList.toArray(), 0, uniqueItems, 0, idxs.length);
			elements = new TableElement(table, recordNo, columns, items, uniqueColumns, uniqueItems);
			elements.setCanModify(true);
		}
		return elements;
	}
}
