/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.tablespace;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ext.oracle.internal.OracleIndexColumnSearcher;
import zigen.plugin.db.ext.oracle.internal.OracleIndexNameSearcher;
import zigen.plugin.db.ui.internal.Table;

public class CalcIndexSpaces {

	private Table table;

	private int dbBlockSize;

	private int pctFree;

	private long maxRecord;

	private CalcIndexSpace[] calcIndexSpaces = null;

	public CalcIndexSpaces(Table table, int pctFree, long maxRecord) {
		this.table = table;
		this.pctFree = pctFree;
		this.maxRecord = maxRecord;
	}

	public CalcIndexSpaces(Table table, int dbBlockSize, int pctFree, long maxRecord) {
		this.table = table;
		this.dbBlockSize = dbBlockSize;
		this.pctFree = pctFree;
		this.maxRecord = maxRecord;
	}

	public void calcurate() throws CalcTableSpaceException {
		try {

			IDBConfig config = table.getDbConfig();
			Connection con = Transaction.getInstance(config).getConnection();

			String[] indexes = OracleIndexNameSearcher.execute(con, table);

			List wk = new ArrayList();

			for (int i = 0; i < indexes.length; i++) {
				OracleIndexColumn[] indexColumns = OracleIndexColumnSearcher.execute(con, table, indexes[i], config.isConvertUnicode());
				CalcIndexSpace calcIndexSpace;
				if (dbBlockSize > 0) {
					calcIndexSpace = new CalcIndexSpace(table, dbBlockSize, indexes[i], indexColumns, pctFree, maxRecord);
				} else {
					calcIndexSpace = new CalcIndexSpace(table, indexes[i], indexColumns, pctFree, maxRecord);
				}

				calcIndexSpace.calcurate();

				wk.add(calcIndexSpace);

			}

			this.calcIndexSpaces = (CalcIndexSpace[]) wk.toArray(new CalcIndexSpace[0]);

		} catch (CalcTableSpaceException e) {
			throw e;

		} catch (Exception e) {
			throw new CalcTableSpaceException("The error occurred by the estimate processing in the table area.", e.getCause()); //$NON-NLS-1$

		}

	}

	public String getCalcResult() throws Exception {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < calcIndexSpaces.length; i++) {
			CalcIndexSpace elem = calcIndexSpaces[i];
			sb.append(elem.getCalcResult());
		}
		return sb.toString();
	}

	public List getList() {
		List elements = new ArrayList(calcIndexSpaces.length);
		for (int i = 0; i < calcIndexSpaces.length; i++) {
			elements.add(calcIndexSpaces[i].getCsvRow());
		}
		return elements;
	}

	public CalcIndexSpace[] getCalcIndexSpaces() {
		return calcIndexSpaces;
	}
}
