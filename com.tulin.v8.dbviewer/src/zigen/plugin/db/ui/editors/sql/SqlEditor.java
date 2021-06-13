/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.sql;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ISelectionValidator;
import org.eclipse.jface.text.ISynchronizable;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension2;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.link.LinkedModeModel;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelExtension;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.MatchingCharacterPainter;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.PropertyPageChangeListener;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.properties.DBPropertyPage;
import zigen.plugin.db.ui.actions.AutoDelayListener;
import zigen.plugin.db.ui.actions.GlobalAction;
import zigen.plugin.db.ui.util.FileUtil;
import zigen.plugin.db.ui.util.ResourceUtil;
import zigen.plugin.db.ui.util.StyledTextUtil;
import zigen.plugin.db.ui.views.CommitModeAction;
import zigen.plugin.db.ui.views.ISQLOperationTarget;
import zigen.plugin.db.ui.views.internal.ColorManager;
import zigen.plugin.db.ui.views.internal.SQLCharacterPairMatcher;
import zigen.plugin.db.ui.views.internal.SQLCodeConfiguration;
import zigen.plugin.db.ui.views.internal.SQLOutinePage;
import zigen.plugin.db.ui.views.internal.SQLPartitionScanner;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;
import zigen.plugin.db.ui.views.internal.SQLToolBarForSqlEditor;

public class SqlEditor extends TextEditor implements ISqlEditor, IPropertyChangeListener, IStatusChangeListener, IPropertyPageChangeListener {

	protected ImageCacher ic = ImageCacher.getInstance();

	protected SQLSourceViewer sqlViewer;

	protected SQLCodeConfiguration sqlConfiguration;

	protected ColorManager colorManager;

	protected LineNumberRulerColumn rulerCol;

	protected IPreferenceStore store;

	protected ProjectionSupport projectionSupport;

	protected Map fGlobalActions = new HashMap();

	protected SQLToolBarForSqlEditor toolBar;

	protected SQLOutinePage outlinePage;

	public SqlEditor() {
		super();
		colorManager = new ColorManager();
		sqlConfiguration = new SQLCodeConfiguration(colorManager);
		setSourceViewerConfiguration(sqlConfiguration);
		this.store = DbPlugin.getDefault().getPreferenceStore();
		this.store.addPropertyChangeListener(this);
		DbPlugin.addStatusChangeListener(this);
		PropertyPageChangeListener.addPropertyPageChangeListener(this);
	}

	public IFile getFile() {
		FileEditorInput fi = (FileEditorInput) getEditorInput();
		return fi.getFile();
	}

	public void propertyPageChanged(Object obj, int status) {
		if (status == PropertyPageChangeListener.EVT_SetDataBase) {
			IDBConfig config = ResourceUtil.getDBConfig(getFile());
			toolBar.updateCombo(config);
		}
	}

	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		setSite(site);
		setInput(editorInput);
	}

	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		sqlViewer = (SQLSourceViewer) getSourceViewer();
		String demiliter = DbPlugin.getDefault().getPreferenceStore().getString(SQLEditorPreferencePage.P_SQL_DEMILITER);

		IDocument doc = sqlViewer.getDocument();
		IDocumentPartitioner partitioner = new FastPartitioner(new SQLPartitionScanner(), new String[] {SQLPartitionScanner.SQL_STRING, SQLPartitionScanner.SQL_COMMENT});
		partitioner.connect(doc);
		doc.setDocumentPartitioner(partitioner);

		ITextViewerExtension2 extension = (ITextViewerExtension2) sqlViewer;
		MatchingCharacterPainter painter = new MatchingCharacterPainter(sqlViewer, new SQLCharacterPairMatcher());
		painter.setColor(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_MATCHING));
		extension.addPainter(painter);

		ProjectionViewer pviewer = (ProjectionViewer) getSourceViewer();
		projectionSupport = new ProjectionSupport(pviewer, getAnnotationAccess(), getSharedColors());
		projectionSupport.install();
		pviewer.doOperation(ProjectionViewer.TOGGLE);
		updateFolding();

		StyledTextUtil.changeColor(colorManager, sqlViewer.getTextWidget());

		hookContextMenu();


		toolBar.setSQLSourceViewer(sqlViewer);

		toolBar.updateCombo(ResourceUtil.getDBConfig(getFile()));

		sqlViewer.getTextWidget().addKeyListener(new AutoDelayAdapter());
		sqlViewer.getTextWidget().addMouseListener(new AutoDelayAdapter());

		getSite().setSelectionProvider(sqlViewer);

	}

	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {

		// Composite header = new Composite(parent, SWT.NONE);
		FormLayout layout = new FormLayout();
		parent.setLayout(layout);
		// createToolbarPart(parent);
		toolBar = new SQLToolBarForSqlEditor(this);
		toolBar.createPartControl(parent);

		Composite sqlComposite = new Composite(parent, SWT.NONE);
		sqlComposite.setLayout(new FillLayout());
		FormData data = new FormData();
		data.top = new FormAttachment(toolBar.getCoolBar(), 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		sqlComposite.setLayoutData(data);

		fAnnotationAccess = getAnnotationAccess();
		fOverviewRuler = createOverviewRuler(getSharedColors());

		ISourceViewer viewer = new SQLSourceViewer(sqlComposite, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
		getSourceViewerDecorationSupport(viewer);

		IDBConfig config = FileUtil.getDBConfig(getFile());
		if (config != null) {
			toolBar.updateCombo(config);
			changeTitleImageAndCommitModeText(config);
		}

		return viewer;
	}

	void changeTitleImageAndCommitModeText(IDBConfig config) {
		try {
			if (config != null && toolBar != null && toolBar.getConfig() != null) {

				if (config.getDbName().equals(toolBar.getConfig().getDbName())) {
					Transaction trans = Transaction.getInstance(config);

					if (trans.isConneting()) {
						super.setTitleImage(ic.getImage(DbPlugin.IMG_CODE_CONNECTED_DB));
					} else {
						super.setTitleImage(ic.getImage(DbPlugin.IMG_CODE_DB));
					}
				}

			}
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
			}
		});
		StyledText text = sqlViewer.getTextWidget();
		Menu menu = menuMgr.createContextMenu(text);
		text.setMenu(menu);
		getSite().registerContextMenu(menuMgr, sqlViewer);
	}

	private SqlEditorContributor getContributor() {
		IEditorActionBarContributor contributor = getEditorSite().getActionBarContributor();
		if (contributor instanceof SqlEditorContributor) {
			return (SqlEditorContributor) contributor;
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

	public void dispose() {
		colorManager.dispose();
		// getEditorSite().getPage().removeSelectionListener(this);
		DbPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(this);
		DbPlugin.removeStatusChangeListener(this);
		PropertyPageChangeListener.removePropertyPageChangeListener(this);
		super.dispose();

		if (outlinePage != null) {
			outlinePage.dispose();
		}

	}

	public void propertyChange(PropertyChangeEvent event) {
		if (sqlConfiguration != null && sqlViewer != null) {
			sqlConfiguration.updatePreferences(sqlViewer.getDocument());
			StyledTextUtil.changeColor(colorManager, sqlViewer.getTextWidget());
			// LineNumberRulerColumnUtil.changeColor(colorManager, rulerCol);
			sqlViewer.invalidateTextPresentation();
		}
	}

	protected void setGlobalAction() {
		IActionBars actionBars = getEditorSite().getActionBars();
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), new GlobalAction(sqlViewer, ITextOperationTarget.UNDO));
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), new GlobalAction(sqlViewer, ITextOperationTarget.REDO));
		actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), new GlobalAction(sqlViewer, ITextOperationTarget.DELETE));
		actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), new GlobalAction(sqlViewer, ITextOperationTarget.SELECT_ALL));
		actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), new GlobalAction(sqlViewer, ITextOperationTarget.COPY));
		actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), new GlobalAction(sqlViewer, ITextOperationTarget.PASTE));
		actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), new GlobalAction(sqlViewer, ITextOperationTarget.CUT));

		setGlobalAction(actionBars, "zigen.plugin.db.actions.SQLExecuteAction", new GlobalAction(sqlViewer, ISQLOperationTarget.ALL_EXECUTE)); //$NON-NLS-1$
		setGlobalAction(actionBars, "zigen.plugin.db.actions.SQLCurrentExecuteAction", new GlobalAction(sqlViewer, ISQLOperationTarget.CURRENT_EXECUTE)); //$NON-NLS-1$
		setGlobalAction(actionBars, "zigen.plugin.db.actions.SQLSelectedExecuteAction", new GlobalAction(sqlViewer, ISQLOperationTarget.SELECTED_EXECUTE)); //$NON-NLS-1$

	}

	protected void setGlobalAction(IActionBars actionBars, String actionID, IAction action) {
		fGlobalActions.put(actionID, action);
		actionBars.setGlobalActionHandler(actionID, action);
	}

	public void setFocus() {
		setGlobalAction();
	}

	public IDBConfig getConfig() {
		return toolBar.getConfig();
	}

	public Object getAdapter(Class adapter) {
		if (projectionSupport != null) {
			Object obj = projectionSupport.getAdapter(getSourceViewer(), adapter);
			if (obj != null) {
				return obj;
			}
		}

		return super.getAdapter(adapter);
	}

	protected void updateFolding() {
	// IDocument doc = sqlViewer.getDocument();
	// int offset = sqlViewer.getTextWidget().getCaretOffset();
	// setStatusLineMessage("");
	// ProjectionAnnotationModel model =
	// sqlViewer.getProjectionAnnotationModel();
	//
	// if (model != null) {
	// UpdateSQLFoldingJob job = new UpdateSQLFoldingJob(model, doc, offset,
	// getIStatusLineManager());
	// job.setPriority(UpdateSQLFoldingJob.SHORT);
	// job.setUser(true);
	// job.schedule();
	// }
	}

	public void doSave(IProgressMonitor progressMonitor) {
		super.doSave(progressMonitor);
		if (outlinePage != null)
			outlinePage.update();
		updateFolding();

		saveProperties();

		setDirty(false);
	}

	public void doSaveAs() {
		doSave(null);
	}

	protected void saveProperties() {
		if (getConfig() != null) {
			try {
				String dbName = getConfig().getDbName();
				getFile().setPersistentProperty(new QualifiedName(DBPropertyPage.QUALIFIER, DBPropertyPage.SELECTED_DB), dbName);
				PropertyPageChangeListener.firePropertyPageChangeListener(this, PropertyPageChangeListener.EVT_SetDataBase);
			} catch (Exception e) {
				DbPlugin.getDefault().showErrorDialog(e);
			}
		}
	}

	// protected void handleCursorPositionChanged() {
	// super.handleCursorPositionChanged();
	// // updateStatusField("categoryName");
	// }

	boolean dirty;

	public boolean isDirty() {
		return dirty;
	}

	protected void setDirty(boolean value) {
		dirty = value;
		firePropertyChange(PROP_DIRTY);
	}

	public void statusChanged(Object obj, int status) {
		if (status == IStatusChangeListener.EVT_UpdateDataBaseList) {
			toolBar.initializeSelectCombo();
		} else if (status == IStatusChangeListener.EVT_ChangeTransactionMode) {
			if (obj instanceof CommitModeAction) {
				CommitModeAction action = (CommitModeAction) obj;
				toolBar.setCommitMode(action.getDbConfig(), action.isAutoCommit());
			}
		} else if (status == IStatusChangeListener.EVT_ChangeDataBase) {
			setDirty(true);
		}
	}

	class SaveAction extends org.eclipse.jface.action.Action {

		public SaveAction() {
			this.setToolTipText("Save"); //$NON-NLS-1$
			this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_SAVE));
		}

		public void run() {
			doSave(null);
		}

	}

	class AutoDelayAdapter extends AutoDelayListener {

		public Runnable createExecutAction() {
			return new Runnable() {

				public void run() {
					try {
						if (outlinePage != null)
							outlinePage.update();
						// updateFolding();
					} catch (Exception e) {
					}
				}
			};

		}

	}

	class OccurrencesFinderJob extends Job {

		private IDocument fDocument;

		private ISelection fSelection;

		private ISelectionValidator fPostSelectionValidator;

		private boolean fCanceled = false;

		private IProgressMonitor fProgressMonitor;

		private Position[] fPositions;

		public OccurrencesFinderJob(IDocument document, Position[] positions, ISelection selection) {
			super(""); //$NON-NLS-1$
			fDocument = document;
			fSelection = selection;
			fPositions = positions;

			if (getSelectionProvider() instanceof ISelectionValidator)
				fPostSelectionValidator = (ISelectionValidator) getSelectionProvider();
		}

		void doCancel() {
			fCanceled = true;
			cancel();
		}

		private boolean isCanceled() {
			return fCanceled || fProgressMonitor.isCanceled() || fPostSelectionValidator != null
					&& !(fPostSelectionValidator.isValid(fSelection) || fForcedMarkOccurrencesSelection == fSelection) || LinkedModeModel.hasInstalledModel(fDocument);
		}

		public IStatus run(IProgressMonitor progressMonitor) {

			fProgressMonitor = progressMonitor;

			// if (isCanceled())
			// return Status.CANCEL_STATUS;

			ITextViewer textViewer = sqlViewer;
			if (textViewer == null)
				return Status.CANCEL_STATUS;

			IDocument document = textViewer.getDocument();
			if (document == null)
				return Status.CANCEL_STATUS;

			IDocumentProvider documentProvider = getDocumentProvider();
			if (documentProvider == null)
				return Status.CANCEL_STATUS;

			fForcedMarkOccurrencesSelection = getSelectionProvider().getSelection();

			IAnnotationModel annotationModel = documentProvider.getAnnotationModel(getEditorInput());
			if (annotationModel == null)
				return Status.CANCEL_STATUS;

			// Add occurrence annotations
			int length = fPositions.length;
			Map annotationMap = new HashMap(length);
			for (int i = 0; i < length; i++) {

				// if (isCanceled())
				// return Status.CANCEL_STATUS;

				String message;
				Position position = fPositions[i];

				// Create & add annotation
				try {
					message = document.get(position.offset, position.length);
				} catch (BadLocationException ex) {
					// Skip this match
					continue;
				}
				annotationMap.put(new Annotation("org.eclipse.jdt.ui.occurrences", false, message), //$NON-NLS-1$
						position);
			}

			// if (isCanceled())
			// return Status.CANCEL_STATUS;

			synchronized (getLockObject(annotationModel)) {

				if (annotationModel instanceof IAnnotationModelExtension) {
					((IAnnotationModelExtension) annotationModel).replaceAnnotations(fOccurrenceAnnotations, annotationMap);
				} else {
					removeOccurrenceAnnotations();
					Iterator iter = annotationMap.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry mapEntry = (Map.Entry) iter.next();
						annotationModel.addAnnotation((Annotation) mapEntry.getKey(), (Position) mapEntry.getValue());
					}
				}
				fOccurrenceAnnotations = (Annotation[]) annotationMap.keySet().toArray(new Annotation[annotationMap.keySet().size()]);
			}

			return Status.OK_STATUS;
		}

	}

	private Annotation[] fOccurrenceAnnotations;

	private IRegion fMarkOccurrenceTargetRegion;

	private long fMarkOccurrenceModificationStamp = IDocumentExtension4.UNKNOWN_MODIFICATION_STAMP;

	private ISelection fForcedMarkOccurrencesSelection;

	private Object getLockObject(IAnnotationModel annotationModel) {
		if (annotationModel instanceof ISynchronizable) {
			Object lock = ((ISynchronizable) annotationModel).getLockObject();
			if (lock != null)
				return lock;
		}
		return annotationModel;
	}

	void removeOccurrenceAnnotations() {
		fMarkOccurrenceModificationStamp = IDocumentExtension4.UNKNOWN_MODIFICATION_STAMP;
		fMarkOccurrenceTargetRegion = null;

		IDocumentProvider documentProvider = getDocumentProvider();
		if (documentProvider == null)
			return;

		IAnnotationModel annotationModel = documentProvider.getAnnotationModel(getEditorInput());
		if (annotationModel == null || fOccurrenceAnnotations == null)
			return;

		synchronized (getLockObject(annotationModel)) {
			if (annotationModel instanceof IAnnotationModelExtension) {
				((IAnnotationModelExtension) annotationModel).replaceAnnotations(fOccurrenceAnnotations, null);
			} else {
				for (int i = 0, length = fOccurrenceAnnotations.length; i < length; i++)
					annotationModel.removeAnnotation(fOccurrenceAnnotations[i]);
			}
			fOccurrenceAnnotations = null;
		}
	}

	public SQLSourceViewer getSqlViewer() {
		return sqlViewer;
	}

}
