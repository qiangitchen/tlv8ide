package tern.eclipse.ide.internal.ui.views;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IToolBarManager;

import tern.eclipse.ide.internal.ui.views.actions.LexicalSortingAction;
import tern.eclipse.ide.ui.TernUIPlugin;
import tern.eclipse.ide.ui.views.AbstractTernContentOutlinePage;

/**
 * Tern outline page which displays variable function declarations in a treeview
 * for the current activated JavaScript editor.
 *
 */
public class TernContentOutlinePage extends AbstractTernContentOutlinePage {

	private LexicalSortingAction sortAction;

	public TernContentOutlinePage(IProject project, TernOutlineView view) {
		super(project, view);
	}

	@Override
	protected String getViewerId() {
		return TernUIPlugin.PLUGIN_ID + ".outline";
	}

	@Override
	public IFile getFile() {
		return getCurrentFile();
	}

	@Override
	protected void registerActions(IToolBarManager manager) {
		sortAction = new LexicalSortingAction(this);
		manager.add(sortAction);
		super.registerActions(manager);
	}

	@Override
	protected boolean isRefreshOutline(IFile oldFile, IFile newFile) {
		if (newFile != null) {
			return !newFile.equals(oldFile);
		}
		return false;
	}

}
