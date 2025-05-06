package com.tulin.v8.imageviewer.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;


/**
 * <p>Title: Eclipse Plugin Development</p>
 * <p>Description: Free download</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Free</p>
 * @author gan.shu.man
 * @version 1.0
 */

public class ImageView extends ViewPart {
	public SWTImageCanvas imageCanvas;
	
	/**
	 * The constructor.
	 */
	public ImageView() {
	}
	
	/**
	 * Create the GUI.
	 * @param frame The Composite handle of parent
	 */
	public void createPartControl(Composite frame) {
		imageCanvas=new SWTImageCanvas(frame);
	}

	/**
	 * Called when we must grab focus.
	 * @see org.eclipse.ui.part.ViewPart#setFocus
	 */
	public void setFocus() {
		imageCanvas.setFocus();
	}

	/**
	 * Called when the View is to be disposed
	 */
	public void dispose() {
		imageCanvas.dispose();
		super.dispose();
	}

}