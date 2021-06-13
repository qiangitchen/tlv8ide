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
import zigen.plugin.db.ui.internal.TreeLeaf;

public class ElementFilter extends ViewerFilter {

	boolean regularExpressions = false;

	boolean caseSensitive = false;

	String text;

	Pattern pattern = null;

	public ElementFilter(String text, boolean regularExpressions, boolean canSensitive) {
		this.text = text;
		this.caseSensitive = canSensitive;
		createPattern();
	}

	private void createPattern() {
		try {
			if (regularExpressions) {
				if (!caseSensitive) {
					pattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE);
				} else {
					pattern = Pattern.compile(text);
				}
			} else {
				pattern = PatternUtil.getPattern(text, caseSensitive);
			}
		} catch (PatternSyntaxException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public boolean select(Viewer viewer, Object parent, Object element) {
		if (text != null && !"".equals(text)) { //$NON-NLS-1$
			if (element instanceof TreeLeaf) {
				TreeLeaf leaf = (TreeLeaf) element;
				if (pattern != null) {
					Matcher mc = pattern.matcher(leaf.getName());
					return !mc.matches();
				} else {
					return true;
				}

			} else {
				return true;
			}

		} else {
			return true;
		}
	}
}
