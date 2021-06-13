/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.wizard;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.Item;

public class ExpWizardPage1CellModifier implements ICellModifier {

	private ExpWizardPage1 page;

	public ExpWizardPage1CellModifier(ExpWizardPage1 page) {
		this.page = page;
	}

	public boolean canModify(Object element, String property) {
		return true;
	}

	public Object getValue(Object element, String property) {
		TableItem item = (TableItem) element;
		// return item.getText();

		if (property == "check") { //$NON-NLS-1$
			return new Boolean(item.isChecked());
		} else {
			return item.getDbName();
		}
	}

	public void modify(Object element, String property, Object value) {
		if (element instanceof Item) {
			element = ((Item) element).getData();
		}
		TableItem item = (TableItem) element;

		if (property == "check") { //$NON-NLS-1$
			item.setChecked(((Boolean) value).booleanValue());

			if (page.isSelected()) {
				page.updateStatus(null);
			} else {
				page.updateStatus(Messages.getString("ExpWizardPage1CellModifier.2")); //$NON-NLS-1$
			}
		}

		page.tableViewer.update(element, null);

	}

}
