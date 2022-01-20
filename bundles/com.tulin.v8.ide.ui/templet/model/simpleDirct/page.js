//工具条
var toolbarItem;
function init_toolbar() {
	var bardiv = J$("stander_bar");
	var toobar = new tlv8.toolbar(bardiv, "readonly", true, "readonly",
			true);
	toolbarItem = toobar.items;
}

/*=======主表配置======*/
var MainData = new tlv8.Data();
function getData() {
	MainData.setDbkey("YJTemplet_dbkey");//指定使用数据库连接
	MainData.setTable("YJTemplet_tableName");
	MainData.setFormId("MAIN_DATA_FORM");//主表关联form
	init_toolbar();
	getData2();
	layui.form.on('submit(mainform)', function(data) {
		//console.log(data.field);
		MainData.saveData(data.field);
		return false;//阻止表单跳转。如果需要表单跳转，去掉这段即可。
	});
}

/*======从表配置======*/
var SubData = new tlv8.Data();
function getData2() {
	SubData.setDbkey("YJTemplet_dbkey");//指定使用数据库连接
	SubData.setTable("YJTemplet_subtableName");
	var d = document.getElementById("direct_from_subgrid");
	var labelid = "No,YJTemplet_subcolumns";//设置字段
	var labels = "No.,YJTemplet_sublabels";//设置标题
	var labelwidth = "40,YJTemplet_subwidths";//设置宽度
	var datatype = "ro,YJTemplet_subdatatypes";//设置字段类型
	var dataAction = {
		"queryAction" : "getGridAction",
		"savAction" : "saveAction",
		"deleteAction" : "deleteAction"
	};
	var maingrid = new tlv8.createGrid(d, labelid, labels, labelwidth,
			dataAction, "100%", "100%", SubData, 20, "", "MAIN_DATA_FORM", "YJTemplet_subdirect", datatype,
			"false", "true");
	maingrid.grid.settoolbar(true, false, true, true);//设置按钮显示{新增、保存、刷新、删除}
	maingrid.grid.seteditModel(true);//设置是否可编辑
	maingrid.grid.refreshData();
}

/*
*从表值改变时改变主表按钮状态
*/
function cheng_stae_bar(){
	toolbarItem.setItemStatus("no", true, "no", "no");//no 代表不改变
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
	$("#mainfsub").click();
}

//数据刷新
function dataRefresh(){
	MainData.refreshData();
}

//数据删除
function dataDeleted(){
	if(MainData.deleteData()){
		dataRefresh();
	}
}

function bodyResize(){
	getData2();
}