package tern.eclipse.ide.internal.ui;

import org.eclipse.ui.IStartup;

/**
 * Need this to make TernNatureTester work from early start
 * 
 * @author Victor Rubezhny
 */
public class TernIDEStartup implements IStartup {

	@Override
	public void earlyStartup() {
		// EditorActivationTracker is thread-safe and registers asynchronously
		EditorActivationTracker.getInstance();
	}

}
