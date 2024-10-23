package tern.server;

import tern.metadata.TernModuleMetadata;
import tern.utils.StringUtils;

/**
 * Abstract class for basic tern module.
 * 
 */
public abstract class AbstractBasicTernModule extends TernModuleInfo implements ITernModule {

	private final ModuleType moduleType;
	private final TernModuleMetadata metadata;

	public AbstractBasicTernModule(TernModuleInfo info, ModuleType moduleType, TernModuleMetadata metadata) {
		super(info);
		this.moduleType = moduleType;
		this.metadata = metadata;
	}

	@Override
	public ModuleType getModuleType() {
		return moduleType;
	}

	@Override
	public TernModuleMetadata getMetadata() {
		return metadata;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public String getOrigin() {
		String origin = null;
		TernModuleMetadata metadata = getMetadata();
		if (metadata != null) {
			origin = metadata.getOrigin();
		}
		return !StringUtils.isEmpty(origin) ? origin : getName();
	}
}
