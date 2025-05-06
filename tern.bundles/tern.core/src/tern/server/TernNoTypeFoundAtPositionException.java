package tern.server;

import tern.TernException;

/**
 * Exception throw when ternjs throws "No type found at the given position."
 *
 */
public class TernNoTypeFoundAtPositionException extends TernException {

	private static final long serialVersionUID = -5360089212208602546L;

	public TernNoTypeFoundAtPositionException(String message) {
		super(message);
	}

}
