package com.tulin.v8.ide.ui.editors.page.design;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.tulin.v8.ide.StudioPlugin;

public class ReadHTML {

	public static void readHead(TreeItem hd, Document htmlDom, WEBDesignEditorInterface webDesignEditorAbst) {
		Element head = htmlDom.getElementsByTag("head").first();
		hd.setData(head);
		Elements items = head.children();
		for (int i = 0; i < items.size(); i++) {
			Element item = items.get(i);
			if (item.tagName().toLowerCase().equals("meta") && item.attr("name") != null
					&& "generator".equals(item.attr("name"))) {
				continue;// 去掉由转换器产生的标志
			}
			final TreeItem tritem = new TreeItem(hd, SWT.NONE);
			tritem.setText(getText(item));
			tritem.setImage(getImageByTagName(item.tagName()));
			tritem.setData(item);
			if (item.attr("id") != null) {
				webDesignEditorAbst.setElementItem(item.attr("id"), tritem);
			}
		}
	}

	public static String getText(Element item) {
		String name = item.tagName().toLowerCase();
		String text = "";
		if ("title".equals(name)) {
			text = name + "[" + item.text() + "]";
		} else if ("meta".equals(name)) {
			text = name + "[" + item.attr("content") + "]";
		} else if ("link".equals(name)) {
			text = name + "[" + item.attr("href") + "]";
		} else if ("script".equals(name)) {
			String src = item.attr("src");
			if (src != null)
				text = name + "[" + item.attr("src") + "]";
			else
				text = name;
		} else if (item.attr("id") != null) {
			text = name + "[" + item.attr("id") + "]";
		} else {
			text = name;
		}
		return text;
	}

	public static void readBody(TreeItem bdy, Document htmlDom, WEBDesignEditorInterface webDesignEditorAbst) {
		Element body = htmlDom.getElementsByTag("body").first();
		bdy.setData(body);
		Elements items = body.children();
		for (int i = 0; i < items.size(); i++) {
			Element item = (Element) items.get(i);
			final TreeItem tritem = new TreeItem(bdy, SWT.NONE);
			tritem.setText(getText(item));
			String tagName = item.tagName();
			if (tagName.toLowerCase().equals("input")) {
				tagName = tagName + "[" + item.attr("type") + "]";
			}
			tritem.setImage(getImageByTagName(tagName));
			tritem.setData(item);
			if (item.attr("id") != null) {
				webDesignEditorAbst.setElementItem(item.attr("id"), tritem);
			}
			readDocument(tritem, item, webDesignEditorAbst);
		}
	}

	public static void readDocument(TreeItem pItem, Element htmlItem, WEBDesignEditorInterface webDesignEditorAbst) {
		Elements items = htmlItem.children();
		for (Element item : items) {
			final TreeItem tritem = new TreeItem(pItem, SWT.NONE);
			tritem.setText(getText(item));
			String tagName = item.tagName();
			if (tagName.toLowerCase().equals("input")) {
				tagName = tagName + "[" + item.attr("type") + "]";
			}
			tritem.setImage(getImageByTagName(tagName));
			tritem.setData(item);
			if (item.attr("id") != null) {
				webDesignEditorAbst.setElementItem(item.attr("id"), tritem);
			}
			if (item.children().size() > 0) {
				readDocument(tritem, item, webDesignEditorAbst);
			}
		}
	}

	public static Image getImageByTagName(String tagName) {
		if ("div".equals(tagName.toLowerCase()))
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW);
		if ("script".equals(tagName.toLowerCase()))
			return StudioPlugin.getIcon("brkp_obj.gif");
		if ("link".equals(tagName.toLowerCase()))
			return StudioPlugin.getIcon("css_prop.gif");
		if ("form".equals(tagName.toLowerCase()))
			return StudioPlugin.getIcon("form.gif");
		if ("meta".equals(tagName.toLowerCase()))
			return StudioPlugin.getIcon("static.gif");
		if ("title".equals(tagName.toLowerCase()))
			return StudioPlugin.getIcon("unknown.gif");
		if ("button".equals(tagName.toLowerCase()))
			return StudioPlugin.getIcon("button.gif");
		if ("table".equals(tagName.toLowerCase()))
			return StudioPlugin.getIcon("tables.png");
		if ("a".equals(tagName.toLowerCase()))
			return StudioPlugin.getIcon("link.gif");
		if ("label".equals(tagName.toLowerCase()))
			return StudioPlugin.getIcon("tag.gif");
		if ("textarea".equals(tagName.toLowerCase()))
			return StudioPlugin.getIcon("textarea.gif");
		if ("select".equals(tagName.toLowerCase()))
			return StudioPlugin.getIcon("select.gif");
		if ("checkbox".equals(tagName.toLowerCase()))
			return StudioPlugin.getIcon("checkbox.gif");
		if ("radio".equals(tagName.toLowerCase()))
			return StudioPlugin.getIcon("radio.gif");
		if ("input[checkbox]".equals(tagName.toLowerCase()))
			return StudioPlugin.getIcon("checkbox.gif");
		if ("input[radio]".equals(tagName.toLowerCase()))
			return StudioPlugin.getIcon("radio.gif");
		if ("input[text]".equals(tagName.toLowerCase()))
			return StudioPlugin.getIcon("text.gif");
		if ("input[button]".equals(tagName.toLowerCase()))
			return StudioPlugin.getIcon("button.gif");
		else
			return StudioPlugin.getIcon("tag.gif");
	}
}
