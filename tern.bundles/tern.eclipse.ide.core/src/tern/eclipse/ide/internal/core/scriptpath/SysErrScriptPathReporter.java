package tern.eclipse.ide.internal.core.scriptpath;

import org.eclipse.core.runtime.IPath;

import tern.eclipse.ide.core.IIDETernScriptPathReporter;

/**
 * Display in the console error trace of tern script path scope.
 *
 */
public class SysErrScriptPathReporter implements IIDETernScriptPathReporter {

	public static final IIDETernScriptPathReporter INSTANCE = new SysErrScriptPathReporter();

	@Override
	public void report(IPath path, IIDETernScriptPath scriptPath, String message, boolean include) {
		err(include ? "Include " : "Exclude ", path, scriptPath, message);
	}

	private void err(String prefix, IPath path, IIDETernScriptPath scriptPath, String message) {
		System.err.println(prefix + path.toString() + " by " + scriptPath + " (" + message + ")");
	}

	@Override
	public void validate(IPath path, boolean validate) {
		System.err.println((validate ? "Do" : "Ignore") + (" validation for " + path));
	}

}
