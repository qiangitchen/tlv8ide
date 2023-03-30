package com.tulin.v8.ide.wizards.templet;

import com.tulin.v8.core.StringArray;
import com.tulin.v8.core.utils.DataType;
import com.tulin.v8.ide.wizards.templet.utils.TempletsReader;

public class MobileSampleDetailTemplet extends TempletsReader {
	/*
	 * 获取页面内容
	 */
	public static String getPageContext(String templet, String filename, StringArray columns, StringArray labels,
			StringArray datatypes) throws Exception {
		String context = getTempletPageContext(templet);
		context = context.replace("YJTemplet_page", filename.subSequence(0, filename.lastIndexOf(".")));
		StringBuffer dataItem = new StringBuffer();
		for (int i = 0; i < columns.getLength(); i++) {
			String column = columns.get(i);
			String label = labels.get(i);
			String datatype = datatypes.get(i);
			dataItem.append(
					"<div class=\"mui-input-row\" data-filtered=\"filtered\">\n\t<label data-filtered=\"filtered\">"
							+ label
							+ "</label>\n\t<input type=\"text\" placeholder=\"普通输入框\" data-filtered=\"filtered\" id=\""
							+ column + "\" name=\"" + column + "\" " + DataType.getItemBydataType(datatype)
							+ "/>\n</div>\n");
		}
		context = context.replace("YJTemplet_dataItem", dataItem);
		return context;
	}

	/*
	 * 获取JS内容
	 */
	public static String getJsContext(String templet, String dbkey, String tableName) throws Exception {
		String context = getTempletJsContext(templet);
		context = context.replace("YJTemplet_dbkey", dbkey);
		context = context.replace("YJTemplet_tableName", tableName);
		return context;
	}
}