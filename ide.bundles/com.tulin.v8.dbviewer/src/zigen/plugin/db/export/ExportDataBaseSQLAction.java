/*
 * TLV8 DbViewer - Export whole database/schema/folder (tables & views) as SQL file
 *
 */
package zigen.plugin.db.export;

import java.util.List;

import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.Schema;

/**
 * Popup action: export all tables and views under the selected DataBase /
 * Schema / Folder node as a single SQL script. Table list is resolved from
 * JDBC metadata in the background job (works even if the schema tree has not
 * been expanded).
 *
 */
public class ExportDataBaseSQLAction extends AbstractExportSQLAction {

	protected List collectExportElements() {
		List result = getSelectedInstances(DataBase.class);
		result.addAll(getSelectedInstances(Schema.class));
		result.addAll(getSelectedInstances(Folder.class));
		return result;
	}

	protected String suggestFileName(List exportElements) {
		if (exportElements.size() == 1) {
			Object element = exportElements.get(0);
			if (element instanceof DataBase) {
				return ((DataBase) element).getName() + ".sql"; //$NON-NLS-1$
			}
			return suggestForContainer(element);
		}
		return "database_export.sql"; //$NON-NLS-1$
	}

}
