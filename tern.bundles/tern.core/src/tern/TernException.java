package tern;

/**
 * Tern Exception.
 * 
 */
public class TernException extends Exception {

	private static final long serialVersionUID = 4202429014735034363L;

	public TernException(String message) {
		super(message);

	}

	public TernException(String message, Throwable e) {
		super(message, e);
	}

	public TernException(Throwable e) {
		super(e);
	}

}
