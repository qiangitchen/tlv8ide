/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.dialogs;

import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import zigen.plugin.db.core.DBConfig;
import zigen.plugin.db.core.IDBConfig;

abstract class DefaultWizardPage extends WizardPage {

	protected int LEVEL_FIELD_WIDTH = 20;

	protected int TEXT_FIELD_WIDTH = 50;

	protected int HEIGHT_HINT = 150;

	protected int WIDTH_HINT = 450;

	protected int BUTTON_WIDTH = 100;

	protected int BUTTON_WIDTH2 = 150;

	protected String DEFAULT_NAME = Messages.getString("DefaultWizardPage.0"); //$NON-NLS-1$

	protected String DEFAULT_URL = ""; //$NON-NLS-1$

	protected String DEFAULT_USERID = ""; //$NON-NLS-1$

	protected String DEFAULT_PASS = ""; //$NON-NLS-1$

	protected String DEFAULT_CHARSET = ""; //$NON-NLS-1$

	protected String DEFAULT_SCHEMA = ""; //$NON-NLS-1$

	protected int DEFAULT_JDBC_TYPE = DBConfig.JDBC_DRIVER_TYPE_4;

	protected boolean DEFAULT_CONVERTUNICODE = true;

	protected boolean DEFAULT_AUTOCOMMIT = false;

	protected boolean ONLY_DEFAULT_SCHEMA = false;

	protected boolean DEFAULT_SAVEPASSWORD = false;

	protected boolean DEFAULT_NO_LOCK_MODE = true;


	public DefaultWizardPage(String pageName) {
		super(pageName);

	}

	abstract public void createControl(Composite parent);

	protected int computeMinimumColumnWidth(GC gc, String string) {
		return gc.stringExtent(string).x + 10; // pad 10 to accommodate table
		// header trimmings
	}

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
			// col.pack();
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

	protected IDBConfig getOldConfig() {
		DBConfigWizard wiz = (DBConfigWizard) getWizard();
		return wiz.oldConfig;
	}


	protected void resize() {
		IWizardContainer con = getContainer();
		int width = con.getShell().getSize().x;
		con.getShell().pack();
		int height = con.getShell().getSize().y;
		con.getShell().setSize(width, height);
	}
}
