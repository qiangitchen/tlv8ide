/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.sql.SQLException;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLHistory;
import zigen.plugin.db.core.SQLHistoryManager;
import zigen.plugin.db.core.SQLInvoker;
import zigen.plugin.db.core.SQLTokenizer;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ext.oracle.internal.OracleSourceErrorInfo;
import zigen.plugin.db.ext.oracle.internal.OracleSourceErrorSearcher;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.actions.ConfirmConnectDBAction;
import zigen.plugin.db.ui.actions.MaxRecordException;
import zigen.plugin.db.ui.editors.QueryViewEditor2;
import zigen.plugin.db.ui.editors.QueryViewEditorInput;
import zigen.plugin.db.ui.views.HistoryView;
import zigen.sql.parser.INode;
import zigen.sql.parser.ast.ASTCreateStatement;
import zigen.sql.parser.ast.ASTInto;
import zigen.sql.parser.ast.ASTRoot;
import zigen.sql.parser.ast.ASTSelectStatement;
import zigen.sql.parser.ast.ASTStatement;
import zigen.sql.parser.ast.ASTTarget;
import zigen.sql.parser.ast.ASTType;

public class SqlExecJob extends AbstractJob {

	protected SQLHistoryManager mgr = DbPlugin.getDefault().getHistoryManager();

	protected Transaction trans;

	protected String sqlString;

	protected String secondarlyId;

	protected int executeCount;

	protected OracleSourceErrorInfo[] oracleSourceErrorInfos;

	SQLHistory fSQLHistory;

	boolean isSuccess = false;

	boolean isRelead = false;

	public SqlExecJob(Transaction trans, String sqlString, String secondarlyId) {
		this(trans, sqlString, secondarlyId, false);
	}

	public SqlExecJob(Transaction trans, String sqlString, String secondarlyId, boolean isRelead) {
		super(Messages.getString("SqlExecJob.0")); //$NON-NLS-1$
		this.trans = trans;
		this.sqlString = sqlString;
		this.secondarlyId = secondarlyId;
		this.isRelead = isRelead;
	}

	protected IStatus run(IProgressMonitor monitor) {
		String sql = null;
		try {
			String demiliter = DbPlugin.getDefault().getPreferenceStore().getString(SQLEditorPreferencePage.P_SQL_DEMILITER);

			if (!trans.isConneting()) {
				Display.getDefault().syncExec(new ConfirmConnectDBAction(trans));
				if (!trans.isConneting()) {
					showWarningMessage(DbPluginConstant.MSG_NO_CONNECTED_DB);
					return Status.CANCEL_STATUS;
				}
			}

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			SQLTokenizer tokenizer = new SQLTokenizer(sqlString, demiliter);
			int total = tokenizer.getTokenCount();
			monitor.beginTask("executing...", total); //$NON-NLS-1$

			int cnt = 0;
			while (tokenizer.hasMoreElements()) {
				cnt++;
				sql = tokenizer.nextToken();
				if (sql != null && sql.length() > 0) {

					String dispSql = sql.replaceAll("\\r\\n|\\r|\\n", "");
					monitor.subTask(cnt + "/" + total + " : " + dispSql); //$NON-NLS-1$ //$NON-NLS-2$

					executeSingleSQL(trans, sql);
					executeCount++;

					monitor.worked(1);

				}
				if (monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}

			}

			if (!isRelead) {
				addHistory(trans.getConfig(), sqlString);
				showResults(new UpdateSQLHistoryAction());
			}

			return Status.OK_STATUS;

		} catch (SQLException e) {
			DbPlugin.log(e.getMessage() + ", SQL = " + sql);
			showWarningMessage(e.getMessage());

		} catch (Exception e) {
			showErrorMessage(Messages.getString("SqlExecJob.2"), e); //$NON-NLS-1$

		} finally {

		}
		return Status.OK_STATUS;

	}

	protected ASTStatement findASTStatement(INode root) {
		int size = root.getChildrenSize();
		for (int i = 0; i < root.getChildrenSize(); i++) {
			INode n = root.getChild(i);
			if (n instanceof ASTStatement) {
				return (ASTStatement) n;
			}
		}
		return null;
	}


	protected ASTCreateStatement findASTCreateStatement(INode node) {
		int size = node.getChildrenSize();
		for (int i = 0; i < node.getChildrenSize(); i++) {
			INode n = node.getChild(i);
			if (n instanceof ASTCreateStatement) {
				return (ASTCreateStatement) n;
			}
		}
		return null;
	}

	protected ASTType findASTType(ASTCreateStatement cs) {
		int size = cs.getChildrenSize();
		for (int i = 0; i < cs.getChildrenSize(); i++) {
			INode n = cs.getChild(i);
			if (n instanceof ASTType) {
				return (ASTType) n;
			}
		}
		return null;
	}

	private static final String PATTERN_EXE = "^GRANT.*|^REVOKE.*|^ALTER.*|^DROP.*|^RENAME.*|^TRUNCATE.*|^COMMENT.*";

	private static final String PATTERN_EXE_UPDATE = "^INSERT.*|^UPDATE.*|^DELETET.*|^MERGE.*|^COMMIT.*|^ROLLBACK.*|^SAVEPOINT.*";

	private static final String PATTERN_EXE_QUERY = "^SELECT.*|^SHOW.*|^DESCRIBE.*";

	private static final String PATTERN_EXE_CREATE = "^CREATE.*|^DECLARE.*";

	private static final Pattern P_EXECUTE = Pattern.compile(PATTERN_EXE, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

	private static final Pattern P_EXECUTE_UPDATE = Pattern.compile(PATTERN_EXE_UPDATE, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

	private static final Pattern P_EXECUTE_QUERY = Pattern.compile(PATTERN_EXE_QUERY, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

	private static final Pattern P_EXECUTE_CREATE = Pattern.compile(PATTERN_EXE_CREATE, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

	protected void executeSingleSQL(Transaction trans, String sql) throws Exception {
		TimeWatcher tw = new TimeWatcher();
		tw.start();

		String wk = sql.trim();
		if ((P_EXECUTE.matcher(wk)).matches()) {
			execute(sql);

		} else if ((P_EXECUTE_UPDATE.matcher(wk)).matches()) {
			executeUpdate(sql);

		} else if ((P_EXECUTE_QUERY.matcher(wk)).matches()) {
			if (wk.indexOf("INTO") > 0 && (wk.indexOf("OUTFILE") > 0 || wk.indexOf("DUMPFILE ") > 0)) {
				INode node = parseSql(sql);

				ASTStatement st = findASTStatement(node);
				if (st instanceof ASTSelectStatement) {
					ASTSelectStatement ss = (ASTSelectStatement) st;
					if (ss != null) {
						ASTInto into = (ASTInto) ss.getChild("ASTInto");
						if (into != null && into.hasASTOutfile()) {
							execute(sql);
							return;
						}
					}
				}

			}
			showDBEditor(sql);

		} else if ((P_EXECUTE_CREATE.matcher(wk)).matches()) {

			switch (DBType.getType(trans.getConfig())) {
			case DBType.DB_TYPE_ORACLE:
				sql = StringUtil.convertLineSep(sql, "\n");
				executeUpdate(sql);
				showErrorMessageForOracle(trans, parseSql(sql));
				break;
			default:
				executeUpdate(sql);
				break;
			}
		} else {
			INode node = parseSql(sql);
			ASTStatement st = findASTStatement(node);
			if (st instanceof ASTSelectStatement) {
				showDBEditor(sql);
			} else {
				executeUpdate(sql);
			}
		}

		tw.stop();

	}

	private void showErrorMessageForOracle(Transaction trans, INode node) throws Exception {
		ASTCreateStatement cs = findASTCreateStatement(node);
		if (cs != null) {
			ASTType astType = findASTType(cs);
			ASTTarget astName = astType.getASTTarget();
			if (astType != null && astName != null) {
				String type = astType.getName().toUpperCase();
				String schema = astName.getSchemaName();
				if (schema == null) {
					schema = trans.getConfig().getSchema().toUpperCase();
				} else {
					schema = schema.toUpperCase();
				}
				String name = astName.getCreateName();
				oracleSourceErrorInfos = OracleSourceErrorSearcher.execute(trans.getConnection(), schema, name, type);
				if (oracleSourceErrorInfos != null && oracleSourceErrorInfos.length > 0) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < oracleSourceErrorInfos.length; i++) {
						OracleSourceErrorInfo info = oracleSourceErrorInfos[i];
						sb.append(info.getErrorText() + "\n"); //$NON-NLS-1$
					}
					showResults(new ShowWarningMessageAction(sb.toString()));
				} else {
					updateMessage(trans.getConfig(), Messages.getString("SqlExecJob.21"), secondarlyId);
				}
			}
		}
	}

	protected INode parseSql(String sql) {
		INode node = new ASTRoot(); //$NON-NLS-1$
		zigen.sql.parser.ISqlParser parser = null;
		try {
			parser = new zigen.sql.parser.SqlParser(sql, DbPlugin.getSqlFormatRult());
			parser.parse(node);
			// for debug
			// parser.dump(node);
		} catch (Exception e) {
			DbPlugin.log(e);
		} finally {
			if (parser != null)
				parser = null;
		}
		return node;
	}

	protected void showDBEditor(String query) throws Exception {
		TableElement[] elements = null;
		TimeWatcher time = new TimeWatcher();
		time.start();
		IDBConfig config = trans.getConfig();
		try {
			elements = SQLInvoker.executeQuery(trans.getConnection(), query, config.isConvertUnicode(), config.isNoLockMode());
			time.stop();
			// addHistory(config, query);
			showResults(new ShowResultAction(config, query, elements, time.getTotalTime()));

		} catch (MaxRecordException e) {
			time.stop();
			// addHistory(config, query);
			elements = e.getTableElements();
			showResults(new ShowResultAction(config, query, elements, time.getTotalTime(), e.getMessage()));
			// showWarningMessage(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
	}

	void addHistory(IDBConfig config, String sql) {
		try {
			SQLHistory history = new SQLHistory(config, sql);
			this.fSQLHistory = history;
			this.isSuccess = mgr.addHistory(history);
		} catch (Exception e) {
			DbPlugin.log(e);
		}
	}

	void executeUpdate(String sql) throws Exception {
		try {
			String message = null;
			if (sql.toLowerCase().equals("commit")) { //$NON-NLS-1$
				message = createCommitMessage(trans.getTransactionCount());
				trans.commit();

			} else if (sql.toLowerCase().equals("rollback")) { //$NON-NLS-1$
				message = createRollbackMessage(trans.getTransactionCount());
				trans.rollback();

			} else {
				int rowAffected = SQLInvoker.executeUpdate(trans.getConnection(), sql);
				trans.addCount(rowAffected);
				message = createQueryMessage(trans.getTransactionCount());

				if (trans.getConfig().isAutoCommit()) {
					trans.resetCount();
				}
			}
			updateMessage(trans.getConfig(), message, secondarlyId);

		} catch (Exception e) {
			throw e;
		}
	}

	void execute(String sql) throws Exception {
		try {
			String message = null;
			boolean b = SQLInvoker.execute(trans.getConnection(), sql);
			if (b) {
				message = Messages.getString("SqlExecJob.8"); //$NON-NLS-1$
			}
			updateMessage(trans.getConfig(), message, secondarlyId);

		} catch (Exception e) {
			throw e;
		}
	}

	String createQueryMessage(int count) {
		StringBuffer sb = new StringBuffer();
		sb.append(Messages.getString("SqlExecJob.9")); //$NON-NLS-1$
		sb.append(Messages.getString("SqlExecJob.10")); //$NON-NLS-1$
		sb.append(count);
		sb.append(Messages.getString("SqlExecJob.11")); //$NON-NLS-1$
		return sb.toString();
	}

	String createCommitMessage(int count) {
		return count + Messages.getString("SqlExecJob.12"); //$NON-NLS-1$
	}

	String createRollbackMessage(int count) {
		return count + Messages.getString("SqlExecJob.13"); //$NON-NLS-1$
	}

	public int getExecuteCount() {
		return executeCount;
	}

	public OracleSourceErrorInfo[] getOracleSourceErrorInfos() {
		return oracleSourceErrorInfos;
	}

	protected class ShowResultAction implements Runnable {

		IDBConfig config = null;

		String query = null;

		TableElement[] elements = null;

		String responseTime = null;

		String message = ""; //$NON-NLS-1$

		public ShowResultAction(IDBConfig config, String query, TableElement[] elements, String responseTime, String message) {
			this.config = config;
			this.query = query;
			this.elements = elements;
			this.responseTime = responseTime;
			this.message = message;
		}

		public ShowResultAction(IDBConfig config, String query, TableElement[] elements, String responseTime) {
			this(config, query, elements, responseTime, ""); //$NON-NLS-1$
		}

		public void run() {
			try {
				IWorkbenchPage page = DbPlugin.getDefault().getPage();
				QueryViewEditorInput input = new QueryViewEditorInput(config, query, secondarlyId);
				IEditorPart editor = IDE.openEditor(page, input, DbPluginConstant.EDITOR_ID_QueryView2, false);

				if (editor instanceof QueryViewEditor2) {
					QueryViewEditor2 tEditor = (QueryViewEditor2) editor;
					tEditor.update(query, elements, responseTime, isRelead);
					tEditor.setInfomationText(message);

				}

			} catch (Exception e) {
				DbPlugin.log(e);
			}

		}

	}

	protected class UpdateSQLHistoryAction implements Runnable {

		public UpdateSQLHistoryAction() {

		}

		public void run() {
			try {
				if (isSuccess && fSQLHistory != null) {
					IViewPart part = DbPlugin.findView(DbPluginConstant.VIEW_ID_HistoryView);
					if (part instanceof HistoryView) {
						HistoryView hv = (HistoryView) part;
						hv.updateHistoryView(fSQLHistory);
						DbPlugin.fireStatusChangeListener(hv, IStatusChangeListener.EVT_UpdateHistory);
					} else {
						DbPlugin.fireStatusChangeListener(null, IStatusChangeListener.EVT_UpdateHistory);
					}

				}
			} catch (Exception e) {
				DbPlugin.log(e);
			}

		}

	}

}
