package tern.eclipse.ide.jsdt.internal.core;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Document;

import tern.IDOMProvider;
import tern.ITernFile;
import tern.internal.resources.DefaultDOMProvider;

@SuppressWarnings("restriction")
public class DOMSSEProvider implements IDOMProvider {

	public static final IDOMProvider INSTANCE = new DOMSSEProvider();

	private DOMSSEProvider() {
	}
	
	/**
	 * Returns the DOM Document id of the file by using SSE DOM Model.
	 * 
	 * @param file
	 * @return
	 */
	@Override
	public Document getDocument(ITernFile file) {
		IFile iFile = (IFile) file.getAdapter(IFile.class);
		if (iFile == null) {
			return DefaultDOMProvider.INSTANCE.getDocument(file);
		}
		IStructuredModel model = null;
		try {
			model = StructuredModelManager.getModelManager()
					.getExistingModelForRead(file);
			if (model == null) {
				model = StructuredModelManager.getModelManager()
						.getModelForRead(iFile);
			}
			if (model != null && model instanceof IDOMModel) {
				return ((IDOMModel) model).getDocument();
			}
		} catch (IOException e) {
			return null;
		} catch (CoreException e) {
			return null;
		} finally {
			if (model != null) {
				model.releaseFromRead();
			}
		}
		return null;
	}

}
