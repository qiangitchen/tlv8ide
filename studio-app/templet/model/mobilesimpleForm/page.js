var mainData;
function initBody() {
	mainData = new tlv8.Data();
	mainData.setDbkey("YJTemplet_dbkey");
	mainData.setTable("YJTemplet_tableName");
	mainData.setFormId("MAIN_DATA_FORM");
}

$(document).ready(function() {
	initBody();
	var sData1 = getParamValueFromUrl("sData1");
	$("#MAIN_DATA_FORM").attr("rowid", sData1);
	mainData.refreshData();
});

function afRefresh() {
	// 数据刷新之后处理
}

function doDataSaveaAtion() {
	mainData.saveData();
}
