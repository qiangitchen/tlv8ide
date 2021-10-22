/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.core.TableElement;

public class TableViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	public String getColumnText(Object obj, int index) {
		try {
			if (obj instanceof TableElement) {
				TableElement element = (TableElement) obj;

				if (index == 0) {
					if (element.isNew()) {
						return "*"; //$NON-NLS-1$

					} else {

						if (element.isModify()) {
							return String.valueOf("*" + element.getRecordNo()); //$NON-NLS-1$
							// return String.valueOf(element.getRecordNo()); //$NON-NLS-1$

						} else {
							return String.valueOf(" " + element.getRecordNo()); //$NON-NLS-1$
							// return String.valueOf(element.getRecordNo()); //$NON-NLS-1$

						}
					}

				} else {

					Object elem = element.getItems()[index - 1];
					// TableColumn colum = element.getColumns()[index-1];

					if (elem != null) {
						//return elem.toString();
						return StringUtil.rTrim(elem.toString(), ' ');

					} else {
						// return "";
						return null;

					}
				}

			} else {
				throw new RuntimeException("It is a type not anticipated."); //$NON-NLS-1$
			}

		} catch (Exception e) {
			DbPlugin.log(e);
		}
		return null;

	}

	public Image getColumnImage(Object obj, int index) {
		return getImage(obj);
	}

	public Image getImage(Object obj) {
		return null;
	}
}
