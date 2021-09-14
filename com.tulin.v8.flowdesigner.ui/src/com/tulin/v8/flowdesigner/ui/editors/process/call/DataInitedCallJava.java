package com.tulin.v8.flowdesigner.ui.editors.process.call;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.json.JSONArray;
import org.json.JSONObject;

import com.tulin.v8.flowdesigner.ui.editors.process.FlowDesignEditor;

public class DataInitedCallJava extends BrowserFunction {
	Tree tree;
	FlowDesignEditor editor;

	public DataInitedCallJava(Browser browser, String name, Tree tree,
			FlowDesignEditor editor) {
		super(browser, name);
		this.tree = tree;
		this.editor = editor;
	}

	@Override
	public Object function(Object[] arguments) {
		editor.clearElementItem();
		tree.removeAll();
		if (arguments.length > 0) {
			String jsons = (String) arguments[0];
			// System.out.println(jsons);
			if ("empty".equals(jsons)) {
				return false;
			}
			try {
				JSONArray json = new JSONArray(jsons);
				for (int i = 0; i < json.length(); i++) {
					JSONObject jon = json.getJSONObject(i);
					TreeItem item = JSONToItem.parseItem(tree, jon);
					editor.setElementItem(jon.getString("id"), item);
				}
			} catch (Exception e) {
			}
		}
		return true;
	}

}
