/*=========创建树==========*/
var param = {
	cell : {
		databaseName:"YJTemplet_dbkey",//数据库
		tableName : "YJTemplet_tableName",//对应的表名
		id : "YJTemplet_ID",//设置构建树的id
		name : "YJTemplet_name",//树显示的名称
		parent : "YJTemplet_parent",//表示树的层级
		other:"YJTemplet_cells",//树中所带字段信息
		rootFilter : "YJTemplet_rootFilter", //跟节点条件
		orderby : "YJTemplet_level" //排序字段
	}
};
var setting = {
	view: {
		selectedMulti: false,		//设置是否允许同时选中多个节点。默认值: true
		autoCancelSelected: false,
		dblClickExpand: true //双击展开
	},
	data : {
		simpleData : {
			enable : true
		}
	},
	async : {
		enable : true, //异步加载
		url : "TreeSelectAction",//加载数据的Action,可以自定义
		autoParam: ["id=currenid"]
	},
	isquickPosition :{
		enable : YJTemplet_quckEnable, //是否有快速查询框
		url:"QuickTreeAction",
		quickCells : "YJTemplet_name",//用于快速查询的字段
		path : "YJTemplet_quckpath"//查询路径字段
	},
	callback : {
		onClick : treeselected
	}
};

var currenttreeNode = null;
// 选中树
function treeselected(event, treeId, node, clickFlag) {
	currenttreeNode = node;
	J$("main_data_form").rowid = node.id;
	$("#main_data_form").attr("rowid", node.id);
	currentgrid.refreshData();
}

var MainJtree = new Jtree();
/*---------*/
var maingrid = null,currentgrid;
var maindata = new tlv8.Data();
maindata.setDbkey("YJTemplet_dbkey");
maindata.setTable("YJTemplet_tableName");
function getDatagrid() {
	MainJtree.init("JtreeView",setting,param);
	var d = document.getElementById("main-grid-view");
	var labelid = "R_ListIDS";
	var labels = "R_ListLBS";
	var labelwidth = "R_ListWDs";
	var datatype = "R_ListTPs";
	var dataAction = {
		"queryAction" : "getGridAction",
		"savAction" : "saveAction",
		"deleteAction" : "deleteAction"
	};
	maingrid = new tlv8.createGrid(d, labelid, labels, labelwidth,
			dataAction, "100%", "100%", maindata, 20,
			"", "main_data_form", "YJTemplet_parent", datatype);
	// 设置按钮显示{新增、保存、刷新、删除}
	maingrid.grid.settoolbar(true, true, true, true);
	// 设置是否可编辑
	maingrid.grid.seteditModel(true);
	currentgrid = maingrid.grid;
}

function afupdate(event) {
	MainJtree.refreshJtree("JtreeView", function(node) {
		if (currenttreeNode) {
			MainJtree.selectNode(MainJtree.getNodeByTId(currenttreeNode.id),
					true);
			MainJtree.expandNode(MainJtree.getNodeByTId(currenttreeNode.id),
					true);
		}
	});
}