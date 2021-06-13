/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.PluginSettingsManager;
import zigen.plugin.db.core.ByteArrayUtil;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.InputStreamUtil;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TransactionForTableEditor;
import zigen.plugin.db.ui.editors.internal.FillCellEditorUtil;

public class LobViewDialog extends Dialog {

	private ImageCacher ic = ImageCacher.getInstance();

	protected PluginSettingsManager pluginMgr = DbPlugin.getDefault().getPluginSettingsManager();

	private static final String DEFAULT_CHARSET = Messages.getString("LobViewDialog.0"); //$NON-NLS-1$

	private static final String ORIGINAL = Messages.getString("LobViewDialog.1"); //$NON-NLS-1$

	private CTabFolder tabFolder;

	private Text orignalText;

	private Object originalData;

	private Text newText;

	private Object newData;

	private int colIndex;

	private TableElement tableElement;

	private TableColumn column;

	private ToolItem updateItem;

	private ToolItem deleteItem;

	private ToolItem impItem;

	private ToolItem expItem;

	private ToolItem charsetItem;

	private int dataType;

	public LobViewDialog(Shell parent, TableElement tableElement, int colIndex) {
		super(parent);
		setShellStyle(getShellStyle() | SWT.MAX | SWT.RESIZE);
		this.tableElement = tableElement;
		this.colIndex = colIndex;
		this.column = tableElement.getColumns()[colIndex - 1];
		this.dataType = column.getDataType();

	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.getString("LobViewDialog.2")); //$NON-NLS-1$
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CLOSE_ID, IDialogConstants.CLOSE_LABEL, true);

	}

	private void doExport() {
		FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
		dialog.setFilterExtensions(new String[] {"*.*"}); //$NON-NLS-1$
		dialog.setFilterNames(new String[] {Messages.getString("LobViewDialog.4")}); //$NON-NLS-1$
		String fileName = dialog.open();
		if (fileName != null) {
			File file = new File(fileName);
			if (file.exists()) {
				if (!confirmOverwrite(getShell(), file.getName())) {
					return;
				}
			}
			FillCellEditorUtil.saveAsFile(tableElement, colIndex, column.getDataType(), file);
		}
	}

	private boolean isClob() {
		switch (dataType) {
		case Types.CLOB:
			return true;
		default:
			return false;
		}
	}

	private void doImport() {
		FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[] {"*.*"}); //$NON-NLS-1$
		dialog.setFilterNames(new String[] {Messages.getString("LobViewDialog.6")}); //$NON-NLS-1$
		String fileName = dialog.open();
		if (fileName != null) {
			File file = new File(fileName);
			if (file.canRead()) {
				try {
					// if (isClob()) {
					// newData = InputStreamUtil.toString(new FileReader(file));
					// addTextPages((String)newData, "*" + file.getName());
					// } else {
					// newData = InputStreamUtil.toByteArray(new
					// FileInputStream(file));
					// addImagePages((byte[])newData, "*" + file.getName());
					// }

					newData = InputStreamUtil.toByteArray(new FileInputStream(file));
					addImagePages((byte[]) newData, "*" + file.getName()); //$NON-NLS-1$

					updateItem.setEnabled(true);
					// charsetItem.setEnabled(true);

				} catch (IOException e) {
					DbPlugin.getDefault().showErrorDialog(e);
				}

			} else {
				DbPlugin.getDefault().showWarningMessage(Messages.getString("LobViewDialog.8")); //$NON-NLS-1$
			}

		}
		// }
	}

	private void doUpdate() {
		if (newData != null) {

			try {
				IDBConfig config = tableElement.getTable().getDbConfig();
				TransactionForTableEditor trans = TransactionForTableEditor.getInstance(config);
				int rowAffected = 0;
				if (isClob()) {
					String str = ByteArrayUtil.toString((byte[]) newData, charsetItem.getText());
					rowAffected = FillCellEditorUtil.update(trans.getConnection(), tableElement, colIndex, str);

					originalData = str;
					charsetItem.setEnabled(false);

				} else {
					rowAffected = FillCellEditorUtil.update(trans.getConnection(), tableElement, colIndex, newData);
					originalData = newData;
					charsetItem.setEnabled(true);
				}

				trans.commit();
				orignalText = newText;

				CTabItem[] list = tabFolder.getItems();
				if (list.length == 1) {
					list[0].setText(ORIGINAL);

				} else if (list.length == 2) {
					list[0].dispose();
					list[1].setText(ORIGINAL);
				}

				updateItem.setEnabled(false);
				deleteItem.setEnabled(true);
				expItem.setEnabled(true);

			} catch (Exception e) {

				DbPlugin.getDefault().showErrorDialog(e);
			}

		} else {
			DbPlugin.getDefault().showWarningMessage(Messages.getString("LobViewDialog.9")); //$NON-NLS-1$
		}
	}

	private void doDelete() {
		if (DbPlugin.getDefault().confirmDialog(Messages.getString("LobViewDialog.10"))) { //$NON-NLS-1$
			try {
				IDBConfig config = tableElement.getTable().getDbConfig();
				TransactionForTableEditor trans = TransactionForTableEditor.getInstance(config);
				int rowAffected = 0;

				rowAffected = FillCellEditorUtil.delete(trans.getConnection(), tableElement, colIndex);

				trans.commit();
				addTextPages(null, ORIGINAL, false);
				CTabItem[] list = tabFolder.getItems();
				for (int i = 0; i < list.length; i++) {
					CTabItem control = list[i];
					control.dispose();
				}
				updateItem.setEnabled(false);
				deleteItem.setEnabled(false);
				expItem.setEnabled(false);
				charsetItem.setEnabled(false);

			} catch (Exception e) {
				DbPlugin.getDefault().showErrorDialog(e);
			}

		}
	}

	private boolean confirmOverwrite(Shell shell, String fileName) {
		MessageBox msg = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
		msg.setMessage(Messages.getString("LobViewDialog.13") + fileName + Messages.getString("LobViewDialog.14")); //$NON-NLS-1$ //$NON-NLS-2$
		msg.setText(Messages.getString("LobViewDialog.15")); //$NON-NLS-1$
		int res = msg.open();
		if (res == SWT.YES) {
			return true;
		} else {
			return false;
		}
	}

	private void createToolItem(ToolBar toolBar) {
		updateItem = new ToolItem(toolBar, SWT.FLAT);
		updateItem.setText(Messages.getString("LobViewDialog.16")); //$NON-NLS-1$
		updateItem.setToolTipText(Messages.getString("LobViewDialog.17")); //$NON-NLS-1$
		updateItem.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				doUpdate();
			}
		});

		deleteItem = new ToolItem(toolBar, SWT.FLAT);
		deleteItem.setText(Messages.getString("LobViewDialog.18")); //$NON-NLS-1$
		deleteItem.setToolTipText(Messages.getString("LobViewDialog.19")); //$NON-NLS-1$
		deleteItem.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				doDelete();
			}
		});

		new ToolItem(toolBar, SWT.SEPARATOR);

		impItem = new ToolItem(toolBar, SWT.FLAT);
		impItem.setText(Messages.getString("LobViewDialog.20")); //$NON-NLS-1$
		impItem.setToolTipText(Messages.getString("LobViewDialog.21")); //$NON-NLS-1$
		impItem.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				doImport();
			}
		});

		expItem = new ToolItem(toolBar, SWT.FLAT);
		expItem.setText(Messages.getString("LobViewDialog.22")); //$NON-NLS-1$
		expItem.setToolTipText(Messages.getString("LobViewDialog.23")); //$NON-NLS-1$
		expItem.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				doExport();
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);

		String defaultCharset = DEFAULT_CHARSET;

		Object obj = pluginMgr.getValue(PluginSettingsManager.KEY_LOB_CHARSET);
		if (obj != null && obj instanceof String) {
			defaultCharset = (String) obj;
		}

		charsetItem = createToolItem(toolBar, SWT.DROP_DOWN, DEFAULT_CHARSET, null, null, "This is dropdown one"); //$NON-NLS-1$
		DropdownSelectionListener listenerOne = new DropdownSelectionListener(charsetItem);

		listenerOne.add(DEFAULT_CHARSET);
		listenerOne.add("SJIS"); //$NON-NLS-1$
		listenerOne.add("UTF-8"); //$NON-NLS-1$
		listenerOne.add("EUC_JP"); //$NON-NLS-1$
		listenerOne.add("ISO2022JP"); //$NON-NLS-1$
		charsetItem.addSelectionListener(listenerOne);
	}

	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(1, false);
		layout.verticalSpacing = 10;
		layout.horizontalSpacing = 5;
		layout.marginWidth = 5;
		layout.marginHeight = 5;
		composite.setLayout(layout);

		ToolBar toolBar = new ToolBar(composite, SWT.FLAT);
		toolBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createToolItem(toolBar);
		createTabFolder(composite);

		originalData = FillCellEditorUtil.getObject(tableElement, colIndex, dataType);

		if (originalData == null) {
			updateItem.setEnabled(false);
			deleteItem.setEnabled(false);
			expItem.setEnabled(false);
			charsetItem.setEnabled(false);
		} else if (originalData instanceof String) {
			addTextPages((String) originalData, ORIGINAL, false);

		} else if (originalData instanceof byte[]) {
			addImagePages((byte[]) originalData, ORIGINAL);
			updateItem.setEnabled(false);
			deleteItem.setEnabled(true);
			expItem.setEnabled(true);
		}

		return composite;
	}

	private ToolItem createToolItem(ToolBar parent, int type, String text, Image image, Image hotImage, String toolTipText) {
		ToolItem item = new ToolItem(parent, type);
		item.setText(text);
		item.setImage(image);
		item.setHotImage(hotImage);
		item.setToolTipText(toolTipText);
		return item;
	}

	private void createTabFolder(Composite composite) {
		GridData gridData = new GridData(GridData.FILL_BOTH);
		tabFolder = new CTabFolder(composite, SWT.NONE);
		tabFolder.setLayoutData(gridData);
		tabFolder.setTabHeight(20);
		tabFolder.setSelectionBackground(new Color[] {composite.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND),
				composite.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT)}, new int[] {100}, true);
		//
		tabFolder.setSelectionForeground(composite.getDisplay().getSystemColor(SWT.COLOR_TITLE_FOREGROUND));
		tabFolder.setSimple(true);
		tabFolder.addSelectionListener(new SelectionAdapter() {

			private void setCharsetItemEnable(String label) {
				if (ORIGINAL.equals(label)) {
					if (originalData instanceof String) {
						charsetItem.setEnabled(false);
					} else {
						charsetItem.setEnabled(true);
					}
				} else {
					charsetItem.setEnabled(true);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				CTabItem item = (CTabItem) e.item;
				setCharsetItemEnable(item.getText());
			}

			public void widgetSelected(SelectionEvent e) {
				CTabItem item = (CTabItem) e.item;
				setCharsetItemEnable(item.getText());
			}

		});
	}

	private void addImagePages(byte[] bytes, String label) {
		Image image;
		try {
			image = new Image(null, new ByteArrayInputStream(bytes));
			CTabItem tabItem = null;
			if (ORIGINAL.equals(label)) {
				tabItem = new CTabItem(tabFolder, SWT.NONE);
			} else {
				tabItem = new CTabItem(tabFolder, SWT.CLOSE);
			}
			tabItem.setText(label);
			tabItem.addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent e) {
					try {
						if (e.getSource() instanceof CTabItem) {
							CTabItem item = (CTabItem) e.getSource();
							if (item.getText().startsWith("*")) { //$NON-NLS-1$
								if (updateItem != null)
									updateItem.setEnabled(false);
							}
						}
					} catch (Exception e1) {
						;
					}
				}
			});
			tabItem.setToolTipText(label);
			ScrolledComposite sc = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
			sc.setLayoutData(new GridData(GridData.FILL_BOTH));
			Composite c = new Composite(sc, SWT.NO);
			GridLayout layout = new GridLayout(1, false);
			layout.verticalSpacing = 0;
			layout.horizontalSpacing = 0;
			layout.marginWidth = 0;
			layout.marginHeight = 0;
			c.setLayout(layout);
			c.setLayoutData(new GridData(GridData.FILL_BOTH));
			Label ctl = new Label(c, SWT.NO);
			GridData gd = new GridData();
			gd.heightHint = image.getImageData().height;
			gd.widthHint = image.getImageData().width;
			ctl.setLayoutData(gd);
			ctl.setImage(image);
			sc.setContent(c);
			sc.setMinSize(ctl.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			sc.setExpandHorizontal(true);
			sc.setExpandVertical(true);
			tabItem.setControl(sc);
			tabFolder.setSelection(tabItem);
			removeTabItem();

			charsetItem.setEnabled(false);
		} catch (Exception e) {
			String str = ByteArrayUtil.toString(bytes, charsetItem.getText());
			addTextPages(str, label, true);

		}

	}

	private void addTextPages(String str, String label, boolean changeCharset) {

		charsetItem.setEnabled(changeCharset);

		if (str == null)
			str = ""; //$NON-NLS-1$

		CTabItem tabItem = null;
		if (ORIGINAL.equals(label)) {
			tabItem = new CTabItem(tabFolder, SWT.NONE);
		} else {
			tabItem = new CTabItem(tabFolder, SWT.CLOSE);
		}
		tabItem.setText(label);
		tabItem.setToolTipText(label);
		tabItem.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				try {
					if (e.getSource() instanceof CTabItem) {
						CTabItem item = (CTabItem) e.getSource();
						if (item.getText().startsWith("*")) { //$NON-NLS-1$
							if (updateItem != null)
								updateItem.setEnabled(false);
						}
					}
				} catch (Exception e1) {
					;
				}
			}
		});
		ScrolledComposite sc = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		sc.setLayoutData(new GridData(GridData.FILL_BOTH));
		Composite c = new Composite(sc, SWT.NO);
		GridLayout layout = new GridLayout(1, false);
		layout.verticalSpacing = 5;
		layout.horizontalSpacing = 5;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		c.setLayout(layout);
		c.setLayoutData(new GridData(GridData.FILL_BOTH));

		Text text = new Text(c, SWT.NO | SWT.MULTI);
		GridData gd = new GridData(GridData.FILL_BOTH);
		text.setLayoutData(gd);
		text.setText(str);
		text.setEditable(false);
		text.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		text.setFont(DbPlugin.getDefaultFont());

		if (ORIGINAL.equals(label)) {
			orignalText = text;
		} else {
			newText = text;
		}
		sc.setContent(c);
		sc.setMinSize(text.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		tabItem.setControl(sc);
		tabFolder.setSelection(tabItem);
		removeTabItem();

	}

	private void removeTabItem() {
		CTabItem[] list = tabFolder.getItems();
		for (int i = 0; i < list.length - 1; i++) {

			CTabItem control = list[i];

			if (!control.getText().equals(ORIGINAL)) {
				control.dispose();
			}

		}
	}

	protected Point getInitialSize() {
		return new Point(640, 480);
	}

	private boolean selectedOriginalTabItem() {
		CTabItem item = tabFolder.getSelection();
		if (item != null) {
			return item.getText().equals(ORIGINAL);
		} else {
			return false;
		}
	}


	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.CLOSE_ID) {
			setReturnCode(buttonId);
			close();
			//
			// } else if (buttonId == BUTTON_ID_EXP) {
			// doExport();
			//
			// } else if (buttonId == BUTTON_ID_IMP) {
			// doImport();
			//
			// } else if (buttonId == BUTTON_ID_DEL) {
			// doDelete();
		}

		super.buttonPressed(buttonId);
	}

	public boolean close() {
		try {
			if (tabFolder != null) {
				CTabItem[] list = tabFolder.getItems();
				if (list != null && list.length > 0) {
					if (list[list.length - 1].getText().startsWith("*")) { //$NON-NLS-1$
						if (!DbPlugin.getDefault().confirmDialog(Messages.getString("LobViewDialog.12"))) { //$NON-NLS-1$
							return false;
						}
					}
				}
			}
		} catch (Exception e) {
			DbPlugin.log(e);
		}
		return super.close();

	}

	class DropdownSelectionListener extends SelectionAdapter {

		private ToolItem dropdown;

		private Menu menu;

		public DropdownSelectionListener(ToolItem dropdown) {
			this.dropdown = dropdown;
			menu = new Menu(dropdown.getParent().getShell());

		}

		private void changeText(String charset) {
			pluginMgr.setValue(PluginSettingsManager.KEY_LOB_CHARSET, charset);

			if (selectedOriginalTabItem()) {
				if (originalData != null && orignalText != null) {

					String text = orignalText.getText();
					if (originalData instanceof byte[]) {
						text = ByteArrayUtil.toString((byte[]) originalData, charset);

					}
					orignalText.setText(text);

				}
			} else {
				if (newData != null && newText != null) {
					String text = newText.getText();
					if (newData instanceof byte[]) {
						text = ByteArrayUtil.toString((byte[]) newData, charset);
					}
					newText.setText(text);
				}
			}
		}

		public void add(String item) {
			MenuItem menuItem = new MenuItem(menu, SWT.NONE);
			menuItem.setText(item);
			menuItem.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent event) {
					MenuItem selected = (MenuItem) event.widget;
					dropdown.setText(selected.getText());
					String charset = selected.getText();
					changeText(charset);
				}
			});
		}

		public void widgetSelected(SelectionEvent event) {
			if (event.detail == SWT.ARROW) {
				ToolItem item = (ToolItem) event.widget;
				Rectangle rect = item.getBounds();
				Point pt = item.getParent().toDisplay(new Point(rect.x, rect.y));
				menu.setLocation(pt.x, pt.y + rect.height);
				menu.setVisible(true);

			} else {
				// They pushed the button; take appropriate action
				String charset = dropdown.getText();
				changeText(charset);

			}
		}
	}

}
