package tern.eclipse.ide.core;

import org.eclipse.core.runtime.IPath;

import tern.eclipse.ide.internal.core.scriptpath.IIDETernScriptPath;

/**
 * Tern script path reporter.
 *
 */
public interface IIDETernScriptPathReporter {

	void report(IPath path, IIDETernScriptPath scriptPath, String message, boolean include);

	void validate(IPath fullPath, boolean validate);

}
