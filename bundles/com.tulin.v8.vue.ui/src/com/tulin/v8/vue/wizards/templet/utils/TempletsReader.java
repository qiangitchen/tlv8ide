package com.tulin.v8.vue.wizards.templet.utils;

import java.io.BufferedReader;
import java.io.InputStream;
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

import com.tulin.v8.core.FileAndString;

/**
 * 新建向导模板配置
 */
@SuppressWarnings("rawtypes")
public class TempletsReader {
	public static String pageName = "page.vue";

	public static Element getConfigElement() throws Exception {
		Element root = null;
		InputStream fileiptstream = TempletsReader.class.getResourceAsStream("/templet/model/config.xml");
		BufferedReader Strreader = new BufferedReader(new InputStreamReader(fileiptstream));
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
			Document doc = DocumentHelper.parseText(TempletConfig.config);
			root = doc.getRootElement();
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

	/**
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
	}

	/**
	 * 获取模板文件目录 page.html page.js 为固定文件名
	 */
	public static String readTempletFoldByName(String templet) throws Exception {
		return "/templet/model/" + templet;
	}

	@Deprecated
	public static String readTempletFoldByNameByEle(Element root, String name) {
		String result = null;
		List items = root.elements();
		for (int i = 0; i < items.size(); i++) {
			Element item = (Element) items.get(i);
			if (name.equals(item.attributeValue("label"))) {
				return item.attributeValue("templet");
			} else if (item.elements().size() > 0) {
				result = readTempletFoldByNameByEle(item, name);
				if (result != null) {
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 获取页面模板内容
	 */
	public static String getTempletPageContext(String templet) throws Exception {
		String path = readTempletFoldByName(templet);
		return FileAndString.FileToString(TempletsReader.class.getResourceAsStream(path + "/" + pageName));
	}

	/**
	 * 获取页面模板内容
	 */
	public static String getTempletPageContext(String templet, String pageName) throws Exception {
		String path = readTempletFoldByName(templet);
		return FileAndString.FileToString(TempletsReader.class.getResourceAsStream(path + "/" + pageName));
	}

	/**
	 * 获取JS模板内容
	 */
	public static String getTempletJsContext(String templet, String jsName) throws Exception {
		String path = readTempletFoldByName(templet);
		return FileAndString.FileToString(TempletsReader.class.getResourceAsStream(path + "/" + jsName));
	}
}
