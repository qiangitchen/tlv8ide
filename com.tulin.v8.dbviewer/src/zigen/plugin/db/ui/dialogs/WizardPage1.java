/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.dialogs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.core.ColumnLayout;
import zigen.plugin.db.ui.editors.event.TextSelectionListener;
import zigen.plugin.db.ui.util.WidgetUtil;

public class WizardPage1 extends DefaultWizardPage {

	ImageCacher ic = ImageCacher.getInstance();

	protected class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object element, int columnIndex) {
			String result = ""; //$NON-NLS-1$

			switch (columnIndex) {
			case 0:
				result = (String) element;

				break;
			default:
				break;
			}
			return result;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			if (element instanceof String) {
				File file = new File((String) element);
				if (file.exists()) {
					if (file.isDirectory()) {
						return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
					} else {
						return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
					}
				} else {
					return ic.getImage(DbPlugin.IMG_CODE_WARNING);
				}
			}

			return null;
		}

		public Image getImage(Object obj) {
			return null;
		}
	}

	protected class TableContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				return ((List) inputElement).toArray();
			}
			return null;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (newInput != null) {
				modified();
			}
		}

		public void dispose() {}

	}

	public static final String MSG = Messages.getString("WizardPage1.1"); //$NON-NLS-1$

	List classpathList = null;

	Text nameText;

	TableViewer tableViewer;

	Table table;

	Button addBtn;

	Button addBtn2;

	Button removeBtn;

	public WizardPage1(ISelection selection) {
		super(Messages.getString("WizardPage1.2")); //$NON-NLS-1$
		setTitle(Messages.getString("WizardPage1.3")); //$NON-NLS-1$
		setPageComplete(false);
	}

	private void modified() {
		String str = nameText.getText().trim();
		if (!"".equals(str) && classpathList.size() > 0) { //$NON-NLS-1$
			super.updateStatus(null);
		} else {
			if ("".equals(str)) { //$NON-NLS-1$
				super.updateStatus(Messages.getString("WizardPage1.6")); //$NON-NLS-1$
			} else if (classpathList.size() == 0) {
				super.updateStatus(Messages.getString("WizardPage1.7")); //$NON-NLS-1$
			}
		}
	}

	public void createControl(Composite parent) {

		Composite container = createDefaultComposite(parent);
		Group group = new Group(container, SWT.NONE);
		group.setText(Messages.getString("WizardPage1.8")); //$NON-NLS-1$
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setLayout(new GridLayout(1, false));

		addDBNameSection(group);

		Group group2 = new Group(container, SWT.NONE);
		group2.setText(Messages.getString("WizardPage1.9")); //$NON-NLS-1$
		// group2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group2.setLayoutData(new GridData(GridData.FILL_BOTH));
		group2.setLayout(new GridLayout(1, false));
		addDriverSection(group2);

		setControl(container);
	}

	private void addDBNameSection(Composite group) {
		nameText = new Text(group, SWT.SINGLE | SWT.BORDER);
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameText.addFocusListener(new TextSelectionListener());
		if (getOldConfig() != null) {
			nameText.setText(getOldConfig().getDbName());
		} else {
			nameText.setText(DEFAULT_NAME);
		}
		nameText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				modified();
			}
		});

	}

	private void addDriverSection(Composite group) {

		Composite tableComposite = new Composite(group, SWT.NONE);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.widthHint = 200;
		data.heightHint = 120;
		// data.heightHint = convertHeightInCharsToPixels(10);
		tableComposite.setLayoutData(data);
		ColumnLayout columnLayout = new ColumnLayout();
		tableComposite.setLayout(columnLayout);

		table = new Table(tableComposite, SWT.BORDER | SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		tableViewer = new TableViewer(table);
		// tableViewer = new TableViewer(tableComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);

		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(false);
		tableViewer.setContentProvider(new TableContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				ISelection select = event.getSelection();
				if (select != null) {
					removeBtn.setEnabled(true);
				} else {
					removeBtn.setEnabled(false);
				}
			}

		});

		// GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		// gd.heightHint = 50;
		tableViewer.getControl().setLayoutData(gd);

		// setHeaderColumn(table, new String[] {""}); //$NON-NLS-1$

		GC gc = new GC(getShell());
		gc.setFont(JFaceResources.getDialogFont());

		TableColumn column1 = new TableColumn(table, SWT.NONE);
		String DISPLAY_TXT = "PATH"; //$NON-NLS-1$
		column1.setText(DISPLAY_TXT);
		int minWidth = computeMinimumColumnWidth(gc, DISPLAY_TXT);
		columnLayout.addColumnData(new ColumnWeightData(1, minWidth, true));
		gc.dispose();

		// initializeDialogUnits(table);

		if (getOldConfig() != null) {
			classpathList = new ArrayList();
			String[] list = getOldConfig().getClassPaths();
			for (int i = 0; i < list.length; i++) {
				classpathList.add(list[i]);
			}
		} else {
			classpathList = new ArrayList();
		}
		tableViewer.setInput(classpathList);



		Composite buttonComp = new Composite(group, SWT.NONE);
		buttonComp.setLayout(new GridLayout(4, false));
		buttonComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		addBtn = WidgetUtil.createButton(buttonComp, SWT.PUSH, Messages.getString("WizardPage1.11"), BUTTON_WIDTH, new GridData()); //$NON-NLS-1$
		addBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				FileDialog openDialog = new FileDialog(table.getShell(), SWT.OPEN);
				String openFile = openDialog.open();
				if (openFile != null) {
					if (!classpathList.contains(openFile)) {
						classpathList.add(openFile);
						tableViewer.setInput(classpathList);
						columnsPack(table);
					}

				}
			}
		});
		addBtn2 = WidgetUtil.createButton(buttonComp, SWT.PUSH, Messages.getString("WizardPage1.12"), BUTTON_WIDTH, new GridData()); //$NON-NLS-1$
		addBtn2.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog openDialog = new DirectoryDialog(table.getShell(), SWT.OPEN);
				String openFile = openDialog.open();
				if (openFile != null) {
					if (!classpathList.contains(openFile)) {
						classpathList.add(openFile);
						tableViewer.setInput(classpathList);
						columnsPack(table);
					}
				}
			}
		});
		removeBtn = WidgetUtil.createButton(buttonComp, SWT.PUSH, Messages.getString("WizardPage1.13"), BUTTON_WIDTH, new GridData()); //$NON-NLS-1$
		removeBtn.setEnabled(false);
		removeBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = table.getSelectionIndex();
				if (selectionIndex >= 0) {
					table.remove(selectionIndex);
					classpathList.remove(selectionIndex);
					tableViewer.setInput(classpathList);
					// columnsPack(table);
				}
			}
		});

		final Button registedBtn = WidgetUtil.createButton(buttonComp, SWT.PUSH, Messages.getString("WizardPage1.14"), BUTTON_WIDTH, new GridData()); //$NON-NLS-1$
		registedBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				DriverSelectDialog dialog = new DriverSelectDialog(registedBtn.getShell());
				if (dialog.open() == DriverSelectDialog.OK) {
					classpathList.addAll(dialog.getTargetNames());
					tableViewer.setInput(classpathList);
					columnsPack(table);

				}
			}
		});


	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if (visible) {
			setDescription(MSG);

			DBConfigWizard wiz = (DBConfigWizard) getWizard();
			Object page = wiz.getNextPage(this);
			if (page instanceof WizardPage2) {
				((WizardPage2) page).searchDriverFlg = true;
			}
		}
	}

}
