/*
*system
*/
function _tlv8_System(){};
_tlv8_System = new _tlv8_System();
tlv8.System.prototype = new _tlv8_System();

/*
*RequestURLParam
*/
function _tlv8_RequestURLParam(){};
_tlv8_RequestURLParam = new _tlv8_RequestURLParam();
tlv8.RequestURLParam.prototype = new _tlv8_RequestURLParam();

/**
@name tlv8.RequestURLParam.getParam
@description 获取URL参数
@param {string} paramName
@returns {string}
 */
_tlv8_RequestURLParam.getParam = function(paramName){return new String();};

/*
*Date
*/
function _tlv8_System_Date(){};
_tlv8_System_Date = new _tlv8_System_Date();
tlv8.System.Date.prototype = new _tlv8_System_Date();

/**
@name tlv8.System.Date.sysDate
@description 获取服务时间（日期）
@returns {string}
 */
_tlv8_System_Date.sysDate = function(){return new String();};

/**
@name tlv8.System.Date.sysDateTime
@description 获取服务时间（日期时间）
@returns {string}
 */
_tlv8_System_Date.sysDateTime = function(){return new String();};

/**
@name tlv8.System.Date.strToDate
@description 日期字符串转日期对象
@param {string} datestr
@returns {Date}
 */
_tlv8_System_Date.strToDate = function(datestr){return new Date();};

/*
*Other
*/
function _tlv8_sqlQueryAction(){};
_tlv8_sqlQueryAction.prototype.data = "";

/**
@name getNode
@description 获取Data对象的值
@param {string} tag -{flag:data:message}
@returns {string}
 */
_tlv8_sqlQueryAction.prototype.getNode = function(tag){};

/**
@name getCount
@description 获取查询结果的条数
@returns {number}
 */
_tlv8_sqlQueryAction.prototype.getCount = function(){return 0;};

/**
@name getColumns
@description 获取查询结果的字段个数
@returns {number}
 */
_tlv8_sqlQueryAction.prototype.getColumns = function(){return 1;};

/**
@name getValueByName
@description 获取查询结果的值
@param {string} columnName -获取数据的列名
@returns {string}
 */
_tlv8_sqlQueryAction.prototype.getValueByName = function(columnName){return new String();};

/**
@name getDatas
@description 获取查询的所有数据
@returns {string}
 */
_tlv8_sqlQueryAction.prototype.getDatas = function(){return new String();};

/**
@class tlv8.GridSelect
@description 列表下拉
@param {HTMLDivElement} div -用于显示表格的DIV标签
@param {string} dbkey -数据库连接标志
@param {string} sql -查询的sql
@param {boolean} master -是否多选
@param {boolean} caninput -是否可输入
*/
tlv8.GridSelect = function(div, dbkey, sql, master, caninput){};
function _gridSelect(){};
_gridSelect.prototype.onselected = "";
_gridSelect.prototype.onchecked = "";
_gridSelect.prototype.onValueChanged = "";
_gridSelect.prototype.select = function(obj){};
_gridSelect.prototype.change = function(obj){};
_gridSelect.prototype.check = function(){};
_gridSelect.prototype.currentRowId = "";
_gridSelect.prototype.checkedValue = "";
_gridSelect.prototype.setSelect = function(obj, m){};
_gridSelect.prototype.outEdit = function(){};
tlv8.GridSelect.prototype.gridSelect = new _gridSelect();

/**
@class tlv8.TreeSelect
@description 树形下拉
@param {HTMLDivElement} div -用于显示树的DIV标签
@param {string} QueryAction -用于构建树的Action
@param {boolean} master -是否多选
*/
tlv8.TreeSelect = function(div, QueryAction, master){};
function _treeSelect(){};
_treeSelect.prototype.rowid = "";
_treeSelect.prototype.value = "";
_treeSelect.prototype.parentid = "";
_treeSelect.prototype.select = function(data){};
_treeSelect.prototype.check = function(data){};
_treeSelect.prototype.setSelect = function(obj){};
tlv8.TreeSelect.prototype.treeSelect = new _treeSelect();


function _Radio(){};
_Radio.prototype.radiocheck = function(obj){};
_Radio.prototype.initData = function(obj){};
_Radio.prototype.setValue = function(val){};
tlv8.Radio.prototype.Radio = new _Radio();

function _CheckBox(){};
_CheckBox.prototype.boxcheck = function(obj){};
_CheckBox.prototype.initData = function(obj){};
_CheckBox.prototype.checkAll = function(){};
_CheckBox.prototype.uncheckAll = function(){};
tlv8.CheckBox.prototype.Check = new _CheckBox();

tlv8.standardPartition.prototype.close = function(srcE, STR){};
tlv8.standardPartition.prototype.open = function(srcE, STR){};
tlv8.standardPartition.prototype.moveToEnd = function(srcE, STR){};

function _JSON() {};
JSON= new _JSON();
JSON.prototype = new _JSON();

/**
@name JSON.parse
@description 字符串转JSON对象
@param {string} jsonStr
@returns {JSON}
*/
_JSON.parse = function(jsonStr){};

/**
@name JSON.stringify
@description JSON对象转字符串
@param {JSON} json
@returns {string}
*/
_JSON.stringify = function(json){return new String();};

/**
@class tlv8.RequestParam
@description 请求参数为了统一编码
@returns {void}
*/
tlv8.RequestParam = function(){};

/**
@name set
@description 添加参数
@param {string} key
@param {string} value
@returns {void}
*/
tlv8.RequestParam.prototype.set = function(key, value){};

/**
@name get
@description 获取参数值
@param {string} key
@returns {string}
*/
tlv8.RequestParam.prototype.get = function(key){return new String();};

/**
@class tlv8.toolbar
@description 工具栏组件
@param {HTMLDivElement} div
@param {string} insertitem -("readonly":只读,true：可操作,false：不可见)
@param {string} saveitem -("readonly":只读,true：可操作,false：不可见)
@param {string} deleteitem -("readonly":只读,true：可操作,false：不可见)
@param {string} refreshitem -("readonly":只读,true：可操作,false：不可见)
@returns {object}
*/
tlv8.toolbar = function(div, insertitem, saveitem, deleteitem, refreshitem){};
tlv8.toolbar.prototype.insertAction = "";
tlv8.toolbar.prototype.saveAction = "";
tlv8.toolbar.prototype.deleteAction = "";
tlv8.toolbar.prototype.refreshAction = "";
function _toolbar(){};
/**
@name setItemStatus
@description 工具栏组件设置按钮状态
@param {string} insertitem -("readonly":只读,true：可操作,false：不可见)
@param {string} saveitem -("readonly":只读,true：可操作,false：不可见)
@param {string} deleteitem -("readonly":只读,true：可操作,false：不可见)
@param {string} refreshitem -("readonly":只读,true：可操作,false：不可见)
*/
_toolbar.prototype.setItemStatus = function(insertitem, saveitem, deleteitem, refreshitem){};
tlv8.toolbar.prototype.items = new _toolbar();

/**
@class tlv8.fileComponent
@description 附件组件
@param {HTMLDivElement} div
@param {object} data
@param {string} cellname
@param {string} docPath
@param {boolean} canupload
@param {boolean} candelete
@param {boolean} canedit
@param {boolean} viewhistory
@param {number} limit
@param {boolean} download
@returns {void}
*/
tlv8.fileComponent = function(div,data,cellname,docPath,canupload,candelete,canedit,viewhistory,limit,download){};