package com.tulin.v8.ide.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public class StudioUtil {
	/**
	 * 汉字转换为大写拼音
	 * 
	 * @param hanzi
	 * @return
	 */
	public static String Hanzi2UPinyin(String hanzi) {
		HanyuPinyinOutputFormat spellFormat = new HanyuPinyinOutputFormat();
		spellFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		spellFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		spellFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

		String pinyinHead = PinyinHelper.toHanyuPinyinString(hanzi, spellFormat, "");
		return pinyinHead;
	}

	/**
	 * 汉字转换为小写拼音
	 * 
	 * @param hanzi
	 * @return
	 */
	public static String Hanzi2LPinyin(String hanzi) {
		HanyuPinyinOutputFormat spellFormat = new HanyuPinyinOutputFormat();
		spellFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		spellFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		spellFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

		String pinyinHead = PinyinHelper.toHanyuPinyinString(hanzi, spellFormat, "");
		return pinyinHead;
	}

	/**
	 * 将字符数组转换成字符串
	 *
	 * @param ch        字符数组
	 * @param separator 各个字符串之间的分隔符
	 * @return
	 */
	public static String charArrayToString(char[] ch, String separator) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ch.length; i++) {
			sb.append(ch[i]);
			if (ch.length != (i + 1)) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	/**
	 * 取汉字的首字母
	 *
	 * @param src
	 * @param isCapital 是否是大写
	 * @return
	 */
	public static char[] getHeadByChar(char src, boolean isCapital) {
		// 如果不是汉字直接返回
		if (src <= 128) {
			return new char[] { src };
		}
		HanyuPinyinOutputFormat spellFormat = new HanyuPinyinOutputFormat();
		spellFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		spellFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		spellFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		// 获取所有的拼音
		String[] pinyingStr = PinyinHelper.toHanyuPinyinStringArray(src, spellFormat);
		// 创建返回对象
		int polyphoneSize = pinyingStr.length;
		char[] headChars = new char[polyphoneSize];
		int i = 0;
		// 截取首字符
		for (String s : pinyingStr) {
			char headChar = s.charAt(0);
			// 首字母是否大写，默认是小写
			if (isCapital) {
				headChars[i] = Character.toUpperCase(headChar);
			} else {
				headChars[i] = headChar;
			}
			i++;
		}
		return headChars;
	}

	/**
	 * 查找字符串首字母
	 *
	 * @param src       汉字字符串
	 * @param isCapital 是否大写
	 * @param separator 分隔符
	 * @return
	 */
	public static String[] getHeadByString(String src, boolean isCapital, String separator) {
		char[] chars = src.toCharArray();
		String[] headString = new String[chars.length];
		int i = 0;
		for (char ch : chars) {

			char[] chs = getHeadByChar(ch, isCapital);
			StringBuffer sb = new StringBuffer();
			if (null != separator) {
				int j = 1;

				for (char ch1 : chs) {
					sb.append(ch1);
					if (j != chs.length) {
						sb.append(separator);
					}
					j++;
				}
			} else {
				sb.append(chs[0]);
			}
			headString[i] = sb.toString();
			i++;
		}
		return headString;
	}

	/**
	 * 查找字符串首字母
	 *
	 * @param src
	 * @param isCapital 是否大写
	 * @return
	 */
	public static String[] getHeadByString(String src, boolean isCapital) {
		return getHeadByString(src, isCapital, null);
	}

	/**
	 * 将字符串数组转换成字符串
	 *
	 * @param str
	 * @param separator 各个字符串之间的分隔符
	 * @return
	 */
	public static String stringArrayToString(String[] str, String separator) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length; i++) {
			sb.append(str[i]);
			if (str.length != (i + 1)) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	/**
	 * 简单的将各个字符数组之间连接起来
	 *
	 * @param str
	 * @return
	 */
	public static String stringArrayToString(String[] str) {
		return stringArrayToString(str, "");
	}

	/**
	 * 查找字符串首字母
	 *
	 * @param src
	 * @return
	 */
	public static String[] getHeadByString(String src) {
		return getHeadByString(src, true);
	}

	/**
	 * 获取汉字简拼（拼音首字母组合）
	 * 
	 * @param src
	 * @param upper
	 * @return
	 */
	public static String getSimPinYinByString(String src, boolean upper) {
		String[] ss = getHeadByString(src, upper);
		return stringArrayToString(ss);
	}

	/**
	 * 是否为HTML页面
	 */
	public static boolean isHTML(String name) {
		return (name.endsWith(".htm") || name.endsWith(".html") || name.endsWith(".xhtml"));
	}

	/**
	 * 是否为JSP页面
	 */
	public static boolean isJSP(String name) {
		return (name.endsWith(".jsp") || name.endsWith(".jspx") || name.endsWith(".jspf"));
	}

	/**
	 * 是否为页面文件
	 */
	public static boolean isTuLinPage(String name) {
		return isHTML(name);// || isJSP(name);
	}

	/**
	 * 是否为图片
	 */
	public static boolean isImage(String name) {
		return (name.endsWith(".gif") || name.endsWith(".jpg") || name.endsWith(".bmp") || name.endsWith(".png"));
	}

	/**
	 * 是否为压缩包
	 */
	public static boolean isZip(String name) {
		return (name.endsWith(".cab") || name.endsWith(".rar") || name.endsWith(".jar") || name.endsWith(".zip"));
	}

	/**
	 * 是否为表达式配置文件
	 */
	public static boolean isFn(String name) {
		return name.endsWith(".fn.xml") || name.endsWith(".fu");
	}

	/**
	 * 是否为功能树配置文件
	 */
	public static boolean isFun(String name) {
		return name.endsWith(".fun.xml") || name.endsWith(".fun");
	}
}
