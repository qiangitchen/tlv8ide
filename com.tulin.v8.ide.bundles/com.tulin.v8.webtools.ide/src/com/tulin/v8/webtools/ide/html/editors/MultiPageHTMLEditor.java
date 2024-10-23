package com.tulin.v8.webtools.ide.html.editors;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

/**
 * The tabbed style HTML editor.
 */
public class MultiPageHTMLEditor extends MultiPageEditorPart implements IResourceChangeListener, HTMLEditorPart {

	/** HTML source editor */
	private HTMLSourceEditor editor;
	/** Browser widget for preview */
	private Browser browser;
	/** wrapper */
	private HTMLEditor wrapper;

	public MultiPageHTMLEditor(HTMLEditor wrapper, HTMLSourceEditor editor) {
		super();
		this.wrapper = wrapper;
		this.editor = editor;
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	public Browser getBrowser() {
		return browser;
	}

	public HTMLSourceEditor getSourceEditor() {
		return editor;
	}

	private void createPage0() {
		try {
			int index = addPage(editor, getEditorInput());
			setPageText(index, WebToolsPlugin.getResourceString("MultiPageHTMLEditor.Source")); //$NON-NLS-1$
			setPartName(getEditorInput().getName());
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "Error creating nested text editor", null, e.getStatus()); //$NON-NLS-1$
		}
	}

	private void createPage1() {
		if (isFileEditorInput()) {
			browser = new Browser(getContainer(), SWT.NONE);
			int index = addPage(browser);
			setPageText(index, WebToolsPlugin.getResourceString("MultiPageHTMLEditor.Preview")); //$NON-NLS-1$
		}
	}

	protected void createPages() {
		createPage0();
		createPage1();
	}

	public void dispose() {
		if (isFileEditorInput()) {
			File tmpFile = editor.getTempFile();
			if (tmpFile.exists()) {
				tmpFile.delete();
			}
		}
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
	}

	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setInput(editor.getEditorInput());
		setPartName(getEditorInput().getName());
	}

	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		super.init(site, editorInput);
	}

	public boolean isFileEditorInput() {
		return editor.isFileEditorInput();
	}

	public boolean isSaveAsAllowed() {
		return true;
	}

	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 1) {
			wrapper.updatePreview();
		}
	}

	/** Change to the source editor, and move calet to the specified offset. */
	public void setOffset(int offset) {
		setActivePage(0);
		editor.selectAndReveal(offset, 0);
	}

	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			final IEditorInput input = editor.getEditorInput();
			if (input instanceof IFileEditorInput) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						IFile file = ((IFileEditorInput) input).getFile();
						if (!file.exists()) {
							IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
							page.closeEditor(MultiPageHTMLEditor.this, false);
						} else if (!getPartName().equals(file.getName())) {
							setPartName(file.getName());
						}
					}
				});
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object getAdapter(Class adapter) {
		return editor.getAdapter(adapter);
	}

	protected void firePropertyChange(int propertyId) {
		super.firePropertyChange(propertyId);
		wrapper.firePropertyChange2(propertyId);
	}

}
