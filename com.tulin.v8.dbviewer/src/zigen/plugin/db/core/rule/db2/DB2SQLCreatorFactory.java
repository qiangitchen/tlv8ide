/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule.db2;

import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public class DB2SQLCreatorFactory extends DefaultSQLCreatorFactory {

	public DB2SQLCreatorFactory(ITable table) {
		super(table);
	}

	// select * from table fetch first 10 rows only
	public String createSelect(String _condition, int limit) {
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT * FROM "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));

		String[] conditions = SQLFormatter.splitOrderCause(_condition);
		String condition = conditions[0];
		String orderBy = conditions[1];

		sb.append(" WHERE 0 = 0");// dummy condition //$NON-NLS-1$

		if (condition != null && !"".equals(condition.trim())) { //$NON-NLS-1$
			sb.append(" AND " + condition); //$NON-NLS-1$
		}
		// ORDER BY
		if (orderBy != null && !"".equals(orderBy)) { //$NON-NLS-1$
			sb.append(" " + orderBy); //$NON-NLS-1$
		}
		if (limit > 0) {
			sb.append(" FETCH FIRST " + (limit + 1) + " ROWS ONLY");
			// //$NON-NLS-1$
			// //$NON-NLS-2$
		}

		return sb.toString();
	}

	public boolean supportsRemarks() {
		return true;
	}

	public boolean supportsModifyColumnType() {
		return false;
	}

	public boolean supportsModifyColumnSize(String columnType) {
		return isVisibleColumnSize(columnType);
	}

	public boolean supportsDropColumnCascadeConstraints() {
		return false;
	}

	public boolean supportsRollbackDDL() {
		return true;
	}

	public String createCommentOnTableDDL(String commnets) {
		StringBuffer sb = new StringBuffer();
		sb.append("COMMENT ON TABLE "); //$NON-NLS-1$

		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));

		sb.append(" IS "); //$NON-NLS-1$
		sb.append("'" + SQLUtil.encodeQuotation(commnets) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}

	public String createCommentOnColumnDDL(Column column) {
		StringBuffer sb = new StringBuffer();
		sb.append("COMMENT ON COLUMN "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append("."); //$NON-NLS-1$
//		sb.append(column.getName());
		sb.append(SQLUtil.enclose(column.getName(), encloseChar));
		sb.append(" IS"); //$NON-NLS-1$
		sb.append(" '" + SQLUtil.encodeQuotation(column.getRemarks()) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}

	public String createRenameTableDDL(String newTableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("RENAME TABLE "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append(" TO "); //$NON-NLS-1$
//		sb.append(newTableName);
		sb.append(SQLUtil.enclose(newTableName, encloseChar));
		return sb.toString();
	}

	public String createRenameColumnDDL(Column from, Column to) {
		return null;

	}

	public String[] createAddColumnDDL(Column column) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append(" ADD "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(column.getName(), encloseChar));
		sb.append(" "); //$NON-NLS-1$
		sb.append(column.getTypeName());
		if (isVisibleColumnSize(column.getTypeName())) {
			sb.append("("); //$NON-NLS-1$
			sb.append(column.getSize());
			sb.append(")"); //$NON-NLS-1$
		}

		if (column.getDefaultValue() != null && !"".equals(column.getDefaultValue())) {// DEFAULT
			// //$NON-NLS-1$
			sb.append(" DEFAULT "); //$NON-NLS-1$
			sb.append(column.getDefaultValue());
		}

		if (column.isNotNull()) { // NOT NULL
			sb.append(" NOT NULL"); //$NON-NLS-1$
		} else {
			;
		}

		return new String[] {sb.toString()};

	}

	public String[] createModifyColumnDDL(Column from, Column to) {
		return null;

	}

	public String[] createDropColumnDDL(Column column, boolean cascadeConstraints) {
		return null;
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
		sb.append(SQLUtil.enclose(table.getSchemaName(), encloseChar));
		sb.append("."); //$NON-NLS-1$
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
		sb.append(SQLUtil.enclose(table.getSchemaName(), encloseChar));
		sb.append("."); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(indexName, encloseChar));
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

	// ALTER TABLE SCOTT.EMP3 ADD CONSTRAINT MY_CHECK CHECK (SAL > 0)
	public String createCreateConstraintCheckDDL(String constraintName, String check) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append(" ADD CONSTRAINT "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(constraintName, encloseChar));
		sb.append(" CHECK"); //$NON-NLS-1$
		sb.append("("); //$NON-NLS-1$
		sb.append(check);
		sb.append(")"); //$NON-NLS-1$
		return sb.toString();
	}

	// ALTER TABLE SCOTT.EMP3 DROP CONSTRAINT TESTPK
	public String createDropConstraintDDL(String constraintName, String type) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append(" DROP CONSTRAINT "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(constraintName, encloseChar));
		return sb.toString();

	}
}
