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


