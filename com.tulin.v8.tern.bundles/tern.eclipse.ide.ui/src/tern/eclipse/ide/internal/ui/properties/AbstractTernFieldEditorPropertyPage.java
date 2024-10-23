package tern.eclipse.ide.internal.ui.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPropertyPage;

import tern.eclipse.ide.core.IIDETernProject;
import tern.eclipse.ide.core.TernCorePlugin;

public abstract class AbstractTernFieldEditorPropertyPage extends
		FieldEditorPreferencePage implements IWorkbenchPropertyPage {

	private IAdaptable element;

	public AbstractTernFieldEditorPropertyPage() {
		super();
	}

	protected AbstractTernFieldEditorPropertyPage(int style) {
		super(style);
	}

	protected AbstractTernFieldEditorPropertyPage(String title, int style) {
		super(title, style);
	}

	protected AbstractTernFieldEditorPropertyPage(String title,
			ImageDescriptor image, int style) {
		super(title, image, style);
	}

	public IAdaptable getElement() {
		return this.element;
	}

	public void setElement(IAdaptable element) {
		this.element = element;
	}

	public IIDETernProject getTernProject() throws CoreException {
		return TernCorePlugin.getTernProject(getResource().getProject());
	}

	private IResource getResource() {
		IResource resource = null;
		IAdaptable adaptable = getElement();
		if (adaptable instanceof IResource) {
			resource = (IResource) adaptable;
		} else if (adaptable != null) {
			Object o = adaptable.getAdapter(IResource.class);
			if (o instanceof IResource) {
				resource = (IResource) o;
			}
		}
		return resource;
	}

}
