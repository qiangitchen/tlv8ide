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
	}
};

var MainJtree = new Jtree();
function initPagefact(){
	MainJtree.init("JtreeView",setting,param);
}