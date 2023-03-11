package com.tulin.v8.flowdesigner.ui.data;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tulin.v8.core.DBUtils;

public class MenuData {

	public static JSONArray getMenuTree() throws Exception {
		JSONArray res = new JSONArray();
		Connection conn = DBUtils.getAppConn("system");
		try {
			String sql = "select sid,label,url from sa_opmenutree where pid is null or pid='' order by sorts";
			List<Map<String, String>> dataList = DBUtils.execQueryforList(conn, sql);
			for (Map<String, String> data : dataList) {
				JSONObject json = new JSONObject();
				json.put("label", data.get("LABEL"));
				json.put("url", data.get("URL"));
				json.put("children", getMenuTreeChild(conn, data.get("SID")));
				res.put(json);
			}
		} catch (Exception e) {
			String sql = "select sid,label,url from sa_function_tree where pid is null or pid='' order by sorts";
			List<Map<String, String>> dataList = DBUtils.execQueryforList(conn, sql);
			for (Map<String, String> data : dataList) {
				JSONObject json = new JSONObject();
				json.put("label", data.get("LABEL"));
				json.put("url", data.get("URL"));
				json.put("children", getMenuTreeChild(conn, data.get("SID")));
				res.put(json);
			}
		} finally {
			DBUtils.CloseConn(conn, null, null);
		}
		return res;
	}

	public static JSONArray getMenuTreeChild(Connection conn, String pid) throws Exception {
		JSONArray res = new JSONArray();
		try {
			String sql = "select sid,label,url from sa_opmenutree where pid='" + pid + "' order by sorts";
			List<Map<String, String>> dataList = DBUtils.execQueryforList(conn, sql);
			for (Map<String, String> data : dataList) {
				JSONObject json = new JSONObject();
				json.put("label", data.get("LABEL"));
				json.put("url", data.get("URL"));
				json.put("children", getMenuTreeChild(conn, data.get("SID")));
				res.put(json);
			}
		} catch (Exception e) {
			String sql = "select sid,label,url from sa_function_tree where pid='" + pid + "' order by sorts";
			List<Map<String, String>> dataList = DBUtils.execQueryforList(conn, sql);
			for (Map<String, String> data : dataList) {
				JSONObject json = new JSONObject();
				json.put("label", data.get("LABEL"));
				json.put("url", data.get("URL"));
				json.put("children", getMenuTreeChild(conn, data.get("SID")));
				res.put(json);
			}
		}
		return res;
	}
}
