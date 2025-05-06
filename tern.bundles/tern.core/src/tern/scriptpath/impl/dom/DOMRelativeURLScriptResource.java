package tern.scriptpath.impl.dom;

import tern.ITernFile;
import tern.scriptpath.ITernScriptResource;

/**
 * Javascript declared in a script/@src element inside HTML/JSP where script/@src
 * defines a relative url to a script file. Ex :
 * 
 * <pre>
 * 	<script src="/scripts/example.js" ></script>
 * </pre>
 */
public class DOMRelativeURLScriptResource implements ITernScriptResource {

	private final ITernFile relativeFile;
	private final String src;

	public DOMRelativeURLScriptResource(ITernFile domFile, String src) {
		this.relativeFile = domFile.getRelativeFile(src);
		this.src = src;
	}

	@Override
	public ITernFile getFile() {
		return relativeFile;
	}

	@Override
	public String getLabel() {
		return src;
	}

}
