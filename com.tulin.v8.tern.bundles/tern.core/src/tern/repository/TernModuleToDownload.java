package tern.repository;

import com.eclipsesource.json.JsonObject;

import tern.metadata.TernModuleMetadata;
import tern.server.AbstractBasicTernModule;
import tern.server.ModuleType;
import tern.server.TernModuleInfo;

public class TernModuleToDownload extends AbstractBasicTernModule {

	public TernModuleToDownload(String name, JsonObject module) {
		super(new TernModuleInfo(name), getModuleType(module), new TernModuleMetadata(module, null));
	}

	private static ModuleType getModuleType(JsonObject module) {
		// TODO : retrieve module type
		return ModuleType.Plugin;
	}
}
