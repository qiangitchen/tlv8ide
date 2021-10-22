package com.tulin.v8.ide.wizards.templet;

import com.tulin.v8.ide.wizards.templet.utils.TempletsReader;

public class MobileListTemplet extends TempletsReader {
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
	public static String getJsContext(String templet, String dbkey, String tableName, String ID, String title,
			String text, String ellip, String filter) throws Exception {
		String context = getTempletJsContext(templet);
		context = context.replace("YJTemplet_dbkey", dbkey);
		context = context.replace("YJTemplet_tableName", tableName);
		context = context.replace("YJTemplet_ID", ID);
		context = context.replace("YJTemplet_title", title);
		context = context.replace("YJTemplet_text", text);
		context = context.replace("YJTemplet_ellip", ellip);
		context = context.replace("YJTemplet_filter", filter == null ? "" : (" " + filter));
		return context;
	}
}
