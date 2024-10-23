package tern.eclipse.ide.ui.hover;

import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;

import tern.eclipse.jface.text.HoverControlCreator;
import tern.eclipse.jface.text.HoverLocationListener;

/**
 * IDE tern hover control creator.
 *
 */
@SuppressWarnings("restriction")
public class IDEHoverControlCreator extends HoverControlCreator {

	private final ITernHoverInfoProvider provider;

	public IDEHoverControlCreator(
			IInformationControlCreator informationPresenterControlCreator,
			ITernHoverInfoProvider provider) {
		super(informationPresenterControlCreator);
		this.provider = provider;
	}

	public IDEHoverControlCreator(
			IInformationControlCreator informationPresenterControlCreator,
			boolean additionalInfoAffordance, ITernHoverInfoProvider provider) {
		super(informationPresenterControlCreator, additionalInfoAffordance);
		this.provider = provider;
	}

	@Override
	protected void addLinkListener(BrowserInformationControl control) {
		HoverLocationListener.addLinkListener(control,
				new IDEHoverLocationListener(control, provider));
	}
}
