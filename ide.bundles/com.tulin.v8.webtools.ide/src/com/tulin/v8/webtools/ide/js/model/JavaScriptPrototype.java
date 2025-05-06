package com.tulin.v8.webtools.ide.js.model;

import java.util.Set;

public class JavaScriptPrototype extends JavaScriptElement {
	private JavaScriptFunction function;

	public JavaScriptPrototype(JavaScriptElement parent) {
		this.parent = parent;
		setName("prototype");
		JavaScriptContext context = parent.getContext();
		context.addElement(this);

		JavaScriptModel root = JavaScriptModelUtil.getRoot(context);

		JavaScriptContext block = new JavaScriptContext(context.model, context, 0, 0);
		setContext(block);

		String baseType = "Object:prototype:" + parent.getName();
		String returnType = baseType;
		int count = 0;
		while (root.objectTypeMap.containsKey(returnType)) {
			count++;
			returnType = baseType + "_" + count;
		}
		root.objectTypeMap.put(returnType, block);
		typeList.add(returnType);
	}

	public void update(JavaScriptElement jsElement) {
		JavaScriptPrototype prototype = null;
		for (JavaScriptElement element : getContext().getElements()) {
			if (element instanceof JavaScriptPrototype) {
				prototype = (JavaScriptPrototype) element;
				break;
			}
		}

		if (prototype == null) {
			prototype = new JavaScriptPrototype(this);
		}

		JavaScriptModel root = JavaScriptModelUtil.getRoot(getContext());

		if (jsElement.getFunction() != null) {
			prototype.function = jsElement.getFunction();
		} else {
			prototype.function = null;
		}
		prototype.setContext(jsElement.getContext());

		for (String type : prototype.typeList) {
			if (type.startsWith("Object:prototype:")) {
				root.objectTypeMap.remove(type);
			}
		}
		prototype.typeList.clear();
		if (prototype.function != null && prototype.function.isClass()) {
			prototype.typeList.add(prototype.function.getName());
		} else {
			for (String type : jsElement.getTypes()) {
				prototype.typeList.add(type);
			}
		}
	}

	@Override
	public JavaScriptFunction getFunction() {
		return function;
	}

	public void setFunction(JavaScriptFunction function) {
		this.function = function;
	}

	@Override
	public String[] getReturnTypes() {
		return getTypes();
	}

	@Override
	public boolean hasReturnType(Set<String> returnTypeSet) {
		return hasType(returnTypeSet);
	}

}
