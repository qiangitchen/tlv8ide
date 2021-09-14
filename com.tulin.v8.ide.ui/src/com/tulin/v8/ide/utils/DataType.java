package com.tulin.v8.ide.utils;

import com.tulin.v8.core.DBUtils;

public class DataType {
	public static String[] dataType = { "string", "number", "date", "datetime",
			"text", "blob" };
	public static String[] gridDataType = { "ro", "string", "number", "date",
			"datetime", "year", "month", "yearmont", "html:reader",
			"select:id", "button:action", "checkBox:map", "radio:map",
			"textarea" };
	public static String[] detailDataType = { "input", "output", "date",
			"datetime", "year", "month", "yearmont" };

	public static String getDataTypeBydatabase(String dat) {
		if (dat.indexOf("VARCHAR") > -1) {
			return "string";
		} else if (dat.equals("NUMBER") || dat.equals("INT")
				|| dat.equals("FLOAT")) {
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
			return "class=\"forminput\"";
		} else if (dataType.equals("output")) {
			return "class=\"forminput\" readonly=\"readonly\" ";
		} else if (dataType.equals("date")) {
			return "class=\"WdateinputLine\" onClick=\"WdatePicker({dateFmt:'yyyy-MM-dd'})\" format=\"yyyy-MM-dd\"";
		} else if (dataType.equals("datetime")) {
			return "class=\"WdateinputLine\" onClick=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})\" format=\"yyyy-MM-dd HH:mm:ss\"";
		} else if (dataType.equals("year")) {
			return "class=\"WdateinputLine\" onClick=\"WdatePicker({dateFmt:'yyyy'})\" format=\"yyyy\"";
		} else if (dataType.equals("month")) {
			return "class=\"WdateinputLine\" onClick=\"WdatePicker({dateFmt:'MM'})\" format=\"MM\"";
		} else if (dataType.equals("yearmont")) {
			return "class=\"WdateinputLine\" onClick=\"WdatePicker({dateFmt:'yyyy-MM'})\" format=\"yyyy-MM\"";
		}
		return "class=\"forminput\"";
	}

	public static String dataTypeTranse(String dbkey, String dataType,
			String length) {
		String datatype = "";
		if (DBUtils.IsOracleDB(dbkey)) {
			if (dataType.toLowerCase().equals("string")) {
				datatype = "VARCHAR2(" + length + ")";
			} else if (dataType.toLowerCase().equals("number")) {
				datatype = "NUMBER";
			} else if (dataType.toLowerCase().equals("date")
					|| dataType.toLowerCase().equals("datetime")) {
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
