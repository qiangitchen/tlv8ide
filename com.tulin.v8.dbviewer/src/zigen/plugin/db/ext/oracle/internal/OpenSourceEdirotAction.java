/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import java.sql.Connection;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ui.editors.sql.SequenceEditorInput;
import zigen.plugin.db.ui.editors.sql.SourceEditorInput;
import zigen.plugin.db.ui.internal.OracleSequence;
import zigen.plugin.db.ui.internal.OracleSource;

public class OpenSourceEdirotAction extends Action implements Runnable {

	StructuredViewer viewer = null;

	public OpenSourceEdirotAction(StructuredViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("OpenSourceEdirotAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("OpenSourceEdirotAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FILE));


	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

		for (Iterator iter = selection.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			if (element instanceof OracleSource) {
				OracleSource source = (OracleSource) element;
				openSourceEditor(source);

			} else if (element instanceof OracleSequence) {
				OracleSequence seq = (OracleSequence) element;
				openSequenceEditor(seq);

			} else {
				throw new IllegalStateException("OpenSourceEdirotAction#run()"); //$NON-NLS-1$
			}
		}

	}

	protected void openSourceEditor(OracleSource source) {
		OracleSourceDetailInfo sourceDetail = null;
		OracleSourceErrorInfo[] sourceErrors = null;

		try {
			Connection con = Transaction.getInstance(source.getDbConfig()).getConnection();

			String owner = source.getOracleSourceInfo().getOwner();
			String type = source.getOracleSourceInfo().getType();
			String name = source.getOracleSourceInfo().getName();

			sourceDetail = OracleSourceDetailSearcher.execute(con, owner, name, type, true);
			sourceErrors = OracleSourceErrorSearcher.execute(con, owner, name, type);

			SourceEditorInput input = new SourceEditorInput(source.getDbConfig(), source, sourceDetail, sourceErrors);
			IWorkbenchPage page = DbPlugin.getDefault().getPage();
			IEditorPart editor = IDE.openEditor(page, input, DbPluginConstant.EDITOR_ID_SOURCE, true);

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

	protected void openSequenceEditor(OracleSequence seq) {

		try {
			SequenceEditorInput input = new SequenceEditorInput(seq.getDbConfig(), seq.getOracleSequenceInfo());
			IWorkbenchPage page = DbPlugin.getDefault().getPage();
			IEditorPart editor = IDE.openEditor(page, input, DbPluginConstant.EDITOR_ID_SEQUENCE, true);

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		}
	}

}
