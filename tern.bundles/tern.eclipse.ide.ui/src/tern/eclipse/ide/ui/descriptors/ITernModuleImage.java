package tern.eclipse.ide.ui.descriptors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * Tern module image API.
 */
public interface ITernModuleImage {

	/**
	 * Returns the module name.
	 * 
	 * @return
	 */
	String getId();

	/**
	 * Returns the image of the module.
	 * 
	 * @return the image of the module.
	 */
	Image getImage();

	/**
	 * Returns the image descriptor of the module.
	 * 
	 * @return the image descriptor of the module.
	 */
	ImageDescriptor getImageDescriptor();

}
