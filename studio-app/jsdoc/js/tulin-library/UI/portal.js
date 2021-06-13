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