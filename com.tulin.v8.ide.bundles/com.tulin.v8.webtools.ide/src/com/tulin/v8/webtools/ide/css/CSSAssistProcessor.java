package com.tulin.v8.webtools.ide.css;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ContextInformationValidator;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.html.HTMLUtil;

/**
 * The implementaion of IContentAssistProcessor for the CSS Editor.
 */
public class CSSAssistProcessor implements IContentAssistProcessor {

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		String text = getSource(viewer).substring(0, offset);
		String word = getLastWord(text);
		List<ICompletionProposal> list = new ArrayList<ICompletionProposal>();
		if (word != null) {
			if (word.endsWith("{")) {
				word = word.substring(0, word.length() - 1);
				if (word.indexOf(":") > 0) {
					String vals = word.substring(word.indexOf(":") + 1).trim();
					for (CSSInfo value : CSSDefinition.Pseudo_Element) {
						if (value.getReplaceString().startsWith(vals)) {
							String replaceString = value.getReplaceString();
							String description = value.getDescription();
							list.add(new CompletionProposal(replaceString, offset - vals.length(), vals.length(),
									replaceString.length(),
									WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_CSS_PROP),
									replaceString, null, description));
						}
					}
				} else if (word.indexOf("@") > -1) {
					String vals = word.substring(word.indexOf("@")).trim();
					for (CSSInfo value : CSSDefinition.Media_Type) {
						if (value.getReplaceString().startsWith(vals)) {
							String replaceString = value.getReplaceString();
							String description = value.getDescription();
							list.add(new CompletionProposal(replaceString, offset - vals.length(), vals.length(),
									replaceString.length(),
									WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_CSS_PROP),
									replaceString, null, description));
						}
					}
				}
			} else {
				if (word.indexOf(":") > 0) {
					String prop = word.substring(0, word.indexOf(":"));
					String vals = word.substring(word.indexOf(":") + 1).trim();
					for (int i = 0; i < CSSDefinition.CSS_VALUES.length; i++) {
						if (CSSDefinition.CSS_VALUES[i].getName().startsWith(prop)) {
							List<CSSInfo> values = CSSDefinition.CSS_VALUES[i].getValues();
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
						if (CSSDefinition.CSS_KEYWORDS[i].getReplaceString().startsWith(word)) {
							String replaceString = CSSDefinition.CSS_KEYWORDS[i].getReplaceString();
							if (replaceString.indexOf(":") < 0) {
								replaceString += ": ";
							}
							list.add(new CompletionProposal(replaceString, offset - word.length(), word.length(),
									replaceString.length(),
									WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_CSS_PROP),
									CSSDefinition.CSS_KEYWORDS[i].getDisplayString(), null,
									CSSDefinition.CSS_KEYWORDS[i].getDescription()));
						}
					}
				}
			}
		}

		// sort
		HTMLUtil.sortCompilationProposal(list);
		ICompletionProposal[] prop = list.toArray(new ICompletionProposal[list.size()]);
		return prop;
	}

	protected String getSource(ITextViewer viewer) {
		return viewer.getDocument().get();
	}

	private String getLastWord(String text) {
		text = HTMLUtil.cssComment2space(text);

		int index0 = text.lastIndexOf('}');
		if (index0 < text.length() - 1) {
			text = text.substring(index0 + 1);
		}

		int index1 = text.lastIndexOf(';');
		int index2 = text.lastIndexOf('{');

		if (index1 >= 0 && index1 > index2) {
			return text.substring(index1 + 1).trim();
		} else if (index2 >= 0) {
			return text.substring(index2 + 1).trim();
		} else {
			return text.trim() + "{";
		}
	}

	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		ContextInformation[] info = new ContextInformation[0];
		return info;
	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[0];
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

}
