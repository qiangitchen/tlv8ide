package tern.server.protocol.definition;

import tern.server.protocol.ITernResultsCollector;

public interface ITernDefinitionCollector extends ITernResultsCollector {

	void setDefinition(String file, Long start, Long end);
}
