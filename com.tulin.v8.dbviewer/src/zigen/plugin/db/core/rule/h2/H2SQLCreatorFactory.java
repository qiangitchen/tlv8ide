/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule.h2;

import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.Constraint;
import zigen.plugin.db.ui.internal.ITable;

public class H2SQLCreatorFactory extends DefaultSQLCreatorFactory {

	public H2SQLCreatorFactory(ITable table) {
		super(table);
	}

	// select * from INFORMATION_SCHEMA.TABLES limit 5 offset 4
	public String createSelect(String _condition, int limit) {
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

		// ORDER BY
		if (orderBy != null && !"".equals(orderBy)) { //$NON-NLS-1$
			sb.append(" " + orderBy); //$NON-NLS-1$
		}

		if (limit > 0) {
			sb.append(" LIMIT ");
			sb.append(limit + 1);
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
		// ORDER BY
		if (orderBy != null && !"".equals(orderBy)) { //$NON-NLS-1$
			sb.append(" " + orderBy); //$NON-NLS-1$
		}

		if (limit > 0) {
			sb.append(" LIMIT ");
			sb.append(limit);
			sb.append(" OFFSET ");
			sb.append(offset - 1);
		}
		return sb.toString();
	}


	public String VisibleColumnSizePattern() {
		return ".*CHAR|^VARCHAR.*|^DECIMAL|^FLOAT|^DOUBLE|^REAL|^TIMESTAMP|^TIME|.*VARYING"; //$NON-NLS-1$
		// return "^VARCHAR";
	}

	public String[] getSupportColumnType() {
		return new String[] {"INT", //$NON-NLS-1$
				"BOOLEAN", //$NON-NLS-1$
				"TINYINT", //$NON-NLS-1$
				"SMALLINT", //$NON-NLS-1$
				"BIGINT", //$NON-NLS-1$
				"IDENTITY", //$NON-NLS-1$
				"DECIMAL", //$NON-NLS-1$
				"DOUBLE", //$NON-NLS-1$
				"REAL", //$NON-NLS-1$
				"TIME", //$NON-NLS-1$
				"DATE", //$NON-NLS-1$
				"TIMESTAMP", //$NON-NLS-1$
				"BINARY", //$NON-NLS-1$
				"OTHER", //$NON-NLS-1$
				"VARCHAR", //$NON-NLS-1$
				"VARCHAR_IGNORECASE", //$NON-NLS-1$
				"BLOB", //$NON-NLS-1$
				"CLOB" //$NON-NLS-1$
		};
	}

	public boolean supportsRemarks() {
		return true;
	}

	public boolean supportsModifyColumnType() {
		return true;
	}

	public boolean supportsModifyColumnSize(String columnType) {
		return isVisibleColumnSize(columnType);
	}

	public boolean supportsDropColumnCascadeConstraints() {
		return false;
	}

	public boolean supportsRollbackDDL() {
		return false;
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
		sb.append(SQLUtil.enclose(column.getName(), encloseChar));
		sb.append(" IS "); //$NON-NLS-1$
		sb.append("'" + SQLUtil.encodeQuotation(column.getRemarks()) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}

	public String createRenameColumnDDL(Column from, Column to) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append(" ALTER COLUMN "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(from.getName(), encloseChar));
		sb.append(" RENAME TO "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(to.getName(), encloseChar));
		return sb.toString();

	}

	public String[] createAddColumnDDL(Column column) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append(" ADD COLUMN "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(column.getName(), encloseChar));
		sb.append(" "); //$NON-NLS-1$

		sb.append(column.getTypeName());

		if (isVisibleColumnSize(column.getTypeName())) {
			sb.append("("); //$NON-NLS-1$
			sb.append(column.getSize());
			sb.append(")"); //$NON-NLS-1$
		}

		// DEFAULT
		if (column.getDefaultValue() != null && !"".equals(column.getDefaultValue())) { //$NON-NLS-1$
			sb.append(" DEFAULT "); //$NON-NLS-1$
			sb.append(column.getDefaultValue());
		}

		// NOT NULL
		if (column.isNotNull()) {
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

		// H2 cannt CASCADE
		return new String[] {sb.toString()};

	}

	public String[] createModifyColumnDDL(Column from, Column to) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append(" ALTER COLUMN "); //$NON-NLS-1$
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

	public String createRenameTableDDL(String newTableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append(" RENAME TO "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(newTableName, encloseChar));
		return sb.toString();
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
		sb.append(" UNIQUE ");
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
		if (Constraint.PRIMARY_KEY.equals(type)) {
			sb.append(" DROP PRIMARY KEY"); //$NON-NLS-1$
		} else {
			sb.append(" DROP CONSTRAINT "); //$NON-NLS-1$
			sb.append(SQLUtil.enclose(constraintName, encloseChar));
		}

		return sb.toString();

	}

}
