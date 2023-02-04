package com.tulin.v8.ide.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.tulin.v8.ide.StudioPlugin;

/*
 * 首选项TuLin Studio
 */
public class StudioPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL);
		composite.setLayoutData(data);

		Image mImage = StudioPlugin.getDefault().getImage(
				"myeclipse_splash_small.gif");
		Label labelImg = new Label(composite, SWT.NONE);
		labelImg.setImage(mImage);

		Label label = new Label(composite, SWT.NONE);
		label.setText("TuLin Enterprise Workbench"
				+ "\nCopyright www.tlv8.com\n"
				+ "\nName: "
				+ StudioPlugin.getPluginName()
				+ "\nVendor: "
				+ StudioPlugin.getPluginVendor()
				+ "\nVersion: "
				+ StudioPlugin.getVersion()
				+ "\nBuild Id: "
				+ StudioPlugin.getPluginVersion());

		return composite;
	}

	public StudioPreferencePage() {
		super(GRID);
	}

	public void createFieldEditors() {

	}

	public void init(IWorkbench workbench) {

	}
}
