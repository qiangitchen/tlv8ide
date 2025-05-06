package com.tulin.v8.webtools.ide.js.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JavaScriptFunction extends JavaScriptElement {
	private List<JavaScriptArgument> argumentList = new ArrayList<JavaScriptArgument>();
	protected List<String> returnTypeList = new ArrayList<String>();
	private boolean isClass = false;
	private boolean isAnonymous = false;

	public JavaScriptFunction(String name) {
		setName(name);
		if (name == null || name.trim().length() == 0) {
			isAnonymous = true;
		}
		typeList.add("Function");
	}

	@Override
	public void setName(String name) {
		super.setName(name);
		if (name == null || name.trim().length() == 0) {
			isAnonymous = true;
		} else {
			isAnonymous = false;
		}
	}

	public JavaScriptArgument[] getArguments() {
		return argumentList.toArray(new JavaScriptArgument[argumentList.size()]);
	}

	public String getDisplayString() {
		StringBuilder buf = new StringBuilder();
		buf.append(name);
		buf.append('(');
		boolean isFirst = true;
		for (JavaScriptArgument arg : argumentList) {
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

	public String getReplaceString() {
		StringBuilder buf = new StringBuilder();
		buf.append(name.replaceAll("\\$", "\\$\\$"));
		buf.append('(');
		boolean isFirst = true;
		for (JavaScriptArgument arg : argumentList) {
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

	public void addArgument(JavaScriptArgument arg) {
		argumentList.add(arg);
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		if (returnTypeList.size() == 1 && "*".equals(returnTypeList.get(0))) {
			buf.append("Any");
		} else if (returnTypeList.size() == 0) {
			buf.append("Any");
		} else {
			for (String returnType : returnTypeList) {
				if (buf.length() > 0) {
					buf.append('|');
				}
				buf.append(returnType);
			}
		}
		buf.append(' ').append(getDisplayString());
		return buf.toString();
	}

	public static class JavaScriptArgument {
		String name;
		List<String> typeList = new ArrayList<String>();
		JavaScriptElement jsElement = null;
		int sourceStart;

		public JavaScriptArgument(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String[] getTypes() {
			return typeList.toArray(new String[typeList.size()]);
		}

		public void addType(String type) {
			typeList.add(type);
		}

		public boolean hasType(String type) {
			return typeList.contains(type);
		}

		public JavaScriptElement getJavaScriptElement() {
			return jsElement;
		}

		public void setJavaScriptElement(JavaScriptElement jsElement) {
			this.jsElement = jsElement;
		}

		public String toString() {
			StringBuilder buf = new StringBuilder();
			if (typeList.size() == 1 && "*".equals(typeList.get(0))) {
				buf.append("Any");
			} else if (typeList.size() == 0) {
				buf.append("Any");
			} else {
				for (String t : typeList) {
					if (buf.length() > 0) {
						buf.append('|');
					}
					buf.append(t);
				}
			}
			buf.append(' ');
			buf.append(name);
			return buf.toString();
		}

		public int getSourceStart() {
			return sourceStart;
		}

		public void setSourceStart(int sourceStart) {
			this.sourceStart = sourceStart;
		}
	}

	public JavaScriptFunction getFunction() {
		return this;
	}

	public boolean isClass() {
		return isClass;
	}

	public void setClass(boolean isClass) {
		this.isClass = isClass;
	}

	public boolean isAnonymous() {
		return isAnonymous;
	}

	public String[] getReturnTypes() {
		return returnTypeList.toArray(new String[returnTypeList.size()]);
	}

	public void addReturnType(String returnType) {
		if (returnType != null && !returnTypeList.contains(returnType)) {
			returnTypeList.add(returnType);
		}
	}

	public void addReturnTypes(String[] returnTypes) {
		for (String returnType : returnTypes) {
			if (returnType != null && !returnTypeList.contains(returnType)) {
				returnTypeList.add(returnType);
			}
		}
	}

	public boolean hasReturnType(String returnType) {
		return returnTypeList.contains(returnType);
	}

	public boolean hasReturnType(Set<String> returnTypeSet) {
		for (String type : returnTypeSet) {
			if (hasReturnType(type)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasStaticProperties() {
		if (context.elementMap.isEmpty()) {
			return false;
		}
		for (JavaScriptElement jsElement : context.elementMap.values()) {
			if (!"prototype".equals(jsElement.getName()) && !"arguments".equals(jsElement.getName())) {
				return true;
			}
		}
		return false;
	}
}
