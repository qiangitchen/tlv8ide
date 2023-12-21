package com.tulin.v8.ide.wizards.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.generator.CodeGenerator;
import com.tulin.v8.ide.wizards.Messages;

import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;

public class NewMapperPage extends WizardPage {
	ISelection selection;

	private Text packgeName;

	public String projectPath;

	public Map<String, IDBConfig> DBConfig = new HashMap<String, IDBConfig>();

	private Tree tree;

	public Map<String, Object> checkedItem = new HashMap<String, Object>();

	private String dbkey;

	private Text keyField;

	private Button isAutoincrementKey;

	private Button createController;

	private Button createService;

	public NewMapperPage(ISelection selection) {
		super("newMapperPage");
		this.selection = selection;
		setTitle(Messages.getString("wizards.newmapper.title"));
		setMessage(Messages.getString("wizards.newmapper.text"));
	}

	@Override
	public void createControl(Composite parent) {
		parent.getShell().setText(Messages.getString("wizards.newmapper.title"));
		Composite container = new Composite(parent, SWT.FILL);
		container.setLayout(new GridLayout(2, false));

		GridData ttlay = new GridData(GridData.FILL_HORIZONTAL);
		ttlay.grabExcessHorizontalSpace = true;
		ttlay.minimumWidth = 200;
		ttlay.heightHint = 25;

		Label label = new Label(container, SWT.NONE);
		label.setText(Messages.getString("wizards.dataselect.message.dataSource"));
		GridData dataG = new GridData(SWT.FILL, SWT.CENTER, false, false);
		dataG.heightHint = 25;
		dataG.widthHint = 60;
		label.setLayoutData(dataG);

		final Combo dbkeyCombo = new Combo(container, SWT.DROP_DOWN);
		dbkeyCombo.setLayoutData(ttlay);

		Label labels = new Label(container, SWT.NONE);
		labels.setText(Messages.getString("wizards.dataselect.message.Tableview"));
		labels.setLayoutData(new GridData(SWT.NONE));

		Composite composite = new Composite(container, SWT.FILL);
		GridData ttlays = new GridData(GridData.FILL_HORIZONTAL);
		ttlays.grabExcessHorizontalSpace = true;
		ttlays.minimumWidth = 200;
		composite.setLayoutData(ttlays);
		composite.setLayout(new GridLayout(2, false));

		final Text searchText = new Text(composite, SWT.BORDER);
		searchText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button button = new Button(composite, SWT.PUSH);
		button.setText(Messages.getString("wizards.dataselect.message.search"));
		button.setLayoutData(new GridData(SWT.NONE));

		tree = new Tree(container, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL);
		GridData treelay = new GridData(GridData.FILL_BOTH);
		treelay.grabExcessHorizontalSpace = true;
		treelay.horizontalSpan = 2;
		tree.setLayoutData(treelay);

		IDBConfig[] dbConfigs = DBConfigManager.getDBConfigs();
		String[] dbkeys = new String[dbConfigs.length];
		for (int i = 0; i < dbConfigs.length; i++) {
			dbkeys[i] = dbConfigs[i].getDbName();
			DBConfig.put(dbConfigs[i].getDbName(), dbConfigs[i]);
		}
		dbkeyCombo.setItems(dbkeys);
		dbkeyCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			// 数据源选择事件
			public void widgetSelected(SelectionEvent e) {
				dbkey = dbkeyCombo.getText();
				try {
					Map<String, List<String>> TreeitemData = CommonUtil.getDataObject(dbkeyCombo.getText(),
							DBConfig.get(dbkey).getSchema());
					List<String> litable = TreeitemData.get("TABLE");
					tree.removeAll();
					for (int i = 0; i < litable.size(); i++) {
						TreeItem treeitem = new TreeItem(tree, SWT.NONE);
						treeitem.setText(litable.get(i));
						treeitem.setImage(
								PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
					}
				} catch (Exception e1) {
					setMessage(e1.toString());
					e1.printStackTrace();
				}
			}
		});

		searchText.addListener(SWT.KeyUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.keyCode == 13) {
					try {
						Map<String, List<String>> TreeitemData = getDataObject(dbkeyCombo.getText(),
								searchText.getText());
						List<String> litable = TreeitemData.get("TABLE");
						tree.removeAll();
						for (int i = 0; i < litable.size(); i++) {
							TreeItem treeitem = new TreeItem(tree, SWT.NONE);
							treeitem.setText(litable.get(i));
							treeitem.setImage(
									PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
						}
					} catch (Exception e1) {
						setMessage(e1.toString());
						e1.printStackTrace();
					}
				}
			}
		});

		tree.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				if (item.getChecked()) {
					checkedItem.put(item.getText(), item);
				} else {
					checkedItem.remove(item.getText());
				}
				if (checkedItem.isEmpty()) {
					setPageComplete(false);
				} else {
					setPageComplete(true);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		Label packgeNameLable = new Label(container, SWT.NONE);
		packgeNameLable.setText(Messages.getString("wizards.newmapper.pname"));
		packgeNameLable.setLayoutData(new GridData(SWT.NONE));
		packgeName = new Text(container, SWT.FILL | SWT.BORDER);
		packgeName.setLayoutData(ttlay);

		Label name = new Label(container, SWT.NONE);
		name.setText(Messages.getString("View.Action.NewMapper.keyfield"));
		name.setLayoutData(new GridData(SWT.NONE));
		keyField = new Text(container, SWT.FILL | SWT.BORDER);
		keyField.setLayoutData(ttlay);

		Label autocl = new Label(container, SWT.NONE);
		autocl.setText(Messages.getString("View.Action.NewMapper.isAutoincrementKey"));
		autocl.setLayoutData(new GridData(SWT.NONE));
		isAutoincrementKey = new Button(container, SWT.CHECK);
		isAutoincrementKey.setLayoutData(ttlay);

		Label createSvc = new Label(container, SWT.NONE);
		createSvc.setText(Messages.getString("View.Action.NewMapper.createService"));
		createSvc.setLayoutData(new GridData(SWT.NONE));
		createService = new Button(container, SWT.CHECK);
		createService.setLayoutData(ttlay);
		createService.setSelection(true);

		Label createCtl = new Label(container, SWT.NONE);
		createCtl.setText(Messages.getString("View.Action.NewMapper.createController"));
		createCtl.setLayoutData(new GridData(SWT.NONE));
		createController = new Button(container, SWT.CHECK);
		createController.setLayoutData(ttlay);
		createController.setSelection(true);

		setControl(container);

		packgeName.setText("com.tlv8");
		keyField.setText("fid");

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IPackageFragment) {
				IPackageFragment selpackage = (IPackageFragment) obj;
				packgeName.setText(selpackage.getElementName());
				packgeName.setEnabled(false);
				projectPath = selpackage.getJavaProject().getResource().getLocation().toOSString();
				System.out.println(projectPath);
			}
		}
		setPageComplete(false);
	}

	public static Map<String, List<String>> getDataObject(String dbkey, String search) throws Exception {
		Map<String, List<String>> rmap = new HashMap<String, List<String>>();
		Map<String, List<String>> map = CommonUtil.getDataObject(dbkey, null);
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

	public boolean canFinish() {
		return isPageComplete();
	}

	/**
	 * 完成操作
	 * 
	 * @param monitor
	 * @throws Exception
	 */
	public void doFinish() throws Exception {
		CodeGenerator codegener = new CodeGenerator(dbkey, packgeName.getText(), keyField.getText(), projectPath);
		for (String tablename : checkedItem.keySet()) {
			// codegener.genCode(tablename);
			codegener.genCodeByCustomModelName(tablename, null, isAutoincrementKey.getSelection(),
					createService.getSelection(), createController.getSelection());
		}
	}

}
