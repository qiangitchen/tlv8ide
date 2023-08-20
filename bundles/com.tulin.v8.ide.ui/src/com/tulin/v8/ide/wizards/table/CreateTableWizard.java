package com.tulin.v8.ide.wizards.table;

import java.io.File;

import org.eclipse.core.internal.filesystem.local.LocalFile;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;

import com.tulin.v8.core.Sys;
import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.ide.StructureComposition;
import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.editors.data.DataEditor;
import com.tulin.v8.ide.editors.data.TableViewEditorInput;
import com.tulin.v8.ide.utils.StudioConfig;
import com.tulin.v8.ide.views.navigator.ModelView;
import com.tulin.v8.ide.wizards.Messages;

import zigen.plugin.db.core.IDBConfig;

@SuppressWarnings({ "unused", "restriction" })
public class CreateTableWizard extends Wizard implements INewWizard {
	public static String ID = "TuLin.wizards.table.CreateTableWizard";
	private IDBConfig dbConfig;
	private String dbkey;
	private String owner;
	private ModelView viewer;
	private SelectDbkeyPage dbselect;
	private TableWritePage tablewritepage;
	private CreateTableEndPage createtableendpage;

	private ISelection selection;

	public CreateTableWizard() {
		super();
		setHelpAvailable(false);
	}

	public CreateTableWizard(ISelection selection) {
		this(selection, null);
	}

	public CreateTableWizard(ISelection selection, Object object) {
		super();
		this.selection = selection;

		setNeedsProgressMonitor(true);
		setHelpAvailable(false);

		TuLinPlugin.setSelection(selection);
	}

	public CreateTableWizard(IDBConfig dbConfig, String dbkey, String owner, ModelView viewer) {
		super();

		this.dbConfig = dbConfig;
		this.dbkey = dbkey;
		this.owner = owner;
		this.viewer = viewer;

		setNeedsProgressMonitor(true);
		setHelpAvailable(false);
	}

	public void addPages() {
		dbselect = new SelectDbkeyPage();
		if (dbkey == null) {
			addPage(dbselect);
			tablewritepage = new TableWritePage(dbselect);
		} else {
			tablewritepage = new TableWritePage(dbkey, owner);
		}
		addPage(tablewritepage);
		createtableendpage = new CreateTableEndPage(tablewritepage);
		addPage(createtableendpage);

		setWindowTitle(Messages.getString("wizardsaction.newtable.title"));
	}

	public boolean canFinish() {
		IWizardPage currentPage = this.getContainer().getCurrentPage();
		if (createtableendpage == currentPage) {
			return true;
		}
		return false;
	}

	@Override
	public boolean performFinish() {
		boolean re = createtableendpage.comitAction();
		if (re) {
			final String cdbkey = dbkey;
			final String tablename = tablewritepage.TableName.getText().toUpperCase();
			viewer.getSite().getShell().getDisplay().syncExec(new Runnable() {
				public void run() {
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					try {
						TableViewEditorInput editorinput = new TableViewEditorInput(dbConfig, tablename, "TABLE");
						IDE.openEditor(page, editorinput, DataEditor.ID);
					} catch (Exception e) {
						showMessage(e.toString());
					}
				}
			});
		}
		return re;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(getShell(), StudioPlugin.getResourceString("perspective.title.0"), message);
	}

}
