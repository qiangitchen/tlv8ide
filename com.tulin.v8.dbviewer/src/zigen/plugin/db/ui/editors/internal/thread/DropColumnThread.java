/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.thread;

import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public class DropColumnThread extends AbstractSQLThread {

	private Column column;

	private boolean cascadeConstraints;

	public DropColumnThread(ITable table, Column column, boolean cascadeConstraints) {
		super(table);
		this.column = column;
		this.cascadeConstraints = cascadeConstraints;
	}

	public String[] createSQL(ISQLCreatorFactory factory, ITable table) {
		String[] sqls = factory.createDropColumnDDL(column, cascadeConstraints);
		return sqls;
	}

}
