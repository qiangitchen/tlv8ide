var setting = {
	databaseName : "YJTemplet_dbkey",// 数据库
	tableName : "YJTemplet_tableName",// 对应的表名
	id : "YJTemplet_ID", // 设置构建树的id
	parentid : "YJTemplet_parent", // 父节点字段
	name : "YJTemplet_name", // 树要显示的名称字段
	other : "YJTemplet_cells",
	rootFilter : "YJTemplet_rootFilter", // 跟节点条件
	otherConditions : "", // 其他条件
	orderby : "YJTemplet_level", // 排序字段
	rootDefaulExpand : false,// 根节点是否默认展开,1=是，0=否 或者摄者为true或者false
	context : "#tree_content", // 树加载在哪个div
	closeicon : "/tlv8/mobileUI/system/images/tree_ez.png", // 关闭的图标
	openicon : "/tlv8/mobileUI/system/images/tree_ex.png", // 展开的图标
	callback : {
		onClick : treeselected
	}
};

// 单击树
function treeselected(event, treeId, node) {

}

var mainJtree = new tlv8.MobileTree();
$(document).ready(function() {
	mainJtree.initTree(setting);
});