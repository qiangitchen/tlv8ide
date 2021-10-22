/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import java.sql.Connection;
import java.util.HashMap;

public class OracleTypeSizeUtil {

	public static final String UB1 = "UB1"; //$NON-NLS-1$

	public static final String UB4 = "UB4"; //$NON-NLS-1$

	public static final String SB2 = "SB2"; //$NON-NLS-1$

	public static final String KCBH = "KCBH"; //$NON-NLS-1$

	public static final String KTBIT = "KTBIT"; //$NON-NLS-1$

	public static final String KTBBH = "KTBBH"; //$NON-NLS-1$

	public static final String KDBH = "KDBH"; //$NON-NLS-1$

	public static final String KDBT = "KDBT"; //$NON-NLS-1$

	private HashMap map = null;

	public OracleTypeSizeUtil(Connection con) throws Exception {
		map = OracleTypeSizeSearcher.execute(con);
	}

	public int getInt(String key) {
		return ((Integer) map.get(key)).intValue();
	}

}
