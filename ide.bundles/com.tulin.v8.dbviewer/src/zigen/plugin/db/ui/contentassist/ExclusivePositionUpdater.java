/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.contentassist;

import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.Position;

public class ExclusivePositionUpdater implements IPositionUpdater {

	private final String fCategory;

	public ExclusivePositionUpdater(String category) {
		fCategory = category;
	}

	public void update(DocumentEvent event) {

		int eventOffset = event.getOffset();
		int eventOldLength = event.getLength();
		int eventNewLength = event.getText() == null ? 0 : event.getText().length();
		int deltaLength = eventNewLength - eventOldLength;

		try {
			Position[] positions = event.getDocument().getPositions(fCategory);

			for (int i = 0; i != positions.length; i++) {

				Position position = positions[i];

				if (position.isDeleted())
					continue;

				int offset = position.getOffset();
				int length = position.getLength();
				int end = offset + length;

				if (offset >= eventOffset + eventOldLength)
					// position comes
					// after change - shift
					position.setOffset(offset + deltaLength);
				else if (end <= eventOffset) {
					// position comes way before change -
					// leave alone
				} else if (offset <= eventOffset && end >= eventOffset + eventOldLength) {
					// event completely internal to the position - adjust length
					position.setLength(length + deltaLength);
				} else if (offset < eventOffset) {
					// event extends over end of position - adjust length
					int newEnd = eventOffset;
					position.setLength(newEnd - offset);
				} else if (end > eventOffset + eventOldLength) {
					// event extends from before position into it - adjust offset
					// and length
					// offset becomes end of event, length adjusted accordingly
					int newOffset = eventOffset + eventNewLength;
					position.setOffset(newOffset);
					position.setLength(end - newOffset);
				} else {
					// event consumes the position - delete it
					position.delete();
				}
			}
		} catch (BadPositionCategoryException e) {
			// ignore and return
		}
	}

	public String getCategory() {
		return fCategory;
	}

}
