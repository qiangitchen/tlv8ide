package com.tulin.v8.webtools.ide.tag;

/**
 * An interface to contribute ICustomTagConverter to ths JSP editor.
 */
public interface ICustomTagConverterContributer {

	/**
	 * Returns a converter. If this contributor don't have converter to process a
	 * tag which specified by an argument, returns null.
	 * 
	 * @param tagName a tag name
	 * @return an instance of ICustomTagConverter or null
	 */
	public ICustomTagConverter getConverter(String tagName);

}
