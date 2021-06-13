/**
@class tlv8.createGrid
@description Grid组件
@param {HTMLDivElement} div
@param {string} labelid
@param {string} labels
@param {string} labelwidths
@param {string} dataAction
@param {string} witdh
@param {string} height
@param {object} data
@param {number} limit
@param {string} where
@param {string} billdataformid
@param {string} billcell
@param {string} datatype
@param {string} master
@param {string} showindex
@param {string} sql
@param {string} fixColumn
*/
tlv8.createGrid = function(div, labelid, labels, labelwidth, dataAction,
		witdh, height, data, limit, where, billdataformid, billcell, datatype,
		master, showindex, sql, fixColumn){};

/*
*grid
*/
function _gridprop(){};

/**
@name staticFilter
@description GRID静态过滤条件
*/
_gridprop.prototype.staticFilter = "";
_gridprop.prototype.sql = "";

/**
@function
@name setSQL
@description 设置查询SQL
@param {string} sql
*/
_gridprop.prototype.setSQL = function(sql){};

/**
@function
@name settoolbar
@description 设置按钮状态
@param insertItem -新增按钮（true，false，"readonly","no"）
@param saveItem -保存按钮（true，false，"readonly","no"）
@param refreshItem -刷新按钮（true，false，"readonly","no"）
@param deleteItem -删除按钮（true，false，"readonly","no"）
*/
_gridprop.prototype.settoolbar = function(insertItem, saveItem, refreshItem, deleteItem){};
_gridprop.prototype.CurrentPage = 1;

/**
@function
@name setPageBar
@description 设置分页按钮
@param {number} page -当前页码
@param {number} allpage -总的页数
*/
_gridprop.prototype.setPageBar = function(page, allpage){};
_gridprop.prototype.inputPage = function(event, obj){};
_gridprop.prototype.beforSortCell = null;
_gridprop.prototype.CellSort = function(obj, cellID){};

/**
@function
@name insertSelfBar
@description 添加自定义按钮
@param {string} label
@param {string} width -按钮宽度（"80px"）
@param {string} action -点击按钮动作（"chanjskdjf()"）
@param {string} img -按钮图片路径（"../img/dddd.png"）
@returns {string} 返回按钮ID
*/
_gridprop.prototype.insertSelfBar = function(label, width, action, img){};

/**
@function
@name setExcelimpBar
@description 设置是否显示Excel导入按钮
@param {boolean} flag
*/
_gridprop.prototype.setExcelimpBar = function(flag){};
_gridprop.prototype.billfilter = "";

/**
@function
@name setExcelexpBar
@description 设置是否显示Excel导出按钮
@param {boolean} flag
*/
_gridprop.prototype.setExcelexpBar = function(flag){};
_gridprop.prototype.Length = 0;

/**
@function
@name getLength
@description 获取数据行数
@returns {number}
*/
_gridprop.prototype.getLength = function(){return 0;};
_gridprop.prototype.RowId = new Array();

/**
@function
@name getRowId
@description 获取指定行rowid
@param {number} index -行数从0开始
@returns {string}
*/
_gridprop.prototype.getRowId = function(index){return new String();};
_gridprop.prototype.Value = new Map();

/**
@function
@name setValueByName
@description 给指定列赋值
@param {string} name -列名
@param {string} param -行rowid或行数
@param {string} value -需要赋的值
*/
_gridprop.prototype.setValueByName = function(name, param, value){};

/**
@function
@name getValueByName
@description 获取指定列的值
@param {string} name -列名
@param {string} param -行rowid或行数
@returns {string}
*/
_gridprop.prototype.getValueByName = function(name, param){return new String();};

/**
@function
@name locate
@description 查找关系值对应的行ID,crowid除外
@param {string} relation -列名
@param {string} value -值
@param {string} crowid -行rowid
@returns {string}
*/
_gridprop.prototype.locate = function(relation, value, crowid){return new String();};

/**
@function
@name getIndex
@description 获取指定行的行号
@param {string} cId -行rowid
@returns {number}
*/
_gridprop.prototype.getIndex = function(cId){return 0;};

/**
@private
@name setdbclick
@description 设置行双击事件
@param {function} fn
*/
_gridprop.prototype.setdbclick = function(fn){};
_gridprop.prototype.gridRelation = "";
_gridprop.prototype.gridLabels = "";

/**
@function
@name getgridRelationIndex
@description 获取指定列的序号
@param {string} relation
@returns {number}
*/
_gridprop.prototype.getgridRelationIndex = function(relation){return 0;};
_gridprop.prototype.editModel = false;
_gridprop.prototype.rowState = new Map();

/**
@function
@name setrowState
@description 设置某行的状态
@param {string} rowid
@param {string} state
@example grid.setrowState(rowid,"readonly");
*/
_gridprop.prototype.setrowState = function(rowid, state){};

/**
@function
@name seteditModel
@description 设置grid的编辑模式
@param {string} editModel
@example grid.seteditModel(true|false); //是否可编辑
@example grid.seteditModel("dbclick"); //双击编辑
*/
_gridprop.prototype.seteditModel = function(editModel){};

_gridprop.prototype.editDataRowIds = "";
_gridprop.prototype.beforeeditData = "";
_gridprop.prototype.editedDatas = new Map();
_gridprop.prototype.RequiredCells = new Map();
_gridprop.prototype.requiredAlert = "";

/**
@function
@name setRequired
@description 设置指定的字段必填
@param {string} cellName -需要设置必填的列名，多个用逗号分隔
@example grid.setRequired("fCODE"); 
@example grid.setRequired("fCODE,fNAME");
*/
_gridprop.prototype.setRequired = function(cellName){};

/**
@private
 */
_gridprop.prototype.outEditBlur = function(){};

/**
@private
 */
_gridprop.prototype.changeValue = function(EditrowID, relation, value, index){};

/**
@function
@name refreshData
@description 刷新数据
@param {string} filter -刷新数据条件
@param {boolean} isconfirm -是否提示数据改变
*/
_gridprop.prototype.refreshData = function(filter, isconfirm){};

/**
@function
@name insertData
@description 新增数据
@param {boolean} num_d -true向下新增|false向上新增  
@returns {string}
*/
_gridprop.prototype.insertData = function(num_d){return new String();};

/**
@function
@name saveData
@description 保存数据
@param {object} event
@param {function} gridsavecalback
*/
_gridprop.prototype.saveData = function(event, gridsavecalback){};

/**
@function
@name deleteData
@description 删除数据
@param {string} deleteRowID
@param {boolean} isConfirm
@param {boolean} isRefresh
*/
_gridprop.prototype.deleteData = function(deleteRowID, isConfirm, isRefresh){};

/**
@function
@name getCurrentRowId
@description 获取当前选中行的rowid
@returns {string}
*/
_gridprop.prototype.getCurrentRowId = function(){return new String();};

_gridprop.prototype.CurrentRowId = "";

/**
@function
@name getCheckedRowIds
@description 获取选中行的ID
@returns {string}
*/
_gridprop.prototype.getCheckedRowIds = function(){return new String();};

/**
@function
@name setRowChecked
@description 设置选择
@param {string} rowid 行ID
@param {boolean} state 选择状态[true/false]
@param {boolean} triggercal 是否需要触发选择事件（默认不触发）
@returns {void}
*/
_gridprop.prototype.setRowChecked = function(){};

/**
@function
@name addFooterrow
@description 添加统计行
@param {string} cells [required]统计行插入的内容，单元格跨行加“:colspan(1)”
@returns {void}
*/
_gridprop.prototype.addFooterrow = function(){};

/**
@function
@name quickSearch
@description 快速查询
@param {string} text 模糊查询的内容
@returns {void}
*/
_gridprop.prototype.quickSearch = function(){};

/**
@function
@name resizeGrid
@description 重置grid的大小（样式）
@returns {void}
 */
_gridprop.prototype.resizeGrid = function(){};

/**
@function
@name resize
@description 重置grid的大小（布局） 适应父容器 fixTable
@returns {void}
 */
_gridprop.prototype.resize = function(){};

tlv8.createGrid.prototype.grid = new _gridprop();