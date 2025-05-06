if (!justep)
	var justep = {};
justep.Context = {
	userInfo : {},
	getAllRoles : function() {
		var result = justep.yn.XMLHttpRequest("getAllRolesAction", null,
				"post", false);
		if (result.flag == "false") {
			alert(result.message);
		} else {
			var data = result.data;
			if (typeof data == "string") {
				data = window.eval("(" + data + ")");
			}
			return data;
		}
	},
	getCurrentDeptID : function() {
		return this.userInfo.deptid;
	},
	getCurrentDeptCode : function() {
		return this.userInfo.deptcode;
	},
	getCurrentDeptFCode : function() {
		return this.userInfo.deptfcode;
	},
	getCurrentDeptFID : function() {
		return this.userInfo.deptfid;
	},
	getCurrentDeptFName : function() {
		return this.userInfo.deptfname;
	},
	getCurrentDeptName : function() {
		return this.userInfo.deptname;
	},
	getCurrentOgnCode : function() {
		return this.userInfo.ogncode;
	},
	getCurrentOgnFCode : function() {
		return this.userInfo.ognfcode;
	},
	getCurrentOgnFID : function() {
		return this.userInfo.ognfid;
	},
	getCurrentOgnFName : function() {
		return this.userInfo.ognfname;
	},
	getCurrentOgnID : function() {
		return this.userInfo.ognid;
	},
	getCurrentOgnName : function() {
		return this.userInfo.ognname;
	},
	getCurrentOrgCode : function() {
		return this.userInfo.orgcode;
	},
	getCurrentOrgFCode : function() {
		return this.userInfo.orgfcode;
	},
	getCurrentOrgFID : function() {
		return this.userInfo.orgfid;
	},
	getCurrentOrgFName : function() {
		return this.userInfo.orgfname;
	},
	getCurrentOrgID : function() {
		return this.userInfo.orgid;
	},
	getCurrentOrgName : function() {
		return this.userInfo.orgname;
	},
	getCurrentPersonCode : function() {
		return this.userInfo.personcode;
	},
	getCurrentPersonID : function() {
		return this.userInfo.personid;
	},
	getCurrentPersonMemberFNameWithAgent : function() {

	},
	getCurrentPersonMemberNameWithAgent : function() {

	},
	getCurrentPersonName : function() {
		return this.userInfo.personName;
	},
	getCurrentPersonFID : function() {
		return this.userInfo.personfid;
	},
	getCurrentPersonFCode : function() {
		return this.userInfo.personfcode;
	},
	getCurrentPersonFName : function() {
		return this.userInfo.personfname;
	},
	getCurrentPosCode : function() {
		return this.userInfo.positioncode;
	},
	getCurrentPosFCode : function() {
		return this.userInfo.positionfcode;
	},
	getCurrentPosFID : function() {
		return this.userInfo.positionfid;
	},
	getCurrentPosFName : function() {
		return this.userInfo.positionfname;
	},
	getCurrentPosID : function() {
		return this.userInfo.positionid;
	},
	getCurrentPosName : function() {
		return this.userInfo.positionname;
	},
	getCurrentProcess : function() {
		return justep.yn.RequestURLParam.getParam("process");
	},
	getCurrentActivity : function() {
		return justep.yn.RequestURLParam.getParam("activity");
	},
	getCurrentProcessLabel : function(process) {
		var param = new justep.yn.RequestParam();
		param.set(process || this.getCurrentProcess());
		var result = justep.yn.XMLHttpRequest("getProcessLabelAction", param);
		return result.processlabel;
	},
	getExecuteContext : function() {

	},
	getExecutor : function() {

	},
	getExecutorPerson : function() {

	},
	getLanguage : function() {
		return this.userInfo.locale;
	},
	getLoginDate : function() {
		return this.userInfo.loginDate;
	},
	getProcessData1 : function() {
		return justep.yn.RequestURLParam.getParam("sData1")
				|| justep.yn.RequestURLParam.getParam("rowid");
	},
	getProcessData2 : function() {

	},
	getProcessData3 : function() {

	},
	getProcessData4 : function() {

	},
	init : function() {
		if(topparent!=window){
			if(topparent.$.jpolite.clientInfo&&topparent.$.jpolite.clientInfo.personid){
				this.userInfo = topparent.$.jpolite.clientInfo;
				return;
			}
		}
		var result = justep.yn.XMLHttpRequest("system/User/initPortalInfo", null, "post", false);
		if (typeof result == "string") {
			result = window.eval("(" + result + ")");
		}
		this.userInfo = window["eval"]("(" + result[0].data + ")");
		if (this.userInfo.personid == "null" || this.userInfo.personid == "") {
			try {
				justepYnApp.reloadLoginPage("连接中断，请重新登录.");
			} catch (e) {
				alert("连接中断，请重新登录.");
				topparent.open(cpath+'/portal/login.html', '_self');
			}
		}
	},
	getBsessionid : function() {
		return topparent.$.jpolite.ClientInfo.businessId||"";
	}
};
$(document).ready(function() {
	justep.Context.init();
});

justep.Portal = {
	openWindow : function(name, url, param, callback) {
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
		param = param || {};
		var option = {
			title : name,
			url : url,
			hideTree : param.hidetreeable || false,
			executor : param.executor
		};
		return topparent.$.X.runFunc(option, callback);
	},
	closeWindow : function(id) {
		var currentTabId = id ? id : topparent.$.jpolite.getTabID();
		topparent.$.jpolite.removeTab(currentTabId);
	},
	existWindow : function(id) {
		return topparent.$.jpolite.getTab(id);
	},
	logout : function() {
		if (confirm("请您注意，是否打开的功能都保存了，关闭系统将导致没有保存的数据丢失！\r\r您确定要退出吗？")) {
			topparent.$.jpolite.Data.system.User.logout(function(data) {
			});
			topparent.location.href = window.location.href.replace(
					/index.*\.html.*/, 'login.html');
		}
	}
};