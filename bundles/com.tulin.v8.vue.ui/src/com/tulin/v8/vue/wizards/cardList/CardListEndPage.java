package com.tulin.v8.vue.wizards.cardList;

import org.eclipse.jface.viewers.ISelection;

import com.tulin.v8.vue.wizards.EndPage;

public class CardListEndPage extends EndPage {

	public CardListEndPage(ISelection selection) {
		super("cardListEnd");
		this.selection = selection;
		setTitle("卡片列表");
	}

	@Override
	protected void initialize() {
		super.initialize();
		fileName = "cardListPage.vue";
		fileText.setText(fileName);
	}

}
