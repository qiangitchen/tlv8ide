/*
 * TLV8 DbViewer - Export selected tables/views DDL as SQL file
 *
 */
package zigen.plugin.db.export;

import java.util.List;

import zigen.plugin.db.ui.internal.ITable;

/**
 * Popup action: export the selected table/view nodes as SQL script.
 * Registered for zigen.plugin.db.ui.internal.ITable (covers Table and View).
 *
 */
public class ExportTableSQLAction extends AbstractExportSQLAction {

	protected List collectExportElements() {
		return getSelectedInstances(ITable.class);
	}

	protected String suggestFileName(List exportElements) {
		if (exportElements.size() == 1) {
			return suggestForContainer(exportElements.get(0));
		}
		return "tables_export.sql"; //$NON-NLS-1$
	}

}
