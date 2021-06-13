package com.tulin.v8.ide.wizards.templet;

import com.tulin.v8.ide.wizards.templet.utils.TempletsReader;

public class DirectGridTemplet extends TempletsReader {
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
	public static String getJsContext(String templet, String dbkey, String tableName, String subtableName,
			String subdirect, String columns, String labels, String widths, String datatypes, String subcolumns,
			String sublabels, String subwidths, String subdatatypes) throws Exception {
		String context = getTempletJsContext(templet);
		context = context.replace("YJTemplet_dbkey", dbkey);
		context = context.replace("YJTemplet_tableName", tableName);
		context = context.replace("YJTemplet_subtableName", subtableName);
		context = context.replace("YJTemplet_subdirect", subdirect);

		context = context.replace("YJTemplet_columns", columns);
		context = context.replace("YJTemplet_labels", labels);
		context = context.replace("YJTemplet_widths", widths);
		context = context.replace("YJTemplet_datatypes", datatypes);

		context = context.replace("YJTemplet_subcolumns", subcolumns);
		context = context.replace("YJTemplet_sublabels", sublabels);
		context = context.replace("YJTemplet_subwidths", subwidths);
		context = context.replace("YJTemplet_subdatatypes", subdatatypes);
		return context;
	}
}