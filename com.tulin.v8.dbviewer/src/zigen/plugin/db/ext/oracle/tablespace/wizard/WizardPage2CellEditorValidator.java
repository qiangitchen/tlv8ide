/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.tablespace.wizard;

import org.eclipse.jface.viewers.ICellEditorValidator;

import zigen.plugin.db.core.rule.Validator;

public class WizardPage2CellEditorValidator implements ICellEditorValidator {

	int columnIndex;

	public WizardPage2CellEditorValidator(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public String isValid(Object value) {
		String input = String.valueOf(value);
		String msg = validate(input);
		return msg;

	}

	private String validate(String input) {
		String columnName;
		String msg = null;

		switch (columnIndex) {
		case 1:
			columnName = WizardPage2.HEADER_PCTFREE;
			if ((msg = Validator.entry_Check(columnName, input)) != null) {
				return msg;
			} else if ((msg = Validator.numeric_Check(columnName, input)) != null) {
				return msg;
			}
		case 2:
			columnName = WizardPage2.HEADER_RECORD;
			if ((msg = Validator.numeric_Check(columnName, input)) != null) {
				return msg;
			} else if ((msg = Validator.numeric_Check(columnName, input)) != null) {
				return msg;
			}

			break;

		default:
			break;
		}

		return msg;
	}

}
