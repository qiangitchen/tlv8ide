package com.tulin.v8.vue.wizards.templet;

import com.tulin.v8.vue.wizards.templet.utils.TempletsReader;

public class TableListTemplet extends TempletsReader {

	public static String getPageContext(String dbkey, String tableName, String join, String join2, String join3,
			String join4, String filename) throws Exception {
		String context = getTempletPageContext("tableList");
		return context;
	}

}
