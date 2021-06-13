package com.tulin.v8.ide.ui.editors.process.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class NodePropertys implements IPropertys {
	JSONArray data = new JSONArray();
	List<IProperty> reslist = new ArrayList<IProperty>();

	public NodePropertys(JSONArray data) {
		if (data != null)
			this.data = data;
	}

	public List<IProperty> getProperty() {
		try {
			Map<String, IProperty> res = new HashMap<String, IProperty>();
			for (int i = 0; i < data.length(); i++) {
				JSONObject json = data.getJSONObject(i);
				PropertyElement proper = new PropertyElement(json);
				String properid = proper.getId();
				proper.setLabel(NodePropertySet.getProperLabel(properid));
				res.put(properid, proper);
			}
			for (String properid : NodePropertySet.nodeproperlist) {
				if (res.containsKey(properid)) {
					reslist.add(res.get(properid));
				} else {
					PropertyElement proper = new PropertyElement();
					proper.setId(properid);
					proper.setLabel(NodePropertySet.getProperLabel(properid));
					proper.setValue("");
					proper.setInputType(NodePropertySet.getProperText(properid));
					proper.setButton(NodePropertySet.getProperButton(properid));
					reslist.add(proper);
				}
			}
		} catch (Exception e) {
		}
		return reslist;
	}

	@Override
	public String toJSON() {
		JSONArray json = new JSONArray();
		for (IProperty proper : reslist) {
			json.put(proper.toJSON());
		}
		return json.toString();
	}
}
