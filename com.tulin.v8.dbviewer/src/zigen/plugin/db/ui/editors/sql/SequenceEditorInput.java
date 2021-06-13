/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.sql;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ext.oracle.internal.OracleSequenceInfo;

public class SequenceEditorInput implements IEditorInput {

	private String tooltip;

	private String name;

	private IDBConfig config;;

	private OracleSequenceInfo sequenceInfo;

	public SequenceEditorInput(IDBConfig config, OracleSequenceInfo sequenceInfo) {

		super();
		this.config = config;
		this.sequenceInfo = sequenceInfo;
		this.name = sequenceInfo.getSequence_name() + "[SEQUENCE]"; //$NON-NLS-1$
		this.tooltip = this.name;

	}

	public boolean exists() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return this.name;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return this.tooltip;
	}

	public Object getAdapter(Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (o instanceof SequenceEditorInput) {
			SequenceEditorInput input = (SequenceEditorInput) o;

			if (config.getDbName().equals(input.config.getDbName())) {
				return name.equals(input.getName());
			} else {
				return false;
			}
		}
		return false;
	}

	public int hashCode() {
		return name.hashCode();
	}

	public IDBConfig getConfig() {
		return config;
	}

	public void setToolTipText(String tooltip) {
		this.tooltip = tooltip;
	}

	public OracleSequenceInfo getSequenceInfo() {
		return sequenceInfo;
	}

	public void setSequenceInfo(OracleSequenceInfo sequenceInfo) {
		this.sequenceInfo = sequenceInfo;
	}

}
