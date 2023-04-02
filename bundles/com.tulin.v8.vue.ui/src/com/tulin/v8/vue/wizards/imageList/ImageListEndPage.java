package com.tulin.v8.vue.wizards.imageList;

import org.eclipse.jface.viewers.ISelection;

import com.tulin.v8.vue.wizards.EndPage;

public class ImageListEndPage extends EndPage {

	public ImageListEndPage(ISelection selection) {
		super("imageListEnd");
		this.selection = selection;
		setTitle("图文列表");
	}

	@Override
	protected void initialize() {
		super.initialize();
		fileName = "imageListPage.vue";
		fileText.setText(fileName);
	}

}
