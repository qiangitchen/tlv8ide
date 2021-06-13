/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.tablespace.wizard;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.Item;

import zigen.plugin.db.DbPlugin;

public class WizardPage2CellModifier implements ICellModifier {

	private WizardPage2 page;

	public WizardPage2CellModifier(WizardPage2 page) {
		this.page = page;
	}

	public boolean canModify(Object element, String property) {
		return true;
	}

	public Object getValue(Object element, String property) {
		IItem item = (IItem) element;
		if (property == WizardPage2.HEADER_PCTFREE) {
			return String.valueOf(item.getPctFree());

		} else if (property == WizardPage2.HEADER_RECORD) {
			return String.valueOf(item.getRecordSize());

		} else {
			return null;
		}
	}

	public void modify(Object element, String property, Object value) {
		if (element instanceof Item) {
			element = ((Item) element).getData();
		}

		TableItem item = (TableItem) element;

		if (property == WizardPage2.HEADER_PCTFREE) {
			String wk = String.valueOf(value);

			if (validate(1)) {
				item.setPctFree(Integer.parseInt(wk));
			}

		} else if (property == WizardPage2.HEADER_RECORD) {
			String wk = String.valueOf(value);
			if (validate(2)) {
				item.setRecordSize(Long.parseLong(wk));
			}
		}

		page.tableViewer.update(element, null);
		page.updateStatus(null);
	}

	private boolean validate(int columnIndex) {
		String msg = page.tableViewer.getCellEditors()[columnIndex].getErrorMessage();
		if (msg != null) {
			DbPlugin.getDefault().showWarningMessage(msg);
			return false;
		}
		return true;
	}

}
