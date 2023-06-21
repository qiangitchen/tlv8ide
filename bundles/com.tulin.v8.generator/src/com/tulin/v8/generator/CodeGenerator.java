package com.tulin.v8.generator;

import com.google.common.base.CaseFormat;
import com.tulin.v8.core.DBUtils;
import com.tulin.v8.core.Sys;
import com.tulin.v8.core.TuLinPlugin;

import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import zigen.plugin.db.ui.internal.DataBase;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 代码生成器，根据数据表名称生成对应的Model、Mapper、Service、Controller简化开发。
 */
public class CodeGenerator {
	// private final String BASE_PATH = System.getProperty("user.dir");//
	// 项目在硬盘上的基础路径
//	private String TEMPLATE_FILE_PATH = System.getProperty("user.dir") + "/template";
	// CodeGenerator.class.getClassLoader().getResource("template").getFile();//
	// 模板位置

	public String PROJECT_PATH = System.getProperty("user.dir");
	// TuLinPlugin.getCurrentProject().getFolder("src").getLocation().toFile().getAbsolutePath();
	// 项目位置

	// JDBC配置，请修改为你项目的实际配置
	public String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/v8oa?characterEncoding=utf8&useUnicode=true&useSSL=false";
	public String JDBC_USERNAME = "root";
	public String JDBC_PASSWORD = "TLv8MySQL";
	public String JDBC_DIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

	private final String JAVA_PATH = "/src/main/java"; // java文件路径
	private final String RESOURCES_PATH = "/src/main/resources";// 资源文件路径

	private String BASE_PACKAGE = "com.tlv8";
	private String MODEL_PACKAGE = BASE_PACKAGE + ".pojo";// 生成的Model所在包
	private String MAPPER_PACKAGE = BASE_PACKAGE + ".mapper";// 生成的Mapper所在包
	private String SERVICE_PACKAGE = BASE_PACKAGE + ".service";// 生成的Service所在包
	private String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";// 生成的ServiceImpl所在包
	private String CONTROLLER_PACKAGE = BASE_PACKAGE + ".controller";// 生成的Controller所在包

	private String PACKAGE_PATH_SERVICE = packageConvertPath(SERVICE_PACKAGE);// 生成的Service存放路径
	private String PACKAGE_PATH_SERVICE_IMPL = packageConvertPath(SERVICE_IMPL_PACKAGE);// 生成的Service实现存放路径
	private String PACKAGE_PATH_CONTROLLER = packageConvertPath(CONTROLLER_PACKAGE);// 生成的Controller存放路径

	private final String AUTHOR = "TLv8 IDE";// @author
	private final String DATE = new SimpleDateFormat("yyyy/MM/dd").format(new Date());// @date

	private String keyField = "fid";

	public CodeGenerator() {
	}

	public CodeGenerator(DataBase db, String packageName, String keyField) {
		this.BASE_PACKAGE = packageName;
		this.MODEL_PACKAGE = BASE_PACKAGE + ".pojo";// 生成的Model所在包
		this.MAPPER_PACKAGE = BASE_PACKAGE + ".mapper";// 生成的Mapper所在包
		this.SERVICE_PACKAGE = BASE_PACKAGE + ".service";// 生成的Service所在包
		this.SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";// 生成的ServiceImpl所在包
		this.CONTROLLER_PACKAGE = BASE_PACKAGE + ".controller";// 生成的Controller所在包
		this.PACKAGE_PATH_SERVICE = packageConvertPath(SERVICE_PACKAGE);// 生成的Service存放路径
		this.PACKAGE_PATH_SERVICE_IMPL = packageConvertPath(SERVICE_IMPL_PACKAGE);// 生成的Service实现存放路径
		this.PACKAGE_PATH_CONTROLLER = packageConvertPath(CONTROLLER_PACKAGE);// 生成的Controller存放路径
		this.PROJECT_PATH = TuLinPlugin.getCurrentProject().getLocation().toFile().getAbsolutePath();
		this.JDBC_URL = db.getDbConfig().getUrl();
		this.JDBC_USERNAME = db.getDbConfig().getUserId();
		this.JDBC_PASSWORD = db.getDbConfig().getPassword();
		this.JDBC_DIVER_CLASS_NAME = db.getDbConfig().getDriverName();
		this.keyField = keyField;
	}

	public CodeGenerator(String dbkey, String packageName, String keyField, String projectPath) {
		this.BASE_PACKAGE = packageName;
		this.MODEL_PACKAGE = BASE_PACKAGE + ".pojo";// 生成的Model所在包
		this.MAPPER_PACKAGE = BASE_PACKAGE + ".mapper";// 生成的Mapper所在包
		this.SERVICE_PACKAGE = BASE_PACKAGE + ".service";// 生成的Service所在包
		this.SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";// 生成的ServiceImpl所在包
		this.CONTROLLER_PACKAGE = BASE_PACKAGE + ".controller";// 生成的Controller所在包
		this.PACKAGE_PATH_SERVICE = packageConvertPath(SERVICE_PACKAGE);// 生成的Service存放路径
		this.PACKAGE_PATH_SERVICE_IMPL = packageConvertPath(SERVICE_IMPL_PACKAGE);// 生成的Service实现存放路径
		this.PACKAGE_PATH_CONTROLLER = packageConvertPath(CONTROLLER_PACKAGE);// 生成的Controller存放路径
		this.PROJECT_PATH = projectPath;
		this.JDBC_URL = DBUtils.getDataUrl(dbkey);
		this.JDBC_USERNAME = DBUtils.getUserName(dbkey);
		this.JDBC_PASSWORD = DBUtils.getPassWord(dbkey);
		this.JDBC_DIVER_CLASS_NAME = DBUtils.getDriverClassName(dbkey);
		this.keyField = keyField;
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		while (true) {
			System.out.println("请输入表名:");
			String tableName = new Scanner(System.in).next();
			new CodeGenerator().genCode(tableName);
			// genCodeByCustomModelName("输入表名","输入自定义Model名称");
		}
	}

	/**
	 * 通过数据表名称生成代码，Model 名称通过解析数据表名称获得，下划线转大驼峰的形式。 如输入表名称 "t_user_detail" 将生成
	 * TUserDetail、TUserDetailMapper、TUserDetailService ...
	 *
	 * @param tableNames 数据表名称...
	 */
	public void genCode(String... tableNames) {
		for (String tableName : tableNames) {
			genCodeByCustomModelName(tableName, null, false, true);
		}
	}

	/**
	 * 通过数据表名称，和自定义的 Model 名称生成代码 如输入表名称 "t_user_detail" 和自定义的 Model 名称 "User" 将生成
	 * User、UserMapper、UserService ...
	 *
	 * @param tableName          数据表名称
	 * @param modelName          自定义的 Model 名称
	 * @param isAutoincrementKey 是否自增主键
	 */
	public void genCodeByCustomModelName(String tableName, String modelName, boolean isAutoincrementKey,
			boolean createController) {
		genModelAndMapper(tableName, modelName, isAutoincrementKey);
		genService(tableName, modelName);
		if (createController) {
			genController(tableName, modelName);
		}
	}

	public void genModelAndMapper(String tableName, String modelName, boolean isAutoincrementKey) {
		Context context = new Context(ModelType.FLAT);
		context.setId("Potato");
		context.setTargetRuntime("MyBatis3Simple");
		context.addProperty(PropertyRegistry.CONTEXT_BEGINNING_DELIMITER, "`");
		context.addProperty(PropertyRegistry.CONTEXT_ENDING_DELIMITER, "`");

		JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
		jdbcConnectionConfiguration.setConnectionURL(JDBC_URL);
		jdbcConnectionConfiguration.setUserId(JDBC_USERNAME);
		jdbcConnectionConfiguration.setPassword(JDBC_PASSWORD);
		jdbcConnectionConfiguration.setDriverClass(JDBC_DIVER_CLASS_NAME);
		context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

		PluginConfiguration pluginConfiguration = new PluginConfiguration();
		pluginConfiguration.setConfigurationType("org.mybatis.generator.plugins.SerializablePlugin");
		context.addPluginConfiguration(pluginConfiguration);

		JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
		File javaDir = new File(PROJECT_PATH + JAVA_PATH);
		if (!javaDir.exists()) {
			javaDir.mkdirs();
		}
		javaModelGeneratorConfiguration.setTargetProject(PROJECT_PATH + JAVA_PATH);
		javaModelGeneratorConfiguration.setTargetPackage(MODEL_PACKAGE);
		context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

		SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
		File dir = new File(PROJECT_PATH + RESOURCES_PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		sqlMapGeneratorConfiguration.setTargetProject(PROJECT_PATH + RESOURCES_PATH);
		sqlMapGeneratorConfiguration.setTargetPackage("mapper");
		context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);

		JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
		javaClientGeneratorConfiguration.setTargetProject(PROJECT_PATH + JAVA_PATH);
		javaClientGeneratorConfiguration.setTargetPackage(MAPPER_PACKAGE);
		javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
		context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

		TableConfiguration tableConfiguration = new TableConfiguration(context);
		tableConfiguration.setTableName(tableName);
		if (StringUtils.isNotEmpty(modelName))
			tableConfiguration.setDomainObjectName(modelName);
		if (isAutoincrementKey) {
			tableConfiguration.setGeneratedKey(new GeneratedKey(keyField, "JDBC", false, null));
		}
		context.addTableConfiguration(tableConfiguration);

		List<String> warnings;
		MyBatisGenerator generator;
		try {
			Configuration config = new Configuration();
			config.addContext(context);
			config.validate();

			boolean overwrite = true;
			DefaultShellCallback callback = new DefaultShellCallback(overwrite);
			warnings = new ArrayList<String>();
			generator = new MyBatisGenerator(config, callback, warnings);
			generator.generate(null);
		} catch (Exception e) {
			throw new RuntimeException("生成Model和Mapper失败", e);
		}

		if (generator.getGeneratedJavaFiles().isEmpty() || generator.getGeneratedXmlFiles().isEmpty()) {
			throw new RuntimeException("生成Model和Mapper失败：" + warnings);
		}
		if (StringUtils.isEmpty(modelName))
			modelName = tableNameConvertUpperCamel(tableName);
		Sys.printMsg(modelName + ".java 生成成功");
		Sys.printMsg(modelName + "Mapper.java 生成成功");
		Sys.printMsg(modelName + "Mapper.xml 生成成功");
	}

	public void genService(String tableName, String modelName) {
		try {
			freemarker.template.Configuration cfg = getConfiguration();
			String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName)
					: modelName;
			Map<String, Object> data = new HashMap<>();
			data.put("date", DATE);
			data.put("author", AUTHOR);
			data.put("modelNameUpperCamel", modelNameUpperCamel);
			data.put("modelNameLowerCamel", tableNameConvertLowerCamel(tableName));
			data.put("basePackage", BASE_PACKAGE);
			data.put("modelPackage", MODEL_PACKAGE);
			data.put("tableName", tableName);

			File file = new File(
					PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_SERVICE + modelNameUpperCamel + "Service.java");
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			Template template = new Template("service.ftl",
					new InputStreamReader(CodeGenerator.class.getResourceAsStream("/template/service.ftl"), "UTF-8"),
					cfg);
//			Template template = Template.getPlainTextTemplate("service.ftl", getContent(CodeGenerator.class.getResourceAsStream("/template/service.ftl")), cfg);
			template.process(data, new FileWriter(file));
//			cfg.getTemplate("service.ftl").process(data, new FileWriter(file));
			Sys.printMsg(modelNameUpperCamel + "Service.java 生成成功");

			File file1 = new File(
					PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_SERVICE_IMPL + modelNameUpperCamel + "ServiceImpl.java");
			if (!file1.getParentFile().exists()) {
				file1.getParentFile().mkdirs();
			}
			Template template1 = new Template("service-impl.ftl", new InputStreamReader(
					CodeGenerator.class.getResourceAsStream("/template/service-impl.ftl"), "UTF-8"), cfg);
//			Template template1 = Template.getPlainTextTemplate("service-impl.ftl", getContent(CodeGenerator.class.getResourceAsStream("/template/service-impl.ftl")), cfg);
			template1.process(data, new FileWriter(file1));
//			cfg.getTemplate("service-impl.ftl").process(data, new FileWriter(file1));
			Sys.printMsg(modelNameUpperCamel + "ServiceImpl.java 生成成功");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("生成Service失败", e);
		}
	}

	public void genController(String tableName, String modelName) {
		try {
			freemarker.template.Configuration cfg = getConfiguration();

			Map<String, Object> data = new HashMap<>();
			data.put("date", DATE);
			data.put("author", AUTHOR);
			String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName)
					: modelName;
			data.put("baseRequestMapping", modelNameConvertMappingPath(modelNameUpperCamel));
			data.put("modelNameUpperCamel", modelNameUpperCamel);
			data.put("modelNameLowerCamel", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelNameUpperCamel));
			data.put("basePackage", BASE_PACKAGE);
			data.put("modelPackage", MODEL_PACKAGE);
			data.put("tableName", tableName);

			File file = new File(
					PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_CONTROLLER + modelNameUpperCamel + "Controller.java");
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			// cfg.getTemplate("controller-restful.ftl").process(data, new
			// FileWriter(file));
			Template template = new Template("controller.ftl",
					new InputStreamReader(CodeGenerator.class.getResourceAsStream("/template/controller.ftl"), "UTF-8"),
					cfg);
//			Template template = Template.getPlainTextTemplate("controller.ftl", getContent(CodeGenerator.class.getResourceAsStream("/template/controller.ftl")), cfg);
			template.process(data, new FileWriter(file));
//			cfg.getTemplate("controller.ftl").process(data, new FileWriter(file));

			Sys.printMsg(modelNameUpperCamel + "Controller.java 生成成功");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("生成Controller失败", e);
		}

	}

	private freemarker.template.Configuration getConfiguration() throws IOException {
		freemarker.template.Configuration cfg = new freemarker.template.Configuration(
				freemarker.template.Configuration.VERSION_2_3_23);
		// cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
		return cfg;
	}

	public static String getContent(InputStream fileiptstream) throws Exception {
		BufferedReader Strreader = new BufferedReader(new InputStreamReader(fileiptstream));
		StringBuffer fileText = new StringBuffer();
		String fileStr = "";
		while ((fileStr = Strreader.readLine()) != null) {
			fileText.append(fileStr);
		}
		Sys.printMsg(fileText);
		return fileText.toString();
	}

	public static String tableNameConvertLowerCamel(String tableName) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName.toLowerCase());
	}

	public static String tableNameConvertUpperCamel(String tableName) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.toLowerCase());

	}

	public static String tableNameConvertMappingPath(String tableName) {
		tableName = tableName.toLowerCase();// 兼容使用大写的表名
		return "/" + (tableName.contains("_") ? tableName.replaceAll("_", "/") : tableName);
	}

	public static String modelNameConvertMappingPath(String modelName) {
		String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, modelName);
		return tableNameConvertMappingPath(tableName);
	}

	public static String packageConvertPath(String packageName) {
		return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
	}

}
