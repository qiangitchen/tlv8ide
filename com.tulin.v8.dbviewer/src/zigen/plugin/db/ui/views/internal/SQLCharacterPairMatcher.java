/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.ICharacterPairMatcher;

public class SQLCharacterPairMatcher implements ICharacterPairMatcher {

	private int depth;

	private int offset;

	private IDocument doc;

	public void clear() {}

	public void dispose() {}

	public int getAnchor() {
		return LEFT;
	}

	public IRegion match(IDocument document, int offset) {
		this.doc = document;
		this.offset = offset;

		if (offset < 0)
			return null;

		try {
			depth = 0;
			if (offset > 0 && doc.getChar(offset - 1) == '(') {
				depth++;
				for (int i = offset; i < doc.getLength(); i++) {
					char c = doc.getChar(i);
					if (c == '(') {
						depth++;
					} else if (c == ')') {
						depth--;
						if (depth == 0) {
							return new Region(i, 1);
						}
					}
				}

			} else if (offset <= doc.getLength() && doc.getChar(offset - 1) == ')') {
				depth++;
				for (int i = offset - 2; 0 <= i; i--) {
					char c = doc.getChar(i);
					if (c == ')') {
						depth++;
					} else if (c == '(') {
						depth--;
						if (depth == 0) {
							return new Region(i, 1);
						}
					}

				}
			}
		} catch (BadLocationException e) {

		}
		return null;
	}

}
