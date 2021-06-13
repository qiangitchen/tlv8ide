/*==数据源===此项为必须定义==*/
var currentgrid;
var Maindata = new tlv8.Data();
Maindata.setDbkey("YJTemplet_dbkey");//指定使用数据库连接
Maindata.setTable("YJTemplet_tableName");//指定grid对应的表
		
/*====*/
function getData() {
	Maindata.setFormId("MAIN_DATA_FORM");
	var d = document.getElementById("main_grid_view");
	var labelid = "No,YJTemplet_columns";//设置字段
	var labels = "No.,YJTemplet_labels";//设置标题
	var labelwidth = "40,YJTemplet_widths";//设置宽度
	var datatype = "ro,YJTemplet_datatypes";//设置字段类型
	var dataAction = {
		"queryAction" : "getGridAction",//查询动作
		"savAction" : "saveAction",//保存动作
		"deleteAction" : "deleteAction"//删除动作
	};
	var maingrid = new tlv8.createGrid(d, labelid, labels, labelwidth,
		dataAction, "100%", "100%", Maindata, 10, "", "", "", datatype,
		"false", "true");
	//设置按钮显示{新增、保存、刷新、删除}
	maingrid.grid.settoolbar(false, false, true, false);
	//设置是否可编辑
	maingrid.grid.seteditModel(false);
	currentgrid = maingrid.grid;
	currentgrid.refreshData();//刷新数据
}

function maingriddbclick(event){
	var rdata = dailogEngin();
	if (rdata && rdata != "")
		tlv8.portal.dailog.dailogEngin(rdata);
}

function dailogEngin() {
	var rowid = currentgrid.getCurrentRowId();
	if(!rowid || rowid==""){
		alert("请选择!");
		return false;
	}
	var returncells = YJTemplet_returncells;//需要返回值的字段
	if(returncells.length>0){
		var rdata = {};
		rdata.rowid = rowid;
		for(var i=0;i<returncells.length;i++){
			rdata[returncells[i]] = currentgrid.getValueByName(returncells[i],rowid);
		}
		return rdata;//指定列时返回json数据
	}else{
		return rowid;//没有指定返回字段时 默认返回主键
	}
}