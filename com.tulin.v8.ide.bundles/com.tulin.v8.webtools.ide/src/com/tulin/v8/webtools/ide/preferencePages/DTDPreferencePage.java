package com.tulin.v8.webtools.ide.preferencePages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.tulin.v8.webtools.ide.TableViewerSupport;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.dialog.AbstractValidationDialog;
import com.tulin.v8.webtools.ide.xml.ElementSchemaMapping;

/**
 * The preference page to configure DTD settings.
 */
public class DTDPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private Table table;
	private Button buttonAdd;
	private Button buttonEdit;
	private Button buttonRemove;
	private Button buttonCache;

	private CustomSchemaTableViewer customSchemaViewer;
	private List<ElementSchemaMapping> customSchemaModel = new ArrayList<ElementSchemaMapping>();

	public DTDPreferencePage() {
		super(WebToolsPlugin.getResourceString("PreferencePage.DTD"));
		setPreferenceStore(WebToolsPlugin.getDefault().getPreferenceStore());
		setDescription(WebToolsPlugin.getResourceString("PreferencePage.LocalDTD"));
	}

	protected Control createContents(Composite parent) {

		TabFolder folder = new TabFolder(parent, SWT.NULL);
		folder.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite composite = new Composite(folder, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		TabItem tab1 = new TabItem(folder, SWT.NULL);
		tab1.setControl(composite);
		tab1.setText(WebToolsPlugin.getResourceString("PreferencePage.tab.LocalSchema"));

		// create table
		table = new Table(composite, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				TableItem[] items = table.getSelection();
				boolean enable = false;
				if (items.length > 0) {
					String path = items[0].getText(1);
					if (!path.equals("[Default]")) {
						enable = true;
					}
				}
				buttonEdit.setEnabled(enable);
				buttonRemove.setEnabled(enable);
			}
		});
		TableColumn col1 = new TableColumn(table, SWT.LEFT);
		col1.setText(WebToolsPlugin.getResourceString("PreferencePage.Uri"));
		col1.setWidth(100);
		TableColumn col2 = new TableColumn(table, SWT.LEFT);
		col2.setText(WebToolsPlugin.getResourceString("PreferencePage.LocalPath"));
		col2.setWidth(150);

		// create buttons
		Composite buttons = new Composite(composite, SWT.NONE);
		buttons.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttons.setLayout(layout);
		buttonAdd = new Button(buttons, SWT.PUSH);
		buttonAdd.setText(WebToolsPlugin.getResourceString("Button.Add"));
		buttonAdd.setLayoutData(createButtonGridData());
		buttonAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				DTDDialog dialog = new DTDDialog(getShell());
				if (dialog.open() == Dialog.OK) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(new String[] { dialog.getName(), dialog.getPath() });
				}
			}
		});
		buttonEdit = new Button(buttons, SWT.PUSH);
		buttonEdit.setText(WebToolsPlugin.getResourceString("Button.Edit"));
		buttonEdit.setLayoutData(createButtonGridData());
		buttonEdit.setEnabled(false);
		buttonEdit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				TableItem[] items = table.getSelection();
				if (items.length > 0) {
					String uri = items[0].getText(0);
					String path = items[0].getText(1);
					DTDDialog dialog = new DTDDialog(getShell(), uri, path);
					if (dialog.open() == Dialog.OK) {
						items[0].setText(new String[] { dialog.getName(), dialog.getPath() });
					}
				}
			}
		});
		buttonRemove = new Button(buttons, SWT.PUSH);
		buttonRemove.setText(WebToolsPlugin.getResourceString("Button.Remove"));
		buttonRemove.setLayoutData(createButtonGridData());
		buttonRemove.setEnabled(false);
		buttonRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				int[] indices = table.getSelectionIndices();
				table.remove(indices);
			}
		});

		// DTD cache
		buttonCache = new Button(composite, SWT.CHECK);
		buttonCache.setText(WebToolsPlugin.getResourceString("PreferencePage.Dialog.Cache"));
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		buttonCache.setLayoutData(gd);

		// Custom Schema Mappings
		customSchemaModel = ElementSchemaMapping.loadFromPreference();
		customSchemaViewer = new CustomSchemaTableViewer(customSchemaModel, folder);
		TabItem tab2 = new TabItem(folder, SWT.NULL);
		tab2.setText(WebToolsPlugin.getResourceString("PreferencePage.tab.ElementMapping"));
		tab2.setControl(customSchemaViewer.getControl());

		// set initial values
		performDefaults();

		return composite;
	}

	public boolean performOk() {
		IPreferenceStore store = getPreferenceStore();
		TableItem[] items = table.getItems();
		StringBuffer uri = new StringBuffer();
		StringBuffer path = new StringBuffer();
		for (int i = 0; i < items.length; i++) {
			if (!items[i].getText(1).equals("[Default]")) {
				uri.append(items[i].getText(0)).append("\n");
				path.append(items[i].getText(1)).append("\n");
			}
		}
		store.setValue(WebToolsPlugin.PREF_DTD_URI, uri.toString());
		store.setValue(WebToolsPlugin.PREF_DTD_PATH, path.toString());
		store.setValue(WebToolsPlugin.PREF_DTD_CACHE, buttonCache.getSelection());

		ElementSchemaMapping.saveToPreference(customSchemaModel);

		return true;
	}

	protected void performDefaults() {
		IPreferenceStore store = getPreferenceStore();
		table.removeAll();

		Map<String, String> innerDTD = WebToolsPlugin.getInnerDTD();
		Iterator<String> ite = innerDTD.keySet().iterator();
		while (ite.hasNext()) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { ite.next(), "[Default]" });
		}

		String[] uri = store.getString(WebToolsPlugin.PREF_DTD_URI).split("\n");
		String[] path = store.getString(WebToolsPlugin.PREF_DTD_PATH).split("\n");

		for (int i = 0; i < uri.length; i++) {
			if (!uri[i].trim().equals("") && !path[i].trim().equals("")) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(new String[] { uri[i].trim(), path[i].trim() });
			}
		}

		buttonCache.setSelection(store.getBoolean(WebToolsPlugin.PREF_DTD_CACHE));
	}

	private class CustomSchemaTableViewer extends TableViewerSupport<ElementSchemaMapping> {

		public CustomSchemaTableViewer(List<ElementSchemaMapping> model, Composite parent) {
			super(model, parent);
		}

		protected ITableLabelProvider createLabelProvider() {
			return new ITableLabelProvider() {
				public Image getColumnImage(Object element, int columnIndex) {
					return null;
				}

				public String getColumnText(Object element, int columnIndex) {
					if (columnIndex == 0) {
						return ((ElementSchemaMapping) element).getRootElement();
					} else if (columnIndex == 1) {
						return ((ElementSchemaMapping) element).getFilePath();
					}
					return null;
				}

				public void addListener(ILabelProviderListener listener) {
				}

				public void dispose() {
				}

				public boolean isLabelProperty(Object element, String property) {
					return false;
				}

				public void removeListener(ILabelProviderListener listener) {
				}
			};
		}

		protected ElementSchemaMapping doAdd() {
			CustomSchemaDialog dialog = new CustomSchemaDialog(getShell());
			if (dialog.open() == Dialog.OK) {
				ElementSchemaMapping mapping = new ElementSchemaMapping(dialog.getName(), dialog.getPath());
				return mapping;
			}
			return null;
		}

		protected void doEdit(ElementSchemaMapping mapping) {
			CustomSchemaDialog dialog = new CustomSchemaDialog(getShell(), mapping.getRootElement(),
					mapping.getFilePath());
			if (dialog.open() == Dialog.OK) {
				mapping.setRootElement(dialog.getName());
				mapping.setFilePath(dialog.getPath());
			}
		}

		protected void initTableViewer(TableViewer viewer) {
			Table table = viewer.getTable();
			TableColumn col1 = new TableColumn(table, SWT.LEFT);
			col1.setText(WebToolsPlugin.getResourceString("PreferencePage.RootElement"));
			col1.setWidth(100);
			TableColumn col2 = new TableColumn(table, SWT.LEFT);
			col2.setText(WebToolsPlugin.getResourceString("PreferencePage.LocalPath"));
			col2.setWidth(150);
		}
	}

	/**
	 * Create LayoutData for &quot;add&quot;, &quot;edit&quot; and
	 * &quot;remove&quot; buttons.
	 * 
	 * @return GridData
	 */
	private static GridData createButtonGridData() {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		return gd;
	}

	public void init(IWorkbench workbench) {
	}

	/**
	 * The dialog to add or edit custom schema mappings.
	 */
	private class CustomSchemaDialog extends DTDDialog {

		public CustomSchemaDialog(Shell parentShell) {
			super(parentShell);
		}

		public CustomSchemaDialog(Shell parentShell, String name, String path) {
			super(parentShell, name, path);
		}

		protected String getNameLabel() {
			return WebToolsPlugin.getResourceString("PreferencePage.Dialog.RootElement");
		}
	}

	/**
	 * The dialog to add or edit local schema mappings.
	 */
	private class DTDDialog extends AbstractValidationDialog {

		private Text textName;
		private Text textPath;
		private String name = "";
		private String path = "";

		public DTDDialog(Shell parentShell) {
			super(parentShell);
		}

		public DTDDialog(Shell parentShell, String name, String path) {
			super(parentShell);
			this.name = name;
			this.path = path;
		}

		protected Point getInitialSize() {
			Point size = super.getInitialSize();
			size.x = 400;
			return size;
		}

		protected String getNameLabel() {
			return WebToolsPlugin.getResourceString("PreferencePage.Dialog.Uri");
		}

		protected Control createDialogArea(Composite parent) {
			getShell().setText(WebToolsPlugin.getResourceString("PreferencePage.DTD"));

			Composite container = new Composite(parent, SWT.NULL);
			container.setLayoutData(new GridData(GridData.FILL_BOTH));
			container.setLayout(new GridLayout(3, false));

			Label label = new Label(container, SWT.NULL);
			label.setText(getNameLabel());

			textName = new Text(container, SWT.BORDER);
			textName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			textName.setText(name);

			// fill GridLayout
			label = new Label(container, SWT.NONE);

			label = new Label(container, SWT.NULL);
			label.setText(WebToolsPlugin.getResourceString("PreferencePage.Dialog.LocalPath"));

			textPath = new Text(container, SWT.BORDER);
			textPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			textPath.setText(path);

			Button button = new Button(container, SWT.PUSH);
			button.setText("...");
			button.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					FileDialog openDialog = new FileDialog(getShell(), SWT.OPEN);
					openDialog.setFileName(textPath.getText());
					String openFile = openDialog.open();
					if (openFile != null) {
						textPath.setText(openFile);
					}
				}
			});

			add(textName);
			add(textPath);

			return container;
		}

		protected void validate() {
			if (textName.getText().equals("")) {
				setErrorMessage(WebToolsPlugin.createMessage(WebToolsPlugin.getResourceString("Error.Required"),
						new String[] { getNameLabel() }));
				return;
			} else if (textPath.getText().equals("")) {
				setErrorMessage(WebToolsPlugin.createMessage(WebToolsPlugin.getResourceString("Error.Required"),
						new String[] { WebToolsPlugin.getResourceString("PreferencePage.LocalPath") }));
				return;
			}
			setErrorMessage(null);
		}

		protected void okPressed() {
			name = textName.getText();
			path = textPath.getText();
			super.okPressed();
		}

		public String getName() {
			return name;
		}

		public String getPath() {
			return path;
		}
	}
}
