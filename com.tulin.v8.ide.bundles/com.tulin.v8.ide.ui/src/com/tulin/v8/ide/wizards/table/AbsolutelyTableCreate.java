package com.tulin.v8.ide.wizards.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.tulin.v8.core.utils.DataType;
import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.dialog.TableCellsSelectDialog;
import com.tulin.v8.ide.wizards.Messages;

public class AbsolutelyTableCreate extends WizardPage {
	SelectDbkeyPage selectdbkeypage;
	protected String dbkey;
	protected String owner;
	public Text TableName;
	public Text TableText;
	public Table celltable;
	public Button checkbox;

	private boolean iscanf = false;
	public ToolItem addItem;
	public ToolItem removeItem;
	public ToolItem selectItem;

	protected AbsolutelyTableCreate() {
		super("tablewritepage");
	}

	protected AbsolutelyTableCreate(String dbkey, String owner) {
		super("tablewritepage");

		this.setDbkey(dbkey);
		this.setOwner(owner);
	}

	protected AbsolutelyTableCreate(SelectDbkeyPage selectdbkeypage) {
		super("tablewritepage");
		this.selectdbkeypage = selectdbkeypage;
	}

	@Override
	public void createControl(Composite parent) {
		Composite Compos = new Composite(parent, SWT.FILL);
		GridData hdlay = new GridData();
		hdlay.grabExcessHorizontalSpace = true;
		Compos.setLayoutData(hdlay);
		Compos.setLayout(new GridLayout(5, false));
		Label namelabel = new Label(Compos, SWT.NONE);
		namelabel.setText(Messages.getString("wizardsaction.createtable.message.tablename"));
		GridData namelabellay = new GridData();
		// namelabellay.widthHint = 40;
		namelabel.setLayoutData(namelabellay);
		TableName = new Text(Compos, SWT.BORDER);
		GridData namelay = new GridData();
		namelay.widthHint = 200;
		TableName.setLayoutData(namelay);

		Label templabel = new Label(Compos, SWT.NONE);
		GridData splay = new GridData();
		splay.widthHint = 10;
		templabel.setLayoutData(splay);

		Label textlabel = new Label(Compos, SWT.NONE);
		textlabel.setText(Messages.getString("wizardsaction.createtable.message.tablemark"));
		GridData textlabellay = new GridData();
		// textlabellay.widthHint = 40;
		textlabel.setLayoutData(textlabellay);
		TableText = new Text(Compos, SWT.BORDER);
		GridData textlay = new GridData();
		textlay.widthHint = 200;
		TableText.setLayoutData(textlay);

		ToolBar toolbar = new ToolBar(Compos, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		GridData toolbarlay = new GridData(GridData.FILL_HORIZONTAL);
		toolbarlay.grabExcessHorizontalSpace = true;
		toolbarlay.horizontalSpan = 5;
		toolbar.setLayoutData(toolbarlay);
		addItem = new ToolItem(toolbar, SWT.PUSH);
		addItem.setText(Messages.getString("wizardsaction.createtable.message.addcolumn"));
		addItem.setImage(StudioPlugin.getIcon("add.gif"));
		removeItem = new ToolItem(toolbar, SWT.PUSH);
		removeItem.setText(Messages.getString("wizardsaction.createtable.message.removecolumn"));
		removeItem.setImage(StudioPlugin.getIcon("delbtn.gif"));
		selectItem = new ToolItem(toolbar, SWT.PUSH);
		selectItem.setText(Messages.getString("wizardsaction.createtable.message.sourcecolumn"));
		selectItem.setImage(StudioPlugin.getIcon("addbtn.gif"));

		celltable = new Table(Compos, SWT.FILL | SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		GridData gridtable = new GridData(GridData.FILL_BOTH);
		gridtable.horizontalSpan = 5;
		gridtable.heightHint = 250;
		celltable.setLayoutData(gridtable);
		celltable.setHeaderVisible(true);
		celltable.setLinesVisible(true);

		TableColumn tCell = new TableColumn(celltable, SWT.CENTER);
		tCell.setWidth(40);
		TableColumn tCell0 = new TableColumn(celltable, SWT.NONE);
		tCell0.setWidth(120);
		tCell0.setText(Messages.getString("wizards.dataselect.message.dataclumn"));
		TableColumn tCell1 = new TableColumn(celltable, SWT.NONE);
		tCell1.setWidth(100);
		tCell1.setText(Messages.getString("wizards.dataselect.message.datatype"));
		TableColumn tCell2 = new TableColumn(celltable, SWT.NONE);
		tCell2.setWidth(100);
		tCell2.setText(Messages.getString("wizardsaction.createtable.message.clength"));
		TableColumn tCell3 = new TableColumn(celltable, SWT.NONE);
		tCell3.setWidth(180);
		tCell3.setText(Messages.getString("wizards.dataselect.message.datatdesc"));

		checkbox = new Button(Compos, SWT.CHECK);
		checkbox.setText(Messages.getString("wizardsaction.createtable.message.autoadcreateinfo"));
		checkbox.setLayoutData(toolbarlay);

		// 添加
		addItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				addcolumn();
			}
		});

		// 删除
		removeItem.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				TableItem[] items = celltable.getItems();
				List<Integer> li = new ArrayList();
				for (int i = 0; i < items.length; i++) {
					if (items[i].getChecked()) {
						li.add(i);
					}
				}
				if (li.size() < 1) {
					MessageDialog.openInformation(getShell(),
							Messages.getString("wizardsaction.createtable.message.removecolumn"),
							Messages.getString("wizardsaction.createtable.message.removecolumnmsg"));
					return;
				}
				int[] list = new int[li.size()];
				for (int j = 0; j < li.size(); j++) {
					list[j] = Integer.valueOf(li.get(j));
				}
				celltable.remove(list);

				changeComplete();
			}
		});

		selectItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (dbkey == null || "".equals(dbkey)) {
					MessageDialog.openError(getShell(), Messages.getString("wizards.dataselect.message.errtitle"),
							Messages.getString("wizardsaction.createtable.message.dbkeymissmsg"));
				} else {
					TableCellsSelectDialog dial = new TableCellsSelectDialog(getShell(), dbkey, null);
					int result = dial.open();
					if (IDialogConstants.OK_ID == result) {
						String[] addcells = dial.getItemsToOpen().split(",");
						Map<String, String> datatype = dial.getdataType();
						Map<String, String> label = dial.getLabel();
						for (int i = 0; i < addcells.length; i++) {
							TableItem item = new TableItem(celltable, SWT.NONE);
							item.setText(new String[] { "", addcells[i],
									DataType.getDataTypeBydatabase(datatype.get(addcells[i])), "100",
									label.get(addcells[i]) });
						}
					}
				}
			}
		});

		// 编辑
		final TableEditor editor = new TableEditor(celltable);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		celltable.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Rectangle clientArea = celltable.getClientArea();
				Point point = new Point(event.x, event.y);
				int index = celltable.getTopIndex();
				while (index < celltable.getItemCount()) {
					boolean visible = false;
					final TableItem item = celltable.getItem(index);
					for (int i = 1; i < celltable.getColumnCount(); i++) {
						Rectangle rectangle = item.getBounds(i);
						if (rectangle.contains(point) && i != 2) {
							final int column = i;
							final Text text = new Text(celltable, SWT.NONE);
							Listener textListener = new Listener() {
								public void handleEvent(final Event event) {
									switch (event.type) {
									case SWT.FocusOut:
										item.setText(column, text.getText());
										text.dispose();
										break;
									case SWT.Traverse:
										switch (event.detail) {
										case SWT.TRAVERSE_RETURN:
											item.setText(column, text.getText());
										case SWT.TRAVERSE_ESCAPE:
											text.dispose();
											event.doit = false;
										}
										break;
									}
								}
							};
							text.addListener(SWT.FocusOut, textListener);
							text.addListener(SWT.Traverse, textListener);
							editor.setEditor(text, item, i);
							text.setText(item.getText(i));
							text.selectAll();
							text.setFocus();
							return;
						} else if (rectangle.contains(point) && i == 2) {
							final Combo combo = new Combo(celltable, SWT.DROP_DOWN);
							final int column = i;
							combo.setItems(DataType.dataType);
							Listener comboListener = new Listener() {
								public void handleEvent(final Event event) {
									switch (event.type) {
									case SWT.FocusOut:
										item.setText(column, combo.getText());
										combo.dispose();
										break;
									case SWT.Traverse:
										switch (event.detail) {
										case SWT.TRAVERSE_RETURN:
											item.setText(column, combo.getText());
										case SWT.TRAVERSE_ESCAPE:
											combo.dispose();
											event.doit = false;
										}
										break;
									}
								}
							};
							combo.addListener(SWT.FocusOut, comboListener);
							combo.addListener(SWT.Traverse, comboListener);
							combo.addSelectionListener(new SelectionListener() {
								public void widgetDefaultSelected(SelectionEvent e) {
								}

								public void widgetSelected(SelectionEvent e) {
									Combo cb = (Combo) e.getSource();
									String val = cb.getText();
									item.setText(column, val);
								}
							});
							editor.setEditor(combo, item, column);
							combo.setText(item.getText(column));
							combo.setFocus();
							return;
						}
						if (!visible && rectangle.intersects(clientArea)) {
							visible = true;
						}
					}
					if (!visible)
						return;
					index++;
				}
			}
		});

		celltable.addListener(SWT.MouseUp, new Listener() {
			public void handleEvent(Event event) {
				changeComplete();
			}
		});

		setControl(Compos);
		setTitle(Messages.getString("wizardsaction.createtable.message.title"));
		setMessage(Messages.getString("wizardsaction.createtable.message.titlemsg"));
		setPageComplete(false);
	}

	// 添加
	public void addcolumn() {

	}

	public boolean canFinish() {
		return iscanf;
	}

	private boolean vicate() {
		return (TableName.getText() != null && !"".equals(TableName.getText())) && celltable.getItemCount() > 0;
	}

	public void changeComplete() {
		setPageComplete(vicate());
	}

	public IWizardPage getNextPage() {
		return getWizard().getPage("createtableendpage");
	}

	public void setDbkey(String dbkey) {
		this.dbkey = dbkey;
	}

	public String getDbkey() {
		return dbkey;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwner() {
		return owner;
	}

}
