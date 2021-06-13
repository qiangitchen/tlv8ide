/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.IAction;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

public class LockColumnWidthAction extends SQLSourceViewerAction {

	public LockColumnWidthAction(SQLSourceViewer viewer) {
		super(viewer, Messages.getString("LockColumnWidthAction.0"), IAction.AS_CHECK_BOX); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("LockColumnWidthAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_PIN));
	}

	public void run() {
	// fSQLSourceViewer.setLockedDataBase(isChecked());
	}

	public void setChecked(boolean checked) {
		super.setChecked(checked);
	}

}
