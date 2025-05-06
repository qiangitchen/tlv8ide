package com.tulin.v8.flowdesigner.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.core.utils.OrgUtils;

@SuppressWarnings({ "rawtypes" })
public class OrgTreeComposite extends Composite {
	private Text searchtext;
	private Tree Orgview;
	private Button searchbtn;

	private boolean async;
	private String rootFilter = "sParent is null";
	private String filter = "SORGKINDID != 'psm'";
	private String orderby = "sSequence asc";

	private Map<String, TreeItem> orgItem = new HashMap<String, TreeItem>();

	private OrgTreeSelectionAdapter orgtreeselectionadapter;

	public OrgTreeComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		GridData laydata = new GridData(GridData.FILL_HORIZONTAL);
		laydata.grabExcessHorizontalSpace = true;
		searchtext = new Text(this, SWT.BORDER);
		searchtext.setLayoutData(laydata);
		searchbtn = new Button(this, SWT.PUSH);
		searchbtn.setImage(TuLinPlugin.getIcon("search.gif"));
		searchbtn.setToolTipText("快速定位");
		Orgview = new Tree(this, SWT.FILL | SWT.BORDER | SWT.V_SCROLL);
		GridData tlaydata = new GridData(GridData.FILL_BOTH);
		tlaydata.grabExcessHorizontalSpace = true;
		tlaydata.grabExcessVerticalSpace = true;
		tlaydata.horizontalSpan = 2;
		Orgview.setLayoutData(tlaydata);

		// 搜索(树)
		searchtext.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.keyCode == 13) {
					e.doit = false;
					doTreeSearch();
				}
			}
		});

		// 搜索(树)
		searchbtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				doTreeSearch();
			}
		});

		Orgview.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (orgtreeselectionadapter != null) {
					orgtreeselectionadapter.widgetSelected(Orgview);
				}
			}
		});
	}

	private void doTreeSearch() {
		List<Map<String, String>> slist = OrgUtils.getOrgViewSearchList(searchtext.getText(), filter);
		if (slist.size() > 0) {
			String sfid = slist.get(0).get("SFID").toString();
			String[] sids = sfid.split("/");
			select(sids[sids.length - 1]);
		} else {
			MessageDialog.openInformation(getShell(), "提示", "没有找到[" + searchtext.getText() + "]相关内容!");
		}
	}

	public void initTreeData() {
		Orgview.removeAll();
		orgItem.clear();
		List<Map<String, String>> roots = OrgUtils.getOrgTreeRootData(rootFilter, filter, orderby);
		for (int i = 0; i < roots.size(); i++) {
			Map m = roots.get(i);
			String id = (String) m.get("SID");
			String name = (String) m.get("SNAME");
			String kind = (String) m.get("SORGKINDID");
			TreeItem item = new TreeItem(Orgview, SWT.NONE);
			item.setText(name);
			item.setImage(TuLinPlugin.getIcon(createIcon(kind)));
			item.setData("id", id);
			item.setData("map", m);
			orgItem.put(id, item);
			chilTreeData(item, id);
		}
	}

	private void chilTreeData(TreeItem pitem, String parent) {
		List<Map<String, String>> datas = OrgUtils.getOrgTreeChiledData(parent, rootFilter, filter, orderby);
		for (int i = 0; i < datas.size(); i++) {
			Map m = datas.get(i);
			String id = (String) m.get("SID");
			String name = (String) m.get("SNAME");
			String kind = (String) m.get("SORGKINDID");
			TreeItem item = new TreeItem(pitem, SWT.NONE);
			item.setText(name);
			item.setImage(TuLinPlugin.getIcon(createIcon(kind)));
			item.setData("id", id);
			item.setData("map", m);
			orgItem.put(id, item);
			chilTreeData(item, id);
		}
	}

	public String createIcon(String kind) {
		if (("ogn".equals(kind)) || ("org".equals(kind)))
			return "org/org.gif";
		if ("dept".equals(kind))
			return "org/dept.gif";
		if ("pos".equals(kind))
			return "org/pos.gif";
		if ("psm".equals(kind)) {
			return "org/person.gif";
		}
		return "folder.gif";
	}

	public void select(String id) {
		TreeItem item = orgItem.get(id);
		if (item != null) {
			Orgview.select(item);
		}
		if (orgtreeselectionadapter != null) {
			orgtreeselectionadapter.widgetSelected(Orgview);
		}
	}

	public String getRootFilter() {
		return rootFilter;
	}

	public void setRootFilter(String rootFilter) {
		this.rootFilter = rootFilter;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	public OrgTreeSelectionAdapter getOrgtreeselectionadapter() {
		return orgtreeselectionadapter;
	}

	public void addTreeSelectionListener(OrgTreeSelectionAdapter orgtreeselectionadapter) {
		this.orgtreeselectionadapter = orgtreeselectionadapter;
	}
}
