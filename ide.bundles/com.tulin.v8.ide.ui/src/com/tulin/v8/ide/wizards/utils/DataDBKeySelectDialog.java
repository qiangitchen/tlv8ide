package com.tulin.v8.ide.wizards.utils;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;

public class DataDBKeySelectDialog extends TitleAreaDialog {
	/*
	 * 数据源选择对话框
	 */
	List list;
	String[] items;
	String itemsToOpen;

	public DataDBKeySelectDialog(Shell shell) {
		super(shell);
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
		setTitle(Messages.getString("wizards.datakeyselect.title"));
		setMessage(Messages.getString("wizards.datakeyselect.message"));
	}
	
	protected int getShellStyle() {
		return super.getShellStyle() | SWT.RESIZE;
	}

	// protected Point getInitialSize() {
	// return new Point(500, 400);
	// }

	// 重写createDialogArea方法，创建对话框区域
	protected Control createDialogArea(Composite parent) {
		// 添加对话框区域的父面板
		final Composite area = new Composite(parent, SWT.FILL);
		area.setLayoutData(new GridData(GridData.FILL_BOTH));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 15;
		gridLayout.marginHeight = 10;
		// 设置父面板的布局方式
		area.setLayout(gridLayout);
		// 在对话框区域中添加List组件
		list = new List(area, SWT.BORDER | SWT.MULTI);
		final GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 3;
		gridData.widthHint = 460;
		gridData.heightHint = 200;
		list.setLayoutData(gridData);
		list.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				itemsToOpen = list.getItem(list.getSelectionIndex());
				validate();
			}
		});
		for (int i = 0; i < items.length; i++) {
			list.add(items[i]);
		}
		return area;
	}

	// 添加自定义对话框的校验方法
	private void validate() {
		boolean selected = (list.getSelectionCount() > 0);
		try {
			getButton(IDialogConstants.OK_ID).setEnabled(selected);
		} catch (Exception e) {
		}
		if (!selected)
			setErrorMessage(Messages.getString("wizards.datakeyselect.ermessage"));
		else {
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
