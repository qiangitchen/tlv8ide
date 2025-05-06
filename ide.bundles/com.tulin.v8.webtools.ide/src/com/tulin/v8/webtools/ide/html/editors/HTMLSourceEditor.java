package com.tulin.v8.webtools.ide.html.editors;

import java.io.File;

import org.apache.commons.jrcs.diff.AddDelta;
import org.apache.commons.jrcs.diff.ChangeDelta;
import org.apache.commons.jrcs.diff.DeleteDelta;
import org.apache.commons.jrcs.diff.Delta;
import org.apache.commons.jrcs.diff.Diff;
import org.apache.commons.jrcs.diff.Revision;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewerExtension2;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.MatchingCharacterPainter;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.tulin.v8.webtools.ide.ProjectParams;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.assist.HTMLAssistProcessor;
import com.tulin.v8.webtools.ide.css.ChooseColorAction;
import com.tulin.v8.webtools.ide.formatter.Formater;
import com.tulin.v8.webtools.ide.handlers.ToggleCommentHandler;
import com.tulin.v8.webtools.ide.html.HTMLUtil;
import com.tulin.v8.webtools.ide.html.HtmlAutoEditStrategy;
import com.tulin.v8.webtools.ide.html.IHTMLOutlinePage;
import com.tulin.v8.webtools.ide.text.WebSourceEditor;
import com.tulin.v8.webtools.ide.xpath.SearchXPathDialog;

/**
 * HTML source editor.
 */
public class HTMLSourceEditor extends WebSourceEditor {
	private IHTMLOutlinePage outlinePage;
	private HTMLCharacterPairMatcher pairMatcher;
	private SoftTabVerifyListener softTabListener;

	public static final String GROUP_HTML = "_html";
	public static final String ACTION_ESCAPE_HTML = "_escape_html";
	public static final String ACTION_OPEN_PALETTE = "_open_palette";
	public static final String ACTION_CHOOSE_COLOR = "_choose_color";
	public static final String ACTION_COMPLETION = "ContentAssistProposal";
	public static final String ACTION_SEARCH_XPATH = "_search_xpath";

//	private boolean useSoftTab;
//	private String softTab;
	private boolean validation = true;

	public HTMLSourceEditor() {
		super();
		colorProvider = WebToolsPlugin.getDefault().getColorProvider();
		configuration = new HTMLConfiguration(this, WebToolsPlugin.getDefault().getColorProvider());
		setSourceViewerConfiguration(configuration);
		setPreferenceStore(new ChainedPreferenceStore(
				new IPreferenceStore[] { getPreferenceStore(), WebToolsPlugin.getDefault().getPreferenceStore() }));

		setAction(ACTION_ESCAPE_HTML, new EscapeHTMLAction());
		setAction(ACTION_COMMENT, new CommentAction());
		setAction(ACTION_OPEN_PALETTE, new OpenPaletteAction());
		setAction(ACTION_CHOOSE_COLOR, new ChooseColorAction(this));
		setAction(ACTION_FORMAT, new FormatAction());
		setAction(ACTION_SEARCH_XPATH, new SearchXPathAction());

		IPreferenceStore store = WebToolsPlugin.getDefault().getPreferenceStore();
		softTabListener = new SoftTabVerifyListener();
		softTabListener.setUseSoftTab(store.getBoolean(WebToolsPlugin.PREF_USE_SOFTTAB));
		softTabListener.setSoftTabWidth(store.getInt(WebToolsPlugin.PREF_SOFTTAB_WIDTH));

		outlinePage = createOutlinePage();

		setEditorContextMenuId("#AmaterasHTMLEditor");
	}

	protected HTMLCharacterPairMatcher getPairMatcher() {
		return this.pairMatcher;
	}

	public void setValidation(boolean validation) {
		this.validation = validation;
	}

	public boolean getValidation() {
		return this.validation;
	}

	protected IHTMLOutlinePage createOutlinePage() {
		return new HTMLOutlinePage(this);
	}

	private ProjectionSupport fProjectionSupport;

	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		ISourceViewer viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(), true, styles);
		getSourceViewerDecorationSupport(viewer);
		viewer.getTextWidget().addVerifyListener(softTabListener);
		return viewer;
	}

	public ISourceViewer getViewer() {
		return this.getSourceViewer();
	}

	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		ProjectionViewer projectionViewer = (ProjectionViewer) getSourceViewer();
		fProjectionSupport = new ProjectionSupport(projectionViewer, getAnnotationAccess(), getSharedColors());
		fProjectionSupport.install();
		projectionViewer.doOperation(ProjectionViewer.TOGGLE);
		projectionViewer.getTextWidget()
				.setTabs(getPreferenceStore().getInt(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH));

		ITextViewerExtension2 extension = (ITextViewerExtension2) getSourceViewer();
		pairMatcher = new HTMLCharacterPairMatcher();
		pairMatcher.setEnable(getPreferenceStore().getBoolean(WebToolsPlugin.PREF_PAIR_CHAR));
		MatchingCharacterPainter painter = new MatchingCharacterPainter(getSourceViewer(), pairMatcher);
		painter.setColor(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		extension.addPainter(painter);

		selectionChangeListener = new EditorSelectionChangedListener();
		selectionChangeListener.install(getSelectionProvider());

		update();
	}

	/** This method is called when configuration is changed. */
	protected boolean affectsTextPresentation(PropertyChangeEvent event) {
		return super.affectsTextPresentation(event) || colorProvider.affectsTextPresentation(event);
	}

	/** This method is called when configuration is changed. */
	protected void handlePreferenceStoreChanged(PropertyChangeEvent event) {
		colorProvider.handlePreferenceStoreChanged(event);
		updateAssistProperties(event);
		softTabListener.preferenceChanged(event);

		String key = event.getProperty();
		if (key.equals(WebToolsPlugin.PREF_PAIR_CHAR)) {
			Object value = event.getNewValue();
			boolean enable = false;
			if (value instanceof String) {
				enable = Boolean.valueOf((String) value).booleanValue();
			} else if (value instanceof Boolean) {
				enable = ((Boolean) value).booleanValue();
			}
			pairMatcher.setEnable(enable);
		}
		if (key.equals(WebToolsPlugin.PREF_AUTO_EDIT)) {
			boolean enable = ((Boolean) event.getNewValue()).booleanValue();
			HTMLConfiguration config = (HTMLConfiguration) getSourceViewerConfiguration();
			HtmlAutoEditStrategy autoEdit = config.getAutoEditStrategy();
			autoEdit.setEnabled(enable);
		}

		super.handlePreferenceStoreChanged(event);
	}

	private void updateAssistProperties(PropertyChangeEvent event) {
		String key = event.getProperty();
		try {
			// auto activation delay
			if (key.equals(WebToolsPlugin.PREF_ASSIST_TIMES)) {
				ContentAssistant assistant = (ContentAssistant) getSourceViewerConfiguration()
						.getContentAssistant(null);
				assistant.setAutoActivationDelay(Integer.parseInt((String) event.getNewValue()));

				// auto activation trigger
			} else if (key.equals(WebToolsPlugin.PREF_ASSIST_CHARS)) {
				ContentAssistant assistant = (ContentAssistant) getSourceViewerConfiguration()
						.getContentAssistant(null);
				HTMLAssistProcessor processor = (HTMLAssistProcessor) assistant
						.getContentAssistProcessor(IDocument.DEFAULT_CONTENT_TYPE);
				processor.setAutoAssistChars(((String) event.getNewValue()).toCharArray());

				// completion close tag
			} else if (key.equals(WebToolsPlugin.PREF_ASSIST_CLOSE)) {
				ContentAssistant assistant = (ContentAssistant) getSourceViewerConfiguration()
						.getContentAssistant(null);
				HTMLAssistProcessor processor = (HTMLAssistProcessor) assistant
						.getContentAssistProcessor(IDocument.DEFAULT_CONTENT_TYPE);
				processor.setAssistCloseTag(((Boolean) event.getNewValue()).booleanValue());

				// enable auto activation or not
			} else if (key.equals(WebToolsPlugin.PREF_ASSIST_AUTO)) {
				ContentAssistant assistant = (ContentAssistant) getSourceViewerConfiguration()
						.getContentAssistant(null);
				assistant.enableAutoActivation(((Boolean) event.getNewValue()).booleanValue());
			}
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}
	}

	/**
	 * Adds actions to the context menu.
	 * <p>
	 * If you want to customize the context menu, you can override this method in
	 * the sub-class instead of editorContextMenuAboutToShow(),
	 *
	 * @param menu IMenuManager
	 */
	protected void addContextMenuActions(IMenuManager menu) {
		super.addContextMenuActions(menu);
		menu.appendToGroup(ITextEditorActionConstants.GROUP_EDIT,
				new MenuManager(WebToolsPlugin.getResourceString("MultiPageHTMLEditor.html"), GROUP_HTML));
		addAction(menu, GROUP_HTML, ACTION_SEARCH_XPATH);
		addAction(menu, GROUP_HTML, ACTION_CHOOSE_COLOR);
		// addAction(menu, GROUP_HTML, ACTION_OPEN_PALETTE);
		addAction(menu, GROUP_HTML, ACTION_ESCAPE_HTML);
	}

	protected void updateSelectionDependentActions() {
		super.updateSelectionDependentActions();
		ITextSelection sel = (ITextSelection) getSelectionProvider().getSelection();
		if (sel.getText().equals("")) {
			getAction(ACTION_COMMENT).setEnabled(false);
			getAction(ACTION_ESCAPE_HTML).setEnabled(false);
		} else {
			getAction(ACTION_COMMENT).setEnabled(true);
			getAction(ACTION_ESCAPE_HTML).setEnabled(true);
		}
	}

	/**
	 * Validates HTML document. If the editor provides other validation, do override
	 * at the subclass.
	 */
	protected void doValidate() {
		new Job("HTML Validation") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					IFileEditorInput input = (IFileEditorInput) getEditorInput();
					IFile file = input.getFile();
					file.deleteMarkers(IMarker.PROBLEM, false, 0);

					ProjectParams params = new ProjectParams(file.getProject());
					if (params.getValidateHTML()) {
						new HTMLValidator(input.getFile()).doValidate();
					}
				} catch (Exception ex) {
					// WebToolsPlugin.logException(ex);
				}

				return Status.OK_STATUS;
			}
		}.schedule();
	}

	/**
	 * Returns HTML source which can be parse by FuzzyXML.
	 *
	 * @return HTML source
	 */
	public String getHTMLSource() {
		return getDocumentProvider().getDocument(getEditorInput()).get();
	}

	/**
	 * This method is called in the following timing:
	 * <ul>
	 * <li>Initialize editor</li>
	 * <li>Save document</li>
	 * </ul>
	 * And do the following sequence.
	 * <ul>
	 * <li>invoke {@link HTMLSourceEditor#updateFolding()}</li>
	 * <li>invoke {@link HTMLSourceEditor#updateAssist()}</li>
	 * <li>invoke {@link HTMLOutlinePage#update()}</li>
	 * <li>update {@link HTMLHyperlinkSupport}
	 * <li>invoke {@link HTMLSourceEditor#doValidate()()} (only getValidation()
	 * returns true)</li>
	 * </ul>
	 * If it's required to update some information about the editor, do override at
	 * the subclass.
	 */
	protected void update() {
		updateAssist();
		outlinePage.update();
		outlinePage.setSelection(getViewer().getTextWidget().getCaretOffset());

		if (validation && isFileEditorInput()) {
			doValidate();
		}
	}

	/**
	 * Update informations about code-completion.
	 */
	protected void updateAssist() {
		// Update AssistProcessors
		new Job("Update Content Assist Information") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				if (!isFileEditorInput()) {
					return Status.OK_STATUS;
				}
				return Status.OK_STATUS;
			}
		}.schedule();
	}

	public void dispose() {
		if (selectionChangeListener != null) {
			selectionChangeListener.uninstall(getSelectionProvider());
			selectionChangeListener = null;
		}

		if (isFileEditorInput() && validation) {
			try {
				ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
					public void run(IProgressMonitor monitor) throws CoreException {
						try {
							IFileEditorInput input = (IFileEditorInput) getEditorInput();
							ProjectParams params = new ProjectParams(input.getFile().getProject());
							if (params.getRemoveMarkers()) {
								input.getFile().deleteMarkers(IMarker.PROBLEM, false, 0);
							}
						} catch (Exception ex) {
						}
					}
				}, null);
			} catch (Exception ex) {
			}
		}

		fProjectionSupport.dispose();
		pairMatcher.dispose();
		super.dispose();
	}

	/**
	 * Returns a java.io.File object that is editing in this editor.
	 */
	public File getFile() {
		IFile file = ((FileEditorInput) this.getEditorInput()).getFile();
		return file.getLocation().makeAbsolute().toFile();
	}

	/**
	 * Returns a java.io.File object of a temporary file for preview.
	 */
	public File getTempFile() {
		IFile file = ((FileEditorInput) this.getEditorInput()).getFile();
		return new File(file.getLocation().makeAbsolute().toFile().getParentFile(), "." + file.getName());
	}

	protected void createActions() {
		super.createActions();
		// add content assist action
		IAction action = new ContentAssistAction(WebToolsPlugin.getDefault().getResourceBundle(),
				"ContentAssistProposal", this);
		action.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		setAction(ACTION_COMPLETION, action);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object getAdapter(Class adapter) {
		if (IContentOutlinePage.class.equals(adapter)) {
			return outlinePage;
		}
		if (ProjectionAnnotationModel.class.equals(adapter) && fProjectionSupport != null) {
			Object obj = fProjectionSupport.getAdapter(getSourceViewer(), adapter);
			if (obj != null) {
				return obj;
			}
		}
		if (IDocumentProvider.class.equals(adapter)) {
			return getDocumentProvider();
		}
		if (ISelectionProvider.class.equals(adapter)) {
			return getSelectionProvider();
		}
		return super.getAdapter(adapter);
	}

	protected IDocumentProvider createDocumentProvider(IEditorInput input) {
		if (input instanceof IFileEditorInput) {
			return new HTMLTextDocumentProvider();
		} else if (input instanceof IStorageEditorInput) {
			return new HTMLFileDocumentProvider();
		} else {
			return new HTMLTextDocumentProvider();
		}
	}

	protected final void doSetInput(IEditorInput input) throws CoreException {
		setDocumentProvider(createDocumentProvider(input));
		if (input instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput) input).getFile();
			if (file.getName().endsWith(".xhtml")) {
				HTMLConfiguration config = (HTMLConfiguration) getSourceViewerConfiguration();
				config.getAssistProcessor().setXHTMLMode(true);
			}
		}
		super.doSetInput(input);
		configuration.watchDocument(getDocumentProvider().getDocument(input));
	}

	public void doSave(IProgressMonitor progressMonitor) {
		super.doSave(progressMonitor);
		update();
	}

	public void doSaveAs() {
		super.doSaveAs();
		update();
	}

	protected void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		ITextSelection textSelection = (ITextSelection) selection;

		// Selects the element in the outline view.
		if (outlinePage != null) {
			outlinePage.setSelection(textSelection.getOffset());
		}
	}

	public void setDiff(String text) {
		// apply the graphical editor contents to the source editor
		IEditorInput input = getEditorInput();
		IDocument doc = getDocumentProvider().getDocument(input);

		getSourceViewer().getTextWidget().setRedraw(false);

		String[] text1 = doc.get().split("\n");
		String[] text2 = text.split("\n");
		try {
			Revision rev = Diff.diff(text1, text2);
			int count1 = 0;
			int count2 = 0;
			int index = 0;
			for (int i = 0; i < rev.size(); i++) {
				Delta delta = rev.getDelta(i);
				Range orgRange = new Range(delta.getOriginal().rangeString());
				Range revRange = new Range(delta.getRevised().rangeString());
				// matched
				while (count1 != orgRange.getFrom() - 1) {
					index = index + text1[count1].length() + 1;
					count1++;
				}
				count1 = orgRange.getFrom() - 1;
				count2 = revRange.getFrom() - 1;
				// added
				if (delta instanceof AddDelta) {
					while (count2 != revRange.getTo()) {
						doc.replace(index, 0, text2[count2] + "\n");
						index = index + text2[count2].length() + 1;
						count2++;
					}
					// removed
				} else if (delta instanceof DeleteDelta) {
					while (count1 != orgRange.getTo()) {
						doc.replace(index, text1[count1].length() + 1, "");
						count1++;
					}
					// replaced
				} else if (delta instanceof ChangeDelta) {
					while (count1 != orgRange.getTo()) {
						doc.replace(index, text1[count1].length() + 1, "");
						count1++;
					}
					while (count2 != revRange.getTo()) {
						doc.replace(index, 0, text2[count2] + "\n");
						index = index + text2[count2].length() + 1;
						count2++;
					}
				}
				count1 = orgRange.getTo();
				count2 = revRange.getTo();
			}
		} catch (Exception ex) {
			doc.set(text);
		}

		getSourceViewer().getTextWidget().setRedraw(true);
	}

	/** This class is used in setDiff(). */
	private class Range {
		private int from;
		private int to;

		public Range(String rangeString) {
			if (rangeString.indexOf(",") != -1) {
				String[] dim = rangeString.split(",");
				from = Integer.parseInt(dim[0]);
				to = Integer.parseInt(dim[1]);
			} else {
				from = Integer.parseInt(rangeString);
				to = Integer.parseInt(rangeString);
			}
		}

		public int getFrom() {
			return this.from;
		}

		public int getTo() {
			return to;
		}
	}

	public boolean isFileEditorInput() {
		if (getEditorInput() instanceof IFileEditorInput) {
			return true;
		}
		return false;
	}

	////////////////////////////////////////////////////////////////////////////
	// actions
	////////////////////////////////////////////////////////////////////////////
	private static SearchXPathDialog dialog;

	private class SearchXPathAction extends Action {

		public SearchXPathAction() {
			super(WebToolsPlugin.getResourceString("XMLEditor.XPathSearch"));
		}

		@Override
		public void run() {
			if (dialog == null) {
				dialog = new SearchXPathDialog(getEditorSite().getShell());
			}
			dialog.open();
		}
	}

	/** The action to escape HTML tags in the selection. */
	private class EscapeHTMLAction extends Action {

		public EscapeHTMLAction() {
			super(WebToolsPlugin.getResourceString("HTMLEditor.EscapeAction"));
			setEnabled(false);
			setAccelerator(SWT.CTRL | '\\');
		}

		public void run() {
			ITextSelection sel = (ITextSelection) getSelectionProvider().getSelection();
			IDocument doc = getDocumentProvider().getDocument(getEditorInput());
			try {
				doc.replace(sel.getOffset(), sel.getLength(), HTMLUtil.escapeHTML(sel.getText()));
			} catch (BadLocationException e) {
				WebToolsPlugin.logException(e);
			}
		}
	}

	private class FormatAction extends Action {
		Formater formater = new Formater();

		public FormatAction() {
			super(WebToolsPlugin.getResourceString("HTMLEditor.Format"));
			setActionDefinitionId("tulin.command.format");
		}

		public void run() {
			try {
				formater.format(HTMLSourceEditor.this);
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	/** The action to comment out selection range. */
	private class CommentAction extends Action {

		public CommentAction() {
			super(WebToolsPlugin.getResourceString("HTMLEditor.CommentAction"));
			setEnabled(false);
			setActionDefinitionId("tulin.command.comment");
		}

		public void run() {
			ToggleCommentHandler.toggleComment(HTMLSourceEditor.this);
		}
	}

	/** The action to open the palette view. */
	private class OpenPaletteAction extends Action {

		public OpenPaletteAction() {
			super(WebToolsPlugin.getResourceString("HTMLEditor.OpenPaletteAction"),
					WebToolsPlugin.getDefault().getImageRegistry().getDescriptor(WebToolsPlugin.ICON_PALETTE));
			setEnabled(true);
		}

		public void run() {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			try {
				window.getActivePage().showView("com.tulin.v8.webtools.ide.views.PaletteView");
			} catch (Exception ex) {
				WebToolsPlugin.openAlertDialog(ex.toString());
			}
		}
	}

	private class EditorSelectionChangedListener extends AbstractSelectionChangedListener {

		public void selectionChanged(SelectionChangedEvent event) {
			HTMLSourceEditor.this.selectionChanged(event);
		}

	}

}
