function _tlv8() {};
/**
 @name tlv8
 @namespace
 @description 名空间
*/
tlv8= new _tlv8();
tlv8.prototype = new _tlv8();
/**
	@name tlv8.showSate
	@function
	@description 操作状态
	@param state
	@returns {void}
*/
_tlv8.showSate = function(state) {};
/**
	@name tlv8.ajaxLoading
	@function
	@description loading状态提示信息
	@param flag
	@param msg
	@returns {void}
*/
_tlv8.ajaxLoading = function(flag,msg) {};
/**
	@name tlv8.XMLHttpRequest
	@function
	@description 发送Action请求
	@param actionName
	@param param
	@param post
	@param ayn
	@param callBack
	@param unShowState
	@returns {JSON}
*/
_tlv8.XMLHttpRequest = function(actionName,param,post,ayn,callBack,unShowState) {return new JSON();};
/**
	@name tlv8.Queryaction
	@function
	@description 公用函数java调用动作针对查询
	@param actionName
	@param post
	@param callBack
	@param data
	@param where
	@param ays
	@returns {JSON}
*/
_tlv8.Queryaction = function(actionName,post,callBack,data,where,ays) {return new JSON();};
/**
	@name tlv8.Deleteaction
	@function
	@description 公用函数：java调用动作{针对带参数操作 删除}
	@param actionName
	@param post
	@param callBack
	@param rowid
	@param data
	@param ays
	@returns {JSON}
*/
_tlv8.Deleteaction = function(actionName,post,callBack,rowid,data,ays) {return new JSON();};
/**
	@name tlv8.saveAction
	@function
	@description 公用函数：java调用动作{针对带参数操作 保存}
	@param actionName
	@param post
	@param callBack
	@param data
	@param allreturn
	@param ays
	@returns {JSON}
*/
_tlv8.saveAction = function(actionName,post,callBack,data,allreturn,ays) {return new JSON();};
/**
	@name tlv8.strToXML
	@function
	@description 将字符串转换成xml
	@param str
	@returns {XML}
*/
_tlv8.strToXML = function(str) {return new XML();};
/**
	@name tlv8.sqlQueryAction
	@function
	@description 执行sql查询动作
	@param dbkey
	@param sql
	@param callBack
	@param ayn
	@returns {_justep_yn_sqlQueryAction}
*/
_tlv8.sqlQueryAction = function(dbkey,sql,callBack,ayn) {return new _justep_yn_sqlQueryAction();};
/**
	@name tlv8.sqlQueryActionforJson
	@function
	@description 执行sql查询动作
	@param dbkey
	@param sql
	@param callBack
	@param ayn
	@returns {JSON}
*/
_tlv8.sqlQueryActionforJson = function(dbkey,sql,callBack,ayn) {return new JSON();};
/**
	@name tlv8.sqlUpdateAction
	@function
	@description 执行sql更新动作
	@param dbkey
	@param sql
	@param callBack
	@param ayn
	@returns {JSON}
*/
_tlv8.sqlUpdateAction = function(dbkey,sql,callBack,ayn) {return new JSON();};
/**
	@name tlv8.callProcedureAction
	@function
	@description 调用存储过程
	@param dbkey
	@param ProduceName
	@param Param
	@param callBack
	@param ayn
	@returns {JSON}
*/
_tlv8.callProcedureAction = function(dbkey,ProduceName,Param,callBack,ayn) {return new JSON();};
/**
	@name tlv8.callFunctionAction
	@function
	@description 调用数据库函数
	@param dbkey
	@param ProduceName
	@param Param
	@param callBack
	@param ayn
	@returns {JSON}
*/
_tlv8.callFunctionAction = function(dbkey,ProduceName,Param,callBack,ayn) {return new JSON();};
/**
	@name tlv8.numberL2U
	@function
	@description 数字转大写
	@param number
	@returns {String}
*/
_tlv8.numberL2U = function(number) {return new String();};
/**
	@name tlv8.numberFormat
	@function
	@description 数字格式化 如：0,000.00
	@param number
	@param format
	@returns {String}
*/
_tlv8.numberFormat = function(number,format) {return new String();};
/**
	@name tlv8.CheckMail
	@function
	@description 邮箱地址验证
	@param mail
	@returns {Boolean}
*/
_tlv8.CheckMail = function(mail) {return new Boolean();};
/**
	@name tlv8.checkdate
	@function
	@description 日期格式验证
	@param dateStr
	@returns {Boolean}
*/
_tlv8.checkdate = function(dateStr) {return new Boolean();};
/**
	@name tlv8.getIdCardInfo
	@function
	@description 根据身份证取 省份,生日，性别
	@param id
	@returns {String[]}
*/
_tlv8.getIdCardInfo = function(id) {};
/**
	@name tlv8.checkId
	@function
	@description 检查身份证号码
	@param pId
	@returns {Boolean}
*/
_tlv8.checkId = function(pId) {return new Boolean();};
/**
	@name tlv8.Telephone
	@function
	@description 检查电话号码
	@param phone
	@returns {Boolean}
*/
_tlv8.Telephone = function(phone) {return new Boolean();};
/**
	@name tlv8.getworkdays
	@function
	@description 计算工作日
	@param starttime
	@returns {Number}
*/
_tlv8.getworkdays = function(starttime) {return new Number();};
/**
	@name tlv8.inputCaption
	@function
	@description 录入提示
	@param input
	@param dbkey
	@param table
	@param cell
	@param where
	@returns {void}
*/
_tlv8.inputCaption = function(input,dbkey,table,cell,where) {};
/**
	@name tlv8.Radio
	@function
	@description 单选组件
	@param div
	@param item
	@param splithtmlarray
	@returns {void}
*/
_tlv8.Radio = function(div,item,splithtmlarray) {};
/**
	@name tlv8.CheckBox
	@function
	@description 多选组件
	@param div
	@param item
	@param splithtmlarray
	@returns {void}
*/
_tlv8.CheckBox = function(div,item,splithtmlarray) {};
/**
	@name tlv8.showMessage
	@function
	@description 浅入浅出提示信息
	@param message
	@returns {void}
*/
_tlv8.showMessage = function(message) {};
/**
	@name tlv8.fileupload
	@function
	@description 文件上传
	@param dbkey
	@param docPath
	@param tablename
	@param cellname
	@param rowid
	@param callback
	@returns {void}
*/
_tlv8.fileupload = function(dbkey,docPath,tablename,cellname,rowid,callback) {};
/**
	@name tlv8.dowloadfile
	@function
	@description 文件下载
	@param fileID
	@param filename
	@returns {void}
*/
_tlv8.dowloadfile = function(fileID,filename) {};
/**
	@name tlv8.deletefile
	@function
	@description 删除文件
	@param fileID
	@param filename
	@param dbkey
	@param tablename
	@param cellname
	@param rowid
	@param callback
	@returns {void}
*/
_tlv8.deletefile = function(fileID,filename,dbkey,tablename,cellname,rowid,callback) {};
/**
	@name tlv8.ExcelImp
	@function
	@description Excel数据导入
	@param dbkey
	@param table
	@param relations
	@param confirmXmlName
	@param callback
	@returns {void}
*/
_tlv8.ExcelImp = function(dbkey,table,relations,confirmXmlName,callback) {};
/**
	@name tlv8.ExcelExp
	@function
	@description Excel数据导出
	@param dbkey
	@param table
	@param relations
	@param labels
	@param where
	@param orderby
	@returns {void}
*/
_tlv8.ExcelExp = function(dbkey,table,relations,labels,where,orderby) {};
/**
	@name tlv8.encodeURIComponent
	@function
	@description 字符编码
	@param str
	@returns {void}
*/
_tlv8.encodeURIComponent = function(str) {};
/**
	@name tlv8.decodeURIComponent
	@function
	@description 字符解码
	@param str
	@returns {void}
*/
_tlv8.decodeURIComponent = function(str) {};
/**
	@name tlv8.standardPartition
	@function
	@description 垂直分割线可拖动宽度
	@param div
	@returns {void}
*/
_tlv8.standardPartition = function(div) {};
/**
	@name tlv8.sTab
	@function
	@description 公用tab组件
	@param div
	@returns {void}
*/
_tlv8.sTab = function(div) {};
/**
	@name tlv8.editfile
	@function
	@description 附件编辑
	@param fileID
	@param fileName
	@param docPath
	@returns {void}
*/
_tlv8.editfile = function(fileID,fileName,docPath) {};
/**
	@name tlv8.changeLog
	@function
	@description 修改日志
	@returns {Object}
*/
_tlv8.changeLog = function() {return new Object();};
/**
	@name tlv8.createVersion
	@function
	@description 提交文档版本
	@param docID
	@param fileID 
	@param docName 
	@param docPath
	@returns {String}
*/
_tlv8.createVersion = function(docID,fileID,docName,docPath) {return new String();};
/**
	@name tlv8.getDocIdByFileId
	@function
	@description 根据fileID获取docID
	@param fileID
	@returns {Object}
*/
_tlv8.getDocIdByFileId = function(fileID) {return new Object();};
/**
	@name tlv8.picComponent
	@function
	@description 图片组件
	@param div
	@param data
	@param cellname
	@param canEdit
	@returns {void}
*/
_tlv8.picComponent = function(div,data,cellname,canEdit) {};
/**
	@name tlv8.isHaveAuthorization
	@function
	@description 判断是否有功能权限
	@param url
	@returns {void}
*/
_tlv8.isHaveAuthorization = function(url) {};
/**
	@name tlv8.isIE6
	@function
	@description 判断IE是否为IE6
	@returns {Boolean}
*/
_tlv8.isIE6 = function() {return new Boolean();};
/**
	@name tlv8.showModelState
	@function
	@description 模式操作
	@param state
	@returns {void}
*/
_tlv8.showModelState = function(state) {};
/**
	@name tlv8.DateAdd
	@function
	@description 日期计算
	@param strInterval
	@param date
	@param Number
	@returns {void}
*/
_tlv8.DateAdd = function(strInterval,date,Number) {};
/**
	@name tlv8.loadOption
	@function
	@description 加载审核意见
	@param viewID
	@param sData1
	@returns {void}
*/
_tlv8.loadOption = function(viewID,sData1) {};
/**
	@name tlv8.writeOpinion
	@function
	@description 填写审批意见
	@param viewID
	@returns {void}
*/
_tlv8.writeOpinion = function(viewID) {};
/**
	@name tlv8.requerat
	@function
	@description 标注必填{rel='remind',rel-value}
	@returns {void}
*/
_tlv8.requerat = function() {};
/**
	@name tlv8.docpigeonhole
	@function
	@description 文件归档  
	@param folder 归档目录  如："/行政审批/会议纪要"
	@param title 显示标题
	@param table 数据库表
	@param billid 数据主键
	@param surl 查看页面地址
	@returns {void}
*/
_tlv8.docpigeonhole = function(folder,title,table,billid,surl) {};
/**
	@name tlv8.Mydocpigeonhole
	@function
	@description 收藏到个人文件柜
	@param title 显示标题
	@param table 数据库表
	@param billid 数据主键
	@param surl 查看页面地址
	@returns {void}
*/
_tlv8.Mydocpigeonhole = function(title,table,billid,surl) {};
/**
	@name tlv8.ognDocpigeonhole
	@function
	@description 收藏到单位文件柜
	@param title 显示标题
	@param table 数据库表
	@param billid 数据主键
	@param surl 查看页面地址
	@returns {void}
*/
_tlv8.ognDocpigeonhole = function(title,table,billid,surl) {};
/**
	@name tlv8.docOpendeatail
	@function
	@description 查看归档的文件
	@param name 功能标题
	@param surl 页面地址
	@param billid 数据主键
	@returns {void}
*/
_tlv8.docOpendeatail = function(name,surl,billid) {};
/**
	@name tlv8.exportbar
	@function
	@description 导出工具栏组件
	@param {HTMLDivElement} div -工具栏显示层
	@param {string} expid -需要打印或导出的内容对象ID
	@param {boolean} canprint -是否打印
	@param {boolean} isword -是否导出Word
	@param {boolean} isexcel -是否导出Excel
	@param {boolean} ispdf -是否导出PDF
	@returns {void}
 */
_tlv8.exportbar = function(div,expid,canprint,isword,isexcel,ispdf) {};

function _tlv8_Context() {};
_tlv8_Context= new _tlv8_Context();
/**
 @name tlv8.Context
 @description 获取登录人员信息
 */
tlv8.Context.prototype = new _tlv8_Context();
 
/**
@name tlv8.Context.getPath
@function
@description 当前系统路径
@returns {string}
*/
_tlv8_Context.getPath =  function() {return new String();};

/**
@name tlv8.Context.getCurrentPersonID
@function
@description 当前登录人ID
@returns {string}
*/
_tlv8_Context.getCurrentPersonID = function() {return new String();};

/**
@name tlv8.Context.getCurrentPersonCode
@function
@description 当前登录人SCODE
@returns {string}
*/
_tlv8_Context.getCurrentPersonCode = function() {return new String();};

/**
@name tlv8.Context.getCurrentPersonName
@function
@description 当前登录人姓名
@returns {string}
*/
_tlv8_Context.getCurrentPersonName = function() {return new String();};

/**
@name tlv8.Context.getCurrentPersonFID
@function
@description 当前登录人SFID
@returns {string}
*/
_tlv8_Context.getCurrentPersonFID = function() {return new String();};

/**
@name tlv8.Context.getCurrentPersonFCode
@function
@description 当前登录人SFCODE
@returns {string}
*/
_tlv8_Context.getCurrentPersonFCode = function() {return new String();};

/**
@name tlv8.Context.getCurrentPersonFName
@function
@description 当前登录人全名称
@returns {string}
*/
_tlv8_Context.getCurrentPersonFName = function() {return new String();};

/**
@name tlv8.Context.getCurrentPostID
@function
@description 当前登录人岗位ID
@returns {string}
*/
_tlv8_Context.getCurrentPostID = function() {return new String();};

/**
@name tlv8.Context.getCurrentPostCode
@function
@description 当前登录人岗位编号
@returns {string}
*/
_tlv8_Context.getCurrentPostCode = function() {return new String();};

/**
@name tlv8.Context.getCurrentPostName
@function
@description 当前登录人岗位名称
@returns {string}
*/
_tlv8_Context.getCurrentPostName = function() {return new String();};

/**
@name tlv8.Context.getCurrentPostFID
@function
@description 当前登录人岗位ID路径
@returns {string}
*/
_tlv8_Context.getCurrentPostFID = function() {return new String();};

/**
@name tlv8.Context.getCurrentPostFCode
@function
@description 当前登录人岗位编号路径
@returns {string}
*/
_tlv8_Context.getCurrentPostFCode = function() {return new String();};

/**
@name tlv8.Context.getCurrentPostFName
@function
@description 当前登录人岗位名称路径
@returns {string}
*/
_tlv8_Context.getCurrentPostFName = function() {return new String();};

/**
@name tlv8.Context.getCurrentDeptID
@function
@description 当前登录人部门ID
@returns {string}
*/
_tlv8_Context.getCurrentDeptID = function() {return new String();};

/**
@name tlv8.Context.getCurrentDeptCode
@function
@description 当前登录人部门编号
@returns {string}
*/
_tlv8_Context.getCurrentDeptCode = function() {return new String();};

/**
@name tlv8.Context.getCurrentDeptName
@function
@description 当前登录人部门名称
@returns {string}
*/
_tlv8_Context.getCurrentDeptName = function() {return new String();};

/**
@name tlv8.Context.getCurrentDeptFID
@function
@description 当前登录人部门ID路径
@returns {string}
*/
_tlv8_Context.getCurrentDeptFID = function() {return new String();};

/**
@name tlv8.Context.getCurrentDeptFCode
@function
@description 当前登录人部门编号路径
@returns {string}
*/
_tlv8_Context.getCurrentDeptFCode = function() {return new String();};

/**
@name tlv8.Context.getCurrentDeptFName
@function
@description 当前登录人部门名称路径
@returns {string}
*/
_tlv8_Context.getCurrentDeptFName = function() {return new String();};

/**
@name tlv8.Context.getCurrentOgnID
@function
@description 当前登录人机构ID
@returns {string}
*/
_tlv8_Context.getCurrentOgnID = function() {return new String();};

/**
@name tlv8.Context.getCurrentOgnCode
@function
@description 当前登录人机构编号
@returns {string}
*/
_tlv8_Context.getCurrentOgnCode = function() {return new String();};

/**
@name tlv8.Context.getCurrentOgnName
@function
@description 当前登录人机构名称
@returns {string}
*/
_tlv8_Context.getCurrentOgnName = function() {return new String();};

/**
@name tlv8.Context.getCurrentOgnFID
@function
@description 当前登录人机构ID路径
@returns {string}
*/
_tlv8_Context.getCurrentOgnFID = function() {return new String();};

/**
@name tlv8.Context.getCurrentOgnFCode
@function
@description 当前登录人机构编号路径
@returns {string}
*/
_tlv8_Context.getCurrentOgnFCode = function() {return new String();};

/**
@name tlv8.Context.getCurrentOgnFName
@function
@description 当前登录人机构名称路径
@returns {string}
*/
_tlv8_Context.getCurrentOgnFName = function() {return new String();};

/**
@name tlv8.Context.getCurrentOrgID
@function
@description 当前登录人组织ID
@returns {string}
*/
_tlv8_Context.getCurrentOrgID = function() {return new String();};

/**
@name tlv8.Context.getCurrentOrgCode
@function
@description 当前登录人组织编号
@returns {string}
*/
_tlv8_Context.getCurrentOrgCode = function() {return new String();};

/**
@name tlv8.Context.getCurrentOrgName
@function
@description 当前登录人组织名称
@returns {string}
*/
_tlv8_Context.getCurrentOrgName = function() {return new String();};

/**
@name tlv8.Context.getCurrentOrgFID
@function
@description 当前登录人组织ID路径
@returns {string}
*/
_tlv8_Context.getCurrentOrgFID = function() {return new String();};

/**
@name tlv8.Context.getCurrentOrgFCode
@function
@description 当前登录人组织编号路径
@returns {string}
*/
_tlv8_Context.getCurrentOrgFCode = function() {return new String();};

/**
@name tlv8.Context.getCurrentOrgFName
@function
@description 当前登录人组织名称路径
@returns {string}
*/
_tlv8_Context.getCurrentOrgFName = function() {return new String();};
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
/**
@class tlv8.flw
@description 流程组件
@param {HTMLDivElement} div
@param {object} data -tlv8.Data
@param {object} setting
*/
tlv8.flw = function(div, data, setting){};

function _floasetting(){};
_floasetting.prototype.autosaveData = true;//自动保存
_floasetting.prototype.autoclose = true;//自动关闭
_floasetting.prototype.autofilter = true;//自动过滤
_floasetting.prototype.autorefresh = true;//自动刷新
_floasetting.prototype.autoselectext = true;//自动选择
function _floasetting_Item(){};
_floasetting_Item.prototype.audit = true;
_floasetting_Item.prototype.back = true;
_floasetting_Item.prototype.out = true;
_floasetting_Item.prototype.transmit = true;
_floasetting_Item.prototype.pause = true;
_floasetting_Item.prototype.stop = true;
_floasetting.prototype.item = new _floasetting_Item();
tlv8.flw.prototype.id = "";
tlv8.flw.prototype.Dom = "";
tlv8.flw.prototype.setting = new _floasetting();

/**
@name data
@description 流程对应的数据组件 tlv8.Data
*/
tlv8.flw.prototype.data = new tlv8.Data();

/**
@name processID
@description 流程标识(流程图ID)
*/
tlv8.flw.prototype.processID = "";

/**
@name flowID
@description 流程ID
*/
tlv8.flw.prototype.flowID = "";

/**
@name taskID
@description 任务ID
*/
tlv8.flw.prototype.taskID = "";

/**
@name sData1
@description 流程关联的数据主键
*/
tlv8.flw.prototype.sData1 = "";

/**
@name setItemStatus
@description 设置流程组件按钮状态
@param {object} item -{
           back : true,//回退按钮
           out : true,//流转按钮
           transmit : false,//转发按钮
           pause : true,//暂停按钮
           stop : "readonly" //终止按钮
         }
*/
tlv8.flw.prototype.setItemStatus = function(item) {};

/**
@name flowAudit
@description 流程审批
@param {string} flowID
@param {string} taskID
@param {string} ePersonID -执行人ID，多个执行人用逗号(,)分隔
@param {string} sData1
*/
tlv8.flw.prototype.flowAudit = function(flowID, taskID, ePersonID, sData1) {};

/**
@name flowstart
@description 启动流程
@param {string} billid -业务表单主键的值
*/
tlv8.flw.prototype.flowstart = function(billid) {};

/**
@name flowback
@description 流程回退
@param {string} flowID 
@param {string} taskID 
*/
tlv8.flw.prototype.flowback = function(flowID, taskID) {};

/**
@name flowout
@description 流程流转
@param {string} flowID
@param {string} taskID
@param {string} ePersonID -执行人ID，多个执行人用逗号(,)分隔
@param {string} sData1 
*/
tlv8.flw.prototype.flowout = function(flowID, taskID, ePersonID, sData1) {};

/**
@name flowtransmit
@description 流程转发
@param {string} flowID
@param {string} taskID
@param {string} ePersonID -执行人ID，多个执行人用逗号(,)分隔
*/
tlv8.flw.prototype.flowtransmit = function(flowID, taskID, ePersonID) {};

/**
@name flowpause
@description 流程暂停
@param {string} flowID
@param {string} taskID
*/
tlv8.flw.prototype.flowpause = function(flowID, taskID) {};

/**
@name flowstop
@description 流程终止
@param {string} flowID
@param {string} taskID
*/
tlv8.flw.prototype.flowstop = function(flowID, taskID) {};

/**
@name viewChart
@description 查看流程流转图
@param {string} flowID
@param {string} taskID
*/
tlv8.flw.prototype.viewChart = function(flowID, taskID) {};

/*
*
*/
function _tlv8_task(){};
_tlv8_task = new _tlv8_task();
tlv8.task.prototype = new _tlv8_task();

/**
@function 
@name tlv8.task.openTask
@description 打开任务页面
@param {string} taskID
@param {string} url
@param {string} executor
*/
_tlv8_task.openTask = function(taskID, url, executor){};
_tlv8_task.processID = "";
_tlv8_task.flowID = "";
_tlv8_task.taskID = "";

/**
@function 
@name tlv8.task.flowback
@description 回退流程
@param {string} flowID
@param {string} taskID
@param {string} calback
*/
_tlv8_task.flowback = function(flowID, taskID, calback){};

/**
@function 
@name tlv8.task.flowout
@description 流转流程
@param {string} flowID
@param {string} taskID
@param {string} ePersonID
@param {string} sData1
@param {function} calback
*/
_tlv8_task.flowout = function(flowID, taskID, ePersonID, sData1, calback){};

/**
@function 
@name tlv8.task.flowtransmit
@description 转发流程
@param {string} flowID
@param {string} taskID
@param {string} ePersonID
*/
_tlv8_task.flowtransmit = function(flowID, taskID, ePersonID){};

/**
@function 
@name tlv8.task.flowpause
@description 暂停流程
@param {string} flowID
@param {string} taskID
@param {function} calback
*/
_tlv8_task.flowpause = function(flowID, taskID, calback){};

/**
@function 
@name tlv8.task.flowrestart
@description 启动已暂停流程
@param {string} flowID
@param {string} taskID
@param {function} calback
*/
_tlv8_task.flowrestart = function(flowID, taskID, calback){};

/**
@function 
@name tlv8.task.flowstop
@description 终止流程
@param {string} flowID
@param {string} taskID
@param {function} calback
*/
_tlv8_task.flowstop = function(flowID, taskID, calback){};

/**
@function 
@name tlv8.task.flowstart
@description 启动流程
@param {string} flowID
@param {string} sData1
@param {function} calback
*/
_tlv8_task.flowstart = function(sEurl, sData1, calback){};

/**
@function 
@name tlv8.task.viewChart
@description 查看流程图
@param {string} sData1
*/
_tlv8_task.viewChart = function(sData1){};

/**
@function 
@name tlv8.task.Update_Flowbillinfo
@description 更新流程业务数据信息
@param {string} tablename
@param {string} fid
@param {string} billitem
*/
_tlv8_task.Update_Flowbillinfo = function(tablename, fid, billitem){};
/**
@class Map
@description jsMap对象
 */
Map = function(){};

/**
@name toString
@description Map对象转换为字符串
@returns {string}
 */
Map.prototype.toString = function(){return new String();};

/**
@name put
@param {string} key
@param {string} value
@description 集合添加值
 */
Map.prototype.put = function(key,value){};

/**
@name get
@param {string} key
@description 获取集合中的值
@returns {string}
 */
Map.prototype.get = function(key){return Object;};

/**
@name remove
@param {string} key
@description 移除集合中的值
 */
Map.prototype.remove = function(key){};

/**
@name isEmpty
@description 集合是否为空
@returns {boolean}
 */
Map.prototype.isEmpty = function(){return true;};

/**
@name containsKey
@param {string} key
@description 集合中是否存在key对应的值
@returns {boolean}
 */
Map.prototype.containsKey = function(key){return false;};

/**
@name containsValue
@param {string} value
@description 集合中是否存在value对应的对
@returns {boolean}
 */
Map.prototype.containsValue = function(value){return false;};

/**
@name keySet
@description 获取集合的key数组
@returns {Array}
 */
Map.prototype.keySet = function(){return new Array();};

/**
@name size
@description 获取集合的长度
@returns {number}
 */
Map.prototype.size = function(){return 0;};

/*
*String扩展
*/
/**
@name trim
@description 去除字符串两边的空格
@returns {string}
 */
String.prototype.trim = function() {return new String();};

/**
@name ltrim
@description 去除字符串左边的空格
@returns {string}
 */
String.prototype.ltrim = function() {return new String();};

/**
@name rtrim
@description 去除字符串右边的空格
@returns {string}
 */
String.prototype.rtrim = function() {return new String();};

/**
@name replaceFirst
@param {string} p
@param {string} m
@description 替换字符串中的第一个p字符为m
@returns {string}
 */
String.prototype.replaceFirst = function(p, m) {return new String();};

/**
@name replaceAll
@param {string} p
@param {string} m
@description 替换字符串中的所有p字符为m
@returns {string}
 */
String.prototype.replaceAll = function(p, m) {return new String();};

/**
@name toJSON
@description 字符串转换为JSON对象
@returns {object} JSON
 */
String.prototype.toJSON = function() {return new Object();};

/**
@name format 
@description 日期格式化
@example var datestr = new Data().format("yyyy-MM-dd");
 */
Date.prototype.format = function(fmt){return new String();};

/**
@class UUID
@description UUID对象
 */
UUID = function(){};

/**
@name valueOf
@description UUID字符串
@returns {string}
 */
UUID.prototype.valueOf = function(){};

/**
@name toString
@description UUID字符串
@returns {string}
 */
UUID.prototype.toString = function(){};

/**
@name createUUID
@description UUID字符串
@returns {string}
 */
UUID.prototype.createUUID = function(){};

/**
@name J$
@description 根据ID获取HTML的Element对象
@param {string} id
@returns {HTMLElement}
*/
J$ = function(id){return new String();};

/**
@name J$n
@description 根据标签名称获取HTML的Element对象数组
@param {string} tagName
@returns {HTMLElements}
*/
J$n = function(tagName){return new String();};

/**
@name J_u_encode
@description 将字符串UTF-8编码
@param {string} str
@returns {string}
*/
J_u_encode = function(str){return new String();};

/**
@name J_u_decode
@description 将字符串UTF-8解码
@param {string} str
@returns {string}
*/
J_u_decode = function(str){return new String();};

/**
@name topparent
@description 获取顶层窗口对象
@returns {window}
 */
topparent = function(){return window;};

/**
@name getOs
@description 获取浏览器名称 
@returns {string} MSIE|Firefox|Safari|Camino|Gecko
 */
getOs = function(){return new String();};

/**
@name closeself
@description 关闭当前窗口页面
@returns {void}
 */
closeself = function(){};

/**
@name DateDiff
@description 计算两个字符型日期的间隔（天）
@param {string} startDate 格式：yyyy-MM-dd
@param {string} endDate   格式：yyyy-MM-dd
@returns {number}
 */
DateDiff = function(startDate, endDate){return 0};
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
var _jQuery = function(selector){return new _jQuery();};
_jQuery.prototype.init = function(selector, context) {return new _jQuery();};
_jQuery.prototype.selector = "";
_jQuery.prototype.length = 0;
_jQuery.prototype.size = function() {return this.length;};
_jQuery.prototype.toArray = function() {return new Array();};

/**
@name get
@description 获取HTMLHtmlElement
@param {number} num
@returns {HTMLHtmlElement}
@example var tselem = $("#tesel").get(0);
 */
_jQuery.prototype.get = function(num) {return new HTMLHtmlElement();};
_jQuery.prototype.pushStack = function(elems, name, selector) {};

/**
@name each
@description 遍历所有对象
@param {function} callback
@param {object} args
@example $("#tesel").each(function(){alert($(this).attr("id"));});
 */
_jQuery.prototype.each = function(callback, args) {};
	
/**
@name first
@description 将匹配元素集合缩减为集合中的第一个元素
@returns {jQuery}
 */
_jQuery.prototype.first = function() {return new _jQuery();};	

/**
@name last
@description 将匹配元素集合缩减为集合中的最后一个元素
@returns {jQuery}
 */
_jQuery.prototype.last = function() {return new _jQuery();};

/**
@name eq
@description 将匹配元素集合缩减为位于指定索引的新元素
@param {number} i
@returns {jQuery}
 */
_jQuery.prototype.eq = function(i) {};	

/**
@name filter
@description 将匹配元素集合缩减为匹配选择器或匹配函数返回值的新元素
@param {string} str
@returns {jQuery}
 */
_jQuery.prototype.filter = function(str) {};

/**
@name not
@description 将匹配元素集合缩减为匹配选择器或匹配函数返回值的新元素
@param {string} str
@returns {jQuery}
 */
_jQuery.prototype.not = function(str) {};		
	
/**
@name slice
@description 将匹配元素集合缩减为指定范围的子集
@returns {jQuery}
 */
_jQuery.prototype.slice = function() {};	

/**
@name map
@description 把当前匹配集合中的每个元素传递给函数，产生包含返回值的新 jQuery 对象
@param {function} callback
@returns {jQuery}
 */
_jQuery.prototype.map = function(callback) {return new _jQuery();};		

/**
@name end
@description 结束当前链中最近的一次筛选操作，并将匹配元素集合返回到前一次的状态
@returns {jQuery}
 */
_jQuery.prototype.end = function() {};	

/**
@name push
@description 方法可向数组的末尾添加一个或多个元素，并返回新的长度
@param {Array} arr
@returns {number}
 */
_jQuery.prototype.push = function(arr){};

/**
@name html
@description 设置或返回所选元素的内容（包括 HTML 标记）
@param {string} html
@returns {string}
 */
_jQuery.prototype.html = function(html){if(!html)return "";};

/**
@name text
@description 设置或返回所选元素的文本内容
@param {string} str
@returns {string}
 */
_jQuery.prototype.text = function(str){if(!str)return "";};

/**
@name val
@description 设置或返回表单字段的值
@param {string} str
@returns {string}
 */
_jQuery.prototype.val = function(str){if(!str)return "";};

/**
@name attr
@description 设置或返回对象的属性
@param {string} atr
@param {string} val
@returns {string}
 */
_jQuery.prototype.attr = function(atr,val){if(!val)return "";};

/**
@name removeAttr
@description 删除对象的属性
@param {string} atr -属性名称
@returns {jQuery}
 */
_jQuery.prototype.removeAttr = function(atr){return new _jQuery();};

/**
@name addClass
@description 添加CSS样式
@param {string} Class -CSS class
@returns {jQuery}
 */
_jQuery.prototype.addClass = function(Class){return new _jQuery();};

/**
@name removeClass
@description 移除CSS样式
@param {string} Class -CSS class
@returns {jQuery}
 */
_jQuery.prototype.removeClass = function(Class){return new _jQuery();};

/**
@name toggleClass
@description 对设置或移除被选元素的一个或多个类进行切换
@param {string} Class -CSS class
@param {boolean} switch
@returns {jQuery}
@example $(selector).toggleClass(class,switch)
 */
_jQuery.prototype.toggleClass = function(Class){return new _jQuery();};

/**
@name css
@description 方法返回或设置匹配的元素的一个或多个样式属性
@param {string} name
@param {string} value
@returns {jQuery}
@example  $("p").css("color","red");
 */
_jQuery.prototype.css = function(name,value){return new _jQuery();};

/**
@name width
@description 方法返回或设置匹配的元素的宽度
@param {number} width
@returns {number}
@example  $("p").width(800);
 */
_jQuery.prototype.width = function(width){return 0;};

/**
@name height
@description 方法返回或设置匹配的元素的高度
@param {number} height
@returns {number}
@example  $("p").height(100);
 */
_jQuery.prototype.height = function(height){return 0;};

/**
@name innerWidth
@description 方法返回第一个匹配元素的内部宽度
@returns {number}
@example var nwidth = $("p").innerWidth();
 */
_jQuery.prototype.innerWidth = function(){return 0;};

/**
@name innerHeight
@description 方法返回第一个匹配元素的内部高度
@returns {number}
@example var nwidth = $("p").innerHeight();
 */
_jQuery.prototype.innerHeight = function(){return 0;};

/**
@name outerWidth
@description 方法返回第一个匹配元素的外部宽度
@param {string} options
@returns {number}
@example var nwidth = $("p").outerWidth();
 */
_jQuery.prototype.outerWidth = function(options){return 0;};

/**
@name outerHeight
@description 方法返回第一个匹配元素的外部高度
@param {string} options
@returns {number}
@example var nwidth = $("p").outerHeight();
 */
_jQuery.prototype.outerHeight = function(options){return 0;};

/**
@name parent
@description 方法返回被选元素的直接父元素
@returns {jQuery}
@example $("p").parent();
 */
_jQuery.prototype.parent = function(){return new _jQuery();};

/**
@name parents
@description 方法返回被选元素的所有祖先元素，它一路向上直到文档的根元素 (<html>)
@param {string} expr
@returns {jQuery}
@example $("p").parents();
 */
_jQuery.prototype.parents = function(expr){return new _jQuery();};

/**
@name parentsUntil
@description 方法返回介于两个给定元素之间的所有祖先元素
@param {string} expr
@returns {jQuery}
@example $("p").parentsUntil();
 */
_jQuery.prototype.parentsUntil = function(expr){return new _jQuery();};

/**
@name children
@description 方法返回被选元素的所有直接子元素
@param {string} expr
@returns {jQuery}
@example $("p").children();
 */
_jQuery.prototype.children = function(expr){return new _jQuery();};

/**
@name find
@description 方方法返回被选元素的后代元素，一路向下直到最后一个后代
@param {string} expr
@returns {jQuery}
@example $("p").find("a");
 */
_jQuery.prototype.find = function(expr){return new _jQuery();};

/**
@name siblings
@description 方法返回被选元素的所有同胞元素
@param {string} expr
@param {string} context
@returns {jQuery}
@example $("p").siblings("a");
 */
_jQuery.prototype.siblings = function(expr,context){return new _jQuery();};

/**
@name next
@description 方法返回被选元素的下一个同胞元素
@param {string} expr
@returns {jQuery}
@example $("p").next();
 */
_jQuery.prototype.next = function(expr){return new _jQuery();};

/**
@name nextAll
@description 方法返回被选元素的所有跟随的同胞元素
@param {string} expr
@returns {jQuery}
@example $("p").nextAll();
 */
_jQuery.prototype.nextAll = function(expr){return new _jQuery();};

/**
@name nextUntil
@description 方法返回介于两个给定参数之间的所有跟随的同胞元素
@param {string} expr
@returns {jQuery}
@example $("p").nextUntil();
 */
_jQuery.prototype.nextUntil = function(expr){return new _jQuery();};

/**
@name prev
@description 方法返回前面的同胞元素
@param {string} expr
@returns {jQuery}
@example $("p").prev();
 */
_jQuery.prototype.prev = function(expr){return new _jQuery();};

/**
@name prevAll
@description 方法返回前面的同胞元素
@param {string} expr
@returns {jQuery}
@example $("p").prevAll();
 */
_jQuery.prototype.prevAll = function(expr){return new _jQuery();};

/**
@name prevUntil
@description 方法返回前面的同胞元素
@param {string} expr
@returns {jQuery}
@example $("p").prevUntil();
 */
_jQuery.prototype.prevUntil = function(expr){return new _jQuery();};

/**
@name add
@description 方法将元素添加到匹配元素的集合中
@param {string} expr
@param {string} context
@returns {jQuery}
@example $("p").add("div");
 */
_jQuery.prototype.add = function(expr,context){return new _jQuery();};

/**
@name contents
@description 方法获得匹配元素集合中每个元素的子节点，包括文本和注释节点
@returns {jQuery}
@example $("p").contents();
 */
_jQuery.prototype.contents = function(){return new _jQuery();};

/**
@name show
@description 显示HTML元素
@param {number} speed
@param {function} callback
@returns {jQuery}
@example $("p").show();
 */
_jQuery.prototype.show = function(speed,callback){return new _jQuery();};

/**
@name hide
@description 隐藏HTML元素
@param {number} speed
@param {function} callback
@returns {jQuery}
@example $("p").hide();
 */
_jQuery.prototype.hide = function(speed,callback){return new _jQuery();};

/**
@name toggle
@description 切换显示/隐藏HTML元素
@returns {jQuery}
@example $("p").toggle();
 */
_jQuery.prototype.toggle = function(){return new _jQuery();};

/**
@name slideDown
@description 方法用于向下滑动元素
@param {number} speed
@param {function} callback
@description 可选的 speed 参数规定效果的时长。它可以取以下值："slow"、"fast" 或毫秒。可选的 callback 参数是滑动完成后所执行的函数名称。
@returns {jQuery}
@example $("p").slideDown();
 */
_jQuery.prototype.slideDown = function(speed,calback){return new _jQuery();};

/**
@name slideUp
@description 方法用于向上滑动元素
@param {number} speed
@param {function} callback
@description 可选的 speed 参数规定效果的时长。它可以取以下值："slow"、"fast" 或毫秒。可选的 callback 参数是滑动完成后所执行的函数名称。
@returns {jQuery}
@example $("p").slideUp();
 */
_jQuery.prototype.slideUp = function(speed,calback){return new _jQuery();};

/**
@name slideToggle
@description 方法可以在 slideDown() 与 slideUp() 方法之间进行切换
@param {number} speed
@param {function} callback
@description 可选的 speed 参数规定效果的时长。它可以取以下值："slow"、"fast" 或毫秒。可选的 callback 参数是滑动完成后所执行的函数名称。
@returns {jQuery}
@example $("p").slideToggle();
 */
_jQuery.prototype.slideToggle = function(speed,calback){return new _jQuery();};

/**
@name fadeIn
@description 用于淡入已隐藏的元素
@param {number} speed
@param {function} callback
@description 可选的 speed 参数规定效果的时长。它可以取以下值："slow"、"fast" 或毫秒。可选的 callback 参数是滑动完成后所执行的函数名称。
@returns {jQuery}
@example $("p").fadeIn();
 */
_jQuery.prototype.fadeIn = function(speed,calback){return new _jQuery();};

/**
@name fadeOut
@description 方法用于淡出可见元素
@param {number} speed
@param {function} callback
@description 可选的 speed 参数规定效果的时长。它可以取以下值："slow"、"fast" 或毫秒。可选的 callback 参数是滑动完成后所执行的函数名称。
@returns {jQuery}
@example $("p").fadeOut();
 */
_jQuery.prototype.fadeOut = function(speed,calback){return new _jQuery();};

/**
@name fadeTo
@description 方法允许渐变为给定的不透明度（值介于 0 与 1 之间）
@param {number} speed
@param {function} callback
@description 可选的 speed 参数规定效果的时长。它可以取以下值："slow"、"fast" 或毫秒。可选的 callback 参数是滑动完成后所执行的函数名称。
@returns {jQuery}
@example $("p").fadeTo();
 */
_jQuery.prototype.fadeTo = function(speed,opacity,fn){return new _jQuery();};

/**
@name fadeToggle
@description 方法可以在 fadeIn() 与 fadeOut() 方法之间进行切换
@param {number} speed
@param {function} callback
@description 可选的 speed 参数规定效果的时长。它可以取以下值："slow"、"fast" 或毫秒。可选的 callback 参数是滑动完成后所执行的函数名称。
@returns {jQuery}
@example $("p").fadeToggle();
 */
_jQuery.prototype.fadeToggle = function(speed){return new _jQuery();};

/**
@name animate
@description 方法用于创建自定义动画
@param {string} params
@param {number} speed
@param {function} callback
@returns {jQuery}
@description 必需的 params 参数定义形成动画的 CSS 属性。
可选的 speed 参数规定效果的时长。它可以取以下值："slow"、"fast" 或毫秒。
可选的 callback 参数是动画完成后所执行的函数名称。
@returns {jQuery}
@example $("button").click(function(){
  $("div").animate({left:'250px'});
}); 
 */
_jQuery.prototype.animate = function(params,speed,callback){return new _jQuery();};

/**
@name stop
@description 法用于在动画或效果完成前对它们进行停止
@param {boolean} stopAll
@param {boolean} goToEnd
@returns {jQuery}
@description 可选的 stopAll 参数规定是否应该清除动画队列。默认是 false，即仅停止活动的动画，允许任何排入队列的动画向后执行。
可选的 goToEnd 参数规定是否立即完成当前动画。默认是 false。
 */
_jQuery.prototype.stop = function(stopAll,goToEnd){return new _jQuery();};

/**
@name after
@description 方法在被选元素之后插入内容
@param {string} content
@returns {jQuery}
@example $("img").after("Some text after");
 */
_jQuery.prototype.after = function(content){return new _jQuery();};

/**
@name before
@description 方法在被选元素之前插入内容
@param {string} content
@returns {jQuery}
@example $("img").before("Some text after");
 */
_jQuery.prototype.before = function(content){return new _jQuery();};

/**
@name insertAfter
@description 方法在被选元素之后插入 HTML 标记或已有的元素
@param {string} content
@returns {jQuery}
@example $("<span>Hello world!</span>").insertAfter("p");
 */
_jQuery.prototype.insertAfter = function(content){return new _jQuery();};

/**
@name insertBefore
@description 方法在被选元素之前插入 HTML 标记或已有的元素
@param {string} content
@returns {jQuery}
@example $("<span>Hello world!</span>").insertBefore("p");
 */
_jQuery.prototype.insertBefore = function(content){return new _jQuery();};

/**
@name append
@description 方法在被选元素的结尾插入内容
@param {string} content
@returns {jQuery}
@example $("p").append("Some appended text.");
 */
_jQuery.prototype.append = function(content){return new _jQuery();};

/**
@name prepend
@description 方法在被选元素的开头插入内容
@param {string} content
@returns {jQuery}
@example $("p").prepend("Some appended text.");
 */
_jQuery.prototype.prepend = function(content){return new _jQuery();};

/**
@name appendTo
@description 方法在被选元素的结尾（仍然在内部）插入指定内容
@param {string} content
@returns {jQuery}
@example $("<b>Hello World!</b>").appendTo("p");
 */
_jQuery.prototype.appendTo = function(content){return new _jQuery();};

/**
@name remove
@description 方法移除被选元素，包括所有文本和子节点
@param {string} expr
@returns {jQuery}
@example $("p").remove();
 */
_jQuery.prototype.remove = function(expr){return new _jQuery();};

/**
@name empty
@description 方法从被选元素移除所有内容，包括所有文本和子节点
@returns {jQuery}
@example $("p").empty();
 */
_jQuery.prototype.empty = function(){return new _jQuery();};

/**
@name ready
@description 当 DOM（文档对象模型） 已经加载，并且页面（包括图像）已经完全呈现时，会发生 ready 事件
@param {function} fn
@returns {jQuery}
@example $(document).ready(fn);
 */
_jQuery.prototype.ready = function(fn) {return new _jQuery();};	

/**
@name click
@description 将函数绑定到指定元素的 click 事件
@param {function} fn
@returns {jQuery}
@example $("p").click(fn);
 */
_jQuery.prototype.click = function(fn){return new _jQuery();};

/**
@name dblclick
@description 将函数绑定到指定元素的 dbclick 事件
@param {function} fn
@returns {jQuery}
@example $("p").dblclick(fn);
 */
_jQuery.prototype.dblclick = function(fn){return new _jQuery();};

/**
@name blur
@description 方法将函数绑定到指定元素的blur事件,当元素失去焦点时发生blur事件。 
@description blur()函数触发blur事件，或者如果设置了 function 参数，该函数也可规定当发生 blur 事件时执行的代码。
@param {function} fn -可选。规定当 blur 事件发生时运行的函数。
@returns {jQuery}
@example $(selector).blur();
@example $("input").blur(fn);
 */
_jQuery.prototype.blur = function(fn){return new _jQuery();};

/**
@name change
@description 将函数绑定到指定元素的 change 事件
@param {function} fn
@returns {jQuery}
@example $("input").change(fn);
 */
_jQuery.prototype.change = function(fn){return new _jQuery();};

/**
@name delegate
@description 方法为指定的元素（属于被选元素的子元素）添加一个或多个事件处理程序，并规定当这些事件发生时运行的函数。
@param {string} childSelector -必需。规定要附加事件处理程序的一个或多个子元素。
@param {string} event -必需。规定附加到元素的一个或多个事件。
@param {string} data -可选。规定传递到函数的额外数据。
@param {function} fn -必需。规定当事件发生时运行的函数。
@returns {jQuery}
@example $(selector).delegate(childSelector,event,data,fn)
 */
_jQuery.prototype.delegate = function(childSelector,event,data,fn){return new _jQuery();};

/**
@name undelegate
@description 方法删除由 delegate() 方法添加的一个或多个事件处理程序。
@param {string} selector -可选。规定需要删除事件处理程序的选择器。
@param {string} event -可选。规定需要删除处理函数的一个或多个事件类型。
@param {function} fn -可选。规定要删除的具体事件处理函数。
@returns {jQuery}
@example $(selector).delegate(selector,event,fn)
 */
_jQuery.prototype.undelegate = function(selector,event,fn){return new _jQuery();};

/**
@name die
@description 方法移除所有通过 live() 方法向指定元素添加的一个或多个事件处理程序
@param {function} ev -必需。规定要移除的一个或多个事件处理程序。
@param {function} fn -可选。规定要移除的特定函数。
@returns {jQuery}
@example $(selector).die(event,function);
 */
_jQuery.prototype.die = function(ev,fn){return new _jQuery();};

/**
@name error
@description 方法触发 error 事件，或规定当发生 error 事件时运行的函数。
@param {function} fn -可选。规定当发生 error 事件时运行的函数。
@returns {jQuery}
@example $(selector).error();
@example $(selector).error(function);
*/
_jQuery.prototype.error = function(fn){return new _jQuery();};

/**
@name focus
@description 当元素获得焦点时，发生 focus 事件。
*/
_jQuery.prototype.focus = function(fn){return new _jQuery();};
_jQuery.prototype.keydown = function(fn){return new _jQuery();};
_jQuery.prototype.keypress = function(fn){return new _jQuery();};
_jQuery.prototype.keyup = function(fn){return new _jQuery();};
_jQuery.prototype.live = function(fn){return new _jQuery();};

/**
@name load
@description 方法从服务器加载数据，并把返回的数据放入被选元素中
@param {string} url -参数规定您希望加载的 URL
@param {object} data -参数规定与请求一同发送的查询字符串键/值对集合
@param {function} callback -参数是 load() 方法完成后所执行的函数名称
@returns {jQuery}
@example $(selector).load(URL,data,callback);
@example $("#div1").load("demo_test.txt",function(responseTxt,statusTxt,xhr){
    if(statusTxt=="success")
      alert("外部内容加载成功！");
    if(statusTxt=="error")
      alert("Error: "+xhr.status+": "+xhr.statusText);
  });
 */
_jQuery.prototype.load = function(url,data,callback){return new _jQuery();};

/**
@name unload
@description 方法将事件处理程序绑定到 unload 事件. 当用户离开页面时，会发生 unload 事件
@param {function} fn -必需。规定当触发 unload 事件时运行的函数。
@returns {jQuery}
@example $(window).unload(function(){
  alert("Goodbye!");
});
*/
_jQuery.prototype.unload = function(fn){return new _jQuery();};

_jQuery.prototype.mousedown = function(fn){return new _jQuery();};
_jQuery.prototype.mouseenter = function(fn){return new _jQuery();};
_jQuery.prototype.mouseleave = function(fn){return new _jQuery();};
_jQuery.prototype.mousemove = function(fn){return new _jQuery();};
_jQuery.prototype.mouseout = function(fn){return new _jQuery();};
_jQuery.prototype.mouseover = function(fn){return new _jQuery();};
_jQuery.prototype.mouseup = function(fn){return new _jQuery();};

/**
@name one
@description 方法为被选元素附加一个或多个事件处理程序，并规定当事件发生时运行的函数。当使用 one() 方法时，每个元素只能运行一次事件处理器函数。
@param {string} event -必需。规定添加到元素的一个或多个事件。
@param {object} data -可选。规定传递到函数的额外数据。
@param {function} fn -必需。规定当事件发生时运行的函数。
@returns {jQuery}
@example $(selector).one(event,data,fn);
*/
_jQuery.prototype.one = function(event,data,fn){return new _jQuery();};
_jQuery.prototype.resize = function(fn){return new _jQuery();};
_jQuery.prototype.scroll = function(fn){return new _jQuery();};

/**
@name select
@description 当 textarea 或文本类型的 input 元素中的文本被选择时，会发生 select 事件。
@param {function} fn -可选。规定当发生 select 事件时运行的函数。
@returns {jQuery}
@example $(selector).select();
@example $(selector).select(function);
*/
_jQuery.prototype.select = function(fn){return new _jQuery();};

/**
@name submit
@description 方法触发 submit 事件，或规定当发生 submit 事件时运行的函数。
@param {function} fn -可选。规定当发生 submit 事件时运行的函数。
@returns {jQuery}
@example $(selector).submit();
@example $(selector).submit(function);
*/
_jQuery.prototype.submit = function(fn){return new _jQuery();};

/**
@name toggle
@description 方法用于绑定两个或多个事件处理器函数，以响应被选元素的轮流的 click 事件。
@param {function} function1 -必需。规定当元素在每偶数次被点击时要运行的函数。
@param {function} function2 -必需。规定当元素在每奇数次被点击时要运行的函数。
@param {function} functionN -可选。规定需要切换的其他函数。
@returns {jQuery}
@example $(selector).toggle(function1(),function2(),functionN(),...);
*/
_jQuery.prototype.toggle = function(function1,function2,functionN){return new _jQuery();};

/**
@name trigger
@description 方法触发被选元素的指定事件类型
@param {string} event -必需。规定指定元素要触发的事件。可以使自定义事件（使用 bind() 函数来附加），或者任何标准事件。
@param {object} param1 -可选。传递到事件处理程序的额外参数。
@returns {jQuery}
@example $(selector).toggle(event,[param1,param2,...]);
*/
_jQuery.prototype.trigger = function(event,param1){return new _jQuery();};

/**
@name triggerHandler
@description 方法触发被选元素的指定事件类型。但不会执行浏览器默认动作，也不会产生事件冒泡。
@param {string} event -必需。规定指定元素要触发的事件。
@param {object} param1 -可选。传递到事件处理程序的额外参数。
@returns {jQuery}
@example $(selector).triggerHandler(event,[param1,param2,...]);
*/
_jQuery.prototype.triggerHandler = function(fn){return new _jQuery();};

/**
@name scrollLeft
@description 方法返回或设置匹配元素的滚动条的水平位置
@param {string} offse
@returns {jQuery}
@example $("div").scrollLeft(100);
 */
_jQuery.prototype.scrollLeft = function(offse){return new _jQuery();};

/**
@name scrollTop
@description 设置 <div> 元素中滚动条的垂直偏移
@param {string} offse
@returns {jQuery}
@example $("div").scrollTop(100);
 */
_jQuery.prototype.scrollTop = function(offse){return new _jQuery();};

/**
@name bind
@description 将函数绑定到指定元素的事件
@param {string} eventname
@param {function} fn
@returns {jQuery}
@example $("input").bind("click",fn);
 */
_jQuery.prototype.bind = function(eventname,fn){return new _jQuery();};

/**
@name unbind
@description 解除绑定的指定元素的事件
@param {string} eventname
@param {function} fn
@returns {jQuery}
@example $("input").unbind("click");
 */
_jQuery.prototype.unbind = function(eventname,fn){return new _jQuery();};

/**
@name sort
@description 数组排序
 */
_jQuery.prototype.sort =  [].sort;

/**
@name splice
@description 方法向/从数组中添加/删除项目，然后返回被删除的项目
@param {number} index -必需。整数，规定添加/删除项目的位置，使用负数可从数组结尾处规定位置。
@param {number} howmany -必需。要删除的项目数量。如果设置为 0，则不会删除项目。
@param item1 -可选。向数组添加的新项目。
@returns {Array}
@example arrayObject.splice(index,howmany,item1,.....,itemX);
 */
_jQuery.prototype.splice =  function(index,howmany,item1){return new Array();};


/**
@name inArray
@description 确定第一个参数在数组中的位置，从0开始计数(如果没有找到则返回 -1 )
@param {string} value
@param {Array} array
@returns {number}
@example $.inArray("js", arr); 
 */
_jQuery.prototype.inArray =  function(value, array){return 0;};

/**
@name clone
@description 方法生成被选元素的副本，包含子节点、文本和属性
@returns {jQuery}
@example $("body").append($("p").clone());
 */
_jQuery.prototype.clone =  function(){return new _jQuery();};

/**
@name ajax
@description 方法通过 HTTP 请求加载远程数据
@param {object} setting
@example $.ajax({async:true|false, type:"post"|"get", url: "test.html", dataType:"xml"|"html"|"json"|"script"|"jsonp", error: function(XMLHttpRequest, errorText){}, success: function(returnType){}});
 */
_jQuery.prototype.ajax = function(setting){};

/**
@name extend
@description 函数用于将一个或多个对象的内容合并到目标对象
@param {object} obj1
@param {object} obj2
@returns {jQuery}
@example $.extend(obj1, obj2); //object2合并到object1中
 */
_jQuery.prototype.extend = function(obj1,obj2){return new _jQuery();};

_jQuery.prototype.support = function(){};

_jQuery.prototype.fn = new _jQuery();

var jQuery = new _jQuery();

var $ = function(selector){return new _jQuery();};
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
/*
*portal
*/
function _tlv8_portal() {};
_tlv8_portal= new _tlv8_portal();
tlv8.portal.prototype = new _tlv8_portal();

/**
@name tlv8.portal.closeWindow
@description 关闭portle页面
@param {string} tabId -可以为空，为空时关闭当前窗口
*/
_tlv8_portal.closeWindow = function(tabId){};

/**
@name tlv8.portal.currentTabId
@description 获取当前portlet的ID 
@returns {string}
*/
_tlv8_portal.currentTabId = function(){return new String();};

/**
@name tlv8.portal.openWindow
@description 打开指定的portlet 
@param {string} name -标签名称
@param {string} url -页面地址
@param {string} param -传递的参数
*/
_tlv8_portal.openWindow = function(name, url, param){};

/**
@name tlv8.portal.callBack
@description 回调指定页面的函数
@param {string} tabID -页面ID
@param {string} FnName -回调函数名
@param {string} param -传递的参数
*/
_tlv8_portal.callBack = function(tabID, FnName, param){};

/*
*dialog
*/
function _tlv8_portal_dailog(){};
_tlv8_portal_dailog = new _tlv8_portal_dailog();
tlv8.portal.dailog.prototype = new _tlv8_portal_dailog();

/**
@name tlv8.portal.dailog.openDailog
@description 打开对话框 
@param {string} name
@param {string} url
@param {number} width
@param {number} height
@param {function} callback
@param {object} itemSetInit -{refreshItem:true,enginItem:true,CanclItem:true}
@param {boolean} titleItem -为false时掩藏标题栏
@param {object} urlParam -JS任意类型可以直接传递到对话框页面 对话框页面通过函数getUrlParam获取
*/
_tlv8_portal_dailog.openDailog=function(name, url, width, height, callback, itemSetInit, titleItem, urlParam){};

/**
@name tlv8.portal.dailog.dailogEngin
@description 自定义对话框确定
@param {object} data
*/
_tlv8_portal_dailog.dailogEngin=function(data){};

/**
@name tlv8.portal.dailog.dailogReload
@description 自定义对话框刷新
*/
_tlv8_portal_dailog.dailogReload=function(){};

/**
@name tlv8.portal.dailog.dailogReload
@description 自定义对话框取消
*/
_tlv8_portal_dailog.dailogCancel=function(){};
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
