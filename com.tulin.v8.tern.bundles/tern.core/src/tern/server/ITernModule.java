package tern.server;

import tern.metadata.TernModuleMetadata;

/**
 * Base API for JSON Type Definition API & Tern plugins.
 * 
 * @see http://ternjs.net/doc/manual.html#typedef
 * @see http://ternjs.net/doc/manual.html#plugins
 * 
 */
public interface ITernModule extends ITernModuleInfo {

	public static final ITernModule[] EMPTY_MODULE = new ITernModule[0];

	/**
	 * Return the origin.
	 * 
	 * @return
	 */
	String getOrigin();

	/**
	 * Returns the module type.
	 * 
	 * @return
	 */
	ModuleType getModuleType();

	/**
	 * Returns the tern metadata and null otherwise.
	 * 
	 * @return the tern metadata and null otherwise.
	 */
	TernModuleMetadata getMetadata();

}
