package tern.server.protocol.outline;

import tern.server.protocol.TernQuery;

/**
 * Tern Outline Query.
 * 
 * @see https://github.com/angelozerr/tern-outline
 *
 */
public class TernOutlineQuery extends TernQuery {

	private static final long serialVersionUID = 1L;

	private static final String OUTLINE_TYPE_QUERY = "outline";

	public TernOutlineQuery(String file) {
		super(OUTLINE_TYPE_QUERY);
		setFile(file);
	}

}
