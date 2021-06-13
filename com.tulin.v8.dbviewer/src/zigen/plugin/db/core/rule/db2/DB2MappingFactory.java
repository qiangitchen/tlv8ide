/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule.db2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import zigen.plugin.db.core.rule.DefaultMappingFactory;
import zigen.plugin.db.core.rule.IMappingFactory;


public class DB2MappingFactory extends DefaultMappingFactory implements IMappingFactory {


	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.");
	private DecimalFormat numberFormatter = new DecimalFormat("000000");


	public DB2MappingFactory(boolean convertUnicode) {
		super(convertUnicode);
	}

	protected String getTimestamp(ResultSet rs, int icol) throws SQLException {

		Timestamp value = rs.getTimestamp(icol);

		if (rs.wasNull()) {
			return nullSymbol;
		}
//		return timeStampFormat2.format(new Date(value.getTime()));
		return dateFormatter.format(value) + numberFormatter.format(value.getNanos() / 1000);

	}

	protected void setTimestamp(PreparedStatement pst, int icol, String str) throws Exception {
		if (nullSymbol.equals(str)) {
			pst.setNull(icol, Types.TIMESTAMP);
		} else {
			pst.setTimestamp(icol, toTimestamp3(str));
		}

	}

	private Timestamp toTimestamp3(String str) throws Exception {
		DateFormat df = null;
		Date date = null;


		switch (str.length()) {
		case 10:
			if (str.indexOf("/") > 0) {
				df = new SimpleDateFormat("yyyy/MM/dd");
			} else {
				df = new SimpleDateFormat("yyyy-MM-dd");
			}
			date = df.parse(str);
			return new Timestamp(date.getTime());

		case 19:
			if (str.indexOf("/") > 0) {
				df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			} else {
				df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}
			date = df.parse(str);
			return new Timestamp(date.getTime());

		case 23:
			if (str.indexOf("/") > 0) {
				df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
			} else {
				df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			}
			date = df.parse(str);
			return new Timestamp(date.getTime());

		case 26:
			// nanos yyyy/MM/dd HH:mm:ss.SSSSSS
			if (str.indexOf("/") > 0) {
				df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.");
			} else {
				df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.");
			}

			Timestamp ts = new Timestamp(df.parse(str.substring(0, 20)).getTime());
			ts.setNanos(numberFormatter.parse(str.substring(20)).intValue() * 1000);
			return ts;

		default:
			return null;
		}

	}


}
