package com.tulin.v8.webtools.ide.js.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ContextInformationValidator;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.html.HTMLUtil;
import com.tulin.v8.webtools.ide.html.editors.HTMLEditor;
import com.tulin.v8.webtools.ide.template.HTMLTemplateManager;
import com.tulin.v8.webtools.ide.template.JavaScriptContextType;

/**
 * A completion processor for JsDoc. For the specification, refer to
 * <a href="http://code.google.com/p/jsdoc-toolkit/">JsDoc Tool</a>.
 */
public class JavaScriptJsDocAssistProcessor implements IContentAssistProcessor {
	public static final int TAG = 10;
	public static final int TYPE = 11;

	public static List<JsDocAssistInfo> TAG_INFO = new ArrayList<JsDocAssistInfo>();
	public static List<JsDocAssistInfo> TYPE_INFO = new ArrayList<JsDocAssistInfo>();
	static {
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@augments - an extended class name", "@augments ${otherClass}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@author - an author name", "@author ${user}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@borrows - a borrowed method or property from another class",
				"@borrows ${otherMemberName} as ${thisMemberName}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@class - a class", "@class ${description}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@constant - a constant", "@constant"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@constructor - a constructor", "@constructor"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@constructs - a function to construct instances of the class",
				"@constructs"));
		TAG_INFO.add(
				new JsDocAssistInfo(TAG, "@default - a default value of an object", "@default ${valueDescription}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@deprecated - a deprecated method or property",
				"@deprecated ${deprecatedDescription}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@description - a description", "@description ${description}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@event - a function fired by an event", "@event"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@example - a snippet of code for the usage", "@example ${snippet}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@exports - information about an alias to another symbol",
				"@exports ${codedName} as ${documentedName}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@field - a field", "@field"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@fileOverview - a documentation for an entire file",
				"@fileOverview ${fileDescription}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@function - a function", "@function"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@ignore - an ignored object", "@ignore"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@inner - an inner function", "@inner"));
		TAG_INFO.add(
				new JsDocAssistInfo(TAG, "@lends - a member of an anonymous object literal", "@lends ${symbolAlias}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@link - a HTML link to documented symbol", "@link ${otherSymbol}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@memberOf - a namepath of the containing object. ",
				"@memberOf ${parentNamepath}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@name - a namepath of an object", "@name ${theNamepath}"));
		TAG_INFO.add(
				new JsDocAssistInfo(TAG, "@namespace - a description of the namespace", "@namespace ${description}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@param - information about the parameters to a function",
				"@param {${paramType}} ${paramName} ${paramDescription}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@private - a private member", "@private"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@property - information about the property",
				"@property {${propertyType}} ${propertyName} ${propertyDescription}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@public - a public member", "@public"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@requires - required resource", "@requires ${requireDescription}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@returns - a value returned by a function or method",
				"@returns {${returnType}} ${returnDescription}"));
		TAG_INFO.add(
				new JsDocAssistInfo(TAG, "@see - reference to another symbol or resource", "@see ${seeDescription}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@since - a version number that introduces this feature",
				"@since ${versionDescription}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@static - a version number that introduces this feature", "@static"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@throws - an exception a function might throw. ",
				"@throws {${exceptionType}} ${exceptionDescription}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@type - a type name", "@type ${typeName}"));
		TAG_INFO.add(new JsDocAssistInfo(TAG, "@version - a version number", "@version ${versionDescription}"));

		TYPE_INFO.add(new JsDocAssistInfo(TYPE, "Boolean", "Boolean"));
		TYPE_INFO.add(new JsDocAssistInfo(TYPE, "Number", "Number"));
		TYPE_INFO.add(new JsDocAssistInfo(TYPE, "String", "String"));
		TYPE_INFO.add(new JsDocAssistInfo(TYPE, "Object", "Object"));
		TYPE_INFO.add(new JsDocAssistInfo(TYPE, "Function", "Function"));
		TYPE_INFO.add(new JsDocAssistInfo(TYPE, "Array", "Array"));
		TYPE_INFO.add(new JsDocAssistInfo(TYPE, "Date", "Date"));
		TYPE_INFO.add(new JsDocAssistInfo(TYPE, "RegExp", "RegExp"));
	}

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		List<ICompletionProposal> proposalList = new ArrayList<ICompletionProposal>();

		String source = getSource(viewer);

		String word = getTargetWord(source, offset);
		if (word.length() == 0) {
			Point range = viewer.getSelectedRange();
			if (range.y != 0) {
				word = source.substring(range.x, range.x + range.y);
			}
		}

		for (JsDocAssistInfo info : TAG_INFO) {
			if (info.getDisplayString().startsWith(word)) {
				proposalList.add(info.createCompletionProposal(offset, word));
			}
		}

		if ("paramType".equals(word) || "propertyType".equals(word) || "returnType".equals(word)) {
			for (JsDocAssistInfo info : TYPE_INFO) {
				proposalList.add(info.createCompletionProposal(offset, word));
			}
		}

		HTMLUtil.sortCompilationProposal(proposalList);

		ICompletionProposal[] props = proposalList.toArray(new ICompletionProposal[proposalList.size()]);
		return props;
	}

	protected String getSource(ITextViewer viewer) {
		return viewer.getDocument().get();
	}

	private String getTargetWord(String source, int offset) {
		String subSource = source.substring(0, offset);
		StringBuilder buf = new StringBuilder();
		for (int i = subSource.length() - 1; i >= 0; i--) {
			char c = subSource.charAt(i);
			if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '@') {
				buf.insert(0, c);
			} else {
				break;
			}
		}
		return buf.toString();
	}

	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return new ContextInformation[0];
	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { '@' };
	}

	public char[] getContextInformationAutoActivationCharacters() {
		return new char[0];
	}

	public String getErrorMessage() {
		return "error";
	}

	public IContextInformationValidator getContextInformationValidator() {
		return new ContextInformationValidator(this);
	}

	/**
	 * The structure for assist informations.
	 */
	public static class JsDocAssistInfo {
		private int type;
		private String displayString;
		private String replaceString;

		public JsDocAssistInfo(int type, String displayString, String replaceString) {
			this.type = type;
			this.displayString = displayString;
			this.replaceString = replaceString;
		}

		public String getReplaceString() {
			return this.replaceString;
		}

		public String getDisplayString() {
			return this.displayString;
		}

		@SuppressWarnings("deprecation")
		public ICompletionProposal createCompletionProposal(int offset, String word) {
			if (type == TAG) {
				Region region = new Region(offset - word.length(), word.length());

				HTMLTemplateManager manager = HTMLTemplateManager.getInstance();
				TemplateContextType contextType = manager.getContextTypeRegistry()
						.getContextType(JavaScriptContextType.CONTEXT_TYPE);

				IEditorPart editor = HTMLUtil.getActiveEditor();
				IDocument document = null;
				if (editor instanceof HTMLEditor) {
					document = ((HTMLEditor) editor).getPaletteTarget().getDocumentProvider()
							.getDocument(editor.getEditorInput());
				} else {
					document = ((AbstractDecoratedTextEditor) editor).getDocumentProvider()
							.getDocument(editor.getEditorInput());

				}

				TemplateContext context = new DocumentTemplateContext(contextType, document, region.getOffset(),
						region.getLength());

				String[] dim = displayString.split("-");
				String templateName = dim[0].trim();

				String description = "";
				if (dim.length > 1) {
					description = dim[1].trim();
				}

				Template template = new Template(templateName, description, contextType.getId(), replaceString, true);

				return new TemplateProposal(template, context, region, getImageFromType(type));
			} else if (type == TYPE) {
				return new CompletionProposal(replaceString, offset, word.length(), replaceString.length(),
						getImageFromType(type), displayString, null, null);
			}
			return null;
		}

		private static Image getImageFromType(int type) {
			switch (type) {
			case TAG:
				return WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_ANNOTATION);
			case TYPE:
				return WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_FUNCTION);
			default:
				return null;
			}
		}
	}

}
