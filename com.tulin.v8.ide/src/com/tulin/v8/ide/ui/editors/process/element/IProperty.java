package com.tulin.v8.ide.ui.editors.process.element;

import org.json.JSONObject;

public interface IProperty {
	public String getId();

	public String getLabel();

	public String getInputType();

	public String getValue();

	public void setValue(String value);

	public String getButton();
	
	public JSONObject toJSON();

}
