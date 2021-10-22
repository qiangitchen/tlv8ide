/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.wizard;

import java.sql.SQLException;

import org.eclipse.jface.wizard.Wizard;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLInvoker;
import zigen.plugin.db.core.SQLTokenizer;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.internal.ITable;

abstract public class DefaultWizard extends Wizard implements IConfirmDDLWizard {


	protected ConfirmDDLWizardPage confirmPage;

	protected ISQLCreatorFactory factory;

	protected ITable tableNode;

	protected IDBConfig config;

	public DefaultWizard(ISQLCreatorFactory factory, ITable tableNode) {
		super();
		this.factory = factory;
		this.tableNode = tableNode;
		this.config = tableNode.getDbConfig();

		setNeedsProgressMonitor(true);
		setHelpAvailable(false);

		this.confirmPage = new ConfirmDDLWizardPage();
	}

	public boolean performFinish() {
		try {
			confirmPage.generateSQL();

			int rowAffected = 0;

			String demiliter = DbPlugin.getDefault().getPreferenceStore().getString(SQLEditorPreferencePage.P_SQL_DEMILITER);
			String sqlString = confirmPage.getDocument().get();

			Transaction trans = Transaction.getInstance(config);
			SQLTokenizer tokenizer = new SQLTokenizer(sqlString, demiliter);
			while (tokenizer.hasMoreElements()) {
				String sql = tokenizer.nextToken();
				if (sql != null && sql.length() > 0) {
					rowAffected += SQLInvoker.executeUpdate(trans.getConnection(), sql);
				}
			}

			if (!config.isAutoCommit() && factory.supportsRollbackDDL()) {
				if (DbPlugin.getDefault().confirmDialog(Messages.getString("DefaultWizard.0"))) { //$NON-NLS-1$
					trans.commit();
				}
			}

			DbPlugin.fireStatusChangeListener(tableNode, IStatusChangeListener.EVT_ModifyTableDefine);

			return true;

		} catch (SQLException e) {
			DbPlugin.getDefault().showWarningMessage(e.getMessage());

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
		return false;
	}

	abstract public String[] createSQL() throws Exception;

	public ConfirmDDLWizardPage getConfirmDDLWizardPage() {
		return confirmPage;
	}

}
