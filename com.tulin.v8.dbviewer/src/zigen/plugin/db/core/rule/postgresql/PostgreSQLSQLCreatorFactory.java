/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule.postgresql;

import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public class PostgreSQLSQLCreatorFactory extends DefaultSQLCreatorFactory {

	public PostgreSQLSQLCreatorFactory(ITable table) {
		super(table);
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
			sb.append(" LIMIT " + (limit + 1));
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
		// -->
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
		return new String[] {"BOOL", //$NON-NLS-1$
				"CHAR", //$NON-NLS-1$
				"INT4", //$NON-NLS-1$
				"INT8", //$NON-NLS-1$
				"SERIAL4", //$NON-NLS-1$
				"SERIAL8", //$NON-NLS-1$
				"VARBIT", //$NON-NLS-1$
				"VARCHAR", //$NON-NLS-1$
				"FLOAT4", //$NON-NLS-1$
				"FLOAT8", //$NON-NLS-1$
				"NUMERIC", //$NON-NLS-1$
				"DATE", //$NON-NLS-1$
				"TIME", //$NON-NLS-1$
				"TIMESTAMP", //$NON-NLS-1$
				"TEXT" //$NON-NLS-1$

		};
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
		sb.append(" IS"); //$NON-NLS-1$
		sb.append(" '" + SQLUtil.encodeQuotation(commnets) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}

	public String createCommentOnColumnDDL(Column column) {
		StringBuffer sb = new StringBuffer();
		sb.append("COMMENT ON COLUMN "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append("."); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(column.getName(), encloseChar));
		sb.append(" IS"); //$NON-NLS-1$
		sb.append(" '" + SQLUtil.encodeQuotation(column.getRemarks()) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}

	public String createRenameTableDDL(String newTableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append(" RENAME TO "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(SQLUtil.encodeQuotation(newTableName), encloseChar));
		return sb.toString();
	}

	public String createRenameColumnDDL(Column from, Column to) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "); //$NON-NLS-1$
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb.append(" RENAME COLUMN "); //$NON-NLS-1$
		sb.append(SQLUtil.enclose(from.getName(), encloseChar));
		sb.append(" TO "); //$NON-NLS-1$
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
		if (column.getDefaultValue() != null && !"".equals(column.getDefaultValue())) {// DEFAULT
			// //$NON-NLS-1$
			sb.append(" DEFAULT "); //$NON-NLS-1$
			sb.append(column.getDefaultValue());
		}

		if (column.isNotNull()) { // NOT NULL
			sb.append(" NOT NULL"); //$NON-NLS-1$
		} else {
			sb.append(" NULL"); //$NON-NLS-1$
		}

		return new String[] {sb.toString()};

	}

	public String[] createModifyColumnDDL(Column from, Column to) {
		StringBuffer sb = new StringBuffer();
		if (!from.getSize().equals(to.getSize())) {
			sb.append("ALTER TABLE "); //$NON-NLS-1$
			sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
			sb.append(" ALTER "); //$NON-NLS-1$
			sb.append(SQLUtil.enclose(to.getName(), encloseChar));
			sb.append(" TYPE "); //$NON-NLS-1$
			sb.append(to.getTypeName());
			if (isVisibleColumnSize(to.getTypeName())) {
				sb.append("("); //$NON-NLS-1$
				sb.append(to.getSize());
				sb.append(")"); //$NON-NLS-1$
			}
		}

		StringBuffer sb2 = new StringBuffer();
		sb2.append("ALTER TABLE "); //$NON-NLS-1$
		sb2.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb2.append(" ALTER COLUMN "); //$NON-NLS-1$
		sb2.append(SQLUtil.enclose(to.getName(), encloseChar));
		sb2.append(" "); //$NON-NLS-1$
		if ("".equals(to.getDefaultValue())) { //$NON-NLS-1$
			sb2.append("DROP DEFAULT"); //$NON-NLS-1$
		} else {
			sb2.append("SET DEFAULT "); //$NON-NLS-1$
			sb2.append(to.getDefaultValue());
		}

		StringBuffer sb3 = new StringBuffer();
		sb3.append("ALTER TABLE "); //$NON-NLS-1$
		sb3.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
		sb3.append(" ALTER COLUMN "); //$NON-NLS-1$
		sb3.append(SQLUtil.enclose(to.getName(), encloseChar));
		sb3.append(" "); //$NON-NLS-1$

		if (to.isNotNull()) {
			sb3.append("SET NOT NULL"); //$NON-NLS-1$
		} else {
			sb3.append("DROP NOT NULL"); //$NON-NLS-1$
		}

		return new String[] {sb.toString(), sb2.toString(), sb3.toString()};
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

	public String createCreateIndexDDL(String indexName, Column[] columns, int indexType) {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE"); //$NON-NLS-1$

		if (TYPE_UNIQUE_INDEX == indexType) {
			sb.append(" UNIQUE"); //$NON-NLS-1$
		} else if (TYPE_BITMAP_INDEX == indexType) {
			sb.append(" BITMAP"); //$NON-NLS-1$
		}
		sb.append(" INDEX "); //$NON-NLS-1$
		// sb.append(table.getSchemaName());
		// sb.append(".");
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
		// sb.append(table.getSchemaName());
		// sb.append(".");
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

	public String createDDL() {
		StringBuffer sb = new StringBuffer();

		String folderName = table.getFolderName();

		if ("VIEW".equals(folderName)) { //$NON-NLS-1$
			sb.append(getCreateView());
		} else {
			sb.append(super.getCreateTableStr());
		}

		sb.append(getTableComment());

		sb.append(getColumnComment());
		return sb.toString();
	}


	protected String getColumnLabel(TableColumn column) {

		StringBuffer sb = new StringBuffer();
		sb.append("    ");
		sb.append(StringUtil.padding(column.getColumnName(), 28));

		String typeName = column.getTypeName().toUpperCase();

		if ("BPCHAR".equals(typeName)) {
			sb.append("CHAR");

		} else {
			sb.append(typeName);
		}

		if (isVisibleColumnSize(typeName)) {
			if (column.getDecimalDigits() == 0) {
				sb.append("(" + column.getColumnSize() + ")");
			} else {
				sb.append("(" + column.getColumnSize() + "," + column.getDecimalDigits() + ")");
			}
		}

		return sb.toString();
	}

	protected String getViewDDL_SQL(String dbName, String owner, String view) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("        definition");
		sb.append("    FROM");
		sb.append("        pg_views");
		sb.append("    WHERE");
		sb.append("        schemaname = '" + SQLUtil.encodeQuotation(owner) + "'");
		sb.append("        AND viewname = '" + SQLUtil.encodeQuotation(view) + "'");
		return sb.toString();
	}


}
