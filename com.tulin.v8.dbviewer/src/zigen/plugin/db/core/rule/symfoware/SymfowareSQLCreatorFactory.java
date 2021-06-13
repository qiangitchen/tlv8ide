/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule.symfoware;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TablePKColumn;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.preference.SQLFormatPreferencePage;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public class SymfowareSQLCreatorFactory extends DefaultSQLCreatorFactory {

	public SymfowareSQLCreatorFactory(ITable table) {
		super(table);
	}

	public String createSelect(String _condition, int limit) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM ");
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));

		String[] conditions = SQLFormatter.splitOrderCause(_condition);
		String condition = conditions[0];
		String orderBy = conditions[1];


		if (condition != null && !"".equals(condition.trim())) {
			sb.append(" WHERE " + condition);
		}

		// ORDER BY
		if (orderBy != null && !"".equals(orderBy)) { //$NON-NLS-1$
			sb.append(" " + orderBy); //$NON-NLS-1$
		}

		/*
		 * if (table.getDbConfig().isNoLockMode()) { if (!sb.toString().trim().endsWith("WITH OPTION LOCK_MODE(NL)")) { sb.append(" WITH OPTION LOCK_MODE(NL)"); } }
		 */
		if (!sb.toString().trim().endsWith("WITH OPTION LOCK_MODE(NL)")) {
			sb.append(" WITH OPTION LOCK_MODE(NL)");
		}

		return sb.toString();
	}

	public String createCountAll(String condition) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT COUNT(*) FROM ");
		sb.append(table.getSqlTableName());

		if (condition != null && !"".equals(condition.trim())) {
			sb.append(" WHERE " + condition);
		}
		sb.append(" WITH OPTION LOCK_MODE(NL)");

		return sb.toString();
	}

	public String createCountForQuery(String query) {
		StringBuffer sb = new StringBuffer();
		sb.append(super.createCountForQuery(query));
		sb.append(" WITH OPTION LOCK_MODE(NL)");
		return sb.toString();
	}

	protected String getCreateView() {
		StringBuffer wk = new StringBuffer();
		try {
			boolean onPatch = DbPlugin.getDefault().getPreferenceStore().getBoolean(SQLFormatPreferencePage.P_FORMAT_PATCH);
			int type = DbPlugin.getDefault().getPreferenceStore().getInt(SQLFormatPreferencePage.P_USE_FORMATTER_TYPE);

			// wk.append("CREATE OR REPLACE VIEW "); //$NON-NLS-1$
			wk.append("CREATE VIEW "); //$NON-NLS-1$
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

	protected String getViewDDL_SQL(String dbName, String owner, String view) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("        D.DESC_VALUE");
		sb.append("    FROM");
		sb.append("        RDBII_SYSTEM.RDBII_TABLE T");
		sb.append("        ,RDBII_SYSTEM.RDBII_DESCRIPTION D");
		sb.append("    WHERE");
		sb.append("        T.DB_CODE = D.DB_CODE");
		sb.append("        AND T.TABLE_CODE = D.OBJECT_CODE");
		sb.append("        AND T.DB_NAME = '" + SQLUtil.encodeQuotation(dbName) + "'");
		sb.append("        AND T.SCHEMA_NAME = '" + SQLUtil.encodeQuotation(owner) + "'");
		sb.append("        AND T.TABLE_NAME = '" + SQLUtil.encodeQuotation(view) + "'");
		sb.append("        AND T.TABLE_TYPE = 'VW'");
		return sb.toString();
	}


	public String[] createAddColumnDDL(Column column) {
		return null;
	}

	public String createCommentOnColumnDDL(Column column) {
		return null;
	}

	public String createCommentOnTableDDL(String commnets) {
		return null;
	}

	public String[] createDropColumnDDL(Column column, boolean cascadeConstraints) {
		return null;
	}

	public String[] createModifyColumnDDL(Column from, Column to) {
		return null;
	}

	public String createRenameColumnDDL(Column from, Column to) {
		return null;
	}

	public String createRenameTableDDL(String newTableName) {
		return null;
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

	public String VisibleColumnSizePattern() {
		return "^CHAR|^VARCHAR.*|^NCHAR.*|^NATIONAL.*|^NUMERIC|^DEC.*|^FLOAT|^BLOB|^INTERVAL.*"; //$NON-NLS-1$
	}

	public String getTableComment() {
		StringBuffer sb = new StringBuffer();

		if (table.getRemarks() != null && !"".equals(table.getRemarks())) { //$NON-NLS-1$
			sb.append("ALTER TABLE "); //$NON-NLS-1$
			sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));

			sb.append(" COMMENT "); //$NON-NLS-1$
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
				sb.append("ALTER TABLE  "); //$NON-NLS-1$
				sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));
				sb.append(" MODIFY "); //$NON-NLS-1$
				sb.append(tCol.getColumnName());
				sb.append(" COMMENT "); //$NON-NLS-1$
				sb.append("'"); //$NON-NLS-1$
				sb.append(tCol.getRemarks());
				sb.append("'"); //$NON-NLS-1$

				setDemiliter(sb);
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
				sb.append("PRIMARY KEY ");
				sb.append("(");
				sb.append(pkc.getColumnName());
			} else {
				sb.append(", " + pkc.getColumnName());
			}

		}
		sb.append(")");
		return sb.toString();
	}

	protected String[] getConstraintIDXStr() {
		return null;
	}
}
