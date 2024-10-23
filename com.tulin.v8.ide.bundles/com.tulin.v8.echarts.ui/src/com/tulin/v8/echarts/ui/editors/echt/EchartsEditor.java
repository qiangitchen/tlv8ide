package com.tulin.v8.echarts.ui.editors.echt;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.editor.FormEditor;

import com.tulin.v8.webtools.ide.xml.editors.XMLEditor;

public class EchartsEditor extends FormEditor implements IResourceChangeListener {
	public static String ID = "com.tulin.v8.echarts.ui.editors.echt.EchartsEditor";
	private XMLEditor sourceEditor;
	private EchartsDesignPage page1;

	public EchartsEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	void createPage0() {
		try {
			sourceEditor = new XMLEditor();
			int index = addPage(sourceEditor, getEditorInput());
			setPageText(index, Messages.getString("editors.EchartsEditor.1"));
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), Messages.getString("editors.EchartsEditor.2"), null,
					e.getStatus());
		}
	}

	public String getSource() {
		return sourceEditor.getDocumentProvider().getDocument(getEditorInput()).get();
	}

	public void setSource(String str) {
		sourceEditor.getDocumentProvider().getDocument(getEditorInput()).set(str);
	}

	void createPage1() {
		page1 = new EchartsDesignPage(this);
		try {
			addPage(page1);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void addPages() {

	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
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
	protected void createPages() {
		createPage0();
		createPage1();
		setPartName(sourceEditor.getPartName());
		setActivePage(1);
	}

	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 0) {

		} else if (newPageIndex == 1) {
			page1.loadModle();
			page1.loadBrowser();
		}
	}

	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			final IEditorInput input = getEditorInput();
			if (input instanceof IFileEditorInput) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						IFile file = ((IFileEditorInput) input).getFile();
						if (!file.exists()) {
							IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
							page.closeEditor(EchartsEditor.this, false);
						} else if (!getPartName().equals(file.getName())) {
							setPartName(file.getName());
						}
					}
				});
			}
		}
	}

}
