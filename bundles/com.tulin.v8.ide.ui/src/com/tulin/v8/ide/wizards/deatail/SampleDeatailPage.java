package com.tulin.v8.ide.wizards.deatail;

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
import com.tulin.v8.core.utils.DataType;
import com.tulin.v8.ide.wizards.DataSelectPage;
import com.tulin.v8.ide.wizards.Messages;

public class SampleDeatailPage extends WizardPage {
	private DataSelectPage dataSelectPage;
	private String dbkey = null;
	private String tvName = null;
	private String bfdbkey = null;
	private String bftvName = null;
	private List<String> columns = new ArrayList<String>();
	private Map<String, String> labels = new HashMap<String, String>();
	private Map<String, String> dedatatypes = new HashMap<String, String>();
	private Table table = null;
	private Table tablegrid = null;

	public SampleDeatailPage(DataSelectPage Page) {
		super("sampleDeatailPage");
		setTitle(Messages.getString("wizardsaction.deatail.title"));
		setDescription(Messages.getString("wizards.dataselect.message.errSelectDatasource"));
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

		ToolBar toolbar = new ToolBar(composite, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		GridData toolbarcl = new GridData(SWT.FILL);
		toolbar.setLayoutData(toolbarcl);
		ToolItem selectallitem = new ToolItem(toolbar, SWT.PUSH | SWT.BORDER);
		selectallitem.setText(Messages.getString("wizards.dataselect.message.checkAll"));
		ToolItem cancelitem = new ToolItem(toolbar, SWT.PUSH | SWT.BORDER);
		cancelitem.setText(Messages.getString("wizards.dataselect.message.uncheckAll"));

		table = new Table(composite, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.CHECK);
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
		label.setText(Messages.getString("wizardsaction.dataselect.message.deatailsetting"));

		tablegrid = new Table(compos, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tablegrid.setLayoutData(new GridData(GridData.FILL_BOTH));
		tablegrid.setHeaderVisible(true);
		tablegrid.setLinesVisible(true);
		tablegrid.setEnabled(true);
		TableColumn tablegridcolumn1 = new TableColumn(tablegrid, SWT.NONE);
		tablegridcolumn1.setWidth(120);
		tablegridcolumn1.setText(Messages.getString("wizards.dataselect.message.dataclumn"));
		TableColumn tablegridcolumn2 = new TableColumn(tablegrid, SWT.NONE);
		tablegridcolumn2.setWidth(120);
		tablegridcolumn2.setText(Messages.getString("wizards.dataselect.message.datatdesc"));
		TableColumn tablegridcolumn3 = new TableColumn(tablegrid, SWT.NONE);
		tablegridcolumn3.setWidth(100);
		tablegridcolumn3.setText(Messages.getString("wizards.dataselect.message.datatype"));
		final TableEditor editor = new TableEditor(tablegrid);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;

		sashForm.setWeights(new int[] { 2, 3 });

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
						TableItem tableitem = new TableItem(tablegrid, SWT.NONE);
						tableitem.setText(new String[] { item.getText(0), item.getText(2), "input" });
						columns.add(item.getText(0));
						labels.put(item.getText(0), item.getText(2));
						dedatatypes.put(item.getText(0), "input");
					}
				} else {
					if (chIdIndex > -1) {
						tablegrid.remove(chIdIndex);
						columns.remove(item.getText(0));
						labels.remove(item.getText(0));
						dedatatypes.remove(item.getText(0));
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
					TableItem tableitem = new TableItem(tablegrid, SWT.NONE);
					tableitem.setText(new String[] { item.getText(0), item.getText(2), "input" });
					columns.add(item.getText(0));
					labels.put(item.getText(0), item.getText(2));
					dedatatypes.put(item.getText(0), "input");
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
					dedatatypes.remove(item.getText(0));
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
						if (rectangle.contains(point) && i != 2) {
							final int column = i;
							final Text text = new Text(tablegrid, SWT.NONE);
							Listener textListener = new Listener() {
								public void handleEvent(final Event event) {
									switch (event.type) {
									case SWT.FocusOut:
										item.setText(column, text.getText());
										if (column == 1) {
											labels.put(item.getText(0), text.getText());
										}
										text.dispose();
										break;
									case SWT.Traverse:
										switch (event.detail) {
										case SWT.TRAVERSE_RETURN:
											item.setText(column, text.getText());
											if (column == 1) {
												labels.put(item.getText(0), text.getText());
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
							final Combo combo = new Combo(tablegrid, SWT.DROP_DOWN);
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

	@Override
	public IWizardPage getNextPage() {
		dbkey = dataSelectPage.getDbkey();
		tvName = dataSelectPage.getProjectName();
		if (tvName != null) {
			// labeltable.setText(Messages.getString("wizardsaction.dataselect.message.delectedtableview")
			// + tvName);
			if (!dbkey.equals(bfdbkey) || !tvName.equals(bftvName)) {
				initData();
				bfdbkey = dbkey;
				bftvName = tvName;
			}
		}
		setMessage(Messages.getString("wizardsaction.dataselect.message.delectedDatasource") + dbkey
				+ Messages.getString("wizardsaction.dataselect.message.delectedTable") + tvName + ".");
		return getWizard().getPage("sampleDeatailEnd");
	}

	private void initData() {
		List<String[]> columnlist;
		table.removeAll();
		tablegrid.removeAll();
		try {
			columnlist = CommonUtil.getTableColumn(dbkey, tvName);
			for (int i = 0; i < columnlist.size(); i++) {
				TableItem tableitem = new TableItem(table, SWT.NONE);
				tableitem.setText(columnlist.get(i));
			}
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

	public Map<String, String> getDedatatypes() {
		return dedatatypes;
	}

	public void setDedatatypes(Map<String, String> dedatatypes) {
		this.dedatatypes = dedatatypes;
	}

	public Table getTablegrid() {
		return this.tablegrid;
	}

}
