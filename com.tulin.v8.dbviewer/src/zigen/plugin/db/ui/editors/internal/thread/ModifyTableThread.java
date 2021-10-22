/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.thread;

import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.ITable;

public class ModifyTableThread extends AbstractSQLThread {

	private String newTableName;

	private String newRemarks;

	public ModifyTableThread(ITable table, String newTableName, String newRemarks) {
		super(table);
		this.newTableName = newTableName;
		this.newRemarks = newRemarks;
	}

	public String[] createSQL(ISQLCreatorFactory factory, ITable table) {
		List list = new ArrayList();

		if (!table.getRemarks().equals(newRemarks)) {
			list.add(factory.createCommentOnTableDDL(newRemarks));
		}
		if (!table.getName().equals(newTableName)) {
			list.add(factory.createRenameTableDDL(newTableName));
		}
		return (String[]) list.toArray(new String[0]);

	}

	public void doAfterExecuteUpdate(ITable table) {
		table.setName(newTableName);
	}

}
