/*
 * Created on 1 juil. 2004
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tulin.v8.tomcat.editors;

import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Bruno
 * 
 *         To change this generated comment go to Window>Preferences>Java>Code
 *         Generation>Code and Comments
 */
public class TomcatFileFieldEditor extends FileFieldEditor {

	boolean enabledField = true;

	public TomcatFileFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}

	protected boolean doCheckState() {
		if (enabledField) {
			return super.doCheckState();
		}
		return true;
	}

	public void setEnabled(boolean enabled, Composite parent) {
		this.enabledField = enabled;
		super.setEnabled(enabled, parent);
	}

	public void valueChanged() {
		super.valueChanged();
	}
}
