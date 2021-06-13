package com.tulin.v8.core;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class StringArray {
	public List<String> list = new ArrayList<String>();

	/*
	 * 默认构造方法
	 */
	public StringArray() {
	}

	/*
	 * List构造方法
	 */
	public StringArray(List<String> list) {
		if (list != null) {
			this.list = list;
		}
	}

	/*
	 * 字符数组构造方法
	 */
	public StringArray(String[] subArray) {
		if (subArray != null) {
			for (int i = 0; i < subArray.length; i++) {
				list.add(subArray[i]);
			}
		}
	}

	/*
	 * 追加值'字符'
	 */
	public void push(String item) {
		if (item == null) {
			item = "";
		}
		list.add(item);
	}

	/*
	 * StringArray追加
	 */
	public void push(StringArray item) {
		if (item != null) {
			for (int i = 0; i < item.list.size(); i++) {
				list.add(item.list.get(i));
			}
		}
	}

	/*
	 * 获取数组总长度
	 */
	public int getLength() {
		return list.size();
	}

	/*
	 * 获取指定位置的值
	 */
	public String get(int i) {
		return (String) list.get(i);
	}

	/*
	 * 获取最后一个值
	 */
	public String pop() {
		return (String) list.get(list.size() - 1);
	}

	/*
	 * 删除指定位置的值
	 */
	public StringArray remove(int idx) {
		if (idx < 0 || idx >= list.size()) {
			return this;
		}
		List<String> nlist = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			if (i != idx) {
				nlist.add(list.get(i));
			}
		}
		list = nlist;
		return this;
	}

	/*
	 * 截取起始位置到结束位置的数组
	 */
	public StringArray slice(int start, int end) {
		if (end <= 0 && start >= list.size()) {
			return this;
		}
		if (end > list.size()) {
			end = list.size();
		}
		if (start < 0) {
			start = 0;
		}
		List<String> nlist = new ArrayList<String>();
		for (int i = start; i < end; i++) {
			nlist.add(list.get(i));
		}
		list = nlist;
		return this;
	}

	/*
	 * 以指定字符分隔连接成字符串
	 */
	public String join(String split) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (i > 0) {
				result.append(split);
			}
			result.append(list.get(i));
		}
		return result.toString();
	}

	/*
	 * 获取指定字符在数组中的位置
	 * 
	 * @不存在返回-1
	 */
	public int indexOf(String val) {
		for (int i = 0; i < list.size(); i++) {
			try {
				if (list.get(i).equals(val)) {
					return i;
				}
			} catch (Exception e) {
				// 抛弃空值异常
			}
		}
		return -1;
	}

	/*
	 * 转换为JSON字符
	 */
	public String toJson() {
		return new JSONArray(list).toString();
	}

	/*
	 * 转换为JSON对象
	 */
	public JSONArray toJSON() {
		return new JSONArray(list);
	}
}
