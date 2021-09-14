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
	
	setCreatorInfo();
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

/**
 * 设置当前用户信息
 */
function setCreatorInfo() {
	$("#FCREATEPSNFID").val(tlv8.Context.getCurrentPersonFID());
	$("#FCREATEPSNID").val(tlv8.Context.getCurrentPersonID());
	$("#FCREATEPSNNAME").val(tlv8.Context.getCurrentPersonName());
	$("#FCREATEDEPTID").val(tlv8.Context.getCurrentDeptID());
	$("#FCREATEDEPTNAME").val(tlv8.Context.getCurrentDeptName());
	$("#FCREATEOGNID").val(tlv8.Context.getCurrentOgnID());
	$("#FCREATEOGNNAME").val(tlv8.Context.getCurrentOgnName());
	$("#FCREATEORGID").val(tlv8.Context.getCurrentOrgID());
	$("#FCREATEORGNAME").val(tlv8.Context.getCurrentOrgName());
	$("#FCREATETIME").val(tlv8.System.Date.sysDateTime());
}