package zigen.plugin.db.ui.editors;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.ITable;

public class TableViewerPager extends AbstractTableViewerPager {

	public TableViewerPager(ITable table, int limit) {
		super(table, limit);
	}

	protected void setEnabledPagerButton() {
		IDBConfig config = table.getDbConfig();
		ISQLCreatorFactory factory = DefaultSQLCreatorFactory.getFactory(config, table);
		if (table != null && factory.isSupportPager()) {
			topPage.setEnabled((pageNo > 1));
			backPage.setEnabled((pageNo > 1));
			nextPage.setEnabled((pageNo < pageCount));
			endPage.setEnabled((pageNo < pageCount));
		} else {
//			topPage.setEnabled(false);
//			backPage.setEnabled(false);
//			nextPage.setEnabled(false);
//			endPage.setEnabled(false);
			topPage.setEnabled((pageNo > 1));
			backPage.setEnabled((pageNo > 1));
			nextPage.setEnabled((pageNo < pageCount));
			endPage.setEnabled((pageNo < pageCount));
			

		}
	}

}
