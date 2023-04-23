package com.tulin.v8.vue.wizards.gridCheckDialog;

import org.eclipse.jface.viewers.ISelection;

import com.tulin.v8.vue.wizards.EndPage;

public class GridCheckDialogEndPage extends EndPage {

	public GridCheckDialogEndPage(ISelection selection) {
		super("gridCheckDialogPageEnd");
		setTitle("列表多选对话框");
		setDescription("列表多选对话框生成路径和文件名配置.");
		this.selection = selection;
	}

	@Override
	protected void initialize() {
		super.initialize();
		fileName = "gridCheckDialogPage.vue";
		fileText.setText(fileName);
	}

}
