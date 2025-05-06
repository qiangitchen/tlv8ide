package com.tulin.v8.webtools.ide.js.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.tulin.v8.webtools.ide.js.model.JavaScriptFunction.JavaScriptArgument;

/**
 * This is a variable element for JavaScript.
 */
public class JavaScriptVariable extends JavaScriptElement {
	protected JavaScriptFunction function;
	private boolean isProperty = false;
	private boolean isMethod = false;

	public JavaScriptVariable(String name, List<String> typeList) {
		setName(name);
		this.typeList = typeList;
	}

	public JavaScriptVariable(String name, String[] types) {
		setName(name);
		this.typeList = new ArrayList<String>();
		for (String type : types) {
			typeList.add(type);
		}
	}

	public JavaScriptFunction getFunction() {
		return function;
	}

	public void setFunction(JavaScriptFunction function) {
		this.function = function;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		if (getTypeList().size() == 1 && "*".equals(getTypeList().get(0))) {
			buf.append("Any");
		} else if (getTypeList().size() == 0) {
			buf.append("Any");
		} else {
			for (String returnType : getTypeList()) {
				if (buf.length() > 0) {
					buf.append('|');
				}
				buf.append(returnType);
			}
		}
		buf.append(' ').append(getName());
		return buf.toString();
	}

	public boolean isProperty() {
		return isProperty;
	}

	public void setProperty(boolean isProperty) {
		this.isProperty = isProperty;
	}

	public boolean isMethod() {
		return isMethod;
	}

	public void setMethod(boolean isMethod) {
		this.isMethod = isMethod;
	}

	@Override
	public String[] getReturnTypes() {
		return getTypes();
	}

	@Override
	public boolean hasReturnType(Set<String> returnTypeSet) {
		for (String type : returnTypeSet) {
			if (getTypeList().contains(type)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDisplayString() {
		if (getFunction() == null) {
			return super.getDisplayString();
		}

		StringBuilder buf = new StringBuilder();
		buf.append(getName());
		buf.append('(');
		boolean isFirst = true;
		for (JavaScriptArgument arg : getFunction().getArguments()) {
			if (!isFirst) {
				buf.append(", ");
			} else {
				isFirst ^= true;
			}
			buf.append(arg.name);
		}
		buf.append(')');
		return buf.toString();
	}

	@Override
	public String getReplaceString() {
		if (getFunction() == null) {
			return super.getReplaceString();
		}

		StringBuilder buf = new StringBuilder();
		buf.append(getName().replaceAll("\\$", "\\$\\$"));
		buf.append('(');
		boolean isFirst = true;
		for (JavaScriptArgument arg : getFunction().getArguments()) {
			if (!isFirst) {
				buf.append(", ");
			} else {
				isFirst ^= true;
			}
			buf.append("${");
			buf.append(arg.name);
			buf.append('}');
		}
		buf.append(')');
		return buf.toString();
	}

	public void setContext(JavaScriptContext block) {
		this.context = block;
	}

}
