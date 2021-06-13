/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors;

public interface IPageChangeListener {

	public static final int EVT_MOVE_TOP = 101;

	public static final int EVT_MOVE_PREVIOUS = 102;

	public static final int EVT_MOVE_NEXT = 103;

	public static final int EVT_MOVE_END = 104;

	public void pageChanged(int status, int offset, int limit);

}
