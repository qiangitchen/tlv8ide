/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.wizard;

import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public class FKWizardPage1 extends PKWizardPage {

	public FKWizardPage1(ISQLCreatorFactory factory, ITable table) {
		super(factory, table);
		setTitle(Messages.getString("FKWizardPage1.0")); //$NON-NLS-1$
	}

	protected String getDefaultConstraintName(){
		return "FK_" + tableNode.getName();
	}

	public String getTargetColumn() {
		StringBuffer sb = new StringBuffer();
		// sb.append(tableNode.getName());
		// sb.append("(");
		for (int i = 0; i < selectedList.size(); i++) {
			Column column = (Column) selectedList.get(i);
			if (i == 0) {
				sb.append(column.getColumn().getColumnName());
			} else {
				sb.append(", "); //$NON-NLS-1$
				sb.append(column.getColumn().getColumnName());
			}
		}

		// sb.append(")");
		return sb.toString();
	}

	public int getTargetColumnCount() {
		return selectedList.size();
	}
}
