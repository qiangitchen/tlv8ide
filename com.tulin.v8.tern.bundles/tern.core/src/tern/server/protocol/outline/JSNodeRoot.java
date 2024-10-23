package tern.server.protocol.outline;

import tern.ITernProject;

/**
 * JavaScript node root.
 *
 */
public class JSNodeRoot extends JSNode implements IJSNodeRoot {

	private static final String ROOT = "#Root";

	private final ITernProject ternProject;

	public JSNodeRoot(ITernProject ternProject) {
		this(ROOT, ternProject);
	}

	public JSNodeRoot(String name, ITernProject ternProject) {
		super(name, null, null, null, null, null, null, null);
		this.ternProject = ternProject;
	}

	@Override
	public ITernProject getTernProject() {
		return ternProject;
	}

	public void clear() {
		getChildren().clear();
	}

}
