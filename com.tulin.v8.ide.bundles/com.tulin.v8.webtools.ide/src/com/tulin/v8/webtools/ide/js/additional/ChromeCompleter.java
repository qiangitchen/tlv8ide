package com.tulin.v8.webtools.ide.js.additional;

import java.util.ArrayList;
import java.util.List;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.js.model.JavaScriptModel;

/**
 * <code>IAdditionalJavaScriptCompleter</code> implementation for
 * <strong>Chrome</strong>.
 * 
 * @Deprecated 使用tern提供js代码辅助
 */
@Deprecated
public class ChromeCompleter extends AbstractCompleter {
	public List<JavaScriptModel> loadModel(List<JavaScriptModel> libModelList) {
		try {
			JsFileCache jsFileCache = getJsFileCache("js/chrome/chrome-16.js");
			String source = new String(jsFileCache.getBytes(), "UTF-8");
			JavaScriptModel model = new JavaScriptModel(jsFileCache.getFile(), source, libModelList);
			if (model != null) {
				libModelList.add(model);
				List<JavaScriptModel> list = new ArrayList<JavaScriptModel>();
				list.add(model);
				return list;
			}
		} catch (Exception e) {
			WebToolsPlugin.logException(e);
		}
		return null;
	}
}
