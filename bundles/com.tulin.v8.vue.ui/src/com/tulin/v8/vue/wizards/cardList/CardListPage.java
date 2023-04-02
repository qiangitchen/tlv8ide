package com.tulin.v8.vue.wizards.cardList;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.tulin.v8.ide.wizards.utils.TablecellSelectDialog;
import com.tulin.v8.vue.wizards.DataSelectPage;
import com.tulin.v8.vue.wizards.Messages;

public class CardListPage extends WizardPage {
	private DataSelectPage dataSelectPage;
	private String dbkey = null;
	private String tvName = null;
	private String keyField = null;

	private String title;
	private String description;
	private String previewImage;
	private String smallIcon;
	private String dataOrder = "";

	List<String[]> columnlist = new ArrayList<>();

	public CardListPage(DataSelectPage Page) {
		super("cardListPage");
		setTitle("卡片列表");
		setDescription("配置列表详细参数.");
		dataSelectPage = Page;
		dbkey = dataSelectPage.getDbkey();
		tvName = dataSelectPage.getTvName();
		keyField = dataSelectPage.getKeyField();
	}

	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.FILL);
		composite.setLayout(new GridLayout(3, false));

		Label label1 = new Label(composite, SWT.NONE);
		label1.setText("Title(*)");
		GridData gridl1 = new GridData();
		gridl1.widthHint = 120;
		label1.setLayoutData(gridl1);
		final Text text1 = new Text(composite, SWT.BORDER | SWT.FILL);
		text1.setEditable(false);
		GridData gridt1 = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gridt1.grabExcessHorizontalSpace = true;
		text1.setLayoutData(gridt1);
		Button button1 = new Button(composite, SWT.NONE);
		button1.setText("...");
		GridData gridb1 = new GridData();
		button1.setLayoutData(gridb1);

		button1.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				TablecellSelectDialog dialog = new TablecellSelectDialog(getShell(), dbkey, tvName);
				int result = dialog.open();
				if (IDialogConstants.OK_ID == result) {
					text1.setText(dialog.getItemsToOpen());
					title = text1.getText();
				}
			}
		});

		Label label2 = new Label(composite, SWT.NONE);
		label2.setText("Description(*)");
		label2.setLayoutData(gridl1);
		final Text text2 = new Text(composite, SWT.BORDER | SWT.FILL);
		text2.setEditable(false);
		text2.setLayoutData(gridt1);
		Button button2 = new Button(composite, SWT.NONE);
		button2.setText("...");
		button2.setLayoutData(gridb1);

		button2.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				TablecellSelectDialog dialog = new TablecellSelectDialog(getShell(), dbkey, tvName);
				int result = dialog.open();
				if (IDialogConstants.OK_ID == result) {
					text2.setText(dialog.getItemsToOpen());
					description = text2.getText();
				}
			}
		});

		Label label3 = new Label(composite, SWT.NONE);
		label3.setText("PreviewImage(*)");
		label3.setLayoutData(gridl1);
		final Text text3 = new Text(composite, SWT.BORDER | SWT.FILL);
		text3.setEditable(false);
		text3.setLayoutData(gridt1);
		Button button3 = new Button(composite, SWT.NONE);
		button3.setText("...");
		button3.setLayoutData(gridb1);

		button3.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				TablecellSelectDialog dialog = new TablecellSelectDialog(getShell(), dbkey, tvName);
				int result = dialog.open();
				if (IDialogConstants.OK_ID == result) {
					text3.setText(dialog.getItemsToOpen());
					previewImage = text3.getText();
				}
			}
		});

		Label label4 = new Label(composite, SWT.NONE);
		label4.setText("SmallIcon(*)");
		label4.setLayoutData(gridl1);
		final Text text4 = new Text(composite, SWT.BORDER | SWT.FILL);
		text4.setEditable(false);
		text4.setLayoutData(gridt1);
		Button button4 = new Button(composite, SWT.NONE);
		button4.setText("...");
		button4.setLayoutData(gridb1);

		button4.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				TablecellSelectDialog dialog = new TablecellSelectDialog(getShell(), dbkey, tvName);
				int result = dialog.open();
				if (IDialogConstants.OK_ID == result) {
					text4.setText(dialog.getItemsToOpen());
					smallIcon = text4.getText();
				}
			}
		});

		Label label5 = new Label(composite, SWT.NONE);
		label5.setText("dataOrder");
		label5.setLayoutData(gridl1);
		final Text text5 = new Text(composite, SWT.BORDER | SWT.FILL);
		text5.setEditable(true);
		text5.setLayoutData(gridt1);
		Button button5 = new Button(composite, SWT.NONE);
		button5.setText("...");
		button5.setLayoutData(gridb1);

		button5.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				TablecellSelectDialog dialog = new TablecellSelectDialog(getShell(), dbkey, tvName);
				int result = dialog.open();
				if (IDialogConstants.OK_ID == result) {
					text5.setText(dialog.getItemsToOpen());
					dataOrder = text5.getText();
				}
			}
		});

		setControl(composite);
		setPageComplete(true);
	}

	@Override
	public IWizardPage getNextPage() {
		dbkey = dataSelectPage.getDbkey();
		tvName = dataSelectPage.getTvName();
		keyField = dataSelectPage.getKeyField();

		setMessage(Messages.getString("wizardsaction.dataselect.message.delectedDatasource") + dbkey
				+ Messages.getString("wizardsaction.dataselect.message.delectedTable") + tvName + ", keyField:"
				+ keyField + ".");

		return getWizard().getPage("cardListEnd");
	}

	@Override
	public boolean canFlipToNextPage() {
		if (title == null || description == null || previewImage == null || smallIcon == null) {
			return false;
		}
		return super.canFlipToNextPage();
	}

	public String getDbkey() {
		return dbkey;
	}

	public void setDbkey(String dbkey) {
		this.dbkey = dbkey;
	}

	public String getTvName() {
		return tvName;
	}

	public void setTvName(String tvName) {
		this.tvName = tvName;
	}

	public String getKeyField() {
		return keyField;
	}

	public void setKeyField(String keyField) {
		this.keyField = keyField;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSmallIcon() {
		return smallIcon;
	}

	public void setSmallIcon(String smallIcon) {
		this.smallIcon = smallIcon;
	}

	public String getDataOrder() {
		return dataOrder;
	}

	public void setDataOrder(String dataOrder) {
		this.dataOrder = dataOrder;
	}

	public String getPreviewImage() {
		return previewImage;
	}

	public void setPreviewImage(String previewImage) {
		this.previewImage = previewImage;
	}

}
