/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.wizard;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Root;

public class ExpWizardPage1 extends DefaultWizardPage {

	public static final String MSG = Messages.getString("ExpWizardPage1.0"); //$NON-NLS-1$

	private String[] headers = {Messages.getString("ExpWizardPage1.1"), Messages.getString("ExpWizardPage1.2")}; //$NON-NLS-1$ //$NON-NLS-2$

	Root root;

	public ExpWizardPage1(Root root) {
		super("wizardPage"); //$NON-NLS-1$
		this.root = root;
		setTitle(Messages.getString("ExpWizardPage1.4")); //$NON-NLS-1$
		setDescription(MSG);
		setPageComplete(false);

	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		Label label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("ExpWizardPage1.5")); //$NON-NLS-1$
		createTable(container);
		setControl(container);
	}

	private void createTable(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = HEIGHT_HINT;
		gridData.widthHint = WIDTH_HINT;

		composite.setLayout(layout);
		composite.setLayoutData(gridData);

		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLayoutData(gridData);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		setHeaderColumn(table, headers);

		gridData = new GridData(GridData.FILL_BOTH);
		tableViewer.getControl().setLayoutData(gridData);

		String[] properties = new String[] {"check", ""}; //$NON-NLS-1$ //$NON-NLS-2$
		tableViewer.setColumnProperties(properties);

		CellEditor[] editors = new CellEditor[] {new CheckboxCellEditor(table), null};

		tableViewer.setCellEditors(editors);

		tableViewer.setCellModifier(new ExpWizardPage1CellModifier(this));
		tableViewer.setContentProvider(new TableContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());

		createTableItems();

		if (tableItems != null) {
			tableViewer.setInput(tableItems);
			columnsPack(table);
		}

		createSelectBtn(composite);
	}

	private void createTableItems() {
		try {
			List list = root.getChildren();
			tableItems = new TableItem[list.size()];
			int i = 0;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Object child = (Object) iterator.next();
				if (child instanceof DataBase) {
					DataBase db = (DataBase) child;
					// tableItems[i] = new TableItem(db.getDbConfig(), true);
					tableItems[i] = new TableItem(db.getDbConfig(), false);
					// False
					i++;
				}
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

}
