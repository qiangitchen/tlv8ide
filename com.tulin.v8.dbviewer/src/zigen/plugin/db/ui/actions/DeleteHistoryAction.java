/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.core.SQLHistoryManager;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.ui.internal.History;
import zigen.plugin.db.ui.internal.HistoryFolder;
import zigen.plugin.db.ui.views.HistoryContentProvider;
import zigen.plugin.db.ui.views.HistoryView;

public class DeleteHistoryAction extends Action implements Runnable {

	StructuredViewer viewer = null;

	public DeleteHistoryAction(StructuredViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("DeleteHistoryAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("DeleteHistoryAction.1")); //$NON-NLS-1$
		// this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_REMOVE));
		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));

	}

	public void run() {

		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

		Iterator iter = selection.iterator();

		IContentProvider obj = viewer.getContentProvider();
		if (obj instanceof HistoryContentProvider) {
			HistoryContentProvider provider = (HistoryContentProvider) obj;
			if (DbPlugin.getDefault().confirmDialog(Messages.getString("DeleteHistoryAction.2"))) { //$NON-NLS-1$
				SQLHistoryManager mgr = DbPlugin.getDefault().getHistoryManager();

				TimeWatcher tw = new TimeWatcher();
				tw.start();

				while (iter.hasNext()) {
					Object object = iter.next();
					if (object instanceof HistoryFolder) {
						HistoryFolder folder = (HistoryFolder) object;
						folder.getParent().removeChild(folder);
						viewer.refresh(folder.getParent());

						List list = folder.getChildren();
						for (Iterator iterator = list.iterator(); iterator.hasNext();) {
							History history = (History) iterator.next();
							if (!history.getSqlHistory().isBlank()) {
								try {
									mgr.remove(history.getSqlHistory());
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}

					} else if (object instanceof History) {
						History history = (History) object;

						// TimeWatcher tw2 = new TimeWatcher();
						// tw2.start();
						history.getParent().removeChild(history);
						// tw2.stop();

						// tw2.start();
						viewer.refresh(history.getParent());
						// tw2.stop();

						try {
							mgr.remove(history.getSqlHistory());
						} catch (IOException e) {
							e.printStackTrace();
						}

					}

				}

				tw.stop();

				tw.start();
				IViewPart part = DbPlugin.findView(DbPluginConstant.VIEW_ID_HistoryView);
				if (part instanceof HistoryView) {
					HistoryView hv = (HistoryView) part;
					// hv.updateHistoryView(history);
					DbPlugin.fireStatusChangeListener(hv, IStatusChangeListener.EVT_UpdateHistory);
				}
				tw.stop();


			}

		}

	}

}
