package com.tulin.v8.ide.ui.editors.process.element;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class AbstractNode implements IElement{
	private String id;
	private String name;
	private String type;
	private String shape;
	private String number;
	private String left;
	private String top;
	private String width;
	private String height;
	
	protected JSONObject data;

	private JSONArray property;
	
	public AbstractNode(JSONObject data) {
		this.data = data;
	}

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

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public JSONArray getProperty() {
		return property;
	}

	public void setProperty(JSONArray property) {
		this.property = property;
	}
	
	public IElement truanse() {
		try {
			setId(data.getString("id"));
			setName(data.getString("name"));
			setType(data.getString("type"));
			setShape(data.getString("shape"));
			setNumber(data.getString("number"));
			setLeft(data.getString("left"));
			setTop(data.getString("top"));
			setWidth(data.getString("width"));
			setHeight(data.getString("height"));
		} catch (Exception e) {
		}
		return this;
	}

}
