package com.tulin.v8.ide.ui.editors.page.design.listener;

import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.json.JSONException;
import org.json.JSONObject;

import com.tulin.v8.ide.ui.editors.page.PageEditorInterface;
import com.tulin.v8.ide.ui.editors.page.design.WEBDesignEditorInterface;

public class DTitleListener implements TitleListener {

	PageEditorInterface pageeditor;
	WEBDesignEditorInterface designeditor;

	public DTitleListener(PageEditorInterface pageeditor, WEBDesignEditorInterface designeditor) {
		this.pageeditor = pageeditor;
		this.designeditor = designeditor;
	}

	@Override
	public void changed(TitleEvent event) {
		try {
			//System.out.println(event.title);
			boolean isselectd = false;
			JSONObject json = new JSONObject(event.title.toString());
			// 根据id选中Element
			if (json.getString("id") != null) {
				String elementid = json.getString("id");
				if (elementid.indexOf("_grid_label_fixTable") > 0) {
					elementid = elementid.substring(0, elementid.indexOf("_grid_label_fixTable"));
				}
				designeditor.selectElement(elementid);
				isselectd = true;
			}
			// 有操作指令
			if (json.getString("action") != null) {
				String action = json.getString("action");
				// 查看源码
				if ("lookResourse".equals(action)) {
					designeditor.viewSourse();
				} else if ("catAction".equals(action)) {// 剪切
					if (isselectd) {
						designeditor.catElement();
					}
				} else if ("copyAction".equals(action)) {// 复制
					if (isselectd) {
						designeditor.copyElement();
					}
				} else if ("deleteAction".equals(action)) {// 删除
					if (isselectd) {
						designeditor.deleteElement();
					}
				}
			}
		} catch (JSONException e) {
		}
	}

}
