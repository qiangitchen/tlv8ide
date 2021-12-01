/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.widgets.Display;

public class ClipboardUtils {

	private static Clipboard clipboard;

	public static Clipboard getInstance() {
		if (clipboard == null) {
			clipboard = new Clipboard(Display.getCurrent());
		}
		return clipboard;
	}
}
