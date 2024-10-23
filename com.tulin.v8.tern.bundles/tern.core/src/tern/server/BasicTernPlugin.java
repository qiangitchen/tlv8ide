package tern.server;

import tern.metadata.TernModuleMetadata;

/**
 * Basic tern plugin.
 *
 */
public class BasicTernPlugin extends AbstractBasicTernModule implements ITernPlugin {

	public BasicTernPlugin(TernModuleInfo info, TernModuleMetadata metadata) {
		super(info, ModuleType.Plugin, metadata);
	}

	@Override
	public boolean isLinter() {
		TernModuleMetadata metadata = getMetadata();
		return metadata != null ? metadata.isLinter() : false;
	}

}
