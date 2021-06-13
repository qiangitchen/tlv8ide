/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.jface.text.IDocument;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.editors.ITableViewEditor;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;

public class TableEditorLogUtil {

	private static final String LINE_SEP = System.getProperty("line.separator"); //$NON-NLS-1$

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$

	public static void successLog(String str) {

		StringBuffer log = new StringBuffer();
		log.append(getSystemDate());
		log.append(" [OK] "); //$NON-NLS-1$
		log.append(str);

		write(log.toString());

	}

	public static void failureLog(String str) {

		StringBuffer log = new StringBuffer();
		log.append(getSystemDate());
		log.append(" [NG] "); //$NON-NLS-1$
		log.append(str);

		write(log.toString());

	}

	public static void debugLog(String str) {

		StringBuffer log = new StringBuffer();
		log.append(getSystemDate());
		log.append(" [DEBUG] "); //$NON-NLS-1$
		log.append(str);

		write(log.toString());

	}

	private static void write(String log) {
		ITableViewEditor editor = DbPlugin.getActiveTableViewEditor();
		if (editor != null && editor instanceof TableViewEditorFor31) {
			IDocument doc = ((TableViewEditorFor31) editor).getLogViewer().getDocument();
			if (doc != null) {
				String pre = doc.get();
				if (pre.length() == 0) {
					doc.set(log);
				} else {
					doc.set(pre + LINE_SEP + log);
				}
			}
		}
	}

	private static String getSystemDate() {
		Calendar cal = Calendar.getInstance();
		return format.format(cal.getTime());
	}

}
