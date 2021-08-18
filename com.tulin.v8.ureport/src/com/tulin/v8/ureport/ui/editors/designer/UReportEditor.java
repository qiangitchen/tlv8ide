package com.tulin.v8.ureport.ui.editors.designer;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

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

import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.core.utils.LocalBrowser;
import com.tulin.v8.ureport.server.utils.UreportWebappManager;
import com.tulin.v8.ureport.ui.Messages;
import com.tulin.v8.ureport.ui.editors.designer.call.SWTImportExcelReportFile;
import com.tulin.v8.ureport.ui.editors.designer.call.SWTLoadReport;
import com.tulin.v8.ureport.ui.editors.designer.call.SWTSaveReportFile;
import com.tulin.v8.xml.editors.XMLEditor;

/**
 * 报表设计器
 * 
 * @author chenqian
 *
 */
public class UReportEditor extends MultiPageEditorPart implements IResourceChangeListener {
	public static String ID = "com.tulin.v8.ureport.ui.editors.designer.UReportEditor";
	private XMLEditor editor;
	private org.eclipse.swt.browser.Browser swtdesigner = null;
	private org.eclipse.swt.browser.Browser swtbrowser = null;
	UReportDesigner designer;
	UReportPreview preview;
	private String url;

	Clipboard clipbd = Toolkit.getDefaultToolkit().getSystemClipboard();

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
		if (CommonUtil.isWinOS()) {
			try {
				designer = new UReportDesigner(this, Messages.getString("UReportEditor.pageEditor.3"));
				int index = addPage(designer, getEditorInput());
				setPageText(index, Messages.getString("UReportEditor.pageEditor.3"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Composite composite = new Composite(getContainer(), SWT.FILL);
			composite.setLayout(new FillLayout());
			swtdesigner = new Browser(composite, SWT.NONE);
			swtdesigner.setJavascriptEnabled(true);
			new SWTLoadReport(this, swtdesigner, "callLoadReport");
			new BrowserFunction(swtdesigner, "callPreviewReport") {
				@Override
				public Object function(Object[] arguments) {
					setActivePage(2);
					return true;
				}
			};
			new SWTSaveReportFile(this, swtdesigner, "callSaveReportFile");
			new SWTImportExcelReportFile(this, swtdesigner, "importExcelReportFile");
			int index = addPage(composite);
			setPageText(index, Messages.getString("UReportEditor.pageEditor.3"));
		}
	}

	void createPage2() {
		if (CommonUtil.isWinOS()) {
			try {
				preview = new UReportPreview(this, Messages.getString("UReportEditor.pageEditor.3"));
				int index = addPage(preview, getEditorInput());
				setPageText(index, Messages.getString("UReportEditor.pageEditor.5"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Composite composite = new Composite(getContainer(), SWT.FILL);
			composite.setLayout(new FillLayout());
			swtbrowser = new Browser(composite, SWT.NONE);
			swtbrowser.setJavascriptEnabled(true);
			new org.eclipse.swt.browser.BrowserFunction(swtbrowser, "open") {
				@Override
				public Object function(Object[] arguments) {
					String url = UreportWebappManager.getHost() + arguments[0];
					return LocalBrowser.openUrl(url);
				}
			};
			int index = addPage(composite);
			setPageText(index, Messages.getString("UReportEditor.pageEditor.5"));
		}
	}

	protected void createPages() {
		createPage0();
		createPage1();
		createPage2();
		setPartName(editor.getPartName());// 标题显示为文件名
		try {
			setActivePage(1);// 默认展示‘设计’
		} catch (Exception e) {
		}
	}

	@Override
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 1) {
			if (url == null) {
				url = UreportWebappManager.getUreportDesignerURL();
				url += "?_u=file:" + ((FileEditorInput) getEditorInput()).getPath().toString();
			}
			if (swtdesigner != null) {
				swtdesigner.setUrl(url);
			} else {
				designer.setUrl(url);
			}
		} else if (newPageIndex == 2) {
			String surl = UreportWebappManager.getUreportPreviewURL();
			surl += "?_u=file:" + ((FileEditorInput) getEditorInput()).getPath().toString();
			surl += "&_i=1";
			if (swtbrowser != null) {
				swtbrowser.setUrl(surl);
			} else {
				preview.setUrl(surl);
			}
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