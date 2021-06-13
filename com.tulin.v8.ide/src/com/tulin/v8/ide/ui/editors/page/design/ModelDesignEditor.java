package com.tulin.v8.ide.ui.editors.page.design;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;

import com.tulin.v8.ide.ui.editors.page.design.parts.GenealogyEditPartFactory;

public class ModelDesignEditor extends GraphicalEditorWithFlyoutPalette {
	
	public ModelDesignEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}
	
	/**
	 * Configure the viewer to display a genealogy graph
	 */
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		final GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new GenealogyEditPartFactory());
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());
		
		// Two different approaches for dynamically modifying the selection
		// to exclude nested figures when that figure's ancestor is selected
		
		// Option #1
		//viewer.addSelectionChangedListener(new SelectionModificationChangeListener(viewer));
		
		// Option #2
		viewer.setSelectionManager(new ModifiedSelectionManager(viewer));
		
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		return EditorPaletteFactory.createPalette();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO 自动生成的方法存根

	}

}
