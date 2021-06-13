package com.tulin.v8.ide.ui.jsLibraryDoc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.ide.utils.FileHelper;
import com.tulin.v8.ide.utils.StudioConfig;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class JsXDocParser {

	public static void main(String[] paramArrayOfString) {
		ArrayList localArrayList = new ArrayList();
		//测试路径
		File localFile = new File(
				"E:\\TuLin1.0_All\\TuLinv8_plugin_win64\\eclipse\\dropins\\studio-app\\jsdoc\\js");
		File[] arrayOfFile = localFile.listFiles();
		for (File localObject2 : arrayOfFile) {
			if ((!localObject2.isFile())
					|| (!localObject2.getName().endsWith(".xdoc")))
				continue;
			localArrayList.addAll(parse(FileHelper.readFileAsStr(
					localObject2.getAbsolutePath(), "\n", false, "utf-8")));
		}
		Object obj = createXDocContext(localArrayList);
		FileHelper.saveFile("E:\\TuLin1.0_All\\all.js", (String) obj);
	}

	public static boolean isFirstUp(String paramString) {
		boolean bool = false;
		if ((paramString != null) && (!paramString.equals("")))
			bool = Character.isUpperCase(paramString.charAt(0));
		return bool;
	}

	public static String firstToUpCase(String paramString) {
		if ((paramString == null) || (paramString.equals("")))
			return "";
		return paramString.substring(0, 1).toUpperCase()
				+ paramString.substring(1, paramString.length());
	}

	public static String[] splitString(String paramString) {
		int i = paramString.length();
		ArrayList localArrayList = new ArrayList();
		StringBuffer localStringBuffer = new StringBuffer();
		for (int j = 0; j < i; j++) {
			char c = paramString.charAt(j);
			if ((c == '.') || (c == '#')) {
				localArrayList.add(localStringBuffer.toString());
				localStringBuffer = new StringBuffer();
			} else if (j >= i - 1) {
				localStringBuffer.append(c);
				localArrayList.add(localStringBuffer.toString());
			} else {
				localStringBuffer.append(c);
			}
		}
		return (String[]) localArrayList.toArray(new String[localArrayList
				.size()]);
	}

	private static String a(String[] paramArrayOfString) {
		String str = "";
		int i = paramArrayOfString.length - 2;
		for (int j = 0; j <= i; j++)
			str = str + "_" + paramArrayOfString[j];
		return str;
	}

	private static String a(String[] paramArrayOfString, int paramInt) {
		String str = "";
		for (int i = 0; i <= paramInt; i++)
			str = str + "_" + paramArrayOfString[i];
		return str;
	}

	private static String a(File paramFile, String paramString) {
		String str1 = null;
		try {
			Document localDocument = FileHelper.getDocumentByPath(paramFile);
			Element localElement1 = localDocument.getRootElement();
			List localList = localElement1.elements("element");
			Iterator localIterator = localList.iterator();
			while (localIterator.hasNext()) {
				Element localElement2 = (Element) localIterator.next();
				String str2 = localElement2.attributeValue("binding-component");
				if ((str2 != null) && (str2.equals(paramString)))
					str1 = localElement2.attributeValue("name");
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return str1;
	}

	public static String getPropertyEntryName(String paramString) {
		String str1 = paramString.substring(0, paramString.indexOf(".xbl.xml"));
		String str2 = new File(StudioConfig.getStudioPath()).getParent()
				+ "/model";
		str2 = CommonUtil.rebuildFilePath(str2);
		String str3 = str2 + str1 + "/designer";
		File localFile1 = new File(str3);
		File[] arrayOfFile1 = localFile1.listFiles();
		String str4 = null;
		for (File localFile2 : arrayOfFile1) {
			if ((!localFile2.isFile())
					|| (!localFile2.getName().endsWith(".xml")))
				continue;
			str4 = a(localFile2, paramString);
			if (str4 != null)
				break;
		}
		return str4 == null ? paramString.replace(".xbl.xml", "")
				.replace("/", "_").replace("#", "_") : str4;
	}

	public static String createXDocContext(List<JsXDocEntity> paramList) {
		StringBuffer localStringBuffer = new StringBuffer();
		HashMap localHashMap = new HashMap();
		for (int i = 0; i < paramList.size(); i++) {
			JsXDocEntity localJsXDocEntity = (JsXDocEntity) paramList.get(i);
			String str2;
			if ((localJsXDocEntity.getType().equals("@class"))
					&& (!CommonUtil.isEmpty(localJsXDocEntity.getName()))) {
				if (!CommonUtil.isEmpty(localJsXDocEntity.getComponent())) {
					Object localObject1 = getPropertyEntryName(localJsXDocEntity
							.getComponent());
					if ((!((String) localObject1).contains("."))
							&& (localHashMap.get(localObject1) == null)) {
						localHashMap.put(localObject1,
								localJsXDocEntity.getName());
						localStringBuffer.append(localJsXDocEntity
								.getFullInformation() + "\n");
						localStringBuffer.append("function "
								+ (String) localObject1 + "() {};\n");
						if (!CommonUtil.isEmpty(localJsXDocEntity.getExtend()))
							localStringBuffer.append((String) localObject1
									+ ".prototype = new "
									+ localJsXDocEntity.getExtend() + "();\n");
						else
							localStringBuffer.append((String) localObject1
									+ ".prototype = new Object();\n");
					}
				} else {
					String[] localObject1 = splitString(localJsXDocEntity
							.getName());
					for (int j = 0; j < localObject1.length; j++) {
						String str1;
						// if (true || isFirstUp(localObject1[j])) {
						str1 = a(localObject1, j);
						if (localHashMap.get(str1) == null) {
							localHashMap.put(str1, localObject1[j]);
							localStringBuffer.append("function " + str1
									+ "() {};\n");
							if (j >= localObject1.length - 1)
								localStringBuffer.append(localJsXDocEntity
										.getFullInformation() + "\n");
							str2 = a(localObject1, j - 1);
							if (!str2.equals(""))
								str2 = str2 + "." + localObject1[j];
							else
								str2 = localObject1[j];
							String str3 = "";
							for (int m = 0; m <= j; m++) {
								str3 = str3 + localObject1[m];
								if (m == j)
									continue;
								str3 = str3 + ".";
							}
							localStringBuffer.append(str2 + "= new " + str1
									+ "();\n");
							// localStringBuffer.append(firstToUpCase(str2)
							// + "= new " + str1 + "();\n");
							localStringBuffer.append(str3 + ".prototype = new "
									+ str1 + "();\n");
						}
						// } else {
						// str1 = "_" + localObject1[j];
						// if (localHashMap.get(str1) != null)
						// continue;
						// localHashMap.put(str1, localObject1[j]);
						// localStringBuffer.append("function " + str1
						// + "() {};\n");
						// localStringBuffer.append("var " + localObject1[j]
						// + " = new " + str1 + "();\n");
						// }
					}
				}
			} else {
				Object localObject3;
				if (localJsXDocEntity.getType().equals("@function")) {
					String[] localObject1 = a(localJsXDocEntity);
					localStringBuffer.append(localJsXDocEntity
							.getFullInformation() + "\n");
					localStringBuffer.append(localObject1[0] + "."
							+ localObject1[1] + " = function(");
					Object localObject2 = localJsXDocEntity.getParams();
					if (localObject2 != null)
						for (int k = 0; k < ((List) localObject2).size(); k++) {
							localStringBuffer
									.append(((JsXDocEntityParam) ((List) localObject2)
											.get(k)).getName());
							if (k >= ((List) localObject2).size() - 1)
								continue;
							localStringBuffer.append(",");
						}
					localStringBuffer.append(") {");
					localObject3 = localJsXDocEntity.getReturns();
					if ((localObject3 != null)
							&& (!((JsXDocEntityReturns) localObject3).getType()
									.equals("void"))) {
						str2 = ((JsXDocEntityReturns) localObject3).getType();
						localStringBuffer.append("return new " + str2 + "();");
					}
					localStringBuffer.append("};\n");
				} else if (localJsXDocEntity.getType().equals("@constant")) {
					String[] localObject1 = a(localJsXDocEntity);
					localStringBuffer.append(localJsXDocEntity
							.getFullInformation() + "\n");
					localStringBuffer.append(localObject1[0] + "."
							+ localObject1[1] + " = \"\";\n");
				} else {
					if (!localJsXDocEntity.getType().equals("@property"))
						continue;
					Object localObject1 = localJsXDocEntity.getDescription();
					if ((localObject1 == null)
							|| (!((String) localObject1).contains("[filed]")))
						continue;
					String[] localObject2 = a(localJsXDocEntity);
					localStringBuffer.append(localJsXDocEntity
							.getFullInformation() + "\n");
					localObject3 = localJsXDocEntity.getPropertyType();
					str2 = "new Object()";
					if (localObject3 != null)
						str2 = "new " + (String) localObject3 + "()";
					localStringBuffer.append(firstToUpCase(localObject2[0])
							+ "." + localObject2[1] + " = " + str2 + ";\n");
				}
			}
		}
		return (String) localStringBuffer.toString();
	}

	private static String[] a(JsXDocEntity paramJsXDocEntity) {
		String[] arrayOfString1 = splitString(paramJsXDocEntity.getName());
		String[] arrayOfString2 = new String[2];
		String str;
		if (paramJsXDocEntity.getParentDocEntity() != null)
			str = getPropertyEntryName(paramJsXDocEntity.getParentDocEntity()
					.getComponent());
		else
			str = a(arrayOfString1);
		arrayOfString2[0] = str;
		arrayOfString2[1] = arrayOfString1[(arrayOfString1.length - 1)];
		return arrayOfString2;
	}

	public static List<JsXDocEntity> parse(String paramString) {
		ArrayList localArrayList = new ArrayList();
		Object localObject1 = null;
		int i = paramString.indexOf("@name");
		while (i != -1) {
			int j = paramString.indexOf("*/", i);
			if (j != -1) {
				String str1 = paramString.substring(i - 1, j);
				JsXDocEntity localJsXDocEntity = new JsXDocEntity();
				localJsXDocEntity.setFullInformation("/**\n" + str1 + "*/");
				int k = str1.indexOf("@name");
				int m = str1.indexOf("@", k + 1);
				if (m == -1)
					m = str1.length();
				String str2 = "";
				if (k != -1)
					str2 = str1.substring(k, m);
				String nameItem = str2.replace("\n", "").replace("*", "");
				localJsXDocEntity.setNameItem(nameItem);
				str2 = str2.replace("@name ", "").replace("\n", "")
						.replace("*", "").trim();
				localJsXDocEntity.setName(str2);
				if ((localObject1 != null)
						&& (str2.startsWith(((JsXDocEntity) localObject1)
								.getName())))
					localJsXDocEntity
							.setParentDocEntity((JsXDocEntity) localObject1);
				int n = str1.indexOf("@class");
				if (n == -1)
					n = str1.indexOf("@namespace");
				String str3 = "@class";
				if (n == -1) {
					n = str1.indexOf("@function");
					str3 = "@function";
				}
				if (n == -1) {
					n = str1.indexOf("@constant");
					str3 = "@constant";
				}
				if (n == -1) {
					n = str1.indexOf("@property");
					str3 = "@property";
				}
				if (n == -1) {
					n = str1.indexOf("@event");
					str3 = "@event";
				}
				String str4 = "";
				int i1 = str1.indexOf("@", n + 1);
				if (i1 == -1)
					i1 = str1.length();
				if (n != -1)
					str4 = str1.substring(n, i1);
				String typeStr = str4.replace("\n", "").replace("*", "");
				localJsXDocEntity.setTypeItem(typeStr);
				localJsXDocEntity.setType(str3);
				if (str3.equals("@property")) {
					int i2 = str1.indexOf("@type");
					if (i2 != -1) {
						int i3 = str1.indexOf("@", i2 + 1);
						if (i3 == -1)
							i3 = str1.length();
						String str5 = str1.substring(i2, i3).replace("\n", "")
								.replace("\t", "");
						localJsXDocEntity.setPropertyTypeItem(str5);
						String str6 = str5.replaceFirst("@type", "").trim();
						localJsXDocEntity.setPropertyType(str6);
					}
				}
				int i2 = str1.indexOf("@component");
				int i3 = str1.indexOf("@", i2 + 1);
				if (i3 == -1)
					i3 = str1.length();
				String str5 = "";
				if (i2 != -1)
					str5 = str1.substring(i2, i3);
				localJsXDocEntity.setPathItem(str5.replace("\n", "").replace(
						"*", ""));
				str5 = str5.replace("@component ", "").replace("\n", "")
						.replace("*", "").trim();
				localJsXDocEntity.setComponent(str5);
				if (localJsXDocEntity.isComponent())
					localObject1 = localJsXDocEntity;
				int i4 = str1.indexOf("@extends");
				int i5 = str1.indexOf("@", i4 + 1);
				if (i5 == -1)
					i5 = str1.length();
				String str7 = "";
				if (i4 != -1)
					str7 = str1.substring(i4, i5);
				localJsXDocEntity.setExtendItem(str7.replace("\n", "").replace(
						"*", ""));
				str7 = str7.replace("@extends ", "").replace("\n", "")
						.replace("*", "").trim();
				localJsXDocEntity.setExtend(str7);
				int i6 = str1.indexOf("@description");
				int i7 = str1.indexOf("@", i6 + 1);
				if (i7 == -1)
					i7 = str1.length();
				String str8 = "";
				if (i6 != -1)
					str8 = str1.substring(i6, i7);
				localJsXDocEntity.setDescriptionItem(str8.replace("\n", "")
						.replace("*", ""));
				str8 = str8.replace("@description ", "").replace("\n", "")
						.replace("*", "").trim();
				localJsXDocEntity.setDescription(str8);
				localJsXDocEntity.setParams(new ArrayList());
				localJsXDocEntity.setParamsItem(new ArrayList());
				int i12;
				int i13;
				int i9 = str1.indexOf("@returns");
				for (int i8 = str1.indexOf("@param"); i8 != -1; i8 = str1
						.indexOf("@param", i9)) {
					i9 = str1.indexOf("@", i8 + 1);
					if (i9 == -1)
						i9 = str1.length();
					String str9 = str1.substring(i8, i9);
					localJsXDocEntity.getParamsItem().add(
							str9.replace("\n", "").replace("*", ""));
					str9 = str9.replace("@param", "").replace("\n", "")
							.replace("*", "").trim();
					JsXDocEntityParam localObject2 = new JsXDocEntityParam();
					int i11 = str9.indexOf(" ");
					i12 = str9.indexOf("\t");
					i13 = Math.min(i11, i12) == -1 ? Math.max(i11, i12) : Math
							.min(i11, i12);
					if (i13 != -1) {
						String str11 = str9.substring(0, i13);
						String str12 = "";
						int i15 = str9.indexOf(" ", i13 + 1);
						int i16 = str9.indexOf("\t", i13 + 1);
						int i17 = Math.min(i15, i16) == -1 ? Math.max(i15, i16)
								: Math.min(i15, i16);
						if (i17 != -1) {
							str12 = str9.substring(i13 + 1, i17);
							if (str11.contains("{")) {
								((JsXDocEntityParam) localObject2)
										.setType(str11);
								if (str12.matches("^[A-Za-z0-9]+$")) {
									((JsXDocEntityParam) localObject2)
											.setName(str12);
									if (i17 < str9.length() - 1)
										((JsXDocEntityParam) localObject2)
												.setDescription(str9.substring(
														i17 + 1, str9.length()));
									else
										((JsXDocEntityParam) localObject2)
												.setDescription("");
								} else {
									((JsXDocEntityParam) localObject2)
											.setName("");
									((JsXDocEntityParam) localObject2)
											.setDescription(str9.substring(
													i13 + 1, str9.length()));
								}
							} else {
								((JsXDocEntityParam) localObject2)
										.setName(str11);
								if (str12.replace("{", "").replace("}", "")
										.trim().matches("a-zA-Z")) {
									((JsXDocEntityParam) localObject2)
											.setType(str12);
									if (i17 < str9.length() - 1)
										((JsXDocEntityParam) localObject2)
												.setDescription(str9.substring(
														i17 + 1, str9.length()));
									else
										((JsXDocEntityParam) localObject2)
												.setDescription("");
								} else {
									((JsXDocEntityParam) localObject2)
											.setType("{String}");
									((JsXDocEntityParam) localObject2)
											.setDescription(str9.substring(
													i13 + 1, str9.length()));
								}
							}
							if (i17 < str9.length() - 1)
								((JsXDocEntityParam) localObject2)
										.setDescription(str9.substring(i17 + 1,
												str9.length()));
							else
								((JsXDocEntityParam) localObject2)
										.setDescription("");
						} else {
							if (str11.contains("{")) {
								((JsXDocEntityParam) localObject2).setName("");
								((JsXDocEntityParam) localObject2)
										.setType(str11);
							} else {
								((JsXDocEntityParam) localObject2)
										.setName(str11);
								((JsXDocEntityParam) localObject2)
										.setType("{String}");
							}
							((JsXDocEntityParam) localObject2)
									.setDescription("");
						}
					} else {
						((JsXDocEntityParam) localObject2).setName(str9);
					}
					localJsXDocEntity.getParams().add(localObject2);
				}
				int i10 = str1.indexOf("@", i9 + 1);
				if (i10 == -1)
					i10 = str1.length();
				Object localObject2 = new JsXDocEntityReturns();
				String str10 = "";
				if (i9 != -1) {
					localJsXDocEntity.setReturnsItem(str10.replace("\n", "")
							.replace("*", ""));
					str10 = str1.substring(i9, i10);
					str10 = str10.replace("@returns", "").replace("\n", "")
							.replace("*", "").trim();
					i12 = str10.indexOf(" ");
					i13 = str10.indexOf("\t");
					int i14 = Math.min(i12, i13) == -1 ? Math.max(i12, i13)
							: Math.min(i12, i13);
					if (i14 != -1) {
						((JsXDocEntityReturns) localObject2).setType(str10
								.substring(0, i14).replace("{", "")
								.replace("}", ""));
						((JsXDocEntityReturns) localObject2)
								.setDescription(str10.substring(i14 + 1,
										str10.length()));
					} else {
						((JsXDocEntityReturns) localObject2).setType(str10
								.replace("{", "").replace("}", ""));
					}
					localJsXDocEntity
							.setReturns((JsXDocEntityReturns) localObject2);
				}
				localArrayList.add(localJsXDocEntity);
				i = paramString.indexOf("@name", j);
			} else {
				i = -1;
			}
		}
		return (List<JsXDocEntity>) localArrayList;
	}
}
