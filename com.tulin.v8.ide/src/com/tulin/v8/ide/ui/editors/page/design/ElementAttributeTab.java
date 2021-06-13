package com.tulin.v8.ide.ui.editors.page.design;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wst.html.core.internal.document.ElementStyleImpl;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

@SuppressWarnings("restriction")
public class ElementAttributeTab {

	/**
	 * 添加属性项
	 */
	public static void setAttributeTab(Table table, ElementStyleImpl element) {
		if (element == null)
			return;
		NamedNodeMap attrs = element.getAttributes();
		table.removeAll();
		for (int i = 0; i < attrs.getLength(); i++) {
			Node attr = attrs.item(i);
			String name = attr.getNodeName();
			if (!name.startsWith("on") && !name.startsWith("before") && !name.startsWith("after")
					&& !"dataValueChanged".equals(name)) {
				TableItem item = new TableItem(table, SWT.BORDER);
				item.setText(new String[] { name, attr.getNodeValue() });
				item.setData(element);
			}
		}
	}

	/**
	 * 添加事件项
	 */
	public static void setEventTab(Table table, ElementStyleImpl element) {
		if (element == null)
			return;
		NamedNodeMap attrs = element.getAttributes();
		table.removeAll();
		Map<String, String> eventm = new HashMap<String, String>();
		for (int i = 0; i < attrs.getLength(); i++) {
			Node attr = attrs.item(i);
			String name = attr.getNodeName();
			String value = attr.getNodeValue();
			if (name.startsWith("on") || name.startsWith("before") || name.startsWith("after")
					|| "dataValueChanged".equals(name)) {
				TableItem item = new TableItem(table, SWT.BORDER);
				item.setText(new String[] { name, value });
				item.setData(element);
				eventm.put(name, value);
			}
		}
		List<String> evlist = getElementEvent(element);
		for (int n = 0; n < evlist.size(); n++) {
			String evname = evlist.get(n);
			if (!eventm.containsKey(evname)) {
				TableItem item = new TableItem(table, SWT.BORDER);
				item.setText(new String[] { evname, "" });
				item.setData(element);
			}
		}
	}

	/**
	 * 获取常用事件列表
	 */
	public static List<String> getElementEvent(ElementStyleImpl element) {
		List<String> relist = new ArrayList<String>();
		String tagName = element.getTagName().toUpperCase();
		if ("A、  AREA、LABEL、B、INPUT、SELECT、C、 TEXTAREA、BUTTON".contains(tagName)) {
			relist.add("on" + "focus");
		}
		if ("A、  AREA、LABEL、INPUT、B、        SELECT、TEXTAREA、C、  BUTTON".contains(tagName)) {
			relist.add("on" + "blur");
		}
		if ("INPUT、SELECT、TEXTAREA".contains(tagName)) {
			relist.add("on" + "change");
		}
		if ("BODY、FRAMESET".contains(tagName)) {
			relist.add("on" + "load");
		}
		if ("BODY、FRAMESET".contains(tagName)) {
			relist.add("on" + "unload");
		}
		if ("FORM".equals(tagName)) {
			relist.add("on" + "submit");
			relist.add("on" + "reset");
			relist.add("beforeSave");
			relist.add("afterSave");
			relist.add("beforeDelete");
			relist.add("afterDelete");
			relist.add("beforeRefresh");
			relist.add("afterRefresh");
		}
		if (!"HEAD、SCRIPT、META、LINK、STYLE、TITLE".contains(tagName) && !tagName.contains("JSP:")) {
			relist.add("on" + "keydown");
			relist.add("on" + "keypress");
			relist.add("on" + "keyup");
			relist.add("on" + "mousedown");
			relist.add("on" + "mousemove");
			relist.add("on" + "mouseout");
			relist.add("on" + "mouseover");
			relist.add("on" + "mouseup");
		}
		String component = element.getAttribute("component");
		if (component != null) {
			if ("dataGrid".equals(component)) {
				relist.add("dataValueChanged");
				relist.add("beforeInsert");
				relist.add("afterInert");
				relist.add("beforeSave");
				relist.add("afterSave");
				relist.add("beforeDelete");
				relist.add("afterDelete");
				relist.add("beforeRefresh");
				relist.add("afterRefresh");
				relist.add("onRowInit");
				relist.add("onselected");
				relist.add("ondbclick");
				relist.add("onchecked");
				relist.add("oncheckAll");
				relist.add("onuncheckAll");
			}
			if ("flowprocess".equals(component)) {
				relist.add("onAbortCommit");
				relist.add("onAdvanceCommit");
				relist.add("onBackCommit");
				relist.add("onStartCommit");
				relist.add("onSuspendCommit");
				relist.add("onTransferCommit");
				relist.add("onAfterAbort");
				relist.add("onAfterAdvance");
				relist.add("onAfterBack");
				relist.add("onAfterStart");
				relist.add("onAfterSuspend");
				relist.add("onAfterTransfer");
				relist.add("onBeforeAbort");
				relist.add("onBeforeAdvance");
				relist.add("onBeforeBack");
				relist.add("onBeforeStart");
				relist.add("onBeforeSuspend");
				relist.add("onBeforeTransfer");
				relist.add("onBeforeFlowAction");
				relist.add("onAfterFlowAction");
				relist.add("onFlowActionCommit");
				relist.add("onFlowEndCommit");
				relist.add("onauditOpionWrited");
			}
		}
		return relist;
	}
}
