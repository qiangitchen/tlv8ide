/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.SourceViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.core.SQLTokenizer;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.preference.SQLFormatPreferencePage;
import zigen.plugin.db.ui.views.SQLExecuteView;

public class SQLFormatAction extends Action implements Runnable {

	private SQLExecuteView view;

	public SQLFormatAction(SQLExecuteView view) {
		setText(Messages.getString("SQLFormatAction.0")); //$NON-NLS-1$
		setToolTipText(Messages.getString("SQLFormatAction.1")); //$NON-NLS-1$
		this.setActionDefinitionId("zigen.plugin.SQLFormatActionCommand"); //$NON-NLS-1$
		this.setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPlugin.IMG_CODE_FORMAT));
		// setAccelerator(SWT.CTRL | SWT.SHIFT | 'F');
		this.view = view;
	}

	public void run() {

		TimeWatcher tw = new TimeWatcher();

		try {
			SourceViewer sv = view.getSqlViewer();

			String preSql = sv.getDocument().get();
			StringBuffer sb = new StringBuffer();

			IPreferenceStore store = DbPlugin.getDefault().getPreferenceStore();
			String demiliter = store.getString(SQLEditorPreferencePage.P_SQL_DEMILITER);

			// boolean onPatch = store.getBoolean(SQLEditorPreferencePage.P_FORMAT_PATCH);
			boolean onPatch = DbPlugin.getDefault().getPreferenceStore().getBoolean(SQLFormatPreferencePage.P_FORMAT_PATCH);
			int type = DbPlugin.getDefault().getPreferenceStore().getInt(SQLFormatPreferencePage.P_USE_FORMATTER_TYPE);

			tw.start();
			SQLTokenizer st = new SQLTokenizer(preSql, demiliter);

			while (st.hasMoreElements()) {
				String sql = (String) st.nextElement();
				if (sql != null && sql.length() > 0) {
					// -----------------------------------------------
					// for BlancoSqlFormatter
					// -----------------------------------------------
					// sb.append(SQLFormatter.format(sql,onPatch));
					// sb.append(StringUtil.convertLineSep(SQLFormatter.format(sql, type, onPatch), DbPluginConstant.LINE_SEP));
					sb.append(SQLFormatter.format(sql, type, onPatch));

					if ("/".equals(demiliter)) { //$NON-NLS-1$
						sb.append(DbPluginConstant.LINE_SEP);
					}
					sb.append(demiliter);
					sb.append(DbPluginConstant.LINE_SEP);

				}

			}
			sv.getDocument().set(sb.toString());
			tw.stop();
			view.setStatusMessage("Complete SQL Format. " + tw.getTotalTime());

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		} finally {
			if (tw != null && tw.isStart()) {
				tw.stop();
			}
		}

	}

}
