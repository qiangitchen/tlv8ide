package tern.eclipse.ide.ui.viewers;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

import tern.scriptpath.ITernScriptPath;

/**
 * Content provider for tern script path {@link ITernScriptPath}.
 *
 */
public class TernScriptPathContentProvider extends ArrayContentProvider
		implements ITreeContentProvider {

	private static final TernScriptPathContentProvider INSTANCE = new TernScriptPathContentProvider();

	public static TernScriptPathContentProvider getInstance() {
		return INSTANCE;
	}

	@Override
	public Object[] getChildren(Object element) {
		if ((element instanceof ITernScriptPath)) {
			return ((ITernScriptPath) element).getScriptResources().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return element instanceof ITernScriptPath;
	}

}
