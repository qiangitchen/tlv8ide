/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.wizard;

import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.ITable;

public class UKWizardPage extends PKWizardPage {

	public static final String MSG_DSC = Messages.getString("UKWizardPage.0"); //$NON-NLS-1$

	private static final String MSG_REQUIRE_NAME = Messages.getString("UKWizardPage.1"); //$NON-NLS-1$

	private static final String MSG_REQUIRE_COLUMN = Messages.getString("UKWizardPage.2"); //$NON-NLS-1$

	public UKWizardPage(ISQLCreatorFactory factory, ITable table) {
		super(factory, table);
		setTitle(Messages.getString("UKWizardPage.3")); //$NON-NLS-1$
	}

	protected String getDefaultConstraintName(){
		return "UIDX_" + tableNode.getName();
	}

}
