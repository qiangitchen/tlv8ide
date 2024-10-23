package tern.tulin.v8.webtools.hover;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;

import tern.eclipse.ide.ui.hover.TernHover;

public class TernGenericeditorHover extends TernHover {

	@Override
	public Object getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion) {
		return super.getHoverInfo2(textViewer, hoverRegion);
	}

}
