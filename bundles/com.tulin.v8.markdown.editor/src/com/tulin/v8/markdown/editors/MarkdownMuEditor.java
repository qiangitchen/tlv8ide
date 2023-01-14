package com.tulin.v8.markdown.editors;

import java.io.File;
import java.net.URI;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import com.tulin.v8.markdown.pagemodel.MarkdownPage;

public class MarkdownMuEditor extends FormEditor {
	private MarkdownEditor editor;
	private Browser viewer;

	@Override
	protected void createPages() {
		try {
			editor = new MarkdownEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, Messages.getString("editors.MKEditor.1"));
			addPage(new MarkdownBrowser(this));
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "MarkdownMuEditor", Messages.getString("editors.MKEditor.2"),
					e.getStatus());
		}
	}

	class MarkdownBrowser extends FormPage {

		public MarkdownBrowser(FormEditor editor) {
			super(editor, "MarkdownBrowser", Messages.getString("editors.MKEditor.3"));
		}

		protected void createFormContent(final IManagedForm managedForm) {
			final ScrolledForm form = managedForm.getForm();
			form.getBody().setLayout(new GridLayout());
			FormToolkit toolkit = managedForm.getToolkit();
			Section section = toolkit.createSection(form.getBody(), Section.NO_TITLE);
			Composite composite = toolkit.createComposite(section, SWT.FILL);
			section.setClient(composite);
			section.setText("Markdown Editor");
			section.setDescription("Markdown Editor Browser");
			section.setLayoutData(new GridData(GridData.FILL_BOTH));
			composite.setLayout(new FillLayout());
			composite.setLayoutData(new GridData(GridData.FILL_BOTH));
			viewer = new Browser(composite, SWT.MULTI);
		}

	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		editor.doSave(monitor);
	}

	@Override
	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setInput(editor.getEditorInput());
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	protected void addPages() {

	}

	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 1) {
			MarkdownPage page = editor.getMarkdownPage();
			String html = page.html();
			html = addBaseURL(editor, html);
			if (page != null && html != null) {
				viewer.setText(html);
			} else {
				viewer.setText("");
			}
		}
	}

	/**
	 * Adjust the URL base to be the file's directory.
	 * 
	 * @param editor
	 * @param html
	 * @return
	 */
	private String addBaseURL(IEditorPart editor, String html) {
		try {
			IPathEditorInput input = (IPathEditorInput) editor.getEditorInput();
			IPath path = input.getPath();
			path = path.removeLastSegments(1);
			File f = path.toFile();
			URI fileURI = f.toURI();
			String html2 = "<html><head><base href='" + fileURI + "' /></head><body>\r\n" + html + "\r\n</body></html>";
			return html2;
		} catch (Exception ex) {
			return html;
		}
	}

	@Override
	public Object getSelectedPage() {
		return editor;
	}

}
