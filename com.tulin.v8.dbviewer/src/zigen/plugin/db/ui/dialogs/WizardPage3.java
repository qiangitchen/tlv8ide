/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */

package zigen.plugin.db.ui.dialogs;

import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ColumnLayout;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.PatternUtil;
import zigen.plugin.db.core.SchemaInfo;
import zigen.plugin.db.core.SchemaSearcher;
import zigen.plugin.db.ui.actions.AutoDelayListener;
import zigen.plugin.db.ui.editors.event.TextSelectionListener;
import zigen.plugin.db.ui.util.WidgetUtil;

public class WizardPage3 extends DefaultWizardPage {

	public static final String MSG = Messages.getString("WizardPage3.0"); //$NON-NLS-1$

	Group symfowareOptionGrp;

	Text charsetText;

	Button unicodeCheck;

	Button commitModeCheck;

	// Button schemaOnlyCheck;

	Button symfowareOptionCheck;

	Composite container;

	Group group4;

	TreeMap settingSchemaMap = new TreeMap();

	SchemaInfo[] settingSchemas = null;

	SchemaInfo[] filterSchemas = null;


	public WizardPage3(ISelection selection) {
		super(Messages.getString("WizardPage3.1")); //$NON-NLS-1$
		setTitle(Messages.getString("WizardPage3.2")); //$NON-NLS-1$
		setPageComplete(true);


	}

	public void createControl(Composite parent) {
		container = createDefaultComposite(parent);
		group4 = new Group(container, SWT.NONE);
		group4.setText(Messages.getString("WizardPage3.6")); //$NON-NLS-1$
		// group4.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group4.setLayoutData(new GridData(GridData.FILL_BOTH));
		group4.setLayout(new GridLayout(1, false));

		Group group3 = new Group(container, SWT.NONE);
		group3.setText(Messages.getString("WizardPage3.5")); //$NON-NLS-1$
		group3.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group3.setLayout(new GridLayout(1, false));
		addAutoCommitSection(group3);

		Group group1 = new Group(container, SWT.NONE);
		group1.setText(Messages.getString("WizardPage3.3")); //$NON-NLS-1$
		group1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group1.setLayout(new GridLayout(1, false));
		addCharsetSection(group1);

		Group group2 = new Group(container, SWT.NONE);
		group2.setText(Messages.getString("WizardPage3.4")); //$NON-NLS-1$
		group2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group2.setLayout(new GridLayout(1, false));
		addConvertUnicodeSection(group2);

		// addOnlyDefaultSchemaSection(group4);
		// ddSchemaFilterSection(group4);


		setControl(container);
	}

	private void addCharsetSection(Composite group) {
		charsetText = new Text(group, SWT.SINGLE | SWT.BORDER);
		charsetText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		charsetText.addFocusListener(new TextSelectionListener());

		if (getOldConfig() != null) {
			if (getOldConfig().getCharset() != null)
				charsetText.setText(getOldConfig().getCharset());
		} else {
			charsetText.setText(DEFAULT_CHARSET);
		}
	}

	private void addConvertUnicodeSection(Composite group) {
		unicodeCheck = new Button(group, SWT.CHECK);
		unicodeCheck.setText(Messages.getString("WizardPage3.8")); //$NON-NLS-1$
		if (getOldConfig() != null) {
			unicodeCheck.setSelection(getOldConfig().isConvertUnicode());
		} else {
			unicodeCheck.setSelection(DEFAULT_CONVERTUNICODE);
		}
	}

	private void addAutoCommitSection(Composite group) {
		commitModeCheck = new Button(group, SWT.CHECK);
		commitModeCheck.setText(Messages.getString("WizardPage3.9")); //$NON-NLS-1$
		if (getOldConfig() != null) {
			commitModeCheck.setSelection(getOldConfig().isAutoCommit());
		} else {
			commitModeCheck.setSelection(DEFAULT_AUTOCOMMIT);
		}
	}

	private void addNoLockModeSection(Composite group) {
		symfowareOptionCheck = new Button(group, SWT.CHECK);
		symfowareOptionCheck.setText(Messages.getString("WizardPage3.11")); //$NON-NLS-1$
		symfowareOptionCheck.setSelection(true);
		if (getOldConfig() != null) {
			symfowareOptionCheck.setSelection(getOldConfig().isNoLockMode());
		} else {
			symfowareOptionCheck.setSelection(DEFAULT_NO_LOCK_MODE);
		}
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			setDescription(MSG);
			DBConfigWizard wiz = (DBConfigWizard) getWizard();
			Object page = wiz.getPreviousPage(this);
			if (page instanceof WizardPage2) {
				WizardPage2 page2 = (WizardPage2) page;
				page2.searchDriverFlg = false;

				String driver = page2.driverCombox.getText();
				if (driver != null && !"".equals(driver)) { //$NON-NLS-1$
					try {
						if (DBType.getType(driver) == DBType.DB_TYPE_SYMFOWARE) {
							if (symfowareOptionGrp == null) {
								symfowareOptionGrp = new Group(container, SWT.NONE);
								symfowareOptionGrp.setText(Messages.getString("WizardPage3.7")); //$NON-NLS-1$
								symfowareOptionGrp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
								symfowareOptionGrp.setLayout(new GridLayout(1, false));
								addNoLockModeSection(symfowareOptionGrp);
								resize();
							}
						}
					} catch (RuntimeException e) {
						e.printStackTrace();
					}
				}
			}


			try {
				if (getOldConfig() != null) {
					settingSchemas = getOldConfig().getDisplayedSchemas();
					if (settingSchemas != null) {
						for (int i = 0; i < settingSchemas.length; i++) {
							SchemaInfo info = settingSchemas[i];
							settingSchemaMap.put(info.getName(), info);
						}
					}
				}
				filterSchemas = loadSchemas();

				if (fTableViewer == null) {
					addSchemaFilterSection(group4);
					resize();
				}
			} catch (RuntimeException e) {
				e.printStackTrace();
			}


		}
	}

	private CheckboxTableViewer fTableViewer;

	private final String DISPLAY_TXT = Messages.getString("WizardPage3.12"); //$NON-NLS-1$

	private IDBConfig config = null;

	private Button selectedCheckOn = null;

	private Button selectedCheckOff = null;

	private Text filterText;

	private Button regularExpressions = null;

	private Button caseSensitive = null;

	// private SchemaFilter textFilter = null;

	private Button visibleCheck;


	boolean checkFilterPattern;

	String filterPattern = ""; //$NON-NLS-1$

	private void addSchemaFilterSection(Composite group) {

		Composite innerPanel1 = new Composite(group, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		innerPanel1.setLayout(gridLayout);
		innerPanel1.setLayoutData(new GridData(GridData.FILL_BOTH));


		visibleCheck = new Button(innerPanel1, SWT.CHECK);
		visibleCheck.setText(Messages.getString("WizardPage3.21")); //$NON-NLS-1$
		visibleCheck.setSelection(false);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		visibleCheck.setLayoutData(data);
		visibleCheck.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				checkFilterPattern = visibleCheck.getSelection();
				if (checkFilterPattern) {
					filter(filterText.getText());
				} else {
					filter(""); //$NON-NLS-1$
				}
				setEnabled(checkFilterPattern);
			}
		});


		// Label label = new Label(innerPanel1, SWT.NONE);
		// label.setText(Messages.getString("WizardPage3.13")); //$NON-NLS-1$
		filterText = new Text(innerPanel1, SWT.SINGLE | SWT.BORDER);
		filterText.setEnabled(false);
		filterText.addFocusListener(new TextSelectionListener());
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		filterText.setLayoutData(data);

		final Label label = new Label(innerPanel1, SWT.NONE);
		label.setText(Messages.getString("WizardPage3.23")); //$NON-NLS-1$
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		label.setLayoutData(data);

		Composite innerPanel3 = new Composite(group, SWT.NONE);
		gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		innerPanel3.setLayout(gridLayout);
		data = new GridData();
		data.horizontalSpan = 2;
		data.horizontalAlignment = GridData.END;
		innerPanel3.setLayoutData(data);

		regularExpressions = new Button(innerPanel3, SWT.CHECK);
		regularExpressions.setEnabled(false);
		regularExpressions.setText(Messages.getString("WizardPage3.14")); //$NON-NLS-1$
		regularExpressions.setSelection(false);
		data = new GridData();
		// data.horizontalSpan = 2;
		data.horizontalAlignment = GridData.END;
		regularExpressions.setLayoutData(data);
		regularExpressions.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				label.setVisible(!regularExpressions.getSelection());
			}

		});

		caseSensitive = new Button(innerPanel3, SWT.CHECK);
		caseSensitive.setEnabled(false);
		caseSensitive.setText(Messages.getString("WizardPage3.15")); //$NON-NLS-1$
		caseSensitive.setSelection(false);
		data = new GridData();
		// data.horizontalSpan = 2;
		data.horizontalAlignment = GridData.END;
		caseSensitive.setLayoutData(data);

		Composite innerPanel2 = new Composite(group, SWT.NONE);
		gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		innerPanel2.setLayout(gridLayout);
		innerPanel2.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite tableComposite = new Composite(innerPanel2, SWT.NONE);
		data = new GridData(GridData.FILL_BOTH);
		data.widthHint = 200;
		data.heightHint = 120;
		// data.heightHint = convertHeightInCharsToPixels(10);
		tableComposite.setLayoutData(data);
		ColumnLayout columnLayout = new ColumnLayout();
		tableComposite.setLayout(columnLayout);
		// Table table = new Table(tableComposite, SWT.CHECK | SWT.BORDER |
		// SWT.MULTI | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		Table table = new Table(tableComposite, SWT.CHECK | SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		GC gc = new GC(getShell());
		gc.setFont(JFaceResources.getDialogFont());

		TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText(DISPLAY_TXT);
		int minWidth = computeMinimumColumnWidth(gc, DISPLAY_TXT);
		columnLayout.addColumnData(new ColumnWeightData(1, minWidth, true));
		// columnLayout.addColumnData(new ColumnPixelData(minWidth, false,
		// false));
		gc.dispose();

		fTableViewer = new CheckboxTableViewer(table);
		fTableViewer.setLabelProvider(new SchemaLabelProvider());
		fTableViewer.setContentProvider(new SchemaContentProvider());

		fTableViewer.setInput(filterSchemas);

		if (filterSchemas != null) {
			for (int i = 0; i < filterSchemas.length; i++) {
				SchemaInfo info = filterSchemas[i];
				fTableViewer.setChecked(info, info.isChecked());
			}
		}

		Composite buttonComposite = new Composite(tableComposite, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(1, true));
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.minimumHeight = 0;
		data.minimumWidth = 0;
		data.horizontalIndent = 0;
		data.verticalIndent = 0;

		buttonComposite.setLayoutData(data);

		Composite buttons = new Composite(innerPanel2, SWT.NONE);
		buttons.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		buttons.setLayout(gridLayout);

		selectedCheckOn = WidgetUtil.createButton(buttons, SWT.PUSH, Messages.getString("WizardPage3.16"), BUTTON_WIDTH2, new GridData()); //$NON-NLS-1$
		selectedCheckOn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				for (int i = 0; i < fTableViewer.getTable().getItemCount(); i++) {
					SchemaInfo schema = (SchemaInfo) fTableViewer.getElementAt(i);
					schema.setChecked(true);
					fTableViewer.setChecked(schema, true);

				}
			}
		});

		selectedCheckOff = WidgetUtil.createButton(buttons, SWT.PUSH, Messages.getString("WizardPage3.17"), BUTTON_WIDTH2, new GridData()); //$NON-NLS-1$
		selectedCheckOff.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				for (int i = 0; i < fTableViewer.getTable().getItemCount(); i++) {
					SchemaInfo schema = (SchemaInfo) fTableViewer.getElementAt(i);
					schema.setChecked(false);
					fTableViewer.setChecked(schema, false);
				}
			}
		});


		Button onlyDefaultSchema = WidgetUtil.createButton(buttons, SWT.PUSH, Messages.getString("WizardPage3.18"), BUTTON_WIDTH2, new GridData()); //$NON-NLS-1$
		onlyDefaultSchema.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				config = ((DBConfigWizard) getWizard()).createNewConfig();
				// schemas = getSchemas();
				for (int i = 0; i < filterSchemas.length; i++) {
					SchemaInfo info = filterSchemas[i];
					if (info.getName().equalsIgnoreCase(config.getSchema())) {
						info.setChecked(true);
					} else {
						info.setChecked(false);
					}
					fTableViewer.setChecked(info, info.isChecked());
				}
				fTableViewer.setInput(filterSchemas);
				// filter(filterText.getText());
			}
		});

		Button reload = WidgetUtil.createButton(buttons, SWT.PUSH, Messages.getString("WizardPage3.19"), BUTTON_WIDTH2, new GridData()); //$NON-NLS-1$
		reload.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				config = ((DBConfigWizard) getWizard()).createNewConfig();
				filterSchemas = loadSchemas();;
				fTableViewer.setInput(filterSchemas);
				// filter(filterText.getText());
			}
		});

		// filterText.addKeyListener(new KeyAdapter() {
		// public void keyReleased(KeyEvent e) {
		// filter(filterText.getText());
		// }
		//
		// });
		filterText.addKeyListener(new FilterListener());

		regularExpressions.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
			// filter(filterText.getText());
			}
		});
		caseSensitive.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
			// filter(filterText.getText());
			}
		});

		fTableViewer.addCheckStateListener(new ICheckStateListener() {

			public void checkStateChanged(CheckStateChangedEvent event) {
				SchemaInfo schema = (SchemaInfo) event.getElement();
				schema.setChecked(event.getChecked());
				// test(filterSchemas);
			}
		});

		filterText.setText(filterPattern);
		visibleCheck.setSelection(checkFilterPattern);
		setEnabled(checkFilterPattern);

	}

	private void setEnabled(boolean b) {
		filterText.setEnabled(b);
		regularExpressions.setEnabled(b);
		caseSensitive.setEnabled(b);
	}

	class FilterListener extends AutoDelayListener {

		private static final int delayTime = 300;

		public FilterListener() {
			super(delayTime);
		}

		public Runnable createExecutAction() {
			return new Runnable() {

				public void run() {
					try {
						filterPattern = filterText.getText();
						filter(filterPattern);
					} catch (Exception e) {
						DbPlugin.log(e);
					}

				}
			};
		}
	}

	private void filter(String condition) {
		for (int i = 0; i < fTableViewer.getTable().getItemCount(); i++) {
			SchemaInfo info = (SchemaInfo) fTableViewer.getElementAt(i);
			info.setChecked(filter(info, condition));
			fTableViewer.setChecked(info, info.isChecked());
		}
		fTableViewer.refresh();

	}

	boolean filter(SchemaInfo info, String text) {
		if (text != null && !"".equals(text)) { //$NON-NLS-1$
			String name = info.getName();

			if (regularExpressions.getSelection()) {
				try {
					Pattern pattern = null;
					if (!caseSensitive.getSelection()) {
						pattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE);
					} else {
						pattern = Pattern.compile(text);
					}
					Matcher mc = pattern.matcher(name);
					return mc.matches();

				} catch (PatternSyntaxException e) {
					return false;
				}
			} else {
				if (text != null && text.trim().length() > 0) {
					try {
						Pattern pattern = PatternUtil.getPattern(text, caseSensitive.getSelection());
						Matcher mc = pattern.matcher(name);
						return mc.matches();
					} catch (PatternSyntaxException e) {
						return false;
					}
				}
				return true;

			}
		} else {
			return true;
		}

	}


	private SchemaInfo[] loadSchemas() {
		SchemaInfo[] schemas = null;

		boolean isSetting = !settingSchemaMap.isEmpty();
		try {
			config = ((DBConfigWizard) getWizard()).createNewConfig();

			String[] names = SchemaSearcher.execute(config);

			if (!isSetting) {
				schemas = new SchemaInfo[names.length];
				for (int i = 0; i < names.length; i++) {
					schemas[i] = new SchemaInfo(config, names[i], true);
				}
			} else {
				schemas = new SchemaInfo[names.length];
				for (int i = 0; i < names.length; i++) {
					if (settingSchemaMap.containsKey(names[i])) {
						SchemaInfo info = (SchemaInfo) settingSchemaMap.get(names[i]);
						if (info.isChecked()) {
							schemas[i] = new SchemaInfo(config, names[i], true);
						} else {
							schemas[i] = new SchemaInfo(config, names[i], false);
						}
					} else {
						schemas[i] = new SchemaInfo(config, names[i], false);
					}
				}
			}

			return schemas;

		} catch (Exception e) {
			StringBuffer sb = new StringBuffer(Messages.getString("WizardPage3.20")); //$NON-NLS-1$
			sb.append("\n").append(e.getMessage()); //$NON-NLS-1$
			super.setMessage(sb.toString(), WizardPage.ERROR);
			return settingSchemas;
		}

	}


	class SchemaLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			SchemaInfo data = (SchemaInfo) element;
			switch (columnIndex) {
			case 0:
				return data.getName();
			default:
				return ""; //$NON-NLS-1$
			}
		}
	}

	class SchemaContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object input) {
			if (input instanceof SchemaInfo[]) {
				return (SchemaInfo[]) input;
			} else {
				return null;
			}
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

		public void dispose() {}
	}

}
