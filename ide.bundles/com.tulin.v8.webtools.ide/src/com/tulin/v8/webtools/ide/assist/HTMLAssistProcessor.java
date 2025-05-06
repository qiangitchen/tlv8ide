package com.tulin.v8.webtools.ide.assist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ContextInformationValidator;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

import com.tulin.v8.fuzzyxml.FuzzyXMLAttribute;
import com.tulin.v8.fuzzyxml.FuzzyXMLDocument;
import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.fuzzyxml.FuzzyXMLNode;
import com.tulin.v8.fuzzyxml.FuzzyXMLParser;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.css.CSSDefinition;
import com.tulin.v8.webtools.ide.css.CSSInfo;
import com.tulin.v8.webtools.ide.css.CSSValue;
import com.tulin.v8.webtools.ide.html.HTMLUtil;
import com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditor;

/**
 * An implementation of <code>IContentAssistProcessor</code>. This processor
 * provides code-completion for the <code>HTMLSourceEditor</code>.
 */
public class HTMLAssistProcessor extends HTMLTemplateAssistProcessor { /* implements IContentAssistProcessor { */

	private boolean xhtmlMode = false;
	private char[] chars = {};
	private Image tagImage;
	private Image attrImage;
	private Image valueImage;
	private boolean assistCloseTag = true;
	private List<CustomAttribute> customAttrs = CustomAttribute.loadFromPreference(false);
	private List<CustomElement> customElems = CustomElement.loadFromPreference(false);
	private Set<String> customElemNames = new HashSet<String>();
	protected CSSAssistProcessor cssAssist = new CSSAssistProcessor();
	protected IFileAssistProcessor[] fileAssistProcessors;

	private IFile file;
	private int offset;
	private FuzzyXMLDocument doc;

	/**
	 * The constructor.
	 */
	public HTMLAssistProcessor() {
		tagImage = WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_TAG);
		attrImage = WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_ATTR);
		valueImage = WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_VALUE);
		fileAssistProcessors = WebToolsPlugin.getDefault().getFileAssistProcessors();

		for (int i = 0; i < customElems.size(); i++) {
			customElemNames.add(((CustomElement) customElems.get(i)).getDisplayName());
		}
	}

	public boolean enableTemplate() {
		return true;
	}

	public void setXHTMLMode(boolean xhtmlMode) {
		this.xhtmlMode = xhtmlMode;
	}

	public void setAutoAssistChars(char[] chars) {
		if (chars != null) {
			this.chars = chars;
		}
	}

	public void setAssistCloseTag(boolean assistCloseTag) {
		this.assistCloseTag = assistCloseTag;
	}

	/**
	 * Returns an array of attribute value proposals.
	 *
	 * @param tagName the tag name
	 * @param value   the attribute value
	 * @param info    the attribute information
	 * @return the array of attribute value proposals
	 */
	protected AssistInfo[] getAttributeValues(String tagName, String value, AttributeInfo info) {
		// CSS
		if (info.getAttributeType() == AttributeInfo.CSS) {
			return cssAssist.getAssistInfo(tagName, value);
		}
		// href="#..."
		if (info.getAttributeName().equalsIgnoreCase("href") && value.startsWith("#")) {
			List<AssistInfo> list = new ArrayList<AssistInfo>();
			String[] anchors = getAnchors(getDocument());
			for (int i = 0; i < anchors.length; i++) {
				list.add(new AssistInfo("#" + anchors[i]));
			}
			return list.toArray(new AssistInfo[list.size()]);
		}
		// href="file.html#..."
		if (info.getAttributeName().equalsIgnoreCase("href") && value.indexOf("#") > 0) {
			List<AssistInfo> list = new ArrayList<AssistInfo>();
			String fileName = value.substring(0, value.indexOf("#"));
			if (this.file != null) {
				IFile targetFile = this.file.getParent().getFile(new Path(fileName));
				if (targetFile != null && targetFile.exists()) {
					try {
						FuzzyXMLDocument doc = new FuzzyXMLParser().parse(targetFile.getContents());
						String[] anchors = getAnchors(doc);
						for (int i = 0; i < anchors.length; i++) {
							list.add(new AssistInfo(fileName + "#" + anchors[i], "#" + anchors[i]));
						}
					} catch (Exception ex) {
					}
				}
			}
			return list.toArray(new AssistInfo[list.size()]);
		}
		// FILE
		if (info.getAttributeType() == AttributeInfo.FILE) {
			List<AssistInfo> list = new ArrayList<AssistInfo>();
			for (int i = 0; i < fileAssistProcessors.length; i++) {
				AssistInfo[] assists = fileAssistProcessors[i].getAssistInfo(value);
				for (int j = 0; j < assists.length; j++) {
					list.add(assists[j]);
				}
			}
			return list.toArray(new AssistInfo[list.size()]);
		}
		// IDREF
		if (info.getAttributeType() == AttributeInfo.IDREF) {
			List<AssistInfo> list = new ArrayList<AssistInfo>();
			String[] ids = getIDs();
			for (int i = 0; i < ids.length; i++) {
				list.add(new AssistInfo(ids[i]));
			}
			return list.toArray(new AssistInfo[list.size()]);
		}
		// IDREFS
		if (info.getAttributeType() == AttributeInfo.IDREFS) {
			List<AssistInfo> list = new ArrayList<AssistInfo>();
			String[] ids = getIDs();
			String prefix = value;
			if (prefix.length() != 0 && !prefix.endsWith(" ")) {
				prefix = prefix + " ";
			}
			for (int i = 0; i < ids.length; i++) {
				list.add(new AssistInfo(prefix + ids[i], ids[i]));
			}
			return list.toArray(new AssistInfo[list.size()]);
		}
		// ETC
		String[] values = AttributeValueDefinition.getAttributeValues(info.getAttributeType());
		AssistInfo[] infos = new AssistInfo[values.length];
		for (int i = 0; i < infos.length; i++) {
			infos[i] = new AssistInfo(values[i]);
		}
		return infos;
	}

	protected String[] getAnchors(FuzzyXMLDocument doc) {
		List<String> list = new ArrayList<String>();
		if (doc != null) {
			FuzzyXMLElement element = doc.getDocumentElement();
			extractAnchor(element, list);
		}
		return list.toArray(new String[list.size()]);
	}

	private void extractAnchor(FuzzyXMLElement element, List<String> list) {
		if (element.getName().equalsIgnoreCase("a")) {
			FuzzyXMLAttribute[] attrs = element.getAttributes();
			for (int i = 0; i < attrs.length; i++) {
				if (attrs[i].getName().equalsIgnoreCase("name")) {
					list.add(attrs[i].getValue());
				}
			}
		}
		FuzzyXMLNode[] nodes = element.getChildren();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] instanceof FuzzyXMLElement) {
				extractAnchor((FuzzyXMLElement) nodes[i], list);
			}
		}
	}

	/**
	 * Returns ID attribute values.
	 *
	 * @return the array which contans ID attribute values
	 */
	protected String[] getIDs() {
		FuzzyXMLDocument doc = getDocument();
		List<String> list = new ArrayList<String>();
		if (doc != null) {
			FuzzyXMLElement element = doc.getDocumentElement();
			extractID(element, list);
		}
		return list.toArray(new String[list.size()]);
	}

	private void extractID(FuzzyXMLElement element, List<String> list) {
		FuzzyXMLAttribute[] attrs = element.getAttributes();
		for (int i = 0; i < attrs.length; i++) {
			TagInfo tagInfo = getTagInfo(element.getName());
			if (tagInfo != null) {
				AttributeInfo attrInfo = tagInfo.getAttributeInfo(attrs[i].getName());
				if (attrInfo != null) {
					if (attrInfo.getAttributeType() == AttributeInfo.ID) {
						list.add(attrs[i].getValue());
					}
				}
			}
		}
		FuzzyXMLNode[] nodes = element.getChildren();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] instanceof FuzzyXMLElement) {
				extractID((FuzzyXMLElement) nodes[i], list);
			}
		}
	}

	/**
	 * Returns the <code>List</code> which contains all <code>TagInfo</code>s.
	 *
	 * @return the <code>List</code> which contains all <code>TagInfo</code>s
	 */
	protected List<TagInfo> getTagList() {
		return TagDefinition.getTagInfoAsList();
	}

	/**
	 * Returns the <code>TagInfo</code> which has the specified name.
	 *
	 * @param name a tag name
	 * @return the <code>TagInfo</code>
	 */
	protected TagInfo getTagInfo(String name) {
		List<TagInfo> tagList = TagDefinition.getTagInfoAsList();
		for (int i = 0; i < tagList.size(); i++) {
			TagInfo info = (TagInfo) tagList.get(i);
			if (info.getTagName().equals(name)) {
				return info;
			}
		}

		return null;
	}

	/**
	 * Returns the <code>FuzzyXMLElement</code> by the offset.
	 *
	 * @return the <code>FuzzyXMLElement</code>
	 */
	protected FuzzyXMLElement getOffsetElement() {
		return doc.getElementByOffset(offset);
	}

	/**
	 * Returns the <code>FuzzyXMLDocument</code>.
	 *
	 * @return the <code>FuzzyXMLDocument</code>
	 */
	protected FuzzyXMLDocument getDocument() {
		return doc;
	}

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int documentOffset) {

		String text = viewer.getDocument().get().substring(0, documentOffset);
		String[] dim = getLastWord(text);
		String word = dim[0].toLowerCase();
		String prev = dim[1].toLowerCase();
		String last = dim[2];
		String attr = dim[3];

		this.offset = documentOffset;
		this.doc = new FuzzyXMLParser().parse(viewer.getDocument().get());

		List<ICompletionProposal> list = new ArrayList<ICompletionProposal>();
		List<TagInfo> tagList = getTagList();

		// attribute value
		if ((word.startsWith("\"") && (word.length() == 1 || !word.endsWith("\"")))
				|| (word.startsWith("'") && (word.length() == 1 || !word.endsWith("\'")))) {
			String value = dim[0].substring(1);
			TagInfo tagInfo = getTagInfo(last.toLowerCase());
			if (tagInfo != null) {
				AttributeInfo attrInfo = tagInfo.getAttributeInfo(attr);
				if (attrInfo != null) {
					AssistInfo[] keywords = getAttributeValues(last, dim[0].substring(1), attrInfo);
					for (int i = 0; i < keywords.length; i++) {
						if (keywords[i].getReplaceString().toLowerCase().startsWith(value.toLowerCase())) {
//							list.add(new CompletionProposal(
//									keywords[i].getReplaceString(),
//									documentOffset - value.length(), value.length(),
//									keywords[i].getReplaceString().length(),
//									keywords[i].getImage()==null ? valueImage : keywords[i].getImage(),
//									keywords[i].getDisplayString(), null, null));
							list.add(keywords[i].toCompletionProposal(documentOffset, value, valueImage));
						}
					}
				}
			}
			// tag
		} else if (word.startsWith("<") && !word.equals("</")) {
			if (supportTagRelation()) {
				TagInfo parent = getTagInfo(last);
				tagList = new ArrayList<TagInfo>();
				if (parent != null) {
					String[] childNames = parent.getChildTagNames();
					for (int i = 0; i < childNames.length; i++) {
						tagList.add(getTagInfo(childNames[i]));
					}
				}
			}
			for (int i = 0; i < tagList.size(); i++) {
				TagInfo tagInfo = (TagInfo) tagList.get(i);
				if (tagInfo instanceof TextInfo) {
					TextInfo textInfo = (TextInfo) tagInfo;
					if ((textInfo.getText().toLowerCase()).indexOf(word) == 0) {
						list.add(new CompletionProposal(textInfo.getText(), documentOffset - word.length(),
								word.length(), textInfo.getPosition(), tagImage, textInfo.getDisplayString(), null,
								tagInfo.getDescription()));
					}
					continue;
				}
				String tagName = tagInfo.getTagName();
				if (("<" + tagInfo.getTagName().toLowerCase()).indexOf(word) == 0) {
					String assistKeyword = tagName;
					int position = 0;
					// required attributes
					AttributeInfo[] requierAttrs = tagInfo.getRequiredAttributeInfo();
					for (int j = 0; j < requierAttrs.length; j++) {
						assistKeyword = assistKeyword + " " + requierAttrs[j].getAttributeName();
						if (requierAttrs[j].hasValue()) {
							assistKeyword = assistKeyword + "=\"\"";
							if (j == 0) {
								position = tagName.length() + requierAttrs[j].getAttributeName().length() + 3;
							}
						}
					}
					if (assistKeyword.equalsIgnoreCase("script")) {
						assistKeyword = assistKeyword + " type=\"text/javascript\"";
						position = tagName.length() + 23;
					}
					if (assistKeyword.equalsIgnoreCase("style")) {
						assistKeyword = assistKeyword + " type=\"text/css\"";
						position = tagName.length() + 17;
					}
					if (tagInfo.hasBody()) {
						assistKeyword = assistKeyword + ">";
						if (assistCloseTag) {
							if (position == 0) {
								position = assistKeyword.length();
							}
							assistKeyword = assistKeyword + "</" + tagName + ">";
						}
					} else {
						if (tagInfo.isEmptyTag() && xhtmlMode == false) {
							assistKeyword = assistKeyword + ">";
						} else {
							assistKeyword = assistKeyword + "/>";
						}
					}
					if (position == 0) {
						position = assistKeyword.length();
					}
					try {
						list.add(new CompletionProposal(assistKeyword, documentOffset - word.length() + 1,
								word.length() - 1, position, tagImage, tagInfo.getDisplayString(), null,
								tagInfo.getDescription()));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			// custom elements
			for (int i = 0; i < customElems.size(); i++) {
				CustomElement element = (CustomElement) customElems.get(i);
				if ((element.getAssistString().toLowerCase()).indexOf(word) == 0) {
					int position = element.getAssistString().indexOf('"');
					if (position == -1) {
						position = element.getAssistString().indexOf("><");
					}
					if (position == -1) {
						position = element.getAssistString().length();
					}
					list.add(new CompletionProposal(element.getAssistString(), documentOffset - word.length(),
							word.length(), position + 1, tagImage, element.getDisplayName(), null, null));
				}
			}
			// attribute
		} else if (!prev.equals("")) {
			String tagName = prev;
			TagInfo tagInfo = getTagInfo(tagName);
			if (tagInfo != null) {
				AttributeInfo[] attrList = tagInfo.getAttributeInfo();
				for (int j = 0; j < attrList.length; j++) {
					if (attrList[j].getAttributeName().toLowerCase().indexOf(word) == 0) {
						String assistKeyword = null;
						int position = 0;
						if (attrList[j].hasValue()) {
							assistKeyword = attrList[j].getAttributeName() + "=\"\"";
							position = 2;
						} else {
							assistKeyword = attrList[j].getAttributeName();
							position = 0;
						}
						list.add(new CompletionProposal(assistKeyword, documentOffset - word.length(), word.length(),
								attrList[j].getAttributeName().length() + position, attrImage,
								attrList[j].getAttributeName(), null, attrList[j].getDescription()));
					}
				}
			}
			// custom attributes
			for (int i = 0; i < customAttrs.size(); i++) {
				CustomAttribute attrInfo = (CustomAttribute) customAttrs.get(i);
				if (attrInfo.getTargetTag().equals("*") || attrInfo.getTargetTag().equals(tagName)) {
					if (tagName.indexOf(":") < 0 || customElemNames.contains(tagName)) {
						list.add(new CompletionProposal(attrInfo.getAttributeName() + "=\"\"",
								documentOffset - word.length(), word.length(), attrInfo.getAttributeName().length() + 2,
								attrImage, attrInfo.getAttributeName(), null, null));
					}
				}
			}
			// close tag
		} else if (!last.equals("")) {
			TagInfo info = getTagInfo(last);
			if (info == null || xhtmlMode == true || info.hasBody() || !info.isEmptyTag()) {
				String assistKeyword = "</" + last + ">";
				int length = 0;
				if (word.equals("</")) {
					length = 2;
				}
				list.add(new CompletionProposal(assistKeyword, documentOffset - length, length, assistKeyword.length(),
						tagImage, assistKeyword, null, null));
			}
		}

		// STYLE Attribute
		if ("style".equalsIgnoreCase(attr)) {
			// attribute value
			if ((word.startsWith("\"") && (word.length() == 1 || !word.endsWith("\"")))
					|| (word.startsWith("'") && (word.length() == 1 || !word.endsWith("\'")))) {
				String sword = word.substring(1);
				if (sword.indexOf(";") > 0) {
					sword = sword.substring(sword.lastIndexOf(";") + 1);
				}
				sword = sword.trim();
				if (sword.indexOf(":") > 0) {
					String prop = sword.substring(0, sword.indexOf(":"));
					String vals = sword.substring(sword.indexOf(":") + 1).trim();
					for (int i = 0; i < CSSDefinition.CSS_VALUES.length; i++) {
						CSSValue cssValue = CSSDefinition.CSS_VALUES[i];
						if (cssValue.getName().startsWith(prop)) {
							List<CSSInfo> values = cssValue.getValues();
							for (CSSInfo value : values) {
								if (value.getReplaceString().startsWith(vals)) {
									list.add(new CompletionProposal(value.getReplaceString() + ";",
											offset - vals.length(), vals.length(),
											value.getReplaceString().length() + 1,
											WebToolsPlugin.getDefault().getImageRegistry()
													.get(WebToolsPlugin.ICON_CSS_PROP),
											value.getDisplayString(), null, value.getDescription()));
								}
							}
						}
					}
				} else {
					for (int i = 0; i < CSSDefinition.CSS_KEYWORDS.length; i++) {
						CSSInfo cssInfo = CSSDefinition.CSS_KEYWORDS[i];
						if (cssInfo.getReplaceString().startsWith(sword)) {
							String replaceString = cssInfo.getReplaceString();
							if (replaceString.indexOf(":") < 0) {
								replaceString += ": ";
							}
							list.add(new CompletionProposal(replaceString, offset - sword.length(), sword.length(),
									replaceString.length(),
									WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_CSS_PROP),
									cssInfo.getDisplayString(), null, cssInfo.getDescription()));
						}
					}
				}
			}
		}

		HTMLUtil.sortCompilationProposal(list);

		if (enableTemplate()) {
			ICompletionProposal[] templates = super.computeCompletionProposals(viewer, documentOffset);
			for (int i = 0; i < templates.length; i++) {
				list.add(templates[i]);
			}
		}

		ICompletionProposal[] prop = list.toArray(new ICompletionProposal[list.size()]);
		return prop;
	}

	/**
	 * Returns true if this processor support parent and child relation. In the
	 * default, this method returns false.
	 */
	protected boolean supportTagRelation() {
		return false;
	}

	/**
	 * Returns same informations for code completion from calet position.
	 *
	 * @return
	 *         <ul>
	 *         <li>0 - last word from calet position (if it's tag, it contains
	 *         &lt;)</li>
	 *         <li>1 - target of attribute completion (only tag name, not contains
	 *         &lt;)</li>
	 *         <li>2 - target of close tag completion (only tag name, not contains
	 *         &lt;)</li>
	 *         <li>3 - previous attribute name</li>
	 *         </ul>
	 */
	protected String[] getLastWord(String text) {

		// TODO It's dirty...
		StringBuffer sb = new StringBuffer();
		Stack<String> stack = new Stack<String>();
		String word = "";
		String prevTag = "";
		String lastTag = "";
		String attr = "";
		String temp1 = ""; // temporary
		String temp2 = ""; // temporary
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			// skip scriptlet
			if (c == '<' && text.length() > i + 1 && text.charAt(i + 1) == '%') {
				i = text.indexOf("%>", i + 2);
				if (i == -1) {
					i = text.length();
				}
				continue;
			}
			// skip XML declaration
			if (c == '<' && text.length() > i + 1 && text.charAt(i + 1) == '?') {
				i = text.indexOf("?>", i + 2);
				if (i == -1) {
					i = text.length();
				}
				continue;
			}

			if (isDelimiter(c)) {
				temp1 = sb.toString();

				// skip whitespaces in the attribute value
				if (temp1.length() > 1 && ((temp1.startsWith("\"") && !temp1.endsWith("\"") && c != '"')
						|| (temp1.startsWith("'") && !temp1.endsWith("'") && c != '\''))) {
					sb.append(c);
					continue;
				}
				if (temp1.length() == 1 && ((temp1.equals("\"") || (temp1.equals("'"))))) {
					sb.append(c);
					continue;
				}

				if (!temp1.equals("")) {
					temp2 = temp1;
					if (temp2.endsWith("=") && !prevTag.equals("") && !temp2.equals("=")) {
						attr = temp2.substring(0, temp2.length() - 1);
					}
				}
				if (temp1.startsWith("<") && !temp1.startsWith("</") && !temp1.startsWith("<!")) {
					prevTag = temp1.substring(1);
					if (!temp1.endsWith("/")) {
						stack.push(prevTag);
					}
				} else if (temp1.startsWith("</") && stack.size() != 0) {
					stack.pop();
				} else if ((!temp1.startsWith("\"") && !temp1.startsWith("'")) && temp1.endsWith("/")
						&& stack.size() != 0) {
					stack.pop();
				}
				sb.setLength(0);

				if (c == '<') {
					sb.append(c);
				} else if (c == '"' || c == '\'') {
					if (temp1.startsWith("\"") || temp1.startsWith("'")) {
						sb.append(temp1);
					}
					sb.append(c);
				} else if (c == '>') {
					prevTag = "";
					attr = "";
				}
			} else {
				if (c == '=' && !prevTag.equals("")) {
					attr = temp2.trim();
				}
				temp1 = sb.toString();
				if (temp1.length() > 1 && (temp1.startsWith("\"") && temp1.endsWith("\""))
						|| (temp1.startsWith("'") && temp1.endsWith("'"))) {
					sb.setLength(0);
				}
				sb.append(c);
			}
		}

		if (stack.size() != 0) {
			lastTag = (String) stack.pop();
		}
		// Hmm... it's not perfect...
		if (attr.endsWith("=")) {
			attr = attr.substring(0, attr.length() - 1);
		}
		word = sb.toString();

		return new String[] { word, prevTag, lastTag, attr };
	}

	/**
	 * Tests a character is delimiter or not delimiter.
	 */
	protected boolean isDelimiter(char c) {
		if (c == ' ' || c == '(' || c == ')' || c == ',' // || c == '.'
				|| c == ';' || c == '\n' || c == '\r' || c == '\t' || c == '+' || c == '>' || c == '<' || c == '*'
				|| c == '^' // || c == '{'
				// || c == '}'
				|| c == '[' || c == ']' || c == '"' || c == '\'') {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int documentOffset) {
		ContextInformation[] info = new ContextInformation[0];
		return info;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return chars;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return chars;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return new ContextInformationValidator(this);
	}

	@Override
	public String getErrorMessage() {
		return "Error";
	}

	/**
	 * Updates internal informations.
	 *
	 * @param editor the <code>HTMLSourceEditor</code> instance
	 * @param source editing source code
	 */
	public void update(HTMLSourceEditor editor, String source) {
		IEditorInput editorInput = editor.getEditorInput();
		if (editorInput instanceof IFileEditorInput) {
			IFileEditorInput input = (IFileEditorInput) editorInput;
			this.file = input.getFile();
			cssAssist.reload(input.getFile(), source);
			customAttrs = CustomAttribute.loadFromPreference(false);
			customElems = CustomElement.loadFromPreference(false);

			customElemNames.clear();
			for (int i = 0; i < customElems.size(); i++) {
				customElemNames.add(((CustomElement) customElems.get(i)).getDisplayName());
			}

			for (int i = 0; i < fileAssistProcessors.length; i++) {
				fileAssistProcessors[i].reload(input.getFile());
			}
		}
	}

}