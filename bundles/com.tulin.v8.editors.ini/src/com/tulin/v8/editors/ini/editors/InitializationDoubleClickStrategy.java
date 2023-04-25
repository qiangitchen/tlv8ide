package com.tulin.v8.editors.ini.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;

public class InitializationDoubleClickStrategy implements ITextDoubleClickStrategy {
	public void doubleClicked(ITextViewer textViewer) {
		int offset = textViewer.getSelectedRange().x;

		if (offset < 0) {
			return;
		}
		selectWord(textViewer, offset);
	}

	protected boolean selectWord(ITextViewer textViewer, int caretPos) {
		IDocument doc = textViewer.getDocument();
		try {
			int pos = caretPos;

			while (pos >= 0) {
				char c = doc.getChar(pos);
				if ((c != '.') && (c != '\\') && (!Character.isJavaIdentifierPart(c))) {
					break;
				}
				pos--;
			}

			int startPos = pos;

			pos = caretPos;
			int length = doc.getLength();

			while (pos < length) {
				char c = doc.getChar(pos);
				if ((c != '.') && (c != '\\') && (!Character.isJavaIdentifierPart(c))) {
					break;
				}
				if ((c == '.') && (pos + 1 < length) && (!Character.isJavaIdentifierPart(doc.getChar(pos + 1)))) {
					break;
				}
				pos++;
			}

			int endPos = pos;

			int offset = startPos + 1;
			int rangeLength = endPos - offset;
			textViewer.setSelectedRange(offset, rangeLength);

			return true;
		} catch (BadLocationException localBadLocationException) {
		}
		return false;
	}
}