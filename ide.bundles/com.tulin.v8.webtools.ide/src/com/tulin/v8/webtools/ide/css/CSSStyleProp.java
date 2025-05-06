package com.tulin.v8.webtools.ide.css;

import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSValue;

/**
 * CSS属性类
 * 
 * @author chenqian
 */
public class CSSStyleProp {
	private String property;
	private CSSValue value;
	private CSSStyleRule parent;
	private int offset;

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public CSSValue getValue() {
		return value;
	}

	public void setValue(CSSValue value) {
		this.value = value;
	}

	public CSSStyleRule getParent() {
		return parent;
	}

	public void setParent(CSSStyleRule parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return property + ":" + value.getCssText();
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
}
