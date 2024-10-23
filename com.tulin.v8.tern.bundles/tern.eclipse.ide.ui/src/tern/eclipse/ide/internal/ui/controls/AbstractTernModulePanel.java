package tern.eclipse.ide.internal.ui.controls;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import tern.server.ITernModule;

/**
 * Display information of tern module.
 *
 */
public abstract class AbstractTernModulePanel extends Composite {

	public AbstractTernModulePanel(Composite parent, ITernModule module,
			IProject project) {
		super(parent, SWT.NONE);
		createUI(this, module, project);
	}

	protected abstract void createUI(Composite parent, ITernModule module,
			IProject project);

}
