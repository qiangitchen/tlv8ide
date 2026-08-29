package com.tulin.v8.webtools.ide.formatter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

public class FileUtils {
	public static String FileToString(String path) {
		String fileStr = "";
		try {
			InputStream fileiptstream = FileUtils.class.getResourceAsStream(path);
			BufferedReader Strreader = new BufferedReader(new InputStreamReader(fileiptstream, "UTF-8"));
			StringBuffer fileText = new StringBuffer();
			while ((fileStr = Strreader.readLine()) != null) {
				fileText.append(fileStr + "\n");
			}
			fileiptstream.close();
			fileStr = fileText.toString().trim();
		} catch (Exception e1) {
			WebToolsPlugin.logException(e1);
		}
		return fileStr;
	}
}
