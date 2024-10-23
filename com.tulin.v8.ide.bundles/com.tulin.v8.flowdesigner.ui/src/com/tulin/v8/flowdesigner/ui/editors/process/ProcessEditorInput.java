package com.tulin.v8.flowdesigner.ui.editors.process;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.tulin.v8.flowdesigner.ui.editors.process.element.ProcessDrawElement;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ProcessEditorInput implements IEditorInput {
	private ProcessDrawElement element;

	public ProcessEditorInput(ProcessDrawElement element) {
		this.element = element;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return element.getSprocessname();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return getName();
	}

	public boolean equals(Object obj) {
		if (obj instanceof ProcessEditorInput)
			return this.element.getSid().equals(((ProcessEditorInput) obj).element.getSid());
		return false;
	}

	public boolean saveData(String sourceText) {
		element.setSprocessacty(sourceText);
		return element.saveData();
	}

	public ProcessDrawElement getElement() {
		return element;
	}

	public void setElement(ProcessDrawElement element) {
		this.element = element;
	}

}
