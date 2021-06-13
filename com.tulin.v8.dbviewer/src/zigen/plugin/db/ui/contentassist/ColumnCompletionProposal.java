package zigen.plugin.db.ui.contentassist;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

public class ColumnCompletionProposal implements ICompletionProposal {

	private String fDisplayString;

	private String fReplacementString;

	private int fReplacementOffset;

	private int fReplacementLength;

	private int fCursorPosition;

	private Image fImage;

	private IContextInformation fContextInformation;

	private String fAdditionalProposalInfo;

	public ColumnCompletionProposal(String replacementString, int replacementOffset, int replacementLength, int cursorPosition) {
		this(replacementString, replacementOffset, replacementLength, cursorPosition, null, null, null, null);
	}

	public ColumnCompletionProposal(String replacementString, int replacementOffset, int replacementLength, int cursorPosition, Image image, String displayString,
			IContextInformation contextInformation, String additionalProposalInfo) {
		fReplacementString = replacementString;
		fReplacementOffset = replacementOffset;
		fReplacementLength = replacementLength;
		fCursorPosition = cursorPosition;
		fImage = image;
		fDisplayString = displayString;
		fContextInformation = contextInformation;
		fAdditionalProposalInfo = additionalProposalInfo;
	}

	public void apply(IDocument document) {
		try {
			document.replace(fReplacementOffset, fReplacementLength, fReplacementString);
		} catch (BadLocationException x) {
		}
	}

	public Point getSelection(IDocument document) {
		return new Point(fReplacementOffset + fCursorPosition, 0);
	}

	public IContextInformation getContextInformation() {
		return fContextInformation;
	}

	public Image getImage() {
		return fImage;
	}

	public String getDisplayString() {
		if (fDisplayString != null)
			return fDisplayString;
		return fReplacementString;
	}

	public String getAdditionalProposalInfo() {
		return fAdditionalProposalInfo;
	}
}
