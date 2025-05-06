package tern.eclipse.ide.ui.views;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import tern.server.protocol.outline.IJSNode;

public class TernOutlineContentProvider implements ITreeContentProvider {

	public static final Object COMPUTING_NODE = new Object();
	private static final Object[] COMPUTING_NODES = { COMPUTING_NODE };
	public static final Object UNAVAILABLE_NODE = new Object();
	private static final Object[] ANAVAILABLE_NODES = { UNAVAILABLE_NODE };

	private static final Object[] EMPTY_ARRAY = new Object[0];

	@Override
	public Object[] getElements(Object element) {
		if (element instanceof IOutlineProvider) {
			IOutlineProvider provider = (IOutlineProvider) element;
			switch (provider.getOutlineState()) {
			case Unavailable:
				return ANAVAILABLE_NODES;
			case Computing:
				return COMPUTING_NODES;
			case Done:
				IJSNode root = provider.getRoot();
				if (root != null) {
					return root.getChildren().toArray();
				}
			}
		}
		return EMPTY_ARRAY;
	}

	@Override
	public Object[] getChildren(Object element) {
		if (element instanceof IJSNode) {
			return ((IJSNode) element).getChildren().toArray();
		}
		return EMPTY_ARRAY;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof IJSNode) {
			return ((IJSNode) element).getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof IJSNode) {
			return ((IJSNode) element).hasChidren();
		}
		return false;
	}

	@Override
	public final void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// Do nothing
	}

	@Override
	public void dispose() {
		// Do nothing
	}
}
