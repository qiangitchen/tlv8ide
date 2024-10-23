package tern.eclipse.ide.internal.ui.console;

import org.eclipse.jface.action.Action;
import org.eclipse.osgi.util.NLS;

import tern.eclipse.ide.core.IIDETernProject;
import tern.eclipse.ide.internal.ui.TernUIMessages;
import tern.eclipse.ide.ui.ImageResource;
import tern.server.ITernServer;
import tern.server.ITernServerListener;

/**
 * Stop tern Server action.
 * 
 */
public class ConsoleTerminateAction extends Action implements
		ITernServerListener {

	private final IIDETernProject project;

	public ConsoleTerminateAction(IIDETernProject project) {
		this.project = project;
		setToolTipText(NLS.bind(
				TernUIMessages.ConsoleTerminateAction_tooltipText, project
						.getProject().getName()));
		setImageDescriptor(ImageResource
				.getImageDescriptor(ImageResource.IMG_STOP_ENABLED));
		setDisabledImageDescriptor(ImageResource
				.getImageDescriptor(ImageResource.IMG_STOP_DISABLED));
		setHoverImageDescriptor(ImageResource
				.getImageDescriptor(ImageResource.IMG_STOP_ENABLED));
		project.addServerListener(this);
	}

	@Override
	public void run() {
		try {
			if (project.isServerDisposed()) {
				this.setEnabled(false);
			} else {
				project.disposeServer();
			}
		} catch (Throwable e) {
		}
	}

	public void dispose() {
		project.removeServerListener(this);
	}

	@Override
	public void onStart(ITernServer server) {
		this.setEnabled(true);
	}

	@Override
	public void onStop(ITernServer server) {
		this.setEnabled(false);
	}

}
