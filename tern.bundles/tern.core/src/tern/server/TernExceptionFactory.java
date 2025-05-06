package tern.server;

import tern.TernException;

public class TernExceptionFactory {

	private static final String NO_TYPE_FOUND_AT_THE_GIVEN_POSITION = "No type found at the given position.";

	private TernExceptionFactory() {
	}

	public static TernException create(String message) {
		if (message.indexOf(NO_TYPE_FOUND_AT_THE_GIVEN_POSITION) != - 1) {
			return new TernNoTypeFoundAtPositionException(message);
		}
		// TODO add other ternjs error
		return new TernException(message);
	}
}
