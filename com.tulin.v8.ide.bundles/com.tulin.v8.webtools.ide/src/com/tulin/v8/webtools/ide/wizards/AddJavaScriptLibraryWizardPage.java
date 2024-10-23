package com.tulin.v8.webtools.ide.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

public class AddJavaScriptLibraryWizardPage extends WizardPage {

	private Text text;
	private Table table;
	private String path = "";

	public AddJavaScriptLibraryWizardPage(IContainer container) {
		super("AddJavaScriptWizardPage");
		setTitle(WebToolsPlugin.getResourceString("AddJavaScriptLibraryWizardPage.Title"));
		setDescription(WebToolsPlugin.getResourceString("AddJavaScriptLibraryWizardPage.Description"));
		if (container != null) {
			this.path = container.getFullPath().toString();
		}
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		new Label(composite, SWT.NULL).setText("Folder:");
		text = new Text(composite, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setEditable(false);
		text.setText(path);

		Button button = new Button(composite, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
				doValidate();
			}
		});

		table = new Table(composite, SWT.CHECK | SWT.BORDER);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 3;
		table.setLayoutData(gd);

		String[] names = JavaScriptLibrariesManager.getLibraryNames();
		for (int i = 0; i < names.length; i++) {
			new TableItem(table, SWT.NULL).setText(names[i]);
		}

		setControl(composite);
		doValidate();
	}

	private void doValidate() {
		if (text.getText().length() == 0) {
			setErrorMessage(WebToolsPlugin.getResourceString("AddJavaScriptLibraryWizardPage.Error.ChooseFolder"));
			return;
		}
		setErrorMessage(null);
	}

	public String[] getSelectedLibraryNames() {
		List<String> result = new ArrayList<String>();
		for (TableItem item : table.getItems()) {
			if (item.getChecked()) {
				result.add(item.getText());
			}
		}
		return result.toArray(new String[result.size()]);
	}

	public IContainer getAddContainer() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(text.getText());
		if (resource instanceof IContainer) {
			return (IContainer) resource;
		}
		return null;
	}

	private void handleBrowse() {
		IContainer initial = null;

		if (text.getText().length() > 0) {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IResource resource = root.findMember(text.getText());
			if (resource != null && resource.exists() && resource instanceof IContainer) {
				initial = (IContainer) resource;
			}
		}

		if (initial == null) {
			initial = ResourcesPlugin.getWorkspace().getRoot();
		}

		ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(), initial, false, "");

		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				text.setText(result[0].toString());
			}
		}
	}

}
