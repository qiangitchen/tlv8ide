/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.tablespace;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ext.oracle.tablespace.wizard.CalcTableSpaceWizard;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.views.TableTypeSearchAction;

public class CalcTableSpaceWizardAction extends Action {

	private TreeViewer viewer = null;

	public CalcTableSpaceWizardAction(TreeViewer viewer) {
		// this.selection = selection;
		this.viewer = viewer;
		this.setText(Messages.getString("CalcTableSpaceWizardAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("CalcTableSpaceWizardAction.1")); //$NON-NLS-1$
	}

	public void run() {
		Object element = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
		if (element instanceof Schema) {
			Schema schema = (Schema) element;
			if (!schema.isExpanded()) {
				schema.setExpanded(true);

				Display display = viewer.getControl().getDisplay();
				display.syncExec(new TableTypeSearchAction(viewer, schema));

			}

		}
		Shell shell = DbPlugin.getDefault().getShell();
		CalcTableSpaceWizard wizard = new CalcTableSpaceWizard(viewer.getSelection());
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.open();
	}

}
