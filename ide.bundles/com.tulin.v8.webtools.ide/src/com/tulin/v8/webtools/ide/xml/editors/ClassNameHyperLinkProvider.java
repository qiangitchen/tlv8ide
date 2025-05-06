package com.tulin.v8.webtools.ide.xml.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;

import com.tulin.v8.fuzzyxml.FuzzyXMLDocument;
import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.webtools.ide.IHyperlinkProvider;
import com.tulin.v8.webtools.ide.html.editors.HTMLHyperlinkInfo;

/**
 * This provides hyperlink for the Java classname.
 * <p>
 * This provider can work for files which are in the java project.
 * 
 * @since 2.0.3
 */
public class ClassNameHyperLinkProvider implements IHyperlinkProvider {

	private XMLEditor editor;

	/**
	 * @param editor the target <code>XMLEditor</code>
	 */
	public void setEditor(XMLEditor editor) {
		this.editor = editor;
	}

	public HTMLHyperlinkInfo getHyperlinkInfo(IFile file, FuzzyXMLDocument doc, FuzzyXMLElement element,
			String attrName, String attrValue, int offset) {

		if (file == null || editor == null) {
			return null;
		}

		IProject project = file.getProject();
		IJavaProject javaProject = JavaCore.create(project);
		if (javaProject == null) {
			return null;
		}

		String[] attrNames = this.editor.getClassNameAttributes();

		for (int i = 0; i < attrNames.length; i++) {
			if (attrName.equals(attrNames[i])) {
				IType type = findType(javaProject, attrValue);
				if (type != null) {
					HTMLHyperlinkInfo info = new HTMLHyperlinkInfo();
					info.setObject(type);
					info.setOffset(0);
					info.setLength(attrValue.length());
					return info;
				}
			}
		}

		return null;
	}

	private IType findType(IJavaProject project, String value) {
		try {
			IType type = project.findType(value);
			return type;
		} catch (Exception ex) {
			return null;
		}
	}

}
