package com.tulin.v8.core.utils;

import com.tulin.v8.core.DBUtils;

/**
 * 数据类型配置及选择 数据库字段类型与业务字段类型匹配选择
 * 
 * @author 陈乾
 *
 */
public class DataType {
	public static String[] dataType = { "string", "number", "int", "bigint", "float", "decimal", "date", "datetime",
			"timestamp", "text", "longtext", "clob", "blob" };
	public static String[] gridDataType = { "ro", "string", "number", "date", "datetime", "year", "month", "yearmont",
			"html:reader", "select:id", "button:action", "checkBox:map", "radio:map", "textarea" };
	public static String[] detailDataType = { "input", "output", "date", "datetime", "year", "month", "yearmont",
			"select", "switch", "checkbox", "radio", "textarea" };

	/**
	 * 获取数据库字段类型对应的业务字段类型
	 * 
	 * @param dat
	 * @return
	 */
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

	/**
	 * 根据数据类型获取表单的字段信息
	 * 
	 * @param dataType
	 * @return
	 */
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

	/**
	 * 数据类型转换
	 * 
	 * @param dbkey
	 * @param dataType 业务数据类型
	 * @param length   字段长度（字符）
	 * @return
	 */
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
				datatype = "varchar(" + length + ")";
			}
			if (dataType.contains("varchar") && dataType.indexOf("(") < 0) {
				datatype = dataType + "(" + length + ")";
			}
		}
		return datatype;
	}
}
