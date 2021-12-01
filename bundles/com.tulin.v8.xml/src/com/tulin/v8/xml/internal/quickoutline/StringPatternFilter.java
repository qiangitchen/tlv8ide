package com.tulin.v8.xml.internal.quickoutline;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.osgi.util.TextProcessor;

import com.tulin.v8.xml.internal.filter.StringMatcher;

/**
 * Default Viewer Filter to be used by the {@link QuickOutlinePopupDialog}
 * <p>
 * Based on
 * </p>
 */
public class StringPatternFilter extends ViewerFilter {

	private StringMatcher fStringMatcher;

	/*
	 * @see
	 * org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers
	 * .Viewer, java.lang.Object, java.lang.Object)
	 */
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		StringMatcher matcher = fStringMatcher;
		if (matcher == null || !(viewer instanceof TreeViewer))
			return true;
		TreeViewer treeViewer = (TreeViewer) viewer;

		String matchName = ((ILabelProvider) treeViewer.getLabelProvider())
				.getText(element);
		matchName = TextProcessor.deprocess(matchName);
		if (matchName != null && matcher.match(matchName))
			return true;

		return hasUnfilteredChild(treeViewer, element);
	}

	private boolean hasUnfilteredChild(TreeViewer viewer, Object element) {
		Object[] children = ((ITreeContentProvider) viewer.getContentProvider())
				.getChildren(element);
		for (int i = 0; i < children.length; i++)
			if (select(viewer, element, children[i]))
				return true;
		return false;
	}

	public void updatePattern(String pattern) {
		if (pattern.length() == 0) {
			fStringMatcher = null;
		} else {
			fStringMatcher = new StringMatcher(pattern, pattern.toLowerCase()
					.equals(pattern), false);
		}

	}

	public StringMatcher getStringMatcher() {
		return fStringMatcher;
	}

}
