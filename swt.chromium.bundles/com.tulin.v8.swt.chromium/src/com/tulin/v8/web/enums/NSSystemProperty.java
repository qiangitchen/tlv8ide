package com.tulin.v8.web.enums;

public enum NSSystemProperty {
	LOCALHOSTADDRESS("nativeswing.localhostAddress", Type.READ_WRITE),

	LOCALHOSTADDRESS_DEBUG_PRINTDETECTION("nativeswing.localhostAddress.debug.printDetection", Type.READ_WRITE),

	LOCALHOSTADDRESS_DEBUG_PRINT("nativeswing.localhostAddress.debug.print", Type.READ_WRITE),

	WEBSERVER_DEBUG_PRINTPORT("nativeswing.webserver.debug.printPort", Type.READ_WRITE),

	WEBSERVER_DEBUG_PRINTREQUESTS("nativeswing.webserver.debug.printRequests", Type.READ_WRITE),

	WEBSERVER_DEBUG_PRINTDATA("nativeswing.webserver.debug.printData", Type.READ_WRITE),

	WEBSERVER_ACTIVATEOLDRESOURCEMETHOD("nativeswing.webserver.activateOldResourceMethod", Type.READ_WRITE),

	COMPONENTS_DEBUG_PRINTOPTIONS("nativeswing.components.debug.printOptions", Type.READ_WRITE),

	COMPONENTS_DEBUG_PRINTSHAPECOMPUTING("nativeswing.components.debug.printShapeComputing", Type.READ_WRITE),

	COMPONENTS_FORCESINGLERECTANGLESHAPES("nativeswing.components.forceSingleRectangleShapes", Type.READ_WRITE),

	INTEGRATION_ACTIVE("nativeswing.integration.active", Type.READ_WRITE),

	DEPENDENCIES_CHECKVERSIONS("nativeswing.dependencies.checkVersions", Type.READ_WRITE),

	JNA_FORCE_HW_POPUP("jna.force_hw_popups", Type.READ_WRITE),
	DEPLOYMENT_TYPE("nativeswing.deployment.type", Type.READ_WRITE),
	INTEGRATION_USEDEFAULTCLIPPING("nativeswing.integration.useDefaultClipping", Type.READ_WRITE);

	private final String _name;
	private final boolean _readOnly;

	private NSSystemProperty(String name) {
		this(name, Type.READ_ONLY);
	}

	private NSSystemProperty(String name, Type type) {
		if (name == null) {
			throw new NullPointerException("name");
		}
		name = name.trim();
		if ("".equals(name)) {
			throw new IllegalArgumentException();
		}

		this._name = name;
		this._readOnly = (type == Type.READ_ONLY);
	}

	public String get() {
		return get(null);
	}

	public String get(String defaultValue) {
		return System.getProperty(_name, defaultValue);
	}

	public String set(String value) {
		if (isReadOnly()) {
			throw new UnsupportedOperationException(getName() + " is a read-only property");
		}
		return System.setProperty(_name, value);
	}

	public String getName() {
		return this._name;
	}

	public boolean isReadOnly() {
		return this._readOnly;
	}

	public String toString() {
		return get();
	}

	public String toDebugString() {
		StringBuilder buf = new StringBuilder();
		buf.append(name()).append(": ");
		buf.append(getName()).append("=");
		buf.append(get());
		if (isReadOnly()) {
			buf.append(" (read-only)");
		}
		return buf.toString();
	}

	private static enum Type {
		READ_WRITE, READ_ONLY;
	}
}
