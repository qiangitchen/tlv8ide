package com.tulin.v8.ide.ui.editors.process.call;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.json.JSONArray;
import org.json.JSONObject;

import com.tulin.v8.ide.ui.editors.process.FlowDesignEditor;

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
				JSONArray json = pro.getJSONArray("nodes");
				for (int i = 0; i < json.length(); i++) {
					JSONObject jon = json.getJSONObject(i);
					TreeItem item = JSONToItem.parseItem(tree, jon);
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