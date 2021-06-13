/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

public class TriggerRoot extends TreeNode {

	private static final long serialVersionUID = 1L;

	private String name = "TRIGGER";

	private String type = "";

	private String paramater = "";

	public TriggerRoot() {}

	public void addConstraint(OracleSource model) {
		addChild(model);
	}

	public String getName() {
		return name;
	}

	public String getParamater() {
		return paramater;
	}

	public String getType() {
		return type;
	}

}
