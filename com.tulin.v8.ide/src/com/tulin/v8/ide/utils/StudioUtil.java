package com.tulin.v8.ide.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public class StudioUtil {
	/*
	 * 汉字转换为大写拼音
	 */
	public static String Hanzi2UPinyin(String hanzi) {
		HanyuPinyinOutputFormat spellFormat = new HanyuPinyinOutputFormat();
		spellFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		spellFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		spellFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

		String pinyinHead = PinyinHelper.toHanyuPinyinString(hanzi,
				spellFormat, "");
		return pinyinHead;
	}

	/*
	 * 汉字转换为小写拼音
	 */
	public static String Hanzi2LPinyin(String hanzi) {
		HanyuPinyinOutputFormat spellFormat = new HanyuPinyinOutputFormat();
		spellFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		spellFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		spellFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

		String pinyinHead = PinyinHelper.toHanyuPinyinString(hanzi,
				spellFormat, "");
		return pinyinHead;
	}

	/*
	 * 是否为HTML页面
	 */
	public static boolean isHTML(String name) {
		return (name.endsWith(".htm") || name.endsWith(".html") || name
				.endsWith(".xhtml"));
	}

	/*
	 * 是否为JSP页面
	 */
	public static boolean isJSP(String name) {
		return (name.endsWith(".jsp") || name.endsWith(".jspx") || name
				.endsWith(".jspf"));
	}

	/*
	 * 是否为页面文件
	 */
	public static boolean isTuLinPage(String name) {
		return isHTML(name);// || isJSP(name);
	}

	/*
	 * 是否为图片
	 */
	public static boolean isImage(String name) {
		return (name.endsWith(".gif") || name.endsWith(".jpg")
				|| name.endsWith(".bmp") || name.endsWith(".png"));
	}

	/*
	 * 是否为压缩包
	 */
	public static boolean isZip(String name) {
		return (name.endsWith(".cab") || name.endsWith(".rar")
				|| name.endsWith(".jar") || name.endsWith(".zip"));
	}

	/*
	 * 是否为表达式配置文件
	 */
	public static boolean isFn(String name) {
		return name.endsWith(".fn.xml") || name.endsWith(".fu");
	}

	/*
	 * 是否为功能树配置文件
	 */
	public static boolean isFun(String name) {
		return name.endsWith(".fun.xml") || name.endsWith(".fun");
	}
}
