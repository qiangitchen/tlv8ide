package com.tulin.v8.ide.views.navigator.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.tulin.v8.core.Sys;
import com.tulin.v8.generator.CodeGenerator;
import com.tulin.v8.ide.views.navigator.action.Messages;

import zigen.plugin.db.core.TablePKColumn;
import zigen.plugin.db.ui.internal.ITable;

public class NewMapperDialog extends Dialog {
	ITable table;

	Text tableName;
	Text keyField;
	Button isAutoincrementKey;
	Text packgeName;
	Text modelName;
	Button createController;

	private Button createService;

	public NewMapperDialog(Shell parentShell, ITable table) {
		super(parentShell);
		this.table = table;
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.getString("View.Action.NewMapper.title"));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());
		Composite composite = new Composite(container, SWT.FILL);
		composite.setLayout(new GridLayout(2, false));

		Label code = new Label(composite, SWT.NONE);
		code.setText(Messages.getString("View.Action.NewMapper.tname"));
		code.setLayoutData(new GridData(SWT.NONE));
		tableName = new Text(composite, SWT.FILL | SWT.BORDER);
		GridData ttlay = new GridData(GridData.FILL_HORIZONTAL);
		ttlay.grabExcessHorizontalSpace = true;
		ttlay.minimumWidth = 200;
		ttlay.heightHint = 25;
		tableName.setLayoutData(ttlay);

		Label name = new Label(composite, SWT.NONE);
		name.setText(Messages.getString("View.Action.NewMapper.keyfield"));
		name.setLayoutData(new GridData(SWT.NONE));
		keyField = new Text(composite, SWT.FILL | SWT.BORDER);
		keyField.setLayoutData(ttlay);

		Label autocl = new Label(composite, SWT.NONE);
		autocl.setText(Messages.getString("View.Action.NewMapper.isAutoincrementKey"));
		autocl.setLayoutData(new GridData(SWT.NONE));
		isAutoincrementKey = new Button(composite, SWT.CHECK);
		isAutoincrementKey.setLayoutData(ttlay);

		Label packgeNameLable = new Label(composite, SWT.NONE);
		packgeNameLable.setText(Messages.getString("View.Action.NewMapper.pname"));
		packgeNameLable.setLayoutData(new GridData(SWT.NONE));
		packgeName = new Text(composite, SWT.FILL | SWT.BORDER);
		packgeName.setLayoutData(ttlay);

		Label modelNameLable = new Label(composite, SWT.NONE);
		modelNameLable.setText(Messages.getString("View.Action.NewMapper.mname"));
		modelNameLable.setLayoutData(new GridData(SWT.NONE));
		modelName = new Text(composite, SWT.FILL | SWT.BORDER);
		modelName.setLayoutData(ttlay);

		Label createSvc = new Label(composite, SWT.NONE);
		createSvc.setText(Messages.getString("View.Action.NewMapper.createService"));
		createSvc.setLayoutData(new GridData(SWT.NONE));
		createService = new Button(composite, SWT.CHECK);
		createService.setLayoutData(ttlay);
		createService.setSelection(true);

		Label createCtl = new Label(composite, SWT.NONE);
		createCtl.setText(Messages.getString("View.Action.NewMapper.createController"));
		createCtl.setLayoutData(new GridData(SWT.NONE));
		createController = new Button(composite, SWT.CHECK);
		createController.setLayoutData(ttlay);
		createController.setSelection(true);

		tableName.setText(table.getName());
		tableName.setEnabled(false);
		String keyf = "fid";
		TablePKColumn[] ks = table.getTablePKColumns();
		if (ks != null && ks.length > 0) {
			keyf = ks[0].getColumnName();
		}
		keyField.setText(keyf);
		packgeName.setText("com.tlv8");
		modelName.setText(CodeGenerator.tableNameConvertUpperCamel(table.getName()));

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
			try {
				CodeGenerator codeGenerator = new CodeGenerator(table.getDataBase(), packgeName.getText(),
						keyField.getText());
				codeGenerator.genCodeByCustomModelName(tableName.getText(), modelName.getText(),
						isAutoincrementKey.getSelection(), createService.getSelection(),
						createController.getSelection());
			} catch (Exception e) {
				Sys.printErrMsg(e);
			}
		}
		super.buttonPressed(buttonId);
	}

}
