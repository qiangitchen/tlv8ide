package com.tulin.v8.ureport.ui.editors.designer;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.forms.editor.FormPage;
import org.osgi.framework.Version;

import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.swt.chromium.Browser;
import com.tulin.v8.swt.chromium.BrowserFunction;
import com.tulin.v8.ureport.ui.editors.designer.action.BackAction;
import com.tulin.v8.ureport.ui.editors.designer.action.CatAction;
import com.tulin.v8.ureport.ui.editors.designer.action.CopyAction;
import com.tulin.v8.ureport.ui.editors.designer.action.ForwardAction;
import com.tulin.v8.ureport.ui.editors.designer.action.PasteAction;
import com.tulin.v8.ureport.ui.editors.designer.action.RefreshAction;
import com.tulin.v8.ureport.ui.editors.designer.action.ViewSourseAction;
import com.tulin.v8.ureport.ui.editors.designer.call.ImportExcelReportFile;
import com.tulin.v8.ureport.ui.editors.designer.call.LoadReport;
import com.tulin.v8.ureport.ui.editors.designer.call.SWTImportExcelReportFile;
import com.tulin.v8.ureport.ui.editors.designer.call.SWTLoadReport;
import com.tulin.v8.ureport.ui.editors.designer.call.SWTSaveReportFile;
import com.tulin.v8.ureport.ui.editors.designer.call.SaveReportFile;

public class UReportDesigner extends FormPage {
	UReportEditor meditor;

	private org.eclipse.swt.browser.Browser swtdesigner = null;

	private Browser designer = null;

	private CatAction catAction;
	private CopyAction copyAction;
	private PasteAction pasteAction;
	private ViewSourseAction viewSourseAction;

	Clipboard clipbd = Toolkit.getDefaultToolkit().getSystemClipboard();

	public UReportDesigner(UReportEditor meditor, String title) {
		super("ureportDesigner", title);
		this.meditor = meditor;
	}

	/**
	 * 创建右键菜单
	 */
	public void makeActions() {
		catAction = new CatAction(clipbd);
		copyAction = new CopyAction(clipbd);
		pasteAction = new PasteAction(clipbd);
		viewSourseAction = new ViewSourseAction(meditor);
	}

	public void fillContextMenu(IMenuManager manager) {
		manager.add(catAction);
		manager.add(copyAction);
		manager.add(pasteAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(viewSourseAction);
	}

	public void createPartControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.FILL);
		composite.setLayout(new FillLayout());
		createBrowser0(composite);
		if (swtdesigner != null) {
			swtdesigner.setJavascriptEnabled(true);
			new SWTLoadReport(meditor, swtdesigner, "callLoadReport");
			new org.eclipse.swt.browser.BrowserFunction(swtdesigner, "callPreviewReport") {
				@Override
				public Object function(Object[] arguments) {
					meditor.setActivePages(2);
					return true;
				}
			};
			new SWTSaveReportFile(meditor, swtdesigner, "callSaveReportFile");
			new SWTImportExcelReportFile(meditor, swtdesigner, "importExcelReportFile");
		} else if (designer != null) {
			designer.setJavascriptEnabled(true);
			new LoadReport(meditor, designer, "callLoadReport");
			new BrowserFunction(designer, "callPreviewReport") {
				@Override
				public Object function(Object[] arguments) {
					meditor.setActivePages(2);
					return true;
				}
			};
			new SaveReportFile(meditor, designer, "callSaveReportFile");
			new ImportExcelReportFile(meditor, designer, "importExcelReportFile");
		}
		if (designer != null && "chromium".equalsIgnoreCase(designer.getBrowserType())) {
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
			makeActions();
		}
	}

	private void createBrowser0(Composite composite) {
		if (CommonUtil.isWinOS()) {
			try {
				Version version = Platform.getBundle("org.eclipse.platform").getVersion();
				if (CommonUtil.getOSVersion() >= 10 && version.getMajor() >= 4 && version.getMinor() > 18) {
					int EDGE = 1 << 18;
					swtdesigner = new org.eclipse.swt.browser.Browser(composite, EDGE);
				} else {
					designer = new Browser(composite, SWT.NONE);
				}
			} catch (SWTError e) {
				try {
					for (Control control : composite.getChildren()) {
						control.dispose();
					}
				} catch (Exception er) {
				} finally {
					swtdesigner = null;
				}
				designer = new Browser(composite, SWT.NONE);
			}
		} else {
			swtdesigner = new org.eclipse.swt.browser.Browser(composite, SWT.NONE);
		}
	}

	public void setUrl(String url) {
		if (swtdesigner != null) {
			swtdesigner.setUrl(url);
		} else {
			designer.setUrl(url);
		}
	}

}
