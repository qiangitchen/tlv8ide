/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.CursorLinePainter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.text.IFindReplaceTargetExtension;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewerExtension2;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.MatchingCharacterPainter;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.FindReplaceAction;
import org.eclipse.ui.texteditor.ITextEditorExtension2;
import org.eclipse.ui.texteditor.IUpdate;
import org.eclipse.ui.texteditor.IWorkbenchActionDefinitionIds;
import org.eclipse.ui.texteditor.ResourceMarkerAnnotationModel;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLHistoryManager;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ext.oracle.internal.ExplainForQueryAction;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.actions.GlobalAction;
import zigen.plugin.db.ui.actions.LockDataBaseAction;
import zigen.plugin.db.ui.actions.OpenViewAction;
import zigen.plugin.db.ui.actions.ShowHistoryViewAction;
import zigen.plugin.db.ui.bookmark.TreeLeafListTransfer;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.util.LineNumberRulerColumnUtil;
import zigen.plugin.db.ui.util.ProjectUtil;
import zigen.plugin.db.ui.util.ResourceUtil;
import zigen.plugin.db.ui.util.StyledTextUtil;
import zigen.plugin.db.ui.views.internal.ColorManager;
import zigen.plugin.db.ui.views.internal.SQLCharacterPairMatcher;
import zigen.plugin.db.ui.views.internal.SQLCodeConfiguration;
import zigen.plugin.db.ui.views.internal.SQLDocument;
import zigen.plugin.db.ui.views.internal.SQLOutinePage;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;
import zigen.plugin.db.ui.views.internal.SQLToolBar;

public class SQLExecuteView extends ViewPart implements ITextEditorExtension2, IPropertyChangeListener, ISelectionListener, IStatusChangeListener {

	//
	public static final String AUTO_COMMIT = Messages.getString("AbstractSQLExecuteView.0"); //$NON-NLS-1$

	public static final String MANUAL_COMMIT = Messages.getString("AbstractSQLExecuteView.1"); //$NON-NLS-1$

	protected StatusLineContributionItem commitModeItem;

	protected StatusLineContributionItem positionItem;

	// protected StatusLineContributionItem responseTimeItem;

	protected IDBConfig[] configs;

	protected SQLHistoryManager historyManager = DbPlugin.getDefault().getHistoryManager();

	// protected IDBConfig config;

	protected Transaction trans;

	protected SQLSourceViewer sqlViewer;

	protected LineNumberRulerColumn rulerCol;

	protected SQLCodeConfiguration sqlConfiguration;

	protected ColorManager colorManager = new ColorManager();

	protected ImageCacher ic = ImageCacher.getInstance();

	protected MatchingCharacterPainter painter;

	protected CursorLinePainter cpainter;

	protected ExplainForQueryAction explainForQueryAction;

	protected ShowHistoryViewAction showHistoryViewAction = new ShowHistoryViewAction();

	IAction currExecAction;

	IAction selectExecAction;

	OpenViewAction openViewAction;

	IAction changeSQLDemiliterAction;

	Composite sqlComposite;

	Composite footerComposite;

	SQLToolBar toolBar;


	public void setSqlText(String sql) {
		if (sqlViewer != null)
			sqlViewer.getDocument().set(sql);
	}

	public IDBConfig getConfig() {
		return toolBar.getConfig();
	}

	public SourceViewer getSqlViewer() {
		return sqlViewer;
	}

	public SQLHistoryManager getSQLHistoryManager() {
		return historyManager;
	}

	public void createContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(sqlViewer.getTextWidget());
		sqlViewer.getTextWidget().setMenu(menu);
		getSite().registerContextMenu(menuMgr, sqlViewer);
		toolBar.updateHistoryButton();
		contributeToActionBars();

	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(openViewAction);
		// manager.add(new Separator());
		manager.add(new LockDataBaseAction(sqlViewer));
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	protected void fillLocalToolBar(IToolBarManager manager) {
		manager.add(showHistoryViewAction);
		manager.add(openViewAction);
	}

	void fillContextMenu(IMenuManager manager) {
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.UNDO));
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.REDO));
		manager.add(new Separator());
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.CUT));
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.COPY));
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.PASTE));
		manager.add(new Separator());
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.DELETE));
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.LINE_DEL));
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.ALL_CLEAR));
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.SELECT_ALL));
		manager.add(new Separator("FIND")); //$NON-NLS-1$
		manager.add((IAction) fGlobalActions.get(ActionFactory.FIND.getId()));
		manager.add(new Separator());
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.ALL_EXECUTE));
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.CURRENT_EXECUTE));
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.SELECTED_EXECUTE));
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.SCRIPT_EXECUTE));
		manager.add(new Separator());
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.BACK_SQL));
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.NEXT_SQL));
		manager.add(new Separator());
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.COMMIT));
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.ROLLBACK));
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.FORMAT));
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.UNFORMAT));
		manager.add(new Separator());
		manager.add(explainForQueryAction);
		manager.add(new Separator());
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.COMMENT));
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(new Separator());
		// manager.add(openViewAction);

	}

	public void createPartControl(Composite parent) {
		Composite header = new Composite(parent, SWT.NONE);
		FormLayout layout = new FormLayout();
		header.setLayout(layout);

		toolBar = new SQLToolBar();
		toolBar.createPartControl(header);
		// createFooterPart(header);

		createSQLInputPart(header);
		makeActions();
		createContextMenu();
		contributeToStatusLine();
		// changeTransaction(config);
		// toolBar.updateCombo(config);

		initializeFindScopeColor(sqlViewer);

		DbPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(this);
		DbPlugin.addStatusChangeListener(this);
		// getSite().getPage().addPartListener(fPartListener);
		setExtensionPoint();

	}

	ResourceMarkerAnnotationModel annotationModel;

	protected void createSQLInputPart(Composite header) {
		sqlComposite = new Composite(header, SWT.NONE);
		sqlComposite.setLayout(new FillLayout());
		FormData data = new FormData();
		data.top = new FormAttachment(toolBar.getCoolBar(), 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		// data.bottom = new FormAttachment(footerComposite, -2);
		data.bottom = new FormAttachment(100, 0);
		sqlComposite.setLayoutData(data);

		CompositeRuler ruler = new CompositeRuler();
		rulerCol = new LineNumberRulerColumn();
		LineNumberRulerColumnUtil.changeColor(colorManager, rulerCol);
		ruler.addDecorator(0, rulerCol);
		sqlViewer = new SQLSourceViewer(sqlComposite, ruler, null, false, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);


		sqlViewer.setSecondaryId(getViewSite().getSecondaryId());
		sqlViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				selectionChangeHandler(event);
			}
		});
		initializeViewerFont(sqlViewer);

		DropTarget target = new DropTarget(sqlViewer.getTextWidget(), DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK);
		Transfer[] types = new Transfer[] {TreeLeafListTransfer.getInstance(), TextTransfer.getInstance(), FileTransfer.getInstance()};
		target.setTransfer(types);
		target.addDropListener(new DropTreeLeafAdapter(sqlViewer));
		sqlConfiguration = new SQLCodeConfiguration(colorManager);
		sqlViewer.configure(sqlConfiguration);

		sqlViewer.setDocument(new SQLDocument());

		// char[] pair = { '(', ')' };
		ITextViewerExtension2 extension = (ITextViewerExtension2) sqlViewer;
		painter = new MatchingCharacterPainter(sqlViewer, new SQLCharacterPairMatcher());
		painter.setColor(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_MATCHING));
		extension.addPainter(painter);

		cpainter = new CursorLinePainter(sqlViewer);
		cpainter.setHighlightColor(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_CURSOR_LINE));
		sqlViewer.addPainter(cpainter);


		getSite().setSelectionProvider(sqlViewer);
		getViewSite().getPage().addSelectionListener(this);

		StyledTextUtil.changeColor(colorManager, sqlViewer.getTextWidget());

		setGlobalAction();

		sqlViewer.getSelectionProvider().addSelectionChangedListener(selectionChangedListener);
		sqlViewer.addTextListener(textListener);

		sqlViewer.getTextWidget().addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent arg0) {
				updatePosition();

			}

		});

		sqlViewer.getTextWidget().addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent arg0) {
				try {
					updatePosition();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

		sqlViewer.setDbConfig(toolBar.getConfig());
		toolBar.setSQLSourceViewer(sqlViewer);

		// sqlViewer.getTextWidget().addKeyListener(new AutoAssistListener(getSecondarlyId()));
		// sqlViewer.getTextWidget().addMouseListener(new AutoAssistListener(getSecondarlyId()));

	}

	protected void initializeViewerFont(ISourceViewer viewer) {
		StyledText styledText = viewer.getTextWidget();
		styledText.setFont(DbPlugin.getDefaultFont());
	}

	public final void updateCombo(IDBConfig newConfig) {
		toolBar.updateCombo(newConfig);
	}

	void changeTitleImageAndCommitModeText(IDBConfig config) {
		try {
			if (config != null && toolBar != null && toolBar.getConfig() != null) {

				if (config.getDbName().equals(toolBar.getConfig().getDbName())) {
					trans = Transaction.getInstance(config);
					ic = ImageCacher.getInstance();
					if (trans.isConneting()) {
						super.setTitleImage(ic.getImage(DbPlugin.IMG_CODE_CONNECTED_DB));
					} else {
						super.setTitleImage(ic.getImage(DbPlugin.IMG_CODE_DB));
					}

					if (config.isAutoCommit()) {
						commitModeItem.setText(AUTO_COMMIT);
					} else {
						commitModeItem.setText(MANUAL_COMMIT);
					}
				}

			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

	public void dispose() {
		colorManager.dispose();
		getViewSite().getPage().removeSelectionListener(this);
		DbPlugin.removeStatusChangeListener(this);

		for (Iterator iter = extensionList.iterator(); iter.hasNext();) {
			ISelectionListener listener = (ISelectionListener) iter.next();
			getViewSite().getPage().removeSelectionListener(listener);
		}

		// getSite().getPage().removePartListener(fPartListener);

		fSelectionActions.clear();
		fGlobalActions.clear();

		sqlViewer.getSelectionProvider().removeSelectionChangedListener(selectionChangedListener);
		sqlViewer.removeTextListener(textListener);
		sqlViewer = null;

		super.dispose();
	}

	protected IStatusLineManager getIStatusLineManager() {
		IViewSite vieweSite = super.getViewSite();
		IActionBars actionBars = vieweSite.getActionBars();
		return actionBars.getStatusLineManager();

	}

	public void updatePosition() {
		StringBuffer sb = new StringBuffer();
		try {
			IDocument doc = getSqlViewer().getDocument();
			StyledText text = getSqlViewer().getTextWidget();
			int offset = text.getCaretOffset();
			int line = doc.getLineOfOffset(offset);
			sb.append(doc.getLineOfOffset(offset) + 1);
			sb.append(" : "); //$NON-NLS-1$
			sb.append(offset - doc.getLineOffset(line) + 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		positionItem.setText(sb.toString());
	}

	public void contributeToStatusLine() {
		IStatusLineManager manager = getIStatusLineManager();
		commitModeItem = new StatusLineContributionItem("CommitMode"); //$NON-NLS-1$
		commitModeItem.setText(MANUAL_COMMIT); //$NON-NLS-1$
		manager.add(commitModeItem);

		positionItem = new StatusLineContributionItem("Position"); //$NON-NLS-1$
		positionItem.setText(""); //$NON-NLS-1$
		manager.add(positionItem);
	}

	public void setStatusMessage(String message) {
		getIStatusLineManager().setMessage(message);
	}

	public void setStatusErrorMessage(String message) {
		getIStatusLineManager().setErrorMessage(message);
	}

	public void propertyChange(PropertyChangeEvent event) {
		if (sqlConfiguration != null && sqlViewer != null) {
			StyledTextUtil.changeColor(colorManager, sqlViewer.getTextWidget());
			LineNumberRulerColumnUtil.changeColor(colorManager, rulerCol);
			sqlConfiguration.updatePreferences(sqlViewer.getDocument());
			painter.setColor(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_MATCHING));
			cpainter.setHighlightColor(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_CURSOR_LINE));

			String property = event.getProperty();
			if (SQLEditorPreferencePage.P_COLOR_FIND_SCOPE.equals(property)) {
				initializeFindScopeColor(sqlViewer);
			}

			sqlViewer.invalidateTextPresentation();

		}
		if (event.getProperty().equals(PreferencePage.P_MAX_HISTORY)) {
			try {
				historyManager.removeOverHistory();
			} catch (IOException e) {
				DbPlugin.log(e);
			}
		}
	}


	public void setFocus() {
		if (sqlViewer != null) {
			DbPlugin.setSecondarlyId(getViewSite().getSecondaryId());
			sqlViewer.getControl().setFocus();
			// sqlViewer.updateOutlinePage();

			setGlobalAction();

			if (toolBar != null && toolBar.getConfig() != null) {
				changeTitleImageAndCommitModeText(toolBar.getConfig());
			}

		}
		IViewPart view = DbPlugin.findView(DbPluginConstant.VIEW_ID_HistoryView);
		if (showHistoryViewAction != null) {
			if (view != null) {
				showHistoryViewAction.setChecked(true);
			} else {
				showHistoryViewAction.setChecked(false);
			}
		}
		DbPlugin.fireStatusChangeListener(this, SWT.Selection);

	}

	public final void updateHistoryButton() {
		toolBar.updateHistoryButton();
	}

	public void setCommitMode(IDBConfig config, boolean autoCommit) {
		toolBar.setCommitMode(config, autoCommit);
	}

	protected void makeActions() {
		explainForQueryAction = new ExplainForQueryAction(sqlViewer);
		openViewAction = new OpenViewAction(this, getViewSite().getWorkbenchWindow());
		openViewAction.setDbConfig(getConfig());
	}

	protected Color fFindScopeHighlightColor;

	protected Map fGlobalActions = new HashMap();

	protected List fSelectionActions = new ArrayList();

	private ISelectionChangedListener selectionChangedListener = new ISelectionChangedListener() {

		public void selectionChanged(SelectionChangedEvent event) {
			updateSelectionDependentActions();
		}
	};

	private ITextListener textListener = new ITextListener() {

		public void textChanged(TextEvent event) {
			IUpdate findReplace = (IUpdate) fGlobalActions.get(ActionFactory.FIND.getId());
			if (findReplace != null) {
				findReplace.update();
			}
		}
	};

	protected void updateSelectionDependentActions() {
		Iterator iterator = fSelectionActions.iterator();
		while (iterator.hasNext()) {
			updateAction((String) iterator.next());
		}
	}

	protected void updateAction(String actionId) {
		IAction action = (IAction) fGlobalActions.get(actionId);
		if (action instanceof IUpdate) {
			((IUpdate) action).update();
		}
	}

	protected void setGlobalAction() {
		IActionBars actionBars = getViewSite().getActionBars();
		ResourceBundle bundle = DbPlugin.getDefault().getResourceBundle();

		setGlobalAction(actionBars, ActionFactory.UNDO.getId(), new GlobalAction(sqlViewer, ITextOperationTarget.UNDO));
		setGlobalAction(actionBars, ActionFactory.REDO.getId(), new GlobalAction(sqlViewer, ITextOperationTarget.REDO));
		setGlobalAction(actionBars, ActionFactory.DELETE.getId(), new GlobalAction(sqlViewer, ITextOperationTarget.DELETE));
		setGlobalAction(actionBars, ActionFactory.SELECT_ALL.getId(), new GlobalAction(sqlViewer, ITextOperationTarget.SELECT_ALL));
		setGlobalAction(actionBars, ActionFactory.COPY.getId(), new GlobalAction(sqlViewer, ITextOperationTarget.COPY));
		setGlobalAction(actionBars, ActionFactory.PASTE.getId(), new GlobalAction(sqlViewer, ITextOperationTarget.PASTE));
		setGlobalAction(actionBars, ActionFactory.CUT.getId(), new GlobalAction(sqlViewer, ITextOperationTarget.CUT));
		setGlobalAction(actionBars, "zigen.plugin.db.actions.SQLExecuteAction", new GlobalAction(sqlViewer, ISQLOperationTarget.ALL_EXECUTE)); //$NON-NLS-1$
		setGlobalAction(actionBars, "zigen.plugin.db.actions.SQLCurrentExecuteAction", new GlobalAction(sqlViewer, ISQLOperationTarget.CURRENT_EXECUTE)); //$NON-NLS-1$
		setGlobalAction(actionBars, "zigen.plugin.db.actions.SQLSelectedExecuteAction", new GlobalAction(sqlViewer, ISQLOperationTarget.SELECTED_EXECUTE)); //$NON-NLS-1$
		setGlobalAction(actionBars, "zigen.plugin.db.actions.SQLBackAction", new GlobalAction(sqlViewer, ISQLOperationTarget.BACK_SQL)); //$NON-NLS-1$
		setGlobalAction(actionBars, "zigen.plugin.db.actions.SQLNextAction", new GlobalAction(sqlViewer, ISQLOperationTarget.NEXT_SQL)); //$NON-NLS-1$

		FindReplaceAction findReplaceAction = new FindReplaceAction(bundle, "find_replace_action_", this); //$NON-NLS-1$
		findReplaceAction.setActionDefinitionId(IWorkbenchActionDefinitionIds.FIND_REPLACE);

		setGlobalAction(actionBars, ActionFactory.FIND.getId(), findReplaceAction); //$NON-NLS-1$

		fSelectionActions.add(ActionFactory.CUT.getId());
		fSelectionActions.add(ActionFactory.COPY.getId());
		fSelectionActions.add(ActionFactory.PASTE.getId());
		fSelectionActions.add(ActionFactory.FIND.getId());

		actionBars.updateActionBars();
	}

	protected void setGlobalAction(IActionBars actionBars, String actionID, IAction action) {
		fGlobalActions.put(actionID, action);
		actionBars.setGlobalActionHandler(actionID, action);
	}


	// ITextEditorExtension2
	public boolean isEditorInputModifiable() {
		return true;
	}

	// ITextEditorExtension2
	public boolean validateEditorInputState() {
		return true;
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

	private List extensionList = new ArrayList();

	private void setExtensionPoint() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(DbPlugin.getDefault().getBundle().getSymbolicName() + ".selection"); //$NON-NLS-1$
		IExtension[] extensions = point.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] elements = extensions[i].getConfigurationElements();
			for (int j = 0; j < elements.length; j++) {
				if (elements[j].getName().equals("contributor")) { //$NON-NLS-1$
					try {
						ISelectionListener listener = (ISelectionListener) elements[j].createExecutableExtension("class"); //$NON-NLS-1$
						getViewSite().getPage().addSelectionListener(listener);
						extensionList.add(listener);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}

	}

	public void statusChanged(Object obj, int status) {
		if (obj instanceof HistoryView) {
			switch (status) {
			case SWT.Selection:
				if (showHistoryViewAction != null)
					showHistoryViewAction.setChecked(true);
				break;
			case SWT.Dispose:
				if (showHistoryViewAction != null)
					showHistoryViewAction.setChecked(false);
				break;

			default:
				break;
			}

		} else if (obj instanceof CommitModeAction) {
			CommitModeAction action = (CommitModeAction) obj;
			switch (status) {
			case IStatusChangeListener.EVT_ChangeTransactionMode:
				toolBar.setCommitMode(action.getDbConfig(), action.isAutoCommit);
				changeTitleImageAndCommitModeText(action.getDbConfig());
				break;
			default:
				break;
			}

		} else if (obj instanceof IDBConfig) {
			if (status == IStatusChangeListener.EVT_ChangeDataBase) {
				toolBar.updateCombo((IDBConfig) obj);
				changeTitleImageAndCommitModeText((IDBConfig) obj);

			}

		}

		if (status == IStatusChangeListener.EVT_UpdateHistory) {
			toolBar.updateHistoryButton();

		} else if (status == IStatusChangeListener.EVT_UpdateDataBaseList) {
			toolBar.initializeSelectCombo();
		}

	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection != null && selection instanceof StructuredSelection) {
			Object obj = ((StructuredSelection) selection).getFirstElement();
			if (obj != null) {
				IDBConfig config = null;

				if (obj instanceof IAdaptable) {
					IAdaptable adaptable = (IAdaptable) obj;
					Object o = adaptable.getAdapter(IResource.class);

					if (o instanceof IProject) {
						config = ProjectUtil.getDBConfig((IProject) o);
					} else if (o instanceof IFolder) {
						config = ResourceUtil.getDBConfig((IFolder) o);
					} else if (o instanceof IFile) {
						config = ResourceUtil.getDBConfig((IFile) o);
					}

				} else if (obj instanceof TreeNode) {
					config = ((TreeNode) obj).getDbConfig();
				}

				if (config != null) {
					toolBar.updateCombo(config);
					changeTitleImageAndCommitModeText(config);
				}

			}

		}

	}

	public void selectionChangeHandler(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		if (selection instanceof TextSelection) {
			TextSelection textSelection = (TextSelection) selection;
			// executeSelectedSQLAction.setSelection(textSelection);

			if (textSelection.getLength() > 0) {
				// executeSelectedSQLAction.setEnabled(true);
				if (toolBar.getConfig() != null && toolBar.getConfig().getDbType() == DBType.DB_TYPE_ORACLE) {
					explainForQueryAction.setEnabled(true);
				}
			} else {
				// executeSelectedSQLAction.setEnabled(false);
				explainForQueryAction.setEnabled(false);
			}

		} else {
			// executeSelectedSQLAction.setSelection(null);
			// executeSelectedSQLAction.setEnabled(false);
			explainForQueryAction.setEnabled(false);
		}
	}

	public boolean isLockedDataBase() {
		if (toolBar != null) {
			return toolBar.isLockedDataBase();
		}
		return false;
	}

	public void setLockedDataBase(boolean isLocked) {
		if (toolBar != null) {
			toolBar.setLockedDataBase(isLocked);
		}
	}

	SQLOutinePage outlinePage;

	public Object getAdapter(Class required) {
		if (IFindReplaceTarget.class.equals(required)) {
			IFindReplaceTarget target = sqlViewer.getFindReplaceTarget();
			if (target instanceof IFindReplaceTargetExtension) {
				IFindReplaceTargetExtension t = (IFindReplaceTargetExtension) target;
				if (fFindScopeHighlightColor != null)
					t.setScopeHighlightColor(fFindScopeHighlightColor);
			}

			return target;
		}
		if (Widget.class.equals(required)) {
			return sqlViewer.getTextWidget();
		}

		return null;
	}
}
