package com.tulin.v8.function.editors;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.tulin.v8.core.TuLinPlugin;

class DataConversion {
	private Tree tree;
	public Document doc;

	public DataConversion(Tree tree, String data) {
		try {
			this.tree = tree;
			this.doc = TxtToXmlDom(data);
			tree.removeAll();
		} catch (Exception e) {
			tree.removeAll();
			initDom();
		}
	}

	// 加载模型
	public void getData() {
		tree.removeAll();
		setData(tree, doc.getDocumentElement());
	}

	// 刷新模型
	public void refresh() {
		tree.removeAll();
		setData(tree, doc.getDocumentElement());
	}

	// 刷新节点模型
	public void refreshItem(TreeItem item) {
		item.removeAll();
		setData(item, (Element) item.getData());
	}

	@SuppressWarnings("rawtypes")
	public void setData(Object parentItem, Element el) {
		try {
			List els = W3cDocumentHelper.getAllChildXmlElements(el);
			for (int i = 0; i < els.size(); i++) {
				Element item = (Element) els.get(i);
				TreeItem treeitem = null;
				if (parentItem.getClass().toString().endsWith("Tree")) {
					treeitem = new TreeItem((Tree) parentItem, SWT.NONE);
				} else {
					treeitem = new TreeItem((TreeItem) parentItem, SWT.NONE);
				}
				String[] row = new String[7];
				row[0] = item.getAttribute("label");
				row[1] = item.getAttribute("icon");
				row[2] = item.getAttribute("layuiIcon");
				row[3] = item.getAttribute("display");
				row[4] = item.getAttribute("url");
				row[5] = item.getAttribute("process");
				row[6] = item.getAttribute("activity");
				treeitem.setText(row);
				treeitem.setImage(getImage(item.getAttribute("url")));
				treeitem.setData(item);
				setData(treeitem, item);
			}
		} catch (Exception e) {
			tree.removeAll();
		}
	}

	// 字符串转换为Document
	public Document TxtToXmlDom(String text) throws Exception {
		Document doc;
		doc = W3cDocumentHelper.StrToXML(text);
		return doc;
	}

	public Image getImage(String path) {
		if (path != null && !"".equals(path)) {
			if (path.toLowerCase().endsWith(".jsp")) {
				return TuLinPlugin.getIcon("jsp.gif");
			} else
				return TuLinPlugin.getIcon("html.gif");
		} else {
			return TuLinPlugin.getIcon("folder-open.gif");
		}
	}

	// 将编辑后的DOM转换成字符串
	public String DomToString() {
		return W3cDocumentHelper.docToString(this.doc);
	}

	// 空白页面初始化
	private void initDom() {
		String xmlstr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		xmlstr += "<root xmlns=\"\">\n";
		xmlstr += "\n</root>";
		try {
			this.doc = W3cDocumentHelper.StrToXML(xmlstr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 添加根目录
	public Element addRootItem() {
		if (this.doc == null) {
			initDom();
		}
		Element rootItem = this.doc.createElement("item");
		rootItem.setAttribute("label", Messages.getString("editors.FunEditor.rootEle"));
		rootItem.setAttribute("icon", "");
		rootItem.setAttribute("display", "solid");
		this.doc.getDocumentElement().appendChild(rootItem);
		return rootItem;
	}

	// 添加子目录
	public Element addChildItem(Element parentItem) {
		Element childItem = parentItem.getOwnerDocument().createElement("item");
		childItem.setAttribute("label", Messages.getString("editors.FunEditor.subEle"));
		childItem.setAttribute("icon", "");
		childItem.setAttribute("display", "");
		parentItem.appendChild(childItem);
		return childItem;
	}

	// 添加功能树
	public Element addFunctionItem(Element parentItem, String[] attr) {
		Element childItem = parentItem.getOwnerDocument().createElement("item");
		childItem.setAttribute("label", attr[0]);
		childItem.setAttribute("icon", attr[1]);
		childItem.setAttribute("layuiIcon", attr[2]);
		childItem.setAttribute("display", attr[3]);
		childItem.setAttribute("url", attr[4]);
		childItem.setAttribute("process", attr[5]);
		childItem.setAttribute("activity", attr[6]);
		parentItem.appendChild(childItem);
		return childItem;
	}
}
