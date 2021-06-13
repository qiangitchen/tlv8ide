package com.tulin.v8.ide.wizards.directGrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
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

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.utils.DataType;
import com.tulin.v8.ide.wizards.Messages;
import com.tulin.v8.ide.wizards.utils.DataDBKeySelectDialog;
import com.tulin.v8.ide.wizards.utils.TableCellsSelectDialog;
import com.tulin.v8.ide.wizards.utils.TableSelectDialog;
import com.tulin.v8.ide.wizards.utils.TablecellSelectDialog;

public class DirectGridPermision extends WizardPage {
	private SashForm sashForm;
	private Composite dataComp;
	private Composite confComp;
	private Composite subconfComp;
	protected StackLayout stackLayout = new StackLayout();
	protected Composite displayComp;
	private Table ptable;
	public String dbkey;
	public String table;
	public String subtable;
	public String subdirect;

	public List<String> columns = new ArrayList<String>();
	public Map<String, String> labels = new HashMap<String, String>();
	public Map<String, String> widths = new HashMap<String, String>();
	public Map<String, String> datatypes = new HashMap<String, String>();

	public List<String> subcolumns = new ArrayList<String>();
	public Map<String, String> sublabels = new HashMap<String, String>();
	public Map<String, String> subwidths = new HashMap<String, String>();
	public Map<String, String> subdatatypes = new HashMap<String, String>();

	public DirectGridPermision() {
		super("directGridPermision");
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

		ptable = new Table(sashForm, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
		ptable.setLayoutData(new GridData(GridData.FILL_BOTH));
		ptable.setHeaderVisible(true);
		ptable.setLinesVisible(true);
		TableColumn tablecolumn1 = new TableColumn(ptable, SWT.NONE);
		tablecolumn1.setWidth(120);
		tablecolumn1.setText(Messages.getString("wizards.dataselect.message.settings"));
		TableItem itemdata = new TableItem(ptable, SWT.NONE);
		itemdata.setText(Messages.getString("wizards.dataselect.message.datasettings"));
		itemdata.setImage(StudioPlugin.getIcon("conf.gif"));
		TableItem itemconf = new TableItem(ptable, SWT.NONE);
		itemconf.setText(Messages.getString("wizards.dataselect.message.maintablesettings"));
		itemconf.setImage(StudioPlugin.getIcon("conf.gif"));
		TableItem itemsubconf = new TableItem(ptable, SWT.NONE);
		itemsubconf.setText(Messages.getString("wizards.dataselect.message.subtablesettings"));
		itemsubconf.setImage(StudioPlugin.getIcon("conf.gif"));

		displayComp = new Composite(sashForm, SWT.NONE);
		displayComp.setLayout(stackLayout);

		sashForm.setWeights(new int[] { 2, 8 });
		setMessage(Messages.getString("wizards.dataselect.message.setbysquens"));

		dataComp = setDatapermision(displayComp);
		confComp = setTablepermision(displayComp);
		subconfComp = setSubdarapermision(displayComp);
		stackLayout.topControl = dataComp;

		ptable.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				TableItem item = (TableItem) e.item;
				String name = item.getText();
				if (Messages.getString("wizards.dataselect.message.datasettings").equals(name)) {
					setTitle("主从列表-数据配置");
					stackLayout.topControl = dataComp;
				}
				if (Messages.getString("wizards.dataselect.message.maintablesettings").equals(name)) {
					setTitle("主从列表-主表配置");
					stackLayout.topControl = confComp;
				}
				if (Messages.getString("wizards.dataselect.message.subtablesettings").equals(name)) {
					setTitle("主从列表-从表配置");
					stackLayout.topControl = subconfComp;
				}
				displayComp.layout();
			}
		});
		ptable.setSelection(0);
		setTitle("主从列表-数据配置");

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
		label1.setText(Messages.getString("wizards.dataselect.message.datasorce"));
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
		label2.setText(Messages.getString("wizards.dataselect.message.maintablename"));
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

		Label label3 = new Label(composite, SWT.NONE);
		label3.setText(Messages.getString("wizards.dataselect.message.subtablename"));
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

		Label label4 = new Label(composite, SWT.NONE);
		label4.setText(Messages.getString("wizards.dataselect.message.subtablekey"));
		GridData gridl4 = new GridData();
		label4.setLayoutData(gridl4);
		final Text text4 = new Text(composite, SWT.BORDER | SWT.FILL);
		text4.setEditable(false);
		GridData gridt4 = new GridData(SWT.FILL, SWT.CENTER, false, false);
		text4.setLayoutData(gridt4);
		Button button4 = new Button(composite, SWT.NONE);
		button4.setText("...");
		GridData gridb4 = new GridData();
		button4.setLayoutData(gridb4);

		button1.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				DataDBKeySelectDialog dial = new DataDBKeySelectDialog(getShell());
				int result = dial.open();
				if (IDialogConstants.OK_ID == result) {
					text1.setText(dial.getItemsToOpen());
					if (dbkey != null && !"".equals(dbkey) && !dbkey.equals(text1.getText())) {
						table = null;
						subtable = null;
						text2.setText("");
						text3.setText("");
					}
					dbkey = text1.getText();
					setPageCompleteEnable();
				}
			}
		});
		button2.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (dbkey == null || "".equals(dbkey)) {
					MessageDialog.openError(getShell(), Messages.getString("wizards.dataselect.message.errtitle"),
							Messages.getString("wizardsaction.dataselect.message.selectdbkey"));
				} else {
					TableSelectDialog dial = new TableSelectDialog(getShell(), dbkey);
					int result = dial.open();
					if (IDialogConstants.OK_ID == result) {
						text2.setText(dial.getItemsToOpen());
						table = text2.getText();
						setPageCompleteEnable();
					}
				}
			}
		});
		button3.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (dbkey == null || "".equals(dbkey) || table == null || "".equals(table)) {
					MessageDialog.openError(getShell(), Messages.getString("wizards.dataselect.message.errtitle"),
							Messages.getString("wizardsaction.dataselect.message.selectdbandtable"));
				} else {
					TableSelectDialog dial = new TableSelectDialog(getShell(), dbkey);
					int result = dial.open();
					if (IDialogConstants.OK_ID == result) {
						text3.setText(dial.getItemsToOpen());
						subtable = text3.getText();
						setPageCompleteEnable();
					}
				}
			}
		});
		button4.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (dbkey == null || "".equals(dbkey) || subtable == null || "".equals(subtable)) {
					MessageDialog.openError(getShell(), Messages.getString("wizards.dataselect.message.errtitle"),
							Messages.getString("wizards.dataselect.message.errSelectDataSubtable"));
				} else {
					TablecellSelectDialog dial = new TablecellSelectDialog(getShell(), dbkey, subtable);
					int result = dial.open();
					if (IDialogConstants.OK_ID == result) {
						text4.setText(dial.getItemsToOpen());
						subdirect = text4.getText();
						setPageCompleteEnable();
					}
				}
			}
		});
		return composite;
	}

	/*
	 * 主表配置
	 */
	private Composite setTablepermision(Composite shell) {
		Composite composite = new Composite(shell, SWT.FILL);
		composite.setLayout(new GridLayout(3, false));

		ToolBar toolbar = new ToolBar(composite, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		GridData toolbarcl = new GridData();
		toolbar.setLayoutData(toolbarcl);
		final ToolItem addroot = new ToolItem(toolbar, SWT.PUSH);
		addroot.setImage(StudioPlugin.getIcon("addbtn.gif"));
		addroot.setText(Messages.getString("wizardsaction.dataselect.message.add"));
		final ToolItem adddel = new ToolItem(toolbar, SWT.PUSH);
		adddel.setImage(StudioPlugin.getIcon("delbtn.gif"));
		adddel.setText(Messages.getString("wizardsaction.dataselect.message.remove"));
		adddel.setEnabled(false);

		final Table tablegrid = new Table(composite, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
		GridData gridbar = new GridData(GridData.FILL_BOTH);
		gridbar.horizontalSpan = 3;
		tablegrid.setLayoutData(gridbar);
		tablegrid.setHeaderVisible(true);
		tablegrid.setLinesVisible(true);
		tablegrid.setEnabled(true);
		TableColumn tablegridcolumn1 = new TableColumn(tablegrid, SWT.NONE);
		tablegridcolumn1.setWidth(80);
		tablegridcolumn1.setText(Messages.getString("wizards.dataselect.message.dataclumn"));
		TableColumn tablegridcolumn2 = new TableColumn(tablegrid, SWT.NONE);
		tablegridcolumn2.setWidth(90);
		tablegridcolumn2.setText(Messages.getString("wizards.dataselect.message.datatdesc"));
		TableColumn tablegridcolumn3 = new TableColumn(tablegrid, SWT.NONE);
		tablegridcolumn3.setWidth(100);
		tablegridcolumn3.setText(Messages.getString("wizardsaction.dataselect.message.width"));
		TableColumn tablegridcolumn4 = new TableColumn(tablegrid, SWT.NONE);
		tablegridcolumn4.setWidth(100);
		tablegridcolumn4.setText(Messages.getString("wizards.dataselect.message.datatype"));
		final TableEditor editor = new TableEditor(tablegrid);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;

		tablegrid.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Rectangle clientArea = tablegrid.getClientArea();
				Point point = new Point(event.x, event.y);
				int index = tablegrid.getTopIndex();
				while (index < tablegrid.getItemCount()) {
					boolean visible = false;
					final TableItem item = tablegrid.getItem(index);
					for (int i = 1; i < tablegrid.getColumnCount(); i++) {
						Rectangle rectangle = item.getBounds(i);
						if (rectangle.contains(point) && i != 3) {
							final int column = i;
							final Text text = new Text(tablegrid, SWT.NONE);
							Listener textListener = new Listener() {
								public void handleEvent(final Event event) {
									switch (event.type) {
									case SWT.FocusOut:
										item.setText(column, text.getText());
										if (column == 1) {
											labels.put(item.getText(0), text.getText());
										} else if (column == 2) {
											widths.put(item.getText(0), text.getText());
										}
										text.dispose();
										break;
									case SWT.Traverse:
										switch (event.detail) {
										case SWT.TRAVERSE_RETURN:
											item.setText(column, text.getText());
											if (column == 1) {
												labels.put(item.getText(0), text.getText());
											} else if (column == 2) {
												widths.put(item.getText(0), text.getText());
											}
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
						} else if (rectangle.contains(point) && i == 3) {
							final Combo combo = new Combo(tablegrid, SWT.DROP_DOWN);
							combo.setItems(DataType.gridDataType);
							Listener comboListener = new Listener() {
								public void handleEvent(final Event event) {
									switch (event.type) {
									case SWT.FocusOut:
										item.setText(3, combo.getText());
										datatypes.put(item.getText(0), combo.getText());
										combo.dispose();
										break;
									case SWT.Traverse:
										switch (event.detail) {
										case SWT.TRAVERSE_RETURN:
											item.setText(3, combo.getText());
											datatypes.put(item.getText(0), combo.getText());
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
									item.setText(3, val);
									datatypes.put(item.getText(0), val);
								}
							});
							editor.setEditor(combo, item, 3);
							combo.setText(item.getText(3));
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
		tablegrid.addListener(SWT.MouseUp, new Listener() {
			public void handleEvent(Event arg0) {
				setPageCompleteEnable();
			}
		});
		tablegrid.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				adddel.setEnabled(true);
			}
		});

		// 添加
		addroot.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (dbkey == null || "".equals(dbkey) || table == null || "".equals(table)) {
					MessageDialog.openError(getShell(), Messages.getString("wizards.dataselect.message.errtitle"),
							Messages.getString("wizards.dataselect.message.errSelectDatatable"));
				} else {
					TableCellsSelectDialog dial = new TableCellsSelectDialog(getShell(), dbkey, table);
					int result = dial.open();
					if (IDialogConstants.OK_ID == result) {
						String cells = dial.getItemsToOpen();
						Map<String, String> dataType = dial.getdataType();
						Map<String, String> label = dial.getLabel();
						if (cells != null && !"".equals(cells)) {
							String[] cellarray = cells.split(",");
							for (int i = 0; i < cellarray.length; i++) {
								setColums(cellarray[i], tablegrid, dataType, label, true);
							}
						}
						setPageCompleteEnable();
					}
				}
			}
		});
		// 删除
		adddel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean result = MessageDialog.openConfirm(getShell(),
						Messages.getString("wizardsaction.dataselect.message.clmremoveConfirm"),
						Messages.getString("wizardsaction.dataselect.message.clmremoveConfirmmsg"));
				if (result) {
					tablegrid.remove(tablegrid.getSelectionIndex());
					setColums(tablegrid.getItem(tablegrid.getSelectionIndex()).getText(), tablegrid, null, null, false);
					setPageCompleteEnable();
				}
			}
		});

		return composite;
	}

	private void setColums(String id, Table tablegrid, Map<String, String> datatype, Map<String, String> label,
			boolean isadd) {
		TableItem[] tbItems = tablegrid.getItems();
		int chIdIndex = -1;
		TableItem citem = null;
		for (int i = 0; i < tbItems.length; i++) {
			citem = tbItems[i];
			if (id.equals(citem.getText(0))) {
				chIdIndex = i;
			}
		}
		if (isadd) {
			if (chIdIndex < 0) {
				String dataType = DataType.getDataTypeBydatabase(datatype.get(id).toUpperCase());
				TableItem tableitem = new TableItem(tablegrid, SWT.NONE);
				tableitem.setText(new String[] { id, label.get(id), "80", dataType });
				columns.add(id);
				labels.put(id, label.get(id));
				widths.put(id, "80");
				datatypes.put(id, dataType);
			}
		} else {
			if (chIdIndex > -1) {
				tablegrid.remove(chIdIndex);
				columns.remove(id);
				labels.remove(id);
				widths.remove(id);
				datatypes.remove(id);
			}
		}
	}

	/*
	 * 从表配置
	 */
	private Composite setSubdarapermision(Composite shell) {
		Composite composite = new Composite(shell, SWT.FILL);
		composite.setLayout(new GridLayout(3, false));

		ToolBar toolbar = new ToolBar(composite, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		GridData toolbarcl = new GridData();
		toolbar.setLayoutData(toolbarcl);
		final ToolItem addroot = new ToolItem(toolbar, SWT.PUSH);
		addroot.setImage(StudioPlugin.getIcon("addbtn.gif"));
		addroot.setText(Messages.getString("wizardsaction.dataselect.message.add"));
		final ToolItem adddel = new ToolItem(toolbar, SWT.PUSH);
		adddel.setImage(StudioPlugin.getIcon("delbtn.gif"));
		adddel.setText(Messages.getString("wizardsaction.dataselect.message.remove"));
		adddel.setEnabled(false);

		final Table tablegrid = new Table(composite, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
		GridData gridbar = new GridData(GridData.FILL_BOTH);
		gridbar.horizontalSpan = 3;
		tablegrid.setLayoutData(gridbar);
		tablegrid.setHeaderVisible(true);
		tablegrid.setLinesVisible(true);
		tablegrid.setEnabled(true);
		TableColumn tablegridcolumn1 = new TableColumn(tablegrid, SWT.NONE);
		tablegridcolumn1.setWidth(80);
		tablegridcolumn1.setText(Messages.getString("wizards.dataselect.message.dataclumn"));
		TableColumn tablegridcolumn2 = new TableColumn(tablegrid, SWT.NONE);
		tablegridcolumn2.setWidth(90);
		tablegridcolumn2.setText(Messages.getString("wizards.dataselect.message.datatdesc"));
		TableColumn tablegridcolumn3 = new TableColumn(tablegrid, SWT.NONE);
		tablegridcolumn3.setWidth(100);
		tablegridcolumn3.setText(Messages.getString("wizardsaction.dataselect.message.width"));
		TableColumn tablegridcolumn4 = new TableColumn(tablegrid, SWT.NONE);
		tablegridcolumn4.setWidth(100);
		tablegridcolumn4.setText(Messages.getString("wizards.dataselect.message.datatype"));
		final TableEditor editor = new TableEditor(tablegrid);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;

		tablegrid.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Rectangle clientArea = tablegrid.getClientArea();
				Point point = new Point(event.x, event.y);
				int index = tablegrid.getTopIndex();
				while (index < tablegrid.getItemCount()) {
					boolean visible = false;
					final TableItem item = tablegrid.getItem(index);
					for (int i = 1; i < tablegrid.getColumnCount(); i++) {
						Rectangle rectangle = item.getBounds(i);
						if (rectangle.contains(point) && i != 3) {
							final int column = i;
							final Text text = new Text(tablegrid, SWT.NONE);
							Listener textListener = new Listener() {
								public void handleEvent(final Event event) {
									switch (event.type) {
									case SWT.FocusOut:
										item.setText(column, text.getText());
										if (column == 1) {
											sublabels.put(item.getText(0), text.getText());
										} else if (column == 2) {
											subwidths.put(item.getText(0), text.getText());
										}
										text.dispose();
										break;
									case SWT.Traverse:
										switch (event.detail) {
										case SWT.TRAVERSE_RETURN:
											item.setText(column, text.getText());
											if (column == 1) {
												sublabels.put(item.getText(0), text.getText());
											} else if (column == 2) {
												subwidths.put(item.getText(0), text.getText());
											}
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
						} else if (rectangle.contains(point) && i == 3) {
							final Combo combo = new Combo(tablegrid, SWT.DROP_DOWN);
							combo.setItems(DataType.gridDataType);
							Listener comboListener = new Listener() {
								public void handleEvent(final Event event) {
									switch (event.type) {
									case SWT.FocusOut:
										item.setText(3, combo.getText());
										subdatatypes.put(item.getText(0), combo.getText());
										combo.dispose();
										break;
									case SWT.Traverse:
										switch (event.detail) {
										case SWT.TRAVERSE_RETURN:
											item.setText(3, combo.getText());
											subdatatypes.put(item.getText(0), combo.getText());
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
									item.setText(3, val);
									subdatatypes.put(item.getText(0), val);
								}
							});
							editor.setEditor(combo, item, 3);
							combo.setText(item.getText(3));
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
		tablegrid.addListener(SWT.MouseUp, new Listener() {
			public void handleEvent(Event arg0) {
				setPageCompleteEnable();
			}
		});
		tablegrid.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				adddel.setEnabled(true);
			}
		});

		// 添加
		addroot.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (dbkey == null || "".equals(dbkey) || subtable == null || "".equals(subtable)) {
					MessageDialog.openError(getShell(), Messages.getString("wizards.dataselect.message.errtitle"),
							Messages.getString("wizards.dataselect.message.errSelectDataSubtable"));
				} else {
					TableCellsSelectDialog dial = new TableCellsSelectDialog(getShell(), dbkey, subtable);
					int result = dial.open();
					if (IDialogConstants.OK_ID == result) {
						String cells = dial.getItemsToOpen();
						Map<String, String> dataType = dial.getdataType();
						Map<String, String> label = dial.getLabel();
						if (cells != null && !"".equals(cells)) {
							String[] cellarray = cells.split(",");
							for (int i = 0; i < cellarray.length; i++) {
								setsubColums(cellarray[i], tablegrid, dataType, label, true);
							}
						}
						setPageCompleteEnable();
					}
				}
			}
		});
		// 删除
		adddel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean result = MessageDialog.openConfirm(getShell(),
						Messages.getString("wizardsaction.dataselect.message.clmremoveConfirm"),
						Messages.getString("wizardsaction.dataselect.message.clmremoveConfirmmsg"));
				if (result) {
					tablegrid.remove(tablegrid.getSelectionIndex());
					setsubColums(tablegrid.getItem(tablegrid.getSelectionIndex()).getText(), tablegrid, null, null,
							false);
					setPageCompleteEnable();
				}
			}
		});

		return composite;
	}

	private void setsubColums(String id, Table tablegrid, Map<String, String> datatype, Map<String, String> label,
			boolean isadd) {
		TableItem[] tbItems = tablegrid.getItems();
		int chIdIndex = -1;
		TableItem citem = null;
		for (int i = 0; i < tbItems.length; i++) {
			citem = tbItems[i];
			if (id.equals(citem.getText(0))) {
				chIdIndex = i;
			}
		}
		if (isadd) {
			if (chIdIndex < 0) {
				String dataType = DataType.getDataTypeBydatabase(datatype.get(id).toUpperCase());
				TableItem tableitem = new TableItem(tablegrid, SWT.NONE);
				tableitem.setText(new String[] { id, label.get(id), "80", dataType });
				subcolumns.add(id);
				sublabels.put(id, label.get(id));
				subwidths.put(id, "80");
				subdatatypes.put(id, dataType);
			}
		} else {
			if (chIdIndex > -1) {
				tablegrid.remove(chIdIndex);
				subcolumns.remove(id);
				sublabels.remove(id);
				subwidths.remove(id);
				subdatatypes.remove(id);
			}
		}
	}

	private void setPageCompleteEnable() {
		boolean enable = (dbkey != null & table != null && subtable != null && !"".equals(subtable) && subdirect != null
				&& subdirect != null && columns.size() > 0 && subcolumns.size() > 0);
		setPageComplete(enable);
	}

	@Override
	public IWizardPage getNextPage() {
		return getWizard().getPage("directGridPageEnd");
	}

}