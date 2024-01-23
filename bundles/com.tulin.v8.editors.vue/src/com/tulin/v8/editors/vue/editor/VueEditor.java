package com.tulin.v8.editors.vue.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

import com.tulin.v8.editors.vue.VuePlugin;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditor;

/**
 * VUE文件编辑器
 * 
 * @author chenqian
 * @see org.eclipse.ui.internal.genericeditor.ExtensionBasedTextEditor
 * @see com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditor
 */
public class VueEditor extends HTMLSourceEditor {// ExtensionBasedTextEditor {
	public static final String ID = "com.tulin.v8.editors.vueEditor";

	public VueEditor() {
		super();
		configuration = new VUEConfiguration(this, WebToolsPlugin.getDefault().getColorProvider());
		setSourceViewerConfiguration(configuration);
	}

	@Override
	protected void addContextMenuActions(IMenuManager menu) {
		menu.appendToGroup(ITextEditorActionConstants.GROUP_EDIT, new MenuManager(
				WebToolsPlugin.getResourceString("SourceEditor.Menu.Source"), WebToolsPlugin.GROUP_SOURCE));
		addAction(menu, WebToolsPlugin.GROUP_SOURCE, ACTION_COMMENT);
		addAction(menu, WebToolsPlugin.GROUP_SOURCE, ACTION_FORMAT);
	}

	@Override
	protected void doValidate() {
		new Job("VUE Validation") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					IFileEditorInput input = (IFileEditorInput) getEditorInput();
					IFile file = input.getFile();
					file.deleteMarkers(IMarker.PROBLEM, false, 0);

					new ValidateVueCode(input.getFile()).doValidate();
				} catch (Exception ex) {
					// WebToolsPlugin.logException(ex);
				}

				return Status.OK_STATUS;
			}
		}.schedule();
	}

	@Override
	protected IDocumentProvider createDocumentProvider(IEditorInput input) {
		if (input instanceof IFileEditorInput) {
			return new VUETextDocumentProvider();
		} else if (input instanceof IStorageEditorInput) {
			return new VUEFileDocumentProvider();
		} else {
			return new VUETextDocumentProvider();
		}
	}

	@Override
	public Image getTitleImage() {
		return VuePlugin.getDefault().getImage("/icons/vueico.png");
	}
}