package tern.server.protocol.completions;

import tern.server.protocol.IJSONObjectHelper;
import tern.server.protocol.ITernResultsCollector;

/**
 * Collector to collect result of completion.
 * 
 */
public interface ITernCompletionCollector extends ITernResultsCollector {

	/**
	 * Collect an item completion.
	 * 
	 * @param proposal
	 *            object containing all required information about the proposal
	 * @param completion
	 *            object of completion item (ex : JsonObject)
	 * @param helper
	 *            the JSON Object helper to visit the given JSON completion object.
	 */
	void addProposal(TernCompletionProposalRec proposal, Object completion,
			IJSONObjectHelper helper);

}
