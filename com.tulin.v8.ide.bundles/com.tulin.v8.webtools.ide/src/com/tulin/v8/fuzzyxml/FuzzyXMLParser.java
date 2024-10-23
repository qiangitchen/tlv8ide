package com.tulin.v8.fuzzyxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tulin.v8.fuzzyxml.event.FuzzyXMLErrorEvent;
import com.tulin.v8.fuzzyxml.event.FuzzyXMLErrorListener;
import com.tulin.v8.fuzzyxml.internal.AbstractFuzzyXMLNode;
import com.tulin.v8.fuzzyxml.internal.FuzzyXMLAttributeImpl;
import com.tulin.v8.fuzzyxml.internal.FuzzyXMLCDATAImpl;
import com.tulin.v8.fuzzyxml.internal.FuzzyXMLCommentImpl;
import com.tulin.v8.fuzzyxml.internal.FuzzyXMLDocTypeImpl;
import com.tulin.v8.fuzzyxml.internal.FuzzyXMLDocumentImpl;
import com.tulin.v8.fuzzyxml.internal.FuzzyXMLElementImpl;
import com.tulin.v8.fuzzyxml.internal.FuzzyXMLProcessingInstructionImpl;
import com.tulin.v8.fuzzyxml.internal.FuzzyXMLTextImpl;
import com.tulin.v8.fuzzyxml.internal.FuzzyXMLUtil;
import com.tulin.v8.fuzzyxml.resources.Messages;

public class FuzzyXMLParser {
	private Stack<FuzzyXMLElement> stack = new Stack<>();
	private String originalSource;
	private FuzzyXMLElementImpl root;
	private FuzzyXMLDocType docType;

	private List<FuzzyXMLErrorListener> listeners = new ArrayList<>();
	private List<FuzzyXMLElement> nonCloseElement = new ArrayList<>();

	private boolean isHTML = false;

	private Pattern tag = Pattern.compile("<((|/)([^<>]*))([^<]|>)");
//	private Pattern attr = Pattern.compile("([\\w:]+?)\\s*=(\"|')([^\"]*?)\\2");
	private Pattern docTypeName = Pattern.compile("^<!DOCTYPE[ \r\n\t]+([\\w\\-_]*)");
	private Pattern docTypePublic = Pattern.compile("PUBLIC[ \r\n\t]+\"(.*?)\"[ \r\n\t]+\"(.*?)\"");
	private Pattern docTypeSystem = Pattern.compile("SYSTEM[ \r\n\t]+\"(.*?)\"");
	private Pattern docTypeSubset = Pattern.compile("\\[([^\\]]*)\\]>");

	public FuzzyXMLParser() {
		this(false);
	}

	public FuzzyXMLParser(boolean isHTML) {
		super();
		this.isHTML = isHTML;
	}

	public void addErrorListener(FuzzyXMLErrorListener listener) {
		this.listeners.add(listener);
	}

	private void fireErrorEvent(int offset, int length, String message, FuzzyXMLNode node) {
		FuzzyXMLErrorEvent evt = new FuzzyXMLErrorEvent(offset, length, message, node);
		for (int i = 0; i < listeners.size(); i++) {
			FuzzyXMLErrorListener listener = (FuzzyXMLErrorListener) listeners.get(i);
			listener.error(evt);
		}
	}

	public FuzzyXMLDocument parse(InputStream in) throws IOException {
		byte[] bytes = FuzzyXMLUtil.readStream(in);
		String encode = FuzzyXMLUtil.getEncoding(bytes);
		if (encode == null) {
			return parse(new String(bytes));
		} else {
			return parse(new String(bytes, encode));
		}

	}

	public FuzzyXMLDocument parse(File file) throws IOException {
		byte[] bytes = FuzzyXMLUtil.readStream(new FileInputStream(file));
		String encode = FuzzyXMLUtil.getEncoding(bytes);
		if (encode == null) {
			return parse(new String(bytes));
		} else {
			return parse(new String(bytes, encode));
		}
	}

	public FuzzyXMLDocument parse(String source) {
		originalSource = source;
		source = FuzzyXMLUtil.comment2space(source, true);
		source = FuzzyXMLUtil.escapeScript(source);
		source = FuzzyXMLUtil.scriptlet2space(source, true);
		source = FuzzyXMLUtil.cdata2space(source, true);
		source = FuzzyXMLUtil.doctype2space(source, true);
		source = FuzzyXMLUtil.processing2space(source, true);
		source = FuzzyXMLUtil.escapeString(source);

		Matcher matcher = tag.matcher(source);
		int lastIndex = -1;
		while (matcher.find()) {
			if (lastIndex != -1 && lastIndex < matcher.start()) {
				handleText(lastIndex, matcher.start(), true);
			}
			String text = matcher.group(1).trim();
			if (text.startsWith("%")) {
				// ignore
				handleText(matcher.start(), matcher.end(), false);
			} else if (text.startsWith("?")) {
				handleDeclaration(matcher.start(), matcher.end());
			} else if (text.startsWith("!DOCTYPE") || text.startsWith("!doctype")) {
				handleDoctype(matcher.start(), matcher.end(), text);
			} else if (text.startsWith("![CDATA[")) {
				handleCDATA(matcher.start(), matcher.end(), originalSource.substring(matcher.start(), matcher.end()));
			} else if (text.startsWith("/")) {
				handleCloseTag(matcher.start(), matcher.end(), text);
			} else if (text.startsWith("!--")) {
				handleComment(matcher.start(), matcher.end(), originalSource.substring(matcher.start(), matcher.end()));
			} else if (text.endsWith("/")) {
				handleEmptyTag(matcher.start(), matcher.end());
			} else {
				handleStartTag(matcher.start(), matcher.end());
			}
			lastIndex = matcher.end();
		}
		FuzzyXMLElement docElement = null;
		if (root == null) {
			docElement = new FuzzyXMLElementImpl(null, "document", 0, originalSource.length());
			// docElement.appendChild(root);
		} else {
			docElement = new FuzzyXMLElementImpl(null, "document", root.getOffset(), root.getLength());
			((FuzzyXMLElementImpl) docElement).appendChildWithNoCheck(root);
		}
		FuzzyXMLDocumentImpl doc = new FuzzyXMLDocumentImpl(docElement, docType);
		doc.setHTML(this.isHTML);
		return doc;
	}

	private void handleCDATA(int offset, int end, String text) {
		if (getParent() != null) {
			text = text.replaceFirst("<!\\[CDATA\\[", "");
			text = text.replaceFirst("\\]\\]>", "");
			FuzzyXMLCDATAImpl cdata = new FuzzyXMLCDATAImpl(getParent(), text, offset, end - offset);
			((FuzzyXMLElement) getParent()).appendChild(cdata);
		}
	}

	private void handleText(int offset, int end, boolean escape) {
		String text = originalSource.substring(offset, end);
		if (getParent() != null) {
			FuzzyXMLTextImpl textNode = new FuzzyXMLTextImpl(getParent(), FuzzyXMLUtil.decode(text, isHTML), offset,
					end - offset);
			textNode.setEscape(escape);
			((FuzzyXMLElement) getParent()).appendChild(textNode);
		}
	}

	private void handleDeclaration(int offset, int end) {
		if (getParent() != null) {
			String text = originalSource.substring(offset, end);
			text = text.replaceFirst("^<\\?", "");
			text = text.replaceFirst("\\?>$", "");
			text = text.trim();

			String[] dim = text.split("[ \r\n\t]+");
			String name = dim[0];
			String data = text.substring(name.length()).trim();

			FuzzyXMLProcessingInstructionImpl pi = new FuzzyXMLProcessingInstructionImpl(null, name, data, offset,
					end - offset);
			((FuzzyXMLElement) getParent()).appendChild(pi);
		}
	}

	private void handleDoctype(int offset, int end, String text) {
		if (docType == null) {
			String name = "";
			String publicId = "";
			String systemId = "";
			String internalSubset = "";

			text = originalSource.substring(offset, end);
			Matcher matcher = docTypeName.matcher(text);
			if (matcher.find()) {
				name = matcher.group(1);
			}
			matcher = docTypePublic.matcher(text);
			if (matcher.find()) {
				publicId = matcher.group(1);
				systemId = matcher.group(2);
			} else {
				matcher = docTypeSystem.matcher(text);
				if (matcher.find()) {
					systemId = matcher.group(1);
				}
			}
			matcher = docTypeSubset.matcher(text);
			if (matcher.find()) {
				internalSubset = matcher.group(1);
			}
			docType = new FuzzyXMLDocTypeImpl(null, name, publicId, systemId, internalSubset, offset, end - offset);
		}
	}

	private void handleCloseTag(int offset, int end, String text) {
		if (stack.size() == 0) {
			return;
		}
		String tagName = text.substring(1).trim();
		FuzzyXMLElementImpl element = (FuzzyXMLElementImpl) stack.pop();
		if (!element.getName().toLowerCase().equals(tagName.toLowerCase())) {
			if (element.getParentNode() != null) {
				((FuzzyXMLElementImpl) element.getParentNode()).appendChildWithNoCheck(element);
				FuzzyXMLNode[] nodes = element.getChildren();
				for (int i = 0; i < nodes.length; i++) {
					((AbstractFuzzyXMLNode) nodes[i]).setParentNode(element.getParentNode());
					element.removeChild(nodes[i]);
					((FuzzyXMLElementImpl) element.getParentNode()).appendChildWithNoCheck(nodes[i]);
				}
			} else {
				fireErrorEvent(offset, end - offset, Messages.getMessage("error.noStartTag", tagName), null);
			}
			handleCloseTag(offset, end, text);
//			stack.push(element);
			return;
		}
		if (element.getChildren().length == 0) {
			element.appendChild(new FuzzyXMLTextImpl(getParent(), "", offset, 0));
		}
		element.setLength(end - element.getOffset());
		nonCloseElement.remove(element);
		if (element.getParentNode() == null) {
			root = element;
			for (int i = 0; i < nonCloseElement.size(); i++) {
				FuzzyXMLElement error = (FuzzyXMLElement) nonCloseElement.get(i);
				fireErrorEvent(error.getOffset(), error.getLength(),
						Messages.getMessage("error.noCloseTag", error.getName()), error);
			}
		} else {
			((FuzzyXMLElementImpl) element.getParentNode()).appendChildWithNoCheck(element);
		}
	}

	private void handleEmptyTag(int offset, int end) {
		TagInfo info = parseTagContents(originalSource.substring(offset + 1, end - 1));
		FuzzyXMLNode parent = getParent();
		FuzzyXMLElementImpl element = new FuzzyXMLElementImpl(parent, info.name, offset, end - offset);
		if (parent == null) {
			root = element;
		} else {
			((FuzzyXMLElement) parent).appendChild(element);
		}
		AttrInfo[] attrs = info.getAttrs();
		for (int i = 0; i < attrs.length; i++) {
			FuzzyXMLAttributeImpl attr = new FuzzyXMLAttributeImpl(element, attrs[i].name, attrs[i].value,
					attrs[i].offset + offset, attrs[i].end - attrs[i].offset + 1);
			element.appendChild(attr);
		}
	}

	private void handleComment(int offset, int end, String text) {
		if (getParent() != null) {
			text = text.replaceFirst("<!--", "");
			text = text.replaceFirst("-->", "");
			FuzzyXMLCommentImpl comment = new FuzzyXMLCommentImpl(getParent(), text, offset, end - offset);
			((FuzzyXMLElement) getParent()).appendChild(comment);
		}
	}

	private void handleStartTag(int offset, int end) {
		TagInfo info = parseTagContents(originalSource.substring(offset + 1, end - 1));
		FuzzyXMLElementImpl element = new FuzzyXMLElementImpl(getParent(), info.name, offset, end - offset);
		AttrInfo[] attrs = info.getAttrs();
		for (int i = 0; i < attrs.length; i++) {
//			if(attrs[i].name.startsWith("xmlns")){
//				String uri    = attrs[i].value;
//				String prefix = null;
//				String[] dim = attrs[i].name.split(":");
//				if(dim.length > 1){
//					prefix = dim[1];
//				}
//				element.addNamespaceURI(prefix,uri);
//			}
			FuzzyXMLAttributeImpl attr = new FuzzyXMLAttributeImpl(element, attrs[i].name, attrs[i].value,
					attrs[i].offset + offset, attrs[i].end - attrs[i].offset + 1);
			attr.setQuoteCharacter(attrs[i].quote);
			if (attrs[i].value.indexOf('"') >= 0 || attrs[i].value.indexOf('\'') >= 0
					|| attrs[i].value.indexOf('<') >= 0 || attrs[i].value.indexOf('>') >= 0
					|| attrs[i].value.indexOf('&') >= 0) {
				attr.setEscape(false);
			}
			element.appendChild(attr);
		}
		stack.push(element);
		nonCloseElement.add(element);
	}

	private FuzzyXMLNode getParent() {
		if (stack.size() == 0) {
			return null;
		} else {
			return (FuzzyXMLNode) stack.get(stack.size() - 1);
		}
	}

	private TagInfo parseTagContents(String text) {
		text = text.trim();
		if (text.endsWith("/")) {
			text = text.substring(0, text.length() - 1);
		}
		TagInfo info = new TagInfo();
		if (FuzzyXMLUtil.getSpaceIndex(text) != -1) {
			info.name = text.substring(0, FuzzyXMLUtil.getSpaceIndex(text)).trim();
			parseAttributeContents(info, text);
		} else {
			info.name = text;
		}
		return info;
	}

	private void parseAttributeContents(TagInfo info, String text) {

		int flag = 0;
		StringBuffer sb = new StringBuffer();
		String name = null;
		char quote = 0;
		int start = -1;
		boolean escape = false;

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (flag == 0 && FuzzyXMLUtil.isWhitespace(c)) {
				flag = 1;
			} else if (flag == 1 && !FuzzyXMLUtil.isWhitespace(c)) {
				if (start == -1) {
					start = i;
				}
				flag = 2;
				sb.append(c);
			} else if (flag == 2) {
				if (c == '=') {
					flag = 3;
					name = sb.toString().trim();
					sb.setLength(0);
				} else {
					sb.append(c);
				}
			} else if (flag == 3 && !FuzzyXMLUtil.isWhitespace(c)) {
				quote = c;
				flag = 4;
			} else if (flag == 4) {
				if (c == quote && escape == true) {
					sb.append(c);
					escape = false;
				} else if (c == quote) {
					// add an attribute
					AttrInfo attr = new AttrInfo();
					attr.name = name;
					attr.value = FuzzyXMLUtil.decode(sb.toString(), isHTML);
					attr.offset = start;
					attr.end = i + 1;
					attr.quote = quote;
					info.addAttr(attr);
					// reset
					sb.setLength(0);
					flag = 1;
					start = -1;
				} else if (c == '\\') {
					if (escape == true) {
						sb.append(c);
					} else {
						escape = true;
					}
				} else {
					sb.append(c);
					escape = false;
				}
			}
		}
//		Matcher matcher = attr.matcher(text);
//		while(matcher.find()){
//			AttrInfo attr = new AttrInfo();
//			attr.name   = matcher.group(1);
//			attr.value  = FuzzyXMLUtil.decode(matcher.group(3));
//			attr.offset = matcher.start();
//			attr.end    = matcher.end();
//			info.addAttr(attr);
//		}
	}

	private class TagInfo {
		private String name;
		private List<AttrInfo> attrs = new ArrayList<>();

		public void addAttr(AttrInfo attr) {
			AttrInfo[] info = getAttrs();
			for (int i = 0; i < info.length; i++) {
				if (info[i].name.equals(attr.name)) {
					return;
				}
			}
			attrs.add(attr);
		}

		public AttrInfo[] getAttrs() {
			return (AttrInfo[]) attrs.toArray(new AttrInfo[attrs.size()]);
		}
	}

	private class AttrInfo {
		private String name;
		private String value;
		private int offset;
		private int end;
		private char quote;
	}

}
