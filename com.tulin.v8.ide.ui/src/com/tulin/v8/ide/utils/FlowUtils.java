package com.tulin.v8.ide.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tulin.v8.core.DBUtils;
import com.tulin.v8.core.utils.IDUtils;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FlowUtils {

	public static List<Map> getRootFolder() {
		List<Map> rli = new ArrayList<Map>();
		String sql = "select * from sa_flowfolder where SPARENT is null ";
		try {
			rli = DBUtils.execQueryforList("system", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rli;
	}

	public static List<Map> getChildFolder(String parent) {
		List<Map> rli = new ArrayList<Map>();
		String sql = "select * from sa_flowfolder where SPARENT = '" + parent
				+ "'";
		try {
			rli = DBUtils.execQueryforList("system", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rli;
	}

	public static boolean addRootFolder(String code, String name) {
		boolean res = false;
		String sql = "insert into sa_flowfolder(SID,SCODE,SNAME,SIDPATH,SCODEPATH,SNAMEPATH,VERSION)values(?,?,?,?,?,?,0)";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			String id = IDUtils.getGUID();
			conn = DBUtils.getAppConn("system");
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, code);
			ps.setString(3, name);
			ps.setString(4, "/" + id);
			ps.setString(5, "/" + code);
			ps.setString(6, "/" + name);
			ps.executeUpdate();
			res = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.CloseConn(conn, ps, null);
		}
		return res;
	}

	public static boolean addChildFolder(String code, String name, String pid,
			String pfid, String pfcode, String pfname) {
		boolean res = false;
		String sql = "insert into sa_flowfolder(SID,SCODE,SNAME,SIDPATH,SCODEPATH,SNAMEPATH,SPARENT,VERSION)values(?,?,?,?,?,?,?,0)";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			String id = IDUtils.getGUID();
			conn = DBUtils.getAppConn("system");
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, code);
			ps.setString(3, name);
			ps.setString(4, pfid + "/" + id);
			ps.setString(5, pfcode + "/" + code);
			ps.setString(6, pfname + "/" + name);
			ps.setString(7, pid);
			ps.executeUpdate();
			res = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.CloseConn(conn, ps, null);
		}
		return res;
	}

	public static boolean removeFlowFolder(String id) {
		boolean res = false;
		String sql = "delete from sa_flowfolder where SID = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBUtils.getAppConn("system");
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.executeUpdate();
			res = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.CloseConn(conn, ps, null);
		}
		return res;
	}

	public static List<Map> getDrawByFolderId(String folderid) {
		List<Map> rli = new ArrayList<Map>();
		String sql = "select SID,SFOLDERID,SPROCESSID,SPROCESSNAME,SPROCESSACTY,FENABLED,VERSION from sa_flowdrawlg where SFOLDERID = '"
				+ folderid + "'";
		try {
			rli = DBUtils.execQueryforList("system", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rli;
	}

	public static Map getDrawById(String sid) {
		Map rmap = new HashMap();
		String sql = "select SID,SFOLDERID,SPROCESSID,SPROCESSNAME,SPROCESSACTY,FENABLED,VERSION from sa_flowdrawlg where SID = '"
				+ sid + "'";
		try {
			List<Map> rli = DBUtils.execQueryforList("system", sql);
			if (rli.size() > 0) {
				rmap = rli.get(0);
			}
		} catch (Exception e) {
		}
		return rmap;
	}

	public static boolean addFlowDraw(String id, String code, String name,
			String pid) {
		boolean res = false;
		String sql = "insert into sa_flowdrawlg(SID,SFOLDERID,SPROCESSID,SPROCESSNAME,SPROCESSACTY,FENABLED,VERSION)values(?,?,?,?,?,?,0)";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBUtils.getAppConn("system");
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, pid);
			ps.setString(3, code);
			ps.setString(4, name);
			ps.setString(5, "");
			ps.setInt(6, 1);
			ps.executeUpdate();
			res = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.CloseConn(conn, ps, null);
		}
		return res;
	}

	public static boolean removeFlowDraw(String id) {
		boolean res = false;
		String sql = "delete from sa_flowdrawlg where SID = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBUtils.getAppConn("system");
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.executeUpdate();
			res = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.CloseConn(conn, ps, null);
		}
		return res;
	}
}
