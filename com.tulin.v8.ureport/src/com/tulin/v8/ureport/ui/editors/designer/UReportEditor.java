package com.tulin.v8.ureport.ui.editors.designer;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.core.utils.LocalBrowser;
import com.tulin.v8.ureport.server.utils.UreportWebappManager;
import com.tulin.v8.ureport.ui.Messages;
import com.tulin.v8.ureport.ui.editors.designer.action.BackAction;
import com.tulin.v8.ureport.ui.editors.designer.action.CatAction;
import com.tulin.v8.ureport.ui.editors.designer.action.CopyAction;
import com.tulin.v8.ureport.ui.editors.designer.action.ForwardAction;
import com.tulin.v8.ureport.ui.editors.designer.action.PasteAction;
import com.tulin.v8.ureport.ui.editors.designer.action.RefreshAction;
import com.tulin.v8.ureport.ui.editors.designer.action.ViewSourseAction;
import com.tulin.v8.ureport.ui.editors.designer.call.ImportExcelReportFile;
import com.tulin.v8.ureport.ui.editors.designer.call.LoadReport;
import com.tulin.v8.ureport.ui.editors.designer.call.SaveReportFile;
import com.tulin.v8.xml.editors.XMLEditor;

public class UReportEditor extends MultiPageEditorPart implements IResourceChangeListener {
	public static String ID = "com.tulin.v8.ureport.ui.editors.designer.UReportEditor";
	private XMLEditor editor;
	private Browser designer;
	private Browser browser;
	private String url;

	private CatAction catAction;
	private CopyAction copyAction;
	private PasteAction pasteAction;
	private ViewSourseAction viewSourseAction;

	Clipboard clipbd = Toolkit.getDefaultToolkit().getSystemClipboard();

	public UReportEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		makeActions();
	}

	public void fillContextMenu(IMenuManager manager) {
		manager.add(catAction);
		manager.add(copyAction);
		manager.add(pasteAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(viewSourseAction);
	}

	/**
	 * 创建右键菜单
	 */
	public void makeActions() {
		catAction = new CatAction(clipbd);
		copyAction = new CopyAction(clipbd);
		pasteAction = new PasteAction(clipbd);
		viewSourseAction = new ViewSourseAction(this);

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
		designer = createBrowser(composite);
		designer.setJavascriptEnabled(true);
		new LoadReport(this, designer, "callLoadReport");
		new BrowserFunction(designer, "callPreviewReport") {
			@Override
			public Object function(Object[] arguments) {
				setActivePage(2);
				return true;
			}
		};
		new SaveReportFile(this, designer, "callSaveReportFile");
		new ImportExcelReportFile(this, designer, "importExcelReportFile");
		int index = addPage(composite);
		setPageText(index, Messages.getString("UReportEditor.pageEditor.3"));
		if ("chromium".equalsIgnoreCase(designer.getBrowserType())) {
			MenuManager menuMgr = new MenuManager("#PopupMenu");
			menuMgr.setRemoveAllWhenShown(true);
			menuMgr.addMenuListener(new IMenuListener() {
				public void menuAboutToShow(IMenuManager manager) {
					manager.removeAll();
					BackAction backaction = new BackAction(designer);
					manager.add(backaction);
					backaction.setEnabled(designer.isBackEnabled());
					ForwardAction forwardaction = new ForwardAction(designer);
					manager.add(forwardaction);
					forwardaction.setEnabled(designer.isForwardEnabled());
					RefreshAction refreshAction = new RefreshAction(designer);
					manager.add(refreshAction);
					manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
					fillContextMenu(manager);
				}
			});
			Menu menu = menuMgr.createContextMenu(designer);
			designer.setMenu(menu);
		}
	}

	void createPage2() {
		try {
			Composite composite = new Composite(getContainer(), SWT.FILL);
			composite.setLayout(new FillLayout());
			browser = createBrowser(composite);
			browser.setJavascriptEnabled(true);
			new BrowserFunction(browser, "open") {
				@Override
				public Object function(Object[] arguments) {
					String url = UreportWebappManager.getHost() + arguments[0];
					return LocalBrowser.openUrl(url);
				}
			};
			int index = addPage(composite);
			setPageText(index, Messages.getString("UReportEditor.pageEditor.5"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("chromium".equalsIgnoreCase(browser.getBrowserType())) {
			MenuManager menuMgr = new MenuManager("#PopupMenu");
			menuMgr.setRemoveAllWhenShown(true);
			menuMgr.addMenuListener(new IMenuListener() {
				public void menuAboutToShow(IMenuManager manager) {
					manager.removeAll();
					BackAction backaction = new BackAction(browser);
					manager.add(backaction);
					backaction.setEnabled(browser.isBackEnabled());
					ForwardAction forwardaction = new ForwardAction(browser);
					manager.add(forwardaction);
					forwardaction.setEnabled(browser.isForwardEnabled());
					manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
					RefreshAction refreshAction = new RefreshAction(browser);
					manager.add(refreshAction);
				}
			});
			Menu menu = menuMgr.createContextMenu(browser);
			browser.setMenu(menu);
		}
	}

	private Browser createBrowser(Composite composite) {
		Browser browser = null;
		try {
			if (CommonUtil.isWinOS() && CommonUtil.getOSVersion() >= 10) {
				browser = new Browser(composite, SWT.EDGE);
			} else {
				browser = new Browser(composite, SWT.CHROMIUM);
			}
		} catch (SWTError e) {
			try {
				for (Control control : composite.getChildren()) {
					control.dispose();
				}
			} catch (Exception er) {
			}
			browser = new Browser(composite, SWT.CHROMIUM);
		}
		return browser;
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
			designer.setUrl(url);
		} else if (newPageIndex == 2) {
			String preview = UreportWebappManager.getUreportPreviewURL();
			preview += "?_u=file:" + ((FileEditorInput) getEditorInput()).getPath().toString();
			preview += "&_i=1";
			browser.setUrl(preview);
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