/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.wizard;

import java.util.Collections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import zigen.plugin.db.core.ColumnSeqSorter;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.editors.event.TextSelectionListener;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public class IndexWizardPage extends PKWizardPage {

	public static final String MSG_DSC = Messages.getString("IndexWizardPage.0"); //$NON-NLS-1$

	private static final String MSG_REQUIRE_NAME = Messages.getString("IndexWizardPage.1"); //$NON-NLS-1$

	private static final String MSG_REQUIRE_COLUMN = Messages.getString("IndexWizardPage.2"); //$NON-NLS-1$

	int indexType = ISQLCreatorFactory.TYPE_NONUNIQUE_INDEX;

	Text txtIndexName;

	public IndexWizardPage(ISQLCreatorFactory factory, ITable table) {
		super(factory, table);
		setTitle(Messages.getString("IndexWizardPage.3")); //$NON-NLS-1$
	}

	public void createNameConstrol(Composite container) {
		Composite composite = new Composite(container, SWT.NULL);
		composite.setLayout(new GridLayout(2, false));
		// composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = new Label(composite, SWT.NULL);
		label.setText(Messages.getString("IndexWizardPage.4")); //$NON-NLS-1$
		txtIndexName = new Text(composite, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		txtIndexName.setLayoutData(gd);
		txtIndexName.setText(getDefaultConstraintName()); //$NON-NLS-1$
		txtIndexName.addFocusListener(new TextSelectionListener());
		txtIndexName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if ("".equals(txtIndexName.getText())) { //$NON-NLS-1$
					updateStatus(MSG_REQUIRE_NAME);
				} else if (selectedList.isEmpty()) {
					updateStatus(MSG_REQUIRE_COLUMN);
				} else {
					updateStatus(null);
				}
			}
		});
	}

	protected void createOptionControl(Composite container) {
		Composite base = new Composite(container, SWT.NULL);
		base.setLayout(new RowLayout());

		final Button chkNonUniqueIndex = new Button(base, SWT.RADIO);
		chkNonUniqueIndex.setText("NonUnique Index"); //$NON-NLS-1$
		chkNonUniqueIndex.setSelection(true);
		chkNonUniqueIndex.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {
				if (chkNonUniqueIndex.getSelection()) {
					indexType = ISQLCreatorFactory.TYPE_NONUNIQUE_INDEX;
				}
			}

		});

		final Button chkUniqueIndex = new Button(base, SWT.RADIO);
		chkUniqueIndex.setText("Unique Index"); //$NON-NLS-1$
		chkUniqueIndex.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {
				if (chkUniqueIndex.getSelection()) {
					indexType = ISQLCreatorFactory.TYPE_UNIQUE_INDEX;
				}
			}

		});

		if (DBType.getType(tableNode.getDbConfig()) == DBType.DB_TYPE_ORACLE) {
			final Button chkBitmapIndex = new Button(base, SWT.RADIO);
			chkBitmapIndex.setText("BitMap Index"); //$NON-NLS-1$
			chkBitmapIndex.setSelection(false);
			chkBitmapIndex.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent arg0) {
					if (chkBitmapIndex.getSelection()) {
						indexType = ISQLCreatorFactory.TYPE_BITMAP_INDEX;
					}
				}

			});
		}
	}

	protected void update() {
		Collections.sort(columnList, new ColumnSeqSorter());
		selectColumnViewer.setInput((Column[]) selectedList.toArray(new Column[0]));
		columnViewer.setInput((Column[]) columnList.toArray(new Column[0]));
		columnsPack(selectColumnViewer.getTable());
		columnsPack(columnViewer.getTable());

		if ("".equals(txtIndexName.getText())) { //$NON-NLS-1$
			updateStatus(MSG_REQUIRE_NAME);
		} else if (selectedList.isEmpty()) {
			updateStatus(MSG_REQUIRE_COLUMN);
		} else {
			updateStatus(null);
		}

	}
	protected String getDefaultConstraintName(){
		return "IDX_" + tableNode.getName();
	}

	public int getIndexType() {
		return indexType;
	}

}
