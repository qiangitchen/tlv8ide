package com.tulin.v8.echarts.ui.wizards.chart;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import com.tulin.v8.core.FileAndString;
import com.tulin.v8.core.XMLFormator;
import com.tulin.v8.echarts.ui.wizards.Messages;

public class ChartModlePage extends WizardPage {
	Text jdmText;
	Text docText;

	String type;
	String modle;

	String modleText;
	String scriptText;

	public ChartModlePage() {
		super("chartmodlepage");
		setTitle(Messages.getString("wizards.echart.title"));
	}

	@Override
	public void createControl(Composite parent) {
		SashForm sashForm = new SashForm(parent, SWT.FILL);

		Group jdm = new Group(sashForm, SWT.FILL);
		jdm.setText("demo");
		jdm.setLayout(new GridLayout());
		jdmText = new Text(jdm, SWT.ABORT | SWT.WRAP | SWT.BORDER);
		jdmText.setEditable(false);
		jdmText.setLayoutData(new GridData(GridData.FILL_BOTH));

		Group dom = new Group(sashForm, SWT.FILL);
		dom.setText("modle");
		dom.setLayout(new GridLayout());
		docText = new Text(dom, SWT.ABORT | SWT.WRAP | SWT.BORDER);
		docText.setLayoutData(new GridData(GridData.FILL_BOTH));

		sashForm.setWeights(new int[] { 1, 1 });

		docText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				modleText = docText.getText();
				// setPageComplete(!isEmpty(docText.getText()));
			}
		});

		// setPageComplete(false);
		setControl(sashForm);
	}

	boolean isEmpty(Object object) {
		return object == null || "".equals(object);
	}

	ChartTypeSelectPage getTypePage() {
		return (ChartTypeSelectPage) getWizard().getPage("charttypeselectpage");
	}

	ChartOptionPage getOptionPage() {
		return (ChartOptionPage) getWizard().getPage("chartoptionpage");
	}

	public String getJdmFileText() {
		Element selectdata = getTypePage().getSelectdata();
		modle = selectdata.attributeValue("id");
		type = selectdata.getParent().attributeValue("label");
		String filepath = "/chartsnewizard/chartmodle/" + type + "/" + modle + ".jdm";
		InputStream input = ChartModlePage.class.getResourceAsStream(filepath);
		return FileAndString.FileToString(input);
	}

	public String getScrtFileText() {
		Element selectdata = getTypePage().getSelectdata();
		modle = selectdata.attributeValue("id");
		type = selectdata.getParent().attributeValue("label");
		String filepath = "/chartsnewizard/chartmodle/" + type + "/" + modle + ".scrt";
		InputStream input = ChartModlePage.class.getResourceAsStream(filepath);
		return FileAndString.FileToString(input);
	}

	void transeDom() {
		String jdmstr = jdmText.getText();
		try {
			Document doc = DocumentHelper.createDocument();
			Element root = doc.addElement("root");
			Element data = root.addElement("data");
			Element sql = data.addElement("sql");
			sql.addAttribute("dbkey", getOptionPage().getDbkey());
			sql.setText(getOptionPage().getSQL());
			if (scriptText != null) {
				Element script = root.addElement("script");
				script.addCDATA(scriptText);
			}
			Element chart = new JSON2Element(jdmstr).parse();
			chart.addAttribute("type", type);
			chart.addAttribute("modle", modle);
			chart.addAttribute("theme", "");
			root.add(chart);
			String domstr = XMLFormator.formatXML(doc);
			domstr = domstr.replace("&quot;", "'");
			docText.setText(domstr);
			modleText = docText.getText();
		} catch (Exception e) {
			setMessage(e.getMessage());
		}
	}

	@Override
	public IWizardPage getNextPage() {
		jdmText.setText(getJdmFileText());
		scriptText = getScrtFileText();
		transeDom();
		return super.getNextPage();
	}

	public String getModleText() {
		return modleText;
	}

}
