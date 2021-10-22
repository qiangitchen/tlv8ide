package com.tulin.v8.ide.internal;

import java.util.Map;

import com.tulin.v8.flowdesigner.ui.editors.process.element.FlowFolderElement;

import zigen.plugin.db.ui.internal.TreeNode;

public class FlowFolder extends TreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4658483285716577543L;
	private FlowFolderElement element;

	public FlowFolder() {
		super();
	}

	public FlowFolder(String name) {
		super(name);
	}

	@SuppressWarnings("rawtypes")
	public FlowFolder(Map map) {
		super((String) map.get("SNAME"));
		this.element = new FlowFolderElement(map);
	}

	public FlowFolder(FlowFolderElement element) {
		super(element.getSname());
		this.element = element;
	}

	public FlowFolderElement getElement() {
		return element;
	}

	public void setElement(FlowFolderElement element) {
		this.element = element;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != getClass()) {
			return false;
		}
		return ((FlowFolder) o).element.getSid().equals(element.getSid());
	}
}
