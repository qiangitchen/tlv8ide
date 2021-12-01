package com.tulin.v8.ide.wizards.templet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.tulin.v8.core.StringArray;
import com.tulin.v8.ide.utils.DataType;
import com.tulin.v8.ide.wizards.templet.utils.TempletsReader;

public class SampleFlowTemplet extends TempletsReader {
	/*
	 * 获取页面内容
	 */
	public static String getPageContext(String templet, String filename, StringArray columns, StringArray labels,
			StringArray datatypes, boolean iscreateinfo) throws Exception {
		String context = iscreateinfo ? getTempletPageContext(templet, "acpage.html") : getTempletPageContext(templet);
		context = context.replace("YJTemplet_page", filename.subSequence(0, filename.lastIndexOf(".")));
		StringBuffer dataItem = new StringBuffer();
		dataItem.append("<div class=\"layui-form-item\">");
		for (int i = 0; i < columns.getLength(); i++) {
			if (i % 3 == 0 && i != 0) {
				dataItem.append("</div><div class=\"layui-form-item\">");
			}
			dataItem.append("<div class=\"layui-inline\">");
			String column = columns.get(i);
			String label = labels.get(i);
			String datatype = datatypes.get(i);
			dataItem.append("<label class=\"layui-form-label\">" + label + "</label>");
			dataItem.append("<div class=\"layui-input-inline\">");
			dataItem.append("<input type=\"text\" id=\"" + column + "\" name=\"" + column
					+ "\" " + DataType.getItemBydataType(datatype) + "/>");
			dataItem.append("</div>");
			dataItem.append("</div>");
		}
		dataItem.append("</div>");
		context = context.replace("YJTemplet_dataItem", dataItem);
		Document doc = Jsoup.parse(context);
		return doc.html();
	}

	/*
	 * 获取JS内容
	 */
	public static String getJsContext(String templet, String dbkey, String tableName, boolean iscreateinfo)
			throws Exception {
		String context = iscreateinfo ? getTempletJsContext(templet, "acpage.js") : getTempletJsContext(templet);
		context = context.replace("YJTemplet_dbkey", dbkey);
		context = context.replace("YJTemplet_tableName", tableName);
		return context;
	}
}