package com.tulin.v8.webtools.ide.views;

import org.eclipse.jface.resource.ImageDescriptor;

import com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditor;

public interface IPaletteItem {
	
	public String getLabel();
	
	public ImageDescriptor getImageDescriptor();
	
	public void execute(HTMLSourceEditor editor);
	
}
