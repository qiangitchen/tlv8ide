function getData() {
	var columns = YJTemplet_GridColumns;
	var data = new tlv8.Data();
	data.setDbkey("YJTemplet_dbkey");
	data.setTable("YJTemplet_tableName");
	var treegrid = new tlv8.treeGrid("#treeGridView", "YJTemplet_ID", "YJTemplet_name",
			"YJTemplet_parent", columns, "100%", "100%", data, "#tb");
	treegrid.refreshData();
}