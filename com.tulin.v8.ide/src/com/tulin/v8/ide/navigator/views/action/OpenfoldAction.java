package com.tulin.v8.ide.navigator.views.action;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.Sys;

import zigen.plugin.db.ui.internal.TreeNode;

public class OpenfoldAction extends Action implements Runnable {
	TreeViewer viewer;

	public OpenfoldAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText(Messages.getString("View.Action.openfile.9"));
		setToolTipText(Messages.getString("View.Action.openfile.10"));
		setImageDescriptor(ImageDescriptor.createFromImage(StudioPlugin.getIcon("folder-open.gif")));
	}

	public void run() {
		Object obj = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
		TreeNode trp = (TreeNode) obj;
		// 打开资源管理器
		try {
			if (trp.getPath() != null) {
				if (CommonUtil.isWinOS()) {
					Runtime.getRuntime().exec("explorer.exe /n," + trp.getPath());
				} else if (CommonUtil.isMacOS()) {
					if (java.awt.Desktop.isDesktopSupported()) {
						java.awt.Desktop.getDesktop().open(new File(trp.getPath()));
					} else {
						Runtime.getRuntime().exec("nautilus " + trp.getPath());
					}
				} else {
					Runtime.getRuntime().exec("nautilus " + trp.getPath());
				}
			}
		} catch (IOException e) {
			Sys.packErrMsg(e.toString());
		}
	}
}
