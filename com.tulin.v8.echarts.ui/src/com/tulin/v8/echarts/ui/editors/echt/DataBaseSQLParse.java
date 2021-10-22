package com.tulin.v8.echarts.ui.editors.echt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.dom4j.Element;

import com.tulin.v8.core.DBUtils;
import com.tulin.v8.core.StringArray;
import com.tulin.v8.core.Sys;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DataBaseSQLParse {

	public static void transeData(Element baseData, Parameters parameter) {
		List<Element> sqlEls = baseData.elements("sql");
		for (int i = 0; i < sqlEls.size(); i++) {
			handleSQLElement(baseData, sqlEls.get(i), parameter);
		}
	}

	public static void handleSQLElement(Element baseData, Element sqlEl, Parameters parameter) {
		String db = sqlEl.attributeValue("dbkey");
		String sql = sqlEl.getText();
		sql = sql.replace("\t", " ");
		sql = sql.replace("\n", " ");
		try {
			List<Map<String, Object>> reslist = execQueryforList(db, sql, parameter);
			if (reslist.size() > 0) {
				Map<String, Object> lmap = reslist.get(0);
				for (String name : lmap.keySet()) {
					Element xele = baseData.element(name);
					if (xele == null) {
						xele = baseData.addElement(name);
					}
					xele.setText(new StringArray(getCellVlues(name, reslist)).toJSON().toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List execQueryforList(String key, String sql, Parameters parameter) throws SQLException {
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		List li = new ArrayList();
		Connection aConn = null;
		try {
			aConn = DBUtils.getAppConn(key);
		} catch (NamingException e1) {
			e1 = new NamingException(e1.toString() + ">>key:" + key + "<< SQL:" + sql);
			e1.printStackTrace();
		}
		List<String> paramlist = new ArrayList<String>();
		Map<String, String> parameters = new HashMap<String, String>();
		if (parameter.getParamsMap().containsKey(sql)) {
			parameters = parameter.getParamsMap().get(sql);
			for (String param : parameters.keySet()) {
				paramlist.add(param);
				sql = sql.replace("{" + param + "}", "?");
			}
		}
		PreparedStatement ps = aConn.prepareStatement(sql);
		try {
			for (int p = 0; p < paramlist.size(); p++) {
				String pam = paramlist.get(p);
				String pav = parameters.get(pam);
				ps.setString(p + 1, pav);
			}
			rs = ps.executeQuery();
			rsmd = rs.getMetaData();
			int size = rsmd.getColumnCount();
			while (rs.next()) {
				Map sm = new HashMap();
				for (int i = 1; i <= size; i++) {
					String cellType = rsmd.getColumnTypeName(i);
					String columnName = rsmd.getColumnLabel(i);
					if ("DATE".equals(cellType) || "DATETIME".equals(cellType)) {
						try {
							try {
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								String v_l = format.format(format.parse(rs.getString(i)));
								sm.put(columnName, v_l);
							} catch (Exception e) {
								try {
									SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
									SimpleDateFormat nformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									String v_l = nformat.format(format.parse(rs.getString(i)));
									sm.put(columnName, v_l);
								} catch (Exception er) {
									SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
									String v_l = format.format(format.parse(rs.getString(i)));
									sm.put(columnName, v_l);
								}
							}
						} catch (Exception e) {
							sm.put(columnName, "");
						}
					} else if ("BLOB".equals(cellType)) {
						try {
							sm.put(columnName, rs.getBlob(i));
						} catch (Exception e) {
							sm.put(columnName, null);
							Sys.printMsg(e.toString());
						}
					} else if ("CLOB".equals(cellType)) {
						Clob clob = rs.getClob(i);
						String content = "";
						if (clob != null) {
							BufferedReader in = new BufferedReader(clob.getCharacterStream());
							StringWriter out = new StringWriter();
							int c;
							try {
								while ((c = in.read()) != -1) {
									out.write(c);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
							content = out.toString();
						}
						sm.put(columnName, content);
					} else {
						sm.put(columnName, (rs.getString(i) == null) ? "" : String.valueOf(rs.getString(i)));
					}
				}
				li.add(sm);
			}
		} catch (SQLException e) {
			Sys.printMsg(e.toString());
			throw new SQLException(e.toString() + ">>\n sql:" + sql);
		} finally {
			DBUtils.CloseConn(aConn, ps, rs);
		}
		return li;
	}

	private static List getCellVlues(String name, List<Map<String, Object>> reslist) {
		List relist = new ArrayList();
		for (int i = 0; i < reslist.size(); i++) {
			Map<String, Object> map = reslist.get(i);
			relist.add(map.get(name));
		}
		return relist;
	}
}
