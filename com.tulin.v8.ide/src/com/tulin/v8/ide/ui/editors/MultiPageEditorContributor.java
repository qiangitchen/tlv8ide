package com.tulin.v8.ide.ui.editors;

import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.sse.ui.internal.ISourceViewerActionBarContributor;
import org.eclipse.wst.xml.ui.internal.tabletree.IDesignViewerActionBarContributor;
import org.eclipse.wst.xml.ui.internal.tabletree.SourceEditorActionBarContributor;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLTableTreeActionBarContributor;

@SuppressWarnings("restriction")
public class MultiPageEditorContributor extends SourceEditorActionBarContributor {

	private boolean needsMultiInit = true;

	public MultiPageEditorContributor() {
		super();
	}

	protected void initDesignViewerActionBarContributor(IActionBars actionBars) {
		super.initDesignViewerActionBarContributor(actionBars);

		if (designViewerActionBarContributor != null) {
			if (designViewerActionBarContributor instanceof IDesignViewerActionBarContributor) {
				((IDesignViewerActionBarContributor) designViewerActionBarContributor).initViewerSpecificContributions(actionBars);
			}
		}
	}

	protected void activateDesignPage(IEditorPart activeEditor) {
		if ((sourceViewerActionContributor != null) && (sourceViewerActionContributor instanceof ISourceViewerActionBarContributor)) {
			// if design page is not really an IEditorPart, activeEditor ==
			// null, so pass in multiPageEditor instead (d282414)
			if (activeEditor == null) {
				sourceViewerActionContributor.setActiveEditor(multiPageEditor);
			}
			else {
				sourceViewerActionContributor.setActiveEditor(activeEditor);
			}
			((ISourceViewerActionBarContributor) sourceViewerActionContributor).setViewerSpecificContributionsEnabled(false);
		}

		if ((designViewerActionBarContributor != null) && (designViewerActionBarContributor instanceof IDesignViewerActionBarContributor)) {
			designViewerActionBarContributor.setActiveEditor(multiPageEditor);
			((IDesignViewerActionBarContributor) designViewerActionBarContributor).setViewerSpecificContributionsEnabled(true);
		}
	}

	protected void activateSourcePage(IEditorPart activeEditor) {
		if ((designViewerActionBarContributor != null) && (designViewerActionBarContributor instanceof IDesignViewerActionBarContributor)) {
			designViewerActionBarContributor.setActiveEditor(multiPageEditor);
			((IDesignViewerActionBarContributor) designViewerActionBarContributor).setViewerSpecificContributionsEnabled(false);
		}

		if ((sourceViewerActionContributor != null) && (sourceViewerActionContributor instanceof ISourceViewerActionBarContributor)) {
			sourceViewerActionContributor.setActiveEditor(activeEditor);
			((ISourceViewerActionBarContributor) sourceViewerActionContributor).setViewerSpecificContributionsEnabled(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorActionBarContributor#init(org.eclipse.ui.IActionBars)
	 */
	public void init(IActionBars actionBars) {
		super.init(actionBars);
		needsMultiInit = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorActionBarContributor#setActiveEditor(org.eclipse.ui.IEditorPart)
	 */
	public void setActiveEditor(IEditorPart targetEditor) {
		if (needsMultiInit) {
			designViewerActionBarContributor = new XMLTableTreeActionBarContributor();
			initDesignViewerActionBarContributor(getActionBars());
			needsMultiInit = false;
		}
		super.setActiveEditor(targetEditor);
	}

}
