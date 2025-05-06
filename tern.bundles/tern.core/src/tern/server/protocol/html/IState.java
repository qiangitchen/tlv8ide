package tern.server.protocol.html;

/**
 * API for state used while parsing HTML content and extract JS content.
 */
interface IState {

	/**
	 * Update the state with the given character and returns the matched region
	 * and null otherwise.
	 * 
	 * @param c
	 *            current character.
	 * @return update the state with the given character and returns the matched
	 *         region and null otherwise.
	 */
	Region update(char c);

	/**
	 * Returns true if the given region type matches the type of the next region
	 * to find and false otherwise.
	 * 
	 * @param type
	 *            region type.
	 * @return true if the given region type matches the type of the next region
	 *         to find and false otherwise.
	 */
	boolean isNextRegionToFindType(RegionType type);

	/**
	 * Reset the state.
	 */
	void reset();
}