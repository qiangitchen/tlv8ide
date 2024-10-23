package com.tulin.v8.webtools.ide.css.editors;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.css.ChooseColorAction;
import com.tulin.v8.webtools.ide.formatter.Formater;
import com.tulin.v8.webtools.ide.text.WebSourceEditor;

/**
 * CSS Editor
 */
public class CSSEditor extends WebSourceEditor {
	private CSSOutlinePage outline;

	public static final String GROUP_CSS = "_css";
	public static final String ACTION_CHOOSE_COLOR = "_choose_color";

	public CSSEditor() {
		super();
		colorProvider = WebToolsPlugin.getDefault().getColorProvider();
		setSourceViewerConfiguration(new CSSConfiguration(colorProvider));

		outline = new CSSOutlinePage(this);

		setAction(ACTION_CHOOSE_COLOR, new ChooseColorAction(this));
		setAction(ACTION_COMMENT, new CommentAction());
		setAction(ACTION_FORMAT, new FormatAction());

		setEditorContextMenuId("#AmaterasCSSEditor");
	}

	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		selectionChangeListener = new EditorSelectionChangedListener();
		selectionChangeListener.install(getSelectionProvider());
	}

	@Override
	public Image getTitleImage() {
		return WebToolsPlugin.getIcon("css.gif");
	}

	protected void addContextMenuActions(IMenuManager menu) {
		super.addContextMenuActions(menu);
		menu.appendToGroup(ITextEditorActionConstants.GROUP_EDIT,
				new MenuManager(WebToolsPlugin.getResourceString("PreferencePage.CSS"), GROUP_CSS));
		addGroup(menu, ITextEditorActionConstants.GROUP_EDIT, GROUP_CSS);
		addAction(menu, GROUP_CSS, ACTION_CHOOSE_COLOR);
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

	@Override
	protected void doSetInput(IEditorInput input) throws CoreException {
		if (input instanceof IFileEditorInput) {
			setDocumentProvider(new CSSTextDocumentProvider());
		} else if (input instanceof IStorageEditorInput) {
			setDocumentProvider(new CSSFileDocumentProvider());
		} else {
			setDocumentProvider(new CSSTextDocumentProvider());
		}
		super.doSetInput(input);
	}

	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		super.doSave(progressMonitor);
		update();
	}

	@Override
	public void doSaveAs() {
		super.doSaveAs();
		update();
	}

	protected void update() {
		outline.update();
		outline.setSelection(getSourceViewer().getTextWidget().getCaretOffset());
	}

	@Override
	protected boolean affectsTextPresentation(PropertyChangeEvent event) {
		return super.affectsTextPresentation(event) || colorProvider.affectsTextPresentation(event);
	}

	@Override
	protected void createActions() {
		super.createActions();
		// Add a content assist action
		IAction action = new ContentAssistAction(WebToolsPlugin.getDefault().getResourceBundle(),
				"ContentAssistProposal", this);
		action.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		setAction("ContentAssistProposal", action);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object getAdapter(Class adapter) {
		if (IContentOutlinePage.class.equals(adapter)) {
			return outline;
		}
		return super.getAdapter(adapter);
	}

	public String getDocText() {
		return getDocumentProvider().getDocument(getEditorInput()).get();
	}

	public int getStartLine() {
		ITextSelection sel = (ITextSelection) getSelectionProvider().getSelection();
		return sel.getStartLine();
	}

	public String getSelectionStartLineText() {
		try {
			IDocument doc = getDocumentProvider().getDocument(getEditorInput());
			int startline = getStartLine();
			int offset = doc.getLineOffset(startline);
			int length = doc.getLineLength(startline);
			return doc.get(offset, length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** The action to comment out selection range. */
	private class CommentAction extends Action {

		public CommentAction() {
			super(WebToolsPlugin.getResourceString("SourceEditor.CommentAction"));
			setEnabled(false);
			setActionDefinitionId("tulin.command.comment");
		}

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

	private class FormatAction extends Action {
		Formater formater = new Formater();

		public FormatAction() {
			super(WebToolsPlugin.getResourceString("HTMLEditor.Format"));
			setActionDefinitionId("tulin.command.format");
		}

		public void run() {
			try {
				formater.format(CSSEditor.this);
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	private class EditorSelectionChangedListener extends AbstractSelectionChangedListener {

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			ISelection selection = event.getSelection();
			ITextSelection textSelection = (ITextSelection) selection;

			// Selects the element in the outline view.
			if (outline != null) {
				outline.setSelection(textSelection.getOffset());
			}
		}

	}

}
