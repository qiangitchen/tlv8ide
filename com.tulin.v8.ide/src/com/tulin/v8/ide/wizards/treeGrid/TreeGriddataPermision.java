package com.tulin.v8.ide.wizards.treeGrid;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.wizards.Messages;
import com.tulin.v8.ide.wizards.utils.DataDBKeySelectDialog;
import com.tulin.v8.ide.wizards.utils.TableCellsSelectDialog;
import com.tulin.v8.ide.wizards.utils.TableSelectDialog;
import com.tulin.v8.ide.wizards.utils.TablecellSelectDialog;

public class TreeGriddataPermision extends WizardPage {
	private SashForm sashForm;
	private Composite dataComp;
	private Composite confComp;
	protected StackLayout stackLayout = new StackLayout();
	protected Composite displayComp;
	private Table ptable;
	public String dbkey;
	public String table;
	public String cells;
	public String ID;
	public String parent;
	public String name;
	public String rootFilter;
	public String level;
	public String quckpath;

	public TreeGriddataPermision() {
		super("treeGriddataPermision");
	}

	@Override
	public void createControl(Composite parent) {
		sashForm = new SashForm(parent, SWT.NONE);
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 3);
		formData.right = new FormAttachment(100, -3);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, -25);
		sashForm.setLayoutData(formData);

		ptable = new Table(sashForm, SWT.BORDER | SWT.V_SCROLL
				| SWT.FULL_SELECTION);
		ptable.setLayoutData(new GridData(GridData.FILL_BOTH));
		ptable.setHeaderVisible(true);
		ptable.setLinesVisible(true);
		TableColumn tablecolumn1 = new TableColumn(ptable, SWT.NONE);
		tablecolumn1.setWidth(120);
		tablecolumn1.setText(Messages.getString("wizards.dataselect.message.settings"));
		TableItem itemdata = new TableItem(ptable, SWT.NONE);
		itemdata.setText(Messages.getString("wizardsaction.dataselect.message.dataperm"));
		itemdata.setImage(StudioPlugin.getIcon("conf.gif"));
		TableItem itemconf = new TableItem(ptable, SWT.NONE);
		itemconf.setText(Messages.getString("wizardsaction.dataselect.message.datatreeperm"));
		itemconf.setImage(StudioPlugin.getIcon("conf.gif"));

		displayComp = new Composite(sashForm, SWT.NONE);
		displayComp.setLayout(stackLayout);

		sashForm.setWeights(new int[] { 2, 8 });
		setMessage(Messages.getString("wizards.dataselect.message.setbysquens"));

		dataComp = setDatapermision(displayComp);
		confComp = setTreepermision(displayComp);
		stackLayout.topControl = dataComp;

		ptable.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				TableItem item = (TableItem) e.item;
				String name = item.getText();
				if (Messages.getString("wizardsaction.dataselect.message.dataperm").equals(name)) {
					setTitle("树详细-主数据");
					stackLayout.topControl = dataComp;
				}
				if (Messages.getString("wizardsaction.dataselect.message.datatreeperm").equals(name)) {
					setTitle("树详细-树配置");
					stackLayout.topControl = confComp;
				}
				displayComp.layout();
			}
		});
		ptable.setSelection(0);
		setTitle("树详细-主数据");

		setPageComplete(false);
		setControl(sashForm);
	}

	/*
	 * 数据配置
	 */
	private Composite setDatapermision(Composite parent) {
		final Composite composite = new Composite(parent, SWT.FILL);
		composite.setLayout(new GridLayout(3, false));
		Label label1 = new Label(composite, SWT.NONE);
		label1.setText("dbkey(*)");
		GridData gridl1 = new GridData();
		gridl1.widthHint = 80;
		label1.setLayoutData(gridl1);
		final Text text1 = new Text(composite, SWT.BORDER | SWT.FILL);
		text1.setEditable(false);
		GridData gridt1 = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gridt1.grabExcessHorizontalSpace = true;
		text1.setLayoutData(gridt1);
		Button button1 = new Button(composite, SWT.NONE);
		button1.setText("...");
		GridData gridb1 = new GridData();
		button1.setLayoutData(gridb1);

		Label label2 = new Label(composite, SWT.NONE);
		label2.setText("table(*)");
		GridData gridl2 = new GridData();
		label2.setLayoutData(gridl2);
		final Text text2 = new Text(composite, SWT.BORDER | SWT.FILL);
		text2.setEditable(false);
		GridData gridt2 = new GridData(SWT.FILL, SWT.CENTER, false, false);
		text2.setLayoutData(gridt2);
		Button button2 = new Button(composite, SWT.NONE);
		button2.setText("...");
		GridData gridb2 = new GridData();
		button2.setLayoutData(gridb2);

		button1.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				DataDBKeySelectDialog dial = new DataDBKeySelectDialog(
						getShell());
				int result = dial.open();
				if (IDialogConstants.OK_ID == result) {
					text1.setText(dial.getItemsToOpen());
					if (dbkey != null && !"".equals(dbkey)
							&& !dbkey.equals(text1.getText())) {
						table = null;
						cells = null;
						text2.setText("");
					}
					dbkey = text1.getText();
					setPageCompleteEnable();
				}
			}
		});
		button2.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (dbkey == null || "".equals(dbkey)) {
					MessageDialog.openError(getShell(), Messages.getString("wizards.dataselect.message.errtitle"), Messages.getString("wizardsaction.dataselect.message.selectdbkey"));
				} else {
					TableSelectDialog dial = new TableSelectDialog(getShell(),
							dbkey);
					int result = dial.open();
					if (IDialogConstants.OK_ID == result) {
						text2.setText(dial.getItemsToOpen());
						if (table != null && !"".equals(table)
								&& !table.equals(text2.getText())) {
							cells = null;
						}
						table = text2.getText();
						setPageCompleteEnable();
					}
				}
			}
		});
		return composite;
	}

	/*
	 * 树配置
	 */
	private Composite setTreepermision(Composite shell) {
		Composite composite = new Composite(shell, SWT.FILL);
		composite.setLayout(new GridLayout(3, false));
		Label label0 = new Label(composite, SWT.NONE);
		label0.setText("ID(*)");
		GridData gridl0 = new GridData();
		gridl0.widthHint = 80;
		label0.setLayoutData(gridl0);
		final Text text0 = new Text(composite, SWT.BORDER | SWT.FILL);
		text0.setEditable(false);
		GridData gridt0 = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gridt0.grabExcessHorizontalSpace = true;
		text0.setLayoutData(gridt0);
		Button button0 = new Button(composite, SWT.NONE);
		button0.setText("...");
		GridData gridb0 = new GridData();
		button0.setLayoutData(gridb0);
		button0.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (dbkey == null || "".equals(dbkey) || table == null
						|| "".equals(table)) {
					MessageDialog.openError(getShell(), Messages.getString("wizards.dataselect.message.errtitle"),
							Messages.getString("wizardsaction.dataselect.message.selectdbandtable"));
				} else {
					TablecellSelectDialog dial = new TablecellSelectDialog(
							getShell(), dbkey, table);
					int result = dial.open();
					if (IDialogConstants.OK_ID == result) {
						text0.setText(dial.getItemsToOpen());
						ID = text0.getText();
						setPageCompleteEnable();
					}
				}
			}
		});

		Label label1 = new Label(composite, SWT.NONE);
		label1.setText("parent(*)");
		GridData gridl1 = new GridData();
		gridl1.widthHint = 80;
		label1.setLayoutData(gridl1);
		final Text text1 = new Text(composite, SWT.BORDER | SWT.FILL);
		text1.setEditable(false);
		GridData gridt1 = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gridt1.grabExcessHorizontalSpace = true;
		text1.setLayoutData(gridt1);
		Button button1 = new Button(composite, SWT.NONE);
		button1.setText("...");
		GridData gridb1 = new GridData();
		button1.setLayoutData(gridb1);
		button1.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (dbkey == null || "".equals(dbkey) || table == null
						|| "".equals(table)) {
					MessageDialog.openError(getShell(), Messages.getString("wizards.dataselect.message.errtitle"),
							Messages.getString("wizardsaction.dataselect.message.selectdbandtable"));
				} else {
					TablecellSelectDialog dial = new TablecellSelectDialog(
							getShell(), dbkey, table);
					int result = dial.open();
					if (IDialogConstants.OK_ID == result) {
						text1.setText(dial.getItemsToOpen());
						parent = text1.getText();
						setPageCompleteEnable();
					}
				}
			}
		});

		Label label2 = new Label(composite, SWT.NONE);
		label2.setText("name(*)");
		GridData gridl2 = new GridData();
		label2.setLayoutData(gridl2);
		final Text text2 = new Text(composite, SWT.BORDER | SWT.FILL);
		text2.setEditable(false);
		GridData gridt2 = new GridData(SWT.FILL, SWT.CENTER, false, false);
		text2.setLayoutData(gridt2);
		Button button2 = new Button(composite, SWT.NONE);
		button2.setText("...");
		GridData gridb2 = new GridData();
		button2.setLayoutData(gridb2);
		button2.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (dbkey == null || "".equals(dbkey) || table == null
						|| "".equals(table)) {
					MessageDialog.openError(getShell(), Messages.getString("wizards.dataselect.message.errtitle"),
							Messages.getString("wizardsaction.dataselect.message.selectdbandtable"));
				} else {
					TablecellSelectDialog dial = new TablecellSelectDialog(
							getShell(), dbkey, table);
					int result = dial.open();
					if (IDialogConstants.OK_ID == result) {
						text2.setText(dial.getItemsToOpen());
						name = text2.getText();
						setPageCompleteEnable();
					}
				}
			}
		});

		Label label3 = new Label(composite, SWT.NONE);
		label3.setText("cells");
		GridData gridl3 = new GridData();
		label3.setLayoutData(gridl3);
		final Text text3 = new Text(composite, SWT.BORDER | SWT.FILL);
		text3.setEditable(false);
		GridData gridt3 = new GridData(SWT.FILL, SWT.CENTER, false, false);
		text3.setLayoutData(gridt3);
		Button button3 = new Button(composite, SWT.NONE);
		button3.setText("...");
		GridData gridb3 = new GridData();
		button3.setLayoutData(gridb3);

		button3.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (dbkey == null || "".equals(dbkey) || table == null
						|| "".equals(table)) {
					MessageDialog.openError(getShell(), Messages.getString("wizards.dataselect.message.errtitle"),
							Messages.getString("wizardsaction.dataselect.message.selectdbandtable"));
				} else {
					TableCellsSelectDialog dial = new TableCellsSelectDialog(
							getShell(), dbkey, table);
					int result = dial.open();
					if (IDialogConstants.OK_ID == result) {
						text3.setText(dial.getItemsToOpen());
						cells = text3.getText();
						setPageCompleteEnable();
					}
				}
			}
		});

		return composite;
	}

	private void setPageCompleteEnable() {
		boolean enable = (dbkey != null & table != null && ID != null
				&& !"".equals(ID) && parent != null && name != null);
		setPageComplete(enable);
	}

	@Override
	public IWizardPage getNextPage() {
		return getWizard().getPage("treeGridPageEnd");
	}

}
