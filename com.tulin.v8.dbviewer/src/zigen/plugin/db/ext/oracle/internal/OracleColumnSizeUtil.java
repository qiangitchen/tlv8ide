/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import java.sql.Connection;

import zigen.plugin.db.ext.oracle.tablespace.IColumn;

public class OracleColumnSizeUtil {

	private OracleTypeSizeUtil ts;

	public OracleColumnSizeUtil() {}

	public int getRowLength(Connection con, IColumn[] columns) throws Exception {
		this.ts = new OracleTypeSizeUtil(con);

		// UB1*3+UB4+SB2
		int a = ts.getInt(OracleTypeSizeUtil.UB1) * 3 + ts.getInt(OracleTypeSizeUtil.UB4) + +ts.getInt(OracleTypeSizeUtil.SB2);

		int b = sumColumnSize(columns);

		int out = 0;
		if (a > b) {
			out = a + ts.getInt(OracleTypeSizeUtil.SB2);
		} else {
			out = b + ts.getInt(OracleTypeSizeUtil.SB2);
		}

		return out;
	}

	private int getOverHead(int length) {
		if (length <= 255) {
			return 1;
		} else {
			return 3;
		}
	}

	private int getColumnSize(String columnType, int length) {
		String type = columnType.toUpperCase();
		if ("CHAR".equals(type)) { //$NON-NLS-1$
			return length;
		} else if ("VARCHAR2".equals(type)) { //$NON-NLS-1$
			return length;
		} else if ("NUMBER".equals(type)) { //$NON-NLS-1$
			return (1 + (int) Math.ceil(length / 2)) + 1;
		} else if ("DATE".equals(type)) { //$NON-NLS-1$
			return 7;
		} else {
			throw new IllegalStateException("UnSupport type:" + columnType); //$NON-NLS-1$
		}

	}

	private int getColumnSizeAddOverHead(String columnType, int length) {
		int normal = getColumnSize(columnType, length);
		int out = normal + getOverHead(normal);
		return out;
	}

	private int sumColumnSize(IColumn[] columns) throws Exception {
		int columnHeader = 3 * ts.getInt(OracleTypeSizeUtil.UB1);

		int sum = 0;
		for (int i = 0; i < columns.length; i++) {
			IColumn column = columns[i];
			// log.debug(column.getColumn_name() + ":" + column.getColumn_type()
			// + "("+ column.getColumn_length() + ")");
			sum = sum + getColumnSizeAddOverHead(column.getColumn_type(), column.getColumn_length());
		}
		int out = columnHeader + sum;
		return out;

	}

}
