/**
@class tlv8.Data
@description 公用对象用于构建提交数据
*/
tlv8.Data = function(){};

/**
@name table
@description 关联的数据库表
*/
tlv8.Data.prototype.table = "";

tlv8.Data.prototype.relation = "";

tlv8.Data.prototype.cells = "";

/**
@name rowid
@description 数据主键
*/
tlv8.Data.prototype.rowid = "";

tlv8.Data.prototype.formid = "";

tlv8.Data.prototype.dbkay = "";

tlv8.Data.prototype.Cascade = false;

tlv8.Data.prototype.filter = "";

tlv8.Data.prototype.orderby = "";

tlv8.Data.prototype.readonly = false;

tlv8.Data.prototype.childrenData = new Map();

/**
@name setReadonly
@description 设置表单只读状态
@param {boolean} sta
*/
tlv8.Data.prototype.setReadonly = function(sta){};

/**
@name setOrderby
@description 设置数据排序
@param {string} ob
@example data.setOrderby("FCODE asc,fNAME desc");
*/
tlv8.Data.prototype.setOrderby = function(ob){};

/**
@name setFilter
@description 设置过滤条件
@param {string} fil
@example data.setFilter("FCODE = '123'");
*/
tlv8.Data.prototype.setFilter = function(fil){};

/**
@name setCascade
@description 设置级联删除{表名:外键,表名:外键,...}
@param {string} cas
@example data.setCascade("OA_ALREADYBUY:fBillID");
@example data.setCascade("OA_ALREADYBUY:fBillID,OA_ALREADYBUY1:fBillID1");
*/
tlv8.Data.prototype.setCascade = function(cas){};

/**
@name setRowId
@description 设置主键，记录表单当前正在编辑的数据主键。
@param {string} rowid
*/
tlv8.Data.prototype.setRowId = function(rowid){};

/**
@name setDbkey
@description 设置关联数据库
@param {string} dbkey
@example data.setDbkey("oa");
*/
tlv8.Data.prototype.setDbkey = function(dbkey){};

tlv8.Data.prototype.setSaveAction = function(a){};

/**
@name setFormId
@description 设置数据对应的FORM表单ID
@param {string} s
@example data.setFormId("main_form");
*/
tlv8.Data.prototype.setFormId = function(s){};

/**
@name setonDataValueChanged
@description 设置表单值改变事件回调函数
@param {function} fn
@example data.setonDataValueChanged(function(event){});
*/
tlv8.Data.prototype.setonDataValueChanged = function(fn){};

/**
@name registerChangeEvent
@description 注册表单值改变事件,一般不需要主动调用
@param {string} formid
@example data.registerChangeEvent();
*/
tlv8.Data.prototype.registerChangeEvent = function(formid){};

/**
@name setTable
@description 设置表单关联数据库表名
@param {string} tablename
@example data.setTable("SA_OPPERSON");
*/
tlv8.Data.prototype.setTable = function(tablename){};

/**
@name setCells
@description 设置列的值
@param {Map} cells
@example var cell=new Map();
         cell.put("a","a");
         cell.put("b","b");
         data.setCells(cell);
*/
tlv8.Data.prototype.setCells = function(cells){};

/**
@name saveData
@description 保存数据
@returns {string}
@example var rowid = data.saveData();
*/
tlv8.Data.prototype.saveData = function(){return new String();};

tlv8.Data.prototype.setDeleteAction = function(del){};

/**
@name deleteData
@description 删除数据
@param {boolean} isconfirm -是否提示确认[默认是]
@example data.deleteData();
*/
tlv8.Data.prototype.deleteData = function(isconfirm){};

/**
@name refreshData
@param {boolean} isconfirm -是否提示确认
@param {boolean} isrefreshSub -是否刷新关联的子表数据
@description 刷新数据 
@description 只是重加载表单的数据，不刷新页面
@example data.refreshData();
*/
tlv8.Data.prototype.refreshData = function(isconfirm, isrefreshSub){};

/**
@name getValueByName
@param {string} cellname -字段名（表单中INPUT、TEXTAREA等字段id）
@description 获取指定字段的值
@returns {string}
@example var nmv = data.getValueByName("FNAME");
*/
tlv8.Data.prototype.getValueByName = function(cellname){return new String();};

/**
@name setValueByName
@param {string} cellname -字段名（表单中INPUT、TEXTAREA等字段id）
@param {string} value -需要给的值
@description 给指定字段赋值
@example data.setValueByName("FNAME","test");
*/
tlv8.Data.prototype.setValueByName = function(cellname,value){};
