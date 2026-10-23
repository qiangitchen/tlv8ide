package com.tulin.v8.webtools.ide.xml.editors;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;
import org.eclipse.ui.IFileEditorInput;

import com.tulin.v8.fuzzyxml.FuzzyXMLAttribute;
import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.assist.AssistInfo;
import com.tulin.v8.webtools.ide.assist.AttributeInfo;
import com.tulin.v8.webtools.ide.assist.HTMLAssistProcessor;
import com.tulin.v8.webtools.ide.assist.TagInfo;
import com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditor;
import com.wutka.dtd.DTD;
import com.wutka.dtd.DTDAttribute;
import com.wutka.dtd.DTDChoice;
import com.wutka.dtd.DTDDecl;
import com.wutka.dtd.DTDElement;
import com.wutka.dtd.DTDEmpty;
import com.wutka.dtd.DTDEnumeration;
import com.wutka.dtd.DTDItem;
import com.wutka.dtd.DTDMixed;
import com.wutka.dtd.DTDName;
import com.wutka.dtd.DTDParseException;
import com.wutka.dtd.DTDParser;
import com.wutka.dtd.DTDSequence;

/**
 * The AssistProcessor for the <code>XMLEditor</code>.
 *
 * @author Naoki Takezoe
 * @see XMLEditor
 */
public class XMLAssistProcessor extends HTMLAssistProcessor {

	private List<TagInfo> tagList = new ArrayList<TagInfo>();
	private Map<String, List<TagInfo>> nsTagListMap = new HashMap<String, List<TagInfo>>();
	private XMLEditor editor;
	private IFileEditorInput input;
	private ClassNameAssistProcessor classNameAssistant = new ClassNameAssistProcessor();

	/**
	 * The constructor without DTD / XSD.
	 * <p>
	 * <code>XMLAssistProcessor</code> that's created by this constructor can
	 * complete only close tags.
	 */
	public XMLAssistProcessor() {
		super();
	}

	/**
	 * Update informations about code-completion.
	 *
	 * @param input  the <code>XMLEditor</code>
	 * @param source XML source code
	 */
	@Override
	public void update(HTMLSourceEditor editor, String source) {
		if (editor.getEditorInput() instanceof IFileEditorInput) {
			this.input = (IFileEditorInput) editor.getEditorInput();
		}
		if (editor instanceof XMLEditor) {
			this.editor = (XMLEditor) editor;
		}
	}

	/**
	 * Refresh DTD informations.
	 *
	 * @param in the <code>InputStream</code> of the DTD
	 */
	public void updateDTDInfo(Reader in) {
		// clear at fisrt
		tagList.clear();
		try {
			DTDParser parser = new DTDParser(in);
			DTD dtd = parser.parse();
			Object[] obj = dtd.getItems();
			for (int i = 0; i < obj.length; i++) {
				if (obj[i] instanceof DTDElement) {
					DTDElement element = (DTDElement) obj[i];
					String name = element.getName();
					DTDItem item = element.getContent();
					boolean hasBody = true;
					if (item instanceof DTDEmpty) {
						hasBody = false;
					}
					TagInfo tagInfo = new TagInfo(name, hasBody);
					@SuppressWarnings("rawtypes")
					Iterator ite = element.attributes.keySet().iterator();

					// set child tags
					if (item instanceof DTDSequence) {
						DTDSequence seq = (DTDSequence) item;
						setChildTagName(tagInfo, seq.getItem());
					} else if (item instanceof DTDMixed) {
						// #PCDATA
					}

					while (ite.hasNext()) {
						String attrName = (String) ite.next();
						DTDAttribute attr = element.getAttribute(attrName);

						DTDDecl decl = attr.getDecl();
						boolean required = false;
						if (decl == DTDDecl.REQUIRED) {
							required = true;
						}

						AttributeInfo attrInfo = new AttributeInfo(attrName, true, AttributeInfo.NONE, required);
						tagInfo.addAttributeInfo(attrInfo);

						Object attrType = attr.getType();
						if (attrType instanceof DTDEnumeration) {
							DTDEnumeration dtdEnum = (DTDEnumeration) attrType;
							String[] items = dtdEnum.getItems();
							for (int j = 0; j < items.length; j++) {
								attrInfo.addValue(items[j]);
							}
						} else if (attrType.equals("ID")) {
							attrInfo.setAttributeType(AttributeInfo.ID);
						} else if (attrType.equals("IDREF")) {
							attrInfo.setAttributeType(AttributeInfo.IDREF);
						} else if (attrType.equals("IDREFS")) {
							attrInfo.setAttributeType(AttributeInfo.IDREFS);
						}
					}
					tagList.add(tagInfo);
					// TODO root tag is an element that was found at first.
				}
			}
		} catch (DTDParseException ex) {
			// ignore
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}
	}

	/**
	 * Refresh XML schema informations.
	 *
	 * @param uri the URI of the XML schema
	 * @param in  the <code>InputStream</code> of XML schema
	 */
	public void updateXSDInfo(String uri, Reader in) {
		try {
			XMLSchemaLoader loader = new XMLSchemaLoader();

			// --- Bug fix (Premature end of file + schema_reference.4):
			// The original code constructed:
			//     new XMLInputSource(null, null, null, reader, null)
			// i.e. NO publicId / systemId / baseSystemId.  When xerces parses an
			// XSD that contains <xsd:include schemaLocation="web-common_3_0.xsd"/>
			// (or any relative include/import), XSDHandler.getSchemaDocument
			// tries to resolve the included schema without any base URI and
			// without an LSResourceResolver.  It ends up passing a NULL / EMPTY
			// InputStream to SchemaParsingConfig.parse which triggers
			// XMLVersionDetector.determineDocVersion -> "Premature end of file"
			// (exactly the stack-trace the user reported).  A by-product warning
			// is "schema_reference.4: Failed to read schema document ..." followed
			// by "src-resolve: Cannot resolve the name 'javaee:web-appType'".
			//
			// The fix has three pieces:
			//   (1) Derive a synthetic systemId from the user-supplied URI so the
			//       input source has a base to resolve relative include paths
			//       against.
			//   (2) Plug an XNI XMLEntityResolver on the loader that first checks
			//       the plugin's bundled innerDTD map (namespace URIs, bare
			//       filenames like "web-common_3_0.xsd") and then falls back to
			//       the plugin classpath / basedir / filesystem so DTDResolver
			//       semantics are preserved.
			//   (3) Defensive: if the resolved resource still cannot be found
			//       (e.g. real javaee_6.xsd / jsp_2_2.xsd files are missing) we
			//       return null instead of an empty InputStream.  That way xerces
			//       only logs the well-understood schema_reference.4 warning and
			//       does NOT attempt to parse zero bytes (which is what produces
			//       the Premature-end-of-file FATAL in the user's log).
			String systemId = toSystemId(uri);
			String baseSystemId = toBaseSystemId(systemId);
			loader.setEntityResolver(new BundledXSDResolver());
			XMLInputSource input = new XMLInputSource(null, systemId, baseSystemId, in, null);

			SchemaGrammar grammer = (SchemaGrammar) loader.loadGrammar(input);

			// clear at first
			String targetNS = grammer.getTargetNamespace();
			nsTagListMap.put(targetNS, new ArrayList<TagInfo>());
			List<TagInfo> tagList = nsTagListMap.get(targetNS);

			XSNamedMap map = grammer.getComponents(XSConstants.ELEMENT_DECLARATION);
			for (int i = 0; i < map.getLength(); i++) {
				XSElementDeclaration element = (XSElementDeclaration) map.item(i);
				parseXSDElement(tagList, element);
			}
		} catch (Exception ex) {

		WebToolsPlugin.logException(ex);

		}
	}

	/**
	 * Convert a schema URI (which might be a plain filename like "web-app_3_0.xsd",
	 * a file: path, a namespace URL like "http://java.sun.com/xml/ns/j2ee", or a
	 * preference-mapped URL) into a value safe to assign as XMLInputSource
	 * systemId.  The only hard guarantee we need is that the id is NOT null so
	 * Xerces won't build a synthetic null-base XMLResourceIdentifier for
	 * include-resolution.
	 */
	private static String toSystemId(String uri) {
		if (uri == null) return "x-schema://local/default.xsd";
		String u = uri.trim();
		if (u.isEmpty()) return "x-schema://local/default.xsd";
		if (u.startsWith("file:") || u.startsWith("http:") || u.startsWith("https:")
				|| u.startsWith("jar:") || u.startsWith("bundleentry:")
				|| u.startsWith("platform:")) {
			return u;
		}
		// Plain filename.  Prepend a "classpath://" authority (not resolved by
		// any real URL stream handler — purely a base URI for Xerces to compute
		// relative include paths off of).  The actual stream comes from the
		// reader already passed in / via BundledXSDResolver for includes.
		if (u.indexOf('/') < 0 && u.indexOf('\\') < 0) {
			return "classpath:///XSD/" + u;
		}
		File f = new File(u);
		if (f.isAbsolute()) return f.toURI().toString();
		return "classpath:///XSD/" + u;
	}

	private static String toBaseSystemId(String systemId) {
		if (systemId == null) return null;
		int slash = systemId.lastIndexOf('/');
		if (slash < 0) return systemId + "/";
		return systemId.substring(0, slash + 1);
	}

	/**
	 * XNI {@link XMLEntityResolver} that resolves XSD include / import references
	 * to the plugin's bundled innerDTD/XSD/ directories.  If we can't find the
	 * schema we deliberately return {@code null} so xerces logs a single
	 * schema_reference.4 warning instead of trying to parse an empty source and
	 * crashing with the infamous "Premature end of file" fatal error.
	 */
	private static final class BundledXSDResolver implements XMLEntityResolver {
		@Override
		public XMLInputSource resolveEntity(XMLResourceIdentifier identifier)
				throws XNIException, IOException {
			if (identifier == null) return null;

			InputStream resolved = null;
			String publicId = identifier.getPublicId();
			String literalSystemId = identifier.getLiteralSystemId();
			String expandedSystemId = identifier.getExpandedSystemId();
			String namespace = identifier.getNamespace();

			// Resolution order matters a lot when several bundled XSDs share the
			// same targetNamespace (e.g. web-app_3_0.xsd + web-common_3_0.xsd +
			// javaee_6.xsd + jsp_2_2.xsd ALL declare
			//     targetNamespace="http://java.sun.com/xml/ns/javaee"
			// and the top-level web-app_3_0.xsd does
			//     <xsd:include schemaLocation="web-common_3_0.xsd"/>
			//     <xsd:include schemaLocation="javaee_6.xsd"/> (transitively)
			// In that situation a naive "namespace first" lookup would map the
			// namespace "http://java.sun.com/xml/ns/javaee" to the top-level
			// /XSD/web-app_3_0.xsd for EVERY include, which makes xerces parse
			// the root schema again inside an include -> every global component
			// collides:
			//     sch-props-correct.2: A schema cannot contain two global
			//         components with the same name; ... {ns,web-app}
			// followed by the classic src-resolve: javaee:web-appType unresolved.
			//
			// So the order is:
			//   (1) literal / exact filename match first — include/import always
			//       give us the explicit schemaLocation here.
			//   (2) expanded (absolute) system id, as a file:// / classpath-URL.
			//   (3) classpath fallback by basename.
			//   (4) ONLY finally, namespace -> "synthetic top-level default"
			//       mapping, used purely for <import namespace="..." schemaLocation="">
			//       where Xerces asks for a resource with no location.
			//   (5) publicId last gasp.

			// (1) innerDTD lookup by literal filename / URL.  xsd:include gives
			//     us the raw schemaLocation here ("web-common_3_0.xsd").
			if (resolved == null && literalSystemId != null && !literalSystemId.isEmpty()) {
				resolved = resolveFromInnerDTD(literalSystemId);
				if (resolved == null) {
					String justName = stripToFileName(literalSystemId);
					if (!justName.equals(literalSystemId)) {
						resolved = resolveFromInnerDTD(justName);
					}
				}
			}

			// (2) Try the expanded (absolute-ised) system id as file or jar URL.
			if (resolved == null && expandedSystemId != null && !expandedSystemId.isEmpty()) {
				resolved = resolveUrl(expandedSystemId);
				if (resolved == null) {
					String justName = stripToFileName(expandedSystemId);
					resolved = resolveClasspath("/XSD/" + justName);
				}
			}

			// (3) Try classpath resource relative to this class using the
			//     /XSD/{filename} form (matches innerDTD paths).
			if (resolved == null && literalSystemId != null && !literalSystemId.isEmpty()) {
				resolved = resolveClasspath("/XSD/" + stripToFileName(literalSystemId));
			}

			// (4) innerDTD lookup by namespace key (e.g. "http://java.sun.com/xml/ns/javaee").
			//     Do NOT apply this step when we already had a concrete filename to
			//     resolve (literal or expanded basename ends in .xsd/.dtd) — in that
			//     case the include/import named a specific file we failed to find,
			//     and substituting a sibling schema under the same namespace would
			//     cause "duplicate global component" errors (see doc above).
			boolean hasConcreteLocation = false;
			if (literalSystemId != null && (literalSystemId.endsWith(".xsd") || literalSystemId.endsWith(".dtd"))) {
				hasConcreteLocation = true;
			}
			if (expandedSystemId != null && (expandedSystemId.endsWith(".xsd") || expandedSystemId.endsWith(".dtd"))) {
				hasConcreteLocation = true;
			}
			if (resolved == null && !hasConcreteLocation
					&& namespace != null && !namespace.isEmpty()) {
				resolved = resolveFromInnerDTD(namespace);
			}

			// (5) Last gasp: if publicId alone is a known innerDTD key, use it.
			if (resolved == null) {
				if (publicId != null && !publicId.isEmpty()) {
					resolved = resolveFromInnerDTD(publicId);
				}
			}

			if (resolved == null) {
				// DO NOT manufacture an empty InputStream.  That is exactly what
				// triggers Xerces' "Premature end of file" fatal error in
				// XMLVersionDetector.determineDocVersion.  Returning null makes
				// Xerces emit the schema_reference.4 warning and continue with
				// whatever partial grammar it has — a strictly better outcome.
				return null;
			}

			String sysId = literalSystemId != null ? literalSystemId : expandedSystemId;
			String baseId = toBaseSystemId(sysId);
			return new XMLInputSource(publicId, sysId, baseId, resolved, null);
		}

		private static InputStream resolveFromInnerDTD(String key) {
			try {
				Map<String, String> innerDTD = WebToolsPlugin.getInnerDTD();
				String path = innerDTD == null ? null : innerDTD.get(key);
				if (path != null) {
					InputStream in = XMLAssistProcessor.class.getResourceAsStream(path);
					if (in != null) return in;
				}
			} catch (Throwable ignore) {
				// If WebToolsPlugin innerDTD static init somehow fails (unit
				// tests without OSGi) do not propagate; fall through.
			}
			return null;
		}

		private static InputStream resolveClasspath(String path) {
			if (path == null) return null;
			try {
				return XMLAssistProcessor.class.getResourceAsStream(path);
			} catch (Throwable ignore) {
				return null;
			}
		}

		private static InputStream resolveUrl(String url) {
			if (url == null) return null;
			try {
				if (url.startsWith("file:")) {
					File f = new File(new URL(url).toURI());
					if (f.isFile()) return new java.io.FileInputStream(f);
				}
				if (url.indexOf(':') < 0) {
					File f = new File(url);
					if (f.isFile()) return new java.io.FileInputStream(f);
				}
			} catch (Throwable ignore) {
				// Keep silent for these fallback probes.  The caller will also
				// silently ignore null returns (see doc above).
			}
			return null;
		}

		private static String stripToFileName(String location) {
			if (location == null) return "";
			int slash = Math.max(location.lastIndexOf('/'), location.lastIndexOf('\\'));
			return slash < 0 ? location : location.substring(slash + 1);
		}
	}

	private void parseXSDElement(List<TagInfo> tagList, XSElementDeclaration element) {
		TagInfo tagInfo = new TagInfo(element.getName(), true);
		if (tagList.contains(tagInfo)) {
			return;
		}
		tagList.add(tagInfo);

		XSTypeDefinition type = element.getTypeDefinition();
		if (type instanceof XSComplexTypeDefinition) {
			XSParticle particle = ((XSComplexTypeDefinition) type).getParticle();
			if (particle != null) {
				XSTerm term = particle.getTerm();
				if (term instanceof XSElementDeclaration) {
					parseXSDElement(tagList, (XSElementDeclaration) term);
					tagInfo.addChildTagName(((XSElementDeclaration) term).getName());
				}
				if (term instanceof XSModelGroup) {
					parseXSModelGroup(tagInfo, tagList, (XSModelGroup) term);
				}
			}
			XSObjectList attrs = ((XSComplexTypeDefinition) type).getAttributeUses();
			for (int i = 0; i < attrs.getLength(); i++) {
				XSAttributeUse attrUse = (XSAttributeUse) attrs.item(i);
				XSAttributeDeclaration attr = attrUse.getAttrDeclaration();
				AttributeInfo attrInfo = new AttributeInfo(attr.getName(), true, AttributeInfo.NONE,
						attrUse.getRequired());
				tagInfo.addAttributeInfo(attrInfo);
			}

		}
	}

	private void parseXSModelGroup(TagInfo tagInfo, List<TagInfo> tagList, XSModelGroup term) {
		XSObjectList list = ((XSModelGroup) term).getParticles();
		for (int i = 0; i < list.getLength(); i++) {
			XSObject obj = list.item(i);

			if (obj instanceof XSParticle) {
				XSTerm term2 = ((XSParticle) obj).getTerm();

				if (term2 instanceof XSElementDeclaration) {
					parseXSDElement(tagList, (XSElementDeclaration) term2);
					tagInfo.addChildTagName(((XSElementDeclaration) term2).getName());
				}
				if (term2 instanceof XSModelGroup) {
					parseXSModelGroup(tagInfo, tagList, (XSModelGroup) term2);
				}
			}
		}
	}

	/**
	 * Sets a child tag name to <code>TagInfo</code>.
	 *
	 * @param tagInfo the <code>TagInfo</code>
	 * @param items   an array of <code>DTDItem</code>
	 */
	private void setChildTagName(TagInfo tagInfo, DTDItem[] items) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] instanceof DTDName) {
				DTDName dtdName = (DTDName) items[i];
				tagInfo.addChildTagName(dtdName.getValue());
			} else if (items[i] instanceof DTDChoice) {
				DTDChoice dtdChoise = (DTDChoice) items[i];
				setChildTagName(tagInfo, dtdChoise.getItem());
			}
		}
	}

	@Override
	protected boolean supportTagRelation() {
		return true;
	}

	/**
	 * Returns an array of an attribute value proposal to complete an attribute
	 * value.
	 *
	 * @param tagName the tag name
	 * @param value   the inputed value
	 * @param info    the attribute information
	 * @return the array of attribute value proposals
	 */
	@Override
	protected AssistInfo[] getAttributeValues(String tagName, String value, AttributeInfo info) {
		if (info.getAttributeType() == AttributeInfo.IDREF || info.getAttributeType() == AttributeInfo.IDREFS) {
			return super.getAttributeValues(tagName, value, info);
		}
		String[] values = info.getValues();
		if (values.length == 0) {
			return getClassAttributeValues(value, info.getAttributeName());
		}
		AssistInfo[] infos = new AssistInfo[values.length];
		for (int i = 0; i < infos.length; i++) {
			infos[i] = new AssistInfo(values[i]);
		}
		return infos;
	}

	/**
	 * Provides classname completion.
	 *
	 * @param value    the inputed value
	 * @param attrName the attribute name
	 * @return the array of attribute value proposals
	 */
	protected AssistInfo[] getClassAttributeValues(String value, String attrName) {
		if (input == null || editor == null || value.length() == 0) {
			return new AssistInfo[0];
		}
		String[] classNameAttributes = editor.getClassNameAttributes();
		for (int i = 0; i < classNameAttributes.length; i++) {
			if (attrName.equals(classNameAttributes[i])) {
				return classNameAssistant.getClassAttributeValues(input.getFile(), value);
			}
		}
		return new AssistInfo[0];
	}

	/**
	 * Returns the <code>List</code> of <code>TagInfo</code>.
	 *
	 * @return the <code>List</code> of <code>TagInfo</code>
	 */
	@Override
	protected List<TagInfo> getTagList() {
		List<TagInfo> list = new ArrayList<TagInfo>();
		list.addAll(this.tagList);
		// get namespace
		FuzzyXMLElement element = getOffsetElement();
		Map<String, String> nsPrefixMap = new HashMap<String, String>();
		getNamespace(nsPrefixMap, element);
		// add prefix to tag names
		Iterator<String> ite = this.nsTagListMap.keySet().iterator();
		while (ite.hasNext()) {
			String uri = ite.next();
			String prefix = nsPrefixMap.get(uri);
			if (prefix == null || prefix.equals("")) {
				list.addAll(this.nsTagListMap.get(uri));
			} else {
				for (TagInfo tagInfo : this.nsTagListMap.get(uri)) {
					list.add(createPrefixTagInfo(tagInfo, prefix));
				}
			}
		}
		return list;
	}

	/**
	 * Adds prefix to <code>TagInfo</code>.
	 *
	 * @param tagInfo the <code>TagInfo</code> instance
	 * @param prefix  the prefix to add
	 * @return the new <code>TagInfo</code> instance that is added the prefix
	 */
	private TagInfo createPrefixTagInfo(TagInfo tagInfo, String prefix) {
		TagInfo newTagInfo = new TagInfo(prefix + ":" + tagInfo.getTagName(), tagInfo.hasBody());
		AttributeInfo[] attrInfos = tagInfo.getAttributeInfo();
		for (int i = 0; i < attrInfos.length; i++) {
			AttributeInfo newAttrInfo = new AttributeInfo(prefix + ":" + attrInfos[i].getAttributeName(), true,
					AttributeInfo.NONE, attrInfos[i].isRequired());
			newTagInfo.addAttributeInfo(newAttrInfo);
		}
		String[] children = tagInfo.getChildTagNames();
		for (int i = 0; i < children.length; i++) {
			newTagInfo.addChildTagName(prefix + ":" + children[i]);
		}
		return newTagInfo;
	}

	/**
	 * Returns mapping of namespace and prefix at the calet position.
	 *
	 * @param map
	 *                <ul>
	 *                <li>key - namespace</li>
	 *                <li>value - prefix</li>
	 *                </ul>
	 * @param element the <code>FuzzyXMLElement</code> at the calet position
	 */
	private void getNamespace(Map<String, String> map, FuzzyXMLElement element) {
		FuzzyXMLAttribute[] attrs = element.getAttributes();
		for (int i = 0; i < attrs.length; i++) {
			if (attrs[i].getName().startsWith("xmlns")) {
				String name = attrs[i].getName();
				String prefix = "";
				if (name.indexOf(":") >= 0) {
					prefix = name.substring(name.indexOf(":") + 1);
				}
				if (map.get(attrs[i].getValue()) == null) {
					map.put(attrs[i].getValue(), prefix);
				}
			}
		}
		if (element.getParentNode() != null) {
			getNamespace(map, (FuzzyXMLElement) element.getParentNode());
		}
	}

	/**
	 * Returns the <code>TagInfo<code> which has the specified name.
	 *
	 * &#64;param name the tag name
	 * &#64;return the <code>TagInfo</code> which has the specified name, or
	 * <code>null</code>.
	 */
	@Override
	protected TagInfo getTagInfo(String name) {
		for (TagInfo info : getTagList()) {
			if (info.getTagName().equals(name)) {
				return info;
			}
		}
		return new XMLTagInfo(name);
	}

	/**
	 * The <code>TagInfo</code> object which will be returned when this processor
	 * has no definition for the specified tag.
	 */
	private class XMLTagInfo extends TagInfo {

		public XMLTagInfo(String tagName) {
			super(tagName, true);
		}

		@Override
		public AttributeInfo getAttributeInfo(String name) {
			AttributeInfo attrInfo = new AttributeInfo(name, true);
			return attrInfo;
		}
	}

}
