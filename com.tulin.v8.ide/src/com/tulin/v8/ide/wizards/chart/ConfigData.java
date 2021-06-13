package com.tulin.v8.ide.wizards.chart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.tulin.v8.ide.utils.StudioConfig;

@SuppressWarnings("rawtypes")
public class ConfigData {

	public static Element getConfigElement() throws Exception {
		String templetConfigFile = StudioConfig.getStudioAppRootPath() + "/chartsnewizard/charts.xml";
		Element root = null;
		File file = new File(templetConfigFile);
		FileInputStream fileiptstream = new FileInputStream(file);
		BufferedReader Strreader = new BufferedReader(new InputStreamReader(fileiptstream, "UTF-8"));
		StringBuffer fileText = new StringBuffer();
		String fileStr = "";
		while ((fileStr = Strreader.readLine()) != null) {
			fileText.append(fileStr);
		}
		fileiptstream.close();
		fileStr = fileText.toString().trim();
		try {
			Document doc = DocumentHelper.parseText(fileStr);
			root = doc.getRootElement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return root;
	}

	private static void readChildItem(TreeItem tree, Element root) {
		List items = root.elements();
		for (int i = 0; i < items.size(); i++) {
			Element item = (Element) items.get(i);
			TreeItem root1 = new TreeItem(tree, SWT.NONE);
			root1.setText(item.attributeValue("label"));
			root1.setData(item);
			if (item.elements().size() > 0) {
				root1.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
				readChildItem(root1, item);
			} else {
				root1.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
			}
		}
	}

	/*
	 * 加载配置的模板信息
	 */
	public static void getProjectItem(Tree tree) throws Exception {
		Element root = getConfigElement();
		List items = root.elements();
		for (int i = 0; i < items.size(); i++) {
			Element item = (Element) items.get(i);
			TreeItem root1 = new TreeItem(tree, SWT.NONE);
			root1.setText(item.attributeValue("label"));
			root1.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
			root1.setData(item);
			readChildItem(root1, item);
		}
		tree.setData("element", root);
	}
}
