package com.tulin.v8.webtools.ide.xml;

import java.io.InputStream;

/**
 * The interface of the DTD provider.
 */
public interface IDTDResolver {

	/**
	 * Returns the <code>InputStream</code> of the DTD.
	 * <p>
	 * If this resolver has no DTD correponded the given URI, this method would
	 * return <code>null</code>.
	 * 
	 * @param uri URI of DTD
	 * @return the <code>InputStream</code> of the DTD or <code>null</code>
	 */
	public InputStream getInputStream(String uri);

}
