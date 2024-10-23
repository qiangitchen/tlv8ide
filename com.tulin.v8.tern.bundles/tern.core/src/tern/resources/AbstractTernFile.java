package tern.resources;

import java.io.IOException;

import tern.ITernFile;
import tern.ITernProject;
import tern.internal.resources.InternalTernResourcesManager;
import tern.server.TernPlugin;
import tern.server.protocol.TernFile;
import tern.server.protocol.html.ScriptTagRegion;

public abstract class AbstractTernFile implements ITernFile {

	@Override
	public ScriptTagRegion[] getScriptTags(ITernProject context) {
		if (context.hasPlugin(TernPlugin.browser_extension)) {
			return null;
		}
		return InternalTernResourcesManager.getInstance().getScriptTagRegions(this);
	}

	@Override
	public TernFile toTernServerFile(ITernProject context) throws IOException {
		return new TernFile(getFullName(context), getContents(), 
				getScriptTags(context), null);
	}
	
}
