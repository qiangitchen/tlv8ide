package com.tulin.v8.ureport.ui.editors.designer;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.forms.editor.FormPage;

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
import com.tulin.v8.ureport.ui.editors.designer.call.SaveReportFile;

/**
 * 报表设计器的（设计）页面【Chromium】
 * 
 * @author chenqian
 *
 */
public class UReportDesigner extends FormPage {
	private UReportEditor meditor;

	private Browser designer = null;

	private CatAction catAction;
	private CopyAction copyAction;
	private PasteAction pasteAction;
	private ViewSourseAction viewSourseAction;

	private Clipboard clipbd;

	public UReportDesigner(UReportEditor meditor, String title) {
		super("ureportDesigner", title);
		this.meditor = meditor;
	}

	/**
	 * 创建右键菜单
	 */
	public void makeActions() {
		if (clipbd == null) {
			clipbd = Toolkit.getDefaultToolkit().getSystemClipboard();
		}
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
		designer = new Browser(composite, SWT.NONE);
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

	public void setUrl(String url) {
		designer.setUrl(url);
	}

}
