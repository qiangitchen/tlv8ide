package com.tulin.v8.webtools.ide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;

import com.tulin.v8.webtools.ide.js.model.ModelManager;

/**
 * This is a class to access and modify project preferences. 配置项
 */
public class ProjectParams {

	private String root = "/";
	private boolean useDTD = true;
	private boolean validateXML = true;
	private boolean validateHTML = true;
	private boolean validateJSP = true;
	private boolean validateDTD = true;
	private boolean validateJS = true;
	private boolean removeMarkers = false;
	private boolean detectTaskTag = false;
	private String[] javaScripts = new String[0];
	private String[] javaScriptCompleters = new String[0];
	private String[] javaScriptLibPaths = new String[0];
	private int javaScriptIndentSize = 4;
	private char javaScriptIndentChar = ' ';
	private boolean javaScriptPreserveNewlines = true;
	private int javaScriptInitIndentLevel = 0;
	private boolean javaScriptSpaceAfterAnonFunc = false;
	private boolean javaScriptBracesOnOwnLine = true;
	private String javaScriptAccessControls;
	private String javaScriptCheckRegExp;
	private String javaScriptCheckTypes;
	private String javaScriptCheckVars;
	private String javaScriptDeprecated;
	private String javaScriptFileoverviewTags;
	private String javaScriptInvalidCasts;
	private String javaScriptMissingProperties;
	private String javaScriptNonStandardJsDocs;
	private String javaScriptStrictModuleDepCheck;
	private String javaScriptUndefinedVars;
	private String javaScriptUnknownDefines;
	private String javaScriptVisibility;
	private String javaScriptUseUndefined;

	public static final String P_ROOT = "root";
	public static final String P_USE_DTD = "useDTD";
	public static final String P_VALIDATE_XML = "validateXML";
	public static final String P_VALIDATE_HTML = "validateHTML";
	public static final String P_VALIDATE_JSP = "validateJSP";
	public static final String P_VALIDATE_DTD = "validateDTD";
	public static final String P_VALIDATE_JS = "validateJS";
	public static final String P_REMOVE_MARKERS = "removeMarkers";
	public static final String P_JAVA_SCRIPTS = "javaScripts";
	public static final String P_JAVA_SCRIPT_COMPLETERS = "javaScriptCompleters";
	public static final String P_JAVA_SCRIPT_REQUIRE_PATHS = "javaScriptLibPaths";
	public static final String P_JAVA_SCRIPT_F_INDENT_SIZE = "javaScriptIndentSize";
	public static final String P_JAVA_SCRIPT_F_INDENT_CHAR = "javaScriptIndentChar";
	public static final String P_JAVA_SCRIPT_F_PRESERVE_NEWLINES = "javaScriptPreserveNewlines";
	public static final String P_JAVA_SCRIPT_F_INIT_INDENT_LEVEL = "javaScriptInitIndentLevel";
	public static final String P_JAVA_SCRIPT_F_SPACE_AFTER_ANON_FUNC = "javaScriptSpaceAfterAnonFunc";
	public static final String P_JAVA_SCRIPT_F_BRACES_ON_OWN_LINES = "javaScriptBracesOnOwnLine";
	public static final String P_JAVA_SCRIPT_V_ACCESS_CONTROLL = "javaScriptAccessControls";
	public static final String P_JAVA_SCRIPT_V_CHECK_REG_EXP = "javaScriptCheckRegExp";
	public static final String P_JAVA_SCRIPT_V_CHECK_TYPES = "javaScriptCheckTypes";
	public static final String P_JAVA_SCRIPT_V_CHECK_VARS = "javaScriptCheckVars";
	public static final String P_JAVA_SCRIPT_V_DEPRECATED = "javaScriptDeprecated";
	public static final String P_JAVA_SCRIPT_V_FILE_OVERVIEW_TAGS = "javaScriptFileoverviewTags";
	public static final String P_JAVA_SCRIPT_V_INVALID_CASTS = "javaScriptInvalidCasts";
	public static final String P_JAVA_SCRIPT_V_MISSING_PROP = "javaScriptMissingProperties";
	public static final String P_JAVA_SCRIPT_V_NON_STANDARD_JSDOC = "javaScriptNonStandardJsDocs";
	public static final String P_JAVA_SCRIPT_V_STRICT_MODULE_DEP_CHECK = "javaScriptStrictModuleDepCheck";
	public static final String P_JAVA_SCRIPT_V_UNDEFINED_VARS = "javaScriptUndefinedVars";
	public static final String P_JAVA_SCRIPT_V_UNKNOWN_DEFINES = "javaScriptUnknownDefines";
	public static final String P_JAVA_SCRIPT_V_VISIBILITY = "javaScriptVisibility";
	public static final String P_JAVA_SCRIPT_V_USE_UNDEFINED = "javaScriptUseUndefined";

	/**
	 * Create empty WebProjectParams.
	 */
	public ProjectParams() {
	}

	/**
	 * Create WebProjectParams loading specified project configuration.
	 * 
	 * @param javaProject Java project
	 * @throws Exception
	 */
	public ProjectParams(IProject project) throws Exception {
		load(project);
	}

	/**
	 * Returns root of the web application.
	 * 
	 * @return Root of the web application
	 */
	public String getRoot() {
		return root;
	}

	/**
	 * Sets root of the web application.
	 * 
	 * @param webAppRoot Root of the web application
	 */
	public void setRoot(String webAppRoot) {
		this.root = webAppRoot;
	}

	/**
	 * @param useDTD enable DTD based validation and code completion or not
	 *               <ul>
	 *               <li>true - enable</li>
	 *               <li>false - disable</li>
	 *               </ul>
	 */
	public void setUseDTD(boolean useDTD) {
		this.useDTD = useDTD;
	}

	/**
	 * @return enable DTD based validation and code completion or not
	 *         <ul>
	 *         <li>true - enable</li>
	 *         <li>false - disable</li>
	 *         </ul>
	 */
	public boolean getUseDTD() {
		return this.useDTD;
	}

	public void setValidateHTML(boolean validateHTML) {
		this.validateHTML = validateHTML;
	}

	public boolean getValidateHTML() {
		return this.validateHTML;
	}

	public void setValidateJSP(boolean validateJSP) {
		this.validateJSP = validateJSP;
	}

	public boolean getValidateJSP() {
		return this.validateJSP;
	}

	public void setValidateDTD(boolean validateDTD) {
		this.validateDTD = validateDTD;
	}

	public boolean getValidateDTD() {
		return this.validateDTD;
	}

	public void setValidateJavaScript(boolean validateJS) {
		this.validateJS = validateJS;
	}

	public boolean getValidateJavaScript() {
		return this.validateJS;
	}

	public void setValidateXML(boolean validateXML) {
		this.validateXML = validateXML;
	}

	public boolean getValidateXML() {
		return this.validateXML;
	}

	public void setRemoveMarkers(boolean removeMarkers) {
		this.removeMarkers = removeMarkers;
	}

	public boolean getRemoveMarkers() {
		return this.removeMarkers;
	}

	public void setDetectTaskTag(boolean detectTaskTag) {
		this.detectTaskTag = detectTaskTag;
	}

	public boolean getDetectTaskTag() {
		return this.detectTaskTag;
	}

	public void setJavaScripts(String[] javaScripts) {
		this.javaScripts = javaScripts;
	}

	public String[] getJavaScripts() {
		return this.javaScripts;
	}

	public void setJavaScriptCompleters(String[] javaScriptCompleters) {
		this.javaScriptCompleters = javaScriptCompleters;
	}

	public String[] getJavaScriptCompleters() {
		return this.javaScriptCompleters;
	}

	/**
	 * Save configuration.
	 * 
	 * @param javaProject Java project
	 * @throws Exception
	 */
	public void save(IProject project) throws Exception {
		IFile configFile = project.getFile(".amateras");
		Properties props = new Properties();
		props.put(P_ROOT, root);
		props.put(P_USE_DTD, String.valueOf(useDTD));
		props.put(P_VALIDATE_XML, String.valueOf(validateXML));
		props.put(P_VALIDATE_HTML, String.valueOf(validateHTML));
		props.put(P_VALIDATE_JSP, String.valueOf(validateJSP));
		props.put(P_VALIDATE_DTD, String.valueOf(validateDTD));
		props.put(P_VALIDATE_JS, String.valueOf(validateJS));
		props.put(P_REMOVE_MARKERS, String.valueOf(removeMarkers));

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < javaScripts.length; i++) {
			if (i != 0) {
				sb.append('\n');
			}
			sb.append(javaScripts[i]);
		}
		props.put(P_JAVA_SCRIPTS, sb.toString());

		sb.setLength(0);
		for (int i = 0; i < javaScriptCompleters.length; i++) {
			if (i != 0) {
				sb.append('\n');
			}
			sb.append(javaScriptCompleters[i]);
		}
		props.put(P_JAVA_SCRIPT_COMPLETERS, sb.toString());

		sb.setLength(0);
		for (int i = 0; i < javaScriptLibPaths.length; i++) {
			if (i != 0) {
				sb.append('\n');
			}
			sb.append(javaScriptLibPaths[i]);
		}
		props.put(P_JAVA_SCRIPT_REQUIRE_PATHS, sb.toString());

		props.put(P_JAVA_SCRIPT_V_ACCESS_CONTROLL, javaScriptAccessControls);
		props.put(P_JAVA_SCRIPT_V_CHECK_REG_EXP, javaScriptCheckRegExp);
		props.put(P_JAVA_SCRIPT_V_CHECK_TYPES, javaScriptCheckTypes);
		props.put(P_JAVA_SCRIPT_V_CHECK_VARS, javaScriptCheckVars);
		props.put(P_JAVA_SCRIPT_V_DEPRECATED, javaScriptDeprecated);
		props.put(P_JAVA_SCRIPT_V_FILE_OVERVIEW_TAGS, javaScriptFileoverviewTags);
		props.put(P_JAVA_SCRIPT_V_INVALID_CASTS, javaScriptInvalidCasts);
		props.put(P_JAVA_SCRIPT_V_MISSING_PROP, javaScriptMissingProperties);
		props.put(P_JAVA_SCRIPT_V_NON_STANDARD_JSDOC, javaScriptNonStandardJsDocs);
		props.put(P_JAVA_SCRIPT_V_STRICT_MODULE_DEP_CHECK, javaScriptStrictModuleDepCheck);
		props.put(P_JAVA_SCRIPT_V_UNDEFINED_VARS, javaScriptUndefinedVars);
		props.put(P_JAVA_SCRIPT_V_UNKNOWN_DEFINES, javaScriptUnknownDefines);
		props.put(P_JAVA_SCRIPT_V_VISIBILITY, javaScriptVisibility);
		props.put(P_JAVA_SCRIPT_V_USE_UNDEFINED, javaScriptUseUndefined);

		props.put(P_JAVA_SCRIPT_F_INDENT_SIZE, Integer.toString(javaScriptIndentSize));
		props.put(P_JAVA_SCRIPT_F_INDENT_CHAR, Character.toString(javaScriptIndentChar));
		props.put(P_JAVA_SCRIPT_F_PRESERVE_NEWLINES, Boolean.toString(javaScriptPreserveNewlines));
		props.put(P_JAVA_SCRIPT_F_INIT_INDENT_LEVEL, Integer.toString(javaScriptInitIndentLevel));
		props.put(P_JAVA_SCRIPT_F_SPACE_AFTER_ANON_FUNC, Boolean.toString(javaScriptSpaceAfterAnonFunc));
		props.put(P_JAVA_SCRIPT_F_BRACES_ON_OWN_LINES, Boolean.toString(javaScriptBracesOnOwnLine));

		File file = configFile.getLocation().makeAbsolute().toFile();
		if (!file.exists()) {
			file.createNewFile();
		}
		props.store(new FileOutputStream(file), "EclipseHTMLEditor configuration file");

		if (detectTaskTag) {
			addNature(project);
		} else {
			removeNature(project);
		}

		ModelManager.getInstance().clearCache(project);
		project.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
	}

	private void addNature(IProject project) throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] natures = description.getNatureIds();
		for (int i = 0; i < natures.length; i++) {
			if (natures[i].equals(ProjectNature.HTML_NATURE_ID)) {
				return;
			}
		}
		String[] newNatures = new String[natures.length + 1];
		System.arraycopy(natures, 0, newNatures, 0, natures.length);
		newNatures[natures.length] = ProjectNature.HTML_NATURE_ID;
		description.setNatureIds(newNatures);
		project.setDescription(description, null);
	}

	private void removeNature(IProject project) throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] natures = description.getNatureIds();
		List<String> newNatures = new ArrayList<String>();
		for (int i = 0; i < natures.length; i++) {
			if (!natures[i].equals(ProjectNature.HTML_NATURE_ID)) {
				newNatures.add(natures[i]);
			}
		}
		description.setNatureIds(newNatures.toArray(new String[newNatures.size()]));
		project.setDescription(description, null);
	}

	/**
	 * Load configuration.
	 * 
	 * @param javaProject Java project
	 * @throws Exception
	 */
	public void load(IProject project) throws Exception {
		IFile configFile = project.getFile(".amateras");

		String useDTD = null;
		String validateXML = null;
		String validateHTML = null;
		String validateJSP = null;
		String validateDTD = null;
		String validateJS = null;
		String removeMarkers = null;
		String javaScripts = "";
		String javaScriptCompleters = "Prototype JavaScript framework\nECMA Script\nFireFox\njQuery\nTLv8"; // JS代码提示默认配置项
		String javaScriptLibPaths = "";
		String indentSize = "4";
		String indentChar = " ";
		String preserveNewlines = "true";
		String initIndentLevel = "0";
		String spaceAfterAnonFunc = "false";
		String bracesOnOwnLine = "true";
		String javaScriptAccessControls = "Off";
		String javaScriptCheckRegExp = "Off";
		String javaScriptCheckTypes = "Off";
		String javaScriptCheckVars = "Off";
		String javaScriptDeprecated = "Off";
		String javaScriptFileoverviewTags = "Off";
		String javaScriptInvalidCasts = "Off";
		String javaScriptMissingProperties = "Off";
		String javaScriptNonStandardJsDocs = "Off";
		String javaScriptStrictModuleDepCheck = "Off";
		String javaScriptUndefinedVars = "Off";
		String javaScriptUnknownDefines = "Off";
		String javaScriptVisibility = "Off";
		String javaScriptUseUndefined = "Off";

		if (configFile.exists()) {
			File file = configFile.getLocation().makeAbsolute().toFile();
			Properties props = new Properties();
			props.load(new FileInputStream(file));

			root = props.getProperty(P_ROOT);
			useDTD = props.getProperty(P_USE_DTD);
			validateXML = props.getProperty(P_VALIDATE_XML);
			validateHTML = props.getProperty(P_VALIDATE_HTML);
			validateJSP = props.getProperty(P_VALIDATE_JSP);
			validateDTD = props.getProperty(P_VALIDATE_DTD);
			validateJS = props.getProperty(P_VALIDATE_JS);
			removeMarkers = props.getProperty(P_REMOVE_MARKERS);

			javaScripts = props.getProperty(P_JAVA_SCRIPTS);
			if (javaScripts == null) {
				javaScripts = "";
			}
			javaScriptCompleters = props.getProperty(P_JAVA_SCRIPT_COMPLETERS);
			if (javaScriptCompleters == null) {
				javaScriptCompleters = "ECMA Script\nFireFox";
			}
			javaScriptLibPaths = props.getProperty(P_JAVA_SCRIPT_REQUIRE_PATHS);
			if (javaScriptLibPaths == null) {
				javaScriptLibPaths = "";
			}
			javaScriptAccessControls = props.getProperty(P_JAVA_SCRIPT_V_ACCESS_CONTROLL);
			if (javaScriptAccessControls == null) {
				javaScriptAccessControls = "Off";
			}
			javaScriptCheckRegExp = props.getProperty(P_JAVA_SCRIPT_V_CHECK_REG_EXP);
			if (javaScriptCheckRegExp == null) {
				javaScriptCheckRegExp = "Off";
			}
			javaScriptCheckTypes = props.getProperty(P_JAVA_SCRIPT_V_CHECK_TYPES);
			if (javaScriptCheckTypes == null) {
				javaScriptCheckTypes = "Off";
			}
			javaScriptCheckVars = props.getProperty(P_JAVA_SCRIPT_V_CHECK_VARS);
			if (javaScriptCheckVars == null) {
				javaScriptCheckVars = "Off";
			}
			javaScriptDeprecated = props.getProperty(P_JAVA_SCRIPT_V_DEPRECATED);
			if (javaScriptDeprecated == null) {
				javaScriptDeprecated = "Off";
			}
			javaScriptFileoverviewTags = props.getProperty(P_JAVA_SCRIPT_V_FILE_OVERVIEW_TAGS);
			if (javaScriptFileoverviewTags == null) {
				javaScriptFileoverviewTags = "Off";
			}
			javaScriptInvalidCasts = props.getProperty(P_JAVA_SCRIPT_V_INVALID_CASTS);
			if (javaScriptInvalidCasts == null) {
				javaScriptInvalidCasts = "Off";
			}
			javaScriptMissingProperties = props.getProperty(P_JAVA_SCRIPT_V_MISSING_PROP);
			if (javaScriptMissingProperties == null) {
				javaScriptMissingProperties = "Off";
			}
			javaScriptNonStandardJsDocs = props.getProperty(P_JAVA_SCRIPT_V_NON_STANDARD_JSDOC);
			if (javaScriptNonStandardJsDocs == null) {
				javaScriptNonStandardJsDocs = "Off";
			}
			javaScriptStrictModuleDepCheck = props.getProperty(P_JAVA_SCRIPT_V_STRICT_MODULE_DEP_CHECK);
			if (javaScriptStrictModuleDepCheck == null) {
				javaScriptStrictModuleDepCheck = "Off";
			}
			javaScriptUndefinedVars = props.getProperty(P_JAVA_SCRIPT_V_UNDEFINED_VARS);
			if (javaScriptUndefinedVars == null) {
				javaScriptUndefinedVars = "Off";
			}
			javaScriptUnknownDefines = props.getProperty(P_JAVA_SCRIPT_V_UNKNOWN_DEFINES);
			if (javaScriptUnknownDefines == null) {
				javaScriptUnknownDefines = "Off";
			}
			javaScriptVisibility = props.getProperty(P_JAVA_SCRIPT_V_VISIBILITY);
			if (javaScriptVisibility == null) {
				javaScriptVisibility = "Off";
			}
			javaScriptUseUndefined = props.getProperty(P_JAVA_SCRIPT_V_USE_UNDEFINED);
			if (javaScriptUseUndefined == null) {
				javaScriptUseUndefined = "Off";
			}
			indentSize = props.getProperty(P_JAVA_SCRIPT_F_INDENT_SIZE);
			if (indentSize == null) {
				indentSize = "4";
			}
			indentChar = props.getProperty(P_JAVA_SCRIPT_F_INDENT_CHAR);
			if (indentChar == null) {
				indentChar = " ";
			}
			preserveNewlines = props.getProperty(P_JAVA_SCRIPT_F_PRESERVE_NEWLINES);
			if (preserveNewlines == null) {
				preserveNewlines = "true";
			}
			initIndentLevel = props.getProperty(P_JAVA_SCRIPT_F_INIT_INDENT_LEVEL);
			if (initIndentLevel == null) {
				initIndentLevel = "0";
			}
			spaceAfterAnonFunc = props.getProperty(P_JAVA_SCRIPT_F_SPACE_AFTER_ANON_FUNC);
			if (spaceAfterAnonFunc == null) {
				spaceAfterAnonFunc = "false";
			}
			bracesOnOwnLine = props.getProperty(P_JAVA_SCRIPT_F_BRACES_ON_OWN_LINES);
			if (bracesOnOwnLine == null) {
				bracesOnOwnLine = "true";
			}
		} else {
			// for old versions
			this.root = project.getPersistentProperty(new QualifiedName(WebToolsPlugin.getPluginId(), P_ROOT));
			useDTD = project.getPersistentProperty(new QualifiedName(WebToolsPlugin.getPluginId(), P_USE_DTD));
			validateHTML = project
					.getPersistentProperty(new QualifiedName(WebToolsPlugin.getPluginId(), P_VALIDATE_HTML));
		}

		if (this.root == null) {
			this.root = "/";
		}

		this.useDTD = getBooleanValue(useDTD, true);
		this.validateXML = getBooleanValue(validateXML, true);
		this.validateHTML = getBooleanValue(validateHTML, true);
		this.validateJSP = getBooleanValue(validateJSP, true);
		this.validateDTD = getBooleanValue(validateDTD, true);
		this.validateJS = getBooleanValue(validateJS, true);
		this.removeMarkers = getBooleanValue(removeMarkers, false);
		this.detectTaskTag = project.hasNature(ProjectNature.HTML_NATURE_ID);

		String[] dim = javaScripts.split("\n");
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < dim.length; i++) {
			if (dim[i].trim().length() != 0) {
				list.add(dim[i]);
			}
		}
		this.javaScripts = list.toArray(new String[list.size()]);

		dim = javaScriptCompleters.split("\n");
		list = new ArrayList<String>();
		for (int i = 0; i < dim.length; i++) {
			if (dim[i].trim().length() != 0) {
				list.add(dim[i]);
			}
		}
		this.javaScriptCompleters = list.toArray(new String[list.size()]);

		dim = javaScriptLibPaths.split("\n");
		list = new ArrayList<String>();
		for (int i = 0; i < dim.length; i++) {
			if (dim[i].trim().length() != 0) {
				list.add(dim[i]);
			}
		}
		this.javaScriptLibPaths = list.toArray(new String[list.size()]);

		// validator
		this.javaScriptAccessControls = javaScriptAccessControls;
		this.javaScriptCheckRegExp = javaScriptCheckRegExp;
		this.javaScriptCheckTypes = javaScriptCheckTypes;
		this.javaScriptCheckVars = javaScriptCheckVars;
		this.javaScriptDeprecated = javaScriptDeprecated;
		this.javaScriptFileoverviewTags = javaScriptFileoverviewTags;
		this.javaScriptInvalidCasts = javaScriptInvalidCasts;
		this.javaScriptMissingProperties = javaScriptMissingProperties;
		this.javaScriptNonStandardJsDocs = javaScriptNonStandardJsDocs;
		this.javaScriptStrictModuleDepCheck = javaScriptStrictModuleDepCheck;
		this.javaScriptUndefinedVars = javaScriptUndefinedVars;
		this.javaScriptUnknownDefines = javaScriptUnknownDefines;
		this.javaScriptVisibility = javaScriptVisibility;
		this.javaScriptUseUndefined = javaScriptUseUndefined;

		// formatter
		try {
			this.javaScriptIndentSize = Integer.parseInt(indentSize);
		} catch (Exception e) {
		}
		this.javaScriptIndentChar = indentChar.length() > 0 ? indentChar.charAt(0) : ' ';
		try {
			this.javaScriptPreserveNewlines = Boolean.parseBoolean(preserveNewlines);
		} catch (Exception e) {
		}
		try {
			this.javaScriptInitIndentLevel = Integer.parseInt(initIndentLevel);
		} catch (Exception e) {
		}
		try {
			this.javaScriptSpaceAfterAnonFunc = Boolean.parseBoolean(spaceAfterAnonFunc);
		} catch (Exception e) {
		}
		try {
			this.javaScriptBracesOnOwnLine = Boolean.parseBoolean(bracesOnOwnLine);
		} catch (Exception e) {
		}
	}

	private boolean getBooleanValue(String value, boolean defaultValue) {
		if (value != null) {
			if (value.equals("true")) {
				return true;
			} else if (value.equals("false")) {
				return false;
			}
		}
		return defaultValue;
	}

	public String[] getJavaScriptLibPaths() {
		return javaScriptLibPaths;
	}

	public void setJavaScriptLibPaths(String[] libPaths) {
		this.javaScriptLibPaths = libPaths;
	}

	public int getJavaScriptIndentSize() {
		return javaScriptIndentSize;
	}

	public void setJavaScriptIndentSize(int javaScriptIndentSize) {
		this.javaScriptIndentSize = javaScriptIndentSize;
	}

	public char getJavaScriptIndentChar() {
		return javaScriptIndentChar;
	}

	public void setJavaScriptIndentChar(char javaScriptIndentChar) {
		this.javaScriptIndentChar = javaScriptIndentChar;
	}

	public boolean isJavaScriptPreserveNewlines() {
		return javaScriptPreserveNewlines;
	}

	public void setJavaScriptPreserveNewlines(boolean javaScriptPreserveNewlines) {
		this.javaScriptPreserveNewlines = javaScriptPreserveNewlines;
	}

	public int getJavaScriptInitIndentLevel() {
		return javaScriptInitIndentLevel;
	}

	public void setJavaScriptInitIndentLevel(int javaScriptInitIndentLevel) {
		this.javaScriptInitIndentLevel = javaScriptInitIndentLevel;
	}

	public boolean isJavaScriptSpaceAfterAnonFunc() {
		return javaScriptSpaceAfterAnonFunc;
	}

	public void setJavaScriptSpaceAfterAnonFunc(boolean javaScriptSpaceAfterAnonFunc) {
		this.javaScriptSpaceAfterAnonFunc = javaScriptSpaceAfterAnonFunc;
	}

	public boolean isJavaScriptBracesOnOwnLine() {
		return javaScriptBracesOnOwnLine;
	}

	public void setJavaScriptBracesOnOwnLine(boolean javaScriptBracesOnOwnLine) {
		this.javaScriptBracesOnOwnLine = javaScriptBracesOnOwnLine;
	}

	public String getJavaScriptAccessControls() {
		return javaScriptAccessControls;
	}

	public void setJavaScriptAccessControls(String javaScriptAccessControls) {
		this.javaScriptAccessControls = javaScriptAccessControls;
	}

	public String getJavaScriptCheckRegExp() {
		return javaScriptCheckRegExp;
	}

	public void setJavaScriptCheckRegExp(String javaScriptCheckRegExp) {
		this.javaScriptCheckRegExp = javaScriptCheckRegExp;
	}

	public String getJavaScriptCheckTypes() {
		return javaScriptCheckTypes;
	}

	public void setJavaScriptCheckTypes(String javaScriptCheckTypes) {
		this.javaScriptCheckTypes = javaScriptCheckTypes;
	}

	public String getJavaScriptCheckVars() {
		return javaScriptCheckVars;
	}

	public void setJavaScriptCheckVars(String javaScriptCheckVars) {
		this.javaScriptCheckVars = javaScriptCheckVars;
	}

	public String getJavaScriptDeprecated() {
		return javaScriptDeprecated;
	}

	public void setJavaScriptDeprecated(String javaScriptDeprecated) {
		this.javaScriptDeprecated = javaScriptDeprecated;
	}

	public String getJavaScriptFileoverviewTags() {
		return javaScriptFileoverviewTags;
	}

	public void setJavaScriptFileoverviewTags(String javaScriptFileoverviewTags) {
		this.javaScriptFileoverviewTags = javaScriptFileoverviewTags;
	}

	public String getJavaScriptInvalidCasts() {
		return javaScriptInvalidCasts;
	}

	public void setJavaScriptInvalidCasts(String javaScriptInvalidCasts) {
		this.javaScriptInvalidCasts = javaScriptInvalidCasts;
	}

	public String getJavaScriptMissingProperties() {
		return javaScriptMissingProperties;
	}

	public void setJavaScriptMissingProperties(String javaScriptMissingProperties) {
		this.javaScriptMissingProperties = javaScriptMissingProperties;
	}

	public String getJavaScriptNonStandardJsDocs() {
		return javaScriptNonStandardJsDocs;
	}

	public void setJavaScriptNonStandardJsDocs(String javaScriptNonStandardJsDocs) {
		this.javaScriptNonStandardJsDocs = javaScriptNonStandardJsDocs;
	}

	public String getJavaScriptStrictModuleDepCheck() {
		return javaScriptStrictModuleDepCheck;
	}

	public void setJavaScriptStrictModuleDepCheck(String javaScriptStrictModuleDepCheck) {
		this.javaScriptStrictModuleDepCheck = javaScriptStrictModuleDepCheck;
	}

	public String getJavaScriptUndefinedVars() {
		return javaScriptUndefinedVars;
	}

	public void setJavaScriptUndefinedVars(String javaScriptUndefinedVars) {
		this.javaScriptUndefinedVars = javaScriptUndefinedVars;
	}

	public String getJavaScriptUnknownDefines() {
		return javaScriptUnknownDefines;
	}

	public void setJavaScriptUnknownDefines(String javaScriptUnknownDefines) {
		this.javaScriptUnknownDefines = javaScriptUnknownDefines;
	}

	public String getJavaScriptVisibility() {
		return javaScriptVisibility;
	}

	public void setJavaScriptVisibility(String javaScriptVisibility) {
		this.javaScriptVisibility = javaScriptVisibility;
	}

	public String getJavaScriptUseUndefined() {
		return javaScriptUseUndefined;
	}

	public void setJavaScriptUseUndefined(String javaScriptUseUndefined) {
		this.javaScriptUseUndefined = javaScriptUseUndefined;
	}
}
