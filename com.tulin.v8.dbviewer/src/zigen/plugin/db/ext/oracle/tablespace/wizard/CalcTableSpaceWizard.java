/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.tablespace.wizard;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.Wizard;

public class CalcTableSpaceWizard extends Wizard {

	private WizardPage1 page1;

	private WizardPage2 page2;

	private WizardPage3 page3;

	private ISelection selection;

	public CalcTableSpaceWizard(ISelection selection) {
		super();
		this.selection = selection;
		setNeedsProgressMonitor(true);
	}

	public void addPages() {
		page1 = new WizardPage1(selection);
		page2 = new WizardPage2();
		page3 = new WizardPage3();

		addPage(page1);
		addPage(page2);
		addPage(page3);

	}

	public boolean performFinish() {
		return true;
	}

	public boolean canFinish() {
		if (page1.isPageComplete() && page2.isPageComplete() && page3.isPageComplete()) {
			return true;
		} else {
			return false;
		}
	}

}
