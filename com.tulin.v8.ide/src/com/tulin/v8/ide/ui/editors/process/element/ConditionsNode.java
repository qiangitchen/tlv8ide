package com.tulin.v8.ide.ui.editors.process.element;

import org.json.JSONArray;

public class ConditionsNode implements IElement {
	private String id;
	private String name;
	private String type;
	private String shape;
	private String number;

	private JSONArray property;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public JSONArray getProperty() {
		return property;
	}

	public void setProperty(JSONArray property) {
		this.property = property;
	}

}
