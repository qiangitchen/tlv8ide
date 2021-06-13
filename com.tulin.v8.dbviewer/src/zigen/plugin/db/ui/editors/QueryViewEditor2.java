/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.MultiPageEditorPart;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.actions.CopyRecordDataAction;
import zigen.plugin.db.ui.actions.ITableViewEditorAction;
import zigen.plugin.db.ui.actions.SelectAllRecordAction;
import zigen.plugin.db.ui.editors.event.TableDefaultSortListener;
import zigen.plugin.db.ui.editors.event.TableKeyAdapter;
import zigen.plugin.db.ui.editors.event.TableKeyEventHandler;
import zigen.plugin.db.ui.editors.event.TableSortListener;
import zigen.plugin.db.ui.editors.internal.CellEditorType;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.jobs.ChangeColorJob;
import zigen.plugin.db.ui.jobs.RecordCountForQueryJob;
import zigen.plugin.db.ui.jobs.RecordSearchJob;
import zigen.plugin.db.ui.jobs.SqlExecForPagerJob;
import zigen.plugin.db.ui.jobs.SqlExecJob;
import zigen.plugin.db.ui.views.StatusLineContributionItem;

public class QueryViewEditor2 extends MultiPageEditorPart implements ITableViewEditor, IQueryViewEditor, IPageChangeListener {
	protected int limit = 0;
	protected int offset = 0;
	
	public void pageChanged(int status, int offset, int limit) {
		this.offset = offset;
		this.limit = limit;
		updateTableViewer(offset, limit);
	}
	protected void updateTableViewer(int offset, int limit) {
		if (limit == 0) {
			offset = 0;
		}
		QueryViewEditorInput ei = (QueryViewEditorInput) getEditorInput();
		Transaction trans = Transaction.getInstance(config);
		SqlExecForPagerJob job = new SqlExecForPagerJob(trans, query, ei.getSecondarlyId(), true, offset, limit);
		// job.setPriority(Job.SHORT);
		job.setUser(false);
		job.schedule();
	}
	
	
	private ImageCacher ic = ImageCacher.getInstance();

	private Table table;

	private TableViewer viewer;

	private TableElement[] elements;

	private String query;

	private IDBConfig config = null;

	private TableSortListener sortListener;

	protected StatusLineContributionItem responseTimeItem;

	protected String responseTime;

	protected SelectAllRecordAction selectAllRecordAction;

	protected CopyRecordDataAction copyAction;

	protected ChangeColorJob changeColorJob;

	protected Label infoLabel;

	TableKeyEventHandler handler;

	public QueryViewEditor2() {
		super();
	}

	public void setInfomationText(String message) {
		infoLabel.setText(message);
	}

	private void makeActions() {
		selectAllRecordAction = new SelectAllRecordAction();
		copyAction = new CopyRecordDataAction();
		selectAllRecordAction.setActiveEditor(this);
		copyAction.setActiveEditor(this);

	}

	protected void createPages() {
		makeActions();
		createLogPage(); // dummy page
	}

	private void createLogPage() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		composite.setLayout(new FillLayout());
		int index = addPage(composite);
		setPageText(index, "log"); //$NON-NLS-1$
	}

	private boolean hasContributionItem(IStatusLineManager manager, String id) {
		IContributionItem[] items = manager.getItems();
		for (int i = 0; i < items.length; i++) {
			IContributionItem item = items[i];
			if (item.getId().equals(id)) {
				responseTimeItem = (StatusLineContributionItem) item;
				return true;
			}
		}
		return false;
	}

	public void contributeToStatusLine() {
		IEditorSite site = super.getEditorSite();
		IActionBars actionBars = site.getActionBars();
		IStatusLineManager manager = actionBars.getStatusLineManager();

		if (!hasContributionItem(manager, "RecordCount")) { //$NON-NLS-1$
			responseTimeItem = new StatusLineContributionItem("RecordCount"); //$NON-NLS-1$
			manager.add(responseTimeItem);
		}

	}

	private void createMessageArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = true;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);

		infoLabel = new Label(composite, SWT.NONE);
		infoLabel.setText(""); //$NON-NLS-1$
		infoLabel.setForeground(new Color(null, 255, 0, 0));
		// Composite composite = new Composite(tool, SWT.NONE);
		GridData data1 = new GridData(GridData.FILL_HORIZONTAL);
		data1.verticalIndent = 1;
		infoLabel.setLayoutData(data1);

		ToolBar toolbar = new ToolBar(composite, SWT.FLAT);
		toolbar.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		ToolItem reloeadItem = new ToolItem(toolbar, SWT.NONE);
		reloeadItem.setImage(ic.getImage(DbPlugin.IMG_CODE_REFRESH));
		reloeadItem.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
//				QueryViewEditorInput ei = (QueryViewEditorInput) getEditorInput();
//				Transaction trans = Transaction.getInstance(config);
//				SqlExecJob job = new SqlExecJob(trans, query, ei.getSecondarlyId(), true);
//				// job.setPriority(Job.SHORT);
//				job.setUser(false);
//				job.schedule();
				
				QueryViewEditorInput ei = (QueryViewEditorInput) getEditorInput();
				Transaction trans = Transaction.getInstance(config);
				SqlExecForPagerJob job = new SqlExecForPagerJob(trans, query, ei.getSecondarlyId(), true, offset, limit);
				// job.setPriority(Job.SHORT);
				job.setUser(false);
				job.schedule();
				
			}
		});
	}

	private void createMainPage() {
		Composite main = new Composite(getContainer(), SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		main.setLayout(gridLayout);

		createMessageArea(main);

		table = new Table(main, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gridData2 = new GridData(GridData.FILL_BOTH);
		table.setLayoutData(gridData2);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setFont(DbPlugin.getDefaultFont());

		viewer = new TableViewer(table);
		setHeaderColumn(table);
		viewer.setContentProvider(new TableViewContentProvider());
		viewer.setLabelProvider(new TableViewLabelProvider());

		table.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.F2) {
					int row = handler.getSelectedRow();
					handler.editTableElement(row, 1);

				}
			}
		});

		table.addFocusListener(new FocusAdapter() {

			public void focusGained(FocusEvent e) {
				if (table.getSelectionIndex() == -1) {
					table.select(0);
					table.notifyListeners(SWT.Selection, null);

				}
				IActionBars bars = getEditorSite().getActionBars();
				setGlobalActionForEditor(bars);
				bars.updateActionBars();

			}
		});

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent e) {
				selectionChangeHandler(e);
			}
		});

		viewer.setInput(elements);

		changeColorJob = new ChangeColorJob(table);
		changeColorJob.setPriority(ChangeColorJob.LONG);
		changeColorJob.setUser(false);
		changeColorJob.schedule();

		handler = new TableKeyEventHandler(this);
		setCellModify(viewer, handler);

		columnsPack(table);
		hookContextMenu();
		contributeToStatusLine();

		createFooterArea(main);
		
		int index = addPage(main);

		setActivePage(getPageCount() - 1);

		getSite().setSelectionProvider(viewer);

	}
	
	CoolItem pagerItem;
	QueryViewerPager pager;
	private void createFooterArea(Composite parent) {
		CoolBar coolBar1 = new CoolBar(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		coolBar1.setLayoutData(gridData);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		coolBar1.setLayout(gridLayout);
		pagerItem = new CoolItem(coolBar1, SWT.FLAT);
		
		limit = DbPlugin.getDefault().getPreferenceStore().getInt(PreferencePage.P_MAX_VIEW_RECORD);
		pager = new QueryViewerPager(limit);
		pagerItem.setControl(pager.createStackedButtons(coolBar1));
		computeSize(pagerItem);
		pager.setPageNo(1);
		pager.addPageChangeListener(this);
//		infoLabelItem = new CoolItem(coolBar1, SWT.NONE);
//		infoLabel = new Label(coolBar1, SWT.NONE);
//		infoLabel.setText(""); //$NON-NLS-1$
//		infoLabel.setForeground(new Color(null, 255, 0, 0));
//		// infoLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		infoLabelItem.setControl(infoLabel);
//
//		computeSize(infoLabelItem);
	}
	private void computeSize(CoolItem item) {
		Control control = item.getControl();
		Point pt = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		pt = item.computeSize(pt.x + 5, pt.y);
		item.setSize(pt);
	}
	
	
	CellEditor[] cellEditors;

	public void refleshAction() {
		// selectAllRecordAction.refresh();
		copyAction.refresh();
	}

	void selectionChangeHandler(SelectionChangedEvent event) {
		refleshAction();
	}

	private void setCellModify(TableViewer viewer, TableKeyEventHandler handler) {
		if (elements == null)
			return;
		final IActionBars bars = getEditorSite().getActionBars();
		TableElement element = elements[0];
		int size = element.getColumns().length + 1;
		String[] properties = new String[size];
		zigen.plugin.db.core.TableColumn[] cols = element.getColumns();
		cellEditors = new CellEditor[size];
		TableKeyAdapter keyAdapter = new TableKeyAdapter(handler);
		for (int i = 0; i < cellEditors.length; i++) {
			properties[i] = String.valueOf(i);
			if (i > 0) {
				CellEditor cellEditor = new TextCellEditor(table, i);

				if (cellEditor.getControl() instanceof Text) {
					Text txt = (Text) cellEditor.getControl();
					txt.setEditable(false);
				}
				cellEditor.getControl().addKeyListener(keyAdapter);
				cellEditor.getControl().addTraverseListener(keyAdapter);
				cellEditor.getControl().addFocusListener(new FocusAdapter() {

					public void focusGained(FocusEvent e) {
						bars.clearGlobalActionHandlers();
						bars.updateActionBars();
					}

					public void focusLost(FocusEvent e) {
					// setInfomationText(EDIT_MODE_OFF); non message
					}
				});
				cellEditors[i] = cellEditor;

			}
		}
		viewer.setColumnProperties(properties);
		viewer.setCellModifier(new ICellModifier() {

			public boolean canModify(Object element, String property) {
				return true;
			}

			public Object getValue(Object element, String property) {
				int index = Integer.parseInt(property);
				if (element instanceof TableElement) {
					TableElement elem = (TableElement) element;
					Object obj = elem.getItems()[index - 1];
					if (obj != null) {
						if (obj instanceof String) {
							return (String) obj;
						} else {
							return CellEditorType.getDataTypeName(elem.getColumns()[index]);
						}
					} else {
						return ""; //$NON-NLS-1$

					}
				}
				return null;
			}

			public void modify(Object element, String property, Object value) {}

		});
		viewer.setCellEditors(cellEditors);
	}

	public void setTotalCount(int dispCount, long totalCount) {
		NumberFormat format = NumberFormat.getInstance();
		String displayCount = format.format(dispCount);
		String displayTotalCount = format.format(totalCount);

		StringBuffer sb = new StringBuffer();
		sb.append(displayCount);
		sb.append(Messages.getString("QueryViewEditor2.3")); //$NON-NLS-1$

		if (!"".equals(displayTotalCount)) { //$NON-NLS-1$
			if ("-1".equals(displayTotalCount)) { //$NON-NLS-1$
				;
			} else {
				sb.append(" / "); //$NON-NLS-1$
				sb.append(Messages.getString("QueryViewEditor2.7")); //$NON-NLS-1$
				sb.append(totalCount);
				sb.append(Messages.getString("QueryViewEditor2.8")); //$NON-NLS-1$
			}
		} else {
			sb.append(Messages.getString("QueryViewEditor2.9")); //$NON-NLS-1$
		}
		sb.append(Messages.getString("QueryViewEditor2.10")); //$NON-NLS-1$
		sb.append(responseTime);
		sb.append("]"); //$NON-NLS-1$

		setPageText(getActivePage(), sb.toString());
		
		pager.setLimit(limit);
		pager.setRecordCount((int) totalCount);
		computeSize(pagerItem);
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;

		if (responseTimeItem != null && responseTime != null && !"".equals(responseTime)) { //$NON-NLS-1$
			StringBuffer sb = new StringBuffer();
			sb.append(Messages.getString("QueryViewEditor2.13")); //$NON-NLS-1$
			sb.append(responseTime);
			responseTimeItem.setText(sb.toString());

		}
	}

	public void update(String query, TableElement[] elements, String responseTime, boolean isReload) {
		try {
			this.query = query;

			
			// remove dummy page
			if (getPageText(0).equals("log")) {
				removePage(0);
			}

			if (viewer == null) {
				this.elements = elements;
				createMainPage();
			} else {
				TableElement element = elements[0];
				if (isSameColumn(element)) {
					viewer.setInput(elements);
					TableColumn col = viewer.getTable().getColumn(0);
					col.pack();
					TableDefaultSortListener defaultSortListener = new TableDefaultSortListener(this, 0);
					col.removeSelectionListener(sortListener);
					col.addSelectionListener(defaultSortListener);
					viewer.getTable().getColumn(0).notifyListeners(SWT.Selection, null);
					col.removeSelectionListener(defaultSortListener);
					sortListener = new TableSortListener(this, 0);
					col.addSelectionListener(sortListener);

					
				} else {
					this.elements = elements;

					if (true) {
						if (getPageCount() > 0) {
							removePage(getPageCount() - 1);
						}
					}

					createMainPage();
				}
			}

			if (!isReload) {
				if(pager != null)
					pager.setPageNo(1);
				
				QueryViewEditorInput ei = (QueryViewEditorInput) getEditorInput();
				DbPlugin.showView(DbPluginConstant.VIEW_ID_SQLExecute, ei.getSecondarlyId());
			}

			setResponseTime(responseTime);

			int dispCnt = elements.length - 1;
			setTotalCount(dispCnt, -1); //$NON-NLS-1$

			QueryViewEditorInput input = (QueryViewEditorInput) getEditorInput();
			RecordCountForQueryJob job2 = new RecordCountForQueryJob(Transaction.getInstance(config), query, input.getSecondarlyId(), dispCnt);
			job2.setUser(false);
			job2.setPriority(RecordCountForQueryJob.LONG);
			job2.schedule();

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				getContributor().fillContextMenu(manager);
				setExtensionPoint(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private QueryViewerContributor getContributor() {
		IEditorActionBarContributor contributor = getEditorSite().getActionBarContributor();
		if (contributor instanceof QueryViewerContributor) {
			return (QueryViewerContributor) contributor;
		} else {
			return null;
		}
	}

	private void setHeaderColumn(Table table) {
		if (elements != null) {
			// TableColumn row = new TableColumn(table, SWT.LEFT);
			TableColumn row = new TableColumn(table, SWT.RIGHT);

			sortListener = new TableSortListener(this, 0);
			row.addSelectionListener(sortListener);
			row.pack();
			TableElement element = elements[0];
			zigen.plugin.db.core.TableColumn[] columns = element.getColumns();
			for (int i = 0; i < columns.length; i++) {
				zigen.plugin.db.core.TableColumn tColumn = columns[i];
				TableColumn col = new TableColumn(table, SWT.LEFT);
				col.setText(tColumn.getColumnName());
				col.addSelectionListener(new TableSortListener(this, i + 1));
				col.pack();
			}
		}
	}

	private int max_column_size = 600;

	private void columnsPack(Table table) {
		table.setVisible(false);
		TableColumn[] cols = table.getColumns();
		for (int i = 0; i < cols.length; i++) {
			TableColumn c = cols[i];
			c.pack();

			if(c.getWidth() > max_column_size){
				c.setWidth(max_column_size);
			}
		}
		table.setVisible(true);
	}


	public void dispose() {
		disposeExtensionPoint();
		super.dispose();
	}

	public void doSave(IProgressMonitor monitor) {}

	public void doSaveAs() {}

	public boolean isSaveAsAllowed() {
		return false;
	}

	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		super.init(site, editorInput);
		try {

			if (editorInput instanceof QueryViewEditorInput) {
				QueryViewEditorInput input = (QueryViewEditorInput) editorInput;
				this.config = input.getConfig();
				this.query = input.getQuery();

				setPartName("[" + config.getDbName() + Messages.getString("QueryViewEditor2.17")); //$NON-NLS-1$ //$NON-NLS-2$
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
	}

	public TableViewer getViewer() {
		return viewer;
	}

	public ITable getTableNode() {
		return null;
	}

	public TableElement getHeaderTableElement() {
		if (this.elements.length > 0) {
			return elements[0];
		}
		return null;
	}

	protected void initializeViewerFont(ISourceViewer viewer) {
		StyledText styledText = viewer.getTextWidget();
		styledText.setFont(DbPlugin.getDefaultFont());
	}

	void setGlobalActionForEditor(IActionBars bars) {
		bars.clearGlobalActionHandlers();
		copyAction.refresh();
		bars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);
		bars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectAllRecordAction);
		bars.updateActionBars();

	}

	public void setFocus() {

		if (table != null && copyAction != null && selectAllRecordAction != null) {
			IActionBars bars = getEditorSite().getActionBars();
			bars.clearGlobalActionHandlers();
			setGlobalActionForEditor(bars);
			table.setFocus();
			setResponseTime(responseTime);
		}
	}

	private boolean isSameColumn(TableElement target) {
		if (this.elements == null)
			return false;

		TableElement old = this.elements[0];
		zigen.plugin.db.core.TableColumn[] oldCols = old.getColumns();
		zigen.plugin.db.core.TableColumn[] targetCols = target.getColumns();
		int oldLen = oldCols.length;
		int targetLen = targetCols.length;

		if (oldLen != targetLen)
			return false;

		for (int i = 0; i < oldCols.length; i++) {
			zigen.plugin.db.core.TableColumn column1 = oldCols[i];
			zigen.plugin.db.core.TableColumn column2 = targetCols[i];
			if (!column1.getColumnName().equals(column2.getColumnName())) {
				return false;
			}
		}
		return true;
	}

	public IDBConfig getDBConfig() {
		return config;
	}

	public String getQuery() {
		return query;
	}

	public void changeColumnColor(Column column) {
	// changeColorJob.setSelectedColumn(column);
	// changeColorJob.schedule();

	}

	public void changeColumnColor() {
		changeColorJob.setTable(table);
		changeColorJob.schedule();
	}

	public void editTableElement(Object element, int column) {
		throw new UnsupportedOperationException("The method is a unmounting."); //$NON-NLS-1$
	}

	public String getCondition() {
		throw new UnsupportedOperationException("The method is a unmounting. "); //$NON-NLS-1$
	}

	private List extensionList = new ArrayList();

	private void setExtensionPoint(IMenuManager manager) {

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(DbPlugin.getDefault().getBundle().getSymbolicName() + ".queryEditor");
		IExtension[] extensions = point.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] elements = extensions[i].getConfigurationElements();
			add(manager, elements);
		}

	}

	private void add(IMenuManager menu, IConfigurationElement[] elems) {

		try {
			for (int k = 0; k < elems.length; k++) {

				IConfigurationElement element = elems[k];
				String name = element.getName();

				if ("contributor".equals(name)) {
					try {
						ITableViewEditorAction action = (ITableViewEditorAction) element.createExecutableExtension("class");
						action.setText(element.getAttribute("label"));
						action.setToolTipText(element.getAttribute("tooltipText"));
						action.setActiveEditor(this);
						action.selectionChanged(viewer.getSelection());
						extensionList.add(action);

						String menubarPath = element.getAttribute("menubarPath");
						IMenuManager subMenu = menu.findMenuUsingPath(menubarPath);

						if (subMenu != null) {
							subMenu.add(action);
							add(subMenu, element.getChildren());
						} else {
							IContributionItem item = menu.findUsingPath(menubarPath);
							if (item != null) {
								if (item instanceof Separator) {
									Separator sep = (Separator) item;
									IContributionManager mgr = sep.getParent();
									mgr.add(action);
									add(subMenu, element.getChildren());
								} else if (item instanceof GroupMarker) {
									GroupMarker sep = (GroupMarker) item;
									IContributionManager mgr = sep.getParent();
									mgr.add(action);
									add(subMenu, element.getChildren());
								} else {
									DbPlugin.log("unexpected Type " + item.getClass().getName());
								}
							}
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else if ("menu".equals(name)) {
					String _id = element.getAttribute("id");
					String _label = element.getAttribute("label");
					IMenuManager subMenu = menu.findMenuUsingPath(_id);
					if (subMenu == null) {
						subMenu = new MenuManager(_label, _id);
						menu.add(subMenu);
						add(subMenu, element.getChildren());
					}

				} else if ("separator".equals(name)) {
					String _name = element.getAttribute("name");
					menu.add(new Separator(_name));

				} else if ("groupMarker".equals(name)) {
					String _name = element.getAttribute("name");
					menu.add(new GroupMarker(_name));

				}

			}
		} catch (Exception e) {
			DbPlugin.log(e);
		}
	}

	private void disposeExtensionPoint() {
		for (Iterator iter = extensionList.iterator(); iter.hasNext();) {
			ITableViewEditorAction action = (ITableViewEditorAction) iter.next();
			action.setActiveEditor(null);
			action = null;
		}
	}


	public void setEnabled(boolean enabled) {
		;
	}

	public int getRecordLimit() {
		return 0;
	}

	public int getRecordOffset() {
		return 0;
	}
}
