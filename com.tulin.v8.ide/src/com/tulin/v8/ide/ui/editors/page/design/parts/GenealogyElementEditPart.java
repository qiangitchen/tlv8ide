package com.tulin.v8.ide.ui.editors.page.design.parts;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.gef.*;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.tulin.v8.ide.ui.editors.page.design.model.GenealogyElement;

/**
 * Shared behavior for several related {@link EditPart} subclasses such as
 * {@link PersonEditPart}.
 */
public abstract class GenealogyElementEditPart extends AbstractGraphicalEditPart
{
	/**
	 * Update the figure based upon the current model state
	 */
	protected void refreshVisuals() {
		GenealogyElement m = (GenealogyElement) getModel();
		Rectangle bounds = new Rectangle(m.getX(), m.getY(), m.getWidth(), m.getHeight());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), bounds);
		super.refreshVisuals();
	}


	// ==========================================================================
	// GenealogyElementListener
	
	/**
	 * Update the figure based upon the new model location
	 */
	public void locationChanged(int x, int y) {
		getFigure().setLocation(new Point(x, y));
	}

	/**
	 * Update the figure based upon the new model size
	 */
	public void sizeChanged(int width, int height) {
		getFigure().setSize(width, height);
	}
}