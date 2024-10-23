package tern.server.protocol.outline;

import tern.ITernProject;
import tern.server.protocol.IJSONObjectHelper;

public class TernOutlineCollector implements ITernOutlineCollector {

	private final ITernProject ternProject;
	private IJSNodeRoot root;

	public TernOutlineCollector(ITernProject ternProject) {
		this.ternProject = ternProject;
	}

	@Override
	public IJSNodeRoot createRoot() {
		this.root = doCreateRoot();
		return root;
	}

	protected IJSNodeRoot doCreateRoot() {
		return new JSNodeRoot(ternProject);
	}

	@Override
	public IJSNode createNode(String name, String type, String kind, String value, Long start, Long end, String file,
			IJSNode parent, Object jsonNode, IJSONObjectHelper helper) {
		return new JSNode(name, type, kind, value, start, end, file, parent);
	}

	public IJSNodeRoot getRoot() {
		return root;
	}

	public ITernProject getTernProject() {
		return ternProject;
	}

}
