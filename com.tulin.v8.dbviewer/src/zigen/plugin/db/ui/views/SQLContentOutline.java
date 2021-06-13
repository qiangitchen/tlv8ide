/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.contentoutline.ContentOutline;

public class SQLContentOutline extends ContentOutline implements ISelectionProvider, ISelectionChangedListener {

	public SQLContentOutline() {
		super();
	}

	public Object getAdapter(Class key) {
		return super.getAdapter(key);
	}

	protected IWorkbenchPart getBootstrapPart() {
		IWorkbenchPage page = getSite().getPage();
		if (page != null) {
			// return page.getActiveEditor();
			return page.getActivePart();
		}
		return null;
	}

	protected boolean isImportant(IWorkbenchPart part) {
		// return (part instanceof IEditorPart);
		if (part instanceof IEditorPart) {
			return true;
		} else if (!(part instanceof SQLContentOutline) && (part instanceof IViewPart)) {
			return true;

		} else {
			return false;
		}

	}

}
