package com.tulin.v8.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import com.tulin.v8.core.DBUtils;
import com.tulin.v8.core.StringArray;

/**
 * 查询组织信息
 * 
 * @author 陈乾
 *
 */
public class OrgUtils {

	public static List<Map<String, String>> getOrgTreeRootData(String rootFilter, String filter, String orderby) {
		List<Map<String, String>> rli = new ArrayList<Map<String, String>>();
//		String sql = "select * from sa_oporg_view where 1=1 ";
		SQL sql = new SQL().SELECT("*");
		if (rootFilter != null && !"".equals(rootFilter)) {
//			sql += " and (" + rootFilter + ")";
			sql.WHERE(rootFilter);
		}
		if (filter != null && !"".equals(filter)) {
//			sql += " and (" + filter + ")";
			sql.WHERE(filter);
		}
		if (orderby != null && !"".equals(orderby)) {
//			sql += " order by " + orderby;
			sql.ORDER_BY(orderby);
		}
		try {
			rli = DBUtils.execQueryforList("system", sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rli;
	}

	public static List<Map<String, String>> getOrgTreeChiledData(String parent, String rootFilter, String filter,
			String orderby) {
		List<Map<String, String>> rli = new ArrayList<Map<String, String>>();
//		String sql = "select * from sa_oporg_view where sParent = '" + parent + "' ";
		SQL sql = new SQL().SELECT("*");
		sql.FROM("sa_oporg_view");
		sql.WHERE("sParent='" + parent + "'");
		if (filter != null && !"".equals(filter)) {
//			sql += " and (" + filter + ")";
			sql.WHERE(filter);
		}
		if (orderby != null && !"".equals(orderby)) {
//			sql += " order by " + orderby;
			sql.ORDER_BY(orderby);
		}
		try {
			rli = DBUtils.execQueryforList("system", sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rli;
	}

	public static boolean haveChildren(String parent) {
//		String sql = "select sID from sa_oporg_view where sParent = '" + parent + "' ";
		SQL sql = new SQL();
		sql.SELECT("sID");
		sql.FROM("sa_oporg_view");
		sql.WHERE("sParent=" + parent + "'");
		boolean res = false;
		try {
			List<Map<String, String>> rli = DBUtils.execQueryforList("system", sql.toString());
			if (rli.size() > 0)
				res = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public static List<Map<String, String>> getOrgViewSearchList(String searchText, String filter) {
		List<Map<String, String>> rli = new ArrayList<Map<String, String>>();
//		String sql = "select * from sa_oporg_view where (sNAME like '%" + searchText + "%' or sCode like '%"
//				+ searchText + "%')";
		SQL sql = new SQL();
		sql.SELECT("*");
		sql.FROM("sa_oporg_view");
		sql.WHERE("(sNAME like '%" + searchText + "%' or sCode like '%" + searchText + "%')");
		if (filter != null && !"".equals(filter)) {
//			sql += " and (" + filter + ")";
			sql.WHERE(filter);
		}
		try {
			rli = DBUtils.execQueryforList("system", sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rli;
	}

	public static List<Map<String, String>> getOrgViewPersonList(String sfid) {
		List<Map<String, String>> rli = new ArrayList<Map<String, String>>();
//		String sql = "select * from sa_oporg_view where SFID like '" + sfid + "%' and sOrgKindID='psm'";
		SQL sql = new SQL();
		sql.SELECT("*");
		sql.FROM("sa_oporg_view");
		sql.WHERE("sOrgKindID='psm'");
		sql.WHERE("SFID like '" + sfid + "%'");
		try {
			rli = DBUtils.execQueryforList("system", sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rli;
	}

	public static List<Map<String, String>> getOrgViewPersonList(String sfid, String searchText) {
		List<Map<String, String>> rli = new ArrayList<Map<String, String>>();
//		String sql = "select * from sa_oporg_view where SFID like '" + sfid
//				+ "%' and sOrgKindID='psm' and (sName like '%" + searchText + "%' or sCode like '%" + searchText
//				+ "%')";
		SQL sql = new SQL();
		sql.SELECT("*");
		sql.FROM("sa_oporg_view");
		sql.WHERE("sOrgKindID='psm'");
		sql.WHERE("SFID like '" + sfid + "'");
		sql.WHERE("(sName like '%" + searchText + "%' or sCode like '%" + searchText + "%')");
		try {
			rli = DBUtils.execQueryforList("system", sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rli;
	}

	public static List<Map<String, String>> getOrgViewPersonList(StringArray ids) {
		List<Map<String, String>> rli = new ArrayList<Map<String, String>>();
//		String sql = "select * from sa_oporg_view where SID in ('" + ids.join("','") + "') and sOrgKindID='psm'";
		SQL sql = new SQL();
		sql.SELECT("*");
		sql.FROM("sa_oporg_view");
		sql.WHERE("sOrgKindID='psm'");
		sql.WHERE("SID in ('" + ids.join("','") + "')");
		try {
			rli = DBUtils.execQueryforList("system", sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rli;
	}
}
