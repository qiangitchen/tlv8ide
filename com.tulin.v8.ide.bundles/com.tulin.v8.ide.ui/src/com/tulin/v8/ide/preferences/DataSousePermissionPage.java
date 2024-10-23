package com.tulin.v8.ide.preferences;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.tulin.v8.core.Configuration;
import com.tulin.v8.core.Sys;
import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.utils.DocServerData;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBConfigManager;


/*
 * 数据源配置
 */
public class DataSousePermissionPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	Table systable = null;
	Table optable = null;
	Table doctable = null;

	protected Control createContents(Composite parent) {
		final Composite contain = new Composite(parent, SWT.NONE);
		contain.setLayout(new FillLayout());// SWT.VERTICAL

		Composite composite = new Composite(contain, SWT.FILL);
		composite.setLayout(new GridLayout());

		Group sysgroup = new Group(composite, SWT.NONE);
		sysgroup.setText(Messages.getString("preferencePages.DataSousePermission.1"));
		sysgroup.setLayout(new GridLayout(2, false));
		sysgroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		systable = new Table(sysgroup, SWT.BORDER | SWT.FULL_SELECTION);
		GridData dataSys = new GridData(GridData.FILL_HORIZONTAL);
		dataSys.heightHint = 80;
		dataSys.grabExcessHorizontalSpace = true;
		systable.setLayoutData(dataSys);
		systable.setHeaderVisible(true);
		systable.setLinesVisible(true);
		TableColumn syscolumn1 = new TableColumn(systable, SWT.NONE);
		syscolumn1.setWidth(80);
		syscolumn1.setText(Messages.getString("preferencePages.DataSousePermission.2"));
		TableColumn syscolumn2 = new TableColumn(systable, SWT.NONE);
		syscolumn2.setWidth(160);
		syscolumn2.setText(Messages.getString("preferencePages.DataSousePermission.3"));
		TableColumn syscolumn3 = new TableColumn(systable, SWT.NONE);
		syscolumn3.setWidth(120);
		syscolumn3.setText(Messages.getString("preferencePages.DataSousePermission.4"));
		GridData sysdatabtn = new GridData();
		sysdatabtn.widthHint = 80;
		sysdatabtn.verticalAlignment = 1;
		final Button syseditbtn = new Button(sysgroup, NONE);
		syseditbtn.setText(Messages.getString("preferencePages.DataSousePermission.5"));
		syseditbtn.setEnabled(false);
		syseditbtn.setLayoutData(sysdatabtn);

		Group bsgroup = new Group(composite, SWT.NONE);
		bsgroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		bsgroup.setText(Messages.getString("preferencePages.DataSousePermission.6"));
		bsgroup.setLayout(new GridLayout(2, false));
		optable = new Table(bsgroup, SWT.BORDER | SWT.FULL_SELECTION);
		GridData dataT = new GridData(GridData.FILL_BOTH);
		dataT.heightHint = 120;
		dataT.verticalSpan = 4;
		dataT.grabExcessHorizontalSpace = true;
		optable.setLayoutData(dataT);
		optable.setHeaderVisible(true);
		optable.setLinesVisible(true);
		TableColumn tablecolumn1 = new TableColumn(optable, SWT.NONE);
		tablecolumn1.setWidth(80);
		tablecolumn1.setText(Messages.getString("preferencePages.DataSousePermission.7"));
		TableColumn tablecolumn2 = new TableColumn(optable, SWT.NONE);
		tablecolumn2.setWidth(160);
		tablecolumn2.setText(Messages.getString("preferencePages.DataSousePermission.8"));
		TableColumn tablecolumn3 = new TableColumn(optable, SWT.NONE);
		tablecolumn3.setWidth(120);
		tablecolumn3.setText(Messages.getString("preferencePages.DataSousePermission.9"));
		Button addbtn = new Button(bsgroup, NONE);
		addbtn.setText(Messages.getString("preferencePages.DataSousePermission.10"));
		GridData databtn = new GridData();
		databtn.widthHint = 80;
		addbtn.setLayoutData(databtn);
		final Button editbtn = new Button(bsgroup, NONE);
		editbtn.setText(Messages.getString("preferencePages.DataSousePermission.11"));
		editbtn.setEnabled(false);
		editbtn.setLayoutData(databtn);
		final Button rembtn = new Button(bsgroup, NONE);
		rembtn.setText(Messages.getString("preferencePages.DataSousePermission.12"));
		rembtn.setEnabled(false);
		rembtn.setLayoutData(databtn);

		Group docgroup = new Group(composite, SWT.NONE);
		docgroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		docgroup.setText(Messages.getString("preferencePages.DataSousePermission.13"));
		docgroup.setLayout(new GridLayout(2, false));
		doctable = new Table(docgroup, SWT.BORDER | SWT.FULL_SELECTION);
		GridData dataDoc = new GridData(GridData.FILL_HORIZONTAL);
		dataDoc.heightHint = 60;
		dataDoc.grabExcessHorizontalSpace = true;
		doctable.setLayoutData(dataDoc);
		doctable.setHeaderVisible(true);
		doctable.setLinesVisible(true);
		TableColumn doccolumn1 = new TableColumn(doctable, SWT.NONE);
		doccolumn1.setWidth(80);
		doccolumn1.setText(Messages.getString("preferencePages.DataSousePermission.14"));
		TableColumn doccolumn2 = new TableColumn(doctable, SWT.NONE);
		doccolumn2.setWidth(160);
		doccolumn2.setText(Messages.getString("preferencePages.DataSousePermission.15"));
		TableColumn doccolumn3 = new TableColumn(doctable, SWT.NONE);
		doccolumn3.setWidth(120);
		doccolumn3.setText(Messages.getString("preferencePages.DataSousePermission.16"));
		GridData docdatabtn = new GridData();
		docdatabtn.widthHint = 80;
		docdatabtn.verticalAlignment = 1;
		final Button doceditbtn = new Button(docgroup, NONE);
		doceditbtn.setText(Messages.getString("preferencePages.DataSousePermission.11"));
		doceditbtn.setEnabled(false);
		doceditbtn.setLayoutData(docdatabtn);

		loadpermision();
		loadDocServerPer();

		systable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				syseditbtn.setEnabled(true);
				editbtn.setEnabled(false);
				doceditbtn.setEnabled(false);
				rembtn.setEnabled(false);
			}
		});
		optable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				syseditbtn.setEnabled(false);
				editbtn.setEnabled(true);
				doceditbtn.setEnabled(false);
				rembtn.setEnabled(true);
			}
		});
		doctable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				syseditbtn.setEnabled(false);
				editbtn.setEnabled(false);
				doceditbtn.setEnabled(true);
				rembtn.setEnabled(false);
			}
		});

		syseditbtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setPermision(systable, systable.getSelection()[0].getText(0));
			}
		});
		addbtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setPermision(optable, "");
			}
		});
		editbtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setPermision(optable, optable.getSelection()[0].getText(0));
			}
		});
		rembtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean result = MessageDialog.openConfirm(getShell(),
						Messages.getString("preferencePages.DataSousePermission.acde1"),
						Messages.getString("preferencePages.DataSousePermission.acde2"));
				if (result) {
					try {
						DataSousePermission.deleteSqlMapFile(optable.getSelection()[0].getText(0));
						DataSousePermission.DeleteBIZServerDatasourse(optable.getSelection()[0].getText(0));
						loadpermision();
					} catch (Exception es) {
						MessageDialog.openError(getShell(),
								Messages.getString("preferencePages.DataSousePermission.12"), es.toString());
						es.printStackTrace();
					}
				}
			}
		});
		doceditbtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setPermision(doctable, doctable.getSelection()[0].getText(0));
			}
		});

		return contain;
	}

	private void setPermision(final Table table, String key) {
		new DataSoursePermisionDialog(this, getShell(), table, key, systable, optable, doctable).open();
	}

	/*
	 * 加载配置信息-来自Sql-Map
	 */
	public void loadpermision() {
		if (systable != null) {
			systable.removeAll();
		}
		if (optable != null) {
			optable.removeAll();
		}
		try {
			Map<String, Map<String, String>> rm = Configuration.getConfig();
			Set<String> k = rm.keySet();
			Iterator<String> it = k.iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				Map<String, String> m = rm.get(key);
				String[] pers = new String[3];
				pers[0] = key;
				pers[1] = m.get("driver");
				pers[2] = m.get("url");
				if ("system".equals(key) || "infolink".equals(key)) {
					TableItem tableitem = new TableItem(systable, SWT.NONE);
					tableitem.setText(pers);
				} else {
					TableItem tableitem = new TableItem(optable, SWT.NONE);
					tableitem.setText(pers);
				}
			}
		} catch (Exception e) {
			Sys.printErrMsg(Messages.getString("preferencePages.DataSousePermission.linktest3"));
		}
	}

	/*
	 * 加载文档服务数据源-来自DocServer
	 */
	public void loadDocServerPer() {
		if (doctable != null) {
			doctable.removeAll();
		}
		try {
			Map<String, String> m = DocServerData.getConfig();
			String[] pers = new String[3];
			pers[0] = "doc";
			pers[1] = m.get("driver");
			pers[2] = m.get("url");
			TableItem tableitem = new TableItem(doctable, SWT.NONE);
			tableitem.setText(pers);
		} catch (Exception e) {
			Sys.printErrMsg(Messages.getString("preferencePages.DataSousePermission.linktest4"));
		}
	}

	public DataSousePermissionPage() {
		super(GRID);
		setPreferenceStore(StudioPlugin.getDefault().getPreferenceStore());
	}

	public void createFieldEditors() {

	}

	public void init(IWorkbench workbench) {

	}

	protected void performApply() {
		try {
			DbPlugin.getDefault().setConfigischange(true);
			DBConfigManager.refreshConfig();
		} catch (Exception localInvocationTargetException) {
			localInvocationTargetException.printStackTrace();
		}
	}
}
