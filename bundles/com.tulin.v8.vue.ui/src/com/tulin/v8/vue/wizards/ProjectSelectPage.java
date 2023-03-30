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
	private Label imageLabel = null;

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

		imageLabel = new Label(sashForm, SWT.BORDER | SWT.V_SCROLL);
//		imageLabel.setText("... ...");
		imageLabel.setImage(Activator.getDefault().getImage("/templet/image/emptyPage.png"));

		sashForm.setWeights(new int[] { 1, 3 });
		setControl(sashForm);

		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				if (item.getItems().length == 0) {
					setMessage("Create " + item.getText());
					projectType = item.getText();
					templet = ((Element) item.getData()).attributeValue("templet");
					setLayImage(item);
					setPageComplete(true);
				} else {
					setPageComplete(false);
					setMessage(Messages.getString("wizards.project.message.mustselecttype"));
				}
			}
		});
	}

	private void setLayImage(TreeItem item) {
		String imagePath = "/templet/image/emptyPage.png";
		if (item.getData() != null) {
			Element el = (Element) item.getData();
			if (!"".equals(el.attributeValue("img")) && el.attributeValue("img") != null) {
				imagePath = "/templet/" + el.attributeValue("img");
			}
		}
		imageLabel.setText("");
		imageLabel.setImage(Activator.getDefault().getImage(imagePath));
		imageLabel.setVisible(true);
	}

	@Override
	public IWizardPage getNextPage() {
		if (projectType == null)
			return super.getNextPage();
		if ("BlankPage".equals(templet)) {
			return getWizard().getPage("SampleNewPage");
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
