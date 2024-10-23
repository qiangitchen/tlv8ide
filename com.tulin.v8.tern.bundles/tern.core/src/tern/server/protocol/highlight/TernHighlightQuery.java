package tern.server.protocol.highlight;

import tern.server.protocol.TernQuery;

/**
 * Tern highlight query to use the tern highlight plugin
 * https://github.com/katspaugh/tj-mode
 *
 */
public class TernHighlightQuery extends TernQuery {

	private static final long serialVersionUID = 1L;

	private static final String HIGHLIGHT_TYPE_QUERY = "highlight";

	private static final String TEXT_FIELD_NAME = "text";

	public TernHighlightQuery(String text) {
		super(HIGHLIGHT_TYPE_QUERY);
		super.set(TEXT_FIELD_NAME, text);
	}

}
