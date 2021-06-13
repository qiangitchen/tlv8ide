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
import zigen.plugin.db.PluginSettingsManager;
import zigen.plugin.db.ui.actions.SQLSourceViewerAction;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

public class FormatModeAction extends SQLSourceViewerAction implements IMenuCreator {

	protected PluginSettingsManager pluginMgr = DbPlugin.getDefault().getPluginSettingsManager();

	public static final String TEXT_MANUAL = Messages.getString("FormatModeAction.0"); //$NON-NLS-1$

	public static final String TEXT_AUTO = Messages.getString("FormatModeAction.1"); //$NON-NLS-1$

	Menu fMenu;

	Action autoAction;

	Action manualAction;

	boolean isAutoFormat = false;

	// SQLSourceViewer fSQLSourceViewer;

	public FormatModeAction(SQLSourceViewer viewer) {
		super(viewer, "Format Mode", Action.AS_DROP_DOWN_MENU); //$NON-NLS-1$
		setMenuCreator(this);
		this.isAutoFormat = getLastAutoFormatMode();

		if (viewer == null) {
			setFormatMode(isAutoFormat);
		}

	}

	public void setSQLSourceViewer(SQLSourceViewer viewer) {
		fSQLSourceViewer = viewer;
		setFormatMode(isAutoFormat);
	}

	public void run() {
		// DO format
		// formatSqlAction.run();
		fSQLSourceViewer.doOperation(ISQLOperationTarget.FORMAT);
	}

	public void dispose() {}

	public Menu getMenu(final Control parent) {
		fMenu = new Menu(parent);

		autoAction = new Action(TEXT_AUTO, IAction.AS_CHECK_BOX) { //$NON-NLS-1$

			public void run() {
				if (!isAutoFormat) {
					isAutoFormat = !isAutoFormat;
					setFormatMode(isAutoFormat);
					// autoCommitSelectHandler();
					// formatSqlAction.run();
				}
			}
		};
		autoAction.setChecked(isAutoFormat);
		autoAction.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_FORMAT2));

		addActionToMenu(fMenu, autoAction);
		manualAction = new Action(TEXT_MANUAL, IAction.AS_CHECK_BOX) { //$NON-NLS-1$

			public void run() {
				if (isAutoFormat) {
					isAutoFormat = !isAutoFormat;
					setFormatMode(isAutoFormat);
					// autoCommitSelectHandler();
					// formatSqlAction.run();
				}
			}
		};
		manualAction.setChecked(!isAutoFormat);
		manualAction.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_FORMAT));
		addActionToMenu(fMenu, manualAction);

		return fMenu;
	}

	public Menu getMenu(Menu parent) {
		return null;
	}

	protected void addActionToMenu(Menu parent, IAction action) {
		ActionContributionItem item = new ActionContributionItem(action);
		item.fill(parent, -1);
	}

	public final void setFormatMode(boolean b) {
		this.isAutoFormat = b;
		if (!isAutoFormat) {
			setToolTipText(TEXT_MANUAL); //$NON-NLS-1$
			setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_FORMAT));
		} else {
			setToolTipText(TEXT_AUTO); //$NON-NLS-1$
			setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_FORMAT2));
		}
		if (fSQLSourceViewer != null)
			fSQLSourceViewer.setFormatPreExecute(b);

		setLastAutoFormatMode(b);
	}

	public boolean isAutoFormat() {
		return isAutoFormat;
	}

	protected boolean getLastAutoFormatMode() {
		Object obj = pluginMgr.getValue(PluginSettingsManager.KEY_AUTO_FORMAT);
		if (obj != null) {
			return ((Boolean) obj).booleanValue();
		} else {
			return false;
		}
	}

	protected void setLastAutoFormatMode(boolean auto) {
		pluginMgr.setValue(PluginSettingsManager.KEY_AUTO_FORMAT, new Boolean(auto));
	}
}
