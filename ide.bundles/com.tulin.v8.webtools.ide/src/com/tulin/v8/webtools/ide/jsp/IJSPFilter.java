package com.tulin.v8.webtools.ide.jsp;

import org.eclipse.core.resources.IFile;

/**
 * The interface for the JSP filter.
 * <p>
 * This filter is called by the <code>JSPValidator</code> before validation
 * processing.
 */
public interface IJSPFilter {

	/**
	 * Return the possibly modified contents of a JSP.
	 * 
	 * @param raw contents
	 * @param the target JSP file
	 * @return filtered contents
	 */
	public String filterJSP(String contents, IFile file);

}
