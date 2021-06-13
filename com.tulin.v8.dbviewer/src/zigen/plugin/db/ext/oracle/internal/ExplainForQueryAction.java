/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import java.sql.Connection;
import java.sql.SQLException;

import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLInvoker;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ui.actions.SQLSourceViewerAction;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;


public class ExplainForQueryAction extends SQLSourceViewerAction {

	private static String PRE_SQL = "EXPLAIN PLAN FOR "; //$NON-NLS-1$

	private static String TRUNCATE_SQL = "truncate table plan_table"; //$NON-NLS-1$

	public ExplainForQueryAction(SQLSourceViewer viewer) {
		super(viewer);
		this.setText(Messages.getString("ExplainForQueryAction.2")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("ExplainForQueryAction.3")); //$NON-NLS-1$
		this.setEnabled(false);
	}

	public void run() {

		try {
			IDBConfig config = fSQLSourceViewer.getDbConfig();
			Transaction trans = Transaction.getInstance(config);

			if (!trans.isConneting()) {
				DbPlugin.getDefault().showWarningMessage(DbPluginConstant.MSG_NO_CONNECTED_DB);
				return;
			}

			ISelection selection = fSQLSourceViewer.getSelection();
			if (!(selection instanceof TextSelection)) {
				return;
			}

			TextSelection textSelection = (TextSelection) selection;
			String sql = textSelection.getText().trim();
			if (sql.endsWith("/")) { //$NON-NLS-1$
				sql = sql.substring(0, sql.length() - 1);
			}

			Connection con = trans.getConnection();

			if (sql != null && sql.trim().length() > 0) {

				if (SQLUtil.isSelect(sql)) {

					// truncate
					// SQLInvoker.executeUpdate(con, TRUNCATE_SQL);
					truncate(con);

					// explain plan for ...
					String explainSql = PRE_SQL + sql;
					SQLInvoker.executeUpdate(con, explainSql);

					ExplainDao dao = new ExplainDao();
					ExplainVo[] vos = dao.execute(con);

					con.commit();

					ExplainTreeModel invisibleRoot = new ExplainTreeModel(new ExplainVo());
					if (vos.length > 0) {

						for (int i = 0; i < vos.length; i++) {
							ExplainVo vo = vos[i];
							ExplainTreeModel model = new ExplainTreeModel(vo);
							invisibleRoot.addEntry(model);
						}

						Shell shell = DbPlugin.getDefault().getShell();
						ExplainResultDialog dialog = new ExplainResultDialog(shell, invisibleRoot);
						dialog.open();
					}

				} else {
					DbPlugin.getDefault().showWarningMessage(Messages.getString("ExplainForQueryAction.5")); //$NON-NLS-1$
				}

			} else {
				DbPlugin.getDefault().showWarningMessage(Messages.getString("ExplainForQueryAction.6")); //$NON-NLS-1$
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

	public static final int ORA_00942 = 942; // table or view does not exist

	private void truncate(Connection con) throws Exception {
		// truncate
		try {
			SQLInvoker.executeUpdate(con, TRUNCATE_SQL);
		} catch (SQLException e) {
			if (ORA_00942 == e.getErrorCode()) {
				if (DbPlugin.getDefault().confirmDialog(Messages.getString("ExplainForQueryAction.7"))) { //$NON-NLS-1$
					SQLInvoker.executeUpdate(con, getCreatePlanTableSql());
					return;
				}
			}
			throw e;

		}
	}

	private String getCreatePlanTableSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE PLAN_TABLE"); //$NON-NLS-1$
		sb.append("("); //$NON-NLS-1$
		sb.append("    STATEMENT_ID                VARCHAR2(30),"); //$NON-NLS-1$
		sb.append("    TIMESTAMP                   DATE,"); //$NON-NLS-1$
		sb.append("    REMARKS                     VARCHAR2(80),"); //$NON-NLS-1$
		sb.append("    OPERATION                   VARCHAR2(30),"); //$NON-NLS-1$
		sb.append("    OPTIONS                     VARCHAR2(255),"); //$NON-NLS-1$
		sb.append("    OBJECT_NODE                 VARCHAR2(128),"); //$NON-NLS-1$
		sb.append("    OBJECT_OWNER                VARCHAR2(30),"); //$NON-NLS-1$
		sb.append("    OBJECT_NAME                 VARCHAR2(30),"); //$NON-NLS-1$
		sb.append("    OBJECT_INSTANCE             NUMBER(22),"); //$NON-NLS-1$
		sb.append("    OBJECT_TYPE                 VARCHAR2(30),"); //$NON-NLS-1$
		sb.append("    OPTIMIZER                   VARCHAR2(255),"); //$NON-NLS-1$
		sb.append("    SEARCH_COLUMNS              NUMBER(22),"); //$NON-NLS-1$
		sb.append("    ID                          NUMBER(22),"); //$NON-NLS-1$
		sb.append("    PARENT_ID                   NUMBER(22),"); //$NON-NLS-1$
		sb.append("    POSITION                    NUMBER(22),"); //$NON-NLS-1$
		sb.append("    COST                        NUMBER(22),"); //$NON-NLS-1$
		sb.append("    CARDINALITY                 NUMBER(22),"); //$NON-NLS-1$
		sb.append("    BYTES                       NUMBER(22),"); //$NON-NLS-1$
		sb.append("    OTHER_TAG                   VARCHAR2(255),"); //$NON-NLS-1$
		sb.append("    PARTITION_START             VARCHAR2(255),"); //$NON-NLS-1$
		sb.append("    PARTITION_STOP              VARCHAR2(255),"); //$NON-NLS-1$
		sb.append("    PARTITION_ID                NUMBER(22),"); //$NON-NLS-1$
		sb.append("    OTHER                       LONG,"); //$NON-NLS-1$
		sb.append("    DISTRIBUTION                VARCHAR2(30),"); //$NON-NLS-1$
		sb.append("    CPU_COST                    NUMBER(22),"); //$NON-NLS-1$
		sb.append("    IO_COST                     NUMBER(22),"); //$NON-NLS-1$
		sb.append("    TEMP_SPACE                  NUMBER(22),"); //$NON-NLS-1$
		sb.append("    ACCESS_PREDICATES           VARCHAR2(4000),"); //$NON-NLS-1$
		sb.append("    FILTER_PREDICATES           VARCHAR2(4000)"); //$NON-NLS-1$
		sb.append(")"); //$NON-NLS-1$

		return sb.toString();

	}

}
