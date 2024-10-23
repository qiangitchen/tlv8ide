package com.tulin.v8.webtools.ide.html;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import com.tulin.v8.fuzzyxml.FuzzyXMLAttribute;
import com.tulin.v8.fuzzyxml.FuzzyXMLDocument;
import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.fuzzyxml.FuzzyXMLNode;
import com.tulin.v8.fuzzyxml.FuzzyXMLParser;
import com.tulin.v8.webtools.ide.IHyperlinkProvider;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.html.editors.HTMLEditor;
import com.tulin.v8.webtools.ide.html.editors.HTMLHyperlinkInfo;
import com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditor;

/**
 * The <code>IHyperlinkDetector</code> implementation for the
 * <code>HTMLSourceEditor</code>.
 * <p>
 * This class detects the <strong>href</string> attribute as the hyperlink in
 * default. And it's possible to add additional rules by
 * <code>addHyperlinkProvider()</code>.
 */
public class HTMLHyperlinkDetector implements IHyperlinkDetector {

	private HTMLSourceEditor editor;
	private List<IHyperlinkProvider> providers = new ArrayList<IHyperlinkProvider>();

	/**
	 * @param editor the <code>HTMLSourceEditor</code> instance
	 */
	public void setEditor(HTMLSourceEditor editor) {
		this.editor = editor;
	}

	/**
	 * Adds the additional hyperlink provider.
	 *
	 * @param provider the additional hyperlink provider
	 */
	public void addHyperlinkProvider(IHyperlinkProvider provider) {
		this.providers.add(provider);
	}

	/**
	 * Returns the <code>IProject</code> of the editing file.
	 * <p>
	 * If the editor input isn't <code>IFileEditorInput</code>, this method returns
	 * <code>null</code>.
	 *
	 * @return the <code>IProject</code>
	 */
	private IProject getProject() {
		IEditorInput input = editor.getEditorInput();
		if (input instanceof IFileEditorInput) {
			return ((IFileEditorInput) input).getFile().getProject();
		}
		return null;
	}

	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
		IHyperlink hyperlink = detectHyperlink(textViewer.getDocument(), region.getOffset());
		if (hyperlink != null) {
			return new IHyperlink[] { hyperlink };
		}
		return null;
	}

	private IHyperlink detectHyperlink(IDocument doc, int offset) {
		FuzzyXMLDocument document = new FuzzyXMLParser().parse(editor.getHTMLSource());
		FuzzyXMLElement element = document.getElementByOffset(offset);
		if (element == null) {
			return null;
		}
		if (element.getName().equalsIgnoreCase("param-value")) {
			String vpath = element.getValue();
			if (vpath.indexOf(":") > 0) {
				vpath = vpath.substring(vpath.indexOf(":") + 1);
			}
			vpath = "src/" + vpath;
			IProject project = getProject();
			IResource resource = project.findMember(vpath);
			return new HTMLHyperlink(new Region(element.getOffset() + element.getName().length() + 2,
					element.getLength() - 2 * (element.getName().length() + 2) - 1), resource, 0, null);
		}
		if (element.getName().contains("-class")) {
			String path = "src." + element.getValue();
			path = path.replace(".", "/") + ".java";
			IProject project = getProject();
			IResource resource = project.findMember(path);
			return new HTMLHyperlink(new Region(element.getOffset() + element.getName().length() + 2,
					element.getLength() - 2 * (element.getName().length() + 2) - 1), resource, 0, null);
		}
		FuzzyXMLAttribute[] attrs = element.getAttributes();
		for (int i = 0; i < attrs.length; i++) {
			if (attrs[i].getOffset() < offset && offset < attrs[i].getOffset() + attrs[i].getLength()) {
				int attrOffset = getAttributeValueOffset(doc.get(), attrs[i]);
				int attrLength = attrs[i].getValue().length();
				if (attrOffset >= 0 && attrLength >= 0 && attrOffset <= offset) {
					HTMLHyperlinkInfo info = getOpenFileInfo(document, element, attrs[i].getName(), attrs[i].getValue(),
							offset - attrOffset);
					if (info == null || info.getObject() == null) {
						return null;
					} else {
						return new HTMLHyperlink(new Region(attrOffset + info.getOffset(), info.getLength()),
								info.getObject(), info.getTargetOffset(), info.getSel());
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns a target of hyperlink.
	 */
	private HTMLHyperlinkInfo getOpenFileInfo(FuzzyXMLDocument doc, FuzzyXMLElement element, String attrName,
			String attrValue, int offset) {
		try {
			IProject project = getProject();
			if (project == null) {
				return null;
			}
			IFile file = ((IFileEditorInput) editor.getEditorInput()).getFile();
			for (int i = 0; i < providers.size(); i++) {
				IHyperlinkProvider provider = this.providers.get(i);
				HTMLHyperlinkInfo info = provider.getHyperlinkInfo(file, doc, element, attrName, attrValue, offset);
				if (info != null && info.getObject() != null) {
					return info;
				}
			}
			if (attrName.equalsIgnoreCase("href")) {
				String href = attrValue;
				String name = null;
				if (href.startsWith("#")) {
					FuzzyXMLElement target = getNameElement(doc, href.substring(1));
					if (target != null) {
						HTMLHyperlinkInfo info = new HTMLHyperlinkInfo();
						info.setObject(file);
						info.setOffset(0);
						info.setLength(attrValue.length());
						info.setTargetOffset(target.getOffset());
						return info;
					}
				}
				if (href.indexOf("#") > 0) {
					name = href.substring(href.indexOf("#") + 1);
					href = href.substring(0, href.indexOf("#"));
				}
				IPath path = file.getParent().getProjectRelativePath();
				IResource resource = project.findMember(path.append(href));
				if (resource != null && resource.exists() && resource instanceof IFile) {
					HTMLHyperlinkInfo info = new HTMLHyperlinkInfo();
					info.setObject(resource);
					info.setOffset(0);
					info.setLength(attrValue.length());
					if (name != null) {
						FuzzyXMLElement target = getNameElement(
								new FuzzyXMLParser().parse(((IFile) resource).getContents()), name);
						if (target != null) {
							info.setTargetOffset(target.getOffset());
						}
					}
					return info;
				}
			}
			if (attrName.equalsIgnoreCase("src")) {
				String src = attrValue;
				IPath path = file.getParent().getProjectRelativePath();
				IResource resource = project.findMember(path.append(src));
				if (resource != null && resource.exists() && resource instanceof IFile) {
					HTMLHyperlinkInfo info = new HTMLHyperlinkInfo();
					info.setObject(resource);
					info.setOffset(0);
					info.setLength(attrValue.length());
					return info;
				}
			}
			if (attrName.equalsIgnoreCase("javacode")) {
				String javacode = "src." + attrValue;
				javacode = javacode.substring(0, javacode.lastIndexOf("."));
				javacode = javacode.replace(".", "/") + ".java";
				String se = attrValue.substring(attrValue.lastIndexOf(".") + 1);
//				IPath path = file.getParent().getProjectRelativePath();
				IResource resource = project.findMember(javacode);
				if (resource != null && resource.exists() && resource instanceof IFile) {
					HTMLHyperlinkInfo info = new HTMLHyperlinkInfo();
					info.setObject(resource);
					info.setOffset(0);
					info.setLength(attrValue.length());
					info.setSel(se);
					return info;
				}
			}
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}
		return null;
	}

	private static FuzzyXMLElement getNameElement(FuzzyXMLDocument doc, String value) {
		FuzzyXMLElement root = doc.getDocumentElement();
		return getNameFromElement(root, value);
	}

	private static FuzzyXMLElement getNameFromElement(FuzzyXMLElement element, String value) {
		if (element.getName().equalsIgnoreCase("a")) {
			FuzzyXMLAttribute[] attrs = element.getAttributes();
			for (int i = 0; i < attrs.length; i++) {
				if (attrs[i].getName().equalsIgnoreCase("name")) {
					if (attrs[i].getValue().equals(value)) {
						return element;
					}
				}
			}
		}
		FuzzyXMLNode[] nodes = element.getChildren();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] instanceof FuzzyXMLElement) {
				FuzzyXMLElement result = getNameFromElement((FuzzyXMLElement) nodes[i], value);
				if (result != null) {
					return result;
				}
			}
		}

		return null;
	}

	/**
	 * Returns an attribute value offset.
	 *
	 * @param source the source code
	 * @param attr   the attribute
	 * @return the offset of the attribute
	 */
	private int getAttributeValueOffset(String source, FuzzyXMLAttribute attr) {
		int offset = source.indexOf('=', attr.getOffset());
		if (offset == -1) {
			return -1;
		}
		char c = ' ';
		while (c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '"' || c == '\'') {
			offset++;
			if (source.length() == offset + 1) {
				break;
			}
			c = source.charAt(offset);
		}
		return offset;
	}

	private class HTMLHyperlink implements IHyperlink {

		private IRegion region;
		private Object openObject;
		private int offset = -1;
		private String sel = null;

//		public HTMLHyperlink(IRegion region, Object openObject){
//			this(region, openObject, -1);
//		}

//		public HTMLHyperlink(IRegion region, Object openObject, int offset) {
//			this.region = region;
//			this.openObject = openObject;
//			this.offset = offset;
//		}

		public HTMLHyperlink(IRegion region, Object openObject, int offset, String sel) {
			this.region = region;
			this.openObject = openObject;
			this.offset = offset;
			this.sel = sel;
		}

		public IRegion getHyperlinkRegion() {
			return region;
		}

		public String getTypeLabel() {
			return null;
		}

		public String getHyperlinkText() {
			return null;
		}

		public String getSel() {
			return sel;
		}

		@SuppressWarnings("deprecation")
		public void open() {
			try {
				if (openObject instanceof IFile) {
					IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					IEditorPart editor = IDE.openEditor(window.getActivePage(), (IFile) openObject);
//					IEditorPart editor = IDE.openEditor(window.getActivePage(),(IFile)openObject,
//							"com.tulin.v8.webtools.ide.editors.HTMLEditor", true);
					if (offset >= 0 && editor instanceof HTMLEditor) {
						((HTMLEditor) editor).getPaletteTarget().selectAndReveal(offset, 0);
					}
					if (getSel() != null && editor instanceof AbstractTextEditor) {
						String se = getSel();
						IDocument document = ((AbstractTextEditor) editor).getDocumentProvider()
								.getDocument(editor.getEditorInput());
						int s = document.search(0, se, true, false, false);
						((AbstractTextEditor) editor).selectAndReveal(s, se.length());// 设置选中
					}
				} else if (openObject instanceof IJavaElement) {
					JavaUI.revealInEditor(JavaUI.openInEditor((IJavaElement) openObject), (IJavaElement) openObject);
				}
			} catch (Exception ex) {
				WebToolsPlugin.logException(ex);
			}
		}

	}

}
