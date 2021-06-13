/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.wizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.editors.event.TextSelectionListener;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public class ColumnWizardPage extends DefaultWizardPage {

	public static final String MSG_DSC = ""; //$NON-NLS-1$

	protected ISQLCreatorFactory factory;

	protected ITable table;

	private Column column;

	private Column newColumn;

	Text txtColumnName;

	Text txtColumnComment;

	Text txtDefualtt;

	Combo cmbColumnType;

	Text txtColumnSize;

	Button chkNotNull;

	boolean isAddColumn = false;

	public ColumnWizardPage(ISQLCreatorFactory factory, ITable table, Column column, boolean isAddColumn) {
		super("wizardPage"); //$NON-NLS-1$
		if (isAddColumn) {
			super.setTitle(Messages.getString("ColumnWizardPage1.2")); //$NON-NLS-1$
		} else {
			super.setTitle(Messages.getString("ColumnWizardPage1.3")); //$NON-NLS-1$
		}

		this.factory = factory;
		this.table = table;
		this.column = column;
		this.isAddColumn = isAddColumn;

	}

	public void createControl(Composite parent) {
		Composite container = createDefaultComposite(parent);
		createNameConstrol(container);
		// createColumnSelectConstrol(container);
		// createOptionControl(container);
		setControl(container);
	}

	protected void createNameConstrol(Composite container) {
		Composite composite = new Composite(container, SWT.NULL);
		composite.setLayout(new GridLayout(4, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label label = new Label(composite, SWT.NULL);
		label.setText(Messages.getString("ColumnWizardPage1.4")); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);

		txtColumnName = new Text(composite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		txtColumnName.setLayoutData(gd);
		txtColumnName.setText(column.getName());
		txtColumnName.addFocusListener(new TextSelectionListener());
		if (!isAddColumn & DBType.DB_TYPE_DERBY == DBType.getType(table.getDbConfig())) {
			txtColumnName.setEnabled(false);
		}

		label = new Label(composite, SWT.NULL);
		label.setText(Messages.getString("ColumnWizardPage1.5")); //$NON-NLS-1$
		// txtColumnComment = new Text(composite, SWT.BORDER);

		txtColumnComment = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);

		// txtTableComment.setLayoutData(new
		// GridData(GridData.FILL_HORIZONTAL));

		GridData gridData2 = new GridData();
		gridData2.horizontalSpan = 3;
		gridData2.horizontalAlignment = GridData.FILL;
		// gridData2.verticalAlignment = GridData.FILL;

		GC gc = new GC(txtColumnComment);
		gc.setFont(txtColumnComment.getFont());
		FontMetrics fm = gc.getFontMetrics();
		// widthHint = fm.getAverageCharWidth() * charWidth;
		// heightHint = fm.getHeight();

		gridData2.heightHint = fm.getHeight() * 3;
		// gridData2.grabExcessHorizontalSpace = true;
		// gridData2.grabExcessVerticalSpace = true;
		txtColumnComment.setLayoutData(gridData2);


		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		// txtColumnComment.setLayoutData(gd);

		if (factory.supportsRemarks()) {
			txtColumnComment.setText(column.getRemarks());
			txtColumnComment.addFocusListener(new TextSelectionListener());
		} else {
			txtColumnComment.setEnabled(false);
		}

		label = new Label(composite, SWT.NULL);
		label.setText(Messages.getString("ColumnWizardPage1.6")); //$NON-NLS-1$

		// cmbColumnType = new Combo(group, SWT.READ_ONLY);
		cmbColumnType = new Combo(composite, SWT.NONE);
		// gd = new GridData(GridData.FILL_HORIZONTAL);
		gd = new GridData();
		gd.horizontalSpan = 3;
		cmbColumnType.setLayoutData(gd);

		cmbColumnType.setVisibleItemCount(20);
		for (int i = 0; i < factory.getSupportColumnType().length; i++) {
			cmbColumnType.add((factory.getSupportColumnType()[i]));
		}


		cmbColumnType.setText(column.getTypeName());

		if (cmbColumnType.getSelectionIndex() > 0) {
			cmbColumnType.add(column.getTypeName());
		}

		if (!isAddColumn && !factory.supportsModifyColumnType()) {
			cmbColumnType.setEnabled(false);
		}

		label = new Label(composite, SWT.NULL);
		label.setText(Messages.getString("ColumnWizardPage1.7")); //$NON-NLS-1$
		txtColumnSize = new Text(composite, SWT.BORDER);
		txtColumnSize.addFocusListener(new TextSelectionListener());

		gd = new GridData();
		gd.widthHint = 100;
		txtColumnSize.setLayoutData(gd);
		txtColumnSize.setText(column.getSize());

		if (!isAddColumn && !factory.supportsModifyColumnSize(cmbColumnType.getText())) {
			txtColumnSize.setEnabled(false);
		}

		chkNotNull = new Button(composite, SWT.CHECK);
		chkNotNull.setText(Messages.getString("ColumnWizardPage1.8")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		chkNotNull.setLayoutData(gd);
		chkNotNull.setSelection(column.isNotNull());

		label = new Label(composite, SWT.NULL);
		label.setText(Messages.getString("ColumnWizardPage1.9")); //$NON-NLS-1$
		txtDefualtt = new Text(composite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		txtDefualtt.setLayoutData(gd);
		txtDefualtt.addFocusListener(new FocusAdapter() {

			public void focusLost(FocusEvent e) {
			// updateColumn();
			}
		});
		txtDefualtt.setText(column.getDefaultValue());
		txtDefualtt.addFocusListener(new TextSelectionListener());


		Label dummy = new Label(composite, SWT.NULL);
		Label label2 = new Label(composite, SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		txtDefualtt.setLayoutData(gd);
		label2.setLayoutData(gd);
		label2.setText(Messages.getString("ColumnWizardPage1.10")); //$NON-NLS-1$


		txtColumnName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		txtColumnComment.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				validate();
			}
		});

		cmbColumnType.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				validate();
				if (txtColumnSize != null) {
					txtColumnSize.setEnabled(factory.isVisibleColumnSize(cmbColumnType.getText()));
				}
			}
		});
		cmbColumnType.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				validate();
				if (txtColumnSize != null) {
					txtColumnSize.setEnabled(factory.isVisibleColumnSize(cmbColumnType.getText()));
				}
			}
		});

		txtColumnSize.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		chkNotNull.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				validate();
			}

		});

		txtDefualtt.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				validate();
			}
		});


		cmbColumnType.notifyListeners(SWT.Selection, null);
	}

	protected void createOptionControl(Composite container) {
		;
	}

	private void validate() {
		if ("".equals(txtColumnName.getText())) { //$NON-NLS-1$
			updateStatus(Messages.getString("ColumnWizardPage1.12")); //$NON-NLS-1$
		} else if ("".equals(cmbColumnType.getText())) { //$NON-NLS-1$
			updateStatus(Messages.getString("ColumnWizardPage1.14")); //$NON-NLS-1$
		} else {

			if (modified()) {
				updateStatus(null);
			} else {
				setPageComplete(false);
			}
		}
	}


	private boolean modified() {
		if (!column.getName().equals(txtColumnName.getText().trim()) || !column.getRemarks().equals(txtColumnComment.getText().trim())
				|| !column.getTypeName().equals(cmbColumnType.getText()) || !column.getSize().equals(txtColumnSize.getText().trim())
				|| column.isNotNull() != chkNotNull.getSelection() || !column.getDefaultValue().equals(txtDefualtt.getText().trim())) {
			return true;
		} else {
			return false;
		}
	}

	protected void setHeaderColumn(Table table) {
		TableColumn col1 = new TableColumn(table, SWT.NONE);
		col1.setText("dummy"); //$NON-NLS-1$
		col1.pack();

	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			setDescription(MSG_DSC);
		}
	}

}
