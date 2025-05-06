package com.tulin.v8.webtools.ide.js.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;

public class JavaScriptDoubleClickStrategy implements ITextDoubleClickStrategy {
	protected ITextViewer fText;

	public void doubleClicked(ITextViewer part) {
		int pos = part.getSelectedRange().x;

		if (pos < 0) {
			return;
		}

		fText = part;

		selectWord(pos);
	}

	protected boolean selectWord(int caretPos) {

		IDocument doc = fText.getDocument();
		int startPos, endPos;

		try {

			int pos = caretPos;
			char c;

			while (pos >= 0) {
				c = doc.getChar(pos);
				if (Character.isWhitespace(c) || c == '=' || c == '/' || c == '"' || c == ':' || c == ',' || c == '['
						|| c == ']' || c == '\'' || c == '(' || c == ')' || c == '{' || c == '}' || c == '.' || c == ';'
						|| c == '-' || c == '+' || c == '*' || c == '%') {
					break;
				}
				--pos;
			}

			startPos = pos;

			pos = caretPos;
			int length = doc.getLength();

			while (pos < length) {
				c = doc.getChar(pos);
				if (Character.isWhitespace(c) || c == '=' || c == '/' || c == '"' || c == ':' || c == ',' || c == '['
						|| c == ']' || c == '\'' || c == '(' || c == ')' || c == '{' || c == '}' || c == '.' || c == ';'
						|| c == '-' || c == '+' || c == '*' || c == '%') {
					break;
				}
				++pos;
			}

			endPos = pos;
			selectRange(startPos, endPos);
			return true;

		} catch (BadLocationException x) {
		}

		return false;
	}

	private void selectRange(int startPos, int stopPos) {
		int offset = startPos + 1;
		int length = stopPos - offset;
		fText.setSelectedRange(offset, length);
	}
}