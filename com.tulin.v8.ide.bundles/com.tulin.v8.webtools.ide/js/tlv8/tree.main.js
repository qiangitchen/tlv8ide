/*-------------------------------------------------------------------------------*\
|  Subject:    树形结构 主要程序 
|  Author:     陈乾
 *  CopyRight:  www.tlv8.com
|  Created:    2014-07-18
|  Version:    v2.3
\*-------------------------------------------------------------------------------*/
/**
 * @param div
 *            树形所属的层
 * @param treeQueryAction
 *            (div, treeQueryAction, img, data, master)查询动作
 *            参数：div:(Element)div,treeQueryAction:(String)查询动作,
 * @param img:(Sting)图标类型,data:(tlv8.Data)
 * @param data
 * @param master:(boolean)是否多选
 * @param calback
 *            回调函数
 */
tlv8.createTree = function(div, treeQueryAction, img, data, master,
		calback) {
	var tree = {
		treeViewRoot : null,
		CurrentRowId : "",
		CurrentValue : "",
		CurrentparentID : "",
		onselected : function(obj) {
			var onS = div.getAttribute("onselect");
			var onSfunc = eval(onS);
			if (typeof (onSfunc) == "function") {
				onSfunc(tree);
			}
		},
		rowdbclick : function(obj) {
			var onS = div.getAttribute("onTreedbclick");
			var onSfunc = eval(onS);
			if (typeof (onSfunc) == "function") {
				onSfunc(tree);
			}
		},
		treeAclass : null,
		treeSelect : function(event) {
			var obj = event.srcElement ? event.srcElement : event.target;
			if (tree.CurrentRowId == obj.id) {
				tree.onselected(obj);
				return;
			}
			tree.CurrentRowId = obj.id;
			tree.CurrentValue = obj.innerHTML;
			tree.CurrentparentID = obj.parentID;
			tree.onselected(obj);
			obj.tmpClass = obj.className;
			obj.className = "se";
			if (tree.treeAclass
					&& tree.treeAclass.getAttribute("id") == obj
							.getAttribute("parentID")) {
				tree.treeAclass.className = "";// obj.tmpClass;
			} else if (tree.treeAclass) {
				tree.treeAclass.className = obj.tmpClass;
			}
			tree.treeAclass = obj;
		},
		refreshData : function() {
			var queryActionandParam = "";
			if (master == true)
				queryActionandParam = (treeQueryAction.indexOf("?") > 0) ? treeQueryAction
						+ "&master=true"
						: treeQueryAction + "?master=true";
			else
				queryActionandParam = (treeQueryAction.indexOf("?") > 0) ? treeQueryAction
						+ "&master=false"
						: treeQueryAction + "?master=false";
			tlv8.Queryaction(queryActionandParam, "POST", initTreeData);
		},
		quickPosition : function(text) {
			var path = text ? text : document.getElementById(div.id
					+ "_tree_quick_position").value;
			path = trim(path);
			if (!path || path == "")
				return;
			var curret_tree = document.getElementById(div.id + "_tree_view");
			var lia = curret_tree.getElementsByTagName("A");
			for (var i = 0; i < lia.length; i++) {
				var liavalue = trim(lia[i].innerHTML);
				// 忽略大小写
				if (liavalue.toUpperCase() == path.toUpperCase()
						|| liavalue.toUpperCase().indexOf(path.toUpperCase()) > -1) {
					tree.quickID = lia[i].id;
					var slPath = tree.getPath(liavalue);
					if (slPath && slPath != "") {
						tree.quickPositionByPath(slPath);
						if (lia[i].className != "se")
							lia[i].click();
						return;
					}
				}
			}
			tlv8.showMessage("未找到相关内容!");
		},
		Path : "",
		quickID : "",
		getPath : function(text) {
			var curret_tree = document.getElementById(div.id + "_tree_view");
			var lia = curret_tree.getElementsByTagName("A");
			var cID = "";
			for (var i = 0; i < lia.length; i++) {
				var liavalue = trim(lia[i].innerHTML);
				var liaID = lia[i].id;
				if (liavalue.toUpperCase() == text.toUpperCase()
						|| liaID == text.toUpperCase()) {
					cID = lia[i].getAttribute("parentID");
					if (cID && cID != "" && cID != "null")
						tree.Path = cID + "/" + tree.Path;
				}
			}
			for (var i = 0; i < lia.length; i++) {
				if (lia[i].id == cID) {
					var liavalue = trim(lia[i].innerHTML);
					tree.getPath(liavalue);
				}
			}
			return tree.Path + tree.quickID;
		},
		quickPositionByPath : function(path) {
			var sPath = path.split("/");
			for (var i = 0; i < sPath.length; i++) {
				var curret_tree = document
						.getElementById(div.id + "_tree_view");
				if (!sPath[i])
					return;
				var treeItem = document.getElementById(sPath[i]);
				if (treeItem.tagName == "A") {
					if (treeItem.className != "se")
						sn.openTarget(treeItem);
				}
			}
		},
		childNode : new Array(),
		getCildNode : function(cuID) {
			var curret_tree = document.getElementById(div.id + "_tree_view");
			var Elm = curret_tree.getElementsByTagName("A");
			for (var i = 0; i < Elm.length; i++) {
				if (Elm[i].getAttribute("parentID") == cuID) {
					tree.childNode.push(Elm[i].id);
				}
			}
			return tree.childNode;
		},
		checkedID : new Array(),
		reMoveCheckedID : function(rId) {
			for ( var i in tree.checkedID) {
				if (tree.checkedID[i] == rId) {
					tree.checkedID.splice(i, 1);
				}
			}
		},
		checkedValue : "",
		treeMasterCheck : function(event) {
			var scOL = event.srcElement ? event.srcElement : event.target;
			var scID = scOL.id.replace("_check", "");
			if (scOL.checked) {
				tree.checkedID.push(scID);
				if (!tlv8.String.isin(tree.checkedValue, document
						.getElementById(scID).innerHTML))
					tree.checkedValue += ","
							+ document.getElementById(scID).innerHTML;
			} else {
				tree.reMoveCheckedID(scID);
				if (tlv8.String.isin(tree.checkedValue, ","
						+ document.getElementById(scID).innerHTML))
					tree.checkedValue = reMoveStr(tree.checkedValue, ","
							+ document.getElementById(scID).innerHTML);
				else
					tree.checkedValue = reMoveStr(tree.checkedValue, document
							.getElementById(scID).innerHTML);
			}
			tree.childNode = new Array();
			var cilList = tree.getCildNode(scID);
			for (var i = 0; i < cilList.length; i++) {
				document.getElementById(cilList[i] + "_check").checked = scOL.checked;
				tree.checkedID.push(cilList[i]);
				if (document.getElementById(cilList[i]).tagName == "A"
						&& scOL.checked)
					if (!tlv8.String.isin(tree.checkedValue, document
							.getElementById(cilList[i]).innerHTML))
						tree.checkedValue += ","
								+ document.getElementById(cilList[i]).innerHTML;
				if (!scOL.checked)
					tree.checkedValue = reMoveStr(tree.checkedValue, ","
							+ document.getElementById(cilList[i]).innerHTML);
				if (tree.getCildNode(cilList[i]).length > 0) {
					tree.treemasterCheck(cilList[i], scOL.checked);
				}
			}
			tree.checkedValue = replaceFirst(tree.checkedValue, ",", "");
			tree.onchecked();
		},
		treemasterCheck : function(scID, checked) {
			var cilList = tree.getCildNode(scID);
			for (var i = 0; i < cilList.length; i++) {
				document.getElementById(cilList[i] + "_check").checked = checked;
				tree.checkedID.push(cilList[i]);
				if (document.getElementById(cilList[i]).tagName == "A"
						&& checked)
					if (!tlv8.String.isin(tree.checkedValue, document
							.getElementById(cilList[i]).innerHTML))
						tree.checkedValue += ","
								+ document.getElementById(cilList[i]).innerHTML;
				if (!checked)
					tree.checkedValue = reMoveStr(tree.checkedValue, ","
							+ document.getElementById(cilList[i]).innerHTML);
			}
		},
		onchecked : function() {
			var onS = div.getAttribute("onchecked");
			var onSfunc = eval(onS);
			if (typeof (onSfunc) == "function") {
				onSfunc(tree);
			}
		}
	};
	if (document.getElementById("tree")) {
		// TODO:同一环境不允许出现多个树
		document.getElementById("tree").parentNode.removeChild(document
				.getElementById("tree"));
	}
	/*
	 * if (div.tree) return;
	 */
	// div.style.overflow = "auto";
	div.tree = tree;
	this.tree = tree;
	if ($('div').layout) {
		var treeTable = '<div class="easyui-layout" fit="true" border="false" id="'
				+ div.id + '_tree">';
		treeTable += '<div data-options="region:\'north\',border:false" style="height:25px;overflow:hidden;">';
		treeTable += "<table><tr><td><input id='"
				+ div.id
				+ "_tree_quick_position' type='text' style='width:100%'></input></td>";
		treeTable += "<td style='width:2px'/><td align='left'>";
		treeTable += "<a id='"
				+ div.id
				+ "_quick' href='javascript:void(0)' class='toobar_item' style='width:20px;height:20px;'><img src='"
				+ $dpimgpath
				+ "toolbar/search.gif' title='查询' style='font-size:12px'/></a></td></tr></table>";
		treeTable += '</div><div data-options="region:\'center\',border:false">';
		treeTable += "<div id='" + div.id + "_tree_view'></div>";
		treeTable += "</div></div>";
		div.innerHTML = treeTable;
	} else {
		var treeTable = "<table id='"
				+ div.id
				+ "_tree' style='border-collapse: collapse;table-layout:fixed; width:100%;height:100%'>";
		treeTable += "<tr><td width='140' height='25px'><input id='"
				+ div.id
				+ "_tree_quick_position' type='text' style='width:100%'></input></td><td style='width:2px'/><td align='left'>"
				+ "<a id='"
				+ div.id
				+ "_quick' href='javascript:void(0)' class='toobar_item' style='width:20px;height:20px;'><img src='"
				+ $dpimgpath
				+ "toolbar/search.gif' title='查询' style='font-size:12px'/></a></td></tr>";
		treeTable += "<tr><td valign='top' colspan='3'><div id='" + div.id
				+ "_tree_view'></div></td></tr></table>";
		div.innerHTML = treeTable;
	}
	document.getElementById(div.id + "_quick").onclick = function(event) {
		event = event ? event : (window.event ? window.event : null);
		tree.quickPosition(document.getElementById(div.id
				+ "_tree_quick_position").value);
	};
	document.getElementById(div.id + "_tree").onkeyup = function(event) {
		event = event ? event : (window.event ? window.event : null);
		if (event.keyCode == 13) {
			tree.quickPosition(document.getElementById(div.id
					+ "_tree_quick_position").value);
		}
	};
	var DOMhelp = {
		debugWindowId : 'DOMhelpdebug',
		init : function() {
			if (!document.getElementById || !document.createTextNode) {
				return;
			}
		},
		lastSibling : function(node) {
			var tempObj = node.parentNode.lastChild;
			while (tempObj.nodeType != 1 && tempObj.previousSibling != null) {
				tempObj = tempObj.previousSibling;
			}
			return (tempObj.nodeType == 1) ? tempObj : false;
		},
		firstSibling : function(node) {
			var tempObj = node.parentNode.firstChild;
			while (tempObj.nodeType != 1 && tempObj.nextSibling != null) {
				tempObj = tempObj.nextSibling;
			}
			return (tempObj.nodeType == 1) ? tempObj : false;
		},
		getText : function(node) {
			if (!node.hasChildNodes()) {
				return false;
			}
			var reg = /^\s+$/;
			var tempObj = node.firstChild;
			while (tempObj.nodeType != 3 && tempObj.nextSibling != null
					|| reg.test(tempObj.nodeValue)) {
				tempObj = tempObj.nextSibling;
			}
			return tempObj.nodeType == 3 ? tempObj.nodeValue : false;
		},
		setText : function(node, txt) {
			if (!node.hasChildNodes()) {
				return false;
			}
			var reg = /^\s+$/;
			var tempObj = node.firstChild;
			while (tempObj.nodeType != 3 && tempObj.nextSibling != null
					|| reg.test(tempObj.nodeValue)) {
				tempObj = tempObj.nextSibling;
			}
			if (tempObj.nodeType == 3) {
				tempObj.nodeValue = txt;
			} else {
				return false;
			}
		},
		createLink : function(to, txt) {
			var tempObj = document.createElement('a');
			tempObj.appendChild(document.createTextNode(txt));
			tempObj.setAttribute('href', to);
			return tempObj;
		},
		createTextElm : function(elm, txt) {
			var tempObj = document.createElement(elm);
			tempObj.appendChild(document.createTextNode(txt));
			return tempObj;
		},
		closestSibling : function(node, direction) {
			var tempObj;
			if (direction == -1 && node.previousSibling != null) {
				tempObj = node.previousSibling;
				while (tempObj.nodeType != 1 && tempObj.previousSibling != null) {
					tempObj = tempObj.previousSibling;
				}
			} else if (direction == 1 && node.nextSibling != null) {
				tempObj = node.nextSibling;
				while (tempObj.nodeType != 1 && tempObj.nextSibling != null) {
					tempObj = tempObj.nextSibling;
				}
			}
			return tempObj.nodeType == 1 ? tempObj : false;
		},
		initDebug : function() {
			if (DOMhelp.debug) {
				DOMhelp.stopDebug();
			}
			DOMhelp.debug = document.createElement('div');
			DOMhelp.debug.setAttribute('id', DOMhelp.debugWindowId);
			document.body.insertBefore(DOMhelp.debug, document.body.firstChild);
		},
		setDebug : function(bug) {
			if (!DOMhelp.debug) {
				DOMhelp.initDebug();
			}
			DOMhelp.debug.innerHTML += bug + '\n';
		},
		stopDebug : function() {
			if (DOMhelp.debug) {
				DOMhelp.debug.parentNode.removeChild(DOMhelp.debug);
				DOMhelp.debug = null;
			}
		},
		getKey : function(e) {
			if (window.event) {
				var key = window.event.keyCode;
			} else if (e) {
				var key = e.keyCode;
			}
			return key;
		},
		/* helper methods */
		getTarget : function(e) {
			var target = e.target ? e.target : e ? e
					: window.event ? window.event.srcElement : null;
			if (target.tagName != "A") {
				target = window.event.srcElement;
			}
			if (!target) {
				return false;
			}
			while (target.nodeType != 1
					&& target.nodeName.toLowerCase() != 'body') {
				target = target.parentNode;
			}
			return target;
		},
		stopBubble : function(e) {
			if (window.event && window.event.cancelBubble) {
				window.event.cancelBubble = true;
			}
			if (e && e.stopPropagation) {
				e.stopPropagation();
			}
		},
		stopDefault : function(e) {
			if (window.event && window.event.returnValue) {
				window.event.returnValue = false;
			}
			if (e && e.preventDefault) {
				e.preventDefault();
			}
		},
		cancelClick : function(e) {
			if (window.event) {
				window.event.cancelBubble = true;
				window.event.returnValue = false;
			}
			if (e && e.stopPropagation && e.preventDefault) {
				e.stopPropagation();
				e.preventDefault();
			}
		},
		addEvent : function(elm, evType, fn, useCapture) {
			if (elm.addEventListener) {
				elm.addEventListener(evType, fn, useCapture);
				return true;
			} else if (elm.attachEvent) {
				var r = elm.attachEvent('on' + evType, fn);
				return r;
			} else {
				elm['on' + evType] = fn;
			}
		},
		cssjs : function(a, o, c1, c2) {
			switch (a) {
			case 'swap':
				o.className = !DOMhelp.cssjs('check', o, c1) ? o.className
						.replace(c2, c1) : o.className.replace(c1, c2);
				break;
			case 'add':
				if (!DOMhelp.cssjs('check', o, c1)) {
					o.className += o.className ? ' ' + c1 : c1;
				}
				break;
			case 'remove':
				var rep = o.className.match(' ' + c1) ? ' ' + c1 : c1;
				o.className = o.className.replace(rep, '');
				break;
			case 'check':
				var found = false;
				var temparray = o.className.split(' ');
				for (var i = 0; i < temparray.length; i++) {
					if (temparray[i] == c1) {
						found = true;
					}
				}
				return found;
				break;
			}
		},
		safariClickFix : function() {
			return false;
		}
	};
	this.DOMhelp = DOMhelp;
	var sn = {
		dynamicClass : img ? img : 'normal',
		showClass : 'show',
		parentClass : 'parent',
		openClass : 'open',
		navID : 'tree',
		init : function() {
			var triggerLink;
			if (!document.getElementById || !document.createTextNode) {
				return;
			}
			var nav = document.getElementById(sn.navID);
			if (!nav) {
				return;
			}
			DOMhelp.cssjs('add', nav, sn.dynamicClass);
			var nested = nav.getElementsByTagName('ul');
			for (var i = 0; i < nested.length; i++) {
				var AtriggerLink = nested[i].parentNode
						.getElementsByTagName('a')[0];
				triggerLink = nested[i].parentNode
						.getElementsByTagName('button')[0];
				if (!triggerLink)
					continue;
				DOMhelp.cssjs('add', triggerLink.parentNode, sn.parentClass);
				DOMhelp.cssjs('add', triggerLink, sn.parentClass);
				DOMhelp.addEvent(triggerLink, 'click', function(e) {
					sn.changeSection(e);
				}, false);
				triggerLink.onclick = DOMhelp.safariClickFix;
				if (nested[i].parentNode.getElementsByTagName('strong').length > 0) {
					DOMhelp.cssjs('add', triggerLink.parentNode, sn.openClass);
					DOMhelp.cssjs('add', triggerLink, sn.openClass);
					DOMhelp.cssjs('add', nested[i], sn.showClass);
				}
			}
			var nestedli = nav.getElementsByTagName('li');
			for (var i = 0; i < nestedli.length; i++) {
				var litriggerLink = nestedli[i].getElementsByTagName('a')[0];
				DOMhelp.cssjs('add', litriggerLink, 'no');
				DOMhelp.addEvent(litriggerLink, 'click', function(event) {
					event = event ? event
							: (window.event ? window.event : null);
					tree.treeSelect(event);
				}, true);
				DOMhelp.addEvent(litriggerLink, 'dbclick', function(event) {
					event = event ? event
							: (window.event ? window.event : null);
					tree.rowdbclick(event);
				}, true);
				var litriggerCheck = nestedli[i].getElementsByTagName('input')[0];
				if (litriggerCheck)
					DOMhelp.addEvent(litriggerCheck, 'click', function(event) {
						event = event ? event : (window.event ? window.event
								: null);
						tree.treeMasterCheck(event);
					}, true);
			}
		},
		changeSection : function(e) {
			e = e ? e : (window.event ? window.event : null);
			var t = DOMhelp.getTarget(e);
			if (!t.parentNode) {
				return;
			}
			var firstList = (t.parentNode.tagName == "UL") ? t.parentNode
					: t.parentNode.getElementsByTagName('ul')[0];
			if (DOMhelp.cssjs('check', firstList, sn.showClass)) {
				DOMhelp.cssjs('remove', firstList, sn.showClass);
				DOMhelp.cssjs('swap', t.parentNode, sn.openClass,
						sn.parentClass);
				DOMhelp.cssjs('swap', t, sn.openClass, sn.parentClass);
			} else {
				DOMhelp.cssjs('add', firstList, sn.showClass);
				DOMhelp.cssjs('swap', t.parentNode, sn.openClass,
						sn.parentClass);
				DOMhelp.cssjs('swap', t, sn.openClass, sn.parentClass);
			}
			try {
				var AtriggerLink = document.getElementById(t.id.substring(0,
						t.id.indexOf("_")));
				DOMhelp.cssjs('remove', AtriggerLink, "no");
			} catch (e) {
			}
			DOMhelp.cancelClick(e);
		},
		openTarget : function(e) {
			var t = DOMhelp.getTarget(e);
			if (!t.parentNode) {
				return;
			}
			var firstList = (t.parentNode.tagName == "UL") ? t.parentNode
					: t.parentNode.getElementsByTagName('ul')[0];
			if (!firstList)
				return;
			DOMhelp.cssjs('add', firstList, sn.showClass);
			DOMhelp.cssjs('swap', t.parentNode, sn.openClass, sn.parentClass);
			DOMhelp.cancelClick(e);
		}
	};
	this.sn = sn;
	var initTreeData = function(r) {
		var msessage = "操作成功!";
		if (r.flag == "true") {
			document.getElementById(div.id + "_tree_view").innerHTML = r.data;
			DOMhelp.init();
			sn.init();
		} else {
			msessage = r.message;
		}
		if (!parent.sAlert)
			sAlert(msessage, 500);// 提示信息
		if (calback) {
			calback();
		}
	};
	if (master == true)
		treeQueryAction += (treeQueryAction.indexOf("?") > 0) ? "&master=true"
				: "?master=true";
	else
		treeQueryAction += (treeQueryAction.indexOf("?") > 0) ? "&master="
				: "?master=";
	tlv8.Queryaction(treeQueryAction, "POST", initTreeData);
	return this;
};

if (!checkPathisHave($dpcsspath + "tree.css"))
	createStyleSheet($dpcsspath + "tree.css");
if (!checkPathisHave($dpcsspath + "toolbar.main.css"))
	createStyleSheet($dpcsspath + "toolbar.main.css");
// ==end>>


/**
*以下为了兼容云捷代码
*/
justep.yn = tlv8;