package tern.eclipse.ide.internal.ui.views;

import tern.eclipse.ide.core.IIDETernProject;
import tern.eclipse.ide.core.resources.TernDocumentFile;
import tern.server.protocol.outline.TernOutlineCollector;

public class TernOutline extends TernOutlineCollector {

	private final TernDocumentFile ternFile;

	public TernOutline(TernDocumentFile ternFile, IIDETernProject ternProject) {
		super(ternProject);
		this.ternFile = ternFile;
	}

	public TernDocumentFile getTernFile() {
		return ternFile;
	}
}
