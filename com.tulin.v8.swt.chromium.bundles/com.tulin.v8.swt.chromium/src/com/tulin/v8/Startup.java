package com.tulin.v8;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;

import com.tulin.v8.cef.CefBrowserManager;

public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
		Display.getDefault().asyncExec(() -> {
			try {
				CefBrowserManager.init();
			} catch (Exception | Error e) {
				e.printStackTrace();
			}
		});
	}

}
