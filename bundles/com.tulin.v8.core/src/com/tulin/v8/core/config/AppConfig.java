package com.tulin.v8.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.core.entity.Spring;
import com.tulin.v8.core.entity.SpringDatasource;

public class AppConfig {
	private static AppConfig a = null;

	public static AppConfig getConfig() {
		if (a == null)
			a = new AppConfig();
		return a;
	}

	public static String getStudioPath() {
		if (isMacOS())
			return new File(System.getProperty("user.dir")).getParentFile().getParentFile().getParent();
		else
			return new File(System.getProperty("user.dir")).getAbsolutePath();
	}

	public static String getOSName() {
		return System.getProperty("os.name").toLowerCase();
	}

	public static boolean isMacOS() {
		return getOSName().contains("mac");
	}

	public static String getWorkspacesPath() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	}

	/**
	 * 获取当前项目的classes位置
	 * 
	 * @return
	 */
	public static String getClassesPath() {
		IProject project = TuLinPlugin.getProject("tlv8");
		// 优先获取tlv8项目没有则获取当前项目
		if (project == null || !project.exists()) {
			project = TuLinPlugin.getCurrentProject();
		}
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
	 * @return
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
			return project.getLocation().toString();
		}
		return ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	}

	/**
	 * 获取spring boot 数据源配置
	 * 
	 * @return SpringDatasource
	 */
	public static SpringDatasource getSpringDatasource() {
		IProject project = TuLinPlugin.getProject("tlv8-main");
		// 优先获取tlv8-main项目没有则获取当前项目
		if (project == null || !project.exists()) {
			project = TuLinPlugin.getCurrentProject();
		}
		if (project != null) {
			IResource resource = project.findMember("src/main/resources/application-druid.yml");
			if (resource != null && resource.exists()) {
				return loadSpringDatasource(resource.getLocation().toFile());
			}
			resource = project.findMember("src/main/resources/application.yml");
			if (resource != null && resource.exists()) {
				return loadSpringDatasource(resource.getLocation().toFile());
			}
			resource = project.findMember("src/main/resources/application.properties");
			if (resource != null && resource.exists()) {
				return ReadProperties(resource.getLocation().toFile());
			}
		}
		return null;
	}

	/**
	 * 加载指定配置文件的数据源配置
	 * 
	 * @param file
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
	 * @param file
	 * @return com.tulin.v8.core.entity.SpringDatasource
	 */
	public static SpringDatasource ReadProperties(File file) {
		SpringDatasource springDatasource = null;
		try {
			Resource resource = new FileSystemResource(file);
			Properties properties = PropertiesLoaderUtils.loadProperties(resource);
			springDatasource = new SpringDatasource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return springDatasource;
	}

}