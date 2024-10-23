package com.tulin.v8.webtools.ide.xml.editors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.tulin.v8.webtools.ide.html.HTMLUtil;

/**
 * An implementation of SAX error handler to validate the XML document. When
 * error method is called, this handler creates a marker as an error.
 */
public class XMLValidationHandler implements ErrorHandler {

	private IResource resource;

	public XMLValidationHandler(IResource resource) {
		this.resource = resource;
	}

	private void addMarker(int line, String message, int type) {
		if (message.startsWith("src-") || message.startsWith("sch-")) {
			return;
		}
		// 行号无效的不做标记
		if (line > -1) {
			HTMLUtil.addMarker(resource, type, line, message);
		}
	}

	public void error(SAXParseException exception) throws SAXException {
		int line = exception.getLineNumber();
		String message = exception.getMessage();
		addMarker(line, message, IMarker.SEVERITY_ERROR);
	}

	public void fatalError(SAXParseException exception) throws SAXException {
		int line = exception.getLineNumber();
		String message = exception.getMessage();
		addMarker(line, message, IMarker.SEVERITY_ERROR);
	}

	public void warning(SAXParseException exception) throws SAXException {
		int line = exception.getLineNumber();
		String message = exception.getMessage();
		addMarker(line, message, IMarker.SEVERITY_WARNING);
	}

}
