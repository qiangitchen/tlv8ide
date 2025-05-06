package com.tulin.v8.flowdesigner.ui.editors.process;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.tulin.v8.core.TuLinPlugin;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FuncTreeController {
	private static final String FILE_POSTFIX = ".fun.xml";
	private static final String FILE_POSTFIXF = ".fun";

	public static String getFunctionTreeFilePath(String filePathDir) {
		/** 文件目录 */
		filePathDir = TuLinPlugin.getCurrentProjectWebFolderPath() + "/" + filePathDir;
		return filePathDir;
	}

	public Element index(String filePathDir) throws IOException, DocumentException {
		Document doc = DocumentHelper.createDocument();
		Element docRoot = DocumentHelper.createElement("root");
		filePathDir = getFunctionTreeFilePath(filePathDir);
		if (!"".equals(filePathDir)) {
			List fileElementList = new ArrayList();
			List filePathList = generateFileList(filePathDir);
			for (int i = 0; i < filePathList.size(); i++) {
				String filePath = (String) filePathList.get(i);
				List tempList = generateNewByFile(filePath);
				fileElementList.addAll(tempList);
			}
			docRoot.clearContent();
			docRoot.setContent(fileElementList);

		}
		doc.add(docRoot);

		Element root = doc.getRootElement();
		return root;
	}

	/**
	 * 创建临时菜单集合
	 * 
	 * @param filePath 菜单文件
	 * @return
	 */
	public List generateNewByFile(String filePath) {
		// System.out.println(filePath);
		ArrayList elementList = new ArrayList();
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(filePath);
			Element root = doc.getRootElement();
			elementList = (ArrayList) root.elements();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return elementList;
	}

	/**
	 * 菜单文件路径列表
	 * 
	 * @param fileDir
	 * @return
	 */
	protected List generateFileList(String fileDir) {
		ArrayList fileList = new ArrayList();
		File file = new File(fileDir);
		File[] subFiles = file.listFiles();
		for (File subFile : subFiles) {
			if (subFile.isFile()) {
				if (subFile.getName().endsWith(FILE_POSTFIX) || subFile.getName().endsWith(FILE_POSTFIXF)) {
					fileList.add(subFile.getAbsolutePath());
				}
			}
		}
		return fileList;
	}

}
