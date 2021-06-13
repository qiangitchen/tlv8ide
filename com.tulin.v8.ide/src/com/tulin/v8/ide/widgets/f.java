package com.tulin.v8.ide.widgets;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;

class f implements ControlListener {

	private JSLibraryConfigComposite a;

	f(JSLibraryConfigComposite paramJSLibraryConfigComposite) {
		this.a = paramJSLibraryConfigComposite;
	}

	public void controlResized(ControlEvent paramControlEvent) {
		this.a.dsTable.setSize(this.a.tabFolder.getBounds().width - 8,
				this.a.tabFolder.getBounds().height - 23);
		this.a.mdsTable.setSize(this.a.tabFolder.getBounds().width - 8,
				this.a.tabFolder.getBounds().height - 23);
	}

	public void controlMoved(ControlEvent paramControlEvent) {
	}
}