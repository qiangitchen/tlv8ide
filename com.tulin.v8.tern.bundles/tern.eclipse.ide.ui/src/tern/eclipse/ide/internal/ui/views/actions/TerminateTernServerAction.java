package tern.eclipse.ide.internal.ui.views.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;

import tern.eclipse.ide.core.IIDETernProject;
import tern.eclipse.ide.core.TernCorePlugin;
import tern.eclipse.ide.internal.ui.TernUIMessages;
import tern.eclipse.ide.internal.ui.Trace;
import tern.eclipse.ide.ui.ImageResource;
import tern.eclipse.ide.ui.views.AbstractTernContentOutlinePage;

/**
 * This action stops the tern server of the current tern project.
 * 
 */
public class TerminateTernServerAction extends Action {

	private final AbstractTernContentOutlinePage page;

	public TerminateTernServerAction(AbstractTernContentOutlinePage page) {
		this.page = page;
		super.setText(TernUIMessages.TerminateTernServerAction_text);
		super.setToolTipText(TernUIMessages.TerminateTernServerAction_tooltip);
		super.setImageDescriptor(ImageResource.getImageDescriptor(ImageResource.IMG_STOP_ENABLED));
	}

	@Override
	public void run() {
		try {
			IProject project = page.getProject();
			IIDETernProject ternProject = TernCorePlugin.getTernProject(project);
			if (ternProject != null) {
				ternProject.disposeServer();
			}
		} catch (CoreException e) {
			Trace.trace(Trace.SEVERE, "Error while getting tern project.", e);
		}
	}
}
