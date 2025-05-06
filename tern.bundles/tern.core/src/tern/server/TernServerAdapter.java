package tern.server;

/**
 * This adapter class provides default implementations for the methods described
 * by the {@link ITernServerListener} interface.
 * 
 * Classes that wish to deal with event can extend this class and override only
 * the methods which they are interested in.
 * 
 */
public class TernServerAdapter implements ITernServerListener {

	@Override
	public void onStart(ITernServer server) {

	}

	@Override
	public void onStop(ITernServer server) {

	}

}
