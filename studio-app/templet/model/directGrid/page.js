/*=======主表配置======*/
var Maingrid;
var MainData = new tlv8.Data();
function getData() {
	MainData.setDbkey("YJTemplet_dbkey");//指定使用数据库连接
	MainData.setTable("YJTemplet_tableName");
	MainData.setFormId("direct_form");//主表关联form
	var d = document.getElementById("direct_from_grid");
	var labelid = "No,YJTemplet_columns";//设置字段
	var labels = "No.,YJTemplet_labels";//设置标题
	var labelwidth = "40,YJTemplet_widths";//设置宽度
	var datatype = "ro,YJTemplet_datatypes";//设置字段类型
	var dataAction = {
		"queryAction" : "getGridAction",
		"savAction" : "saveAction",
		"deleteAction" : "deleteAction"
	};
	var maingrid = new tlv8.createGrid(d, labelid, labels, labelwidth,
			dataAction, "100%", "100%", MainData, 20, "", "", "", datatype,
			"false", "true");
	maingrid.grid.settoolbar(true, true, true, true);//设置按钮显示{新增、保存、刷新、删除}
	maingrid.grid.seteditModel(true);//设置是否可编辑
	Maingrid = maingrid.grid;
	Maingrid.refreshData();
}

/*======从表配置======*/
var SubData = new tlv8.Data();
var subGrid;
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
			dataAction, "100%", "100%", SubData, 20, "", "direct_form", "YJTemplet_subdirect", datatype,
			"false", "true");
	maingrid.grid.settoolbar(true, false, true, true);//设置按钮显示{新增、保存、刷新、删除}
	maingrid.grid.seteditModel(true);//设置是否可编辑
	subGrid = maingrid.grid;
}

/*
*从表值改变时改变主表按钮状态
*/
function cheng_stae_bar(){
	document.getElementById("direct_from_grid").grid.settoolbar("no", true, "no", "no");//no 代表不改变
}

/*
*主表换行时刷新从表
*/
function refresh_child_data(){
	document.getElementById("direct_from_subgrid").grid.refreshData(); 
}

function bodyResize() {
	getData();
	getData2();
}