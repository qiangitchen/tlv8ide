package com.tulin.v8.ide.preferences;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.tulin.v8.core.FileAndString;
import com.tulin.v8.core.Dao.SqlMap.GetSqlMapByKey;
import com.tulin.v8.core.config.AppConfig;
import com.tulin.v8.ide.utils.DocServerData;
import com.tulin.v8.ide.utils.GetTomcatconfig;
import com.tulin.v8.ide.utils.StudioConfig;

@SuppressWarnings({ "deprecation", "rawtypes" })
public class DataSousePermission {

	/**
	 * 写入数据源配置
	 * 
	 * @param name
	 * @param driverStr
	 * @param url
	 * @param userName
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static boolean WritePer(String name, String driverStr, String url, String userName, String password)
			throws Exception {
		boolean suc = true;
		if (name == null || "".equals(name)) {
			suc = false;
			throw new Exception(Messages.getString("preferencePages.DataSousePermission0.1"));
		}
		if (driverStr == null || Messages.getString("preferencePages.DataSousePermission0.2").equals(driverStr)) {
			suc = false;
			throw new Exception(Messages.getString("preferencePages.DataSousePermission0.3"));
		}
		if (url == null || "".equals(url)) {
			suc = false;
			throw new Exception(Messages.getString("preferencePages.DataSousePermission0.4"));
		}
		if (userName == null || "".equals(userName)) {
			suc = false;
			throw new Exception(Messages.getString("preferencePages.DataSousePermission0.5"));
		}
		if (password == null || "".equals(password)) {
			suc = false;
			throw new Exception(Messages.getString("preferencePages.DataSousePermission0.6"));
		}
		if ("doc".equals(name)) {
//			WriteDocServerDatasourse(driverStr, url, userName, password);
			DocServerData.writeConfig(name, driverStr, url, userName, password);
		} else {
			WriteToSqlMap(name, driverStr, url, userName, password);
			WriteBIZServerDatasourse(name, driverStr, url, userName, password);
		}
		return suc;
	}

	/**
	 * 写入文档服务数据源配置-TOMCAT
	 * 
	 * @param driverStr
	 * @param url
	 * @param userName
	 * @param password
	 */
	public static void WriteDocServerDatasourse(String driverStr, String url, String userName, String password) {
		try {
			File file = new File(GetTomcatconfig.getConfigFilePath());
			String fileStr = FileAndString.FileToString(file);
			// Sys.printMsg(fileStr);
			Document doc = DocumentHelper.parseText(fileStr);
			// Sys.packErrMsg(doc.asXML());
			Element root = doc.getRootElement();
			root = root.element("Service");
			root = root.element("Engine");
			root = root.element("Host");
			List Itorate = root.elements();
			for (int i = 0; i < Itorate.size(); i++) {
				String Context = ((Element) Itorate.get(i)).getName();
				Element transactionManager = (Element) Itorate.get(i);
				if (Context.equals("Context") && transactionManager.attributeValue("docBase").endsWith("DocServer")) {
					Element dataSource = (Element) (transactionManager.elements("Resource")).get(0);
					dataSource.setAttributeValue("driverClassName", driverStr);
					dataSource.setAttributeValue("url", url);
					dataSource.setAttributeValue("username", userName);
					dataSource.setAttributeValue("password", password);
					String vstr = "select 1";
					if (driverStr.toLowerCase().contains("oracle")) {
						vstr = "select 1 from dual";
					}
					dataSource.setAttributeValue("validationQuery", vstr);
				}
			}
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			format.setIndent(true);
			format.setNewLineAfterNTags(1);
			format.setNewlines(true);
			try {
				XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
				writer.write(doc);
				writer.close();
			} catch (Exception errr) {
				errr.printStackTrace();
			}
			// Sys.printMsg(doc.asXML());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 写入业务服务数据源配置-TOMCAT
	 * 
	 * @param name
	 * @param driverStr
	 * @param url
	 * @param userName
	 * @param password
	 */
	public static void WriteBIZServerDatasourse(String name, String driverStr, String url, String userName,
			String password) {
		try {
			File file = new File(GetTomcatconfig.getConfigFilePath());
			String fileStr = FileAndString.FileToString(file);
			// Sys.printMsg(fileStr);
			Document doc = DocumentHelper.parseText(fileStr);
			// Sys.packErrMsg(doc.asXML());
			Element root = doc.getRootElement();
			root = root.element("Service");
			root = root.element("Engine");
			root = root.element("Host");
			List Itorate = root.elements();
			for (int i = 0; i < Itorate.size(); i++) {
				String Context = ((Element) Itorate.get(i)).getName();
				Element transactionManager = (Element) Itorate.get(i);
				if (Context.equals("Context")
						&& transactionManager.attributeValue("docBase").endsWith(StudioConfig.PROJECT_PWEB_FOLDER)) {
					List sourses = transactionManager.elements("Resource");
					// boolean iswrite = true;
					for (int j = 0; j < sourses.size(); j++) {
						Element dataSource = (Element) sourses.get(j);
						if (name.equals(dataSource.attributeValue("name"))) {
							// dataSource.setAttributeValue("driverClassName",
							// driverStr);
							// dataSource.setAttributeValue("url", url);
							// dataSource.setAttributeValue("username",
							// userName);
							// dataSource.setAttributeValue("password",
							// password);
							// iswrite = false;
							dataSource.getParent().remove(dataSource);// 不使用Tomcat数据连接池
							break;
						}
					}
					// if (iswrite) {
					// Element newElm =
					// transactionManager.addElement("Resource");
					// newElm.addAttribute("name", name);
					// newElm.addAttribute("auth", "Container");
					// newElm.addAttribute("type", "javax.sql.DataSource");
					// newElm.addAttribute("driverClassName", driverStr);
					// newElm.addAttribute("url", url);
					// newElm.addAttribute("username", userName);
					// newElm.addAttribute("password", password);
					// newElm.addAttribute("maxActive", "10");
					// newElm.addAttribute("maxIdle", "5");
					// }
				}
			}
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			format.setIndent(true);
			format.setNewLineAfterNTags(1);
			format.setNewlines(true);
			try {
				XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
				writer.write(doc);
				writer.close();
			} catch (Exception errr) {
				errr.printStackTrace();
			}
			// Sys.printMsg(doc.asXML());
		} catch (Exception e1) {
			// e1.printStackTrace();
		}
	}

	/**
	 * 将配置信息写入sqlMap文件
	 * 
	 * @param name
	 * @param driverStr
	 * @param url
	 * @param userName
	 * @param password
	 */
	public static void WriteToSqlMap(String name, String driverStr, String url, String userName, String password) {
		Map<String, Map<String, String>> rm = GetSqlMapByKey.getKeyDbDriverMap();
		if (rm.get(name) == null) {
			WriteNewSqlMapFile(rm, name, driverStr, url, userName, password);
		} else {
			WriteSqlMapFile(rm, name, driverStr, url, userName, password);
		}
	}

	/**
	 * 将配置信息写入新的sqlMap文件
	 * 
	 * @param rm
	 * @param name
	 * @param driverStr
	 * @param url
	 * @param userName
	 * @param password
	 */
	public static void WriteNewSqlMapFile(Map<String, Map<String, String>> rm, String name, String driverStr,
			String url, String userName, String password) {
		String vstr = "select 1";
		if (driverStr.toLowerCase().contains("oracle")) {
			vstr = "select 1 from dual";
		}
		String SqlMapfilePath = "";
		Map<String, String> system = rm.get("system");
		if (system != null) {
			SqlMapfilePath = system.get("filePath");
			SqlMapfilePath = SqlMapfilePath.replace("system.mybatis.xml", name + ".mybatis.xml");
		} else {
			String filePathDir = AppConfig.getResourcesPath();
			SqlMapfilePath = filePathDir + File.separator + name + ".mybatis.xml";
		}
		StringBuffer strb = new StringBuffer();
		strb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		strb.append("\n");
		strb.append(
				"<!DOCTYPE configuration PUBLIC \"-//mybatis.org//DTD Config 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-config.dtd\">\n");
		strb.append("\n");
		strb.append("<configuration>\n");
		strb.append("\n");
		strb.append("   <settings>\n");
		strb.append("     <setting name=\"logImpl\" value=\"LOG4J\" />\n");
		strb.append("   </settings>\n");
		strb.append("\n");
		strb.append("   <environments default=\"" + name + "\">\n");
		strb.append("     <environment id=\"" + name + "\">\n");
		strb.append("     	<transactionManager type=\"JDBC\" />\n");
		strb.append("      	<dataSource type=\"POOLED\"> \n");
		strb.append("          <property name=\"driver\" value=\"" + driverStr + "\" /> \n");
		strb.append("          <property name=\"url\" value=\"" + url + "\" /> \n");
		strb.append("          <property name=\"username\" value=\"" + userName + "\" /> \n");
		strb.append("          <property name=\"password\" value=\"" + password + "\" /> \n");
		strb.append("          <property name=\"poolPingEnabled\" value=\"true\" /> \n");
		strb.append("          <property name=\"poolPingQuery\" value=\"" + vstr + "\" /> \n");
		strb.append("          <property name=\"poolPingConnectionsNotUsedFor\" value=\"3600000\" /> \n");
		strb.append("      	</dataSource> \n");
		strb.append("     </environment>\n");
		strb.append("   </environments> \n");
		strb.append("    \n");
		strb.append("   <databaseIdProvider type=\"VENDOR\">\n");
		strb.append("   	<property name=\"SQL Server\" value=\"sqlserver\" />\n");
		strb.append("   	<property name=\"DB2\" value=\"db2\" />\n");
		strb.append("   	<property name=\"Oracle\" value=\"oracle\" />\n");
		strb.append("   	<property name=\"MySQL\" value=\"mysql\" />\n");
		strb.append("   	<property name=\"Dmdb\" value=\"dm\" />\n");
		strb.append("   	<property name=\"Kingdb\" value=\"kingbasees\" />\n");
		strb.append("   </databaseIdProvider>\n");
		strb.append("    \n");
		strb.append("   <mappers>\n");
		strb.append("   	<!-- 公用操作配置 -->\n");
		strb.append("   	<mapper resource=\"com/tlv8/base/db/dao/UtilsMapper.xml\" /> \n");
		strb.append("	\n");
		strb.append("   	<!-- 自定义配置... -->\n\n\n");
		strb.append("   </mappers>\n");
		strb.append("	\n");
		strb.append("</configuration>");
		try {
			FileOutputStream os = new FileOutputStream(new File(SqlMapfilePath));
			DataOutputStream out = new DataOutputStream(os);
			out.write(strb.toString().trim().getBytes("UTF-8"));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将配置信息写入已有的sqlMap文件
	 * 
	 * @param rm
	 * @param name
	 * @param driverStr
	 * @param url
	 * @param userName
	 * @param password
	 */
	public static void WriteSqlMapFile(Map<String, Map<String, String>> rm, String name, String driverStr, String url,
			String userName, String password) {
		String SqlMapfilePath = rm.get(name).get("filePath");
		StringBuffer strb = new StringBuffer();
		strb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		strb.append("\n");
		strb.append(
				"<!DOCTYPE configuration PUBLIC \"-//mybatis.org//DTD Config 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-config.dtd\">\n");
		try {
			File file = new File(SqlMapfilePath);
			StringBuffer fileText = FileAndString.FileToStringBuffer(file);
			String fileStr = "";
			if (fileText.indexOf("<!DOCTYPE") > 0) {
				fileText.delete(fileText.indexOf("<!DOCTYPE"), fileText.indexOf("mybatis-3-config.dtd") + 22);
			}
			fileStr = fileText.toString();
			Document doc = DocumentHelper.parseText(fileStr);
			Element root = doc.getRootElement();
			Element transactionManager = root.element("environments");
			List<Element> environmentli = transactionManager.elements("environment");
			for (Element environment : environmentli) {
				Element dataSource = environment.element("dataSource");
				List propertys = dataSource.elements();
				String vstr = "select 1";
				if (driverStr.toLowerCase().contains("oracle")) {
					vstr = "select 1 from dual";
				}
				for (int j = 0; j < propertys.size(); j++) {
					Element property = (Element) propertys.get(j);
					if (property.attributeValue("name").equalsIgnoreCase("driver")) {
						property.setAttributeValue("value", driverStr);
					}
					if (property.attributeValue("name").equalsIgnoreCase("url")) {
						property.setAttributeValue("value", url);
					}
					if (property.attributeValue("name").equalsIgnoreCase("username")) {
						property.setAttributeValue("value", userName);
					}
					if (property.attributeValue("name").equalsIgnoreCase("password")) {
						property.setAttributeValue("value", password);
					}
					if (property.attributeValue("name").equalsIgnoreCase("poolPingQuery")) {
						property.setAttributeValue("value", vstr);
					}
				}
			}
			file.exists();
			strb.append("\n");
			strb.append(root.asXML().toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			FileOutputStream os = new FileOutputStream(new File(SqlMapfilePath));
			DataOutputStream out = new DataOutputStream(os);
			out.write(strb.toString().trim().getBytes("UTF-8"));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 刪除配置文件
	 * 
	 * @param name
	 * @throws Exception
	 */
	public static void deleteSqlMapFile(String name) throws Exception {
		Map<String, Map<String, String>> rm = GetSqlMapByKey.getKeyDbDriverMap();
		Map<String, String> data = rm.get(name);
		String SqlMapfilePath = "";
		if (data != null) {
			SqlMapfilePath = data.get("filePath");
		} else {
			String filePathDir = AppConfig.getResourcesPath();
			SqlMapfilePath = filePathDir + File.separator + name + ".mybatis.xml";
		}
		File f = new File(SqlMapfilePath);
		f.delete();
		if (SqlMapfilePath.indexOf(StudioConfig.PROJECT_WEB_FOLDER) > 0) {
			String srcFilePath = SqlMapfilePath.substring(0, SqlMapfilePath.indexOf(StudioConfig.PROJECT_WEB_FOLDER));
			String[] sts = SqlMapfilePath.split("/");
			srcFilePath += "src/" + sts[sts.length - 1];
			File fs = new File(srcFilePath);
			fs.delete();
		}
	}

	/**
	 * 刪除业务服务数据源配置-TOMCAT
	 * 
	 * @param name
	 */
	public static void DeleteBIZServerDatasourse(String name) {
		try {
			String configFilePath = GetTomcatconfig.getConfigFilePath();
			if (configFilePath != null) {
				File file = new File(configFilePath);
				if (file.exists()) {
					String fileStr = FileAndString.FileToString(file);
					// Sys.printMsg(fileStr);
					Document doc = DocumentHelper.parseText(fileStr);
					// Sys.packErrMsg(doc.asXML());
					Element root = doc.getRootElement();
					root = root.element("Service");
					root = root.element("Engine");
					root = root.element("Host");
					List Itorate = root.elements();
					for (int i = 0; i < Itorate.size(); i++) {
						String Context = ((Element) Itorate.get(i)).getName();
						Element transactionManager = (Element) Itorate.get(i);
						if (Context.equals("Context") && transactionManager.attributeValue("docBase")
								.endsWith(StudioConfig.PROJECT_PWEB_FOLDER)) {
							List sourses = transactionManager.elements("Resource");
							for (int j = 0; j < sourses.size(); j++) {
								Element dataSource = (Element) sourses.get(j);
								if (name.equals(dataSource.attributeValue("name"))) {
									transactionManager.remove(dataSource);
								}
							}
						}
					}
					OutputFormat format = OutputFormat.createPrettyPrint();
					format.setEncoding("UTF-8");
					format.setIndent(true);
					format.setNewLineAfterNTags(1);
					format.setNewlines(true);
					try {
						XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
						writer.write(doc);
						writer.close();
					} catch (Exception errr) {
						errr.printStackTrace();
					}
					// Sys.printMsg(doc.asXML());
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
