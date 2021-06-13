/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetUtil {

	private ResultSetUtil() {}

	public static final void close(ResultSet rs) {
		if (rs == null)
			return;

		try {
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
