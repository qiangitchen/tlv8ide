/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.tablespace.wizard;

import java.sql.SQLException;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Schema;

public class WizardPage1 extends DefaultWizardPage {

	public static final String HEADER_TABLE = Messages.getString("WizardPage1.0"); //$NON-NLS-1$

	private String[] headers = {HEADER_TABLE};

	private ISelection selection;

	zigen.plugin.db.ui.internal.Schema schemaNode;

	ITable[] tableNodes;

	public WizardPage1(ISelection selection) {
		super("wizardPage"); //$NON-NLS-1$
		this.selection = selection;
		this.schemaNode = getSelectedSchema(selection);

		setTitle(Messages.getString("WizardPage1.2")); //$NON-NLS-1$
		setDescription(Messages.getString("WizardPage1.3")); //$NON-NLS-1$
		setPageComplete(false);

	}

	private Schema getSelectedSchema(ISelection _selection) {
		Object obj = (Object) ((StructuredSelection) _selection).getFirstElement();
		if (obj instanceof Schema) {
			return (Schema) obj;
		} else {
			return null;
		}
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));

		Label label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("WizardPage1.4")); //$NON-NLS-1$

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

		String[] properties = new String[] {"check"}; //$NON-NLS-1$
		tableViewer.setColumnProperties(properties);

		CellEditor[] editors = new CellEditor[] {new CheckboxCellEditor(table)};

		tableViewer.setCellEditors(editors);

		tableViewer.setCellModifier(new WizardPage1CellModifier(this));
		tableViewer.setContentProvider(new TableContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());

		createTableItems();

		if (tableItems != null) {
			tableViewer.setInput(tableItems);
			columnsPack(table);
		}


	}

	private void setHeaderColumn(Table table, String[] headers) {

		for (int i = 0; i < headers.length; i++) {
			TableColumn col = new TableColumn(table, SWT.NONE, i);
			col.setText(headers[i]);
			col.setResizable(true);
			col.pack();

		}
	}

	private void createTableItems() {
		try {
			zigen.plugin.db.ui.internal.Table[] tables = schemaNode.getTables();
			tableItems = new TableItem[tables.length];
			for (int i = 0; i < tables.length; i++) {
				tableItems[i] = new TableItem(tables[i]);
				// tableItems[i].setDbBlockSize(dbBlockSize);
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

	boolean isSelected() {
		for (int i = 0; i < tableItems.length; i++) {
			IItem item = tableItems[i];
			if (item.isChecked()) {
				return true;
			}
		}
		return false;
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
	}

	private class TableContentProvider implements IStructuredContentProvider {

		private List contents = null;

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IItem[]) {
				return (TableItem[]) inputElement;
			}
			return null;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			contents = null;
		}

		public void dispose() {
			contents = null;
		}

	}

	public static final String CHECKED_IMAGE = "checked"; //$NON-NLS-1$

	public static final String UNCHECKED_IMAGE = "unchecked"; //$NON-NLS-1$

	private static ImageRegistry imageRegistry = new ImageRegistry();

	static {
		String iconPath = ""; //$NON-NLS-1$
		imageRegistry.put(CHECKED_IMAGE, ImageDescriptor.createFromFile(WizardPage1.class, iconPath + CHECKED_IMAGE + ".gif")); //$NON-NLS-1$
		imageRegistry.put(UNCHECKED_IMAGE, ImageDescriptor.createFromFile(WizardPage1.class, iconPath + UNCHECKED_IMAGE + ".gif")); //$NON-NLS-1$
	}

	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

		private Image getImage(boolean isSelected) {
			String key = isSelected ? CHECKED_IMAGE : UNCHECKED_IMAGE;
			return imageRegistry.get(key);
		}

		public String getColumnText(Object element, int columnIndex) {
			String result = ""; //$NON-NLS-1$
			TableItem item = (TableItem) element;
			switch (columnIndex) {
			case 0:
				result = item.getTableName();
				break;
			default:
				break;
			}
			return result;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return (columnIndex == 0) ? getImage(((TableItem) element).isChecked()) : null;
		}

		public Image getImage(Object obj) {
			return null;
		}
	}

}
