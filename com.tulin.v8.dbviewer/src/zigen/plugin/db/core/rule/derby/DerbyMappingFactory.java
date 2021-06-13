/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule.derby;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import zigen.plugin.db.core.rule.DefaultMappingFactory;
import zigen.plugin.db.core.rule.IMappingFactory;

public class DerbyMappingFactory extends DefaultMappingFactory implements IMappingFactory {

	public static final int TYPES_DERBY_ORG_APACHE_DERBY_CATALOG_ALISASINFO = -4;

	public DerbyMappingFactory(boolean convertUnicode) {
		super(convertUnicode);
	}

	public Object getObject(ResultSet rs, int icol) throws SQLException {
		ResultSetMetaData rmd = rs.getMetaData();
		int type = rmd.getColumnType(icol);
		switch (type) {
		case Types.LONGVARBINARY: // -4
			return "<<UnSuport(org.apache.derby.catalog.aliasinfo)>>"; //$NON-NLS-1$

		default:
			return super.getObject(rs, icol);
		}
	}

}
