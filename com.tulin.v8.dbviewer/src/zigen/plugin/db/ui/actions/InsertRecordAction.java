/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.viewers.TableViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TableNewElement;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.editors.internal.TableViewerManager;
import zigen.plugin.db.ui.internal.ITable;

public class InsertRecordAction extends TableViewEditorAction {

	public InsertRecordAction() {
		this.setText(Messages.getString("InsertRecordAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("InsertRecordAction.1")); //$NON-NLS-1$
		this.setActionDefinitionId("zigen.plugin.InsertRecordCommand"); //$NON-NLS-1$
		// this.setImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD);
		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_ADD));

	}

	public void run() {
		String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);
		ITable table = editor.getTableNode();
		TableViewer viewer = editor.getViewer();
		TableElement elem = editor.getHeaderTableElement();
		int count = viewer.getTable().getItems().length;

		Object[] items = new Object[elem.getColumns().length];
		for (int i = 0; i < items.length; i++) {
			TableColumn column = elem.getColumns()[i];
			items[i] = getDefaultValue(column);
		}

		// NewRecord
		// log.debug(elem.getUniqueColumns());
		TableElement newElement = new TableNewElement(table, count + 1, elem.getColumns(), items, elem.getUniqueColumns());

		TableViewerManager.insert(viewer, newElement);

		editor.editTableElement(newElement, 1);

	}

	public static String getDefaultValue(TableColumn column) {
		String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);

		String defaultValue = column.getDefaultValue();

		if (defaultValue != null && !"".equals(defaultValue)) { //$NON-NLS-1$

			if (defaultValue.matches("^'.*'$")) { //$NON-NLS-1$
				defaultValue = defaultValue.replaceAll("^'|'$", "");//$NON-NLS-1$ //$NON-NLS-2$
				defaultValue = defaultValue.replaceAll("''", "'");//$NON-NLS-1$ //$NON-NLS-2$
				return defaultValue;
			} else if (defaultValue.matches("^\\(.*\\)$")) { //$NON-NLS-1$
				defaultValue = defaultValue.replaceAll("^\\(|\\)$", "");//$NON-NLS-1$ //$NON-NLS-2$

				if (defaultValue.matches("^'.*'$")) { //$NON-NLS-1$
					defaultValue = defaultValue.replaceAll("^'|'$", "");//$NON-NLS-1$ //$NON-NLS-2$
					defaultValue = defaultValue.replaceAll("''", "'");//$NON-NLS-1$ //$NON-NLS-2$
				}

				return defaultValue;

			} else {
				if (defaultValue.equalsIgnoreCase("NULL")) { //$NON-NLS-1$
					return nullSymbol;
				} else if (StringUtil.isNumeric(defaultValue.trim())) {
					return defaultValue.trim();
				} else {
					return ""; //$NON-NLS-1$
				}
			}

		} else {
			if (!column.isNotNull()) {
				return nullSymbol;
			} else {
				return ""; //$NON-NLS-1$
			}
		}
	}

}
