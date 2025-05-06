package tern.eclipse.ide.server.nodejs.core;

import java.io.File;

/**
 * Nodejs installation API.
 * 
 */
public interface INodejsInstall {

	public static final String NODE_NATIVE = "node-native";
	
	/**
	 * Returns the id of this Nodejs install. Each known Nodejs install has a
	 * distinct id. Ids are intended to be used internally as keys; they are not
	 * intended to be shown to end users.
	 * 
	 * @return the Nodejs install id
	 */
	String getId();

	/**
	 * Returns the displayable name for this Nodejs install.
	 * <p>
	 * Note that this name is appropriate for the current locale.
	 * </p>
	 * 
	 * @return a displayable name for this Nodejs install
	 */
	String getName();

	/**
	 * Returns the file path for this Nodejs install.
	 * 
	 * @return a the file path for this Nodejs install
	 */
	File getPath();

	/**
	 * Returns true if the node install is native and false otherwise (embed).
	 * 
	 * @return
	 */
	boolean isNative();

}
