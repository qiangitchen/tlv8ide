package com.tulin.v8.ide.views.navigator.dialog;

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

import com.tulin.v8.core.utils.IDUtils;
import com.tulin.v8.flowdesigner.ui.editors.process.element.FlowFolderElement;
import com.tulin.v8.ide.internal.FlowFolder;
import com.tulin.v8.ide.utils.FlowUtils;
import com.tulin.v8.ide.views.navigator.Messages;

public class NewFlowDrawDialog extends Dialog {
	private FlowFolder node;

	private String drawid;

	Text scode;
	Text sname;

	public NewFlowDrawDialog(Shell parentShell, FlowFolder node) {
		super(parentShell);
		this.node = node;
	}

	/**
	 * 在这个方法里构建Dialog中的界面内容
	 */
	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.getString("ModelView.EditFlowDraw.0"));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());
		Composite composite = new Composite(container, SWT.FILL);
		composite.setLayout(new GridLayout(2, false));
		Label code = new Label(composite, SWT.NONE);
		code.setText(Messages.getString("ModelView.EditFlowDraw.2"));
		code.setLayoutData(new GridData(SWT.NONE));
		scode = new Text(composite, SWT.FILL | SWT.BORDER);
		scode.setText(IDUtils.getGUID());
		GridData ttlay = new GridData(GridData.FILL_HORIZONTAL);
		ttlay.grabExcessHorizontalSpace = true;
		ttlay.minimumWidth = 200;
		ttlay.heightHint = 25;
		scode.setLayoutData(ttlay);
		Label name = new Label(composite, SWT.NONE);
		name.setText(Messages.getString("ModelView.EditFlowDraw.3"));
		name.setLayoutData(new GridData(SWT.NONE));
		sname = new Text(composite, SWT.FILL | SWT.BORDER);
		sname.setLayoutData(ttlay);
		return composite;
	}

	protected int getShellStyle() {
		return super.getShellStyle() | SWT.RESIZE;
	}

	/**
	 * Dialog点击按钮时执行的方法
	 */
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			if ("".equals(scode.getText()) || scode.getText() == null || "".equals(sname.getText())
					|| sname.getText() == null) {
				MessageDialog.openInformation(getShell(), Messages.getString("ModelView.EditFlowDraw.4"),
						Messages.getString("ModelView.EditFlowDraw.5"));
				return;
			}
			drawid = IDUtils.getGUID();
			FlowFolderElement element = node.getElement();
			FlowUtils.addFlowDraw(drawid, scode.getText(), sname.getText(), element.getSid());
		}
		super.buttonPressed(buttonId);
	}

	public String getDrawid() {
		return drawid;
	}

	public void setDrawid(String drawid) {
		this.drawid = drawid;
	}

}
