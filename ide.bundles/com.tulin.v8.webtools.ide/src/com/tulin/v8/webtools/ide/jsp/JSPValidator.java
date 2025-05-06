package com.tulin.v8.webtools.ide.jsp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;

import com.tulin.v8.fuzzyxml.FuzzyXMLAttribute;
import com.tulin.v8.fuzzyxml.FuzzyXMLDocument;
import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.fuzzyxml.FuzzyXMLNode;
import com.tulin.v8.fuzzyxml.XPath;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.assist.AttributeInfo;
import com.tulin.v8.webtools.ide.assist.TagInfo;
import com.tulin.v8.webtools.ide.html.HTMLUtil;
import com.tulin.v8.webtools.ide.html.editors.HTMLValidator;
import com.tulin.v8.webtools.ide.tag.ICustomTagValidator;
import com.tulin.v8.webtools.ide.tag.ICustomTagValidatorContributer;

/**
 * The JSP Validator that is called by <code>JSPSourceEditor</code>.
 * <p>
 * This validator validates the JSP document using FuzzyXML API, and reports
 * following errors.
 * <ul>
 * <li>TLDs are not extst</li>
 * <li>taglibs are not defined</li>
 * <li>required attributes of taglibs are not specified</li>
 * </ul>
 */
public class JSPValidator extends HTMLValidator implements IJSPValidationMarkerCreator {

	private JSPInfo info;

	/**
	 * The constructor.
	 *
	 * @param file
	 */
	public JSPValidator(IFile file) {
		super(file);
	}

	/**
	 * The constructor.
	 *
	 * @param file
	 * @param info
	 */
	public JSPValidator(IFile file, JSPInfo info) {
		super(file);
		this.info = info;
	}

	@Override
	protected void validateDocument(FuzzyXMLDocument doc) {
		if (doc != null) {
			if (this.info == null) {
				this.info = JSPInfo.getJSPInfo(getFile(), getContent());
			}
			validateElement((FuzzyXMLElement) XPath.selectSingleNode(doc.getDocumentElement(), "*"));
		}
	}

	@Override
	protected boolean validateUsingFuzzyXML() {
		return true;
	}

	@Override
	protected boolean validateUsingTidy() {
		return false;
	}

	private void validateElement(FuzzyXMLElement element) {
		if (element == null) {
			return;
		}
		if (element.getName().indexOf(":") > 0) {
			String[] dim = element.getName().split(":");
			if (dim.length == 1) {
				// I can't judge that this case is marked an error...
			} else {
				String prefix = dim[0];
//				String tagName = dim[1];
				String uri = info.getTaglibUri(prefix);
				ICustomTagValidator validator = null;
				if (uri != null) {
					ICustomTagValidatorContributer contributer = WebToolsPlugin.getDefault()
							.getCustomTagValidatorContributer(uri);
					if (contributer != null) {
						validator = contributer.getConverter(dim[1]);
					}

					if (validator != null) {
						Map<String, String> attrMap = new HashMap<String, String>();
						FuzzyXMLAttribute[] attrs = element.getAttributes();
						for (int i = 0; i < attrs.length; i++) {
							attrMap.put(attrs[i].getName(), attrs[i].getValue());
						}
						validator.validate(this, attrMap, element, info);
						return;
					}
				}
				if (!prefix.equals("jsp")) {
					TLDInfo tld = getTLDInfo(prefix);
					if (tld == null) {
						// tld does not exist.
						addElementMarker(element, "Validation.NoTLD", prefix);
					} else if (tld.getUri() != null || tld.getTagdir() != null) {
						TagInfo tagInfo = getTagInfo(tld, element.getName());
						if (tagInfo == null) {
							// taglib does not exist.
							addElementMarker(element, "Validation.NoCustomTag", element.getName());
						} else {
							AttributeInfo[] attrs = tagInfo.getAttributeInfo();
							for (int i = 0; i < attrs.length; i++) {
								if (attrs[i].isRequired() && !element.hasAttribute(attrs[i].getAttributeName())) {
									// check jsp:attribute
									boolean findAttribute = false;
									FuzzyXMLNode[] nodes = element.getChildren();
									for (int j = 0; j < nodes.length; j++) {
										if (nodes[j] instanceof FuzzyXMLElement
												&& ((FuzzyXMLElement) nodes[j]).getName().equals("jsp:attribute")) {
											FuzzyXMLAttribute attr = ((FuzzyXMLElement) nodes[j])
													.getAttributeNode("name");
											if (attr != null && attr.getValue().equals(attrs[i].getAttributeName())) {
												findAttribute = true;
												break;
											}
										}
									}
									// required attributes wasn't specified.
									if (!findAttribute) {
										addElementMarker(element, "Validation.RequiredAttr",
												attrs[i].getAttributeName());
									}
								}
							}
						}
					}
				}
			}
		}

		FuzzyXMLNode[] nodes = element.getChildren();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] instanceof FuzzyXMLElement) {
				validateElement((FuzzyXMLElement) nodes[i]);
			}
		}
	}

	private TLDInfo getTLDInfo(String prefix) {
		TLDInfo[] tlds = info.getTLDInfo();
		for (int i = 0; i < tlds.length; i++) {
			if (tlds[i].getPrefix().equals(prefix)) {
				return tlds[i];
			}
		}
		return null;
	}

	private TagInfo getTagInfo(TLDInfo tld, String tagName) {
		List<TagInfo> list = tld.getTagInfo();
		for (int j = 0; j < list.size(); j++) {
			TagInfo tagInfo = (TagInfo) list.get(j);
			if (tagInfo.getTagName().equals(tagName)) {
				return tagInfo;
			}
		}
		return null;
	}

	private void addElementMarker(FuzzyXMLElement element, String key, String value) {
		// create message
		String message = WebToolsPlugin.createMessage(WebToolsPlugin.getResourceString(key), new String[] { value });

		int offset = element.getOffset() + 1;
		HTMLUtil.addMarker(getFile(), IMarker.SEVERITY_ERROR, getLineAtOffset(offset), offset,
				element.getName().length(), message);
	}

	public void addMarker(int severity, int offset, int length, String message) {
		HTMLUtil.addMarker(getFile(), severity, getLineAtOffset(offset), offset, length, message);
	}

	/*
	 * Call any contributed JSP filter extensions
	 * 
	 * @since 2.0.5
	 */
	@Override
	protected String filterContents(String jspContents, IFile file) {
		IJSPFilter[] jspFilters = WebToolsPlugin.getDefault().getJSPFilters();
		for (int i = 0; i < jspFilters.length; i++) {
			jspContents = jspFilters[i].filterJSP(jspContents, file);
		}
		return jspContents;
	}
}
