package com.tulin.v8.flowdesigner.ui.editors.process.listener;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.widgets.TreeItem;
import org.json.JSONArray;
import org.json.JSONObject;

import com.tulin.v8.flowdesigner.ui.editors.process.FlowDesignEditor;
import com.tulin.v8.flowdesigner.ui.editors.process.call.JSONToItem;
import com.tulin.v8.flowdesigner.ui.editors.process.element.ConditionPropertySet;

/**
 * 监听title进行JS与Java交互操作
 * 
 * @text 做这个奇怪的操作是因为，Webkit下浏览器注入JS函数有BUG
 * @author 陈乾
 *
 */
public class BreowserTitleListener implements TitleListener {
	FlowDesignEditor editor;
	Browser browser;

	public BreowserTitleListener(FlowDesignEditor editor, Browser browser) {
		this.editor = editor;
		this.browser = browser;
	}

	@Override
	public void changed(TitleEvent event) {
		System.out.println(event.title);
		try {
			JSONObject json = new JSONObject(event.title);
			String action = json.getString("action");
			if ("dataInitedCall".equals(action)) {
				String jsonstr = (String) browser.evaluate("return getPageDataJson();");
				dataInitedCall(jsonstr);
			}
			if ("designChangeCall".equals(action)) {
				String jsonstr = (String) browser.evaluate("return getPageDataJson();");
				designChangeCall(jsonstr);
			}
			if ("selectedCall".equals(action)) {
				String id = json.getString("data");
				TreeItem item = editor.getElementItem(id);
				if (item != null) {
					editor.tree.setSelection(item);
					JSONObject seljson = (JSONObject) item.getData();
					editor.setPropertyTable(seljson);
					editor.changeRemoveHanler(true);
				} else {
					editor.tree.deselectAll();
					editor.setPropertyTable(null);
					editor.changeRemoveHanler(false);
				}
			}
			if ("EditorCallJava".equals(action)) {
				String fun = json.getString("option");
				if ("remove".equals(fun)) {
					String nodeid = json.getString("data");
					TreeItem treeitem = editor.getElementItem(nodeid);
					treeitem.dispose();
					editor.removeElementItem(nodeid);
				}
				if ("add".equals(fun)) {
					String nodes = json.getString("data");
					addNode(nodes);
				}
				if ("setDomConditionSelect".equals(fun)) {
					String nodes = json.getString("data");
					ConditionPropertySet.DomConditionSelects = nodes;
				}
				if ("setDeleteActionEnable".equals(fun)) {
					boolean enbale = Boolean.valueOf(json.getString("data"));
					editor.deleteAction.setEnabled(enbale);
					editor.changeRemoveHanler(enbale);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void dataInitedCall(String jsons) {
		System.out.println(jsons);
		editor.clearElementItem();
		editor.tree.removeAll();
		if ("empty".equals(jsons) || jsons == null || "".equals(jsons)) {
			return;
		}
		try {
			JSONObject json = new JSONObject(jsons);
			JSONArray nodes = json.getJSONArray("nodes");
			for (int i = 0; i < nodes.length(); i++) {
				JSONObject jon = nodes.getJSONObject(i);
				TreeItem item = JSONToItem.parseItem(editor.tree, jon);
				editor.setElementItem(jon.getString("id"), item);
			}
			JSONArray lines = json.getJSONArray("lines");
			for (int i = 0; i < lines.length(); i++) {
				JSONObject jon = lines.getJSONObject(i);
				TreeItem item = JSONToItem.parseLine(editor.tree, jon);
				editor.setElementItem(jon.getString("id"), item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void designChangeCall(String jsons) {
		String beforejsons = editor.getSourceText();
		if (jsons.equals(beforejsons)) {
			return;
		}
		editor.clearElementItem();
		editor.tree.removeAll();
		// System.out.println(jsons);
		editor.setSourceText(jsons);
		try {
			JSONObject pro = new JSONObject(jsons);
			JSONArray nodes = pro.getJSONArray("nodes");
			for (int i = 0; i < nodes.length(); i++) {
				JSONObject jon = nodes.getJSONObject(i);
				TreeItem item = JSONToItem.parseItem(editor.tree, jon);
				editor.setElementItem(jon.getString("id"), item);
			}
			JSONArray lines = pro.getJSONArray("lines");
			for (int i = 0; i < lines.length(); i++) {
				JSONObject jon = lines.getJSONObject(i);
				TreeItem item = JSONToItem.parseLine(editor.tree, jon);
				editor.setElementItem(jon.getString("id"), item);
			}
		} catch (Exception e) {
		}
		System.out.println("Changed... ...");
		editor.fireAllPropertyChange(beforejsons, jsons);
	}

	private void addNode(String data) {
		try {
			JSONObject jon = new JSONObject(data);
			TreeItem item = JSONToItem.parseItem(editor.tree, jon);
			editor.setElementItem(jon.getString("id"), item);
		} catch (Exception e) {
		}
	}

}
