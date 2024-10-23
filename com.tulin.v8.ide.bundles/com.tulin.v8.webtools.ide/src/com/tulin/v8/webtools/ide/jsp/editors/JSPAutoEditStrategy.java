package com.tulin.v8.webtools.ide.jsp.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.html.HtmlAutoEditStrategy;

/**
 * Provides auto inserting for JSPs.
 * 
 * <ul>
 * <li><strong>${</strong> would be <strong>${}</strong></li>
 * <li><strong>&lt%--</strong> would be <strong>&lt;%-- --%&gt;</strong></li>
 * <li><del><strong>&lt;%</strong> would be <strong>&lt;%
 * %&gt;</strong></del></li>
 * </ul>
 */
public class JSPAutoEditStrategy extends HtmlAutoEditStrategy {

	@Override
	public void customizeDocumentCommand(IDocument d, DocumentCommand c) {
		if (enable) {
			try {
				if ("{".equals(c.text) && c.offset > 0) {
					if (d.getChar(c.offset - 1) == '$') {
						c.text = "{}";
						c.shiftsCaret = false;
						c.caretOffset = c.offset + 1;
						c.doit = false;
						return;
					}
				}
				if ("-".equals(c.text) && c.offset >= 3 && d.get(c.offset - 3, 3).equals("<%-")) {
					c.text = "-  --%>";
					c.shiftsCaret = false;
					c.caretOffset = c.offset + 2;
					c.doit = false;
					return;
				}
			} catch (BadLocationException e) {
				WebToolsPlugin.logException(e);
			}
		}
		super.customizeDocumentCommand(d, c);
	}

}
