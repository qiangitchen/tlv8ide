package tern.eclipse.ide.ui.hover;

import tern.eclipse.jface.text.HoverLocationListener;
import tern.eclipse.jface.text.PresenterControlCreator;
import tern.eclipse.jface.text.TernBrowserInformationControl;

/**
 * IDE presenter control creator
 *
 */
public class IDEPresenterControlCreator extends PresenterControlCreator {

	private final ITernHoverInfoProvider provider;

	public IDEPresenterControlCreator(ITernHoverInfoProvider provider) {
		this.provider = provider;
	}

	@Override
	protected void addLinkListener(TernBrowserInformationControl control) {
		HoverLocationListener.addLinkListener(control,
				new IDEHoverLocationListener(control, provider));

	}
}
