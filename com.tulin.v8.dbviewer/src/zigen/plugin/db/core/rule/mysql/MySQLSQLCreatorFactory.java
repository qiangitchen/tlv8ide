/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule.mysql;

import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.TablePKColumn;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.Constraint;
import zigen.plugin.db.ui.internal.ITable;

public class MySQLSQLCreatorFactory extends DefaultSQLCreatorFactory {

	public MySQLSQLCreatorFactory(ITable table) {
		super(table);
	}

	public String VisibleColumnSizePattern() {
		return ".*CHAR|^VARCHAR.*|^DECIMAL|^FLOAT|^DOUBLE|^REAL|^TIMESTAMP|^TIME|.*VARYING"; //$NON-NLS-1$
		// return "^VARCHAR";
	}

	public String[] getSupportColumnType() {
		return new String[] {"TINYINT", //$NON-NLS-1$
				"BIT", //$NON-NLS-1$
				"BOOL", //$NON-NLS-1$
				"BOOLEAN", //$NON-NLS-1$
				"SMALLINT", //$NON-NLS-1$
				"MEDIUMINT", //$NON-NLS-1$
				"INT", //$NON-NLS-1$
				"INTEGER", //$NON-NLS-1$
				"BIGINT", //$NON-NLS-1$
				"FLOAT", //$NON-NLS-1$
				"DOUBLE", //$NON-NLS-1$
				"DECIMAL", //$NON-NLS-1$
				"DEC", //$NON-NLS-1$
				"DATE", //$NON-NLS-1$
				"DATETIME", //$NON-NLS-1$
				"TIMESTAMP", //$NON-NLS-1$
				"TIME", //$NON-NLS-1$
				"YEAR", //$NON-NLS-1$
				"CHAR", //$NON-NLS-1$
				"VARCHAR", //$NON-NLS-1$
				"TINYBLOB", //$NON-NLS-1$
				"TINYTEXT", //$NON-NLS-1$
				"BLOB", //$NON-NLS-1$
				"TEXT", //$NON-NLS-1$
				"MEDIUMBLOB", //$NON-NLS-1$
				"MEDIUMTEXT", //$NON-NLS-1$
				"LONGBLOB", //$NON-NLS-1$
				"LONGTEXT", //$NON-NLS-1$
				"ENUM" //$NON-NLS-1$
		};
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

				sb.append("CONSTRAINT "); //$NON-NLS-1$
				// sb.append(pkc.getName());
				// sb.append(" PRIMARY KEY ");

				sb.append("PRIMARY KEY "); //$NON-NLS-1$
				sb.append("("); //$NON-NLS-1$
				sb.append(pkc.getColumnName());
			} else {
				sb.append(", " + pkc.getColumnName()); //$NON-NLS-1$
			}

		}
		sb.append(")"); //$NON-NLS-1$
		return sb.toString();
	}

	public String createSelect(String _condition, int limit) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));

		String[] conditions = SQLFormatter.splitOrderCause(_condition);
		String condition = conditions[0];
		String orderBy = conditions[1];

		if (condition != null && !"".equals(condition.trim())) { //$NON-NLS-1$
			sb.append(" WHERE " + condition); //$NON-NLS-1$
		}

		// ORDER BY
		if (orderBy != null && !"".equals(orderBy)) { //$NON-NLS-1$
			sb.append(" " + orderBy); //$NON-NLS-1$
		}

		if (limit > 0) {
			sb.append(" LIMIT " + (limit + 1));//$NON-NLS-1$
		}

		return sb.toString();
	}

	public boolean isSupportPager() {
		return true;
	}

	public String createSelectForPager(String _condition, int offset, int limit) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));

		String[] conditions = SQLFormatter.splitOrderCause(_condition);
		String condition = conditions[0];
		String orderBy = conditions[1];

		if (condition != null && !"".equals(condition.trim())) { //$NON-NLS-1$
			sb.append(" WHERE "); //$NON-NLS-1$
			sb.append(condition);
		}
		if (limit > 0) {
			sb.append(" LIMIT ");
			sb.append(offset - 1);
			sb.append(", ");
			sb.append(limit);
		}

		// ORDER BY
		if (orderBy != null && !"".equals(orderBy)) { //$NON-NLS-1$
			sb.append(" " + orderBy); //$NON-NLS-1$
		}
		return sb.toString();
	}


	public String createCommentOnTableDDL(String commnets) {
		return null;
	}

	public String createCommentOnColumnDDL(Column column) {
		return null;
	}

	public String createRenameTableDDL(String newTableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append(" RENAME TO "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(newTableName, encloseChar));
		return sb.toString();
	}

	public String createRenameColumnDDL(Column from, Column to) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append(" CHANGE COLUMN "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(from.getName(), encloseChar));
		sb.append(" "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(to.getName(), encloseChar));
		sb.append(" "); //$NON-NLS-1$

		sb.append(to.getTypeName());
		if (isVisibleColumnSize(from.getTypeName())) {
			sb.append("("); //$NON-NLS-1$
			sb.append(from.getSize());
			sb.append(")"); //$NON-NLS-1$
		}

		return sb.toString();

	}

	// for Oracle
	public String[] createAddColumnDDL(Column column) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append(" ADD ("); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(column.getName(), encloseChar));
		sb.append(" "); //$NON-NLS-1$
		sb.append(column.getTypeName());
		if (isVisibleColumnSize(column.getTypeName())) {
			sb.append("("); //$NON-NLS-1$
			sb.append(column.getSize());
			sb.append(")"); //$NON-NLS-1$
		}
		if (column.getDefaultValue() != null && !"".equals(column.getDefaultValue())) {// DEFAULT //$NON-NLS-1$
			// //$NON-NLS-1$
			sb.append(" DEFAULT "); //$NON-NLS-1$
			sb.append(column.getDefaultValue());
		}

		if (column.isNotNull()) { // NOT NULL
			sb.append(" NOT NULL"); //$NON-NLS-1$
		} else {
			sb.append(" NULL"); //$NON-NLS-1$
		}
		sb.append(")"); //$NON-NLS-1$

		return new String[] {sb.toString()};

	}

	public String[] createModifyColumnDDL(Column from, Column to) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append(" CHANGE COLUMN "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(to.getName(), encloseChar));
		sb.append(" "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(to.getName(), encloseChar));
		sb.append(" "); //$NON-NLS-1$
		sb.append(to.getTypeName());
		if (isVisibleColumnSize(to.getTypeName())) {
			sb.append("("); //$NON-NLS-1$
			sb.append(to.getSize());
			sb.append(")"); //$NON-NLS-1$
		}

		/*
		 * if (!from.getDefaultValue().equals(to.getDefaultValue())) {// DEFAULT sb.append(" DEFAULT "); //$NON-NLS-1$ if ("".equals(to.getDefaultValue())) { //$NON-NLS-1$ sb.append("NULL");
		 * //$NON-NLS-1$ } else { sb.append(to.getDefaultValue()); } }
		 */
		sb.append(" DEFAULT "); //$NON-NLS-1$
		if ("".equals(to.getDefaultValue())) { //$NON-NLS-1$
			sb.append("NULL"); //$NON-NLS-1$
		} else {
			sb.append(to.getDefaultValue());
		}

		if (to.isNotNull()) {
			sb.append(" NOT NULL"); //$NON-NLS-1$
		} else {
			sb.append(" NULL"); //$NON-NLS-1$
		}

		return new String[] {sb.toString()};

	}

	public String[] createDropColumnDDL(Column column, boolean cascadeConstraints) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append(" DROP COLUMN "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(column.getName(), encloseChar));
		// sb.append(" CASCADE CONSTRAINTS ");
		return new String[] {sb.toString()};

	}

	public boolean supportsModifyColumnSize(String columnType) {
		return false;
	}

	public boolean supportsModifyColumnType() {
		return false;
	}

	public boolean supportsRemarks() {
		return false;
	}

	public boolean supportsDropColumnCascadeConstraints() {
		return false;
	}

	public boolean supportsRollbackDDL() {
		return false;
	}

	public String createCreateIndexDDL(String indexName, Column[] columns, int indexType) {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE"); //$NON-NLS-1$

		if (TYPE_UNIQUE_INDEX == indexType) {
			sb.append(" UNIQUE"); //$NON-NLS-1$
		} else if (TYPE_BITMAP_INDEX == indexType) {
			sb.append(" BITMAP"); //$NON-NLS-1$
		}
		sb.append(" INDEX "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(indexName, encloseChar));
		sb.append(" ON "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));


		sb.append("("); //$NON-NLS-1$
		for (int i = 0; i < columns.length; i++) {
			Column column = columns[i];
			if (i != 0) {
				sb.append(", "); //$NON-NLS-1$
			}
			sb.append(SQLUtil.enclose(column.getColumn().getColumnName(), encloseChar));
		}
		sb.append(")"); //$NON-NLS-1$

		return sb.toString();
	}

	public String createDropIndexDDL(String indexName) {
		StringBuffer sb = new StringBuffer();
		sb.append("DROP INDEX "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(indexName, encloseChar));
		sb.append(" ON "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		return sb.toString();
	}

	// ALTER TABLE SCOTT.EMP3 ADD CONSTRAINT PK_TEST PRIMARY KEY (EMPNO)
	public String createCreateConstraintPKDDL(String constraintName, Column[] columns) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append(" ADD CONSTRAINT "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(constraintName, encloseChar));
		sb.append(" PRIMARY KEY"); //$NON-NLS-1$
		sb.append("("); //$NON-NLS-1$
		for (int i = 0; i < columns.length; i++) {
			Column column = columns[i];
			if (i != 0) {
				sb.append(", "); //$NON-NLS-1$
			}
			sb.append(SQLUtil.enclose(column.getColumn().getColumnName(), encloseChar));
		}
		sb.append(")"); //$NON-NLS-1$
		return sb.toString();
	}

	public String createCreateConstraintUKDDL(String constraintName, Column[] columns) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append(" ADD CONSTRAINT "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(constraintName, encloseChar));
		sb.append(" UNIQUE ");//$NON-NLS-1$
		sb.append("("); //$NON-NLS-1$
		for (int i = 0; i < columns.length; i++) {
			Column column = columns[i];
			if (i != 0) {
				sb.append(", "); //$NON-NLS-1$
			}
			sb.append(SQLUtil.enclose(column.getColumn().getColumnName(), encloseChar));
		}
		sb.append(")"); //$NON-NLS-1$
		return sb.toString();
	}

	// ALTER TABLE SCOTT.EMP3 ADD CONSTRAINT FK_EMP
	// FOREIGN KEY (EMPNO) REFERENCES SCOTT.DEPT(DEPTNO) ON DELETE CASCADE
	public String createCreateConstraintFKDDL(String constraintName, Column[] columns, ITable refTable, Column[] refColumns, boolean onDeleteCascade) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append(" ADD CONSTRAINT "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(constraintName, encloseChar));
		sb.append(" FOREIGN KEY"); //$NON-NLS-1$
		sb.append("("); //$NON-NLS-1$
		for (int i = 0; i < columns.length; i++) {
			Column column = columns[i];
			if (i != 0) {
				sb.append(", "); //$NON-NLS-1$
			}
			sb.append(SQLUtil.enclose(column.getColumn().getColumnName(), encloseChar));
		}
		sb.append(")"); //$NON-NLS-1$
		sb.append(" REFERENCES "); //$NON-NLS-1$

		sb.append(getTableNameWithSchemaForSQL(refTable, isVisibleSchemaName));
		sb.append("("); //$NON-NLS-1$
		for (int i = 0; i < refColumns.length; i++) {
			Column refColumn = refColumns[i];
			if (i != 0) {
				sb.append(", "); //$NON-NLS-1$
			}
			sb.append(SQLUtil.enclose(refColumn.getColumn().getColumnName(), encloseChar));
		}
		sb.append(")"); //$NON-NLS-1$
		if (onDeleteCascade) {
			sb.append(" ON DELETE CASCADE"); //$NON-NLS-1$
		}

		return sb.toString();
	}

	// ALTER TABLE SCOTT.EMP3 DROP CONSTRAINT TESTPK
	public String createDropConstraintDDL(String constraintName, String type) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));

		if (type.equals(Constraint.PRIMARY_KEY)) {
			sb.append(" DROP PRIMARY KEY"); //$NON-NLS-1$

		} else if (type.equals(Constraint.FOREGIN_KEY)) {
			sb.append(" DROP FOREIGN KEY "); //$NON-NLS-1$
			sb.append(SQLUtil.enclose(constraintName, encloseChar));
		}

		return sb.toString();
	}

	public String createDDL() {
		StringBuffer sb = new StringBuffer();

		String folderName = table.getFolderName();

		if ("VIEW".equals(folderName)) { //$NON-NLS-1$
			sb.append(super.getCreateView());
		} else {
			sb.append(super.getCreateTableStr());
		}

		return sb.toString();
	}

	protected String getViewDDL_SQL(String dbName, String owner, String view) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT"); //$NON-NLS-1$
		sb.append("        view_definition"); //$NON-NLS-1$
		sb.append("    FROM"); //$NON-NLS-1$
		sb.append("        information_schema.views"); //$NON-NLS-1$
		sb.append("    WHERE"); //$NON-NLS-1$
		sb.append("        table_schema = '" + SQLUtil.encodeQuotation(owner) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("        AND table_name = '" + SQLUtil.encodeQuotation(view) + "'"); //$NON-NLS-1$ //$NON-NLS-2$

		return sb.toString();
	}
}
