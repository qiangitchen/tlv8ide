package tern.server;

/**
 * Tern plugin API.
 * 
 * @see http://ternjs.net/doc/manual.html#plugins
 */
public interface ITernPlugin extends ITernModule {

	public static ITernPlugin[] EMPTY_PLUGIN = new ITernPlugin[0];

	/**
	 * Returns true if the plugin is a linter and false otherwise.
	 * 
	 * @return true if the plugin is a linter and false otherwise.
	 */
	boolean isLinter();
}
