package com.tulin.v8.ide.wizards.utils;

import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.ide.wizards.DataSelectPage;
import com.tulin.v8.ide.wizards.Messages;

import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;

public class TableSelectDialog extends TitleAreaDialog {
	/*
	 * 库表选择对话框
	 */
	private Tree tree = null;
	String dbkey;
	String[] items;
	String itemsToOpen;
	Text searchtext;
	TreeItem root1;
	TreeItem root2;

	public TableSelectDialog(Shell shell, String dbkey) {
		super(shell);
		this.dbkey = dbkey;
		IDBConfig[] dbConfigs = DBConfigManager.getDBConfigs();
		String[] dbkeys = new String[dbConfigs.length];
		for (int i = 0; i < dbConfigs.length; i++) {
			dbkeys[i] = dbConfigs[i].getDbName();
		}
		items = dbkeys;
	}

	public void create() {
		super.create();
		getShell().setText("TuLin Studio");
		setTitle(Messages.getString("wizards.dataselect.message.title"));
		setMessage(Messages.getString("wizards.dataselect.message.unselectedTable"));
	}

	protected int getShellStyle() {
		return super.getShellStyle() | SWT.RESIZE;
	}

	// protected Point getInitialSize() {
	// return new Point(510, 450);
	// }

	// 重写createDialogArea方法，创建对话框区域
	protected Control createDialogArea(Composite parent) {
		// 添加对话框区域的父面板
		final Composite area = new Composite(parent, SWT.FILL);
		area.setLayoutData(new GridData(GridData.FILL_BOTH));
		final GridLayout gridLayout = new GridLayout(2, false);
		// 设置父面板的布局方式
		area.setLayout(gridLayout);
		searchtext = new Text(area, SWT.BORDER);
		GridData textcl = new GridData(GridData.FILL_HORIZONTAL);
		// textcl.widthHint = 430;
		textcl.grabExcessHorizontalSpace = true;
		searchtext.setLayoutData(textcl);
		searchtext.setEditable(true);
		searchtext.setText("");
		Button searchBtn = new Button(area, SWT.PUSH);
		searchBtn.setText(Messages.getString("wizards.dataselect.message.search"));
		// 在对话框区域中添加List组件
		tree = new Tree(area, SWT.BORDER | SWT.V_SCROLL);
		GridData girddata = new GridData(GridData.FILL_BOTH);
		girddata.grabExcessHorizontalSpace = true;
		girddata.grabExcessVerticalSpace = true;
		girddata.horizontalSpan = 2;
		girddata.widthHint = 460;
		girddata.heightHint = 200;
		tree.setLayoutData(girddata);
		root1 = new TreeItem(tree, SWT.NONE);
		root2 = new TreeItem(tree, SWT.NONE);
		root1.setText("TABLE");
		root1.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
		root2.setText("VIEW");
		root2.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));

		Map<String, java.util.List<String>> TreeitemData;
		try {
			TreeitemData = CommonUtil.getDataObject(dbkey);
			java.util.List<String> litable = TreeitemData.get("TABLE");
			root1.removeAll();
			for (int i = 0; i < litable.size(); i++) {
				TreeItem treeitem = new TreeItem(root1, SWT.NONE);
				treeitem.setText(litable.get(i));
				treeitem.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
			}
			root2.removeAll();
			java.util.List<String> liview = TreeitemData.get("VIEW");
			for (int i = 0; i < liview.size(); i++) {
				TreeItem treeitem = new TreeItem(root2, SWT.NONE);
				treeitem.setText(liview.get(i));
				treeitem.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
			}
		} catch (Exception e1) {
			setMessage(e1.toString());
		}
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				itemsToOpen = item.getText();
				validate();
			}
		});
		searchtext.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.keyCode == 13) {
					e.doit = false;
					doSearch();
				}
			}
		});

		// 搜索
		searchBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				doSearch();
			}
		});
		return area;
	}

	private void doSearch() {
		String str = searchtext.getText();
		Map<String, java.util.List<String>> TreeitemData;
		try {
			TreeitemData = DataSelectPage.getDataObject(dbkey, str);
			java.util.List<String> litable = TreeitemData.get("TABLE");
			root1.removeAll();
			for (int i = 0; i < litable.size(); i++) {
				TreeItem treeitem = new TreeItem(root1, SWT.NONE);
				treeitem.setText(litable.get(i));
				treeitem.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
			}
			root2.removeAll();
			java.util.List<String> liview = TreeitemData.get("VIEW");
			for (int i = 0; i < liview.size(); i++) {
				TreeItem treeitem = new TreeItem(root2, SWT.NONE);
				treeitem.setText(liview.get(i));
				treeitem.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
			}
		} catch (Exception e1) {
			setMessage(e1.toString());
		}
	}

	// 添加自定义对话框的校验方法
	private void validate() {
		boolean selected = (tree.getSelectionCount() > 0 && !"TABLE".equals(itemsToOpen)
				&& !"VIEW".equals(itemsToOpen));
		getButton(IDialogConstants.OK_ID).setEnabled(selected);
		if (!selected) {
			setErrorMessage(Messages.getString("wizards.dataselect.message.unselectedTable"));
		} else {
			setErrorMessage(null);
		}
	}

	// protected void createButtonsForButtonBar(Composite parent) {
	// createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
	// true);
	// createButton(parent, IDialogConstants.CANCEL_ID,
	// IDialogConstants.CANCEL_LABEL, false);
	// }

	public String getItemsToOpen() {
		return itemsToOpen;
	}
}