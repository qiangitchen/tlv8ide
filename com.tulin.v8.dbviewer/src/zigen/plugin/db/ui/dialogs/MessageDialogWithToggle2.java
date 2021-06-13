/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class MessageDialogWithToggle2 extends MessageDialogWithToggle {

	private Button toggleButton2 = null;

	private String toggleMessage2 = "Defualt Message";

	private boolean toggleState2;

	public boolean getToggleState2() {
		return toggleState2;
	}

	public MessageDialogWithToggle2(Shell parentShell, String dialogTitle, Image image, String message, int dialogImageType, String[] dialogButtonLabels, int defaultIndex,
			String toggleMessage, boolean toggleState) {

		super(parentShell, dialogTitle, image, message, dialogImageType, dialogButtonLabels, defaultIndex, toggleMessage, toggleState);

	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		setToggleButton2(createToggleButton(composite));
		return composite;
	}

	protected Button createToggleButton2(Composite parent) {
		final Button button2 = new Button(parent, SWT.CHECK | SWT.LEFT);
		GridData data = new GridData(SWT.NONE);
		data.horizontalSpan = 2;
		button2.setLayoutData(data);
		button2.setFont(parent.getFont());
		button2.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				toggleState2 = button2.getSelection();
			}
		});
		return button2;
	}

	protected void setToggleButton2(Button button) {
		if (!button.isDisposed()) {
			button.setText(toggleMessage2);
			button.setSelection(toggleState2);
		}
		this.toggleButton2 = button;
	}

	public static MessageDialogWithToggle2 open(Shell parent, String title, String message, String toggleMessage, boolean toggleState, String toggleMessage2, boolean toggleState2) {
		MessageDialogWithToggle2 dialog = new MessageDialogWithToggle2(parent, title, null, // accept the default window icon
				message, QUESTION, new String[] {IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL}, 0, // yes is the default
				toggleMessage, toggleState);

		dialog.toggleMessage2 = toggleMessage2;
		dialog.toggleState2 = toggleState2;
		dialog.open();
		return dialog;
	}
}
