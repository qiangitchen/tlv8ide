package com.tulin.v8.ide.ui.editors.process.element;

import org.json.JSONArray;

public interface IElement {

	public String getId();

	public String getName();

	public String getType();

	public String getShape();

	public String getNumber();

	public JSONArray getProperty();

}
