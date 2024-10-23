/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import zigen.plugin.db.ui.actions.ExecuteScriptAction;
import zigen.plugin.db.ui.editors.sql.IPlsqlEditor;
import zigen.plugin.db.ui.views.ISQLOperationTarget;

public class PLSQLSourceViewer extends SQLSourceViewer implements ISQLOperationTarget {

	private IPlsqlEditor plsqlEditor;

	public PLSQLSourceViewer(Composite parent, IVerticalRuler ruler, IOverviewRuler overviewRuler, boolean showsAnnotationOverview, int styles) {
		super(parent, ruler, overviewRuler, showsAnnotationOverview, styles);
	}

	public boolean canDoOperation(int operation) {

		switch (operation) {
		case ISQLOperationTarget.ALL_EXECUTE:
		case ISQLOperationTarget.CURRENT_EXECUTE:
		case ISQLOperationTarget.SELECTED_EXECUTE:
		case ISQLOperationTarget.SCRIPT_EXECUTE:
		case ISQLOperationTarget.LINE_DEL:
		case ISQLOperationTarget.COMMENT:
		case ISQLOperationTarget.FORMAT:
		case ISQLOperationTarget.UNFORMAT:

			return isEditable();

		case ISQLOperationTarget.NEXT_SQL:
		case ISQLOperationTarget.BACK_SQL:
			// case ISQLOperationTarget.FORMAT:
			// case ISQLOperationTarget.UNFORMAT:
		case ISQLOperationTarget.COMMIT:
		case ISQLOperationTarget.ROLLBACK:
			return false;

		default:
			return super.canDoOperation(operation);
		}

	}

	protected void doScriptExecute() {
		Display display = Display.getDefault();
		IPlsqlEditor editor = getPlsqlEditor();
		if (editor != null) {
			display.syncExec((Runnable) new ExecuteScriptAction(editor));
		} else {
			display.syncExec((Runnable) new ExecuteScriptAction(config, getDocument(), secondaryId));
		}
	}

	public IPlsqlEditor getPlsqlEditor() {
		return plsqlEditor;
	}

	public void setPlsqlEditor(IPlsqlEditor plsqlEditor) {
		this.plsqlEditor = plsqlEditor;
	}

}
