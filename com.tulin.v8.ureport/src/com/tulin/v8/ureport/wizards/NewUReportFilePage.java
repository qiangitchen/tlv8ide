package com.tulin.v8.ureport.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

import com.tulin.v8.ureport.Activator;

public class NewUReportFilePage extends WizardPage {
	private Text containerText;
	private Text fileText;
	private ISelection selection;
	private String containerName;
	private String fileName;
	private boolean canFinish;
	String extname = ".ureport";

	public NewUReportFilePage(String name, ISelection selection) {
		super(name);
		this.selection = selection;
		setTitle(Messages.getString("wizardsaction.newfilefold.title"));
		setDescription(Messages.getString("wizardsaction.newfilefold.createmessage"));
		setImageDescriptor(ImageDescriptor.createFromImage(Activator.getIcon("wizban/newfile_wiz.png")));
	}

	@Override
	public IWizardPage getNextPage() {
		return null;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		Label label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("wizardspage.message.folder"));

		containerText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		containerText.setLayoutData(gd);
		containerText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button button = new Button(container, SWT.PUSH);
		button.setText(Messages.getString("wizardspage.message.btbrowser"));
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
		label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("wizardsaction.newfilefold.namelabel"));

		fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fileText.setLayoutData(gd);
		fileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		initialize();
//		dialogChanged();
		setControl(container);
		fileText.setFocus();
	}

	private void initialize() {
		if (selection != null && selection.isEmpty() == false && selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
				containerText.setText(container.getFullPath().toString());
				setContainerName(container.getFullPath().toString());
			}
		}
		fileText.setText("");
		setFileName("");
	}

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(),
				ResourcesPlugin.getWorkspace().getRoot(), false,
				Messages.getString("wizardspage.message.selectnewfolder"));
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				containerText.setText(((Path) result[0]).toString());
				setContainerName(((Path) result[0]).toString());
			}
		}
	}

	private void dialogChanged() {
		String fileName = fileText.getText();
		setPageComplete(false);
		setCanFinish(false);
		if (containerText.getText().length() == 0) {
			updateStatus(Messages.getString("wizardspage.message.mustafolder"));
			return;
		}
		if (fileName.length() == 0) {
			updateStatus(Messages.getString("wizardspage.message.musthaveFilename"));
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus(Messages.getString("wizardspage.message.mustivaFilename"));
			return;
		}
		if (checkName(fileName)) {
			updateStatus(Messages.getString("wizardsaction.newfilefold.nameerr0") + "\"" + fileName + "\""
					+ Messages.getString("wizardsaction.newfilefold.nameerr1"));
			return;
		}
		if (extname != null) {
			if (fileName.indexOf(".") > 0) {
				if (!extname.equals(fileName.substring(fileName.lastIndexOf(".")))) {
					updateStatus("后缀名必须 为：" + extname);
					return;
				}
			}
		}
		this.setFileName(fileName);
		updateStatus(null);
		setPageComplete(true);
		setCanFinish(true);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	boolean checkName(String name) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		IContainer container = (IContainer) resource;
		String sfileName = name;
		if (sfileName.indexOf(".") < 0) {
			sfileName = sfileName + ".ureport";
		}
		IFile files = container.getFile(new Path(sfileName));
		return files.exists();
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setCanFinish(boolean canFinish) {
		this.canFinish = canFinish;
	}

	public boolean isCanFinish() {
		return canFinish;
	}

}
