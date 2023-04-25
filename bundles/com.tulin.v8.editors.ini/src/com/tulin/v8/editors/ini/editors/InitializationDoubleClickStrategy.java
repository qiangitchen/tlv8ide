package com.tulin.v8.editors.ini.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;

public class InitializationDoubleClickStrategy implements ITextDoubleClickStrategy {
	public void doubleClicked(ITextViewer textViewer) {
		/* 18 */ int offset = textViewer.getSelectedRange().x;

		/* 20 */ if (offset < 0) {
			/* 21 */ return;
		}
		/* 23 */ selectWord(textViewer, offset);
	}

	protected boolean selectWord(ITextViewer textViewer, int caretPos) {
		/* 28 */ IDocument doc = textViewer.getDocument();
		try {
			/* 33 */ int pos = caretPos;

			/* 36 */ while (pos >= 0) {
				/* 37 */ char c = doc.getChar(pos);
				/* 38 */ if ((c != '.') &&
				/* 39 */ (c != '\\') &&
				/* 40 */ (!Character.isJavaIdentifierPart(c))) {
					break;
				}
				/* 44 */ pos--;
			}

			/* 47 */ int startPos = pos;

			/* 49 */ pos = caretPos;
			/* 50 */ int length = doc.getLength();

			/* 52 */ while (pos < length) {
				/* 53 */ char c = doc.getChar(pos);
				/* 54 */ if ((c != '.') &&
				/* 55 */ (c != '\\') &&
				/* 56 */ (!Character.isJavaIdentifierPart(c))) {
					break;
				}
				/* 59 */ if ((c == '.') && (pos + 1 < length)
						&& (!Character.isJavaIdentifierPart(doc.getChar(pos + 1)))) {
					break;
				}
				/* 63 */ pos++;
			}

			/* 66 */ int endPos = pos;

			/* 68 */ int offset = startPos + 1;
			/* 69 */ int rangeLength = endPos - offset;
			/* 70 */ textViewer.setSelectedRange(offset, rangeLength);

			/* 72 */ return true;
		} catch (BadLocationException localBadLocationException) {
		}
		/* 77 */ return false;
	}
}