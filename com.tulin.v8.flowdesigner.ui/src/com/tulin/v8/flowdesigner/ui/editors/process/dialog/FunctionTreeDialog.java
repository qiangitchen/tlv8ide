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

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.flowdesigner.ui.editors.process.FuncTreeController;

public class FunctionTreeDialog extends Dialog {
	private Tree funTree;
	private Element element;
	private String curUrl = "";
	private TreeItem curtreeitem;

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
			Element root = new FuncTreeController().index("WEB-INF/funtree/");
			List<Element> funs = root.elements();
			for (int i = 0; i < funs.size(); i++) {
				TreeItem treeitem = new TreeItem(funTree, SWT.NONE);
				readElement(funs.get(i), treeitem);
			}
		} catch (Exception e) {

		}
	}

	private void readElement(Element ele, TreeItem treeitem) {
		treeitem.setText(ele.attributeValue("label"));
		treeitem.setData(ele);
		if (curUrl.equals(ele.attributeValue("url"))) {
			curtreeitem = treeitem;
			element = ele;
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
				element = (Element) select[0].getData();
				String url = element.attributeValue("url");
				if (url == null) {
					MessageDialog.openInformation(getShell(),
							Messages.getString("TLEditor.message.title1"),
							Messages.getString("TLEditor.message.selpage"));
					return;
				}
			} else {
				MessageDialog.openInformation(getShell(),
						Messages.getString("TLEditor.message.title1"),
						Messages.getString("TLEditor.message.selpage"));
				return;
			}
		}
		super.buttonPressed(buttonId);
	}

	public Element getSelectData() {
		return element;
	}

	public String getCurUrl() {
		return curUrl;
	}

	public void setCurUrl(String curUrl) {
		this.curUrl = curUrl;
	}
}
