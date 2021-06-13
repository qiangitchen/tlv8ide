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
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.OracleSequence;
import zigen.plugin.db.ui.internal.OracleSource;
import zigen.plugin.db.ui.internal.Synonym;

public class TableFilter extends ViewerFilter {

	boolean caseSensitive = false;

	String text;

	Pattern pattern;

	public TableFilter(String text) {
		this(text, false);
	}

	public TableFilter(String text, boolean canSensitive) {
		this.text = text;
		this.caseSensitive = canSensitive;
		createPattern();
	}

	private void createPattern() {
		try {
			pattern = PatternUtil.getPattern(text, caseSensitive);
		} catch (PatternSyntaxException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public boolean select(Viewer viewer, Object parent, Object element) {
		if (text != null && !"".equals(text)) { //$NON-NLS-1$
			if (element instanceof Bookmark) {
				return true;

			} else if (element instanceof ITable) {
				try {
					ITable table = (ITable) element;
					Matcher mc = pattern.matcher(table.getName());
					Matcher mc2 = pattern.matcher(table.getRemarks());
					return mc.matches() || mc2.matches();
				} catch (PatternSyntaxException e) {
					return false;
				}

			} else if (element instanceof Synonym) {

				try {
					Synonym synonym = (Synonym) element;
					Matcher mc = pattern.matcher(synonym.getName());
					Matcher mc2 = pattern.matcher(synonym.getRemarks());
					return mc.matches() || mc2.matches();
				} catch (PatternSyntaxException e) {
					return false;
				}

			} else if (element instanceof OracleSequence) {

				try {
					OracleSequence seq = (OracleSequence) element;
					Matcher mc = pattern.matcher(seq.getName());
					return mc.matches();
				} catch (PatternSyntaxException e) {
					return false;
				}

			} else if (element instanceof OracleSource) {
				try {
					OracleSource source = (OracleSource) element;
					Matcher mc = pattern.matcher(source.getName());
					return mc.matches();
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
