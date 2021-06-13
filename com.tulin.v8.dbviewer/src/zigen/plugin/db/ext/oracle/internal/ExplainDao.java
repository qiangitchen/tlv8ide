/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.Transaction;

public class ExplainDao {

	public ExplainVo[] execute(IDBConfig config) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con);

		} catch (Exception e) {
			throw e;
		}
	}

	public ExplainVo[] execute(Connection con) throws Exception {
		List list = new ArrayList();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery(getQuery());
			while (rs.next()) {
				list.add(parse(rs));
			}
			return (ExplainVo[]) list.toArray(new ExplainVo[0]);

		} catch (Exception e) {
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}
	}

	private String getQuery() {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT"); //$NON-NLS-1$
		sb.append("         ID"); //$NON-NLS-1$
		sb.append("         ,PARENT_ID"); //$NON-NLS-1$
		sb.append("         ,POSITION"); //$NON-NLS-1$
		sb.append("         ,OPERATION"); //$NON-NLS-1$
		sb.append("         ,OPTIONS"); //$NON-NLS-1$
		sb.append("         ,OBJECT_NAME"); //$NON-NLS-1$
		sb.append("         ,OBJECT_TYPE"); //$NON-NLS-1$
		sb.append("         ,COST"); //$NON-NLS-1$
		sb.append("         ,CARDINALITY"); //$NON-NLS-1$
		sb.append("         ,BYTES"); //$NON-NLS-1$
		sb.append("         ,ACCESS_PREDICATES"); //$NON-NLS-1$
		sb.append("         ,FILTER_PREDICATES"); //$NON-NLS-1$
		sb.append("     FROM"); //$NON-NLS-1$
		sb.append("         PLAN_TABLE"); //$NON-NLS-1$
		sb.append("     ORDER BY"); //$NON-NLS-1$
		sb.append("         ID"); //$NON-NLS-1$
		sb.append("         ,PARENT_ID"); //$NON-NLS-1$
		return sb.toString();
	}

	public Object parse(ResultSet rs) throws Exception {
		ExplainVo vo = new ExplainVo();
		int i = 0;
		vo.setId(rs.getBigDecimal(++i));
		vo.setParent_id(rs.getBigDecimal(++i));
		vo.setPosition(rs.getBigDecimal(++i));
		vo.setOperation(rs.getString(++i));
		vo.setOptions(rs.getString(++i));
		vo.setObject_name(rs.getString(++i));
		vo.setObject_type(rs.getString(++i));
		vo.setCost(rs.getBigDecimal(++i));
		vo.setCardinality(rs.getBigDecimal(++i));
		vo.setBytes(rs.getBigDecimal(++i));
		vo.setAccess_predicates(rs.getString(++i));
		vo.setFilter_predicates(rs.getString(++i));
		return vo;
	}

}
