/*-------------------------------------------------------------------------------*\
|  Subject:  J YJsf grid 主要程序
|  Author:   陈乾
 *  CopyRight:www.tlv8.com
|  Created:  2011-12-25
|  Update:   2013-12-16
|  Version:  v3.1
|  Update:   2015-07-31 
|  Version:  v3.7
|  Update:   2018-11-01
\*-------------------------------------------------------------------------------*/
/**
 * @ngdocs function
 * @name tlv8.grid 主要程序
 * @param {DIVElement}
 *            div 参数说明【div:需要放置grid的层，为div对象】
 * @param {String}
 *            labelid【labelid：表头对应的id，用逗号分隔 如："id,name"】
 * @param labels【labels：表头内容，用逗号分隔
 *            如："编号,名称"】
 * @param labelwidth【labelwidth：表格列宽，用逗号分隔
 *            如："60,80"】
 * @param dataAction【dataAction：刷新数据动作
 *            如"getPersonInfo"】
 * @param width【width：grid的宽度
 *            如：300px，100%】
 * @param height【height：grid的高度
 *            如：300px，100%】
 * @param data
 *            数据对象
 * @param limit
 *            :分页行数
 * @param where：过滤条件
 * @param billdataformid：主从设置：指定主表的formid
 *            billcell:外键字段名
 * @param datatype:
 *            字段类型{string,number,year,yearmonth,month,date,datetime,ro,html:reader,select:id,button:action,checkBox:map,radio:map}
 *            html:reader 自定内容类型 reader为读取值的js函数； 函数reader(event)
 *            event有rowid、value ro为只读列 select:id{下拉选择类型 id为下拉组件id}
 *            button:action为按钮类型（action为点击按钮时的动作 类型为js function） checkBox:map
 *            单选类型 map为多选内容k为值,v为项显示内容map{k,v} radio:map 多选类型 map为选择内容
 *            k为值,v为项显示内容map{k,v}
 * @param master:
 *            是否多选(列master_check:多选) showindex:是否序号(列No:序号)
 *            工具条扩展：document.getElementById('"+div.id+"-grid-item').innerHTML =
 *            "";嵌入自定义工具条内容 自定义按钮{标题，宽度,动作(js 函数)，图片}
 *            如：currentgrid.insertSelfBar("重置密码","30px","resetPassword()","../../comon/image/toolbar/action/enable.gif");
 *            选中行事件onselected="" grid.setExcelexpBar(true);//设置excel导出
 * 
 * @param showindex
 *            参数：true grid默认情况不出现 grid.setExcelimpBar(true);//设置excel导入 参数：true
 *            grid默认情况不出现 grid.setRequired(cellName); //cellName:为需要设置必填的列名
 *            如：grid.setRequired("fCode"); 多个用逗号分隔
 *            grid.addFooterrow("cell1:colspan(1),cell2,cell3"); grid末尾添加统计行
 *            嵌入内容如:<span id="d"></sapn> d.innerHTML = s.;
 *            操作事件:beforeInsert,afterInert,beforeSave,afterSave,beforeDelete,afterDelete,beforeRefresh,afterRefresh
 *            用法： beforeInsert="binsert" 在新增数据之前执行 binsert js 函数
 * @param sql
 *            grid.setSQL(sql);//查询sql 查询动作为getGridActionBySQL时使用
 *            dataValueChanged值改变事件
 * @param fixColumn
 *            锁定列 数字 如：锁定前两列,设置为fixColumn 2 默认情况 锁定的列变为灰色背景 设定锁定列的 背景
 *            grid.setFixColumnBack('颜色代码');
 */
tlv8.createGrid = function(div, labelid, labels, labelwidth, dataAction,
		witdh, height, data, limit, where, billdataformid, billcell, datatype,
		master, showindex, sql, fixColumn) {
	// 修正没有加单位的高度
	if(height!="100%" && (height+"").indexOf("px")<0){
		height = height+"px";
	}
	if (!master || master == "")
		master = "";
	if (!showindex || showindex == "")
		showindex = "";
	if (!div.id || div.id == "") {
		alert("定义grid的div的id不能为空！");
		return
	}
	var grid = {
		data : data,
		fixColumn : fixColumn || 0, // 固定列 航向拖动时不滚动
		setFixColumn : function(num){
			this.fixColumn = num;
		},
		fixColumnBack : "#eee", 
		setFixColumn : function(num){
			this.fixColumn = num;
		},
		setFixColumnBack : function(color){
			this.fixColumnBack = color;
		},
		toolbar : {
			insertItem : true,
			saveItem : true,
			refreshItem : true,
			deleteItem : true
		},
		/**
		 * @name staticFilter
		 * @description GRID静态过滤条件
		 */
		staticFilter : "",
		setStaticFilter : function(filter) {
			this.staticFilter = filter;
		},
		sql : "",
		/**
		 * @function
		 * @name setSQL
		 * @description 设置查询SQL
		 * @param {string}
		 *            sql
		 */
		setSQL : function(sql) {
			grid.sql = CryptoJS.AESEncrypt(sql);
			var cupage = (grid.CurrentPage && grid.CurrentPage == 0) ? 1
					: grid.CurrentPage;
			var tempQueryAction = dataAction.queryAction + "?page="
					+ parseInt(cupage) + "&rows=" + parseInt(limit)
					+ "&gridid="
					+ encodeURIComponent(encodeURIComponent(div.id))
					+ "&columns=" + labelid + "&columnstype=" + datatype
					+ "&master=" + master + "&showindex=" + showindex
					+ "&insql="
					+ encodeURIComponent(encodeURIComponent(grid.sql));
			grid.queryAction = tempQueryAction;
		},
		/**
		 * @function
		 * @name settoolbar
		 * @description 设置按钮状态
		 * @param insertItem
		 *            -新增按钮（true，false，"readonly","no"）
		 * @param saveItem
		 *            -保存按钮（true，false，"readonly","no"）
		 * @param refreshItem
		 *            -刷新按钮（true，false，"readonly","no"）
		 * @param deleteItem
		 *            -删除按钮（true，false，"readonly","no"）
		 */
		settoolbar : function(insertItem, saveItem, refreshItem, deleteItem) {
			//查看流程【已办】时grid设置为“只读”，编辑按钮禁用
			if ("detail" == tlv8.RequestURLParam.getParam("activity-pattern")) {
				$("#"+div.id + "_insertItem").hide();
				$("#"+div.id + "_saveItem").hide();
				$("#"+div.id + "_deleteItem").hide();
				this.seteditModel(false);
				return;
			}
			this.toolbar.insertItem =  insertItem;
			this.toolbar.saveItem = saveItem;
			this.toolbar.refreshItem =  refreshItem;
			this.toolbar.deleteItem =  deleteItem;
			if(insertItem == "no"){
				// 不做任何处理
			}else if (this.toolbar.insertItem == true) {
				$("#"+div.id + "_insertItem").show();
				$("#"+div.id + "_insertItem").find("img").attr("src",$dpimgpath
						+ "toolbar/insert.gif");
				document.getElementById(div.id + "_insertItem").onclick = grid.insertData;
			} else if (this.toolbar.insertItem == "readonly") {
				$("#"+div.id + "_insertItem").show();
				$("#"+div.id + "_insertItem").find("img").attr("src", $dpimgpath
						+ "toolbar/un_insert.gif");
				document.getElementById(div.id + "_insertItem").onclick = null;
			} else{
				$("#"+div.id + "_insertItem").hide();
			}
			if(saveItem == "no"){
				// 不做任何处理
			}else if (this.toolbar.saveItem == true) {
				$("#"+div.id + "_saveItem").show();
				$("#"+div.id + "_saveItem").find("img").attr("src", $dpimgpath
						+ "toolbar/save.gif");
				document.getElementById(div.id + "_saveItem").onclick = grid.saveData;
			} else if (this.toolbar.saveItem == "readonly") {
				$("#"+div.id + "_saveItem").show();
				$("#"+div.id + "_saveItem").find("img").attr("src", $dpimgpath
						+ "toolbar/un_save.gif");
				document.getElementById(div.id + "_saveItem").onclick = null;
			} else{
				$("#"+div.id + "_saveItem").hide();
			}
			if(refreshItem == "no"){
				// 不做任何处理
			}else if (this.toolbar.refreshItem == true) {
				$("#"+div.id + "_refreshItem").show();
				$("#"+div.id + "_refreshItem").find("img").attr("src", $dpimgpath
						+ "toolbar/refreshbill.gif");
				document.getElementById(div.id + "_refreshItem").onclick = grid.refreshData;
			} else if (this.toolbar.refreshItem == "readonly") {
				$("#"+div.id + "_refreshItem").show();
				$("#"+div.id + "_refreshItem").find("img").attr("src", $dpimgpath
						+ "toolbar/un_refreshbill.gif");
				document.getElementById(div.id + "_refreshItem").onclick =null;
			} else{
				$("#"+div.id + "_refreshItem").hide();
			}
			if(deleteItem == "no"){
				// 不做任何处理
			}else if (this.toolbar.deleteItem == true) {
				$("#"+div.id + "_deleteItem").show();
				$("#"+div.id + "_deleteItem").find("img").attr("src", $dpimgpath
						+ "toolbar/remove.gif");
				document.getElementById(div.id + "_deleteItem").onclick = grid.deleteData;
			} else if (this.toolbar.deleteItem == "readonly") {
				$("#"+div.id + "_deleteItem").show();
				$("#"+div.id + "_deleteItem").find("img").attr("src", $dpimgpath
						+ "toolbar/un_remove.gif");
				document.getElementById(div.id + "_deleteItem").onclick = null;
			} else{
				$("#"+div.id + "_deleteItem").hide();
			}
		},
		CurrentPage : 1,
		standImagePath:'toolbar/standard_toolbar/standard/',
		/**
		 * @function
		 * @name setPageBar
		 * @description 设置分页按钮
		 * @param {number}
		 *            page -当前页码
		 * @param {number}
		 *            allpage -总的页数
		 */
		setPageBar : function(page, allpage) {
			var first_page = document.getElementById(div.id + "_first-page");
			var first = document.getElementById(div.id + "_first");
			var last = document.getElementById(div.id + "_last");
			var last_page = document.getElementById(div.id + "_last-page");
			limit = limit ? limit : 10;
			var penable = $dpimgpath
					+ grid.standImagePath+"first-page.gif";
			var pread = $dpimgpath
					+ grid.standImagePath+"un_first-page.gif";
			var fenable = $dpimgpath
					+ grid.standImagePath+"first.gif";
			var fread = $dpimgpath
					+ grid.standImagePath+"un_first.gif";
			var lenable = $dpimgpath
					+ grid.standImagePath+"last.gif";
			var lread = $dpimgpath
					+ grid.standImagePath+"un_last.gif";
			var lsenable = $dpimgpath
					+ grid.standImagePath+"last-page.gif";
			var lsread = $dpimgpath
					+ grid.standImagePath+"un_last-page.gif";
			if (allpage == 1) {
				first_page.src = pread;
				first_page.onclick = "";
				first.src = fread;
				first.onclick = "";
				last.src = lread;
				last.onclick = "";
				last_page.src = lsread;
				last_page.onclick = "";
			} else if (page == 1) {
				first_page.src = pread;
				first_page.onclick = "";
				first.src = fread;
				first.onclick = "";
				last.src = lenable;
				last_page.src = lsenable;
				last.onclick = function() {
					grid.nextPage();
				};
				last_page.onclick = function() {
					grid.lastPage();
				};
			} else if (allpage == page) {
				first_page.src = penable;
				first.src = fenable;
				last.src = lread;
				last.onclick = "";
				last_page.src = lsread;
				first_page.onclick = function() {
					grid.firstPage();
				};
				first.onclick = function() {
					grid.previousPage();
				};
			} else {
				first_page.src = penable;
				first.src = fenable;
				last.src = lenable;
				last_page.src = lsenable;
				first_page.onclick = function() {
					grid.firstPage();
				};
				first.onclick = function() {
					grid.previousPage();
				};
				last.onclick = function() {
					grid.nextPage();
				};
				last_page.onclick = function() {
					grid.lastPage();
				};
			}
			grid.allpage = allpage;
		},
		firstPage : function(){
			// 第一页
			grid.queryAction = dataAction.queryAction + "?page=" + 1
					+ "&rows=" + limit + "&gridid="
					+ encodeURIComponent(encodeURIComponent(div.id))
					+ "&columns=" + labelid + "&columnstype="
					+ datatype + "&master=" + master + "&showindex="
					+ showindex + "&insql="
					+ encodeURIComponent(encodeURIComponent(grid.sql));
			grid.savAction = dataAction.savAction + "?page=" + 1
					+ "&rows=" + limit;
			grid.refreshData();
		},
		previousPage : function(){
			// 上一页
			grid.queryAction = dataAction.queryAction + "?page="
					+ parseInt(grid.CurrentPage - 1) + "&rows=" + limit
					+ "&gridid="
					+ encodeURIComponent(encodeURIComponent(div.id))
					+ "&columns=" + labelid + "&columnstype="
					+ datatype + "&master=" + master + "&showindex="
					+ showindex + "&insql="
					+ encodeURIComponent(encodeURIComponent(grid.sql));
			grid.savAction = dataAction.savAction + "?page="
					+ parseInt(grid.CurrentPage - 1) + "&rows=" + limit;
			grid.refreshData();
		},
		nextPage : function(){
			// 下一页
			grid.queryAction = dataAction.queryAction + "?page="
					+ parseInt(grid.CurrentPage + 1) + "&rows=" + limit
					+ "&gridid="
					+ encodeURIComponent(encodeURIComponent(div.id))
					+ "&columns=" + labelid + "&columnstype="
					+ datatype + "&master=" + master + "&showindex="
					+ showindex + "&insql="
					+ encodeURIComponent(encodeURIComponent(grid.sql));
			grid.savAction = dataAction.savAction + "?page="
					+ parseInt(grid.CurrentPage + 1) + "&rows=" + limit;
			grid.refreshData();
		},
		lastPage : function(){
			// 最后一页
			grid.queryAction = dataAction.queryAction + "?page="
					+ grid.allpage + "&rows=" + limit + "&gridid="
					+ encodeURIComponent(encodeURIComponent(div.id))
					+ "&columns=" + labelid + "&columnstype="
					+ datatype + "&master=" + master + "&showindex="
					+ showindex + "&insql="
					+ encodeURIComponent(encodeURIComponent(grid.sql));
			grid.savAction = dataAction.savAction + "?page=" + grid.allpage
					+ "&rows=" + limit;
			grid.refreshData();
		},
		inputPage : function(event, obj) {
			if (event.keyCode == 13) {
				var page = obj.value;
				if (!page || page == "")
					return;
				grid.toPage(page);
			};
		},
		allpage:1,
		getPages : function(){
			// 获取数据总页数
			return grid.allpage;
		},
		toPage : function(page){
			if(parseInt(page)<0){
				alert("页码不能小于0!");
				return;
			}
			if(parseInt(page)>grid.allpage){
				alert("指定的页码不能大于总页数!");
				return;
			}
			grid.queryAction = dataAction.queryAction + "?page="
					+ parseInt(page) + "&rows=" + limit + "&gridid="
					+ encodeURIComponent(encodeURIComponent(div.id))
					+ "&columns=" + labelid + "&columnstype=" + datatype
					+ "&master=" + master + "&showindex=" + showindex
					+ "&insql="
					+ encodeURIComponent(encodeURIComponent(grid.sql));
			grid.savAction = dataAction.savAction + "?page="
					+ parseInt(page) + "&rows=" + limit;
			grid.refreshData();
		},
		beforSortCell : null,
		CellSort : function(obj, cellID) {
			var orDerby = "";
			var celltype = "textarea";
			var cellIndex = (obj.tagName == "TD") ? obj.cellIndex
					: obj.parentNode.cellIndex;
			var datatypes = datatype.split(",");
			var dtype = datatypes[cellIndex];
			if(dtype.toUpperCase()=="CLOB"){
				return;
			}
			if (grid.beforSortCell && obj != grid.beforSortCell){
				grid.beforSortCell.className = "";
				$(grid.beforSortCell).find("div").attr("class","");
			}
			grid.sortID = cellID;
			var objlaebl = $(obj).find("div");
			if (obj.className && obj.className == "sortASC") {
				$(objlaebl).attr("class","sortDESC");
				obj.className = "sortDESC";
				orDerby = cellID + " desc";
				grid.sortSC = "sortDESC";
			} else {
				$(objlaebl).attr("class","sortASC");
				obj.className = "sortASC";
				orDerby = cellID + " asc";
				grid.sortSC = "sortASC";
			}
			data.setOrderby(orDerby);
			grid.refreshData();
			grid.beforSortCell = obj;
		},
		/**
		 * @function
		 * @name insertSelfBar
		 * @description 添加自定义按钮
		 * @param {string}
		 *            label
		 * @param {string}
		 *            width -按钮宽度（"80px"）
		 * @param {string}
		 *            action -点击按钮动作（"chanjskdjf()"）
		 * @param {string}
		 *            img -按钮图片路径（"../img/dddd.png"）
		 * @returns {string} 返回按钮ID
		 */
		insertSelfBar : function(label, width, action, img) {
			var barID = new UUID().createUUID();
			var tal = "<a href='javascript:void(0)' class='toobar_item' title='"
					+ label + "' >";
			if(img){
				tal += "<img src='" + img + "' id='" + barID + "' onclick='"
				+ action + "'/>";
				tal += "</a>";
			}else{
				tal = "<input id='" + barID
				+ "' type='button' value='" + label + "' onclick='"
				+ action 
				+ "' class='xui-button' style='margin-left:2px;width:"
				+ width
				+ ";height:22px;cursor:pointer;'/>";
			}
			var main_tollbar = document.getElementById(div.id
					+ "_self_item_table");
			var main_tollbar_tr = main_tollbar.getElementsByTagName("tr")[0];
			var Celllength = main_tollbar_tr.cells.length;
			var cellindex = Celllength - 1;
			var cell = main_tollbar_tr.insertCell(cellindex);
			cell.style.className = "grid_td";
			cell.style.width = width;
			cell.innerHTML = tal;
			return barID;
		},
		/**
		 * @function
		 * @name setExcelimpBar
		 * @description 设置是否显示Excel导入按钮
		 * @param {boolean}
		 *            flag
		 */
		setExcelimpBar : function(flag) {
			var excelImgPath = $dpimgpath
					+ grid.standImagePath+"imp-excel.gif";
			if (flag) {
				var mimportbar = grid
						.insertSelfBar("Excel数据导入", "30px", "",
								excelImgPath);
				J$(mimportbar).onclick = function(){
					tlv8.ExcelImp(data.dbkay,data.table,labelids,"",function(){
						grid.refreshData();
					});
				}
			}
		},
		billfilter : "",
		/**
		 * @function
		 * @name setExcelexpBar
		 * @description 设置是否显示Excel导出按钮
		 * @param {boolean}
		 *            flag
		 */
		setExcelexpBar : function(flag) {
			var excelImgPath = $dpimgpath
					+ grid.standImagePath+"exp-excel.gif";
			if (flag) {
				var excelexpbarID = grid.insertSelfBar("导出Excel", "30px", "",
						excelImgPath);
				J$(excelexpbarID).onclick = function() {
					var cells = [];
					var labelas = [];
					var labelids = labelid.split(",");
					var widths = labelwidth.split(",");
					var labelas = labels.split(",");
					for(var i=0;i<labelids.length;i++){
						if(widths[i]!="0"){
							cells.push(labelids[i]);
							labelas.push(labelas[i]);
						}
					}
					tlv8.ExcelExp(data.dbkay, data.table, cells.join(","), labelas.join(","),
							grid.billfilter, data.orderby);
				};
			}
		},
		Length : 0,
		/**
		 * @function
		 * @name getLength
		 * @description 获取数据行数
		 * @returns {number}
		 */
		getLength : function() {
			return grid.Length;
		},
		RowId : new Array(),
		/**
		 * @function
		 * @name getRowId
		 * @description 获取指定行rowid
		 * @param {number}
		 *            index -行数从0开始
		 * @returns {string}
		 */
		getRowId : function(index) {
			return grid.RowId[index];
		},
		reMoveRowId : function(rId) {
			for ( var i = 0; i < grid.RowId.length; i++) {
				if (grid.RowId[i] == rId) {
					grid.RowId.splice(i, 1);
					break;
				}
			}
		},
		Value : new Map(),
		/**
		 * @function
		 * @name setValueByName
		 * @description 给指定列赋值
		 * @param {string}
		 *            name -列名
		 * @param {string}
		 *            param -行rowid或行数
		 * @param {string}
		 *            value -需要赋的值
		 */
		setValueByName : function(name, param, value) {
			grid.beforeeditData = grid.getValueByName(name, param);
			var datatypes = datatype.split(",");
			var result, rowid, index;
			value = value || "";
			var dcodeV = value.toString().replaceAll("<", "&lt;");
			dcodeV = dcodeV.replaceAll(">", "&gt;");
			if (typeof param != "number" || parseInt(param) == "NaN") {
				rowid = param;
				index = this.getgridRelationIndex(name);
				if (index > -1) {
					var setRow = document.getElementById(param);
					if (!setRow)
						return false;
					var setCell = setRow.getElementsByTagName("td")[index];
					if (datatypes[index].indexOf("html:") < 0
							&& datatypes[index].indexOf(
									"checkBox:") < 0
							&& datatypes[index].indexOf("radio:") < 0
							&& datatypes[index].indexOf("select:") < 0) {
						$(setCell).html("<div class='gridValue' >"+dcodeV+"</div>");
						setCell.title = value;
					} else if (datatypes[index].indexOf("html:") > -1) {
						try {
							var htmlReader = trim(datatypes[index].replace("html:", ""));
							var rsFn = eval(htmlReader);
							if (typeof rsFn == "function") {
								var readValue = rsFn( {
									rowid : rowid,
									value : value,
									gridCom : grid,
									cellname : name
								});
								$(setCell).html("<div class='gridValue' >"+readValue+"</div>");
								setCell.title = value;
							}
						} catch (e) {
						}
					}else if(datatypes[index].indexOf("select:") > -1){
						try {
							var selectID = datatypes[index].split(":")[1];
							var selectObj = document.getElementById(selectID);
							if (selectObj.tagName == "DIV") {
								selectObj = selectObj
										.getElementsByTagName("SELECT")[0];
							}
							// selectObj.value = value;
							$(selectObj).val(trim(value));
							var nlabel = $(selectObj).find("option:selected").text();
							// for ( var n in selectObj.childNodes) {
							// if (selectObj.childNodes[n].value == value) {
									// nlabel =
									// selectObj.childNodes[n].innerHTML;
							// break;
							// }
							// }
							setCell.title = nlabel;
							$(setCell).html("<div class='gridValue' >"+nlabel+"</div>");
						} catch (e) {
						}
					}
					result = true;
				} else {
					result = false;
				}
			} else {
				index = this.getgridRelationIndex(name);
				if (index > -1) {
					rowid = grid.getRowId(param);
					var setRow = document.getElementById(rowid);
					if (!setRow)
						return false;
					var setCell = setRow.getElementsByTagName("td")[index];
					if (datatypes[index].indexOf("html:") < 0
							&& datatypes[index].indexOf(
									"checkBox:") < 0
							&& datatypes[index].indexOf("radio:") < 0 
							&& datatypes[index].indexOf("select:") < 0) {
						setCell.innerHTML = "<div class='gridValue' >"+dcodeV+"</div>";
						setCell.title = value;
					} else if (datatypes[index].indexOf("html:") > -1) {
						try {
							var htmlReader = trim(datatypes[index].replace("html:", ""));
							var rsFn = eval(htmlReader);
							if (typeof rsFn == "function") {
								var readValue = rsFn( {
									rowid : rowid,
									value : value,
									gridCom : grid,
									cellname : name
								});
								setCell.innerHTML = "<div class='gridValue' >"+readValue+"</div>";
								setCell.title = value;
							}
						} catch (e) {
						}
					}else if(datatypes[index].indexOf("select:") > -1){
						try {
							var selectID = datatypes[index].split(":")[1];
							var selectObj = document
									.getElementById(selectID);
							if (selectObj.tagName == "DIV") {
								selectObj = selectObj
										.getElementsByTagName("SELECT")[0];
							}
							// selectObj.value = value;
							$(selectObj).val(trim(value));
							var nlabel = $(selectObj).find("option:selected").text();
							// for ( var n in selectObj.childNodes) {
							// if (selectObj.childNodes[n].value == value) {
									// nlabel =
									// selectObj.childNodes[n].innerHTML;
							// break;
							// }
							// }
							setCell.title = nlabel;
							$(setCell).html("<div class='gridValue' >"+nlabel+"</div>");
						} catch (e) {
						}
					}
					result = true;
				} else {
					result = false;
				}
			}
			grid.changeValue(rowid, name, value, index);
			if (grid.toolbar.saveItem != false)
				grid.settoolbar("no", true, "no", "no");
			return result;
		},
		/**
		 * @function
		 * @name getValueByName
		 * @description 获取指定列的值
		 * @param {string}
		 *            name -列名
		 * @param {string}
		 *            param -行rowid或行数
		 * @returns {string}
		 */
		getValueByName : function(name, param) {
			var grid_data_table = document.getElementById(div.id + "_main_grid");
			if (typeof param != "number" || parseInt(param) == "NaN") {
				var rowid = param;
				var rowIndex = grid.getIndex(rowid);
				var index = this.getgridRelationIndex(name);
				if (rowIndex && index > -1) {
					var iValue = "";
					try {
						iValue = $(grid_data_table.rows[rowIndex].cells[index]).text();
						if (grid_data_table.rows[rowIndex].cells[index].childNodes[0].tagName == "INPUT") {
							iValue = grid_data_table.rows[rowIndex].cells[index].childNodes[0].value;
						}
					} catch (e) {
					}
					return grid.Value.get(rowid + index) || iValue;
				}
			} else {
				var index = this.getgridRelationIndex(name);
				if (index > -1) {
					var rowid = this.getRowId(param);
					var iValue = "";
					try {
						iValue = $(grid_data_table.rows[rowIndex].cells[index]).text();
						if (grid_data_table.rows[rowIndex].cells[index].childNodes[0].tagName == "INPUT") {
							iValue = grid_data_table.rows[rowIndex].cells[index].childNodes[0].value;
						}
					} catch (e) {
					}
					return grid.Value.get(rowid + index) || iValue;
				}
			}
		},
		/**
		 * @function
		 * @name locate
		 * @description 查找关系值对应的行ID,crowid除外
		 * @param {string}
		 *            relation -列名
		 * @param {string}
		 *            value -值
		 * @param {string}
		 *            crowid -行rowid
		 * @returns {string}
		 */
		locate : function(relation, value, crowid) {
			var rowids = "";
			try {
				var grid_data_table = document.getElementById(div.id + "_main_grid");
				var index = grid.getgridRelationIndex(relation);
				var havCount = 0;
				for ( var i = 0; i < grid_data_table.rows.length; i++) {
					var row = grid_data_table.rows[i];
					var cell = row.cells[index];
					if (($(cell).text() == value || grid.Value.get(row.id
							+ index) == value)
							&& crowid != row.id) {
						if (havCount > 0) {
							rowids += ",";
						}
						rowids += row.id;
						havCount++;
					}
				}
			} catch (e) {
			}
			return rowids;
		},
		/**
		 * @function
		 * @name getIndex
		 * @description 获取指定行的行号
		 * @param {string}
		 *            cId -行rowid
		 * @returns {number}
		 */
		getIndex : function(cId) {
			for ( var i in grid.RowId) {
				if (grid.RowId[i] == cId)
					return i;
			}
			return -1;
		},
		dbclick : function(data) {
			if (div.ondbclick) {
				var gridondbclick = div.ondbclick;
				if (gridondbclick.indexOf("(") > 0) {
					eval(gridondbclick);
				} else {
					var dbckfn = eval(gridondbclick);
					dbckfn(grid);
				}
			}
		},
		/**
		 * @private
		 * @name setdbclick
		 * @description 设置行双击事件
		 * @param {function}
		 *            fn
		 */
		setdbclick : function(fn) {
			this.dbclick = fn;
		},
		gridRelation : null,
		gridLabels : null,
		/**
		 * @function
		 * @name getgridRelationIndex
		 * @description 获取指定列的序号
		 * @param {string}
		 *            relation
		 * @returns {number}
		 */
		getgridRelationIndex : function(relation) {
			for ( var i = 0; i < this.gridRelation.length; i++) {
				if (relation == this.gridRelation[i]) {
					return i;
				}
			}
			return -1;
		},
		editModel : false,
		editMOdelVal : "readonly",
		rowState : new Map(),
		/**
		 * @function
		 * @name setrowState
		 * @description 设置某行的状态
		 * @param {string}
		 *            rowid
		 * @param {string}
		 *            state
		 * @example grid.setrowState(rowid,"readonly");
		 */
		setrowState : function(rowid, state) {
			this.rowState.put(rowid, state);
		},
		/**
		 * @function
		 * @name seteditModel
		 * @description 设置grid的编辑模式
		 * @param {string}
		 *            editModel
		 * @example grid.seteditModel(true|false); //是否可编辑
		 * @example grid.seteditModel("dbclick"); //双击编辑
		 */
		seteditModel : function(editModel) {
			this.editModel = editModel;
			div.editModel = editModel;
			if (editModel == true){
				dbclick = null;
			}
			if(div.editModel == true || div.editModel == "dbclcik" || div.editModel == "dbclick"){
				this.editMOdelVal = "edit";
			}else{
				this.editMOdelVal = "readonly";
			}
		},
		editDataRowIds : null,
		beforeeditData : null,
		editedDatas : new Map(),
		RequiredCells : new Map(),
		requiredAlert : "",
		/**
		 * @function
		 * @name setRequired
		 * @description 设置指定的字段必填
		 * @param {string}
		 *            cellName -需要设置必填的列名，多个用逗号分隔
		 * @example grid.setRequired("fCODE");
		 * @example grid.setRequired("fCODE,fNAME");
		 */
		setRequired : function(cellName) {
			var cellsn = cellName.split(",");
			for ( var i = 0; i < cellsn.length; i++) {
				grid.RequiredCells.put(cellsn[i], true);
			}
		},
		outEditBlur : function() {
			try {
				var temP_ipt_d = document.getElementById(div.id
						+ "_editgridipt");
				if (temP_ipt_d) {
					grid.editended(temP_ipt_d, false, false);
				}
			} catch (e) {
			}
		},
		editData : function(event, obj) {
			if (grid.rowState.containsKey(obj.id)) {
				if (grid.rowState.get(obj.id) == "readonly") {
					grid.outEditBlur();
					return false;
				}
			}
			event = event ? event : (window.event ? window.event : null);
			var objEdit = event.srcElement ? event.srcElement : event.target;
			if (objEdit.tagName == "INPUT") {
				try {
					objEdit.focus();
				} catch (e) {
				}
				return false;
			} else {
				grid.outEditBlur();
			}
			if(objEdit.tagName=="DIV"){
				objEdit = objEdit.parentNode;
			}
			var Editrow = objEdit.parentNode;
			var TDvalue = $(objEdit).text() ? $(objEdit).text() : "";
			grid.beforeeditData = grid.Value
					.get(Editrow.id + objEdit.cellIndex);
					// || TDvalue;
			if (objEdit.cellIndex >= Editrow.getElementsByTagName("td").length - 1)
				return;
			try {
				var valDocument = objEdit.childNodes[0];
				if (valDocument.tagName == "INPUT") {
					return false;
				}
			} catch (e) {
			}
			TDvalue = TDvalue.toString().replaceAll("&lt;", "<");
			TDvalue = TDvalue.toString().replaceAll("&gt;", ">");
			var changeTabIndex = function(enter) {
				var datatypes = datatype.split(",");
				var witdhs = labelwidth.split(",");
				if (objEdit.cellIndex < Editrow.cells.length - 2) {
					var sta = objEdit.cellIndex + 1;
					var len = Editrow.cells.length;
					for ( var i = sta; i < len; i++) {
						if (i == len - 1) {
							gotNextLineBg(enter);
							return;
						}
						if (datatypes[i] == "ro" || witdhs[i] == 0)
							continue;
						else {
							Editrow.cells(i).click();
							return;
						}
					}
				}
				if (Editrow.rowIndex < Editrow.parentNode.rows.length - 1) {
					gotNextLineBg(enter);
				} else if (enter) {
					try {
						var newdataFn = document.getElementById(div.id + "_insertItem").click();
					} catch (e) {
					}
				}
			};
			var gotNextLineBg = function(enter) {
				var datatypes = datatype.split(",");
				var witdhs = labelwidth.split(",");
				try {
					var nextRow = Editrow.parentNode.rows(Editrow.rowIndex + 1);
				} catch (e) {
				}
				if (nextRow
						&& nextRow.rowIndex < nextRow.parentNode.rows.length) {
					var sta = (showindex == "true") ? 1 : 0;
					var len = nextRow.cells.length;
					for ( var i = sta; i < len; i++) {
						if (datatypes[i] == "ro" || witdhs[i] == 0)
							continue;
						else {
							nextRow.cells(i).click();
							break;
						}
					}
					try {
						document.getElementById(div.id + "_editgridipt").focus();
						document.getElementById(div.id + "_editgridipt").select();
					} catch (e) {
					}
				} else {
					if (enter){
						try {
							var newdataFn = document.getElementById(div.id + "_insertItem").click();
							setTimeout(getSelectedRowEdit, 50);
						} catch (e) {
						}
					}
				}
			};
			var getSelectedRowEdit = function() {
				var datatypes = datatype.split(",");
				var witdhs = labelwidth.split(",");
				var Editrow = document.getElementById(grid.CurrentRowId);
				if (Editrow && Editrow.tagName == "TR") {
					var sta = (showindex == "true") ? 1 : 0;
					var len = Editrow.cells.length;
					for ( var i = sta; i < len; i++) {
						if (datatypes[i] == "ro" || witdhs[i] == 0)
							continue;
						else {
							Editrow.cells(i).click();
							break;
						}
					}
					try {
						document.getElementById(div.id + "_editgridipt").focus();
						document.getElementById(div.id + "_editgridipt").select();
					} catch (e) {
					}
				}
			};
			var NextLineCuCell = function(eventact) {
				var rowindex = (eventact == "down") ? (Editrow.rowIndex + 1)
						: (Editrow.rowIndex - 1);
				if (Editrow.rowIndex < Editrow.parentNode.rows.length - 1
						&& eventact == "down") {
					var nextRow = Editrow.parentNode.rows(rowindex);
					nextRow.cells(objEdit.cellIndex).click();
				}
				if (Editrow.rowIndex > 0 && eventact == "up") {
					var nextRow = Editrow.parentNode.rows(rowindex);
					nextRow.cells(objEdit.cellIndex).click();
				}
				try {
					document.getElementById(div.id + "_editgridipt").focus();
					document.getElementById(div.id + "_editgridipt").select();
				} catch (e) {
				}
			};
			var gotBeforeLineBg = function(enter) {
				var datatypes = datatype.split(",");
				var witdhs = labelwidth.split(",");
				try {
					var nextRow = Editrow.parentNode.rows(Editrow.rowIndex - 1);
				} catch (e) {
				}
				if (nextRow) {
					var sta = nextRow.cells.length - 2;
					var end = (showindex == "true") ? 1 : 0;
					for ( var i = sta; i >= end; i--) {
						if (datatypes[i] == "ro" || witdhs[i] == 0)
							continue;
						else {
							nextRow.cells(i).click();
							break;
						}
					}
					try {
						document.getElementById(div.id + "_editgridipt").focus();
						document.getElementById(div.id + "_editgridipt").select();
					} catch (e) {
					}
				}
			};
			var gotoLeftCell = function() {
				var datatypes = datatype.split(",");
				var witdhs = labelwidth.split(",");
				var sta = objEdit.cellIndex - 1;
				for ( var i = sta; i >= 0; i--) {
					if (i == 0) {
						gotBeforeLineBg();
						return
					}
					if (datatypes[i] == "ro" || witdhs[i] == 0) {
						continue;
					} else {
						Editrow.cells(i).click();
						return
					}
				}
				if (Editrow.rowIndex == 0) {
					gotBeforeLineBg();
				}
			};
			var buttonInputTabkey = function(e) {
				var event = e || window.event;
				if(!event.keyCode){
					grid.outEditBlur();
				}
				if (event.keyCode == 13 || event.keyCode == 9
						|| event.keyCode == 0) {
					grid.editended(document.getElementById(div.id + "_editgridipt"));
					if (event.keyCode == 9) {
						try {
							setTimeout(changeTabIndex, 5);
						} catch (e) {
						}
					} else if (event.keyCode == 13) {
						try {
							setTimeout(function() {
								changeTabIndex(true);
							}, 5);
						} catch (e) {
						}
					}
					return true;
				}
				/*
				if (event.keyCode == 40) {
					try {
						setTimeout(function() {
							NextLineCuCell("down");
						}, 5);
					} catch (e) {
					}
					return false;
				}
				if (event.keyCode == 38) {
					try {
						setTimeout(function() {
							NextLineCuCell("up");
						}, 5);
					} catch (e) {
					}
					return false;
				}
				if (event.keyCode == 37) {
					try {
						setTimeout(function() {
							gotoLeftCell();
						}, 5);
					} catch (e) {
					}
					return false;
				}
				if (event.keyCode == 39) {
					try {
						setTimeout(function() {
							changeTabIndex();
						}, 5);
					} catch (e) {
					}
					return false;
				}
				*/
			};
			var input = "<input id='"
					+ div.id
					+ "_editgridipt' name='editgridipt' type='text' value='"
					+ TDvalue
					+ "' style='width:100%;height:100%;font-size:12px;'></input>";
			if (datatype && datatype != "") {
				var datatypes = datatype.split(",");
				if (!datatypes[objEdit.cellIndex]
						|| (datatypes[objEdit.cellIndex].toLowerCase() == "ro"
								|| datatypes[objEdit.cellIndex].toLowerCase() == "textarea"
								|| datatypes[objEdit.cellIndex].indexOf("html:") > -1
								|| datatypes[objEdit.cellIndex]
										.indexOf("checkBox:") > -1
								|| datatypes[objEdit.cellIndex]
										.indexOf("checkBoxSp:") > -1 || datatypes[objEdit.cellIndex]
								.indexOf("radio:") > -1)) {
					if (datatypes[objEdit.cellIndex].toLowerCase() == "textarea") {
						var msWidth, msLeft, msTop;
						var aBox = objEdit;
						msLeft = $(objEdit).offset().left;
						msTop = $(objEdit).offset().top;
//						do {
//							aBox = aBox.offsetParent;
//							msLeft += aBox.offsetLeft-aBox.scrollLeft;
//							msTop += aBox.offsetTop-aBox.scrollTop;
//						} while (aBox != div);
						msWidth = $(objEdit).width();
//						var body_scroll = div.parentNode;
//						if (body_scroll.tagName != "BODY") {
//							do {
//								body_scroll = body_scroll.parentNode;
//							} while (body_scroll.tagName != "DIV"
//									&& body_scroll.tagName != "BODY")
//						}
//						msLeft = msLeft - body_scroll.scrollLeft
//								+ body_scroll.clientLeft;
//						msTop = msTop - body_scroll.scrollTop
//								+ body_scroll.clientTop;
//						msLeft = msLeft
//								- document.getElementById(div.id
//										+ "_body_layout").scrollLeft;
//						msTop = msTop
//								- document.getElementById(div.id
//										+ "_body_layout").scrollTop;
//						if ((msLeft + msWidth) > document.body.offsetWidth)
//							msLeft = document.body.offsetWidth - msWidth - 20;
						var tAreav_ = document.createElement("div");
						tAreav_.id = "grid_textarea_editer";
						tAreav_.style.filter = "alpha(opacity=100)";
						tAreav_.style.opacity = "1";
						tAreav_.style.textAlign = "center";
						tAreav_.style.width = msWidth + "px";
						tAreav_.style.height = "120px";
//						tAreav_.style.background = "#fff";
						tAreav_.style.border = "0px solid #849BCA";
						tAreav_.style.marginTop = "-1px";
//						tAreav_.style.marginLeft = "1px";
						tAreav_.style.padding = "0px";
						tAreav_.style.overflow = "hidden";
						tAreav_.style.position = "fixed";
						tAreav_.style.left = msLeft;
						tAreav_.style.top = msTop;
						tAreav_.style.zIndex = "999";
						var iptArea = document.createElement("textarea");
						iptArea.style.width = "100%";
						iptArea.style.height = "100%";
						var s_v = $(objEdit).text();
						s_v = s_v.toString().replaceAll("&lt;", "<");
						s_v = s_v.toString().replaceAll("&gt;", ">");
						iptArea.value = s_v;
						iptArea.onblur = function() {
							var cellName = grid.gridRelation[objEdit.cellIndex];
							var seditValue = this.value ? this.value : "";
							grid.setValueByName(cellName, grid.CurrentRowId,
									seditValue);
							$("#grid_textarea_editer").remove();
						};
						$("#grid_textarea_editer").remove();
//						document.body.appendChild(tAreav_);
						div.appendChild(tAreav_);
						tAreav_.appendChild(iptArea);
						iptArea.focus();
					}
					return
				} else if (datatypes[objEdit.cellIndex]
						&& datatypes[objEdit.cellIndex].toLowerCase() == "number") {
					input = "<input  id='"
							+ div.id
							+ "_editgridipt' name='editgridipt' type='text' value='"
							+ TDvalue
							+ "' style='width:100%;height:100%;font-size:12px;' "
							+ " onkeydown='tlv8.CheckNumber.valNum(event);' "
							+ " onpaste='tlv8.CheckNumber.valClip(event);'></input>";
					objEdit.innerHTML = input;
					addEvent(document.getElementById(div.id + "_editgridipt"),
							"blur", buttonInputTabkey, false);
					addEvent(document.getElementById(div.id + "_editgridipt"),
							"keydown", function() {
								buttonInputTabkey();
								tlv8.CheckNumber.valNum();
							}, false);
					try {
						document.getElementById(div.id + "_editgridipt")
								.focus();
						document.getElementById(div.id + "_editgridipt")
								.select();
					} catch (e) {
					}
					return
				} else if (datatypes[objEdit.cellIndex]
						&& datatypes[objEdit.cellIndex].toLowerCase() == "month") {
					input = "<input class='Wdate' id='"
							+ div.id
							+ "_editgridipt' name='editgridipt' type='text' value='"
							+ TDvalue
							+ "' style='width:100%;height:100%;font-size:12px;' "
							+ " onClick='WdatePicker({dateFmt:\"MM\",onpicked:function(dp){"
							+ " var editFN = eval(document.getElementById(\""
							+ div.id
							+ "\").grid.editended);editFN(document.getElementById(\""
							+ div.id
							+ "_editgridipt\"))},oncleared:function(dp){"
							+" var editFN = eval(document.getElementById(\""
							+ div.id
							+ "\").grid.editended);editFN(document.getElementById(\""
							+ div.id + "_editgridipt\"))}})'></input>";
					objEdit.innerHTML = input;
					addEvent(document.getElementById(div.id + "_editgridipt"),
							"keydown", buttonInputTabkey, false);
				} else if (datatypes[objEdit.cellIndex]
						&& datatypes[objEdit.cellIndex].toLowerCase() == "yearmonth") {
					input = "<input class='Wdate' id='"
							+ div.id
							+ "_editgridipt' name='editgridipt' type='text' value='"
							+ TDvalue
							+ "' style='width:100%;height:100%;font-size:12px;' "
							+ "onClick='WdatePicker({dateFmt:\"yyyy-MM\",onpicked:function(dp){"
							+ "var editFN = eval(document.getElementById(\""
							+ div.id
							+ "\").grid.editended);editFN(document.getElementById(\""
							+ div.id
							+ "_editgridipt\"))},oncleared:function(dp){"
							+ "var editFN = eval(document.getElementById(\""
							+ div.id
							+ "\").grid.editended);editFN(document.getElementById(\""
							+ div.id + "_editgridipt\"))}})'></input>";
					objEdit.innerHTML = input;
					addEvent(document.getElementById(div.id + "_editgridipt"),
							"keydown", buttonInputTabkey, false);
				} else if (datatypes[objEdit.cellIndex]
						&& datatypes[objEdit.cellIndex].toLowerCase() == "year") {
					input = "<input class='Wdate' id='"
							+ div.id
							+ "_editgridipt' name='editgridipt' type='text' value='"
							+ TDvalue
							+ "' style='width:100%;height:100%;font-size:12px;' "
							+ "onClick='WdatePicker({dateFmt:\"yyyy\",onpicked:function(dp){"
							+ "var editFN = eval(document.getElementById(\""
							+ div.id
							+ "\").grid.editended);editFN(document.getElementById(\""
							+ div.id
							+ "_editgridipt\"))},oncleared:function(dp){"
							+ "var editFN = eval(document.getElementById(\""
							+ div.id
							+ "\").grid.editended);editFN(document.getElementById(\""
							+ div.id + "_editgridipt\"))}})'></input>";
					objEdit.innerHTML = input;
					addEvent(document.getElementById(div.id + "_editgridipt"),
							"keydown", buttonInputTabkey, false);
				} else if (datatypes[objEdit.cellIndex]
						&& datatypes[objEdit.cellIndex].toLowerCase() == "date") {
					input = "<input class='Wdate' id='"
							+ div.id
							+ "_editgridipt' name='editgridipt' type='text' value='"
							+ TDvalue
							+ "' style='width:100%;height:100%;font-size:12px;' "
							+ " onClick='WdatePicker({dateFmt:\"yyyy-MM-dd\",onpicked:function(dp){"
							+ "var editFN = eval(document.getElementById(\""
							+ div.id
							+ "\").grid.editended);editFN(document.getElementById(\""
							+ div.id
							+ "_editgridipt\"))},oncleared:function(dp){"
							+ " var editFN = eval(document.getElementById(\""
							+ div.id
							+ "\").grid.editended);editFN(document.getElementById(\""
							+ div.id + "_editgridipt\"))}})'></input>";
					objEdit.innerHTML = input;
					addEvent(document.getElementById(div.id + "_editgridipt"),
							"keydown", buttonInputTabkey, false);
				} else if (datatypes[objEdit.cellIndex]
						&& datatypes[objEdit.cellIndex].toLowerCase() == "datetime") {
					input = "<input class='Wdate' id='"
							+ div.id
							+ "_editgridipt' name='editgridipt' type='text' value='"
							+ TDvalue
							+ "' style='width:100%;height:100%;font-size:12px;' "
							+ " onClick='WdatePicker({dateFmt:\"yyyy-MM-dd HH:mm:ss\",onpicked:function(dp){"
							+ "var editFN = eval(document.getElementById(\""
							+ div.id
							+ "\").grid.editended);editFN(document.getElementById(\""
							+ div.id
							+ "_editgridipt\"))},oncleared:function(dp){var editFN = eval(document.getElementById(\""
							+ div.id
							+ "\").grid.editended);editFN(document.getElementById(\""
							+ div.id + "_editgridipt\"))}})'></input>";
					objEdit.innerHTML = input;
					addEvent(document.getElementById(div.id + "_editgridipt"),
							"keydown", buttonInputTabkey, false);
				}else if (datatypes[objEdit.cellIndex]
						&& datatypes[objEdit.cellIndex].toLowerCase() == "time") {
					input = "<input class='Wdate' id='"
							+ div.id
							+ "_editgridipt' name='editgridipt' type='text' value='"
							+ TDvalue
							+ "' style='width:100%;height:100%;font-size:12px;' "
							+ " onClick='WdatePicker({dateFmt:\"HH:mm\",onpicked:function(dp){"
							+ "var editFN = eval(document.getElementById(\""
							+ div.id
							+ "\").grid.editended);editFN(document.getElementById(\""
							+ div.id
							+ "_editgridipt\"))},oncleared:function(dp){"
							+ " var editFN = eval(document.getElementById(\""
							+ div.id
							+ "\").grid.editended);editFN(document.getElementById(\""
							+ div.id + "_editgridipt\"))}})'></input>";
					objEdit.innerHTML = input;
					addEvent(document.getElementById(div.id + "_editgridipt"),
							"keydown", buttonInputTabkey, false);
				} else if (datatypes[objEdit.cellIndex]
						&& datatypes[objEdit.cellIndex].toLowerCase().indexOf(
								"select:") > -1) {
					try {
						var selectID = datatypes[objEdit.cellIndex].split(":")[1];
					} catch (e) {
					}
					if (!selectID) {
						alert("指定的类型错误！grid datatype select:id");
						return
					} else {
						var selectElement = document.getElementById(selectID);
						selectElement.style.display = "";
						objEdit.innerHTML = "";
						objEdit.appendChild(selectElement);
						selectElement.value = grid.beforeeditData;
						if (selectElement.tagName == "DIV") {
							document.body.appendChild(selectElement);
							selectElement.style.display = "none";
							objEdit.innerHTML = selectElement.innerHTML;
							try {
								var selectIpt = selectElement
										.getElementsByTagName("INPUT")[0];
								selectIpt.value = grid.beforeeditData;
							} catch (e) {
							}
							if (selectIpt
									&& selectIpt.type.toLowerCase() == "text") {
								objEdit.innerHTML = "";
								objEdit.appendChild(selectElement);
								selectElement.style.display = "";
								var selectIpt = selectElement
										.getElementsByTagName("INPUT")[0];
								selectIpt.value = grid.beforeeditData;
								if (selectElement.master == true) {
									selectElement.onchecked = function() {
										grid.editended(selectIpt, true, true);
									};
								} else {
									selectElement.onselected = function() {
										grid.editended(selectIpt, true, true);
									};
								}
							} else {
								try {
									var selectIpt = objEdit
											.getElementsByTagName("SELECT")[0];
									selectIpt.value = grid.beforeeditData;
								} catch (e) {
								}
								if (selectIpt) {
									selectIpt.onblur = function(event) {
										event = event ? event
												: (window.event ? window.event
														: null);
										var nvalue = this.value;
										var nlabel = $(this).find("option:selected").text();;
										// for ( var n in this.childNodes) {
										// if (this.childNodes[n].value ==
										// nvalue) {
										// nlabel =
										// this.childNodes[n].innerHTML;
										// break;
										// }
										// }
										var name = grid.gridRelation[objEdit.cellIndex];
										var index = grid
												.getgridRelationIndex(name);
										grid.changeValue(Editrow.id, name,
												nvalue, index);
										document.body.appendChild(this);
										//objEdit.innerHTML = nlabel;
										$(objEdit).html("<div class='gridValue' >"+nlabel+"</div>");
										objEdit.title = nvalue;
										$(this).hide();
									};
									selectIpt.focus();
								}
							}
						} else if (selectElement.tagName == "SELECT") {
							selectElement.onblur = function(event) {
								event = event ? event
										: (window.event ? window.event : null);
								var nvalue = this.value;
								var nlabel = $(this).find("option:selected").text();
								// for ( var n in this.childNodes) {
								// if (this.childNodes[n].value == nvalue) {
								// nlabel = this.childNodes[n].innerHTML;
								// break;
								// }
								// }
								var name = grid.gridRelation[objEdit.cellIndex];
								var index = grid.getgridRelationIndex(name);
								grid.changeValue(Editrow.id, name, nvalue,
										index);
								document.body.appendChild(this);
								//objEdit.innerHTML = nlabel;
								$(objEdit).html("<div class='gridValue' >"+nlabel+"</div>");
								objEdit.title = nvalue;
								$(this).hide();
							};
							selectElement.focus();
						} else {
							selectElement.onBlur = function(event) {
								event = event ? event
										: (window.event ? window.event : null);
								grid.editended(event, true);
							};
							selectElement.focus();
						}
						return
					}
					return
				} else if (datatypes[objEdit.cellIndex]
						&& datatypes[objEdit.cellIndex].toLowerCase().indexOf(
								"button:") > -1) {
					try {
						var action = datatypes[objEdit.cellIndex].split(":")[1];
					} catch (e) {
					}
					var buttonClick = function(e) {
						var sFNSA = eval(action);
						var event = e || window.event;
						var objBlur = event.srcElement ? event.srcElement
								: event.target;
						if (objBlur.id == div.id + "_editgridipt") {
							setTimeout(function() {
								if (document.activeElement != document
										.getElementById(div.id
												+ "ipt_buttonClick")) {
									grid.editended(document
											.getElementById(div.id
													+ "_editgridipt"), false);
								} else {
									document.getElementById(
											div.id + "ipt_buttonClick").focus();
								}
							}, 5);
							return
						}
						if (typeof (sFNSA) == "function") {
							sFNSA();
						} else {
							grid.editended(document.getElementById(div.id
									+ "_editgridipt"));
						}
					};
					input = "<input id='"
							+ div.id
							+ "_editgridipt' name='editgridipt' type='text' value='"
							+ TDvalue
							+ "' style='width:80%;height:100%;font-size:12px;'></input>";
					input += "<input type='button' id='"
							+ div.id
							+ "ipt_buttonClick' style='width:20%;height:100%;align:left;' value='...'></input>";
					objEdit.innerHTML = input;
					addEvent(document
							.getElementById(div.id + "ipt_buttonClick"),
							"click", buttonClick, false);
					addEvent(document.getElementById(div.id + "_editgridipt"),
							"blur", buttonClick, false);
					var clickEnd = function() {
						setTimeout(function() {
							if (document.activeElement != document
									.getElementById(div.id + "_editgridipt")) {
								grid.editended(document.getElementById(div.id
										+ "_editgridipt"), false);
							}
						}, 5);
					};
					addEvent(document
							.getElementById(div.id + "ipt_buttonClick"),
							"blur", clickEnd, false);
					addEvent(objEdit, "keydown", buttonInputTabkey, false);
				} else {
					objEdit.innerHTML = input;
					addEvent(document.getElementById(div.id + "_editgridipt"),
							"keydown", buttonInputTabkey, false);
					addEvent(document.getElementById(div.id + "_editgridipt"),
							"blur", buttonInputTabkey, false);
				}
			} else if (datatypes[objEdit.cellIndex]
					&& datatypes[objEdit.cellIndex].toLowerCase().indexOf(
							"required") > -1) {
				"required";
			} else {
				objEdit.innerHTML = input;
				addEvent(document.getElementById(div.id + "_editgridipt"),
						"keydown", buttonInputTabkey, false);
				addEvent(document.getElementById(div.id + "_editgridipt"),
						"blur", buttonInputTabkey, false);
			}
			try {
				document.getElementById(div.id + "_editgridipt").value = TDvalue;
				document.getElementById(div.id + "_editgridipt").focus();
				document.getElementById(div.id + "_editgridipt").select();
			} catch (e) {
			}
		},
		editended : function(event, isselect, isDiv) {
			try {
				var scTag = event.srcElement ? event.srcElement : event.target;
			} catch (e) {
			}
			if (scTag && scTag == "INPUT") {
				var obj = scTag;
			} else {
				event = event ? event : (window.event ? window.event : null);
				var obj = event.tagName ? event
						: event.srcElement ? event.srcElement : event.target;
				var isuIpt = (obj && obj.tagName && obj.tagName == "TD") ? false
						: true;
			}
			if (event.tagName && event.tagName == "INPUT") {
				var obj = event;
			}
			var value = "";
			var TDsrc = null;
			if (isuIpt) {
				value = obj.value;
				TDsrc = obj.parentNode;
				try{
					EditrowID = TDsrc.parentNode.id;
				}catch (e) {
					return;
				}
			} else {
				value = $(obj).text();
				TDsrc = obj;
			}
			TDsrc = (isDiv) ? TDsrc.parentNode : TDsrc;
			var EditrowID = TDsrc.parentNode.id;
			var dcodeV = value.toString();
			var ds_val = grid.encodeValue(dcodeV);
			var datatypes = datatype.split(",");
			if (datatypes[TDsrc.cellIndex] == "number") {
				if (ds_val != "") {
					ds_val = ds_val.toString().replaceAll(",", "");
				}
			}
			if (!grid.beforeeditData || value != grid.beforeeditData) {
				if (grid.editDataRowIds
						&& grid.editDataRowIds.indexOf(EditrowID) < 0) {
					grid.editDataRowIds += "," + EditrowID;
				} else if (!grid.editDataRowIds || grid.editDataRowIds == "") {
					grid.editDataRowIds = EditrowID;
				}
				var isChanged = false;
				var oldValue = "";
				this.editedDatas = grid.editedDatas ? grid.editedDatas
						: new Map();
				if (grid.editedDatas.containsKey(EditrowID)) {
					isChanged = true;
					oldValue = grid.editedDatas.get(EditrowID);
				}
				if (isChanged) {
					grid.editedDatas.remove(EditrowID);
					if (oldValue.indexOf("<"
							+ grid.gridRelation[TDsrc.cellIndex] + ">") >= 0) {
						var intd = oldValue.indexOf("<"
								+ grid.gridRelation[TDsrc.cellIndex] + ">")
								+ grid.gridRelation[TDsrc.cellIndex].length + 2;
						var len = oldValue.indexOf("</"
								+ grid.gridRelation[TDsrc.cellIndex] + ">");
						var beforeData = oldValue.substring(0, intd);
						var afterData = oldValue
								.substring(len, oldValue.length);
						var oldVa = oldValue.substring(intd, len);
						var newValue = beforeData + ds_val + afterData;
						grid.editedDatas.put(EditrowID, newValue);
					} else {
						grid.editedDatas.put(EditrowID, oldValue + "<"
								+ grid.gridRelation[TDsrc.cellIndex] + ">"
								+ ds_val + "</"
								+ grid.gridRelation[TDsrc.cellIndex] + ">");
					}
				} else {
					grid.editedDatas.put(EditrowID, "<"
							+ grid.gridRelation[TDsrc.cellIndex] + ">" + ds_val
							+ "</" + grid.gridRelation[TDsrc.cellIndex] + ">");
				}
			}
			if (isselect) {
				if (isDiv) {
					obj.parentNode.style.display = "none";
					document.body.appendChild(obj.parentNode);
				} else {
					obj.style.display = "none";
					document.body.appendChild(obj);
				}
			}
			var celloldValue = grid.beforeeditData;
			$(TDsrc).html("<div class='gridValue' >"+dcodeV+"</div>");
			try {
				grid.Value.put(EditrowID + TDsrc.cellIndex, value);
			} catch (e) {
			}
			var dataValueChanged = div.getAttribute("dataValueChanged");
			if (dataValueChanged && (celloldValue != value)) {
				var dataValueChangedFn = eval(dataValueChanged);
				var vData = {
					olddata : celloldValue,
					newdata : value,
					cellname : grid.gridRelation[TDsrc.cellIndex],
					obj : TDsrc,
					rowid : EditrowID
				};
				if (typeof (dataValueChangedFn) == "function") {
					dataValueChangedFn(vData);
				}
			}
			if (grid.toolbar.saveItem != false && celloldValue != value)
				grid.settoolbar("no", true, "no", "no");
		},
		changeValue : function(EditrowID, relation, value, index) {
			var oldValue = "";
			var celloldValue = grid.Value.get(EditrowID + index);
			var dcodeV = value.toString();
			var ds_val = grid.encodeValue(dcodeV);
			var datatypes = datatype.split(",");
			if (datatypes[index] == "number") {
				if (ds_val != "") {
					ds_val = ds_val.toString().replaceAll(",", "");
				}
			}
			if (!grid.beforeeditData || value != grid.beforeeditData) {
				if (grid.editDataRowIds
						&& grid.editDataRowIds.indexOf(EditrowID) < 0) {
					grid.editDataRowIds += "," + EditrowID;
				} else if (!grid.editDataRowIds || grid.editDataRowIds == "") {
					grid.editDataRowIds = EditrowID;
				}
				var isChanged = false;
				this.editedDatas = grid.editedDatas ? grid.editedDatas
						: new Map();
				if (grid.editedDatas.containsKey(EditrowID)) {
					isChanged = true;
					oldValue = grid.editedDatas.get(EditrowID);
				}
				if (isChanged) {
					if (oldValue.indexOf("<" + relation + ">") >= 0) {
						var intd = oldValue.indexOf("<" + relation + ">")
								+ relation.length + 2;
						var len = oldValue.indexOf("</" + relation + ">");
						var beforeData = oldValue.substring(0, intd);
						var afterData = oldValue
								.substring(len, oldValue.length);
						var oldVa = oldValue.substring(intd, len);
						var newValue = beforeData + ds_val + afterData;
						grid.editedDatas.put(EditrowID, newValue);
					} else {
						grid.editedDatas.put(EditrowID, oldValue + "<"
								+ relation + ">" + ds_val + "</" + relation
								+ ">");
					}
				} else {
					grid.editedDatas.put(EditrowID, "<" + relation + ">"
							+ ds_val + "</" + relation + ">");
				}
			}
			try {
				grid.Value.put(EditrowID + index, value);
			} catch (e) {
			}
			if (celloldValue != value) {
				var dataValueChanged = div.getAttribute("dataValueChanged");
				if (dataValueChanged) {
					var dataValueChangedFn = eval(dataValueChanged);
					var editCell;
					try {
						var Editrow = document.getElementById(EditrowID);
						editCell = Editrow.cells(index);
					} catch (e) {
					}
					var vData = {
						olddata : celloldValue,
						newdata : value,
						cellname : relation,
						obj : editCell,
						rowid : EditrowID
					};
					if (typeof (dataValueChangedFn) == "function") {
						dataValueChangedFn(vData);
					}
				}
			}
			if (grid.toolbar.saveItem != false && value != grid.beforeeditData)
				grid.settoolbar("no", true, "no", "no");
		},
		encodeValue :function(val){
			var dcodeV = val.toString().replaceAll("<", "#lt;");
			dcodeV = dcodeV.replaceAll(">", "#gt;");
			dcodeV = dcodeV.replaceAll("\"", "#quot;");
			dcodeV = dcodeV.replaceAll("'", "#apos;");
			dcodeV = dcodeV.replaceAll("&", "#amp;");
			return dcodeV;
		},
// refreshData : function() {
// },
// insertData : function() {
// },
// saveData : function() {
// },
// deleteData : function() {
// },
		advancedQueryData : function() {
			var queryUrl = "/comon/gridFilter/FilterSet.html?uuid="
					+ new UUID().toString();
			var sendParam = {
				labelids : labelid,
				labels : labels,
				labelwidth : labelwidth
			};
			tlv8.portal.dailog.openDailog('高级查询', queryUrl, 700, 425,
					div.grid.advancedQueryDataBack, null, true, sendParam);
			if (document.body.clientWidth < 700) {
				try {
					document.getElementById("dialogTilteMax").click();
					document.getElementById("dialogTilteNor").onclick = null;
					document.getElementById("dialogTilteMin").onclick = null;
				} catch (e) {
				}
			}
		},
		advancedQueryDataBack : function(re) {
			if (re && typeof re == "string") {
				grid.refreshData(re);
			}
		},
		CurrentRowId : "",
		/**
		 * @function
		 * @name getCurrentRowId
		 * @description 获取当前选中行的rowid
		 * @returns {string}
		 */
		getCurrentRowId : function() {
			return this.CurrentRowId;
		},
		sFilter : "",
		queryAction : "",
		savAction : "",
		deleteAction : "",
		sAlert : true,
		setDatatoGrid : function(r) {
			if(!r.gridid || r.gridid == ""){
				return;
			}
			var msessage = "操作成功!";
			if (r.flag == "true") {
				try {
					var grid_view_id = r.gridid;
					var grid = document.getElementById(grid_view_id).grid;
					var griddataRows = r.data;
					if(r.page>r.allpage){
						grid.toPage(1);
						return;
					}
					$("#"+grid_view_id + "_main_grid").html(griddataRows);
					$("#"+div.id+"_grid_label").fixTable({
					 	fixColumn: grid.fixColumn || 0,// 固定列数
					 	fixColumnBack: grid.fixColumnBack || "#ccc",// 固定列数
						width:  $("#"+div.id+"_body_layout").width(),// 显示宽度
						height: $("#"+div.id+"_body_layout").height()// 显示高度
					});
					if (grid.gridFooterRow) {
						$("#"+div.id+"_underline").remove();
						$("#"+div.id+"_grid_label_fixTableMain").append(grid.gridFooterRow);
						// document.getElementById(grid_view_id +
						// "_main_grid").parentNode.parentNode.innerHTML +=
						// grid.gridFooterRow;
					}
					document.getElementById(grid_view_id + "_page").value = r.page;
					document.getElementById(grid_view_id + "_page").title = r.page;
					document.getElementById(grid_view_id + "_pages").innerHTML = "第"
							+ r.page + "/" + r.allpage + "页";
					document.getElementById(grid_view_id + "_pages").title = "第"
							+ r.page + "/" + r.allpage + "页";
					grid.CurrentPage = r.page;
					grid.setPageBar(r.page, r.allpage);
					try {
						grid.RowId = new Array();
						grid.Value = new Map();
						isresetCell = false;
						grid.changelay(grid_view_id);
					} catch (e) {
					}
				} catch (e) {
					msessage.toString();
				}
			} else {
				console.log(r);
				alert(r.message);
				msessage = r.message;
				grid.CurrentRowId = "";
			}
			initTtableview();
			try {
				grid.checkedRowIds = "";
				document.getElementById(div.id + "_grid_master_check").checked = false;
			} catch (e) {
			}
			var afterRefresh = div.getAttribute("afterRefresh");
			if (afterRefresh) {
				var arinFn = eval(afterRefresh);
				if (typeof (arinFn) == "function") {
					arinFn(grid);
				}
			}
			try {
				var currentRowID = grid.CurrentRowId;
				var online = document.getElementById(currentRowID);
				if (!online || online.tagName != "TR") {
					var dataTtr = document.getElementById(div.id + "_main_grid")
							.getElementsByTagName("tr");
					online = dataTtr[0];
				}
				online.click();
			} catch (e) {
			}
			try{
				initTable(document.getElementById(div.id + "_tab_title"));// 表头事件
			}catch (e) {
				alert(e.message);
			}
		},
		setDeleteMessage : function(r) {
			var msessage = "操作成功!";
			if (r.flag == "true") {
				msessage = "操作成功!";
			} else {
				msessage = r.message;
			}
			sAlert(msessage, 500);
		},
		checkAll : function() {
			var el = div.getElementsByTagName('input');
			for ( var i = 1; i < el.length; i++) {
				if ((el[i].type == "checkbox")
						&& (el[i].id.indexOf("_checkbox") > 0)) {
					el[i].checked = true;
				}
			}
			try{
				document.getElementById(div.id + "_grid_master_check").checked = true;
			}catch(e){
			}
			grid.checkedRowIds = "";
			var allRowId = grid.RowId;
			grid.checkedRowIds = allRowId.join(",");
			if (div.getAttribute("oncheckAll")) {
				var chFn = eval(div.getAttribute("oncheckAll"));
				if (typeof (chFn) == "function") {
					var rEvnt = grid;
					rEvnt.checked = true;
					chFn(rEvnt);
				}
			}
		},
		uncheckAll : function() {
			var el = div.getElementsByTagName('input');
			for ( var i = 1; i < el.length; i++) {
				if ((el[i].type == "checkbox")
						&& (el[i].id.indexOf("_checkbox") > 0)) {
					el[i].checked = false;
				}
			}
			try{
				document.getElementById(div.id + "_grid_master_check").checked = false;
			}catch(e){
			}
			if (div.getAttribute("onuncheckAll")) {
				var chFn = eval(div.getAttribute("onuncheckAll"));
				if (typeof (chFn) == "function") {
					var rEvnt = grid;
					rEvnt.checked = false;
					chFn(rEvnt);
				}
			}
			grid.checkedRowIds = "";
		},
		masterselect : function(event) {
			event = event ? event : (window.event ? window.event : null);
			var SOL = event.srcElement ? event.srcElement : event.target;
			var state = (SOL.checked == true) ? true : false;
			var el = div.getElementsByTagName('input');
			var len = el.length;
			for ( var i = 1; i < len; i++) {
				if ((el[i].type == "checkbox")
						&& (el[i].id.indexOf("_checkbox") > 0)) {
					el[i].checked = state;
				}
			}
			if (state) {
				grid.checkedRowIds = "";
				var allRowId = grid.RowId;
				grid.checkedRowIds = allRowId.join(",");
				if (div.getAttribute("oncheckAll")) {
					var chFn = eval(div.getAttribute("oncheckAll"));
					if (typeof (chFn) == "function") {
						var rEvnt = grid;
						rEvnt.checked = state;
						chFn(rEvnt);
					}
				}
			} else {
				if (div.getAttribute("onuncheckAll")) {
					var chFn = eval(div.getAttribute("onuncheckAll"));
					if (typeof (chFn) == "function") {
						var rEvnt = grid;
						rEvnt.checked = state;
						chFn(rEvnt);
					}
				}
				grid.checkedRowIds = "";
			}
		},
		checkedRowIds : "",
		getCheckedRowIds : function() {
			return this.checkedRowIds;
		},
		checkRows : function(event) {
			event = event ? event : (window.event ? window.event : null);
			var SOL = event.srcElement ? event.srcElement : event.target;
			var srID = SOL.id.replace("_checkbox", "");
			var state = (SOL.checked == true) ? true : false;
			if (state) {
				if (!tlv8.String.isin(grid.checkedRowIds, srID)) {
					grid.checkedRowIds += "," + srID;
					grid.checkedRowIds = replaceFirst(grid.checkedRowIds, ",","");
				}
			} else {
				if (tlv8.String.isin(grid.checkedRowIds, "," + srID)){
					grid.checkedRowIds = grid.checkedRowIds.replace("," + srID,
							"");
				}else if (tlv8.String.isin(grid.checkedRowIds, srID)){
					grid.checkedRowIds = grid.checkedRowIds.replace(srID, "");
				}
				$(div).find("input[id='"+div.id+"_grid_master_check']").removeAttr("checked");
			}
			grid.checkedRowIds = replaceFirst(grid.checkedRowIds, ",", "");
			if (div.getAttribute("onchecked")) {
				var chFn = eval(div.getAttribute("onchecked"));
				if (typeof (chFn) == "function") {
					var rEvnt = grid;
					rEvnt.checked = state;
					rEvnt.rowid = srID;
					chFn(rEvnt);
				}
			}
		},
		/**
		 * 设置选择
		 * @rowid 行ID
 		 * @state 选择状态[true/false]
 		 * @triggercal 是否需要触发选择事件（默认不触发）
		 */
		setRowChecked : function(rowid, state, triggercal){
			if(state){
				if (!tlv8.String.isin(grid.checkedRowIds, rowid)) {
					grid.checkedRowIds += "," + rowid;
					grid.checkedRowIds = replaceFirst(grid.checkedRowIds, ",","");
				}
				$(div).find("input[id='"+rowid+"_checkbox']").attr("checked","checked");
			}else{
				if (tlv8.String.isin(grid.checkedRowIds, "," + rowid)){
					grid.checkedRowIds = grid.checkedRowIds.replace("," + rowid,
							"");
				}else if (tlv8.String.isin(grid.checkedRowIds, rowid)){
					grid.checkedRowIds = grid.checkedRowIds.replace(rowid, "");
				}
				$(div).find("input[id='"+rowid+"_checkbox']").removeAttr("checked");
				$(div).find("input[id='"+div.id+"_grid_master_check']").removeAttr("checked");
			}
			grid.checkedRowIds = replaceFirst(grid.checkedRowIds, ",", "");
			
			if(triggercal){
				grid.checkedRowIds = replaceFirst(grid.checkedRowIds, ",", "");
				if (div.getAttribute("onchecked")) {
					var chFn = eval(div.getAttribute("onchecked"));
					if (typeof (chFn) == "function") {
						var rEvnt = grid;
						rEvnt.checked = state;
						rEvnt.rowid = rowid;
						chFn(rEvnt);
					}
				}
			}
		},
		gridFooterRow : "",
		addFooterrow : function(param) {
			var cellsText = param.split(",");
			var labelwidths = labelwidth.split(',');
			var gridFooterLine = "<div class='grid_underline' style='";
			if(tlv8.isIE() && !tlv8.islowIE10()){
				gridFooterLine += " margin-left:-2px;";
			}else if(!tlv8.isIE()){
				gridFooterLine += " margin-left:-1px;";
			}
			gridFooterLine += "' id='"+div.id+"_underline'><table class='grid' id='"
					+ div.id
					+ "_footer_line' style='width:100%;height:100%;margin-left:1px;'><tr class='grid_tr_underline'>";
			var ind = 0;
			for ( var s = 0; s < cellsText.length; s++) {
				var celop = cellsText[s];
				var celltextSp = celop.split(":colspan");
				var celltext = celltextSp[0];
				var cellSpan = "";
				var celwidth = parseInt(labelwidths[ind]);
				if(tlv8.islowIE10()){
					celwidth += 1;
				}
				if (celltextSp.length > 0) {
					cellSpan = celltextSp[1];
					if (cellSpan && cellSpan != "") {
						var spn = cellSpan.substring(cellSpan.indexOf("(") + 1,
								cellSpan.indexOf(")"));
						for ( var i = 1; i < parseInt(spn); i++) {
							celwidth = celwidth + parseInt(labelwidths[s + i]);
							ind++;
						}
						gridFooterLine += "<td class='grid_td_underline' colspan='"
								+ spn
								+ "'>"
								+ celltext + "</td>";
					} else {
						gridFooterLine += "<td class='grid_td_underline'>" + celltext + "</td>";
					}
				} else {
					gridFooterLine += "<td class='grid_td_underline'>" + celltext + "</td>";
				}
				ind++;
			}
			gridFooterLine += "<td class='grid_td_underline'>&nbsp;</td></tr></table></div>";
			grid.gridFooterRow = gridFooterLine;
		},
		quickSearch : function(text) {
			var sfilter = "";
			var labelids = labelid.split(",");
			var datatypes = datatype.split(",");
			for ( var i = 0; i < labelids.length; i++) {
				if (labelids[i] != "No" && labelids[i] != "master_check"
						&& datatypes[i] != "date" && datatypes[i] != "datetime") {
					sfilter += "or upper(" + labelids[i] + ") like upper('%"
							+ text + "%') ";
				}
			}
			sfilter = sfilter.replaceFirst("or", "");
			grid.refreshData(sfilter);
		},
		showGridTextArea : function(event) {
			event = event ? event : (window.event ? window.event : null);
			var objEdit = event.srcElement ? event.srcElement : event.target;
			if (objEdit.tagName == "INPUT") {
				return
			} else {
				grid.outEditBlur();
			}
			if(objEdit.tagName == "DIV"){
				objEdit = objEdit.parentNode;
			}
			var Editrow = objEdit.parentNode;
			var TDvalue = $(objEdit).text() ? $(objEdit).text() : "";
			if (objEdit.cellIndex >= Editrow.getElementsByTagName("td").length - 1)
				return;
			try {
				var valDocument = objEdit.childNodes[0];
				if (valDocument.tagName == "INPUT") {
					return
				}
			} catch (e) {
			}
			if (datatype && datatype != "") {
				var datatypes = datatype.split(",");
				if (datatypes[objEdit.cellIndex]
						&& datatypes[objEdit.cellIndex].toLowerCase() == "textarea") {
					tlv8.portal.dailog.openDailog("详细信息",
							"/comon/gridTextView/textarea.html", 350, 280,
							null, {
								refreshItem : false,
								enginItem : false,
								CanclItem : true
							}, null, TDvalue);
				}
			}
		}
	};
	grid.sql = CryptoJS.AESEncrypt(sql);
	var cqgrid = {};
	grid.rechangelay = function(obj) {
		try {
			var rowIndex = obj.rowIndex;
			var Ttd = obj.getElementsByTagName("td");
			var tbody = obj.parentNode;
			var nextTr = tbody.getElementsByTagName("tr")[1];
			if (!nextTr)
				return;
			if (rowIndex == "0") {
				var nTtd = nextTr.getElementsByTagName("td");
				for ( var i = 0; i < Ttd.length; i++) {
					nTtd[i].style.width = Ttd[i].style.width;
					nTtd[i].id = Ttd[i].id;
				}
				if (tbody.rows.length > 1) {
					var seeRow = tbody.rows[rowIndex + 1];
					try {
						seeRow.click();
					} catch (e) {
					}
				}
			} else {
				var seeRow = tbody.rows[rowIndex - 1];
				try {
					seeRow.click();
				} catch (e) {
				}
			}
		} catch (e) {
		}
	};
	grid.changelay = function(divid, uncellRead, chlayid) {
		var gird_view_id = (divid && divid != "") ? divid : div.id;
		try {
			var Ptr = document.getElementById(gird_view_id + "_main_grid")
					.getElementsByTagName("tr");
		} catch (e) {
		}
		grid.Length = 0;
		if (Ptr && Ptr.length > 0) {
			var Ptd = Ptr[0].getElementsByTagName("td");
			var Ttr = document.getElementById(gird_view_id + "_thead")
					.getElementsByTagName("tr");
			var Ttd = Ttr[0].getElementsByTagName("td");
			cqgrid.currenttr = null;
			cqgrid.currentRowId = null;
			cqgrid.dbclick = function(rowId) {
				if (parent.griddbclick)
					parent.griddbclick(rowId);
			};
			for (i = 1; i < Ptr.length + 1; i++) {
				Ptr[i - 1].className = (i % 2 > 0) ? "t1" : "t2";
				if (!isresetCell)
					grid.RowId.push(Ptr[i - 1].id);
				var Ptds = Ptr[i - 1].getElementsByTagName("td");
				var chTD = Ptr[i - 1].getElementsByTagName("td")[0];
				var scCheck = chTD.getElementsByTagName("input")[0];
				if (scCheck && scCheck != "") {
					addEvent(scCheck, "click", grid.checkRows, false);
				}
				if (!isresetCell) {
					var forlength = Ptds.length - 1;
					if (grid.editModel != true && i > 400 && forlength > 2) {
						forlength = 2;
					} else if (grid.editModel == true && i > 100) {
						mAlert("grid编辑模式下，单页数据不宜超过100行!");
						break;
					}
					for ( var j = 0; j < forlength; j++) {
						if (uncellRead == true) {
							break;
						} else if (chlayid && chlayid != ""
								&& chlayid != Ptr[i - 1].id) {
							break;
						}
						grid.Value.put(Ptr[i - 1].id + j, $(Ptds[j]).text());
						var datatypes = datatype.split(",");
						var labelids = labelid.split(",");
						if (datatypes[j].indexOf("html:") > -1) {
							var readerFn = eval(datatypes[j].split(":")[1]);
							if (readerFn && typeof (readerFn) == "function") {
								var htmllabel = readerFn( {
									rowid : Ptr[i - 1].id,
									value : $(Ptds[j]).text(),
									gridCom : grid,
									cellname : labelids[j]
								});
								Ptds[j].innerHTML = "<div class='gridValue' >"+htmllabel+"</div>";
							}
						}
						if (datatypes[j].indexOf("checkBox:") > -1) {
							var readerMap = eval(datatypes[j].split(":")[1]);
							if (readerMap && typeof (readerMap) != "string") {
								var checkComp = "<div>";
								var kys = readerMap.keySet();
								var checkboxinited = function() {
									var chs = Ptds[j]
											.getElementsByTagName("input");
									if (chs.length > 0)
										return true;
									else
										return false;
								};
								if (checkboxinited())
									continue;
								var thisvalue = $(Ptds[j]).text();
								var splValue = thisvalue.split(",");
								var isCheckVal = function(str) {
									for ( var s = 0; s < splValue.length; s++) {
										if (splValue[s] == str)
											return true;
									}
									return false;
								};
								for (k in kys) {
									var sk = (isCheckVal(kys[k])) ? "checked = true"
											: "";
									checkComp += "<input type='checkbox' onclick='javascript:{var rdv = this.parentNode; "
											+ " var checkedValue = \"\";var checks = rdv.getElementsByTagName(\"input\");"
											+ " for(var i=0;i<checks.length;i++){ " 
											+ "var rd = checks[i]; if(rd.checked)	checkedValue += \",\"+rd.value;" 
											+ "}checkedValue = checkedValue.replaceFirst(\",\",\"\");"
											+ " var grid_ = document.getElementById(\""
											+ gird_view_id
											+ "\").grid; grid_.changeValue(\""
											+ Ptr[i - 1].id
											+ "\",\""
											+ grid.gridRelation[j]
											+ "\",checkedValue,"
											+ j
											+ ");}'  value='"
											+ kys[k]
											+ "' "
											+ sk
											+ ">"
											+ readerMap.get(kys[k])
											+ "</input>";
								}
								checkComp += "</div>";
								Ptds[j].innerHTML = checkComp;
							}
						}
						if (datatypes[j].indexOf("checkBoxSp:") > -1) {
							var readerMap = eval(datatypes[j].split(":")[1]);
							if (readerMap && typeof (readerMap) != "string") {
								var checkComp = "<div>";
								var kys = readerMap.keySet();
								var checkboxinited = function() {
									var chs = Ptds[j]
											.getElementsByTagName("input");
									if (chs.length > 0)
										return true;
									else
										return false;
								};
								if (checkboxinited())
									continue;
								var thisvalue = $(Ptds[j]).text();
								var splValue = thisvalue.split(" ");
								var isCheckVal = function(str) {
									for ( var s = 0; s < splValue.length; s++) {
										if (splValue[s] == str)
											return true;
									}
									return false;
								};
								for (k in kys) {
									var sk = (isCheckVal(kys[k])) ? "checked = true"
											: "";
									checkComp += "<input type='checkbox' onclick='javascript:{var rdv = this.parentNode;"
											+ " var checkedValue = \"\";var checks = rdv.getElementsByTagName(\"input\");"
											+ " for(var i=0;i<checks.length;i++){ var rd = checks[i]; "
											+ " if(rd.checked)	checkedValue += \" \"+rd.value;}"
											+ " checkedValue = checkedValue.replaceFirst(\",\",\"\");"
											+ " var grid_ = document.getElementById(\""
											+ gird_view_id
											+ "\").grid; grid_.changeValue(\""
											+ Ptr[i - 1].id
											+ "\",\""
											+ grid.gridRelation[j]
											+ "\",checkedValue,"
											+ j
											+ ");}'  value='"
											+ kys[k]
											+ "' "
											+ sk
											+ ">"
											+ readerMap.get(kys[k])
											+ "</input>";
								}
								checkComp += "</div>";
								Ptds[j].innerHTML = checkComp;
							}
						}
						if (datatypes[j].indexOf("radio:") > -1) {
							var readerMap = eval(datatypes[j].split(":")[1]);
							if (readerMap && typeof (readerMap) != "string") {
								var checkComp = "<div>";
								var kys = readerMap.keySet();
								var checkboxinited = function() {
									var chs = Ptds[j]
											.getElementsByTagName("input");
									if (chs.length > 0)
										return true;
									else
										return false;
								};
								if (checkboxinited())
									continue;
								var thisvalue = $(Ptds[j]).text();
								for (k in kys) {
									var sk = (thisvalue == kys[k]) ? "checked = true"
											: "";
									checkComp += "<input type='radio' onclick='javascript:{var rdv = this.parentNode;"
											+ "var radios = rdv.getElementsByTagName(\"input\");"
											+ "for(var i=0;i<radios.length;i++){ var rd = radios[i]; if(rd!=this)rd.checked = false;} "
											+ "this.checked=true; var grid_1 = document.getElementById(\""
											+ gird_view_id
											+ "\").grid; grid_1.changeValue(\""
											+ Ptr[i - 1].id
											+ "\",\""
											+ grid.gridRelation[j]
											+ "\",this.value,"
											+ j
											+ ");}' value='"
											+ kys[k]
											+ "' "
											+ sk
											+ ">"
											+ readerMap.get(kys[k])
											+ "</input>";
								}
								checkComp += "</div>";
								Ptds[j].innerHTML = checkComp;
							}
						}
						if (datatypes[j].indexOf("select:") > -1) {
							try {
								var selectID = datatypes[j].split(":")[1];
								var selectObj = document
										.getElementById(selectID);
								if (selectObj.tagName == "DIV") {
									selectObj = selectObj
											.getElementsByTagName("SELECT")[0];
								}
								var nvalue = Ptds[j].innerText;
								// selectObj.value = nvalue;
								$(selectObj).val(trim(nvalue));
								var nlabel = $(selectObj).find("option:selected").text();
								// for ( var n in selectObj.childNodes) {
								// if (selectObj.childNodes[n].value == nvalue)
								// {
								// nlabel = selectObj.childNodes[n].innerHTML;
								// break;
								// }
								// }
								var name = grid.gridRelation[j];
								var dcodeV = nlabel || nvalue;
								$(Ptds[j]).html("<div class='gridValue' >"+dcodeV+"</div>");
							} catch (e) {
							}
						}
					}
				}
				addEvent(
						Ptr[i - 1],
						"click",
						function(event) {
							if (cqgrid.currentRowId == this.id && this.className == "t3") {
								// TODO:当颜色已经是选中颜色时，不做改变。
							}else{
								this.tmpClass = this.className;
								this.className = "t3";
							}
							if (cqgrid.currenttr && cqgrid.currenttr!=this) {
								cqgrid.currenttr.className = cqgrid.currenttr.tmpClass;
							}
							cqgrid.currenttr = this;
							cqgrid.currentRowId = this.id;
							grid.CurrentRowId = this.id;
							if (data.formid && data.formid != "") {
								try {
									var mainForm = document
											.getElementById(data.formid);
									mainForm.rowid = cqgrid.currentRowId;
									$(mainForm).attr("rowid", cqgrid.currentRowId);
								} catch (e) {
								}
							}
							if (div.getAttribute("onselected")
									&& div.getAttribute("onselected") != "") {
								var clcikFN = eval(div
										.getAttribute("onselected"));
								if (typeof (clcikFN) == "function")
									clcikFN(grid);
							}
							if (grid.editModel == true) {
								try {
									grid.editData(event, this);
								} catch (e) {
								}
							} else {
								try {
									grid.showGridTextArea(event);
								} catch (e) {
								}
							}
						}, false);
				addEvent(
						Ptr[i - 1],
						"dblclick",
						function(event) {
							if (cqgrid.currentRowId != this.id) {
								this.tmpClass = this.className;
								this.className = "t3";
								if (cqgrid.currenttr) {
									cqgrid.currenttr.className = cqgrid.currenttr.tmpClass;
								}
								cqgrid.currenttr = this;
								cqgrid.currentRowId = this.id;
								grid.CurrentRowId = this.id;
							}
							if (grid.editModel != true
									&& "dbclcik" != grid.editModel) {
								var ondbclick = div.getAttribute("ondbclick");
								if (ondbclick) {
									var dnFN = eval(ondbclick);
								}
								if (typeof (dnFN) == "function")
									dnFN(grid);
							} else if ("dbclcik" == grid.editModel) {
								grid.editData(event, this);
							}
						}, false);
				try {
					var onRowInit = div.getAttribute("onRowInit");
					if (onRowInit) {
						var onRowInitdnFN = eval(onRowInit);
					}
					if (typeof (onRowInitdnFN) == "function")
						onRowInitdnFN(grid, Ptr[i - 1]);
				} catch (e) {
				}
			}
			grid.Length = Ptr.length;
			for ( var i = 0; i < Ptd.length; i++) {
				Ptd[i].style.width = Ttd[i].style.width;
				Ptd[i].id = Ttd[i].id + "P";
			}
		} else {
			grid.CurrentRowId = "";
		}
		var gFooterLine = document.getElementById(gird_view_id
				+ "_footer_line");
		$(gFooterLine).width($("#"+gird_view_id+"_grid_label_fixTableHeader").find("table").width());
		try {
			var headTr = document.getElementById(gird_view_id+"_tab_title");
			var headTtd = headTr.getElementsByTagName("td");
			var gFtr = gFooterLine.getElementsByTagName("tr")[0];
			var gfTD = gFtr.getElementsByTagName("td");
			var sp = 0;
			for ( var l = 0; l < gfTD.length; l++) {
				var spn = gfTD[l].getAttribute("colspan") ? gfTD[l]
						.getAttribute("colspan") : 1;
				var scelWidth = 0;
				if(tlv8.islowIE10()){
					scelWidth = 1;
				}
				for ( var j = 0; j < spn; j++) {
					if (parseInt($(headTtd[sp]).width()))
						scelWidth = scelWidth
								+ parseInt($(headTtd[sp]).width());
					if(j<spn-2){
						scelWidth += 1;
					}
					sp++;
				}
				if (l == gfTD.length-1){
					gfTD[l].style.width = "";
				}else{
					$(gfTD[l]).width(scelWidth);
				}
			}
		} catch (e) {
		}
		grid.resizeGrid();
	};
	grid.resizeGrid = function() {
		try {
			var cuRow = document.getElementById(grid.CurrentRowId);
			cuRow.tmpClass = cuRow.className;
			cuRow.className = "t3";
			if (cqgrid.currenttr) {
				cqgrid.currenttr.className = cqgrid.currenttr.tmpClass;
			}
			cqgrid.currenttr = cuRow;
			cqgrid.currentRowId = cuRow.id;
			grid.CurrentRowId = cuRow.id;
			if (data.formid && data.formid != "") {
				try {
					var mainForm = document.getElementById(data.formid);
					mainForm.rowid = cqgrid.currentRowId;
				} catch (e) {
				}
			}
		} catch (e) {
		}
	};
	var element = null;
	var Ttableview = null;
	var isresetCell = false;
	function initTable(_tr) {
		element = document.getElementById(div.id + "_grid_label");
		$("#"+div.id + "_splitLine").remove();
		var _line = window.document.createElement("DIV");
		_line.id = div.id + "_splitLine";
		_line.style.position = "absolute";
		_line.style.backgroundColor = "#c5c5c5";
		_line.style.width = 1;
		window.document.body.appendChild(_line);
		element.splitLine = _line;
		element.splitLine.style.display = "none";
		addEvent(_tr, "mousedown", fnMousedown, false);
		addEvent(_tr, "mousemove", fnMousemove, false);
		addEvent(_tr, "mouseover", fnMouseover, false);
		addEvent(_tr, "selectstart", fnCancel, false);
		addEvent(window.document, "mouseup", fnMouseup, false);
		addEvent(window.document, "mousemove", fnMouseMove, false);
	}
	function addEvent(elm, evType, fn, useCapture) {
		if(!elm)return;
		try {
			elm['on' + evType] = fn;
		} catch (e) {
			if (elm.addEventListener) {
				elm.addEventListener(evType, fn, useCapture);
				return true;
			} else if (elm.attachEvent) {
				var r = elm.attachEvent('on' + evType, fn);
				return r;
			}
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
	function initTtableview() {
		var _line = document.getElementById(div.id + "_splitLine");
		var Ttableview = document.getElementById(div.id + "_maintable");
		if (!Ttableview)
			return;
		Ttableview.splitLine = _line;
		Ttableview.splitLine.style.display = "none";
	}
	function fnMouseover() {
		return
	}
	function fnMouseMove() {
		if (!element.splitlocked)
			return;
		fnMousemove();
	}
	function mouseCoords(ev) {
		if (ev.pageX || ev.pageY) {
			return {
				x : ev.pageX,
				y : ev.pageY
			};
		}
		return {
			x : ev.clientX + document.body.scrollLeft
					- document.body.clientLeft,
			y : ev.clientY + document.body.scrollTop - document.body.clientTop
		};
	}
	function fnMousemove(event) {
		event = event ? event : (window.event ? window.event : null);
		var oEl = event.srcElement ? event.srcElement : event.target;
		element.splitLine.style.left = mouseCoords(event).x;
		element.splitLine.style.top = getTop(element);
		element.splitLine.style.height = element.parentNode.clientHeight;
		if (element.splitlocked)
			return;
		if (!IfSplitLocation(event, oEl))
			return
	}
	function fnClick(event) {
		event = event ? event : (window.event ? window.event : null);
		var oEl = event.srcElement ? event.srcElement : event.target;
	}
	function fnMousedown(event) {
		event = event ? event : (window.event ? window.event : null);
		var oEl = event.srcElement ? event.srcElement : event.target;
		if($(oEl).attr("unresize")){
			return;
		}
		addEvent(window.document, "mouseup", fnMouseup, false);
		addEvent(window.document, "mousemove", fnMouseMove, false);
		var innerElement = $(oEl).text();
		if (oEl.tagName == "INPUT") {
			return;
		}
		if (IfSplitLocation(event, oEl)) {
			element.splitLine.style.display = "";
			element.splitlocked = true;
			addEvent(window.document, "selectstart", fnCancel, false);
		}
	}
	function fnMouseup(event) {
		event = event ? event : (window.event ? window.event : null);
		element.splitLine.style.display = "none";
		element.splitlocked = false;
		document.body.style.cursor = 'default';
		if (element.curResizeTD != null) {
			try {
				var otd = element.curResizeTD;
				var botheadTD = $("#"+div.id+"_thead").find("tr").find("td");
				var otdLeft = getLeft(otd);
				var cuLeft = parseInt(element.splitLine.style.left);
				cuLeft = cuLeft
						+ document.getElementById(div.id + "_body_layout").scrollLeft
						- document.getElementById(div.id + "_body_layout").clientLeft;
				var otdwidth = cuLeft - otdLeft;
				if (otdwidth < 5)
					otdwidth = 5;
				otd.style.width = otdwidth;
				botheadTD[otd.cellIndex].style.width = otdwidth;
				isresetCell = true;
				grid.changelay(null, true);
				removeEvent(window.document, "selectstart", fnCancel, false);
				window.document.onselectstart = null;
			} catch (e) {
			}
		}
	}
	function IfSplitLocation(event, oEl) {
		if (oEl.tagName == "DIV")
			oEl = oEl.parentNode;
		if (oEl.tagName == "TD") {
			var offelayerX = event.offsetX ? event.offsetX : event.layerX;
			if (!event.offsetX)
				offelayerX = offelayerX - getLeft(oEl);
			if (oEl.cellIndex < 0) {
				element.curResizeTD = null;
				document.body.style.cursor = 'default';
				return
			} else if (Math.abs(offelayerX - oEl.clientWidth) <= 5) {
				element.curResizeTD = oEl;
				document.body.style.cursor = 'col-resize';
			} else {
				element.curResizeTD = null;
				document.body.style.cursor = 'default';
				return false;
			}
		}
		return true;
	}
	function getTop(e) {
		var t = e.offsetTop;
		while (e = e.offsetParent) {
			t += e.offsetTop;
		}
		return t;
	}
	function getLeft(e) {
		var l = e.offsetLeft;
		while (e = e.offsetParent) {
			l += e.offsetLeft;
		}
		return l;
	}
	function fnCancel(event) {
		event = event ? event : (window.event ? window.event : null);
		setReturnValueFalse(event);
		return false;
	}
	function setReturnValueFalse(event) {
		if (document.all) {
			window.event.returnValue = false;
		} else {
			event.preventDefault();
		}
	}
	var temJSpath = $dpjspath.replace("/js/", "/");
	if (!checkPathisHave(temJSpath + "My97DatePicker/WdatePicker.js"))
		createJSSheet(temJSpath + "My97DatePicker/WdatePicker.js");
	if (!checkPathisHave($dpcsspath + "toolbar.main.css"))
		createStyleSheet($dpcsspath + "toolbar.main.css");
	div.style.overflow = "hidden";
	var sHeight = 0;
	if (window.innerHeight && window.scrollMaxY) {
		sHeight = window.innerHeight + window.scrollMaxY;
	} else if (document.body.scrollHeight > document.body.offsetHeight) {
		sHeight = document.body.scrollHeight;
	} else {
		sHeight = document.body.offsetHeight;
	}
	var t_height = (parseInt(div.parentNode.offsetHeight) <= sHeight) ? parseInt(div.parentNode.offsetHeight) - 55
			: sHeight - 155;
	if (!height || height == "100%") {
		height = t_height + "px";
	} else if (height.indexOf("%") > 0) {
		height = parseInt(t_height) * (parseInt(height) / 100);
		height += "px";
	}
	height = (parseInt(height) < 0) ? (parseInt(document.body.offsetHeight) - 140)
			+ "px"
			: height;
	var gridTable = "<table border='0' style='width:100%;align:left;' class='toolbar'><tr><td height='30px'>"
			+ "<table border='0' class='toolbar_item' style='width:100%;align:left;' id='"
			+ div.id
			+ "_toolbar'><tr><td width='54px' id='"
			+ div.id
			+ "_insertItem'><a href='javascript:void(0)' class='toobar_item' style='width:99%;'><img src='"
			+ $dpimgpath
			+ "toolbar/insert.gif' title='新增' class='toolbar'/></a></td><td width='54px' id='"
			+ div.id
			+ "_saveItem'><a href='javascript:void(0)' class='toobar_item' style='width:99%;'><img src='"
			+ $dpimgpath
			+ "toolbar/save.gif' title='保存' class='toolbar'/></a></td><td width='54px' id='"
			+ div.id
			+ "_refreshItem'><a href='javascript:void(0)' class='toobar_item' style='width:99%;'><img src='"
			+ $dpimgpath
			+ "toolbar/refreshbill.gif' title='刷新'/></a></td><td width='54px' id='"
			+ div.id
			+ "_deleteItem'><a href='javascript:void(0)' class='toobar_item' style='width:99%;'><img src='"
			+ $dpimgpath
			+ "toolbar/remove.gif' title='删除'/> </a></td><td width='54px' id='"
			+ div.id
			+ "_queryItem'><a href='javascript:void(0)' class='toobar_item' style='width:99%;'><img src='"
			+ $dpimgpath
			+ "toolbar/query.gif' title='高级查询'/> </a></td><td align='left' width='1'><table id='"
			+ div.id
			+ "_self_item_table'><tr><td/></tr></table></td><td align='left' valign='middle'><div style='text-align:left;float:left; lien-height:22px;' id='"
			+ div.id
			+ "-grid-item'></div><div style='text-align:right;float:left;'>"
			+ "<table><tr><td><input type='text' value='' style='margin-left:3px;float:left;height:22px;' id='"
			+ div.id
			+ "_quick_text'/></td><td><a href='javascript:void(0)' class='toobar_item' style='float:left;width:60px;' id='"
			+ div.id
			+ "_quick_button'><img src='"
			+ $dpimgpath
			+ "toolbar/search.gif' title='快速查询' style='float:left;font-size:12px;margin-top:3px;padding-left:3px;'/>"
			+ "<span style='float:left;line-height: 22px;padding-right:3px;color:#000;cursor:pointer;'>查询</span></a></td></tr></table>"
			+ "</div></td><td>&nbsp;</td></tr></table></td></tr></table>";
	if (!witdh || witdh == "")
		witdh = "100%";
	gridTable += "<table style='width:100%; 100%; word-break: break-all; table-layout:fixed;' border='0' class='gridtable'><tr><td><div id='"
			+ div.id
			+ "_body_layout' class='grid_data_view' style='align: left; width: "
			+ witdh + "; height:" + height + "; overflow: hidden;'>";
	gridTable += "<table id='" + div.id
			+ "_grid_label' border='0' style='width:" + witdh
			+ ";' class='grid'>";
	gridTable += "<thead id='" + div.id
			+ "_thead' class='scrollColThead'>";
	var label = labels.split(',');
	labelids = labelid.split(",");
	var length = label.length;
	var idlength = labelids.length;
	var labelwidths = labelwidth.split(',');
	var labeldatatypes = datatype.split(',');
	grid.gridRelation = labelids;
	grid.gridLabels = label;
	if (idlength != length || labelwidths.length != idlength
			|| labeldatatypes.length != idlength) {
		var createMessage = "";
		if (idlength != length) {
			createMessage += "labelid数不等于labelwidth数，不能创建grid！";
		}
		if (labelwidths.length != idlength) {
			createMessage += "labelid数不等于labelwidth数，不能创建grid！";
		}
		if (labeldatatypes.length != idlength) {
			createMessage += "labelid数不等于datatype数，不能创建grid！";
		}
		alert(createMessage);
		return false;
	}
	var grouplist = [];
	var groupValuelist = new Map();
	var labelList = [];
	var groupValue = [];
	grouplist.length = length;
	groupValue.length = length;
	var isGroup = (labels.indexOf("|")>0);
	var GroupStyle = "";
	for ( var i = 0; i < length; i++) {
		var grLabel = label[i];
		var norgrLabel = grLabel;
		var tigrLabel = grLabel;
		var thiscellisGroup = false;
		if(grLabel.indexOf("|")>0){
			norgrLabel = grLabel.split("|")[0];
			tigrLabel = grLabel.split("|")[1];
			grouplist[i] = norgrLabel;
			thiscellisGroup = true;
		}else{
			thiscellisGroup = false;
			grouplist[i] = "";
		}
		if(isGroup){
			GroupStyle = " rowspan='2' ";
		}
		if (labelids[i] == "master_check") {
			groupValuelist.put("master_check","<td class='grid_label' align='center' id='"
					+ labelids[i] + "' style='width:"
					+ parseInt(labelwidths[i])
					+ "px;' "+GroupStyle+"><input type='checkbox' id='" + div.id
					+ "_grid_master_check' onclick='document.getElementById(\""+div.id+"\").grid.masterselect(event)'></input></td>");
			labelList.push(groupValuelist.get("master_check"));
			groupValue[i] = "";
		} else if (labelids[i] == "No" || grLabel == "No.") {
			groupValuelist.put("No","<td class='grid_label' id='" + labelids[i]
					+ "' style='width:" + parseInt(labelwidths[i])
				+ "px;' title='" + norgrLabel
				+ "' "+GroupStyle+"><div class='gridLabel'>"
				+ norgrLabel + "</div></td>");
			labelList.push("<td class='grid_label' id='" + labelids[i]
				+ "' style='width:" + parseInt(labelwidths[i])
				+ "px;' title='" + norgrLabel
				+ "' "+GroupStyle+"><div class='gridLabel'>"
				+ norgrLabel + "</div></td>");
			groupValue[i] = "";
		} else {
			if(thiscellisGroup){
				if( i>0 && grouplist[i] == grouplist[i-1]){
					var brgroup = groupValuelist.get(grouplist[i-1]);
					var tempxmlDom = tlv8.strToXML("<div>"+brgroup+"</div>"); 
					var colspan = tempxmlDom.getElementsByTagName("td")[0].getAttribute("colspan");
					var newcolspan = parseInt(colspan||"0") + 1;
					brgroup = brgroup.replace("colspan='"+colspan+"'","colspan='"+newcolspan+"'");
					var pgroupstyle = tempxmlDom.getElementsByTagName("td")[0].getAttribute("style");
					pgroupstyle = pgroupstyle.replace("width:","");
					var pgroupwidth = parseInt(pgroupstyle);
					var groupwidth = pgroupwidth + parseInt(labelwidths[i]);
					brgroup = brgroup.replace("width:"+pgroupwidth+"px","width:"+groupwidth+"px");
					groupValuelist.put(grouplist[i], brgroup);
				}else{
					groupValuelist.put(grouplist[i],"<td class='grid_label' style='width:" + parseInt(labelwidths[i])
						+ "px;' title='" + norgrLabel + "' colspan='1'><div class='gridLabel'>" 
						+ norgrLabel + "</div></td>");
				}
				groupValue[i] = "<td class='grid_label' id='" + labelids[i]
						+ "' style='width:" + parseInt(labelwidths[i])
					+ "px;' title='" + tigrLabel + "' onclick='document.getElementById(\"" + div.id + "\").grid.CellSort(this,\"" + labelids[i] + "\")'>"
					+ "<div class='gridLabel'>" 
					+ tigrLabel + "</div></td>";
			}else{
				groupValuelist.put(labelids[i],"<td class='grid_label' id='" + labelids[i]
					+ "' style='width:" + parseInt(labelwidths[i])
					+ "px;' title='" + norgrLabel + "' onclick='document.getElementById(\"" + div.id + "\").grid.CellSort(this,\"" + labelids[i] + "\")' "
					+ GroupStyle + "><div class='gridLabel'>" 
					+ norgrLabel + "</div></td>");
				labelList.push("<td class='grid_label' id='" + labelids[i]
					+ "' style='width:" + parseInt(labelwidths[i])
					+ "px;' title='" + norgrLabel + "' onclick='document.getElementById(\"" + div.id + "\").grid.CellSort(this,\"" + labelids[i] + "\")' "
					+ GroupStyle + "><div class='gridLabel'>" 
					+ norgrLabel + "</div></td>");
				groupValue[i] = "";
			}
		}
	}
	if(isGroup){
		gridTable += "<tr class='scrollColThead' id='" + div.id + "_tab_title'>";
	}else{
		gridTable += "<tr class='scrollColTheadBottom' id='" + div.id + "_tab_title'>";
	}
	if(isGroup){
		var grupkeys = groupValuelist.keySet();
		for(var m = 0; m < grupkeys.length; m++){
			var grouplabl = groupValuelist.get(grupkeys[m]);
			gridTable += grouplabl;
		}
	}else{
		for(var m = 0; m < labelList.length; m++){
			var grouplabl = labelList[m];
			gridTable += grouplabl;
		}
	}
	gridTable += "<td class='grid_label' "+GroupStyle+">&nbsp;</td></tr>";
	if(isGroup){
		gridTable += "<tr class='scrollColTheadBottom'>"+groupValue.join("")+"</tr>";
	}
	var colspan = parseInt(length) + 1;
	gridTable += "</thead><tbody  id='"
			+ div.id
			+ "_main_grid'><tr><td colspan='" + colspan
			+ "' align='left' style='border:0px;'>";
	gridTable += "<div align='left' style='width:100%; height:100%; overflow: hidden; margin-left: 0px; padding: 0px; border:0 none;'></div>";
	gridTable += "</td></tr></tbody></table></div></td></tr><tfoot><tr><td align='left'><table align='left' class='grid'><tr><td width='30px' align='left'><a href='javascript:void(0)' class='toobar_item'><img id='"
			+ div.id
			+ "_first-page' class='firt-page' title='第一页' src='"
			+ $dpimgpath
			+ grid.standImagePath+"un_first-page.gif'/></a></td><td width='30px' align='left'><a href='javascript:void(0)' class='toobar_item'><img id='"
			+ div.id
			+ "_first' class='firt' title='上一页' src='"
			+ $dpimgpath
			+ grid.standImagePath+"un_first.gif'/></a></td><td width='40px' align='left' valign='center'><a class='toobar_item'><input id='"
			+ div.id
			+ "_page' title='1' style='height:22px;line-height:22px; font-size:12px; margin:0px;' size='2' onKeyup='document.getElementById(\""
			+ div.id
			+ "\").grid.inputPage(event,this)' value='1'></input></a></td><td width='30px' align='left'><a href='javascript:void(0)' class='toobar_item'><img id='"
			+ div.id
			+ "_last' class='last' title='下一页' src='"
			+ $dpimgpath
			+ grid.standImagePath+"un_last.gif'/></a></td><td width='30px' align='left'><a href='javascript:void(0)' class='toobar_item'><img id='"
			+ div.id
			+ "_last-page' class='last-page' title='最后一页' src='"
			+ $dpimgpath
			+ grid.standImagePath+"un_last-page.gif'></a></td><td align='left' valign='top'><a id='"
			+ div.id
			+ "_pages' title='第1/1页' class='grid_pagetextbar'>第1/1页</a></td></tr></table></td></tr></tfoot></table>";
	div.innerHTML = gridTable;
	document.getElementById(div.id + "_quick_text").onkeyup = function(event) {
		event = event ? event : (window.event ? window.event : null);
		if (event.keyCode == 13) {
			var target = event.target || event.srcElement;
			var value_text = target.value;
			grid.quickSearch(value_text);
		}
	};
	document.getElementById(div.id + "_quick_text").onblur = function(event) {
	};
	document.getElementById(div.id + "_quick_button").onclick = function() {
		var value_text = document.getElementById(div.id + "_quick_text").value;
		grid.quickSearch(value_text);
	};
	addEvent(document.getElementById(div.id + "_main_grid"), "mouseover",
			function() {
				element.splitLine.style.display = "none";
				element.splitlocked = false;
				document.body.style.cursor = 'default';
				element.curResizeTD = null;
			}, false);
	addEvent(document.getElementById(div.id + "_toolbar"), "mouseover",
			function() {
				element.splitLine.style.display = "none";
				element.splitlocked = false;
				document.body.style.cursor = 'default';
				element.curResizeTD = null;
			}, false);
	initTable(document.getElementById(div.id + "_tab_title"));
	var cupage = (grid.CurrentPage && grid.CurrentPage == 0) ? 1
			: grid.CurrentPage;
	var tempQueryAction = dataAction.queryAction + "?page=" + parseInt(cupage)
			+ "&rows=" + parseInt(limit) + "&gridid="
			+ encodeURIComponent(encodeURIComponent(div.id)) + "&columns="
			+ labelid + "&columnstype=" + datatype + "&master=" + master
			+ "&showindex=" + showindex + "&insql="
			+ encodeURIComponent(encodeURIComponent(grid.sql));
	grid.queryAction = tempQueryAction;
	var tempsavAction = dataAction.savAction + "?page=" + parseInt(cupage)
			+ "&rows=" + parseInt(limit);
	grid.savAction = tempsavAction;
	grid.deleteAction = dataAction.deleteAction;
	/**
	 * @function
	 * @name refreshData
	 * @description 刷新数据
	 * @param {string}
	 *            filter -刷新数据条件
	 * @param {boolean}
	 *            isconfirm -是否提示数据改变
	 */
	grid.refreshData = function(filter, isconfirm) {
		var beforeRefresh = div.getAttribute("beforeRefresh");
		if (beforeRefresh) {
			var brinFn = eval(beforeRefresh);
			if (typeof (brinFn) == "function") {
				brinFn(grid);
			}
		}
		if ((!grid.queryAction || grid.queryAction == "") && !grid.queryAction) {
			alert("未定义刷新动作！");
			return
		} else if (grid.queryAction) {
			var billfilter = "";
			if (!where || where == null)
				where = "";
			if (billdataformid) {
				var billid = document.getElementById(billdataformid)
						.getAttribute("rowid")
						|| document.getElementById(billdataformid).rowid;
				billid = billid ? billid : "";
				if(where && trim(where)!=""){
					var swhere_ = trim(where);
					if(swhere_.indexOf("and")==swhere_.length-3){
						billfilter = where + "(" + billcell + "= '" + billid + "')";
					}else{
						billfilter = where + " and (" + billcell + "= '" + billid + "')";
					}
				}else{
					billfilter = billcell + "= '" + billid + "'";
				}
			} else {
				billfilter = where;
			}
			if (filter && typeof (filter) == "string") {
				grid.sFilter = filter;
				if (where && where != "" || billdataformid)
					billfilter += " and (" + grid.sFilter + ")";
				else
					billfilter += " " + grid.sFilter;
			} else if (typeof (filter) == "object" || filter == "") {
				billfilter;
			} else if (grid.sFilter && grid.sFilter != "") {
				if (where && where != "" || billdataformid)
					billfilter += " and (" + grid.sFilter + ")";
				else
					billfilter += " " + grid.sFilter;
			}
			var tempId = new UUID().toString();
			if (billfilter && billfilter.trim() != "")
				billfilter += " and '" + tempId + "'='" + tempId + "'";
			else
				billfilter += " '" + tempId + "'='" + tempId + "'";
			if (grid.staticFilter && grid.staticFilter != "") {
				if (billfilter != "")
					billfilter = "(" + billfilter + ") and "
							+ grid.staticFilter;
				else
					billfilter += " " + grid.staticFilter;
			}
			grid.billfilter = billfilter;
			if (grid.editDataRowIds && grid.editDataRowIds != "") {
				if (isconfirm != false) {
					if (!confirm("数据已经更改刷新会导致数据丢失,确定刷新吗?")) {
						return false;
					}
				}
				grid.editDataRowIds = "";
			}
			tlv8.Queryaction(grid.queryAction, "post", grid.setDatatoGrid,
					data, billfilter, true);
		}
		if (grid.toolbar.saveItem != false)
			grid.settoolbar("no", "readonly", "no", "no");
	};
	/**
	 * @function
	 * @name insertData
	 * @description 新增数据
	 * @param {boolean}
	 *            num_d -true向下新增|false向上新增
	 * @returns {string}
	 */
	grid.insertData = function(num_d) {
		num_d = num_d || grid.insertNum;
		var beforeInert = div.getAttribute("beforeInsert");
		if (beforeInert) {
			var inFn = eval(beforeInert);
			if (typeof (inFn) == "function") {
				inFn(grid);
			}
		}
		var data_tale = "";
		var _main_grid = document.getElementById(div.id + "_main_grid");
		try {
			var _tbody = _main_grid;
			var grid_data = $(_tbody).html();
			var _tr = _tbody.getElementsByTagName("tr")[0];
		} catch (e) {
		}
		var myUUID = new UUID();
		var uuid = myUUID.createUUID().substring(0, 20) + m_getCount();
		var newrowid = myUUID.createUUID();
		var newData = "<TR id='" + newrowid + "'>";
		if (_tr && _tr.tagName == "TR") {
			var _td = _tr.getElementsByTagName("td");
			for ( var i = 0; i < _td.length; i++) {
				if (i == 0 && showindex && showindex == "true")
					newData += "<TD style='WIDTH:" + _td[i].style.width
							+ "' align='center'> </TD>";
				else
					newData += "<TD class='grid_td' style='WIDTH:"
							+ _td[i].style.width + "' align='center'></TD>";
			}
		} else {
			var newtds = labelid.split(",");
			for ( var i = 0; i < newtds.length; i++) {
				if (i == 0 && showindex && showindex == "true")
					newData += "<TD align='center'> </TD>";
				else
					newData += "<TD class='grid_td' align='center'></TD>";
			}
			newData += "<TD class='grid_td'></TD>";
		}
		newData += "</TR>";
		if (num_d == true) {
			grid_data = (grid_data ? grid_data : "") + newData;
		} else {
			grid_data = newData + (grid_data ? grid_data : "");
		}
		data_tale += grid_data;
		$(_main_grid).html(data_tale);
		grid.CurrentRowId = newrowid;
		isresetCell = true;
		try {
			grid.RowId = new Array();
			isresetCell = false;
			grid.changelay(div.id, false, newrowid);
		} catch (e) {
		}
		if (num_d == true) {
			document.getElementById(div.id + "_body_layout").scrollTop = _main_grid.clientHeight;
		} else {
			document.getElementById(div.id + "_body_layout").scrollTop = 0;
		}
		if (grid.toolbar.saveItem != false)
			grid.settoolbar("no", true, "no", "no");
		var afterInert = div.getAttribute("afterInert");
		if (afterInert) {
			var ainFn = eval(afterInert);
			if (typeof (ainFn) == "function") {
				ainFn(grid);
			}
		}
		try {
			var cu_row = document.getElementById(newrowid);
			cu_row.click();
		} catch (e) {
		}
		return newrowid;
	};
	/**
	 * @function
	 * @name saveData
	 * @description 保存数据
	 * @param {object}
	 *            event
	 * @param {function}
	 *            gridsavecalback
	 */
	grid.saveData = function(event, gridsavecalback) {
		var beforeSave = div.getAttribute("beforeSave");
		if (beforeSave) {
			var asavFn = eval(beforeSave);
			if (typeof (asavFn) == "function") {
				asavFn(grid);
			}
		}
		grid.sAlert = true;
		var cell = new Map();
		if (!grid.editDataRowIds) {
			if (!data.childrenData.isEmpty()) {
				var keyset = data.childrenData.keySet();
				for (i in keyset) {
					key = keyset[i];
					var childData = data.childrenData.get(key);
					var isCsave = childData.saveData();
					if (!isCsave || isCsave == false) {
						break;
					}
				}
			}
			return true;
		}
		var editDataRow = grid.editDataRowIds.split(",");
		if (editDataRow.length > 0) {
			var submitData = new Map();
			for ( var j = 0; j < editDataRow.length; j++) {
				data.setCells(null);
				if (!grid.editedDatas.containsKey(editDataRow[j])) {
					continue;
				}
				if (!grid.RequiredCells.isEmpty()) {
					var reqiredCellSet = grid.RequiredCells.keySet();
					var sAmessage = "";
					for (k in reqiredCellSet) {
						var cellname = reqiredCellSet[k];
						var cRowid = editDataRow[j];
						var CellValue = grid.getValueByName(cellname, cRowid);
						if (!CellValue || CellValue == "")
							sAmessage += grid.gridLabels[grid
									.getgridRelationIndex(cellname)]
									+ "不能为空!";
					}
					if (sAmessage != "") {
						alert(sAmessage);
						return false;
					}
				}
				var editRowtrs = grid.editedDatas.get(editDataRow[j]);
				cell.put("rowid", editDataRow[j]);
				if (billdataformid) {
					var billid = $("#"+billdataformid).attr("rowid")||J$(billdataformid).rowid;
					if (billid)
						cell.put(billcell, billid);
				}
				var version = $(div).find("tr[id='"+editDataRow[j]+"']").attr("version")||0;
				cell.put("VERSION", (parseInt(version)+1)+"");
				data.setCells("<root>");
				data.setCells(cell);
				if (editRowtrs)
					data.setCells(editRowtrs);
				data.setCells("</root>");
				if (grid.savAction.indexOf("saveAction") > -1
						|| grid.savAction.indexOf("BaseSaveGridAction") > -1) {
					submitData.put(editDataRow[j], data.cells);
					continue;
				}
				if (!grid.savAction || grid.savAction == "") {
					alert("未定义保存动作！");
					return;
				}
				if (j == editDataRow.length - 1) {
					tlv8.saveAction(grid.savAction, "post", function(sd) {
						grid.editDataRowIds = "";
						data.setCells(null);
						if (sd.flag == "false") {
							grid.sAlert = false;
							alert(sd.message);
						} else {
							grid.sAlert = true;
							if (grid.sAlert)
								sAlert("操作成功！", 500);
						}
						if (!data.childrenData.isEmpty()) {
							var keyset = data.childrenData.keySet();
							for (i in keyset) {
								key = keyset[i];
								var childData = data.childrenData.get(key);
								var isCsave = childData.saveData();
								if (!isCsave || isCsave == false) {
									break;
								}
							}
						}
						grid.refreshData();
					}, data);
				} else {
					tlv8.saveAction(grid.savAction, "post", null, data);
				}
			}
			if (grid.savAction.indexOf("saveAction") > -1
					|| grid.savAction.indexOf("BaseSaveGridAction") > -1) {
				var rParam = new tlv8.RequestParam();
				rParam.set("dbkay", data.dbkay);
				rParam.set("table", data.table);
				rParam.set("cells", submitData.toString());
				if (billdataformid) {
					var billid = document.getElementById(billdataformid).rowid;
					if (billid) {
						rParam.set("billid", billcell + ":" + billid);
					} else {
						rParam.set("billid", "");
					}
				}
				var saveBack = function(_rf) {
					grid.editDataRowIds = "";
					if (_rf.data.flag == "false") {
						grid.sAlert = false;
						var msessage = _rf.data.message;
						// 截取java异常
						if (msessage.indexOf("Exception:") > 0) {
							msessage = msessage.substring(msessage
									.indexOf("Exception:") + 10);
						}
						alert(msessage);
					} else {
						grid.sAlert = true;
						if (grid.sAlert)
							sAlert("操作成功！", 500);
					}
					if (!data.childrenData.isEmpty()) {
						var keyset = data.childrenData.keySet();
						for (i in keyset) {
							key = keyset[i];
							var childData = data.childrenData.get(key);
							var isCsave = childData.saveData();
							if (!isCsave || isCsave == false) {
								break;
							}
						}
					}
					if (gridsavecalback && typeof gridsavecalback == "function") {
						try {
							gridsavecalback();
						} catch (e) {
						}
					} else {
						grid.refreshData();
					}
				};
				var rg = tlv8.XMLHttpRequest("saveGridAction", rParam,
						"POST", false, null);
				saveBack(rg);
			}
		}
		var afterSave = div.getAttribute("afterSave");
		if (afterSave) {
			var afsavFn = eval(afterSave);
			if (typeof (afsavFn) == "function") {
				afsavFn(grid);
			}
		}
		grid.editDataRowIds = null;
		if (grid.toolbar.saveItem != false)
			grid.settoolbar("no", "readonly", "no", "no");
		return true;
	};
	/**
	 * @function
	 * @name deleteData
	 * @description 删除数据
	 * @param {string}
	 *            deleteRowID
	 * @param {boolean}
	 *            isConfirm
	 * @param {boolean}
	 *            isRefresh
	 */
	grid.deleteData = function(deleteRowID, isConfirm, isRefresh) {
		var beforeDelete = div.getAttribute("beforeDelete");
		if (beforeDelete) {
			var bdelFn = eval(beforeDelete);
			if (typeof (bdelFn) == "function") {
				bdelFn(grid);
			}
		}
		if(master == "true" || master == true){
			var chrowids = grid.checkedRowIds;
			if(!chrowids || chrowids==""){
				alert("请选择需要删除的数据!");
				return;
			}else{
				var dodeleteis = false;
				if (isConfirm != false) {
					if (confirm("确定删除数据吗?")) {
						dodeleteis = true;
					}
				}else{
					dodeleteis  = true;
				}
				if(dodeleteis){
					writeLog(window.event, "删除数据", "操作的表:"+data.dbkay+"."+data.table);
					var param = new tlv8.RequestParam();
					param.set("dbkay", data.dbkay);
					param.set("table", data.table);
					param.set("rowids", chrowids);
					param.set("Cascade", data.Cascade);
					tlv8.XMLHttpRequest("deleteMutiAction", param, "post", true, function(r){
						try {
							var chrowidsary = chrowids.split(",");
							for(var i=0;i<chrowidsary.length;i++){
								grid.editedDatas.remove(chrowidsary[i]);
								grid.reMoveRowId(chrowidsary[i]);
								$("tr[id='"+chrowidsary[i]+"']").remove();
							}
							grid.editDataRowIds = grid.editDataRowIds.replace(chrowids, "");
						} catch (e) {
						}
						var afterDelete = div.getAttribute("afterDelete");
						if (afterDelete) {
							var afdelFn = eval(afterDelete);
							if (typeof (afdelFn) == "function") {
								afdelFn(grid);
							}
						}
						if (isRefresh == true) {
							grid.refreshData();
						}
					});
				}
			}
			return;
		}
		if(typeof deleteRowID!="string"){
			deleteRowID = null;
		}
		var rowid = deleteRowID || grid.CurrentRowId;
		if (rowid && rowid != "") {
			if (!grid.deleteAction || grid.deleteAction == "") {
				alert("未定义删除动作！");
				return false;
			}
			if (isConfirm != false) {
				if (confirm("确定删除数据吗?")) {
					writeLog(window.event, "删除数据", "操作的表:"+data.dbkay+"."+data.table);
					tlv8.Deleteaction(grid.deleteAction, "post", function(
							r) {
						if (r.flag == "false") {
							alert(r.message);
							return false;
						} else {
							sAlert("操作成功！", 500);
						}
						var afterDelete = div.getAttribute("afterDelete");
						if (afterDelete) {
							var afdelFn = eval(afterDelete);
							if (typeof (afdelFn) == "function") {
								afdelFn(grid);
							}
						}
						if (isRefresh == true) {
							grid.refreshData();
						}
						try {
							grid.editedDatas.remove(rowid);
							grid.reMoveRowId(rowid);
							if (grid.editDataRowIds.indexOf("," + rowid)) {
								grid.editDataRowIds = grid.editDataRowIds
										.replace("," + rowid, "");
							} else {
								grid.editDataRowIds = grid.editDataRowIds
										.replace(rowid, "");
							}
						} catch (e) {
						}
					}, rowid, data);
					grid.CurrentRowId = "";
					grid.rechangelay(document.getElementById(rowid));
					$("tr[id='"+rowid+"']").remove();
				}
			} else {
				writeLog(window.event, "删除数据", "操作的表:"+data.dbkay+"."+data.table);
				tlv8.Deleteaction(grid.deleteAction, "post", function(r) {
					if (r.flag == "false") {
						alert(r.message);
						return false;
					} else {
					}
					var afterDelete = div.getAttribute("afterDelete");
					if (afterDelete) {
						var afdelFn = eval(afterDelete);
						if (typeof (afdelFn) == "function") {
							afdelFn(grid);
						}
					}
					if (isRefresh == true) {
						grid.refreshData();
					}
					try {
						grid.editedDatas.remove(rowid);
						grid.reMoveRowId(rowid);
						if (grid.editDataRowIds.indexOf("," + rowid)) {
							grid.editDataRowIds = grid.editDataRowIds.replace(
									"," + rowid, "");
						} else {
							grid.editDataRowIds = grid.editDataRowIds.replace(
									rowid, "");
						}
					} catch (e) {
					}
				}, rowid, data);
				grid.CurrentRowId = "";
				grid.rechangelay(document.getElementById(rowid));
				$("tr[id='"+rowid+"']").remove();
			}
		} else {
			alert("请选中需要删除的行！");
			return false;
		}
		return true;
	};
	div.grid = grid;
	document.getElementById(div.id + "_insertItem").onclick = div.grid.insertData;
	document.getElementById(div.id + "_saveItem").onclick = div.grid.saveData;
	document.getElementById(div.id + "_refreshItem").onclick = div.grid.refreshData;
	document.getElementById(div.id + "_deleteItem").onclick = div.grid.deleteData;
	document.getElementById(div.id + "_queryItem").onclick = div.grid.advancedQueryData;
	if (billdataformid && billdataformid != "") {
		try {
			var mian_data_current = document.getElementById(billdataformid).data;
			if (billcell && billcell != "" && mian_data_current) {
				var main_Cascade = mian_data_current.Cascade.trim();
				var current_Cascade = (main_Cascade == "") ? (data.table + ":" + billcell)
						: (main_Cascade.indexOf(data.table + ":" + billcell) < 0) ? (main_Cascade
								+ "," + data.table + ":" + billcell)
								: main_Cascade;
				mian_data_current.setCascade(current_Cascade);
				mian_data_current.childrenData.put(div.id, grid);
			}
		} catch (e) {
		}
	}
	$(div).find("td.grid_label").each(function(){
		if($(this).width() < 1){
			$(this).css("border","0 none");
			$(this).css("font-size","0px");
			$(this).attr("unresize", "true");
			$(this).find("div").hide();
		}
	});
	var oldwidth = $("#"+div.id+"_body_layout").width();
	var oldheight = $("#"+div.id+"_body_layout").height();
	$("#"+div.id+"_grid_label").fixTable({
	 	fixColumn: grid.fixColumn || 0,// 固定列数
	 	fixColumnBack: grid.fixColumnBack || "#ccc",// 固定列数
		width:  $("#"+div.id+"_body_layout").width(),// 显示宽度
		height: $("#"+div.id+"_body_layout").height()// 显示高度
	});
	$(window).resize(function() {
		setTimeout(function(){
			$("#"+div.id+"_grid_label").fixTable({
			 	fixColumn: grid.fixColumn || 0,// 固定列数
			 	fixColumnBack: grid.fixColumnBack || "#ccc",// 固定列数
				width:  $("#"+div.id+"_body_layout").width(),// 显示宽度
				height: $("#"+div.id+"_body_layout").height()// 显示高度
			});
		},300);
	});
	grid.resize = function(){
		$("#"+div.id+"_grid_label").fixTable({
		 	fixColumn: grid.fixColumn || 0,// 固定列数
		 	fixColumnBack: grid.fixColumnBack || "#ccc",// 固定列数
			width:  $("#"+div.id+"_body_layout").width(),// 显示宽度
			height: $("#"+div.id+"_body_layout").height()// 显示高度
		});
	};
	$("#"+div.id + "_main_grid").html("");
	this.grid = grid;
	return this;
};

var grid_newCount = 1;
var m_getCount = function() {
	if (grid_newCount < 9) {
		return grid_newCount++;
	} else {
		grid_newCount = 1;
		return grid_newCount;
	}
};
function show_popup(obj) {
	return;
}

if (!checkPathisHave($dpcsspath + "grid.main.css"))
	createStyleSheet($dpcsspath + "grid.main.css");
if (topparent != window) {
	if (!checkPathisHave(cpath+"/comon/js/jquery/jquery.min.js")
			&& !checkPathisHave($dpjspath + "jquery/jquery.min.js")) {
		createJSSheet($dpjspath + "jquery/jquery.min.js");
	}
}

(function($){  
    $.fn.fixTable = function(options){ 
		var defaults = {  
			fixColumn: 0,  
			fixColumnBack: "#eee",  
			width:0,
			height:0  
		};    
	    var opts = $.extend(defaults, options); 
		var _this = $(this);
		$("table.grid").css("border-right","1px solid #eee");
		var fixet_id = $(this).attr("id");
		if(!fixet_id || fixet_id=="")return;
		var grid_id = fixet_id.replace("_grid_label","");
		_this.find("thead").find("tr").attr("id",grid_id+"_tab_title");
		var _clone = _this.clone();
		_clone.attr("id",fixet_id+"_Header");
		_clone.find("thead").removeAttr("id");
		_clone.find("tbody").removeAttr("id");
		_clone.find("tbody").find("tr").removeAttr("id");
		var colsp = _clone.find("thead").find("tr").find("td").length;
		var cemptyh = _clone.find("tbody").html();
		// TODO: 对空表格处理
		if(!cemptyh || cemptyh =="" || cemptyh == "&nbsp;"){
			cemptyh = '<tr><td style="border: 0px currentColor;" colspan="'+colsp+'" align="left"><div '+
				'style="padding: 0px; border: 0px currentColor; width: 100%; height: 100%; '+
				'overflow: hidden; margin-left: 0px;" align="left"></div></td></tr>';
			_clone.find("tbody").html(cemptyh);
		}
		var _columnClone = _this.clone();
		_columnClone.attr("id",fixet_id+"_columnClone");
		_columnClone.find("thead").removeAttr("id");
		_columnClone.find("thead").find("tr").removeAttr("id");
		_columnClone.find("tbody").removeAttr("id");
		_columnClone.find("tbody").find("tr").removeAttr("id");
		var _columnDataClone = _this.clone();
		_columnDataClone.attr("id",fixet_id+"_columnDataClone");
		_columnDataClone.find("thead").removeAttr("id");
		_columnDataClone.find("thead").find("tr").removeAttr("id");
		_columnDataClone.find("tbody").removeAttr("id");
		_columnDataClone.find("tbody").find("tr").removeAttr("id");
		
		_this.find("thead").find("tr").removeAttr("id");
		
		if($("#"+fixet_id+"_fixTableMain").length==0){
			_this.wrap(function() {
               return $("<div id='"+fixet_id+"_fixTableMain'></div>");
            });
		}
		$("#"+fixet_id+"_fixTableMain").css({
			"width":$("#"+grid_id).width()+"px",
			"height":defaults["height"],
			"overflow":"auto",
			"position":"relative"
		});
		
		if($("#"+fixet_id+"_fixTableBody").length==0){
			$("#"+fixet_id+"_fixTableMain").wrap(function() {
               return $("<div id='"+fixet_id+"_fixTableBody'></div>");
            });
		}
		$("#"+fixet_id+"_fixTableBody").css({
			"width": defaults["width"],
			"height":defaults["height"],
			"overflow":"hidden",
			"position":"relative"
		});
		$("#"+fixet_id+"_fixTableHeader").remove();
		$("#"+fixet_id+"_fixTableBody").append("<div id='"+fixet_id+"_fixTableHeader'></div>");
		$(_clone).height($(_clone).find("thead").height());
		$("#"+fixet_id+"_fixTableHeader").html("");
		$("#"+fixet_id+"_fixTableHeader").append(_clone);
		$("#"+fixet_id+"_fixTableHeader").css({
			"overflow":"hidden",
			"width": defaults["width"]-17,
			"height":_clone.find("thead").height()+1,
			"position":"absolute",
			"z-index":"2",
			"top":"0"
		});
		
		
		$("#"+fixet_id+"_fixTableColumn").remove();
		$("#"+fixet_id+"_fixTableBody").append("<div id='"+fixet_id+"_fixTableColumn'></div>"); 
		var _fixColumnNum = defaults["fixColumn"]; 
		var _fixColumnWidth = 0;
		$($(_this).find("thead").find("tr").find("td")).each(function(index,element) { 
			  if((index+1)<=_fixColumnNum){ 
				  _fixColumnWidth += $(this).width() + 1; 
			  } 
		}); 
		$("#"+fixet_id+"_fixTableColumn").css({
			  "background-color":defaults["fixColumnBack"], 
			  "overflow":"hidden", 
			  "width":_fixColumnWidth,
			  "height":defaults["height"]-17, 
			  "position":"absolute",
			  "z-index":"3", 
			  "top":"0", 
			  "left":"0" 
		});
		
		$("#"+fixet_id+"_fixTableColumnHeader").remove();
		$("#"+fixet_id+"_fixTableColumn").append("<div id='"+fixet_id+"_fixTableColumnHeader'></div>");
		$("#"+fixet_id+"_fixTableColumnHeader").css({
			  "width":$("#"+fixet_id+"_fixTableColumn").width(),
			  "height":_this.find("thead").find("tr").height()+1,
			  "overflow":"hidden", 
			  "position":"absolute", 
			  "z-index":"3"
		  }); 
		$("#"+fixet_id+"_fixTableColumnHeader").html("");
		$("#"+fixet_id+"_fixTableColumnHeader").append(_columnClone);
		
		$("#"+fixet_id+"_fixTableColumnBody").remove();
		$("#"+fixet_id+"_fixTableColumn").append("<div id='"+fixet_id+"_fixTableColumnBody'></div>");
		$("#"+fixet_id+"_fixTableColumnBody").css({
			"width":$("#"+fixet_id+"_fixTableColumn").width(),
			"height":defaults["height"]-17,
			"overflow":"hidden",
			"position":"absolute",
			"z-index":"3",
			"top":"0"
		});
		$("#"+fixet_id+"_fixTableColumnBody").html("");
		$("#"+fixet_id+"_fixTableColumnBody").append(_columnDataClone);
		$("#"+fixet_id+"_fixTableMain").scroll(function(e) {
            $("#"+fixet_id+"_fixTableHeader").scrollLeft($(this).scrollLeft());
			$("#"+fixet_id+"_fixTableColumnBody").scrollTop($(this).scrollTop());
        });
		$("#"+fixet_id+"_fixTableMain").scroll();
		
		var grid = document.getElementById(grid_id).grid;
		if(grid.sortID && grid.sortID!=""){
			var sortTD = $("#"+fixet_id+"_fixTableHeader").find("td[id='"+grid.sortID+"']");
			sortTD.attr("class",grid.sortSC);
			sortTD.find("div").attr("class",grid.sortSC);
		}
	};
})(jQuery); 

/**
*以下为了兼容云捷代码
*/
justep.yn = tlv8;