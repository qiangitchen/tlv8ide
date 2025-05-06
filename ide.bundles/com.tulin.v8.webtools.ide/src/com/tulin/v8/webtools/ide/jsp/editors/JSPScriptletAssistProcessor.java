package com.tulin.v8.webtools.ide.jsp.editors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.DefaultWorkingCopyOwner;
import org.eclipse.jdt.internal.ui.text.java.AbstractJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.CompletionProposalCollector;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ContextInformationValidator;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.html.HTMLUtil;
import com.tulin.v8.webtools.ide.jsp.compiler.CompileResult;
import com.tulin.v8.webtools.ide.jsp.compiler.JSPCompiler;

/**
 * Provides code completion for Java code.
 * 
 * @since 2.0.3
 */
public class JSPScriptletAssistProcessor implements IContentAssistProcessor {

	private IFile file;

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		if (file == null) {
			return new ICompletionProposal[0];
		}
		try {
			String source = viewer.getDocument().get();
			String[] words = getLastWord(source, offset);
//			String target = words[0];
//			String last = words[1];
			String word = words[2];
//			String requirePath = words[3];
			CompileResult result = JSPCompiler.compile(source);
			IJavaProject project = JavaCore.create(file.getProject());
			if (project != null) {
				ICompilationUnit unit = HTMLUtil.getTemporaryCompilationUnit(project);
				HTMLUtil.setContentsToCU(unit, result.toString());

				CompletionProposalCollector collector = new CompletionProposalCollector(project);
				int headerLength = result.getHeader().length();
				unit.codeComplete(headerLength + offset, collector, DefaultWorkingCopyOwner.PRIMARY);

				IJavaCompletionProposal[] proposals = collector.getJavaCompletionProposals();
				List<ICompletionProposal> list = new ArrayList<ICompletionProposal>();
				for (int i = 0; i < proposals.length; i++) {
					if (proposals[i] instanceof AbstractJavaCompletionProposal) {
						AbstractJavaCompletionProposal proposal = (AbstractJavaCompletionProposal) proposals[i];
						if (proposal.getDisplayString().endsWith("_xxx")) {
							continue;
						}
						proposal.setReplacementOffset(proposal.getReplacementOffset() - headerLength);
						list.add(proposal);
					}
				}
				// 提示内容排序 【根据输入的内容匹配的排序】
				Collections.sort(list, new Comparator<ICompletionProposal>() {
					@Override
					public int compare(ICompletionProposal o1, ICompletionProposal o2) {
						String n1 = o1.getDisplayString().toLowerCase();
						String n2 = o2.getDisplayString().toLowerCase();
						if (n1.indexOf("-") > 0) {
							n1 = n1.substring(0, n1.indexOf("-"));
						}
						if (n2.indexOf("-") > 0) {
							n2 = n2.substring(0, n2.indexOf("-"));
						}
						if (!n1.startsWith(word) && n2.startsWith(word)) {
							return 1;
						}
						if (n1.startsWith(word) && !n2.startsWith(word)) {
							return -1;
						}
						if (n1.startsWith(word) && n2.startsWith(word)) {
							if (n1.length() < n2.length()) {
								return -1;
							} else {
								return 1;
							}
						}
						return 0;
					}
				});
				return list.toArray(new ICompletionProposal[list.size()]);
			}
		} catch (Exception e) {
			WebToolsPlugin.logException(e);
		}
		return new ICompletionProposal[0];
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

	public IContextInformationValidator getContextInformationValidator() {
		return new ContextInformationValidator(this);
	}

	public String getErrorMessage() {
		return "Error";
	}

	public void update(JSPSourceEditor editor) {
		IEditorInput input = editor.getEditorInput();
		if (input instanceof IFileEditorInput) {
			this.file = ((IFileEditorInput) input).getFile();
		}
	}

	public String[] getLastWord(String source, int offset) {
		StringBuilder buf = new StringBuilder();
		int current = offset - 1;
		int b1 = 0; // ()
		int b2 = 0; // []
		while (current >= 0) {
			char c = source.charAt(current);
			if (c >= 'a' && c <= 'z') {
				if (b1 == 0 && b2 == 0) {
					buf.insert(0, c);
				}
			} else if (c >= 'A' && c <= 'Z') {
				if (b1 == 0 && b2 == 0) {
					buf.insert(0, c);
				}
			} else if (c >= '0' && c <= '9') {
				if (b1 == 0 && b2 == 0) {
					buf.insert(0, c);
				}
			} else if (c == '_' || c == '$' || c == '.') {
				if (b1 == 0 && b2 == 0) {
					buf.insert(0, c);
				}
			} else if (c == '(' && b1 > 0) {
				b1--;
			} else if (c == ')') {
				b1++;
			} else if (c == '[' && b2 > 0) {
				b2--;
			} else if (c == ']') {
				b2++;
			} else if (c == ' ' && buf.length() > 0 && b1 == 0 && b2 == 0) {
				break;
			} else if (b1 == 0 && b2 == 0) {
				break;
			}
			current--;
		}

		String target = source.substring(current + 1, offset);

		String word = buf.toString().trim();
		int pos = word.lastIndexOf('.');
		if (pos >= 0) {
			return new String[] { target, word.substring(0, pos + 1), word.substring(pos + 1), "" };
		} else {
			StringBuilder requirePathBuf = new StringBuilder();
			int i = offset - 1;
			while (i >= 0) {
				char c = source.charAt(i);
				if (c >= 'a' && c <= 'z') {
					requirePathBuf.insert(0, c);
				} else if (c >= 'A' && c <= 'Z') {
					requirePathBuf.insert(0, c);
				} else if (c >= '0' && c <= '9') {
					requirePathBuf.insert(0, c);
				} else if (c == '.' || c == '/' || c == '(' || c == '"' || c == '\'' || c == '_' || c == '$') {
					requirePathBuf.insert(0, c);
				} else {
					break;
				}
				i--;
			}
			return new String[] { target, "", word, requirePathBuf.toString() };
		}
	}

}
