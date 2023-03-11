package com.tulin.v8.flowdesigner.ui.editors.process.element;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import com.tulin.v8.core.DBUtils;

@SuppressWarnings({ "rawtypes" })
public class ProcessDrawElement {
	private String sid;
	private String sfolderid;
	private String sprocessid;
	private String sprocessname;
	private String sprocessacty;
	private int fenabled;
	private int version;

	public ProcessDrawElement(String id) {
		loadData(id);
	}

	public ProcessDrawElement(Map m) {
		sid = (String) m.get("SID");
		sfolderid = (String) m.get("SFOLDERID");
		sprocessid = (String) m.get("SPROCESSID");
		sprocessname = (String) m.get("SPROCESSNAME");
		sprocessacty = (String) m.get("SPROCESSACTY");
		String ena = (String) m.get("FENABLED");
		if (ena == null || "".equals(ena)) {
			ena = "0";
		}
		fenabled = Integer.parseInt(ena);
		version = Integer.parseInt((String) m.get("VERSION"));
	}

	public void loadData(String id) {
		String sql = "select SID,SFOLDERID,SPROCESSID,SPROCESSNAME,SPROCESSACTY,FENABLED,VERSION from sa_flowdrawlg where SID = '"
				+ id + "'";
		try {
			List<Map<String, String>> rli = DBUtils.execQueryforList("system", sql);
			if (rli.size() > 0) {
				Map m = rli.get(0);
				sid = (String) m.get("SID");
				sfolderid = (String) m.get("SFOLDERID");
				sprocessid = (String) m.get("SPROCESSID");
				sprocessname = (String) m.get("SPROCESSNAME");
				sprocessacty = (String) m.get("SPROCESSACTY");
				String ena = (String) m.get("FENABLED");
				if (ena == null || "".equals(ena)) {
					ena = "0";
				}
				fenabled = Integer.parseInt(ena);
				version = Integer.parseInt((String) m.get("VERSION"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reload() {
		loadData(sid);
	}

	public boolean saveData() {
		boolean res = false;
		String sql = "update sa_flowdrawlg set SPROCESSACTY = ? where SID = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBUtils.getAppConn("system");
			ps = conn.prepareStatement(sql);
			ps.setString(1, sprocessacty);
			ps.setString(2, sid);
			ps.executeUpdate();
			res = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.CloseConn(conn, ps, null);
		}
		return res;
	}

	public boolean update() {
		boolean res = false;
		version += 1;
		String sql = "update sa_flowdrawlg set SFOLDERID=?,SPROCESSID=?,SPROCESSNAME=?,SPROCESSACTY=?,FENABLED=?,VERSION=? where SID = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBUtils.getAppConn("system");
			ps = conn.prepareStatement(sql);
			ps.setString(1, sfolderid);
			ps.setString(2, sprocessid);
			ps.setString(3, sprocessname);
			ps.setString(4, sprocessacty);
			ps.setInt(5, fenabled);
			ps.setInt(6, version);
			ps.setString(7, sid);
			ps.executeUpdate();
			res = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.CloseConn(conn, ps, null);
		}
		return res;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSfolderid() {
		return sfolderid;
	}

	public void setSfolderid(String sfolderid) {
		this.sfolderid = sfolderid;
	}

	public String getSprocessid() {
		return sprocessid;
	}

	public void setSprocessid(String sprocessid) {
		this.sprocessid = sprocessid;
	}

	public String getSprocessname() {
		return sprocessname;
	}

	public void setSprocessname(String sprocessname) {
		this.sprocessname = sprocessname;
	}

	public String getSprocessacty() {
		return sprocessacty;
	}

	public void setSprocessacty(String sprocessacty) {
		this.sprocessacty = sprocessacty;
	}

	public int getFenabled() {
		return fenabled;
	}

	public void setFenabled(int fenabled) {
		this.fenabled = fenabled;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
