package com.tulin.v8.webtools.ide.jsp;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.xerces.parsers.DOMParser;
import org.eclipse.jdt.core.IJavaProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.assist.AttributeInfo;
import com.tulin.v8.webtools.ide.assist.TagInfo;
import com.tulin.v8.webtools.ide.gefutils.IJarVisitor;
import com.tulin.v8.webtools.ide.gefutils.JarAcceptor;
import com.tulin.v8.webtools.ide.jsp.editors.Function;

public class TLDParser {

	private IJavaProject project;
	private String uri = null;
	private String prefix = "";
	private List<TagInfo> tags = new ArrayList<TagInfo>();
	private List<Function> functions = new ArrayList<Function>();

	/**
	 * Use the specified prefix.
	 *
	 * @param the prefix
	 */
	public TLDParser(IJavaProject project, String prefix) {
		this.project = project;
		this.prefix = prefix;
	}

	/**
	 * Use tld's <code>shortname</code> (or <code>short-name</code>) as prefix.
	 */
	public TLDParser(IJavaProject project) {
		this(project, null);
	}

	/**
	 * Returns the <code>uri</code> which declared in the TLD.
	 *
	 * @return the <code>uri</code>
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Returns parsed <code>List</code> which contains <code>TagInfo</code>.
	 *
	 * @return <code>List</code> which contains <code>TagInfo</code>
	 */
	public List<TagInfo> getTags() {
		return tags;
	}

	public List<Function> getFunctions() {
		return functions;
	}

	public void parse(InputStream in) throws Exception {
		DOMParser parser = new DOMParser();
		parser.setEntityResolver(new TLDResolver());
		parser.parse(new InputSource(in));
		Document doc = parser.getDocument();
		Element element = doc.getDocumentElement();

		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				Element childElement = (Element) nodeList.item(i);
				String elementName = childElement.getNodeName();
				if (elementName.equals("tag")) {
					tags.add(parseTagElement(childElement));
				} else if (elementName.equals("uri")) {
					uri = getChildText(childElement);
				} else if (elementName.equals("shortname") || elementName.equals("short-name")) {
					if (prefix == null) {
						prefix = getChildText(childElement);
					}
				} else if (elementName.equals("tag-file")) {
					TagInfo tagInfo = parseTagFileElement(childElement);
					if (tagInfo != null) {
						tags.add(tagInfo);
					}
				} else if (elementName.equals("function")) {
					functions.add(parseFunctionElement(childElement));
				}
			}
		}
	}

	/**
	 * Parses the tag element and returns <code>TagInfo</code> as result.
	 *
	 * @param tagFile the tag element
	 * @return the <code>TagInfo</code> from the given tag
	 */
	private TagInfo parseTagElement(Element tag) {
		NodeList children = tag.getChildNodes();

		List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
		String name = null;
		String description = "";
		boolean hasBody = true;
		boolean dynamicAttributes = false;

		for (int j = 0; j < children.getLength(); j++) {
			Node node = children.item(j);
			if (node instanceof Element) {
				Element element = (Element) node;
				String elementName = element.getNodeName();
				if (elementName.equals("name")) {
					name = prefix + ":" + getChildText(element);
				} else if (elementName.equals("bodycontent") || elementName.equals("body-content")) {
					hasBody = !getChildText(element).equals("empty");
				} else if (elementName.equals("description")) {
					description = wrap(getChildText(element));
				} else if (elementName.equals("attribute")) {
					AttributeInfo attrInfo = parseAttributeElement(element);
					attributes.add(attrInfo);
				} else if (elementName.equals("dynamic-attributes")) {
					dynamicAttributes = getChildText(element).equals("true");
				}
			}
		}

		JSPTagInfo info = new JSPTagInfo(name, hasBody);
		info.setDescription(description);
		info.setDynamicAttributes(dynamicAttributes);
		for (int i = 0; i < attributes.size(); i++) {
			info.addAttributeInfo(attributes.get(i));
		}

		return info;
	}

	private AttributeInfo parseAttributeElement(Element attr) {
		NodeList children = attr.getChildNodes();

		String name = null;
		String description = "";
		boolean required = false;

		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Element) {
				Element element = (Element) node;
				String elementName = element.getNodeName();
				if (elementName.equals("name")) {
					name = getChildText(element);
				} else if (elementName.equals("description")) {
					description = wrap(getChildText(element));
				} else if (elementName.equals("required")) {
					if (getChildText(element).equals("true")) {
						required = true;
					} else {
						required = false;
					}
				}
			}
		}

		AttributeInfo attrInfo = new AttributeInfo(name, true, AttributeInfo.NONE, required);
		attrInfo.setDescription(description);
		return attrInfo;
	}

	private static String getChildText(Element element) {
		StringBuffer sb = new StringBuffer();
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Text) {
				sb.append(node.getNodeValue());
			}
		}
		return sb.toString().trim().replaceAll("\\s+", " ");
	}

	private static String wrap(String text) {
		StringBuffer sb = new StringBuffer();
		int word = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (word > 40) {
				if (c == ' ' || c == '\t') {
					sb.append('\n');
					word = 0;
					continue;
				}
			}
			sb.append(c);
			word++;
		}
		return sb.toString();
	}

	/**
	 * Parses the tag-file element and returns <code>TagInfo</code> as result.
	 *
	 * @since 2.0.5
	 * @param tagFile the tag-file element
	 * @return the <code>TagInfo</code> from given tag file or <code>null</code> if
	 *         given tag file does not found.
	 */
	private TagInfo parseTagFileElement(Element tagFile) throws Exception {
		NodeList children = tagFile.getChildNodes();

		List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
		String name = null;
		String description = "";
		boolean hasBody = true;

		for (int j = 0; j < children.getLength(); j++) {
			Node node = children.item(j);
			if (node instanceof Element) {
				Element element = (Element) node;
				String elementName = element.getNodeName();
				if (elementName.equals("name")) {
					name = prefix + ":" + getChildText(element);
				} else if (elementName.equals("description")) {
					description = wrap(getChildText(element));
				} else if (elementName.equals("path") && project != null) {
					String path = getChildText(element);
					InputStream in = getTagFile(path);
					if (in != null) {
						TagInfo tagInfo = TagFileParser.parseTagFile(null, null, in);
						for (int i = 0; i < tagInfo.getAttributeInfo().length; i++) {
							attributes.add(tagInfo.getAttributeInfo()[i]);
						}
					} else {
						return null;
					}
				}
			}
		}

		TagInfo info = new TagInfo(name, hasBody);
		info.setDescription(description);
		for (int i = 0; i < attributes.size(); i++) {
			info.addAttributeInfo(attributes.get(i));
		}

		return info;
	}

	/**
	 *
	 * @since 2.0.6
	 */
	private Function parseFunctionElement(Element element) throws Exception {
		NodeList children = element.getChildNodes();
		Function function = new Function();

		for (int j = 0; j < children.getLength(); j++) {
			Node node = children.item(j);
			if (node instanceof Element) {
				Element child = (Element) node;
				String elementName = child.getNodeName();
				if (elementName.equals("name")) {
					function.setName(prefix + ":" + getChildText(child));
				} else if (elementName.equals("description")) {
					function.setDescription(wrap(getChildText(child)));
//				} else if(elementName.equals("function-signature")){
//					function.setSignature(getChildText(child));
				}
			}
		}

		return function;
	}

	/**
	 * TODO This method might have to be moved to the utility class.
	 *
	 * @since 2.0.5
	 * @param path the path of the tag file
	 *             <ul>
	 *             <li>starts with &quot;/META-INF/tags&quot; : in the WAR file
	 *             <li>starts with &quot;/WEB-INF/tags&quot; : in the JAR file
	 *             </ul>
	 * @return the <code>InputSream</code> of the given tag file or
	 *         <code>null</code>.
	 */
	private InputStream getTagFile(final String path) {
		try {
			if (path.startsWith("/META-INF/tags")) {
				// in JAR (or classpath?)
				return (InputStream) JarAcceptor.accept(project.getProject(), new IJarVisitor() {
					public Object visit(ZipFile file, ZipEntry entry) throws Exception {
						if (("/" + entry.getName()).equals(path)) {
							return file.getInputStream(entry);
						}
						return null;
					}
				});
			}

			// in WAR
			File basedir = TLDInfo.getBaseDir(project.getProject()).getLocation().makeAbsolute().toFile();
			File file = new File(basedir, path);
			return new FileInputStream(file);

		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
			return null;
		}
	}

}
