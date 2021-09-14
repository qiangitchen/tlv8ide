package com.tulin.v8.flowdesigner.ui.editors.process.call;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.json.JSONObject;

import com.tulin.v8.flowdesigner.ui.editors.process.FlowDesignEditor;
import com.tulin.v8.flowdesigner.ui.editors.process.element.ConditionPropertySet;

public class CallJava extends BrowserFunction {
	Tree tree;
	FlowDesignEditor editor;

	public CallJava(Browser browser, String name, Tree tree, FlowDesignEditor editor) {
		super(browser, name);
		this.tree = tree;
		this.editor = editor;
	}

	@Override
	public Object function(Object[] arguments) {
		// System.err.println(arguments[0].toString());
		try {
			String fun = (String) arguments[0];
			if ("remove".equals(fun)) {
				String nodeid = arguments[1].toString();
				TreeItem treeitem = editor.getElementItem(nodeid);
				treeitem.dispose();
				editor.removeElementItem(nodeid);
			}
			if ("add".equals(fun)) {
				String nodes = arguments[1].toString();
				addNode(nodes);
			}
			if ("setDomConditionSelect".equals(fun)) {
				String nodes = arguments[1].toString();
				ConditionPropertySet.DomConditionSelects = nodes;
			}
			if ("setDeleteActionEnable".equals(fun)) {
				boolean enbale = Boolean.valueOf(arguments[1].toString());
				editor.deleteAction.setEnabled(enbale);
				editor.changeRemoveHanler(enbale);
			}
		} catch (Exception e) {
		}
		return super.function(arguments);
	}

	private void addNode(String data) {
		try {
			JSONObject jon = new JSONObject(data);
			TreeItem item = JSONToItem.parseItem(tree, jon);
			editor.setElementItem(jon.getString("id"), item);
		} catch (Exception e) {
		}
	}

}
