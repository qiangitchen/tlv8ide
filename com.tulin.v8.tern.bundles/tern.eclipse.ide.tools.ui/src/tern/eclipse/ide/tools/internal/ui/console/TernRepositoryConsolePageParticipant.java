package tern.eclipse.ide.tools.internal.ui.console;

import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.part.IPageBookViewPage;

/**
 * Tern repository console page participant.
 *
 */
public class TernRepositoryConsolePageParticipant implements
		IConsolePageParticipant {

	//private TernRepositoryConsole fConsole;

	@Override
	public void init(IPageBookViewPage page, IConsole console) {
		//this.fConsole = (TernRepositoryConsole) console;
	}

	@Override
	public void activated() {
		// do nothing
	}

	@Override
	public void deactivated() {
		// do nothing
	}

	@Override
	public void dispose() {
		//this.fConsole = null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(@SuppressWarnings("rawtypes") Class required) {
		return null;
	}

}
