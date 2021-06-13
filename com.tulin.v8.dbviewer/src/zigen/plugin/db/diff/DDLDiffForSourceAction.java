/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

import java.sql.Connection;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ext.oracle.internal.OracleSourceDetailInfo;
import zigen.plugin.db.ext.oracle.internal.OracleSourceDetailSearcher;
import zigen.plugin.db.ui.internal.OracleSource;

public class DDLDiffForSourceAction extends Action implements Runnable {

	private StructuredViewer viewer = null;

	private OracleSource left = null;

	private OracleSource right = null;

	public DDLDiffForSourceAction(StructuredViewer viewer) {
		this.viewer = viewer;
		this.setText("&Diff DDL");
	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		try {
			int index = 0;
			for (Iterator iter = selection.iterator(); iter.hasNext();) {
				Object obj = iter.next();

				if (obj instanceof OracleSource) {
					OracleSource s = (OracleSource) obj;

					if (index == 0) {
						left = s;

						index++;
					} else if (index == 1) {
						right = s;
						index++;
					} else {
						break;
					}

				}

			}

			if (index == 2) {
				showDDLDiff();
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

	private void showDDLDiff() throws Exception {

		SourceDDLDiff diff = new SourceDDLDiff(new SourceDDL(left), new SourceDDL(right));
		DDLDiffEditorInput input = new DDLDiffEditorInput(new SourceDDLDiff[] {diff}, true);
		IWorkbenchPage page = DbPlugin.getDefault().getPage();
		IDE.openEditor(page, input, DDLDiffEditor.ID, true);

	}

	protected OracleSourceDetailInfo getOracleSourceDetailInfo(OracleSource source) {
		OracleSourceDetailInfo sourceDetail = null;
		// OracleSourceErrorInfo[] sourceErrors = null;
		try {
			Connection con = Transaction.getInstance(source.getDbConfig()).getConnection();
			String owner = source.getOracleSourceInfo().getOwner();
			String type = source.getOracleSourceInfo().getType();
			String name = source.getOracleSourceInfo().getName();
			sourceDetail = OracleSourceDetailSearcher.execute(con, owner, name, type, false);
			// sourceErrors = OracleSourceErrorSearcher.execute(con, owner, name, type);

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
		return sourceDetail;
	}


}
