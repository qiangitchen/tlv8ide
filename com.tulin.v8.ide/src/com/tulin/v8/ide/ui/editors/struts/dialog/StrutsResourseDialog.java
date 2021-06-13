package com.tulin.v8.ide.ui.editors.struts.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.editors.text.TextEditor;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.ui.editors.struts.Messages;

/*
 * 资源选择对话框
 */
public class StrutsResourseDialog extends Dialog {
	Tree foldlist;
	TextEditor editor;
	private Text seachText;
	public List<String> selectlist = new ArrayList<String>();
	StrutsResourse strutsresourse;

	/**
	 * 构造函数
	 */
	public StrutsResourseDialog(Shell parentShell, TextEditor editor) {
		super(parentShell);
		this.editor = editor;
		strutsresourse = new StrutsResourse(editor);
	}

	/**
	 * 在这个方法里构建Dialog中的界面内容
	 */
	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.getString("editors.StrutsConfigEditor.resdialog"));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new RowLayout());
		Composite composite = new Composite(container, SWT.FILL);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);

		seachText = new Text(composite, SWT.BORDER);
		GridData seachTextctl = new GridData(SWT.NONE);
		seachTextctl.widthHint = 500;
		seachText.setLayoutData(seachTextctl);
		Button seach = new Button(composite, SWT.PUSH);
		seach.setText(Messages.getString("editors.StrutsConfigEditor.resdialog.1"));
		seach.setLayoutData(new GridData(60, 25));

		foldlist = new Tree(composite, SWT.BORDER | SWT.FULL_SELECTION);
		GridData foldlistctl = new GridData(SWT.FILL, SWT.CENTER, false, false);
		foldlistctl.horizontalSpan = 2;
		foldlistctl.grabExcessHorizontalSpace = true;
		foldlistctl.grabExcessVerticalSpace = true;
		foldlistctl.widthHint = 560;
		foldlistctl.heightHint = 350;
		foldlist.setLayoutData(foldlistctl);
		strutsresourse.loadFileTree(foldlist);

		foldlist.addListener(SWT.Expand, new Listener() {
			public void handleEvent(final Event event) {
				final TreeItem root = (TreeItem) event.item;
				root.setImage(StudioPlugin.getIcon("folder-open.gif"));
			}
		});
		foldlist.addListener(SWT.Collapse, new Listener() {
			public void handleEvent(final Event event) {
				final TreeItem root = (TreeItem) event.item;
				root.setImage(StudioPlugin.getIcon("folder.gif"));
			}
		});

		seachText.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyPressed(KeyEvent event) {
				if (event.keyCode == 13) {
					doSearch();
				}
			}
		});

		seach.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				doSearch();
			}
		});
		return composite;
	}

	// 过滤
	private void doSearch() {
		foldlist.removeAll();
		strutsresourse.loadFileTree(foldlist);
		String text = seachText.getText().trim();
		TreeItem[] items = foldlist.getItems();
		searchTree(items, text);
		treeExpandAll(foldlist.getItems());
	}

	private void searchTree(TreeItem[] items, String text) {
		if (!"".equals(text)) {
			for (int i = 0; i < items.length; i++) {
				if (!items[i].getText().contains(text)) {
					if (items[i].getItems().length > 0) {
						if (!hasChild(items[i].getItems(), text)) {
							items[i].dispose();
						} else {
							searchTree(items[i].getItems(), text);
						}
					} else {
						items[i].dispose();
					}
				}
			}
		}
	}

	private boolean hasChild(TreeItem[] items, String text) {
		for (int i = 0; i < items.length; i++) {
			if (items[i].getText().contains(text)) {
				return true;
			} else {
				if (items[i].getItems().length > 0) {
					boolean has = hasChild(items[i].getItems(), text);
					if (has) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void treeExpandAll(TreeItem[] treeitem) {
		for (int i = 0; i < treeitem.length; i++) {
			treeitem[i].setExpanded(true);
			if (!treeitem[i].getText().endsWith(".xml")) {
				treeitem[i].setImage(StudioPlugin.getIcon("folder-open.gif"));
			}
			if (treeitem[i].getItems().length > 0) {
				treeExpandAll(treeitem[i].getItems());
			}
		}
	}

	public Tree getTree() {
		return foldlist;
	}

	/**
	 * Dialog点击按钮时执行的方法
	 */
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			TreeItem[] select = foldlist.getSelection();
			if (select.length > 0) {
				for (int n = 0; n < select.length; n++) {
					TreeItem nitem = select[n];
					String filename = nitem.getText();
					if (filename.toLowerCase().endsWith(".xml")) {
						selectlist = new ArrayList<String>();
						selectlist.add(filename);
					} else {
						MessageDialog.openInformation(getShell(),
								Messages.getString("editors.StrutsConfigEditor.resdialog.2"),
								Messages.getString("editors.StrutsConfigEditor.resdialog.3"));
						return;
					}
				}
				super.buttonPressed(buttonId);
				return;
			}
			MessageDialog.openInformation(getShell(), Messages.getString("editors.StrutsConfigEditor.resdialog.2"),
					Messages.getString("editors.StrutsConfigEditor.resdialog.3"));
		} else {
			super.buttonPressed(buttonId);
		}
	}
}
