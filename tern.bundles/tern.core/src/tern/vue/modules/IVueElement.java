package tern.vue.modules;

import tern.server.protocol.outline.IJSNode;
import tern.vue.VueType;

public interface IVueElement extends IJSNode {

	IModule getModule();

	boolean isType(VueType type);

	VueType getVueType();

}
