/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db;

import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;

public class WindowAdapter implements IWindowListener {

	public void windowActivated(IWorkbenchWindow window) {
	}

	public void windowClosed(IWorkbenchWindow window) {
		//System.out.println(window.getClass().getName());
	}

	public void windowDeactivated(IWorkbenchWindow window) {
	}

	public void windowOpened(IWorkbenchWindow window) {
	}


}
