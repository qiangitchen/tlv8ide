package tern.eclipse.ide.core;

import tern.server.ITernModule;

/**
 * Working copy listener to observe changes of add/remove checked tern modules.
 * 
 */
public interface IWorkingCopyListener {

	/**
	 * This method is call when tern modules id checked/unchecked for tern
	 * project.
	 * 
	 * @param module
	 *            the module which was added/removed.
	 * @param selected
	 *            true if module was checked and false otherwise.
	 */
	void moduleSelectionChanged(ITernModule module, boolean selected);

}
