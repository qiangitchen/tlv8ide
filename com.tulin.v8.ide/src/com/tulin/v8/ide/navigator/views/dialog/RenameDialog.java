package com.tulin.v8.ide.navigator.views.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.tulin.v8.ide.navigator.views.action.Messages;

import zigen.plugin.db.ui.internal.TreeNode;

public class RenameDialog extends Dialog {
	TreeNode node;
	Text text;
	String nName;

	public RenameDialog(Shell parentShell, TreeNode node) {
		super(parentShell);
		this.node = node;
	}

	/**
	 * 在这个方法里构建Dialog中的界面内容
	 */
	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.getString("View.Action.Rename.1"));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());
		Composite topComp = new Composite(container, SWT.NONE);
		topComp.setToolTipText(Messages.getString("View.Action.Rename.3"));
		topComp.setLayout(new GridLayout(2, false));
		// 加入一个文本标签
		Label label = new Label(topComp, SWT.NONE);
		label.setText(Messages.getString("View.Action.NewTable.3"));
		label.setLayoutData(new GridData(SWT.NONE));
		// 加入一个文本框
		text = new Text(topComp, SWT.BORDER);
		// 用RowData来设置文本框的长度
		text.setText(this.node.getName());
		GridData ttlay = new GridData(GridData.FILL_HORIZONTAL);
		ttlay.grabExcessHorizontalSpace = true;
		ttlay.minimumWidth = 300;
		text.setLayoutData(ttlay);
		return topComp;
	}

	/**
	 * 改写这个父类Dialog的方法可以改变窗口的默认式样
	 */
	protected int getShellStyle() {
		return super.getShellStyle() | SWT.RESIZE;
	}

	/**
	 * 改写父类创建按钮的方法，使其失效
	 */
	protected Button createButton(Composite parent, int buttonId,
			String buttonText, boolean defaultButton) {
		return null;
	}

	/**
	 * 改写父类的initializeBounds方法，并利用父类的createButton方法建立按钮
	 */
	protected void initializeBounds() {
		super.createButton((Composite) getButtonBar(), IDialogConstants.OK_ID,
				Messages.getString("View.Action.PropertyViewer.19"), false);
		super.createButton((Composite) getButtonBar(),
				IDialogConstants.CANCEL_ID,
				Messages.getString("View.Action.PropertyViewer.20"), false);
		super.initializeBounds();
	}

	/**
	 * Dialog点击按钮时执行的方法
	 */
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			nName = text.getText();
		}
		super.buttonPressed(buttonId);
	}

	public String getNewName() {
		return nName;
	}

}
