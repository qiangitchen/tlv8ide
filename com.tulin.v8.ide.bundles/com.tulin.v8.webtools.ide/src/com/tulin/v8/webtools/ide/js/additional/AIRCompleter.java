package com.tulin.v8.webtools.ide.js.additional;

import java.util.List;

import com.tulin.v8.webtools.ide.js.model.JavaScriptModel;

/**
 * <code>IAdditionalJavaScriptCompleter</code> implementation for Adobe AIR.
 * @Deprecated 使用tern提供js代码辅助
 */
@Deprecated
public class AIRCompleter extends AbstractCompleter {
	public List<JavaScriptModel> loadModel(List<JavaScriptModel> libModelList) {
		return null;
	}
}
