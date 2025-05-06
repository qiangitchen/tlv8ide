package tern.scriptpath.impl;

import java.util.Collections;
import java.util.List;

import tern.ITernFile;
import tern.ITernProject;
import tern.scriptpath.ITernScriptResource;

/**
 * HTML/JSP page script path. This script path implementation gives the
 * capability to select an js file with "Add File" in the Tern "Script Path"
 * property page project.
 * 
 */
public class JSFileScriptPath extends AbstractTernFileScriptPath {

	private final List<ITernScriptResource> resources;

	public JSFileScriptPath(ITernProject project, ITernFile file, String external) {
		super(project, file, external);
		this.resources = Collections.<ITernScriptResource>singletonList(
				new JSFileScriptResource(project, file));
	}

	@Override
	public List<ITernScriptResource> getScriptResources() {
		return resources;
	}
	
}
