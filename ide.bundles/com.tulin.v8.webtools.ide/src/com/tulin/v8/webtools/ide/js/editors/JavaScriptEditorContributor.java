package com.tulin.v8.webtools.ide.js.editors;

import com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditorContributer;

public class JavaScriptEditorContributor extends HTMLSourceEditorContributer {

	public JavaScriptEditorContributor() {
		addActionId(JavaScriptEditor.ACTION_COMMENT);
		addActionId(JavaScriptEditor.ACTION_OUTLINE);
	}

}
