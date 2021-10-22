package com.tulin.v8.fn;

import org.dom4j.Element;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

class FunctionItem extends TreeItem {
	private String id;
	private String pid;
	private String type;
	private String param;
	private String paramvalue;
	private String javacode;
	private String helper;

	public FunctionItem(Tree parent, int style) {
		super(parent, style);
	}

	public FunctionItem(TreeItem parent, int style) {
		super(parent, style);
	}

	public FunctionItem(TreeItem parent, Element element, int style) {
		super(parent, style);
		setId(element.attributeValue("id"));
		setPid(element.attributeValue("pId"));
		setType(element.getName());
		setParam(element.attributeValue("param"));
		setParamvalue(element.attributeValue("paramvalue"));
		setJavacode(element.attributeValue("javacode"));
		setHelper(element.attributeValue("helper"));
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPid() {
		return pid;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getParam() {
		return param;
	}

	public void setParamvalue(String paramvalue) {
		this.paramvalue = paramvalue;
	}

	public String getParamvalue() {
		return paramvalue;
	}

	public void setJavacode(String javacode) {
		this.javacode = javacode;
	}

	public String getJavacode() {
		return javacode;
	}

	public void setHelper(String helper) {
		this.helper = helper;
	}

	public String getHelper() {
		return helper;
	}

}
