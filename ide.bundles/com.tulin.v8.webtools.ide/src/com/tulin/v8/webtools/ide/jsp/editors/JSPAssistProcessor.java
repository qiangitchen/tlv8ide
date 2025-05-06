package com.tulin.v8.webtools.ide.jsp.editors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IFileEditorInput;

import com.tulin.v8.fuzzyxml.FuzzyXMLDocument;
import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.fuzzyxml.FuzzyXMLParser;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.assist.AssistInfo;
import com.tulin.v8.webtools.ide.assist.AttributeInfo;
import com.tulin.v8.webtools.ide.assist.HTMLAssistProcessor;
import com.tulin.v8.webtools.ide.assist.TagDefinition;
import com.tulin.v8.webtools.ide.assist.TagInfo;
import com.tulin.v8.webtools.ide.assist.TextInfo;
import com.tulin.v8.webtools.ide.html.HTMLUtil;
import com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditor;
import com.tulin.v8.webtools.ide.jsp.IJSPELAssistProcessor;
import com.tulin.v8.webtools.ide.jsp.JSPInfo;
import com.tulin.v8.webtools.ide.jsp.TLDInfo;
import com.tulin.v8.webtools.ide.tag.ICustomTagAttributeAssist;
import com.tulin.v8.webtools.ide.xml.editors.ClassNameAssistProcessor;

/**
 * This is an implementation of the AssistProcessor for the
 * <code>JSPSourceEditor</code>.
 */
public class JSPAssistProcessor extends HTMLAssistProcessor {

	private List<TagInfo> tagList = new ArrayList<TagInfo>(TagDefinition.getTagInfoAsList());
	private Image functionImage;
	private static final int SCOPE = 100;
	private static final int CLASS = 101;
	private List<TagInfo> cunstomTagList = new ArrayList<TagInfo>();
	private List<Function> functions = new ArrayList<Function>();
	private Map<String, String> namespace = new HashMap<String, String>();
	private ClassNameAssistProcessor classNameProcessor = new ClassNameAssistProcessor();
	private JSPScriptletAssistProcessor scriptletProcessor = new JSPScriptletAssistProcessor();
	private IFile file = null;

	public JSPAssistProcessor() {
		functionImage = WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_FUNCTION);

		// JSP actions
		TagInfo useBean = new TagInfo("jsp:useBean", true);
		useBean.addAttributeInfo(new AttributeInfo("id", true));
		useBean.addAttributeInfo(new AttributeInfo("scope", true, SCOPE));
		useBean.addAttributeInfo(new AttributeInfo("class", true, CLASS));
		tagList.add(useBean);

		TagInfo setProperty = new TagInfo("jsp:setProperty", false);
		setProperty.addAttributeInfo(new AttributeInfo("name", true));
		setProperty.addAttributeInfo(new AttributeInfo("param", true));
		setProperty.addAttributeInfo(new AttributeInfo("property", true));
		tagList.add(setProperty);

		TagInfo include = new TagInfo("jsp:include", false);
		include.addAttributeInfo(new AttributeInfo("page", true, AttributeInfo.FILE));
		tagList.add(include);

		TagInfo forward = new TagInfo("jsp:forward", true);
		forward.addAttributeInfo(new AttributeInfo("page", true));
		tagList.add(forward);

		TagInfo param = new TagInfo("jsp:param", false);
		param.addAttributeInfo(new AttributeInfo("name", true));
		param.addAttributeInfo(new AttributeInfo("value", true));
		tagList.add(param);

		TagInfo attribute = new TagInfo("jsp:attribute", true);
		attribute.addAttributeInfo(new AttributeInfo("name", true));
		tagList.add(attribute);

		TagInfo body = new TagInfo("jsp:body", true);
		tagList.add(body);

		TagInfo element = new TagInfo("jsp:element", true);
		element.addAttributeInfo(new AttributeInfo("name", true));
		tagList.add(element);

		TagInfo text = new TagInfo("jsp:text", true);
		tagList.add(text);

		// JSP directives
		tagList.add(new TextInfo("<%  %>", 3));
		tagList.add(new TextInfo("<%=  %>", 4));
		tagList.add(new TextInfo("<%@ page %>", 9));
		tagList.add(new TextInfo("<%@ include %>", "<%@ include file=\"\" %>", 18));
		tagList.add(new TextInfo("<%@ taglib %>", "<%@ taglib prefix=\"\" %>", 19));
	}

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int documentOffset) {
		// Java code completion for partitions which are not parted as
		// HTMLPartitionScanner#HTML_SCRIPT.
		String rawText = viewer.getDocument().get();
		rawText = HTMLUtil.comment2space(rawText, false);
		String text = rawText.substring(0, documentOffset);
		int begin = text.lastIndexOf("<%");
		if (begin >= 0) {
			int end = rawText.indexOf("%>", begin);
			if (end >= 0 && documentOffset < end) {
				return scriptletProcessor.computeCompletionProposals(viewer, documentOffset);
			}
		}

		// EL Function
		begin = text.lastIndexOf("${");
		if (begin >= 0) {
			int end = rawText.indexOf("}", begin);
			if (end >= 0 && documentOffset <= end) {
				List<ICompletionProposal> result = new ArrayList<ICompletionProposal>();
				String expression = getLastWord2(text).replaceAll("^[\"']?\\$\\{", "");

				FuzzyXMLDocument doc = new FuzzyXMLParser().parse(viewer.getDocument().get());
				FuzzyXMLElement element = doc.getElementByOffset(documentOffset);

				for (IJSPELAssistProcessor assist : WebToolsPlugin.getDefault().getJSPELAssists()) {
					AssistInfo[] assistInfos = assist.getCompletionProposals(element, expression);
					if (assistInfos != null) {
						for (AssistInfo info : assistInfos) {
							if (info.getReplaceString().startsWith(expression)) {
								result.add(info.toCompletionProposal(documentOffset, expression, null));
							}
						}
					}
				}

				for (Function function : functions) {
					if (function.getName().startsWith(expression)) {
						String name = function.getName();
						result.add(new CompletionProposal(name + "()", documentOffset - expression.length(),
								expression.length(), name.length() + 1, functionImage, name + "()", null,
								function.getDescription()));
					}
				}

				return result.toArray(new ICompletionProposal[result.size()]);
			}
		}

		return super.computeCompletionProposals(viewer, documentOffset);
	}

	@Override
	protected AssistInfo[] getAttributeValues(String tagName, String value, AttributeInfo info) {
		if (tagName.indexOf(":") != -1) {
			String[] dim = tagName.split(":");
			String uri = getUri(dim[0]);
			ICustomTagAttributeAssist[] assists = WebToolsPlugin.getDefault().getCustomTagAttributeAssists();
			for (int i = 0; i < assists.length; i++) {
				FuzzyXMLElement element = getOffsetElement();
				AssistInfo[] values = assists[i].getAttributeValues(dim[1], uri, value, info, element);
				if (values != null) {
					return values;
				}
			}
		}
		if (info.getAttributeType() == SCOPE) {
			return new AssistInfo[] { new AssistInfo("application"), new AssistInfo("page"), new AssistInfo("request"),
					new AssistInfo("session") };
		}
		if (info.getAttributeType() == CLASS && this.file != null) {
			return classNameProcessor.getClassAttributeValues(this.file, value);
		}
		return super.getAttributeValues(tagName, value, info);
	}

	@Override
	protected TagInfo getTagInfo(String name) {
		List<TagInfo> tagList = getTagList();
		for (int i = 0; i < tagList.size(); i++) {
			TagInfo info = tagList.get(i);
			if (info.getTagName() != null) {
				if (name.equals(info.getTagName().toLowerCase())) {
					return info;
				}
			}
		}
		return null;
	}

	@Override
	protected List<TagInfo> getTagList() {
		List<TagInfo> list = new ArrayList<TagInfo>();
		list.addAll(tagList);
		list.addAll(cunstomTagList);
		return list;
	}

	/** Returns URI from taglib prefix. */
	private String getUri(String prefix) {
		return (String) namespace.get(prefix);
	}

	/**
	 * Updates informations about code completion.
	 *
	 * @param input  the <code>HTMLSourceEditor</code> instance
	 * @param source JSP source code
	 */
	@Override
	public void update(HTMLSourceEditor editor, String source) {
		super.update(editor, source);
		this.scriptletProcessor.update((JSPSourceEditor) editor);
		if (editor.getEditorInput() instanceof IFileEditorInput) {
			IFileEditorInput input = (IFileEditorInput) editor.getEditorInput();
			cunstomTagList.clear();
			namespace.clear();
			functions.clear();
			JSPInfo jspInfo = JSPInfo.getJSPInfo(input.getFile(), source);
			TLDInfo[] tlds = jspInfo.getTLDInfo();
			for (int i = 0; i < tlds.length; i++) {
				namespace.put(tlds[i].getPrefix(), tlds[i].getTaglibUri());
				cunstomTagList.addAll(tlds[i].getTagInfo());
				functions.addAll(tlds[i].getFunctions());
			}
			this.file = input.getFile();
		}
	}

	private String getLastWord2(String text) {
		StringBuilder sb = new StringBuilder();
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
			if (isDelimiter2(c)) {
				sb.setLength(0);
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	private boolean isDelimiter2(char c) {
		if (isDelimiter(c)) {
			return true;
		} else {
			if (c == '"' || c == '\'') {
				return true;
			}
		}
		return false;
	}

}
