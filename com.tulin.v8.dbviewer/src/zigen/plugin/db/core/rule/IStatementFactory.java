/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import java.sql.SQLException;

public interface IStatementFactory {

	public String getString(int DataType, Object value) throws SQLException;

	public char getEncloseChar();
}
