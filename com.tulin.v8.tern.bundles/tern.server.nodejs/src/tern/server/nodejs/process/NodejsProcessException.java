package tern.server.nodejs.process;

import tern.TernException;

public class NodejsProcessException extends TernException {

	private static final long serialVersionUID = 2572873614024553811L;

	public NodejsProcessException(String message) {
		super(message);
	}

	public NodejsProcessException(Throwable e) {
		super(e);
	}

}
