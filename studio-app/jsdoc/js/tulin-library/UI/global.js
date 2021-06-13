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