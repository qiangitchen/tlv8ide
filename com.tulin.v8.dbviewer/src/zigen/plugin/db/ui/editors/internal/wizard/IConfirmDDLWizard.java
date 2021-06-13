/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.wizard;

public interface IConfirmDDLWizard {

	public String[] createSQL() throws Exception;

	public ConfirmDDLWizardPage getConfirmDDLWizardPage();
}
