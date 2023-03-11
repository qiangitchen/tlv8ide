package com.tulin.v8.flowdesigner.ui.editors.process.dialog;

import java.util.List;

import org.dom4j.Element;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.json.JSONArray;
import org.json.JSONObject;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.flowdesigner.ui.data.MenuData;
import com.tulin.v8.flowdesigner.ui.editors.process.FuncTreeController;

public class FunctionTreeDialog extends Dialog {
	private Tree funTree;
	private String curUrl = "";
	private TreeItem curtreeitem;
	private JSONObject selData;

	public FunctionTreeDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * 在这个方法里构建Dialog中的界面内容
	 */
	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.getString("TLEditor.FuncPagSelect.title"));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());
		funTree = new Tree(container, SWT.FILL | SWT.BORDER | SWT.V_SCROLL);
		loadData();
		funTree.addListener(SWT.Expand, new Listener() {
			public void handleEvent(final Event event) {
				final TreeItem root = (TreeItem) event.item;
				root.setImage(TuLinPlugin.getIcon("folder-open.gif"));
			}
		});
		funTree.addListener(SWT.Collapse, new Listener() {
			public void handleEvent(final Event event) {
				final TreeItem root = (TreeItem) event.item;
				root.setImage(TuLinPlugin.getIcon("folder.gif"));
			}
		});
		if (curtreeitem != null) {
			funTree.select(curtreeitem);
		}
		return funTree;
	}

	private void loadData() {
		try {
			JSONArray jsona = MenuData.getMenuTree();
			for (int i = 0; i < jsona.length(); i++) {
				TreeItem treeitem = new TreeItem(funTree, SWT.NONE);
				readElement(jsona.getJSONObject(i), treeitem);
			}
		} catch (Exception er) {
			try {
				Element root = new FuncTreeController().index("WEB-INF/funtree/");
				List<Element> funs = root.elements();
				for (int i = 0; i < funs.size(); i++) {
					TreeItem treeitem = new TreeItem(funTree, SWT.NONE);
					readElement(funs.get(i), treeitem);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void readElement(Element ele, TreeItem treeitem) {
		try {
			treeitem.setText(ele.attributeValue("label"));
			JSONObject json = new JSONObject();
			json.put("label", ele.attributeValue("label"));
			json.put("url", ele.attributeValue("url"));
			treeitem.setData(json);
			if (curUrl.equals(ele.attributeValue("url"))) {
				curtreeitem = treeitem;
			}
			List<Element> eles = ele.elements();
			if (!eles.isEmpty()) {
				treeitem.setImage(TuLinPlugin.getIcon("folder.gif"));
				for (int i = 0; i < eles.size(); i++) {
					TreeItem ctreeitem = new TreeItem(treeitem, SWT.NONE);
					readElement(eles.get(i), ctreeitem);
				}
			} else {
				treeitem.setImage(TuLinPlugin.getIcon("html.gif"));
			}
		} catch (Exception e) {
		}
	}

	private void readElement(JSONObject json, TreeItem treeitem) {
		try {
			treeitem.setText(json.getString("label"));
			treeitem.setData(json);
			if (curUrl.equals(json.getString("url"))) {
				curtreeitem = treeitem;
			}
			JSONArray eles = json.getJSONArray("children");
			if (eles.length() > 0) {
				treeitem.setImage(TuLinPlugin.getIcon("folder.gif"));
				for (int i = 0; i < eles.length(); i++) {
					TreeItem ctreeitem = new TreeItem(treeitem, SWT.NONE);
					readElement(eles.getJSONObject(i), ctreeitem);
				}
			} else {
				treeitem.setImage(TuLinPlugin.getIcon("html.gif"));
			}
		} catch (Exception e) {
		}
	}

	protected int getShellStyle() {
		return super.getShellStyle() | SWT.RESIZE;
	}

	protected Point getInitialSize() {
		return new Point(400, 500);// 400是宽500是高
	}

	/**
	 * Dialog点击按钮时执行的方法
	 */
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			TreeItem[] select = funTree.getSelection();
			if (select.length > 0) {
				selData = (JSONObject) select[0].getData();
				try {
					String url = selData.getString("url");
					if (url == null) {
						MessageDialog.openInformation(getShell(), Messages.getString("TLEditor.message.title1"),
								Messages.getString("TLEditor.message.selpage"));
						return;
					}
				} catch (Exception e) {
				}
			} else {
				MessageDialog.openInformation(getShell(), Messages.getString("TLEditor.message.title1"),
						Messages.getString("TLEditor.message.selpage"));
				return;
			}
		}
		super.buttonPressed(buttonId);
	}

	public JSONObject getSelectData() {
		return selData;
	}

	public String getCurUrl() {
		return curUrl;
	}

	public void setCurUrl(String curUrl) {
		this.curUrl = curUrl;
	}
}
