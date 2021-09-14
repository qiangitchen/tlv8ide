package com.tulin.v8.fn;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.tulin.v8.core.StringArray;
import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.core.XMLFormator;
import com.tulin.v8.core.utils.XMLDocument;

@SuppressWarnings("rawtypes")
class ModelManage {
	boolean doing = false;
	Document document;

	public ModelManage(String str) {
		loadModel(str);
	}

	public ModelManage() {
	}

	public void update(String str) {
		loadModel(str);
	}

	public String toString() {
		if (document != null) {
			return XMLFormator.formatXML(document);
		}
		return "";
	}

	public void loadModel(String editorText) {
		try {
			document = XMLDocument.str2dom(editorText);
		} catch (Exception e) {
			try {
				document = DocumentHelper.createDocument();
				document.addElement("root");
			} catch (Exception er) {
				er.printStackTrace();
			}
		}
	}

	public void loadTree(Tree tree) {
		tree.removeAll();
		if (document != null) {
			Element element = document.getRootElement();
			List items = element.elements("item");
			for (int i = 0; i < items.size(); i++) {
				Element item = (Element) items.get(i);
				TreeItem treeitem = new TreeItem(tree, SWT.NONE);
				treeitem.setText(item.attributeValue("id"));// name
				treeitem.setImage(TuLinPlugin.getIcon("folder.gif"));
				treeitem.setData(item);
				loadChildren(item, treeitem);
			}
		}
	}

	public void loadChildren(Element element, TreeItem treeitem) {
		List items = element.elements("function");
		for (int i = 0; i < items.size(); i++) {
			Element item = (Element) items.get(i);
			TreeItem childtreeitem = new TreeItem(treeitem, SWT.NONE);
			childtreeitem.setText(item.attributeValue("id"));// name
			childtreeitem.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
			childtreeitem.setData(item);
		}
	}

	public void loadParamList(Element element, Table table) {
		String param = element.attributeValue("param");
		String paramVal = element.attributeValue("paramvalue");
		if (param != null && !"".equals(param)) {
			String[] pas = param.trim().split(",");
			String[] vals = null;
			if (paramVal != null && !"".equals(paramVal)) {
				vals = paramVal.trim().split(",");
			}
			for (int i = 0; i < pas.length; i++) {
				TableItem tableitem = new TableItem(table, SWT.NONE);
				String val = "";
				try {
					val = vals == null ? "" : vals[i];
				} catch (Exception e) {
				}
				tableitem.setText(new String[] { pas[i], val });
				tableitem.setData(element);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void addFn(FnEditor editorpart, Tree tree) {
		Element root = document.getRootElement().element("item");
		if (root == null) {
			root = document.getRootElement().addElement("item");
			root.setAttributeValue("id", "newFnItem");
			root.setAttributeValue("isParent", "true");
			root.setAttributeValue("name", Messages.getString("editors.FnEditor.25"));
		}
		AddFnDialog dialog = new AddFnDialog(editorpart.getSite().getShell(), editorpart, this, tree, root);
		dialog.open();
	}

	@SuppressWarnings("deprecation")
	public void realoadParam(TreeItem item, Table table) {
		Element fn = (Element) item.getData();
		if (fn != null) {
			fn.setAttributeValue("param", "");
			fn.setAttributeValue("paramvalue", "");
			TableItem[] items = table.getItems();
			StringArray array = new StringArray();
			StringArray varray = new StringArray();
			for (int i = 0; i < items.length; i++) {
				array.push(items[i].getText(0));
				varray.push(items[i].getText(1));
			}
			fn.setAttributeValue("param", array.join(","));
			fn.setAttributeValue("paramvalue", varray.join(","));
		}
	}

}
