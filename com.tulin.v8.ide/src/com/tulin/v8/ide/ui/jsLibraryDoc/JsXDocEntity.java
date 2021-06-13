package com.tulin.v8.ide.ui.jsLibraryDoc;

import java.util.List;

public class JsXDocEntity extends JsEntityValidator {
	private String jdField_a_of_type_JavaLangString;
	private String jdField_b_of_type_JavaLangString;
	private String c;
	private String d;
	private String e;
	private String f;
	private String g;
	private String h;
	private String i;
	private String j;
	private JsXDocEntity jdField_a_of_type_ComStudioUiJsLibraryDocJsXDocEntity;
	private String k;
	private String l;
	private JsXDocEntityReturns jdField_a_of_type_ComStudioUiJsLibraryDocJsXDocEntityReturns;
	private String m;
	private List<JsXDocEntityParam> jdField_a_of_type_JavaUtilList;
	private List<String> jdField_b_of_type_JavaUtilList;
	private String n;

	public String getFullInformation() {
		return this.n;
	}

	public void setFullInformation(String paramString) {
		this.n = paramString;
	}

	public List<String> getParamsItem() {
		return this.jdField_b_of_type_JavaUtilList;
	}

	public void setParamsItem(List<String> paramList) {
		this.jdField_b_of_type_JavaUtilList = paramList;
	}

	public String getReturnsItem() {
		return this.m;
	}

	public void setReturnsItem(String paramString) {
		this.m = paramString;
	}

	public String getDescriptionItem() {
		return this.l;
	}

	public void setDescriptionItem(String paramString) {
		this.l = paramString;
	}

	public String getTypeItem() {
		return this.d;
	}

	public void setTypeItem(String paramString) {
		this.d = paramString;
	}

	public String getName() {
		return this.jdField_a_of_type_JavaLangString;
	}

	public void setName(String paramString) {
		this.jdField_a_of_type_JavaLangString = paramString;
	}

	public String getType() {
		return this.c;
	}

	public void setType(String paramString) {
		this.c = paramString;
	}

	public String getDescription() {
		return this.k;
	}

	public void setDescription(String paramString) {
		this.k = paramString;
	}

	public List<JsXDocEntityParam> getParams() {
		return this.jdField_a_of_type_JavaUtilList;
	}

	public void setParams(List<JsXDocEntityParam> paramList) {
		this.jdField_a_of_type_JavaUtilList = paramList;
	}

	public JsXDocEntityReturns getReturns() {
		return this.jdField_a_of_type_ComStudioUiJsLibraryDocJsXDocEntityReturns;
	}

	public void setReturns(JsXDocEntityReturns paramJsXDocEntityReturns) {
		this.jdField_a_of_type_ComStudioUiJsLibraryDocJsXDocEntityReturns = paramJsXDocEntityReturns;
	}

	public String getNameItem() {
		return this.jdField_b_of_type_JavaLangString;
	}

	public void setNameItem(String paramString) {
		this.jdField_b_of_type_JavaLangString = paramString;
	}

	public String getPropertyType() {
		return this.e;
	}

	public void setPropertyType(String paramString) {
		this.e = paramString;
		a();
	}

	private void a() {
		this.e = (this.e.matches(PATTERN_JSFUNCTION) ? this.e : "Object");
		if ((!this.e.contains(".")) && (!this.e.equals("Object")))
			this.e = JsXDocParser.firstToUpCase(this.e);
	}

	public String getPropertyTypeItem() {
		return this.f;
	}

	public void setPropertyTypeItem(String paramString) {
		this.f = paramString;
	}

	public String getComponent() {
		return this.g;
	}

	public void setComponent(String paramString) {
		this.g = paramString;
	}

	public String getComponentItem() {
		return this.h;
	}

	public void setComponentItem(String paramString) {
		this.h = paramString;
	}

	public String getPathItem() {
		return this.h;
	}

	public void setPathItem(String paramString) {
		this.h = paramString;
	}

	public String getExtend() {
		return this.i;
	}

	public void setExtend(String paramString) {
		this.i = paramString;
	}

	public String getExtendItem() {
		return this.j;
	}

	public void setExtendItem(String paramString) {
		this.j = paramString;
	}

	public JsXDocEntity getParentDocEntity() {
		return this.jdField_a_of_type_ComStudioUiJsLibraryDocJsXDocEntity;
	}

	public void setParentDocEntity(JsXDocEntity paramJsXDocEntity) {
		this.jdField_a_of_type_ComStudioUiJsLibraryDocJsXDocEntity = paramJsXDocEntity;
	}

	public boolean isComponent() {
		return (getComponent() != null) && (getComponent().length() > 0);
	}
}
