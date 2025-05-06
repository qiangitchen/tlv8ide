/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.tulin.v8.ide.views.navigator.action;

import org.eclipse.jdt.internal.ui.actions.AbstractToggleLinkingAction;

import com.tulin.v8.ide.views.navigator.ModelView;

@SuppressWarnings("restriction")
public class ToggleLinkingAction extends AbstractToggleLinkingAction {

	ModelView view;

	public ToggleLinkingAction(ModelView modelView) {
		this.view = modelView;
		setChecked(view.isLinkingEnabled());
	}

	public void run() {
		view.setLinkingEnabled(isChecked());
	}

}
