package tern.eclipse.ide.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;


/**
 * Tern nature adapter, can be used by adopters to provide more
 * advanced logic for which projects should be treated as tern projects
 */
public interface ITernNatureCapability {

	boolean hasTernNature( IProject project ) throws CoreException;

}
