package com.tulin.v8.xml.internal.quickoutline;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.information.IInformationPresenter;

public class QuickOutlineHandler extends AbstractHandler {

	IInformationPresenter fPresenter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (fPresenter != null)
			fPresenter.showInformation();
		return null;
	}

	public void configure(IInformationPresenter presenter) {
		fPresenter = presenter;
	}

	public void dispose() {
		super.dispose();
		if (fPresenter != null) {
			fPresenter.uninstall();
			fPresenter = null;
		}
	}
}
