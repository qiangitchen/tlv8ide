package tern.server;

import tern.metadata.TernModuleMetadata;

/**
 * Tern plugin.
 *
 */
public enum TernPlugin implements ITernPlugin {

	aui15("aui1.5.x"),
	aui2("aui2.0.x"), 
	yui3("yui3"),
	
	angular1("angular1"), 
	vue("vue"), 
	browser_extension("browser-extension"), 
	chrome_apps("chrome-apps"), 
	ckeditor_4_4_x("ckeditor4.4.x"), 
	closure("closure"), 
	cordovajs("cordovajs"), 
	doc_comment("doc_comment"), 
	
	dojotoolkit_1_6("dojotoolkit1.8"), 	
	dojotoolkit_1_8("dojotoolkit1.8"),
	dojotoolkit_1_9("dojotoolkit1.9"),
	dojotoolkit_1_10("dojotoolkit1.10"),
	
	es_modules("es_modules"), 
	
	extjs_4_2_1("extjs_4.2.1"), 
	extjs_5_0_0("extjs_5.0.0"), 
	extjs_5_1_1("extjs_5.1.1"),
	
	grunt("grunt"), 
	gulp("gulp"), 
	
	gas("gas"), 
	liferay("liferay"), 
	node_express("node-express"),
	node_mongodb_native("node-mongodb-native"),
	node_mongoose("node-mongoose"),
	node("node"), 
	tabris("tabris"), 
	meteor("meteor"), 
	
	qooxdoo_4_1("qooxdoo4.1"),
	requirejs("requirejs"), 

	// tern linter
	lint("lint", true),
	jshint("jshint", true),
	jscs("jscs", true),
	eslint("eslint", true),

	// other
	guess_types("guess-types"),
	outline("outline"),
	push("push");
	
	private final String name;
	private final String type;
	private final String version;
	private final boolean linter;

	private TernPlugin(String name) {
		this(name, false);
	}
	
	private TernPlugin(String name, boolean linter) {
		TernModuleInfo info = new TernModuleInfo(name);
		this.name = info.getName();
		this.type = info.getType();
		this.version = info.getVersion();
		this.linter = linter;
	}
	
	@Override
	public String getName() {
		return name;
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
		return ModuleType.Plugin;
	}

	@Override
	public boolean isLinter() {
		return linter;
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
