package tern.eclipse.ide.server.nodejs.core.debugger;

import org.eclipse.debug.core.DebugPlugin;

/**
 * Abstract class for {@link INodejsDebuggerDelegate}.
 *
 */
public abstract class AbstractNodejsDebuggerDelegate implements INodejsDebuggerDelegate {

	private final String launchConfigId;
	private final boolean canSupportDebug;

	public AbstractNodejsDebuggerDelegate(String launchConfigId, boolean canSupportDebug) {
		this.launchConfigId = launchConfigId;
		this.canSupportDebug = canSupportDebug;
	}

	public String getLaunchId() {
		return launchConfigId;
	}

	@Override
	public boolean canSupportDebug() {
		return canSupportDebug;
	}

	@Override
	public boolean isInstalled() {
		return DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurationType(launchConfigId) != null;
	}

}
