package com.tulin.v8.webtools.ide.js.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * An alias class for JavaScriptVariable.
 */
public class JavaScriptAlias extends JavaScriptVariable {
	private JavaScriptVariable jsVar;

	public JavaScriptAlias(String name, JavaScriptVariable jsVar) {
		super(name, new ArrayList<String>());
		this.name = name;
		this.jsVar = jsVar;
	}

	public JavaScriptFunction getFunction() {
		return jsVar.getFunction();
	}

	public void setFunction(JavaScriptFunction function) {
		jsVar.setFunction(function);
	}

	public JavaScriptContext getContext() {
		return jsVar.getContext();
	}

	public String[] getTypes() {
		return jsVar.getTypes();
	}

	public String[] getReturnTypes() {
		return jsVar.getReturnTypes();
	}

	public void addTypes(String[] types) {
		jsVar.addTypes(types);
	}

	public boolean hasReturnType(Set<String> returnTypeSet) {
		return jsVar.hasReturnType(returnTypeSet);
	}

	public boolean hasType(Set<String> typeSet) {
		return jsVar.hasType(typeSet);
	}

	public JavaScriptPrototype createPrototype() {
		return jsVar.createPrototype();
	}

	public void setContext(JavaScriptContext block) {
		jsVar.setContext(block);
	}

	protected List<String> getTypeList() {
		return jsVar.getTypeList();
	}
}
