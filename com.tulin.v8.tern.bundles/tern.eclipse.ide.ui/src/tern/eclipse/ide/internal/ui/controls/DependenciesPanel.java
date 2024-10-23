package tern.eclipse.ide.internal.ui.controls;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Composite;

import tern.server.ITernModule;

/**
 * Content of the "Dependencies" Tab of tern module selection.
 *
 */
public class DependenciesPanel extends AbstractPanel {

	public DependenciesPanel(Composite parent, IProject project) {
		super(parent, project);
	}

	@Override
	protected void createEmptyBodyContent(Composite parent) {

	}

	@Override
	protected Composite createContent(Composite parent, ITernModule module,
			IProject project) {
		return new TernModuleDependenciesPanel(parent, module, project);
	}
}
