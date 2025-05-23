package com.tulin.v8.webtools.ide.html;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.JavadocContentAccess;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;

import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.fuzzyxml.FuzzyXMLNode;
import com.tulin.v8.fuzzyxml.XPath;
import com.tulin.v8.fuzzyxml.internal.FuzzyXMLUtil;
import com.tulin.v8.webtools.ide.WebToolsPlugin;

/**
 * This provides utility methods.
 *
 */
public class HTMLUtil {

	public static String extractJavadoc(IMember member, IProgressMonitor monitor)
			throws JavaModelException, IOException {
		if (member != null) {
			Reader reader = JavadocContentAccess.getContentReader(member, true);
			if (reader != null) {
				String info = getStringFromReader(reader);
//				if (info != null && info.length() > 0) {
//					StringBuffer buffer= new StringBuffer();
//					HTMLPrinter.insertPageProlog(buffer, 0);
//					buffer.append(info);
//					HTMLPrinter.addPageEpilog(buffer);
//					info= buffer.toString();
//				}
				return info;
			}
		}
		return null;
	}

	private static String getStringFromReader(Reader reader) {
		StringBuffer buf = new StringBuffer();
		char[] buffer = new char[1024];
		int count;
		try {
			while ((count = reader.read(buffer)) != -1)
				buf.append(buffer, 0, count);
		} catch (IOException e) {
			return null;
		}
		return buf.toString();
	}

	/**
	 * Returns whether the <code>IResourceDelta</code> contains the specified
	 * <code>IFile</code>.
	 *
	 * @param delta the <code>IResourceDelta</code>
	 * @param file  the <code>IFile</code>
	 * @return true if the file is contained, otherwise false
	 */
	public static boolean contains(IResourceDelta delta, IFile file) {
		IResourceDelta member = delta.findMember(file.getFullPath());
		if (member == null) {
			return false;
		}
		return true;
	}

	/**
	 * Escape HTML special characters.
	 *
	 * @param str the raw string
	 * @return the escaped string
	 */
	public static String escapeHTML(String str) {
		return FuzzyXMLUtil.escape(str, true);
	}

	/**
	 * Replaces comments of CSS with whitespaces.
	 *
	 * @param source CSS source code
	 * @return processed source code
	 */
	public static String cssComment2space(String source) {
		int index = 0;
		int last = 0;
		StringBuffer sb = new StringBuffer();
		while ((index = source.indexOf("/*", last)) != -1) {
			int end = source.indexOf("*/", index);
			if (end != -1) {
				sb.append(source.substring(last, index));
				int length = end - index + 2;
				for (int i = 0; i < length; i++) {
					sb.append(" ");
				}
			} else {
				break;
			}
			last = end + 2;
		}
		if (last != source.length() - 1) {
			sb.append(source.substring(last));
		}
		return sb.toString();
	}

	/**
	 * Replace comments of HTML/JSP/XML with whitespaces.
	 *
	 * <ul>
	 * <li>replace &lt;!-- ... --&gt; to the whitespaces</li>
	 * <li>replace &lt;%-- ... --%&gt; to the whitespaces</li>
	 * </ul>
	 *
	 * @param source       source code of the HTML/JSP/XML
	 * @param contentsOnly
	 *                     <ul>
	 *                     <li>true - &lt;!--A--&gt; and &lt;%--A--%&gt; are not
	 *                     replaced.
	 *                     <li>
	 *                     <li>false - &lt;!--A--&gt; and &lt;%--A--%&gt; are also
	 *                     replaced.
	 *                     <li>
	 *                     </ul>
	 * @return processed source code
	 */
	public static String comment2space(String source, boolean contentsOnly) {
		source = jspComment2space(source, contentsOnly);
		source = FuzzyXMLUtil.comment2space(source, contentsOnly);
		return source;
	}

	/**
	 * Replace comments of the JSP with whitespaces.
	 *
	 * @param source       source code of the JSP
	 * @param contentsOnly
	 *                     <ul>
	 *                     <li>true - &lt;%A%&gt; are not replaced.</li>
	 *                     <li>false - &lt;%A%&gt; are also replaced.</li>
	 *                     </ul>
	 * @return processed source code
	 */
	public static String jspComment2space(String source, boolean contentsOnly) {
		int index = 0;
		int last = 0;
		StringBuffer sb = new StringBuffer();
		while ((index = source.indexOf("<%--", last)) != -1) {
			int end = source.indexOf("--%>", index);
			if (end != -1) {
				sb.append(source.substring(last, index));
				int length = end - index + 4;
				if (contentsOnly) {
					sb.append("<%--");
					length = length - 8;
				}
				for (int i = 0; i < length; i++) {
					sb.append(" ");
				}
				if (contentsOnly) {
					sb.append("--%>");
				}
			} else {
				break;
			}
			last = end + 4;
		}
		if (last != source.length() - 1) {
			sb.append(source.substring(last));
		}
		return sb.toString();
	}

	/**
	 * Replace scriptlet in the JSP to whitespaces.
	 *
	 * @param source       source code of the JSP
	 * @param contentsOnly
	 *                     <ul>
	 *                     <li>true - &lt;%A%&gt; are not replaced.
	 *                     <li>false - &lt;%A%&gt; are also replaced.
	 *                     </ul>
	 * @return processed source code
	 */
	public static String scriptlet2space(String source, boolean contentsOnly) {
		int index = 0;
		int last = 0;
		StringBuffer sb = new StringBuffer();
		while ((index = source.indexOf("<%", last)) != -1) {
			int end = source.indexOf("%>", index);
			if (end != -1) {
				sb.append(source.substring(last, index));
				int length = end - index + 2;
				if (contentsOnly) {
					sb.append("<%");
					length = length - 4;
				}
				for (int i = 0; i < length; i++) {
					sb.append(" ");
				}
				if (contentsOnly) {
					sb.append("%>");
				}
			} else {
				break;
			}
			last = end + 2;
		}
		if (last != source.length() - 1) {
			sb.append(source.substring(last));
		}
		return sb.toString();
	}

	/**
	 * Escape XML string.
	 * <p>
	 * If value is null, this method returns an empty string.
	 * </p>
	 * 
	 * @param value string
	 * @return escaped string
	 */
	public static String escapeXML(String value) {
		return FuzzyXMLUtil.escape(value, false);
	}

	/**
	 * Wrapps XPath#getValue() of FuzzyXML. This method provides following
	 * additional features.
	 *
	 * <ul>
	 * <li>returns null when exceptions are caused.</li>
	 * <li>trims a return value.</li>
	 * </ul>
	 *
	 * @param element the base element
	 * @param xpath   XPath
	 * @return the selected value or null
	 */
	public static String getXPathValue(FuzzyXMLElement element, String xpath) {
		try {
			String value = (String) XPath.getValue(element, xpath);
			return value.trim();
		} catch (Exception ex) {
			return null;
		}
	}

	public static FuzzyXMLNode selectXPathNode(FuzzyXMLElement element, String xpath) {
		try {
			return XPath.selectSingleNode(element, xpath);
		} catch (Exception ex) {
			return null;
		}
	}

	public static FuzzyXMLNode[] selectXPathNodes(FuzzyXMLElement element, String xpath) {
		try {
			return XPath.selectNodes(element, xpath);
		} catch (Exception ex) {
			return new FuzzyXMLNode[0];
		}
	}

	/**
	 * Returns an empty string if an argument is null.
	 */
	public static String nullConv(String value) {
		if (value == null) {
			return "";
		}
		return value;
	}

	/**
	 * Creates a Document object from InputStream.
	 *
	 * @param in       InputStream of a XML document
	 * @param resolver EntityResolver or null
	 * @return Document object
	 * @throws ParserConfigurationException
	 * @throws FactoryConfigurationError
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document createDocument(InputStream in, EntityResolver resolver)
			throws ParserConfigurationException, FactoryConfigurationError, SAXException, IOException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		if (resolver != null) {
			builder.setEntityResolver(resolver);
		}
		Document doc = builder.parse(in);
		return doc;
	}

	/**
	 * Sorts informations of code completion in alphabetical order.
	 *
	 * @param prop the list of ICompletionProposal
	 */
	public static void sortCompilationProposal(List<ICompletionProposal> prop) {
		Collections.sort(prop, new Comparator<ICompletionProposal>() {
			public int compare(ICompletionProposal o1, ICompletionProposal o2) {
				return o1.getDisplayString().compareTo(o2.getDisplayString());
			}
		});
	}

	/**
	 * Returns a project encoding.
	 *
	 * @param project project
	 * @return encoding
	 */
	public static String getProjectCharset(IProject project) {
		try {
			String charset = project.getDefaultCharset();
			if (charset.equals("MS932")) {
//				charset = "Shift_JIS";
				charset = "Windows-31J";
			}
			return charset;
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}
		return null;
	}

	/**
	 * Adds marker to the specified line.
	 *
	 * @param resource the target resource
	 * @param type     the error type that defined by IMaker
	 * @param line     the line number
	 * @param message  the error message
	 */
	public static void addMarker(IResource resource, int type, int line, String message) {
		try {
			IMarker marker = resource.createMarker(IMarker.PROBLEM);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(IMarker.SEVERITY, type);
			map.put(IMarker.MESSAGE, message);
			map.put(IMarker.LINE_NUMBER, line);
			marker.setAttributes(map);
		} catch (CoreException ex) {
			WebToolsPlugin.logException(ex);
		}
	}

	/**
	 * Adds task marker to the specified range.
	 *
	 * @param resource the target resource
	 * @param priority the priority that defined by IMaker
	 * @param line     the line number
	 * @param offset   the offset
	 * @param length   the length
	 * @param message  the error message
	 */
	public static void addTaskMarker(IResource resource, int priority, int line, String message) {
		try {
			IMarker marker = resource.createMarker(IMarker.TASK);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(IMarker.PRIORITY, priority);
			map.put(IMarker.MESSAGE, message);
			map.put(IMarker.LINE_NUMBER, line);
			marker.setAttributes(map);
		} catch (CoreException ex) {
			WebToolsPlugin.logException(ex);
		}
	}

	/**
	 * Adds marker to the specified range.
	 *
	 * @param resource the target resource
	 * @param type     the error type that defined by IMaker
	 * @param line     the line number
	 * @param offset   the offset
	 * @param length   the length
	 * @param message  the error message
	 */
	public static void addMarker(IResource resource, int type, int line, int offset, int length, String message) {
		try {
			IMarker marker = resource.createMarker(IMarker.PROBLEM);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(IMarker.SEVERITY, type);
			map.put(IMarker.MESSAGE, message);
			map.put(IMarker.CHAR_START, offset);
			map.put(IMarker.CHAR_END, offset + length);
			map.put(IMarker.LINE_NUMBER, line);
			marker.setAttributes(map);
		} catch (CoreException ex) {
			WebToolsPlugin.logException(ex);
		}
	}

	/**
	 * Trim all elements in the array.
	 *
	 * @param array The string array
	 */
	public static void trim(String[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = array[i].trim();
		}
	}

	/**
	 * Returns the first child element of the specified element.
	 *
	 * @param element the element
	 * @return the first child element
	 */
	public static FuzzyXMLElement getFirstElement(FuzzyXMLElement element) {
		FuzzyXMLNode[] nodes = element.getChildren();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] instanceof FuzzyXMLElement) {
				return (FuzzyXMLElement) nodes[i];
			}
		}
		return null;
	}

	/**
	 * Converts {@link RGB} to the hex string.
	 *
	 * @param color the RGB object
	 * @return the hex string
	 */
	public static String toHex(RGB color) {
		StringBuffer sb = new StringBuffer();
		sb.append("#").append(toHex(color.red)).append(toHex(color.green)).append(toHex(color.blue));
		return sb.toString();
	}

	private static String toHex(int value) {
		String hex = Integer.toHexString(value);
		if (hex.length() == 1) {
			hex = "0" + hex;
		}
		return hex;
	}

	/**
	 * Converts the hex string to {@link RGB}.
	 *
	 * @param value the hex string
	 * @return {@link RGB}
	 */
	public static RGB toRGB(String value) {
		if (value.startsWith("#")) {
			String red = value.substring(1, 3);
			String green = value.substring(3, 5);
			String blue = value.substring(5, 7);
			return new RGB(toDecimal(red), toDecimal(green), toDecimal(blue));
		}
		return null;
	}

	private static int toDecimal(String value) {
		int result = 0;
		int count = 1;
		for (int i = value.length() - 1; i >= 0; i--) {
			char c = value.charAt(i);
			if (c >= '0' && c <= '9') {
				result += Integer.parseInt(String.valueOf(c)) * count;
			} else if (c >= 'a' || c <= 'f') {
				result += (c - 'a' + 10) * count;
			} else if (c >= 'A' || c <= 'F') {
				result += (c - 'A' + 10) * count;
			}
			count = count * 16;
		}
		return result;
	}

	/**
	 * Returns the getter method name from the property name.
	 *
	 * @param propertyName the property name
	 * @param isBool
	 *                     <ul>
	 *                     <li>true - returns isXxxx</li>
	 *                     <li>false - returns getXxxx</li>
	 *                     </ul>
	 * @return the getter method name
	 */
	public static String getGetterName(String propertyName, boolean isBool) {
		if (isBool) {
			return "is" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		} else {
			return "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		}
	}

	/**
	 * Returns the setter method name from the property name.
	 *
	 * @param propertyName the propery name
	 * @return the setter method name
	 */
	public static String getSetterName(String propertyName) {
		return "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
	}

	/**
	 * Returns an active editor part in the workbench.
	 *
	 * @return An instance of an active editorpart.
	 */
	public static IEditorPart getActiveEditor() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		if (window == null) {
			window = workbench.getWorkbenchWindows()[0];
		}
		IWorkbenchPage page = window.getActivePage();
		IEditorPart editorPart = page.getActiveEditor();
		if (editorPart instanceof MultiPageEditorPart) {
			MultiPageEditorPart meditor = (MultiPageEditorPart) editorPart;
			Object seditor = meditor.getSelectedPage();
			if (seditor instanceof IEditorPart) {
				return (IEditorPart) meditor.getSelectedPage();
			}
		}
		return editorPart;
	}

	public static IFile getActiveFile() {
		IEditorInput input = HTMLUtil.getActiveEditor().getEditorInput();
		if (input instanceof IFileEditorInput) {
			return ((IFileEditorInput) input).getFile();
		}
		return null;
	}

	private static Map<IJavaProject, ICompilationUnit> unitMap = new HashMap<IJavaProject, ICompilationUnit>();

	/**
	 * Creates the <code>ICompilationUnit</code> to use temporary.
	 *
	 * @param project the java project
	 * @return the temporary <code>ICompilationUnit</code>
	 * @throws JavaModelException
	 * @since 2.0.3
	 */
	public synchronized static ICompilationUnit getTemporaryCompilationUnit(IJavaProject project)
			throws JavaModelException {

		if (unitMap.get(project) != null) {
			return unitMap.get(project);
		}

		IPackageFragment root = project.getPackageFragments()[0];
		ICompilationUnit unit = root.getCompilationUnit("_xxx.java").getWorkingCopy(new NullProgressMonitor());

		unitMap.put(project, unit);

		return unit;
	}

	/**
	 * Set contents of the compilation unit to the translated jsp text.
	 *
	 * @param unit  the ICompilationUnit on which to set the buffer contents
	 * @param value Java source code
	 * @since 2.0.3
	 */
	public static void setContentsToCU(ICompilationUnit unit, String value) {
		if (unit == null)
			return;

		synchronized (unit) {
			IBuffer buffer;
			try {

				buffer = unit.getBuffer();
			} catch (JavaModelException e) {
				e.printStackTrace();
				buffer = null;
			}

			if (buffer != null)
				buffer.setContents(value);
		}
	}

	/**
	 * Open the class select dialog.
	 *
	 * @param parent the parent control
	 * @return the selected type name or <code>null</code>
	 */
	public static String openClassSelectDialog(IJavaProject project, Control parent) {
		try {
			Shell shell = parent.getShell();
			SelectionDialog dialog = JavaUI.createTypeDialog(shell, new ProgressMonitorDialog(shell),
					SearchEngine.createJavaSearchScope(new IJavaElement[] { project }),
					IJavaElementSearchConstants.CONSIDER_CLASSES, false);

			if (dialog.open() == SelectionDialog.OK) {
				Object[] result = dialog.getResult();
				return ((IType) result[0]).getFullyQualifiedName();
			}
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}
		return null;
	}

	/**
	 * Open the type (class and interface) select dialog.
	 *
	 * @param parent the parent control
	 * @return the selected type name or <code>null</code>
	 */
	public static String openTypeSelectDialog(IJavaProject project, Control parent) {
		try {
			Shell shell = parent.getShell();
			SelectionDialog dialog = JavaUI.createTypeDialog(shell, new ProgressMonitorDialog(shell),
					SearchEngine.createJavaSearchScope(new IJavaElement[] { project }),
					IJavaElementSearchConstants.CONSIDER_CLASSES | IJavaElementSearchConstants.CONSIDER_INTERFACES,
					false);

			if (dialog.open() == SelectionDialog.OK) {
				Object[] result = dialog.getResult();
				return ((IType) result[0]).getFullyQualifiedName();
			}
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}
		return null;
	}

	public static boolean isDynamicWebProject(IProject project) {
		try {
			return project.hasNature("org.eclipse.wst.common.project.facet.core.nature");
		} catch (CoreException ex) {
			return false;
		}
	}

	public static boolean compareString(String value1, String value2) {
		if (value1 == null && value2 == null) {
			return true;
		}
		if (value1 != null && value1.equals(value2)) {
			return true;
		}
		return false;
	}
}
