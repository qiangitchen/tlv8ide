/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule.symfoware;

import java.sql.SQLException;

import zigen.plugin.db.core.rule.DefaultStatementFactory;

public class SymfowareStatementFactory extends DefaultStatementFactory {

	public SymfowareStatementFactory(boolean convertUnicode) {
		super(convertUnicode);
	}

	// protected String getDate(Object value) throws SQLException {
	// if (value == null)
	// return NULL;
	// return "to_date('" + value + "','YYYY-MM-DD HH24:MI:SS')";
	// }

	protected String getTimestamp(Object value) throws SQLException {
		if (value == null)
			return NULL;

		return "CAST('" + value + "' AS TIMESTAMP)";
	}
}
