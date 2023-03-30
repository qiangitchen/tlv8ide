package com.tulin.v8.vue.wizards.utils;

import java.util.ArrayList;
import java.util.List;

import com.tulin.v8.core.utils.CommonUtil;

public class TableColumnUtils {

	public static List<String[]> getTableColumn(String dbkey, String tableName, String search) throws Exception {
		List<String[]> rlist = new ArrayList<String[]>();
		List<String[]> list = CommonUtil.getTableColumn(dbkey, tableName);
		for (String[] row : list) {
			if (row[0].toUpperCase().indexOf(search.toUpperCase()) > -1
					|| row[1].toUpperCase().indexOf(search.toUpperCase()) > -1
					|| row[2].toUpperCase().indexOf(search.toUpperCase()) > -1) {
				rlist.add(row);
			}
		}
		return rlist;
	}

}
