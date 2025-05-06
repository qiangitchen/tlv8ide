package tern.eclipse.ide.server.nodejs.core.debugger.launchConfigurations;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

import tern.TernException;
import tern.eclipse.ide.server.nodejs.core.TernNodejsCorePlugin;

/**
 * Protractor launch configuration delegate.
 *
 */
public abstract class AbstractNodejsCliFileLaunchConfigurationDelegate extends LaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		if (monitor.isCanceled()) {
			return;
		}

		try {
			createLauncher(configuration, mode).start();
		} catch (TernException e) {
			throw new CoreException(
					new Status(IStatus.ERROR, TernNodejsCorePlugin.PLUGIN_ID, "Error while launching client file", e));
		}
	}

	protected abstract AbstractNodejsCliFileLauncher createLauncher(ILaunchConfiguration configuration, String mode)
			throws NodejsCliFileConfigException, CoreException;

}
