/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLInvoker;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.core.Transaction;

public class SqlExecForPagerJob extends SqlExecJob {

	int offset;
	int limit;
	
	public SqlExecForPagerJob(Transaction trans, String sqlString, String secondarlyId, int offset, int limit) {
		this(trans, sqlString, secondarlyId, false, offset, limit);
	}

	public SqlExecForPagerJob(Transaction trans, String sqlString, String secondarlyId, boolean isRelead, int offset, int limit) {
		super(trans, sqlString, secondarlyId, isRelead);
		this.offset = offset;
		this.limit = limit;
	}

	protected void showDBEditor(String query) throws Exception {
		TableElement[] elements = null;
		TimeWatcher time = new TimeWatcher();
		time.start();
		IDBConfig config = trans.getConfig();
		try {
			elements = SQLInvoker.executeQueryForPager(trans.getConnection(), query, config.isConvertUnicode(), config.isNoLockMode(), offset, limit);
			time.stop();
			showResults(new ShowResultAction(config, query, elements, time.getTotalTime()));
		} catch (Exception e) {
			throw e;
		}
	}
}
