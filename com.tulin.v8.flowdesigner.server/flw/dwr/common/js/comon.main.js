/*
 * JS基础类库文件
 */
var J$ = function(id) {
	return document.getElementById(id);
};
var J$n = function(name) {
	return document.getElementsByTagName(name);
};
var J_u_encode = function(str) {
	return encodeURIComponent(encodeURIComponent(str));
};
var J_u_decode = function(str) {
	try {
		return decodeURIComponent(decodeURIComponent(str));
	} catch (e) {
		return str;
	}
};
var topparent = function() {
	var nowwdw = window;
	var parentwdw = window.parent;
	while (nowwdw != parentwdw) {
		nowwdw = parentwdw;
		parentwdw = nowwdw.parent;
	}
	return nowwdw;
}();
function checkEncode() {
	var META = document.getElementsByTagName("META");
	for (var i = 0; i < META.length; i++) {
		if (META[i].content.toLowerCase().indexOf("utf-8") > -1)
			return true;
	}
	return false;
}
var createMeta = function() {
	var HTMLhead = document.getElementsByTagName('HEAD')[0];
	var meta = document.createElement('meta');
	meta.setAttribute("http-equiv", "content-type");
	meta.setAttribute("content", "text/html; charset=utf-8");
	HTMLhead.appendChild(meta);
};
if (!checkEncode())
	createMeta();
var justep = {};
justep.yn = {};
/*
 * Action提交URL参数
 */
justep.yn.RequestParam = function() {
	var params = new Map();
	this.params = params;
	var set = function(key, value) {
		this.params.put(key, value);
	};
	this.set = set;
	var get = function(key) {
		return this.params.gut(key);
	};
	this.get = get;
	return this;
};
/*
 * ajax
 */
justep.yn.xmlHttp = function() {
	var xmlhttp;
	if (window.XMLHttpRequest) {
		xmlhttp = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	return xmlhttp;
};
justep.yn.getRequestURI = function() {
	var url = window.location.pathname;
	return url.substring(0, url.lastIndexOf("/"));
};
function getHost() {
	var surl = window.location.href;
	var host = surl.substring(0, surl.indexOf("JBIZ")) + "JBIZ";
	return host;
}
/*
 * @TODO 显示loading状态 @Prama state{true/false}
 */
justep.yn.showSate = function(state) {
	try {
		if (topparent.justep.yn.showSate) {
			topparent.justep.yn.showSate(state);
		}
	} catch (e) {
	}
};
// 添加页面加载事件
addEvent(window, "load", function() {
	if (!window.actiondoing) {
		justep.yn.showSate(false);
	}
	try {
		window.top.checkLogin();
	} catch (e) {
	}
	justep.yn.Context.init();
}, true);
/*
 * 发送请求 @actionName:Action名称 @param:参数 @post:{"post"/"get"}
 * @ayn:是否异步提交{true/false} @callBack:回调函数 @unShowState:是否显示loading{true/false}
 */
justep.yn.XMLHttpRequest = function(actionName, param, post, ayn, callBack,
		unShowState) {
	var localurl = window.location.href;
	if (localurl.indexOf("file://") > -1) {
		return;
	}
	if (!unShowState) {
		justep.yn.showModelState(true);
		window.actiondoing = true;
	}
	var paramMap = param ? param.params : new Map();
	var params = "";
	if (paramMap) {
		if (!paramMap.isEmpty()) {
			var list = paramMap.keySet();
			for (var i = 0; i < list.length; i++) {
				var sk = list[i];
				if (i > 0)
					params += "&";
				params += sk + "=" + J_u_encode(paramMap.get(sk));
			}
		}
	}
	try {
		var rs;
		var hideModelState = function() {
			justep.yn.showSate(false);
			$("#mod_allv").remove();
		};
		$.ajax({
			type : post ? post : "post",
			async : ayn ? ayn : false,
			url : actionName,
			data : params,
			success : function(result, textStatus) {
				rs = result;
				hideModelState();
				try {
					if (rs.data.flag == "timeout") {
						window.top.sessionTimeout();
						return;
					}
				} catch (e) {
				}
				if (callBack && typeof callBack == "function") {
					callBack(rs);
				}
			},
			error : function() {
				hideModelState();
			}
		});
		return rs;
	} catch (e) {
	}
	var xmlHttp = justep.yn.xmlHttp();
	xmlHttp.onreadystatechange = function() {
		if (xmlHttp.readyState == 4) {
			if (xmlHttp.status == 200) {
				try {
					var result = eval('(' + xmlHttp.responseText + ')');
					if (callBack)
						callBack(result);
				} catch (e) {
				}
				if (!unShowState) {
					try {
						justep.yn.showModelState(false);
						window.actiondoing = false;
					} catch (e) {
					}
				}
			}
			if (xmlHttp.status > 200) {
				var err = {
					data : {
						flag : "false",
						data : "",
						message : "action:" + actionName + ",请求错误!"
					}
				};
				if (!unShowState) {
					justep.yn.showModelState(false);
					window.actiondoing = false;
				}
			}
		}
	};
	var brType = getNevType();
	if (brType != 'IE') {
		ayn = true;
	}
	if (post && post.toUpperCase() == "POST") {
		var sendParam = null;
		if (actionName.indexOf("?") > 0) {
			sendParam = actionName.split("?")[1] + "&" + params;
			actionName = actionName.split("?")[0];
		} else {
			sendParam = params;
		}
		xmlHttp.open("POST", actionName, ayn ? ayn : false);
		xmlHttp.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;");
		xmlHttp.setRequestHeader("Content-length", sendParam.length);
		xmlHttp.setRequestHeader("Connection", "close");
		try {
			xmlHttp.send(sendParam);
		} catch (e) {
			justep.yn.showModelState(false);
			return false;
		}
	} else {
		if (actionName.indexOf("?") < 0 && params && params != "")
			actionName += "?";
		else if (params && params != "")
			actionName += "&";
		xmlHttp.open("GET", actionName + params, ayn ? ayn : false);
		xmlHttp.send(null);
	}
	if (ayn != true) {
		try {
			justep.yn.showModelState(false);
			window.actiondoing = false;
			var rs = eval('(' + xmlHttp.responseText + ')');
			return rs;
		} catch (e) {
		}
	}
};
// 初始化当前登陆人信息
function initUserInfo() {
	var personID = justep.yn.Context.getCurrentPersonID();
	var param = new justep.yn.RequestParam();
	param.set("personID", personID);
	justep.yn.XMLHttpRequest("InitUserInfo", param, "get", true, null, true);
}
/*
 * 写操作日志
 */
function writeLog(ev, actionName, discription) {
	if (window.isWriteLog == false)
		return;
	try {
		var HTMLhead = document.getElementsByTagName('HEAD')[0];
		var activateName = HTMLhead.getElementsByTagName("title")[0].innerHTML;
		var srcPath = window.location.pathname;
		if (srcPath.indexOf("?") > 0)
			srcPath = srcPath.substring(0, srcPath.indexOf("?"));
		var param = new justep.yn.RequestParam();
		param.set("personID", justep.yn.Context.getCurrentPersonID());
		param.set("processName", "");
		param.set("activateName", activateName);
		param.set("actionName", actionName ? actionName : "查看");
		param.set("srcPath", srcPath);
		param.set("discription", discription ? discription : "");
		justep.yn.XMLHttpRequest("WriteSystemLogAction", param, "post", true,
				null, true);
	} catch (e) {
	}
}
addEvent(window, "load", writeLog, false);
justep.yn.RequestURLParam = {
	getParam : function(name) {
		var lurl = window.location.href;
		if (lurl.indexOf(name) < 0)
			return;
		if (lurl.indexOf("?") < 0) {
			return "";
		} else {
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
			var r = window.location.search.substr(1).match(reg);
			if (r != null)
				return unescape(r[2]);
			return;
			var par;
			if (lurl.indexOf("?" + name) > 0) {
				par = lurl.substring(lurl.indexOf("?" + name) + 1, lurl.length);
			}
			if (lurl.indexOf("&" + name) > 0) {
				par = lurl.substring(lurl.indexOf("&" + name) + 1, lurl.length);
			}
			if (!par)
				return;
			var start = name.length + 1;
			var len = (par.indexOf("&") > 0) ? par.indexOf("&") : lurl.length;
			var pav = par.substring(start, len);
			return J_u_decode(pav);
		}
	},
	getParamByURL : function(lurl, name) {
		if (lurl.indexOf(name) < 0)
			return;
		if (lurl.indexOf("?") < 0) {
			return "";
		} else {
			var par;
			if (lurl.indexOf("?" + name) > 0) {
				par = lurl.substring(lurl.indexOf("?" + name) + 1, lurl.length);
			}
			if (lurl.indexOf("&" + name) > 0) {
				par = lurl.substring(lurl.indexOf("&" + name) + 1, lurl.length);
			}
			if (!par)
				return;
			var start = name.length + 1;
			var len = (par.indexOf("&") > 0) ? par.indexOf("&") : lurl.length;
			return J_u_decode(par.substring(start, len));
		}
	}
};
justep.yn.Queryaction = function(actionName, post, callBack, data, where, ays) {
	var table = data ? data.table : "";
	where = where ? where : "";
	var dbkay = data ? data.dbkay : "";
	var relation = data ? data.relation : "";
	var orderby = data ? data.orderby : "";
	if (!where || where == "") {
		where = justep.yn.RequestURLParam.getParamByURL(actionName, "where");
	}
	var param = new justep.yn.RequestParam();
	param.set("dbkay", dbkay);
	param.set("table", table);
	param.set("relation", relation);
	param.set("orderby", orderby);
	if (actionName.indexOf("getGridActionBySQL") > -1) {
		actionName += "&where=" + J_u_encode(where);
	} else {
		param.set("where", where);
	}
	var isay = (ays == false) ? ays : true;
	var rscallBack = function(r) {
		if (callBack)
			callBack(r.data);
	};
	var result = justep.yn.XMLHttpRequest(actionName, param, post, isay,
			rscallBack);
	if (ays == false) {
		if (callBack)
			callBack(result.data);
		return result.data;
	}
};
justep.yn.Deleteaction = function(actionName, post, callBack, rowid, data, ays) {
	if (!rowid || rowid == "") {
		alert("rowid不能为空！");
		return;
	}
	var table = data ? data.table : "";
	var dbkay = data ? data.dbkay : "";
	var Cascade = data ? data.Cascade : "";
	var param = new justep.yn.RequestParam();
	param.set("dbkay", dbkay);
	param.set("table", table);
	param.set("rowid", rowid);
	param.set("Cascade", Cascade);
	var isay = (ays == false) ? ays : true;
	var rscallBack = function(r) {
		if (callBack)
			callBack(r.data);
	};
	var result = justep.yn.XMLHttpRequest(actionName, param, post, isay,
			rscallBack);
	if (ays == false) {
		if (callBack)
			callBack(result.data);
		return result.data;
	}
};
justep.yn.saveAction = function(actionName, post, callBack, data, allreturn,
		ays) {
	if (!data || data == "") {
		return;
	}
	var table = data.table;
	var cells = data.cells;
	var dbkay = data ? data.dbkay : "";
	var param = new justep.yn.RequestParam();
	param.set("dbkay", dbkay);
	param.set("table", table);
	param.set("cells", cells);
	var ays_true = (ays == false) ? ays : true;
	var rscallBack = function(r) {
		if (callBack && allreturn)
			callBack(r);
		else if (callBack) {
			callBack(r.data);
		}
	};
	var result = justep.yn.XMLHttpRequest(actionName, param, post, ays_true,
			rscallBack);
	if (ays_true == false && result) {
		if (callBack && allreturn)
			callBack(result);
		else if (callBack) {
			callBack(result.data);
		}
		return result;
	}
};
/*
 * 将字符串转换为XMLDom对象
 */
justep.yn.strToXML = function(str) {
	str = str.toString().replaceAll("&", "&amp;");
	var x = "<?xml version=\"1.0\" encoding='UTF-8' ?>\n<root>\n" + str
			+ "</root>";
	if (window.ActiveXObject) {
		var xmlDom = new ActiveXObject("Microsoft.XMLDOM");
		xmlDom.async = "false";
		xmlDom.loadXML(x);
		return xmlDom;
	} else if (document.implementation
			&& document.implementation.createDocument) {
		var parser = new DOMParser();
		var xmlDom = parser.parseFromString(x, "text/xml");
		return xmlDom;
	}
};
justep.yn.sqlQueryAction = function(dbkey, sql, callBack, ayn) {
	var type = getOs();
	var rsultData = function(r) {
		this.r = r;
		this.data = r.data;
		this.getNode = function(tag) {
			var s = "";
			if (tag == "data") {
				s = r.data.data;
			} else if (tag == "flag") {
				s = r.data.flag;
			} else {
				s = r.data.message;
			}
			return s;
		};
		this.getCount = function() {
			var txt = r.data.data;
			var xmlDoc = justep.yn.strToXML(txt);
			return (type == "MSIE") ? xmlDoc.getElementsByTagName("count")[0].childNodes[0].nodeValue
					: xmlDoc.getElementsByTagName("count")[0].childNodes
							.item(0).textContent;
		};
		this.getColumns = function() {
			var txt = r.data.data;
			var xmlDoc = justep.yn.strToXML(txt);
			return (type == "MSIE") ? xmlDoc.getElementsByTagName("columns")[0].childNodes[0].nodeValue
					: xmlDoc.getElementsByTagName("columns")[0].childNodes
							.item(0).textContent;
		};
		this.getValueByName = function(columnName) {
			if (this.getCount() == 0) {
				return "";
			}
			var txt = r.data.data;
			var xmlDoc = justep.yn.strToXML(txt);
			var redata = (type == "MSIE") ? xmlDoc.getElementsByTagName("rows")
					.item(0).text
					: xmlDoc.getElementsByTagName("rows").item(0).textContent;
			var $dval = redata.split(";");
			var colums = this.getColumns();
			var colum = colums.split(",");
			var index = 0;
			var have = false;
			for (var i = 0; i < colum.length; i++) {
				if (colum[i] == columnName) {
					index = i;
					have = true;
					break;
				}
			}
			if (have == false) {
				alert("指定的列名无效，请注意大小写！");
				return false;
			}
			return $dval[index];
		};
		this.getDatas = function() {
			var $dval = r.data.data;
			if (!$dval || $dval == "")
				return [ "" ];
			var xmldata = justep.yn.strToXML($dval);
			if (!xmldata)
				return [ "" ];
			try {
				var datas = (type == "MSIE") ? xmldata.getElementsByTagName(
						"rows").item(0).text : xmldata.getElementsByTagName(
						"rows").item(0).textContent;
			} catch (e) {
			}
			if (!datas)
				return [ "" ];
			var $dval = datas.split(";");
			return $dval;
		};
	};
	ayn = (ayn == true) ? true : false;
	var param = new justep.yn.RequestParam();
	param.set("dbkey", dbkey);
	param.set("sql", sql);
	var recallback = function(r) {
		if (r.data.flag == "false") {
			alert(r.data.message);
		}
		if (callBack) {
			var res = new rsultData(r);
			callBack(res);
		}
	};
	var result = justep.yn.XMLHttpRequest("sqlQueryAction", param, "POST", ayn,
			recallback);
	if (ayn == false) {
		var res = new rsultData(result);
		return res;
	}
};
justep.yn.sqlQueryActionforJson = function(dbkey, sql, callBack, ayn) {
	ayn = (ayn == true) ? true : false;
	var param = new justep.yn.RequestParam();
	param.set("dbkey", dbkey);
	param.set("sql", sql);
	var recallback = function(r) {
		if (r.data.flag == "false") {
			alert(r.data.message);
			return;
		}
		if (callBack) {
			var reData = (r.data.data) ? (eval("(" + r.data.data + ")")) : [];
			r.data.data = reData;
			callBack(r.data);
		}
	};
	var r = justep.yn.XMLHttpRequest("sqlQueryActionforJson", param, "POST",
			ayn, recallback);
	if (ayn == false) {
		var rvl = r.data.data;
		rvl = rvl.replaceAll("\n", "").replaceAll("\r", "");
		var reData = (r.data.data) ? (eval("(" + rvl + ")")) : [];
		r.data.data = reData;
		return r.data;
	}
};
justep.yn.sqlUpdateAction = function(dbkey, sql, callBack, ayn) {
	ayn = (ayn == true) ? true : false;
	var param = new justep.yn.RequestParam();
	param.set("dbkey", dbkey);
	param.set("sql", sql);
	var recallback = function(r) {
		if (callBack) {
			callBack(r.data);
		}
	};
	var r = justep.yn.XMLHttpRequest("sqlUpdateAction", param, "POST", ayn,
			recallback);
	if (ayn == false) {
		var res = r.data;
		return res;
	}
};
justep.yn.callProcedureAction = function(dbkey, ProduceName, Param, callBack,
		ayn) {
	ayn = (ayn == true) ? true : false;
	var param = new justep.yn.RequestParam();
	param.set("dbkey", dbkey);
	param.set("ProduceName", ProduceName);
	param.set("Param", Param);
	var recallback = function(r) {
		if (callBack) {
			callBack(r.data);
		}
	};
	var r = justep.yn.XMLHttpRequest("callProcedureAction", param, "POST", ayn,
			recallback);
	if (ayn == false) {
		var res = r.data;
		return res;
	}
};
justep.yn.callFunctionAction = function(dbkey, ProduceName, Param, callBack,
		ayn) {
	ayn = (ayn == true) ? true : false;
	var param = new justep.yn.RequestParam();
	param.set("dbkey", dbkey);
	param.set("ProduceName", ProduceName);
	param.set("Param", Param);
	var recallback = function(r) {
		if (callBack) {
			callBack(r.data);
		}
	};
	var r = justep.yn.XMLHttpRequest("callFunctionAction", param, "POST", ayn,
			recallback);
	if (ayn == false) {
		var res = r.data;
		return res;
	}
};
/*
 * 信息提示
 */
function sAlert(str, time) {
	if (topparent != window && topparent.sAlert) {
		topparent.sAlert(str, time);
		return;
	}
	var sAlertDiv = document.getElementById("msgDiv");
	if (sAlertDiv && sAlertDiv.tagName == "DIV") {
		document.body.removeChild(sAlertDiv);
	}
	var msgw, msgh, bordercolor;
	msgw = 100;
	msgh = 20;
	titleheight = 20;
	bordercolor = "#ffffff";
	titlecolor = "#FFFF66";
	var sWidth, sHeight;
	sWidth = document.body.clientWidth;
	sHeight = document.body.clientHeight;
	if (window.innerHeight && window.scrollMaxY) {
		sHeight = window.innerHeight + window.scrollMaxY;
	} else if (document.body.scrollHeight > document.body.offsetHeight) {
		sHeight = document.body.scrollHeight;
	} else {
		sHeight = document.body.offsetHeight;
	}
	var msgObj = document.createElement("div");
	msgObj.setAttribute("id", "msgDiv");
	msgObj.setAttribute("align", "center");
	msgObj.style.background = titlecolor;
	msgObj.style.border = "1px solid " + bordercolor;
	msgObj.style.position = "absolute";
	msgObj.style.left = "88%";
	msgObj.style.top = "1%";
	msgObj.style.font = "12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
	msgObj.style.width = msgw + "px";
	msgObj.style.height = msgh + "px";
	msgObj.style.textAlign = "center";
	msgObj.style.lineHeight = "2px";
	msgObj.style.zIndex = "101";
	var title = document.createElement("h4");
	title.setAttribute("id", "msgTitle");
	title.setAttribute("align", "center");
	title.style.margin = "0";
	title.style.padding = "3px";
	title.style.background = titlecolor;
	title.style.filter = "progid:DXImageTransform.Microsoft.Alpha(startX=20, startY=20, finishX=100, finishY=100,style=1,opacity=75,finishOpacity=100);";
	title.style.opacity = "0.75";
	title.style.height = "20px";
	title.style.font = "12px Verdana, Geneva, Arial, Helvetica, sans-serif";
	title.style.color = "#000000";
	title.innerHTML = str;
	title.onclick = function() {
		document.getElementById("msgDiv").removeChild(title);
		document.body.removeChild(msgObj);
	};
	document.body.appendChild(msgObj);
	document.getElementById("msgDiv").appendChild(title);
	var txt = document.createElement("p");
	txt.style.margin = "1em 0";
	txt.setAttribute("id", "msgTxt");
	txt.innerHTML = str;
	time = time ? time : 500;
	setTimeout('displayActionMessage()', time);
}
var displayActionMessage = function() {
	if (document.getElementById("msgTitle"))
		document.getElementById("msgDiv").removeChild(
				document.getElementById("msgTitle"));
	if (document.getElementById("msgDiv"))
		document.body.removeChild(document.getElementById("msgDiv"));
};
justep.yn.Data = function() {
	this.table = "";
	this.relation = null;
	this.cells = "";
	this.rowid = null;
	this.formid = "";
	this.savAction = "";
	this.dbkay = "";
	this.version = 0;
	this.Cascade = "";
	this.filter = "";
	this.orderby = "";
	this.readonly = false;
	this.childrenData = new Map();
	this.setVersion = function(vi) {
		this.version = vi;
	};
	this.setReadonly = function(sta) {
		try {
			this.readonly = sta;
			if (this.relation && this.relation != "") {
				var relations = this.relation.split(",");
				for (var i = 0; i < relations.length; i++) {
					var revalueEl = document.getElementById(relations[i]);
					if (revalueEl) {
						if (this.readonly == true) {
							revalueEl.readOnly = true;
							if (revalueEl.parentNode.Check
									|| revalueEl.parentNode.Radio) {
								revalueEl.parentNode.disabled = true;
							}
							if (revalueEl.tagName == "DIV" || revalueEl.tagName == "SELECT") {
								revalueEl.disabled = true;
							}
						} else {
							revalueEl.readOnly = false;
							if (revalueEl.parentNode.Check
									|| revalueEl.parentNode.Radio) {
								revalueEl.parentNode.disabled = false;
							}
							if (revalueEl.tagName == "DIV" || revalueEl.tagName == "SELECT") {
								revalueEl.disabled = false;
							}
						}
					}
					try {
						// 只读设置取消单击事件
						$(revalueEl).removeAttr("onclick");
					} catch (e) {
					}
				}
			}
		} catch (e) {
		}
	};
	this.setOrderby = function(ob) {
		if (typeof (ob) == "string")
			this.orderby = ob;
		else
			this.orderby = ob.toString();
	};
	this.setFilter = function(fil) {
		if (typeof (fil) == "string")
			this.filter = fil;
		else
			this.filter = fil.toString();
	};
	this.setCascade = function(cas) {
		if (typeof (cas) == "string")
			this.Cascade = cas;
		else
			this.Cascade = cas.toString();
	};
	this.setRowId = function(id) {
		try {
			if (typeof (id) == "string")
				this.rowid = id;
			else
				this.rowid = id.toString();
		} catch (e) {
		}
	};
	this.setDbkey = function(k) {
		if (typeof (k) == "string")
			this.dbkay = k;
		else {
			try {
				this.dbkay = k.toString();
			} catch (e) {
			}
		}
	};
	this.setSaveAction = function(a) {
		if (typeof (a) == "string")
			this.savAction = a;
		else
			this.savAction = a.toString();
	};
	this.setFormId = function(s) {
		if (typeof (s) == "string")
			this.formid = s;
		else
			this.formid = s.toString();
		try {
			document.getElementById(this.formid).data = this;
		} catch (e) {
		}
	};
	var onDataValueChanged;
	var isEdited = false;
	this.setonDataValueChanged = function(fn) {
		onDataValueChanged = fn;
		this.registerChangeEvent();
		document.getElementById(this.formid).data = this;
	};
	this.registerChangeEvent = function(formid) {
		var dataform = formid || this.formid;
		var mainform = document.getElementById(dataform);
		var $JromTag = function(tagname) {
			return mainform.getElementsByTagName(tagname);
		};
		var inputs = $JromTag("INPUT");
		for (var i = 0; i < inputs.length; i++) {
			if (inputs[i].type == "text" || inputs[i].type == "textarea"
					|| inputs[i].type == "password"
					|| inputs[i].type == "hidden") {
				var $rid = inputs[i].id;
				if ($rid && $rid != ""
						&& $rid.toUpperCase().substr(4, 12) != "_FIXFFCURSOR"
						&& $rid.indexOf("_quick_text") < 0
						&& $rid.indexOf("_page") < 0
						&& $rid.indexOf("_editgridipt") < 0) {
					addEvent(inputs[i], "change", function(evt) {
						isEdited = true;
						var event = evt || window.event;
						var objEdit = event.srcElement ? event.srcElement
								: event.target;
						var _dchg = onDataValueChanged;
						if (_dchg && typeof (_dchg) == "function") {
							_dchg(evt, objEdit.id, objEdit
									.getAttribute("value"), objEdit.type);
						} else if (_dchg && _dchg != "") {
							_dchg = eval(_dchg);
							_dchg(evt, objEdit.id, objEdit
									.getAttribute("value"), objEdit.type);
						}
					}, true);
				}
			}
		}
		var textareas = $JromTag("TEXTAREA");
		for (var i = 0; i < textareas.length; i++) {
			var $rid = textareas[i].id;
			var $rval = textareas[i].getAttribute("value")
					|| textareas[i].innerHTML;
			if ($rid && $rid != ""
					&& $rid.toUpperCase().substr(4, 12) != "_FIXFFCURSOR") {
				addEvent(textareas[i], "change", function(evt) {
					isEdited = true;
					var event = evt || window.event;
					var objEdit = event.srcElement ? event.srcElement
							: event.target;
					var _dchg = onDataValueChanged;
					if (_dchg && typeof (_dchg) == "function") {
						_dchg(evt, objEdit.id, objEdit.getAttribute("value"),
								objEdit.type);
					} else if (_dchg && _dchg != "") {
						_dchg = eval(_dchg);
						_dchg(evt, objEdit.id, objEdit.getAttribute("value"),
								objEdit.type);
					}
				}, true);
			}
		}
		var selects = $JromTag("SELECT");
		for (var i = 0; i < selects.length; i++) {
			var $rid = selects[i].id;
			var $rval = selects[i].getAttribute("value");
			if ($rid && $rid != ""
					&& $rid.toUpperCase().substr(4, 12) != "_FIXFFCURSOR") {
				addEvent(selects[i], "change", function(evt) {
					isEdited = true;
					var event = evt || window.event;
					var objEdit = event.srcElement ? event.srcElement
							: event.target;
					var _dchg = onDataValueChanged;
					if (_dchg && typeof (_dchg) == "function") {
						_dchg(evt, objEdit.id, objEdit.getAttribute("value"),
								objEdit.type);
					} else if (_dchg && _dchg != "") {
						_dchg = eval(_dchg);
						_dchg(evt, objEdit.id, objEdit.getAttribute("value"),
								objEdit.type);
					}
				}, true);
			}
		}
		var labels = $JromTag("LABEL");
		for (var i = 0; i < labels.length; i++) {
			var $rid = labels[i].id;
			var $rval = labels[i].innerHTML;
			if ($rid && $rid != ""
					&& $rid.toUpperCase().substr(4, 12) != "_FIXFFCURSOR") {
				addEvent(labels[i], "change", function(evt) {
					isEdited = true;
					var event = evt || window.event;
					var objEdit = event.srcElement ? event.srcElement
							: event.target;
					var _dchg = onDataValueChanged;
					if (_dchg && typeof (_dchg) == "function") {
						_dchg(evt, objEdit.id, objEdit.getAttribute("value"),
								objEdit.type);
					} else if (_dchg && _dchg != "") {
						_dchg = eval(_dchg);
						_dchg(evt, objEdit.id, objEdit.getAttribute("value"),
								objEdit.type);
					}
				}, true);
			}
		}
	};
	this.setTable = function(t) {
		if (typeof (t) == "string")
			this.table = t;
		else {
			try {
				this.table = t.toString();
			} catch (e) {
			}
		}
	};
	this.setCells = function(cell) {
		if (cell && typeof (cell) == "object") {
			var keys = cell.keySet();
			for (i in keys) {
				var key = keys[i];
				var value = cell.get(key);
				try {
					value = value.replaceAll("<", "#lt;");
					value = value.replaceAll(">", "#gt;");
					value = value.replaceAll("&nbsp;", "#160;");
					value = value.replaceAll("'", "#apos;");
					value = value.replaceAll("&", "#amp;");
				} catch (e) {
				}
				this.cells += "<" + key + "><![CDATA[" + value + "]]></" + key
						+ ">";
			}
		} else if (typeof (cell) == "function") {
			alert("传入的类型不能为：function");
		} else if (!cell) {
			this.cells = "";
		} else {
			this.cells += cell;
		}
	};
	this.saveData = function() {
		if (!this.formid || this.formid == "") {
			alert("未指定保存内容！");
			return;
		}
		var bfSave = document.getElementById(this.formid).getAttribute(
				"beforeSave");
		if (bfSave && bfSave != "") {
			if (typeof bfSave == "function") {
				bfSave(this);
			} else {
				var bfS = window.eval(bfSave);
				if (typeof bfS == "function") {
					bfS(this);
				}
			}
		}
		var mainform = document.getElementById(this.formid);
		mainform.data = this;
		var cell = new Map();
		var $JromTag = function(tagname) {
			return mainform.getElementsByTagName(tagname);
		};
		var inputs = $JromTag("INPUT");
		for (var i = 0; i < inputs.length; i++) {
			if (inputs[i].type == "text" || inputs[i].type == "textarea"
					|| inputs[i].type == "password"
					|| inputs[i].type == "hidden") {
				var $rid = inputs[i].id;
				var $rval = $(inputs[i]).val() || inputs[i].value
						|| inputs[i].getAttribute("value");
				if ($(inputs[i]).attr("rel") == 'remind'
						&& $(inputs[i]).attr("rel-value") == $rval) {
					$rval = "";
				}
				if ($rid && $rid != ""
						&& $rid.toUpperCase().substr(4, 12) != "_FIXFFCURSOR"
						&& $rid.indexOf("_quick_text") < 0
						&& $rid.indexOf("_page") < 0
						&& $rid.indexOf("_editgridipt") < 0) {
					if ($(inputs[i]).attr("format")
							&& $(inputs[i]).attr("format") != "") {
						try {
							$rval = $rval.replaceAll(",", "");
						} catch (e) {
							$rval = "";
						}
					}
					cell.put($rid, $rval);
				}
			}
		}
		var textareas = $JromTag("TEXTAREA");
		for (var i = 0; i < textareas.length; i++) {
			var $rid = textareas[i].id;
			var $rval = $(textareas[i]).val()
					|| textareas[i].getAttribute("value")
					|| textareas[i].innerHTML;
			if ($(textareas[i]).attr("rel") == 'remind'
					&& $(textareas[i]).attr("rel-value") == $rval) {
				$rval = "";
			}
			if ($rid && $rid != ""
					&& $rid.toUpperCase().substr(4, 12) != "_FIXFFCURSOR") {
				if ($(textareas[i]).attr("format")
						&& $(textareas[i]).attr("format") != "") {
					try {
						$rval = $rval.replaceAll(",", "");
					} catch (e) {
						$rval = "";
					}
				}
				cell.put($rid, $rval);
			}
		}
		var selects = $JromTag("SELECT");
		for (var i = 0; i < selects.length; i++) {
			var $rid = selects[i].id;
			var $rval = $(selects[i]).val() || selects[i].value
					|| selects[i].getAttribute("value");
			if ($rid && $rid != ""
					&& $rid.toUpperCase().substr(4, 12) != "_FIXFFCURSOR") {
				if ($(selects[i]).attr("format")
						&& $(selects[i]).attr("format") != "") {
					try {
						$rval = $rval.replaceAll(",", "");
					} catch (e) {
						$rval = "";
					}
				}
				cell.put($rid, $rval);
			}
		}
		var labels = $JromTag("LABEL");
		for (var i = 0; i < labels.length; i++) {
			var $rid = labels[i].id;
			var $rval = labels[i].innerHTML;
			if ($rid && $rid != ""
					&& $rid.toUpperCase().substr(4, 12) != "_FIXFFCURSOR") {
				if ($(labels[i]).attr("format")
						&& $(labels[i]).attr("format") != "") {
					try {
						$rval = $rval.replaceAll(",", "");
					} catch (e) {
						$rval = "";
					}
				}
				cell.put($rid, $rval);
			}
		}
		var rowid = $(mainform).attr("rowid") || mainform.getAttribute("rowid")
				|| mainform.rowid;
		if (rowid) {
			cell.put("rowid", rowid);
		} 
		cell.put("VERSION", (this.version + 1) + "");
		this.setCells();
		this.setCells("<root>");
		this.setCells(cell);
		this.setCells("</root>");
		if (!this.savAction || this.savAction == "") {
			this.setSaveAction("saveAction");
		} else {
			this.setSaveAction(this.savAction);
		}
		var self = this;
		var r = justep.yn.saveAction(this.savAction, "post", null, this, true,
				false);
		writeLog(window.event, "保存数据", "操作的表:" + this.dbkay + "." + this.table);
		if (r) {
			isEdited = false;
			var msessage = "操作成功!";
			if (r.data.flag != "true") {
				msessage = r.data.message;
				// 截取java异常
				if (msessage.indexOf("Exception:") > 0) {
					msessage = msessage.substring(msessage
							.indexOf("Exception:") + 10);
				}
				alert(msessage);
			} else {
				this.version++;// 保存成功记录新的版本号
			}
			if (parent.justep && parent.justep.yn) {
				parent.sAlert(msessage, 500);
			} else {
				sAlert(msessage, 500);
			}
			var rRowID = r.data.rowid;
			self.setRowId(rRowID);
			try {
				mainform.setAttribute("rowid", rRowID);
			} catch (e) {
				mainform.rowid = rRowID;
			}
			if (!self.childrenData.isEmpty()) {
				var keyset = self.childrenData.keySet();
				for (i in keyset) {
					key = keyset[i];
					var childData = self.childrenData.get(key);
					var isCsave = childData.saveData(event, self.refreshData);
					if (!isCsave || isCsave == false) {
						break;
					}
				}
			}
			var afSave = mainform.getAttribute("afterSave");
			if (afSave && afSave != "") {
				if (typeof afSave == "function") {
					afSave(this, r.data);
				} else {
					var afS = window.eval(afSave);
					if (typeof afS == "function") {
						afS(this, r.data);
					}
				}
			}
			return rRowID;
		}
	};
	this.deleteAction = "";
	this.setDeleteAction = function(del) {
		if (typeof (del) == "string")
			this.deleteAction = del;
		else
			this.deleteAction = del.toString();
	};
	this.deleteData = function(isconfirm) {
		var bfDelete = document.getElementById(this.formid).getAttribute(
				"beforeDelete");
		if (bfDelete && bfDelete != "") {
			if (typeof bfDelete == "function") {
				bfDelete(this);
			} else {
				var bfd = window.eval(bfDelete);
				if (typeof bfd == "function") {
					bfd(this);
				}
			}
		}
		if (!this.deleteAction || this.deleteAction == "") {
			this.setDeleteAction("deleteAction");
		} else {
			this.setDeleteAction(this.deleteAction);
		}
		document.getElementById(this.formid).data = this;
		var self = this;
		if (isconfirm == false) {
			justep.yn.Deleteaction(this.deleteAction, "post", function() {
				self.afdelete(self);
			}, this.rowid, this);
			isEdited = false;
			return true;
		} else if (confirm("确定删除数据吗?")) {
			justep.yn.Deleteaction(this.deleteAction, "post", function() {
				self.afdelete(self);
			}, this.rowid, this);
			isEdited = false;
			return true;
		}
		return false;
	};
	this.afdelete = function(self) {
		writeLog(window.event, "删除数据", "操作的表:" + self.dbkay + "." + self.table);
		try {
			document.getElementById(self.formid).reset();
		} catch (e) {
		}
		var afDelete = document.getElementById(self.formid).getAttribute(
				"afterDelete");
		if (afDelete && afDelete != "") {
			if (typeof afDelete == "function") {
				afDelete(document.getElementById(self.formid).data);
			} else {
				var afd = window.eval(afDelete);
				if (typeof afd == "function") {
					afd(document.getElementById(self.formid).data);
				}
			}
		}
	};
	this.refreshData = function(isconfirm, isrefreshSub) {
		if (isEdited == true) {
			if (isconfirm != false)
				if (!confirm("当前数据已更改，刷新数据更改后的数据将丢失，是否确定刷新?")) {
					return false;
				}
		}
		var bfrefresh = document.getElementById(this.formid).getAttribute(
				"beforeRefresh");
		if (bfrefresh && bfrefresh != "") {
			if (typeof bfrefresh == "function") {
				bfrefresh(this);
			} else {
				var bfr = window.eval(bfrefresh);
				if (typeof bfr == "function") {
					bfr(this);
				}
			}
		}
		this.relation = "";
		var mainform = document.getElementById(this.formid);
		mainform.reset();
		var $JromTag = function(tagname) {
			return mainform.getElementsByTagName(tagname);
		};
		mainform.isrefreshSub = isrefreshSub;
		mainform.data = this;
		var rowid = $(mainform).attr("rowid") || mainform.getAttribute("rowid")
				|| mainform.rowid;
		if (rowid && rowid != "") {
			this.setRowId(trim(rowid));
		}
		if (!this.filter || this.filter == "") {
			if (this.dbkay && this.dbkay != "system") {
				this.setFilter("fID='" + this.rowid + "'");
			} else {
				this.setFilter("sID='" + this.rowid + "'");
			}
		}
		if (this.dbkay && this.dbkay != "system") {
			this.relation += ",FID";
		} else {
			this.relation += ",SID";
		}
		var inputs = $JromTag("INPUT");
		for (var i = 0; i < inputs.length; i++) {
			if (inputs[i].type == "text" || inputs[i].type == "textarea"
					|| inputs[i].type == "password"
					|| inputs[i].type == "hidden") {
				var $rid = inputs[i].id;
				if ($rid && $rid != ""
						&& $rid.toUpperCase().substr(4, 12) != "_FIXFFCURSOR"
						&& $rid.indexOf("_editgridipt") < 0
						&& $rid.indexOf("_quick_text") < 0
						&& $rid.indexOf("_page") < 0) {
					this.relation += "," + $rid;
				}
			}
		}
		var textareas = $JromTag("TEXTAREA");
		for (var i = 0; i < textareas.length; i++) {
			var $rid = textareas[i].id;
			if ($rid && $rid != ""
					&& $rid.toUpperCase().substr(4, 12) != "_FIXFFCURSOR") {
				this.relation += "," + $rid;
			}
		}
		var selects = $JromTag("SELECT");
		for (var i = 0; i < selects.length; i++) {
			var $rid = selects[i].id;
			if ($rid && $rid != ""
					&& $rid.toUpperCase().substr(4, 12) != "_FIXFFCURSOR") {
				this.relation += "," + $rid;
			}
		}
		var labels = $JromTag("LABEL");
		for (var i = 0; i < labels.length; i++) {
			var $rid = labels[i].id;
			if ($rid && $rid != ""
					&& $rid.toUpperCase().substr(4, 12) != "_FIXFFCURSOR") {
				this.relation += "," + $rid;
			}
		}
		this.relation += ",VERSION";
		this.relation = this.relation.replace(",", "");
		var self = this;
		justep.yn.Queryaction("queryAction", "post", function(rd) {
			self.setData(rd, self.formid);
		}, this, this.filter, true);
		return true;
	};
	this.setData = function(data, nowformid) {
		var taptt = justep.yn.RequestURLParam.getParam("activity-pattern");
		var isTasksub = (taptt=="detail");
		if(isTasksub){
			this.setReadonly(true);
		}
		var message = "操作成功!";
		if (data.flag == "false") {
			msessage = r.data.message;
			if (msessage.indexOf("Exception:") > 0) {
				msessage = msessage
						.substring(msessage.indexOf("Exception:") + 10);
			}
			alert(msessage);
		} else {
			try {
				var xmlElem = justep.yn.strToXML(data.data);
				var relations = data.relation.split(",");
				var type = getOs();
				for (var i = 0; i < relations.length; i++) {
					var revalueEl = document.getElementById(nowformid)[relations[i]]
							|| document.getElementById(relations[i]);
					if (data.data && data.data != "") {
						var sValue = (type == "MSIE") ? xmlElem
								.getElementsByTagName(relations[i])[0].text
								: xmlElem.getElementsByTagName(relations[i])
										.item(0).textContent;
						if (relations[i] == "SID" || relations[i] == "FID") {
							document.getElementById(nowformid).rowid = sValue;
							document.getElementById(nowformid).data
									.setRowId(sValue);
						}
						if (relations[i] == "VERSION") {
							document.getElementById(nowformid).data
									.setVersion(parseInt(sValue) || 0);
						}
					} else {
						document.getElementById(nowformid).rowid = "";
						document.getElementById(nowformid).data.setRowId("");
					}
					if (revalueEl && data.data && data.data != "") {
						var $dval = "";
						try {
							$dval = (type == "MSIE") ? xmlElem
									.getElementsByTagName(relations[i])[0].text
									: xmlElem
											.getElementsByTagName(relations[i])
											.item(0).textContent
											|| "";
						} catch (e) {
						}
						var elformat = "";
						try {
							elformat = revalueEl.getAttribute("format");
						} catch (e) {
						}
						if (elformat && elformat != "") {
							var Sdate = justep.yn.System.Date.strToDate($dval);
							if (Sdate && elformat == "yyyy-MM-dd") {
								$dval = Sdate.format("yyyy-MM-dd");
							} else if (Sdate
									&& elformat == "yyyy-MM-dd hh:mm:ss") {
								$dval = Sdate.format("yyyy-MM-dd hh:mm:ss");
							} else if (Sdate
									&& elformat.indexOf("yyyy-MM-dd") > -1) {
								$dval = Sdate.format(elformat);
							} else if ($dval) {
								revalueEl.olvalue = $dval;
								$dval = justep.yn.numberFormat($dval, elformat);
								if (!revalueEl.getAttribute("readonly")) {
									revalueEl.onfocus = function() {
										this
												.setAttribute("value",
														this.olvalue);
									};
									revalueEl.onblur = function() {
										this.olvalue = this
												.getAttribute("value");
										this.setAttribute("value", justep.yn
												.numberFormat(this
														.getAttribute("value"),
														this.format));
									};
								}
							}
						} else {
							$dval = $dval.replaceAll("#160;", "&nbsp;");
							$dval = $dval.replaceAll("#lt;", "<");
							$dval = $dval.replaceAll("#gt;", ">");
							$dval = $dval.replaceAll("#apos;", "'");
							$dval = $dval.replaceAll("#amp;", "&");
						}
						if ("LABEL" == revalueEl.tagName) {
							try {
								revalueEl.innerHTML = $dval;
								revalueEl.title = $dval;
								revalueEl.setAttribute("value", $dval);
							} catch (e) {
							}
						} else if ("TEXTAREA" == revalueEl.tagName) {
							try {
								revalueEl.value = $dval;
								revalueEl.title = $dval;
								revalueEl.setAttribute("value", $dval);
								$(revalueEl).val($dval);
							} catch (e) {
							}
						} else {
							try {
								if (revalueEl.tagName == "DIV") {
									revalueEl = revalueEl
											.getElementsByTagName("input")[0] ? revalueEl
											.getElementsByTagName("input")[0]
											: revalueEl
													.getElementsByTagName("select")[0];
									revalueEl.setAttribute("value", $dval);
									revalueEl.title = $dval;
									revalueEl.value = $dval;
								} else {
									revalueEl.setAttribute("value", $dval);
									revalueEl.title = $dval;
									revalueEl.value = $dval;
								}
								$(revalueEl).val($dval);
							} catch (e) {
							}
							try {
								// 单选框赋值
								document.getElementById(revalueEl.id
										+ "_compent").Radio.initData(revalueEl);
							} catch (e) {
							}
							try {
								// 多选框赋值
								document.getElementById(revalueEl.id
										+ "_compent").Check.initData(revalueEl);
							} catch (e) {
							}
						}
					}
				}
			} catch (e) {
				alert(e.name + ":" + e.message);
			}
		}
		isEdited = false;
		var childrenData = document.getElementById(nowformid).data.childrenData;
		var isrefreshSub = document.getElementById(nowformid).isrefreshSub;
		if (!childrenData.isEmpty() && isrefreshSub != false) {
			var keyset = childrenData.keySet();
			for (i in keyset) {
				key = keyset[i];
				var childData = childrenData.get(key);
				var isCsave = childData.refreshData(null, false);
			}
		}
		justep.yn.requerat();// 提示设置
		var afrefresh = document.getElementById(nowformid).getAttribute(
				"afterRefresh");
		if (afrefresh && afrefresh != "") {
			if (typeof afrefresh == "function") {
				afrefresh(document.getElementById(nowformid).data);
			} else {
				var afr = window.eval(afrefresh);
				if (typeof afr == "function") {
					afr(document.getElementById(nowformid).data);
				}
			}
		}
		if (parent.justep && parent.justep.yn) {
			parent.sAlert(message, 500);
		} else {
			sAlert(message, 500);
		}
	};
	this.getValueByName = function(name) {
		try {
			var revalueEl = document.getElementById(this.formid)[name]
					|| document.getElementById(name);
			var elformat = revalueEl.getAttribute("format");
			if (revalueEl) {
				if (revalueEl.tagName == "INPUT"
						|| revalueEl.tagName == "SELECT") {
					var $dval = $(revalueEl).val()
							|| revalueEl.getAttribute("value");
					try {
						if (elformat && elformat == "0,000.00") {
							$dval = $dval.toString().replaceAll(",", "");
						}
					} catch (e) {
					}
					return $dval;
				} else if (revalueEl.tagName == "LABEL") {
					var $dval = revalueEl.innerHTML;
					try {
						if (elformat && elformat == "0,000.00") {
							$dval = $dval.toString().replaceAll(",", "");
						}
					} catch (e) {
					}
					return $dval;
				} else {
					var $dval = $(revalueEl).val()
							|| revalueEl.getAttribute("value");
					return $dval;
				}
			} else {
				alert("指定的列名无效：" + name);
			}
		} catch (e) {
			alert("指定的列名无效：" + name);
		}
		return "";
	};
	this.setValueByName = function(name, value) {
		try {
			var revalueEl = document.getElementById(this.formid)[name]
					|| document.getElementById(name);
			if (revalueEl) {
				if (revalueEl.tagName == "LABEL") {
					revalueEl.innerHTML = value;
					revalueEl.title = value;
					revalueEl.setAttribute("value", value);
				} else {
					revalueEl.setAttribute("value", value);
					$(revalueEl).val(value);
					revalueEl.title = value;
				}
				isEdited = true;
			} else {
				alert("指定的列名无效：" + name);
			}
		} catch (e) {
			alert("指定的列名无效：" + name);
		}
	};
	return this;
};
function getOs() {
	var OsObject = "";
	if (navigator.userAgent.indexOf("MSIE") > 0) {
		return "MSIE";
	}
	if (isFirefox = navigator.userAgent.indexOf("Firefox") > 0) {
		return "Firefox";
	}
	if (isSafari = navigator.userAgent.indexOf("Safari") > 0) {
		return "Safari";
	}
	if (isCamino = navigator.userAgent.indexOf("Camino") > 0) {
		return "Camino";
	}
	if (isMozilla = navigator.userAgent.indexOf("Gecko/") > 0) {
		return "Gecko";
	}
}
justep.yn.toolbar = function(div, insertitem, saveitem, deleteitem, refreshitem) {
	var items = {
		insertAction : function() {
			if (div.getAttribute("insertAction")) {
				eval(div.getAttribute("insertAction"));
			}
		},
		saveAction : function() {
			if (div.getAttribute("saveAction")) {
				eval(div.getAttribute("saveAction"));
			}
		},
		deleteAction : function() {
			if (div.getAttribute("deleteAction")) {
				eval(div.getAttribute("deleteAction"));
			}
		},
		refreshAction : function() {
			if (div.getAttribute("refreshAction")) {
				eval(div.getAttribute("refreshAction"));
			}
		},
		setItemStatus : function(insertitem, saveitem, deleteitem, refreshitem) {
			var taptt = justep.yn.RequestURLParam.getParam("activity-pattern");
			var isTasksub = (taptt=="detail");
			var insert_item = document.getElementById(div.id + "insert-item");
			var insert_item_img = document.getElementById(div.id
					+ "insert-item-img");
			var save_item = document.getElementById(div.id + "save-item");
			var save_item_img = document.getElementById(div.id
					+ "save-item-img");
			var delete_item = document.getElementById(div.id + "delete-item");
			var delete_item_img = document.getElementById(div.id
					+ "delete-item-img");
			var refresh_item = document.getElementById(div.id + "refresh-item");
			var refresh_item_img = document.getElementById(div.id
					+ "refresh-item-img");
			var ienable = $dpimgpath + "toolbar/insert.gif";
			var iread = $dpimgpath + "toolbar/un_insert.gif";
			var senable = $dpimgpath + "toolbar/save.gif";
			var sread = $dpimgpath + "toolbar/un_save.gif";
			var denable = $dpimgpath + "toolbar/remove.gif";
			var dread = $dpimgpath + "toolbar/un_remove.gif";
			var renable = $dpimgpath + "toolbar/refreshbill.gif";
			var rread = $dpimgpath + "toolbar/un_refreshbill.gif";
			if (insertitem == false) {
				document.getElementById(div.id + "insert-item-tr").style.display = "none";
			} else if (insertitem == "readonly" || isTasksub) {
				insert_item_img.src = iread;
				insert_item.onclick = null;
			} else if (insertitem == true) {
				document.getElementById(div.id + "insert-item-tr").style.display = "";
				insert_item_img.src = ienable;
				insert_item.onclick = items.insertAction;
			}
			if (saveitem == false) {
				document.getElementById(div.id + "save-item-tr").style.display = "none";
			} else if (saveitem == "readonly" || isTasksub) {
				save_item_img.src = sread;
				save_item.onclick = null;
			} else if (saveitem == true) {
				document.getElementById(div.id + "save-item-tr").style.display = "";
				save_item_img.src = senable;
				save_item.onclick = items.saveAction;
			}
			if (deleteitem == false) {
				document.getElementById(div.id + "delete-item-tr").style.display = "none";
			} else if (deleteitem == "readonly" || isTasksub) {
				delete_item_img.src = dread;
				delete_item.onclick = null;
			} else if (deleteitem == true) {
				document.getElementById(div.id + "delete-item-tr").style.display = "";
				delete_item_img.src = denable;
				delete_item.onclick = items.deleteAction;
			}
			if (refreshitem == false) {
				document.getElementById(div.id + "refresh-item-tr").style.display = "none";
			} else if (refreshitem == "readonly" || isTasksub) {
				refresh_item_img.src = rread;
				refresh_item.onclick = null;
			} else if (refreshitem == true) {
				document.getElementById(div.id + "refresh-item-tr").style.display = "";
				refresh_item_img.src = renable;
				refresh_item.onclick = items.refreshAction;
			}
		}
	};
	this.items = items;
	if (!checkPathisHave($dpcsspath + "toolbar.main.css"))
		createStyleSheet($dpcsspath + "toolbar.main.css");
	div.style.overflow = "hidden";
	var Stander = "<table style='width:100%;align:left;' class='standard_toolbar' border='0' id='toolbar'><tr><td width='54px' align='left' id='"
			+ div.id
			+ "insert-item-tr'><a href='javascript:void(0)' id='"
			+ div.id
			+ "insert-item' class='toobar_item' title='新增'><img id='"
			+ div.id
			+ "insert-item-img'  src='"
			+ $dpimgpath
			+ "toolbar/insert.gif'/></a></td><td width='54px' align='left' id='"
			+ div.id
			+ "save-item-tr'><a href='javascript:void(0)' id='"
			+ div.id
			+ "save-item' class='toobar_item' title='保存'><img id='"
			+ div.id
			+ "save-item-img'  src='"
			+ $dpimgpath
			+ "toolbar/save.gif'/></a></td><td width='54px' align='left' id='"
			+ div.id
			+ "delete-item-tr'><a href='javascript:void(0)' id='"
			+ div.id
			+ "delete-item' class='toobar_item' title='删除'><img id='"
			+ div.id
			+ "delete-item-img'  src='"
			+ $dpimgpath
			+ "toolbar/remove.gif'/></a></td><td width='55px' align='left' id='"
			+ div.id
			+ "refresh-item-tr'><a href='javascript:void(0)' id='"
			+ div.id
			+ "refresh-item' class='toobar_item' title='刷新'><img id='"
			+ div.id
			+ "refresh-item-img' src='"
			+ $dpimgpath
			+ "toolbar/refreshbill.gif'/></a></td><td /></tr></table>";
	div.innerHTML = Stander;
	div.items = items;
	this.items.setItemStatus(insertitem, saveitem, deleteitem, refreshitem);
	return this;
};
var Map = function() {
	var struct = function(key, value) {
		this.key = key;
		this.value = value;
	};
	var put = function(key, value) {
		for (var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].key === key) {
				this.arr[i].value = value;
				return;
			}
		}
		this.arr[this.arr.length] = new struct(key, value);
	};
	var get = function(key) {
		for (var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].key === key) {
				return this.arr[i].value;
			}
		}
		return null;
	};
	var remove = function(key) {
		var v;
		for (var i = 0; i < this.arr.length; i++) {
			v = this.arr.pop();
			if (v.key === key) {
				continue;
			}
			this.arr.unshift(v);
		}
	};
	var size = function() {
		return this.arr.length;
	};
	var isEmpty = function() {
		return this.arr.length <= 0;
	};
	var containsKey = function(key) {
		var result = false;
		for (var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].key === key) {
				result = true;
			}
		}
		return result;
	};
	var containsValue = function(value) {
		var result = false;
		for (var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].value === value) {
				result = true;
			}
		}
		return result;
	};
	var keySet = function() {
		var result = new Array();
		for (var i = 0; i < this.arr.length; i++) {
			result.push(this.arr[i].key);
		}
		return result;
	};
	this.arr = new Array();
	this.struct = struct;
	this.get = get;
	this.put = put;
	this.remove = remove;
	this.size = size;
	this.isEmpty = isEmpty;
	this.containsKey = containsKey;
	this.containsValue = containsValue;
	this.keySet = keySet;
	return this;
};
Map.prototype.toString = function() {
	var rs = "{";
	for (var i = 0; i < this.arr.length; i++) {
		if (i > 0)
			rs += ",";
		rs += "\"" + this.arr[i].key + "\":\"" + this.arr[i].value + "\"";
	}
	rs += "}";
	return rs;
};
justep.yn.portal = {};
justep.yn.portal.closeWindow = function(tabId) {
	try {
		var isclose = false;
		if (topparent != window) {
			try {
				topparent.justep.yn.portal.closeWindow(tabId);
				isclose = true;
			} catch (e) {
				isclose = false;
			}
		}
		if (!isclose) {
			var currentTabId = tabId ? tabId : topparent.$.jpolite.getTabID();
			topparent.$.jpolite.removeTab(currentTabId);
		}
	} catch (e) {
		window.opener = null;
		window.open('', '_self');
		window.close();
	}
};
justep.yn.portal.currentTabId = function() {
	try {
		return topparent.$.jpolite.getTabID();
	} catch (e) {
		alert("justep.yn.portal.currentTabId: " + e.message);
	}
};
justep.yn.portal.openWindow = function(name, url, param) {
	try {
		if (url.indexOf("?") > 0) {
			var reUrl = url.substring(0, url.indexOf("?"));
			var rePar = url.substring(url.indexOf("?") + 1);
			var parPe = rePar.split("&");
			for (var i = 0; i < parPe.length; i++) {
				var perP = parPe[i];
				parPe[i] = perP.split("=")[0] + "="
						+ J_u_encode(J_u_decode(perP.split("=")[1]));
			}
			url = reUrl + "?" + parPe.join("&");
		}
		if (url.indexOf("?") > 0) {
			url += "&systemp=" + new Date().getTime();
		} else {
			url += "?systemp=" + new Date().getTime();
		}
		topparent.$.X.runFunc({
			name : name,
			url : url,
			process : "",
			activity : "",
			params : param
		});
	} catch (e) {
		window.open(url, name);
		// alert("justep.yn.portal.openWindow: " + e.message);
	}
};
justep.yn.portal.callBack = function(tabID, FnName, param) {
	try {
		topparent.justep.yn.portal.callBack(tabID, FnName, param);
	} catch (e) {
	}
};
justep.yn.portal.dailog = {
	transeUrl : function(url) {
		// 路径转换[utf-8编码]
		if (url.indexOf("http://") < 0 && url.indexOf("https://") < 0
				&& url.indexOf("/JBIZ/") != 0) {
			url = "/JBIZ" + url;
		}
		if (url.indexOf("?") > 0) {
			var reUrl = url.substring(0, url.indexOf("?"));
			var rePar = url.substring(url.indexOf("?") + 1);
			var parPe = rePar.split("&");
			for (var i = 0; i < parPe.length; i++) {
				var perP = parPe[i];
				parPe[i] = perP.split("=")[0] + "="
						+ J_u_encode(J_u_decode(perP.split("=")[1]));
			}
			url = reUrl + "?" + parPe.join("&");
		}
		return url;
	},
	openDailog : function(name, url, width, height, callback, itemSetInit,
			titleItem, urlParam) {
		justep.yn.portal.dailog.callback = callback;
		url = this.transeUrl(url);
		if ($("<div></div>").dialog) {
			$("#windowdialogIframe").remove();
			$("#gldd").remove();
			var dihtml = '<iframe id="windowdialogIframe" frameborder="0" style="width: 100%;height: 100%;"></iframe>';
			var globaldialog = $('<div id="gldd" style="width:0px;height:0px;overflow:hidden;">'
					+ dihtml + '</div>');
			$(document.body).append(globaldialog);
			var dlwidth = parseInt(width);
			var dlheight = parseInt(height);
			if (dlwidth > $(window).width()) {
				dlwidth = $(window).width() - 40;
			}
			if (dlheight > $(window).height()) {
				dlheight = $(window).height() - 40;
			}
			var dlparam = {
				title : name,
				width : dlwidth,
				height : dlheight,
				closed : false,
				cache : false,
				modal : true,
				collapsible : true,
				minimizable : false,
				maximizable : true,
				resizable : true
			};
			if (itemSetInit == false) {
				dlparam.buttons = undefined;
			} else {
				dlparam.buttons = [ {
					text : '确定',
					handler : function() {
						var dlw = J$("windowdialogIframe").contentWindow;
						if (dlw.dailogEngin) {
							var re = dlw.dailogEngin();
							if ((typeof re == "boolean") && re == false) {

							} else {
								if (callback) {
									callback(re);
								}
								$('#gldd').dialog('close');
							}
						} else {
							$('#gldd').dialog('close');
						}
					}
				}, {
					text : '取消',
					handler : function() {
						$('#gldd').dialog('close');
					}
				} ];
			}
			globaldialog.dialog(dlparam);
			$("#windowdialogIframe").height(
					$("#windowdialogIframe").parent().height());
			$("#windowdialogIframe").attr("src", url);
			try {
				var func_iframe = document.getElementById("windowdialogIframe");
				if (func_iframe.attachEvent) {
					func_iframe.attachEvent("onload", function() {
						try {
							var dialogWin = func_iframe.contentWindow;
							if (dialogWin.getUrlParam) {
								dialogWin.getUrlParam(urlParam);
							}
						} catch (e) {
						}
					});
				} else {
					func_iframe.onload = function() {
						try {
							var dialogWin = func_iframe.contentWindow;
							if (dialogWin.getUrlParam) {
								dialogWin.getUrlParam(urlParam);
							}
						} catch (e) {
						}
					};
				}
			} catch (e) {
			}
			return;
		}
		if (!checkPathisHave($dpcsspath + "toolbar.main.css"))
			createStyleSheet($dpcsspath + "toolbar.main.css");
		var isIE6 = justep.yn.isIE6();
		var msgw, msgh, bordercolor;
		msgw = width;
		msgh = height;
		titleheight = 16;
		bordercolor = "#e3ebff";
		titlecolor = "#fff";
		var sWidth, sHeight;
		sWidth = document.body.clientWidth;
		sHeight = document.body.clientHeight;
		var sLeftS = (parseInt(sWidth) - parseInt(width)) / 2
				/ parseInt(sWidth);
		sLeftS = ((sLeftS > 0) ? sLeftS : 0.3) * 100;
		var sTopS = (parseInt(sHeight) - parseInt(height)) / 2
				/ parseInt(sHeight);
		sTopS = ((sTopS > 0) ? sTopS : 0.1) * 100;
		if (window.innerHeight && window.scrollMaxY) {
			sHeight = window.innerHeight + window.scrollMaxY;
		} else if (document.body.scrollHeight > document.body.offsetHeight) {
			sHeight = document.body.scrollHeight;
		} else {
			sHeight = document.body.offsetHeight;
		}
		window.isIE = window.Event ? false : true;
		var getMouseCoords = function(e) {
			var xx = e.originalEvent.x || e.originalEvent.layerX || 0;
			var yy = e.originalEvent.y || e.originalEvent.layerY || 0;
			var mus = {
				x : xx,
				y : yy
			};
			return mus;
		};
		function vDrag(o, ho, initArr) {
			ho = ho || o;
			o.style.position = "absolute";
			if (!isIE) {
				ho.firstChild.onmousedown = function() {
					return false;
				};
			}
			var homousedownfn = function(a) {
				a = a || window.event;
				var lf = $(o).offset().left;
				var tp = $(o).offset().top;
				var amus = getMouseCoords(a);
				var x = amus.x, y = amus.y;
				var dfleft = amus.x - lf;
				var dftop = amus.y - tp;
				if (ho.setCapture) {
					ho.setCapture();
				} else if (window.captureEvents) {
					window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);
				}
				var documentmousemoveFn = function(a) {
					a = a || window.event;
					var mus = getMouseCoords(a);
					var tx = mus.x - dfleft, ty = mus.y - dftop;
					if (initArr) {
						o.style.left = (tx < initArr[0] ? initArr[0]
								: tx > initArr[2] ? initArr[2] : tx)
								+ "px";
						o.style.top = (ty < initArr[1] ? initArr[1]
								: ty > initArr[3] ? initArr[3] : ty)
								+ "px";
					} else {
						var newW = parseInt(o.style.width);
						var newH = parseInt(o.style.height);
						if (tx > (sWidth - newW))
							tx = (sWidth - newW);
						if (tx < 0)
							tx = 0;
						var Height = (sHeight > document.body.offsetHeight) ? sHeight
								: document.body.offsetHeight;
						if (ty > (Height - newH))
							ty = (Height - newH);
						if (ty < 0)
							ty = 0;
						o.style.left = tx + "px";
						o.style.top = ty + "px";
					}
					;
				};
				$(document).bind("mousemove", documentmousemoveFn);
				$(document).bind("mouseup", function() {
					if (ho.releaseCapture)
						ho.releaseCapture();
					else if (window.captureEvents)
						window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);
					$(document).unbind("mousemove");
					$(document).unbind("mouseup");
				});
			};
			$(ho).bind("mousedown", homousedownfn);
		}
		function createWebWindow(o, intW, intH) {
			var winSelf = o;
			var winTitle = document.getElementById("dialog_title_lab");
			var winContent = document.getElementById("dailog_table_trIf");
			var winDbox = document.getElementById("dialog_footer_cod");
			var minW = 50, minH = 50;
			var _self = this;
			var wX = winSelf.offsetWidth - winContent.offsetWidth;
			var wH = winSelf.offsetHeight - winContent.offsetHeight;
			winTitle.style.cursor = "move";
			winDbox.style.cursor = "se-resize";
			$(winDbox).bind("mousedown", function(e) {
				e = e || window.event;
				var MCD = getMouseCoords(e);
				winSelf.startX = MCD.x;
				winSelf.startY = MCD.y;
				winSelf.startW = winSelf.offsetWidth;
				winSelf.startH = winSelf.offsetHeight;
				if (winDbox.setCapture)
					winDbox.setCapture();
				else if (window.captureEvents)
					window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);
				$(document).bind("mousemove", function(e) {
					e = e || window.event;
					var mus = getMouseCoords(e);
					var newW = (winSelf.startW + (mus.x - winSelf.startX));
					var newH = (winSelf.startH + (mus.y - winSelf.startY));
					resizeWin(newW, newH);
				});
				$(document).bind("mouseup", function() {
					if (winDbox.releaseCapture)
						winDbox.releaseCapture();
					else if (window.captureEvents)
						window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);
					$(document).unbind("mousemove");
					$(document).unbind("mouseup");
				});
			});
			function resizeWin(newW, newH) {
				newW = newW < minW ? minW : newW;
				newH = newH < minH ? minH : newH;
				winSelf.style.width = newW + "px";
				winSelf.style.height = newH + "px";
			}
			{
				resizeWin(intW, intH);
				vDrag(o, winTitle);
			}
		}
		var allviewcap = document.createElement("div");
		allviewcap.setAttribute("id", "dailogmsgDiv");
		allviewcap.style.left = "0px";
		allviewcap.style.top = "0px";
		allviewcap.style.width = ($(document.body).width()) + "px";
		allviewcap.style.height = sHeight;
		allviewcap.style.position = "absolute";
		allviewcap.style.background = "#777";
		allviewcap.style.filter = "alpha(opacity = 30)";
		allviewcap.style.opacity = 0.3;
		allviewcap.style.zIndex = "998";
		var msgObj = document.createElement("div");
		msgObj.setAttribute("id", "msgObjDiv");
		msgObj.setAttribute("align", "center");
		msgObj.style.background = "#fff";
		msgObj.style.position = "absolute";
		msgObj.style.border = "1px solid #73A2d6";
		msgObj.style.width = msgw + "px";
		msgObj.style.height = msgh + "px";
		var cuScrollTop = (document.body.scrollTop || document.documentElement.scrollTop);
		var myLeft = (document.body.offsetWidth - msgw) / 2;
		var myTop = (document.body.offsetHeight + cuScrollTop - msgh - 10) / 2;
		msgObj.style.left = myLeft
				+ (document.body.scrollLeft || document.documentElement.scrollLeft);
		msgObj.style.top = (myTop < 0) ? 5
				: (document.body.offsetHeight - msgh - 10) / 2 + cuScrollTop;
		msgObj.style.zIndex = "999";
		var dialogMain = "<table width='100%' height='100%' border='0' cellpadding='0' cellspacing='0' id='dailog_table' style='margin :0; border: 0;  table-layout: fixed; word-break: break-all;'>  <tr> <td style='background-color: #73A2d6; color: #fff; padding-left: 4px; padding-top: 2px; font-weight: bold; font-size: 12px; height: 25px;' align='left' id='dialog_title_lab'> "
				+ name
				+ "</td><td style='align:right; background-color: #73A2d6; color: #fff; padding-top: 0px; padding-right: 0px; margin-right:2px; font-size: 12px; width:140px; height: 25px; text-align:right;'><div "
				+ (false == titleItem ? "style='display:none;'" : "")
				+ "><a class='dialog_item' href='javascript:void(0)' id='dialogTilteMax' style='text-decoration: none; color: #FFFFFF' title='最大化'>&nbsp;口&nbsp;</a><a class='dialog_item' href='javascript:void(0)' id='dialogTilteNor' style='text-decoration: none; color: #FFFFFF; display:none; ' title='还原'>&nbsp;□&nbsp;</a> <a class='dialog_item' href='javascript:void(0)' id='dialogTilteMin' style='text-decoration: none; color: #FFFFFF' title='最小化'>&nbsp;一&nbsp;</a> <a class='dialog_item' href='javascript:void(0)' id='dialogTilteCancel' style='text-decoration: none;color: #FFFFFF;' title='关闭对话框'><b>&nbsp;×&nbsp;</b></a></div></td></tr>";
		dialogMain += "<tr id='dailog_table_trIf'><td colspan='2' align='center' style='height: 98% auto;'><div id='msgTxt' style='height:100%'/></td></tr><tr id='dialogActionItem'><td colspan='2' align='center' style='height: 25px;'>";
		var footItemTableView = "<table style='width:98%;height:22px;table-layout:fixed; word-break: break-all;'><tr>";
		if (itemSetInit) {
			if (itemSetInit.refreshItem) {
				footItemTableView += "<td width='60px' align='left'><button id='dailogrefreshItem'  title='刷新' style='background: url("
						+ $dpimgpath
						+ "csbar/d_5.gif); border:1px solid #A4DBF1; padding-top: 2px; width:60px; height:22px;' onmouseover=\"this.style.background='url("
						+ $dpimgpath
						+ "csbar/d_5_h.gif)';this.style.borderColor='#baceeb'\" onMouseOut=\"this.style.background='url("
						+ $dpimgpath
						+ "csbar/d_5.gif)';this.style.borderColor='#A4DBF1'\">刷&nbsp;新</button></td>";
			} else {
				footItemTableView += "<td width='60px' align='left'></td>";
			}
			footItemTableView += "<td/>";
			if (itemSetInit.enginItem) {
				footItemTableView += "<td align='right'><button id='dailogenginItem' type='button' title='确定' style='background: url("
						+ $dpimgpath
						+ "csbar/d_5.gif); border:1px solid #A4DBF1; padding-top: 2px; width: 60px; height:22px;' onmouseover=\"this.style.background='url("
						+ $dpimgpath
						+ "csbar/d_5_h.gif)';this.style.borderColor='#baceeb'\" onMouseOut=\"this.style.background='url("
						+ $dpimgpath
						+ "csbar/d_5.gif)';this.style.borderColor='#A4DBF1'\">确&nbsp;定</button></td>";
			} else {
				footItemTableView += "<td align='right'></td>";
			}
			if (itemSetInit.CanclItem) {
				footItemTableView += "<td width='80px' align='right'><button id='dailogCancelItem'  type='button' title='取消' style='background: url("
						+ $dpimgpath
						+ "csbar/d_5.gif); border:1px solid #A4DBF1; padding-top: 2px; width:60px; height:22px;' onmouseover=\"this.style.background='url("
						+ $dpimgpath
						+ "csbar/d_5_h.gif)';this.style.borderColor='#baceeb'\" onMouseOut=\"this.style.background='url("
						+ $dpimgpath
						+ "csbar/d_5.gif)';this.style.borderColor='#A4DBF1'\">取&nbsp;消</button></td>";
			} else {
				footItemTableView += "<td width='60px' align='right'></td>";
			}
		} else {
			footItemTableView += "<td width='60px' align='left'><button id='dailogrefreshItem'  title='刷新' style='background: url("
					+ $dpimgpath
					+ "csbar/d_5.gif); border:1px solid #A4DBF1; padding-top: 2px; width:60px; height:22px;' onmouseover=\"this.style.background='url("
					+ $dpimgpath
					+ "csbar/d_5_h.gif)';this.style.borderColor='#baceeb'\" onMouseOut=\"this.style.background='url("
					+ $dpimgpath
					+ "csbar/d_5.gif)';this.style.borderColor='#A4DBF1'\">刷&nbsp;新</button></td>";
			footItemTableView += "<td/>";
			footItemTableView += "<td align='right'><button id='dailogenginItem' type='button' title='确定' style='background: url("
					+ $dpimgpath
					+ "csbar/d_5.gif); border:1px solid #A4DBF1; padding-top: 2px; width:60px; height:22px;' onmouseover=\"this.style.background='url("
					+ $dpimgpath
					+ "csbar/d_5_h.gif)';this.style.borderColor='#baceeb'\" onMouseOut=\"this.style.background='url("
					+ $dpimgpath
					+ "csbar/d_5.gif)';this.style.borderColor='#A4DBF1'\">确&nbsp;定</button></td>";
			footItemTableView += "<td width='80px' align='right'><button id='dailogCancelItem'  type='button' title='取消' style='background: url("
					+ $dpimgpath
					+ "csbar/d_5.gif); border:1px solid #A4DBF1; padding-top: 2px; width:60px; height:22px;' onmouseover=\"this.style.background='url("
					+ $dpimgpath
					+ "csbar/d_5_h.gif)';this.style.borderColor='#baceeb'\" onMouseOut=\"this.style.background='url("
					+ $dpimgpath
					+ "csbar/d_5.gif)';this.style.borderColor='#A4DBF1'\">取&nbsp;消</button></td>";
		}
		footItemTableView += "<tr></table>";
		dialogMain += footItemTableView;
		dialogMain += "</td></tr><tr><td colspan='2' style='width:100%;height:2px;align:right;'><div id='dialog_footer_cod' style='width:5px;height:5px;position: absolute; right: 1px;bottom: 1px;'></div></td></tr></table>";
		document.body.appendChild(allviewcap);
		document.body.appendChild(msgObj);
		msgObj.innerHTML = dialogMain;
		createWebWindow(msgObj, msgw, msgh);
		var txt = document.getElementById("msgTxt");
		txt.style.borderBottom = "1px solid #A4DBF1";
		txt.innerHTML = "<iframe id='windowdialogIframe' name='windowdialogIframe' frameborder='0' src='' style='width:100%;height:100%;'></iframe>";
		try {
			document.getElementById("windowdialogIframe").src = url;
			try {
				var func_iframe = document.getElementById("windowdialogIframe");
				if (func_iframe.attachEvent) {
					func_iframe.attachEvent("onload", function() {
						try {
							var dialogWin = func_iframe.contentWindow;
							if (dialogWin.getUrlParam) {
								dialogWin.getUrlParam(urlParam);
							}
						} catch (e) {
						}
					});
				} else {
					func_iframe.onload = function() {
						try {
							var dialogWin = func_iframe.contentWindow;
							if (dialogWin.getUrlParam) {
								dialogWin.getUrlParam(urlParam);
							}
						} catch (e) {
						}
					};
				}
			} catch (e) {
			}
		} catch (e) {
		}
		var dsiTabKey = function(e) {
			e = e || event;
			if (e.keyCode == 9)
				return false;
		};
		addEvent(window.document, "keydown", dsiTabKey, true);
		addEvent(window.document, "keyup", dsiTabKey, true);
		if ((itemSetInit == false)) {
			document.getElementById("dialogActionItem").style.display = "none";
		}
		try {
			if ((itemSetInit.refreshItem == false
					&& itemSetInit.enginItem == false && itemSetInit.CanclItem == false))
				document.getElementById("dialogActionItem").style.display = "none";
		} catch (e) {
		}
		try {
			document.frames("windowdialogIframe").contentWindow.location
					.reload();
		} catch (e) {
		}
		try {
			document.getElementById("dailogrefreshItem").onclick = function() {
				try {
					document.getElementById("windowdialogIframe").contentWindow.location
							.reload();
				} catch (e) {
					document.getElementById("windowdialogIframe").src = "";
					document.getElementById("windowdialogIframe").src = url;
				}
			};
		} catch (e) {
		}
		try {
			document.getElementById("dailogenginItem").onclick = function() {
				try {
					var dailogEngin = document
							.getElementById("windowdialogIframe").contentWindow.dailogEngin;
				} catch (e) {
				}
				if (dailogEngin) {
					var reData = dailogEngin();
					if (reData == false)
						return;
					justep.yn.portal.dailog.dailogEngin(reData, true);
				} else {
					justep.yn.portal.dailog.dailogEngin(null, true);
				}
			};
		} catch (e) {
		}
		try {
			document.getElementById("dailogCancelItem").onclick = function() {
				document.getElementById("msgTxt").removeChild(
						document.getElementById("windowdialogIframe"));
				document.body.removeChild(allviewcap);
				document.body.removeChild(msgObj);
			};
		} catch (e) {
		}
		try {
			document.getElementById("dialogTilteMax").onclick = function() {
				msgObj.style.left = "10px";
				msgObj.style.top = "10px";
				msgObj.style.width = (parseInt(document.body.clientWidth) - 20)
						+ "px";
				msgObj.style.height = (parseInt(document.body.clientHeight) - 20)
						+ "px";
				document.getElementById("dailog_table_trIf").style.display = "";
				if (itemSetInit != false)
					document.getElementById("dialogActionItem").style.display = "";
				document.getElementById("dialogTilteNor").style.display = "";
				document.getElementById("dialogTilteMax").style.display = "none";
			};
		} catch (e) {
		}
		document.getElementById("dialogTilteNor").onclick = function() {
			msgObj.style.left = (document.body.offsetWidth - msgw) / 2;
			msgObj.style.top = (document.body.offsetHeight - msgh) / 2;
			msgObj.style.width = msgw + "px";
			msgObj.style.height = msgh + "px";
			document.getElementById("dailog_table_trIf").style.display = "";
			if (itemSetInit != false)
				document.getElementById("dialogActionItem").style.display = "";
			document.getElementById("dialogTilteMax").style.display = "";
			document.getElementById("dialogTilteNor").style.display = "none";
		};
		document.getElementById("dialogTilteMin").onclick = function() {
			msgObj.style.width = msgw + "px";
			msgObj.style.height = "20px";
			document.getElementById("dailog_table_trIf").style.display = "none";
			if (itemSetInit != false)
				document.getElementById("dialogActionItem").style.display = "none";
			document.getElementById("dialogTilteNor").style.display = "";
			document.getElementById("dialogTilteMax").style.display = "none";
		};
		document.getElementById("dialogTilteCancel").onclick = function() {
			document.getElementById("msgTxt").removeChild(
					document.getElementById("windowdialogIframe"));
			document.body.removeChild(allviewcap);
			document.body.removeChild(msgObj);
		};
	},
	callback : null,
	sendData : null,
	returnData : null,
	dailogEngin : function(data, b_g) {
		this.returnData = data ? data : this.returnData;
		if ($('<div></div>').dialog
				|| (topparent != parent && parent.$('<div></div>').dialog)) {
			try {
				if (b_g) {
					var callbackFn = justep.yn.portal.dailog.callback;
				} else {
					var callbackFn = parent.justep.yn.portal.dailog.callback;
				}
				if (callbackFn) {
					callbackFn(this.returnData);
				}
			} catch (e) {
			}
			try {
				parent.$('#gldd').dialog('close');
			} catch (e) {
				$('#gldd').dialog('close');
			}
			return true;
		}
		if (b_g) {
			var callbackFn = justep.yn.portal.dailog.callback;
			if (callbackFn) {
				callbackFn(this.returnData);
			}
			var dialogview = document.getElementById("dailogmsgDiv");
			if (dialogview) {
				document.getElementById("windowdialogIframe").src = "about:blanck";
				document.getElementById("windowdialogIframe").setAttribute(
						"src", "about:blanck");
				document.getElementById("msgTxt").removeChild(
						document.getElementById("windowdialogIframe"));
				document.body.removeChild(document
						.getElementById("dailogmsgDiv"));
				document.body.removeChild(document.getElementById("msgObjDiv"));
				try {
					document.getElementById("windowdialogIframe").parentNode
							.removeChild(document
									.getElementById("windowdialogIframe"));
				} catch (e) {
				}
			}
		} else {
			try {
				var callbackFn = parent.justep.yn.portal.dailog.callback;
			} catch (e) {
			}
			if (callbackFn) {
				callbackFn(this.returnData);
			}
			try {
				parent.document.body.removeChild(parent.document
						.getElementById("dailogmsgDiv"));
				parent.document.getElementById("windowdialogIframe").src = "about:blanck";
				parent.document.body.removeChild(parent.document
						.getElementById("msgObjDiv"));
				if (parent.document.getElementById("windowdialogIframe"))
					parent.document.getElementById("msgTxt").removeChild(
							parent.document
									.getElementById("windowdialogIframe"));
			} catch (e) {
			}
			try {
				parent.document.getElementById("windowdialogIframe").parentNode
						.removeChild(parent.document
								.getElementById("windowdialogIframe"));
			} catch (e) {
			}
		}
		var dsiTabKey = function(e) {
			e = e || event;
			if (e.keyCode == 9)
				return false;
		};
		try {
			removeEvent(window.document, "keydown", dsiTabKey, false);
			removeEvent(window.document, "keyup", dsiTabKey, false);
		} catch (e) {
		}
	},
	dailogReload : function() {
		try {
			var dialogview = parent.document.getElementById("dailogmsgDiv");
			if (dialogview)
				parent.document.getElementById("windowdialogIframe").contentWindow.location
						.reload();
		} catch (e) {
		}
		var dialogview = document.getElementById("dailogmsgDiv");
		if (dialogview)
			document.getElementById("windowdialogIframe").contentWindow.location
					.reload();
	},
	dailogCancel : function() {
		if ($('<div></div>').dialog || parent.$('<div></div>').dialog) {
			try {
				parent.$('#gldd').dialog('close');
			} catch (e) {
				$('#gldd').dialog('close');
			}
			return true;
		}
		try {
			parent.document.body.removeChild(parent.document
					.getElementById("dailogmsgDiv"));
			parent.document.getElementById("windowdialogIframe").src = "about:blanck";
			parent.document.body.removeChild(parent.document
					.getElementById("msgObjDiv"));
			if (parent.document.getElementById("windowdialogIframe"))
				parent.document.getElementById("msgTxt").removeChild(
						parent.document.getElementById("windowdialogIframe"));
		} catch (e) {
		}
		try {
			var dialogview = document.getElementById("dailogmsgDiv");
			if (dialogview) {
				document.getElementById("windowdialogIframe").src = "about:blanck";
				document.getElementById("msgTxt").removeChild(
						document.getElementById("windowdialogIframe"));
				document.body.removeChild(dialogview);
				document.body.removeChild(document.getElementById("msgObjDiv"));
			}
		} catch (e) {
		}
		var dsiTabKey = function(e) {
			e = e || event;
			if (e.keyCode == 9)
				return false;
		};
		try {
			removeEvent(window.document, "keydown", dsiTabKey, false);
			removeEvent(window.document, "keyup", dsiTabKey, false);
		} catch (e) {
		}
	}
};
function setTab(n) {
	var tli = document.getElementById("menu").getElementsByTagName("li");
	var mli = document.getElementById("tab_view").getElementsByTagName("ul");
	for (var i = 0; i < tli.length; i++) {
		tli[i].className = i == n ? "hover" : "";
		mli[i].style.display = i == n ? "block" : "none";
	}
}

justep.yn.Context = {
	userInfo : {},
	getPath : function() {
		this.checklogin();
		return this.userInfo.uiserverremoteurl;
	},
	getCurrentPersonID : function() {
		this.checklogin();
		return this.userInfo.personid;
	},
	getCurrentPersonCode : function() {
		this.checklogin();
		return this.userInfo.personcode;
	},
	getCurrentPersonName : function() {
		this.checklogin();
		return this.userInfo.personName;
	},
	getCurrentPersonFID : function() {
		this.checklogin();
		return this.userInfo.personfid;
	},
	getCurrentPersonFCode : function() {
		this.checklogin();
		return this.userInfo.personfcode;
	},
	getCurrentPersonFName : function() {
		this.checklogin();
		return this.userInfo.personfname;
	},
	getCurrentPostID : function() {
		this.checklogin();
		return this.userInfo.positionid;
	},
	getCurrentPostCode : function() {
		this.checklogin();
		return this.userInfo.positioncode;
	},
	getCurrentPostName : function() {
		this.checklogin();
		return this.userInfo.positionname;
	},
	getCurrentPostFID : function() {
		this.checklogin();
		return this.userInfo.positionfid;
	},
	getCurrentPostFCode : function() {
		this.checklogin();
		return this.userInfo.positionfcode;
	},
	getCurrentPostFName : function() {
		this.checklogin();
		return this.userInfo.positionfname;
	},
	getCurrentDeptID : function() {
		this.checklogin();
		return this.userInfo.deptid;
	},
	getCurrentDeptCode : function() {
		this.checklogin();
		return this.userInfo.deptcode;
	},
	getCurrentDeptName : function() {
		this.checklogin();
		return this.userInfo.deptname;
	},
	getCurrentDeptFID : function() {
		this.checklogin();
		return this.userInfo.deptfid;
	},
	getCurrentDeptFCode : function() {
		this.checklogin();
		return this.userInfo.deptfcode;
	},
	getCurrentDeptFName : function() {
		this.checklogin();
		return this.userInfo.deptfname;
	},
	getCurrentOgnID : function() {
		this.checklogin();
		return this.userInfo.ognid;
	},
	getCurrentOgnCode : function() {
		this.checklogin();
		return this.userInfo.ogncode;
	},
	getCurrentOgnName : function() {
		this.checklogin();
		return this.userInfo.ognname;
	},
	getCurrentOgnFID : function() {
		this.checklogin();
		return this.userInfo.ognfid;
	},
	getCurrentOgnFCode : function() {
		this.checklogin();
		return this.userInfo.ognfcode;
	},
	getCurrentOgnFName : function() {
		this.checklogin();
		return this.userInfo.ognfname;
	},
	getCurrentOrgID : function() {
		this.checklogin();
		return this.userInfo.orgid;
	},
	getCurrentOrgCode : function() {
		this.checklogin();
		return this.userInfo.orgcode;
	},
	getCurrentOrgName : function() {
		this.checklogin();
		return this.userInfo.orgname;
	},
	getCurrentOrgFID : function() {
		this.checklogin();
		return this.userInfo.orgfid;
	},
	getCurrentOrgFCode : function() {
		this.checklogin();
		return this.userInfo.orgfcode;
	},
	getCurrentOrgFName : function() {
		this.checklogin();
		return this.userInfo.orgfname;
	},
	init : function() {
		var result = justep.yn
				.XMLHttpRequest("controller/system/User/initPortalInfo");
		if (!result)
			return;
		if (typeof result == "string") {
			result = window.eval("(" + result + ")");
		}
		this.userInfo = window["eval"]("(" + result[0].data + ")");
	},
	checklogin : function() {
		if (!this.userInfo.personid || this.userInfo.personid == ""
				|| this.userInfo.personid == "null") {
			this.init();
		}
	}
};

function trim(text) {
	if (text == undefined) {
		return "";
	} else {
		return text.replace(/(^\s*)|(\s*$)/g, "");
	}
}
String.prototype.trim = function() {
	return this.replace(/(^\s+)|(\s+$)/g, "");
};
String.prototype.ltrim = function() {
	return this.replace(/^\s+/, "");
};
String.prototype.rtrim = function() {
	return this.replace(/\s+$/, "");
};
var reMoveStr = function(str1, str2) {
	if (str1.indexOf(str2) > -1)
		str1 = str1.replace(str2, "");
	return str1;
};
var replaceFirst = function(str, p, m) {
	if (str.indexOf(p) == 0)
		str = str.replace(p, m);
	return str;
};
String.prototype.replaceFirst = function(p, m) {
	if (this.indexOf(p) == 0) {
		return this.replace(p, m);
	}
	return this;
};
String.prototype.replaceAll = function(p, m) {
	return this.replace(new RegExp(p, "gm"), m);
};

function closeself() {
	window.opener = null;
	window.open("", "_self");
	window.close();
}
Date.prototype.format = function(fmt) {
	var o = {
		"M+" : this.getMonth() + 1,
		"d+" : this.getDate(),
		"h+" : this.getHours() % 24 == 0 ? 24 : this.getHours() % 24,
		"H+" : this.getHours(),
		"m+" : this.getMinutes(),
		"s+" : this.getSeconds(),
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		"S" : this.getMilliseconds()
	};
	var week = {
		"0" : "\u65e5",
		"1" : "\u4e00",
		"2" : "\u4e8c",
		"3" : "\u4e09",
		"4" : "\u56db",
		"5" : "\u4e94",
		"6" : "\u516d"
	};
	if (/(y+)/.test(fmt)) {
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}
	if (/(E+)/.test(fmt)) {
		fmt = fmt
				.replace(
						RegExp.$1,
						((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "\u661f\u671f"
								: "\u5468")
								: "")
								+ week[this.getDay() + ""]);
	}
	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(fmt)) {
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
		}
	}
	return fmt;
};
var daxieshuzhu = new Array("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖");
var danwei = new Array("仟", "佰", "拾", "");
var dadanwei = new Array(" ", "万", "亿", "万亿");
var input = new Array();
var zhuanhuan = function() {
	this.input = new Array();
	this.jsq = 0;
	this.output = new Array();
	this.daxiezhuanhuan = function() {
		this.strtemp = "";
		for (var jwq = 0; jwq < this.input.length; jwq++) {
			this.strtemp += daxieshuzhu[this.input[jwq]] + danwei[jwq];
		}
		this.strtemp += " ";
		this.re = /(零\D)+/g;
		this.strtemp = this.strtemp.replace(this.re, "零");
		this.re = /零$/g;
		this.strtemp = this.strtemp.replace(this.re, "");
		if (this.strtemp != "") {
			this.output[this.jsq] = this.strtemp.replace(" ", "")
					+ dadanwei[this.jsq];
		} else {
			this.output[this.jsq] = this.strtemp.replace(" ", "");
		}
		this.jsq++;
	};
	this.taifen = function(input, pb) {
		this.pb = pb;
		this.input = input;
		var Ainput = new Array();
		var Atemp = new Array(0, 0, 0, 0);
		for (var temp = this.input.length - 1, temp2 = 3, jwq = 0; temp >= 0; temp--, temp2--) {
			if (temp2 < 0) {
				temp2 = 3;
			}
			Atemp[temp2] = input.substr(temp, 1);
			if ((temp2 % 4) == 0) {
				Ainput[jwq] = Atemp;
				jwq++;
				Atemp = new Array(0, 0, 0, 0);
			} else if (temp == 0) {
				Ainput[jwq] = Atemp;
			}
		}
		if (this.pb == 1) {
			var Ainput2 = new Array();
			for (var temp = 1; temp < Ainput[0].length; temp++) {
				Ainput2[temp - 1] = Ainput[0][temp];
			}
			Ainput2[3] = 0;
			Ainput = new Array(Ainput2);
			return this.zhuanhuandaxie(Ainput, this.pb);
		} else {
			return this.zhuanhuandaxie(Ainput, this.pb);
		}
	};
	this.zhuanhuandaxie = function(Ainput, pb) {
		this.pb = pb;
		this.Ainput = Ainput;
		for (var temp = 0; temp < this.Ainput.length; temp++) {
			this.input = Ainput[temp];
			this.daxiezhuanhuan();
		}
		return this.chongzhu(this.output, this.pb);
	};
	this.chongzhu = function(output, pb) {
		this.pb = pb;
		this.output = output;
		this.Stroutput = "";
		for (var temp = this.output.length - 1; temp >= 0; temp--) {
			this.Stroutput += this.output[temp];
		}
		if (this.pb == 1) {
			return this.Stroutput.replace("仟", "角").replace("佰", "分").replace(
					"拾", "厘");
		} else {
			this.re = /^零/g;
			this.Stroutput = this.Stroutput.replace(this.re, "");
			return this.Stroutput;
		}
	};
};
justep.yn.numberL2U = function(data) {
	var input = (data) ? data.toString() : "0";
	var xiaoshu = 0;
	var re = /^0+/g;
	var xiaoshu = 0;
	var output = "", output2 = "";
	var chafen = new Array();
	chafen = input.split(".");
	if (chafen.length == 2) {
		xiaoshu = chafen[1];
	}
	input = chafen[0];
	var myzhuanhuan = new zhuanhuan();
	output = myzhuanhuan.taifen(input, 0);
	if (xiaoshu != 0) {
		for (var temp = 3 - xiaoshu.length; temp > 0; temp--) {
			xiaoshu += "0";
		}
		var myzhuanhuan = new zhuanhuan();
		output2 = myzhuanhuan.taifen(xiaoshu, 1);
	}
	if (output != "" && output2 != 0) {
		return (output + "元" + output2).replace(" ", "");
	} else {
		if (output != "" && output2 == 0) {
			return (output + "元整").replace(" ", "");
		} else {
			var re = /^零/g;
			return "";
		}
	}
};
justep.yn.numberFormat = function(number, format) {
	var result = "";
	try {
		if (number && !isNaN(parseInt(number)) && number.indexOf("E") > 0) {
			var tempNumD = number.split("E")[0];
			var tempNumEx = number.split("E")[1];
			var newNum = tempNumD * Math.pow(10, tempNumEx);
			number = newNum;
		}
	} catch (e) {
	}
	if (number && number != "") {
		number = number.toString().replaceAll(",", "");
	}
	var fix = (format.split(".").length > 1) ? format.split(".")[1].length : 0;
	number = parseFloat(number);
	var isfushu = false;
	if (number < 0) {
		isfushu = true;
		number = -number;
	}
	var val = number.toFixed(fix);
	if (format.split(".")[0].indexOf(",") > 0) {
		var sfa = format.split(".")[0].split(",");
		var scl = sfa[sfa.length - 1].length;
		var numList = new Array();
		var inNum = val.split(".")[0];
		var n = 0;
		var m = inNum.length;
		for (var i = inNum.length; i > 0; i--) {
			if (i - scl > 0 && n % scl == 0) {
				numList.push(inNum.substring(i - scl, i));
				m = m - scl;
			} else if (i == 1) {
				numList.push(inNum.substring(0, m));
			}
			n++;
		}
		for (k in numList) {
			if (result != "") {
				result = numList[k] + "," + result;
			} else
				result = numList[k];
		}
		var zer = "";
		for (var i = 0; i < fix; i++) {
			zer += "0";
		}
		if (val.split(".").length > 1)
			result = result + "." + val.split(".")[1];
		else {
			if (zer != "")
				result = result + "." + zer;
		}
	} else {
		result = val;
	}
	if (isfushu) {
		result = "-" + result;
	}
	return result;
};
justep.yn.CheckMail = function(mail) {
	var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if (filter.test(mail))
		return true;
	else {
		alert('您的电子邮件格式不正确!');
		return false;
	}
};
justep.yn.checkdate = function(date) {
	var filter = /^(\d{4})+\/|-([1-9]|0+[1-9]|1+[0-2])+\/|-([1-9]|0+[1-9]|[1-2]+[0-9]|3+[0-1])+$/;
	if (filter.test(date))
		return true;
	else {
		alert('您的日期格式不正确!');
		return false;
	}
};
justep.yn.CheckNumber = {
	valNum : function(event) {
		event = event || window.event;
		var e = event.keyCode;
		var objEdit = event.srcElement ? event.srcElement : event.target;
		if (e != 48 && e != 49 && e != 50 && e != 51 && e != 52 && e != 53
				&& e != 54 && e != 55 && e != 56 && e != 57 && e != 96
				&& e != 97 && e != 98 && e != 99 && e != 100 && e != 101
				&& e != 102 && e != 103 && e != 104 && e != 105 && e != 109
				&& e != 37 && e != 39 && e != 13 && e != 8 && e != 46
				&& e != 190 && e != 110 && e != 189 && e != 229) {
			if (event.ctrlKey == false) {
				if (event.preventDefault) {
					event.preventDefault();
				} else {
					event.returnValue = false;
				}
				return false;
			} else {
				justep.yn.CheckNumber.valClip(event);
			}
		} else if (e == 109 || e == 189 || e == 190 || e == 110) {
			if ((e == 189 || e == 109)
					&& objEdit.getAttribute("value").indexOf("-") > -1) {
				if (event.preventDefault) {
					event.preventDefault();
				} else {
					event.returnValue = false;
				}
				return false;
			}
			if ((e == 190 || e == 110)
					&& objEdit.getAttribute("value").indexOf(".") > -1) {
				if (event.preventDefault) {
					event.preventDefault();
				} else {
					event.returnValue = false;
				}
				return false;
			}
		} else if (e == 229) {
			addEvent(objEdit, 'keyup', function(event) {
				justep.yn.CheckNumber.checkValNum(objEdit);
			}, true);
		}
	},
	checkValNum : function(obj) {
		if (obj.getAttribute("value") == "") {
		} else if (Number(obj.getAttribute("value")) == "NaN") {
			var str = (obj.getAttribute("value") || "").toString();
			obj.setAttribute("value", str.substring(0, str.length - 1));
		} else if (obj.getAttribute("value") == "NaN") {
			obj.setAttribute("value", "");
		} else {
			obj.setAttribute("value", Number(obj.getAttribute("value")));
		}
	},
	valClip : function(ev) {
		ev = ev || window.event;
		var content = window.clipboardData.getData('Text');
		if (content != null) {
			try {
				var test = parseFloat(content);
				var str = "" + test;
				if (isNaN(test) == true) {
					window.clipboardData.setData("Text", "");
				} else {
					if (str != content)
						window.clipboardData.setData("Text", str);
				}
			} catch (e) {
				alert("粘贴出现错误!");
			}
		}
	}
};
justep.yn.String = {};
justep.yn.String.isin = function(abs, str) {
	var substr = abs.split(',');
	for (var i = 0; i < substr.length; i++) {
		if (str == substr[i])
			return true;
	}
	if (abs.indexOf(str) > -1)
		return true;
	return false;
};
justep.yn.String.isHave = function(abs, str) {
	var substr = abs.split(',');
	for (var i = 0; i < substr.length; i++) {
		if (str == substr[i])
			return true;
	}
	return false;
};
justep.yn.getIdCardInfo = function(id) {
	var arr = [ null, null, null, null, null, null, null, null, null, null,
			null, "北京", "天津", "河北", "山西", "内蒙古", null, null, null, null, null,
			"辽宁", "吉林", "黑龙江", null, null, null, null, null, null, null, "上海",
			"江苏", "浙江", "安微", "福建", "江西", "山东", null, null, null, "河南", "湖北",
			"湖南", "广东", "广西", "海南", null, null, null, "重庆", "四川", "贵州", "云南",
			"西藏", null, null, null, null, null, null, "陕西", "甘肃", "青海", "宁夏",
			"新疆", null, null, null, null, null, "台湾", null, null, null, null,
			null, null, null, null, null, "香港", "澳门", null, null, null, null,
			null, null, null, null, "国外" ];
	var isid = justep.yn.checkId(id);
	if (isid != "true" && isid)
		return "错误的身份证号码";
	var id = String(id), prov = arr[id.slice(0, 2)], sex = id.slice(14, 17) % 2 ? "男"
			: "女";
	var birthday = (new Date(id.slice(6, 10), id.slice(10, 12) - 1, id.slice(
			12, 14))).format('yyyy-MM-dd');
	return [ prov, birthday, sex ];
};
justep.yn.checkId = function(pId) {
	var arrVerifyCode = [ 1, 0, "x", 9, 8, 7, 6, 5, 4, 3, 2 ];
	var Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
	var Checker = [ 1, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1 ];
	var result = "";
	if (pId.length != 15 && pId.length != 18)
		result += "身份证号共有 15 码或18位";
	var Ai = pId.length == 18 ? pId.substring(0, 17) : pId.slice(0, 6) + "19"
			+ pId.slice(6, 16);
	if (!/^\d+$/.test(Ai))
		result += "身份证除最后一位外，必须为数字！";
	var yyyy = Ai.slice(6, 10), mm = Ai.slice(10, 12) - 1, dd = Ai
			.slice(12, 14);
	var d = new Date(yyyy, mm, dd), now = new Date();
	var year = d.getFullYear(), mon = d.getMonth(), day = d.getDate();
	if (year != yyyy || mon != mm || day != dd || d > now || year < 1940)
		result += "身份证输入错误！";
	for (var i = 0, ret = 0; i < 17; i++)
		ret += Ai.charAt(i) * Wi[i];
	Ai += arrVerifyCode[ret %= 11];
	if (!result || result == "")
		return "true";
	return pId == (Ai) ? "true" : result;
};
justep.yn.Telephone = function(date) {
	var filter = /(\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/;
	if (filter.test(date))
		return true;
	else {
		alert('您输入的电话号码不正确，请重新输入！');
		return false;
	}
};
justep.yn.getworkdays = function(starttime) {
	var date = justep.System.datetime();
	var createweek = starttime.getDay();
	var num = justep.Date.diff(starttime, date, 'd');
	var weekwork = 0;
	var holiday = Math.round(num / 7) * 2;
	if (num > 7) {
		weekwork = num - holiday;
	} else {
		var week = date.getDay();
		if (week == 6) {
			weekwork = num - 1;
		} else if (weekwork - createweek <= 0 && num != 0) {
			weekwork = num - 2;
		} else {
			weekwork = num;
		}
	}
	return weekwork;
};
if (!justep.yn.System)
	justep.yn.System = {};
justep.yn.System.Date = {
	sysDate : function() {
		var param = new justep.yn.RequestParam();
		var r = justep.yn.XMLHttpRequest("getSystemDate", param, "post", false,
				null, true);
		var rdate = justep.yn.System.Date.strToDate(r.sysdate);
		return rdate.format("yyyy-MM-dd");
	},
	sysDateTime : function() {
		var param = new justep.yn.RequestParam();
		var r = justep.yn.XMLHttpRequest("getSystemDateTime", param, "post",
				false, null, true);
		return r.sysdate;
	},
	strToDate : function(str) {
		if (!str || str == "" || str.indexOf("-") < 0) {
			return;
		}
		str = trim(str);
		var val = str.split(" ");
		var newDate;
		if (val.length > 1) {
			var sdate = val[0].split("-");
			var sTime = val[1].split(":");
			newDate = new Date(sdate[0], sdate[1] - 1, sdate[2], sTime[0],
					sTime[1], sTime[2]);
		} else {
			var sdate = val[0].split("-");
			newDate = new Date(sdate[0], sdate[1] - 1, sdate[2]);
		}
		return newDate;
	}
};
var $dpjspath = null;
var scripts = document.getElementsByTagName("script");
for (i = 0; i < scripts.length; i++) {
	if (scripts[i].src.substring(scripts[i].src.length - 13).toLowerCase() == 'comon.main.js') {
		$dpjspath = scripts[i].src.substring(0, scripts[i].src.length - 13);
		break;
	}
}
var $dpcsspath = $dpjspath ? $dpjspath.replace("/js/", "/css/") : null;
var $dpimgpath = $dpjspath ? $dpjspath.replace("/js/", "/image/") : null;
var createJSSheet = function(jsPath) {
	var head = document.getElementsByTagName('HEAD')[0];
	var script = document.createElement('script');
	script.src = jsPath;
	script.type = 'text/javascript';
	head.appendChild(script);
};
var createStyleSheet = function(cssPath) {
	var head = document.getElementsByTagName('HEAD')[0];
	var style = document.createElement('link');
	style.href = cssPath;
	style.rel = 'stylesheet';
	style.type = 'text/css';
	head.appendChild(style);
};
var checkPathisHave = function(path) {
	var Hhead = document.getElementsByTagName('HEAD')[0];
	var Hscript = Hhead.getElementsByTagName("SCRIPT");
	for (var i = 0; i < Hscript.length; i++) {
		if (Hscript[i].src == path)
			return true;
	}
	var Hstyle = Hhead.getElementsByTagName("LINK");
	for (var i = 0; i < Hstyle.length; i++) {
		if (Hstyle[i].href == path)
			return true;
	}
	return false;
};
justep.yn.GridSelect = function(div, dbkey, sql, master, caninput) {
	if (!div.id || div.id == "") {
		alert("div的id不能为空！");
		return;
	}
	var disabled = "";
	if (div.disabled)
		disabled = "disabled=true";
	var gridSelect = {
		onselected : div.onselected,
		onchecked : div.onchecked,
		onValueChanged : div.onValueChanged,
		value : "",
		select : function(obj) {
			if (gridSelect.onValueChanged) {
				if (gridSelect.value != this.getAttribute("value")) {
					var exfn = eval(gridSelect.onValueChanged);
					if (gridSelect.onValueChanged
							&& gridSelect.onValueChanged.indexOf("(") < 0)
						exfn(this);
				}
			}
			gridSelect.value = $(this).val();
			if (obj && !gridSelect.value)
				gridSelect.value = $(obj).val();
			div.setAttribute("value", $(this).val());
			if (div.getAttribute("onselected")) {
				var efn = eval(div.getAttribute("onselected"));
				if (typeof (efn) == "function")
					efn(this);
			}
		},
		change : function(obj) {
			if (div.getAttribute("onValueChanged")) {
				var efn = eval(div.getAttribute("onValueChanged"));
				if (typeof (efn) == "function")
					efn(obj);
			}
		},
		check : function() {
			if (div.getAttribute("onchecked")) {
				var efn = eval(div.getAttribute("onchecked"));
				if (typeof (efn) == "function")
					efn(this);
			}
			gridSelect.onselectting = false;
			this.reMoveGridselect();
		},
		currentRowId : "",
		currenttr : null,
		currentRowId : "",
		checkedValue : "",
		setSelect : function(obj, m) {
			var seleDiv = document.getElementById(div.id + "_gridSelect");
			if (seleDiv) {
				return;
			}
			var posX = obj.offsetLeft;
			var posY = obj.offsetTop;
			var aBox = obj;
			do {
				aBox = aBox.offsetParent;
				posX += aBox.offsetLeft;
				posY += aBox.offsetTop;
			} while (aBox.tagName != "BODY");
			var selectIfram = document.createElement("div");
			selectIfram.setAttribute("id", div.id + "_gridSelect");
			selectIfram.setAttribute("align", "center");
			selectIfram.style.background = "#ffffff";
			selectIfram.style.border = "1px solid #eee";
			selectIfram.style.position = "absolute";
			selectIfram.style.left = posX;
			selectIfram.style.top = (parseInt(posY) + 22) + "px";
			selectIfram.style.font = "12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
			selectIfram.style.width = obj.offsetWidth;
			selectIfram.style.height = 100 + "px";
			selectIfram.style.textAlign = "left";
			selectIfram.style.lineHeight = "22px";
			selectIfram.style.zIndex = "101";
			var isChecked = function(val) {
				var strV = obj.getAttribute("value");
				var strVs = strV.split(",");
				for ( var h in strVs) {
					if (strVs[h] == val)
						return true;
				}
				return false;
			};
			if (obj.getAttribute("value") && obj.getAttribute("value") != "")
				gridSelect.checkedValue = obj.getAttribute("value");
			var dataTable = "<div style='height:200px;word-break:break-all;overflow-Y:auto;overflow-X:hide;background:#fff;'><table id='"
					+ div.id
					+ "_table' style='width:100%;border-collapse: collapse;table-layout:fixed;word-break: break-all;line-height:22px;'>";
			if (!m) {
				if (datares.length > 2) {
					for (var i = 0; i < datares.length; i++) {
						var fV = (datares[i] == "null") ? "" : datares[i];
						if (fV && trim(fV) != "")
							dataTable += "<tr id='" + fV
									+ "'><td class='grid_td'>" + fV
									+ "</td></tr>";
					}
				} else {
					var datare = datares[0].split(" ");
					for (var i = 0; i < datare.length; i++) {
						var fV = (datare[i] == "null") ? "" : datare[i];
						if (fV && trim(fV) != "")
							dataTable += "<tr id='" + fV
									+ "'><td class='grid_td'>" + fV
									+ "</td></tr>";
					}
				}
			} else {
				if (datares.length > 2) {
					for (var i = 0; i < datares.length; i++) {
						var fV = (datares[i] == "null") ? "" : datares[i];
						if (fV && trim(fV) != "") {
							if (isChecked(fV)) {
								dataTable += "<tr id='"
										+ fV
										+ "'><td width='20px'><input type='checkbox' id='"
										+ fV
										+ "' checked='true' class='gridselectchackbox'></td><td class='grid_td'>"
										+ fV + "</td></tr>";
							} else {
								dataTable += "<tr id='"
										+ fV
										+ "'><td width='20px'><input type='checkbox' id='"
										+ fV
										+ "' class='gridselectchackbox'></td><td class='grid_td'>"
										+ fV + "</td></tr>";
							}
						}
					}
				} else {
					var datare = datares[0].split(" ");
					for (var i = 0; i < datare.length; i++) {
						var fV = (datare[i] == "null") ? "" : datare[i];
						if (fV && trim(fV) != "") {
							if (isChecked(fV)) {
								dataTable += "<tr id='"
										+ fV
										+ "'><td width='20px'><input type='checkbox' id='"
										+ fV
										+ "' checked='true' class='gridselectchackbox'></td><td class='grid_td'>"
										+ fV + "</td></tr>";
							} else {
								dataTable += "<tr id='"
										+ fV
										+ "'><td width='20px'><input type='checkbox' id='"
										+ fV
										+ "' class='gridselectchackbox'></td><td class='grid_td'>"
										+ fV + "</td></tr>";
							}
						}
					}
				}
			}
			dataTable += "</table></div>";
			// if (m)
			// dataTable += "<table class='grid'><tr><td width='90%'></td><td
			// width='40px' align='left'><input type='button' value='确定'
			// title='确定' style='font-size:12px;'
			// onclick='document.getElementById(\""
			// + div.id + "\").gridSelect.check()'></td></tr><table>";
			selectIfram.innerHTML = dataTable;
			document.body.appendChild(selectIfram);
			var dataTable = document.getElementById(div.id + "_table");
			var sTR = dataTable.getElementsByTagName("TR");
			for (var i = 0; i < sTR.length; i++) {
				sTR[i].onmouseover = function() {
					if (gridSelect.currentRowId == this.id)
						return;
					this.tmpClass = this.className;
					this.className = "t4";
					if (gridSelect.currenttr) {
						gridSelect.currenttr.className = gridSelect.currenttr.tmpClass;
					}
					gridSelect.currenttr = this;
					gridSelect.currentRowId = this.id;
				};
				if (!m)
					sTR[i].onclick = function() {
						var dInput = div.getElementsByTagName("INPUT")[0];
						if (dInput) {
							dInput.setAttribute("value", this.id);
							$(dInput).val(this.id);
						}
						gridSelect.select(dInput);
						gridSelect.outEdit();
					};
				if (m) {
					var sTD = sTR[i].getElementsByTagName("TD")[0];
					var sChek = sTD.getElementsByTagName("INPUT")[0];
					sChek.onclick = function() {
						gridSelect.onselectting = true;
						if (this.checked) {
							if (!justep.yn.String.isin(gridSelect.checkedValue,
									this.id))
								gridSelect.checkedValue += "," + this.id;
						} else {
							if (justep.yn.String.isin(gridSelect.checkedValue,
									"," + this.id))
								gridSelect.checkedValue = reMoveStr(
										gridSelect.checkedValue, "," + this.id);
							else if (justep.yn.String.isin(
									gridSelect.checkedValue, this.id))
								gridSelect.checkedValue = reMoveStr(
										gridSelect.checkedValue, this.id);
						}
						var dInput = div.getElementsByTagName("INPUT")[0];
						if (dInput) {
							dInput.setAttribute("value", replaceFirst(
									gridSelect.checkedValue, ",", ""));
							gridSelect.checkedValue = replaceFirst(
									gridSelect.checkedValue, ",", "");
						}
					};
				}
			}
			var bfdocclick = document.onclick;
			document.onclick = function(event) {
				try {
					event = event || window.event;
					var objEdit = event.srcElement ? event.srcElement
							: event.target;
					if (J$(div.id + "_gridSelect") != objEdit
							&& objEdit.id != div.id
							&& $(objEdit).attr("class") != "gridselectchackbox") {
						if (m) {
							document.getElementById(div.id).gridSelect.check();
						} else {
							document.getElementById(div.id).gridSelect
									.reMoveGridselect();
						}
						document.onclick = bfdocclick;
					}
				} catch (e) {
				}
			};
		},
		reMoveGridselect : function() {
			if (gridSelect.onselectting)
				return;
			if (document.getElementById(div.id + "_gridSelect"))
				document.body.removeChild(document.getElementById(div.id
						+ "_gridSelect"));
		},
		onselectting : false,
		outEdit : function() {
			gridSelect.reMoveGridselect();
		}
	};
	this.gridSelect = gridSelect;
	div.gridSelect = gridSelect;
	div.master = master;
	if (!checkPathisHave($dpcsspath + "grid.main.css"))
		createStyleSheet($dpcsspath + "grid.main.css");
	var gsID = div.id ? div.id : "";
	var r = justep.yn.sqlQueryAction(dbkey, sql);
	var datares = r.getDatas();
	if (!master && !caninput) {
		var option = "<option value=''></option>";
		if (datares.length > 2) {
			for (var i = 0; i < datares.length; i++) {
				var fV = (datares[i] == "null") ? "" : datares[i];
				if (fV && trim(fV) != "")
					option += "<option value='" + fV + "'>" + fV + "</option>";
			}
		} else {
			var datare = datares[0].split(" ");
			for (var i = 0; i < datare.length; i++) {
				var fV = (datare[i] == "null") ? "" : datare[i];
				if (fV && trim(fV) != "")
					option += "<option value='" + fV + "'>" + fV + "</option>";
			}
		}
		var select = "<select id='"
				+ gsID
				+ "' "
				+ disabled
				+ " style='width:100%' onpropertychange='document.getElementById(\""
				+ div.id + "\").gridSelect.change(this);'>";
		select += option;
		select += "</select>";
		div.innerHTML = select;
		var op = div.getElementsByTagName("SELECT")[0];
		op.onchange = gridSelect.select;
	} else if (!master && caninput) {
		var selectinput = "<input id='"
				+ div.id
				+ "' type='text' class='Dselect' style='width:100%;cursor:pointer;' value='' "
				+ disabled + " onFocus='document.getElementById(\"" + div.id
				+ "\").gridSelect.setSelect(this,false)'/>";
		div.innerHTML = selectinput;
	} else {
		var selectinput = "<input id='"
				+ div.id
				+ "' type='text' class='Dselect' style='width:100%;cursor:pointer;' value='' "
				+ disabled
				+ " onFocus='document.getElementById(\""
				+ div.id
				+ "\").gridSelect.setSelect(this,true)' onpropertychange='document.getElementById(\""
				+ div.id + "\").gridSelect.change(this);'/>";
		div.innerHTML = selectinput;
	}
	return this;
};
justep.yn.TreeSelect = function(div, QueryAction, master) {
	if (!div) {
		return;
	}
	var treeSelect = {
		inputtext : null,
		rowid : "",
		value : "",
		parentid : "",
		checking : false,
		select : function(data) {
			treeSelect.checking = true;
			if (master) {
				return;
			}
			treeSelect.inputtext.setAttribute("value", data.CurrentValue);
			treeSelect.value = data.CurrentValue;
			treeSelect.rowid = data.CurrentRowId;
			treeSelect.parentid = data.CurrentparentID;
		},
		check : function(data) {
			treeSelect.checking = true;
			if (!master)
				return;
			treeSelect.inputtext.setAttribute("value", data.checkedValue);
			treeSelect.value = data.checkedValue;
			treeSelect.rowid = data.checkedID;
		},
		setSelect : function(obj) {
			var seleDiv = document.getElementById(div.id + "_treeSelect");
			if (seleDiv) {
				return;
			}
			var posX = obj.offsetLeft;
			var posY = obj.offsetTop;
			var aBox = obj;
			do {
				aBox = aBox.offsetParent;
				posX += aBox.offsetLeft;
				posY += aBox.offsetTop;
			} while (aBox.tagName != "BODY");
			var selectIfram = document.createElement("div");
			selectIfram.setAttribute("id", div.id + "_treeSelect");
			selectIfram.setAttribute("align", "center");
			selectIfram.style.background = "#ffffff";
			selectIfram.style.border = "1px solid #eee";
			selectIfram.style.position = "absolute";
			selectIfram.style.left = posX;
			selectIfram.style.top = (parseInt(posY) + 22) + "px";
			selectIfram.style.font = "12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
			selectIfram.style.width = obj.offsetWidth;
			selectIfram.style.height = (getOs() == "MSIE") ? 100 + "px"
					: 100 + "px auto";
			selectIfram.style.textAlign = "left";
			selectIfram.style.lineHeight = "22px";
			selectIfram.style.zIndex = "101";
			selectIfram.innerHTML = "<div id='"
					+ div.id
					+ "_tree_data' onselect='document.getElementById(\""
					+ div.id
					+ "\").treeSelect.select'onchecked='document.getElementById(\""
					+ div.id
					+ "\").treeSelect.check' style='height:200px;word-break:break-all;overflow-Y:auto;background:#fff;'></div><table style='width:100%;height:22px;'><tr><td align='right'><input id='"
					+ div.id
					+ "_engButton' type='button' value='确定' title='确定' style='font-size:12px;line-height:12px;'></td><td width='20px'/></tr></table>";
			document.body.appendChild(selectIfram);
			var treeDataView = document.getElementById(div.id + "_tree_data");
			document.getElementById(div.id + "_engButton").onclick = reMoveTreeselect;
			var tree = new justep.yn.createTree(treeDataView, QueryAction,
					"normal", null, master);
		}
	};
	if (!checkPathisHave($dpjspath + "tree.main.js"))
		createJSSheet($dpjspath + "tree.main.js");
	if (!checkPathisHave($dpcsspath + "tree.css"))
		createStyleSheet($dpcsspath + "tree.css");
	var reMoveTreeselect = function() {
		if (master) {
			if (div.getAttribute("onchecked")) {
				var efn = eval(div.getAttribute("onchecked"));
				if (typeof (efn) == "function")
					efn(treeSelect);
			}
		} else {
			if (div.getAttribute("onselected")) {
				var efn = eval(div.getAttribute("onselected"));
				if (typeof (efn) == "function")
					efn(treeSelect);
			}
		}
		if (document.getElementById(div.id + "_treeSelect"))
			document.body.removeChild(document.getElementById(div.id
					+ "_treeSelect"));
	};
	this.reMoveTreeselect = reMoveTreeselect;
	var disabled = false;
	if (div.disabled)
		disabled = true;
	var reMoveTreeselectOnl = function() {
		if (treeSelect.checking)
			return;
		if (document.getElementById(div.id + "_treeSelect"))
			document.body.removeChild(document.getElementById(div.id
					+ "_treeSelect"));
	};
	var input = document.createElement("input");
	input.id = div.id;
	input.type = "text";
	input.className = "Dselect";
	input.style.width = "100%";
	input.style.height = "22px";
	input.disabled = disabled;
	input.onfocus = function() {
		treeSelect.checking = false;
		document.getElementById(div.id).treeSelect.setSelect(input);
	};
	input.onblur = function() {
		setTimeout(reMoveTreeselectOnl, 300);
	};
	treeSelect.inputtext = input;
	div.appendChild(input);
	div.treeSelect = treeSelect;
	div.master = master;
	return this;
};
justep.yn.inputCaption = function(input, dbkey, table, cell, where) {
	if (!input)
		return;
	var reMovetextCaption = function() {
		if (document.getElementById(input.id + "_textCaption"))
			document.body.removeChild(document.getElementById(input.id
					+ "_textCaption"));
	};
	if (!input.getAttribute("value") || input.getAttribute("value") == "") {
		reMovetextCaption();
		return;
	}
	var sql = "select " + cell + " from " + table + " where " + cell
			+ " like '%" + input.getAttribute("value") + "%'";
	if (where && where != "")
		sql += " and (" + where + ")";
	var selDataback = function(re) {
		r = re;
		var datares = r.getDatas();
		var currenttr = null;
		var posX = input.offsetLeft;
		var posY = input.offsetTop;
		var aBox = input;
		do {
			aBox = aBox.offsetParent;
			posX += aBox.offsetLeft;
			posY += aBox.offsetTop;
		} while (aBox.tagName != "BODY");
		if (!checkPathisHave($dpcsspath + "grid.main.css"))
			createStyleSheet($dpcsspath + "grid.main.css");
		var selectIfram = document.createElement("div");
		selectIfram.setAttribute("id", input.id + "_textCaption");
		selectIfram.setAttribute("align", "center");
		selectIfram.style.background = "#ffffff";
		selectIfram.style.border = "1px solid #eee";
		selectIfram.style.position = "absolute";
		selectIfram.style.left = posX;
		selectIfram.style.top = (parseInt(posY) + input.offsetHeight) + "px";
		selectIfram.style.font = "12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
		selectIfram.style.width = input.offsetWidth;
		selectIfram.style.height = 100 + "px";
		selectIfram.style.textAlign = "left";
		selectIfram.style.lineHeight = "22px";
		selectIfram.style.zIndex = "101";
		var dataTable = "<div style='height:200px;word-break:break-all;overflow-Y:auto;background:#fff;'><table id='"
				+ input.id
				+ "_table' style='width:100%;border-collapse: collapse;table-layout:fixed;word-break: break-all;line-height:22px;'>";
		if (datares.length > 2) {
			for (var i = 0; i < datares.length; i++) {
				var fV = (datares[i] == "null") ? "" : datares[i];
				if (fV && fV != "")
					dataTable += "<tr id='" + fV + "'><td class='grid_td'>"
							+ fV + "</td></tr>";
			}
		} else {
			var datare = datares[0].split(" ");
			for (var i = 0; i < datare.length; i++) {
				var fV = (datare[i] == "null") ? "" : datare[i];
				if (fV && fV != "")
					dataTable += "<tr id='" + fV + "'><td class='grid_td'>"
							+ fV + "</td></tr>";
			}
		}
		dataTable += "</table></div>";
		selectIfram.innerHTML = dataTable;
		document.body.appendChild(selectIfram);
		var dataTable = document.getElementById(input.id + "_table");
		var sTR = dataTable.getElementsByTagName("TR");
		for (var i = 0; i < sTR.length; i++) {
			sTR[i].onmouseover = function() {
				this.tmpClass = this.className;
				this.className = "t4";
				if (currenttr) {
					currenttr.className = currenttr.tmpClass;
				}
				currenttr = this;
			};
			sTR[i].onclick = function() {
				input.setAttribute("value", this.id);
				reMovetextCaption();
			};
		}
		input.onblur = function() {
			setTimeout(reMovetextCaption, 300);
		};
	};
	justep.yn.sqlQueryAction(dbkey, sql, selDataback);
	return this;
};
justep.yn.Radio = function(div, item, splithtmlarray) {
	if (div.Radio)
		return;
	if (!div || !item) {
		alert("参数无效：justep.yn.Radio");
		return;
	}
	var checkboxcompid = div.id;
	var divnid = checkboxcompid + "_compent";
	var radio = "<input id='"
			+ checkboxcompid
			+ "' type='text' style='display:none;'onpropertychange='document.getElementById(\""
			+ divnid + "\").Radio.initData(this)'/>";
	div.innerHTML = radio;
	var Radio = {
		radiocheck : function(obj) {
			var radiolab = div.getElementsByTagName("input")[0];
			radiolab.setAttribute("value", obj.getAttribute("value"));
			div.setAttribute("value", obj.getAttribute("value"));
			var radios = div.getElementsByTagName("INPUT");
			for (var i = 1; i < radios.length; i++) {
				if (radios[i] != obj)
					radios[i].checked = false;
			}
			var onselected = div.getAttribute("onselected");
			if (onselected && onselected != "") {
				var sFn = eval(onselected);
				if (typeof (sFn) == "function") {
					sFn(radiolab.getAttribute("value"));
				}
			}
		},
		initData : function(obj) {
			var radios = div.getElementsByTagName("INPUT");
			for (var i = 1; i < radios.length; i++) {
				if (radios[i].getAttribute("value") == obj
						.getAttribute("value"))
					radios[i].checked = true;
				else
					radios[i].checked = false;
			}
		},
		setValue : function(val) {
			var radiolab = div.getElementsByTagName("input")[0];
			radiolab.setAttribute("value", val);
			this.initData(radiolab);
		}
	};
	var set = item.keySet();
	var disabled = "";
	if (div.disabled)
		disabled = "disabled=true";
	for (k in set) {
		div.innerHTML += "<input type='radio' value='" + set[k]
				+ "' onclick='document.getElementById(\"" + divnid
				+ "\").Radio.radiocheck(this)' " + disabled
				+ "><a style='font-size:12px'>" + item.get(set[k]) + "</a>";
		if (splithtmlarray && splithtmlarray.length == set.length
				&& k < set.length - 1) {
			try {
				div.innerHTML += splithtmlarray[k];
			} catch (e) {
				mAlert("参数：splithtmlarray无效，应该为item等长度的数组.");
			}
		}
	}
	div.Radio = Radio;
	document.getElementById(checkboxcompid).Radio = Radio;
	div.id = checkboxcompid + "_compent";
	div.setAttribute("id", checkboxcompid + "_compent");
	return this;
};
justep.yn.CheckBox = function(div, item, splithtmlarray) {
	if (div.Check)
		return;
	if (!div || !item) {
		alert("参数无效：justep.yn.CheckBox");
		return;
	}
	var checkboxcompid = div.id;
	var divnid = checkboxcompid + "_compent";
	var check = "<input id='"
			+ checkboxcompid
			+ "' style='display:none;' onpropertychange='document.getElementById(\""
			+ divnid + "\").Check.initData(this)'/>";
	div.innerHTML = check;
	var Check = {
		boxcheck : function(obj) {
			var checklab = div.getElementsByTagName("input")[0];
			var Cvalue = "";
			var checks = div.getElementsByTagName("INPUT");
			for (var i = 1; i < checks.length; i++) {
				if (checks[i].checked)
					Cvalue += "," + checks[i].getAttribute("value");
			}
			Cvalue = Cvalue.replaceFirst(",", "");
			checklab.setAttribute("value", Cvalue);
			div.setAttribute("value", Cvalue);
			var onchecked = div.getAttribute("onchecked");
			if (onchecked && onchecked != "") {
				var chFn = eval(onchecked);
				if (typeof chFn == "function") {
					chFn(Cvalue);
				}
			}
			div.setAttribute("value", Cvalue);
		},
		initData : function(obj) {
			var checks = div.getElementsByTagName("INPUT");
			var value = obj.getAttribute("value");
			for (var i = 1; i < checks.length; i++) {
				if (justep.yn.String.isHave(value, checks[i]
						.getAttribute("value")))
					checks[i].checked = true;
				else
					checks[i].checked = false;
			}
		},
		checkAll : function() {
			var checks = div.getElementsByTagName("INPUT");
			for (var i = 1; i < checks.length; i++) {
				checks[i].checked = true;
				Check.boxcheck(checks[i]);
			}
		},
		uncheckAll : function() {
			var checks = div.getElementsByTagName("INPUT");
			for (var i = 1; i < checks.length; i++) {
				checks[i].checked = false;
				Check.boxcheck(checks[i]);
			}
		}
	};
	var set = item.keySet();
	var disabled = "";
	if (div.disabled)
		disabled = "disabled=true";
	for (k in set) {
		div.innerHTML += "<input type='checkbox' name='" + item.get(set[k])
				+ "' value='" + set[k]
				+ "' onclick='document.getElementById(\"" + divnid
				+ "\").Check.boxcheck(this)' " + disabled
				+ "/><a style='font-size:12px'>" + item.get(set[k]) + "</a>";
		if (splithtmlarray && splithtmlarray.length == set.length
				&& k < set.length - 1) {
			try {
				div.innerHTML += splithtmlarray[k];
			} catch (e) {
				mAlert("参数：splithtmlarray无效，应该为item等长度的数组.");
			}
		}
	}
	div.disabled = false;
	document.getElementById(checkboxcompid).Check = Check;
	div.Check = Check;
	div.id = divnid;
	div.setAttribute("id", divnid);
	return this;
};
var easyTitle = function(dModule) {
	this.parent = dModule;
	this.init();
};
easyTitle.prototype = {
	getElementsByTitle : function(dModule) {
		if (!dModule)
			return null;
		var dMC = dModule.childNodes, aDC = [], at = null;
		for (var i = 0, l = dMC.length; i < l; i++) {
			at = (dMC[i].getAttribute) ? dMC[i].getAttribute("title") : null;
			if (!!at)
				aDC.push(dMC[i]);
		}
		return aDC.slice(0);
	},
	addEventListen : function(d, e, f, c) {
		if (d && f) {
			if (document.attachEvent) {
				for (var i = 0, l = d.length; i < l; i++)
					d[i].attachEvent('on' + e, f);
			} else {
				for (var i = 0, l = d.length; i < l; i++)
					d[i].addEventListener(e, f, c);
			}
		}
	},
	showTitle : function(e) {
		var e = e || window.event, dObj = e.srcElement || e.target;
		var x = e.clientX, y = e.clientY;
		var sTitle = dObj.getAttribute('title'), sCss = dObj
				.getAttribute('titlestyle');
		var dTitle = document.getElementById('easyTitleShow');
		if (sTitle) {
			dTitle.innerHTML = sTitle;
			sCss += ';position:absolute';
			sCss += ';left:' + (x - 2) + 'px';
			sCss += ';top:' + (y + 19) + 'px';
			sCss += ';display:;';
			dTitle.style.cssText = sCss;
		}
	},
	hideTitle : function() {
		var dTitle = document.getElementById('easyTitleShow');
		dTitle.style.display = 'none';
	},
	init : function() {
		var aTitle = this.getElementsByTitle(this.parent);
		if (aTitle) {
			var dTitle = document.createElement('DIV');
			dTitle.style.display = 'none';
			dTitle.setAttribute('id', 'easyTitleShow');
			document.body.appendChild(dTitle);
			this.addEventListen(aTitle, 'mouseover', this.showTitle, false);
			this.addEventListen(aTitle, 'mousemove', this.showTitle, false);
			this.addEventListen(aTitle, 'mouseout', this.hideTitle, false);
		}
	}
};
var et = new easyTitle(document.body);
function getSelectedText() {
	if (window.getSelection) {
		return window.getSelection().toString();
	} else if (document.getSelection) {
		return document.getSelection();
	} else if (document.selection) {
		return document.selection.createRange().text;
	}
}
justep.yn.showMessage = function(message) {
	if (parent.justep && parent.justep.yn && parent.justep.yn.showMessage) {
		parent.justep.yn.showMessage(message);
		return;
	}
	if (!checkPathisHave($dpcsspath + "showMessage.css"))
		createStyleSheet($dpcsspath + "showMessage.css");
	var oldsg = document.getElementById("msg-showmessage");
	if (oldsg)
		document.body.removeChild(oldsg);
	var main_Message_view = document.createElement("div");
	main_Message_view.setAttribute("id", "msg-showmessage");
	main_Message_view.setAttribute("class", "showmessage");
	main_Message_view.style.position = "absolute";
	main_Message_view.style.left = (document.body.offsetWidth - 300) / 2;
	main_Message_view.style.top = (document.body.offsetHeight - 100) / 2;
	main_Message_view.style.zIndex = "101";
	var message_show = "<div class=\"mborder\" style=\"margin-left:3px;width:294px;\"></div><div class=\"mborder\" style=\"margin-left:2px;width:296px;\"></div><div class=\"mborder\" style=\"margin-left:1px;width:298px\"></div><div id=\"msg_Text\" style=\"width:300px;height:25px;font-size:16px;font-color:#FF3333;\">"
			+ message
			+ "</div><div class=\"mborder\" style=\"margin-left:1px;width:298px\"></div><div class=\"mborder\" style=\"margin-left:2px;width:296px;\"></div><div class=\"mborder\" style=\"margin-left:3px;width:294px;\"></div>";
	document.body.appendChild(main_Message_view);
	main_Message_view.innerHTML = message_show;
	function startMove(obj) {
		obj.iAlpha = 60;
		obj.times && clearInterval(obj.time);
		obj.times = setInterval(function() {
			doMove(obj);
		}, 100);
	}
	function doMove(obj) {
		var iSpeed = 5;
		if (obj.iAlpha >= 90) {
			clearInterval(obj.times);
			obj.iAlpha = 100;
			obj.times = setInterval(function() {
				endMove(obj);
			}, 100);
		} else {
			obj.iAlpha += iSpeed;
		}
		obj.style.filter = "alpha(opacity=" + obj.iAlpha + ")";
		obj.style.opacity = obj.iAlpha / 100;
	}
	function endMove(obj) {
		var iSpeed = 5;
		if (obj.iAlpha <= 10) {
			clearInterval(obj.times);
			obj.iAlpha = 10;
			obj.time = null;
			document.body.removeChild(obj);
		} else {
			obj.iAlpha -= iSpeed;
		}
		obj.style.filter = "alpha(opacity=" + obj.iAlpha + ")";
		obj.style.opacity = obj.iAlpha / 100;
	}
	function showMessage() {
		var msgView = document.getElementById("msg-showmessage");
		startMove(msgView);
	}
	showMessage();
};
justep.yn.fileupload = function(dbkey, docPath, tablename, cellname, rowid,
		callback) {
	var url = "/comon/fileupload/upload.jsp";
	url += "?dbkey=" + dbkey;
	url += "&docPath=" + docPath;
	url += "&tablename=" + (tablename ? tablename : "undefined");
	url += "&cellname=" + (cellname ? cellname : "undefined");
	url += "&rowid=" + (rowid ? rowid : "undefined");
	url += "&personID=" + justep.yn.Context.getCurrentPersonID();
	justep.yn.portal.dailog.openDailog('文件上传', url, 350, 200, callback, false);
};
justep.yn.dowloadfile = function(fileID, filename) {
	var xmlHttp = justep.yn.xmlHttp();
	xmlHttp.onreadystatechange = function() {
		if (xmlHttp.readyState == 4) {
			if (xmlHttp.status == 200) {
				var r = eval('(' + xmlHttp.responseText + ')');
			}
		}
	};
	xmlHttp.open("post", "GetDownloadURLAction?fileID=" + J_u_encode(fileID)
			+ "&filename=" + J_u_encode(filename), false);
	xmlHttp.send(null);
	try {
		var r = eval('(' + xmlHttp.responseText + ')');
	} catch (e) {
	}
	if (!r) {
		alert("获取文件信息失败!可能文档服务配置错误，请联系管理员");
		return;
	}
	if (r.url && r.url != "err")
		window.open(r.url, "文件下载", "height=" + (screen.availHeight - 60)
				+ ",width=" + (screen.availWidth)
				+ ",toolbar=no,menubar=no,status=no,location=no,top=0,left=0");
	else
		alert("下载失败!");
	return;
};
justep.yn.deletefile = function(fileID, filename, dbkey, tablename, cellname,
		rowid, callback) {
	if (!fileID || fileID == "" || !filename)
		return;
	if (confirm("确定删除文件'" + filename + "'吗？")) {
		var url = "deleteFileAction?fileID=" + fileID;
		url += "&filename=" + J_u_encode(filename);
		url += "&dbkey=" + dbkey;
		url += "&tablename=" + tablename;
		url += "&cellname=" + cellname;
		url += "&rowid=" + rowid;
		var xmlHttp = justep.yn.xmlHttp();
		xmlHttp.onreadystatechange = function() {
			if (xmlHttp.readyState == 4) {
				if (xmlHttp.status == 200) {
					var r = eval('(' + xmlHttp.responseText + ')');
					if (callback)
						callback(r);
				}
			}
		};
		xmlHttp.open("post", url, true);
		xmlHttp.send(null);
	}
};
function UUID() {
	this.id = this.createUUID();
	return this;
}
UUID.prototype.valueOf = function() {
	return this.id;
};
UUID.prototype.toString = function() {
	return this.id;
};
UUID.prototype.createUUID = function() {
	var dg = new Date(1582, 10, 15, 0, 0, 0, 0);
	var dc = new Date();
	var t = dc.getTime() - dg.getTime();
	var tl = UUID.getIntegerBits(t, 0, 31);
	var tm = UUID.getIntegerBits(t, 32, 47);
	var thv = UUID.getIntegerBits(t, 48, 59) + '1';
	var csar = UUID.getIntegerBits(UUID.rand(4095), 0, 7);
	var csl = UUID.getIntegerBits(UUID.rand(4095), 0, 7);
	var n = UUID.getIntegerBits(UUID.rand(8191), 0, 7)
			+ UUID.getIntegerBits(UUID.rand(8191), 8, 15)
			+ UUID.getIntegerBits(UUID.rand(8191), 0, 7)
			+ UUID.getIntegerBits(UUID.rand(8191), 8, 15)
			+ UUID.getIntegerBits(UUID.rand(8191), 0, 15);
	return tl + tm + thv + csar + csl + n;
};
UUID.getIntegerBits = function(val, start, end) {
	var base16 = UUID.returnBase(val, 16);
	var quadArray = new Array();
	var quadString = '';
	var i = 0;
	for (i = 0; i < base16.length; i++) {
		quadArray.push(base16.substring(i, i + 1));
	}
	for (i = Math.floor(start / 4); i <= Math.floor(end / 4); i++) {
		if (!quadArray[i] || quadArray[i] == '')
			quadString += '0';
		else
			quadString += quadArray[i];
	}
	return quadString;
};
UUID.returnBase = function(number, base) {
	return (number).toString(base).toUpperCase();
};
UUID.rand = function(max) {
	return Math.floor(Math.random() * (max + 1));
};
justep.yn.ExcelImp = function(dbkey, table, relation, confirmXmlName, callback) {
	var srcPath = justep.yn.getRequestURI();
	var url = "/comon/report/import-compent.jsp";
	url += "?srcPath=" + srcPath;
	url += "&dbkey=" + dbkey;
	url += "&table=" + table;
	url += "&relation=" + relation;
	url += "&confirmXmlName=" + confirmXmlName;
	justep.yn.portal.dailog.openDailog('Excel导入', url, 300, 320, callback,
			false);
};
justep.yn.ExcelExp = function(dbkey, table, relation, labels, where, orderby) {
	var srcPath = justep.yn.getRequestURI();
	var url = "/comon/report/export-compent.jsp";
	var params = {
		dbkey : dbkey,
		table : table,
		relation : relation,
		labels : labels,
		where : where,
		orderby : orderby
	};
	justep.yn.portal.dailog.openDailog('导出Excel', url, 270, 300, null, false,
			null, params);
};
justep.yn.encodeURIComponent = function(str) {
	try {
		var $rval = str.toString().replaceAll("<", "#lt;");
		$rval = $rval.replaceAll(">", "#gt;");
		$rval = $rval.replaceAll("&nbsp;", "#160;");
		$rval = $rval.replaceAll("'", "#apos;");
		return J_u_encode($rval);
	} catch (e) {
		return "";
	}
};
justep.yn.decodeURIComponent = function(str) {
	try {
		str = decodeURIComponent(decodeURIComponent(str));
		var sValue = str.toString().replaceAll("#160;", "&nbsp;");
		sValue = sValue.replaceAll("#lt;", "<");
		sValue = sValue.replaceAll("#gt;", ">");
		sValue = sValue.replaceAll("#apos;", "'");
		return sValue;
	} catch (e) {
		return "";
	}
};
justep.yn.standardPartition = function(div) {
	if (!div)
		return;
	if (div.tagName != "DIV")
		return;
	var spheight = "100%";
	if (div.parentNode.tagName == "BODY") {
		spheight = $(document).height() + "px";
	}
	var spTable = "<table border='0' style='width:"
			+ (div.style.width ? div.style.width : "100%")
			+ ";height:"
			+ (div.style.height == "100%" ? spheight : "100%")
			+ ";table-layout:fixed;word-break:break-all; border:1px solid #eee; border-collapse: collapse;'>";
	spTable += "<tr style='border:1px solid #eee; word-break:break-all;'><td valign='top' style='";
	var spLTRT = div.getElementsByTagName("div");
	var left = null;
	var right = null;
	for (var i = 0; i < spLTRT.length; i++) {
		if (spLTRT[i].getAttribute("stpa") == "left") {
			left = spLTRT[i];
		}
		if (spLTRT[i].getAttribute("stpa") == "right") {
			right = spLTRT[i];
		}
	}
	if (!left || left.getAttribute("stpa") != "left") {
		alert("未定义左边内容！At:justep.yn.standardPartition");
		return;
	}
	spTable += "width:" + left.style.width + ";'>";
	spTable += left.innerHTML + "</td>";
	spTable += "<td width='6px' onmouseover='justep.yn.splitMove(this)' id='"
			+ div.id
			+ "_spTD' style='background: url("
			+ $dpcsspath
			+ "formDetail/img/shadow_r3_c1.gif);table-layout: fixed;word-break: break-all;'><a href='javascript:void(0)' style='border:0px;'><img id='"
			+ div.id + "_splitItem' src='" + $dpimgpath
			+ "splitimg/left.gif' style='width:6px;height:56px;'/></a></td>";
	spTable += "<td valign='top' ";
	if (!right || right.getAttribute("stpa") != "right") {
		alert("未定义右边内容！At:justep.yn.standardPartition");
		return;
	}
	spTable += "style='width:" + right.style.width + ";'>";
	spTable += right.innerHTML + "</td></tr></table>";
	div.innerHTML = spTable;
	var onresizeFn = div.getAttribute("onspResize");
	var shEvent = function() {
		if (onresizeFn && onresizeFn != "") {
			onresizeFn = window.eval("(" + onresizeFn + ")");
			if (typeof onresizeFn == "function") {
				onresizeFn();
			}
		}
	};
	document.getElementById(div.id + "_splitItem").onclick = function(e) {
		var ev = e || event;
		var srcE = ev.srcElement ? ev.srcElement : ev.target;
		var STR = srcE.parentNode.parentNode.parentNode;
		if (srcE.tagName == "IMG"
				&& srcE.tempsrc != $dpimgpath + "splitimg/right.gif") {
			srcE.src = $dpimgpath + "splitimg/right.gif";
			STR.cells[0].tempWidth = STR.cells[0].style.width;
			STR.cells[0].style.width = "0px";
			STR.cells[1].style.width = "6px";
			STR.cells[2].style.width = "100%";
			srcE.tempsrc = $dpimgpath + "splitimg/right.gif";
		} else {
			srcE.src = $dpimgpath + "splitimg/left.gif";
			STR.cells[0].style.width = STR.cells[0].tempWidth;
			STR.cells[1].style.width = "6px";
			srcE.tempsrc = $dpimgpath + "splitimg/left.gif";
		}
		shEvent();
	};
	this.close = function(srcE, STR) {
		srcE = srcE || document.getElementById(div.id + "_splitItem");
		STR = STR || div.getElementsByTagName("table")[0].rows[0];
		srcE.src = $dpimgpath + "splitimg/right.gif";
		STR.cells[0].tempWidth = STR.cells[0].style.width;
		STR.cells[0].style.width = "0px";
		STR.cells[1].style.width = "6px";
		STR.cells[2].style.width = "100%";
		srcE.tempsrc = $dpimgpath + "splitimg/right.gif";
		shEvent();
	};
	this.open = function(srcE, STR) {
		srcE = srcE || document.getElementById(div.id + "_splitItem");
		STR = STR || div.getElementsByTagName("table")[0].rows[0];
		srcE.src = $dpimgpath + "splitimg/left.gif";
		STR.cells[0].style.width = STR.cells[0].tempWidth;
		STR.cells[1].style.width = "6px";
		srcE.tempsrc = $dpimgpath + "splitimg/left.gif";
		shEvent();
	};
	this.moveToEnd = function(srcE, STR) {
		srcE = srcE || document.getElementById(div.id + "_splitItem");
		STR = STR || div.getElementsByTagName("table")[0].rows[0];
		srcE.src = $dpimgpath + "splitimg/left.gif";
		STR.cells[0].style.width = "100%";
		STR.cells[1].style.width = "6px";
		STR.cells[2].style.width = "0px";
		srcE.tempsrc = $dpimgpath + "splitimg/left.gif";
		shEvent();
	};
	div.standardPartition = this;
	return this;
};
var unselecttext = function() {
	return false;
};
justep.yn.splitMove = function(obj) {
	document.resizeControl = obj;
	var objID = obj.id.replace("_spTD", "");
	var src = document.resizeControl;
	var SCPTable = obj.parentNode.parentNode.parentNode;
	var roldWidth = parseInt(SCPTable.parentNode.clientWidth) - 6;
	var getoffset = function(src) {
		var ct = 0;
		var cl = 0;
		var p = src;
		while (p.offsetParent) {
			ct += p.offsetTop;
			cl += p.offsetLeft;
			p = p.offsetParent;
		}
		return [ ct, cl, src.clientWidth, src.clientHeight ];
	};
	var fnMouseup = function() {
		document.down = null;
		var src = document.resizeControl;
		if (src) {
			src.style.cursor = null;
			src.resize = null;
			src.srcData = null;
			document.resizeControl = null;
		}
		$(document).unbind('selectstart', unselecttext);
	};
	addEvent(document, "mouseup", fnMouseup, false);
	var fnMouseDown = function(e) {
		if (document.resizeControl) {
			document.down = 1;
			$(document).bind('selectstart', unselecttext);
			var src = document.resizeControl;
			src = obj.parentNode.cells[src.cellIndex - 1];
			var srcData = getoffset(obj);
			var ev = e || event;
			var cX = ev.clientX;
			var cY = ev.clientY;
			src.srcData = [ src.clientWidth, ev.clientX ];
			if (cY >= srcData[0] && cY <= srcData[0] + srcData[3]
					&& cX >= srcData[1] + srcData[2] - 6
					&& cX <= srcData[1] + srcData[2]) {
				src.resize = 1;
			} else {
				src.style.cursor = null;
			}
		}
	};
	addEvent(obj, "mousedown", fnMouseDown, false);
	var fnMouseMove = function(e) {
		if (document.resizeControl) {
			var src = document.resizeControl;
			src = obj.parentNode.cells[src.cellIndex - 1];
			var srcData = getoffset(obj);
			var ev = e || event;
			var cX = ev.clientX;
			var cY = ev.clientY;
			if (cY >= srcData[0] && cY <= srcData[0] + srcData[3]
					&& cX >= srcData[1] + srcData[2] - 6
					&& cX <= srcData[1] + srcData[2]) {
				obj.style.cursor = "W-resize";
			} else {
				if (!document.dow) {
					src.style.cursor = null;
				}
			}
			if (document.down && src.resize) {
				var newWidth = src.srcData[0] + cX - src.srcData[1];
				if (newWidth > 0) {
					src.style.width = (newWidth).toString() + "px";
					var SCPTable = obj.parentNode.parentNode.parentNode;
					var rTDwidth = roldWidth - newWidth - 6;
					if (rTDwidth < 0)
						rTDwidth = 0;
					obj.parentNode.cells[obj.cellIndex + 1].style.width = rTDwidth
							.toString()
							+ "px";
					obj.style.width = "6px";
					if (document.down) {
						var onresizeFn = document.getElementById(objID)
								.getAttribute("onspResize");// onresize 在IE8以下冲突
						var shEvent = function() {
							if (onresizeFn && onresizeFn != "") {
								onresizeFn = window
										.eval("(" + onresizeFn + ")");
								if (typeof onresizeFn == "function") {
									onresizeFn();
								}
							}
						};
						shEvent();
					}
				}
			}
		} else {
			return true;
		}
	};
	addEvent(document, "mousemove", fnMouseMove, false);
	var fnSelectstart = function() {
		return false;
	};
	$(document).bind("mouseup", function() {
		$(document).unbind('selectstart', unselecttext);
		document.resizeControl = null;
		document.down = null;
	});
	return this;
};
function addEvent(elm, evType, fn, useCapture) {
	if (elm.addEventListener) {
		elm.addEventListener(evType, fn, useCapture);
		return true;
	} else if (elm.attachEvent) {
		var r = elm.attachEvent('on' + evType, fn);
		return r;
	} else {
		elm['on' + evType] = fn;
	}
}
function removeEvent(obj, type, fn, cap) {
	var cap = cap || false;
	if (obj.removeEventListener) {
		obj.removeEventListener(type, fn, cap);
	} else {
		obj.detachEvent("on" + type, fn);
	}
}
justep.yn.sTab = function(div) {
	if (!div)
		return;
	var stabLabels = div.getElementsByTagName("div");
	if (!checkPathisHave($dpcsspath + "tab.main.css"))
		createStyleSheet($dpcsspath + "tab.main.css");
	var $tab_creat_t = "<table id='" + div.id + "_tab_table' style='width:"
			+ div.style.width + ";height:" + div.style.height
			+ ";' class='Main_tab'><tr style='width:100%;height:25px;'>";
	var $sLabs = new Array();
	var $sContext = new Array();
	for (var i = 0; i < stabLabels.length; i++) {
		if (stabLabels[i].getAttribute("type") == "tLabel")
			$sLabs.push(stabLabels[i]);
		if (stabLabels[i].getAttribute("type") == "tContext")
			$sContext.push(stabLabels[i]);
	}
	var contectIDs = "";
	for (i in $sContext) {
		contectIDs += "," + div.id + "_st_" + i;
	}
	contectIDs.replaceFirst(",", "");
	if ($sLabs.length != $sContext.length) {
		alert("标题和内容没有对应！At:justep.yn.sTab");
		return;
	}
	for (i in $sLabs) {
		var $_width = $sLabs[i].style.width ? $sLabs[i].style.width : "100px";
		if (i == 0)
			$tab_creat_t += "<td class='Tab_label' align='left' width='"
					+ $_width
					+ "' id='"
					+ div.id
					+ "_sp_td"
					+ i
					+ "'><div class='Tab_labelL_s' style='width:10px;'></div><div style='width:"
					+ (parseInt($_width) - 35)
					+ "px;' class='Tab_labelC_s' id='"
					+ div.id
					+ "_sl_"
					+ i
					+ "' onclick='$sys_selectTab(\""
					+ div.id
					+ "_sl_"
					+ i
					+ "\",\""
					+ contectIDs
					+ "\",\""
					+ div.id
					+ "_st_"
					+ i
					+ "\")'>"
					+ $sLabs[i].innerHTML
					+ "</div><div style='width:15px;' class='Tab_labelR_s'></div></td>";
		if (i != 0)
			$tab_creat_t += "<td class='Tab_label' align='left' width='"
					+ $_width
					+ "' id='"
					+ div.id
					+ "_sp_td"
					+ i
					+ "'><div class='Tab_labelL' style='width:10px;'></div><div style='width:"
					+ (parseInt($_width) - 35)
					+ "px;' class='Tab_labelC' id='"
					+ div.id
					+ "_sl_"
					+ i
					+ "' onclick='$sys_selectTab(\""
					+ div.id
					+ "_sl_"
					+ i
					+ "\",\""
					+ contectIDs
					+ "\",\""
					+ div.id
					+ "_st_"
					+ i
					+ "\")'>"
					+ $sLabs[i].innerHTML
					+ "</div><div style='width:15px;' class='Tab_labelR'></div></td>";
	}
	$tab_creat_t += "<td></td></tr><tr class='Tab_Contect'><td colspan='"
			+ ($sLabs.length + 1) + "' class='Tab_Contect'>";
	for (i in $sContext) {
		if (i == 0)
			$tab_creat_t += "<div id='"
					+ div.id
					+ "_st_"
					+ i
					+ "' style='border:1px solid #ddd;width:100%;height:100%;'>"
					+ $sContext[i].innerHTML + "</div>";
		if (i != 0)
			$tab_creat_t += "<div id='"
					+ div.id
					+ "_st_"
					+ i
					+ "' style='border:1px solid #ddd;width:100%;height:100%;display:none;'>"
					+ $sContext[i].innerHTML + "</div>";
	}
	$tab_creat_t += "</td></tr>";
	$tab_creat_t += "</table>";
	div.innerHTML = $tab_creat_t;
	return this;
};
var $sys_selectTab = function(stab, contectID, sConID) {
	var ceurrentTab = document.getElementById(sConID);
	ceurrentTab.style.display = "";
	var CurrentTD = document.getElementById(stab).parentNode;
	var currentTDiv = CurrentTD.getElementsByTagName("div");
	if (currentTDiv.length >= 3) {
		currentTDiv[0].className = "Tab_labelL_s";
		currentTDiv[1].className = "Tab_labelC_s";
		currentTDiv[2].className = "Tab_labelR_s";
	}
	var stab_view = document.getElementById(stab).parentNode.parentNode;
	var stab_view_tds = stab_view.getElementsByTagName("td");
	for (var i = 0; i < stab_view_tds.length - 1; i++) {
		var currentTD = stab_view_tds[i].getElementsByTagName("div");
		if (currentTD.length >= 3
				&& CurrentTD.getAttribute("id") != stab_view_tds[i]
						.getAttribute("id")) {
			currentTD[0].className = "Tab_labelL";
			currentTD[1].className = "Tab_labelC";
			currentTD[2].className = "Tab_labelR";
		}
	}
	var contectIDs = contectID.split(",");
	for (var k = 0; k < contectIDs.length; k++) {
		if (contectIDs[k] != sConID) {
			if (document.getElementById(contectIDs[k]))
				document.getElementById(contectIDs[k]).style.display = "none";
		}
	}
};
// *****添加了下载控制参数20140605
justep.yn.fileComponent = function(div, data, cellname, docPath, canupload,
		candelete, canedit, viewhistory, limit, download) {
	if (!div || !data || !cellname)
		return;
	var $commonpath = $dpjspath.replace("/js/", "/");
	if (!checkPathisHave("/JBIZ/comon/doc_ocx/WindowDialog.js")
			&& !checkPathisHave($commonpath + "doc_ocx/WindowDialog.js")) {
		createJSSheet($commonpath + "doc_ocx/WindowDialog.js");
	}
	if (!checkPathisHave("/JBIZ/comon/doc_ocx/common/base64.js")
			&& !checkPathisHave($commonpath + "doc_ocx/common/base64.js")) {
		createJSSheet($commonpath + "doc_ocx/common/base64.js");
	}
	if (!checkPathisHave("/JBIZ/comon/doc_ocx/common/json.js")
			&& !checkPathisHave($commonpath + "doc_ocx/common/json.js")) {
		createJSSheet($commonpath + "doc_ocx/common/json.js");
	}
	if (!checkPathisHave("/JBIZ/comon/doc_ocx/yahooUtil.js")
			&& !checkPathisHave($commonpath + "doc_ocx/yahooUtil.js")) {
		createJSSheet($commonpath + "doc_ocx/yahooUtil.js");
	}
	if (!checkPathisHave("/JBIZ/comon/doc_ocx/docUtil.js")
			&& !checkPathisHave($commonpath + "doc_ocx/docUtil.js")) {
		createJSSheet($commonpath + "doc_ocx/docUtil.js");
	}
	this.uploadcount = 0;
	div.uploader = null;
	div.writedata = new Array();
	this.div = div;
	this.CimitDataParam = function(docName, kind, size, cacheName,
			revisionCacheName, commentFileContent, filecount, compment) {
		var pa_log = {};
		pa_log.dbkey = data.dbkay;
		pa_log.docPath = docPath || "/root";
		pa_log.tablename = data.table;
		pa_log.cellname = cellname;
		pa_log.rowid = data.rowid;
		pa_log.docName = docName;
		pa_log.kind = kind;
		pa_log.size = size;
		pa_log.cacheName = cacheName;
		compment.div.writedata.push(pa_log);
		compment.uploadcount++;
		if (compment.uploadcount == filecount) {
			compment.comitDataFn();
		}
	};
	this.comitDataFn = function() {
		var paramlog = JSON.stringify(this.div.writedata);
		var pas = new justep.yn.RequestParam();
		pas.set("writelog", paramlog);
		var self = this;
		justep.yn.XMLHttpRequest("writeUploadDataAction", pas, "POST", true,
				function(r) {
					self.uploadcallback(r, self);
				});
		this.uploadcount = 0;
	};
	this.uploadcallback = function(r, compment) {
		compment.$refreshFileComp();
		compment.div.writedata = new Array();
	};
	this.$file_upload = function() {
		var self = (this.tagName) ? this.compment : this;
		var dbkey = data.dbkay;
		var tablename = data.table;
		var rowid = data.saveData();
		if (!rowid || rowid == "") {
			alert("请先保存数据！");
			return;
		} else {
			if (!self.div.uploader) {
				self.createUploadElement(self);
			}
			self.div.uploader.setDocPath(docPath);
		}
	};
	this.createUploadElement = function(self) {
		try {
			var uploadDocItemDiv = document.createElement("div");
			uploadDocItemDiv.setAttribute("id", self.div.id
					+ "_uploadDocItemDiv");
			uploadDocItemDiv.style.position = "absolute";
			// uploadDocItemDiv.style.position = "relative";
			var uploadMenuItem = document.getElementById(self.div.id
					+ "_uploadItem");
			uploadMenuItem.parentElement.insertBefore(uploadDocItemDiv,
					uploadMenuItem);
			// $(uploadDocItemDiv).css("top",
			// ($(uploadMenuItem).position().top) + "px");
			// $(uploadDocItemDiv).css("left",
			// ($(uploadMenuItem).position().left) + "px");
			$(uploadDocItemDiv).css("top", "0px");
			$(uploadDocItemDiv).css("left", "0px");
			$(uploadDocItemDiv).css("z-index", 999);
			this.div.uploader = justep.Doc.getUploader(self.div.id
					+ "_uploadDocItemDiv", "/", -1,
					function(docName, kind, size, cacheName, revisionCacheName,
							commentFileContent, filecount) {
						self.CimitDataParam(docName, kind, size, cacheName,
								revisionCacheName, commentFileContent,
								filecount, self);
					}, function() {
					}, 160, 27, 1, undefined, !(limit == 1));
		} catch (e) {
		}
	};
	var taptt = justep.yn.RequestURLParam.getParam("activity-pattern");
	var isTasksub = (taptt=="detail");
	this.div.file_upload = this.$file_upload;
	var fiTablehead = "<table style='width:100%;' border='0'>";
	if (false != canupload && !isTasksub) {
		fiTablehead += "<tr id=\""
				+ div.id
				+ "_uploadTR\"><td colspan='6' align='left' style='width:100%;height:20px;border:0px none;'><div style='position:relative;'>"
				+ "<a href='javascript:void(0)' id='"
				+ div.id
				+ "_uploadItem' onclick='document.getElementById(\""
				+ div.id
				+ "\").file_upload()' title='上传文件' style='font-size:12px;color:#0033FF;text-decoration: none;'>上传文件</a><hr/></div></td></tr>";
	}
	var filetableBody = "<tr><td valign='top' style='border:0px none;'><div id='"
			+ div.id + "_fileList'></div></td></tr>";
	var filetableFooter = "<tr><td style='border:0px none;'></td></tr></table>";
	this.div.innerHTML = fiTablehead + filetableBody + filetableFooter;
	this.$refreshFileComp = function() {
		var dbkey = data.dbkay;
		var tablename = data.table;
		var rowid = data.rowid;
		var sID = (dbkey == "system" || !dbkey) ? "SID" : "fID";
		var random = Math.random();
		var sql = "select " + cellname + " FILECOMPE from " + tablename
				+ " where " + sID + " = '" + rowid + "' and " + random + "="
				+ random;
		var r = justep.yn.sqlQueryActionforJson(dbkey, sql);
		var dilelist = [];
		var transeJson = function(str) {
			str = str.toString().replaceAll(":", ":\"");
			str = str.toString().replaceAll(",", "\",");
			str = str.toString().replaceAll("}", "\"}");
			str = str.toString().replaceAll("}{", "},{");
			str = str.toString().replaceAll(";", "\",");
			var filelist = eval("([" + str + "])");
			return filelist;
		};
		try {
			if (r.data != "") {
				var datas = r.data[0];
				datas = datas.FILECOMPE;
				if("null"==datas){
					datas = "";
				}
				if (datas && datas != "") {
					try {
						dilelist = eval("(" + datas + ")");
					} catch (e) {
						dilelist = transeJson(datas);
					}
				}
			}
		} catch (e) {
			alert(e.message);
		}
		var fileIDs = new Array();
		var filenames = new Array();
		for (var i = 0; i < dilelist.length; i++) {
			fileIDs.push(dilelist[i].fileID);
			filenames.push(dilelist[i].filename);
		}
		filetableBody = "<table>";
		docPath = docPath || "/";
		try {
			if (limit && limit != -1 && limit <= filenames.length) {
				document.getElementById(div.id + "_uploadItem").style.display = "none";
			} else {
				document.getElementById(div.id + "_uploadItem").style.display = "";
			}
		} catch (e) {
		}
		if (filenames && filenames.length > 0) {
			for (i in filenames) {
				var fileID = fileIDs[i];
				filetableBody += "<tr style='width:100%;height:20px;'><td style='border:0px none;'>"
						+ "<a href='javascript:void(0)' id=\""
						+ div.id
						+ "_titleItem\" style='font-size:12px;color:#000000;' title='"
						+ filenames[i]
						+ "' onclick='justep.Doc.browseDocByID(\""
						+ fileID
						+ "\",\""
						+ filenames[i]
						+ "\")'>"
						+ filenames[i]
						+ "</a></td>";
				filetableBody += "<td width='40px;' style='border:0px none;'>"
						+ "<a href='javascript:void(0)' style='font-size:12px;color:#0033FF;text-decoration: none;' title='文件属性' onclick='justep.Doc.openDocInfoDialog(\""
						+ fileID + "\")'>属性</a></td>";
				if (canedit == true && !isTasksub) {
					// filetableBody += "<td width='40px;' style='border:0px
					// none;'><a href='javascript:void(0)'
					// style='font-size:12px;color:#0033FF;text-decoration:
					// none;' title='编辑文件' onclick='justep.yn.editfile(\""
					// + fileID
					// + "\",\""
					// + filenames[i]
					// + "\",\""
					// + docPath + "\")'>编辑</a></td>";
					filetableBody += "<td width='40px;' style='border:0px none;'><a href='javascript:void(0)' "
							+ "style='font-size:12px;color:#0033FF;text-decoration: none;' title='编辑文件' "
							+ "onclick='justep.yn.trangereditfile(\""
							+ fileID
							+ "\",\""
							+ filenames[i]
							+ "\",\""
							+ docPath
							+ "\",\""
							+ data.dbkay
							+ "\",\""
							+ data.table
							+ "\",\""
							+ data.rowid
							+ "\",\""
							+ cellname
							+ "\",\"" + div.id + "\")'>编辑</a></td>";
				} else {
					filetableBody += "<td style='border:0px none;'></td>";
				}
				if (viewhistory == true) {
					filetableBody += "<td width='40px;' style='border:0px none;'><a href='javascript:void(0)' style='font-size:12px;color:#0033FF;text-decoration: none;' title='历史版本' onclick='justep.Doc.openDocHistoryDialog(null,\""
							+ fileID + "\")'>历史</a></td>";
				} else {
					filetableBody += "<td style='border:0px none;'></td>";
				}
				if (candelete != false && !isTasksub) {
					filetableBody += "<td width='40px;' style='border:0px none;'><a href='javascript:void(0)' style='font-size:12px;color:#0033FF;text-decoration: none;' title='删除附件' onclick='justep.yn.deletefile(\""
							+ fileID
							+ "\",\""
							+ filenames[i]
							+ "\",\""
							+ dbkey
							+ "\",\""
							+ tablename
							+ "\",\""
							+ cellname
							+ "\",\""
							+ rowid
							+ "\",function(){document.getElementById(\""
							+ div.id + "\").refreshFileComp()})'>删除</a></td>";
				} else {
					filetableBody += "<td></td>";
				}
				if (download != false) {
					filetableBody += "<td width='40px;' style='border:0px none;'><a href='javascript:void(0)' style='font-size:12px;color:#0033FF;text-decoration: none;' title='下载附件' onclick='justep.Doc.downloadDocByFileID(\""
							+ docPath + "\",\"" + fileID + "\")'>下载</a></td>";
				} else {
					filetableBody += "<td style='border:0px none;'></td>";
				}
				filetableBody += "</tr>";
			}
		}
		filetableBody += "</table>";
		document.getElementById(div.id + "_fileList").innerHTML = filetableBody;
		if (data.rowid && data.rowid != "") {
			try {
				setTimeout(div.compment.createUploadElement, 1000);
			} catch (e) {
			}
		}
	};
	this.div.refreshFileComp = this.$refreshFileComp;
	this.$refreshFileComp();
	div.compment = this;
};
var editFilecoment;
function rahgereditercalback() {
	var reloadfilefn = document.getElementById(editFilecoment).refreshFileComp;
	reloadfilefn();
}
// 阮航空间编辑
justep.yn.trangereditfile = function(fileID, fileName, docPath, dbkey,
		tablename, billid, cellname, comentid) {
	editFilecoment = comentid;
	if ('.doc.docx.xls.xlsx.ppt.pptx.mpp.vsd.dps.wps.et.'
			.indexOf(String(/\.[^\.]+$/.exec(fileName)) + '.') < 0) {
		alert("不支持非Office文件编辑");
		return;
	}
	justep.yn.portal.openWindow("文件" + fileName + "编辑",
			"/JBIZ/comon/doc_ocx/tangerOffice/officeediter.jsp?fileID="
					+ fileID + "&fileName=" + fileName + "&dbkey=" + dbkey
					+ "&tablename=" + tablename + "&billid=" + billid
					+ "&cellname=" + cellname + "&callerName="
					+ justep.yn.portal.currentTabId() + "&option=edit");
};
// X5文件编辑器编辑
justep.yn.editfile = function(fileID, fileName, docPath) {
	var OVP = {
		host : docPath,
		userName : justep.yn.Context.getCurrentPersonName(),
		userInitials : justep.yn.Context.getCurrentPersonName(),
		programID : "OpenOffice",
		filename : fileName.substr(0, fileName.lastIndexOf('.')),
		cacheName : fileID,
		fileID : fileID,
		fileExt : String(/\.[^\.]+$/.exec(fileName)),
		showField : true,
		isPrint : true
	};
	if (!justep.Doc.checkOcx())
		return;
	if ('.doc.docx.xls.xlsx.ppt.pptx.mpp.vsd.'.indexOf(String(/\.[^\.]+$/
			.exec(fileName))
			+ '.') < 0) {
		alert("不支持非Office文件编辑");
		return;
	}
	var docID = justep.yn.getDocIdByFileId(fileID);
	var afterEditOfficeDialogSelect = function(data) {
		if (data != '' && data.changes != 'W10=') {
			document.lastOperation = "noNeedLock";
			justep.yn.updateDoc(docID, fileID, docPath, data.filename,
					data.mediatype, data.size, data.cacheName,
					data.revisionCacheName, data.changes, data.createVersion);
		} else {
			if (data != '' && data.changes == 'W10=' && data.createVersion) {
				justep.yn.createVersion(docID, fileID, data.filename, docPath);
			} else {
				var currentNode = justep.Doc.evalChangeLog(justep.yn.changeLog,
						docID);
				if (currentNode == null && document.lastOperation == "success") {
					justep.Doc.unLockDoc(docID);
				}
			}
		}
	};
	if (!document.getElementById("docExtDiv")) {
		var domdiv = document.createElement("div");
		domdiv.setAttribute("id", "docExtDiv");
		document.body.appendChild(domdiv);
	}
	justep.Doc.openOfficeDialog("docExtDiv", "docExt", OVP,
			afterEditOfficeDialogSelect);
};
justep.yn.changeLog = {
	items : [],
	autoCreateVersion : 0,
	createVersionLogs : [],
	"operate" : "",
	"url" : "",
	process : "",
	activity : ""
};
justep.yn.updateDoc = function(docID, fileID, docPath, docName, kind, size,
		cacheName, revisionCacheName, commentFileContent, createVersion) {
	var node = justep.Doc.evalChangeLog(justep.yn.changeLog, docID);
	if (node) {
		var version = node.version;
		var parentID = node.parent_id;
		var displayPath = node.doc_display_path;
		var docVersionID = node.doc_version_id;
		var description = node.description;
		var classification = node.classification;
		var keywords = node.keywords;
		var finishTime = node.finish_time;
		var serialNumber = node.serial_number;
		justep.Doc.modifyChangeLog(node,
				[ version, fileID, docVersionID, docName, kind, size, parentID,
						docPath, displayPath, description, classification,
						keywords, finishTime, serialNumber ], [ "attachment",
						cacheName, revisionCacheName, commentFileContent ]);
	} else {
		var row = justep.Doc.queryDoc(docID, undefined, [ "VERSION",
				"SPARENTID", "SDOCDISPLAYPATH", "SDOCLIVEVERSIONID",
				"SDESCRIPTION", "SCLASSIFICATION", "SKEYWORDS", "SFINISHTIME",
				"SDOCSERIALNUMBER" ], undefined, undefined, "single");
		var version = row.VERSION;
		var parentID = row.SPARENTID;
		var displayPath = row.SDOCDISPLAYPATH;
		var docVersionID = row.SDOCLIVEVERSIONID;
		var description = row.SDESCRIPTION;
		var classification = row.SCLASSIFICATION;
		var keywords = row.SKEYWORDS;
		var finishTime = !row.SFINISHTIME ? "" : row.SFINISHTIME;
		var serialNumber = row.SDOCSERIALNUMBER;
		justep.Doc.addChangeLog(justep.yn.changeLog, "edit", [ docID, version,
				fileID, docVersionID, docName, kind, size, parentID, docPath,
				displayPath, description, classification, keywords, finishTime,
				serialNumber ], [ "attachment", cacheName, revisionCacheName,
				commentFileContent ]);
	}
	if (fileID) {
		justep.Doc.commitDocCache(docID, justep.yn.changeLog);
	}
	if (createVersion && fileID) {
		justep.yn.createVersion(docID, fileID, docName, docPath);
	}
};
justep.yn.createVersion = function(docID, fileID, docName, docPath) {
	if (fileID == '') {
		return;
	}
	if ('.doc.docx.xls.xlsx.ppt.mpp.vsd.'.indexOf(String(/\.[^\.]+$/
			.exec(docName))
			+ '.') < 0) {
		alert("不支持非Office文件成文");
		return;
	}
	var currentNode = justep.Doc.evalChangeLog(justep.yn.changeLog, docID);
	if (currentNode != null) {
		justep.Doc.removeChangeLog(justep.yn.changeLog, docID);
	}
	justep.Doc.createVersion(docID);
};
justep.yn.getDocIdByFileId = function(fileID) {
	var r = justep.yn.sqlQueryAction("system",
			"select SID from sa_docnode where (sfileid='" + fileID + "')");
	if (r.getCount() > 0) {
		return r.getValueByName("SID");
	}
};
justep.yn.picComponent = function(div, data, cellname, canEdit) {
	if (!div || !data || !cellname) {
		mAlert("justep.yn.picComponent：参数无效");
		return;
	}
	div.style.position = "relative";
	div.style.border = "1px solid #eee";
	var $CompPath = $dpimgpath.replace("/image", "") + "picCompant/";
	var bsPIC = false;
	var lookPIC = function() {
		var dbkey = data.dbkay ? data.dbkay : "system";
		var tablename = data.table;
		var rowid = data.rowid;
		if (!rowid) {
			div.innerHTML = "<img src='" + $CompPath
					+ "pic/img.gif' style='width:" + div.clientWidth
					+ ";height:" + div.clientHeight + ";z-index:-1;'/>";
			return;
		}
		var url = $dpimgpath.replace("/image", "");
		url += "picCompant/Pic-read.jsp?dbkey=" + dbkey + "&tablename="
				+ tablename + "&cellname=" + cellname + "&fID=" + rowid
				+ "&Temp=" + new UUID().toString();
		var image = "<img src='" + url + "' style='width:" + div.clientWidth
				+ ";height:" + div.clientHeight + ";'/>";
		bsPIC = true;
		div.innerHTML = image;
	};
	div.lookPIC = lookPIC;
	var picBorder = function() {
		// $("#" + div.id + "_edit_bar").remove();
		var editBar = document.getElementById(div.id + "_edit_bar");
		if (editBar) {
			return;
		}
		var editBar = document.createElement("div");
		editBar.setAttribute("id", div.id + "_edit_bar");
		editBar.style.background = "#eee";
		editBar.style.border = "0";
		editBar.style.position = "absolute";
		editBar.style.left = "0px";
		editBar.style.top = "0px";
		editBar.style.font = "12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
		editBar.style.width = div.offsetWidth;
		editBar.style.height = div.offsetHeight;
		editBar.style.textAlign = "right";
		editBar.style.lineHeight = "18px";
		editBar.style.filter = "alpha(opacity = 30)";
		editBar.style.opacity = 0.3;
		editBar.style.zIndex = 99;
		$(editBar).css("opacity", "0.3");
		$(editBar).css("-moz-opacity", "0.3");
		var dataTable = "<table id='"
				+ div.id
				+ "_table' style='width:100%;height:100%;z-index:999;'><tr><td colspan='3'></td></tr><tr style='width:100%;height:20px;align:right;'><td></td>";
		dataTable += "<td style='width:20px;'><a href='javascript:void(0)' style='color:#eee;' onclick='document.getElementById(\""
				+ div.id
				+ "\").uploadPIC()'><img src='"
				+ $CompPath
				+ "pic/edit.gif' title='上传图片' style='width:18px;'/></a></td>";
		dataTable += "<td style='width:20px;'><a href='javascript:void(0)' style='color:#eee;' onclick='document.getElementById(\""
				+ div.id
				+ "\").deletePIC()'><img src='"
				+ $CompPath
				+ "pic/remove.gif' title='删除图片' style='width:18px;'/></a></td>";
		dataTable += "</tr></table>";
		editBar.innerHTML = dataTable;
		// document.body.appendChild(editBar);
		$(div).append(editBar);
		var removeBar = function() {
			var editBar = document.getElementById(div.id + "_edit_bar");
			if (editBar) {
				div.removeChild(editBar);
			}
		};
		addEvent(document.getElementById(div.id + "_edit_bar"), "mouseout",
				removeBar, false);
	};
	div.picBorder = picBorder;
	if (canEdit != false) {
		addEvent(div, "mouseover", picBorder, false);
	}
	var uploadPIC = function() {
		var dbkey = data.dbkay ? data.dbkay : "system";
		var tablename = data.table;
		var rowid = data.saveData();
//		if (!rowid || rowid == "") {
//			alert("请先保存数据！");
//			return;
//		}
		var url = "/comon/picCompant/Imag-upload.jsp";
		url += "?dbkey=" + dbkey;
		url += "&tablename=" + tablename;
		url += "&cellname=" + cellname;
		url += "&rowid=" + rowid;
		var upLcallback = function() {
			document.getElementById(div.id).lookPIC();
		};
		justep.yn.portal.dailog.openDailog('图片上传', url, 350, 300, upLcallback,
				false);
	};
	div.uploadPIC = uploadPIC;
	var deletePIC = function() {
		if (!bsPIC)
			return;
		var dbkey = data.dbkay;
		var tablename = data.table;
		var rowid = data.rowid;
		if (!rowid || rowid == "") {
			return;
		}
		if (confirm("确定删除图片吗？")) {
			var dbkey = data.dbkay ? data.dbkay : "system";
			var tablename = data.table;
			var rowid = data.rowid;
			var url = "ImageDeleteAction";
			url += "?dbkey=" + dbkey;
			url += "&tablename=" + tablename;
			url += "&cellname=" + cellname;
			url += "&rowid=" + rowid;
			var xmlHttp = justep.yn.xmlHttp();
			xmlHttp.onreadystatechange = function() {
				if (xmlHttp.readyState == 4) {
					if (xmlHttp.status == 200) {
						var r = eval('(' + xmlHttp.responseText + ')');
						if (r.flag == "false") {
							alert(r.caption);
						} else {
							document.getElementById(div.id).lookPIC();
						}
					}
				}
			};
			xmlHttp.open("post", url, true);
			xmlHttp.send(null);
		}
	};
	div.deletePIC = deletePIC;
	$(div).css("overflow", "hidden");
	lookPIC();
	return this;
};
var mAlert = function(msg, img) {
	try {
		var allviewcap = document.createElement("div");
		allviewcap.setAttribute("id", "mAlertmsgDiv");
		allviewcap.style.left = "0px";
		allviewcap.style.top = "0px";
		allviewcap.style.width = document.body.clientWidth;
	} catch (e) {
		alert(msg);
		return;
	}
	var sHeight = document.body.clientHeight;
	if (window.innerHeight && window.scrollMaxY) {
		sHeight = window.innerHeight + window.scrollMaxY;
	} else if (document.body.scrollHeight > document.body.offsetHeight) {
		sHeight = document.body.scrollHeight;
	} else {
		sHeight = document.body.offsetHeight;
	}
	allviewcap.style.height = sHeight;
	allviewcap.style.position = "absolute";
	allviewcap.style.background = "#777";
	allviewcap.style.filter = "alpha(opacity = 20)";
	allviewcap.style.opacity = 0.2;
	allviewcap.style.zIndex = "1";
	if (document.getElementById("mAlertmsgDiv"))
		document.getElementById("mAlertmsgDiv").parentNode.removeChild(document
				.getElementById("mAlertmsgDiv"));
	document.body.appendChild(allviewcap);
	var msWidth = parseInt(msg.length) * 16;
	msWidth = ((!msWidth ? 0 : msWidth) < 200) ? 200 : msWidth;
	var msgView = document.createElement("div");
	msgView.id = "fd";
	msgView.style.filter = "alpha(opacity=100)";
	msgView.style.opacity = "1";
	msgView.style.textAlign = "center";
	msgView.style.width = msWidth + "px";
	msgView.style.height = "80px";
	msgView.style.background = "#EDF1F8";
	msgView.style.border = "1px solid #849BCA";
	msgView.style.marginTop = "1px";
	msgView.style.marginLeft = "1px";
	msgView.style.overflow = "hidden";
	msgView.style.position = "absolute";
	msgView.style.cursor = "move";
	msgView.style.display = "none";
	msgView.style.left = (document.body.offsetWidth - msWidth) / 2;
	msgView.style.top = 200;
	msgView.style.zIndex = "2";
	if (document.getElementById("fd"))
		document.getElementById("fd").parentNode.removeChild(document
				.getElementById("fd"));
	document.body.appendChild(msgView);
	img = img ? img : $dpimgpath + "toolbar/confirm/info.gif";
	var msgTable = "<table style='width:100%;height:100%;'><tr style='background:url("
			+ $dpimgpath
			+ "button_line.gif) 0px -2px;'><td align='left' style='vertical-align:middle;height:18px;'><div style='align:left;font-size:12px;'><strong>信息提示</strong></div></td></tr><tr><td align='center' style='vertical-align:middle;'><div class='content' style='font-size:14px;'><table style='width:100%;'><tr style='align:center;'><td><img src='"
			+ img
			+ "' style='width:24px;height:24px;'/></td><td><p style='float:left;font-size:12px;'>&nbsp;"
			+ msg
			+ "</p></td></tr></table></div></td></tr><tr><td style='vertical-align: bottom;'><div class='content' style='text-align:center;height:20px;'><input type='button' class='center' id='mAlert_EgnButton' value='确定'></input></div></td></tr></table>";
	msgView.innerHTML = msgTable;
	document.getElementById("mAlert_EgnButton").onclick = function() {
		closeed("fd");
		return false;
	};
	var prox;
	var proy;
	var proxc;
	var proyc;
	var show = function(id) {
		clearInterval(prox);
		clearInterval(proy);
		clearInterval(proxc);
		clearInterval(proyc);
		var o = document.getElementById(id);
		o.style.display = "block";
		o.style.width = "1px";
		o.style.height = "1px";
		prox = setInterval(function() {
			openx(o, msWidth);
		}, 10);
	};
	show("fd");
	var openx = function(o, x) {
		var cx = parseInt(o.style.width);
		if (cx < x) {
			o.style.width = (cx + Math.ceil((x - cx) / 5)) + "px";
		} else {
			clearInterval(prox);
			proy = setInterval(function() {
				openy(o, 80);
			}, 10);
		}
	};
	var openy = function(o, y) {
		var cy = parseInt(o.style.height);
		if (cy < y) {
			o.style.height = (cy + Math.ceil((y - cy) / 5)) + "px";
		} else {
			clearInterval(proy);
			document.getElementById("mAlert_EgnButton").focus();
		}
	};
	var closeed = function(id) {
		clearInterval(prox);
		clearInterval(proy);
		clearInterval(proxc);
		clearInterval(proyc);
		var o = document.getElementById(id);
		proyc = setInterval(function() {
			closey(o);
		}, 10);
	};
	var closey = function(o) {
		var cy = parseInt(o.style.height);
		if (cy > 0) {
			o.style.height = (cy - Math.ceil(cy / 5)) + "px";
		} else {
			clearInterval(proyc);
			o.innerHTML = "";
			proxc = setInterval(function() {
				closex(o);
			}, 10);
		}
	};
	var closex = function(o) {
		var cx = parseInt(o.style.width);
		if (cx > 0) {
			o.style.width = (cx - Math.ceil(cx / 5)) + "px";
		} else {
			clearInterval(proxc);
			document.body.removeChild(o);
			document.body.removeChild(allviewcap);
		}
	};
	var od = document.getElementById("fd");
	var dx, dy, mx, my, mouseD;
	var odrag;
	var isIE = document.all ? true : false;
	document.onmousedown = function(e) {
		var e = e ? e : event;
		if (e.button == (document.all ? 1 : 0)) {
			mouseD = true;
		}
	};
	document.onmouseup = function() {
		mouseD = false;
		odrag = "";
		if (isIE) {
			od.releaseCapture();
			od.filters.alpha.opacity = 100;
		} else {
			window.releaseEvents(od.MOUSEMOVE);
			od.style.opacity = 1;
		}
	};
	od.onmousedown = function(e) {
		odrag = this;
		var e = e ? e : event;
		if (e.button == (document.all ? 1 : 0)) {
			mx = e.clientX;
			my = e.clientY;
			od.style.left = od.offsetLeft + "px";
			od.style.top = od.offsetTop + "px";
			if (isIE) {
				od.setCapture();
				od.filters.alpha.opacity = 50;
			} else {
				window.captureEvents(Event.mousemove);
				od.style.opacity = 0.5;
			}
		}
	};
	document.onmousemove = function(e) {
		var e = e ? e : event;
		if (mouseD == true && odrag) {
			var mrx = e.clientX - mx;
			var mry = e.clientY - my;
			od.style.left = parseInt(od.style.left) + mrx + "px";
			od.style.top = parseInt(od.style.top) + mry + "px";
			mx = e.clientX;
			my = e.clientY;
		}
	};
};
function Confirm(msg, okcallFn, cancelcallFn, img) {
	var allviewcap = document.createElement("div");
	allviewcap.setAttribute("id", "mAlertmsgDiv");
	allviewcap.style.left = "0px";
	allviewcap.style.top = "0px";
	allviewcap.style.width = document.body.clientWidth;
	var sHeight = document.body.clientHeight;
	if (window.innerHeight && window.scrollMaxY) {
		sHeight = window.innerHeight + window.scrollMaxY;
	} else if (document.body.scrollHeight > document.body.offsetHeight) {
		sHeight = document.body.scrollHeight;
	} else {
		sHeight = document.body.offsetHeight;
	}
	allviewcap.style.height = sHeight;
	allviewcap.style.position = "absolute";
	allviewcap.style.background = "#777";
	allviewcap.style.filter = "alpha(opacity = 20)";
	allviewcap.style.opacity = 0.2;
	allviewcap.style.zIndex = "1";
	if (document.getElementById("mAlertmsgDiv"))
		document.getElementById("mAlertmsgDiv").parentNode.removeChild(document
				.getElementById("mAlertmsgDiv"));
	document.body.appendChild(allviewcap);
	var msWidth = parseInt(msg.length) * 16;
	msWidth = (msWidth < 200) ? 200 : msWidth;
	var msgView = document.createElement("div");
	msgView.id = "fd_Confirm";
	msgView.style.filter = "alpha(opacity=100)";
	msgView.style.opacity = "1";
	msgView.style.textAlign = "center";
	msgView.style.width = msWidth + "px";
	msgView.style.height = "80px";
	msgView.style.background = "#EDF1F8";
	msgView.style.border = "1px solid #849BCA";
	msgView.style.marginTop = "1px";
	msgView.style.marginLeft = "1px";
	msgView.style.overflow = "hidden";
	msgView.style.position = "absolute";
	msgView.style.cursor = "move";
	msgView.style.left = (document.body.offsetWidth - msWidth) / 2;
	msgView.style.top = 200;
	msgView.style.zIndex = "2";
	if (document.getElementById("fd"))
		document.getElementById("fd").parentNode.removeChild(document
				.getElementById("fd"));
	document.body.appendChild(msgView);
	img = img ? img : $dpimgpath + "toolbar/confirm/ask.gif";
	var msgTable = "<table style='width:100%;height:100%;'><tr style='background:url("
			+ $dpimgpath
			+ "button_line.gif) 0px -2px;'><td align='left' style='vertical-align:middle;height:18px;float:left;'><table style='width:100%;'><tr><td><div style='align:left;font-size:12px;float:left;'><strong>选择提示</strong></div></td><td style='width:20px;'><div style='align:right;font-size:12px;float:left;'><input type='button' id='Confirm_closeBtn' value='×' style='height:18px;align:right;'></input></div></td></tr></table></td></tr><tr><td align='center' style='vertical-align:middle;'><div class='content' style='font-size:14px;'><table style='width:100%;'><tr style='align:center;'><td><img src='"
			+ img
			+ "' style='width:24px;height:24px;'/></td><td><p style='float:left;font-size:14px;'>&nbsp;"
			+ msg
			+ "</p></td></tr></table></div></td></tr><tr><td style='vertical-align: bottom;'><div class='content' style='text-align:center;height:20px;'><input type='button' class='center' id='Confirm_EgnButton' value='确定'></input><input type='button' class='center' id='Confirm_ConcelButton' value='取消'></input></div></td></tr></table>";
	msgView.innerHTML = msgTable;
	var closeed = function() {
		document.body.removeChild(document.getElementById("mAlertmsgDiv"));
		document.body.removeChild(document.getElementById("fd_Confirm"));
	};
	document.getElementById("Confirm_EgnButton").onclick = function() {
		closeed();
		if (okcallFn && typeof (okcallFn) == "function") {
			okcallFn();
		} else if (typeof (okcallFn) == "string") {
			var sFN = eval(okcallFn);
			try {
				sFN();
			} catch (e) {
			}
		}
		return true;
	};
	document.getElementById("Confirm_ConcelButton").onclick = function() {
		closeed();
		if (cancelcallFn && typeof (cancelcallFn) == "function") {
			cancelcallFn();
		} else if (typeof (cancelcallFn) == "string") {
			var sFN = eval(cancelcallFn);
			try {
				sFN();
			} catch (e) {
			}
		}
		return false;
	};
	document.getElementById("Confirm_closeBtn").onclick = function() {
		closeed();
		return false;
	};
	document.getElementById("Confirm_EgnButton").focus();
	var od = document.getElementById("fd_Confirm");
	var dx, dy, mx, my, mouseD;
	var odrag;
	var isIE = document.all ? true : false;
	document.onmousedown = function(e) {
		var e = e ? e : event;
		if (e.button == (document.all ? 1 : 0)) {
			mouseD = true;
		}
	};
	document.onmouseup = function() {
		mouseD = false;
		odrag = "";
		if (isIE) {
			od.releaseCapture();
			od.filters.alpha.opacity = 100;
		} else {
			window.releaseEvents(od.MOUSEMOVE);
			od.style.opacity = 1;
		}
	};
	od.onmousedown = function(e) {
		odrag = this;
		var e = e ? e : event;
		if (e.button == (document.all ? 1 : 0)) {
			mx = e.clientX;
			my = e.clientY;
			od.style.left = od.offsetLeft + "px";
			od.style.top = od.offsetTop + "px";
			if (isIE) {
				od.setCapture();
				od.filters.alpha.opacity = 50;
			} else {
				window.captureEvents(Event.mousemove);
				od.style.opacity = 0.5;
			}
		}
	};
	document.onmousemove = function(e) {
		var e = e ? e : event;
		if (mouseD == true && odrag) {
			var mrx = e.clientX - mx;
			var mry = e.clientY - my;
			od.style.left = parseInt(od.style.left) + mrx + "px";
			od.style.top = parseInt(od.style.top) + mry + "px";
			mx = e.clientX;
			my = e.clientY;
		}
	};
}
justep.yn.isHaveAuthorization = function(url) {
	var orgFID = justep.yn.Context.getCurrentPersonFID();
	var param = new justep.yn.RequestParam();
	param.set("orgFID", orgFID);
	param.set("url", url);
	var callBackfn = function(r) {
		if (r.data.flag == "false") {
			alert(r.data.message);
		} else {
			if (r.data.data == "true")
				return true;
			else
				return false;
		}
	};
	var r = justep.yn.XMLHttpRequest("HaveFunctionAuthority", param, "get",
			false, callBackfn);
	if (r.data.flag == "false") {
		alert(r.data.message);
	} else {
		if (r.data.data == "true")
			return true;
		else
			return false;
	}
};
justep.yn.isIE6 = function() {
	if (window.ActiveXObject) {
		var ua = navigator.userAgent.toLowerCase();
		var ie = ua.match(/msie ([\d.]+)/)[1];
		if (ie == 6.0) {
			return true;
		}
	}
	return false;
};
if (!JSON)
	var JSON = {};
JSON.parse = function(jsonStr) {
	try {
		return eval("(" + jsonStr + ")");
	} catch (e) {
		alert("JSON.parse: " + e);
	}
};
String.prototype.toJSON = function() {
	try {
		return eval("(" + this + ")");
	} catch (e) {
		alert("toJSON: " + e);
	}
};
JSON.toString = function(jsonObj) {
	if (jsonObj.toString().indexOf(",") < 0) {
		var str = "{";
		for ( var k in jsonObj) {
			str += ",\"" + k + "\":\"" + jsonObj[k] + "\"";
		}
		str += "}";
		str = str.replace(",", "");
		return str;
	} else {
		var str = "[";
		for (var i = 0; i < jsonObj.length; i++) {
			if (i > 0)
				str += ",";
			var ostr = "{";
			for ( var k in jsonObj[i]) {
				ostr += ",\"" + k + "\":\"" + jsonObj[i][k] + "\"";
			}
			ostr += "}";
			ostr = ostr.replace(",", "");
			str += ostr;
		}
		str += "]";
		return str;
	}
};
justep.yn.showModelState = function(state) {
	if (parent == window)
		return;
	justep.yn.showSate(state);
	if (state) {
		var sHeight;
		if (!document.body) {
			return;
		}
		if (window.innerHeight && window.scrollMaxY) {
			sHeight = window.innerHeight + window.scrollMaxY;
		} else if (document.body.scrollHeight > document.body.offsetHeight) {
			sHeight = document.body.scrollHeight;
		} else {
			sHeight = document.body.offsetHeight;
		}
		var mod_allv = document.createElement("div");
		mod_allv.setAttribute("id", "mod_allv");
		mod_allv.style.left = "0px";
		mod_allv.style.top = "0px";
		mod_allv.style.width = document.body.clientWidth;
		mod_allv.style.height = sHeight;
		mod_allv.style.position = "absolute";
		mod_allv.style.background = "#eee";
		mod_allv.style.filter = "alpha(opacity=30)";
		mod_allv.style.opacity = 0.3;
		mod_allv.style.zIndex = "1";
		document.body.appendChild(mod_allv);
	} else {
		try {
			document.body.removeChild(document.getElementById("mod_allv"));
		} catch (e) {
		}
	}
};
justep.yn.DateAdd = function(strInterval, date, Number) {
	var dtTmp = date;
	switch (strInterval) {
	case 's':
		return new Date(Date.parse(dtTmp) + (1000 * Number));
	case 'n':
		return new Date(Date.parse(dtTmp) + (60000 * Number));
	case 'h':
		return new Date(Date.parse(dtTmp) + (3600000 * Number));
	case 'd':
		return new Date(Date.parse(dtTmp) + (86400000 * Number));
	case 'w':
		return new Date(Date.parse(dtTmp) + ((86400000 * 7) * Number));
	case 'q':
		return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number * 3,
				dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp
						.getSeconds());
	case 'm':
		return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number, dtTmp
				.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp
				.getSeconds());
	case 'y':
		return new Date((dtTmp.getFullYear() + Number), dtTmp.getMonth(), dtTmp
				.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp
				.getSeconds());
	}
};
if (topparent == window) {
	if (!$) {
		$ = {};
	}
	if (!$.jpolite) {
		$.jpolite = {};
	}
	if (!$.jpolite.clientInfo) {
		$.jpolite.clientInfo = {
			username : "",
			businessid : "",
			uiserverremoteurl : "",
			locale : "",
			orgfullname : "",
			personid : "",
			personname : "",
			personcode : "",
			positionid : "",
			positioncode : "",
			positionname : "",
			positionfid : "",
			positionfcode : "",
			positionfname : "",
			deptid : "",
			deptcode : "",
			deptname : "",
			deptfid : "",
			deptfcode : "",
			deptfname : "",
			orgid : "",
			orgcode : "",
			orgname : "",
			orgfid : "",
			orgfcode : "",
			orgfname : "",
			ognid : "",
			ogncode : "",
			ognname : "",
			ognfid : "",
			ognfcode : "",
			ognfname : "",
			personfid : "",
			personfcode : "",
			personfname : "",
			init : function() {
				var param = new justep.yn.RequestParam();
				justep.yn
						.XMLHttpRequest(
								"initPortalInfo",
								param,
								"post",
								true,
								function(data) {
									if (!data || data == "")
										return;
									var datainfo = window["eval"]("(" + data
											+ ")");
									$.jpolite.clientInfo.orgfullname = datainfo.orgFullName;
									$.jpolite.clientInfo.personid = datainfo.personid;
									$.jpolite.clientInfo.personname = datainfo.personName;
									$.jpolite.clientInfo.username = datainfo.username;
									$.jpolite.clientInfo.personcode = datainfo.personcode;
									$.jpolite.clientInfo.positionid = datainfo.positionid;
									$.jpolite.clientInfo.positioncode = datainfo.positioncode;
									$.jpolite.clientInfo.positionname = datainfo.positionname;
									$.jpolite.clientInfo.positionfid = datainfo.positionfid;
									$.jpolite.clientInfo.positionfcode = datainfo.positionfcode;
									$.jpolite.clientInfo.positionfname = datainfo.positionfname;
									$.jpolite.clientInfo.deptid = datainfo.deptid;
									$.jpolite.clientInfo.deptcode = datainfo.deptcode;
									$.jpolite.clientInfo.deptname = datainfo.deptname;
									$.jpolite.clientInfo.deptfid = datainfo.deptfid;
									$.jpolite.clientInfo.deptfcode = datainfo.deptfcode;
									$.jpolite.clientInfo.deptfname = datainfo.deptfname;
									$.jpolite.clientInfo.orgid = datainfo.orgid;
									$.jpolite.clientInfo.orgcode = datainfo.orgcode;
									$.jpolite.clientInfo.orgname = datainfo.orgname;
									$.jpolite.clientInfo.orgfid = datainfo.orgfid;
									$.jpolite.clientInfo.orgfcode = datainfo.orgfcode;
									$.jpolite.clientInfo.orgfname = datainfo.orgfname;
									$.jpolite.clientInfo.ognid = datainfo.ognid;
									$.jpolite.clientInfo.ogncode = datainfo.ogncode;
									$.jpolite.clientInfo.ognname = datainfo.ognname;
									$.jpolite.clientInfo.ognfid = datainfo.ognfid;
									$.jpolite.clientInfo.ognfcode = datainfo.ognfcode;
									$.jpolite.clientInfo.ognfname = datainfo.ognfname;
									$.jpolite.clientInfo.personfid = datainfo.personfid;
									$.jpolite.clientInfo.personfcode = datainfo.personfcode;
									$.jpolite.clientInfo.personfname = datainfo.personfname;
									$.jpolite.clientInfo.businessid = datainfo.businessid;
									$.jpolite.clientInfo.locale = datainfo.locale;
									$.jpolite.clientInfo.uiserverremoteurl = datainfo.uiserverremoteurl;
								});
			}
		};
	}
	$.jpolite.clientInfo.init();
}
function getNevType() {
	try {
		if ((navigator.userAgent.indexOf('MSIE') >= 0)
				&& (navigator.userAgent.indexOf('Opera') < 0)) {
			return 'IE';
		} else if (navigator.userAgent.indexOf('Firefox') >= 0) {
			return 'Firefox';
		} else if (navigator.userAgent.indexOf("Opera") >= 0) {
			return 'Opera';
		} else if (navigator.userAgent.indexOf("Camino") >= 0) {
			return 'Camino';
		} else {
			return 'Other';
		}
	} catch (e) {
		return false;
	}
}
function getIEVersion() {
	if (navigator.appVersion.match(/6./i) == "6.") {
		return "IE6";
	} else if (navigator.appVersion.match(/7./i) == "7.") {
		return "IE7";
	} else if (navigator.appVersion.match(/8./i) == "8.") {
		return "IE8";
	} else if (navigator.appVersion.match(/9./i) == "9.") {
		return "IE9";
	}
	return "Other";
}

if (topparent != window) {
	// if (!checkPathisHave("/JBIZ/comon/js/jquery/jquery-1.4.4.min.js")
	// && !checkPathisHave($dpjspath + "jquery/jquery-1.4.4.min.js")) {
	// createJSSheet($dpjspath + "jquery/jquery-1.4.4.min.js");
	// }
}

justep.yn.requerat = function() {
	$("[rel='remind']").each(function() {
		var val = $(this).val();
		if (!val || val == "") {
			$(this).val($(this).attr("rel-value"));
			$(this).css("color", "blue");
		}
		$(this).unbind("focus");
		$(this).bind('focus', function() {
			var val = $(this).val();
			if (val && val == $(this).attr("rel-value")) {
				$(this).val("");
			}
		});
		$(this).unbind("blur");
		$(this).bind('blur', function() {
			var val = $(this).val();
			if (!val || val == "") {
				$(this).val($(this).attr("rel-value"));
				$(this).css("color", "blue");
			} else {
				$(this).css("color", "#000");
			}
		});
	});
};

$(document).ready(function() {
	justep.yn.requerat();
});

/*
 * @folder 目录 如："/行政审批/会议纪要" @title 标题 @table 业务数据表 @billid 单据主键 @surl 查看页面地址
 */
justep.yn.docpigeonhole = function(folder, title, table, billid, surl) {
	var param = new justep.yn.RequestParam();
	param.set("folder", folder);
	param.set("title", title);
	param.set("table", table);
	param.set("billid", billid);
	param.set("surl", surl);
	justep.yn.XMLHttpRequest("docpigeonholeAction", param, "POST", false);
};

/*
 * @title 标题 @table 业务数据表 @billid 单据主键 @surl 查看页面地址
 */
justep.yn.Mydocpigeonhole = function(title, table, billid, surl) {
	var newid = new UUID().toString();
	var filename = "<a href=\"javascript:justep.yn.docOpendeatail(''" + title
			+ "'',''" + surl + "'',''" + billid + "'')\">" + title + "</a>";
	justep.yn.portal.dailog
			.openDailog(
					"选择归档目录",
					"/SA/docnode/dialog/myFolderSelect.html",
					300,
					400,
					function(rdata) {
						var sql = "insert into PERSONAL_FILE(SID,SFILENAME,SCREATORID,SCREATORNAME,SMASTERID,VERSION)"
								+ " select '"
								+ newid
								+ "','"
								+ filename
								+ "','"
								+ justep.yn.Context.getCurrentPersonID()
								+ "',"
								+ "'"
								+ justep.yn.Context.getCurrentPersonName()
								+ "','" + rdata + "',0 from dual";
						justep.yn.sqlUpdateAction("system", sql, function(re) {
							if (re.flag == "false") {
								alert(re.message);
							} else {
								alert("归档成功!");
							}
						}, true);
					});
};

/*
 * @title 标题 @table 业务数据表 @billid 单据主键 @surl 查看页面地址
 */
justep.yn.ognDocpigeonhole = function(title, table, billid, surl) {
	var selsql = "select * from oa_dz_filecabinet where FMAINID='" + billid
			+ "'";
	var result = justep.yn.sqlQueryActionforJson("oa", selsql);
	if (result.data.length > 0) {
		if (confirm("文件已归过档,是否更换文件夹")) {
			justep.yn.portal.dailog.openDailog("选择归档目录",
					"/SA/docnode/dialog/ognFolderSelect.html", 300, 400,
					function(rdata) {
						var sql = "update oa_dz_filecabinet set FMASTERID='"
								+ rdata + "' where FMAINID='" + billid + "'";
						justep.yn.sqlUpdateAction("oa", sql);
						alert("归档成功!");
					});
		}
	} else {
		var newid = new UUID().toString();
		var filename = "<a href=\"javascript:justep.yn.docOpendeatail(''"
				+ title + "'',''" + surl + "'',''" + billid + "'')\">" + title
				+ "</a>";
		justep.yn.portal.dailog
				.openDailog(
						"选择归档目录",
						"/SA/docnode/dialog/ognFolderSelect.html",
						300,
						400,
						function(rdata) {
							var sql = "insert into oa_dz_filecabinet(FID,FFILENAME,FCREATORID,FCREATORNAME,FMASTERID,FMAINID,VERSION)"
									+ " select '"
									+ newid
									+ "','"
									+ filename
									+ "','"
									+ justep.yn.Context.getCurrentPersonID()
									+ "',"
									+ "'"
									+ justep.yn.Context.getCurrentPersonName()
									+ "','"
									+ rdata
									+ "','"
									+ billid
									+ "',0 from dual";
							justep.yn.sqlUpdateAction("oa", sql, function(re) {
								if (re.flag == "false") {
									alert(re.message);
								} else {
									alert("归档成功!");
								}
							}, true);
						});
	}
};

justep.yn.docOpendeatail = function(name, surl, billid) {
	var url = "/JBIZ" + surl + "?sData1=" + billid;
	justep.yn.portal.openWindow(name, url);
};

// 填写审批意见
justep.yn.writeOpinion = function(view) {
	var taptt = justep.yn.RequestURLParam.getParam("activity-pattern");
	var isTasksub = (taptt=="detail");
	if(isTasksub){
		alert("已办任务不能再填写意见!");
		return;
	}
	var sData1 = justep.yn.RequestURLParam.getParam("sData1");
	var flowID = justep.yn.RequestURLParam.getParam("flowID");
	var taskID = justep.yn.RequestURLParam.getParam("taskID");
	var url = "/flw/flwcommo/flowDialog/processAudit.html";
	url += "?flowID=" + flowID;
	url += "&taskID=" + taskID;
	url += "&sData1=" + sData1;
	url += "&opviewID=" + view;
	justep.yn.portal.dailog.openDailog("填写意见", url, 660, 400, function(r) {
		// 参数:显示div ID，业务ID
		justep.yn.loadOption(view, sData1);
	});
};

// 加载审核意见
justep.yn.loadOption = function(viewID, sData1) {
	var sql = "select t.FAGREETEXT,FCREATETIME,FCREATEPERID,FCREATEPERNAME from OA_FLOWRECORD t where  FBILLID='"
			+ sData1 + "' and t.FOPVIEWID = '" + viewID + "'";
	var re = justep.yn.sqlQueryActionforJson("oa", sql, null, false);
	var viewHTml = "";
	var redata = re.data;
	viewHTml += "<table style='width:100%;' border='0'>";
	for (var i = 0; i < redata.length; i++) {
		var opinion = redata[i].FAGREETEXT;
		var writeDate = redata[i].FCREATETIME;
		var personid = redata[i].FCREATEPERID;
		var personname = redata[i].FCREATEPERNAME;
		if (writeDate && writeDate != "") {
			writeDate = justep.yn.System.Date.strToDate(writeDate);
			writeDate = writeDate.format("yyyy-MM-dd HH:mm");
		}
		var hdws = viewID + "_handwrite";
		var pcre = justep.yn.sqlQueryAction("system",
				"select SID from SA_HANDWR_SIGNATURE where SPERSONID='"
						+ personid + "'", null, false);
		var picID = "";
		if (pcre.getCount() > 0) {
			picID = pcre.getValueByName("SID");
		}
		var url = "/JBIZ/comon/picCompant/Pic-read.jsp?dbkey=system"
				+ "&tablename=SA_HANDWR_SIGNATURE&cellname=SHSPIC&fID=" + picID
				+ "&Temp=" + new UUID().toString();
		var image = "<img src='" + url
				+ "' style='width:100px;;height:30px;'></img>";
		var writpsm = personname;
		if (picID && picID != "") {
			writpsm = image;
		}
		viewHTml += "<tr><td style='font-size:14px;text-decoration:underline; border:0px none;'>"
				+ opinion + "</td>";
		viewHTml += "<td style='width:100px; text-decoration:underline; border:0px none;'><div id='"
				+ hdws
				+ "' style='width:100px;height:30px; font-size:14px;text-decoration:underline;'>"
				+ writpsm
				+ "</div>"
				+ "</td><td style='font-size:12px;width:100px;text-decoration:underline; border:0px none;'>"
				+ writeDate + "</td></tr>";
	}
	viewHTml += "<table>";
	$("#" + viewID).html(viewHTml);
};

// 禁止回退键“返回”页面
function disbackspace() {
	$("body")
			.bind(
					"keydown",
					function(event) {
						event = event || window.event;
						var elem = event.srcElement ? event.srcElement
								: event.target;
						if (event.keyCode == 8) {
							var name = elem.nodeName;
							if (name != 'INPUT' && name != 'TEXTAREA') {
								if (event.preventDefault) {
									event.preventDefault();
								} else {
									event.returnValue = false;
								}
								return false;
							}
							var type_e = elem.type.toUpperCase();
							if (name == 'INPUT'
									&& (type_e != 'TEXT'
											&& type_e != 'TEXTAREA'
											&& type_e != 'PASSWORD' && type_e != 'FILE')) {
								if (event.preventDefault) {
									event.preventDefault();
								} else {
									event.returnValue = false;
								}
								return false;
							}
							if (name == 'INPUT'
									&& (elem.readOnly == true || elem.disabled == true)) {
								if (event.preventDefault) {
									event.preventDefault();
								} else {
									event.returnValue = false;
								}
								return false;
							}
						}
					});
}

$(document).ready(function() {
	disbackspace();
});