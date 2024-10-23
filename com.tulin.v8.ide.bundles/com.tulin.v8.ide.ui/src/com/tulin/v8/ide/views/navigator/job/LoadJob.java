package com.tulin.v8.ide.views.navigator.job;

import org.eclipse.jface.viewers.TreeViewer;

import com.tulin.v8.ide.internal.Root;
import com.tulin.v8.ide.utils.ActionUtils;

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
