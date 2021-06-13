package com.tulin.v8.ide.ui.editors.process.call;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.json.JSONObject;

import com.tulin.v8.ide.StudioPlugin;

public class JSONToItem {
	public static TreeItem parseItem(Tree tree, JSONObject jon) {
		TreeItem item = new TreeItem(tree, SWT.NONE);
		try {
			item.setText(jon.getString("name"));
			if ("start".equals(jon.getString("type"))) {
				item.setImage(StudioPlugin
						.getIcon("process_icons/start.png"));
			} else if ("end".equals(jon.getString("type"))) {
				item.setImage(StudioPlugin
						.getIcon("process_icons/end.png"));
			} else if ("node".equals(jon.getString("type"))) {
				item.setImage(StudioPlugin
						.getIcon("process_icons/biz.png"));
			} else if ("condition".equals(jon.getString("type"))) {
				item.setImage(StudioPlugin
						.getIcon("process_icons/fork.png"));
			}
			item.setData(jon);
		} catch (Exception e) {
		}
		return item;
	}
}
