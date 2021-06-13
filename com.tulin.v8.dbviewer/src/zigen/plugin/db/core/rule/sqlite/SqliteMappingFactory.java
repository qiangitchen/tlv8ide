package zigen.plugin.db.core.rule.sqlite;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import zigen.plugin.db.core.rule.DefaultMappingFactory;

public class SqliteMappingFactory extends DefaultMappingFactory {
	
	public static final int VARCHAR = 0;
	
	public SqliteMappingFactory(boolean convertUnicode) {
		super(convertUnicode);
	}
	
	public Object getObject(ResultSet rs, int icol) throws SQLException {
		ResultSetMetaData rmd = rs.getMetaData();
		int type = rmd.getColumnType(icol);
		switch (type) {

		case 0:	// 無理やりStringとして扱ってみた。
			return getString(rs, icol);

		default:
			return super.getObject(rs, icol);
		}
	}
	
}
