package tern.eclipse.ide.ui.contentassist;

import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;

import tern.ITernFile;
import tern.ITernProject;
import tern.server.protocol.IJSONObjectHelper;
import tern.server.protocol.ITernResultsAsyncCollector;
import tern.server.protocol.completions.TernCompletionProposalRec;

public class JSTernCompletionAsyncCollector extends JSTernCompletionCollector
		implements ITernResultsAsyncCollector {

	private boolean timedOut;
	private boolean contentAdded;

	private int startOffset;

	public JSTernCompletionAsyncCollector(List<ICompletionProposal> proposals,
			int startOffset, ITernFile ternFile, ITernProject project) {
		super(proposals, startOffset, ternFile, project);
		this.startOffset = startOffset;
	}

	@Override
	public void addProposal(TernCompletionProposalRec proposal,
			Object completion, IJSONObjectHelper jsonObjectHelper) {
		if (!timedOut) {
			contentAdded = true;
			super.addProposal(proposal, completion, jsonObjectHelper);
		}
	}

	@Override
	public void done() {
		if (!timedOut) {
			contentAdded = true;
		}
	}

	@Override
	public String getRequestDisplayName() {
		return "Calculating completion proposals...";
	}

	public void timeout(final TimeoutReason reason) {
		timedOut = true;
		if (!contentAdded) {
			proposals.add(createTimeoutProposal(startOffset, reason));
		}
	}

	protected ICompletionProposal createTimeoutProposal(int startOffset,
			TimeoutReason reason) {
		return new TimeoutProposal(startOffset, reason);
	}

}
