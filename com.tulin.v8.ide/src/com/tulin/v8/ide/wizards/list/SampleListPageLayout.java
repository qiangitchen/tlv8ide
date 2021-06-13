package com.tulin.v8.ide.wizards.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
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

import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.ide.utils.DataType;
import com.tulin.v8.ide.wizards.DataSelectPage;
import com.tulin.v8.ide.wizards.Messages;

public class SampleListPageLayout extends WizardPage {
	private DataSelectPage dataSelectPage;
	private String dbkey = null;
	private String tvName = null;
	private String bfdbkey = null;
	private String bftvName = null;
	private List<String> columns = new ArrayList<String>();
	private Map<String, String> labels = new HashMap<String, String>();
	private Map<String, String> widths = new HashMap<String, String>();
	private Map<String, String> datatypes = new HashMap<String, String>();
	// private Label labeltable = null;
	private Table table = null;
	private Table tablegrid = null;

	public SampleListPageLayout(DataSelectPage Page) {
		super("samplelistPageLayout");
		setTitle("简单列表");
		setDescription("配置列表详细参数.");
		dataSelectPage = Page;
	}

	public void createControl(Composite parent) {
		SashForm sashForm = new SashForm(parent, SWT.NONE);
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 3);
		formData.right = new FormAttachment(100, -3);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, -25);
		sashForm.setLayoutData(formData);

		Composite composite = new Composite(sashForm, SWT.FILL);
		composite.setLayout(new GridLayout());

		// labeltable = new Label(composite, SWT.NONE);
		// labeltable.setText(Messages.getString("wizardsaction.dataselect.message.delectedtableview"));
		ToolBar toolbar = new ToolBar(composite, SWT.FLAT | SWT.WRAP
				| SWT.RIGHT);
		GridData toolbarcl = new GridData(SWT.FILL);
		toolbar.setLayoutData(toolbarcl);
		ToolItem selectallitem = new ToolItem(toolbar, SWT.PUSH | SWT.BORDER);
		selectallitem.setText("全  选");
		ToolItem cancelitem = new ToolItem(toolbar, SWT.PUSH | SWT.BORDER);
		cancelitem.setText("全取消");

		final Text searchText = new Text(composite, SWT.BORDER);
		searchText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		table = new Table(composite, SWT.BORDER | SWT.V_SCROLL
				| SWT.FULL_SELECTION | SWT.CHECK);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableColumn tablecolumn1 = new TableColumn(table, SWT.NONE);
		tablecolumn1.setWidth(80);
		tablecolumn1.setText(Messages.getString("wizards.dataselect.message.dataclumn"));
		TableColumn tablecolumn2 = new TableColumn(table, SWT.NONE);
		tablecolumn2.setWidth(90);
		tablecolumn2.setText(Messages.getString("wizards.dataselect.message.datatype"));
		TableColumn tablecolumn3 = new TableColumn(table, SWT.NONE);
		tablecolumn3.setWidth(100);
		tablecolumn3.setText(Messages.getString("wizards.dataselect.message.datatdesc"));

		Composite compos = new Composite(sashForm, SWT.FILL);
		compos.setLayout(new GridLayout());

		Label label = new Label(compos, SWT.NONE);
		label.setText("&列表详细配置:");

		tablegrid = new Table(compos, SWT.BORDER | SWT.V_SCROLL
				| SWT.FULL_SELECTION);
		tablegrid.setLayoutData(new GridData(GridData.FILL_BOTH));
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

		sashForm.setWeights(new int[] { 2, 3 });

		searchText.addListener(SWT.KeyUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.keyCode == 13) {
					List<String[]> columnlist;
					try {
						columnlist = DataSelectPage.getTableColumn(dbkey,
								tvName, searchText.getText());
						table.removeAll();
						tablegrid.removeAll();
						for (int i = 0; i < columnlist.size(); i++) {
							TableItem tableitem = new TableItem(table, SWT.NONE);
							tableitem.setText(columnlist.get(i));
						}
						setPageComplete(false);
					} catch (Exception e) {
						setMessage(e.toString());
					}
				}
			}
		});

		table.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				TableItem item = (TableItem) e.item;
				String id = item.getText();
				TableItem[] tbItems = tablegrid.getItems();
				int chIdIndex = -1;
				TableItem citem = null;
				for (int i = 0; i < tbItems.length; i++) {
					citem = tbItems[i];
					if (id.equals(citem.getText(0))) {
						chIdIndex = i;
					}
				}
				if (item.getChecked()) {
					if (chIdIndex < 0) {
						String dataType = DataType.getDataTypeBydatabase(item
								.getText(1).toUpperCase());
						TableItem tableitem = new TableItem(tablegrid, SWT.NONE);
						tableitem.setText(new String[] { item.getText(0),
								item.getText(2), "80", dataType });
						columns.add(item.getText(0));
						labels.put(item.getText(0), item.getText(2));
						widths.put(item.getText(0), "80");
						datatypes.put(item.getText(0), dataType);
					}
				} else {
					if (chIdIndex > -1) {
						tablegrid.remove(chIdIndex);
						columns.remove(item.getText(0));
						labels.remove(item.getText(0));
						widths.remove(item.getText(0));
						datatypes.remove(item.getText(0));
					}
				}
			}
		});

		// 全选
		selectallitem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getItems();
				tablegrid.removeAll();
				for (int i = 0; i < items.length; i++) {
					items[i].setChecked(true);
					TableItem item = items[i];
					String dataType = DataType.getDataTypeBydatabase(item
							.getText(1).toUpperCase());
					TableItem tableitem = new TableItem(tablegrid, SWT.NONE);
					tableitem.setText(new String[] { item.getText(0),
							item.getText(2), "80", dataType });
					columns.add(item.getText(0));
					labels.put(item.getText(0), item.getText(2));
					widths.put(item.getText(0), "80");
					datatypes.put(item.getText(0), "input");
				}
				setPageComplete(true);
			}
		});

		// 全取消
		cancelitem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getItems();
				for (int i = 0; i < items.length; i++) {
					items[i].setChecked(false);
					TableItem item = items[i];
					columns.remove(item.getText(0));
					labels.remove(item.getText(0));
					widths.remove(item.getText(0));
					datatypes.remove(item.getText(0));
				}
				tablegrid.removeAll();
				setPageComplete(false);
			}
		});

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
											labels.put(item.getText(0),
													text.getText());
										} else if (column == 2) {
											widths.put(item.getText(0),
													text.getText());
										}
										text.dispose();
										break;
									case SWT.Traverse:
										switch (event.detail) {
										case SWT.TRAVERSE_RETURN:
											item.setText(column, text.getText());
											if (column == 1) {
												labels.put(item.getText(0),
														text.getText());
											} else if (column == 2) {
												widths.put(item.getText(0),
														text.getText());
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
							final Combo combo = new Combo(tablegrid,
									SWT.DROP_DOWN);
							combo.setItems(DataType.gridDataType);
							Listener comboListener = new Listener() {
								public void handleEvent(final Event event) {
									switch (event.type) {
									case SWT.FocusOut:
										item.setText(3, combo.getText());
										datatypes.put(item.getText(0),
												combo.getText());
										combo.dispose();
										break;
									case SWT.Traverse:
										switch (event.detail) {
										case SWT.TRAVERSE_RETURN:
											item.setText(3, combo.getText());
											datatypes.put(item.getText(0),
													combo.getText());
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
								public void widgetDefaultSelected(
										SelectionEvent e) {
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

		table.addListener(SWT.MouseUp, new Listener() {
			public void handleEvent(Event arg0) {
				if (tablegrid.getItems().length > 0) {
					setPageComplete(true);
				} else {
					setPageComplete(false);
				}
			}
		});
		tablegrid.addListener(SWT.MouseUp, new Listener() {

			public void handleEvent(Event arg0) {
				if (tablegrid.getItems().length > 0) {
					setPageComplete(true);
				} else {
					setPageComplete(false);
				}
			}
		});
		setControl(sashForm);
		setPageComplete(true);
	}

	public IWizardPage getNextPage() {
		dbkey = dataSelectPage.getDbkey();
		tvName = dataSelectPage.getProjectName();
		if (tvName != null) {
			// labeltable.setText(Messages.getString("wizardsaction.dataselect.message.delectedtableview") + tvName);
			if (!dbkey.equals(bfdbkey) || !tvName.equals(bftvName)) {
				initData();
				bfdbkey = dbkey;
				bftvName = tvName;
			}
		}
		setMessage(Messages.getString("wizardsaction.dataselect.message.delectedDatasource") + dbkey + Messages.getString("wizardsaction.dataselect.message.delectedTable") + tvName + ".");
		return getWizard().getPage("sampleListPageEnd");
	}

	private void initData() {
		List<String[]> columnlist;
		try {
			columnlist = CommonUtil.getTableColumn(dbkey, tvName);
			table.removeAll();
			tablegrid.removeAll();
			for (int i = 0; i < columnlist.size(); i++) {
				TableItem tableitem = new TableItem(table, SWT.NONE);
				tableitem.setText(columnlist.get(i));
			}
			setPageComplete(false);
		} catch (Exception e) {
			setMessage(e.toString());
		}
	}

	public String getDbkey() {
		return dbkey;
	}

	public void setDbkey(String dbkey) {
		this.dbkey = dbkey;
	}

	public String getTvName() {
		return tvName;
	}

	public void setTvName(String tvName) {
		this.tvName = tvName;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public Map<String, String> getLabels() {
		return labels;
	}

	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}

	public Map<String, String> getWidths() {
		return widths;
	}

	public void setWidths(Map<String, String> widths) {
		this.widths = widths;
	}

	public Map<String, String> getDatatypes() {
		return datatypes;
	}

	public void setDatatypes(Map<String, String> datatypes) {
		this.datatypes = datatypes;
	}

	public Table getTablegrid() {
		return this.tablegrid;
	}

}
