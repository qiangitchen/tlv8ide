/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.util.List;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.views.internal.ParseSqlThread;
import zigen.sql.parser.INode;
import zigen.sql.parser.ast.ASTParentheses;

public class UpdateSQLFoldingJob extends AbstractJob {

	IDocument document;

	int offset;

	ProjectionAnnotationModel model;

	IPreferenceStore st;

	IStatusLineManager statusLineManager;

	public UpdateSQLFoldingJob(ProjectionAnnotationModel model, IDocument document, int offset, IStatusLineManager statusLineManager) {
		super(Messages.getString("UpdateSQLFoldingJob.0")); //$NON-NLS-1$
		this.model = model;
		this.document = document;
		this.offset = offset;
		this.st = DbPlugin.getDefault().getPreferenceStore();
		this.statusLineManager = statusLineManager;
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {

			monitor.beginTask("Update Folding...", IProgressMonitor.UNKNOWN);
			ParseSqlThread t = new ParseSqlThread(document, offset);
			Thread th = new Thread(t);
			th.setPriority(Thread.MIN_PRIORITY);
			th.start();
			int timeout = 5;
			if (timeout > 0) {
				th.join(timeout * 1000);
			} else {
				th.join();
			}
			if (!t.isComplete()) {
				showWarningMessage(statusLineManager, "Update Folding is timeout.");
				return Status.CANCEL_STATUS;
			}

			monitor.done();
			showResults(new UpdateFoldingAction(t.getNode(), t.getBeginOffset()));
			showMessage(statusLineManager, "Update Folding is complete.");

			return Status.OK_STATUS;

		} catch (Exception e) {
			e.printStackTrace();

		}
		return Status.OK_STATUS;

	}

	protected class UpdateFoldingAction implements Runnable {

		INode node;

		int beginOffset;

		public UpdateFoldingAction(INode node, int beginOffset) {
			this.node = node;
			this.beginOffset = beginOffset;
		}

		public void run() {
			try {
				if (model == null || node == null) {
					return;
				}
				model.removeAllAnnotations();
				applyFolding(model, node);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		private void applyFolding(ProjectionAnnotationModel model, INode node) {
			Position position = null;
			try {

				if (node instanceof ASTParentheses) {
					ASTParentheses p = (ASTParentheses) node;

					if (!p.isForFunction()) {
						int _offset = beginOffset + p.getOffset();
						int _length = p.getEndOffset() - p.getOffset() + 1;

						if (_offset >= 0 && _length >= 0) {
							position = new Position(_offset, _length);
							model.addAnnotation(new ProjectionAnnotation(), position);

						}
					}
				}

				List list = node.getChildren();
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						INode _node = node.getChild(i);
						applyFolding(model, _node);
					}
				}

			} catch (AssertionFailedException e) {
				;
			} catch (Exception e) {
				e.printStackTrace();
				if (position != null) {
					System.err.println("Folding Error " + position.getOffset() + ", " + position.getLength());
				};
				// DbPlugin.log(e);
			}
		}

	}
}
