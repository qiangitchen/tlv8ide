package com.tulin.v8.webtools.ide;

import org.eclipse.core.resources.IFile;

import com.tulin.v8.fuzzyxml.FuzzyXMLDocument;
import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.webtools.ide.html.editors.HTMLHyperlinkInfo;

/**
 * This provides additional hyperlinks to the <code>HTMLSourceEditor</code>.
 */
public interface IHyperlinkProvider {
	
	/**
	 * This method returns a target object of hyperlink.
	 * If this provider doesn't support specified arguments, returns null.
	 * 
	 * @param file      the <code>IFile</code> instance
	 * @param doc       a document object of FuzzyXML
	 * @param element   an element that are calet position
	 * @param attrName  an attribute name that are calet position
	 * @param attrValue an attribute value that are calet position
	 * @return 
	 *   <ul>
	 *     <li><code>IFile</code></li>
	 *     <li><code>IJavaElement</code></li>
	 *     <li><code>null</code></li>
	 *   </ul>
	 */
	public HTMLHyperlinkInfo getHyperlinkInfo(IFile file,FuzzyXMLDocument doc,
			FuzzyXMLElement element,String attrName,String attrValue,int offset);
	
}
