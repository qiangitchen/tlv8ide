/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import zigen.plugin.db.DbPlugin;

public abstract class AbstractToggleLinkingAction extends Action {

	public AbstractToggleLinkingAction() {
		super(Messages.getString("AbstractToggleLinkingAction.1"), IAction.AS_CHECK_BOX); //$NON-NLS-1$
		setToolTipText(Messages.getString("AbstractToggleLinkingAction.1")); //$NON-NLS-1$
		setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_SYNCED));
	}

	public abstract void run();
}
