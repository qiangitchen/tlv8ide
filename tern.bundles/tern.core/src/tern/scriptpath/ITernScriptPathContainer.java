package tern.scriptpath;

/**
 * Tern script path container which defines inclusion, exclusion patterns.
 *
 */
public interface ITernScriptPathContainer extends ITernScriptPath {

	/**
	 * Returns the list of the inclusion patterns.
	 * 
	 * @return the list of the inclusion patterns.
	 */
	String[] getInclusionPatterns();

	/**
	 * Returns the list of the exclusion patterns.
	 * 
	 * @return the list of the exclusion patterns.
	 */
	String[] getExclusionPatterns();
}
