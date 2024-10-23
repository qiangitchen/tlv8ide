package tern.eclipse.ide.server.nodejs.internal.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import tern.eclipse.ide.server.nodejs.core.TernNodejsCorePlugin;

/**
 * Helper class to route trace output.
 */
public class Trace {
	public static final byte CONFIG = 0;
	public static final byte INFO = 1;
	public static final byte WARNING = 2;
	public static final byte SEVERE = 3;
	public static final byte FINEST = 4;
	public static final byte FINER = 5;
	public static final byte PERFORMANCE = 6;
	public static final byte EXTENSION_POINT = 7;

	private static final String[] levelNames = new String[] { "CONFIG ",
			"INFO   ", "WARNING", "SEVERE ", "FINER  ", "FINEST ", "PERF   ",
			"EXTENSION" };

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"dd/MM/yy HH:mm.ss.SSS");

	private static Set<String> logged = new HashSet<String>();

	/**
	 * Trace constructor comment.
	 */
	private Trace() {
		super();
	}

	/**
	 * Trace the given text.
	 * 
	 * @param level
	 *            a trace level
	 * @param s
	 *            a message
	 */
	public static void trace(byte level, String s) {
		trace(level, s, null);
	}

	/**
	 * Trace the given message and exception.
	 * 
	 * @param level
	 *            a trace level
	 * @param s
	 *            a message
	 * @param t
	 *            a throwable
	 */
	public static void trace(byte level, String s, Throwable t) {
		if (s == null)
			return;

		if (level == SEVERE) {
			if (!logged.contains(s)) {
				TernNodejsCorePlugin
						.getDefault()
						.getLog()
						.log(new Status(IStatus.ERROR,
								TernNodejsCorePlugin.PLUGIN_ID, s, t));
				logged.add(s);
			}
		}

		if (!TernNodejsCorePlugin.getDefault().isDebugging())
			return;

		StringBuilder sb = new StringBuilder(TernNodejsCorePlugin.PLUGIN_ID);
		sb.append(" ");
		sb.append(levelNames[level]);
		sb.append(" ");
		sb.append(sdf.format(new Date()));
		sb.append(" ");
		sb.append(s);
		System.out.println(sb.toString());
		if (t != null)
			t.printStackTrace();
	}
}