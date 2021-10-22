/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.tablespace.wizard;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ext.oracle.tablespace.CSVResultWriter;
import zigen.plugin.db.ext.oracle.tablespace.CalcIndexSpace;
import zigen.plugin.db.ext.oracle.tablespace.CalcIndexSpaces;
import zigen.plugin.db.ext.oracle.tablespace.CalcTableSpace;

public class WizardPage3 extends DefaultWizardPage {

	public static final String HEADER_SCHEMANAME = Messages.getString("WizardPage3.0"); //$NON-NLS-1$

	public static final String HEADER_TABLENAME = Messages.getString("WizardPage3.1"); //$NON-NLS-1$

	public static final String HEADER_INDEXNAME = Messages.getString("WizardPage3.2"); //$NON-NLS-1$

	public static final String HEADER_PCTFREE = Messages.getString("WizardPage3.3"); //$NON-NLS-1$

	public static final String HEADER_RECORD = Messages.getString("WizardPage3.4"); //$NON-NLS-1$

	public static final String HEADER_TABLESPACESIZE = Messages.getString("WizardPage3.5"); //$NON-NLS-1$

	public static final String HEADER_SAFECONEFFICIENT = Messages.getString("WizardPage3.6"); //$NON-NLS-1$

	public static final String HEADER_TOTALTABLESPACESIZE = Messages.getString("WizardPage3.7"); //$NON-NLS-1$

	public static final int COLUMN_TABLENAME = 0;

	public static final int COLUMN_INDEXNAME = 1;

	public static final int COLUMN_PCTFREE = 2;

	public static final int COLUMN_RECORD = 3;

	public static final int COLUMN_TABLESPACESIZE = 4;

	public static final int COLUMN_SAFECONEFFICIENT = 5;

	public static final int COLUMN_TOTALTABLESPACESIZE = 6;

	public static final String[] headers = {HEADER_TABLENAME, HEADER_INDEXNAME, HEADER_PCTFREE, HEADER_RECORD, HEADER_TABLESPACESIZE, HEADER_SAFECONEFFICIENT,
			HEADER_TOTALTABLESPACESIZE};

	private Text dbBlockSizeText;

	private int dbBlockSize = 0;

	public WizardPage3() {
		super("wizardPage"); //$NON-NLS-1$

		setTitle(Messages.getString("WizardPage3.9")); //$NON-NLS-1$
		setDescription(Messages.getString("WizardPage3.10") + Messages.getString("WizardPage3.11")); //$NON-NLS-1$ //$NON-NLS-2$
		setPageComplete(false);

	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));

		Composite composite = createDefaultComposite(container);
		Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setText(Messages.getString("WizardPage3.12")); //$NON-NLS-1$
		// nameLabel.setLayoutData(getGridData(LEVEL_FIELD_WIDTH));
		dbBlockSizeText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		// dbBlockSizeText.setEnabled(false);
		dbBlockSizeText.setEditable(false);

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

		gridData = new GridData(GridData.FILL_BOTH);
		tableViewer.getControl().setLayoutData(gridData);

		Table table = tableViewer.getTable();
		table.setLayoutData(gridData);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		setHeaderColumn(table, headers);

		tableViewer.setContentProvider(new TableContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());

		// Create buttons
		final Button exportCsvBtn = new Button(composite, SWT.PUSH);
		exportCsvBtn.setText(Messages.getString("WizardPage3.13")); //$NON-NLS-1$

		exportCsvBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				exportCsvButtonPressedHandler();
			}
		});

		if (tableItems != null) {
			tableViewer.setInput(tableItems);
			columnsPack(table);
		}

	}

	void exportCsvButtonPressedHandler() {
		String[] headers = {HEADER_SCHEMANAME, HEADER_TABLENAME, HEADER_INDEXNAME, WizardPage2.HEADER_BLOCKSIZE, WizardPage2.HEADER_PCTFREE, HEADER_RECORD, HEADER_TABLESPACESIZE,
				HEADER_SAFECONEFFICIENT, HEADER_TOTALTABLESPACESIZE};

		CSVResultWriter writer = new CSVResultWriter();
		writer.setHeaders(headers);
		writer.setAppend(false);

		Shell shell = DbPlugin.getDefault().getShell();
		File file = saveCsv(shell, null);

		if (file.exists()) {
			if (!confirmOverwrite(shell, file.getName())) {
				return;
			}
		}

		try {
			writer.execute(file, csvResultList);
			DbPlugin.getDefault().showInformationMessage(Messages.getString("WizardPage3.15")); //$NON-NLS-1$

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

	private File saveCsv(Shell shell, String defaultFileName) {
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setFileName(defaultFileName);
		dialog.setFilterExtensions(new String[] {"*.csv", "*.*"}); //$NON-NLS-1$ //$NON-NLS-2$
		dialog.setFilterNames(new String[] {Messages.getString("WizardPage3.18"), Messages.getString("WizardPage3.19")}); //$NON-NLS-1$ //$NON-NLS-2$
		String fileName = dialog.open();
		// File file = new File(fileName);
		if (fileName != null) {
			return new File(fileName);
		} else {
			return null;
		}
	}

	private boolean confirmOverwrite(Shell shell, String fileName) {
		MessageBox msg = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
		msg.setMessage(Messages.getString("WizardPage3.20") + fileName + Messages.getString("WizardPage3.21")); //$NON-NLS-1$ //$NON-NLS-2$
		msg.setText(Messages.getString("WizardPage3.22")); //$NON-NLS-1$
		int res = msg.open();
		if (res == SWT.YES) {
			return true;
		} else {
			return false;
		}
	}

	private void setHeaderColumn(Table table, String[] headers) {

		for (int i = 0; i < headers.length; i++) {
			TableColumn col;
			if (headers[i].equals(WizardPage3.HEADER_TABLENAME) || headers[i].equals(WizardPage3.HEADER_INDEXNAME)) {
				col = new TableColumn(table, SWT.NONE, i);

			} else {
				col = new TableColumn(table, SWT.RIGHT, i);
			}

			col.setText(headers[i]);
			col.setResizable(true);
			// column.setWidth(200);
			col.pack();

		}
	}

	private List csvResultList = null;

	private IItem[] calcurate(TableItem[] tableItems) {
		List result = new ArrayList();
		csvResultList = new ArrayList();

		try {
			for (int i = 0; i < tableItems.length; i++) {
				TableItem item = tableItems[i];

				if (!item.isChecked())
					continue;
				CalcTableSpace tableSpace = new CalcTableSpace(item.table, dbBlockSize, item.getPctFree(), item.getRecordSize());
				tableSpace.calcurate();

				item.setCalcTableSpace(tableSpace);

				result.add(item);
				csvResultList.add(tableSpace.getCsvRow());

				CalcIndexSpaces indexSpaces = new CalcIndexSpaces(item.table, dbBlockSize, item.getPctFree(), item.getRecordSize());
				indexSpaces.calcurate();

				CalcIndexSpace[] spaces = indexSpaces.getCalcIndexSpaces();
				for (int k = 0; k < spaces.length; k++) {
					CalcIndexSpace space = spaces[k];
					IndexItem indexItem = new IndexItem(item.table, space.getIndexName(), space.getColumns());

					indexItem.setCalcIndexSpace(space);

					result.add(indexItem);
					csvResultList.add(space.getCsvRow());

				}

			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

		return (IItem[]) result.toArray(new IItem[0]);
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			CalcTableSpaceWizard wiz = (CalcTableSpaceWizard) getWizard();
			WizardPage2 page = (WizardPage2) wiz.getPreviousPage(this);
			tableItems = page.tableItems;
			if (tableItems != null) {

				String str = page.dbBlockSizeText.getText();
				if (str != null && !"".equals(str)) { //$NON-NLS-1$
					this.dbBlockSize = Integer.parseInt(str);
					dbBlockSizeText.setText(str);
				}
				tableViewer.setInput(calcurate(tableItems));
				columnsPack(tableViewer.getTable());
				setPageComplete(true);
			}
		}

	}

	private class TableContentProvider implements IStructuredContentProvider {

		private List contents = null;

		public Object[] getElements(Object inputElement) {
			return (IItem[]) inputElement;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			contents = null;
		}

		public void dispose() {
			contents = null;
		}

	}

	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object element, int columnIndex) {
			String result = ""; //$NON-NLS-1$
			IItem item = (IItem) element;
			switch (columnIndex) {
			case COLUMN_TABLENAME:
				result = item.getTableName();
				break;
			case COLUMN_INDEXNAME:
				if (item instanceof IndexItem) {
					result = item.getIndexName();
				}
				break;
			case COLUMN_PCTFREE:
				result = String.valueOf(item.getPctFree());
				break;

			case COLUMN_RECORD:
				result = String.valueOf(item.getRecordSize());
				break;

			case COLUMN_TABLESPACESIZE:
				result = String.valueOf(item.getTableSpaceSize());
				break;
			case COLUMN_SAFECONEFFICIENT:
				result = String.valueOf(item.getSafeCoefficient());
				break;

			case COLUMN_TOTALTABLESPACESIZE:
				result = String.valueOf(item.getTableSpaceSafeSize());
				break;

			default:
				break;
			}
			return result;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public Image getImage(Object obj) {
			return null;
		}
	}

}
