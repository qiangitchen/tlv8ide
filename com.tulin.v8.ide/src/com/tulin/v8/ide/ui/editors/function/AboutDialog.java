package com.tulin.v8.ide.ui.editors.function;

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

class AboutDialog extends Dialog {
	/**
	 * 构造函数
	 */
	public AboutDialog(Shell parentShell) {
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
		funcnamel.setText(Messages.getString("editors.FunEditor.fname"));
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
		/*
		 * createButton(Composite parent, int id, String label,boolean
		 * defaultButton) 参数parent：取得放置按钮的面板， 参数id：定义按钮的id值 参数label：按钮上的文字
		 * 参数defaultButton：是否为Dialog的默认按钮
		 */
		// super.createButton((Composite) getButtonBar(), APPLY_ID, "应用", true);
		super.createButton((Composite) getButtonBar(), IDialogConstants.OK_ID,
				Messages.getString("editors.FunEditor.dm"), false);
		super.createButton((Composite) getButtonBar(),
				IDialogConstants.CANCEL_ID,
				Messages.getString("editors.FunEditor.cl"), false);
		super.initializeBounds();
	}
}