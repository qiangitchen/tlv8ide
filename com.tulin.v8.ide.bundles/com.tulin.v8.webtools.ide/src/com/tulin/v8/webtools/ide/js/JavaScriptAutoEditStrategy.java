package com.tulin.v8.webtools.ide.js;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

import com.tulin.v8.webtools.ide.html.HTMLUtil;
import com.tulin.v8.webtools.ide.js.model.JavaScriptContext;
import com.tulin.v8.webtools.ide.js.model.JavaScriptFunction;
import com.tulin.v8.webtools.ide.js.model.JavaScriptModel;
import com.tulin.v8.webtools.ide.js.model.JavaScriptFunction.JavaScriptArgument;

public class JavaScriptAutoEditStrategy extends DefaultIndentLineAutoEditStrategy {
	private List<String> commandTextList = new LinkedList<String>();

	public void customizeDocumentCommand(IDocument document, DocumentCommand command) {
		String text = command.text;
		String prev = "";
		String current = "";
		String next = "";
		boolean isSpace = true;
		int offset = command.offset;
		if (offset > 0) {
			try {
				prev = document.get(offset - 1, 1);
			} catch (BadLocationException ex) {
				// ignore
			}
		}
		if (offset < document.getLength() - 1) {
			try {
				current = document.get(offset, 1);
				next = document.get(offset + 1, 1);
				if (!next.equals("\r") && !next.equals("\n") && !next.equals(" ") && !next.equals("\t")) {
					isSpace = false;
				}
			} catch (BadLocationException ex) {
				// ignore
			}
		}

		// {[(
		if (isSpace) {
			if (text.equals("{")) {
				append(command, "}", 1);
				addCommandText(text);
				return;
			}
			if (text.equals("[")) {
				append(command, "]", 1);
				addCommandText(text);
				return;
			}
			if (text.equals("(")) {
				append(command, ")", 1);
				addCommandText(text);
				return;
			}
		}

		// }
		if (containCommandEndText("{", "}", text, prev, current, next)) {
			command.text = "";
			command.caretOffset = offset + 1;
			return;
		}
		// ]
		if (containCommandEndText("[", "]", text, prev, current, next)) {
			command.text = "";
			command.caretOffset = offset + 1;
			return;
		}
		// )
		if (containCommandEndText("(", ")", text, prev, current, next)) {
			command.text = "";
			command.caretOffset = offset + 1;
			return;
		}
		// ""
		if (text.equals("\"")) {
			if (containCommandText("\"")) {
				command.text = "";
				command.caretOffset = offset + 1;
				return;
			} else if (!prev.equals("\\")) {
				append(command, "\"", 1);
				addCommandText(text);
				return;
			}
		}
		// ''
		if (text.equals("'")) {
			if (containCommandText("'")) {
				command.text = "";
				command.caretOffset = offset + 1;
				return;
			} else if (!prev.equals("\\")) {
				append(command, "'", 1);
				addCommandText(text);
				return;
			}
		}
		// clear the stack
		if (text.equals(";") || text.equals("\n") || text.equals("\r")) {
			commandTextList.clear();
		}

		// JSDoc
		if (text.equals("\r\n") || text.equals("\n") || text.equals("\r")) {
			try {
				String currLine = getLineText(document, command.offset);
				String nextLine = getLineText(document, command.offset + text.length());

				String trim = currLine.trim();

				if ((trim.endsWith("/*") || trim.endsWith("/**")) && prev.equals("*")) {
					String indent = getIndent(currLine);
					StringBuilder jsdoc = new StringBuilder();

					if (nextLine.indexOf("*") == -1) {
						jsdoc.append(indent).append(" * ").append(text);
						if (nextLine.length() > 0 && trim.endsWith("/**")) {
							String source = document.get();
							source = source.substring(0, command.offset - 3) + "   "
									+ source.subSequence(command.offset, source.length());

							int currentLine = document.getLineOfOffset(command.offset);
							int nextLineEndOffset = document.getLineOffset(currentLine + 1)
									+ document.getLineLength(currentLine + 1);

							JavaScriptModel model = new JavaScriptModel(HTMLUtil.getActiveFile(), source);
							JavaScriptContext context = model.getContextFromOffset(nextLineEndOffset);

							if (context.getTarget() != null && context.getTarget().getFunction() != null) {
								JavaScriptFunction func = context.getTarget().getFunction();
								JavaScriptArgument[] arguments = func.getArguments();
								if (arguments.length > 0) {
									for (JavaScriptArgument arg : arguments) {
										jsdoc.append(indent).append(" * @param {} ").append(arg.getName()).append(text);
									}
								}
								jsdoc.append(indent).append(" * @returns {} ").append(text);
							}
						}
						jsdoc.append(indent).append(" */");

						append(command, jsdoc.toString(), indent.length() + 5);

					} else {
						append(command, indent + " * ", indent.length() + 5);
					}
				}
				if (trim.startsWith("*") && !trim.endsWith("*/")) {
					String indent = getIndent(currLine);
					append(command, indent + "* ", indent.length() + 4);
				}
			} catch (BadLocationException ex) {
			}
		}
		super.customizeDocumentCommand(document, command);
	}

	private static String getLineText(IDocument document, int offset) {
		try {
			int line = document.getLineOfOffset(offset);
			IRegion reg = document.getLineInformation(line);
			return document.get(reg.getOffset(), reg.getLength());
		} catch (BadLocationException ex) {
			return "";
		}
	}

	private static String getIndent(String line) throws BadLocationException {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (c == ' ' || c == '\t') {
				sb.append(c);
			} else {
				break;
			}
		}
		return sb.toString();
	}

	private void append(DocumentCommand command, String append, int position) {
		command.text = command.text + append;
		command.doit = false;
		command.shiftsCaret = false;
		command.caretOffset = command.offset + position;
	}

	private void addCommandText(String text) {
		if (commandTextList.size() > 10) {
			commandTextList.remove(0);
		}
		commandTextList.add(text);
	}

	private boolean containCommandEndText(String startBracket, String endBracket, String input, String start,
			String current, String end) {
		if (!endBracket.equals(input)) {
			return false;
		}

		if (containCommandText(startBracket)) {
			return true;
		}

		if (current.equals(input) && (";".equals(end) || "\n".equals(end) || "\r".equals(end))) {
			return true;
		}
		return false;
	}

	private boolean containCommandText(String text) {
		if (commandTextList.isEmpty()) {
			return false;
		}

		String last = commandTextList.get(commandTextList.size() - 1);
		if ("\"".equals(last) && !"\"".equals(text)) {
			return false;
		}
		if ("\'".equals(last) && !"\'".equals(text)) {
			return false;
		}

		for (int i = commandTextList.size() - 1; i >= 0; i--) {
			if (text.equals(commandTextList.get(i))) {
				for (int j = i; j < commandTextList.size(); j++) {
					commandTextList.remove(j);
				}
				return true;
			}
		}
		return false;
	}
}
