/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.dialogs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBConfig;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.DriverSearcherThread;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.PluginClassLoader;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.preference.URLPreferencePage;
import zigen.plugin.db.ui.editors.event.TextSelectionListener;
import zigen.plugin.db.ui.jobs.TestConnectThread;
import zigen.plugin.db.ui.util.WidgetUtil;

public class WizardPage2 extends DefaultWizardPage {

	public static final String MSG = Messages.getString("WizardPage2.0"); //$NON-NLS-1$

	Combo driverCombox;

	Text urlText;

	Text userIdText;

	Text schemaText;

	Text passwordText;

	Button radio4;

	Button radio2;

	Button savePassword;

	Button testBtn;

	boolean searchDriverFlg = true;

	protected IPreferenceStore store;

	Composite container;

	Combo connectionModeCombox; // for oracle

	Combo connectionModeCombox2; // for mySQL

	Group group;

	public WizardPage2(ISelection selection) {
		super(Messages.getString("WizardPage2.1")); //$NON-NLS-1$
		setTitle(Messages.getString("WizardPage2.2")); //$NON-NLS-1$
		setPageComplete(true);
		store = DbPlugin.getDefault().getPreferenceStore();
	}

	public void createControl(Composite parent) {
		container = createDefaultComposite(parent);
		group = new Group(container, SWT.NONE);
		group.setText(Messages.getString("WizardPage2.3")); //$NON-NLS-1$
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setLayout(new GridLayout(2, false));

		addDriverSection(group);
		addTypeSection(group);
		addURLSection(group);
		addUseridSection(group);
		addPasswordSection(group);
		// addSavePassowrdSection(group);
		addSchemaSection(group);

		addTestConnectSection(group);

		setControl(container);
	}

	private void addDriverSection(Composite group) {
		Label urlLabel = new Label(group, SWT.NONE);
		urlLabel.setText(Messages.getString("WizardPage2.4")); //$NON-NLS-1$
		driverCombox = new Combo(group, SWT.READ_ONLY);
		driverCombox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		driverCombox.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				modified();

				Combo comb = (Combo) e.widget;
				if (comb.getText().indexOf("OracleDriver") >= 0) { //$NON-NLS-1$
					if (schemaText != null && userIdText != null) {
						schemaText.setText(userIdText.getText());
						schemaText.setEnabled(false);
					}
				} else if (schemaText != null && comb.getText().indexOf("mysql") >= 0) {
					schemaText.setEnabled(false);
				}

				setDefaultURLString(driverCombox.getText());
			}
		});

		if (getOldConfig() != null) {
			IDBConfig old = getOldConfig();
			driverCombox.add(old.getDriverName());
			driverCombox.select(0);
		}
	}

	private void addTypeSection(Composite group) {
		Label label2 = new Label(group, SWT.NULL);
		label2.setText(Messages.getString("WizardPage2.6")); //$NON-NLS-1$
		Composite left = new Composite(group, SWT.NONE);
		left.setLayout(new GridLayout(2, false));
		radio4 = new Button(left, SWT.RADIO);
		radio4.setText(Messages.getString("WizardPage2.7")); //$NON-NLS-1$
		radio2 = new Button(left, SWT.RADIO);
		radio2.setText(Messages.getString("WizardPage2.8")); //$NON-NLS-1$
		if (getOldConfig() != null) {
			if (getOldConfig().getJdbcType() == DBConfig.JDBC_DRIVER_TYPE_2) {
				radio2.setSelection(true);
				radio4.setSelection(false);
			} else {
				radio2.setSelection(false);
				radio4.setSelection(true);
			}
		} else {
			radio2.setSelection(false);
			radio4.setSelection(true);
		}
	}

	private void addURLSection(Composite group) {
		Label urlLabel = new Label(group, SWT.NONE);
		urlLabel.setText(Messages.getString("WizardPage2.9")); //$NON-NLS-1$
		urlText = new Text(group, SWT.SINGLE | SWT.BORDER);
		urlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		urlText.addFocusListener(new TextSelectionListener());
		urlText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				modified();

				if (driverCombox != null && driverCombox.getText().indexOf("mysql") >= 0 && schemaText != null) {
					String url = urlText.getText();
					String[] wk = url.split("/");
					if (wk.length >= 4) {
						String s = wk[3];
						int index = s.indexOf('?');
						if (index >= 0) {
							schemaText.setText(s.substring(0, index)); // ?
						} else {
							schemaText.setText(s);
						}
					} else {
						schemaText.setText("");
					}
					schemaText.setEnabled(false);

				}
			}
		});

		urlText.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
			}

		});

		if (getOldConfig() != null) {
			urlText.setText(getOldConfig().getUrl());
		} else {
			urlText.setText(DEFAULT_URL);
		}
	}

	private void addUseridSection(Composite group) {
		Label urlLabel = new Label(group, SWT.NONE);
		urlLabel.setText(Messages.getString("WizardPage2.10")); //$NON-NLS-1$
		userIdText = new Text(group, SWT.SINGLE | SWT.BORDER);
		userIdText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		userIdText.addFocusListener(new TextSelectionListener());
		userIdText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent evt) {
				modified();

				if (driverCombox != null && driverCombox.getText().indexOf("mysql") >= 0 && schemaText != null) {
				} else {
					Text text = (Text) evt.widget;
					if (schemaText != null)
						schemaText.setText(text.getText());
				}

			}
		});

		if (getOldConfig() != null) {
			userIdText.setText(getOldConfig().getUserId());
		} else {
			userIdText.setText(DEFAULT_USERID);
		}
	}

	private void addPasswordSection(Composite group) {
		Label urlLabel = new Label(group, SWT.NONE);
		urlLabel.setText(Messages.getString("WizardPage2.11")); //$NON-NLS-1$
		passwordText = new Text(group, SWT.SINGLE | SWT.BORDER);
		passwordText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		passwordText.addFocusListener(new TextSelectionListener());
		passwordText.setEchoChar('*');
		if (getOldConfig() != null) {
			passwordText.setText(getOldConfig().getPassword());
		} else {
			passwordText.setText(DEFAULT_PASS);
		}
	}

	private void addSchemaSection(Composite group) {
		Label urlLabel = new Label(group, SWT.NONE);
		urlLabel.setText(Messages.getString("WizardPage2.12")); //$NON-NLS-1$
		schemaText = new Text(group, SWT.SINGLE | SWT.BORDER);
		schemaText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		schemaText.addFocusListener(new TextSelectionListener());
		if (getOldConfig() != null) {
			schemaText.setText(getOldConfig().getSchema());
		} else {
			schemaText.setText(DEFAULT_SCHEMA);
		}
	}

	private void addSavePassowrdSection(Composite group) {
		Label urlLabel = new Label(group, SWT.NONE);

		savePassword = new Button(group, SWT.CHECK);
		savePassword.setText(Messages.getString("WizardPage2.13")); //$NON-NLS-1$
		if (getOldConfig() != null) {
			savePassword.setSelection(getOldConfig().isSavePassword());
		} else {
			savePassword.setSelection(DEFAULT_SAVEPASSWORD);
		}

	}

	private void addTestConnectSection(Composite group) {
		Label dummy = new Label(group, SWT.NULL);
		testBtn = WidgetUtil.createButton(group, SWT.PUSH, Messages.getString("WizardPage2.14"), BUTTON_WIDTH, new GridData()); //$NON-NLS-1$
		testBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				pressTestBtn();

			}
		});
	}

	private void pressTestBtn() {
		IDBConfig config = ((DBConfigWizard) getWizard()).createNewConfig();
		int timeout = store.getInt(PreferencePage.P_CONNECT_TIMEOUT);
		TestConnectThread t = new TestConnectThread(config, timeout);
		Thread th = new Thread(t);
		th.start();
		try {
			if (timeout > 0) {
				th.join(timeout * 1000);
			} else {
				th.join();
			}
		} catch (InterruptedException e) {
			DbPlugin.log(e);
		}
		if (t.isSuccess()) {
			super.setMessage(t.getMessage(), IMessageProvider.INFORMATION);
		} else {
			super.setMessage(t.getMessage(), IMessageProvider.ERROR);
		}
	}

	private void updateComboBox(List classpathList) {
		this.driverCombox.removeAll();
		// DriverSearch
		try {
			String[] classpaths = (String[]) classpathList.toArray(new String[0]);
			String[] drivers = searchDriver(getShell(), classpaths);

			if (drivers == null || drivers.length == 0) {
				updateStatus(Messages.getString("WizardPage2.15")); //$NON-NLS-1$
			} else {
				// updateComboBox
				for (int i = 0; i < drivers.length; i++) {
					String string = drivers[i];
					driverCombox.add(string);
					if (getOldConfig() == null) {
						if (i == 0) {
							driverCombox.select(i);
						}
						driverCombox.select(0);
					} else {
						if (string.equals(getOldConfig().getDriverName())) {
							driverCombox.select(i);
						}
					}
				}
			}
		} catch (InterruptedException e) {
			DbPlugin.getDefault().showErrorDialog(e);
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

	private void createClassPathList(List list, String folderName, File dir) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isDirectory()) {
					createClassPathList(list, folderName, file);
				} else if (file.isFile()) {
					String path = file.getPath();
					path = path.replace('\\', '/');
					path = path.replaceAll(folderName.replace('\\', '/') + "/", ""); //$NON-NLS-1$ //$NON-NLS-2$
					list.add(path);
				}
			}
		}
	}

	private File[] toFiles(String[] classpaths) {
		File[] files = new File[classpaths.length];
		for (int i = 0; i < classpaths.length; i++) {
			files[i] = new File(classpaths[i]);
		}
		return files;
	}

	private String[] searchDriver(Shell shell, String[] classpaths) throws InterruptedException {
		File[] files = toFiles(classpaths);
		List driverList = new ArrayList();
		for (int j = 0; j < files.length; j++) {
			File f = files[j];
			if (f.isDirectory()) {
				createClassPathList(driverList, f.getPath(), f);
			} else {
				driverList.add(f.getPath());
			}
		}
		String[] driverClassPath = (String[]) driverList.toArray(new String[0]);
		ClassLoader loader = PluginClassLoader.getClassLoader(classpaths, getClass().getClassLoader());

		DriverSearcherThread thread = new DriverSearcherThread(loader, driverClassPath);
		thread.run();
		List list = thread.getDriverNames();
		return (String[]) list.toArray(new String[0]);

	}

	private void modified() {
		super.setMessage(null);

		int selectIndex = 0;

		try {
			if (testBtn != null)
				testBtn.setEnabled(false);

			if (driverCombox != null) {
				selectIndex = driverCombox.getSelectionIndex();
				if (selectIndex >= 0 && urlText != null && userIdText != null) {
					String url = urlText.getText().trim();
					String userid = userIdText.getText().trim();
					String driver = driverCombox.getItem(selectIndex).trim();

					// if(!"".equals(url) && !"".equals(userid) &&
					// !"".equals(driver)){
					if (!"".equals(url) && !"".equals(driver)) { //$NON-NLS-1$ //$NON-NLS-2$
						if (url.matches(".*<.*|.*>.*")) { //$NON-NLS-1$
							super.updateStatus(Messages.getString("WizardPage2.21")); //$NON-NLS-1$
						} else {
							super.updateStatus(null);
							if (testBtn != null)
								testBtn.setEnabled(true);
						}

					} else {
						if (url.matches(".*<.*|.*>.*")) { //$NON-NLS-1$
							super.updateStatus(Messages.getString("WizardPage2.23")); //$NON-NLS-1$
						} else if ("".equals(url)) { //$NON-NLS-1$
							super.updateStatus(Messages.getString("WizardPage2.25")); //$NON-NLS-1$
							// }else if("".equals(userid)){
						} else if ("".equals(driver)) { //$NON-NLS-1$
							super.updateStatus(Messages.getString("WizardPage2.27")); //$NON-NLS-1$
						}

					}
				}

			}

		} catch (Exception e) {
			DbPlugin.log(e);
		}
	}

	private void setDefaultURLString(String driverString) {
		if (driverString != null && !"".equals(driverString)) { //$NON-NLS-1$
			String oldDriver = (getOldConfig() == null) ? "" : getOldConfig().getDriverName(); //$NON-NLS-1$
			String checkDriver = driverString.toLowerCase();

			if (!oldDriver.equals(driverString)) {
				List list = URLPreferencePage.getURLTemplates();
				for (int i = 0; i < list.size(); i++) {
					String[] wk = (String[]) list.get(i);

					if (checkDriver.equals(wk[0].toLowerCase())) {
						if (DbPlugin.getDefault().confirmDialog(Messages.getString("WizardPage2.30"))) { //$NON-NLS-1$
							urlText.setText(wk[1]);
						}
						return;
					}
				}
			}
		}

	}

	private void addOracleOptionSection(Composite group) {
		Label urlLabel = new Label(group, SWT.NONE);
		urlLabel.setText(Messages.getString("WizardPage2.31")); //$NON-NLS-1$
		connectionModeCombox = new Combo(group, SWT.READ_ONLY);
		connectionModeCombox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		connectionModeCombox.add("NORMAL"); //$NON-NLS-1$
		connectionModeCombox.add("SYSDBA"); //$NON-NLS-1$
		connectionModeCombox.add("SYSOPER"); //$NON-NLS-1$
		connectionModeCombox.select(0);

		if (getOldConfig() != null) {
			if (getOldConfig().isConnectAsSYSDBA())
				connectionModeCombox.select(1);
			if (getOldConfig().isConnectAsSYSOPER())
				connectionModeCombox.select(2);
		}

		int width = group.getSize().x;
		group.pack();
		int height = group.getSize().y;
		group.setSize(width, height);

	}


	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if (visible) {
			setDescription(MSG);
			if (searchDriverFlg) {
				DBConfigWizard wiz = (DBConfigWizard) getWizard();
				WizardPage1 page = (WizardPage1) wiz.getPreviousPage(this);
				updateComboBox(page.classpathList);

				String driver = driverCombox.getText();
				if (DBType.getType(driver) == DBType.DB_TYPE_ORACLE) {
					if (connectionModeCombox == null) {
						addOracleOptionSection(group);
						resize();
					}
				} else if (DBType.getType(driver) == DBType.DB_TYPE_MYSQL) {
				}
			}
		}

	}

}
