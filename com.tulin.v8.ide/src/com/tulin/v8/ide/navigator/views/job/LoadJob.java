package com.tulin.v8.ide.navigator.views.job;

import org.eclipse.jface.viewers.TreeViewer;

import com.tulin.v8.ide.navigator.views.Root;
import com.tulin.v8.ide.navigator.views.action.ActionUtils;

public class LoadJob implements Runnable {
	TreeViewer viewer;
	Root root;

	public LoadJob(TreeViewer viewer,Root root) {
		this.viewer = viewer;
		this.root = root;
	}

	@Override
	public void run() {
		ActionUtils.loadModel(viewer,root);
	}
}
