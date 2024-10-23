package com.tulin.v8.vue.wizards.baseForm;

import org.eclipse.jface.viewers.ISelection;

import com.tulin.v8.vue.wizards.EndPage;

public class BaseFormEndPage extends EndPage {

	public BaseFormEndPage(ISelection selection) {
		super("baseFormEnd");
		setTitle("基础表单");
		setDescription("基础表单生成路径和文件名配置.");
		this.selection = selection;
	}

	@Override
	protected void initialize() {
		super.initialize();
		fileName = "baseFormPage.vue";
		fileText.setText(fileName);
	}
}
