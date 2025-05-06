package tern.eclipse.jface.text;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.text.AbstractReusableInformationControlCreator;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("restriction")
public class PresenterControlCreator extends
		AbstractReusableInformationControlCreator {

	@Override
	public IInformationControl doCreateInformationControl(Shell parent) {
		if (BrowserInformationControl.isAvailable(parent)) {
			ToolBarManager tbm = new ToolBarManager(SWT.FLAT);
			TernBrowserInformationControl control = new TernBrowserInformationControl(
					parent, null, tbm);
			tbm.update(true);
			addLinkListener(control);
			return control;
		} else {
			return new DefaultInformationControl(parent, true);
		}
	}

	protected void addLinkListener(TernBrowserInformationControl control) {
		HoverLocationListener.addLinkListener(control);
	}

}
