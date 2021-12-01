package com.tulin.v8.ureport.ui.editors.designer.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.tulin.v8.ureport.Activator;
import com.tulin.v8.ureport.ui.Messages;
import com.tulin.v8.ureport.ui.editors.designer.UReportEditor;

/**
 * @DE 跳转到源码页
 * @author ChenQian
 */
public class ViewSourseAction extends Action {
	private UReportEditor editor;

	public ViewSourseAction(UReportEditor editor) {
		super();
		this.editor = editor;
		this.setText(Messages.getString("design.action.viewsource"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(Activator.getIcon("tag.gif")));
	}

	public void run() {
		editor.setActivePages(0);
	}

}
