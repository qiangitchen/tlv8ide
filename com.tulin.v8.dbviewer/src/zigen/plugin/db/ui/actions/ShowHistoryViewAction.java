/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.ui.views.HistoryView;

public class ShowHistoryViewAction extends Action {

	public ShowHistoryViewAction() {
		super(Messages.getString("ShowHistoryViewAction.0"), IAction.AS_CHECK_BOX); //$NON-NLS-1$

		this.setToolTipText(Messages.getString("ShowHistoryViewAction.0")); //$NON-NLS-1$
		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_CLOCK));
	}

	public void run() {
		try {
			if (isChecked()) {
				DbPlugin.showView(DbPluginConstant.VIEW_ID_HistoryView);
			} else {
				IViewPart part = DbPlugin.findView(DbPluginConstant.VIEW_ID_HistoryView);
				if (part instanceof HistoryView) {
					HistoryView view = (HistoryView) part;
					DbPlugin.hideView(view);
				}
			}
		} catch (PartInitException e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

}
