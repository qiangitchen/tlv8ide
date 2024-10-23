/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.SQLHistoryManager;
import zigen.plugin.db.ui.internal.History;

public class HistoryViewTableFilter extends ViewerFilter {

	SQLHistoryManager mgr = DbPlugin.getDefault().getHistoryManager();

	protected String text;

	public HistoryViewTableFilter(String text) {
		this.text = text.toLowerCase();
	}

	public boolean select(Viewer viewer, Object parent, Object node) {
		if (text != null && !"".equals(text)) { //$NON-NLS-1$
			if (node instanceof History) {
				History his = (History) node;
				// String sql = his.getSqlHistory().getSql();
				String sql = mgr.loadContents(his.getSqlHistory());
				if (sql.toLowerCase().indexOf(text) >= 0) {
					return true;
				} else {
					return false;
				}
			}
		}
		return true;

	}
}
