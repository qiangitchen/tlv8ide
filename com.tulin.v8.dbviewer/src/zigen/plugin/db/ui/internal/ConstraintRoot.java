/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

public class ConstraintRoot extends TreeNode {

	private static final long serialVersionUID = 1L;

	private String name = "CONSTRAINT";

	public ConstraintRoot() {}

	public void addConstraint(Constraint model) {
		addChild(model);
	}

	public String getName() {
		return name;
	}

}
