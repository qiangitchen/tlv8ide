package tern.server;

import tern.metadata.TernModuleMetadata;

public enum TernDef implements ITernDef {

	browser("browser"),
	
	chai("chai"), 
	ecmascript("ecmascript"), 
	
	jquery("jquery"), 
	vue("vue"), 
	underscore("underscore"),
	titanium("titanium.json");

	private final String name;
	private final String type;
	private final String version;

	private TernDef(String name) {
		TernModuleInfo info = new TernModuleInfo(name);
		this.name = info.getName();
		this.type = info.getType();
		this.version = info.getVersion();
	}

	@Override
	public String getName() {
		return name != null ? name : name();
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public ModuleType getModuleType() {
		return ModuleType.Def;
	}

	@Override
	public String getOrigin() {
		return getName();
	}

	@Override
	public TernModuleMetadata getMetadata() {
		throw new UnsupportedOperationException();
	}

}
