package com.tulin.v8.ide.ui.editors.struts;

import java.util.HashMap;
import java.util.Map;

import com.tulin.v8.ide.StudioPlugin;

class AttributeCode {
	public static String STRUTS_ENCODING = "struts.i18n.encoding";
	public static String DYNAMICMETHODINVOCATION = "struts.enable.DynamicMethodInvocation";
	public static String STRUTS_DEVMODE = "struts.devMode";
	public static String STRUTS_RESOURCES = "struts.custom.i18n.resources";
	public static String STRUTS_RELOAD = "struts.convention.classes.reload";
	public static String STRUTS_ALLOWSTATICMETHODACCESS = "struts.ognl.allowStaticMethodAccess";
	public static String STRUTS_FILE_MAXSIZE = "struts.multipart.maxSize";
	public static String STRUTS_FORCELOWERCASE = "struts.configuration.classpath.forceLowerCase";
	public static String STRUTS_EXTENSION = "struts.action.extension";
	public static String STRUTS_BOWSERCACHE = "struts.serve.static.bowserCache";
	public static String STRUTS_XMLRELOAD = "struts.configuration.xmlreload";
	public static String STRUTS_THEME = "struts.ui.theme";
	public static Map<String, String> codemap = new HashMap<String, String>();

	public static void init() {
		codemap = new HashMap<String, String>();
		codemap.put(STRUTS_ENCODING,
				StudioPlugin.getResourceString(STRUTS_ENCODING));
		codemap.put(DYNAMICMETHODINVOCATION,
				StudioPlugin.getResourceString(DYNAMICMETHODINVOCATION));
		codemap.put(STRUTS_DEVMODE,
				StudioPlugin.getResourceString(STRUTS_DEVMODE));
		codemap.put(STRUTS_RESOURCES,
				StudioPlugin.getResourceString(STRUTS_RESOURCES));
		codemap.put(STRUTS_RELOAD,
				StudioPlugin.getResourceString(STRUTS_RELOAD));
		codemap.put(STRUTS_ALLOWSTATICMETHODACCESS, StudioPlugin
				.getResourceString(STRUTS_ALLOWSTATICMETHODACCESS));
		codemap.put(STRUTS_FILE_MAXSIZE,
				StudioPlugin.getResourceString(STRUTS_FILE_MAXSIZE));
		codemap.put(STRUTS_FORCELOWERCASE,
				StudioPlugin.getResourceString(STRUTS_FORCELOWERCASE));
		codemap.put(STRUTS_EXTENSION,
				StudioPlugin.getResourceString(STRUTS_EXTENSION));
		codemap.put(STRUTS_BOWSERCACHE,
				StudioPlugin.getResourceString(STRUTS_BOWSERCACHE));
		codemap.put(STRUTS_XMLRELOAD,
				StudioPlugin.getResourceString(STRUTS_XMLRELOAD));
		codemap.put(STRUTS_THEME,
				StudioPlugin.getResourceString(STRUTS_THEME));
	}

	public static String getText(String keycode) {
		if (codemap.isEmpty()) {
			init();
		}
		return codemap.get(keycode);
	}
}
