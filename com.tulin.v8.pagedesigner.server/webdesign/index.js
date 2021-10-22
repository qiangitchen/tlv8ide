function changlayoutsize() {
	$("body").height($(document).height());
}
$(document).ready(function() {
	var content = getParam("content");
	// 加载编辑页面
	$("#mainiframe").attr("src", content);
	changlayoutsize();
	$(window).resize(function() {
		changlayoutsize();
		_window.location.reload();
	});
});
var obj, _frame, _window;
var isInit = false;
var ii = 0;
// 初始化编辑区域
function initEditor() {
	_frame = document.getElementById("mainiframe");
	_window = _frame.contentWindow;
	// _window.document.designMode="on"; //设置为设计模式，就可以填写内容了
	// _window.document.canHaveHTML=true; //.可以包含HTML
	// _window.document.contentEditable = true; //可以写一堆事件处理
	$(_window.document).unbind("mouseup");
	$(_window.document).bind("mouseup", function(event) {
		obj = event.target || event.srcElement;
		window.document.title = "{\"boj\":\"" + obj + "\",\"id\":\""
			+ obj.id + "\"}";
	});
	$(_window.document).unbind("click");
	$(_window.document).bind("click", function(event) {
		obj = event.target || event.srcElement;
		window.document.title = "{\"boj\":\"" + obj + "\",\"id\":\""
			+ obj.id + "\"}";
	});
	$(_window.document.body).attr('contenteditable', true);
	$(_window.document.body).find('img').each(function() {
		$(this).attr("onclick", "");// 祛除页面按钮的事件
		$(this).unbind("click");
	});
	/*
	 * $(document.getElementById("mainiframe").contentWindow.document).bind('click',function(event){
	 * var obj = event.target||event.srcElement; window.document.title =
	 * "{\"boj\":\""+obj+"\",\"id\":\""+obj.id+"\"}"; });
	 */
	//if (ii == 0) {
	// initRightMenu();// 初始化右键菜单
	// rm.bind(_window,_window.document.body);
	//}
	ii++;
	isInit = true;
}

function getFrameMouseCoords(ev, _window) {
	if (ev.pageX || ev.pageY) {
		return { left: ev.pageX, top: ev.pageY };
	}
	return {
		left: ev.clientX + _window.document.body.scrollLeft - _window.document.body.clientLeft,
		top: ev.clientY + _window.document.body.scrollTop - _window.document.body.clientTop
	};
}

// 查看html源码
function lookResourse() {
	var objTile = "{\"action\":\"lookResourse\"";
	if (obj && obj != "") {
		objTile += ",\"boj\":\"" + obj + "\",\"id\":\"" + obj.id + "\"}"
	} else {
		objTile += "}";
	}
	window.document.title = objTile;
	setTimeout(function() {
		rm.release();
	}, 100);// 完成事件后再释放右键菜单
}

// 根据id选中html dom
function selectElement(strid) {
	try {
		_window.document.getElementById(strid).focus();
	} catch (e) {
	}
}

// 剪切dom对象
function catElement() {
	var objTile = "{\"action\":\"catAction\"";
	if (obj && obj != "") {
		objTile += ",\"boj\":\"" + obj + "\",\"id\":\"" + obj.id + "\"}"
	} else {
		objTile += "}";
	}
	window.document.title = objTile;
	setTimeout(function() {
		$(obj).remove();
	}, 100);// 完成事件后再释放右键菜单
}

// 复制dom对象
function copyElement() {
	var objTile = "{\"action\":\"copyAction\"";
	if (obj && obj != "") {
		objTile += ",\"boj\":\"" + obj + "\",\"id\":\"" + obj.id + "\"}"
	} else {
		objTile += "}";
	}
	window.document.title = objTile;
}

// 删除dom对象
function deleteElement() {
	var objTile = "{\"action\":\"deleteAction\"";
	if (obj && obj != "") {
		objTile += ",\"boj\":\"" + obj + "\",\"id\":\"" + obj.id + "\"}"
	} else {
		objTile += "}";
	}
	window.document.title = objTile;
	setTimeout(function() {
		//rm.release();
		$(obj).remove();
	}, 100);// 完成事件后再释放右键菜单
}

//刷新
function realodFrame() {
	try {
		_window.location.reload(true);
	} catch (e) {
	}
}