package tern.eclipse.jface.text;

import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.AbstractReusableInformationControlCreator;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IInformationControlExtension4;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("restriction")
public class HoverControlCreator extends
		AbstractReusableInformationControlCreator {
	/**
	 * The information presenter control creator.
	 * 
	 */
	private final IInformationControlCreator fInformationPresenterControlCreator;
	/**
	 * <code>true</code> to use the additional info affordance,
	 * <code>false</code> to use the hover affordance.
	 */
	//private final boolean fAdditionalInfoAffordance;

	/**
	 * @param informationPresenterControlCreator
	 *            control creator for enriched hover
	 */
	public HoverControlCreator(
			IInformationControlCreator informationPresenterControlCreator) {
		this(informationPresenterControlCreator, false);
	}

	/**
	 * @param informationPresenterControlCreator
	 *            control creator for enriched hover
	 * @param additionalInfoAffordance
	 *            <code>true</code> to use the additional info affordance,
	 *            <code>false</code> to use the hover affordance
	 */
	public HoverControlCreator(
			IInformationControlCreator informationPresenterControlCreator,
			boolean additionalInfoAffordance) {
		fInformationPresenterControlCreator = informationPresenterControlCreator;
		//fAdditionalInfoAffordance = additionalInfoAffordance;
	}

	/*
	 * @see org.eclipse.jdt.internal.ui.text.java.hover.
	 * AbstractReusableInformationControlCreator
	 * #doCreateInformationControl(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	public IInformationControl doCreateInformationControl(Shell parent) {
		String tooltipAffordanceString = "Press F2 for focus";
		if (BrowserInformationControl.isAvailable(parent)) {
			String font = JFaceResources.DIALOG_FONT;
			BrowserInformationControl iControl = new BrowserInformationControl(
					parent, font, tooltipAffordanceString) {
				/*
				 * @see org.eclipse.jface.text.IInformationControlExtension5#
				 * getInformationPresenterControlCreator()
				 */
				@Override
				public IInformationControlCreator getInformationPresenterControlCreator() {
					return fInformationPresenterControlCreator;
				}
			};
			addLinkListener(iControl);
			return iControl;
		} else {
			return new DefaultInformationControl(parent,
					tooltipAffordanceString);
		}
	}

	protected void addLinkListener(BrowserInformationControl control) {
		HoverLocationListener.addLinkListener(control);
	}

	/*
	 * @see org.eclipse.jdt.internal.ui.text.java.hover.
	 * AbstractReusableInformationControlCreator
	 * #canReuse(org.eclipse.jface.text.IInformationControl)
	 */
	@Override
	public boolean canReuse(IInformationControl control) {
		if (!super.canReuse(control))
			return false;

		if (control instanceof IInformationControlExtension4) {
			String tooltipAffordanceString = "Press F2 for focus";
			((IInformationControlExtension4) control)
					.setStatusText(tooltipAffordanceString);
		}

		return true;
	}

}
