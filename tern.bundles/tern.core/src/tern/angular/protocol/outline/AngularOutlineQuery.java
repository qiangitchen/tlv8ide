package tern.angular.protocol.outline;

import tern.server.protocol.TernQuery;

/**
 * Angular Outline Query.
 *
 */
public class AngularOutlineQuery extends TernQuery {

	private static final long serialVersionUID = 1L;

	private static final String OUTLINE_TYPE_QUERY = "angular-outline";

	public AngularOutlineQuery() {
		super(OUTLINE_TYPE_QUERY);
	}

}
