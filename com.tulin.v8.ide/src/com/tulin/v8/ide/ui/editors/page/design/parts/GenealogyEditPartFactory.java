package com.tulin.v8.ide.ui.editors.page.design.parts;

import org.eclipse.gef.*;

/**
 * A factory for constructing {@link EditPart}s for genealogy model objects.
 */
public class GenealogyEditPartFactory implements EditPartFactory {
	public EditPart createEditPart(EditPart context, Object model) {

		throw new IllegalStateException("No EditPart for " + model.getClass());
	}
}
