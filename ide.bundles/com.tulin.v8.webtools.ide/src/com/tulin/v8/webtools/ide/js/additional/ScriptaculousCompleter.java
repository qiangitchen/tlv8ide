package com.tulin.v8.webtools.ide.js.additional;

import java.util.ArrayList;
import java.util.List;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.js.model.JavaScriptModel;

/**
 * @Deprecated 使用tern提供js代码辅助
 */
@Deprecated
public class ScriptaculousCompleter extends AbstractCompleter {
	private static final String[] JS_FILES = new String[] { "scriptaculous.js", "builder.js", "effects.js",
			"dragdrop.js", "controls.js", "slider.js", "sound" };
	private static final String BASE_JS_DIR = "js/scriptaculous/1.9.0/";

	public List<JavaScriptModel> loadModel(List<JavaScriptModel> libModelList) {
		try {
			List<JavaScriptModel> list = new ArrayList<JavaScriptModel>();
			for (String jsFile : JS_FILES) {
				JsFileCache jsFileCache = getJsFileCache(BASE_JS_DIR + jsFile);
				String source = new String(jsFileCache.getBytes(), "UTF-8");
				JavaScriptModel model = new JavaScriptModel(jsFileCache.getFile(), source, libModelList);
				if (model != null) {
					libModelList.add(model);
					list.add(model);
				}
			}
			return list.isEmpty() ? null : list;
		} catch (Exception e) {
			WebToolsPlugin.logException(e);
		}
		return null;
	}
}
