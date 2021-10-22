package com.tulin.v8.ide.views.navigator.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import com.tulin.v8.core.DBUtils;
import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.views.navigator.ModelView;
import com.tulin.v8.ide.wizards.table.CreateTableWizard;

import zigen.plugin.db.ui.internal.TreeNode;

public class NewTableAction extends Action implements Runnable {

	ModelView view;
	TreeViewer viewer;

	public NewTableAction(ModelView view, TreeViewer viewer) {
		this.view = view;
		this.viewer = viewer;
		this.setText(Messages.getString("View.Action.NewTable.1"));
		this.setToolTipText(Messages.getString("View.Action.NewTable.2"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("add.gif")));
	}

	public void run() {
		// 新建表
		Object obj = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
		TreeNode trp = (TreeNode) obj;
		if ("dbtype".equals(trp.getTvtype()) && "TABLE".equals(trp.getName())) {
			Shell shell = StudioPlugin.getShell();
			CreateTableWizard wizard = new CreateTableWizard(trp.getDbkey(), DBUtils.getUserName(trp.getDbkey()), view);
			WizardDialog dialog = new WizardDialog(shell, wizard);
			int ret = dialog.open();
			if (ret == IDialogConstants.OK_ID) {
				// view.showMessage("建表成功!");
				RefreshAction refresher = new RefreshAction(viewer);
				refresher.setShowDialog(false);
				refresher.run();
			}
		}
	}

}
