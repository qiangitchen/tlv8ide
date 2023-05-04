package zigen.plugin.db;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.tulin.v8.core.Configuration;
import com.tulin.v8.core.DBUtils;
import com.tulin.v8.core.FileAndString;
import com.tulin.v8.core.XMLFormator;
import com.tulin.v8.core.config.AppConfig;
import com.tulin.v8.core.entity.JdbcDatasource;
import com.tulin.v8.core.entity.SpringDatasource;

@SuppressWarnings({ "rawtypes", "deprecation" })
public class Connection {
	/**
	 * 数据源配置转移到 数据库管理中
	 */
	public static void ConnectionTranse(String filePath) {
		try {
			Document doc = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
					+ "<section name=\"Workbench\"></section>");
			try {
				Map<String, Map<String, String>> rm = Configuration.getConfig();
				Iterator conIt = rm.entrySet().iterator();
				while (conIt.hasNext()) {
					Entry entry = (Entry) conIt.next();
					String name = (String) entry.getKey();
					Element section = doc.getRootElement().addElement("section");
					section.setAttributeValue("name", name);
					{
						Map<String, String> m = rm.get(name);

						Element item4 = section.addElement("item");
						item4.setAttributeValue("key", "CONNECT_AS_INFORMATION_SCHEMA");
						item4.setAttributeValue("value", "false");

						Element item11 = section.addElement("item");
						item11.setAttributeValue("key", "CHARSET");
						item11.setAttributeValue("value", "");

						Element item12 = section.addElement("item");
						item12.setAttributeValue("key", "CONNECT_AS_SYSDBA");
						item12.setAttributeValue("value", "false");

						Element item14 = section.addElement("item");
						item14.setAttributeValue("key", "PASSWORD");
						item14.setAttributeValue("value", m.get("password"));

						Element item5 = section.addElement("item");
						item5.setAttributeValue("key", "AUTOCOMMIT");
						item5.setAttributeValue("value", "true");

						Element item0 = section.addElement("item");
						item0.setAttributeValue("key", "CONVUNICODE");
						item0.setAttributeValue("value", "false");

						Element item9 = section.addElement("item");
						item9.setAttributeValue("key", "ONLYDEFAULTSCHEMA");
						item9.setAttributeValue("value", "false");

						Element item3 = section.addElement("item");
						item3.setAttributeValue("key", "SCHEMA");
						if (DBUtils.IsOracleDB(name) || DBUtils.IsDMDB(name)) {
							item3.setAttributeValue("value", m.get("username"));
						} else if (DBUtils.IsMySQLDB(name)) {
							String linkurl = m.get("url");
							if (linkurl.indexOf("?") > 0) {
								linkurl = linkurl.substring(0, linkurl.indexOf("?"));
							}
							try {
								String[] urlsp = linkurl.split("/");
								String schema = urlsp[urlsp.length - 1];
								item3.setAttributeValue("value", schema);
							} catch (Exception e) {
							}
						} else if (DBUtils.IsPostgreSQL(name)) {
							item3.setAttributeValue("value", "public");
						} else {
							item3.setAttributeValue("value", "dbo");
						}

						Element item8 = section.addElement("item");
						item8.setAttributeValue("key", "KEY_FILTER_PATTERN");
						if (DBUtils.IsDMDB(name)) {
							item8.setAttributeValue("value", name);
						} else {
							item8.setAttributeValue("value", "");
						}

						Element item15 = section.addElement("item");
						item15.setAttributeValue("key", "CONNECT_AS_SYSOPEA");
						item15.setAttributeValue("value", "false");

						Element item16 = section.addElement("item");
						item16.setAttributeValue("key", "USERID");
						item16.setAttributeValue("value", m.get("username"));

						Element item6 = section.addElement("item");
						item6.setAttributeValue("key", "DRIVER");
						item6.setAttributeValue("value", m.get("driver"));

						Element item13 = section.addElement("item");
						item13.setAttributeValue("key", "URL");
						item13.setAttributeValue("value", m.get("url"));

						Element item1 = section.addElement("item");
						item1.setAttributeValue("key", "NAME");
						item1.setAttributeValue("value", name);

						Element item10 = section.addElement("item");
						item10.setAttributeValue("key", "JDBCTYPE");
						item10.setAttributeValue("value", "2");

						Element item2 = section.addElement("item");
						item2.setAttributeValue("key", "KEY_IS_FILTER_PATTERN");
						item2.setAttributeValue("value", "false");

						Element item7 = section.addElement("item");
						item7.setAttributeValue("key", "NOLOCKMODE");
						item7.setAttributeValue("value", "true");

						Element item17 = section.addElement("list");
						item17.setAttributeValue("key", "CLASSPATH");

						String schema = m.get("username");
						if (DBUtils.IsOracleDB(name) || DBUtils.IsDMDB(name)) {
							schema = schema.toUpperCase();
						}
						Element item18 = section.addElement("schemas");
						item18.setAttributeValue("key", "KEY_DISPLAYED_SCHEMAS");
						if (DBUtils.IsOracleDB(name) || DBUtils.IsDMDB(name)) {
							Element item18_1 = item18.addElement("schema");
							item18_1.setAttributeValue("checked", "true");
							item18_1.setAttributeValue("name", schema);
						} else if (DBUtils.IsMySQLDB(name)) {

						} else {
							Element item18_1 = item18.addElement("schema");
							item18_1.setAttributeValue("checked", "true");
							item18_1.setAttributeValue("name", "DBO");
							Element item18_2 = item18.addElement("schema");
							item18_2.setAttributeValue("checked", "false");
							item18_2.setAttributeValue("name", "GUEST");
							Element item18_3 = item18.addElement("schema");
							item18_3.setAttributeValue("checked", "false");
							item18_3.setAttributeValue("name", "INFORMATION_SCHEMA");
							Element item18_4 = item18.addElement("schema");
							item18_4.setAttributeValue("checked", "false");
							item18_4.setAttributeValue("name", "SYS");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			springConnectionTranse(doc);// 自动加载spring数据源配置
			jdbcConnectionTranse(doc);// 自动加载jdbc数据源配置
			String text = XMLFormator.formatXML(doc);
			FileAndString.string2File(text, filePath);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 数据源配置 Spring Boot
	 * 
	 * @param doc
	 */
	public static void springConnectionTranse(Document doc) {
		SpringDatasource springData = AppConfig.getSpringDatasource();
		if (springData != null) {
			Element section = doc.getRootElement().addElement("section");
			String name = "spring";
			section.setAttributeValue("name", name);
			Element item4 = section.addElement("item");
			item4.setAttributeValue("key", "CONNECT_AS_INFORMATION_SCHEMA");
			item4.setAttributeValue("value", "false");

			Element item11 = section.addElement("item");
			item11.setAttributeValue("key", "CHARSET");
			item11.setAttributeValue("value", "");

			Element item12 = section.addElement("item");
			item12.setAttributeValue("key", "CONNECT_AS_SYSDBA");
			item12.setAttributeValue("value", "false");

			Element item14 = section.addElement("item");
			item14.setAttributeValue("key", "PASSWORD");
			item14.setAttributeValue("value", springData.getPassword());

			Element item5 = section.addElement("item");
			item5.setAttributeValue("key", "AUTOCOMMIT");
			item5.setAttributeValue("value", "true");

			Element item0 = section.addElement("item");
			item0.setAttributeValue("key", "CONVUNICODE");
			item0.setAttributeValue("value", "false");

			Element item9 = section.addElement("item");
			item9.setAttributeValue("key", "ONLYDEFAULTSCHEMA");
			item9.setAttributeValue("value", "false");

			Element item3 = section.addElement("item");
			item3.setAttributeValue("key", "SCHEMA");
			if (springData.isOracleDB() || springData.isDMDB()) {
				item3.setAttributeValue("value", springData.getUsername());
			} else if (springData.isMySQLDB()) {
				String linkurl = springData.getUrl();
				if (linkurl.indexOf("?") > 0) {
					linkurl = linkurl.substring(0, linkurl.indexOf("?"));
				}
				try {
					String[] urlsp = linkurl.split("/");
					String schema = urlsp[urlsp.length - 1];
					item3.setAttributeValue("value", schema);
				} catch (Exception e) {
				}
			} else if (springData.isPostgreSQL()) {
				item3.setAttributeValue("value", "public");
			} else {
				item3.setAttributeValue("value", "dbo");
			}

			Element item8 = section.addElement("item");
			item8.setAttributeValue("key", "KEY_FILTER_PATTERN");
			if (springData.isDMDB()) {
				item8.setAttributeValue("value", name);
			} else {
				item8.setAttributeValue("value", "");
			}

			Element item15 = section.addElement("item");
			item15.setAttributeValue("key", "CONNECT_AS_SYSOPEA");
			item15.setAttributeValue("value", "false");

			Element item16 = section.addElement("item");
			item16.setAttributeValue("key", "USERID");
			item16.setAttributeValue("value", springData.getUsername());

			Element item6 = section.addElement("item");
			item6.setAttributeValue("key", "DRIVER");
			item6.setAttributeValue("value", springData.getDriverClassName());

			Element item13 = section.addElement("item");
			item13.setAttributeValue("key", "URL");
			item13.setAttributeValue("value", springData.getUrl());

			Element item1 = section.addElement("item");
			item1.setAttributeValue("key", "NAME");
			item1.setAttributeValue("value", name);

			Element item10 = section.addElement("item");
			item10.setAttributeValue("key", "JDBCTYPE");
			item10.setAttributeValue("value", "2");

			Element item2 = section.addElement("item");
			item2.setAttributeValue("key", "KEY_IS_FILTER_PATTERN");
			item2.setAttributeValue("value", "false");

			Element item7 = section.addElement("item");
			item7.setAttributeValue("key", "NOLOCKMODE");
			item7.setAttributeValue("value", "true");

			Element item17 = section.addElement("list");
			item17.setAttributeValue("key", "CLASSPATH");

			String schema = springData.getUsername();
			if (springData.isOracleDB() || springData.isDMDB()) {
				schema = schema.toUpperCase();
			}
			Element item18 = section.addElement("schemas");
			item18.setAttributeValue("key", "KEY_DISPLAYED_SCHEMAS");
			if (springData.isOracleDB() || springData.isDMDB()) {
				Element item18_1 = item18.addElement("schema");
				item18_1.setAttributeValue("checked", "true");
				item18_1.setAttributeValue("name", schema);
			} else if (springData.isMySQLDB()) {

			} else {
				Element item18_1 = item18.addElement("schema");
				item18_1.setAttributeValue("checked", "true");
				item18_1.setAttributeValue("name", "DBO");
				Element item18_2 = item18.addElement("schema");
				item18_2.setAttributeValue("checked", "false");
				item18_2.setAttributeValue("name", "GUEST");
				Element item18_3 = item18.addElement("schema");
				item18_3.setAttributeValue("checked", "false");
				item18_3.setAttributeValue("name", "INFORMATION_SCHEMA");
				Element item18_4 = item18.addElement("schema");
				item18_4.setAttributeValue("checked", "false");
				item18_4.setAttributeValue("name", "SYS");
			}
		}
	}

	/**
	 * 数据源配置 JDBC Data
	 * 
	 * @param doc
	 */
	public static void jdbcConnectionTranse(Document doc) {
		JdbcDatasource jdbcData = AppConfig.getJdbcDatasource();
		if (jdbcData != null) {
			Element section = doc.getRootElement().addElement("section");
			String name = "jdbc";
			section.setAttributeValue("name", name);
			Element item4 = section.addElement("item");
			item4.setAttributeValue("key", "CONNECT_AS_INFORMATION_SCHEMA");
			item4.setAttributeValue("value", "false");

			Element item11 = section.addElement("item");
			item11.setAttributeValue("key", "CHARSET");
			item11.setAttributeValue("value", "");

			Element item12 = section.addElement("item");
			item12.setAttributeValue("key", "CONNECT_AS_SYSDBA");
			item12.setAttributeValue("value", "false");

			Element item14 = section.addElement("item");
			item14.setAttributeValue("key", "PASSWORD");
			item14.setAttributeValue("value", jdbcData.getPassword());

			Element item5 = section.addElement("item");
			item5.setAttributeValue("key", "AUTOCOMMIT");
			item5.setAttributeValue("value", "true");

			Element item0 = section.addElement("item");
			item0.setAttributeValue("key", "CONVUNICODE");
			item0.setAttributeValue("value", "false");

			Element item9 = section.addElement("item");
			item9.setAttributeValue("key", "ONLYDEFAULTSCHEMA");
			item9.setAttributeValue("value", "false");

			Element item3 = section.addElement("item");
			item3.setAttributeValue("key", "SCHEMA");
			if (jdbcData.isOracleDB() || jdbcData.isDMDB()) {
				item3.setAttributeValue("value", jdbcData.getUsername());
			} else if (jdbcData.isMySQLDB()) {
				String linkurl = jdbcData.getUrl();
				if (linkurl.indexOf("?") > 0) {
					linkurl = linkurl.substring(0, linkurl.indexOf("?"));
				}
				try {
					String[] urlsp = linkurl.split("/");
					String schema = urlsp[urlsp.length - 1];
					item3.setAttributeValue("value", schema);
				} catch (Exception e) {
				}
			} else if (jdbcData.isPostgreSQL()) {
				item3.setAttributeValue("value", "public");
			} else {
				item3.setAttributeValue("value", "dbo");
			}

			Element item8 = section.addElement("item");
			item8.setAttributeValue("key", "KEY_FILTER_PATTERN");
			if (jdbcData.isDMDB()) {
				item8.setAttributeValue("value", name);
			} else {
				item8.setAttributeValue("value", "");
			}

			Element item15 = section.addElement("item");
			item15.setAttributeValue("key", "CONNECT_AS_SYSOPEA");
			item15.setAttributeValue("value", "false");

			Element item16 = section.addElement("item");
			item16.setAttributeValue("key", "USERID");
			item16.setAttributeValue("value", jdbcData.getUsername());

			Element item6 = section.addElement("item");
			item6.setAttributeValue("key", "DRIVER");
			item6.setAttributeValue("value", jdbcData.getDriver());

			Element item13 = section.addElement("item");
			item13.setAttributeValue("key", "URL");
			item13.setAttributeValue("value", jdbcData.getUrl());

			Element item1 = section.addElement("item");
			item1.setAttributeValue("key", "NAME");
			item1.setAttributeValue("value", name);

			Element item10 = section.addElement("item");
			item10.setAttributeValue("key", "JDBCTYPE");
			item10.setAttributeValue("value", "2");

			Element item2 = section.addElement("item");
			item2.setAttributeValue("key", "KEY_IS_FILTER_PATTERN");
			item2.setAttributeValue("value", "false");

			Element item7 = section.addElement("item");
			item7.setAttributeValue("key", "NOLOCKMODE");
			item7.setAttributeValue("value", "true");

			Element item17 = section.addElement("list");
			item17.setAttributeValue("key", "CLASSPATH");

			String schema = jdbcData.getUsername();
			if (jdbcData.isOracleDB() || jdbcData.isDMDB()) {
				schema = schema.toUpperCase();
			}
			Element item18 = section.addElement("schemas");
			item18.setAttributeValue("key", "KEY_DISPLAYED_SCHEMAS");
			if (jdbcData.isOracleDB() || jdbcData.isDMDB()) {
				Element item18_1 = item18.addElement("schema");
				item18_1.setAttributeValue("checked", "true");
				item18_1.setAttributeValue("name", schema);
			} else if (jdbcData.isMySQLDB()) {

			} else {
				Element item18_1 = item18.addElement("schema");
				item18_1.setAttributeValue("checked", "true");
				item18_1.setAttributeValue("name", "DBO");
				Element item18_2 = item18.addElement("schema");
				item18_2.setAttributeValue("checked", "false");
				item18_2.setAttributeValue("name", "GUEST");
				Element item18_3 = item18.addElement("schema");
				item18_3.setAttributeValue("checked", "false");
				item18_3.setAttributeValue("name", "INFORMATION_SCHEMA");
				Element item18_4 = item18.addElement("schema");
				item18_4.setAttributeValue("checked", "false");
				item18_4.setAttributeValue("name", "SYS");
			}
		}
	}
}
