package com.tulin.v8.ide.ui.editors.echt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Element;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.ui.editors.echt.dialog.ParameterDialog;

public class Parameters {
	Element baseData;
	Map<String, Map<String, String>> params = new HashMap<String, Map<String, String>>();

	public Parameters(Element baseData) {
		this.baseData = baseData;
		List<Element> sqlEls = baseData.elements("sql");
		for (int i = 0; i < sqlEls.size(); i++) {
			Element sqlEl = sqlEls.get(i);
			String sql = sqlEl.getText();
			sql = sql.replace("\t", " ");
			sql = sql.replace("\n", " ");
			Pattern pattern = Pattern.compile("(?<=\\{)(.+?)(?=\\})");
			Matcher matcher = pattern.matcher(sql);
			Map<String, String> map = new HashMap<String, String>();
			while (matcher.find()) {
				String param = matcher.group(1);
				map.put(param, "");
			}
			if (!map.isEmpty()) {
				params.put(sql, map);
			}
		}
		if (!params.isEmpty()) {
			init();
		}
	}

	public void init() {
		ParameterDialog pdialog = new ParameterDialog(StudioPlugin.getShell(), Parameters.this);
		pdialog.open();
	}

	public Map<String, Map<String, String>> getParamsMap() {
		return params;
	}

	public Map<String, String> getParamsMaps() {
		Map<String, String> maps = new HashMap<String, String>();
		for (Map<String, String> map : params.values()) {
			maps.putAll(map);
		}
		return maps;
	}

	public String getParameters(String p) {
		return getParamsMaps().get(p);
	}

	public void setParameters(String data, String text) {
		for (Map<String, String> map : params.values()) {
			if (map.containsKey(data)) {
				map.put(data, text);
				break;
			}
		}
	}

}
