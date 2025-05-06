package tern.eclipse.ide.internal.ui.console;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.part.IPageBookViewPage;

public class TernConsolePageParticipant implements IConsolePageParticipant {

	private TernConsole fConsole;
	private ConsoleTerminateAction fTerminate;

	@Override
	public void init(IPageBookViewPage page, IConsole console) {
		this.fConsole = (TernConsole) console;
		this.fTerminate = new ConsoleTerminateAction(fConsole.getProject());
		IActionBars actionBars = page.getSite().getActionBars();
		configureToolBar(actionBars.getToolBarManager());
	}

	protected void configureToolBar(IToolBarManager mgr) {
		mgr.appendToGroup(IConsoleConstants.LAUNCH_GROUP, this.fTerminate);
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
		if (this.fTerminate != null) {
			this.fTerminate.dispose();
			this.fTerminate = null;
		}
		this.fConsole = null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(@SuppressWarnings("rawtypes") Class required) {
		return null;
	}

}
