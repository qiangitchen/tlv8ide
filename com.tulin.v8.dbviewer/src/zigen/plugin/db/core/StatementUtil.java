/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.SQLException;
import java.sql.Statement;

public class StatementUtil {

	private StatementUtil() {}

	public static final void close(Statement st) {
		if (st == null)
			return;

		try {
			st.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
