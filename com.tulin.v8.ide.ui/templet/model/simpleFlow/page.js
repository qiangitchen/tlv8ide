//工具条
var toolbarItem,flwCompent;
function init_toolbar() {
	var bardiv = J$("stander_bar");
	var toobar = new tlv8.toolbar(bardiv, "readonly", true, "readonly",
			true);
	toolbarItem = toobar.items;
}

//流程配置
var setting = {
  autosaveData : true, //自动保存数据
  autoclose : true, //自动关闭页面
  autofilter : true, //自动过滤数据
  autorefresh : true, //自动刷新数据
  autoselectext : true, //自动获取执行人
  item : {//按钮配置
    audit : false, //审批
    back : false,//流转按钮
    out : "readonly",//流转按钮
    transmit : false,//转发按钮
    pause : false,//暂停按钮
    stop : "readonly" //终止按钮 
  },
  auditParam : {//审批信息配置
    busiDataKey : "YJTemplet_dbkey", //业务库数据连接
    busiTable : "YJTemplet_tableName", //业务表名
    auditTable : "OA_FLOWRECORD", //审核意见表
    billidRe : "FBILLID", //外键字段
    FAGREETEXTRe : "FAGREETEXT", //意见字段
    isRequired : false //是否为必须填写意见
  }
}

//数据配置
var datamian;
function initDocumentPage() {
	datamian = new tlv8.Data();
	datamian.setDbkey("YJTemplet_dbkey");
	datamian.setTable("YJTemplet_tableName");
	datamian.setFormId("MAIN_DATA_FORM");
	flwCompent = new tlv8.flw("flowToolbar", datamian, setting);
	
	var rowid = tlv8.RequestURLParam.getParam("sData1");
	if (rowid && rowid != "") {
		J$("MAIN_DATA_FORM").rowid = rowid;
		J$("MAIN_DATA_FORM").setAttribute("rowid", rowid);
		$("#MAIN_DATA_FORM").attr("rowid", rowid);
		dataRefresh();
	}
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