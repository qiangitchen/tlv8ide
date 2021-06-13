/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class DDLDiffEditorInput implements IEditorInput {

	private String tooltip;

	private String name;

	private IDDLDiff[] diffs;

	private boolean isTableDiff = false;

	public DDLDiffEditorInput(IDDLDiff[] diffs, boolean isTableDiff) {
		super();
		this.name = Messages.getString("DDLDiffEditorInput.0"); //$NON-NLS-1$
		this.tooltip = Messages.getString("DDLDiffEditorInput.1"); //$NON-NLS-1$
		this.diffs = diffs;
		this.isTableDiff = isTableDiff;

	}

	public boolean exists() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return name;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return tooltip;
	}

	public Object getAdapter(Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;

		return false;
	}

	public int hashCode() {
		return name.hashCode();
	}

	public void setToolTipText(String tooltip) {
		this.tooltip = tooltip;
	}

	public IDDLDiff[] getDiffs() {
		return diffs;
	}

	public boolean isTableDiff() {
		return isTableDiff;
	}

}
