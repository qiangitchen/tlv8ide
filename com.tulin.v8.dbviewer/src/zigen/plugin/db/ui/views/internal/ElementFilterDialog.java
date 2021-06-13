/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.PluginSettingsManager;
import zigen.plugin.db.core.ColumnLayout;
import zigen.plugin.db.core.FolderInfo;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.views.TreeView;

public class ElementFilterDialog extends Dialog {

	PluginSettingsManager pluginMgr = DbPlugin.getDefault().getPluginSettingsManager();

	private TreeView treeView;

	FilterComposite filterComposite;

	FolderInfo[] settingFolders = null;

	FolderInfo[] filterFolders = null;

	CheckboxTableViewer fTableViewer;

	Map settingFolderMap = new TreeMap();

	private static final int BUTTON_ID_SELECTALL = -100;

	private static final int BUTTON_ID_REMOVEALL = -101;

	public ElementFilterDialog(Shell parent, TreeView treeView) {
		super(parent);
		super.setDefaultImage(ImageCacher.getInstance().getImage(DbPlugin.IMG_CODE_DB));
		setShellStyle(getShellStyle() | SWT.MAX | SWT.RESIZE);
		this.treeView = treeView;
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.getString("ElementFilterDialog.3")); //$NON-NLS-1$

	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, BUTTON_ID_SELECTALL, Messages.getString("ElementFilterDialog.4"), false); //$NON-NLS-1$
		createButton(parent, BUTTON_ID_REMOVEALL, Messages.getString("ElementFilterDialog.5"), false); //$NON-NLS-1$

		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

	}

	public boolean close() {
		return super.close();
	}

	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.CANCEL_ID) {
			setReturnCode(buttonId);
			close();
		} else if (buttonId == IDialogConstants.OK_ID) {
			pluginMgr.setValue(PluginSettingsManager.KEY_ELEM_FILTER_VISIBLE, new Boolean(filterComposite.visibleCheck.getSelection()));
			pluginMgr.setValue(PluginSettingsManager.KEY_ELEM_FILTER_PATTERN, filterComposite.filterText.getText());
			pluginMgr.setValue(PluginSettingsManager.KEY_ELEM_FILTER_REGULAREXP, new Boolean(filterComposite.regularExpressions.getSelection()));
			pluginMgr.setValue(PluginSettingsManager.KEY_ELEM_FILTER_CASESENSITIVE, new Boolean(filterComposite.caseSensitive.getSelection()));

			pluginMgr.setValue(PluginSettingsManager.KEY_ELEM_FILTER_FOLDER_LIST, filterFolders);

		} else if (buttonId == BUTTON_ID_SELECTALL) {
			for (int i = 0; i < fTableViewer.getTable().getItemCount(); i++) {
				FolderInfo info = (FolderInfo) fTableViewer.getElementAt(i);
				info.setChecked(true);
				fTableViewer.setChecked(info, true);
			}
		} else if (buttonId == BUTTON_ID_REMOVEALL) {
			for (int i = 0; i < fTableViewer.getTable().getItemCount(); i++) {
				FolderInfo info = (FolderInfo) fTableViewer.getElementAt(i);
				info.setChecked(false);
				fTableViewer.setChecked(info, false);
			}
		}

		super.buttonPressed(buttonId);
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		filterComposite = new FilterComposite(composite, Messages.getString("ElementFilterDialog.0")); //$NON-NLS-1$
		filterComposite.visibleCheck.setSelection(pluginMgr.getValueBoolean(PluginSettingsManager.KEY_ELEM_FILTER_VISIBLE));
		filterComposite.filterText.setText(pluginMgr.getValueString(PluginSettingsManager.KEY_ELEM_FILTER_PATTERN));
		filterComposite.regularExpressions.setSelection(pluginMgr.getValueBoolean(PluginSettingsManager.KEY_ELEM_FILTER_REGULAREXP));
		filterComposite.caseSensitive.setSelection(pluginMgr.getValueBoolean(PluginSettingsManager.KEY_ELEM_FILTER_CASESENSITIVE));
		filterComposite.visibleCheck.notifyListeners(SWT.Selection, null);
		filterComposite.regularExpressions.notifyListeners(SWT.Selection, null);

		settingFolders = (FolderInfo[]) pluginMgr.getValue(PluginSettingsManager.KEY_ELEM_FILTER_FOLDER_LIST);
		if (settingFolders != null) {
			for (int i = 0; i < settingFolders.length; i++) {
				FolderInfo info = settingFolders[i];
				settingFolderMap.put(info.getName(), info);
			}
		}
		createFolderArea(composite);

		return composite;

	}


	protected void createFolderArea(Composite composite) {
		Group group = new Group(composite, SWT.NONE);
		group.setText(Messages.getString("ElementFilterDialog.1")); //$NON-NLS-1$
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setLayout(new GridLayout(1, false));

		final Label labe2 = new Label(group, SWT.NONE);
		labe2.setText(Messages.getString("ElementFilterDialog.2")); //$NON-NLS-1$
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		labe2.setLayoutData(data);
		Composite tableComposite = new Composite(group, SWT.NONE);
		data = new GridData(GridData.FILL_BOTH);
		data.widthHint = 200;
		data.heightHint = 120;
		tableComposite.setLayoutData(data);
		ColumnLayout columnLayout = new ColumnLayout();
		tableComposite.setLayout(columnLayout);
		Table table = new Table(tableComposite, SWT.CHECK | SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setHeaderVisible(false);
		table.setLinesVisible(false);
		GC gc = new GC(getShell());
		gc.setFont(JFaceResources.getDialogFont());
		TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText(""); //$NON-NLS-1$
		int minWidth = computeMinimumColumnWidth(gc, ""); //$NON-NLS-1$
		columnLayout.addColumnData(new ColumnWeightData(1, minWidth, true));
		gc.dispose();
		fTableViewer = new CheckboxTableViewer(table);
		fTableViewer.setLabelProvider(new FolderLabelProvider());
		fTableViewer.setContentProvider(new FolderContentProvider());
		filterFolders = loadFolders();
		fTableViewer.setInput(filterFolders);

		if (filterFolders != null) {
			for (int i = 0; i < filterFolders.length; i++) {
				FolderInfo info = filterFolders[i];
				fTableViewer.setChecked(info, info.isChecked());
			}
		}

		fTableViewer.addCheckStateListener(new ICheckStateListener() {

			public void checkStateChanged(CheckStateChangedEvent event) {
				FolderInfo info = (FolderInfo) event.getElement();
				info.setChecked(event.getChecked());
			}
		});
	}


	protected int computeMinimumColumnWidth(GC gc, String string) {
		return gc.stringExtent(string).x + 10;
	}


	private FolderInfo[] loadFolders() {
		FolderInfo[] folders = null;
		boolean isSetting = !settingFolderMap.isEmpty();
		try {
			DataBase[] dbs = treeView.getContentProvider().getDataBases();
			List newList = new ArrayList();
			newList.add("TABLE"); //$NON-NLS-1$
			newList.add("VIEW"); //$NON-NLS-1$
			for (int i = 0; i < dbs.length; i++) {
				DataBase db = dbs[i];
				if (db.isConnected()) {
					String[] types = db.getTableType();
					for (int j = 0; j < types.length; j++) {
						String type = types[j];
						if (!newList.contains(type)) {
							newList.add(type);
						}
					}
				}
			}
			String[] newNames = (String[]) newList.toArray(new String[0]);
			if (!isSetting) {
				folders = new FolderInfo[newList.size()];
				for (int i = 0; i < newList.size(); i++) {
					folders[i] = new FolderInfo(newNames[i], false);
				}
			} else {
				folders = new FolderInfo[newNames.length];
				for (int i = 0; i < newNames.length; i++) {
					if (settingFolderMap.containsKey(newNames[i])) {
						folders[i] = (FolderInfo) settingFolderMap.get(newNames[i]);
					} else {
						folders[i] = new FolderInfo(newNames[i], false);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return folders;

	}

	class FolderLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			FolderInfo data = (FolderInfo) element;
			switch (columnIndex) {
			case 0:
				return data.getName();
			default:
				return ""; //$NON-NLS-1$
			}
		}
	}

	class FolderContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object input) {
			if (input instanceof FolderInfo[]) {
				return (FolderInfo[]) input;
			} else {
				return null;
			}
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

		public void dispose() {}
	}

	public FolderInfo[] getFilterFolders() {
		return filterFolders;
	}
}
