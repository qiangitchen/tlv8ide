package com.tulin.v8.flowdesigner.ui.editors.process.element;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import com.tulin.v8.core.DBUtils;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class FlowFolderElement {
	private String sid;
	private String sparent;
	private String scode;
	private String sname;
	private String sidpath;
	private String scodepath;
	private String snamepath;
	private int version;

	public FlowFolderElement(String id) {
		String sql = "select SID,SPARENT,SCODE,SNAME,SIDPATH,SCODEPATH,SNAMEPATH,VERSION from sa_flowfolder where SID = '"
				+ id + "'";
		try {
			List<Map> rli = DBUtils.execQueryforList("system", sql);
			if (rli.size() > 0) {
				Map m = rli.get(0);
				sid = (String) m.get("SID");
				sparent = (String) m.get("SPARENT");
				scode = (String) m.get("SCODE");
				sname = (String) m.get("SNAME");
				sidpath = (String) m.get("SCODEPATH");
				scodepath = (String) m.get("SNAMEPATH");
				snamepath = (String) m.get("SNAMEPATH");
				version = Integer.parseInt((String) m.get("VERSION"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean update() {
		boolean res = false;
		version += 1;
		String sql = "update sa_flowfolder set SPARENT=?,SCODE=?,SNAME=?,SIDPATH=?,SCODEPATH=?,SNAMEPATH=?,VERSION=? where SID = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBUtils.getAppConn("system");
			ps = conn.prepareStatement(sql);
			if (sparent == null || "".equals(sparent)) {
				ps.setNull(1, java.sql.Types.NULL);
			} else {
				ps.setString(1, sparent);
			}
			ps.setString(2, scode);
			ps.setString(3, sname);
			ps.setString(4, sidpath);
			ps.setString(5, scodepath);
			ps.setString(6, snamepath);
			ps.setInt(7, version);
			ps.setString(8, sid);
			ps.executeUpdate();
			res = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.CloseConn(conn, ps, null);
		}
		return res;
	}

	public FlowFolderElement(Map m) {
		sid = (String) m.get("SID");
		sparent = (String) m.get("SPARENT");
		scode = (String) m.get("SCODE");
		sname = (String) m.get("SNAME");
		sidpath = (String) m.get("SCODEPATH");
		scodepath = (String) m.get("SNAMEPATH");
		snamepath = (String) m.get("SNAMEPATH");
		version = Integer.parseInt((String) m.get("VERSION"));
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSparent() {
		return sparent;
	}

	public void setSparent(String sparent) {
		this.sparent = sparent;
	}

	public String getScode() {
		return scode;
	}

	public void setScode(String scode) {
		this.scode = scode;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getSidpath() {
		return sidpath;
	}

	public void setSidpath(String sidpath) {
		this.sidpath = sidpath;
	}

	public String getScodepath() {
		return scodepath;
	}

	public void setScodepath(String scodepath) {
		this.scodepath = scodepath;
	}

	public String getSnamepath() {
		return snamepath;
	}

	public void setSnamepath(String snamepath) {
		this.snamepath = snamepath;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
