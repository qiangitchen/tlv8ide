package com.tulin.v8.ide.views.navigator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.PerformanceStats;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jdt.internal.ui.viewsupport.IViewPartInputProvider;
import org.eclipse.jdt.ui.IPackagesViewPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.ui.part.IShowInTarget;
import org.eclipse.ui.part.ShowInContext;

import com.tulin.v8.core.Sys;
import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.ide.EditorUtility;
import com.tulin.v8.ide.PluginSettingsManager;
import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.internal.FlowDraw;
import com.tulin.v8.ide.internal.TreeContentProvider;
import com.tulin.v8.ide.utils.SelectionUtil;
import com.tulin.v8.ide.views.navigator.action.CollapseAllAction;
import com.tulin.v8.ide.views.navigator.action.DeleteObjAction;
import com.tulin.v8.ide.views.navigator.action.NewFlowDrawAction;
import com.tulin.v8.ide.views.navigator.action.NewFlowFolderAction;
import com.tulin.v8.ide.views.navigator.action.NewTableAction;
import com.tulin.v8.ide.views.navigator.action.OpenFileAction;
import com.tulin.v8.ide.views.navigator.action.OpenFlowDrawAction;
import com.tulin.v8.ide.views.navigator.action.OpenTableViewFileAction;
import com.tulin.v8.ide.views.navigator.action.PropertyViewerAction;
import com.tulin.v8.ide.views.navigator.action.RePropertyFlowDrawAction;
import com.tulin.v8.ide.views.navigator.action.RefreshAction;
import com.tulin.v8.ide.views.navigator.action.RenameAction;
import com.tulin.v8.ide.views.navigator.action.ToggleLinkingAction;

import zigen.plugin.db.ext.oracle.internal.OpenSourceEdirotAction;
import zigen.plugin.db.ui.actions.CloseDBAction;
import zigen.plugin.db.ui.actions.ConnectDBAction;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.OracleSequence;
import zigen.plugin.db.ui.internal.OracleSource;
import zigen.plugin.db.ui.internal.Table;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.jobs.RefreshOracleSourceJob;
import zigen.plugin.db.ui.jobs.RefreshTableJob;
import zigen.plugin.db.ui.views.TreeView;

/**
 * 模型视图
 */

@SuppressWarnings("restriction")
public class ModelView extends TreeView implements IStatusChangeListener, IViewPartInputProvider, IPackagesViewPart,
		IMenuListener, IPropertyChangeListener, ISetSelectionTarget, IShowInTarget {
	public static final String ID = "com.tulin.v8.ide.navigator.views.modelview";
	protected PluginSettingsManager settringMgr = StudioPlugin.getDefault().getPluginSettingsManager();
	public TreeViewer viewer;
	private CollapseAllAction collapseAllAction;
	private ToggleLinkingAction toggleLinkingAction;
	private ConnectDBAction connectDBAction;
	private CloseDBAction closeDBAction;
	private Action doubleClickAction;
	private OpenFileAction openAction;
	private OpenTableViewFileAction opentableviewfileaction;
	private OpenFlowDrawAction openflowdrawaction;
	private RefreshAction actionRefresh;
	private NewFlowFolderAction newflowfolderaction;
	private NewFlowDrawAction newflowdrawaction;
	private NewTableAction newTableAction;
	private RenameAction reNameAction;
	private DeleteObjAction DeleteAction;

	public ModelView() {
		this.fDialogSettings = StudioPlugin.getDefault().getDialogSettingsSection(getClass().getName());
		this.fLinkingEnabled = this.fDialogSettings.getBoolean("linkWithEditor");
	}

	@Override
	public void createPartControl(Composite paramComposite) {
		PerformanceStats localPerformanceStats = PerformanceStats
				.getStats("org.eclipse.jdt.ui/perf/explorer/createPartControl", this);
		localPerformanceStats.startRun();

		Composite main = new Composite(paramComposite, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		main.setLayoutData(gridData);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 2;
		main.setLayout(gridLayout);

		createTreeArea(main);

		makeActions();
		hookContextMenu();
		hookDoubleClickAction();

		contributeToActionBars();
		// setGlobalAction(getViewSite().getActionBars());

		StudioPlugin.addStatusChangeListener(this);
		JavaPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(this);

		this.viewer.addTreeListener(this.fExpansionListener);
		setLinkingEnabled(isLinkingEnabled());

		localPerformanceStats.endRun();
	}

	public static ModelView getFromActivePerspective() {
		IWorkbenchPage activePage = JavaPlugin.getActivePage();
		if (activePage == null)
			return null;
		IViewPart view = activePage.findView(ID);
		if ((view instanceof PackageExplorerPart))
			return (ModelView) view;
		return null;
	}

	public static ModelView openInActivePerspective() {
		try {
			return (ModelView) JavaPlugin.getActivePage().showView(ID);
		} catch (PartInitException localPartInitException) {
		}
		return null;
	}

	protected TreeContentProvider contentProvider;

	private boolean fLinkingEnabled;

	private IDialogSettings fDialogSettings;

	private IPartListener2 fLinkWithEditorListener = new IPartListener2() {
		public void partVisible(IWorkbenchPartReference partRef) {
		}

		public void partBroughtToTop(IWorkbenchPartReference partRef) {
		}

		public void partClosed(IWorkbenchPartReference partRef) {
		}

		public void partDeactivated(IWorkbenchPartReference partRef) {
		}

		public void partHidden(IWorkbenchPartReference partRef) {
		}

		public void partOpened(IWorkbenchPartReference partRef) {
		}

		public void partInputChanged(IWorkbenchPartReference partRef) {
			if ((partRef instanceof IEditorReference))
				ModelView.this.editorActivated(((IEditorReference) partRef).getEditor(true));
		}

		public void partActivated(IWorkbenchPartReference partRef) {
			if ((partRef instanceof IEditorReference))
				ModelView.this.editorActivated(((IEditorReference) partRef).getEditor(true));
		}
	};
	private OpenSourceEdirotAction openEdirotForSourceAction;

	protected void createTreeArea(Composite parent) {
		Composite body = new Composite(parent, SWT.NONE);
		body.setLayoutData(new GridData(GridData.FILL_BOTH));
		body.setLayout(new FillLayout());

		viewer = new TreeViewer(body, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

		int dragOption = DND.DROP_DEFAULT | DND.DROP_MOVE | DND.DROP_COPY;
		Transfer[] transfers = new Transfer[] { TreeLeafListTransfer.getInstance() };
		viewer.addDragSupport(dragOption, transfers, new DragBookmarkAdapter(viewer));

		contentProvider = new TreeContentProvider(viewer);
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(new TreeLabelProvider());
		viewer.setComparator(new TreeViewSorter());// 排序新方法
		// viewer.setSorter(new TreeViewSorter());//排序老方法
		viewer.setUseHashlookup(true);

		viewer.setInput(getViewSite());

		viewer.addTreeListener(new TreeViewListener());

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				selectionChangeHandler(event);
			}
		});

		getSite().setSelectionProvider(viewer);
	}

	protected void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				JavaPlugin.createStandardGroups(manager);
				ModelView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getTree());
		viewer.getTree().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	protected void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	protected void fillLocalPullDown(IMenuManager manager) {
		manager.add(openAction);
		manager.add(new Separator());
		manager.add(reNameAction);
		manager.add(DeleteAction);
	}

	protected void fillContextMenu(IMenuManager manager) {
		Object obj = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
		manager.removeAll();
		if (obj == null)
			return;
		if (obj instanceof OracleSource || obj instanceof OracleSequence) {
			manager.add(openEdirotForSourceAction);
		} else if (obj instanceof DataBase) {
			DataBase db = (DataBase) obj;
			manager.add(connectDBAction);
			manager.add(closeDBAction);
			manager.add(new Separator());
			if (db.isEnabled()) {
				if (db.isConnected()) {
					connectDBAction.setEnabled(false);
					closeDBAction.setEnabled(true);
				} else {
					connectDBAction.setEnabled(true);
					closeDBAction.setEnabled(false);
				}
			} else {
				connectDBAction.setEnabled(false);
				closeDBAction.setEnabled(false);
			}
		} else if (obj instanceof ITable) {
			opentableviewfileaction = new OpenTableViewFileAction(this.viewer);
			Action openaction = new Action() {
				public void run() {
					opentableviewfileaction.run();
				}
			};
			openaction.setText(Messages.getString("ModelView.opentableview.1"));
			manager.add(openaction);
			MenuManager openmenu = new MenuManager(Messages.getString("ModelView.opentableview.2"));
			manager.add(openmenu);
			openmenu.add(opentableviewfileaction);
			// 可以删除表和视图 View是Table的子类
			if (obj instanceof Table) {
				manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
				manager.add(DeleteAction);
				DeleteAction.setEnabled(true);
			}
		} else if (obj instanceof FlowDraw) {
			openflowdrawaction = new OpenFlowDrawAction(this.viewer);
			Action openaction = new Action() {
				public void run() {
					openflowdrawaction.run();
				}
			};
			openaction.setText(Messages.getString("ModelView.opentableview.1"));
			manager.add(openaction);
			MenuManager openmenu = new MenuManager(Messages.getString("ModelView.opentableview.2"));
			manager.add(openmenu);
			openmenu.add(openflowdrawaction);

			manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
			manager.add(DeleteAction);
			DeleteAction.setEnabled(true);
			manager.add(reNameAction);
			manager.add(new RePropertyFlowDrawAction(viewer));
		} else if (obj instanceof TreeNode) {
			TreeNode tr = (TreeNode) obj;
			MenuManager newmenu = new MenuManager(Messages.getString("ModelView.newTableAction.new"));
			manager.add(newmenu);
			if ("process".equals(tr.getTvtype())) {
				newmenu.add(newflowfolderaction);
			} else if ("flowfolder".equals(tr.getTvtype())) {
				newmenu.add(newflowfolderaction);
				newmenu.add(newflowdrawaction);
			} else if ("dbtype".equals(tr.getTvtype())) {
				newmenu.add(newTableAction);
				if ("TABLE".equals(tr.getName())) {
					newTableAction.setText(Messages.getString("ModelView.newTableAction.table"));
					newTableAction
							.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("table.gif")));
				} else {
					newTableAction.setText(Messages.getString("ModelView.newTableAction.view"));
					newTableAction
							.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("view.gif")));
				}
			}
			if ("file".equals(tr.getTvtype())) {
				Action openaction = new Action() {
					public void run() {
						openAction.run();
					}
				};
				openaction.setText(Messages.getString("ModelView.opentableview.1"));
				manager.add(openaction);
			}
			MenuManager openmenu = new MenuManager(Messages.getString("ModelView.opentableview.2"));
			manager.add(openmenu);
			if (tr.getTvtype() != null && tr.getBiz() == null) {
				manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
				manager.add(DeleteAction);
				if ("flowfolder".equals(tr.getTvtype())) {
					if (tr.hasChildren()) {
						DeleteAction.setEnabled(false);
					} else {
						DeleteAction.setEnabled(true);
					}
				} else if (!"table".equals(tr.getTvtype()) && !"view".equals(tr.getTvtype())) {
					DeleteAction.setEnabled(false);
				} else {
					DeleteAction.setEnabled(true);
				}
			}
			if ("folder".equals(tr.getTvtype()) || "file".equals(tr.getTvtype())) {
				manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
				manager.add(DeleteAction);
			}
			if (tr.getBiz() != null || "table".equals(tr.getTvtype()) || "view".equals(tr.getTvtype())
					|| "flowfolder".equals(tr.getTvtype()) || "flowdraw".equals(tr.getTvtype())) {
				manager.add(reNameAction);
			}
		} else {
			manager.add(openAction);
			manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
			manager.add(DeleteAction);
			DeleteAction.setEnabled(true);
		}
		// 数据源没有刷新按钮
		if (!(obj instanceof DataBase)) {
			manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
			manager.add(actionRefresh);
		}
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		PropertyViewerAction propertyvieweraction = new PropertyViewerAction(viewer);
		manager.add(propertyvieweraction);
		manager.add(new Separator("additions"));
		manager.add(new Separator("additions-end"));
	}

	protected void fillLocalToolBar(IToolBarManager manager) {
		manager.add(new Separator());
		manager.add(collapseAllAction);
		manager.add(toggleLinkingAction);
	}

	protected void makeActions() {
		connectDBAction = new ConnectDBAction(viewer);
		closeDBAction = new CloseDBAction(viewer);
		openAction = new OpenFileAction(viewer);
		newflowfolderaction = new NewFlowFolderAction(this.viewer);
		newflowdrawaction = new NewFlowDrawAction(this.viewer);
		reNameAction = new RenameAction(this.viewer);
		DeleteAction = new DeleteObjAction(this.viewer);
		actionRefresh = new RefreshAction(this.viewer);
		openEdirotForSourceAction = new OpenSourceEdirotAction(viewer);
		doubleClickAction = new Action() {
			public void run() {
				openAction.run();
			}
		};
		newTableAction = new NewTableAction(this, viewer);
		collapseAllAction = new CollapseAllAction(viewer);
		toggleLinkingAction = new ToggleLinkingAction(this);
	}

	protected void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				try {
					doubleClickAction.run();
				} catch (Exception e) {
					showMessage(e.toString());
					e.printStackTrace();
				}
			}
		});
	}

	public void showMessage(String message) {
		MessageDialog.openInformation(StudioPlugin.getShell(), StudioPlugin.getResourceString("perspective.title.0"),
				message);
	}

	public void statusChanged(Object obj, int status) {
		if (status == IStatusChangeListener.EVT_ModifyTableDefine) {
			if (obj instanceof ITable) {
				ITable table = (ITable) obj;
				RefreshTableJob job = new RefreshTableJob(viewer, table);
				job.setPriority(RefreshTableJob.SHORT);
				job.setUser(true);
				job.schedule();
			}
		} else if (status == IStatusChangeListener.EVT_LinkTable) {
			if (isLinkingEnabled()) {
				if (obj instanceof ISelection) {
					ISelection selection = (ISelection) obj;
					viewer.setSelection(selection, true);
				}
			}
		} else if (status == IStatusChangeListener.EVT_RefreshOracleSource) {
			if (obj instanceof OracleSource) {
				OracleSource source = (OracleSource) obj;
				RefreshOracleSourceJob job = new RefreshOracleSourceJob(viewer, source);
				job.setPriority(RefreshOracleSourceJob.SHORT);
				job.setUser(true);
				job.schedule();
			}
		}
	}

	protected void selectionChangeHandler(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		if (isLinkingEnabled())
			linkToEditor(selection);
	}

	public TreeViewer getTreeViewer() {
		return this.viewer;
	}

	protected IStatusLineManager getIStatusLineManager() {
		IViewSite vieweSite = super.getViewSite();
		IActionBars actionBars = vieweSite.getActionBars();
		return actionBars.getStatusLineManager();
	}

	public boolean isLinkingEnabled() {
		return this.fLinkingEnabled;
	}

	public void setLinkingEnabled(boolean enabled) {
		this.fLinkingEnabled = enabled;
		saveDialogSettings();
		IWorkbenchPage page = getSite().getPage();
		if (enabled) {
			page.addPartListener(this.fLinkWithEditorListener);
			IEditorPart editor = page.getActiveEditor();
			if (editor != null)
				editorActivated(editor);
		} else {
			page.removePartListener(this.fLinkWithEditorListener);
		}
	}

	private void saveDialogSettings() {
		this.fDialogSettings.put("linkWithEditor", this.fLinkingEnabled);
	}

	public void dispose() {
		StudioPlugin.removeStatusChangeListener(this);
		JavaPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(this);
		if (this.viewer != null) {
			this.viewer.removeTreeListener(this.fExpansionListener);
		}
	}

	@Override
	public void setFocus() {
		viewer.getTree().setFocus();

	}

	@Override
	public void selectAndReveal(Object arg0) {
		Sys.printMsg("selectAndReveal");
	}

	public void selectReveal(ISelection selection) {
		Control ctrl = getTreeViewer().getControl();
		if ((ctrl == null) || (ctrl.isDisposed())) {
			return;
		}
		this.contentProvider.runPendingUpdates();
		this.viewer.setSelection(convertSelection(selection), true);
	}

	public ISelection convertSelection(ISelection s) {
		if (!(s instanceof IStructuredSelection)) {
			return s;
		}
		Object[] elements = ((IStructuredSelection) s).toArray();

		boolean changed = false;
		for (int i = 0; i < elements.length; i++) {
			Object convertedElement = convertElement(elements[i]);
			changed = (changed) || (convertedElement != elements[i]);
			elements[i] = convertedElement;
		}
		if (changed) {
			return new StructuredSelection(elements);
		}
		return s;
	}

	private Object convertElement(Object original) {
		if ((original instanceof IJavaElement)) {
			if ((original instanceof ICompilationUnit)) {
				ICompilationUnit cu = (ICompilationUnit) original;
				IJavaProject javaProject = cu.getJavaProject();
				if ((javaProject != null) && (javaProject.exists()) && (!javaProject.isOnClasspath(cu))) {
					IResource resource = cu.getResource();
					if (resource != null) {
						return resource;
					}
				}
			}
			return original;
		}
		if ((original instanceof IResource)) {
			IJavaElement je = JavaCore.create((IResource) original);
			if ((je != null) && (je.exists())) {
				IJavaProject javaProject = je.getJavaProject();
				if ((javaProject != null) && (javaProject.exists()))
					return je;
			}
		} else if ((original instanceof IAdaptable)) {
			IAdaptable adaptable = (IAdaptable) original;
			IJavaElement je = (IJavaElement) adaptable.getAdapter(IJavaElement.class);
			if ((je != null) && (je.exists())) {
				return je;
			}
			IResource r = (IResource) adaptable.getAdapter(IResource.class);
			if (r != null) {
				je = JavaCore.create(r);
				if ((je != null) && (je.exists())) {
					return je;
				}
				return r;
			}
		}
		return original;
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {

	}

	@Override
	public Object getViewPartInput() {
		if (this.viewer != null)
			return this.viewer.getInput();
		return null;
	}

	public void collapseAll() {
		try {
			this.viewer.getControl().setRedraw(false);
			this.viewer.collapseToLevel(getViewPartInput(), -1);
		} finally {
			this.viewer.getControl().setRedraw(true);
		}
	}

	@Override
	public void menuAboutToShow(IMenuManager paramIMenuManager) {
		JavaPlugin.createStandardGroups(paramIMenuManager);
	}

	@Override
	public boolean show(ShowInContext context) {
		ISelection selection = context.getSelection();
		if ((selection instanceof IStructuredSelection)) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() == 1) {
				int res = tryToReveal(structuredSelection.getFirstElement());
				if (res == 0)
					return true;
				if (res == 8)
					return false;
			} else if (structuredSelection.size() > 1) {
				selectReveal(structuredSelection);
				return true;
			}
		}

		Object input = context.getInput();
		if ((input instanceof IEditorInput)) {
			Object elementOfInput = getInputFromEditor((IEditorInput) input);
			return (elementOfInput != null) && (tryToReveal(elementOfInput) == 0);
		}

		return false;
	}

	void editorActivated(IEditorPart editor) {
		IEditorInput editorInput = editor.getEditorInput();
		getCurrentItem(editorInput);
	}

	TreeItem getCurrentItem(IEditorInput editorinput) {
		String editPath = null;
		if (editorinput instanceof FileEditorInput) {
			editPath = ((FileEditorInput) editorinput).getPath().makeAbsolute().toString();
		} else if (editorinput instanceof FileStoreEditorInput) {
			editPath = ((FileStoreEditorInput) editorinput).getToolTipText();
		}
		if (editPath == null) {
			return null;
		}
		if (CommonUtil.isWinOS()) {
			editPath = editPath.replace("/", "\\");
		}
		TreeNode node = SelectionUtil.fiendItem(((TreeContentProvider) this.viewer.getContentProvider()).getRoot(),
				editPath);
		if (node != null) {
			showInput(node);
		}
		return null;
	}

	private Object getInputFromEditor(IEditorInput editorInput) {
		Object input = null;
		if (input == null) {
			input = editorInput.getAdapter(IFile.class);
		}
		if ((input == null) && ((editorInput instanceof IStorageEditorInput)))
			try {
				input = ((IStorageEditorInput) editorInput).getStorage();
			} catch (CoreException localCoreException) {
			}
		return input;
	}

	boolean showInput(Object input) {
		Object element = null;
		if (element == null) {
			element = input;
		}
		if (element != null) {
			ISelection newSelection = new StructuredSelection(element);
			if (this.viewer.getSelection().equals(newSelection)) {
				this.viewer.reveal(element);
			} else {
				this.viewer.setSelection(newSelection, true);
			}
			return true;
		}
		return false;
	}

	public int tryToReveal(Object paramObject) {
		if (b(paramObject))
			return 0;
		return 8;
	}

	private boolean b(Object paramObject) {
		if (cs(paramObject))
			return true;
		paramObject = c(paramObject);
		if (paramObject != null) {
			if (cs(paramObject))
				return true;
			if ((paramObject instanceof IJavaElement)) {
				IResource localIResource = ((IJavaElement) paramObject).getResource();
				if ((localIResource != null) && (cs(localIResource)))
					return true;
			}
		}
		return false;
	}

	private Object c(Object paramObject) {
		if (paramObject == null)
			return null;
		if (!(paramObject instanceof IJavaElement))
			return paramObject;
		IJavaElement localIJavaElement = (IJavaElement) paramObject;
		switch (localIJavaElement.getElementType()) {
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
			localIJavaElement = (IJavaElement) localIJavaElement.getOpenable();
			break;
		case 1:
			localIJavaElement = null;
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		}
		return localIJavaElement;
	}

	private boolean cs(Object paramObject) {
		if (paramObject == null)
			return false;
		selectReveal(new StructuredSelection(paramObject));
		return !getSite().getSelectionProvider().getSelection().isEmpty();
	}

	private ITreeViewerListener fExpansionListener = new TreeViewListener();

	private void linkToEditor(ISelection selection) {
		Object obj = SelectionUtil.getSingleElement(selection);
		if (obj != null) {
			IEditorPart part = EditorUtility.isOpenInEditor(obj);
			if (part != null) {
				IWorkbenchPage page = getSite().getPage();
				page.bringToTop(part);
				if ((obj instanceof IJavaElement))
					EditorUtility.revealInEditor(part, (IJavaElement) obj);
			}
		}
	}

}