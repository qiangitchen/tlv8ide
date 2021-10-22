package com.tulin.v8.core.Dao.SqlMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.tulin.v8.core.config.AppConfig;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ReadmapFileList {
	private List fileList;
	private static String extrealPath;

	private static final String FILE_POSTFIX = ".mybatis.xml";

	public void setFileList(List fileList) {
		this.fileList = fileList;
	}

	public List<String> getFileList() {
		return fileList;
	}

	public ReadmapFileList() {
		String filePathDir = AppConfig.getClassesPath();
		List list = generateFileList(filePathDir);
		setExtrealPath(filePathDir);
		setFileList(list);
	}

	private List<String> generateFileList(String fileDir) {
		ArrayList fileList = new ArrayList();
		File file = new File(fileDir);
		File[] subFiles = file.listFiles();
		for (File subFile : subFiles) {
			if (subFile.isFile()) {
				if (subFile.getName().endsWith(FILE_POSTFIX)) {
					fileList.add(subFile.getAbsolutePath());
				}
			}
		}
		return fileList;
	}

	public void setExtrealPath(String extrealPath) {
		ReadmapFileList.extrealPath = extrealPath;
	}

	public static String getExtrealPath() {
		return extrealPath;
	}
}
