package com.tulin.v8.ide.wizards.tree;

import java.io.File;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
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

import com.tulin.v8.core.Sys;
import com.tulin.v8.ide.wizards.Messages;

import zigen.plugin.db.ui.internal.TreeNode;

public class TreePageEnd extends WizardPage {

	private Text containerText;
	private Text fileText;
	private ISelection selection;
	private String containerName;
	private String fileName;

	public TreePageEnd(ISelection selection) {
		super("treePageEnd");
		setTitle("简单树形");
		setDescription("简单树形生成路径和文件名配置.");
		this.selection = selection;
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
		label.setText(Messages.getString("wizardspage.message.filename"));

		fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fileText.setLayoutData(gd);
		fileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		initialize();
		dialogChanged();
		setControl(container);
	}

	@Override
	public IWizardPage getNextPage() {
		return null;
	}

	@SuppressWarnings("deprecation")
	private void initialize() {
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
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
				containerName = container.getFullPath().toString();
			}
			if (obj instanceof TreeNode) {
				TreeNode tr = (TreeNode) obj;
				try {
					File file = new File(tr.getPath());
					String conpath = file.toURL().toString();
					conpath = conpath.substring(
							conpath.indexOf("Workspaces") + 10,
							conpath.length() - 1);
					containerText.setText(conpath);
					containerName = conpath;
				} catch (Exception e) {
					Sys.packErrMsg(e.toString());
				}
			}
		}
		fileText.setText("MySampleTreePage.html");
		fileName = "MySampleTreePage.html";
	}

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				Messages.getString("wizardspage.message.selectnewfolder"));
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				containerText.setText(((Path) result[0]).toString());
				containerName = ((Path) result[0]).toString();
			}
		}
	}

	private void dialogChanged() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(containerText.getText()));
		String fileName = fileText.getText();

		if (containerText.getText().length() == 0) {
			updateStatus(Messages.getString("wizardspage.message.mustafolder"));
			return;
		}
		if (container == null
				|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus(Messages.getString("wizardspage.message.missthefolder"));
			return;
		}
		if (!container.isAccessible()) {
			updateStatus("Project must be writable");
			return;
		}
		if (fileName.length() == 0) {
			updateStatus(Messages.getString("wizardspage.message.musthaveFilename"));
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("File name must be valid");
			return;
		}
		int dotLoc = fileName.lastIndexOf('.');
		if (dotLoc != -1) {
			String ext = fileName.substring(dotLoc + 1);
			if (ext.equalsIgnoreCase("html") == false) {
				updateStatus(Messages.getString("wizardspage.message.musthtmlExam"));
				return;
			}
		}
		this.fileName = fileName;
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getContainerName() {
		return containerName;
	}

	public String getFileName() {
		return fileName;
	}

}
