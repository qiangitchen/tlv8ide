package com.tulin.v8.ide.ui.editors.javascript;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.PartService;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.CompilationUnitEditor;

@SuppressWarnings("restriction")
public class JavaScriptEditor extends CompilationUnitEditor {
	public JavaScriptEditor() {
		super();
	}

	public boolean isFileEditorInput() {
		if (getEditorInput() instanceof IFileEditorInput) {
			return true;
		}
		return false;
	}

	public File getTempFile() {
		IFile file = ((FileEditorInput) this.getEditorInput()).getFile();
		return new File(file.getLocation().makeAbsolute().toFile().getParentFile(), "." + file.getName());
	}

	@Override
	public void dispose() {
		try {
			//为了移除Part服务监听【解决JS编辑器嵌入多页签编辑器关闭问题--一直报空指针异常】
			IWorkbenchWindow window = getSite().getWorkbenchWindow();
			PartService psvi = (PartService) window.getPartService();
			psvi.partClosed(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.dispose();
	}

}
