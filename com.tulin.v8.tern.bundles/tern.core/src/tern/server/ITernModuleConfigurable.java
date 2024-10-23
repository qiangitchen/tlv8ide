package tern.server;

import java.util.Collection;
import java.util.Set;

import tern.TernException;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * Wrapper for {@link ITernModule} used to configure {@link ITernModule} :
 * 
 * <ul>
 * <li>version of the module</li>
 * <li>options of the module if it's a plugin</li>
 * </ul>
 *
 */
public interface ITernModuleConfigurable extends ITernModule {

	/**
	 * Set version of the tern module
	 */
	ITernModule setVersion(String version) throws TernException;

	/**
	 * Returns the current wrapped module.
	 */
	ITernModule getWrappedModule();

	/**
	 * returns list of available versions.
	 */
	Set<String> getAvailableVersions();

	/**
	 * Set tern plugin options.
	 * 
	 * @param options
	 */
	void setOptions(JsonValue options);

	/**
	 * Returns the tern plugin options.
	 * 
	 * @return the tern plugin options.
	 */
	JsonValue getOptions();

	/**
	 * Returns the tern plugin options as object.
	 * 
	 * @return the tern plugin options as object.
	 */
	JsonObject getOptionsObject();

	/**
	 * Returns the module by name and null otherwise.
	 * 
	 * @param name
	 * @return the module by name and null otherwise.
	 */
	ITernModule getModule(String name);

	/**
	 * Return list of modules
	 * 
	 * @return list of modules
	 */
	Collection<ITernModule> getModules();

	/**
	 * Returns true if configurable module has version and false otherwise.
	 * 
	 * @return true if configurable module has version and false otherwise.
	 */
	boolean hasVersion();
}
