package com.tulin.v8.ide.wizards.templet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.tulin.v8.core.StringArray;
import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.ide.wizards.templet.utils.TempletsReader;

public class TreeGridTemplet extends TempletsReader {
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

		List<String[]> columnlist = CommonUtil.getTableColumn(dbkey, null, tableName);
		List<Map<String, String>> YJTemplet_GridColumns = new ArrayList<Map<String, String>>();
		for (int i = 0; i < YJTemplet_cellsListIDS.getLength(); i++) {
			Map<String, String> m = new HashMap<String, String>();
			m.put("field", YJTemplet_cellsListIDS.get(i));
			m.put("title", LeftTreeTemplet.getClumnLabel(columnlist, YJTemplet_cellsListIDS.get(i)));
			m.put("width", "100");
			YJTemplet_GridColumns.add(m);
		}
		context = context.replace("YJTemplet_GridColumns", new JSONArray(YJTemplet_GridColumns).toString());
		return context;
	}
}
