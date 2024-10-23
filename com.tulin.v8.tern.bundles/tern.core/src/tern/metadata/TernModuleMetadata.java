package tern.metadata;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import tern.server.ITernModule;
import tern.server.TernModuleInfo;
import tern.server.protocol.JsonHelper;
import tern.utils.StringUtils;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * Tern module metadata loded from *.metadata.json file.
 *
 */
public class TernModuleMetadata {

	public static String ANY_VERSION = "?";

	private static final String NAME_FIELD = "name";
	private static final String LABEL_FIELD = "label";
	private static final String ORIGIN_FIELD = "origin";
	private static final String DESCRIPTION_FIELD = "description";
	private static final String HOMEPAGE_FIELD = "homepage";
	private static final String AUTHOR_FIELD = "author";
	private static final String REPOSITORY_FIELD = "repository";
	private static final String BUGS_FIELD = "bugs";
	private static final String HELP_FIELD = "help";
	private static final String LINTER_FIELD = "linter";
	private static final String DEF_FIELD = "def";
	private static final String URL_FIELD = "url";
	private static final String DEPENDENCIES_FIELD = "dependencies";
	private static final String OPTIONS_FIELD = "options";
	private static final String ICON_FIELD = "icon";

	private final String name;
	private final String origin;
	private final String label;
	private final String description;
	private final String homepage;
	private final String author;
	private final String repositoryURL;
	private final String bugsURL;
	private final String helpURL;
	private final boolean linter;
	private final boolean def;
	private final Map<String, Collection<String>> dependencies;
	private final Map<String, Collection<String>> requiredDependencies;
	private final Collection<TernModuleMetadataOption> options;
	private File fileIcon;

	/**
	 * Create module metadata from JSON object.
	 * 
	 * @param json
	 * @param file
	 */
	public TernModuleMetadata(JsonObject json, File file) {
		this.name = JsonHelper.getString(json, NAME_FIELD);
		this.label = JsonHelper.getString(json, LABEL_FIELD);
		this.origin = !StringUtils.isEmpty(JsonHelper.getString(json, ORIGIN_FIELD))
				? JsonHelper.getString(json, ORIGIN_FIELD) : null;
		this.description = JsonHelper.getString(json, DESCRIPTION_FIELD);
		this.homepage = JsonHelper.getString(json, HOMEPAGE_FIELD);
		this.author = JsonHelper.getString(json, AUTHOR_FIELD);
		this.repositoryURL = getURL(json, REPOSITORY_FIELD);
		this.bugsURL = getURL(json, BUGS_FIELD);
		this.helpURL = getURL(json, HELP_FIELD);
		this.linter = json.getBoolean(LINTER_FIELD, false);
		this.def = json.getBoolean(DEF_FIELD, false);
		// dependencies
		JsonValue dependencies = json.get(DEPENDENCIES_FIELD);
		if (dependencies != null) {
			this.dependencies = parseDependencies((JsonValue) dependencies);
		} else {
			this.dependencies = Collections.emptyMap();
		}
		// required dependencies
		requiredDependencies = getRequiredDependencies();
		// options
		JsonValue options = json.get(OPTIONS_FIELD);
		if (options != null && options instanceof JsonArray) {
			this.options = parseOptions((JsonArray) options);
		} else {
			this.options = null;
		}
		// icon
		this.fileIcon = getFileIcon(json, file);
	}

	private File getFileIcon(JsonObject json, File file) {
		if (file == null) {
			return null;
		}
		String icon = JsonHelper.getString(json, ICON_FIELD);
		if (StringUtils.isEmpty(icon)) {
			return null;
		}
		File fileIcon = new File(file.getParentFile(), icon);
		return fileIcon.exists() ? fileIcon : null;
	}

	private Map<String, Collection<String>> getRequiredDependencies() {
		Map<String, Collection<String>> requiredDependenciesMap = new HashMap<String, Collection<String>>();
		Collection<String> requiredDependencies = null;
		String version = null;
		Collection<String> dependencies = null;
		TernModuleInfo info = null;
		for (Map.Entry<String, Collection<String>> entry : this.dependencies.entrySet()) {
			version = entry.getKey();
			dependencies = entry.getValue();
			for (String dependency : dependencies) {
				info = new TernModuleInfo(dependency);
				if (info.getType().equals(getName())) {
					// same type, add it
					requiredDependencies = requiredDependenciesMap.get(version);
					if (requiredDependencies == null) {
						requiredDependencies = new ArrayList<String>();
						requiredDependenciesMap.put(version, requiredDependencies);
					}
					requiredDependencies.add(dependency);
				}
			}
		}
		return requiredDependenciesMap;
	}

	public String getURL(JsonObject json, String name) {
		JsonValue value = json.get(name);
		if (value != null) {
			return JsonHelper.getString((JsonObject) value, URL_FIELD);
		}
		return null;
	}

	private Map<String, Collection<String>> parseDependencies(JsonValue jsonDependencies) {
		if (jsonDependencies instanceof JsonArray) {
			return parseDependencies((JsonArray) jsonDependencies);
		} else if (jsonDependencies instanceof JsonObject) {
			return parseDependencies((JsonObject) jsonDependencies);
		}
		return Collections.emptyMap();
	}

	private Map<String, Collection<String>> parseDependencies(JsonArray jsonDependencies) {
		List<String> dependencies = new ArrayList<String>();
		for (JsonValue jsonDependency : jsonDependencies) {
			dependencies.add(JsonHelper.getString(jsonDependency));
		}
		Map<String, Collection<String>> dependenciesMap = new HashMap<String, Collection<String>>();
		parseDependencies(jsonDependencies, ANY_VERSION, dependenciesMap);
		return dependenciesMap;
	}

	private void parseDependencies(JsonArray jsonDependencies, String version,
			Map<String, Collection<String>> dependenciesMap) {
		List<String> dependencies = new ArrayList<String>();
		for (JsonValue jsonDependency : jsonDependencies) {
			dependencies.add(JsonHelper.getString(jsonDependency));
		}
		dependenciesMap.put(version, dependencies);
	}

	private Map<String, Collection<String>> parseDependencies(JsonObject jsonDependencies) {
		Map<String, Collection<String>> dependenciesMap = new HashMap<String, Collection<String>>();
		Iterator<com.eclipsesource.json.JsonObject.Member> a = jsonDependencies.iterator();
		while (a.hasNext()) {
			JsonObject.Member member = (JsonObject.Member) a.next();
			String version = member.getName();
			if (member.getValue() instanceof JsonArray) {
				parseDependencies((JsonArray) member.getValue(), version, dependenciesMap);
			}
			if (!StringUtils.isEmpty(version) && !ANY_VERSION.equals(version)) {
				Collection<String> commons = dependenciesMap.get(ANY_VERSION);
				if (commons != null) {
					dependenciesMap.get(version).addAll(commons);
				}
			}
		}
		return dependenciesMap;
	}

	private Collection<TernModuleMetadataOption> parseOptions(JsonArray jsonOptions) {
		List<TernModuleMetadataOption> options = new ArrayList<TernModuleMetadataOption>();
		for (JsonValue jsonOption : jsonOptions) {
			options.add(new TernModuleMetadataOption((JsonObject) jsonOption));
		}
		return options;
	}

	/**
	 * Returns the label of the module.
	 * 
	 * @return the label of the module.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Returns the name of the module.
	 * 
	 * @return the name of the module.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the origin of the module.
	 * 
	 * @return the origin of the module.
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * Returns the description of the module.
	 * 
	 * @return the description of the module.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the home page of the module.
	 * 
	 * @return the home page of the module.
	 */
	public String getHomePage() {
		return homepage;
	}

	/**
	 * Returns the author of the module.
	 * 
	 * @return the author of the module.
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Returns the repository URL of the module.
	 * 
	 * @return the repository URL of the module.
	 */
	public String getRepositoryURL() {
		return repositoryURL;
	}

	/**
	 * Returns the bugs URL of the module.
	 * 
	 * @return the bugs URL of the module.
	 */
	public String getBugsURL() {
		return bugsURL;
	}

	/**
	 * Returns the help URL of the module.
	 * 
	 * @return the help URL of the module.
	 */
	public String getHelpURL() {
		return helpURL;
	}

	/**
	 * Returns list of options.
	 * 
	 * @return list of options.
	 */
	public Collection<TernModuleMetadataOption> getOptions() {
		return options;
	}

	/**
	 * Returns list of {@link ITernModule} name dependencies.
	 * 
	 * @return list of {@link ITernModule} name dependencies.
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> getDependencies(String version) {
		Collection<String> deps = dependencies.get(StringUtils.isEmpty(version) ? ANY_VERSION : version);
		if (deps == null) {
			deps = dependencies.get(ANY_VERSION);
		}
		return (Collection<String>) (deps != null ? deps : Collections.emptyList());
	}

	/**
	 * Returns list of required {@link ITernModule} name dependencies.
	 * 
	 * @return list of required {@link ITernModule} name dependencies.
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> getRequiredDependencies(String version) {
		Collection<String> deps = requiredDependencies.get(StringUtils.isEmpty(version) ? ANY_VERSION : version);
		if (deps == null) {
			deps = requiredDependencies.get(ANY_VERSION);
		}
		return (Collection<String>) (deps != null ? deps : Collections.emptyList());
	}

	/**
	 * Returns true if the plugin is a linter and false otherwise.
	 * 
	 * @return true if the plugin is a linter and false otherwise.
	 */
	public boolean isLinter() {
		return linter;
	}

	/**
	 * Returns true if the the module is a JSON Type Definition and false
	 * otherwise( it is a plugin)
	 * 
	 * @return true if the the module is a JSON Type Definition and false
	 *         otherwise( it is a plugin)
	 */
	public boolean isDef() {
		return def;
	}

	/**
	 * Returns true if the module has options and false otherwise.
	 * 
	 * @return true if the module has options and false otherwise.
	 */
	public boolean hasOptions() {
		return options != null && options.size() > 0;
	}

	/**
	 * Returns the file icon and null otherwise.
	 * 
	 * @return the file icon and null otherwise.
	 */
	public File getFileIcon() {
		return fileIcon;
	}
}
