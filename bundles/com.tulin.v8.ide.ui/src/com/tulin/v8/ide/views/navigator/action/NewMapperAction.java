package com.tulin.v8.ide.views.navigator.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.generator.CodeGenerator;

import zigen.plugin.db.ui.internal.ITable;

public class NewMapperAction extends Action implements Runnable {
	TreeViewer viewer;

	public NewMapperAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText("New Mapper");
		this.setToolTipText("New Mapper Controller Entry for Table");
		this.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("add.gif")));
	}

	public void run() {
		Object element = ((StructuredSelection) viewer.getSelection()).getFirstElement();
		if (element instanceof ITable) {
			ITable table = (ITable) element;
			new CodeGenerator(table.getDataBase(), "com.tlv8").genCode(table.getName());;
		}
	}
}
