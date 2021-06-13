/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import zigen.plugin.db.ui.editors.sql.IPropertyPageChangeListener;

public class PropertyPageChangeListener {

	public static final int EVT_SetDataBase = 101;

	private static List listeners = new ArrayList();

	public static void addPropertyPageChangeListener(IPropertyPageChangeListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	public static void removePropertyPageChangeListener(IPropertyPageChangeListener listener) {
		listeners.remove(listener);
	}

	public static void firePropertyPageChangeListener(Object obj, int status) {
		for (Iterator iter = listeners.iterator(); iter.hasNext();) {
			IPropertyPageChangeListener element = (IPropertyPageChangeListener) iter.next();
			if (element != null) {
				element.propertyPageChanged(obj, status);
			}
		}

	}

}
