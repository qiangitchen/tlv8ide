/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.wizard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ColumnSeqSorter;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.editors.event.TextSelectionListener;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.util.WidgetUtil;
import zigen.plugin.db.ui.views.TreeViewSorter;

public class PKWizardPage extends DefaultWizardPage {

	public static final String MSG_DSC = Messages.getString("PKWizardPage.0"); //$NON-NLS-1$

	private static final String MSG_REQUIRE_NAME = Messages.getString("PKWizardPage.1"); //$NON-NLS-1$

	private static final String MSG_REQUIRE_COLUMN = Messages.getString("PKWizardPage.2"); //$NON-NLS-1$

	protected ISQLCreatorFactory factory;

	protected ITable tableNode;

	protected Text txtConstraintName;

	protected List selectedList = new ArrayList();

	protected List columnList = new ArrayList();

	protected TableViewer selectColumnViewer;

	protected TableViewer columnViewer;

	public PKWizardPage(ISQLCreatorFactory factory, ITable table) {
		super(Messages.getString("PKWizardPage.3")); //$NON-NLS-1$
		setTitle(Messages.getString("PKWizardPage.4")); //$NON-NLS-1$
		this.factory = factory;
		this.tableNode = table;
		Column[] columns = table.getColumns();
		for (int i = 0; i < columns.length; i++) {
			columnList.add(columns[i]);
		}
	}

	public void createControl(Composite parent) {
		Composite container = createDefaultComposite(parent);
		createNameConstrol(container);
		createColumnSelectConstrol(container);
		createOptionControl(container);
		setControl(container);
	}

	protected String getDefaultConstraintName(){
		return "PK_" + tableNode.getName();
	}

	protected void createNameConstrol(Composite container) {
		Composite composite = new Composite(container, SWT.NULL);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label label = new Label(composite, SWT.NULL);
		label.setText(Messages.getString("PKWizardPage.5")); //$NON-NLS-1$
		txtConstraintName = new Text(composite, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		txtConstraintName.setLayoutData(gd);
		//txtConstraintName.setText(""); //$NON-NLS-1$
		// PK_ + TableName
		txtConstraintName.setText(getDefaultConstraintName()); //$NON-NLS-1$

		txtConstraintName.addFocusListener(new TextSelectionListener());
		txtConstraintName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if ("".equals(txtConstraintName.getText())) { //$NON-NLS-1$
					updateStatus(MSG_REQUIRE_NAME);
				} else if (selectedList.isEmpty()) {
					updateStatus(MSG_REQUIRE_COLUMN);
				} else {
					updateStatus(null);
				}
			}
		});
	}

	protected void createColumnSelectConstrol(Composite container) {
		Composite main = new Composite(container, SWT.NULL);
		main.setLayout(new GridLayout(3, false));
		// main.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		gd.widthHint = 400;
		main.setLayoutData(gd);

		Label label1 = new Label(main, SWT.NULL);
		label1.setText(Messages.getString("PKWizardPage.8")); //$NON-NLS-1$
		Label dummy = new Label(main, SWT.NULL);
		Label label2 = new Label(main, SWT.NULL);
		label2.setText(Messages.getString("PKWizardPage.9")); //$NON-NLS-1$

		columnViewer = new TableViewer(main, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 100;
		gd.widthHint = 200;
		final Table table2 = columnViewer.getTable();
		table2.setLayoutData(gd);
		table2.setHeaderVisible(false);
		table2.setLinesVisible(false);
		table2.setFont(DbPlugin.getDefaultFont());
		columnViewer.setContentProvider(new ConstraintsColumnContentProvider());
		columnViewer.setLabelProvider(new ConstraintsColumnLabelProvider());
		columnViewer.setSorter(new TreeViewSorter());
		setHeaderColumn(table2);

		columnViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				TableItem[] items = table2.getSelection();
				for (int i = 0; i < items.length; i++) {
					TableItem item = items[i];
					Column col = (Column) item.getData();
					selectedList.add(col);
					columnList.remove(col);
				}
				update();
			}

		});

		Composite ButtonArea = new Composite(main, SWT.NULL);
		ButtonArea.setLayout(new GridLayout(1, false));

		int BUTTON_WIDTH = 50;
		Button addBtn = WidgetUtil.createButton(ButtonArea, SWT.PUSH, ">>", BUTTON_WIDTH, new GridData()); //$NON-NLS-1$
		Button addBtn2 = WidgetUtil.createButton(ButtonArea, SWT.PUSH, ">", BUTTON_WIDTH, new GridData()); //$NON-NLS-1$
		Button addBtn3 = WidgetUtil.createButton(ButtonArea, SWT.PUSH, "<", BUTTON_WIDTH, new GridData()); //$NON-NLS-1$
		Button addBtn4 = WidgetUtil.createButton(ButtonArea, SWT.PUSH, "<<", BUTTON_WIDTH, new GridData()); //$NON-NLS-1$

		selectColumnViewer = new TableViewer(main, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 100;
		gd.widthHint = 200;
		final Table table = selectColumnViewer.getTable();
		table.setLayoutData(gd);
		table.setHeaderVisible(false);
		table.setLinesVisible(false);
		table.setFont(DbPlugin.getDefaultFont());
		setHeaderColumn(table);

		selectColumnViewer.setContentProvider(new ConstraintsColumnContentProvider());
		selectColumnViewer.setLabelProvider(new ConstraintsColumnLabelProvider());
		// defineViewer.setSorter(new TreeViewSorter());

		selectColumnViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				TableItem[] items = table.getSelection();
				for (int i = 0; i < items.length; i++) {
					TableItem item = items[i];
					Column col = (Column) item.getData();
					selectedList.remove(col);
					columnList.add(col);
				}
				update();
			}

		});

		addBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				while (!columnList.isEmpty()) {
					Column col = (Column) columnList.get(0);
					selectedList.add(col);
					columnList.remove(col);
				}
				update();
			}
		});

		addBtn2.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table2.getSelection();
				for (int i = 0; i < items.length; i++) {
					TableItem item = items[i];
					Column col = (Column) item.getData();
					selectedList.add(col);
					columnList.remove(col);
				}
				update();
			}
		});

		addBtn3.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				for (int i = 0; i < items.length; i++) {
					TableItem item = items[i];
					Column col = (Column) item.getData();
					selectedList.remove(col);
					columnList.add(col);
				}
				update();

			}
		});
		addBtn4.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				while (!selectedList.isEmpty()) {
					Column col = (Column) selectedList.get(0);
					selectedList.remove(col);
					columnList.add(col);
				}
				update();
			}
		});
	}

	protected void createOptionControl(Composite container) {
		;
	}

	protected void update() {
		Collections.sort(columnList, new ColumnSeqSorter());
		selectColumnViewer.setInput((Column[]) selectedList.toArray(new Column[0]));
		columnViewer.setInput((Column[]) columnList.toArray(new Column[0]));
		columnsPack(selectColumnViewer.getTable());
		columnsPack(columnViewer.getTable());

		if ("".equals(txtConstraintName.getText())) { //$NON-NLS-1$
			updateStatus(MSG_REQUIRE_NAME);
		} else if (selectedList.isEmpty()) {
			updateStatus(MSG_REQUIRE_COLUMN);
		} else {
			updateStatus(null);
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
			update();
		}
	}


}
