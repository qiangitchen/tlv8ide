/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.exceptions;

public class NotFoundDBConfigException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotFoundDBConfigException(String message) {
		super(message);
	}

}
