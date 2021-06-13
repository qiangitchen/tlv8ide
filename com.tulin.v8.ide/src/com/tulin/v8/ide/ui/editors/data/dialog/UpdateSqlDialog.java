package com.tulin.v8.ide.ui.editors.data.dialog;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.tulin.v8.ide.ui.editors.Messages;

public class UpdateSqlDialog extends Dialog {
	private Map<String, String> sqlMap;
	private String sqlText;
	private Text name;

	public UpdateSqlDialog(Shell parentShell) {
		super(parentShell);
	}

	public UpdateSqlDialog(Shell parentShell, Map<String, String> sqlMap) {
		super(parentShell);
		this.sqlMap = sqlMap;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.getString("TLEditor.dataEditor.4"));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());

		Composite composite = new Composite(container, SWT.FILL);
		GridLayout layout = new GridLayout(1, false);
		composite.setLayout(layout);

		Label funcnamel = new Label(composite, SWT.NONE);
		funcnamel.setText(Messages.getString("TLEditor.dataEditor.5"));
		GridData funcnamectl = new GridData(GridData.FILL_HORIZONTAL);
		funcnamectl.grabExcessHorizontalSpace = true;
		funcnamel.setLayoutData(funcnamectl);

		name = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		GridData namectl = new GridData(GridData.FILL_BOTH);
		namectl.widthHint = 600;
		namectl.heightHint = 400;
		namectl.grabExcessHorizontalSpace = true;
		namectl.grabExcessVerticalSpace = true;
		name.setLayoutData(namectl);
		String sqlStr = "";
		Set<String> key = sqlMap.keySet();
		for (Iterator it = key.iterator(); it.hasNext();) {
			String s = (String) it.next();
			sqlStr += sqlMap.get(s);
		}
		name.setText(sqlStr);

		return composite;
	}

	protected int getShellStyle() {
		return super.getShellStyle() | SWT.RESIZE | SWT.MAX;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			setSqlText(name.getText());
		}
		super.buttonPressed(buttonId);
	}

	public String getSqlText() {
		return sqlText;
	}

	public void setSqlText(String sqlText) {
		this.sqlText = sqlText;
	}

}
