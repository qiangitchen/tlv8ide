//工具条
var toolbarItem;
function init_toolbar() {
	var bardiv = $("#stander-bar")[0];
	var toobar = new tlv8.toolbar(bardiv, false, "readonly", "readonly",
			true);
	toolbarItem = toobar.items;
}

//数据配置
var datamian;
function initDocumentPage() {
	datamian = new tlv8.Data();
	datamian.setDbkey("YJTemplet_dbkey");
	datamian.setTable("YJTemplet_tableName");
	datamian.setFormId("MAIN_DATA_FORM");
	var rowid = tlv8.RequestURLParam.getParam("rowid");
	J$("MAIN_DATA_FORM").rowid = rowid;
	J$("MAIN_DATA_FORM").setAttribute("rowid", rowid);
	dataRefresh();
}

//数据保存
function dataSave() {
	var rowid = datamian.saveData();
	J$("MAIN_DATA_FORM").rowid = rowid;
	J$("MAIN_DATA_FORM").setAttribute("rowid", rowid);
	$("#MAIN_DATA_FORM").attr("rowid", rowid);
}

//数据刷新
function dataRefresh(){
	datamian.refreshData();
}

//数据删除
function dataDeleted(){
	if(datamian.deleteData()){
		dataRefresh();
	}
}