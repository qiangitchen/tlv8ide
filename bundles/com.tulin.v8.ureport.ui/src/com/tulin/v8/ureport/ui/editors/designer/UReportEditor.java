package com.tulin.v8.ureport.ui.editors.designer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.tulin.v8.core.utils.LocalBrowser;
import com.tulin.v8.ureport.server.utils.UreportWebappManager;
import com.tulin.v8.ureport.ui.Messages;
import com.tulin.v8.ureport.ui.editors.designer.call.ImportExcelReportFile;
import com.tulin.v8.ureport.ui.editors.designer.call.LoadReport;
import com.tulin.v8.ureport.ui.editors.designer.call.SaveReportFile;
import com.tulin.v8.webtools.ide.xml.editors.XMLEditor;

/**
 * 报表设计器
 * 
 * @author chenqian
 *
 */
public class UReportEditor extends MultiPageEditorPart implements IResourceChangeListener {
	public static String ID = "com.tulin.v8.ureport.ui.editors.designer.UReportEditor";
	private XMLEditor editor;
	private Browser swtdesigner = null;
	private Browser swtbrowser = null;

	public UReportEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
	}

	void createPage0() {
		try {
			editor = new XMLEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, Messages.getString("UReportEditor.pageEditor.1"));
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "UReportEditor", null, e.getStatus());
		}
	}

	void createPage1() {
		Composite composite = new Composite(getContainer(), SWT.FILL);
		composite.setLayout(new FillLayout());
		swtdesigner = new Browser(composite, SWT.EDGE);
		swtdesigner.setJavascriptEnabled(true);
		new LoadReport(this, swtdesigner, "callLoadReport");
		new BrowserFunction(swtdesigner, "callPreviewReport") {
			@Override
			public Object function(Object[] arguments) {
				setActivePage(2);
				return true;
			}
		};
		new SaveReportFile(this, swtdesigner, "callSaveReportFile");
		new ImportExcelReportFile(this, swtdesigner, "importExcelReportFile");
		int index = addPage(composite);
		setPageText(index, Messages.getString("UReportEditor.pageEditor.3"));
	}

	void createPage2() {
		Composite composite = new Composite(getContainer(), SWT.FILL);
		composite.setLayout(new FillLayout());
		swtbrowser = new Browser(composite, SWT.EDGE);
		swtbrowser.setJavascriptEnabled(true);
		new BrowserFunction(swtbrowser, "open") {
			@Override
			public Object function(Object[] arguments) {
				String url = UreportWebappManager.getHost() + arguments[0];
				return LocalBrowser.openUrl(url);
			}
		};
		int index = addPage(composite);
		setPageText(index, Messages.getString("UReportEditor.pageEditor.5"));
	}

	protected void createPages() {
		setPartName(getEditorInput().getName());// 标题显示为文件名
		createPage0();
		createPage1();
		createPage2();
		try {
			setActivePage(1);// 默认展示‘设计’
		} catch (Exception e) {
		}
	}

	@Override
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 1) {
			String url = UreportWebappManager.getUreportDesignerURL();
			url += "?_u=file:" + ((FileEditorInput) getEditorInput()).getPath().toString();
			swtdesigner.setUrl(url);
		} else if (newPageIndex == 2) {
			String surl = UreportWebappManager.getUreportPreviewURL();
			surl += "?_u=file:" + ((FileEditorInput) getEditorInput()).getPath().toString();
			surl += "&_i=1";
			swtbrowser.setUrl(surl);
		}
	}

	public void setText(String text) {
		String otext = editor.getDocumentProvider().getDocument(getEditorInput()).get();
		if (!otext.equals(text)) {
			editor.getDocumentProvider().getDocument(getEditorInput()).set(text);
		}
	}

	public String getText() {
		return editor.getDocumentProvider().getDocument(getEditorInput()).get();
	}

	public void setActivePages(int i) {
		setActivePage(i);
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			final IEditorInput input = getEditorInput();
			if (input instanceof IFileEditorInput) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						IFile file = ((IFileEditorInput) input).getFile();
						if (!file.exists()) {
							IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
							page.closeEditor(UReportEditor.this, false);
						} else if (!getPartName().equals(file.getName())) {
							setPartName(file.getName());
						}
					}
				});
			}
		}
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
		return false;
	}

	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

}