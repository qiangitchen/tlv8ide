/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class ProjectSelectDialog extends TitleAreaDialog {

	TreeViewer viewer;

	Text fileText;

	String saveFileName;

	IContainer container;

	private String title = Messages.getString("ProjectSelectDialog.0"); //$NON-NLS-1$

	public ProjectSelectDialog(Shell parentShell, String text) {
		super(parentShell);
		this.title = Messages.getString("ProjectSelectDialog.1"); //$NON-NLS-1$
		setShellStyle(getShellStyle() | SWT.RESIZE);
		parentShell.setText(text);
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(title);
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	protected Control createDialogArea(Composite parent) {
		super.setTitle(title);
		super.setMessage(Messages.getString("ProjectSelectDialog.5"), IMessageProvider.NONE); //$NON-NLS-1$

		Composite composite = (Composite) super.createDialogArea(parent);

		Composite composite2 = new Composite(composite, SWT.NONE);
		composite2.setLayoutData(new GridData(GridData.FILL_BOTH));

		composite2.setLayout(new GridLayout(1, false));

		viewer = new TreeViewer(composite2, SWT.BORDER);
		viewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite composite3 = new Composite(composite2, SWT.NONE);
		composite3.setLayout(new GridLayout(2, false));
		composite3.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = new Label(composite3, SWT.NONE);
		label.setText(Messages.getString("ProjectSelectDialog.2")); //$NON-NLS-1$

		fileText = new Text(composite3, SWT.BORDER);
		fileText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fileText.setText(Messages.getString("ProjectSelectDialog.3")); //$NON-NLS-1$
		fileText.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				if (fileText.getText().trim().length() == 0) {
					getButton(IDialogConstants.OK_ID).setEnabled(false);
				} else {
					getButton(IDialogConstants.OK_ID).setEnabled(true);
				}
			}

		});

		viewer.setContentProvider(new WorkbenchContentProvider());

		viewer.addFilter(new SourceDirViewerFilter());

		WorkbenchLabelProvider provider = new WorkbenchLabelProvider();
		viewer.setLabelProvider(provider);

		IWorkspace ws = ResourcesPlugin.getWorkspace();
		viewer.setInput(ws);

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				selectionChangeHandler(event);
			}
		});

		return composite;
	}

	protected Control createContents(Composite parent) {
		Control ctl = super.createContents(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		return ctl;
	}


	protected void okPressed() {
		saveFileName = fileText.getText().trim();
		super.okPressed();
	}

	private void selectionChangeHandler(SelectionChangedEvent event) {
		Object obj = (Object) ((StructuredSelection) event.getSelection()).getFirstElement();
		if (obj instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) obj;
			IResource resource = (IResource) adaptable.getAdapter(IResource.class);

			if (resource instanceof IProject || resource instanceof IFolder) {
				container = (IContainer) resource;
				getButton(IDialogConstants.OK_ID).setEnabled(true);

			} else if (resource instanceof IFile) {
				IFile file = (IFile) resource;
				fileText.setText(file.getName());

				container = file.getParent();
				getButton(IDialogConstants.OK_ID).setEnabled(true);

			}
		} else {

			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}

	}

	private void validate() {
		IContainer container = getContainer();
		if (container != null) {
			IFile file = container.getFile(new Path(getSaveFileName()));
			if (file.exists()) {
				super.setMessage(Messages.getString("ProjectSelectDialog.4"), IMessageProvider.WARNING); //$NON-NLS-1$
			}
		}
	}

	protected Point getInitialSize() {
		return new Point(480, 450);
	}

	class SourceDirViewerFilter extends ViewerFilter {

		public boolean select(Viewer viewer, Object parentElement, Object element) {

			if (element instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) element;

				IResource res = (IResource) adaptable.getAdapter(IResource.class);

				if (res != null) {

					if (res instanceof IProject || res instanceof IFolder) {
						return true;
					}

					if ("diff".equals(res.getFileExtension().toLowerCase())) { //$NON-NLS-1$
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}

			}

			return false;
		}

	}

	public IContainer getContainer() {
		return container;
	}

	public String getSaveFileName() {
		return saveFileName;
	}

	public void setSaveFileName(String saveFileName) {
		this.saveFileName = saveFileName;
	}

}
