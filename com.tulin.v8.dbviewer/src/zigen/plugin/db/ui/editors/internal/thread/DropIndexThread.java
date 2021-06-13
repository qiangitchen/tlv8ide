/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.thread;

import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.ITable;

public class DropIndexThread extends AbstractSQLThread {

	private String indexName;

	public DropIndexThread(ITable table, String indexName) {
		super(table);
		this.indexName = indexName;
	}

	public String[] createSQL(ISQLCreatorFactory factory, ITable table) {
		String sql = factory.createDropIndexDDL(indexName);
		return new String[] {sql};
	}

}
