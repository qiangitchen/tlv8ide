package com.tulin.v8.core.utils;

import com.tulin.v8.core.DBUtils;

public class DataType {
	public static String[] dataType = { "string", "number", "date", "datetime", "text", "blob" };
	public static String[] gridDataType = { "ro", "string", "number", "date", "datetime", "year", "month", "yearmont",
			"html:reader", "select:id", "button:action", "checkBox:map", "radio:map", "textarea" };
	public static String[] detailDataType = { "input", "output", "date", "datetime", "year", "month", "yearmont",
			"select", "switch", "checkbox", "radio", "textarea" };

	public static String getDataTypeBydatabase(String dat) {
		if (dat.indexOf("VARCHAR") > -1) {
			return "string";
		} else if (dat.equals("NUMBER") || dat.equals("INT") || dat.equals("FLOAT")) {
			return "number";
		} else if (dat.equals("DATE")) {
			return "date";
		} else if (dat.equals("DATETIME")) {
			return "datetime";
		} else if (dat.equals("CLOB")) {
			return "text";
		}
		return "string";
	}

	public static String getItemBydataType(String dataType) {
		if (dataType.equals("input")) {
			return "type=\"text\" class=\"layui-input\"";
		} else if (dataType.equals("output")) {
			return "type=\"text\" class=\"layui-input\" readonly=\"readonly\" ";
		} else if (dataType.equals("date")) {
			return "type=\"text\" class=\"layui-input Wdate\" onClick=\"WdatePicker({dateFmt:'yyyy-MM-dd'})\" format=\"yyyy-MM-dd\"";
		} else if (dataType.equals("datetime")) {
			return "type=\"text\" class=\"layui-input Wdate\" onClick=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})\" format=\"yyyy-MM-dd HH:mm:ss\"";
		} else if (dataType.equals("year")) {
			return "type=\"text\" class=\"layui-input Wdate\" onClick=\"WdatePicker({dateFmt:'yyyy'})\" format=\"yyyy\"";
		} else if (dataType.equals("month")) {
			return "type=\"text\" class=\"layui-input Wdate\" onClick=\"WdatePicker({dateFmt:'MM'})\" format=\"MM\"";
		} else if (dataType.equals("yearmont")) {
			return "type=\"text\" class=\"layui-input Wdate\" onClick=\"WdatePicker({dateFmt:'yyyy-MM'})\" format=\"yyyy-MM\"";
		} else if (dataType.equals("switch")) {
			return "type=\"checkbox\" lay-skin=\"switch\"";
		} else if (dataType.equals("checkbox")) {
			return "type=\"checkbox\" class=\"layui-input\"";
		} else if (dataType.equals("radio")) {
			return "type=\"radio\" class=\"layui-input\"";
		} else if (dataType.equals("textarea")) {
			return "type=\"text\" class=\"layui-textarea\"";
		} else if (dataType.equals("select")) {
			return "type=\"text\" class=\"layui-input\"";
		}
		return "class=\"layui-input\"";
	}

	public static String dataTypeTranse(String dbkey, String dataType, String length) {
		String datatype = "";
		if (DBUtils.IsOracleDB(dbkey)) {
			if (dataType.toLowerCase().equals("string")) {
				datatype = "VARCHAR2(" + length + ")";
			} else if (dataType.toLowerCase().equals("number")) {
				datatype = "NUMBER";
			} else if (dataType.toLowerCase().equals("date") || dataType.toLowerCase().equals("datetime")) {
				datatype = "DATE";
			} else if (dataType.toLowerCase().equals("text")) {
				datatype = "CLOB";
			} else {
				datatype = dataType.toUpperCase();
			}
		} else {
			if (dataType.toLowerCase().equals("string")) {
				datatype = "VARCHAR(" + length + ")";
			} else {
				datatype = dataType.toUpperCase();
			}
		}
		return datatype;
	}
}
