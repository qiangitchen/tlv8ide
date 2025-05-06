package tern.scriptpath.impl.dom;

import tern.ITernFile;
import tern.scriptpath.ITernScriptResource;

/**
 * Javascript declared in a script element inside HTML/JSP where script is
 * declared in the content of the script element. Ex:
 * 
 * <pre>
 * 	<script>
 * 		var arr = [];
 * </script>
 * </pre>
 */
public class DOMContentScriptResource implements ITernScriptResource {

	private final ITernFile domFile;
	private final int indexScript;

	public DOMContentScriptResource(ITernFile domFile, int indexScript) {
		this.domFile = domFile;
		this.indexScript = indexScript;
	}

	@Override
	public ITernFile getFile() {
		return domFile;
	}

	@Override
	public String getLabel() {
		return new StringBuilder("script#").append(indexScript).toString(); //$NON-NLS-1$
	}

}
