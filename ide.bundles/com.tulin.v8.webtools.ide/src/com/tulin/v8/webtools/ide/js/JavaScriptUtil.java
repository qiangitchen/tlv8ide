package com.tulin.v8.webtools.ide.js;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.WorkbenchPart;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

import com.tulin.v8.webtools.ide.html.HTMLUtil;
import com.tulin.v8.webtools.ide.html.editors.HTMLEditor;
import com.tulin.v8.webtools.ide.js.JsDocParser.JsDoc;
import com.tulin.v8.webtools.ide.template.HTMLTemplateManager;
import com.tulin.v8.webtools.ide.template.JavaScriptContextType;

/**
 * Provides utility methods about JavaScript.
 */
public class JavaScriptUtil {

	public static JsDoc extractJsDoc(String source, int position) {
		// extract JsDoc
		String comment = source.substring(0, position).trim();
		JsDoc jsDoc = null;
		if (comment.endsWith("*/")) {
			int index = comment.lastIndexOf("/**");
			if (index >= 0) {
				comment = comment.substring(index);
				jsDoc = JsDocParser.parse(comment);
			}
		}
		return jsDoc;
	}

	public static String removeComments(String source) {
		source = HTMLUtil.cssComment2space(source);

		int index = 0;
		int last = 0;

		StringBuffer sb = new StringBuffer();
		while ((index = source.indexOf("//", last)) != -1) {
			int end1 = source.indexOf("\n", index);
			int end2 = source.indexOf("\r", index);
			if (end1 > end2) {
				end1 = end2;
			}
			if (end1 != -1) {
				sb.append(source.substring(last, index));
				int length = end1 - index + 2;
				for (int i = 0; i < length; i++) {
					sb.append(" ");
				}
			} else {
				break;
			}
			last = end1 + 1;
		}
		if (last != source.length() - 1) {
			sb.append(source.substring(last));
		}
		return sb.toString();

	}

	public static boolean isWhitespace(char c) {
		if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
			return true;
		} else {
			return false;
		}
	}

	private static final Pattern ANON_FUNCTION_PATTERN = Pattern.compile("function[\\s\r\n]*?\\((.*?)\\)[\\s\r\n]*?\\{",
			Pattern.DOTALL);

	public static String extractAnonFuncArguments(String source) {
		Matcher matcher = ANON_FUNCTION_PATTERN.matcher(source);
		if (matcher.find() && matcher.start() == 0) {
			String args = matcher.group(1).replaceAll("[\\s\r\n]*,[\\s\r\n]*", ", ").trim();
			return args;
		}
		return null;
	}

	/**
	 * Creates a {@link JavaScriptFunctionProposal} instance. This method is called
	 * by {@link JavaScriptAssistProcessor}.
	 *
	 * @param displayString the display string
	 * @param replaceString the replace string
	 * @param description   the description (JsDoc)
	 * @param image         the image
	 * @param region        the region to replace
	 * @return {@link JavaScriptFunctionProposal}
	 */
	@SuppressWarnings("deprecation")
	public static ICompletionProposal createTemplateCompletionProposal(String displayString, String replaceString,
			String fromSource, String description, Image image, Region region) {

		HTMLTemplateManager manager = HTMLTemplateManager.getInstance();
		TemplateContextType contextType = manager.getContextTypeRegistry()
				.getContextType(JavaScriptContextType.CONTEXT_TYPE);

		IEditorPart editor = HTMLUtil.getActiveEditor();

		if (fromSource == null) {
			fromSource = ((WorkbenchPart) editor).getPartName();
		}
		Template template = new Template(displayString, fromSource, contextType.getId(), replaceString, true);

		IDocument document = null;
		if (editor instanceof HTMLEditor) {
			document = ((HTMLEditor) editor).getPaletteTarget().getDocumentProvider()
					.getDocument(editor.getEditorInput());
		} else if (editor instanceof AbstractDecoratedTextEditor) {
			document = ((AbstractDecoratedTextEditor) editor).getDocumentProvider()
					.getDocument(editor.getEditorInput());
		}

		TemplateContext templateContext = new DocumentTemplateContext(contextType, document, region.getOffset(),
				region.getLength());

		ICompletionProposal proposal = new JavaScriptFunctionProposal(template, templateContext, region, image,
				description);

		return proposal;
	}

}
