package tern.vue.modules;

/**
 * Vue restriction.
 * 
 */
public enum Restriction {

	A, E, C, M;

	/**
	 * Returns true if the given restrict match a restriction and false
	 * otherwise.
	 * 
	 * @param restrict
	 * @return
	 */
	public boolean isMatch(String restrict) {
		return restrict.contains(name());
	}
}
