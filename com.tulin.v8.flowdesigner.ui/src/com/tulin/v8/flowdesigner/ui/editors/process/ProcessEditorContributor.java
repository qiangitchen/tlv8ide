package com.tulin.v8.flowdesigner.ui.editors.process;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

public class ProcessEditorContributor extends MultiPageEditorActionBarContributor {

	@Override
	public void setActivePage(IEditorPart activeEditor) {
		// TODO 自动生成的方法存根
		//System.out.println(activeEditor);
		if (activeEditor instanceof FlowDesignEditor) {
			((FlowDesignEditor) activeEditor).initializeOperationHistory();
		}
	}

}
