package com.tulin.v8.ide.ui.editors.fn;

import org.dom4j.Element;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

class AddFnDialog extends Dialog {
	FnEditor editorpart;
	ModelManage manage;
	Tree tree;
	Element element;
	private Text text;
	private Text nametext;
	private Text codetext;
	private Text helptext;

	public AddFnDialog(Shell parentShell, FnEditor editorpart, ModelManage manage, Tree tree, Element element) {
		super(parentShell);
		this.editorpart = editorpart;
		this.manage = manage;
		this.tree = tree;
		this.element = element;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.getString("editors.FnEditor.AddFnDialog.1"));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());

		Group group = new Group(container, SWT.FILL);
		group.setText(Messages.getString("editors.FnEditor.AddFnDialog.2"));
		group.setLayout(new GridLayout(2, false));

		Label label = new Label(group, SWT.NONE);
		label.setText(Messages.getString("editors.FnEditor.10"));
		text = new Text(group, SWT.BORDER);
		GridData textlay = new GridData(GridData.FILL_HORIZONTAL);
		textlay.grabExcessHorizontalSpace = true;
		// textlay.widthHint = 300;
		text.setLayoutData(textlay);

		Label namelabel = new Label(group, SWT.NONE);
		namelabel.setText(Messages.getString("editors.FnEditor.11"));
		nametext = new Text(group, SWT.BORDER);
		nametext.setLayoutData(textlay);

		Label codelabel = new Label(group, SWT.NONE);
		codelabel.setText(Messages.getString("editors.FnEditor.12"));
		codetext = new Text(group, SWT.BORDER);
		codetext.setLayoutData(textlay);

		final Label helplabel = new Label(group, SWT.NONE);
		helplabel.setText(Messages.getString("editors.FnEditor.13"));
		helptext = new Text(group, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.LEFT);
		GridData helplay = new GridData(GridData.FILL_BOTH);
		// helplay.grabExcessVerticalSpace = true;
		// helplay.verticalSpan = 3;
		// helplay.heightHint = 110;
		helptext.setLayoutData(helplay);

		return group;
	}

	protected int getShellStyle() {
		return super.getShellStyle() | SWT.RESIZE;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(550, 450);
	}

	private boolean addFn(String id, String name, String code, String help) {
		if (id == null || "".equals(id)) {
			MessageDialog.openInformation(tree.getParent().getShell(),
					Messages.getString("editors.FnEditor.AddFnDialog.5"),
					Messages.getString("editors.FnEditor.AddFnDialog.6"));
			return false;
		}
		if (name == null || "".equals(name)) {
			MessageDialog.openInformation(tree.getParent().getShell(),
					Messages.getString("editors.FnEditor.AddFnDialog.5"),
					Messages.getString("editors.FnEditor.AddFnDialog.7"));
			return false;
		}
		Element newFn = element.addElement("function");
		newFn.addAttribute("id", id);
		newFn.addAttribute("name", name);
		newFn.addAttribute("javacode", code);
		newFn.addAttribute("helper", help.replace("\n", "<br/>").replace("\r", "<br/>"));
		newFn.setText("");
		int itemcout = tree.getItemCount();
		TreeItem rootitem = null;
		if (itemcout > 0) {
			rootitem = tree.getItem(0);
		} else {
			rootitem = new TreeItem(tree, SWT.NONE);
			rootitem.setText(element.attributeValue("name"));
			rootitem.setData(element);
		}
		TreeItem item = new TreeItem(rootitem, SWT.NONE);
		item.setText(name);
		item.setData(newFn);
		item.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
		tree.setSelection(item);
		editorpart.clearData();
		editorpart.currentTreeItem = item;
		editorpart.setData();
		return true;
	}

	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			if (addFn(text.getText(), nametext.getText(), codetext.getText(), helptext.getText())) {
				manage.doing = true;
			} else {
				return;
			}
		}
		super.buttonPressed(buttonId);
	}
}
