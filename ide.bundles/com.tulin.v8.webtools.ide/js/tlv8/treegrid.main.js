/*-------------------------------------------------------------------------------*\
|  Subject:    树形列表
|  Author:     ChenQian
 *  CopyRight:  www.tlv8.com
|  Created:    2014-07-18
|  Version:    v2.3
\*-------------------------------------------------------------------------------*/
tlv8.treeGrid = function(teger, idField, treeField, parentField, columns,
		witdh, height, data, toolbar) {
	this.pagerFilter = function(data) {
		if ($.isArray(data)) {
			data = {
				total : data.length,
				rows : data
			};
		}
		var dg = $(this);
		var state = dg.data('treegrid');
		var opts = dg.treegrid('options');
		var pager = dg.treegrid('getPager');
		pager.pagination({
			onSelectPage : function(pageNum, pageSize) {
				opts.pageNumber = pageNum;
				opts.pageSize = pageSize;
				pager.pagination('refresh', {
					pageNumber : pageNum,
					pageSize : pageSize
				});
				dg.treegrid('loadData', data);
			}
		});
		if (!data.topRows) {
			data.topRows = [];
			data.childRows = [];
			for (var i = 0; i < data.rows.length; i++) {
				var row = data.rows[i];
				row._parentId ? data.childRows.push(row) : data.topRows
						.push(row);
			}
			data.total = (data.topRows.length);
		}
		var start = (opts.pageNumber - 1) * parseInt(opts.pageSize);
		var end = start + parseInt(opts.pageSize);
		data.rows = $.extend(true, [], data.topRows.slice(start, end).concat(
				data.childRows));
		return data;
	};
	$(teger).treegrid({
		idField : idField,
		treeField : treeField,
		rownumbers : true,
		animate : true,
		fitColumns : true,
		pagination : true,
		showFooter : false,
		witdh : witdh,
		height : height,
		fit : ("100%" == witdh && "100%" == height),
		toolbar : toolbar,
		loadFilter : this.pagerFilter,
		columns : [ columns ]
	});
	this.refreshData = function(where) {
		var param = new tlv8.RequestParam();
		param.set("dbkey", data.dbkay);
		param.set("table", data.table);
		param.set("idField", idField);
		param.set("treeField", treeField);
		param.set("parentField", parentField);
		param.set("columns", JSON.stringify(columns));
		param.set("filter", where || "");
		tlv8.XMLHttpRequest("queryTreegridDataAction", param, "post",
				true, function(redata) {
					redata = JSON.parse(redata);
					$(teger).treegrid('loadData', redata);
				});
	};
};

/**
*以下为了兼容云捷代码
*/
justep.yn = tlv8;