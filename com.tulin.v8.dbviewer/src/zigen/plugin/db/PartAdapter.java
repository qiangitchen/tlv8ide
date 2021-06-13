/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

public class PartAdapter implements IPartListener {

	public void partActivated(IWorkbenchPart part) {
	// System.out.println("partActivated:" + part.getClass().getName());
	}

	public void partBroughtToTop(IWorkbenchPart part) {
	// System.out.println("partBroughtToTop:" + part.getClass().getName());

	}

	public void partClosed(IWorkbenchPart part) {
	// System.out.println("partClosed:" + part.getClass().getName());
	}

	public void partDeactivated(IWorkbenchPart part) {
	// System.out.println("partDeactivated:" + part.getClass().getName());

	}

	public void partOpened(IWorkbenchPart part) {
	// System.out.println("partOpened:" + part.getClass().getName());

	}

}
