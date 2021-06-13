/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.PluginSettingsManager;
import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLHistoryManager;
import zigen.plugin.db.ui.actions.GlobalAction;
import zigen.plugin.db.ui.actions.LockDataBaseAction;
import zigen.plugin.db.ui.actions.OpenSQLAction;
import zigen.plugin.db.ui.actions.SaveSQLAction;
import zigen.plugin.db.ui.actions.ShowHistoryViewAction;
import zigen.plugin.db.ui.views.CommitModeAction;
import zigen.plugin.db.ui.views.FormatModeAction;
import zigen.plugin.db.ui.views.ISQLOperationTarget;

public class SQLToolBar {

	protected PluginSettingsManager pluginMgr = DbPlugin.getDefault().getPluginSettingsManager();

	protected SQLHistoryManager historyManager = DbPlugin.getDefault().getHistoryManager();

	protected IDBConfig[] configs;

	protected SQLSourceViewer fSourceViewer;

	protected CoolBar coolBar;

	protected Combo selectCombo;

	protected ComboContributionItem comboContributionItem = new ComboContributionItem("SelectDataBase"); //$NON-NLS-1$

	protected GlobalAction allExecAction = new GlobalAction(null, ISQLOperationTarget.ALL_EXECUTE);

	protected GlobalAction currExecAction = new GlobalAction(null, ISQLOperationTarget.CURRENT_EXECUTE);

	protected GlobalAction selectExecAction = new GlobalAction(null, ISQLOperationTarget.SELECTED_EXECUTE);

	protected GlobalAction scriptExecAction = new GlobalAction(null, ISQLOperationTarget.SCRIPT_EXECUTE);

	protected GlobalAction allClearAction = new GlobalAction(null, ISQLOperationTarget.ALL_CLEAR);

	protected GlobalAction nextSqlAction = new GlobalAction(null, ISQLOperationTarget.NEXT_SQL);

	protected GlobalAction backSqlAction = new GlobalAction(null, ISQLOperationTarget.BACK_SQL);

	protected GlobalAction formatSqlAction = new GlobalAction(null, ISQLOperationTarget.FORMAT);

	protected GlobalAction commitAction = new GlobalAction(null, ISQLOperationTarget.COMMIT);

	protected GlobalAction rollbackAction = new GlobalAction(null, ISQLOperationTarget.ROLLBACK);

	protected OpenSQLAction openAction = new OpenSQLAction(null);

	protected SaveSQLAction saveAction = new SaveSQLAction(null);

	protected CommitModeAction commitModeAction = new CommitModeAction(null);

	protected FormatModeAction formatModeAction = new FormatModeAction(null);

	protected ShowHistoryViewAction showHistoryViewAction = new ShowHistoryViewAction();

	protected LockDataBaseAction lockDataBaseAction = new LockDataBaseAction(null);

	String lastSelectedDB;

	boolean lastAutoFormatMode;


	protected ToolBarContributionItem getToolBarContributionItem1(CoolBarManager coolBarMgr) {
		ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
		toolBarMgr.add(openAction);
		toolBarMgr.add(saveAction);
		return new ToolBarContributionItem(toolBarMgr);
	}

	protected ToolBarContributionItem getToolBarContributionItem2(CoolBarManager coolBarMgr) {
		ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
		toolBarMgr.add(allExecAction);
		toolBarMgr.add(scriptExecAction);
		toolBarMgr.add(allClearAction);
		return new ToolBarContributionItem(toolBarMgr);
	}

	protected ToolBarContributionItem getToolBarContributionItem3(CoolBarManager coolBarMgr) {
		ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
		toolBarMgr.add(backSqlAction);
		toolBarMgr.add(nextSqlAction);
		return new ToolBarContributionItem(toolBarMgr);
	}

	protected ToolBarContributionItem getToolBarContributionItem4(CoolBarManager coolBarMgr) {
		ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
		toolBarMgr.add(commitModeAction);
		toolBarMgr.add(commitAction);
		toolBarMgr.add(rollbackAction);
		return new ToolBarContributionItem(toolBarMgr);
	}

	protected ToolBarContributionItem getToolBarContributionItem5(CoolBarManager coolBarMgr) {
		ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
		toolBarMgr.add(formatModeAction);
		return new ToolBarContributionItem(toolBarMgr);
	}


	protected ToolBarContributionItem getToolBarContributionItem6(CoolBarManager coolBarMgr) {
		ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
		toolBarMgr.add(comboContributionItem); //$NON-NLS-1$
		toolBarMgr.add(lockDataBaseAction);
		return new ToolBarContributionItem(toolBarMgr);
	}


	public void createPartControl(final Composite parent) {
		coolBar = new CoolBar(parent, SWT.FLAT);
		// coolBar = new CoolBar(parent, SWT.NONE);

		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		coolBar.setLayoutData(data);
		CoolBarManager coolBarMgr = new CoolBarManager(coolBar);
		coolBarMgr.add(getToolBarContributionItem1(coolBarMgr));
		coolBarMgr.add(getToolBarContributionItem2(coolBarMgr));
		coolBarMgr.add(getToolBarContributionItem3(coolBarMgr));
		coolBarMgr.add(getToolBarContributionItem4(coolBarMgr));
		coolBarMgr.add(getToolBarContributionItem5(coolBarMgr));
		coolBarMgr.add(getToolBarContributionItem6(coolBarMgr));
		coolBarMgr.update(true);

		coolBar.addControlListener(new ControlListener() {

			public void controlMoved(ControlEvent e) {}

			public void controlResized(ControlEvent e) {
				parent.getParent().layout(true);
				parent.layout(true);
			}
		});
	}

	public SQLToolBar() {}


	public void setSQLSourceViewer(SQLSourceViewer sqlSourceViewer) {
		fSourceViewer = sqlSourceViewer;
		openAction.setSQLSourceViewer(sqlSourceViewer);
		saveAction.setSQLSourceViewer(sqlSourceViewer);
		nextSqlAction.setTextViewer(sqlSourceViewer);
		backSqlAction.setTextViewer(sqlSourceViewer);
		allExecAction.setTextViewer(sqlSourceViewer);
		currExecAction.setTextViewer(sqlSourceViewer);
		selectExecAction.setTextViewer(sqlSourceViewer);
		scriptExecAction.setTextViewer(sqlSourceViewer);
		allClearAction.setTextViewer(sqlSourceViewer);
		formatModeAction.setSQLSourceViewer(sqlSourceViewer);
		commitModeAction.setSQLSourceViewer(sqlSourceViewer);
		commitAction.setTextViewer(sqlSourceViewer);
		rollbackAction.setTextViewer(sqlSourceViewer);
	}

	public final void updateHistoryButton() {
		if (historyManager.hasPrevHistory()) {
			if (backSqlAction != null)
				backSqlAction.setEnabled(true);
		} else {
			if (backSqlAction != null)
				backSqlAction.setEnabled(false);
		}
		if (historyManager.hasNextHistory()) {
			if (nextSqlAction != null)
				nextSqlAction.setEnabled(true);
		} else {
			if (nextSqlAction != null)
				nextSqlAction.setEnabled(false);
		}
	}

	public void updateCombo(IDBConfig config) {
		if (!lockDataBaseAction.isChecked()) {
			comboContributionItem.updateCombo(config);
		}
	}

	public void initializeSelectCombo() {
		comboContributionItem.initializeSelectCombo();
	}

	public void setCommitMode(IDBConfig targetConfig, boolean autoCommit) {
		comboContributionItem.setCommitMode(targetConfig, autoCommit);
	}

	class ComboContributionItem extends ControlContribution {

		boolean isSaveLastDb = false;

		public ComboContributionItem(String id) {
			super(id);
		}

		public void setSaveLastDb(boolean isSave) {
			this.isSaveLastDb = isSave;
		}

		protected Control createControl(Composite parent) {
			selectCombo = new Combo(parent, SWT.READ_ONLY);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.widthHint = 200;
			selectCombo.setLayoutData(data);

			lastSelectedDB = getLastSelectedDBName();

			initializeSelectCombo();

			selectCombo.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					IDBConfig config = getConfig();

					if (fSourceViewer != null) {
						updateCombo(config);
					}

					if (isSaveLastDb) {
						lastSelectedDB = config.getDbName();
						setLastSelectedDBName(lastSelectedDB);
					}
					DbPlugin.fireStatusChangeListener(config, IStatusChangeListener.EVT_ChangeDataBase);
				}
			});

			return selectCombo;
		}

		String getLastSelectedDBName() {
			Object obj = pluginMgr.getValue(PluginSettingsManager.KEY_DEFAULT_DB);
			if (obj != null) {
				return (String) obj;
			} else {
				return null;
			}
		}

		void setLastSelectedDBName(String dbName) {
			pluginMgr.setValue(PluginSettingsManager.KEY_DEFAULT_DB, dbName);
		}

		void initializeSelectCombo() {
			IDBConfig config = getConfig();
			selectCombo.removeAll();
			configs = DBConfigManager.getDBConfigs();
			for (int i = 0; i < configs.length; i++) {
				IDBConfig w_config = configs[i];
				selectCombo.add(w_config.getSchema() + " : " + w_config.getDbName() + "  "); //$NON-NLS-1$ //$NON-NLS-2$

				if (lastSelectedDB != null && lastSelectedDB.equals(w_config.getDbName())) {
					selectCombo.select(i);
				}

				if (config != null && config.getDbName().equals(w_config.getDbName())) {
					selectCombo.select(i);
				}
			}
		}

		void updateCombo(IDBConfig newConfig) {
			if (newConfig != null) {
				IDBConfig config = null;
				for (int i = 0; i < configs.length; i++) {
					IDBConfig w_config = configs[i];
					if (newConfig != null) {
						if (newConfig.getDbName().equals(w_config.getDbName())) {
							config = w_config;
							selectCombo.select(i);
						}
					}
				}
				if (fSourceViewer != null && config != null) {
					fSourceViewer.setDbConfig(config);
					commitModeAction.setSQLSourceViewer(fSourceViewer);
					comboContributionItem.setCommitMode(newConfig, config.isAutoCommit());
				}
			} else {
				selectCombo.select(-1);
			}
		}

		private void setCommitMode(IDBConfig targetConfig, boolean isAutoCommit) {
			IDBConfig config = getConfig();
			if (config == null || config.getDbName().equals(targetConfig.getDbName())) {
				commitAction.setEnabled(!isAutoCommit);
				rollbackAction.setEnabled(!isAutoCommit);
				commitModeAction.setCommitMode(isAutoCommit);

			}

		}

		public IDBConfig selectedConfig() {
			int index = selectCombo.getSelectionIndex();
			if (index >= 0) {
				return configs[index];
			} else {
				return null;
			}
		}
	}

	public ComboContributionItem getComboContributionItem() {
		return comboContributionItem;
	}

	public IDBConfig getConfig() {
		return comboContributionItem.selectedConfig();
	}

	public IDBConfig[] getConfigs() {
		return configs;
	}

	public CoolBar getCoolBar() {
		return coolBar;
	}

	public SQLSourceViewer getFSourceViewer() {
		return fSourceViewer;
	}

	public Combo getSelectCombo() {
		return selectCombo;
	}

	public boolean isLockedDataBase() {
		return lockDataBaseAction.isChecked();
	}

	public void setLockedDataBase(boolean isLocked) {
		lockDataBaseAction.setChecked(isLocked);
	}

}
