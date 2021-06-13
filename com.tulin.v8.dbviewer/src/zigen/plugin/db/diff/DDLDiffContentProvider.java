/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

class DDLDiffContentProvider implements IMergeViewerContentProvider {

	private CompareConfiguration fCompareConfiguration;

	public DDLDiffContentProvider(CompareConfiguration cc) {
		fCompareConfiguration = cc;
	}

	public String getAncestorLabel(Object input) {
		return fCompareConfiguration.getAncestorLabel(input);
	}

	public Image getAncestorImage(Object input) {
		return fCompareConfiguration.getAncestorImage(input);
	}

	public Object getAncestorContent(Object input) {
		return null;
	}

	public boolean showAncestor(Object input) {
		return false;
	}

	public String getLeftLabel(Object input) {
		if (input == null)
			return Messages.getString("DDLDiffContentProvider.0"); //$NON-NLS-1$

//		if (input instanceof DDLDiff) {
		if (input instanceof IDDLDiff) {
			return ((IDDLDiff) input).getLeftDisplayedName();
		}

		return null;

	}

	public Image getLeftImage(Object input) {
		return fCompareConfiguration.getLeftImage(input);
	}

	public Object getLeftContent(Object input) {
		if (input instanceof IDDLDiff) {
			return new Document(((IDDLDiff) input).getLeftDDLString());
		}
		return null;
	}

	public boolean isLeftEditable(Object input) {
		return false;
	}

	public void saveLeftContent(Object input, byte[] bytes) {}

	public String getRightLabel(Object input) {
		if (input == null)
			return Messages.getString("DDLDiffContentProvider.1"); //$NON-NLS-1$

		if (input instanceof IDDLDiff) {
			return ((IDDLDiff) input).getRightDisplayedName();
		}
		return null;

	}

	public Image getRightImage(Object input) {
		return fCompareConfiguration.getRightImage(input);
	}

	public Object getRightContent(Object input) {
		if (input instanceof IDDLDiff) {
			return new Document(((IDDLDiff) input).getRightDDLString());
		}
		return null;
	}

	public boolean isRightEditable(Object input) {
		return false;
	}

	public void saveRightContent(Object input, byte[] bytes) {}

	public void dispose() {}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}
}
