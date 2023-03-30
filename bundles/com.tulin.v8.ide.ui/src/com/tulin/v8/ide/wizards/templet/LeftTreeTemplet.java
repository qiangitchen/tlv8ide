package com.tulin.v8.ide.wizards.templet;

import java.util.List;

import com.tulin.v8.core.StringArray;
import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.core.utils.DataType;
import com.tulin.v8.ide.wizards.templet.utils.TempletsReader;

public class LeftTreeTemplet extends TempletsReader {
	/*
	 * 获取页面内容
	 */
	public static String getPageContext(String templet, String filename) throws Exception {
		String context = getTempletPageContext(templet);
		context = context.replace("YJTemplet_page", filename.subSequence(0, filename.lastIndexOf(".")));
		return context;
	}

	/*
	 * 获取JS内容
	 */
	public static String getJsContext(String templet, String dbkey, String tableName, String ID, String parent,
			String label, String cells, String rootFilter, String level, String quckpath) throws Exception {
		String context = getTempletJsContext(templet);
		context = context.replace("YJTemplet_dbkey", dbkey);
		context = context.replace("YJTemplet_tableName", tableName);
		context = context.replace("YJTemplet_ID", ID);
		context = context.replace("YJTemplet_name", label);
		context = context.replace("YJTemplet_parent", parent);
		context = context.replace("YJTemplet_cells", cells == null ? "" : cells);
		context = context.replace("YJTemplet_rootFilter", rootFilter == null ? "" : (" " + rootFilter));
		context = context.replace("YJTemplet_level", level == null ? "" : level + " asc");
		context = context.replace("YJTemplet_quckpath", quckpath == null ? "" : quckpath);
		context = context.replace("YJTemplet_quckEnable",
				(quckpath != null && !"".equals(quckpath)) ? "true" : "false");
		StringArray YJTemplet_cellsListIDS = new StringArray();
		if (cells == null || "".equals(cells)) {
			YJTemplet_cellsListIDS.push(label);
		} else {
			if (cells.indexOf(label) < 0) {
				YJTemplet_cellsListIDS.push(label);
			}
			String[] ids = cells.split(",");
			for (int i = 0; i < ids.length; i++) {
				YJTemplet_cellsListIDS.push(ids[i]);
			}
		}
		context = context.replace("R_ListIDS", YJTemplet_cellsListIDS.join(","));
		List<String[]> columnlist = CommonUtil.getTableColumn(dbkey, tableName);
		StringArray YJTemplet_cellsListListLBS = new StringArray();
		StringArray YJTemplet_cellsListWDs = new StringArray();
		StringArray YJTemplet_cellsListTPs = new StringArray();
		for (int i = 0; i < YJTemplet_cellsListIDS.getLength(); i++) {
			YJTemplet_cellsListListLBS.push(getClumnLabel(columnlist, YJTemplet_cellsListIDS.get(i)));
			YJTemplet_cellsListWDs.push("100");
			YJTemplet_cellsListTPs.push(getClumnType(columnlist, YJTemplet_cellsListIDS.get(i)));
		}
		context = context.replace("R_ListLBS", YJTemplet_cellsListListLBS.join(","));
		context = context.replace("R_ListWDs", YJTemplet_cellsListWDs.join(","));
		context = context.replace("R_ListTPs", YJTemplet_cellsListTPs.join(","));
		return context;
	}

	public static String getClumnLabel(List<String[]> columnlist, String cellname) {
		String rlable = "";
		for (String[] row : columnlist) {
			if (row[0].equals(cellname)) {
				rlable = (row[2]==null||"".equals(row[2])) ? cellname : row[2];
			}
		}
		return rlable;
	}

	public static String getClumnType(List<String[]> columnlist, String cellname) {
		String rtype = "";
		for (String[] row : columnlist) {
			if (row[0].equals(cellname)) {
				rtype = DataType.getDataTypeBydatabase(row[1].toUpperCase());
			}
		}
		return rtype;
	}
}
