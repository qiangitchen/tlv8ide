package com.tulin.v8.echarts.ui.wizards.chart;

import java.util.Iterator;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONObject;

@SuppressWarnings("rawtypes")
public class JSON2Element {
	JSONObject json;
	String jsonstr;

	public JSON2Element(String jsonstr) {
		try {
			this.json = new JSONObject(jsonstr);
		} catch (Exception e) {
			this.jsonstr = jsonstr;
		}
	}

	public JSON2Element(JSONObject json) {
		this.json = json;
	}

	public Element parse() {
		Element chart = DocumentHelper.createElement("chart");
		try {
			Iterator iter = json.keys();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				String value = json.getString(key);
				try {
					JSONArray jarr = new JSONArray(value);
					addJsonArr(chart, key, jarr);
				} catch (Exception e) {
					try {
						JSONObject jobj = new JSONObject(value);
						addJsonObj(chart, key, jobj);
					} catch (Exception er) {
						Element pp = chart.addElement(key);
						pp.setText(value);
					}
				}
			}
		} catch (Exception e) {
			if(jsonstr!=null && !"".equals(jsonstr)) {
				chart.addCDATA(jsonstr);
			}
		}
		return chart;
	}

	void addJsonArr(Element chart, String itemname, JSONArray jarr) {
		boolean unjson = false;
		for (int i = 0; i < jarr.length(); i++) {
			try {
				JSONObject cjson = jarr.getJSONObject(i);
				Element ele = chart.addElement(itemname);
				for (Iterator it = cjson.keys(); it.hasNext();) {
					String prp = (String) it.next();
					ele.addAttribute(prp, parseValue(cjson.getString(prp)));
				}
			} catch (Exception e) {
				unjson = true;
				break;
			}
		}
		if (unjson) {
			Element ele = chart.addElement(itemname);
			ele.setText(jarr.toString());
		}
	}

	void addJsonObj(Element chart, String itemname, JSONObject jobj) {
		try {
			Element ele = chart.addElement(itemname);
			for (Iterator it = jobj.keys(); it.hasNext();) {
				String prp = (String) it.next();
				ele.addAttribute(prp, parseValue(jobj.getString(prp)));
			}
		} catch (Exception e) {
		}
	}

	String parseValue(String bvalue) {
		try {
			return new JSONArray(bvalue).toString();
		} catch (Exception e) {
			try {
				return new JSONObject(bvalue).toString();
			} catch (Exception ee) {
				return bvalue;
			}
		}
	}
}
