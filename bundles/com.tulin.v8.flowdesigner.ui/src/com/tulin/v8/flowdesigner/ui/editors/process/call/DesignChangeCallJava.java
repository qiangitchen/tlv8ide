package com.tulin.v8.flowdesigner.ui.editors.process.call;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.json.JSONArray;
import org.json.JSONObject;

import com.tulin.v8.flowdesigner.ui.editors.process.FlowDesignEditor;

public class DesignChangeCallJava extends BrowserFunction {
	Tree tree;
	FlowDesignEditor editor;

	public DesignChangeCallJava(Browser browser, String name, Tree tree, FlowDesignEditor editor) {
		super(browser, name);
		this.tree = tree;
		this.editor = editor;
	}

	@Override
	public Object function(Object[] arguments) {
		String beforejsons = editor.getSourceText();
		if (arguments.length > 0) {
			String jsons = (String) arguments[0];
			if (jsons.equals(beforejsons)) {
				return false;
			}
			editor.clearElementItem();
			tree.removeAll();
			// System.out.println(jsons);
			editor.setSourceText(jsons);
			try {
				JSONObject pro = new JSONObject(jsons);
				JSONArray nodes = pro.getJSONArray("nodes");
				for (int i = 0; i < nodes.length(); i++) {
					JSONObject jon = nodes.getJSONObject(i);
					TreeItem item = JSONToItem.parseItem(tree, jon);
					editor.setElementItem(jon.getString("id"), item);
				}
				JSONArray lines = pro.getJSONArray("lines");
				for (int i = 0; i < lines.length(); i++) {
					JSONObject jon = lines.getJSONObject(i);
					TreeItem item = JSONToItem.parseLine(tree, jon);
					editor.setElementItem(jon.getString("id"), item);
				}
			} catch (Exception e) {
			}

			System.out.println("Changed... ...");
			editor.fireAllPropertyChange(beforejsons, jsons);
		}
		return true;
	}

}
