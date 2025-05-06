package tern.eclipse.ide.jsdt.internal.ui.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;

import tern.eclipse.ide.ui.utils.EditorUtils;

/**
 * Utilities for SSE DOM node {@link IDOMNode}.
 * 
 */
@SuppressWarnings("restriction")
public class DOMUtils {

	/**
	 * Returns the owner file of the JFace document {@link IDocument}.
	 * 
	 * @param document
	 * @return
	 */
	public static final IFile getFile(IDocument document) {
		if (document == null) {
			return null;
		}
		IStructuredModel model = null;
		try {
			model = StructuredModelManager.getModelManager().getExistingModelForRead(document);
			if (model != null) {
				return getFile(model);
			}
		} finally {
			if (model != null)
				model.releaseFromRead();
		}
		return EditorUtils.getFile(document);
	}

	/**
	 * Returns the owner file of the SSE model {@link IStructuredModel}.
	 * 
	 * @param node the SSE model.
	 * @return
	 */
	public static final IFile getFile(IStructuredModel model) {
		String baselocation = model.getBaseLocation();
		if (baselocation != null) {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IPath filePath = new Path(baselocation);
			if (filePath.segmentCount() > 1) {
				return root.getFile(filePath);
			}
		}
		return null;
	}

	/**
	 * 获取当前正在编辑的文件（异步调用会返回null）
	 * 
	 * @return
	 */
	public static final IFile getCurrentFile() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				IEditorPart editorPart = page.getActiveEditor();
				if (editorPart != null) {
					IEditorInput editorInput = editorPart.getEditorInput();
					if (editorInput instanceof IFileEditorInput) {
						IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
						IFile file = fileEditorInput.getFile();
						return file;
					}
				}
			}
		}
		return null;
	}
}
