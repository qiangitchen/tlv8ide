package tern.eclipse.ide.core.resources;

import java.io.IOException;

import org.eclipse.core.resources.IFile;

import tern.ITernFile;
import tern.eclipse.ide.internal.core.resources.IDETernFile;

public class TernTextFile extends IDETernFile implements ITernFile {

	private String text;

	public TernTextFile(IFile file, String text) {
		super(file);
		this.text = text;
	}

	@Override
	public String getContents() throws IOException {
		return text;
	}

	@Override
	public String toString() {
		return super.toString() + " [DOCUMENT]"; //$NON-NLS-1$
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapterClass) {
		if (adapterClass == String.class) {
			return text;
		}
		return super.getAdapter(adapterClass);
	}

}
