/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

import zigen.plugin.db.DbPlugin;

public class DiffTreeSelectionHandler implements ISelectionChangedListener {

	private DDLDiffEditor editor;

	public DiffTreeSelectionHandler(DDLDiffEditor editor) {
		this.editor = editor;

	}

	public void selectionChanged(SelectionChangedEvent event) {

		try {
			ISelection selection = event.getSelection();

			if (selection instanceof StructuredSelection) {
				Object element = ((StructuredSelection) selection).getFirstElement();

				if (element instanceof IDDLDiff) {
					IDDLDiff diff = (IDDLDiff) element;
					editor.getDiffviewer().setInput(diff);

				}

			}
		} catch (Exception e) {
			DbPlugin.log(e);
		}
	}
}
