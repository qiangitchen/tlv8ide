/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.event;

@SuppressWarnings({"rawtypes","unchecked"})
public class PasteRecordMonitor {

	private static final ThreadLocal session = new ThreadLocal();

	public static void begin() {
		Boolean b = true;
		session.set(b);
	}

	public static void end() {
		Boolean b = (Boolean) session.get();
		if (b != null) {
			session.set(null);
		} 
	}

	public static boolean isPasting() {
		Boolean bool = (Boolean) session.get();
		if (bool == null) {
			return false;
		} else {
			return true;
		}
	}

}
