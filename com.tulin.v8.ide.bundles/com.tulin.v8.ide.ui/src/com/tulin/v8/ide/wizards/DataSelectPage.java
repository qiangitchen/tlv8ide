package com.tulin.v8.ide.wizards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.tulin.v8.core.utils.CommonUtil;

import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;

public class DataSelectPage extends WizardPage {
	public Map<String, IDBConfig> DBConfig = new HashMap<String, IDBConfig>();
	private String dbkey = null;
	private String projectName = null;
	private Tree tree = null;
	private ProjectSelectPage prevPage = null;

	public DataSelectPage(ProjectSelectPage projectPage) {
		super("dataSelectPage");
		setTitle(Messages.getString("wizards.dataselect.message.title"));
		setDescription(Messages.getString("wizards.dataselect.message.titleDescription"));
		prevPage = projectPage;
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);

		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.getString("wizards.dataselect.message.dataSource"));
		GridData dataG = new GridData(SWT.FILL, SWT.CENTER, false, false);
		dataG.heightHint = 25;
		dataG.widthHint = 60;
		label.setLayoutData(dataG);

		final Combo dbkeyCombo = new Combo(composite, SWT.DROP_DOWN);

		IDBConfig[] dbConfigs = DBConfigManager.getDBConfigs();
		String[] dbkeys = new String[dbConfigs.length];
		for (int i = 0; i < dbConfigs.length; i++) {
			dbkeys[i] = dbConfigs[i].getDbName();
			DBConfig.put(dbConfigs[i].getDbName(), dbConfigs[i]);
		}
		dbkeyCombo.setItems(dbkeys);

		GridData dataV = new GridData(SWT.FILL, SWT.CENTER, false, false);
		dataV.grabExcessHorizontalSpace = true;
		dbkeyCombo.setLayoutData(dataV);

		Label sepLabel1 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData sepData1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		sepLabel1.setLayoutData(sepData1);

		Composite compos = new Composite(composite, SWT.FILL);
		GridData datacomp = new GridData(GridData.FILL_BOTH);
		datacomp.grabExcessHorizontalSpace = true;
		datacomp.grabExcessVerticalSpace = true;
		datacomp.horizontalSpan = 2;
		compos.setLayoutData(datacomp);
		compos.setLayout(new FormLayout());

		SashForm sashForm = new SashForm(compos, SWT.NONE);
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 3);
		formData.right = new FormAttachment(100, -3);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, -0);
		sashForm.setLayoutData(formData);

		Composite compos1 = new Composite(sashForm, SWT.BORDER);
		compos1.setLayout(new GridLayout(3, false));

		label = new Label(compos1, SWT.NONE);
		label.setText(Messages.getString("wizards.dataselect.message.Tableview"));
		label.setLayoutData(new GridData(SWT.NONE));
		final Text searchText = new Text(compos1, SWT.BORDER);
		searchText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button button = new Button(compos1, SWT.PUSH);
		button.setText(Messages.getString("wizards.dataselect.message.search"));
		button.setLayoutData(new GridData(SWT.NONE));
		// Group tvgroup = new Group(compos1, SWT.FILL);
		// tvgroup.setText("表视图:");

		tree = new Tree(compos1, SWT.BORDER | SWT.V_SCROLL);
		GridData treelay = new GridData(GridData.FILL_BOTH);
		treelay.grabExcessHorizontalSpace = true;
		treelay.horizontalSpan = 3;
		tree.setLayoutData(treelay);
		final TreeItem root1 = new TreeItem(tree, SWT.NONE);
		final TreeItem root2 = new TreeItem(tree, SWT.NONE);
		root1.setText("TABLE");
		root1.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
		root2.setText("VIEW");
		root2.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));

		Composite compos2 = new Composite(sashForm, SWT.BORDER);
		compos2.setLayout(new GridLayout());

		label = new Label(compos2, SWT.NONE);
		label.setText(Messages.getString("wizards.dataselect.message.proptype"));

		final Table table = new Table(compos2, SWT.BORDER | SWT.FULL_SELECTION);
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

		sashForm.setWeights(new int[] { 2, 3 });

		dbkeyCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			// 数据源选择事件
			public void widgetSelected(SelectionEvent e) {
				setDbkey(dbkeyCombo.getText());
				Map<String, List<String>> TreeitemData;
				try {
					TreeitemData = CommonUtil.getDataObject(dbkeyCombo.getText(),
							DBConfig.get(dbkeyCombo.getText()).getSchema());
					List<String> litable = TreeitemData.get("TABLE");
					root1.removeAll();
					for (int i = 0; i < litable.size(); i++) {
						TreeItem treeitem = new TreeItem(root1, SWT.NONE);
						treeitem.setText(litable.get(i));
						treeitem.setImage(
								PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
					}
					root2.removeAll();
					List<String> liview = TreeitemData.get("VIEW");
					for (int i = 0; i < liview.size(); i++) {
						TreeItem treeitem = new TreeItem(root2, SWT.NONE);
						treeitem.setText(liview.get(i));
						treeitem.setImage(
								PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
					}
				} catch (Exception e1) {
					setMessage(e1.toString());
				}
			}
		});

		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				table.removeAll();
				if (item.getItems().length == 0) {
					List<String[]> columnlist;
					try {
						columnlist = CommonUtil.getTableColumn(dbkey, DBConfig.get(dbkey).getSchema(), item.getText());
						for (int i = 0; i < columnlist.size(); i++) {
							TableItem tableitem = new TableItem(table, SWT.NONE);
							tableitem.setText(columnlist.get(i));
						}
						setMessage(Messages.getString("wizards.dataselect.message.selectLbale")
								+ item.getParentItem().getText() + "-" + item.getText());
						setProjectName(item.getText());
						setPageComplete(true);
						getWizard().getPage("samplelistPageLayout").getNextPage();
					} catch (Exception e1) {
						setMessage(e1.toString());
					}
				} else {
					setMessage(Messages.getString("wizards.dataselect.message.unselectedTable"));
				}
			}
		});

		searchText.addListener(SWT.KeyUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.keyCode == 13) {
					setDbkey(dbkeyCombo.getText());
					Map<String, List<String>> TreeitemData;
					try {
						TreeitemData = getDataObject(dbkeyCombo.getText(),
								DBConfig.get(dbkeyCombo.getText()).getSchema(), searchText.getText());
						List<String> litable = TreeitemData.get("TABLE");
						root1.removeAll();
						for (int i = 0; i < litable.size(); i++) {
							TreeItem treeitem = new TreeItem(root1, SWT.NONE);
							treeitem.setText(litable.get(i));
							treeitem.setImage(
									PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
						}
						root2.removeAll();
						List<String> liview = TreeitemData.get("VIEW");
						for (int i = 0; i < liview.size(); i++) {
							TreeItem treeitem = new TreeItem(root2, SWT.NONE);
							treeitem.setText(liview.get(i));
							treeitem.setImage(
									PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
						}
					} catch (Exception e1) {
						setMessage(e1.toString());
					}
				}
			}
		});

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDbkey(dbkeyCombo.getText());
				Map<String, List<String>> TreeitemData;
				try {
					TreeitemData = getDataObject(dbkeyCombo.getText(), DBConfig.get(dbkeyCombo.getText()).getSchema(),
							searchText.getText());
					List<String> litable = TreeitemData.get("TABLE");
					root1.removeAll();
					for (int i = 0; i < litable.size(); i++) {
						TreeItem treeitem = new TreeItem(root1, SWT.NONE);
						treeitem.setText(litable.get(i));
						treeitem.setImage(
								PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
					}
					root2.removeAll();
					List<String> liview = TreeitemData.get("VIEW");
					for (int i = 0; i < liview.size(); i++) {
						TreeItem treeitem = new TreeItem(root2, SWT.NONE);
						treeitem.setText(liview.get(i));
						treeitem.setImage(
								PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
					}
				} catch (Exception e1) {
					setMessage(e1.toString());
				}
			}
		});
		setPageComplete(false);
		setControl(composite);
	}

	public static Map<String, List<String>> getDataObject(String dbkey, String schemaPattern, String search)
			throws Exception {
		Map<String, List<String>> rmap = new HashMap<String, List<String>>();
		Map<String, List<String>> map = CommonUtil.getDataObject(dbkey, schemaPattern);
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

	public static List<String[]> getTableColumn(String dbkey, String schemaPattern, String tableName, String search)
			throws Exception {
		List<String[]> rlist = new ArrayList<String[]>();
		List<String[]> list = CommonUtil.getTableColumn(dbkey, schemaPattern, tableName);
		for (String[] row : list) {
			if (row[0].toUpperCase().indexOf(search.toUpperCase()) > -1
					|| row[1].toUpperCase().indexOf(search.toUpperCase()) > -1
					|| row[2].toUpperCase().indexOf(search.toUpperCase()) > -1) {
				rlist.add(row);
			}
		}
		return rlist;
	}

	@Override
	public IWizardPage getNextPage() {
		// String proType = prevPage.getPojectType();
		String templet = prevPage.getTemplet();
		if ("simpleGrid".equals(templet)) {// "简单列表".equals(proType)
			return getWizard().getPage("samplelistPageLayout");
		} else if ("simpleDetail".equals(templet)) {// "单表详细".equals(proType)
			return getWizard().getPage("sampleDeatailPage");
		} else if ("simpleFlow".equals(templet)) {// "单表流程".equals(proType)
			return getWizard().getPage("sampleFlowPage");
		} else if ("mobilesimpleForm".equals(templet) || "mobileFlow".equals(templet)) {// "手机版表单".equals(proType)
			return getWizard().getPage("mobilesampleDeatailPage");
		}
		return null;
	}

	public void setDbkey(String dbkey) {
		this.dbkey = dbkey;
	}

	public String getDbkey() {
		return dbkey;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectName() {
		return projectName;
	}
}
