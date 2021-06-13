/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.exceptions;

public class UpdateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UpdateException(String message, Throwable cause) {
		super(message, cause);
	}

}
