package com.tulin.v8.webtools.ide.js.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jdt.debug.core.IJavaStratumLineBreakpoint;
import org.eclipse.jdt.debug.core.JDIDebugModel;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewerExtension2;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.MatchingCharacterPainter;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.tulin.v8.webtools.ide.ProjectParams;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.formatter.Formater;
import com.tulin.v8.webtools.ide.html.editors.SoftTabVerifyListener;
import com.tulin.v8.webtools.ide.js.launch.JavaScriptLaunchUtil;
import com.tulin.v8.webtools.ide.text.WebSourceEditor;
import com.tulin.v8.webtools.ide.utils.StringUtils;

/**
 * The JavaScript editor.
 */
public class JavaScriptEditor extends WebSourceEditor {
	private SoftTabVerifyListener softTabListener;
	private JavaScriptOutlinePage outlinePage;
	private JavaScriptCharacterPairMatcher pairMatcher;
	private ProjectionSupport fProjectionSupport;
	private JavaScriptHyperlinkDetector hyperlinkDetector;

	public static final String GROUP_JAVASCRIPT = "_javascript";
	public static final String ACTION_DEBUGGER = "_launch_debugger";
	public static final String ACTION_TOGGLE_BREAKPOINT = "_toggle_breakpoint";

	private JavaScriptConfiguration configuration;

	/**
	 * The constructor.
	 */
	public JavaScriptEditor() {
		super();
		configuration = new JavaScriptConfiguration(this, colorProvider);
		setSourceViewerConfiguration(configuration);

		outlinePage = new JavaScriptOutlinePage(this);

		IPreferenceStore store = WebToolsPlugin.getDefault().getPreferenceStore();
		softTabListener = new SoftTabVerifyListener();
		softTabListener.setUseSoftTab(store.getBoolean(WebToolsPlugin.PREF_USE_SOFTTAB));
		softTabListener.setSoftTabWidth(store.getInt(WebToolsPlugin.PREF_SOFTTAB_WIDTH));

		setAction(ACTION_COMMENT, new CommentAction());
		setAction(ACTION_TOGGLE_BREAKPOINT, new ToggleBreakPointAction());
		setAction(ACTION_DEBUGGER, new LaunchDebuggerAction());
		setAction(ACTION_FORMAT, new FormatAction());
		setAction(ACTION_OUTLINE, new QuickOutlineAction());

		setEditorContextMenuId("#AmaterasJavaScriptEditor");
	}

	protected void updateSelectionDependentActions() {
		super.updateSelectionDependentActions();
		ITextSelection sel = (ITextSelection) getSelectionProvider().getSelection();
		if (sel.getText().equals("")) {
			getAction(ACTION_COMMENT).setEnabled(false);
		} else {
			getAction(ACTION_COMMENT).setEnabled(true);
		}
	}

	protected void rulerContextMenuAboutToShow(IMenuManager menu) {
		menu.add(getAction(ACTION_TOGGLE_BREAKPOINT));
		super.rulerContextMenuAboutToShow(menu);
	}

	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		ISourceViewer viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(), true, styles);
		getSourceViewerDecorationSupport(viewer);
		viewer.getTextWidget().addVerifyListener(softTabListener);
		return viewer;
	}

	protected void addContextMenuActions(IMenuManager menu) {
		super.addContextMenuActions(menu);
		menu.appendToGroup(ITextEditorActionConstants.GROUP_EDIT,
				new MenuManager(WebToolsPlugin.getResourceString("PreferencePage.JavaScript"), GROUP_JAVASCRIPT));
		addAction(menu, GROUP_JAVASCRIPT, ACTION_OUTLINE);
		if (getEditorInput() instanceof IFileEditorInput) {
			addAction(menu, GROUP_JAVASCRIPT, ACTION_DEBUGGER);
		}
	}

	protected void doSetInput(IEditorInput input) throws CoreException {
		if (input instanceof IFileEditorInput) {
			setDocumentProvider(new JavaScriptTextDocumentProvider());
		} else if (input instanceof IStorageEditorInput) {
			setDocumentProvider(new JavaScriptFileDocumentProvider());
		} else {
			setDocumentProvider(new JavaScriptTextDocumentProvider());
		}
		super.doSetInput(input);
		configuration.watchDocument(getDocumentProvider().getDocument(input));
	}

	public void doSave(IProgressMonitor progressMonitor) {
		super.doSave(progressMonitor);
		update();
	}

	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		ProjectionViewer viewer = (ProjectionViewer) getSourceViewer();
		fProjectionSupport = new ProjectionSupport(viewer, getAnnotationAccess(), getSharedColors());
		fProjectionSupport.install();
		viewer.doOperation(ProjectionViewer.TOGGLE);

		StyledText widget = viewer.getTextWidget();
		widget.setTabs(getPreferenceStore().getInt(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH));
		widget.addVerifyListener(softTabListener);

		ITextViewerExtension2 extension = (ITextViewerExtension2) getSourceViewer();
		pairMatcher = new JavaScriptCharacterPairMatcher();
		pairMatcher.setEnable(getPreferenceStore().getBoolean(WebToolsPlugin.PREF_PAIR_CHAR));
		MatchingCharacterPainter painter = new MatchingCharacterPainter(getSourceViewer(), pairMatcher);
		painter.setColor(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		extension.addPainter(painter);

		hyperlinkDetector = new JavaScriptHyperlinkDetector();
		viewer.setHyperlinkDetectors(new IHyperlinkDetector[] { hyperlinkDetector }, SWT.CTRL);

		selectionChangeListener = new EditorSelectionChangedListener();
		selectionChangeListener.install(getSelectionProvider());

		update();
	}

	/**
	 * Updates internal status.
	 *
	 * <ol>
	 * <li>updates the outline view</li>
	 * <li>updates the foldings</li>
	 * <li>validates contents if the editor input is
	 * <code>IFileEditorInput</code></li>
	 * <li>updates assist information if the editor input is
	 * <code>IFileEditorInput</code></li>
	 * <li>updates hyperlink information if the editor input is
	 * <code>IFileEditorInput</code></li>
	 * </ol>
	 */
	protected void update() {
		outlinePage.update();
		outlinePage.setSelection(getSourceViewer().getTextWidget().getCaretOffset());

		if (getEditorInput() instanceof IFileEditorInput) {
			doValidate();

			getEditorInput();
			getSourceViewerConfiguration();
		}
	}

	/**
	 * Invokes <code>JavaScriptValidator</code> to validate editing source code.
	 *
	 * @see JavaScriptValidator
	 */
	protected void doValidate() {
		try {
			ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					try {
						IFileEditorInput input = (IFileEditorInput) getEditorInput();
						new JavaScriptValidator(input.getFile()).doValidate();
					} catch (Exception ex) {
					}
				}
			}, null);
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}
	}

	public void dispose() {
		if (selectionChangeListener != null) {
			selectionChangeListener.uninstall(getSelectionProvider());
			selectionChangeListener = null;
		}

		if (getEditorInput() instanceof IFileEditorInput) {
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

		pairMatcher.dispose();
		super.dispose();
	}

	public void doSaveAs() {
		super.doSaveAs();
		update();
	}

	protected boolean affectsTextPresentation(PropertyChangeEvent event) {
		return super.affectsTextPresentation(event) || colorProvider.affectsTextPresentation(event);
	}

	protected void handlePreferenceStoreChanged(PropertyChangeEvent event) {
		colorProvider.handlePreferenceStoreChanged(event);
//		updateAssistProperties(event);

		String key = event.getProperty();
		if (key.equals(WebToolsPlugin.PREF_PAIR_CHAR)) {
			boolean enable = ((Boolean) event.getNewValue()).booleanValue();
			pairMatcher.setEnable(enable);
		}
		if (key.equals(WebToolsPlugin.PREF_AUTO_EDIT)) {
			boolean enable = ((Boolean) event.getNewValue()).booleanValue();
			JavaScriptConfiguration config = (JavaScriptConfiguration) getSourceViewerConfiguration();
			JavaScriptAutoEditStrategy autoEdit = config.getAutoEditStrategy();
			autoEdit.setEnabled(enable);
		}

		super.handlePreferenceStoreChanged(event);
		softTabListener.preferenceChanged(event);
	}

	protected void createActions() {
		super.createActions();
		// Add a content assist action
		IAction action = new ContentAssistAction(WebToolsPlugin.getDefault().getResourceBundle(),
				"ContentAssistProposal", this);
		action.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		setAction("ContentAssistProposal", action);
	}

	protected void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		ITextSelection textSelection = (ITextSelection) selection;

		// Selects the element in the outline view.
		if (outlinePage != null) {
			outlinePage.setSelection(textSelection.getOffset());
		}

		// Highlight a selected word.
		String text = textSelection.getText();
		StyledText styledText = getSourceViewer().getTextWidget();
		String source = styledText.getText();

		Color selectColor = Display.getDefault().getSystemColor(SWT.COLOR_CYAN);

		for (StyleRange range : styledText.getStyleRanges()) {
			if (range.background != null && range.background.equals(selectColor)) {
				range.background = null;
				styledText.setStyleRange(range);
			}
		}

		if (StringUtils.isNotEmpty(text.trim()) && Character.isJavaIdentifierPart(text.charAt(0))) {
			int index = 0;
			int lastIndex = 0;

			while ((index = source.indexOf(text, lastIndex)) >= 0) {
				if (index != textSelection.getOffset()) {
					StyleRange original = styledText.getStyleRangeAtOffset(index);
					StyleRange range = new StyleRange(index, text.length(), original.foreground, selectColor);
					styledText.setStyleRange(range);
				}
				lastIndex = index + 1;
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object getAdapter(Class adapter) {
		if (IContentOutlinePage.class.equals(adapter)) {
			return outlinePage;
		}
		return super.getAdapter(adapter);
	}

	/**
	 * Toggle comment out selection range.
	 */
	private class CommentAction extends Action {

		public CommentAction() {
			super(WebToolsPlugin.getResourceString("JavaScriptEditor.CommentAction"));
			setEnabled(false);
			setActionDefinitionId("tulin.command.comment");
		}

		@Override
		public void run() {
			ITextSelection sel = (ITextSelection) getSelectionProvider().getSelection();
			IDocument doc = getDocumentProvider().getDocument(getEditorInput());
			String text = sel.getText();
			if ("".equals(text)) {
				return;
			}
			try {
				if (text.startsWith("/*") && text.indexOf("*/") > 3) {
					text = text.replaceFirst("/\\*", "");
					text = text.replaceFirst("\\*/", "");
					doc.replace(sel.getOffset(), sel.getLength(), text);
				} else {
					doc.replace(sel.getOffset(), sel.getLength(), "/*" + sel.getText() + "*/");
				}
			} catch (BadLocationException e) {
				WebToolsPlugin.logException(e);
			}
		}
	}

	/**
	 * Launches the Rhino debugger.
	 */
	private class LaunchDebuggerAction extends Action {
		public LaunchDebuggerAction() {
			super(WebToolsPlugin.getResourceString("JavaScriptEditor.LauncheDebugger"));
		}

		@Override
		public void run() {
			try {
				IEditorInput input = getEditorInput();
				if (input instanceof IFileEditorInput) {
					IFile file = ((IFileEditorInput) input).getFile();
					JavaScriptLaunchUtil.copyLibraries();

					VMRunnerConfiguration vmConfig = new VMRunnerConfiguration(
							"org.mozilla.javascript.tools.debugger.Main",
							JavaScriptLaunchUtil.getClassPathAsStringArray());
					vmConfig.setProgramArguments(new String[] { file.getLocation().makeAbsolute().toString() });
					vmConfig.setVMArguments(new String[] { "-Dfile.encoding=" + file.getCharset() });

					Launch launch = new Launch(null, ILaunchManager.RUN_MODE, null);
					IVMRunner vmRunner = JavaRuntime.getDefaultVMInstall().getVMRunner(ILaunchManager.RUN_MODE);

					vmRunner.run(vmConfig, launch, null);
				}
			} catch (Exception ex) {
				WebToolsPlugin.logException(ex);
			}
		}
	}

	/**
	 * Format source code using JsDecoder.
	 */
	private class FormatAction extends Action {
		Formater formater = new Formater();

		public FormatAction() {
			super(WebToolsPlugin.getResourceString("HTMLEditor.Format"));
			setActionDefinitionId("tulin.command.format");
		}

		@Override
		public void run() {
			try {
				formater.format(JavaScriptEditor.this);
			} catch (Exception ex) {
				WebToolsPlugin.logException(ex);
			}
		}
	}

	private class QuickOutlineAction extends Action {

		public QuickOutlineAction() {
			super(WebToolsPlugin.getResourceString("JavaScriptEditor.QuickOutline"));
			setAccelerator(SWT.CTRL | 'o');
		}

		@Override
		public void run() {
			JavaScriptInformationControl quickOutline = new JavaScriptInformationControl(getSourceViewer());

			quickOutline.setVisible(true);
		}

	}

	private class EditorSelectionChangedListener extends AbstractSelectionChangedListener {

		public void selectionChanged(SelectionChangedEvent event) {
			JavaScriptEditor.this.selectionChanged(event);
		}

	}

	/** An action to toggle breakpoints */
	private class ToggleBreakPointAction extends Action {

		public ToggleBreakPointAction() {
			super(WebToolsPlugin.getResourceString("JavaScriptEditor.ToggleBreakPoint"));
			setEnabled(true);
		}

		@Override
		public void run() {
			IBreakpointManager manager = DebugPlugin.getDefault().getBreakpointManager();
			IBreakpoint[] breakpoints = manager.getBreakpoints();
			IEditorInput input = getEditorInput();
			IResource resource = (IResource) input.getAdapter(IFile.class);
			if (resource == null) {
				resource = (IResource) input.getAdapter(IResource.class);
			}
			int lineNumber = getVerticalRuler().getLineOfLastMouseButtonActivity() + 1;
			for (int i = 0; i < breakpoints.length; i++) {
				if (breakpoints[i] instanceof IJavaStratumLineBreakpoint) {
					IJavaStratumLineBreakpoint breakpoint = (IJavaStratumLineBreakpoint) breakpoints[i];
					if (breakpoint.getMarker().getResource().equals(resource)) {
						try {
							if (breakpoint.getLineNumber() == lineNumber) {
								breakpoint.delete();
								return;
							}
						} catch (Exception ex) {
							WebToolsPlugin.logException(ex);
						}
					}
				}
			}
			try {
				JDIDebugModel.createStratumBreakpoint(resource, "JavaScript", resource.getFullPath().toString(), null,
						"*", lineNumber, -1, -1, 0, true, null);
			} catch (Exception ex) {
				WebToolsPlugin.logException(ex);
			}
		}
	}
}
