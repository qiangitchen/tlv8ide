//工具条
var toolbarItem;
function init_toolbar() {
	var bardiv = J$("stander_bar");
	var toobar = new tlv8.toolbar(bardiv, "readonly", true, "readonly",
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
}

//新增数据
function dataInsert(){
	J$("MAIN_DATA_FORM").reset();
	J$("MAIN_DATA_FORM").rowid = "";
	J$("MAIN_DATA_FORM").setAttribute("rowid", "");
	$("#MAIN_DATA_FORM").attr("rowid", "");
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