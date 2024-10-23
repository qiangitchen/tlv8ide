package com.tulin.v8.imageviewer.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import com.tulin.v8.imageviewer.views.ImageView;
import com.tulin.v8.imageviewer.views.SWTImageCanvas;

/**
 * <p>
 * Title: Eclipse Plugin Development
 * </p>
 * <p>
 * Description: Free download
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Free
 * </p>
 * 
 * @author gan.shu.man
 * @version 1.0
 */

public class PushActionDelegate implements IViewActionDelegate {
	/** pointer to image view */
	public ImageView view = null;
	/** Action id of this delegate */
	public String id;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart viewPart) {
		if (viewPart instanceof ImageView) {
			this.view = (ImageView) viewPart;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		String id = action.getId();
		SWTImageCanvas imageCanvas = view.imageCanvas;
		if (id.equals("toolbar.open")) {
			imageCanvas.onFileOpen();
			return;
		}
		if (imageCanvas.getSourceImage() == null)
			return;
		if (id.equals("toolbar.zoomin")) {
			imageCanvas.zoomIn();
			return;
		} else if (id.equals("toolbar.zoomout")) {
			imageCanvas.zoomOut();
			return;
		} else if (id.equals("toolbar.fit")) {
			imageCanvas.fitCanvas();
			return;
		} else if (id.equals("toolbar.rotate")) {
			imageCanvas.rotateLeft();
			return;
		} else if (id.equals("toolbar.rotater")) {
			imageCanvas.rotateRight();
			return;
		} else if (id.equals("toolbar.original")) {
			imageCanvas.showOriginal();
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.
	 * IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
