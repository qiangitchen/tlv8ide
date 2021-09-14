package com.tulin.v8.flowdesigner.ui.editors.process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.tulin.v8.core.TuLinPlugin;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ExpressionTreeHelper {
	private final static String FILE_POSTFIX = ".fn.xml";
	private final static String FILE_POSTFIXN = ".fn";

	private String getRealFilePath() {
		return TuLinPlugin.getCurrentProjectWebFolderPath() + "/WEB-INF/fn/";
	}

	public List generateNewByFile(String filePath) {
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

	private List generateFileList(String fileDir) {
		ArrayList fileList = new ArrayList();
		File file = new File(fileDir);
		File[] subFiles = file.listFiles();
		for (File subFile : subFiles) {
			if (subFile.isFile()) {
				if (subFile.getName().endsWith(FILE_POSTFIX)
						|| subFile.getName().endsWith(FILE_POSTFIXN)) {
					fileList.add(fileDir + subFile.getName());
				}
			}
		}
		return fileList;
	}

	public Element getExpressionTree() {
		Document doc = DocumentHelper.createDocument();
		Element docRoot = DocumentHelper.createElement("root");
		String filePathDir = getRealFilePath();
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
}
