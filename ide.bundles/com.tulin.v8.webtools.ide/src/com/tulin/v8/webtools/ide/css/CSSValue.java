package com.tulin.v8.webtools.ide.css;

import java.util.ArrayList;
import java.util.List;

/**
 * CSS样式属性值
 */
public class CSSValue {
	private String name;
	private List<CSSInfo> values = new ArrayList<>();

	public CSSValue() {
	}

	public CSSValue(String name) {
		this.setName(name);
	}

	public CSSValue(String name, CSSInfo[] value) {
		this.setName(name);
		for (CSSInfo v : value) {
			values.add(v);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValues(CSSInfo[] value) {
		values.clear();
		for (CSSInfo v : value) {
			values.add(v);
		}
	}

	public void setValues(List<CSSInfo> values) {
		this.values = values;
	}

	public void addValue(CSSInfo value) {
		values.add(value);
	}

	public List<CSSInfo> getValues() {
		return values;
	}

}
