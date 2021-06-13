package com.tulin.v8.ide.preferences;

import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.json.JSONObject;

import com.tulin.v8.core.Configuration;
import com.tulin.v8.core.DataSourceUtils;
import com.tulin.v8.ide.Sys;
import com.tulin.v8.ide.utils.DocServerData;

public class DataSoursePermisionDialog extends Dialog {
	public static int TEST_ID = 9;
	DataSousePermissionPage page;
	Table systable;
	Table optable;
	Table doctable;
	Table table;
	String key;
	private Combo dbkeyCombo;
	private Text sDbUrlv;
	private Text sUserv;
	private Text sPasswv;
	private Text sNamev;

	public DataSoursePermisionDialog(DataSousePermissionPage page, Shell parentShell, Table table, String key,
			Table systable, Table optable, Table doctable) {
		super(parentShell);
		this.page = page;
		this.table = table;
		this.key = key;
		this.systable = systable;
		this.optable = optable;
		this.doctable = doctable;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.getString("preferencePages.DataSousePermission.link01"));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());

		Composite composite = new Composite(container, SWT.FILL);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);

		Composite copPer = new Composite(composite, SWT.NONE);
		copPer.setLayout(new GridLayout(2, false));
		copPer.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label sTypel = new Label(copPer, SWT.NONE);
		sTypel.setText(Messages.getString("preferencePages.DataSousePermission.link02"));
		GridData dataS = new GridData(SWT.FILL, SWT.CENTER, false, false);
		dataS.widthHint = 100;
		sTypel.setLayoutData(dataS);
		dbkeyCombo = new Combo(copPer, SWT.DROP_DOWN);
		String[] dbkeys = { Messages.getString("preferencePages.DataSousePermission.acde5"),
				"oracle.jdbc.driver.OracleDriver", "net.sourceforge.jtds.jdbc.Driver", "com.mysql.jdbc.Driver",
				"org.postgresql.Driver", "dm.jdbc.driver.DmDriver", "com.kingbase8.Driver" };
		dbkeyCombo.setItems(dbkeys);
		String dbDrivtype = (table.getSelection().length > 0) ? (table.getSelection()[0]).getText(1)
				: Messages.getString("preferencePages.DataSousePermission.acde5");
		dbkeyCombo.setText(dbDrivtype);
		GridData dataV = new GridData(GridData.FILL_HORIZONTAL);
		dataV.grabExcessHorizontalSpace = true;
		dataV.widthHint = 380;
		dbkeyCombo.setLayoutData(dataV);

		Label sNamel = new Label(copPer, SWT.NONE);
		sNamel.setText(Messages.getString("preferencePages.DataSousePermission.link03"));
		sNamel.setLayoutData(dataS);
		sNamev = new Text(copPer, SWT.LEFT | SWT.VIRTUAL | SWT.BORDER);
		sNamev.setText(key);
		sNamev.setLayoutData(dataV);
		if (table == systable || table == doctable) {
			sNamev.setEditable(false);
		}

		Label sDbTypel = new Label(copPer, SWT.NONE);
		sDbTypel.setText(Messages.getString("preferencePages.DataSousePermission.link04"));
		sDbTypel.setLayoutData(dataS);
		final Text sDbTypev = new Text(copPer, SWT.LEFT | SWT.VIRTUAL | SWT.BORDER);
		String dbtype = (table.getSelection().length > 0) ? (table.getSelection()[0]).getText(1) : "";
		dbtype = (dbtype != null && !"".equals(dbtype)) ? dbtype.substring(0, dbtype.indexOf(".")) : "";
		if ("net".equals(dbtype)) {
			dbtype = "sqlserver";
		}
		if ("com".equals(dbtype)) {
			dbtype = "mysql";
		}
		sDbTypev.setText(dbtype);
		sDbTypev.setLayoutData(dataV);
		sDbTypev.setEditable(false);

		Label sDbUrll = new Label(copPer, SWT.NONE);
		sDbUrll.setText(Messages.getString("preferencePages.DataSousePermission.link1"));
		sDbUrll.setLayoutData(dataS);
		sDbUrlv = new Text(copPer, SWT.LEFT | SWT.VIRTUAL | SWT.BORDER);
		sDbUrlv.setLayoutData(dataV);
		sDbUrlv.setEditable(true);

		Label sUserl = new Label(copPer, SWT.NONE);
		sUserl.setText(Messages.getString("preferencePages.DataSousePermission.link2"));
		sUserl.setLayoutData(dataS);
		sUserv = new Text(copPer, SWT.LEFT | SWT.VIRTUAL | SWT.BORDER);
		sUserv.setLayoutData(dataV);
		sUserv.setEditable(true);

		Label sPasswl = new Label(copPer, SWT.NONE);
		sPasswl.setText(Messages.getString("preferencePages.DataSousePermission.link3"));
		sPasswl.setLayoutData(dataS);
		sPasswv = new Text(copPer, SWT.LEFT | SWT.VIRTUAL | SWT.PASSWORD | SWT.BORDER);
		sPasswv.setLayoutData(dataV);
		sPasswv.setEditable(true);

		if ("doc".equals(key)) {
			try {
				Map<String, String> m = DocServerData.getConfig();
				sDbUrlv.setText(m.get("url"));
				sUserv.setText(m.get("username"));
				sPasswv.setText(m.get("password"));
			} catch (Exception e) {
			}
		} else if (!"".equals(key) && key != null) {
			Map<String, Map<String, String>> rm = Configuration.getConfig();
			Map<String, String> m = rm.get(key);
			sDbUrlv.setText(m.get("url"));
			sUserv.setText(m.get("username"));
			sPasswv.setText(m.get("password"));
		}

		// 数据源类型选择事件
		dbkeyCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					String sType = dbkeyCombo.getText();
					String dbType = "";
					if (!Messages.getString("preferencePages.DataSousePermission.2").equals(sType)) {
						dbType = sType.substring(0, sType.indexOf("."));
						if (!dbType.equals(sDbTypev.getText())) {
							sDbTypev.setText(dbType);
							if (sType.equals("oracle.jdbc.driver.OracleDriver")) {
								sDbUrlv.setText("jdbc:oracle:thin:@127.0.0.1:1521:orcl");
							}
							if (sType.equals("net.sourceforge.jtds.jdbc.Driver")) {
								sDbUrlv.setText("jdbc:jtds:sqlserver://127.0.0.1:1433/TLoa");
								sDbTypev.setText("sqlserver");
							}
							if (sType.equals("com.mysql.jdbc.Driver")) {
								sDbUrlv.setText(
										"jdbc:mysql://127.0.0.1:3306/TLoa?characterEncoding=utf8&amp;useUnicode=true&amp;useSSL=false");
								sDbTypev.setText("mysql");
							}
							if (sType.equals("org.postgresql.Driver")) {
								sDbUrlv.setText("jdbc:postgresql://127.0.0.1:5432/postgres");
								sDbTypev.setText("postgresql");
							}
							if (sType.equals("dm.jdbc.driver.DmDriver")) {
								sDbUrlv.setText("jdbc:dm://127.0.0.1:5236");
								sDbTypev.setText("dm");
							}
							if (sType.equals("com.kingbase8.Driver")) {
								sDbUrlv.setText("jdbc:kingbase8://127.0.0.1:54321/TEST");
								sDbTypev.setText("kingbasees");
							}
						}
						// testBtn.setEnabled(true);
						// OKBtn.setEnabled(true);
					} else {
						// testBtn.setEnabled(false);
						// OKBtn.setEnabled(false);
					}
				} catch (Exception er) {
				}
			}
		});

		return composite;
	}

	// protected int getShellStyle() {
	// return super.getShellStyle() | SWT.RESIZE;
	// }
	//
	// @Override
	// protected Point getInitialSize() {
	// return new Point(550, 400);
	// }

	/**
	 * 改写父类创建按钮的方法，使其失效
	 */
	protected Button createButton(Composite parent, int buttonId, String buttonText, boolean defaultButton) {
		return null;
	}

	/**
	 * 改写父类的initializeBounds方法，并利用父类的createButton方法建立按钮
	 */
	protected void initializeBounds() {
		super.createButton((Composite) getButtonBar(), TEST_ID,
				Messages.getString("preferencePages.DataSousePermission.link4"), false);
		super.createButton((Composite) getButtonBar(), IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
		super.createButton((Composite) getButtonBar(), IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL,
				false);
		super.initializeBounds();
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == TEST_ID) {
			try {
				JSONObject res = DataSourceUtils.testLink(dbkeyCombo.getText(), sDbUrlv.getText(), sUserv.getText(),
						sPasswv.getText());
				String msg = res.getString("message");
				if (!res.getBoolean("flag")) {
					Sys.printErrMsg(msg);
					if (msg.length() > 230) {
						msg = msg.substring(0, 230);
					}
					MessageDialog.openError(getShell(),
							Messages.getString("preferencePages.DataSousePermission.linktest1"), msg);
				} else {
					MessageDialog.openInformation(getShell(),
							Messages.getString("preferencePages.DataSousePermission.linktest1"), msg);
				}
			} catch (Exception e1) {
				Sys.printErrMsg(e1.toString());
				MessageDialog.openInformation(getShell(),
						Messages.getString("preferencePages.DataSousePermission.linktest1"),
						Messages.getString("preferencePages.DataSousePermission.linktest2"));
			}
			return;
		}
		if (buttonId == IDialogConstants.OK_ID) {
			boolean sur = false;
			try {
				sur = DataSousePermission.WritePer(sNamev.getText(), dbkeyCombo.getText(), sDbUrlv.getText(),
						sUserv.getText(), sPasswv.getText());
			} catch (Exception err) {
				String ermsg = err.toString();
				if (ermsg.indexOf(":") > 0) {
					ermsg = ermsg.substring(ermsg.indexOf(":") + 1);
				}
				MessageDialog.openError(getShell(), Messages.getString("preferencePages.DataSousePermission.acde3"),
						ermsg);
				return;
			}
			if (sur) {
				if (table == doctable) {
					page.loadDocServerPer();
				} else {
					page.loadpermision();
				}
			} else {
				page.loadpermision();
			}
		}
		super.buttonPressed(buttonId);
	}

}
