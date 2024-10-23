package com.tulin.v8.webtools.ide.xml.editors;

import com.tulin.v8.webtools.ide.assist.HTMLAssistProcessor;
import com.tulin.v8.webtools.ide.color.ColorProvider;
import com.tulin.v8.webtools.ide.html.HTMLHyperlinkDetector;
import com.tulin.v8.webtools.ide.html.editors.HTMLConfiguration;
import com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditor;

/**
 * The editor configuration for the <code>XMLEditor</code>.
 */
public class XMLConfiguration extends HTMLConfiguration {

	private ClassNameHyperLinkProvider classNameHyperlinkProvider = null;

	public XMLConfiguration(HTMLSourceEditor editor, ColorProvider colorProvider) {
		super(editor, colorProvider);
	}

	/**
	 * Returns the <code>XMLAssistProcessor</code> as the assist processor.
	 * 
	 * @return the <code>XMLAssistProcessor</code>
	 */
	protected HTMLAssistProcessor createAssistProcessor() {
		return new XMLAssistProcessor();
	}

	public ClassNameHyperLinkProvider getClassNameHyperlinkProvider() {
		return this.classNameHyperlinkProvider;
	}

	/**
	 * Returns the <code>HTMLHyperlinkDetector</code> which has
	 * <code>ClassNameHyperLinkProvider</code>.
	 * <p>
	 * Provides the classname hyperlink for the following attributes.
	 * <ul>
	 * <li>type</li>
	 * <li>class</li>
	 * <li>classname</li>
	 * <li>bean</li>
	 * <li>component</li></li>
	 */
	@Override
	protected HTMLHyperlinkDetector createHyperlinkDetector() {
		if (this.classNameHyperlinkProvider == null) {
			this.classNameHyperlinkProvider = new ClassNameHyperLinkProvider();
			this.classNameHyperlinkProvider.setEditor((XMLEditor) editor);
		}
		HTMLHyperlinkDetector detector = super.createHyperlinkDetector();
		detector.addHyperlinkProvider(this.classNameHyperlinkProvider);
		return detector;
	}

}
