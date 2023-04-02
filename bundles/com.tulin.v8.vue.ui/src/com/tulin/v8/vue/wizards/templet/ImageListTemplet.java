package com.tulin.v8.vue.wizards.templet;

import java.util.HashMap;
import java.util.Map;

import com.tulin.v8.core.utils.PlaceholderUtils;
import com.tulin.v8.vue.wizards.templet.utils.TempletsReader;

public class ImageListTemplet extends TempletsReader {

	public static String getPageContext(String dbkey, String tableName, String keyField, String dataOrder, String title,
			String description, String previewImage, String smallIcon, String content) throws Exception {
		String context = getTempletPageContext("imageList");
		Map<String, Object> data = new HashMap<>();
		data.put("tableName", tableName);
		data.put("keyField", keyField);
		data.put("dataOrder", dataOrder);
		data.put("Title", title);
		data.put("Description", description);
		data.put("PreviewImage", previewImage);
		data.put("SmallIcon", smallIcon);
		data.put("content", content);
		context = PlaceholderUtils.process(context, data);
		return context;
	}
}
