/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

public class IndexRoot extends TreeNode {

	private static final long serialVersionUID = 1L;

	private String name = "INDEX";

	private String type = "";

	private String paramater = "";

	public IndexRoot() {}

	public void addConstraint(Index model) {
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
