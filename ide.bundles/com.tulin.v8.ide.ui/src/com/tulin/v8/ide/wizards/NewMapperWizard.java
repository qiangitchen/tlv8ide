package com.tulin.v8.ide.wizards;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.ide.wizards.mapper.NewMapperPage;

/**
 * 新建Mapper相关向导
 * 
 * @author 陈乾
 *
 */
public class NewMapperWizard extends Wizard implements INewWizard {
	ISelection selection;
	NewMapperPage newMapperPage;

	public NewMapperWizard() {
		super();
		setNeedsProgressMonitor(true);
		setHelpAvailable(false);
	}

	public NewMapperWizard(ISelection selection) {
		super();
		this.selection = selection;
		setNeedsProgressMonitor(true);
		setHelpAvailable(false);
		setWindowTitle(Messages.getString("wizards.newmapper.title"));
		TuLinPlugin.setSelection(selection);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		setWindowTitle(Messages.getString("wizards.newmapper.title"));
		TuLinPlugin.setSelection(selection);
	}

	@Override
	public void addPages() {
		newMapperPage = new NewMapperPage(selection);
		addPage(newMapperPage);
	}

	@Override
	public boolean canFinish() {
		return newMapperPage.canFinish();
	}

	@Override
	public boolean performFinish() {
		try {
			newMapperPage.doFinish();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
