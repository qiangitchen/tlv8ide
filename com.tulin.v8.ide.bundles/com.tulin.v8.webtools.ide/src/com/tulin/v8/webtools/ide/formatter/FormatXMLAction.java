package com.tulin.v8.webtools.ide.formatter;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xerces.dom.CoreDOMImplementationImpl;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

/**
 * XML格式化
 * 
 * @author chenqian
 *
 */
public class FormatXMLAction extends Action {
	TextEditor texteditor;

	public FormatXMLAction(TextEditor texteditor) {
		super(WebToolsPlugin.getResourceString("HTMLEditor.Format"));
		this.texteditor = texteditor;
	}

	@Override
	public void run() {
		try {
			IDocument doc = texteditor.getDocumentProvider().getDocument(texteditor.getEditorInput());
			InputSource source = new InputSource(new StringReader(doc.get()));

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			Document document = factory.newDocumentBuilder().parse(source);

			CoreDocumentImpl coreDocumentImpl = new CoreDocumentImpl();
			DOMImplementation implementation = coreDocumentImpl.getImplementation();
			CoreDOMImplementationImpl impl2 = (CoreDOMImplementationImpl) implementation;
			LSSerializer serializer = impl2.createLSSerializer();

			String charset = System.getProperty("file.encoding");
			IEditorInput input = texteditor.getEditorInput();
			if (input instanceof IFileEditorInput) {
				IFile file = ((IFileEditorInput) input).getFile();
				charset = file.getCharset();
			}

			String xml = serializer.writeToString(document);
			xml = xml.replaceFirst("\"UTF-16\"", "\"" + charset + "\"");

			doc.set(xml);

		} catch (Exception ex) {
			WebToolsPlugin.openAlertDialog(ex.toString());
		}
	}
}
