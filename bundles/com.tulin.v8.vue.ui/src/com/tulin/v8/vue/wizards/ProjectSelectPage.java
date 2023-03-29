package com.tulin.v8.vue.wizards;

import org.dom4j.Element;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.tulin.v8.vue.Activator;
import com.tulin.v8.vue.wizards.templet.utils.TempletsReader;

public class ProjectSelectPage extends WizardPage {
	private String projectType;
	private String templet;
	private Tree tree = null;

	public ProjectSelectPage() {
		super("Project Selection");
		setTitle(Messages.getString("wizards.project.message.title"));
		setDescription(Messages.getString("wizards.project.message.titleDescription"));
	}

	public void createControl(Composite parent) {
		SashForm sashForm = new SashForm(parent, SWT.NONE);
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 3);
		formData.right = new FormAttachment(100, -3);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, -25);
		sashForm.setLayoutData(formData);

		tree = new Tree(sashForm, SWT.BORDER | SWT.V_SCROLL);

		try {
			TempletsReader.getProjectItem(tree);
		} catch (Exception e1) {
			setMessage(Messages.getString("wizards.project.message.loadmodelErr"));
			e1.printStackTrace();
		}

		// TreeItem root5 = new TreeItem(tree, SWT.NONE);
		// root5.setText("其他");
		// root5.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
		// TreeItem emppage = new TreeItem(root5, SWT.NONE);
		// emppage.setText("空白页面");
		// emppage.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));

		final Label label = new Label(sashForm, SWT.BORDER | SWT.V_SCROLL);
		label.setText("... ...");

		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				if (item.getItems().length == 0) {
					setMessage("Create " + item.getText());
					projectType = item.getText();
					templet = ((Element) item.getData()).attributeValue("templet");
					setLayImage(item, label);
					setPageComplete(true);
				} else {
					setPageComplete(false);
					setMessage(Messages.getString("wizards.project.message.mustselecttype"));
				}
			}
		});

		sashForm.setWeights(new int[] { 1, 3 });
		setControl(sashForm);
	}

	private void setLayImage(TreeItem item, Label label) {
		String imagePath = "/templet/image/emptyPage.png";
		if (item.getData() != null) {
			Element el = (Element) item.getData();
			if (!"".equals(el.attributeValue("img")) && el.attributeValue("img") != null) {
				imagePath = "/templet/" + el.attributeValue("img");
			}
		}
		label.setText("");
		label.setImage(Activator.getDefault().getImage(imagePath));
	}

	@Override
	public IWizardPage getNextPage() {
		if (projectType == null)
			return super.getNextPage();
		if ("BlankPage".equals(templet)) {// projectType.equals("空白页面")
			return getWizard().getPage("SampleNewPage");
		} else if ("directGrid".equals(templet)) {// projectType.equals("主从列表")
			return getWizard().getPage("directGridPermision");
		} else if ("listdetailTag".equals(templet) || "listDetailportal".equals(templet)) {// projectType.equals("列表详细(tab)")||projectType.equals("列表详细(open)")
			return getWizard().getPage("listDetailPermision");
		} else if ("simpleDirct".equals(templet)) {// projectType.equals("主从详细")
			return getWizard().getPage("directDetailPermision");
		} else if ("directFlow".equals(templet)) {// projectType.equals("主从流程")
			return getWizard().getPage("directFlowPermision");
		} else if ("treeTemplet".equals(templet)) {// projectType.equals("简单树形")
			return getWizard().getPage("treedataPermision");
		} else if ("LefttreeTemplet".equals(templet)) {// projectType.equals("左边树形")
			return getWizard().getPage("lefttreedataPermision");
		} else if ("treeGridTemplet".equals(templet)) {// projectType.equals("树形列表")
			return getWizard().getPage("treeGriddataPermision");
		} else if ("gridSelectDialogTemplet".equals(templet)) {// projectType.equals("列表单选对话框")
			return getWizard().getPage("selectListPermision");
		} else if ("gridCheckDialogTemplet".equals(templet)) {// projectType.equals("列表多选对话框")
			return getWizard().getPage("checkListPermision");
		} else if ("mobileTree".equals(templet)) {// projectType.equals("手机树形")
			return getWizard().getPage("mobiletreedataPermision");
		} else if ("mobileList".equals(templet)) {// projectType.equals("手机版列表")
			return getWizard().getPage("mobileListdataPermision");
		} else {
			return getWizard().getPage("dataSelectPage");
		}
	}

	public String getPojectType() {
		return projectType;
	}

	public String getTemplet() {
		return templet;
	}

}
