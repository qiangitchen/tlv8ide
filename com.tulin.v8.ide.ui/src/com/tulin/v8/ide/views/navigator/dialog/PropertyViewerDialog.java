package com.tulin.v8.ide.views.navigator.dialog;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.util.Date;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.tulin.v8.ide.utils.StudioConfig;
import com.tulin.v8.ide.views.navigator.action.Messages;

import zigen.plugin.db.ui.internal.TreeNode;

public class PropertyViewerDialog extends Dialog {

	private TreeNode treenode;

	public PropertyViewerDialog(Shell parentShell, TreeNode node) {
		super(parentShell);
		this.treenode = node;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(treenode.getName() + Messages.getString("View.Action.PropertyViewer.2"));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());

		Composite composite = new Composite(container, SWT.FILL);
		GridLayout layout = new GridLayout(1, false);
		composite.setLayout(layout);

		final SashForm sashForm = new SashForm(composite, SWT.NONE);
		sashForm.setLayout(new FillLayout());
		final GridData sashFormlay = new GridData(GridData.FILL_BOTH);
		sashFormlay.grabExcessHorizontalSpace = true;
		sashFormlay.grabExcessVerticalSpace = true;
		sashFormlay.widthHint = 600;
		sashFormlay.heightHint = 400;
		sashForm.setLayoutData(sashFormlay);

		final Tree tree = new Tree(sashForm, SWT.V_SCROLL | SWT.FILL | SWT.BORDER);
		TreeItem root = new TreeItem(tree, SWT.NONE);
		root.setText(Messages.getString("View.Action.PropertyViewer.3"));
		tree.setSelection(root);

		final Composite child = new Composite(sashForm, SWT.NONE);
		GridLayout rlayout = new GridLayout(2, false);
		child.setLayout(rlayout);
		Label namelabel = new Label(child, SWT.WRAP);
		namelabel.setText(Messages.getString("View.Action.PropertyViewer.3"));
		FontData newFontData = namelabel.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		Font newFont = new Font(getShell().getDisplay(), newFontData);
		namelabel.setFont(newFont);
		final GridData namelabellay = new GridData();
		namelabellay.grabExcessHorizontalSpace = true;
		namelabellay.horizontalSpan = 2;
		namelabel.setLayoutData(namelabellay);

		final Label hrrtop = new Label(child, SWT.SEPARATOR | SWT.HORIZONTAL);
		final GridData hrrtoplay = new GridData(GridData.FILL_HORIZONTAL);
		hrrtoplay.grabExcessHorizontalSpace = true;
		hrrtoplay.horizontalSpan = 2;
		hrrtop.setLayoutData(hrrtoplay);

		Label pathlabel = new Label(child, SWT.BOLD);
		pathlabel.setText(Messages.getString("View.Action.PropertyViewer.4"));
		Text pathtext = new Text(child, SWT.MULTI | SWT.WRAP | SWT.LEFT);
		pathtext.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if (treenode.getPath() != null) {
			File file = new File(treenode.getPath());
			String conpath;
			try {
				conpath = file.toURL().toString();
				conpath = conpath.substring(conpath.indexOf("Workspaces") + 10);
				conpath = conpath.replace("/" + StudioConfig.PROJECT_WEB_FOLDER + "/", "/");
				pathtext.setText(conpath);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
		} else {
			String dpath = treenode.getName();
			TreeNode node = treenode.getParent();
			while (!"invisible".equals(node.getName()) && node != null) {
				dpath = node.getName() + "/" + dpath;
				node = node.getParent();
			}
			pathtext.setText(dpath);
		}
		pathtext.pack();
		pathtext.setEditable(false);

		Label typelabel = new Label(child, SWT.BOLD);
		typelabel.setText(Messages.getString("View.Action.PropertyViewer.5"));
		Text typetext = new Text(child, SWT.MULTI | SWT.WRAP | SWT.LEFT);
		typetext.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if ("file".equals(treenode.getTvtype())) {
			typetext.setText(Messages.getString("View.Action.PropertyViewer.6"));
		} else if ("folder".equals(treenode.getTvtype())) {
			typetext.setText(Messages.getString("View.Action.PropertyViewer.6"));
		} else if (treenode.getTvtype() != null) {
			typetext.setText(treenode.getTvtype());
		}
		typetext.setEditable(false);

		Label dirlabel = new Label(child, SWT.BOLD);
		dirlabel.setText(Messages.getString("View.Action.PropertyViewer.8"));
		Text dirtext = new Text(child, SWT.MULTI | SWT.WRAP | SWT.LEFT);
		dirtext.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if (treenode.getPath() != null) {
			dirtext.setText(treenode.getPath());
		} else {
			dirtext.setText(treenode.getName());
		}
		dirtext.pack();
		dirtext.setEditable(false);

		if ("file".equals(treenode.getTvtype()) || "folder".equals(treenode.getTvtype())) {
			File file = new File(treenode.getPath());
			if ("file".equals(treenode.getTvtype())) {
				Label sizelabel = new Label(child, SWT.BOLD);
				sizelabel.setText(Messages.getString("View.Action.PropertyViewer.9"));
				Text sizetext = new Text(child, SWT.MULTI | SWT.WRAP | SWT.LEFT);
				sizetext.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				FileInputStream fis;
				try {
					fis = new FileInputStream(file);
					sizetext.setText(
							String.valueOf(fis.available()) + Messages.getString("View.Action.PropertyViewer.11"));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				sizetext.setEditable(false);
			}
			Label lastlabel = new Label(child, SWT.BOLD);
			lastlabel.setText(Messages.getString("View.Action.PropertyViewer.10"));
			Text lasttext = new Text(child, SWT.MULTI | SWT.WRAP | SWT.LEFT);
			lasttext.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			lasttext.setText(String.valueOf(new Date(file.lastModified()).toLocaleString()));
			lasttext.setEditable(false);
		}

		final Label hrmid = new Label(child, SWT.SEPARATOR | SWT.HORIZONTAL);
		final GridData hrmidlay = new GridData(GridData.FILL_HORIZONTAL);
		hrmidlay.grabExcessHorizontalSpace = true;
		hrmidlay.horizontalSpan = 2;
		hrmid.setLayoutData(hrmidlay);

		if ("file".equals(treenode.getTvtype()) || "folder".equals(treenode.getTvtype())) {
			Label typicl = new Label(child, SWT.NONE);
			GridData typicllay = new GridData(GridData.FILL_HORIZONTAL);
			typicllay.grabExcessHorizontalSpace = true;
			typicllay.horizontalSpan = 2;
			// typicllay.widthHint = 500;
			typicl.setLayoutData(typicllay);
			typicl.setText(Messages.getString("View.Action.PropertyViewer.12"));
			Button read = new Button(child, SWT.CHECK);
			read.setText(Messages.getString("View.Action.PropertyViewer.13"));
			read.setLayoutData(typicllay);
			Button wread = new Button(child, SWT.CHECK);
			wread.setText(Messages.getString("View.Action.PropertyViewer.14"));
			wread.setLayoutData(typicllay);
			Button vread = new Button(child, SWT.CHECK);
			vread.setText(Messages.getString("View.Action.PropertyViewer.15"));
			vread.setLayoutData(typicllay);
			if ("file".equals(treenode.getTvtype())) {
				File file = new File(treenode.getPath());
				if (file.canWrite()) {
					wread.setSelection(true);
				} else {
					read.setSelection(true);
				}
			}
		}

		Group gp = new Group(child, GridData.FILL_HORIZONTAL);
		gp.setText(Messages.getString("View.Action.PropertyViewer.16"));
		gp.setLayout(new GridLayout(1, false));
		final GridData gplay = new GridData(GridData.FILL_HORIZONTAL);
		gplay.grabExcessHorizontalSpace = true;
		gplay.horizontalSpan = 2;
		gp.setLayoutData(gplay);
		Button encode = new Button(gp, SWT.RADIO);
		encode.setText(
				Messages.getString("View.Action.PropertyViewer.17") + System.getProperty("file.encoding") + ")(I)");
		encode.setSelection(true);
		encode.setEnabled(false);
		Button otherencode = new Button(gp, SWT.RADIO);
		otherencode.setText(Messages.getString("View.Action.PropertyViewer.18"));
		otherencode.setEnabled(false);

		sashForm.setWeights(new int[] { 2, 5 });

		// 水平分割线
		final Label hrbottom = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		final GridData hrlay = new GridData(GridData.FILL_HORIZONTAL);
		hrlay.grabExcessHorizontalSpace = true;
		hrlay.heightHint = 2;
		hrbottom.setLayoutData(hrlay);

		return composite;
	}

	protected int getShellStyle() {
		return super.getShellStyle() | SWT.RESIZE | SWT.MAX;
	}

}
