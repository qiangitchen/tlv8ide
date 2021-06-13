/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import zigen.plugin.db.core.TableElement;

public class MaxRecordException extends Throwable {

	private static final long serialVersionUID = 1L;

	TableElement[] elements = null;

	public MaxRecordException(String message, TableElement[] elements) {
		super(message);
		this.elements = elements;
	}

	public TableElement[] getTableElements() {
		return this.elements;
	}

}
