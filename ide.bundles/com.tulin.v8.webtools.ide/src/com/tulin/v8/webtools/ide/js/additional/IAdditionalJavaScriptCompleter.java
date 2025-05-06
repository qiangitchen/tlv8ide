package com.tulin.v8.webtools.ide.js.additional;

import java.util.List;

import com.tulin.v8.webtools.ide.js.model.JavaScriptModel;

/**
 * Provides additional JavaScript completion proposals.
 */
public interface IAdditionalJavaScriptCompleter {

	/**
	 * Load JS file and put the created model to the list.
	 * 
	 * @param libModelList
	 */
	List<JavaScriptModel> loadModel(List<JavaScriptModel> libModelList);
}
