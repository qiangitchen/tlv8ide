package tern.eclipse.ide.internal.core;

import java.util.ArrayList;
import java.util.List;

import tern.eclipse.ide.core.IWorkingCopyListener;
import tern.server.ITernModule;

/**
 * List of tern modules which fire
 * {@link IWorkingCopyListener#moduleSelectionChanged(ITernModule, boolean)}
 * when a tern module is added or removed.
 */
@SuppressWarnings("serial")
public class WorkingCopyModuleList extends ArrayList<ITernModule> {

	private final WorkingCopy workingCopy;

	public WorkingCopyModuleList(WorkingCopy workingCopy,
			List<ITernModule> modules) {
		super(modules);
		this.workingCopy = workingCopy;
	}

	@Override
	public boolean add(ITernModule e) {
		boolean result = super.add(e);
		if (result) {
			workingCopy.fireSelectionModules(e, true);
		}
		return result;
	}

	@Override
	public boolean remove(Object o) {
		boolean result = super.remove(o);
		if (result && o instanceof ITernModule) {
			workingCopy.fireSelectionModules((ITernModule) o, false);
		}
		return result;
	}
}
