/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.wizard;

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
import zigen.plugin.db.core.IDBConfig;

public class ImpWizardPage1 extends DefaultWizardPage {

	private final String LINE_SEP = System.getProperty("line.separator"); //$NON-NLS-1$

	public static final String MSG = Messages.getString("ImpWizardPage1.1"); //$NON-NLS-1$

	private String[] headers = {Messages.getString("ImpWizardPage1.2"), Messages.getString("ImpWizardPage1.3")}; //$NON-NLS-1$ //$NON-NLS-2$

	private IDBConfig[] configs;

	public ImpWizardPage1(IDBConfig[] configs) {
		super("wizardPage"); //$NON-NLS-1$
		this.configs = configs;
		setTitle(Messages.getString("ImpWizardPage1.5")); //$NON-NLS-1$
		setDescription(MSG);
		setPageComplete(false);

	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));

		Label label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("ImpWizardPage1.6")); //$NON-NLS-1$

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

		tableViewer.setCellModifier(new ImpWizardPage1CellModifier(this));
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
			tableItems = new TableItem[configs.length];
			for (int i = 0; i < configs.length; i++) {
				tableItems[i] = new TableItem(configs[i], false);
			}
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

}
