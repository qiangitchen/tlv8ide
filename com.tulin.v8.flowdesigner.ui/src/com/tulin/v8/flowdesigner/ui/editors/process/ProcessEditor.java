package com.tulin.v8.flowdesigner.ui.editors.process;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.tulin.v8.flowdesigner.ui.editors.Messages;
import com.tulin.v8.js.ui.editors.javascript.JavaScriptEditor;

public class ProcessEditor extends MultiPageEditorPart implements IResourceChangeListener {// IStatusChangeListener
	public static String ID = "com.tulin.v8.flowdesigner.ui.editors.process.ProcessEditor";

	private JavaScriptEditor sourceEditor;
	private FlowDesignEditor designEditor;

//	private ISelection selection;

	public ProcessEditor() {
		super();
//		StudioPlugin.addStatusChangeListener(this);
	}

	@Override
	protected void createPages() {
		createPage0();
		createPage1();

//		setSelection(StudioPlugin.getSelection());
	}

	private void createPage0() {
		if (getEditorInput() instanceof IFileEditorInput) {
			sourceEditor = new JavaScriptEditor();
			try {
				int deindex = addPage(sourceEditor, getEditorInput());
				setPageText(deindex, Messages.getString("TLEditor.pageEditor.1"));
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
	}

	private void createPage1() {
		try {
			designEditor = new FlowDesignEditor(this);
			int deindex = addPage(designEditor, getEditorInput());
			setPageText(deindex, Messages.getString("TLEditor.pageEditor.3"));
			setPartName(getEditorInput().getName());
			designEditor.loadBrowser(getSourceText());

			setActivePage(deindex);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		if (sourceEditor != null) {
			sourceEditor.doSave(monitor);
		} else {
			designEditor.doSave(monitor);
		}
	}

	@Override
	public void doSaveAs() {
		// TODO 自动生成的方法存根
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public boolean isDirty() {
		if (sourceEditor != null)
			return sourceEditor.isDirty();
		if (designEditor != null)
			designEditor.isDirty();
		return super.isDirty();
	}

	public String getSourceText() {
		if (sourceEditor != null) {
			return sourceEditor.getDocumentProvider().getDocument(getEditorInput()).get();
		} else if (designEditor != null) {
			return designEditor.getSourceText();
		}
		return "";
	}

	public void setSourceText(String jsons) {
		if (sourceEditor != null) {
			String ptext = sourceEditor.getDocumentProvider().getDocument(getEditorInput()).get();
			if (!ptext.equals(jsons)) {
				sourceEditor.getDocumentProvider().getDocument(getEditorInput()).set(jsons);
			}
		}
	}

//	public void setSelection(ISelection selection) {
//		this.selection = selection;
//	}

	public void setFocus() {
//		StudioPlugin.fireStatusChangeListener(selection, IStatusChangeListener.EVT_LinkTable);
	}

	public void statusChanged(Object obj, int status) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					page.closeEditor(ProcessEditor.this, false);
				}
			});
		}
	}

	@Override
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 1) {
			designEditor.loadBrowser(getSourceText());
		}
	}

	@Override
	public Object getSelectedPage() {
		int i = getActivePage();
		if (i == 1)
			return designEditor;
		return sourceEditor;
	}

}