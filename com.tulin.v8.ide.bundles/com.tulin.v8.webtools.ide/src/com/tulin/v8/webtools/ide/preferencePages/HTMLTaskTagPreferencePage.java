package com.tulin.v8.webtools.ide.preferencePages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.tulin.v8.webtools.ide.ProjectBuilder;
import com.tulin.v8.webtools.ide.ProjectNature;
import com.tulin.v8.webtools.ide.TableViewerSupport;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.html.tasktag.TaskTag;

/**
 * The preference page to add / edit / remove TaskTags.
 */
public class HTMLTaskTagPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private TableViewer viewer;
	private List<TaskTag> model = new ArrayList<TaskTag>();
	private List<TaskTag> oldModel = new ArrayList<TaskTag>();

	public HTMLTaskTagPreferencePage() {
		setPreferenceStore(WebToolsPlugin.getDefault().getPreferenceStore());
		setTitle(WebToolsPlugin.getResourceString("PreferencePage.TaskTag"));
	}

	protected Control createContents(Composite parent) {
		TableViewerSupport<TaskTag> support = new TableViewerSupport<TaskTag>(model, parent) {

			protected void initTableViewer(TableViewer viewer) {
				Table table = viewer.getTable();

				TableColumn col1 = new TableColumn(table, SWT.LEFT);
				col1.setText(WebToolsPlugin.getResourceString("PreferencePage.Tag"));
				col1.setWidth(100);

				TableColumn col2 = new TableColumn(table, SWT.LEFT);
				col2.setText(WebToolsPlugin.getResourceString("PreferencePage.Priority"));
				col2.setWidth(100);
			}

			protected TaskTag doAdd() {
				TaskTagDialog dialog = new TaskTagDialog(getShell());
				if (dialog.open() == Dialog.OK) {
					return dialog.getTaskTag();
				}
				return null;
			}

			protected void doEdit(TaskTag obj) {
				TaskTagDialog dialog = new TaskTagDialog(getShell(), obj);
				if (dialog.open() == Dialog.OK) {
					TaskTag newElement = dialog.getTaskTag();
					obj.setTag(newElement.getTag());
					obj.setPriority(newElement.getPriority());
				}
			}

			protected ITableLabelProvider createLabelProvider() {
				return new ITableLabelProvider() {

					public Image getColumnImage(Object element, int columnIndex) {
						return null;
					}

					public String getColumnText(Object element, int columnIndex) {
						switch (columnIndex) {
						case 0:
							return ((TaskTag) element).getTag();
						case 1:
							return ((TaskTag) element).getPriorityName();
						default:
							return element.toString();
						}
					}

					public void addListener(ILabelProviderListener listener) {
					}

					public void dispose() {
					}

					public boolean isLabelProperty(Object element, String property) {
						return false;
					}

					public void removeListener(ILabelProviderListener listener) {
					}
				};
			}

		};

		viewer = support.getTableViewer();
		model.addAll(TaskTag.loadFromPreference(false));
		syncModels();
		viewer.refresh();

		return support.getControl();
	}

	protected void performDefaults() {
		model.clear();
		model.addAll(TaskTag.loadFromPreference(true));
		viewer.refresh();
		processChange();
	}

	public boolean performOk() {
		TaskTag.saveToPreference(model);
		processChange();
		return true;
	}

	private void syncModels() {
		try {
			oldModel.clear();
			for (int i = 0; i < model.size(); i++) {
				oldModel.add((model.get(i)).clone());
			}
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}
	}

	public void init(IWorkbench workbench) {
	}

	private void processChange() {
		if (TaskTag.hasChange(oldModel, model)) {
			syncModels();
			try {
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IProject[] projects = root.getProjects();
				for (int i = 0; i < projects.length; i++) {
					if (projects[i].hasNature(ProjectNature.HTML_NATURE_ID)) {
						ProjectBuilder.doBuild(projects[i]);
					}
				}
			} catch (Exception ex) {
				WebToolsPlugin.logException(ex);
			}
		}
	}

	/**
	 * The dialog to add / edit TaskTags.
	 */
	private class TaskTagDialog extends Dialog {

		private Text textTag;
		private Combo comboPriority;
		private TaskTag element;

		public TaskTagDialog(Shell parentShell) {
			super(parentShell);
			setShellStyle(getShellStyle() | SWT.RESIZE);
		}

		public TaskTagDialog(Shell parentShell, TaskTag element) {
			super(parentShell);
			this.element = element;
		}

		protected Point getInitialSize() {
			Point size = super.getInitialSize();
			size.x = 300;
			return size;
		}

		protected Control createDialogArea(Composite parent) {
			getShell().setText(WebToolsPlugin.getResourceString("PreferencePage.TaskTag"));

			Composite composite = new Composite(parent, SWT.NULL);
			composite.setLayoutData(new GridData(GridData.FILL_BOTH));
			composite.setLayout(new GridLayout(2, false));

			Label label = new Label(composite, SWT.NULL);
			label.setText(WebToolsPlugin.getResourceString("PreferencePage.Dialog.Tag"));

			textTag = new Text(composite, SWT.BORDER);
			if (element != null) {
				textTag.setText(element.getTag());
			}
			textTag.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			label = new Label(composite, SWT.NULL);
			label.setText(WebToolsPlugin.getResourceString("PreferencePage.Dialog.Priority"));

			comboPriority = new Combo(composite, SWT.READ_ONLY);
			comboPriority.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			for (int i = 0; i < TaskTag.PRIORITIES.length; i++) {
				comboPriority.add(TaskTag.PRIORITIES[i]);
			}
			if (element != null) {
				comboPriority.setText(element.getPriorityName());
			} else {
				comboPriority.setText(TaskTag.NORMAL);
			}

			return composite;
		}

		protected void okPressed() {
			if (textTag.getText().length() == 0) {
				WebToolsPlugin.openAlertDialog(
						WebToolsPlugin.createMessage(WebToolsPlugin.getResourceString("Error.Required"),
								new String[] { WebToolsPlugin.getResourceString("PreferencePage.Tag") }));
				return;
			}

			element = new TaskTag(textTag.getText(), TaskTag.convertPriority(comboPriority.getText()));

			super.okPressed();
		}

		public TaskTag getTaskTag() {
			return element;
		}
	}

}
