/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.ui.IEditorPart;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.actions.SaveSQLAction;

public class DDLToolBar {

	private SQLSourceViewer fSourceViewer;

	private CoolBar coolBar;

	protected SaveSQLAction saveAction = new SaveSQLAction(null);

	IEditorPart fEditor;

	public DDLToolBar(final Composite parent, IEditorPart editor) {
		this.fEditor = editor;
		// this.coolBar = new CoolBar(parent, SWT.FLAT);
		this.coolBar = new CoolBar(parent, SWT.NONE);

		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		coolBar.setLayoutData(data);

		CoolBarManager coolBarMgr = new CoolBarManager(coolBar);
		ToolBarManager toolBarMgr1 = new ToolBarManager(SWT.FLAT);
		toolBarMgr1.add(saveAction);
		coolBarMgr.add(new ToolBarContributionItem(toolBarMgr1));
		coolBarMgr.update(true);
		coolBar.addControlListener(new ControlListener() {

			public void controlMoved(ControlEvent e) {}

			public void controlResized(ControlEvent e) {
				parent.getParent().layout(true);
				parent.layout(true);
			}
		});

	}

	public DDLToolBar(final Composite parent) {
		this(parent, null);
	}

	public void setSQLSourceViewer(SQLSourceViewer sqlSourceViewer) {
		fSourceViewer = sqlSourceViewer;
		saveAction.setSQLSourceViewer(sqlSourceViewer);
	}

	public CoolBar getCoolBar() {
		return coolBar;
	}

	public SQLSourceViewer getFSourceViewer() {
		return fSourceViewer;
	}

	private class SaveAction extends Action {

		public SaveAction() {
			this.setToolTipText(Messages.getString("SQLToolBar.3")); //$NON-NLS-1$
			this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_SAVE));
		}

		public void run() {
			fEditor.doSave(null);
		}

	}
}
