package tern.server.protocol.outline;

import tern.server.protocol.IJSONObjectHelper;
import tern.server.protocol.ITernResultsCollector;

/**
 * Tern outline collector.
 *
 */
public interface ITernOutlineCollector extends ITernResultsCollector {

	IJSNodeRoot createRoot();

	IJSNode createNode(String name, String type, String kind, String value, Long start, Long end, String file,
			IJSNode parent, Object jsonNode, IJSONObjectHelper helper);

}
