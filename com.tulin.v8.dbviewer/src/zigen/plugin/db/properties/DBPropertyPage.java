/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.PropertyPage;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.PropertyPageChangeListener;
import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;

public class DBPropertyPage extends PropertyPage {

	private static final String CONNECT_DB = Messages.getString("DBPropertyPage.0"); //$NON-NLS-1$

	public static final String QUALIFIER = "zigen.plugin.db"; //$NON-NLS-1$

	public static final String SELECTED_DB = "SELECTED_DB"; //$NON-NLS-1$

	public static final String INITIAL_VALUE = Messages.getString("DBPropertyPage.3"); //$NON-NLS-1$

	private Combo selectCombo;

	private IDBConfig[] configs;

	private IResource resource;

	public DBPropertyPage() {
		super();
	}

	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);

		createSelectCombo(composite);
		// addSeparator(composite);
		return composite;
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

	private void createSelectCombo(Composite parent) {

		Composite composite = createDefaultComposite(parent);
		Label pathLabel = new Label(composite, SWT.NONE);
		pathLabel.setText(CONNECT_DB);

		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 200;

		selectCombo = new Combo(composite, SWT.READ_ONLY);
		selectCombo.setLayoutData(data);
		selectCombo.add(INITIAL_VALUE);

		configs = DBConfigManager.getDBConfigs();

		Object obj = getElement();

		if (obj instanceof IResource) {
			resource = (IResource) obj;
			setDefaultValue(resource);
		}

	}

	private void setDefaultValue(IResource resource) {
		try {
			String dbName = resource.getPersistentProperty(new QualifiedName(QUALIFIER, SELECTED_DB));

			for (int i = 0; i < configs.length; i++) {
				IDBConfig w_config = configs[i];
				selectCombo.add(w_config.getDbName());
				if (i == 0)
					selectCombo.select(0);
				if (dbName != null && dbName.equals(w_config.getDbName())) {
					// selectCombo.select(i);
					selectCombo.select(i + 1);

				}
			}
		} catch (Exception e) {
			DbPlugin.log(e);
		}

	}

	protected void performDefaults() {
		selectCombo.select(0);
		PropertyPageChangeListener.firePropertyPageChangeListener(this, PropertyPageChangeListener.EVT_SetDataBase);
	}

	public boolean performOk() {
		try {

			if (resource != null) {
				int i = selectCombo.getSelectionIndex();
				if (i >= 0) {
					String dbName = selectCombo.getItem(i);
					resource.setPersistentProperty(new QualifiedName(QUALIFIER, SELECTED_DB), dbName);
				}
			}
			PropertyPageChangeListener.firePropertyPageChangeListener(this, PropertyPageChangeListener.EVT_SetDataBase);
		} catch (Exception e) {
			DbPlugin.log(e);
			return false;
		}
		return true;
	}

}
