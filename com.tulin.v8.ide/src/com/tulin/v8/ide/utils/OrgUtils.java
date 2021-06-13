package com.tulin.v8.ide.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tulin.v8.core.DBUtils;
import com.tulin.v8.core.StringArray;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class OrgUtils {

	public static List<Map> getOrgTreeRootData(String rootFilter,
			String filter, String orderby) {
		List<Map> rli = new ArrayList<Map>();
		String sql = "select * from sa_oporg_view where 1=1 ";
		if (rootFilter != null && !"".equals(rootFilter)) {
			sql += " and (" + rootFilter + ")";
		}
		if (filter != null && !"".equals(filter)) {
			sql += " and (" + filter + ")";
		}
		if (orderby != null && !"".equals(orderby)) {
			sql += " order by " + orderby;
		}
		try {
			rli = DBUtils.execQueryforList("system", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rli;
	}

	public static List<Map> getOrgTreeChiledData(String parent,
			String rootFilter, String filter, String orderby) {
		List<Map> rli = new ArrayList<Map>();
		String sql = "select * from sa_oporg_view where sParent = '" + parent
				+ "' ";
		if (filter != null && !"".equals(filter)) {
			sql += " and (" + filter + ")";
		}
		if (orderby != null && !"".equals(orderby)) {
			sql += " order by " + orderby;
		}
		try {
			rli = DBUtils.execQueryforList("system", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rli;
	}

	public static boolean haveChildren(String parent) {
		String sql = "select sID from sa_oporg_view where sParent = '" + parent
				+ "' ";
		boolean res = false;
		try {
			List<Map> rli = DBUtils.execQueryforList("system", sql);
			if (rli.size() > 0)
				res = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public static List<Map> getOrgViewSearchList(String searchText,
			String filter) {
		List<Map> rli = new ArrayList<Map>();
		String sql = "select * from sa_oporg_view where (sNAME like '%"
				+ searchText + "%' or sCode like '%" + searchText + "%')";
		if (filter != null && !"".equals(filter)) {
			sql += " and (" + filter + ")";
		}
		try {
			rli = DBUtils.execQueryforList("system", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rli;
	}

	public static List<Map> getOrgViewPersonList(String sfid) {
		List<Map> rli = new ArrayList<Map>();
		String sql = "select * from sa_oporg_view where SFID like '" + sfid
				+ "%' and sOrgKindID='psm'";
		try {
			rli = DBUtils.execQueryforList("system", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rli;
	}

	public static List<Map> getOrgViewPersonList(String sfid, String searchText) {
		List<Map> rli = new ArrayList<Map>();
		String sql = "select * from sa_oporg_view where SFID like '" + sfid
				+ "%' and sOrgKindID='psm' and (sName like '%" + searchText
				+ "%' or sCode like '%" + searchText + "%')";
		try {
			rli = DBUtils.execQueryforList("system", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rli;
	}

	public static List<Map> getOrgViewPersonList(StringArray ids) {
		List<Map> rli = new ArrayList<Map>();
		String sql = "select * from sa_oporg_view where SID in ('"
				+ ids.join("','") + "') and sOrgKindID='psm'";
		try {
			rli = DBUtils.execQueryforList("system", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rli;
	}
}
