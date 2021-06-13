/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.csv;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;

public class CSVWriter {

	private String DEMILITER = ","; //$NON-NLS-1$

	private IDBConfig config = null;

	private CSVConfig csvConfig = null;

	private ICsvMappingFactory factory = null;

	public CSVWriter(IDBConfig config, CSVConfig csvConfig) {
		this.config = config;
		this.csvConfig = csvConfig;
		this.factory = AbstractCsvMappingFactory.getFactory(config, csvConfig.isNonDoubleQuate());


		if (csvConfig.getSeparator() != null && !"".equals(csvConfig)) {
			this.DEMILITER = csvConfig.getSeparator();
		}

	}


	public void execute() throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			execute(con, csvConfig.getQuery());
		} catch (Exception e) {
			throw e;
		}
	}


	public void execute(Connection con, String query) throws Exception {
		ResultSet rs = null;
		Statement stmt = null;

		PrintStream pout = null;

		try {
			pout = new PrintStream(new FileOutputStream(csvConfig.getCsvFile()), true, csvConfig.getCsvEncoding());

			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			ResultSetMetaData meta = rs.getMetaData();

			if (!csvConfig.isNonHeader()) {
				writeColumnLabel(pout, meta);
			}

			writeColumnValue(pout, rs);

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (pout != null)
				pout.close();
		}

	}

	private void writeColumnLabel(PrintStream pout, ResultSetMetaData meta) throws SQLException {
		int size = meta.getColumnCount();
		for (int i = 0; i < size; i++) {
			if (i == size - 1) {
				pout.println("\"" + meta.getColumnLabel(i + 1) + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				pout.print("\"" + meta.getColumnLabel(i + 1) + "\"" + DEMILITER); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

	private int writeColumnValue(PrintStream pout, ResultSet rs) throws Exception {
		ResultSetMetaData meta = rs.getMetaData();
		int size = meta.getColumnCount();
		int recordNo = 1;
		String value;
		while (rs.next()) {
			for (int i = 0; i < size; i++) {
				value = factory.getCsvValue(rs, i + 1);
				if (i == size - 1) {
					pout.println(value);
				} else {
					pout.print(value + DEMILITER);
				}
			}
			recordNo++;

		}
		return recordNo;
	}
}
