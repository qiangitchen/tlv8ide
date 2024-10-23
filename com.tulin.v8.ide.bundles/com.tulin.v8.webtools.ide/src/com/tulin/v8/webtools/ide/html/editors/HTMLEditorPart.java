package com.tulin.v8.webtools.ide.html.editors;

import org.eclipse.swt.browser.Browser;

/**
 * This interface is implemented by an EditPart of HTML editor.
 */
public interface HTMLEditorPart {

	public Browser getBrowser();

	public HTMLSourceEditor getSourceEditor();

	public boolean isFileEditorInput();

}
