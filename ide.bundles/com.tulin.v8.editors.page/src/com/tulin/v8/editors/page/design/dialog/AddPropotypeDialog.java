package com.tulin.v8.editors.page.design.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.tulin.v8.editors.page.Messages;

public class AddPropotypeDialog extends Dialog {
	private String sname;
	private String svalue;
	private Text name;
	private Text value;

	public AddPropotypeDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.getString("TLEditor.WEBDesign.addprotype.1"));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());

		Composite composite = new Composite(container, SWT.FILL);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);

		Label funcnamel = new Label(composite, SWT.NONE);
		funcnamel.setText(Messages.getString("TLEditor.WEBDesign.addprotype.2"));
		GridData funcnamectl = new GridData();
		funcnamel.setLayoutData(funcnamectl);

		name = new Text(composite, SWT.BORDER | SWT.FILL);
		GridData namectl = new GridData(GridData.FILL_HORIZONTAL);
		namectl.grabExcessHorizontalSpace = true;
		name.setLayoutData(namectl);

		Label funcvalue = new Label(composite, SWT.NONE);
		funcvalue.setText(Messages.getString("TLEditor.WEBDesign.addprotype.3"));
		GridData gfuncvalue = new GridData();
		funcvalue.setLayoutData(gfuncvalue);

		value = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		GridData valuectl = new GridData(GridData.FILL_BOTH);
		valuectl.widthHint = 300;
		valuectl.heightHint = 90;
		valuectl.grabExcessHorizontalSpace = true;
		valuectl.grabExcessVerticalSpace = true;
		value.setLayoutData(valuectl);

		return composite;
	}

	protected int getShellStyle() {
		return super.getShellStyle() | SWT.RESIZE;
	}

	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			setSname(name.getText());
			setSvalue(value.getText());
			if (sname == null || "".equals(sname)) {
				MessageDialog.openError(getShell(), Messages.getString("TLEditor.WEBDesign.addprotype.4"),
						Messages.getString("TLEditor.WEBDesign.addprotype.5"));
				return;
			}
		}
		super.buttonPressed(buttonId);
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String name) {
		this.sname = name;
	}

	public String getSvalue() {
		return svalue;
	}

	public void setSvalue(String value) {
		this.svalue = value;
	}

}
