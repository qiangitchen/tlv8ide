package com.tulin.v8.ide.ui.editors.process.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.tulin.v8.core.StringArray;
import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.ui.CheckButtonSelectionAdapter;
import com.tulin.v8.ide.ui.OrgTreeComposite;
import com.tulin.v8.ide.ui.OrgTreeSelectionAdapter;
import com.tulin.v8.ide.utils.OrgUtils;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class PersonsSelectionDialog extends Dialog {
	private OrgTreeComposite containerOrg;
	private Table psmtable;
	private Text searchtext1;
	private Composite selecetedArea;
	private TreeItem currentItem;

	private Map<String, String> selectedPsm = new HashMap<String, String>();
	private Map<String, Map> selectedItem = new HashMap<String, Map>();
	private List<Group> selectionGroups = new ArrayList<Group>();
	private Map<String, Group> selecetedAreaCh = new HashMap<String, Group>();

	private String perSelectIds = "";

	public PersonsSelectionDialog(Shell parentShell) {
		super(parentShell);
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.getString("TLEditor.PersonsSelect.title"));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());
		SashForm sashForm = new SashForm(container, SWT.FILL | SWT.HORIZONTAL);

		containerOrg = new OrgTreeComposite(sashForm, SWT.FILL);

		SashForm sashright = new SashForm(sashForm, SWT.FILL | SWT.VERTICAL);

		Composite area = new Composite(sashright, SWT.FILL);
		GridLayout gridLayout = new GridLayout(3, false);
		area.setLayout(gridLayout);
		ToolBar toolbar = new ToolBar(area, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		GridData toolbarcl = new GridData(SWT.FILL);
		toolbar.setLayoutData(toolbarcl);

		ToolItem selectallitem = new ToolItem(toolbar, SWT.PUSH | SWT.BORDER);
		selectallitem.setText(Messages
				.getString("TLEditor.PersonsSelect.checkall"));
		ToolItem cancelitem = new ToolItem(toolbar, SWT.PUSH | SWT.BORDER);
		cancelitem.setText(Messages
				.getString("TLEditor.PersonsSelect.uncheckall"));
		searchtext1 = new Text(area, SWT.BORDER);
		GridData textcl = new GridData(GridData.FILL_HORIZONTAL);
		textcl.grabExcessHorizontalSpace = true;
		searchtext1.setLayoutData(textcl);
		searchtext1.setEditable(true);
		searchtext1.setText("");
		Button searchBtn1 = new Button(area, SWT.PUSH);
		searchBtn1.setText(Messages.getString("TLEditor.PersonsSelect.search"));

		psmtable = new Table(area, SWT.BORDER | SWT.V_SCROLL
				| SWT.FULL_SELECTION | SWT.CHECK);
		GridData tablelay = new GridData(GridData.FILL_BOTH);
		tablelay.grabExcessHorizontalSpace = true;
		tablelay.horizontalSpan = 3;
		psmtable.setLayoutData(tablelay);
		psmtable.setHeaderVisible(true);
		psmtable.setLinesVisible(true);
		TableColumn tablecolumn1 = new TableColumn(psmtable, SWT.NONE);
		tablecolumn1.setWidth(160);
		tablecolumn1.setText(Messages.getString("TLEditor.PersonsSelect.code"));
		TableColumn tablecolumn2 = new TableColumn(psmtable, SWT.NONE);
		tablecolumn2.setWidth(120);
		tablecolumn2.setText(Messages.getString("TLEditor.PersonsSelect.name"));

		Group containerrbot = new Group(sashright, SWT.FILL);
		containerrbot.setText(Messages
				.getString("TLEditor.PersonsSelect.slist"));
		containerrbot.setLayout(new GridLayout(1, false));
		ToolBar rtoolbar = new ToolBar(containerrbot, SWT.FLAT | SWT.WRAP
				| SWT.RIGHT);
		rtoolbar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		ToolItem removeitem = new ToolItem(rtoolbar, SWT.PUSH | SWT.BORDER);
		removeitem.setImage(StudioPlugin.getIcon("templete/reset.gif"));
		removeitem.setToolTipText(Messages
				.getString("TLEditor.PersonsSelect.delsel"));
		ToolItem rmvallitem = new ToolItem(rtoolbar, SWT.PUSH | SWT.BORDER);
		rmvallitem.setImage(StudioPlugin
				.getIcon("templete/reset_all.gif"));
		rmvallitem.setToolTipText(Messages
				.getString("TLEditor.PersonsSelect.delall"));

		GridData stablelay = new GridData(GridData.FILL_BOTH);
		stablelay.grabExcessHorizontalSpace = true;
		stablelay.grabExcessVerticalSpace = true;
		ScrolledComposite scrolledComposite = new ScrolledComposite(
				containerrbot, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		scrolledComposite.setLayoutData(stablelay);
		selecetedArea = new Composite(scrolledComposite, SWT.NONE);
		selecetedArea.setLayout(new RowLayout());
		scrolledComposite.setContent(selecetedArea);

		sashright.setWeights(new int[] { 5, 2 });
		sashForm.setWeights(new int[] { 2, 3 });

		// 全选
		selectallitem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = psmtable.getItems();
				for (int i = 0; i < items.length; i++) {
					TableItem item = items[i];
					item.setChecked(true);
					selectedItem.put((String) item.getData("id"),
							(Map) item.getData("map"));
				}
				refreshSelecetedArea();
			}
		});

		// 全取消
		cancelitem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = psmtable.getItems();
				for (int i = 0; i < items.length; i++) {
					TableItem item = items[i];
					item.setChecked(false);
					selectedItem.remove((String) item.getData("id"));
				}
				refreshSelecetedArea();
			}
		});

		psmtable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem selected = (TableItem) e.item;
				if (selected.getChecked()) {
					selectedItem.put((String) selected.getData("id"),
							(Map) selected.getData("map"));
				} else {
					selectedItem.remove((String) selected.getData("id"));
				}
				refreshSelecetedArea();
			}
		});

		searchtext1.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.keyCode == 13) {
					e.doit = false;
					doSearch();
				}
			}
		});

		// 搜索
		searchBtn1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				doSearch();
			}
		});

		// 删除单个选择
		removeitem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				for (String id : selecetedAreaCh.keySet()) {
					selectedItem.remove(id);
					Group g = selecetedAreaCh.get(id);
					selectionGroups.remove(g);
					g.dispose();
				}
			}
		});

		// 删除全部选择
		rmvallitem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				for (Group g : selectionGroups) {
					String id = (String) g.getData("id");
					selectedItem.remove(id);
					g.dispose();
				}
				selectionGroups.clear();
			}
		});

		containerOrg.addTreeSelectionListener(new OrgTreeSelectionAdapter() {
			public void widgetSelected(Tree Orgview) {
				loadPsmTableData(Orgview);
			}
		});

		containerOrg.initTreeData();

		insidePerSelect();// 初始化选中

		return sashForm;
	}

	private void loadPsmTableData(Tree Orgview) {
		psmtable.removeAll();
		TreeItem[] treeitems = Orgview.getSelection();
		List<Map> rli = new ArrayList<Map>();
		currentItem = treeitems[0];
		Map itemdata = (Map) currentItem.getData("map");
		String sfid = (String) itemdata.get("SFID");
		if (searchtext1.getText() == null || "".equals(searchtext1.getText())) {
			rli = OrgUtils.getOrgViewPersonList(sfid);
		} else {
			rli = OrgUtils.getOrgViewPersonList(sfid, searchtext1.getText());
		}
		buildPersonTable(rli);
	}

	private void buildPersonTable(List<Map> rli) {
		for (int i = 0; i < rli.size(); i++) {
			Map<String, String> m = rli.get(i);
			TableItem item = new TableItem(psmtable, SWT.NONE);
			item.setText(new String[] { m.get("SCODE"), m.get("SNAME") });
			item.setData("id", m.get("SID"));
			item.setData("map", m);
		}
	}

	private void doSearch() {
		psmtable.removeAll();
		String sfid = "/";
		if (currentItem != null) {
			Map itemdata = (Map) currentItem.getData("map");
			sfid = (String) itemdata.get("SFID");
		}
		List<Map> rli = OrgUtils.getOrgViewPersonList(sfid,
				searchtext1.getText());
		buildPersonTable(rli);
	}

	private void refreshSelecetedArea() {
		for (Group g : selectionGroups) {
			g.dispose();
		}
		selectionGroups.clear();
		for (String key : selectedItem.keySet()) {
			Map<String, String> m = selectedItem.get(key);
			Group group = new Group(selecetedArea, SWT.NONE);
			group.setLayout(new GridLayout(2, false));
			group.setData("id", m.get("SID"));
			group.setData("map", m);
			Button check = new Button(group, SWT.CHECK);
			Label label = new Label(group, SWT.NONE);
			label.setText(m.get("SNAME"));
			check.setData("id", m.get("SID"));
			check.setData("group", group);
			selectionGroups.add(group);
			check.addSelectionListener(new CheckButtonSelectionAdapter(check) {
				public void widgetSelected(SelectionEvent e, Button btn) {
					if (btn.getSelection()) {
						// System.err.println("选中");
						selecetedAreaCh.put((String) btn.getData("id"),
								(Group) btn.getData("group"));
					} else {
						// System.err.println("取消选中");
						selecetedAreaCh.remove((String) btn.getData("id"));
					}
				}
			});
		}
		selecetedArea.pack();
		// selecetedArea.layout();
		selecetedArea.setSize(selecetedArea.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
	}

	protected Point getInitialSize() {
		return new Point(700, 600);
	}

	protected int getShellStyle() {
		return super.getShellStyle() | SWT.RESIZE;
	}

	protected Button createButton(Composite parent, int buttonId,
			String buttonText, boolean defaultButton) {
		return null;
	}

	protected void initializeBounds() {
		super.createButton((Composite) getButtonBar(), IDialogConstants.OK_ID,
				Messages.getString("TLEditor.button.ensure"), false);
		super.createButton((Composite) getButtonBar(),
				IDialogConstants.CANCEL_ID,
				Messages.getString("TLEditor.button.cancel"), false);
		super.initializeBounds();
	}

	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			selectedPsm.clear();
			if (selectedItem.isEmpty()) {
				selectedPsm.put("id", "");
				selectedPsm.put("code", "");
				selectedPsm.put("name", "");
			} else {
				StringArray ids = new StringArray();
				StringArray codes = new StringArray();
				StringArray names = new StringArray();
				for (String id : selectedItem.keySet()) {
					Map m = selectedItem.get(id);
					ids.push(id);
					codes.push((String) m.get("SCODE"));
					names.push((String) m.get("SNAME"));
				}
				selectedPsm.put("id", ids.join(","));
				selectedPsm.put("code", codes.join(","));
				selectedPsm.put("name", names.join(","));
			}
		}
		super.buttonPressed(buttonId);
	}

	public Map<String, String> getSelectedPsms() {
		return selectedPsm;
	}

	public String getPerSelectIds() {
		return perSelectIds;
	}

	public void setPerSelectIds(String perSelectIds) {
		this.perSelectIds = perSelectIds;
	}

	private void insidePerSelect() {
		List<Map> rli = new ArrayList<Map>();
		try {
			StringArray ids = new StringArray(perSelectIds.split(","));
			rli = OrgUtils.getOrgViewPersonList(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Map m : rli) {
			String id = (String) m.get("SID");
			selectedItem.put(id, m);
		}
		refreshSelecetedArea();
	}

}
