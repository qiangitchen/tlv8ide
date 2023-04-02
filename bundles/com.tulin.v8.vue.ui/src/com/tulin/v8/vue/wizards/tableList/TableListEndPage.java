package com.tulin.v8.vue.wizards.tableList;

import org.eclipse.jface.viewers.ISelection;

import com.tulin.v8.vue.wizards.EndPage;

public class TableListEndPage extends EndPage {

	public TableListEndPage(ISelection selection) {
		super("tableListPageEnd");
		setTitle("表格列表");
		setDescription("表格列表生成路径和文件名配置.");
		this.selection = selection;
	}

	@Override
	protected void initialize() {
		super.initialize();
		fileName = "tableListPage.vue";
		fileText.setText(fileName);
	}

}
