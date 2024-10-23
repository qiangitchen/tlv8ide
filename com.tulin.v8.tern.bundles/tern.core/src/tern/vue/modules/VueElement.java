package tern.vue.modules;

import tern.server.protocol.outline.BaseJSNode;
import tern.server.protocol.outline.IJSNode;
import tern.vue.VueType;

public class VueElement extends BaseJSNode implements IVueElement {

	private final VueType type;

	public VueElement(String name, VueType type, Long start, Long end, String file, IJSNode parent) {
		super(name, type.name(), null, start, end, file, parent);
		this.type = type;
	}

	@Override
	public boolean isType(VueType type) {
		return this.type.equals(type);
	}

	@Override
	public VueType getVueType() {
		return type;
	}

	@Override
	public IModule getModule() {
		return (IModule) getParent();
	}

}
