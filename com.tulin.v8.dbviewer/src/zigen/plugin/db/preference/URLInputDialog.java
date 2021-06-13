/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.preference;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.editors.event.TextSelectionListener;

public class URLInputDialog extends Dialog {

	private static final int LEVEL_FIELD_WIDTH = 20;

	private static final int TEXT_FIELD_WIDTH = 50;

	protected Text driverText;

	protected Text urlText;

	protected static final String KEY_DRIVER = "driver"; //$NON-NLS-1$

	protected static final String KEY_URL = "url"; //$NON-NLS-1$

	protected Map valueMap = new HashMap();

	public String getStringValue(String key) {
		return (String) valueMap.get(key);
	}

	public void setStringValue(String key, String value) {
		valueMap.put(key, value);
	}

	public URLInputDialog(Shell parent) {
		super(parent);
	}

	protected void okPressed() {
		if (save()) {
			super.okPressed();
		}
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.getString("URLInputDialog.2")); //$NON-NLS-1$
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 5;
		composite.setLayout(layout);

		addDriverSection(composite);
		addURLSection(composite);

		return composite;
	}

	private String validate(String fieldName, Text text) {
		String value = text.getText();
		if (value == null || value.length() == 0) {
			text.setFocus();
			return fieldName + Messages.getString("URLInputDialog.3"); //$NON-NLS-1$
		}
		if (value.indexOf(",") > 0 || value.indexOf("|") > 0) { //$NON-NLS-1$ //$NON-NLS-2$
			text.setFocus();
			return fieldName + Messages.getString("URLInputDialog.6"); //$NON-NLS-1$
		}
		return null;
	}

	private boolean save() {

		String msg = null;
		if ((msg = validate("Driver", driverText)) != null) { //$NON-NLS-1$
			DbPlugin.getDefault().showWarningMessage(msg);
			return false;
		}

		if ((msg = validate("URL", urlText)) != null) { //$NON-NLS-1$
			DbPlugin.getDefault().showWarningMessage(msg);
			return false;
		}

		valueMap.put(KEY_DRIVER, driverText.getText());
		valueMap.put(KEY_URL, urlText.getText());

		return true;

	}

	private void addDriverSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);
		Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setText(Messages.getString("URLInputDialog.9")); //$NON-NLS-1$
		nameLabel.setLayoutData(getGridData(LEVEL_FIELD_WIDTH));
		driverText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		driverText.setLayoutData(getGridData(TEXT_FIELD_WIDTH));
		driverText.addFocusListener(new TextSelectionListener());

		driverText.addVerifyListener(new VerifyListener() {

			public void verifyText(VerifyEvent e) {
				verifyHandler(e);
			}
		});

		if (valueMap.containsKey(KEY_DRIVER)) {
			driverText.setText(getStringValue(KEY_DRIVER));
		}
	}

	private void addURLSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);
		Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setText(Messages.getString("URLInputDialog.10")); //$NON-NLS-1$
		nameLabel.setLayoutData(getGridData(LEVEL_FIELD_WIDTH));
		urlText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		urlText.setLayoutData(getGridData(TEXT_FIELD_WIDTH));
		urlText.addFocusListener(new TextSelectionListener());
		urlText.addVerifyListener(new VerifyListener() {

			public void verifyText(VerifyEvent e) {
				verifyHandler(e);
			}
		});
		if (valueMap.containsKey(KEY_URL)) {
			urlText.setText(getStringValue(KEY_URL));
		}
	}

	protected Point getInitialSize() {
		return new Point(400, 150);
	}

	private Composite createDefaultComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(data);

		return composite;
	}

	private GridData getGridData(int width) {
		GridData gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(width);
		return gd;
	}

	public void verifyHandler(VerifyEvent event) {
		if (event.character == ',' || event.character == '|') {
			event.doit = false;
		}
	}

	public void validateHandler(TypedEvent event) {
		Text text = (Text) event.widget;
		String value = text.getText();
		if (value.indexOf(",") > 0 || value.indexOf("|") > 0) { //$NON-NLS-1$ //$NON-NLS-2$
			text.setFocus();

		}
	}

}
