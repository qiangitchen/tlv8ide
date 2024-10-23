package com.tulin.v8.webtools.ide.tag;

/**
 * An interface to contribute <code>ICustomTagConverter</code> to ths JSP editor.
 */
public interface ICustomTagValidatorContributer {
	
	/**
	 * Returns a converter.
	 * <p>
	 * If this contributor don't have converter to process a tag which specified by an argument,
	 * returns <code>null</code>.
	 * 
	 * @param tagName a tag name
	 * @return an instance of ICustomTagConverter or null
	 */
	public ICustomTagValidator getConverter(String tagName);
	
}
