package com.tulin.v8.ide.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FileHelper {
	public static void createFile(IFile paramIFile, String paramString) {
		ByteArrayInputStream localByteArrayInputStream = null;
		try {
			localByteArrayInputStream = new ByteArrayInputStream(
					paramString.getBytes());
			if (paramIFile.exists())
				paramIFile.delete(true, null);
			paramIFile.create(localByteArrayInputStream, true, null);
		} catch (Exception localException) {
			localException.printStackTrace();
		} finally {
			closeStream(localByteArrayInputStream);
		}
	}

	public static String transStreamToStr(InputStream paramInputStream,
			String paramString) {
		try {
			InputStreamReader localInputStreamReader = null;
			if ((paramString != null) && (!paramString.equals("")))
				localInputStreamReader = new InputStreamReader(
						paramInputStream, paramString);
			else
				localInputStreamReader = new InputStreamReader(paramInputStream);
			BufferedReader localBufferedReader = new BufferedReader(
					localInputStreamReader);
			StringBuffer localStringBuffer = new StringBuffer();
			String str;
			while ((str = localBufferedReader.readLine()) != null)
				localStringBuffer.append(str);
			localBufferedReader.close();
			return localStringBuffer.toString().trim();
		} catch (Exception localException) {
			localException.toString();
		}
		return "";
	}

	public static void closeStream(InputStream paramInputStream) {
		if (paramInputStream != null)
			try {
				paramInputStream.close();
			} catch (IOException localIOException) {
				localIOException.printStackTrace();
			}
	}

	public static void closeStream(OutputStream paramOutputStream) {
		if (paramOutputStream != null)
			try {
				paramOutputStream.close();
			} catch (IOException localIOException) {
				localIOException.printStackTrace();
			}
	}

	public static boolean writeFile(String paramString1,
			InputStream paramInputStream, String paramString2) {
		return writeFile(paramString1,
				transStreamToStr(paramInputStream, paramString2), paramString2);
	}

	public static boolean wirteFile(String paramString,
			org.w3c.dom.Document paramDocument) {
		FileOutputStream localFileOutputStream = null;
		try {
			File localFile = new File(paramString);
			if (!localFile.exists()) {
				File localObject1 = localFile.getParentFile();
				if (!((File) localObject1).exists())
					((File) localObject1).mkdirs();
				localFile.createNewFile();
			}
			Object localObject1 = TransformerFactory.newInstance();
			Transformer localTransformer = ((TransformerFactory) localObject1)
					.newTransformer();
			localTransformer.setOutputProperty("encoding", "UTF-8");
			localFileOutputStream = new FileOutputStream(localFile);
			localTransformer.transform(new DOMSource(paramDocument),
					new StreamResult(localFileOutputStream));
		} catch (Exception localException) {
			localException.printStackTrace();
		} finally {
			closeStream(localFileOutputStream);
		}
		return true;
	}

	public static boolean writeFile(String paramString1, String paramString2,
			String paramString3) {
		File localFile = new File(paramString1);
		try {
			if (!localFile.exists())
				localFile.createNewFile();
			FileOutputStream localFileOutputStream = new FileOutputStream(
					paramString1);
			OutputStreamWriter localOutputStreamWriter = null;
			if ((paramString3 != null) && (!paramString3.equals("")))
				localOutputStreamWriter = new OutputStreamWriter(
						localFileOutputStream, paramString3);
			else
				localOutputStreamWriter = new OutputStreamWriter(
						localFileOutputStream);
			localOutputStreamWriter.write(paramString2);
			localOutputStreamWriter.close();
			localFileOutputStream.close();
			return true;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return false;
	}

	public static boolean appendFile(String paramString1, String paramString2,
			String paramString3) {
		File localFile = new File(paramString1);
		if (!localFile.exists())
			return false;
		try {
			FileOutputStream localFileOutputStream = new FileOutputStream(
					paramString1, true);
			OutputStreamWriter localOutputStreamWriter = null;
			if ((paramString3 != null) && (!paramString3.equals("")))
				localOutputStreamWriter = new OutputStreamWriter(
						localFileOutputStream, paramString3);
			else
				localOutputStreamWriter = new OutputStreamWriter(
						localFileOutputStream);
			localOutputStreamWriter.write(paramString2);
			localOutputStreamWriter.close();
			localFileOutputStream.close();
			return true;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return false;
	}

	public static boolean saveFile(String paramString1, String paramString2) {
		FileWriter localFileWriter = null;
		try {
			File localFile = new File(paramString1);
			if (!localFile.exists())
				localFile.createNewFile();
			localFileWriter = new FileWriter(paramString1);
			localFileWriter.write(paramString2);
			localFileWriter.flush();
			localFileWriter.close();
			return true;
		} catch (Exception localException) {
			localException.printStackTrace();
			if (localFileWriter != null)
				try {
					localFileWriter.close();
				} catch (IOException localIOException) {
					localIOException.printStackTrace();
				}
		}
		return false;
	}

	public static boolean saveFile(String paramString1, String paramString2,
			String paramString3) {
		FileOutputStream localFileOutputStream = null;
		OutputStreamWriter localOutputStreamWriter = null;
		try {
			File localFile = new File(paramString1);
			if (!localFile.exists())
				localFile.createNewFile();
			localFileOutputStream = new FileOutputStream(localFile);
			localOutputStreamWriter = new OutputStreamWriter(
					localFileOutputStream, paramString3);
			localOutputStreamWriter.write(paramString2);
			localOutputStreamWriter.close();
			localFileOutputStream.close();
		} catch (Exception localException1) {
			localException1.printStackTrace();
			try {
				if (localFileOutputStream != null)
					localFileOutputStream.close();
				if (localOutputStreamWriter != null)
					localOutputStreamWriter.close();
			} catch (Exception localException2) {
				localException2.printStackTrace();
			}
			return false;
		}
		return true;
	}

	public static org.dom4j.Document getDocumentByPath(IFile paramIFile) throws Exception {
		if (paramIFile == null)
			throw new Exception("file is null");
		String str = paramIFile.getLocation().toString();
		SAXReader localSAXReader = new SAXReader();
		org.dom4j.Document localDocument = null;
		try {
			localDocument = localSAXReader.read(new File(str));
		} catch (DocumentException localDocumentException) {
			localDocumentException.printStackTrace();
		}
		return localDocument;
	}

	public static org.dom4j.Document getDocumentByPath(File paramFile) throws Exception {
		if (paramFile == null)
			throw new Exception("file is null");
		SAXReader localSAXReader = new SAXReader();
		localSAXReader
				.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
						false);
		localSAXReader
				.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-external-dtd",
						false);
		org.dom4j.Document localDocument = null;
		localDocument = localSAXReader.read(paramFile);
		return localDocument;
	}

	public static org.dom4j.Document getDocumentByPath(File paramFile,
			String paramString) throws Exception {
		if (paramFile == null)
			throw new Exception("file is null");
		SAXReader localSAXReader = new SAXReader();
		localSAXReader.setValidation(false);
		localSAXReader
				.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
						false);
		localSAXReader
				.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-external-dtd",
						false);
		localSAXReader.setEncoding(paramString);
		org.dom4j.Document localDocument = null;
		try {
			localDocument = localSAXReader.read(paramFile);
		} catch (DocumentException localDocumentException) {
			localDocumentException.printStackTrace();
		}
		return localDocument;
	}

	public static org.dom4j.Document getDocumentByPath(
			InputStream paramInputStream) throws Exception {
		if (paramInputStream == null)
			throw new Exception("input stream is null");
		SAXReader localSAXReader = new SAXReader();
		localSAXReader.setValidation(false);
		localSAXReader
				.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
						false);
		localSAXReader
				.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-external-dtd",
						false);
		org.dom4j.Document localDocument = null;
		try {
			localDocument = localSAXReader.read(paramInputStream);
		} catch (DocumentException localDocumentException) {
			localDocumentException.printStackTrace();
		}
		return localDocument;
	}

	public static String readFileAsStr(String paramString1,
			String paramString2, boolean paramBoolean) {
		if ((paramString1 == null) || (paramString1.equals("")))
			return "";
		StringBuffer localStringBuffer = new StringBuffer();
		try {
			File localFile = new File(paramString1);
			if (!localFile.exists())
				if (paramBoolean) {
					localFile.getParentFile().mkdirs();
					localFile.createNewFile();
				} else {
					return "";
				}
			FileInputStream localFileInputStream = new FileInputStream(
					localFile);
			BufferedReader localBufferedReader = new BufferedReader(
					new InputStreamReader(localFileInputStream));
			String str;
			while ((str = localBufferedReader.readLine()) != null)
				localStringBuffer.append(str + paramString2);
			localBufferedReader.close();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return localStringBuffer.toString().trim();
	}

	public static String readFileAsStr(String paramString1,
			String paramString2, boolean paramBoolean, String paramString3) {
		if ((paramString1 == null) || (paramString1.equals("")))
			return "";
		StringBuffer localStringBuffer = new StringBuffer();
		try {
			File localFile = new File(paramString1);
			if (!localFile.exists())
				if (paramBoolean)
					localFile.createNewFile();
				else
					return "";
			FileInputStream localFileInputStream = new FileInputStream(
					localFile);
			BufferedReader localBufferedReader = new BufferedReader(
					new InputStreamReader(localFileInputStream, paramString3));
			String str;
			while ((str = localBufferedReader.readLine()) != null)
				localStringBuffer.append(str + paramString2);
			localBufferedReader.close();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return localStringBuffer.toString().trim();
	}

	public static Set readFileAsSet(String paramString, boolean paramBoolean) throws Exception {
		if ((paramString == null) || (paramString.equals("")))
			throw new Exception("文件名不能为空！");
		File localFile = new File(paramString);
		if (!localFile.exists())
			if (paramBoolean)
				localFile.createNewFile();
			else
				throw new Exception("文件：" + paramString + "不存在！");
		FileInputStream localFileInputStream = new FileInputStream(localFile);
		BufferedReader localBufferedReader = new BufferedReader(
				new InputStreamReader(localFileInputStream));
		HashSet localHashSet = new HashSet();
		String str;
		while ((str = localBufferedReader.readLine()) != null) {
			if ("".equals(str.trim()))
				continue;
			localHashSet.add(str.trim());
		}
		localBufferedReader.close();
		return localHashSet;
	}

	public static org.dom4j.Document readFileAsXML(String paramString) throws Exception {
		if ((paramString == null) || (paramString.equals("")))
			throw new Exception("文件名不能为空！");
		File localFile = new File(paramString);
		if (!localFile.exists())
			throw new Exception("文件：" + paramString + "不存在！");
		SAXReader localSAXReader = new SAXReader();
		org.dom4j.Document localDocument = localSAXReader.read(localFile);
		return localDocument;
	}

	public static boolean move(String paramString1, String paramString2) {
		File localFile1 = new File(paramString1);
		File localFile2 = new File(paramString2);
		boolean bool = localFile1.renameTo(new File(localFile2, localFile1
				.getName()));
		return bool;
	}

	public static void copyfile(String paramString1, String paramString2) {
		File localFile = new File(paramString1);
		if ((!localFile.exists()) || (localFile.isDirectory()))
			return;
		copyfile(paramString1, paramString2, localFile.getName());
	}

	public static void copyfile(String paramString1, String paramString2,
			String paramString3) {
		try {
			File localFile1 = new File(paramString1);
			if (!localFile1.exists())
				return;
			File localFile2 = new File(paramString2);
			if (!localFile2.exists())
				localFile2.mkdirs();
			if (localFile1.isDirectory())
				return;
			FileInputStream localFileInputStream = new FileInputStream(
					localFile1);
			FileOutputStream localFileOutputStream = new FileOutputStream(
					paramString2 + "/" + paramString3);
			byte[] arrayOfByte = new byte[localFileInputStream.available()];
			localFileInputStream.read(arrayOfByte);
			localFileOutputStream.write(arrayOfByte);
			localFileOutputStream.flush();
			localFileOutputStream.close();
			localFileInputStream.close();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	public static boolean checkFileExist(String paramString) {
		File localFile = new File(paramString);
		return localFile.exists();
	}

	public static org.dom4j.Document getXmlDocument(String paramString) {
		org.dom4j.Document localDocument = null;
		File localFile = new File(paramString);
		try {
			localDocument = getDocumentByPath(localFile, "UTF-8");
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return localDocument;
	}

	public static int formatXMLFile(String paramString1, String paramString2) {
		int i = 0;
		try {
			SAXReader localSAXReader = new SAXReader();
			org.dom4j.Document localDocument = localSAXReader.read(new File(
					paramString1));
			XMLWriter localXMLWriter = null;
			OutputFormat localOutputFormat = OutputFormat.createPrettyPrint();
			localOutputFormat.setEncoding(paramString2);
			localXMLWriter = new XMLWriter(new FileOutputStream(new File(
					paramString1)), localOutputFormat);
			localXMLWriter.write(localDocument);
			localXMLWriter.close();
			i = 1;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return i;
	}

	public static int writeformatXMLFile(String paramString1,
			org.dom4j.Document paramDocument, String paramString2) {
		int i = 0;
		try {
			XMLWriter localXMLWriter = null;
			OutputFormat localOutputFormat = OutputFormat.createPrettyPrint();
			localOutputFormat.setEncoding(paramString2);
			localXMLWriter = new XMLWriter(new FileOutputStream(new File(
					paramString1)), localOutputFormat);
			localXMLWriter.write(paramDocument);
			localXMLWriter.close();
			i = 1;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return i;
	}

	public static void deleteDirectFiles(File paramFile) {
		if (paramFile.isFile()) {
			paramFile.delete();
		} else {
			File[] arrayOfFile = paramFile.listFiles();
			if ((arrayOfFile != null) && (arrayOfFile.length > 0))
				for (int i = 0; i < arrayOfFile.length; i++)
					deleteDirectFiles(arrayOfFile[i]);
			paramFile.delete();
		}
	}
}
