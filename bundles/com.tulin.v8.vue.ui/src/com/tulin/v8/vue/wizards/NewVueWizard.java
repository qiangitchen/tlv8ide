package com.tulin.v8.vue.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.tulin.v8.editors.vue.editor.VueEditor;
import com.tulin.v8.vue.wizards.baseForm.BaseFormEndPage;
import com.tulin.v8.vue.wizards.baseForm.BaseFormPage;
import com.tulin.v8.vue.wizards.baseForm.WriteBaseForm;
import com.tulin.v8.vue.wizards.cardList.CardListEndPage;
import com.tulin.v8.vue.wizards.cardList.CardListPage;
import com.tulin.v8.vue.wizards.cardList.WriteCardList;
import com.tulin.v8.vue.wizards.tableList.TableListEndPage;
import com.tulin.v8.vue.wizards.tableList.TableListLayoutPage;
import com.tulin.v8.vue.wizards.tableList.WriteTableList;

public class NewVueWizard extends Wizard implements INewWizard {
	ISelection selection;

	ProjectSelectPage projctPage;
	DataSelectPage dataSelectPage;

	TableListLayoutPage tableListPageLayoutPage;
	TableListEndPage tableListPageEndPage;

	BaseFormPage baseFormPage;
	BaseFormEndPage baseFormEndPage;

	public NewVueWizard() {
		super();
	}

	public NewVueWizard(ISelection selection) {
		super();
		this.selection = selection;
		setWindowTitle(Messages.getString("wizards.vue.title"));
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		setWindowTitle(Messages.getString("wizards.vue.title"));
	}

	@Override
	public void addPages() {
		projctPage = new ProjectSelectPage();
		addPage(projctPage);
		dataSelectPage = new DataSelectPage(projctPage);
		addPage(dataSelectPage);

		tableListPageLayoutPage = new TableListLayoutPage(dataSelectPage);
		addPage(tableListPageLayoutPage);
		tableListPageEndPage = new TableListEndPage(selection);
		addPage(tableListPageEndPage);

		baseFormPage = new BaseFormPage(dataSelectPage);
		addPage(baseFormPage);
		baseFormEndPage = new BaseFormEndPage(selection);
		addPage(baseFormEndPage);

		addPage(new CardListPage(dataSelectPage));
		addPage(new CardListEndPage(selection));

		addPage(new EndPage(selection));
	}

	@Override
	public boolean canFinish() {
		IWizardPage cpage = getContainer().getCurrentPage();
		return cpage.isPageComplete() && cpage.getNextPage() == null;
	}

	@Override
	public boolean performFinish() {
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(monitor);
				} catch (Exception e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}

	private void doFinish(IProgressMonitor monitor) throws Exception {
		IWizardPage currentPage = getContainer().getCurrentPage();
		IFile files = null;
		if (currentPage instanceof TableListEndPage) {// 表格列表
			try {
				files = new WriteTableList((TableListLayoutPage) getPage("tableListPageLayout"),
						(TableListEndPage) getPage("tableListPageEnd")).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		}
		if (currentPage instanceof CardListEndPage) {// 卡片列表
			try {
				files = new WriteCardList((CardListPage) getPage("cardListPage"),
						(CardListEndPage) getPage("cardListEnd")).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		}
		if (currentPage instanceof BaseFormEndPage) {// 基础表单
			try {
				files = new WriteBaseForm((BaseFormPage) getPage("baseFormPage"),
						(BaseFormEndPage) getPage("baseFormEnd")).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		}
		System.out.println(currentPage);
		if (files == null) {
			return;
		}
		final IFile file = files;
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					file.setPersistentProperty(IDE.EDITOR_KEY, VueEditor.ID);
					IDE.openEditor(page, file);
				} catch (Exception e) {
				}
			}
		});
		monitor.worked(1);
	}

	InputStream openContentStream() throws Exception {
		String contents = "<template>\n</template>";
		return new ByteArrayInputStream(contents.getBytes("UTF-8"));
	}

	void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "TuLin Studio", IStatus.OK, message, null);
		throw new CoreException(status);
	}

}
