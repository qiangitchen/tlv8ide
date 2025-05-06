package com.tulin.v8.web.common;

public enum SystemProperty {
	COM_IBM_VM_BITMODE("com.ibm.vm.bitmode"),

	FILE_ENCODING("file.encoding"), FILE_ENCODING_PKG("file.encoding.pkg"), FILE_SEPARATOR("file.separator"),

	JAVA_AWT_GRAPHICSENV("java.awt.graphicsenv"), JAVA_AWT_PRINTERJOB("java.awt.printerjob"),
	JAVA_AWT_SMARTINVALIDATE("java.awt.smartInvalidate"),

	JAVA_CLASS_PATH("java.class.path"), JAVA_CLASS_VERSION("java.class.version"),

	JAVA_ENDORSED_DIRS("java.endorsed.dirs"), JAVA_EXT_DIRS("java.ext.dirs"),

	JAVA_HOME("java.home"),

	JAVA_IO_TMPDIR("java.io.tmpdir", Type.READ_WRITE),

	JAVA_LIBRARY_PATH("java.library.path"), JAVA_RUNTIME_NAME("java.runtime.name"),
	JAVA_RUNTIME_VERSION("java.runtime.version"),

	JAVA_SPECIFICATION_NAME("java.specification.name"), JAVA_SPECIFICATION_VENDOR("java.specification.vendor"),
	JAVA_SPECIFICATION_VERSION("java.specification.version"),

	JAVA_VERSION("java.version"), JAVA_VENDOR("java.vendor"), JAVA_VENDOR_URL("java.vendor.url"),
	JAVA_VENDOR_URL_BUG("java.vendor.url.bug"),

	JAVAWEBSTART_VERSION("javawebstart.version"),

	JAVA_VM_INFO("java.vm.info"), JAVA_VM_NAME("java.vm.name"),
	JAVA_VM_SPECIFICATION_NAME("java.vm.specification.name"),
	JAVA_VM_SPECIFICATION_VENDOR("java.vm.specification.vendor"),
	JAVA_VM_SPECIFICATION_VERSION("java.vm.specification.version"), JAVA_VM_VERSION("java.vm.version"),
	JAVA_VM_VENDOR("java.vm.vendor"),

	LINE_SEPARATOR("line.separator"),

	OS_NAME("os.name"), OS_ARCH("os.arch"), OS_VERSION("os.version"),

	PATH_SEPARATOR("path.separator"),

	SUN_ARCH_DATA_MODEL("sun.arch.data.model"), SUN_BOOT_CLASS_PATH("sun.boot.class.path"),
	SUN_BOOT_LIBRARY_PATH("sun.boot.library.path"), SUN_CPU_ENDIAN("sun.cpu.endian"),
	SUN_CPU_ISALIST("sun.cpu.isalist"),

	SUN_IO_UNICODE_ENCODING("sun.io.unicode.encoding"), SUN_JAVA_LAUNCHER("sun.java.launcher"),
	SUN_JNU_ENCODING("sun.jnu.encoding"), SUN_MANAGEMENT_COMPILER("sun.management.compiler"),
	SUN_OS_PATCH_LEVEL("sun.os.patch.level"),

	USER_COUNTRY("user.country"), USER_DIR("user.dir"), USER_HOME("user.home"), USER_LANGUAGE("user.language"),
	USER_NAME("user.name"), USER_TIMEZONE("user.timezone"),

	SUN_AWT_DISABLEMIXING("sun.awt.disableMixing", Type.READ_WRITE),
	SUN_AWT_NOERASEBACKGROUND("sun.awt.noerasebackground", Type.READ_WRITE),
	SUN_AWT_XEMBEDSERVER("sun.awt.xembedserver", Type.READ_WRITE),

	SUN_DESKTOP("sun.desktop"),

	AWT_NATIVE_DOUBLE_BUFFERING("awt.nativeDoubleBuffering"),

	AWT_TOOLKIT("awt.toolkit"),

	FTP_NON_PROXY_HOSTS("ftp.nonProxyHosts"),

	GOPHER_PROXY_SET("gopherProxySet"),

	HTTP_NON_PROXY_HOSTS("http.nonProxyHosts"),

	MRJ_VERSION("mrj.version"),

	SOCKS_NON_PROXY_HOSTS("socksNonProxyHosts");

	private final String _name;
	private final boolean _readOnly;

	private SystemProperty(String name) {
		this(name, Type.READ_ONLY);
	}

	private SystemProperty(String name, Type type) {
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
