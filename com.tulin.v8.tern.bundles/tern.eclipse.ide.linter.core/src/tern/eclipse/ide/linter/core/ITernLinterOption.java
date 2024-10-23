package tern.eclipse.ide.linter.core;

import java.util.Collection;

/**
 * Tern linter option API.
 *
 */
public interface ITernLinterOption {

	/**
	 * Returns the identifier of the linter option.
	 * 
	 * @return the identifier of the linter option.
	 */
	String getId();

	/**
	 * Returns the URL of the linter option.
	 * 
	 * @return the URL of the linter option.
	 */
	String getUrl();

	/**
	 * Returns the documentation of the linter option.
	 * 
	 * @return the documentation of the linter option.
	 */
	String getDoc();

	void addOption(ITernLinterOption option);

	/**
	 * Returns the list of options of the linter option.
	 * 
	 * @return the list of options of the linter option.
	 */
	Collection<ITernLinterOption> getOptions();

	/**
	 * Returns true if value of the options is a boolean type and false
	 * otherwise.
	 * 
	 * @return true if value of the options is a boolean type and false
	 *         otherwise.
	 */
	boolean isBooleanType();

	/**
	 * Returns true if value of the options is a number type and false
	 * otherwise.
	 * 
	 * @return true if value of the options is a number type and false
	 *         otherwise.
	 */
	boolean isNumberType();

	/**
	 * Returns true if value of the options is a string type and false
	 * otherwise.
	 * 
	 * @return true if value of the options is a string type and false
	 *         otherwise.
	 */
	boolean isStringType();

	/**
	 * Returns true if value of the options is a category type and false
	 * otherwise.
	 * 
	 * @return true if value of the options is a category type and false
	 *         otherwise.
	 */
	boolean isCategoryType();

	/**
	 * Returns the value of the option
	 * 
	 * @return
	 */
	Object getValue();

	/**
	 * Set the value option
	 * 
	 * @param value
	 */
	void setValue(Object value);

	/**
	 * Returns true if the option has value and false otherwise.
	 * 
	 * @return true if the option has value and false otherwise.
	 */
	boolean hasValue();

	/**
	 * Returns the boolean value.
	 * 
	 * @return the boolean value.
	 */
	boolean getBooleanValue();

	/**
	 * Returns the int value.
	 * 
	 * @return the int value.
	 */
	Long getNumberValue();

	/**
	 * Returns the String value.
	 * 
	 * @return the String value.
	 */
	String getStringValue();
}
