package com.tulin.v8.ide.wizards.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.tulin.v8.core.StringArray;
import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.ide.wizards.DataSelectPage;
import com.tulin.v8.ide.wizards.Messages;

import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;

public class TableCellsSelectDialog extends TitleAreaDialog {
	/*
	 * 库表选择对话框
	 */
	private Table table = null;
	public Map<String, IDBConfig> DBConfig = new HashMap<String, IDBConfig>();
	String dbkey;
	String tablename;
	String[] items;
	String itemsToOpen;
	Text searchtext;
	Map<String, String> dataType = new HashMap<String, String>();
	Map<String, String> label = new HashMap<String, String>();

	public TableCellsSelectDialog(Shell shell, String dbkey, String table) {
		super(shell);
		this.dbkey = dbkey;
		this.tablename = table;
		IDBConfig[] dbConfigs = DBConfigManager.getDBConfigs();
		String[] dbkeys = new String[dbConfigs.length];
		for (int i = 0; i < dbConfigs.length; i++) {
			dbkeys[i] = dbConfigs[i].getDbName();
			DBConfig.put(dbkeys[i], dbConfigs[i]);
		}
		items = dbkeys;
	}

	public void create() {
		super.create();
		getShell().setText("TuLin Studio");
		setTitle(Messages.getString("wizards.dataselect.message.title"));
		setMessage(Messages.getString("wizards.dataselect.message.unselectedColumn"));
	}

	protected int getShellStyle() {
		return super.getShellStyle() | SWT.RESIZE;
	}

	// protected Point getInitialSize() {
	// int width = 500;
	// int height = 380;
	// if (CommonUtil.isLinuxOS()) {
	// height = 420;
	// }
	// return new Point(width, height);
	// }

	// 重写createDialogArea方法，创建对话框区域
	protected Control createDialogArea(Composite parent) {
		// 添加对话框区域的父面板
		final Composite area = new Composite(parent, SWT.FILL);
		area.setLayoutData(new GridData(GridData.FILL_BOTH));
		final GridLayout gridLayout = new GridLayout(3, false);
		// 设置父面板的布局方式
		area.setLayout(gridLayout);
		// 在对话框区域中添加List组件

		ToolBar toolbar = new ToolBar(area, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		GridData toolbarcl = new GridData(SWT.NONE);
		toolbar.setLayoutData(toolbarcl);

		ToolItem selectallitem = new ToolItem(toolbar, SWT.PUSH | SWT.BORDER);
		selectallitem.setWidth(160);
		selectallitem.setText(Messages.getString("wizards.dataselect.message.checkAll"));
		ToolItem cancelitem = new ToolItem(toolbar, SWT.PUSH | SWT.BORDER);
		cancelitem.setWidth(160);
		cancelitem.setText(Messages.getString("wizards.dataselect.message.uncheckAll"));

		searchtext = new Text(area, SWT.BORDER);
		GridData textcl = new GridData(GridData.FILL_HORIZONTAL);
		// textcl.widthHint = 330;
		textcl.grabExcessHorizontalSpace = true;
		searchtext.setLayoutData(textcl);
		searchtext.setEditable(true);
		searchtext.setText("");

		Button searchBtn = new Button(area, SWT.PUSH);
		GridData btncl = new GridData(SWT.NONE);
		btncl.widthHint = 80;
		searchBtn.setLayoutData(btncl);
		searchBtn.setText(Messages.getString("wizards.dataselect.message.search"));

		table = new Table(area, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.CHECK);
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
		// gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 3;
		gridData.widthHint = 460;
		gridData.heightHint = 250;
		table.setLayoutData(gridData);
		List<String[]> columnlist;
		try {
			columnlist = CommonUtil.getTableColumn(dbkey, DBConfig.get(dbkey).getSchema(), tablename);
			for (int i = 0; i < columnlist.size(); i++) {
				TableItem tableitem = new TableItem(table, SWT.NONE);
				tableitem.setText(columnlist.get(i));
			}
		} catch (Exception e1) {
			// setMessage(e1.toString());
		}
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] selected = table.getItems();
				if (selected.length > 0) {
					StringArray arry = new StringArray();
					for (int i = 0; i < selected.length; i++) {
						if (selected[i].getChecked()) {
							TableItem tableitem = selected[i];
							arry.push(tableitem.getText(0));
							dataType.put(tableitem.getText(0), tableitem.getText(1));
							label.put(tableitem.getText(0), tableitem.getText(2) == null ? "" : tableitem.getText(2));
						}
					}
					itemsToOpen = arry.join(",");
				} else {
					// setMessage("请选择需要的列.");
				}
				validate();
			}
		});

		// 全选
		selectallitem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getItems();
				StringArray arry = new StringArray();
				for (int i = 0; i < items.length; i++) {
					items[i].setChecked(true);
					TableItem tableitem = items[i];
					arry.push(tableitem.getText(0));
					dataType.put(tableitem.getText(0), tableitem.getText(1));
					label.put(tableitem.getText(0), tableitem.getText(2) == null ? "" : tableitem.getText(2));
				}
				itemsToOpen = arry.join(",");
			}
		});

		// 全取消
		cancelitem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getItems();
				StringArray arry = new StringArray();
				for (int i = 0; i < items.length; i++) {
					items[i].setChecked(false);
				}
				itemsToOpen = arry.join(",");
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
		table.removeAll();
		List<String[]> columnlist = new ArrayList<String[]>();
		if ("".equals(str)) {
			try {
				columnlist = CommonUtil.getTableColumn(dbkey, DBConfig.get(dbkey).getSchema(), tablename);
			} catch (Exception e1) {
				setMessage(e1.toString());
			}
		} else {
			try {
				columnlist = DataSelectPage.getTableColumn(dbkey, DBConfig.get(dbkey).getSchema(), tablename, str);
			} catch (Exception e1) {
				setMessage(e1.toString());
			}
		}
		for (int i = 0; i < columnlist.size(); i++) {
			TableItem tableitem = new TableItem(table, SWT.NONE);
			tableitem.setText(columnlist.get(i));
		}
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

	public Map<String, String> getdataType() {
		return dataType;
	}

	public Map<String, String> getLabel() {
		return label;
	}
}