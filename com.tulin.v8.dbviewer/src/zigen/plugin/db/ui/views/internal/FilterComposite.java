/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.PluginSettingsManager;
import zigen.plugin.db.ui.editors.event.TextSelectionListener;

public class FilterComposite {

	protected PluginSettingsManager pluginMgr = DbPlugin.getDefault().getPluginSettingsManager();

	// boolean checkFilterPattern;

	Button visibleCheck;

	Text filterText;

	Button regularExpressions;

	Button caseSensitive;

	public FilterComposite(final Composite composite, String groupName) {
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Group group = new Group(composite, SWT.NONE);
		group.setText(groupName);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setLayout(new GridLayout(1, false));

		Composite innerPanel1 = new Composite(group, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		innerPanel1.setLayout(gridLayout);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		innerPanel1.setLayoutData(data);
		visibleCheck = new Button(innerPanel1, SWT.CHECK);
		visibleCheck.setText(Messages.getString("FilterComposite.0")); //$NON-NLS-1$
		visibleCheck.setSelection(false);
		data = new GridData(GridData.FILL_HORIZONTAL);
		visibleCheck.setLayoutData(data);
		visibleCheck.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (visibleCheck.getSelection()) {
					filterText.setEnabled(true);
					regularExpressions.setEnabled(true);
					caseSensitive.setEnabled(true);
				} else {
					filterText.setEnabled(false);
					regularExpressions.setEnabled(false);
					caseSensitive.setEnabled(false);
				}
				setEnabled(visibleCheck.getSelection());
			}

		});

		filterText = new Text(innerPanel1, SWT.SINGLE | SWT.BORDER);
		filterText.setEnabled(false);
		data = new GridData(GridData.FILL_HORIZONTAL);
		filterText.setLayoutData(data);

		filterText.addFocusListener(new TextSelectionListener());
		filterText.setFont(DbPlugin.getDefaultFont());

		final Label label = new Label(innerPanel1, SWT.NONE);
		label.setText(Messages.getString("FilterComposite.1")); //$NON-NLS-1$
		data = new GridData(GridData.FILL_HORIZONTAL);
		label.setLayoutData(data);


		Composite innerPanel2 = new Composite(group, SWT.NONE);
		gridLayout = new GridLayout(3, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		innerPanel2.setLayout(gridLayout);
		data = new GridData(GridData.FILL_HORIZONTAL);
		innerPanel2.setLayoutData(data);

		regularExpressions = new Button(innerPanel2, SWT.CHECK);
		regularExpressions.setEnabled(false);
		regularExpressions.setText(Messages.getString("FilterComposite.2")); //$NON-NLS-1$
		regularExpressions.setSelection(false);
		data = new GridData();
		regularExpressions.setLayoutData(data);
		regularExpressions.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				label.setVisible(!regularExpressions.getSelection());
			}
		});

		caseSensitive = new Button(innerPanel2, SWT.CHECK);
		caseSensitive.setEnabled(false);
		caseSensitive.setText(Messages.getString("FilterComposite.3")); //$NON-NLS-1$
		caseSensitive.setSelection(false);
		data = new GridData();
		data.horizontalAlignment = GridData.END;
		caseSensitive.setLayoutData(data);


	}

	private void setEnabled(boolean b) {
		filterText.setEnabled(b);
		regularExpressions.setEnabled(b);
		caseSensitive.setEnabled(b);
	}


}
