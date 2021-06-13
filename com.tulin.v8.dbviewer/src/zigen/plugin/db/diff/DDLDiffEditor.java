/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import zigen.plugin.db.DbPlugin;

public class DDLDiffEditor extends EditorPart {

	private IDDLDiff[] diffs;

	private DDLDiffViewer diffviewer;

	private TreeViewer treeViewer;

	private SashForm sash;

	private boolean isTableDiff = false;

	public static final String ID = "zigen.plugin.db.diff.DDLDiffEditor"; //$NON-NLS-1$

	public DDLDiffEditor() {
		super();

	}

	public void createPartControl(Composite parent) {
		CompareConfiguration cc = new CompareConfiguration();

		if (!isTableDiff) {
			sash = new SashForm(parent, SWT.VERTICAL | SWT.NONE);
			treeViewer = new TreeViewer(sash, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FLAT);
			treeViewer.setContentProvider(new DiffContentProvider());
			treeViewer.setLabelProvider(new DDLLabelProvider());
			treeViewer.setInput(diffs);

			Composite body = new Composite(sash, SWT.BORDER | SWT.FLAT);

			body.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 1;
			gridLayout.makeColumnsEqualWidth = false;
			gridLayout.marginHeight = 1;
			gridLayout.marginWidth = 1;
			gridLayout.horizontalSpacing = 2;
			gridLayout.verticalSpacing = 2;
			body.setLayout(gridLayout);

			ToolBar toolBar = new ToolBar(body, SWT.FLAT);
			// toolBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			toolBar.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
			ToolBarManager toolBarManager = new ToolBarManager(toolBar);

			Composite lower = new Composite(body, SWT.BORDER | SWT.FLAT);
			lower.setLayout(new FillLayout(SWT.HORIZONTAL));
			lower.setLayoutData(new GridData(GridData.FILL_BOTH));
			diffviewer = new DDLDiffViewer(lower, cc);
			diffviewer.addToolItems(toolBarManager);

			diffviewer.setContentProvider(new DDLDiffContentProvider(cc));
			if (diffs != null && diffs.length >= 1) {
				diffviewer.setInput(diffs[0]);
			}

			treeViewer.addSelectionChangedListener(new DiffTreeSelectionHandler(this));

			TreeItem topItem = treeViewer.getTree().getTopItem();
			treeViewer.getTree().setSelection(new TreeItem[] {topItem});

			sash.setWeights(new int[] {30, 70});

			toolBarManager.update(true);

			hookContextMenu();

		} else {

			parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 1;
			gridLayout.makeColumnsEqualWidth = false;
			gridLayout.marginHeight = 1;
			gridLayout.marginWidth = 1;
			gridLayout.horizontalSpacing = 2;
			gridLayout.verticalSpacing = 2;
			parent.setLayout(gridLayout);

			ToolBar toolBar = new ToolBar(parent, SWT.FLAT);
			// toolBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			toolBar.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

			ToolBarManager toolBarManager = new ToolBarManager(toolBar);

			Composite lower = new Composite(parent, SWT.BORDER | SWT.FLAT);
			lower.setLayout(new FillLayout(SWT.HORIZONTAL));
			lower.setLayoutData(new GridData(GridData.FILL_BOTH));
			diffviewer = new DDLDiffViewer(lower, cc);
			diffviewer.addToolItems(toolBarManager);
			diffviewer.setContentProvider(new DDLDiffContentProvider(cc));

			if (diffs != null && diffs.length >= 1) {
				diffviewer.setInput(diffs[0]);
			}

			toolBarManager.update(true);
		}

	}

	private IToolBarManager getToolBarManager() {
		return getEditorSite().getActionBars().getToolBarManager();

	}

	public void dispose() {
		super.dispose();
	}

	public void doSave(IProgressMonitor monitor) {
		ByteArrayOutputStream out = null;
		ObjectOutputStream oos = null;
		try {
			out = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(out);
			oos.writeObject(diffs);

			Shell shell = DbPlugin.getDefault().getShell();

			ProjectSelectDialog dialog = new ProjectSelectDialog(shell, Messages.getString("DDLDiffEditor.0")); //$NON-NLS-1$
			if (dialog.open() == ProjectSelectDialog.OK) {
				setDirty(false);
				IContainer container = dialog.getContainer();
				IFile file = container.getFile(new Path(dialog.getSaveFileName()));
				InputStream is = new ByteArrayInputStream(out.toByteArray());

				if (file.exists()) {
					if (DbPlugin.getDefault().confirmDialog(Messages.getString("DDLDiffEditor.1"))) { //$NON-NLS-1$
						file.delete(true, monitor);
					} else {
						setDirty(true);
						return;
					}


				}
				file.create(is, true, monitor);

				setDirty(false);
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
			setDirty(true);

		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	boolean dirty = false;

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean value) {
		dirty = value;
		firePropertyChange(PROP_DIRTY);
	}

	public void doSaveAs() {}

	public boolean isSaveAsAllowed() {
		return false;
	}

	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		try {
			setSite(site);
			setInput(editorInput);

			// System.out.println(editorInput.getClass().getName());
			if (editorInput instanceof DDLDiffEditorInput) {
				DDLDiffEditorInput input = (DDLDiffEditorInput) editorInput;
				diffs = input.getDiffs();
				isTableDiff = input.isTableDiff();

				setPartName(input.getName());
				setDirty(true);

			} else if (editorInput instanceof IFileEditorInput) {
				IFile file = ((IFileEditorInput) getEditorInput()).getFile();
				ObjectInputStream in = new ObjectInputStream(file.getContents());
				diffs = (IDDLDiff[]) in.readObject();
				in.close();
				setPartName(file.getName());
				setDirty(false);
				isTableDiff = false;
			}

		} catch (Exception e) {
			DbPlugin.log(e);
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

	public void setFocus() {}

	public TreeViewer getTreeViewer() {
		return treeViewer;
	}

	public DDLDiffViewer getDiffviewer() {
		return diffviewer;
	}

	public SashForm getSash() {
		return sash;
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				getContributor().fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
		treeViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, treeViewer);
	}

	private DDLDiffContributor getContributor() {
		IEditorActionBarContributor contributor = getEditorSite().getActionBarContributor();
		if (contributor instanceof DDLDiffContributor) {
			return (DDLDiffContributor) contributor;
		} else {
			return null;
		}
	}

}
