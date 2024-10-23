package com.tulin.v8.webtools.ide.text;

import org.eclipse.jface.text.IDocumentPartitioningListener;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

import com.tulin.v8.webtools.ide.IDocumentConfirg;

public abstract class AbsTextSourceViewerConfiguration extends TextSourceViewerConfiguration
		implements IDocumentPartitioningListener, IDocumentConfirg {

}
