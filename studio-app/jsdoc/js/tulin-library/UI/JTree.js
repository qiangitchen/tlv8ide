/**
@class tlv8.tree
@description 树形组件
*/
tlv8.tree = function(){};
tlv8.tree.prototype.tlv8.treeCount = 1;
tlv8.tree.prototype.treeId = "";//树的Id
tlv8.tree.prototype.tlv8.treeid = "";//树的Id
tlv8.tree.prototype.tlv8.treename = "";//显示数的名称
tlv8.tree.prototype.tlv8.treeparent = "";//树的层级
tlv8.tree.prototype.tlv8.treeother = "";
tlv8.tree.prototype.tableName = "";//表名
tlv8.tree.prototype.databaseName = "";
tlv8.tree.prototype.zNodes = {};

/**
@function
@name cancelSelectedNode
@description 取消选中节点
@param {object} node
*/
tlv8.tree.prototype.cancelSelectedNode = function(node){};

/**
@function
@name expandAll
@description 展开或收缩全部(true:展开,false:收缩,默认切换)
@param {boolean} expandFlag
*/
tlv8.tree.prototype.expandAll = function(expandFlag){};
tlv8.tree.prototype.expandNode = function(node, expandFlag, sonSign, focus, callbackFlag){};
tlv8.tree.prototype.getNodes = function(){};
tlv8.tree.prototype.getNodeByParam = function(key, value, parentNode){};
tlv8.tree.prototype.getNodeByTId = function(tId){};
tlv8.tree.prototype.getNodesByParam = function(key, value, parentNode){};
tlv8.tree.prototype.getNodesByParamFuzzy = function(key, value, parentNode){};
tlv8.tree.prototype.getNodeIndex = function(node){};

/**
@function
@name getSelectedNodes
@description 获取选中的节点
@returns {Array}
*/
tlv8.tree.prototype.getSelectedNodes = function(){return new Array();};

/**
@function
@name isSelectedNode
@description 判断节点是否被选中
@param {object} node
@returns {boolean}
*/
tlv8.tree.prototype.isSelectedNode = function(node){return false;};

tlv8.tree.prototype.reAsyncChildNodes = function(parentNode, reloadType, isSilent){};
tlv8.tree.prototype.refresh = function(){};
tlv8.tree.prototype.selectNode = function(node, addFlag){};
tlv8.tree.prototype.transformTozTreeNodes = function(simpleNodes){};
tlv8.tree.prototype.transformToArray = function(nodes){};
tlv8.tree.prototype.updateNode = function(node, checkTypeFlag){};
tlv8.tree.prototype.getCaches = function(){};
tlv8.tree.prototype.getSetting = function(node){};
tlv8.tree.prototype.get = function(treeID) {};
tlv8.tree.prototype.init = function(treebody, setting, param){};
tlv8.tree.prototype.quickPosition = function(text) {};

/**
@function
@name refreshtlv8.tree
@description 重新加载树
@param {string} panle 
@param {function} afcalback 
*/
tlv8.tree.prototype.refreshtlv8.tree = function(panle,afcalback){};

var _tree = {};
_tree.cancelSelectedNode = function(node){};
_tree.expandAll = function(expandFlag){};
_tree.expandNode = function(node, expandFlag, sonSign, focus, callbackFlag){};
_tree.getNodes = function(){};
_tree.getNodeByParam = function(key, value, parentNode){};
_tree.getNodeByTId = function(tId){};
_tree.getNodesByParam = function(key, value, parentNode){};
_tree.getNodesByParamFuzzy = function(key, value, parentNode){};
_tree.getNodeIndex = function(node){};
_tree.getSelectedNodes = function(){};
_tree.isSelectedNode = function(node){};
_tree.reAsyncChildNodes = function(parentNode, reloadType, isSilent){};
_tree.refresh = function(){};
_tree.selectNode = function(node, addFlag){};
_tree.transformTozTreeNodes = function(simpleNodes){};
_tree.transformToArray = function(nodes){};
_tree.updateNode = function(node, checkTypeFlag){};
_tree.getCaches = function(){};
_tree.getSetting = function(node){};
tlv8.tree.prototype.tree = _tree;

var _setting = {};
var _setting_view = {};
_setting_view.autoCancelSelected = false;
_setting_view.selectedMulti = false;
_setting_view.autoCancelSelected = false;
_setting_view.dblClickExpand = true;
_setting_view.expandSpeed = "fast";
_setting_view.nameIsHTML = false;
_setting_view.showIcon = true;
_setting_view.showLine = true;
_setting_view.showTitle = true;
_setting.view = _setting_view;
tlv8.tree.prototype.setting = _setting;

var _param = {};
var _param_cell = {};
_param_cell.id = "";//设置构建树的id
_param_cell.name = "";//树显示的名称
_param_cell.parent = "";//表示树的层级
_param_cell.other = "";
_param_cell.tableName = "";//对应的表名
_param_cell.databaseName = "";//数据库
_param_cell.rootFilter = ""; //跟节点条件
_param_cell.filter = "";
_param.cell = _param_cell;
tlv8.tree.prototype.param = _param;