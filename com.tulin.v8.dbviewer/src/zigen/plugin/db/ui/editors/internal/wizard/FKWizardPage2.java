/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.wizard;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import zigen.plugin.db.core.ColumnSeqSorter;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.core.TableSearcher;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.DefaultColumnSearcherFactory;
import zigen.plugin.db.core.rule.IColumnSearcherFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public class FKWizardPage2 extends PKWizardPage {

	public static final String MSG_DSC = Messages.getString("FKWizardPage2.0"); //$NON-NLS-1$

	private static final String MSG_REQUIRE_TABLE = Messages.getString("FKWizardPage2.1"); //$NON-NLS-1$

	private static final String MSG_REQUIRE_COLUMN = Messages.getString("FKWizardPage2.2"); //$NON-NLS-1$

	private static final String MSG_OVER_COLUMN = Messages.getString("FKWizardPage2.3"); //$NON-NLS-1$

	// protected ISQLCreatorFactory factory;
	//
	protected IDBConfig config;

	//
	// protected ITable tableNode;

	// private Column[] columns;

	protected Text targetTxt;

	protected Combo comboRefTable;

	protected Button chkDeleteCascade;

	protected List refTableList = new ArrayList();

	// protected List selectedList = new ArrayList();
	//
	// protected List columnList = new ArrayList();
	//
	// private TableViewer selectedColumnViewer;
	//
	// private TableViewer columnViewer;

	public FKWizardPage2(ISQLCreatorFactory factory, ITable table) {
		// super("wizardPage");
		super(factory, table);
		setTitle(Messages.getString("FKWizardPage2.4")); //$NON-NLS-1$
		// this.factory = factory;
		// this.tableNode = table;
		this.config = table.getDbConfig();

		setPageComplete(false);

		columnList = new ArrayList();

		createTableList();

	}

	protected void createTableList() {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			TableInfo[] tables = TableSearcher.execute(con, tableNode.getSchemaName(), new String[] {"TABLE"}); //$NON-NLS-1$
			for (int i = 0; i < tables.length; i++) {
				TableInfo info = tables[i];
				refTableList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void createColumnList(String tableName) {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			// boolean isConvertUnicode = config.isConvertUnicode();

			// zigen.plugin.db.core.TableColumn[] columns = ColumnSearcher.execute(con, tableNode.getSchemaName(), tableName, isConvertUnicode);
			IDBConfig config = tableNode.getDbConfig();
			IColumnSearcherFactory factory = DefaultColumnSearcherFactory.getFactory(con.getMetaData(), config.isConvertUnicode());
			zigen.plugin.db.core.TableColumn[] columns = factory.execute(con, tableNode.getSchemaName(), tableName);
			columnList = new ArrayList();
			for (int i = 0; i < columns.length; i++) {
				Column column = new Column();
				column.setColumn(columns[i]);
				columnList.add(column);
			}
			update();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected FKWizardPage1 getFKWizardPage1() {
		IWizardPage page = getPreviousPage();
		if (page instanceof FKWizardPage1) {
			return (FKWizardPage1) page;
		} else {
			return null;
		}
	}

	protected void createNameConstrol(Composite container) {
		Composite composite = new Composite(container, SWT.NULL);
		composite.setLayout(new GridLayout(2, false));
		// composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label0 = new Label(composite, SWT.NULL);
		label0.setText(Messages.getString("FKWizardPage2.6")); //$NON-NLS-1$
		targetTxt = new Text(composite, SWT.BORDER);
		targetTxt.setText(""); //$NON-NLS-1$
		targetTxt.setEnabled(false);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		targetTxt.setLayoutData(gd);

		Label label = new Label(composite, SWT.NULL);
		label.setText(Messages.getString("FKWizardPage2.8")); //$NON-NLS-1$
		comboRefTable = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		// gd.horizontalSpan = 2;
		comboRefTable.setLayoutData(gd);

		for (Iterator iter = refTableList.iterator(); iter.hasNext();) {
			TableInfo w_table = (TableInfo) iter.next();
			if (w_table.getComment() != null && !"".equals(w_table.getComment())) { //$NON-NLS-1$
				comboRefTable.add(w_table.getName() + " [" + w_table.getComment() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				comboRefTable.add(w_table.getName());
			}
		}

		comboRefTable.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent evt) {
				int index = comboRefTable.getSelectionIndex();
				TableInfo tableInfo = (TableInfo) refTableList.get(index);
				createColumnList(tableInfo.getName());

				selectColumnViewer.setInput(null);
			}

		});
	}

	protected void createOptionControl(Composite container) {
		chkDeleteCascade = new Button(container, SWT.CHECK);
		chkDeleteCascade.setText(Messages.getString("FKWizardPage2.12")); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		// gd.horizontalSpan = 2;
		chkDeleteCascade.setLayoutData(gd);
		chkDeleteCascade.setSelection(true);
	}

	protected void update() {
		Collections.sort(columnList, new ColumnSeqSorter());
		selectColumnViewer.setInput((Column[]) selectedList.toArray(new Column[0]));
		columnViewer.setInput((Column[]) columnList.toArray(new Column[0]));
		columnsPack(selectColumnViewer.getTable());
		columnsPack(columnViewer.getTable());
		if (columnList.size() == 0 && selectedList.size() == 0) {
			updateStatus(MSG_REQUIRE_TABLE);

		} else if (selectedList.size() < getFKWizardPage1().getTargetColumnCount()) {
			updateStatus(MSG_REQUIRE_COLUMN);

		} else if (selectedList.size() > getFKWizardPage1().getTargetColumnCount()) {
			updateStatus(MSG_OVER_COLUMN);

		} else {
			updateStatus(null);
		}

	}


	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			FKWizard wiz = (FKWizard) getWizard();
			Object page = wiz.getPreviousPage(this);
			if (page instanceof FKWizardPage1) {
				FKWizardPage1 page1 = (FKWizardPage1) page;
				targetTxt.setText(page1.getTargetColumn());
			}
			setDescription(MSG_DSC);
			update();
		}
	}

	public TableInfo getRerenceTableInfo() {
		int index = comboRefTable.getSelectionIndex();
		return (TableInfo) refTableList.get(index);
	}
}
