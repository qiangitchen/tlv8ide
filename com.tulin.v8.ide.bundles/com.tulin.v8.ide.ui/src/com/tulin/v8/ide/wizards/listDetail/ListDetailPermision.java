package com.tulin.v8.ide.wizards.listDetail;

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

import com.tulin.v8.core.utils.DataType;
import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.wizards.Messages;
import com.tulin.v8.ide.wizards.ProjectSelectPage;
import com.tulin.v8.ide.wizards.utils.DataDBKeySelectDialog;
import com.tulin.v8.ide.wizards.utils.TableCellsSelectDialog;
import com.tulin.v8.ide.wizards.utils.TableSelectDialog;

public class ListDetailPermision extends WizardPage {
	private SashForm sashForm;
	private Composite dataComp;
	private Composite confComp;
	private Composite deconfComp;
	protected StackLayout stackLayout = new StackLayout();
	protected Composite displayComp;
	private Table ptable;
	private Table tablegrid;
	private Table detablegrid;
	public String dbkey;
	public String table;
	public String subtable;
	public String subdirect;

	public List<String> columns = new ArrayList<String>();
	public Map<String, String> labels = new HashMap<String, String>();
	public Map<String, String> widths = new HashMap<String, String>();
	public Map<String, String> datatypes = new HashMap<String, String>();

	public List<String> decolumns = new ArrayList<String>();
	public Map<String, String> delabels = new HashMap<String, String>();
	public Map<String, String> dedatatypes = new HashMap<String, String>();

	public ListDetailPermision() {
		super("listDetailPermision");
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
		itemdata.setText(Messages.getString("wizardsaction.dataselect.message.dataperm"));
		itemdata.setImage(StudioPlugin.getIcon("conf.gif"));
		TableItem itemconf = new TableItem(ptable, SWT.NONE);
		itemconf.setText("列表配置");
		itemconf.setImage(StudioPlugin.getIcon("conf.gif"));
		TableItem itemdeconf = new TableItem(ptable, SWT.NONE);
		itemdeconf.setText("详细配置");
		itemdeconf.setImage(StudioPlugin.getIcon("conf.gif"));

		displayComp = new Composite(sashForm, SWT.NONE);
		displayComp.setLayout(stackLayout);

		sashForm.setWeights(new int[] { 2, 8 });
		setMessage(Messages.getString("wizards.dataselect.message.setbysquens"));

		dataComp = setDatapermision(displayComp);
		confComp = setTablepermision(displayComp);
		deconfComp = setdeTablepermision(displayComp);
		stackLayout.topControl = dataComp;

		ptable.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				TableItem item = (TableItem) e.item;
				String name = item.getText();
				if (Messages.getString("wizardsaction.dataselect.message.dataperm").equals(name)) {
					setTitle("列表详细-主数据");
					stackLayout.topControl = dataComp;
				}
				if ("列表配置".equals(name)) {
					setTitle("列表详细-列表配置");
					stackLayout.topControl = confComp;
				}
				if ("详细配置".equals(name)) {
					setTitle("列表详细-详细配置");
					stackLayout.topControl = deconfComp;
				}
				displayComp.layout();
			}
		});
		ptable.setSelection(0);
		setTitle("列表详细-主数据");

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
				DataDBKeySelectDialog dial = new DataDBKeySelectDialog(getShell());
				int result = dial.open();
				if (IDialogConstants.OK_ID == result) {
					text1.setText(dial.getItemsToOpen());
					if (dbkey != null && !"".equals(dbkey) && !dbkey.equals(text1.getText())) {
						table = null;
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
		return composite;
	}

	/*
	 * 列表配置
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

		tablegrid = new Table(composite, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
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
					MessageDialog.openError(getShell(), Messages.getString("wizards.dataselect.message.errtitle"), Messages.getString("wizardsaction.dataselect.message.selectdbandtable"));
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
				boolean result = MessageDialog.openConfirm(getShell(), Messages.getString("wizardsaction.dataselect.message.clmremoveConfirm"), Messages.getString("wizardsaction.dataselect.message.clmremoveConfirmmsg"));
				if (result) {
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
	 * 详细配置
	 */
	private Composite setdeTablepermision(Composite shell) {
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

		detablegrid = new Table(composite, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
		GridData gridbar = new GridData(GridData.FILL_BOTH);
		gridbar.horizontalSpan = 3;
		detablegrid.setLayoutData(gridbar);
		detablegrid.setHeaderVisible(true);
		detablegrid.setLinesVisible(true);
		detablegrid.setEnabled(true);
		TableColumn tablegridcolumn1 = new TableColumn(detablegrid, SWT.NONE);
		tablegridcolumn1.setWidth(120);
		tablegridcolumn1.setText(Messages.getString("wizards.dataselect.message.dataclumn"));
		TableColumn tablegridcolumn2 = new TableColumn(detablegrid, SWT.NONE);
		tablegridcolumn2.setWidth(180);
		tablegridcolumn2.setText(Messages.getString("wizards.dataselect.message.datatdesc"));
		TableColumn tablegridcolumn4 = new TableColumn(detablegrid, SWT.NONE);
		tablegridcolumn4.setWidth(100);
		tablegridcolumn4.setText(Messages.getString("wizards.dataselect.message.datatype"));
		final TableEditor editor = new TableEditor(detablegrid);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;

		detablegrid.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Rectangle clientArea = detablegrid.getClientArea();
				Point point = new Point(event.x, event.y);
				int index = detablegrid.getTopIndex();
				while (index < detablegrid.getItemCount()) {
					boolean visible = false;
					final TableItem item = detablegrid.getItem(index);
					for (int i = 1; i < detablegrid.getColumnCount(); i++) {
						Rectangle rectangle = item.getBounds(i);
						if (rectangle.contains(point) && i != 2) {
							final int column = i;
							final Text text = new Text(detablegrid, SWT.NONE);
							Listener textListener = new Listener() {
								public void handleEvent(final Event event) {
									switch (event.type) {
									case SWT.FocusOut:
										item.setText(column, text.getText());
										if (column == 1) {
											delabels.put(item.getText(0), text.getText());
										}
										text.dispose();
										break;
									case SWT.Traverse:
										switch (event.detail) {
										case SWT.TRAVERSE_RETURN:
											item.setText(column, text.getText());
											if (column == 1) {
												delabels.put(item.getText(0), text.getText());
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
						} else if (rectangle.contains(point) && i == 2) {
							final Combo combo = new Combo(detablegrid, SWT.DROP_DOWN);
							combo.setItems(DataType.detailDataType);
							Listener comboListener = new Listener() {
								public void handleEvent(final Event event) {
									switch (event.type) {
									case SWT.FocusOut:
										item.setText(2, combo.getText());
										dedatatypes.put(item.getText(0), combo.getText());
										combo.dispose();
										break;
									case SWT.Traverse:
										switch (event.detail) {
										case SWT.TRAVERSE_RETURN:
											item.setText(2, combo.getText());
											dedatatypes.put(item.getText(0), combo.getText());
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
									item.setText(2, val);
									dedatatypes.put(item.getText(0), val);
								}
							});
							editor.setEditor(combo, item, 2);
							combo.setText(item.getText(2));
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
		detablegrid.addListener(SWT.MouseUp, new Listener() {
			public void handleEvent(Event arg0) {
				setPageCompleteEnable();
			}
		});
		detablegrid.addSelectionListener(new SelectionAdapter() {
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
					MessageDialog.openError(getShell(), Messages.getString("wizards.dataselect.message.errtitle"), Messages.getString("wizardsaction.dataselect.message.selectdbandtable"));
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
								setDeColums(cellarray[i], detablegrid, dataType, label, true);
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
				boolean result = MessageDialog.openConfirm(getShell(), Messages.getString("wizardsaction.dataselect.message.clmremoveConfirm"), Messages.getString("wizardsaction.dataselect.message.clmremoveConfirmmsg"));
				if (result) {
					setDeColums(detablegrid.getItem(detablegrid.getSelectionIndex()).getText(), detablegrid, null, null,
							false);
					setPageCompleteEnable();
				}
			}
		});

		return composite;
	}

	private void setDeColums(String id, Table tablegrid, Map<String, String> datatype, Map<String, String> label,
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
				TableItem tableitem = new TableItem(tablegrid, SWT.NONE);
				tableitem.setText(new String[] { id, label.get(id), "input" });
				decolumns.add(id);
				delabels.put(id, label.get(id));
				dedatatypes.put(id, "input");
			}
		} else {
			if (chIdIndex > -1) {
				tablegrid.remove(chIdIndex);
				decolumns.remove(id);
				delabels.remove(id);
				dedatatypes.remove(id);
			}
		}
	}

	private void setPageCompleteEnable() {
		boolean enable = (dbkey != null & table != null && tablegrid.getItems().length > 0
				&& detablegrid.getItems().length > 0);
		setPageComplete(enable);
	}

	@Override
	public IWizardPage getNextPage() {
		// String projectType = ((ProjectSelectPage) getWizard().getPage("Project
		// Selection")).getPojectType();
		String templet = ((ProjectSelectPage) getWizard().getPage("Project Selection")).getTemplet();
		if ("listDetailportal".equals(templet)) {// projectType.equals("列表详细(open)")
			return getWizard().getPage("listDetailPortalPageEnd");
		} else {
			return getWizard().getPage("listDetailPageEnd");
		}
	}

}