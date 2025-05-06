package com.tulin.v8.webtools.ide.wizards;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class WebProjectWizardPage extends WizardPage {
	private Text prntext;
	private Combo modtext;
	private Text srctext;
	private Text cettext;
	private Text outtext;

	public WebProjectWizardPage() {
		super("webprojectWidzard");
		setTitle(Messages.getString("wizards.message.titleLable"));
		setDescription(Messages.getString("wizards.message.titleDescription"));
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, 0);
		composite.setLayout(new GridLayout(2, false));

		Label namel = new Label(composite, SWT.NONE);
		namel.setLayoutData(new GridData());
		namel.setText(Messages.getString("wizards.message.name"));

		prntext = new Text(composite, SWT.BORDER);
		prntext.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label modl = new Label(composite, SWT.NONE);
		modl.setLayoutData(new GridData());
		modl.setText(Messages.getString("wizards.message.model"));

		modtext = new Combo(composite, SWT.DROP_DOWN);
		modtext.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		modtext.add("2.0");
		modtext.add("3.0");
		modtext.add("4.0");
		modtext.add("5.0");
		modtext.setText("5.0");

		Label srcl = new Label(composite, SWT.NONE);
		srcl.setLayoutData(new GridData());
		srcl.setText(Messages.getString("wizards.message.classsrc"));

		srctext = new Text(composite, SWT.BORDER);
		srctext.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		srctext.setText("src");

		Label cetl = new Label(composite, SWT.NONE);
		cetl.setLayoutData(new GridData());
		cetl.setText(Messages.getString("wizards.message.webcont"));

		cettext = new Text(composite, SWT.BORDER);
		cettext.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cettext.setText("WebContent");

		Label outl = new Label(composite, SWT.NONE);
		outl.setLayoutData(new GridData());
		outl.setText(Messages.getString("wizards.message.classout"));

		outtext = new Text(composite, SWT.BORDER);
		outtext.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		outtext.setText("WebContent" + File.separator + "WEB-INF" + File.separator + "classes");

		setControl(composite);

		prntext.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (prntext.getText().isEmpty()) {
					setMessage(Messages.getString("wizards.message.emptyname"), ERROR);
					setPageComplete(false);
				} else if (isHaveProject(prntext.getText())) {
					setMessage(Messages.getString("wizards.message.exitsname"), ERROR);
					setPageComplete(false);
				} else {
					setMessage(Messages.getString("wizards.message.titleDescription"));
					setPageComplete(true);
				}
			}
		});

		cettext.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				outtext.setText(cettext.getText() + "/WEB-INF/" + "classes");
			}
		});

		modtext.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String mv = modtext.getText();
				if (Float.valueOf(mv) > 3) {
					srctext.setText("src/main/java");
					cettext.setText("src/main/webapp");
				} else {
					srctext.setText("src");
					cettext.setText("WebContent");
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	@Override
	public boolean isPageComplete() {
		if (prntext.getText() == null || "".equals(prntext.getText())) {
			setMessage(Messages.getString("wizards.message.emptyname"), ERROR);
			return false;
		}
		return isHaveProject(prntext.getText()) ? false : true;
	}

	private boolean isHaveProject(String name) {
		IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject myWebProject = myWorkspaceRoot.getProject(name);
		return myWebProject.exists();
	}

	public String getName() {
		return prntext.getText();
	}

	public String getSRC() {
		return srctext.getText();
	}

	public String getCentont() {
		return cettext.getText();
	}

	public String getOut() {
		return outtext.getText();
	}

}
