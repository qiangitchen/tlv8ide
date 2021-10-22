package com.tulin.v8.flowdesigner.ui.editors.process.call;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.json.JSONObject;

import com.tulin.v8.flowdesigner.ui.editors.process.FlowDesignEditor;

public class SelectedCallJava extends BrowserFunction {
	Tree tree;
	FlowDesignEditor editor;

	public SelectedCallJava(Browser browser, String name, Tree tree,
			FlowDesignEditor editor) {
		super(browser, name);
		this.tree = tree;
		this.editor = editor;
	}

	@Override
	public Object function(Object[] arguments) {
		if (arguments.length > 0) {
			String jsons = (String) arguments[0];
			// System.out.println(jsons);
			try {
				JSONObject json = new JSONObject(jsons);
				editor.setPropertyTable(json);
				TreeItem item = editor.getElementItem(json.getString("id"));
				if (item != null) {
					tree.setSelection(item);
					editor.changeRemoveHanler(true);
				}
			} catch (Exception e) {
				editor.setPropertyTable(null);
				editor.changeRemoveHanler(false);
			}
		}
		return true;
	}
}