package com.tulin.v8.ide.wizards.chart;

import java.util.List;
import java.util.Locale;

import org.dom4j.Element;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.tulin.v8.ide.utils.StudioConfig;
import com.tulin.v8.ide.wizards.Messages;

public class ChartTypeSelectPage extends WizardPage {
	ChartTypeItem selecteditem;
	Element selectdata;

	public ChartTypeSelectPage() {
		super("charttypeselectpage");
		setTitle(Messages.getString("wizards.echart.title"));
		setDescription(Messages.getString("wizards.echart.titleDescription"));
	}

	@Override
	public void createControl(Composite parent) {
		TabFolder tabFolder = new TabFolder(parent, SWT.LEFT);
		try {
			Element root = ConfigData.getConfigElement();
			@SuppressWarnings("rawtypes")
			List items = root.elements();
			for (int i = 0; i < items.size(); i++) {
				Element item = (Element) items.get(i);
				TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
				if ("zh_CN".equals(Locale.getDefault().toString())) {
					tabItem.setText(item.attributeValue("tiptext"));
				} else {
					tabItem.setText(item.attributeValue("label"));
				}
				tabItem.setToolTipText(item.attributeValue("label"));
				ScrolledComposite sc1 = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
				final Composite c1 = new Composite(sc1, SWT.FILL);
				// TODO 此处在 c1 中添加组件
				c1.setLayout(new GridLayout(4, true));
//				c1.setLayout(new RowLayout(SWT.WRAP));
				for (int m = 0; m < item.elements().size(); m++) {
					ChartTypeItem chart = new ChartTypeItem(c1, SWT.CENTER | SWT.RADIO);
					GridData chtlay = new GridData();
					chtlay.widthHint = 360;
					chart.setLayoutData(chtlay);
//					RowData rowlayoutd = new RowData();
//					chart.setLayoutData(rowlayoutd);
					String imagePath = StudioConfig.getTempletPath() + "/image/emptyPage.png";
					Element el = (Element) item.elements().get(m);
					if (!"".equals(el.attributeValue("icon")) && el.attributeValue("icon") != null) {
						imagePath = StudioConfig.getStudioAppRootPath() + "/chartsnewizard/"
								+ el.attributeValue("icon");
					}
					chart.setData(el);
					chart.setText(el.attributeValue("label"));
					chart.setImage(new Image(null, imagePath));
					new ChartTypeSelectListener(chart) {

						@Override
						public void selected(ChartTypeItem item) {
							if (selecteditem != null) {
								selecteditem.setSelected(false);
							}
							//System.out.println("选中：" + item.getText());
							setMessage(item.getText());
							selectdata = (Element) item.getData();
							selecteditem = item;
							setPageComplete(true);
						}
					};
				}
				// TODO 设置内容面板的大小，当滚动面板无法全部展现内容面板时，将会自动显示滚动条
				c1.setSize(c1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				sc1.setContent(c1);
				tabItem.setControl(sc1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		setPageComplete(false);
		setControl(tabFolder);
	}

	@Override
	public IWizardPage getNextPage() {
		if (selectdata == null) {
			return super.getNextPage();
		} else {
			return getWizard().getPage("chartoptionpage");
		}
	}
	
	public Element getSelectdata() {
		return selectdata;
	}

}
