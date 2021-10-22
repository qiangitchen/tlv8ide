/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.editors.QueryViewEditorInput;
import zigen.plugin.db.ui.editors.TableViewEditorInput;
import zigen.plugin.db.ui.editors.sql.SequenceEditorInput;
import zigen.plugin.db.ui.editors.sql.SourceEditorInput;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.jobs.DisconnectDBJob;
import zigen.plugin.db.ui.jobs.ConnectDBJob;

public class CloseDBAction extends Action implements Runnable {

	TreeViewer viewer = null;

	public CloseDBAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("CloseDBAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("CloseDBAction.1")); //$NON-NLS-1$
		this.setEnabled(false);

	}

	public void run() {
		
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

		for (Iterator iter = selection.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			if (element instanceof DataBase) {

				DataBase db = (DataBase) element;
				
				try {
					List target = new ArrayList();
					IEditorReference[] references = DbPlugin.getDefault().getPage().getEditorReferences();
					for (int i = 0; i < references.length; i++) {
						IEditorReference reference = references[i];
						IEditorInput input = reference.getEditorInput();

						if (input instanceof QueryViewEditorInput) {
							IDBConfig config = ((QueryViewEditorInput) input).getConfig();
							if (config.getDbName().equals(db.getName())) {
								target.add(reference);
							}

						} else if (input instanceof TableViewEditorInput) {
							IDBConfig config = ((TableViewEditorInput) input).getConfig();
							if (config.getDbName().equals(db.getName())) {
								target.add(reference);
							}

						} else if (input instanceof SequenceEditorInput) {
							IDBConfig config = ((SequenceEditorInput) input).getConfig();
							if (config.getDbName().equals(db.getName())) {
								target.add(reference);
							}
						} else if (input instanceof SourceEditorInput) {
							IDBConfig config = ((SourceEditorInput) input).getConfig();
							if (config.getDbName().equals(db.getName())) {
								target.add(reference);
							}
						}

					}
					if (target.size() > 0) {
						DbPlugin.getCloseEditors((IEditorReference[]) target.toArray(new IEditorReference[0]));
					}

					
					DbPlugin.fireStatusChangeListener(db.getDbConfig(), IStatusChangeListener.EVT_RemoveSchemaFilter);
					
					
					DisconnectDBJob job = new DisconnectDBJob(viewer, db);
					job.setPriority(ConnectDBJob.SHORT);
					job.setUser(false);
					job.setSystem(false);
					job.schedule();
					
					
				} catch (Exception e) {
					DbPlugin.getDefault().showErrorDialog(e);
				}


			}
		}
		
		


	}

}
