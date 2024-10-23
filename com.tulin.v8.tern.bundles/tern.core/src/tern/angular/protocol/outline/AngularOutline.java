package tern.angular.protocol.outline;

import tern.ITernProject;
import tern.server.protocol.outline.JSNodeRoot;

/**
 * Angular model which hosts modules, directives, controllers of the Angular
 * application.
 * 
 */
public class AngularOutline extends JSNodeRoot {

	public static final String ANGULAR_MODEL_CHANGED_EVENT = "angular:modelChanged";

	private static final String ROOT = "#Angular";

	public AngularOutline(ITernProject ternProject) {
		super(ROOT, ternProject);
	}

}
