package tern.eclipse.ide.core;

import java.util.Collection;

import org.eclipse.core.resources.IProject;

/**
 * Default tern modules provider.
 *
 */
public interface IDefaultTernModulesProvider {

	/**
	 * Returns list of tern modules names which must be added by default when
	 * the given project is converted to a tern project.
	 * 
	 * @param project
	 * @return list of tern modules names which must be added by default when
	 *         the given project is converted to a tern project.
	 */
	Collection<DefaultTernModule> getTernModules(IProject project);
}
