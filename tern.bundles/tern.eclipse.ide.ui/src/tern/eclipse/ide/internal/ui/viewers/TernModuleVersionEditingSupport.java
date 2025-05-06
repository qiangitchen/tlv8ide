package tern.eclipse.ide.internal.ui.viewers;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import tern.TernException;
import tern.server.ITernDef;
import tern.server.ITernModuleConfigurable;
import tern.server.ITernPlugin;

/**
 * {@link EditingSupport} to display a combo with version for
 * {@link ITernPlugin} or {@link ITernDef}.
 *
 */
public class TernModuleVersionEditingSupport extends EditingSupport {

	private final ComboBoxViewerCellEditor cellEditor;

	public TernModuleVersionEditingSupport(ColumnViewer viewer) {
		super(viewer);
		cellEditor = new ComboBoxViewerCellEditor((Composite) getViewer()
				.getControl(), SWT.READ_ONLY);
		cellEditor.setLabelProvider(new LabelProvider());
		cellEditor.setContentProvider(ArrayContentProvider.getInstance());
	}

	@Override
	protected boolean canEdit(Object element) {
		if (element instanceof ITernModuleConfigurable) {
			return ((ITernModuleConfigurable) element).getAvailableVersions()
					.size() > 0;
		}
		return false;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		if (element instanceof ITernModuleConfigurable) {
			cellEditor.setInput(((ITernModuleConfigurable) element)
					.getAvailableVersions());
			return cellEditor;
		}
		return null;
	}

	@Override
	protected Object getValue(Object element) {
		if (element instanceof ITernModuleConfigurable) {
			return ((ITernModuleConfigurable) element).getVersion();
		}
		return null;
	}

	@Override
	public void setValue(Object element, Object value) {
		if (element instanceof ITernModuleConfigurable) {
			try {
				((ITernModuleConfigurable) element).setVersion(value.toString());
				getViewer().update(element, null);
			} catch (TernException e) {
				e.printStackTrace();
			}
		}
	}

}
