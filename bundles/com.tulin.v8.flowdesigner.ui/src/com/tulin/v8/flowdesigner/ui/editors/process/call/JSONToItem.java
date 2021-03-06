package com.tulin.v8.flowdesigner.ui.editors.process.call;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.json.JSONObject;

import com.tulin.v8.flowdesigner.ui.Activator;

public class JSONToItem {
	public static TreeItem parseItem(Tree tree, JSONObject jon) {
		TreeItem item = new TreeItem(tree, SWT.NONE);
		try {
			item.setText(jon.getString("name"));
			if ("start".equals(jon.getString("type"))) {
				item.setImage(Activator.getIcon("process_icons/start.png"));
			} else if ("end".equals(jon.getString("type"))) {
				item.setImage(Activator.getIcon("process_icons/end.png"));
			} else if ("node".equals(jon.getString("type"))) {
				item.setImage(Activator.getIcon("process_icons/biz.png"));
			} else if ("condition".equals(jon.getString("type"))) {
				item.setImage(Activator.getIcon("process_icons/fork.png"));
			}
			item.setData(jon);
		} catch (Exception e) {
		}
		return item;
	}

	public static TreeItem parseLine(Tree tree, JSONObject jon) {
		TreeItem item = new TreeItem(tree, SWT.NONE);
		try {
			item.setText(jon.getString("name") + "[" + jon.getString("from") + "->" + jon.getString("to") + "]");
			if ("polyline".equals(jon.getString("shape"))) {
				item.setImage(Activator.getIcon("process_icons/transition.png"));
			} else {
				item.setImage(Activator.getIcon("process_icons/forward.gif"));
			}
			item.setData(jon);
		} catch (Exception e) {
		}
		return item;
	}
}
