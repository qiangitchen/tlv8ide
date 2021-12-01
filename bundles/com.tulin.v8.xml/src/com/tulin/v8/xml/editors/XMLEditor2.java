package com.tulin.v8.xml.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.editor.EditorModelUtil;
import org.eclipse.wst.xml.core.internal.provisional.contenttype.ContentTypeIdForXML;

@SuppressWarnings("restriction")
public class XMLEditor2 extends StructuredTextEditor {

	public XMLEditor2() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#doSetInput(org.eclipse.ui
	 * .IEditorInput)
	 */
	protected void doSetInput(IEditorInput input) throws CoreException {
		// System.out.println("文件类型未定义...默认为xml");
		IStructuredModel model = StructuredModelManager.getModelManager()
				.createUnManagedStructuredModelFor(ContentTypeIdForXML.ContentTypeID_XML);
		EditorModelUtil.addFactoriesTo(model);
		// attempt to get the model for the given input
		super.doSetInput(input);
	}

}
