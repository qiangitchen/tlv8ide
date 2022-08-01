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
import com.tulin.v8.ide.utils.StudioConfig;

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
	public static String getTablePermision(String dbkey, String tablename, String tbtype) throws Exception {
		String result = "";
		String dataPath = StudioConfig.getUIPath() + "/.data";
		String fileName = dbkey + "_" + tablename + ".xml";
		String bzPath = dataPath + "/" + fileName;
		File dirfiel = new File(dataPath);
		if (!dirfiel.exists()) {
			dirfiel.mkdir();
		}
		File file = new File(bzPath);
		if (file.exists()) {
			try {
				file.delete();
			} catch (Exception e) {
			}
		}
		file.createNewFile();
		result = WriteTablePermision(file, dbkey, tablename, tbtype);
		return result;
	}

	/**
	 * 生成配置文件
	 * 
	 * @param file
	 * @param dbkey
	 * @param tablename
	 * @param tbtype
	 * @return String
	 */
	@SuppressWarnings("deprecation")
	public static String WriteTablePermision(File file, String dbkey, String tablename, String tbtype) {
		String xmlstr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		xmlstr += "<root xmlns=\"\">\n";
		xmlstr += "\n</root>";
		String result = "";
		try {
			Document dom = DocumentHelper.parseText(xmlstr);
			Element tableItem = dom.getRootElement().addElement("item");
			tableItem.setAttributeValue("dbkey", dbkey);
			tableItem.setAttributeValue("name", tablename);
			tableItem.setAttributeValue("text", CommonUtil.getTableComments(dbkey, tablename));
			tableItem.setAttributeValue("type", tbtype);
			List<String[]> list = CommonUtil.getTableColumn(dbkey, tablename);
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
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			format.setIndent(true);
			format.setNewLineAfterNTags(1);
			format.setNewlines(true);
			XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
			writer.write(dom);
			writer.close();
			result = dom.asXML();
		} catch (Exception e) {
			Sys.packErrMsg(e.toString());
		}
		return result;
	}

}
