package tern.server;

import tern.metadata.TernModuleMetadata;

/**
 * Basic JSON Type Definition.
 * 
 *
 */
public class BasicTernDef extends AbstractBasicTernModule implements ITernDef {

	public BasicTernDef(TernModuleInfo info, TernModuleMetadata metadata) {
		super(info, ModuleType.Def, metadata);
	}

}
