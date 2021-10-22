package com.tulin.v8.ide.editors.data;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import com.tulin.v8.function.editors.SelectFunction;
import com.tulin.v8.ide.editors.Messages;

class ExcuteSqlDialog extends Dialog {
	/**
	 * 构造函数
	 */
	public ExcuteSqlDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * 在这个方法里构建Dialog中的界面内容
	 */
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);

		Label funcnamel = new Label(composite, SWT.NONE);
		funcnamel.setText(Messages.getString("TLEditor.ExcuteSql.1"));
		GridData funcnamectl = new GridData();
		funcnamel.setLayoutData(funcnamectl);

		Text name = new Text(composite, SWT.BORDER | SWT.FILL);
		GridData namectl = new GridData(SWT.FILL, SWT.CENTER, true, true);
		namectl.grabExcessHorizontalSpace = true;
		namectl.grabExcessVerticalSpace = true;
		name.setLayoutData(namectl);

		Tree foldlist = new Tree(composite, SWT.BORDER | SWT.FULL_SELECTION);
		GridData foldlistctl = new GridData(SWT.FILL, SWT.CENTER, true, true);
		foldlistctl.horizontalSpan = 2;
		foldlistctl.grabExcessHorizontalSpace = true;
		foldlistctl.grabExcessVerticalSpace = true;
		foldlist.setLayoutData(foldlistctl);
		SelectFunction.loadFileTree(foldlist);
		return composite;
	}

	/**
	 * 改写这个父类Dialog的方法可以改变窗口的默认式样
	 */
	protected int getShellStyle() {
		/*
		 * super.getShellStyle()得到原有的式样 SWT.RESIZE：窗口可以变大小 SWT.MAX：窗口可最大化、最小化
		 */
		return super.getShellStyle() | SWT.RESIZE;// | SWT.MAX;
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
	public static final int APPLY_ID = 101; // 自定义“应用”按钮的ID值，一个整型常量

	protected void initializeBounds() {
		super.createButton((Composite) getButtonBar(), IDialogConstants.OK_ID,
				Messages.getString("TLEditor.button.ensure"), false);
		super.createButton((Composite) getButtonBar(),
				IDialogConstants.CANCEL_ID,
				Messages.getString("TLEditor.button.cancel"), false);
		super.initializeBounds();
	}
}