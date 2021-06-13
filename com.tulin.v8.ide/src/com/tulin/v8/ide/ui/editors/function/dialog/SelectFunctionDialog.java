package com.tulin.v8.ide.ui.editors.function.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.tulin.v8.ide.ui.editors.function.Messages;
import com.tulin.v8.ide.ui.editors.function.SelectFunction;

public class SelectFunctionDialog extends Dialog {

	private Text name;
	private Tree foldlist;
	private String sname;
	private String surl;
	private String process;
	private String activity;

	public SelectFunctionDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.getString("editors.FunEditor.eddialogaddti"));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());

		Composite composite = new Composite(container, SWT.FILL);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);

		Label funcnamel = new Label(composite, SWT.NONE);
		funcnamel.setText(Messages.getString("editors.FunEditor.fname"));
		GridData funcnamectl = new GridData();
		funcnamel.setLayoutData(funcnamectl);

		name = new Text(composite, SWT.BORDER);
		GridData namectl = new GridData(GridData.FILL_HORIZONTAL);
		namectl.grabExcessHorizontalSpace = true;
		name.setLayoutData(namectl);

		foldlist = new Tree(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.FILL | SWT.V_SCROLL);
		GridData foldlistctl = new GridData(GridData.FILL_BOTH);
		foldlistctl.horizontalSpan = 2;
		foldlistctl.grabExcessHorizontalSpace = true;
		foldlist.setLayoutData(foldlistctl);
		SelectFunction.loadFileTree(foldlist);

		return composite;
	}

	protected int getShellStyle() {
		return super.getShellStyle() | SWT.RESIZE;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(600, 450);
	}

	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			String fName = name.getText();
			TreeItem[] selectItems = foldlist.getSelection();
			if (fName == null || "".equals(fName)) {
				MessageDialog.openError(getShell(), Messages.getString("editors.FunEditor.funtreeSet"),
						Messages.getString("editors.FunEditor.funnameNempty"));
				return;
			}
			if (selectItems.length > 0) {
				TreeItem treeItem = selectItems[0];
				if (!treeItem.getText().toLowerCase().contains(".htm")
						&& !treeItem.getText().toLowerCase().contains(".jsp")) {
					MessageDialog.openError(getShell(), Messages.getString("editors.FunEditor.funtreeSet"),
							Messages.getString("editors.FunEditor.selecetpFile"));
					return;
				} else {
					setSurl(treeItem.getText());
					String p = getSurl();
					setProcess(p.substring(0, p.lastIndexOf("/")) + "/process");
					String a = p.substring(p.lastIndexOf("/") + 1);
					setActivity(a.substring(0, a.indexOf(".")));
				}
			} else {
				MessageDialog.openError(getShell(), Messages.getString("editors.FunEditor.funtreeSet"),
						Messages.getString("editors.FunEditor.selecetpFile"));
				return;
			}
			setSname(fName);
		}
		super.buttonPressed(buttonId);
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getSurl() {
		return surl;
	}

	public void setSurl(String surl) {
		this.surl = surl;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}
}
