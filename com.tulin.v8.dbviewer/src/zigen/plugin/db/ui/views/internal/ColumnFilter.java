/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import zigen.plugin.db.core.PatternUtil;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.OracleColumn;

public class ColumnFilter extends ViewerFilter {

	boolean caseSensitive = false;

	String text;

	public ColumnFilter(String text) {
		this(text, false);
	}

	public ColumnFilter(String text, boolean canSensitive) {
		this.text = text;
		this.caseSensitive = canSensitive;
	}

	public boolean select(Viewer viewer, Object parent, Object element) {
		if (text != null && !"".equals(text)) { //$NON-NLS-1$
			if (element instanceof Column) {
				try {
					Column col = (Column) element;
					Pattern pattern = PatternUtil.getPattern(text, caseSensitive);
					Matcher mc = pattern.matcher(col.getName());
					Matcher mc2 = pattern.matcher(col.getRemarks());
					return mc.matches() || mc2.matches();
				} catch (PatternSyntaxException e) {
					return false;
				}

			} else if (element instanceof OracleColumn) {
				try {
					OracleColumn col = (OracleColumn) element;
					Pattern pattern = PatternUtil.getPattern(text, caseSensitive);
					Matcher mc = pattern.matcher(col.getName());
					Matcher mc2 = pattern.matcher(col.getColumn().getRemarks());
					return mc.matches() || mc2.matches();
				} catch (PatternSyntaxException e) {
					return false;
				}
			} else {
				return true;
			}

		} else {
			return true;
		}
	}

}
