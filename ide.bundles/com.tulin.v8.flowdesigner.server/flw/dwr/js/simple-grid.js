/*
 * Subject: 静态列表(属性列表)
 * Author:   云南起步科技有限公司 技术研发部
 * CopyRight: 云南起步科技有限公司
 * Version:  v1.1.1
 */
var simpleGrid = function() {
	this.id = null;
	this.dataID = null;
	this.name = [];
	this.value = [];
	this.init = function(parent) {
		if (!parent)
			return;
		var parentObj = null;
		if (typeof parent == "string") {
			parentObj = document.getElementById(parent);
		} else {
			parentObj = parent;
		}
		this.id = parentObj.id + "_gridTable";
		this.dataID = parentObj.id + "_gridTable_data_TD";
		var gridTable = "<table width='100%' height='100%' id='"
				+ this.id
				+ "' class='grid'><tbead>"
				+ "<tr class='scrollColThead' style='height:22px;'>"
				+ "<td class='grid_label' width='160'>Name</td><td class='grid_label'>Value</td></tr>"
				+ "</thead><tbody><tr><td colspan='2' id='" + this.dataID
				+ "'></td></tr></tbody></table>";
		if (typeof parent == "string") {
			document.getElementById(parent).innerHTML = gridTable;
		} else {
			parent.innerHTML = gridTable;
		}
	};
	this.selectedLine = function() {

	};
	/*
	 * 清除
	 * @memberOf {TypeName} 
	 */
	this.clearData = function() {
		try {
			document.getElementById(this.dataID).innerHTML = "";
		} catch (e) {
		}
	};
	/*
	 * 添加
	 * @param {Object} jsonArray
	 * @memberOf {TypeName} 
	 * @return {TypeName} 
	 */
	this.addItem = function(jsonArray) {
		if (!jsonArray) {
			this.clearData();
			return;
		}
		var dataTable = "<table class='grid' width='100%' height='100%' id='"
				+ this.id + "_Data'>";
		for ( var i in jsonArray) {
			var jsondata = jsonArray[i];
			if (jsondata && jsondata.Name) {
				dataTable += "<tr style='height:22px;' id='" + jsondata.Name
						+ "'><td class='grid_td' width='160'>" + jsondata.Name
						+ "</td><td class='grid_td'>"
						+ (jsondata.Value ? jsondata.Value : "") + "</td></tr>";
			}
		}
		dataTable += "<tr><td></td><td></td></tr></table>";
		document.getElementById(this.dataID).innerHTML = dataTable;
		try {
			var dataTable = document.getElementById(this.id + "_Data");
			var dataRws = dataTable.rows;
			for ( var i = 0; i < dataRws.length; i++) {
				if (dataRws[i].cells[0].innerHTML) {
					dataRws[i].cells[1].onclick = function() {
						new simpleGrid().editParam(this);
					}
				}
			}
			return reData;
		} catch (e) {
		}
	};
	/*
	 * 编辑
	 * @param {Object} obj
	 * @memberOf {TypeName} 
	 * @return {TypeName} 
	 */
	this.editParam = function(obj) {
		if (!obj)
			return false;
		if (obj.innerHTML && obj.innerHTML.indexOf("INPUT") > 0)
			return true;
		try {
			var oValue = obj.innerHTML;
			var editeript = "<input type='text' id='param_editer_ipt' value='"
					+ oValue + "' style='width:100%;'/>";
			obj.innerHTML = editeript;
			document.getElementById("param_editer_ipt").focus();
			document.getElementById("param_editer_ipt").onblur = function() {
				new simpleGrid().eidterend(this);
			};
		} catch (e) {
		}
	};
	/*
	 * 编辑完成
	 * @param {Object} obj
	 * @return {TypeName} 
	 */
	this.eidterend = function(obj) {
		if (!obj)
			return;
		try {
			var nValue = obj.value;
			obj.parentNode.innerHTML = nValue;
		} catch (e) {
		}
	};
	/*
	 * 取出所有参数(值)
	 * @memberOf {TypeName} 
	 * @return {TypeName} 
	 */
	this.getDatas = function() {
		try {
			var dataTable = document.getElementById(this.id + "_Data");
			var dataRws = dataTable.rows;
			var reData = new Array();
			for ( var i = 0; i < dataRws.length; i++) {
				var jsonD = {};
				if (dataRws[i].cells[0].innerHTML) {
					jsonD.Name = dataRws[i].cells[0].innerHTML;
					jsonD.Value = dataRws[i].cells[1].innerHTML;
					reData.push(jsonD);
				}
			}
			return reData;
		} catch (e) {
		}
	};
}