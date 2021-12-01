package com.tulin.v8.ide.wizards.table;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableItem;

import com.tulin.v8.ide.wizards.utils.AddcolumnDialog;

public class TableWritePage extends AbsolutelyTableCreate {
	private String text;
	private String name;
	private String datatype;
	private Action ensureAction;

	public TableWritePage(SelectDbkeyPage dbselect) {
		super(dbselect);
	}

	public TableWritePage(String dbkey, String owner) {
		super(dbkey, owner);
	}

	// 添加
	public void addcolumn() {
		ensureAction = new Action() {
			public void run() {
				TableItem item = new TableItem(celltable, SWT.NONE);
				item.setText(new String[] { "", name, datatype, "100", text });
				changeComplete();
			}
		};
		AddcolumnDialog dial = new AddcolumnDialog(getShell());
		int result = dial.open();
		if (IDialogConstants.OK_ID == result) {
			name = dial.namev;
			text = dial.textv;
			datatype = dial.datatypev;
			ensureAction.run();
		}
	}

}
