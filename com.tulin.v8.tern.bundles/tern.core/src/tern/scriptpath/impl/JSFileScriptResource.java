package tern.scriptpath.impl;

import tern.ITernFile;
import tern.ITernProject;
import tern.scriptpath.ITernScriptResource;

/**
 * Script resources linked to a JS file.
 */
public class JSFileScriptResource implements ITernScriptResource {

	private final ITernProject project;
	private final ITernFile file;

	public JSFileScriptResource(ITernProject project, ITernFile file) {
		this.project = project;
		this.file = file;
	}

	@Override
	public ITernFile getFile() {
		return file;
	}

	@Override
	public String getLabel() {
		return file.getFileName() + " - " + file.getFullName(project); //$NON-NLS-1$
	}

}
