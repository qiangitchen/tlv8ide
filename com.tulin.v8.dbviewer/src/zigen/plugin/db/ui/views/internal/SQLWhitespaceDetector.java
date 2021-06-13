/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class SQLWhitespaceDetector implements IWhitespaceDetector {

	public SQLWhitespaceDetector() {}

	public boolean isWhitespace(char c) {
		// return Character.isWhitespace(c);
		// return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '(' || c == ')' || c == ',');

	}
}
