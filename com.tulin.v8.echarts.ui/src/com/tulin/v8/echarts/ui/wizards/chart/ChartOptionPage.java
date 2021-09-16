package com.tulin.v8.echarts.ui.wizards.chart;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.tulin.v8.core.Configuration;
import com.tulin.v8.core.DBUtils;
import com.tulin.v8.core.StringArray;
import com.tulin.v8.core.Sys;
import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.echarts.ui.wizards.Messages;

public class ChartOptionPage extends WizardPage {
	private String dbkey = null;

	Group data;
	Tree tableview;
	TreeItem root1;
	TreeItem root2;
	Text sqlText;

	Map<String, String> checkedTable;
	Map<String, String> checkedColumn;

	Button testbtn;
	Table resulttable;

	public ChartOptionPage() {
		super("chartoptionpage");
		setTitle(Messages.getString("wizards.echart.title"));
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		Group dbs = new Group(composite, SWT.NONE);
		dbs.setText(Messages.getString("wizards.dataselect.message.dataSource"));
		dbs.setLayout(new GridLayout());
		dbs.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		final Combo dbkeyCombo = new Combo(dbs, SWT.DROP_DOWN);
		Map<String, Map<String, String>> rm = Configuration.getConfig();
		Set<String> k = rm.keySet();
		Iterator<String> it = k.iterator();
		String[] dbkeys = new String[k.size()];
		int i = 0;
		while (it.hasNext()) {
			String key = (String) it.next();
			dbkeys[i] = key;
			i++;
		}
		dbkeyCombo.setItems(dbkeys);
		dbkeyCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		SashForm sashForm = new SashForm(composite, SWT.FILL);
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		data = new Group(sashForm, SWT.NONE);
		data.setText(Messages.getString("wizards.dataselect.message.Tableview"));
		data.setLayout(new GridLayout(2, false));

		final Text searchText = new Text(data, SWT.BORDER);
		searchText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button button = new Button(data, SWT.PUSH);
		button.setText(Messages.getString("wizards.dataselect.message.search"));
		button.setLayoutData(new GridData(SWT.NONE));
		tableview = new Tree(data, SWT.BORDER | SWT.CHECK | SWT.V_SCROLL);
		GridData treelay = new GridData(GridData.FILL_BOTH);
		treelay.grabExcessHorizontalSpace = true;
		treelay.horizontalSpan = 2;
		tableview.setLayoutData(treelay);
		root1 = new TreeItem(tableview, SWT.NONE);
		root2 = new TreeItem(tableview, SWT.NONE);
		root1.setText("TABLE");
		root1.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
		root2.setText("VIEW");
		root2.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));

		dbkeyCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			// 数据源选择事件
			public void widgetSelected(SelectionEvent e) {
				setDbkey(dbkeyCombo.getText());
				try {
					Map<String, List<String>> TreeitemData = CommonUtil.getDataObject(dbkeyCombo.getText());
					loadTreeItem(TreeitemData);
					setMessage(getDbkey());
				} catch (Exception e1) {
					setMessage(e1.toString());
				}
			}
		});

		searchText.addListener(SWT.KeyUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.keyCode == 13) {
					Map<String, List<String>> TreeitemData;
					try {
						TreeitemData = getDataObject(getDbkey(), searchText.getText());
						loadTreeItem(TreeitemData);
					} catch (Exception e1) {
						setMessage(e1.toString());
					}
				}
			}
		});

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Map<String, List<String>> TreeitemData;
				try {
					TreeitemData = getDataObject(getDbkey(), searchText.getText());
					loadTreeItem(TreeitemData);
				} catch (Exception e1) {
					setMessage(e1.toString());
				}
			}
		});

		SashForm child = new SashForm(sashForm, SWT.VERTICAL);
		Group sqls = new Group(child, SWT.NONE);
		sqls.setText("sql");
		sqls.setLayout(new GridLayout());
		sqlText = new Text(sqls, SWT.ABORT | SWT.WRAP | SWT.BORDER);
		sqlText.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite childcomp = new Composite(child, SWT.NONE);
		childcomp.setLayout(new GridLayout(2, false));

		testbtn = new Button(childcomp, SWT.PUSH);
		testbtn.setText("Test SQL");
		testbtn.setLayoutData(new GridData());
		testbtn.setEnabled(false);

		Label label = new Label(childcomp, SWT.NONE);
		label.setText(Messages.getString("wizards.echart.datareslendes"));
		label.setLayoutData(new GridData());

		Group sqlres = new Group(childcomp, SWT.NONE);
		sqlres.setText("data");
		GridData sqlreslay = new GridData(GridData.FILL_BOTH);
		sqlreslay.grabExcessHorizontalSpace = true;
		sqlreslay.horizontalSpan = 2;
		sqlres.setLayoutData(sqlreslay);
		sqlres.setLayout(new GridLayout());
		resulttable = new Table(sqlres, SWT.BORDER | SWT.FULL_SELECTION);
		resulttable.setHeaderVisible(true);
		resulttable.setLinesVisible(true);
		resulttable.setLayoutData(new GridData(GridData.FILL_BOTH));

		child.setWeights(new int[] { 1, 1 });

		sashForm.setWeights(new int[] { 1, 3 });

		testbtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				testSQL();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		sqlText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				changeSate(!isEmpty(getSQL()));
			}
		});

		tableview.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				TreeItem[] selection = tableview.getSelection();
				TreeItem item = selection[0];
				if (item.getItemCount() < 1) {
					try {
						String dbkey = getDbkey();
						List<String[]> columnlist = CommonUtil.getTableColumn(dbkey, item.getText());
						for (int i = 0; i < columnlist.size(); i++) {
							TreeItem cellitem = new TreeItem(item, SWT.NONE);
							cellitem.setText(columnlist.get(i));
							cellitem.setImage(TuLinPlugin.getIcon("column.gif"));
						}
						item.setExpanded(true);
					} catch (Exception e1) {
						setMessage(e1.toString());
					}
				}
			}

			@Override
			public void mouseDown(MouseEvent arg0) {
			}

			@Override
			public void mouseUp(MouseEvent arg0) {
			}
		});

		tableview.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (event.detail == SWT.CHECK) {
					TreeItem item = (TreeItem) event.item;
					boolean checked = item.getChecked();
					checkChildren(item.getItems(), checked);

					// 触发这个的Item的grayed = false，因为这是个CHECK事件，要么全选，要么全不选。
					checkParent(item.getParentItem(), checked, false);

					checkItemData(item, checked);

					parseSQL();
				}
			}
		});

		setPageComplete(false);
		setControl(composite);
	}

	void checkParent(TreeItem parent, boolean checked, boolean grayed) {
		if (parent == null)// 递归退出条件：父亲为空。
			return;
		for (TreeItem child : parent.getItems()) {
			if (child.getGrayed() || checked != child.getChecked()) {
				// 1，子节点有一个为【部分选中的】，直接设置父节点为【部分选中的】。
				// 2，子节点不完全相同，说明【部分选中的】。
				checked = grayed = true;
				break;
			}
		}
		parent.setChecked(checked);
		parent.setGrayed(grayed);
		String pt = parent.getText();
		if ("TABLE".equals(pt) || "VIEW".equals(pt)) {
			if (!grayed) {
				checkedTable.remove(parent.getText());
			}
		}
		checkParent(parent.getParentItem(), checked, grayed);
	}

	void checkChildren(TreeItem[] children, boolean checked) {
		if (children.length == 0)// 递归退出条件：孩子为空。
			return;
		for (TreeItem child : children) {
			child.setGrayed(false);// 必须设置这个，因为本来节点可能【部分选中的】。
			child.setChecked(checked);
			checkItemData(child, checked);
			checkChildren(child.getItems(), checked);
		}
	}

	void checkItemData(TreeItem treeitem, boolean checked) {
		TreeItem prant = treeitem.getParentItem();
		if (prant != null) {
			String pt = prant.getText();
			if (checked) {
				if ("TABLE".equals(pt) || "VIEW".equals(pt)) {
					checkedTable.put(treeitem.getText(), prant.getText());
				} else if (prant != null) {
					checkedColumn.put(treeitem.getText(), prant.getText());
					checkedTable.put(prant.getText(), prant.getParentItem().getText());
				}
			} else {
				if ("TABLE".equals(pt) || "VIEW".equals(pt)) {
					checkedTable.remove(treeitem.getText());
				} else if (prant != null) {
					checkedColumn.remove(treeitem.getText());
				}
			}
		}
	}

	void changeSate(boolean b) {
		testbtn.setEnabled(b);
		setPageComplete(b);
	}

	void parseSQL() {
		StringBuffer sqlbf = new StringBuffer();
		if (checkedColumn.isEmpty() && !checkedTable.isEmpty()) {
			sqlbf.append("select * from ");
			StringArray arr = new StringArray();
			for (String table : checkedTable.keySet()) {
				arr.push(table);
			}
			sqlbf.append(arr.join(","));
		} else if (!checkedColumn.isEmpty() && !checkedTable.isEmpty()) {
			sqlbf.append("select ");
			StringArray arr = new StringArray();
			for (String column : checkedColumn.keySet()) {
				if (checkedTable.keySet().size() > 1) {
					arr.push(checkedColumn.get(column) + "." + column);
				} else {
					arr.push(column);
				}
			}
			sqlbf.append(arr.join(","));
			sqlbf.append(" from ");
			StringArray arr1 = new StringArray();
			for (String table : checkedTable.keySet()) {
				arr1.push(table);
			}
			sqlbf.append(arr1.join(","));
		}
		sqlText.setText(sqlbf.toString());
		changeSate(!isEmpty(getSQL()));
	}

	public static Map<String, List<String>> getDataObject(String dbkey, String search) throws Exception {
		Map<String, List<String>> rmap = new HashMap<String, List<String>>();
		Map<String, List<String>> map = CommonUtil.getDataObject(dbkey);
		List<String> rtable = new ArrayList<String>();
		List<String> table = map.get("TABLE");
		for (String t : table) {
			if (t.toUpperCase().indexOf(search.toUpperCase()) > -1) {
				rtable.add(t);
			}
		}
		List<String> rview = new ArrayList<String>();
		List<String> view = map.get("VIEW");
		for (String v : view) {
			if (v.toUpperCase().indexOf(search.toUpperCase()) > -1) {
				rview.add(v);
			}
		}
		rmap.put("TABLE", rtable);
		rmap.put("VIEW", rview);
		return rmap;
	}

	private void loadTreeItem(Map<String, List<String>> TreeitemData) {
		checkedTable = new HashMap<String, String>();
		checkedColumn = new HashMap<String, String>();
		List<String> litable = TreeitemData.get("TABLE");
		root1.removeAll();
		for (int i = 0; i < litable.size(); i++) {
			TreeItem treeitem = new TreeItem(root1, SWT.NONE);
			treeitem.setText(litable.get(i));
			treeitem.setImage(TuLinPlugin.getIcon("table.gif"));
		}
		root2.removeAll();
		List<String> liview = TreeitemData.get("VIEW");
		for (int i = 0; i < liview.size(); i++) {
			TreeItem treeitem = new TreeItem(root2, SWT.NONE);
			treeitem.setText(liview.get(i));
			treeitem.setImage(TuLinPlugin.getIcon("view.gif"));
		}
	}

	void clearColumns(Table table) {
		table.removeAll();
		table.clearAll();
		for (TableColumn column : table.getColumns()) {
			column.dispose();
		}
	}

	void testSQL() {
		String sql = sqlText.getText();
		sql = sql.replace("\t", " ");
		sql = sql.replace("\n", " ");
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getAppConn(dbkey);
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			clearColumns(resulttable);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columns = rsmd.getColumnCount();
			for (int i = 1; i <= columns; i++) {
				String cellName = rsmd.getColumnLabel(i).toUpperCase();
				TableColumn columnName = new TableColumn(resulttable, SWT.NONE);
				columnName.setText(cellName);
				columnName.setWidth(80);
				columnName.pack();
			}
			int r = 0;
			while (rs.next()) {
				if (r > 100) {
					break;
				}
				String[] row = new String[columns];
				for (int c = 1; c <= columns; c++) {
					String cellType = rsmd.getColumnTypeName(c);
					if ("BLOB".equals(cellType.toUpperCase())) {
						row[c - 1] = "<<Blob>>";
					} else {
						row[c - 1] = rs.getString(c);
					}
				}
				TableItem item = new TableItem(resulttable, SWT.NONE);
				item.setText(row);
				r++;
			}
			resulttable.setVisible(true);
		} catch (Exception e) {
			setMessage(e.getMessage());
			Sys.packErrMsg(e.getMessage());
		} finally {
			DBUtils.CloseConn(conn, stm, rs);
		}
	}

	boolean isEmpty(Object object) {
		return object == null || "".equals(object);
	}

	@Override
	public IWizardPage getNextPage() {
		if (isEmpty(getDbkey()) || isEmpty(getSQL())) {
			return super.getNextPage();
		} else {
			return getWizard().getPage("chartmodlepage");
		}
	}

	protected void setDbkey(String dbkey) {
		this.dbkey = dbkey;
	}

	public String getDbkey() {
		return dbkey;
	}

	public String getSQL() {
		return sqlText.getText();
	}

}
