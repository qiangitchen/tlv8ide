package tern.eclipse.ide.genericeditor.contentassist;

import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;

import tern.ITernFile;
import tern.ITernProject;
import tern.eclipse.ide.ui.contentassist.JSTernCompletionAsyncCollector;
import tern.eclipse.ide.ui.contentassist.JSTernCompletionProposal;
import tern.server.protocol.completions.TernCompletionProposalRec;

public class GenericeditorTernCompletionCollector extends JSTernCompletionAsyncCollector {

	public static final int TERN_RELEVANT = 10000;

	public GenericeditorTernCompletionCollector(List<ICompletionProposal> proposals, int startOffset,
			ITernFile ternFile, ITernProject project) {
		super(proposals, startOffset, ternFile, project);
	}

	@Override
	protected JSTernCompletionProposal createProposal(TernCompletionProposalRec proposal) {
		return new GenericeditorTernCompletionProposal(proposal);
	}

	@Override
	protected ICompletionProposal createTimeoutProposal(int startOffset, TimeoutReason reason) {
		return new GenericeditorTimeoutProposal(startOffset, reason);
	}

}
