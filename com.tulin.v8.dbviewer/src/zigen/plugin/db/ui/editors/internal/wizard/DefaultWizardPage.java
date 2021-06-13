/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.wizard;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import zigen.plugin.db.ui.internal.Column;

abstract class DefaultWizardPage extends WizardPage {


	public DefaultWizardPage(String pageName) {
		super(pageName);

	}

	abstract public void createControl(Composite parent);

	protected void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	protected void createLine(Composite parent, int ncol) {
		Label line = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.BOLD);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = ncol;
		line.setLayoutData(gridData);
	}

	protected Composite createDefaultComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 10;
		composite.setLayout(gridLayout);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(data);

		return composite;
	}

	protected void setHeaderColumn(Table table, String[] headers) {
		for (int i = 0; i < headers.length; i++) {
			TableColumn col = new TableColumn(table, SWT.NONE, i);
			col.setText(headers[i]);
			col.setResizable(true);
			col.pack();
		}
	}

	protected void columnsPack(Table table) {
		table.setVisible(false);
		TableColumn[] cols = table.getColumns();
		for (int i = 0; i < cols.length; i++) {
			cols[i].pack();
		}
		table.setVisible(true);
	}

	public void setVisible(boolean b) {
		super.setVisible(b);
	}

	protected void generateSQL() {
		IWizardPage[] pages = getWizard().getPages();
		IWizardPage confirmpage = pages[pages.length - 1];
		if (confirmpage instanceof ConfirmDDLWizardPage) {
			ConfirmDDLWizardPage cPage = (ConfirmDDLWizardPage) confirmpage;
			cPage.generateSQL();
		}
	}

	protected class ConstraintsColumnLabelProvider extends LabelProvider implements ITableLabelProvider {

		// private ImageCacher imageCacher = ImageCacher.getInstance();

		public String getColumnText(Object element, int columnIndex) {
			String result = ""; //$NON-NLS-1$
			Column col = (Column) element;
			switch (columnIndex) {
			case 0:
				result = col.getName().trim();
				break;
			default:
				break;
			}
			return result;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}

	protected class ConstraintsColumnContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Column[]) {
				return (Column[]) inputElement;
			}
			return null;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

		public void dispose() {}

	}

}
