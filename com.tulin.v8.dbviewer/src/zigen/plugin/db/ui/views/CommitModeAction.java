/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ui.actions.SQLSourceViewerAction;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

public class CommitModeAction extends SQLSourceViewerAction implements IMenuCreator {

	Menu fMenu;

	Action autoAction;

	Action manualAction;

	boolean isAutoCommit = false;

	IDBConfig config;

	public CommitModeAction(SQLSourceViewer viewer) {
		super(viewer, "Commit Mode", Action.AS_DROP_DOWN_MENU); //$NON-NLS-1$
		setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_AUTO));

		setMenuCreator(this);

		if (viewer != null) {
			this.config = viewer.getDbConfig();
			setCommitMode((config == null) ? false : config.isAutoCommit());
		}
	}

	// override
	public void setSQLSourceViewer(SQLSourceViewer viewer) {
		this.fSQLSourceViewer = viewer;
		if (viewer != null) {
			this.config = viewer.getDbConfig();
			setCommitMode((config == null) ? false : config.isAutoCommit());
		}
	}

	public void run() {
		isAutoCommit = !isAutoCommit;
		setCommitMode(isAutoCommit);
		autoCommitSelectHandler();
	}

	public void dispose() {}

	public Menu getMenu(final Control parent) {
		fMenu = new Menu(parent);

		autoAction = new Action(Messages.getString("AbstractSQLExecuteView.9"), IAction.AS_CHECK_BOX) { //$NON-NLS-1$

			public void run() {
				if (!isAutoCommit) {
					isAutoCommit = !isAutoCommit;
					setCommitMode(isAutoCommit);
					autoCommitSelectHandler();
				}
			}
		};
		autoAction.setChecked(isAutoCommit);
		autoAction.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_AUTO));

		addActionToMenu(fMenu, autoAction);
		manualAction = new Action(Messages.getString("AbstractSQLExecuteView.10"), IAction.AS_CHECK_BOX) { //$NON-NLS-1$

			public void run() {
				if (isAutoCommit) {
					isAutoCommit = !isAutoCommit;
					setCommitMode(isAutoCommit);
					autoCommitSelectHandler();
				}
			}
		};
		manualAction.setChecked(!isAutoCommit);
		manualAction.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_MANUAL));
		addActionToMenu(fMenu, manualAction);

		return fMenu;
	}

	private void autoCommitSelectHandler() {
		if (isAutoCommit()) {
			Transaction trans = Transaction.getInstance(config);
			if (trans == null || trans.getTransactionCount() == 0) {
				setCommitMode(true);
			} else {
				DbPlugin.getDefault().showWarningMessage(Messages.getString("SQLExecuteView.7")); //$NON-NLS-1$
				setCommitMode(false);
			}
		} else {
			setCommitMode(false);
		}
		DbPlugin.fireStatusChangeListener(this, IStatusChangeListener.EVT_ChangeTransactionMode);
	}


	public Menu getMenu(Menu parent) {
		return null;
	}

	protected void addActionToMenu(Menu parent, IAction action) {
		ActionContributionItem item = new ActionContributionItem(action);
		item.fill(parent, -1);
	}

	public final void setCommitMode(boolean _isAutoCommit) {
		this.isAutoCommit = _isAutoCommit;
		if (_isAutoCommit) {
			setToolTipText(Messages.getString("AbstractSQLExecuteView.11")); //$NON-NLS-1$
			setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_AUTO));
		} else {
			setToolTipText(Messages.getString("AbstractSQLExecuteView.12")); //$NON-NLS-1$
			setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_MANUAL));
		}

		if (config != null) {
			config.setAutoCommit(_isAutoCommit);
			DBConfigManager.setAutoCommit(config, _isAutoCommit);
		}

	}

	public boolean isAutoCommit() {
		return isAutoCommit;
	}

	public IDBConfig getDbConfig() {
		return config;
	}

}
