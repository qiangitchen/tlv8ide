package tern.server.protocol.guesstypes;

import tern.server.protocol.IJSONObjectHelper;
import tern.server.protocol.ITernResultsCollector;
import tern.server.protocol.completions.TernCompletionProposalRec;

public interface ITernGuessTypesCollector extends ITernResultsCollector {

	void addProposal(int arg, TernCompletionProposalRec proposal, Object completion, IJSONObjectHelper jsonManager);
}
