package zigen.plugin.db.ui.editors;

public class QueryViewerPager extends AbstractTableViewerPager{
	
	public QueryViewerPager(int limit) {
		super(null, limit);
	}
	
	protected void setEnabledPagerButton() {
		topPage.setEnabled((pageNo > 1));
		backPage.setEnabled((pageNo > 1));
		nextPage.setEnabled((pageNo < pageCount));
		endPage.setEnabled((pageNo < pageCount));
	}

}
