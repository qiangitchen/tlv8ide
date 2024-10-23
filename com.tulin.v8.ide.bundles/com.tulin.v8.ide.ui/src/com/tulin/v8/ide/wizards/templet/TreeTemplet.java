package com.tulin.v8.ide.wizards.templet;

import com.tulin.v8.ide.wizards.templet.utils.TempletsReader;

public class TreeTemplet extends TempletsReader {
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
		return context;
	}
}
