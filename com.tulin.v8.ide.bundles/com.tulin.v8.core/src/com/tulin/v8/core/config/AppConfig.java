package com.tulin.v8.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.core.entity.DynamicDatasource;
import com.tulin.v8.core.entity.JdbcDatasource;
import com.tulin.v8.core.entity.Spring;
import com.tulin.v8.core.entity.SpringDatasource;

/**
 * 公用基础配置
 * 
 * @author 陈乾
 *
 */
public class AppConfig {
	/**
	 * 获取studio所在位置
	 * 
	 * @return 文件夹路径
	 */
	public static String getStudioPath() {
		if (isMacOS())
			return new File(System.getProperty("user.dir")).getParentFile().getParentFile().getParent();
		else
			return new File(System.getProperty("user.dir")).getAbsolutePath();
	}

	/**
	 * 获取操作系统名称
	 * 
	 * @return 系统名称小写
	 */
	public static String getOSName() {
		return System.getProperty("os.name").toLowerCase();
	}

	/**
	 * 判断是否问苹果操作系统
	 * 
	 * @return boolean
	 */
	public static boolean isMacOS() {
		return getOSName().contains("mac");
	}

	/**
	 * 获取工作空间位置
	 * 
	 * @return 文件夹路径
	 */
	public static String getWorkspacesPath() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	}

	/**
	 * 获取当前项目的classes位置
	 * 
	 * @return String
	 */
	public static String getClassesPath() {
		IProject project = TuLinPlugin.getCurrentProject();
		IResource resource = TuLinPlugin.getProjectWebFolder(project);
		if (resource == null || !resource.exists()) {
			resource = project.findMember("src/main/resources");
			if (resource != null && resource.exists()) {
				return resource.getLocation().toString();
			}
		}
		return project.findMember(resource.getName() + "/WEB-INF/classes").getLocation().toString();
	}

	/**
	 * 获取资源位置
	 * 
	 * @return String
	 */
	public static String getResourcesPath() {
		IProject project = TuLinPlugin.getProject("tlv8");
		// 优先获取tlv8项目没有则获取当前项目
		if (project == null || !project.exists()) {
			project = TuLinPlugin.getCurrentProject();
		}
		if (project != null) {
			IResource dsource = project.findMember("src/main/resources");
			if (dsource == null || !dsource.exists()) {
				dsource = project.findMember("src");
			}
			if (dsource != null) {
				return dsource.getLocation().toString();
			}
			IPath path = project.getLocation();
			if (path != null) {
				return path.toString();
			}
		}
		return ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	}

	public static Map<String, DynamicDatasource> getDynamicDatasources() {
		IProject project = TuLinPlugin.getCurrentProject();
		Map<String, DynamicDatasource> map = getDynamicDatasources(project);
		if (map.isEmpty()) {
			if (project == null || !project.exists()) {
				project = TuLinPlugin.findProject("main");
			}
			if (project == null || !project.exists()) {
				project = TuLinPlugin.findProject("admin");
			}
			if (project != null) {
				map = getDynamicDatasources(project);
			}
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, DynamicDatasource> getDynamicDatasources(IProject project) {
		Map<String, DynamicDatasource> map = new HashMap<>();
		try {
			IResource resource = project.findMember("src/main/resources/application.yml");
			if (resource != null && resource.exists()) {
				Yaml yaml = new Yaml();
				FileInputStream in = new FileInputStream(resource.getLocation().toFile());
				Map<String, Object> loadedMap = yaml.load(in);
				Map<String, Object> spring = (Map<String, Object>) loadedMap.get("spring");
				Map<String, Object> datasource = (Map<String, Object>) spring.get("datasource");
				if (datasource != null) {
					Map<String, Object> dynamic = (Map<String, Object>) datasource.get("dynamic");
					Map<String, Object> dynamicDatasource = (Map<String, Object>) dynamic.get("datasource");
					for (String name : dynamicDatasource.keySet()) {
						map.put(name, new DynamicDatasource(name, (Map<String, Object>) dynamicDatasource.get(name)));
					}
				} else {
					Map<String, Object> profiles = (Map<String, Object>) spring.get("profiles");
					String active = (String) profiles.get("active");
					IResource aresource = project.findMember("src/main/resources/application-" + active + ".yml");
					if (aresource != null && aresource.exists()) {
						Yaml ayaml = new Yaml();
						FileInputStream ain = new FileInputStream(aresource.getLocation().toFile());
						Map<String, Object> aloadedMap = ayaml.load(ain);
						Map<String, Object> aspring = (Map<String, Object>) aloadedMap.get("spring");
						Map<String, Object> adatasource = (Map<String, Object>) aspring.get("datasource");
						Map<String, Object> adynamic = (Map<String, Object>) adatasource.get("dynamic");
						Map<String, Object> dynamicDatasource = (Map<String, Object>) adynamic.get("datasource");
						for (String name : dynamicDatasource.keySet()) {
							map.put(name,
									new DynamicDatasource(name, (Map<String, Object>) dynamicDatasource.get(name)));
						}
					}
				}
			}
		} catch (Exception | Error e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取spring boot 数据源配置
	 * 
	 * @return SpringDatasource
	 */
	public static SpringDatasource getSpringDatasource() {
		IProject project = TuLinPlugin.getCurrentProject();
		SpringDatasource dataSource = getSpringDatasource(project);
		if (dataSource == null) {
			project = TuLinPlugin.getProject("tlv8");
//			if (project == null || !project.exists()) {
//				project = TuLinPlugin.getProject("tlv8-main");
//			}
//			if (project == null || !project.exists()) {
//				project = TuLinPlugin.getProject("tlv8-v3-main");
//			}
//			if (project == null || !project.exists()) {
//				project = TuLinPlugin.getProject("tlv8-admin");
//			}
			if (project == null || !project.exists()) {
				project = TuLinPlugin.findProject("main");
			}
			if (project == null || !project.exists()) {
				project = TuLinPlugin.findProject("admin");
			}
			if (project != null) {
				dataSource = getSpringDatasource(project);
			}
		}
		return dataSource;
	}

	/**
	 * 获取spring boot 数据源配置
	 * 
	 * @param project 项目
	 * @return SpringDatasource
	 */
	public static SpringDatasource getSpringDatasource(IProject project) {
		if (project != null) {
			IResource resource = project.findMember("src/main/resources/application-druid.yml");
			if (resource != null && resource.exists()) {
				return loadSpringDatasource(resource.getLocation().toFile());
			}
			resource = project.findMember("src/main/resources/application-dev.yml");
			if (resource != null && resource.exists()) {
				return loadSpringDatasource(resource.getLocation().toFile());
			}
			resource = project.findMember("src/main/resources/application-prod.yml");
			if (resource != null && resource.exists()) {
				return loadSpringDatasource(resource.getLocation().toFile());
			}
			resource = project.findMember("src/main/resources/application-local.yml");
			if (resource != null && resource.exists()) {
				return loadSpringDatasource(resource.getLocation().toFile());
			}
			resource = project.findMember("src/main/resources/application.yml");
			if (resource != null && resource.exists()) {
				return loadSpringDatasource(resource.getLocation().toFile());
			}
			resource = project.findMember("src/main/resources/application.properties");
			if (resource != null && resource.exists()) {
				return readProperties(resource.getLocation().toFile());
			}
		}
		return null;
	}

	/**
	 * 加载指定配置文件的数据源配置
	 * 
	 * @param file spring配置文件
	 * @return com.tulin.v8.core.entity.SpringDatasource
	 */
	public static SpringDatasource loadSpringDatasource(File file) {
		try {
			Yaml yaml = new Yaml(new Constructor(Spring.class, new LoaderOptions()));
			Spring spring = yaml.load(new FileInputStream(file));
			return spring.getSpring().getDatasource();
		} catch (Exception e) {
			YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
			yaml.setResources(new FileSystemResource(file));
			Properties properties = yaml.getObject();
			return new SpringDatasource(properties);
		}
	}

	/**
	 * 加载properties文件里的数据源配置
	 * 
	 * @param file 配置文件
	 * @return com.tulin.v8.core.entity.SpringDatasource
	 */
	public static SpringDatasource readProperties(File file) {
		SpringDatasource springDatasource = null;
		try {
			Resource resource = new FileSystemResource(file);
			Properties properties = PropertiesLoaderUtils.loadProperties(resource);
			springDatasource = new SpringDatasource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!StringUtils.isEmpty(springDatasource.getUrl())) {
			return springDatasource;
		}
		return null;
	}

	/**
	 * 加载jdbc数据源
	 * 
	 * @param file
	 * @return JdbcDatasource
	 */
	public static JdbcDatasource readJdbcProperties(File file) {
		JdbcDatasource jdbcDatasource = null;
		try {
			Resource resource = new FileSystemResource(file);
			Properties properties = PropertiesLoaderUtils.loadProperties(resource);
			jdbcDatasource = new JdbcDatasource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!StringUtils.isEmpty(jdbcDatasource.getUrl())) {
			return jdbcDatasource;
		}
		return null;
	}

	/**
	 * 获取spring boot 数据源配置
	 * 
	 * @return SpringDatasource
	 */
	public static JdbcDatasource getJdbcDatasource() {
		IProject project = TuLinPlugin.getProject("tlv8-web");
		// 优先获取tlv8-main项目没有则获取当前项目
		if (project == null || !project.exists()) {
			project = TuLinPlugin.getCurrentProject();
		}
		if (project != null) {
			IResource resource = project.findMember("src/main/resources/jdbc.properties");
			if (resource != null && resource.exists()) {
				return readJdbcProperties(resource.getLocation().toFile());
			}
			resource = project.findMember("src/jdbc.properties");
			if (resource != null && resource.exists()) {
				return readJdbcProperties(resource.getLocation().toFile());
			}
		}
		return null;
	}

}