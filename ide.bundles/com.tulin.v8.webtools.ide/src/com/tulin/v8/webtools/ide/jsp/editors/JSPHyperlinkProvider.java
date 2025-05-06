package com.tulin.v8.webtools.ide.jsp.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;

import com.tulin.v8.fuzzyxml.FuzzyXMLDocument;
import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.webtools.ide.IHyperlinkProvider;
import com.tulin.v8.webtools.ide.html.editors.HTMLHyperlinkInfo;

public class JSPHyperlinkProvider implements IHyperlinkProvider {

	public HTMLHyperlinkInfo getHyperlinkInfo(IFile file, FuzzyXMLDocument doc, FuzzyXMLElement element,
			String attrName, String attrValue, int offset) {

		if (element.getName().equals("jsp:include") && attrName.equals("page")) {
			IPath path = file.getParent().getProjectRelativePath();
			IResource resource = file.getProject().findMember(path.append(attrValue));
			if (resource != null && resource.exists() && resource instanceof IFile) {
				HTMLHyperlinkInfo info = new HTMLHyperlinkInfo();
				info.setObject(resource);
				info.setOffset(0);
				info.setLength(attrValue.length());
				return info;
			}
		}

		return null;
	}

}
