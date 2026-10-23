/*
 * TLV8 DbViewer - Export scope choice dialog
 *
 */
package zigen.plugin.db.export;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.Messages;

/**
 * Dialog that lets the user choose the export scope: structure only (DDL) or
 * structure + data (INSERT statements).
 *
 */
public class ExportScopeDialog extends Dialog {

	private Button ddlOnlyRadio;

	private Button withDataRadio;

	private boolean includeData = false;

	public ExportScopeDialog(Shell parentShell) {
		super(parentShell);
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(Messages.getString("ExportScopeDialog.0")); //$NON-NLS-1$
	}

	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);

		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 10;
		layout.marginHeight = 10;
		container.setLayout(layout);

		Label label = new Label(container, SWT.NONE);
		label.setText(Messages.getString("ExportScopeDialog.1")); //$NON-NLS-1$

		ddlOnlyRadio = new Button(container, SWT.RADIO);
		ddlOnlyRadio.setText(Messages.getString("ExportScopeDialog.2")); //$NON-NLS-1$
		ddlOnlyRadio.setSelection(true);

		withDataRadio = new Button(container, SWT.RADIO);
		withDataRadio.setText(Messages.getString("ExportScopeDialog.3")); //$NON-NLS-1$

		return area;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	protected void okPressed() {
		includeData = withDataRadio.getSelection();
		super.okPressed();
	}

	/**
	 * @return true if the user chose "structure + data".
	 */
	public boolean isIncludeData() {
		return includeData;
	}

}
