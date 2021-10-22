/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.sql;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.CursorLinePainter;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.text.IFindReplaceTargetExtension;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewerExtension2;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.MatchingCharacterPainter;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.editors.text.TextEditor;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ext.oracle.internal.OracleSourceDetailInfo;
import zigen.plugin.db.ext.oracle.internal.OracleSourceErrorInfo;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.actions.GlobalAction;
import zigen.plugin.db.ui.actions.OpenSQLAction;
import zigen.plugin.db.ui.actions.SaveSQLAction;
import zigen.plugin.db.ui.internal.Root;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.util.LineNumberRulerColumnUtil;
import zigen.plugin.db.ui.util.StyledTextUtil;
import zigen.plugin.db.ui.views.ISQLOperationTarget;
import zigen.plugin.db.ui.views.internal.ColorManager;
import zigen.plugin.db.ui.views.internal.PLSQLCodeConfiguration;
import zigen.plugin.db.ui.views.internal.PLSQLSourceViewer;
import zigen.plugin.db.ui.views.internal.SQLCharacterPairMatcher;
import zigen.plugin.db.ui.views.internal.SQLOutinePage;
import zigen.plugin.db.ui.views.internal.SQLPartitionScanner;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

public class SourceEditor extends TextEditor implements IPlsqlEditor, IPropertyChangeListener {

	public static final String ID = "zigen.plugin.db.ui.editors.sql.SourceEditor"; //$NON-NLS-1$

	protected IDBConfig config;

	protected PLSQLSourceViewer sourceViewer;

	protected PLSQLCodeConfiguration sqlConfiguration;

	protected OracleSourceDetailInfo sourceDetailInfo;

	protected OracleSourceErrorInfo[] sourceErrorInfos;

	protected ColorManager colorManager;

	protected LineNumberRulerColumn rulerCol;

	protected IPreferenceStore store;

	protected boolean dirty = false;

	protected TreeViewer errorViewer;

	protected SashForm sash;

	protected CursorLinePainter cpainter;

	protected MatchingCharacterPainter painter;

	protected Color fFindScopeHighlightColor;

	GlobalAction execScriptAction = new GlobalAction(null, ISQLOperationTarget.SCRIPT_EXECUTE);

	OpenSQLAction openSQLAction = new OpenSQLAction(null);

	SaveSQLAction saveSQLAction = new SaveSQLAction(null);

	CoolBar coolBar;

	ProjectionSupport projectionSupport;

	Document document;

	public SourceEditor() {
		super();

	}

	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		super.init(site, editorInput);

		if (editorInput instanceof SourceEditorInput) {
			SourceEditorInput input = (SourceEditorInput) editorInput;
			this.sourceDetailInfo = input.getSourceDetailInfo();
			this.sourceErrorInfos = input.getSourceErrorInfos();
			config = input.getConfig();
			setPartName(input.getName());
			colorManager = new ColorManager();
			sqlConfiguration = new PLSQLCodeConfiguration(colorManager);
			setSourceViewerConfiguration(sqlConfiguration);
			this.store = DbPlugin.getDefault().getPreferenceStore();
			this.store.addPropertyChangeListener(this);
			// DbPlugin.addStatusChangeListener(this);
			// PropertyPageChangeListener.addPropertyPageChangeListener(this);
			document = (Document) getDocumentProvider().getDocument(editorInput);
		}

	}

	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		sourceViewer = (PLSQLSourceViewer) getSourceViewer();
		String demiliter = DbPlugin.getDefault().getPreferenceStore().getString(SQLEditorPreferencePage.P_SQL_DEMILITER);

		IDocument doc = sourceViewer.getDocument();
		IDocumentPartitioner partitioner = new FastPartitioner(new SQLPartitionScanner(), new String[] {SQLPartitionScanner.SQL_STRING, SQLPartitionScanner.SQL_COMMENT});
		partitioner.connect(doc);
		doc.setDocumentPartitioner(partitioner);

		ITextViewerExtension2 extension = (ITextViewerExtension2) sourceViewer;
		MatchingCharacterPainter painter = new MatchingCharacterPainter(sourceViewer, new SQLCharacterPairMatcher());
		painter.setColor(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_MATCHING));
		extension.addPainter(painter);

		ProjectionViewer pviewer = (ProjectionViewer) getSourceViewer();
		projectionSupport = new ProjectionSupport(pviewer, getAnnotationAccess(), getSharedColors());
		projectionSupport.install();
		pviewer.doOperation(ProjectionViewer.TOGGLE);

		StyledTextUtil.changeColor(colorManager, sourceViewer.getTextWidget());

		hookContextMenu();

	}

	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {

		// FormLayout layout = new FormLayout();
		// parent.setLayout(layout);

		Composite content = new Composite(parent, SWT.NONE);
		FormLayout layout = new FormLayout();
		content.setLayout(layout);
		//
		createToolbarPart(content);

		sash = new SashForm(content, SWT.VERTICAL | SWT.NONE);
		FormData data = new FormData();
		data.top = new FormAttachment(coolBar, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		sash.setLayoutData(data);

		/*
		 * if (ruler instanceof CompositeRuler) { LineNumberRulerColumn rulerCol = new LineNumberRulerColumn(); LineNumberRulerColumnUtil.changeColor(colorManager, rulerCol);
		 * ((CompositeRuler)ruler).addDecorator(0, rulerCol); }
		 */
		// fAnnotationAccess = getAnnotationAccess();
		// fOverviewRuler = createOverviewRuler(getSharedColors());
		sourceViewer = new PLSQLSourceViewer(sash, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
		getSourceViewerDecorationSupport(sourceViewer);

		data = new FormData();
		data.top = new FormAttachment(100, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		sourceViewer.setPlsqlEditor(this);
		initializeViewerFont(sourceViewer);
		errorViewer = new TreeViewer(sash, SWT.MULTI | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		data = new FormData();
		data.top = new FormAttachment(100, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		errorViewer.getControl().setLayoutData(data);

		errorViewer.getTree().setLinesVisible(true);
		ErrorContentProvider cp = new ErrorContentProvider();

		errorViewer.setContentProvider(cp);
		errorViewer.setLabelProvider(new ErrorLabelProvider());
		errorViewer.setInput(sourceErrorInfos);
		errorViewer.expandAll();

		sash.setWeights(new int[] {80, 20});

		hookContextMenu();

		setSQLViewerToAction(sourceViewer);

		return sourceViewer;
	}

	protected void initializeViewerFont(ISourceViewer viewer) {
		StyledText styledText = viewer.getTextWidget();
		styledText.setFont(DbPlugin.getDefaultFont());
	}

	public void createToolbarPart(final Composite parent) {
		// coolBar = new CoolBar(parent, SWT.FLAT);
		coolBar = new CoolBar(parent, SWT.NONE);


		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		coolBar.setLayoutData(data);

		CoolBarManager coolBarMgr = new CoolBarManager(coolBar);

		// GridData gid = new GridData();
		// gid.horizontalAlignment = GridData.FILL;
		// coolBar.setLayoutData(gid);

		ToolBarManager toolBarMgr1 = new ToolBarManager(SWT.FLAT);
		toolBarMgr1.add(execScriptAction);

		ToolBarManager toolBarMgr2 = new ToolBarManager(SWT.FLAT);
		toolBarMgr2.add(openSQLAction);
		toolBarMgr2.add(saveSQLAction);

		coolBarMgr.add(new ToolBarContributionItem(toolBarMgr2));
		coolBarMgr.add(new ToolBarContributionItem(toolBarMgr1));
		coolBarMgr.update(true);

		coolBar.addControlListener(new ControlListener() {

			public void controlMoved(ControlEvent e) {}

			public void controlResized(ControlEvent e) {
				parent.getParent().layout(true);
				parent.layout(true);
			}
		});
	}

	private void setSQLViewerToAction(SQLSourceViewer viewer) {
		execScriptAction.setTextViewer(viewer);
		openSQLAction.setSQLSourceViewer(viewer);
		saveSQLAction.setSQLSourceViewer(viewer);
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				getContributor().fillContextMenu(manager);
			}
		});
		StyledText text = sourceViewer.getTextWidget();
		Menu menu = menuMgr.createContextMenu(text);
		text.setMenu(menu);
		getSite().registerContextMenu(menuMgr, sourceViewer);
	}

	private SourceEditorContributor getContributor() {
		IEditorActionBarContributor contributor = getEditorSite().getActionBarContributor();
		if (contributor instanceof SourceEditorContributor) {
			return (SourceEditorContributor) contributor;
		} else {
			return null;
		}
	}

	public String getEditingSource() {
		return getSourceViewer().getDocument().get();
	}

	protected IStatusLineManager getIStatusLineManager() {
		IEditorSite site = super.getEditorSite();
		IActionBars actionBars = site.getActionBars();
		return actionBars.getStatusLineManager();
	}

	// private boolean hasContributionItem(IContributionItem[] items, String
	// targetId) {
	// for (int i = 0; i < items.length; i++) {
	// IContributionItem item = items[i];
	// if (targetId.equals(item.getId())) {
	// return true;
	// }
	// }
	// return false;
	// }

	// public void contributeToStatusLine() {
	// IStatusLineManager manager = getIStatusLineManager();
	// IContributionItem[] items = manager.getItems();
	// String commitMode = Messages.getString("SqlEditor.1"); //$NON-NLS-1$
	// if (!hasContributionItem(items, commitMode)) {
	// commitModeItem = new StatusLineContributionItem(commitMode);
	// commitModeItem.setText(Messages.getString("SqlEditor.2")); //$NON-NLS-1$
	// manager.add(commitModeItem);
	// }
	// }

	public void dispose() {
		super.dispose();
		colorManager.dispose();
		DbPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(this);

	}

	public void propertyChange(PropertyChangeEvent event) {
		if (sqlConfiguration != null && sourceViewer != null) {
			sqlConfiguration.updatePreferences(sourceViewer.getDocument());
			StyledTextUtil.changeColor(colorManager, sourceViewer.getTextWidget());
			// LineNumberRulerColumnUtil.changeColor(colorManager, rulerCol);
			sourceViewer.invalidateTextPresentation();

			cpainter.setHighlightColor(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_CURSOR_LINE));

			StyledTextUtil.changeColor(colorManager, sourceViewer.getTextWidget());
			LineNumberRulerColumnUtil.changeColor(colorManager, rulerCol);
			sqlConfiguration.updatePreferences(sourceViewer.getDocument());
			painter.setColor(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_MATCHING));
			cpainter.setHighlightColor(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_CURSOR_LINE));

			String property = event.getProperty();
			if (SQLEditorPreferencePage.P_COLOR_FIND_SCOPE.equals(property)) {
				initializeFindScopeColor(sourceViewer);
			}

			sourceViewer.invalidateTextPresentation();

		}
	}

	private void initializeFindScopeColor(ISourceViewer viewer) {
		IPreferenceStore store = DbPlugin.getDefault().getPreferenceStore();
		if (store != null) {
			// StyledText styledText= viewer.getTextWidget();
			Color color = colorManager.getColor(SQLEditorPreferencePage.P_COLOR_FIND_SCOPE);
			IFindReplaceTarget target = viewer.getFindReplaceTarget();
			if (target != null && target instanceof IFindReplaceTargetExtension)
				((IFindReplaceTargetExtension) target).setScopeHighlightColor(color);

			if (fFindScopeHighlightColor != null)
				fFindScopeHighlightColor.dispose();

			fFindScopeHighlightColor = color;
		}
	}

	protected void setGlobalAction() {
		IActionBars actionbars = getEditorSite().getActionBars();
		actionbars.setGlobalActionHandler(ActionFactory.UNDO.getId(), new GlobalAction(sourceViewer, ITextOperationTarget.UNDO));
		actionbars.setGlobalActionHandler(ActionFactory.REDO.getId(), new GlobalAction(sourceViewer, ITextOperationTarget.REDO));
		actionbars.setGlobalActionHandler(ActionFactory.DELETE.getId(), new GlobalAction(sourceViewer, ITextOperationTarget.DELETE));
		actionbars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), new GlobalAction(sourceViewer, ITextOperationTarget.SELECT_ALL));
		actionbars.setGlobalActionHandler(ActionFactory.COPY.getId(), new GlobalAction(sourceViewer, ITextOperationTarget.COPY));
		actionbars.setGlobalActionHandler(ActionFactory.PASTE.getId(), new GlobalAction(sourceViewer, ITextOperationTarget.PASTE));
		actionbars.setGlobalActionHandler(ActionFactory.CUT.getId(), new GlobalAction(sourceViewer, ITextOperationTarget.CUT));
	}

	//
	// protected void setGlobalAction(IActionBars actionBars, String actionID,
	// IAction action) {
	// fGlobalActions.put(actionID, action);
	// actionBars.setGlobalActionHandler(actionID, action);
	// }

	public void setFocus() {
		setGlobalAction();
		// sqlViewer.updateOutlinePage();
	}

	public IDBConfig getConfig() {
		return config;
	}

	public PLSQLSourceViewer getPLSQLSourceViewer() {
		return sourceViewer;
	}

	SQLOutinePage outlinePage;

	public Object getAdapter(Class adapter) {
		if (projectionSupport != null) {
			Object obj = projectionSupport.getAdapter(getSourceViewer(), adapter);
			if (obj != null) {
				return obj;
			}
		}
		return super.getAdapter(adapter);
	}

	public void doSave(IProgressMonitor progressMonitor) {
		super.doSave(progressMonitor);
		saveSQLAction.run();
		// if(outlinePage != null)outlinePage.update();
		// updateFolding();
	}

	public void doSaveAs() {
		super.doSaveAs();
		saveSQLAction.run();
		// if(outlinePage != null)outlinePage.update();
		// updateFolding();
	}

	public void setError(OracleSourceErrorInfo[] OracleSourceErrorInfos) {
		errorViewer.setInput(OracleSourceErrorInfos);
		errorViewer.expandAll();
		// setDirtyMonde(false);
	}

	public void clearError() {
		errorViewer.setInput(null);
		// setDirtyMonde(false);
	}

	public boolean isDirty() {
		return false;
	}

	public boolean isSaveAsAllowed() {
		return false;
	}

	public class ErrorLabelProvider extends LabelProvider {

		ImageCacher ic = ImageCacher.getInstance();

		public String getText(Object obj) {
			return obj.toString();
		}

		public Image getImage(Object obj) {
			if (obj instanceof Root) {
				return ic.getImage(DbPlugin.IMG_CODE_ERROR_ROOT);
			} else {
				return ic.getImage(DbPlugin.IMG_CODE_ERROR);
			}
		}
	}

	private class ErrorContentProvider implements ITreeContentProvider {

		private Root invisibleRoot;

		private TreeViewer viewer;

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			this.viewer = (TreeViewer) v;
			this.invisibleRoot = new Root("invisible", true);
			if (newInput instanceof OracleSourceErrorInfo[]) {
				OracleSourceErrorInfo[] errors = (OracleSourceErrorInfo[]) newInput;
				Root root = new Root("Errors (" + errors.length + " items)");
				invisibleRoot.addChild(root);
				for (int i = 0; i < errors.length; i++) {
					OracleSourceErrorInfo err = errors[i];
					root.addChild(new TreeNode(err.getErrorText()));
				}
			}

		}

		public void dispose() {}

		public Object[] getElements(Object inputElement) {
			return getChildren(invisibleRoot);
		}

		public Object getParent(Object element) {
			if (element instanceof TreeLeaf) {
				return ((TreeLeaf) element).getParent();
			}
			return null;
		}

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof TreeNode) {
				return ((TreeNode) parentElement).getChildrens();
			}
			return new Object[0];
		}

		public boolean hasChildren(Object element) {
			if (element instanceof TreeNode)
				return ((TreeNode) element).hasChildren();
			return false;
		}

		public Root getInvisibleRoot() {
			return invisibleRoot;
		}
	}
}
