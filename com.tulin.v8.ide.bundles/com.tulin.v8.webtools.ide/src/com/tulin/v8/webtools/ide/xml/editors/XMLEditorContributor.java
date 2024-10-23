package com.tulin.v8.webtools.ide.xml.editors;

import com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditorContributer;

/**
 * The editor contributor for the <code>XMLEditor</code>.
 */
public class XMLEditorContributor extends HTMLSourceEditorContributer {

	public XMLEditorContributor() {
		addActionId(XMLEditor.ACTION_ESCAPE_XML);
		addActionId(XMLEditor.ACTION_COMMENT);
	}

}
