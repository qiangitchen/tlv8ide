package com.tulin.v8.webtools.ide.html;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.js.JavaScriptAutoEditStrategy;

public class HtmlAutoEditStrategy extends DefaultIndentLineAutoEditStrategy {
	private JavaScriptAutoEditStrategy jsAutoEditStrategy = new JavaScriptAutoEditStrategy();
	private String charset = System.getProperty("file.encoding");
	protected boolean enable;

	public void setEnabled(boolean enable) {
		this.enable = enable;
	}

	public void setFile(IFile file) {
		try {
			this.charset = file.getCharset();
		} catch (CoreException e) {
			WebToolsPlugin.logException(e);
		}
	}

	@Override
	public void customizeDocumentCommand(IDocument d, DocumentCommand c) {
		try {
			if (d.getContentType(c.offset).equals(HTMLPartitionScanner.JAVASCRIPT)) {
				jsAutoEditStrategy.customizeDocumentCommand(d, c);
				return;
			}
		} catch (Exception ex) {
		}

		if (enable) {
			try {
				if ("-".equals(c.text) && c.offset >= 3 && d.get(c.offset - 3, 3).equals("<!-")) {
					c.text = "-  -->";
					c.shiftsCaret = false;
					c.caretOffset = c.offset + 2;
					c.doit = false;
					return;
				}
				if ("[".equals(c.text) && c.offset >= 2 && d.get(c.offset - 2, 2).equals("<!")) {
					c.text = "[CDATA[]]>";
					c.shiftsCaret = false;
					c.caretOffset = c.offset + 7;
					c.doit = false;
					return;
				}
				if ("l".equals(c.text) && c.offset >= 4 && d.get(c.offset - 4, 4).equals("<?xm")) {
					c.text = "l version=\"1.0\" encoding=\"" + charset + "\"?>";
					return;
				}
			} catch (BadLocationException e) {
				WebToolsPlugin.logException(e);
			}
		}
		super.customizeDocumentCommand(d, c);
	}

}
