package com.tulin.v8.ide.wizards.templet;

import com.tulin.v8.core.StringArray;
import com.tulin.v8.ide.utils.DataType;
import com.tulin.v8.ide.wizards.templet.utils.TempletsReader;

public class ListDeatailPortalTemplet extends TempletsReader {
	/*
	 * 获取详细页面内容
	 */
	public static String getListPageContext(String templet, String filename) throws Exception {
		String context = getTempletPageContext(templet, "list.html");
		context = context.replace("YJTemplet_page", filename.subSequence(0, filename.lastIndexOf(".")));
		return context;
	}

	/*
	 * 获取详细页面内容
	 */
	public static String getDetailPageContext(String templet, String filename, StringArray columns, StringArray labels,
			StringArray dedatatypesText) throws Exception {
		String context = getTempletPageContext(templet, "detail.html");
		StringBuffer dataItem = new StringBuffer();
		dataItem.append("<div class=\"layui-form-item\">");
		for (int i = 0; i < columns.getLength(); i++) {
			dataItem.append("<div class=\"layui-inline\">");
			String column = columns.get(i);
			String label = labels.get(i);
			String datatype = dedatatypesText.get(i);
			dataItem.append("<label class=\"layui-form-label\">" + label + "</label>");
			dataItem.append("<div class=\"layui-input-inline\">");
			dataItem.append("<input type=\"text\" id=\"" + column + "\" name=\"" + column
					+ "\" " + DataType.getItemBydataType(datatype) + "/>");
			dataItem.append("</div>");
			dataItem.append("</div>");
		}
		dataItem.append("</div>");
		context = context.replace("YJTemplet_dataItem", dataItem);
		context = context.replace("YJTemplet_page", filename.subSequence(0, filename.lastIndexOf(".")));
		return context;
	}

	/*
	 * 获取列表页面JS内容
	 */
	public static String getListJsContext(String templet, String dbkey, String tableName, String columns, String labels,
			String widths, String datatypes, String detailPage) throws Exception {
		String context = getTempletJsContext(templet, "list.js");
		context = context.replace("YJTemplet_dbkey", dbkey);
		context = context.replace("YJTemplet_tableName", tableName);
		context = context.replace("YJTemplet_columns", columns);
		context = context.replace("YJTemplet_labels", labels);
		context = context.replace("YJTemplet_widths", widths);
		context = context.replace("YJTemplet_datatypes", datatypes);
		context = context.replace("YJTemplet_detailPage", detailPage);
		return context;
	}

	/*
	 * 获取详细页面JS内容
	 */
	public static String getDetailJsContext(String templet, String dbkey, String tableName) throws Exception {
		String context = getTempletJsContext(templet, "detail.js");
		context = context.replace("YJTemplet_dbkey", dbkey);
		context = context.replace("YJTemplet_tableName", tableName);
		return context;
	}
}
