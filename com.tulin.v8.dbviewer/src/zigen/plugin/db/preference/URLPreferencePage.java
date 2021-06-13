/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.preference;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.util.WidgetUtil;

public class URLPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	public static final String P_URLDefine = "URLPreferencePage.URLDefine"; //$NON-NLS-1$

	public static final String SEP_ROWS = "|"; //$NON-NLS-1$

	public static final String SEP_COLS = ","; //$NON-NLS-1$

	protected TableContentProvider contentProvider = new TableContentProvider();

	protected int BUTTON_WIDTH = 100;

	private List properties = null;

	private TableViewer tableViewer;

	private Button envEditButton;

	private Button envRemoveButton;

	protected String[] tableHeader = {zigen.plugin.db.preference.Messages.getString("URLPreferencePage.0"), zigen.plugin.db.preference.Messages.getString("URLPreferencePage.1") //$NON-NLS-1$ //$NON-NLS-2$
	};

	protected ColumnLayoutData[] columnLayoutDatas = {new ColumnWeightData(1), new ColumnWeightData(1)};

	public void init(IWorkbench workbench) {}

	public URLPreferencePage() {
		super();

		String desc = zigen.plugin.db.preference.Messages.getString("URLPreferencePage.2") + zigen.plugin.db.preference.Messages.getString("URLPreferencePage.3"); //$NON-NLS-1$ //$NON-NLS-2$

		setDescription(desc);
		properties = getDefaultList();

		super.setPreferenceStore(DbPlugin.getDefault().getPreferenceStore());

	}

	public static List getURLTemplates() {
		URLPreferencePage page = new URLPreferencePage();
		return page.getDefaultList();
	}

	public List getDefaultList() {

		IPreferenceStore store = DbPlugin.getDefault().getPreferenceStore();
		String defaultString = store.getString(P_URLDefine);
		List list = new ArrayList();
		String[] wk = defaultString.split("[" + SEP_ROWS + "]"); //$NON-NLS-1$ //$NON-NLS-2$
		for (int i = 0; i < wk.length; i++) {
			list.add(wk[i].split("[" + SEP_COLS + "]")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return list;
	}

	private void addProperty(String driver, String url) {
		String[] property = new String[2];
		property[0] = driver;
		property[1] = url;
		properties.add(property);
	}

	private void updateProperty(int index, String driver, String url) {
		String[] property = new String[2];
		property[0] = driver;
		property[1] = url;
		properties.set(index, property);

	}

	private void removeProperty(int index) {
		properties.remove(index);
	}

	private void saveProperties() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < properties.size(); i++) {
			String[] wk = (String[]) properties.get(i);
			for (int j = 0; j < wk.length; j++) {
				if (j == 0) {
					sb.append(wk[j]);
				} else {
					sb.append(SEP_COLS + wk[j]);
				}
			}
			sb.append(SEP_ROWS);
		}
		// save
		getPreferenceStore().setValue(P_URLDefine, sb.toString());

	}

	public void createControl(Composite parent) {
		super.createControl(parent);
	}

	protected Control createContents(Composite parent) {
		noDefaultAndApplyButton();
		Font font = parent.getFont();
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		composite.setLayout(layout);
		composite.setFont(font);

		createTable(composite);
		createButtons(composite);
		return composite;
	}

	private void createTable(Composite parent) {
		Font font = parent.getFont();
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 1;
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 150;
		gridData.widthHint = 400;
		composite.setLayout(layout);
		composite.setLayoutData(gridData);
		composite.setFont(font);

		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		TableLayout tableLayout = new TableLayout();
		table.setLayout(tableLayout);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setFont(font);
		gridData = new GridData(GridData.FILL_BOTH);
		tableViewer.getControl().setLayoutData(gridData);

		for (int i = 0; i < tableHeader.length; i++) {
			tableLayout.addColumnData(columnLayoutDatas[i]);
			TableColumn tc = new TableColumn(table, SWT.NONE, i);
			tc.setResizable(columnLayoutDatas[i].resizable);
			tc.setText(tableHeader[i]);
		}

		tableViewer.setContentProvider(contentProvider);
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setInput(properties);

		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				tableSelectionChangedHandler(event);
			}
		});

		tableViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				if (!tableViewer.getSelection().isEmpty()) {
					editButtonPressedHandler();
				}
			}
		});

	}

	private void createButtons(Composite parent) {
		// Create button composite
		Composite buttonComposite = new Composite(parent, SWT.NONE);
		GridLayout glayout = new GridLayout();
		glayout.marginHeight = 0;
		glayout.marginWidth = 0;
		glayout.numColumns = 1;
		GridData gdata = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		buttonComposite.setLayout(glayout);
		buttonComposite.setLayoutData(gdata);
		buttonComposite.setFont(parent.getFont());

		// Create buttons

		final Button envAddButton = WidgetUtil.createButton(buttonComposite, SWT.PUSH,
				zigen.plugin.db.preference.Messages.getString("URLPreferencePage.4"), BUTTON_WIDTH, new GridData()); //$NON-NLS-1$
		envAddButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				addButtonPressedHandler();
			}
		});

		envEditButton = WidgetUtil.createButton(buttonComposite, SWT.PUSH, zigen.plugin.db.preference.Messages.getString("URLPreferencePage.5"), BUTTON_WIDTH, new GridData()); //$NON-NLS-1$
		envEditButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				editButtonPressedHandler();
			}
		});
		envEditButton.setEnabled(false);

		envRemoveButton = WidgetUtil.createButton(buttonComposite, SWT.PUSH, zigen.plugin.db.preference.Messages.getString("URLPreferencePage.6"), BUTTON_WIDTH, new GridData()); //$NON-NLS-1$
		envRemoveButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				removeButtonPressedHandler();
			}
		});
		envRemoveButton.setEnabled(false);
	}

	protected void tableSelectionChangedHandler(SelectionChangedEvent event) {
		int size = ((IStructuredSelection) event.getSelection()).size();
		envEditButton.setEnabled(size == 1);
		envRemoveButton.setEnabled(size > 0);
	}

	private void addButtonPressedHandler() {
		URLInputDialog dialog = new URLInputDialog(getShell());
		if (dialog.open() != Window.OK) {
			return;
		}
		addProperty(dialog.getStringValue(URLInputDialog.KEY_DRIVER), dialog.getStringValue(URLInputDialog.KEY_URL));
		tableViewer.setInput(properties);
		tableViewer.refresh();
	}

	private void editButtonPressedHandler() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
		String[] strs = (String[]) selection.getFirstElement();

		URLInputDialog dialog = new URLInputDialog(getShell());

		dialog.setStringValue(URLInputDialog.KEY_DRIVER, strs[0]);
		dialog.setStringValue(URLInputDialog.KEY_URL, strs[1]);
		if (dialog.open() != Window.OK) {
			return;
		}

		updateProperty(tableViewer.getTable().getSelectionIndex(), dialog.getStringValue(URLInputDialog.KEY_DRIVER), dialog.getStringValue(URLInputDialog.KEY_URL));
		tableViewer.setInput(properties);
		tableViewer.refresh();
	}

	private void removeButtonPressedHandler() {
		removeProperty(tableViewer.getTable().getSelectionIndex());
		tableViewer.setInput(properties);
		tableViewer.refresh();
	}

	protected void performDefaults() {
		super.performDefaults();
	}

	public boolean performOk() {
		saveProperties();
		return super.performOk();
	}

	private class TableContentProvider implements IStructuredContentProvider {

		private List contents = null;

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				List list = (List) inputElement;
				return list.toArray();
			}
			return null;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (newInput instanceof List) {
				contents = (List) newInput;
			} else {
				contents = null;
			}
		}

		public void dispose() {
		}

	}

	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof String[]) {
				String[] list = (String[]) element;
				if (columnIndex < list.length) {
					return list[columnIndex];
				} else {
					return ""; //$NON-NLS-1$
				}
			}
			return null;
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return null;
		}
	}

}
