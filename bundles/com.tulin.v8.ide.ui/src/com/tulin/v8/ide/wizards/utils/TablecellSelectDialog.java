package com.tulin.v8.ide.wizards.utils;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.ide.wizards.Messages;

import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;

public class TablecellSelectDialog extends TitleAreaDialog {
	/*
	 * 库表选择对话框
	 */
	private Table table = null;
	String dbkey;
	String tablename;
	String[] items;
	String itemsToOpen;

	public TablecellSelectDialog(Shell shell, String dbkey, String table) {
		super(shell);
		this.dbkey = dbkey;
		this.tablename = table;
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
	// return new Point(500, 400);
	// }

	// 重写createDialogArea方法，创建对话框区域
	protected Control createDialogArea(Composite parent) {
		// 添加对话框区域的父面板
		final Composite area = new Composite(parent, SWT.NONE);
		area.setLayoutData(new GridData(GridData.FILL_BOTH));
		area.setLayout(new GridLayout());
		// 在对话框区域中添加List组件
		table = new Table(area, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableColumn tablecolumn1 = new TableColumn(table, SWT.NONE);
		tablecolumn1.setWidth(160);
		tablecolumn1.setText(Messages.getString("wizards.dataselect.message.dataclumn"));
		TableColumn tablecolumn2 = new TableColumn(table, SWT.NONE);
		tablecolumn2.setWidth(100);
		tablecolumn2.setText(Messages.getString("wizards.dataselect.message.datatype"));
		TableColumn tablecolumn3 = new TableColumn(table, SWT.NONE);
		tablecolumn3.setWidth(200);
		tablecolumn3.setText(Messages.getString("wizards.dataselect.message.datatdesc"));
		final GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.widthHint = 460;
		gridData.heightHint = 200;
		table.setLayoutData(gridData);
		List<String[]> columnlist;
		try {
			columnlist = CommonUtil.getTableColumn(dbkey, tablename);
			for (int i = 0; i < columnlist.size(); i++) {
				TableItem tableitem = new TableItem(table, SWT.NONE);
				tableitem.setText(columnlist.get(i));
			}
		} catch (Exception e1) {
			setMessage(e1.toString());
		}
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem item = (TableItem) e.item;
				itemsToOpen = item.getText();
				validate();
			}
		});

		return area;
	}

	// 添加自定义对话框的校验方法
	private void validate() {
		boolean selected = (itemsToOpen != null && !"".equals(itemsToOpen));
		getButton(IDialogConstants.OK_ID).setEnabled(selected);
		if (!selected) {
			setErrorMessage(Messages.getString("wizards.dataselect.message.unselectedColumn"));
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