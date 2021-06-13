package com.tulin.v8.ide;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;

public class StudioStartup implements IStartup {
	public void earlyStartup() {
		Display.getDefault().syncExec(new e(this));
		Display.getDefault().syncExec(new d(this));
	}
}
