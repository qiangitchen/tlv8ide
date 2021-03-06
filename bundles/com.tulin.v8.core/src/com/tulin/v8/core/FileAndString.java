package com.tulin.v8.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

public class FileAndString {
	public static String FileToString(InputStream input) {
		String fileStr = "";
		try {
			BufferedReader Strreader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
			StringBuffer fileText = new StringBuffer();
			while ((fileStr = Strreader.readLine()) != null) {
				fileText.append(fileStr + "\n");
			}
			fileStr = fileText.toString().trim();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return fileStr;
	}

	public static String FileToString(File file) {
		String fileStr = "";
		try {
			FileInputStream fileiptstream = new FileInputStream(file);
			BufferedReader Strreader = new BufferedReader(new InputStreamReader(fileiptstream, "UTF-8"));
			StringBuffer fileText = new StringBuffer();
			while ((fileStr = Strreader.readLine()) != null) {
				fileText.append(fileStr + "\n");
			}
			fileiptstream.close();
			fileStr = fileText.toString().trim();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return fileStr;
	}

	public static StringBuffer FileToStringBuffer(File file) {
		StringBuffer fileText = new StringBuffer();
		try {
			FileInputStream fileiptstream = new FileInputStream(file);
			BufferedReader Strreader = new BufferedReader(new InputStreamReader(fileiptstream, "UTF-8"));
			String fileStr = "";
			while ((fileStr = Strreader.readLine()) != null) {
				fileText.append(fileStr + "\n");
			}
			fileiptstream.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return fileText;
	}

	public static String FileToString(String filepath) {
		String fileStr = "";
		try {
			File file = new File(filepath);
			FileInputStream fileiptstream = new FileInputStream(file);
			BufferedReader Strreader = new BufferedReader(new InputStreamReader(fileiptstream, "UTF-8"));
			StringBuffer fileText = new StringBuffer();
			while ((fileStr = Strreader.readLine()) != null) {
				fileText.append(fileStr + "\n");
			}
			fileiptstream.close();
			fileStr = fileText.toString().trim();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return fileStr;
	}

	/**
	 * ?????????????????????????????????????????????
	 * 
	 * @param file     ????????????
	 * @param encoding ????????????
	 * @return ?????????????????????
	 * @throws IOException
	 */
	public static String file2String(File file, String encoding) {
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		InputStreamReader reader = null;
		StringWriter writer = new StringWriter();
		try {
			if (encoding == null || "".equals(encoding.trim())) {
				reader = new InputStreamReader(new FileInputStream(file), encoding);
			} else {
				reader = new InputStreamReader(new FileInputStream(file));
			}
			// ???????????????????????????
			int n = 1024;
			char[] buffer = new char[n];
			while (-1 != (n = reader.read(buffer, 0, n))) {
				writer.write(buffer, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		// ??????????????????
		return writer.toString();
	}

	/**
	 * ??????????????????????????????(???????????????????????????????????????????????????????????????????????????????????????????????????)
	 * 
	 * @param res      ????????????
	 * @param filePath ????????????
	 * @return ????????????
	 */
	public static boolean string2File(String res, String filePath) {
		boolean flag = true;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			File distFile = new File(filePath);
			if (!distFile.getParentFile().exists())
				distFile.getParentFile().mkdirs();
			bufferedReader = new BufferedReader(new StringReader(res));
			bufferedWriter = new BufferedWriter(new FileWriter(distFile));
			char buf[] = new char[1024]; // ???????????????
			int len;
			while ((len = bufferedReader.read(buf)) != -1) {
				bufferedWriter.write(buf, 0, len);
			}
			bufferedWriter.flush();
			bufferedReader.close();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * ??????????????????????????????(???????????????????????????????????????????????????????????????????????????????????????????????????)
	 * 
	 * @param res  ????????????
	 * @param file ??????
	 * @return ????????????
	 */
	public static boolean string2File(String res, File file) {
		boolean flag = true;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			File distFile = file;
			if (!distFile.getParentFile().exists())
				distFile.getParentFile().mkdirs();
			bufferedReader = new BufferedReader(new StringReader(res));
			bufferedWriter = new BufferedWriter(new FileWriter(distFile));
			char buf[] = new char[1024]; // ???????????????
			int len;
			while ((len = bufferedReader.read(buf)) != -1) {
				bufferedWriter.write(buf, 0, len);
			}
			bufferedWriter.flush();
			bufferedReader.close();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
}
