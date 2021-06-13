/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

public class SameDbNameException extends Throwable {

	private static final long serialVersionUID = 1L;

	public SameDbNameException(String message) {
		super(message);
	}
}
