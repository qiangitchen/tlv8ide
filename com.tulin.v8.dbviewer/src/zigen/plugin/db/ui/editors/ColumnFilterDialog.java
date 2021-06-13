/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.core.PatternUtil;
import zigen.plugin.db.ui.actions.AutoDelayListener;
import zigen.plugin.db.ui.editors.event.TextSelectionListener;
import zigen.plugin.db.ui.editors.internal.ColumnFilterInfo;
import zigen.plugin.db.ui.jobs.RecordSearchJob;
import zigen.plugin.db.ui.jobs.TableFilterJob;

public class ColumnFilterDialog extends Dialog {

	private TableViewEditorFor31 editor;

	private TableViewer columnTableViewer;

	private int currentSortNumber = 0;

	private static final int BUTTON_ID_SELECTALL = -100;

	private static final int BUTTON_ID_REMOVEALL = -101;

	public ColumnFilterDialog(Shell parent, TableViewEditorFor31 editor) {
		super(parent);
		super.setDefaultImage(ImageCacher.getInstance().getImage(DbPlugin.IMG_CODE_DB));

		setShellStyle(getShellStyle() | SWT.MAX | SWT.RESIZE);
		this.editor = editor;

		initialized(editor.filterInfos);
	}

	private void initialized(ColumnFilterInfo[] infos) {
		TreeMap map = new TreeMap();
		for (int i = 0; i < infos.length; i++) {
			int sortNum = infos[i].getSortNo();
			if (sortNum > 0) {
				map.put(new Integer(sortNum), infos[i]);
			}
		}
		int cnt = 0;
		for (Iterator itr = map.keySet().iterator(); itr.hasNext();) {
			Integer key = (Integer) itr.next();
			ColumnFilterInfo info = (ColumnFilterInfo) map.get(key);
			info.setSortNo(++cnt);
		}
		currentSortNumber = cnt;
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.getString("ColumnFilterDialog.0")); //$NON-NLS-1$
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, BUTTON_ID_SELECTALL, Messages.getString("ColumnFilterDialog.1"), false); //$NON-NLS-1$
		createButton(parent, BUTTON_ID_REMOVEALL, Messages.getString("ColumnFilterDialog.2"), false); //$NON-NLS-1$

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

			// int limit = DbPlugin.getDefault().getPreferenceStore().getInt(PreferencePage.P_MAX_VIEW_RECORD);

			RecordSearchJob job1 = new RecordSearchJob(editor, editor.getCondition(), editor.getOrderByString(), editor.offset, editor.limit);
			job1.setPriority(RecordSearchJob.SHORT);
			job1.setUser(true);
			job1.schedule();
			try {
				job1.join();
			} catch (InterruptedException e) {
				DbPlugin.log(e);
			}
			TableFilterJob job = new TableFilterJob(editor.getViewer(), editor.filterInfos);
			job.setPriority(TableFilterJob.SHORT);
			job.setUser(false);
			job.schedule();

		} else if (buttonId == BUTTON_ID_SELECTALL) {
			selectAllHandler();
		} else if (buttonId == BUTTON_ID_REMOVEALL) {
			removeAllHandler();
		}

		super.buttonPressed(buttonId);
	}

	private static final String[] headers = {
			"", Messages.getString("ColumnFilterDialog.4"), Messages.getString("ColumnFilterDialog.14"), Messages.getString("ColumnFilterDialog.13"), Messages.getString("ColumnFilterDialog.5"), Messages.getString("ColumnFilterDialog.6"), Messages.getString("ColumnFilterDialog.7"), Messages.getString("ColumnFilterDialog.8"), Messages.getString("ColumnFilterDialog.9"), Messages.getString("ColumnFilterDialog.10"), Messages.getString("ColumnFilterDialog.11")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$//$NON-NLS-10$//$NON-NLS-11$

	Button visibleCheck;

	Text filterText;

	Button regularExpressions;

	Button caseSensitive;

	boolean isFilterPattern;

	String filterPattern;

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		// Label label = new Label(composite, SWT.NONE);
		// label.setText(Messages.getString("ColumnFilterDialog.12"));
		// //$NON-NLS-1$

		Composite innerPanel1 = new Composite(composite, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		innerPanel1.setLayout(gridLayout);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		innerPanel1.setLayoutData(data);

		visibleCheck = new Button(innerPanel1, SWT.CHECK);
		visibleCheck.setText(Messages.getString("ColumnFilterDialog.18")); //$NON-NLS-1$
		visibleCheck.setSelection(false);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		visibleCheck.setLayoutData(data);
		visibleCheck.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (visibleCheck.getSelection()) {
					filterText.setEnabled(true);
					regularExpressions.setEnabled(true);
					caseSensitive.setEnabled(true);
					filter(filterText.getText());
				} else {
					filterText.setEnabled(false);
					regularExpressions.setEnabled(false);
					caseSensitive.setEnabled(false);
					filter(""); //$NON-NLS-1$
				}
				isFilterPattern = visibleCheck.getSelection();
			}

		});

		// Label label2 = new Label(innerPanel1, SWT.NONE);
		// label2.setText(Messages.getString("ColumnFilterDialog.15"));
		// //$NON-NLS-1$
		filterText = new Text(innerPanel1, SWT.SINGLE | SWT.BORDER);
		filterText.setEnabled(false);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		filterText.setLayoutData(data);

		filterText.addFocusListener(new TextSelectionListener());
		filterText.setFont(DbPlugin.getDefaultFont());
		filterText.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
			// filter(filterText.getText());
			}

		});

		filterText.addKeyListener(new AutoDelayFilterListener());

		Composite innerPanel2 = new Composite(composite, SWT.NONE);
		gridLayout = new GridLayout(3, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		innerPanel2.setLayout(gridLayout);
		data = new GridData(GridData.FILL_HORIZONTAL);
		// data.horizontalAlignment = GridData.END;
		innerPanel2.setLayoutData(data);


		final Label label = new Label(innerPanel2, SWT.NONE);
		label.setText(Messages.getString("ColumnFilterDialog.23")); //$NON-NLS-1$
		data = new GridData(GridData.FILL_HORIZONTAL);
		label.setLayoutData(data);


		regularExpressions = new Button(innerPanel2, SWT.CHECK);
		regularExpressions.setEnabled(false);
		regularExpressions.setText(Messages.getString("ColumnFilterDialog.16")); //$NON-NLS-1$
		regularExpressions.setSelection(false);
		data = new GridData();
		// data.horizontalAlignment = GridData.END;
		regularExpressions.setLayoutData(data);
		regularExpressions.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				label.setVisible(!regularExpressions.getSelection());
			}
		});


		caseSensitive = new Button(innerPanel2, SWT.CHECK);
		caseSensitive.setEnabled(false);
		caseSensitive.setText(Messages.getString("ColumnFilterDialog.17")); //$NON-NLS-1$
		caseSensitive.setSelection(false);
		data = new GridData();
		data.horizontalAlignment = GridData.END;
		caseSensitive.setLayoutData(data);

		Composite tableComposite = new Composite(composite, SWT.NONE);
		tableComposite.setLayout(new GridLayout(1, false));
		data = new GridData(GridData.FILL_BOTH);
		// data.widthHint = 200;
		data.heightHint = 400;
		tableComposite.setLayoutData(data);

		columnTableViewer = new TableViewer(tableComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		columnTableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));


		Label label3 = new Label(tableComposite, SWT.NONE);
		label3.setText(Messages.getString("ColumnFilterDialog.12")); //$NON-NLS-1$
		data = new GridData(GridData.FILL_HORIZONTAL);
		label3.setLayoutData(data);


		Table table = columnTableViewer.getTable();
		table.setFont(DbPlugin.getDefaultFont());

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		setHeaderColumn(table, headers);

		columnTableViewer.setColumnProperties(new String[] { // "dummy",
				// "check",
						// "check",
						// "check",
						// "check",
						// "check",
						// "check",
						// "check",
						// "check"});
						// //$NON-NLS-1$
						// //$NON-NLS-2$
						// //$NON-NLS-3$
						// //$NON-NLS-4$
						// //$NON-NLS-5$
						// //$NON-NLS-6$
						// //$NON-NLS-7$
						// //$NON-NLS-8$
						// //$NON-NLS-9$
						"dummy", //$NON-NLS-1$
						"check", "sort", "sort", "check", "check", "check", "check", "check", "check", "check"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$

		CheckboxCellEditor checkEditor = new CheckboxCellEditor(table);
		CheckboxCellEditor checkEditor2 = new CheckboxCellEditor(table);
		CellEditor[] editors = new CellEditor[] {null, checkEditor, checkEditor2, checkEditor2, null, null, null, null, null, null, null};

		columnTableViewer.setCellEditors(editors);
		columnTableViewer.setCellModifier(new ColumnSelectCellModifier());
		columnTableViewer.setLabelProvider(new ColumnSelectLabelProvider());
		columnTableViewer.setContentProvider(new ColumnSelectContentProvider());

		columnTableViewer.setInput(editor.filterInfos);
		columnsPack(table);


		filterPattern = (editor.filterPattern == null) ? "" : editor.filterPattern; //$NON-NLS-1$
		filterText.setText(filterPattern);

		visibleCheck.setSelection(editor.checkFilterPattern);


		if (visibleCheck.getSelection()) {
			filterText.setEnabled(true);
			regularExpressions.setEnabled(true);
			caseSensitive.setEnabled(true);
		} else {
			filterText.setEnabled(false);
			regularExpressions.setEnabled(false);
			caseSensitive.setEnabled(false);
		}
		isFilterPattern = visibleCheck.getSelection();

		return composite;
	}


	private void selectAllHandler() {
		for (int i = 0; i < columnTableViewer.getTable().getItemCount(); i++) {
			ColumnFilterInfo info = (ColumnFilterInfo) columnTableViewer.getElementAt(i);
			info.setChecked(true);
		}
		columnTableViewer.refresh();
	}

	private void removeAllHandler() {
		for (int i = 0; i < columnTableViewer.getTable().getItemCount(); i++) {
			ColumnFilterInfo info = (ColumnFilterInfo) columnTableViewer.getElementAt(i);
			if (info.isPrimaryKey()) {
				info.setChecked(true);
			} else {
				info.setChecked(false);
			}
		}
		columnTableViewer.refresh();
	}

	private void setHeaderColumn(Table table, String[] headers) {
		for (int i = 0; i < headers.length; i++) {
			TableColumn col;

			switch (i) {
			case 0:
				col = new TableColumn(table, SWT.NONE, i);
				col.setText(headers[i]);
				col.setResizable(false);
				col.setWidth(0);
				break;
			case 1:
				col = new TableColumn(table, SWT.CENTER, i);
				col.setText(headers[i]);
				col.setResizable(false);
				col.setWidth(50);
				break;
			case 2:
				col = new TableColumn(table, SWT.NONE, i);
				col.setText(headers[i]);
				col.setResizable(false);
				col.setWidth(50);
				break;
			case 3:
				col = new TableColumn(table, SWT.CENTER, i);
				col.setText(headers[i]);
				col.setResizable(false);
				col.setWidth(50);
				break;
			case 7:
			case 8:
				col = new TableColumn(table, SWT.CENTER, i);
				col.setText(headers[i]);
				col.setResizable(true);
				col.pack();
				break;
			default:
				col = new TableColumn(table, SWT.LEFT, i);
				col.setText(headers[i]);
				col.setResizable(true);
				col.pack();
				break;
			}

		}
	}

	private void columnsPack(Table table) {
		table.setVisible(false);
		TableColumn[] cols = table.getColumns();
		for (int i = 3; i < cols.length; i++) {
			cols[i].pack();
		}
		table.setVisible(true);
	}

	private class ColumnSelectLabelProvider extends LabelProvider implements ITableLabelProvider {

		private ImageCacher imageCacher = ImageCacher.getInstance();

		private Image getImage(boolean isSelected) {
			String key = isSelected ? DbPlugin.IMG_CODE_CHECKED_CENTER : DbPlugin.IMG_CODE_UNCHECKED_CENTER;
			return imageCacher.getImage(key);
		}

		public String getColumnText(Object element, int columnIndex) {
			String result = ""; //$NON-NLS-1$
			ColumnFilterInfo col = (ColumnFilterInfo) element;
			switch (columnIndex) {
			case 0:
			case 1:
			case 2:
				break;
			case 3:
				result = (col.getSortNo() == 0) ? "" : String.valueOf(col.getSortNo()); //$NON-NLS-1$
				// if(!"".equals(result)){ //$NON-NLS-1$
				// if(col.isDesc()){
				// result = result +
				// Messages.getString("ColumnFilterDialog.17"); //$NON-NLS-1$
				// }else{
				// result = result +
				// Messages.getString("ColumnFilterDialog.18"); //$NON-NLS-1$
				// }
				// }
				// result = String.valueOf(col.getSortNo());
				break;
			case 4:
				result = col.getColumnName();
				break;
			case 5:
				result = col.getTypeName().toUpperCase();
				break;
			case 6:
				result = col.getSize();
				break;
			case 7:
				result = col.isPrimaryKey() ? Messages.getString("ColumnFilterDialog.24") : Messages.getString("ColumnFilterDialog.25"); //$NON-NLS-1$ //$NON-NLS-2$
				break;

			case 8:
				result = col.isNotNull() ? Messages.getString("ColumnFilterDialog.26") : Messages.getString("ColumnFilterDialog.27"); //$NON-NLS-1$ //$NON-NLS-2$
				break;
			case 9:
				result = col.getDefaultValue();
				break;
			case 10:
				result = col.getCommentName();
				break;
			default:
				break;
			}
			return result;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			ColumnFilterInfo info = (ColumnFilterInfo) element;
			if (columnIndex == 1) {
				if (!info.isPrimaryKey()) {
					return getImage((info.isChecked()));
				} else {
					return imageCacher.getImage(DbPlugin.IMG_CODE_DISABLED_CHECKED_CENTER);
				}
			} else if (columnIndex == 2) {
				if (info.getSortNo() == 0) {
					return imageCacher.getImage(DbPlugin.IMG_CODE_DUMMY);
				} else {
					if (info.isDesc()) {
						return imageCacher.getImage(DbPlugin.IMG_CODE_DESC);
					} else {
						return imageCacher.getImage(DbPlugin.IMG_CODE_ASC);
					}
				}

			}
			return null;
		}

		public Image getImage(Object obj) {
			return null;
		}
	}

	private class ColumnSelectContentProvider implements IStructuredContentProvider {

		private List contents = null;

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ColumnFilterInfo[]) {
				return (ColumnFilterInfo[]) inputElement;
			}
			return null;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			contents = null;
		}

		public void dispose() {
			contents = null;
		}

	}

	private class ColumnSelectCellModifier implements ICellModifier {

		public ColumnSelectCellModifier() {}

		public boolean canModify(Object element, String property) {
			return true;
		}

		public Object getValue(Object element, String property) {
			ColumnFilterInfo item = (ColumnFilterInfo) element;
			if (property == "check") { //$NON-NLS-1$
				return new Boolean(item.isChecked());
			} else if (property == "sort") { //$NON-NLS-1$
				return new Boolean(item.isChecked());
			} else {
				return item.getColumnName();
			}
		}

		public void modify(Object element, String property, Object value) {
			if (element instanceof Item) {
				element = ((Item) element).getData();
			}
			ColumnFilterInfo info = (ColumnFilterInfo) element;

			if (property == "check" && !info.isPrimaryKey()) { //$NON-NLS-1$
				info.setChecked(((Boolean) value).booleanValue());
				columnTableViewer.update(element, new String[] {"check"}); //$NON-NLS-1$

			} else if (property == "sort") { //$NON-NLS-1$
				int current = info.getSortNo();

				if (current == 0) {
					info.setSortNo(++currentSortNumber);
					info.setDesc(false);
					columnTableViewer.update(element, new String[] {"sort"}); //$NON-NLS-1$
				} else if (!info.isDesc()) {
					info.setDesc(true);
					columnTableViewer.update(element, new String[] {"sort"}); //$NON-NLS-1$
				} else if (info.isDesc()) {
					info.setSortNo(0);
					updateAll();
				}

			}

		}

	}

	private void updateAll() {
		ColumnFilterInfo[] infos = editor.filterInfos;
		TreeMap map = new TreeMap();
		for (int i = 0; i < infos.length; i++) {
			int sortNum = infos[i].getSortNo();
			if (sortNum > 0) {
				map.put(new Integer(sortNum), infos[i]);
			}
		}
		int cnt = 0;
		for (Iterator itr = map.keySet().iterator(); itr.hasNext();) {
			Integer key = (Integer) itr.next();
			ColumnFilterInfo info = (ColumnFilterInfo) map.get(key);
			info.setSortNo(++cnt);
		}
		currentSortNumber = cnt;
		columnTableViewer.update(infos, new String[] {"sort"}); //$NON-NLS-1$
	}


	private void filter(String condition) {

		if (!visibleCheck.getSelection())
			return;

		for (int i = 0; i < columnTableViewer.getTable().getItemCount(); i++) {
			ColumnFilterInfo info = (ColumnFilterInfo) columnTableViewer.getElementAt(i);
			if (!info.isPrimaryKey()) {
				info.setChecked(filter(info, condition));
			} else {
				info.setChecked(true);
			}
		}
		columnTableViewer.refresh();

	}

	boolean filter(ColumnFilterInfo info, String text) {
		if (text != null && !"".equals(text)) { //$NON-NLS-1$
			String columnName = info.getColumnName();
			String remarks = info.getCommentName();

			if (regularExpressions.getSelection()) {
				try {
					Pattern pattern = null;
					if (!caseSensitive.getSelection()) {
						pattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE);
					} else {
						pattern = Pattern.compile(text);
					}
					Matcher mc = pattern.matcher(columnName);
					Matcher mc2 = pattern.matcher(remarks);
					return mc.matches() || mc2.matches();

				} catch (PatternSyntaxException e) {
					return false;
				}
			} else {
				if (text != null && text.trim().length() > 0) {
					try {
						Pattern pattern = PatternUtil.getPattern(text, caseSensitive.getSelection());
						Matcher mc = pattern.matcher(columnName);
						Matcher mc2 = pattern.matcher(remarks);
						return mc.matches() || mc2.matches();
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


	class AutoDelayFilterListener extends AutoDelayListener {

		private static final int delayTime = 300;

		public AutoDelayFilterListener() {
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


}
