package tern.internal.resources;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import tern.IDOMProvider;
import tern.ITernFile;

public class DefaultDOMProvider implements IDOMProvider {

	public static final IDOMProvider INSTANCE = new DefaultDOMProvider();

	private DefaultDOMProvider() {
		
	}
	
	@Override
	public Document getDocument(ITernFile file) {
		if (file == null) {
			return null;
		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(file.getContents());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
