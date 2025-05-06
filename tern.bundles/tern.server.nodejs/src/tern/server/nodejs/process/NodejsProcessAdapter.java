package tern.server.nodejs.process;

import java.io.File;
import java.util.List;

/**
 * This adapter class provides default implementations for the methods described
 * by the {@link INodejsProcessListener} interface.
 * 
 * Classes that wish to deal with event can extend this class and override only
 * the methods which they are interested in.
 * 
 */
public class NodejsProcessAdapter implements INodejsProcessListener {

	@Override
	public void onCreate(INodejsProcess process, List<String> commands,
			File projectDir) {
	}

	@Override
	public void onStart(INodejsProcess process) {
	}

	@Override
	public void onData(INodejsProcess process, String line) {
	}

	@Override
	public void onStop(INodejsProcess process) {
	}

	@Override
	public void onError(INodejsProcess process, String line) {
	}

}
