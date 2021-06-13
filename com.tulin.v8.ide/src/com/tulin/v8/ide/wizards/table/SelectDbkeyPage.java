package com.tulin.v8.ide.wizards.table;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import com.tulin.v8.core.Configuration;

public class SelectDbkeyPage extends WizardPage {
	List list;
	String[] items;
	String itemsToOpen;

	protected SelectDbkeyPage() {
		super("dbselect");
		Map<String, Map<String, String>> rm = Configuration.getConfig();
		Set<String> k = rm.keySet();
		Iterator<String> it = k.iterator();
		String[] dbkeys = new String[k.size()];
		int i = 0;
		while (it.hasNext()) {
			String key = (String) it.next();
			dbkeys[i] = key;
			i++;
		}
		items = dbkeys;
		setTitle("选择数据源");
		setMessage("在列表框中选择自己需要创建表的数据源.");
	}

	@Override
	public void createControl(Composite parent) {
		// 在对话框区域中添加List组件
		list = new List(parent, SWT.BORDER | SWT.MULTI);
		final GridData gridData = new GridData(GridData.FILL_BOTH);
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

		setControl(list);
	}

	private void validate() {
		// TODO Auto-generated method stub

	}

	public String getDBKey() {
		return this.itemsToOpen;
	}

	@Override
	public IWizardPage getNextPage() {
		return null;

	}
}
