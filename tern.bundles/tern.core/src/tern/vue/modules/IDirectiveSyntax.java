package tern.vue.modules;

/**
 * Directive name syntax.
 */
public interface IDirectiveSyntax {

	/**
	 * Returns true if directive name can support th eoriginal name (ex :
	 * ngBind) and false otherwise.
	 * 
	 * @return
	 */
	boolean isUseOriginalName();

	/**
	 * Returns true if directive starts with nothing and false otherwise.
	 * 
	 * @return
	 */
	boolean isStartsWithNothing();

	/**
	 * Returns true if directive starts with 'x-' and false otherwise.
	 * 
	 * @return
	 */
	boolean isStartsWithX();

	/**
	 * Returns true if directive starts with 'data-' and false otherwise.
	 * 
	 * @return
	 */
	boolean isStartsWithData();

	/**
	 * Returns if ':' must be used as delimiter and false otherwise.
	 * 
	 * @return
	 */
	boolean isColonDelimiter();

	/**
	 * Returns if '-' must be used as delimiter and false otherwise.
	 * 
	 * @return
	 */
	boolean isMinusDelimiter();

	/**
	 * Returns if '_' must be used as delimiter and false otherwise.
	 * 
	 * @return
	 */
	boolean isUnderscoreDelimiter();

}
