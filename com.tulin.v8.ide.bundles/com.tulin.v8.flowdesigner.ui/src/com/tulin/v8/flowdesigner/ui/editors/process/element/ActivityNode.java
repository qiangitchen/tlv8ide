package com.tulin.v8.flowdesigner.ui.editors.process.element;

import org.json.JSONObject;

public class ActivityNode extends AbstractNode {

	public ActivityNode(JSONObject data) {
		super(data);
	}

	public IElement truanse() {
		super.truanse();
		try {
			setProperty(data.getJSONArray("property"));
		} catch (Exception e) {
		}
		return this;
	}
}
