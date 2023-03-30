package com.tulin.v8.ide.wizards.templet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.tulin.v8.core.StringArray;
import com.tulin.v8.core.utils.DataType;
import com.tulin.v8.ide.wizards.templet.utils.TempletsReader;

public class DirectDetailTemplet extends TempletsReader {
	/*
	 * 获取页面内容
	 */
	public static String getPageContext(String templet, String filename, StringArray columns, StringArray labels,
			StringArray datatypes) throws Exception {
		String context = getTempletPageContext(templet);
		context = context.replace("YJTemplet_page", filename.subSequence(0, filename.lastIndexOf(".")));
		StringBuffer dataItem = new StringBuffer();
		dataItem.append("<div class=\"layui-form-item\">");
		for (int i = 0; i < columns.getLength(); i++) {
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