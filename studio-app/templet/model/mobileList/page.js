var setting = {
	databaseName : "YJTemplet_dbkey",// 数据库
	tableName : "YJTemplet_tableName",// 对应的表名
	idcolumn : "YJTemplet_ID",// 设置id字段
	title : {
		label : "",// 字段描述
		column : "YJTemplet_title"
	},
	texts : {
		label : "",// 字段描述
		column : "YJTemplet_text"
	},
	ellips : {
		label : "",// 字段描述
		column : "YJTemplet_ellip"
	},
	staticfilter : "YJTemplet_filter",// 过滤条件
	orderby : "",// 排序
	pagelimit : 10,
	onclick : function(id) {
		// 点击列表时响应
	}
};

var moblist;
$(document).ready(
		function() {
			moblist = new tlv8.MobileList(document
					.getElementById("contenView"), setting);
			moblist.init();
		});

function doSearch(searchText) {
	moblist.doSearch(searchText);
}