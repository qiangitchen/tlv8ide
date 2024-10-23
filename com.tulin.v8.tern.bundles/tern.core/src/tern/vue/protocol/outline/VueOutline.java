package tern.vue.protocol.outline;

import tern.ITernProject;
import tern.server.protocol.outline.JSNodeRoot;

/**
 * Vue model which hosts modules, directives, controllers of the Vue
 * application.
 * 
 */
public class VueOutline extends JSNodeRoot {

	public static final String vue_MODEL_CHANGED_EVENT = "vue:modelChanged";

	private static final String ROOT = "#Vue";

	public VueOutline(ITernProject ternProject) {
		super(ROOT, ternProject);
	}

}
