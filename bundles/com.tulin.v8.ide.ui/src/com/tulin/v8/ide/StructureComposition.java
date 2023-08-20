package com.tulin.v8.ide;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.tulin.v8.core.Sys;
import com.tulin.v8.core.utils.CommonUtil;

import zigen.plugin.db.core.IDBConfig;

public class StructureComposition {
	/**
	 * 获取表的配置文件内容
	 * 
	 * @param dbkey
	 * @param tablename
	 * @param tbtype
	 * @return String
	 * @throws Exception
	 */
	/*
	 * public static File getTablePermision(IDBConfig dbConfig, String dbkey, String
	 * tablename, String tbtype) throws Exception { String dataPath =
	 * StudioConfig.getUIPath() + "/.data"; String fileName = dbkey + "_" +
	 * tablename + ".xml"; String bzPath = dataPath + "/" + fileName; File dirfiel =
	 * new File(dataPath); if (!dirfiel.exists()) { dirfiel.mkdirs(); } File file =
	 * new File(bzPath); if (file.exists()) { try { file.delete(); } catch
	 * (Exception e) { } } file.createNewFile(); writeTablePermision(file, dbConfig,
	 * dbkey, tablename, tbtype); return file; }
	 */

	/**
	 * 生成配置文件
	 * 
	 * @param dbkey
	 * @param tablename
	 * @param tbtype
	 * @param file
	 * @return String
	 */
	@SuppressWarnings("deprecation")
	public static String writeTablePermision(IDBConfig dbConfig, String dbkey, String tablename, String tbtype,
			File file) {
		String xmlstr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		xmlstr += "<root xmlns=\"\">\n";
		xmlstr += "\n</root>";
		String result = "";
		try {
			Document dom = DocumentHelper.parseText(xmlstr);
			Element tableItem = dom.getRootElement().addElement("item");
			tableItem.setAttributeValue("dbkey", dbkey);
			tableItem.setAttributeValue("name", tablename);
			tableItem.setAttributeValue("text", CommonUtil.getTableComments(dbkey, dbConfig.getSchema(), tablename,
					new String[] { "TABLE", "VIEW" }));
			tableItem.setAttributeValue("type", tbtype);
			List<String[]> list = CommonUtil.getTableColumn(dbkey, dbConfig.getSchema(), tablename);
			for (int i = 0; i < list.size(); i++) {
				String[] column = list.get(i);
				Element columnItem = tableItem.addElement("item");
				columnItem.setAttributeValue("name", column[0]);
				columnItem.setAttributeValue("type", column[1]);
				columnItem.setAttributeValue("length", column[3]);
				columnItem.setAttributeValue("text", column[2]);
				try {
					columnItem.setAttributeValue("COLUMN_TYPE", column[4]);
				} catch (Exception e) {
				}
				columnItem.setAttributeValue("def", column[5]);
			}
			if (file != null) {
				OutputFormat format = OutputFormat.createPrettyPrint();
				format.setEncoding("UTF-8");
				format.setIndent(true);
				format.setNewLineAfterNTags(1);
				format.setNewlines(true);
				XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
				writer.write(dom);
				writer.close();
			}
			result = dom.asXML();
		} catch (Exception e) {
			Sys.packErrMsg(e.toString());
		}
		return result;
	}

	/**
	 * 生成表和视图的详细信息
	 * 
	 * @param dbConfig
	 * @param dbkey
	 * @param tablename
	 * @param tbtype
	 * @return
	 */
	public static String writeTablePermision(IDBConfig dbConfig, String dbkey, String tablename, String tbtype) {
		return writeTablePermision(dbConfig, dbkey, tablename, tbtype, null);
	}

}
