package tern.scriptpath.impl.dom;

import tern.ITernFile;
import tern.scriptpath.ITernScriptResource;

/**
 * Javascript declared in a script/@src element inside HTML/JSP where script/@src
 * defines an absolute url. Ex :
 * 
 * <pre>
 * 	<script src="http://www.example.com/example.js" ></script>
 * </pre>
 */
public class DOMAbsoluteURLScriptResource implements ITernScriptResource {
	
	private final String url;

	public DOMAbsoluteURLScriptResource(String url) {
		this.url = url;
	}

	@Override
	public ITernFile getFile() {
		return null;
	}

	@Override
	public String getLabel() {
		return url;
	}

}
