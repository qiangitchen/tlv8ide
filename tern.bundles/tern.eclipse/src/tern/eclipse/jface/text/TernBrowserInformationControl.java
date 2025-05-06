package tern.eclipse.jface.text;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("restriction")
public class TernBrowserInformationControl extends BrowserInformationControl {

	public TernBrowserInformationControl(Shell parent, String symbolicFontName,
			ToolBarManager toolBarManager) {
		super(parent, symbolicFontName, toolBarManager);
	}

	@Override
	public IInformationControlCreator getInformationPresenterControlCreator() {
		// Hack: We don't wan't to have auto-enrichment when the mouse moves
		// into the hover,
		// but we do want F2 to persist the hover. The framework has no way to
		// distinguish the
		// two requests, so we have to implement this aspect.
		for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
			if ("canMoveIntoInformationControl".equals(element.getMethodName()) //$NON-NLS-1$
					&& "org.eclipse.jface.text.AbstractHoverInformationControlManager".equals(element.getClassName())) //$NON-NLS-1$
				return null; // do not enrich bracket hover
		}
		return super.getInformationPresenterControlCreator();
	}

}
