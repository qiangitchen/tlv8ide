package tern;

import org.w3c.dom.Document;

public interface IDOMProvider {

	Document getDocument(ITernFile resource);

}
