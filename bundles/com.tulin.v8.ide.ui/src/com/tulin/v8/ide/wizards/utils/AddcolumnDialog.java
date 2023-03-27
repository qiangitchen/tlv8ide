package com.tulin.v8.ide.wizards.utils;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.tulin.v8.core.DBUtils;
import com.tulin.v8.ide.utils.DataType;
import com.tulin.v8.ide.utils.StudioUtil;

public class AddcolumnDialog extends Dialog {
	public String dbkey;
	public String textv;
	public String namev;
	public String datatypev;
	private Text text;
	private Text name;
	private Combo datatype;

	public AddcolumnDialog(Shell parentShell) {
		super(parentShell);
	}

	public void create() {
		super.create();
		getShell().setText(Messages.getString("wizards.addcolumn.title"));
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.FILL);
		GridLayout layout = new GridLayout(4, false);
		composite.setLayout(layout);

		Label textlabel = new Label(composite, SWT.NONE);
		textlabel.setText(Messages.getString("wizards.addcolumn.comment"));
		text = new Text(composite, SWT.BORDER);
		GridData tgl = new GridData(GridData.FILL_HORIZONTAL);
		tgl.widthHint = 300;
		tgl.grabExcessHorizontalSpace = true;
		tgl.horizontalSpan = 3;
		text.setLayoutData(tgl);

		Label namelabel = new Label(composite, SWT.NONE);
		namelabel.setText(Messages.getString("wizards.addcolumn.columnm"));
		name = new Text(composite, SWT.BORDER);
		GridData ngl = new GridData(GridData.FILL_HORIZONTAL);
		ngl.widthHint = 300;
		ngl.grabExcessHorizontalSpace = true;
		ngl.horizontalSpan = 3;
		name.setLayoutData(ngl);

		Label typelabel = new Label(composite, SWT.NONE);
		typelabel.setText(Messages.getString("wizards.addcolumn.columnt"));
		datatype = new Combo(composite, SWT.DROP_DOWN);
		datatype.setItems(DataType.dataType);
		GridData dgl = new GridData(GridData.FILL_HORIZONTAL);
		dgl.widthHint = 300;
		ngl.grabExcessHorizontalSpace = true;
		dgl.horizontalSpan = 3;
		datatype.setLayoutData(dgl);
		datatype.setText("string");

		text.addListener(SWT.FocusOut, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				String cellname = "F" + StudioUtil.Hanzi2UPinyin(text.getText());
				if (cellname.length() > 10) {
					cellname = "F" + StudioUtil.getSimPinYinByString(text.getText(), true);
				}
				if (DBUtils.IsPostgreSQL(dbkey)) {
					cellname = cellname.toLowerCase();
				}
				name.setText(cellname);
				validate();
			}
		});

		datatype.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				validate();
			}
		});

		text.addListener(SWT.KeyUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.keyCode == 13) {
					name.setFocus();
				}
			}
		});

		name.addListener(SWT.KeyUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.keyCode == 13) {
					datatype.setFocus();
				}
			}
		});

		name.addListener(SWT.KeyUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.keyCode == 13) {
					// ensureAction.run();
				}
			}
		});

		return composite;
	}

	private void validate() {
		// 添加自定义对话框的校验方法
		boolean selected = (text.getText() != null && !"".equals(text.getText()) && !"".equals(name.getText())
				&& name.getText() != null && datatype.getText() != null);
		getButton(IDialogConstants.OK_ID).setEnabled(selected);
	}

	// protected void createButtonsForButtonBar(Composite parent) {
	// createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
	// true);
	// createButton(parent, IDialogConstants.CANCEL_ID,
	// IDialogConstants.CANCEL_LABEL, false);
	// }

	@Override
	protected void okPressed() {
		textv = text.getText();
		namev = name.getText();
		datatypev = datatype.getText();
		super.okPressed();
	}

}
