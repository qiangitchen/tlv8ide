package com.tulin.v8.flowdesigner.ui.editors.process.element;

import org.json.JSONException;
import org.json.JSONObject;

public class PropertyElement implements IProperty {
	private String id;
	private String label;
	private String inputType;
	private String value;
	private String button;

	protected JSONObject data = new JSONObject();

	public PropertyElement() {
	}

	public PropertyElement(JSONObject data) {
		this.data = data;
		try {
			id = data.getString("id");
			inputType = data.getString("text");
			value = data.getString("value");
			button = NodePropertySet.getProperButton(id);
		} catch (Exception e) {
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getButton() {
		return button;
	}

	public void setButton(String button) {
		this.button = button;
	}

	@Override
	public JSONObject toJSON() {
		// return "{\"id\":\"n_p_id\",\"text\":\"span\",\"value\":\"node_3\"}";
		JSONObject json = new JSONObject();
		try {
			json.put("id", id);
			json.put("text", inputType);
			json.put("value", value);
		} catch (JSONException e) {
		}
		return json;
	}

}
