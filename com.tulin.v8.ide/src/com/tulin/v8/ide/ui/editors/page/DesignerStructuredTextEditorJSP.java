package com.tulin.v8.ide.ui.editors.page;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jst.pagedesigner.dnd.internal.DesignerSourceDropTargetListener;
import org.eclipse.jst.pagedesigner.utils.StructuredModelUtil;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditorDropTargetListener;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelStateListener;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

/**
 * @author mengbo
 * @author 陈乾
 */
@SuppressWarnings("restriction")
public class DesignerStructuredTextEditorJSP extends StructuredTextEditor {
	private ITextEditorDropTargetListener _dropTargetListener;
	private DropTarget _dropTarget;
	private IStructuredModel _structuredModel;
	private IModelStateListener _modelStateListener;

	@Override
	protected void initializeDrop(final ITextViewer viewer) {
		try {
//			int operations = DND.DROP_COPY | DND.DROP_MOVE;
//			_dropTarget = new DropTarget(viewer.getTextWidget(), operations);
//			_dropTargetListener = new DesignerSourceDropTargetListener(this);
//			_dropTarget.setTransfer(_dropTargetListener.getTransfers());
//			_dropTarget.addDropListener(_dropTargetListener);
		}catch(Exception e) {
			
		}
	}

	@Override
	public IAction getAction(final String actionID) {
		try {
			return super.getAction(actionID);
		} catch (final Exception e) {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(final Class required) {
		if (ITextEditorDropTargetListener.class.equals(required)) {
			final DesignerSourceDropTargetListener listener = new DesignerSourceDropTargetListener(this);
			return listener;
		}
		return super.getAdapter(required);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.sse.ui.StructuredTextEditor#doSetInput(org.eclipse.ui.
	 * IEditorInput)
	 */
	@Override
	protected void doSetInput(IEditorInput input) throws CoreException {
		super.doSetInput(input);
		addModelStateListener();
	}

	@Override
	public void dispose() {
		if (_dropTargetListener != null) {
			_dropTargetListener = null;
		}
		if (_dropTarget != null) {
			_dropTarget.dispose();
			_dropTarget = null;
		}
		removeModelStateListener();
		if (_structuredModel != null) {
			_structuredModel.releaseFromRead();
			_structuredModel = null;
		}
		super.dispose();
	}

	public IStructuredModel getStructuredModel() {
		IStructuredModel model = null;
		if (_structuredModel != null) {
			model = _structuredModel;
		} else {
			IDocumentProvider provider = getDocumentProvider();
			if (provider != null) {
				IDocument document = provider.getDocument(getEditorInput());
				_structuredModel = StructuredModelManager.getModelManager().getExistingModelForRead(document);
				model = _structuredModel;
			}
		}
		return model;
	}

	private void addModelStateListener() {
		IStructuredModel model = getStructuredModel();
		if (model != null) {
			if (_modelStateListener == null) {
				_modelStateListener = new ModelStateListener();
			} else {
				model.removeModelStateListener(_modelStateListener);
			}
			model.addModelStateListener(_modelStateListener);
		}
	}

	private void removeModelStateListener() {
		IStructuredModel model = getStructuredModel();
		if (model != null) {
			if (_modelStateListener != null) {
				model.removeModelStateListener(_modelStateListener);
				_modelStateListener = null;
			}
		}
	}

	private class ModelStateListener implements IModelStateListener {

		public void modelAboutToBeChanged(IStructuredModel model) {
			// do nothing
		}

		public void modelChanged(IStructuredModel model) {
			// do nothing
		}

		public void modelDirtyStateChanged(IStructuredModel model, boolean isDirty) {
			// Bug 330412 - [WPE] Drag 'n' Drop of tag from palette doesn't
			// notify team system of edit
			// Bug 330413 - [WPE] Modifying tag attribute using property sheet
			// doesn't notify team system of edit
			if (isDirty) {
				IFile file = StructuredModelUtil.getFileFor(model);
				if (file != null) {
					file.getWorkspace().validateEdit(new IFile[] { file }, null);
				}
			}
		}

		public void modelResourceDeleted(IStructuredModel model) {
			// do nothing
		}

		public void modelResourceMoved(IStructuredModel oldModel, IStructuredModel newModel) {
			// do nothing
		}

		public void modelAboutToBeReinitialized(IStructuredModel structuredModel) {
			// do nothing
		}

		public void modelReinitialized(IStructuredModel structuredModel) {
			// do nothing
		}
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

	public File getFile() {
		if (this.getEditorInput() instanceof FileEditorInput) {
			FileEditorInput fileeditorinput = (FileEditorInput) this.getEditorInput();
			IFile file = fileeditorinput.getFile();
			return file.getLocation().makeAbsolute().toFile();
		} else {
			File fle = new File(this.getEditorInput().getToolTipText());
			return fle;
		}

	}

}
