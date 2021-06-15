package com.tulin.v8.echarts.ui.editors.echt.dialog;

import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.tulin.v8.core.StringArray;
import com.tulin.v8.echarts.ui.editors.echt.Parameters;
import com.tulin.v8.ide.Sys;

public class ParameterDialog extends Dialog {
	Parameters parameter;

	public ParameterDialog(Shell parentShell, Parameters parameter) {
		super(parentShell);
		this.parameter = parameter;
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText("Parameters");
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());
		Composite composite = new Composite(container, SWT.FILL);
		composite.setLayout(new GridLayout(2, false));
		Map<String, String> params = parameter.getParamsMaps();
		for (String p : params.keySet()) {
			Label code = new Label(composite, SWT.NONE);
			code.setText(p);
			code.setLayoutData(new GridData(SWT.NONE));
			final Text scode = new Text(composite, SWT.FILL | SWT.BORDER);
			scode.setData(p);
			scode.setText(parameter.getParameters(p));
			GridData ttlay = new GridData(GridData.FILL_HORIZONTAL);
			ttlay.grabExcessHorizontalSpace = true;
			ttlay.minimumWidth = 200;
			ttlay.heightHint = 25;
			scode.setLayoutData(ttlay);
			scode.addModifyListener(new ModifyListener() {
				Text text = scode;

				@Override
				public void modifyText(ModifyEvent e) {
					parameter.setParameters((String) text.getData(), text.getText());
				}
			});
		}
		return composite;
	}

	protected int getShellStyle() {
		return super.getShellStyle() | SWT.RESIZE;
	}

	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			Map<String, String> params = parameter.getParamsMaps();
			StringArray pp = new StringArray();
			for (String p : params.keySet()) {
				pp.push(p + "=" + params.get(p));
			}
			Sys.printMsg(pp.join(","));
		}
		super.buttonPressed(buttonId);
	}

}
