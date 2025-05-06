package com.tulin.v8.ide.internal;

import java.util.Map;

import com.tulin.v8.flowdesigner.ui.editors.process.element.ProcessDrawElement;

import zigen.plugin.db.ui.internal.TreeNode;

public class FlowDraw extends TreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5187912837160994635L;

	private ProcessDrawElement element;

	public FlowDraw() {
		super();
	}

	public FlowDraw(String name) {
		super(name);
	}

	@SuppressWarnings("rawtypes")
	public FlowDraw(Map map) {
		super((String) map.get("SPROCESSNAME"));
		this.element = new ProcessDrawElement(map);
	}

	public FlowDraw(ProcessDrawElement element) {
		super(element.getSprocessname());
		this.element = element;
	}

	public ProcessDrawElement getElement() {
		return element;
	}

	public void setElement(ProcessDrawElement element) {
		this.element = element;
		name = element.getSprocessname();
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
		return ((FlowDraw) o).element.getSid().equals(element.getSid());
	}

}
