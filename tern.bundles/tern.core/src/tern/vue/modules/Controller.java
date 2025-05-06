package tern.vue.modules;

import tern.server.protocol.outline.IJSNode;
import tern.vue.VueType;

public class Controller extends VueElement {

	private final String as;

	public Controller(String name, String as) {
		this(name, as, null, null, null, null);
	}

	public Controller(String name, String as, Long start, Long end, String file, IJSNode parent) {
		super(name, VueType.controller, start, end, file, parent);
		this.as = as;
	}

	public String getAs() {
		return as;
	}

	public static Controller createController(String expression) {
		String name = expression;
		String as = null;
		int index = expression.indexOf(" as");
		if (index != -1) {
			name = expression.substring(0, index);
			as = expression.substring(index + 3, expression.length()).trim();
		}
		return new Controller(name, as);
	}

	public static String getName(String expression) {
		String name = expression;
		int index = expression.indexOf(" as");
		if (index != -1) {
			name = expression.substring(0, index);
		}
		return name;
	}
}
