package com.tulin.v8.core;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Sys {
	// JavaTesting控制是否处于java调试中，调试是要在main函数里置为true
	public static boolean testing = false;
	public static boolean allowPrintDebugMsg = true;

	public static void printMsg(Object aMsg) {
		if (allowPrintDebugMsg)
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ")
					.format(new Date())
					+ " " + aMsg);
		ConsoleFactory.printToConsole(new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss ").format(new Date())
				+ " " + aMsg, true, false);
	}

	public static void printErrMsg(Object aMsg) {
		if (allowPrintDebugMsg)
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ")
					.format(new Date())
					+ " " + aMsg);
		ConsoleFactory.printToConsole(new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss ").format(new Date())
				+ " " + aMsg, true, true);
	}

	public static void printResult(String aMethodName, Object aResult) {
		if (aResult != null)
			printMsg(String.format("%s: %s", aMethodName, aResult.toString()));
		else
			printMsg(String.format("%s: null", aMethodName));
	}

	public static String packErrMsg(String msg) {
		String result = "jar-exception:%s";
		result = String.format(result, msg);
		printErrMsg(result);
		return result;
	}

	public static boolean isHave(String[] strs, String s) {
		/*
		 * 此方法有两个参数，第一个是要查找的字符串数组，第二个是要查找的字符或字符串
		 */
		for (int i = 0; i < strs.length; i++) {
			if (strs[i].indexOf(s) != -1) {// 循环查找字符串数组中的每个字符串中是否包含所有查找的内容
				return true;// 查找到了就返回真，不在继续查询
			}
		}
		return false;// 没找到返回false
	}

	public static Object invoke(String fullMethodName, Object... args) {

		String s = "";
		for (int i = 0; i < args.length; i++) {
			s = String.format("%s,%s(%s)", s, args[i].toString(), args[i]
					.getClass().toString());
		}
		if (s.length() > 0)
			s = s.substring(1, s.length());
		printMsg(String.format("Sys.invoke(fullMethodName: %s(%s)",
				fullMethodName, s));

		String cName = extractClassName(fullMethodName);
		String mName = extractMethodName(fullMethodName);
		try {
			Class c = Class.forName(cName);
			Class[] paratype = new Class[args.length];

			int i = 0;
			for (Object o : args) {
				paratype[i] = o.getClass();
				i++;
			}
			Method m;
			m = c.getMethod(mName, paratype);
			return m.invoke(cName, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String extractClassName(String value) {
		String result = "";
		int i = value.lastIndexOf('.');
		result = value.substring(0, i);
		return result;
	}

	public static String extractMethodName(String value) {
		String result = "";
		int i = value.lastIndexOf('.');
		result = value.substring(i + 1, value.length());
		return result;
	}

	public static void main(String[] args) {
		Sys.testing = true;
		printMsg("main-Result:"
				+ invoke("sqlAction.callProduce", "ProduceTest",
						"123456;DYCRM201").toString());
	}

	public static Object tempMethod(Object arg0) {
		System.out.println(arg0.toString());
		return arg0;
	}
}
