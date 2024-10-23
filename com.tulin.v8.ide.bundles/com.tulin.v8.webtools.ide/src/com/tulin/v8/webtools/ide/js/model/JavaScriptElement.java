package com.tulin.v8.webtools.ide.js.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;

public abstract class JavaScriptElement {
	protected List<String> typeList = new ArrayList<String>();
	protected String name;
	protected JavaScriptContext context;
	protected String description;
	protected int start = -1;
	protected JavaScriptPrototype prototype;
	protected boolean isPrivate = false;
	protected boolean isStatic = false;
	protected JavaScriptElement parent;
	protected boolean isTemporary = false;
	protected boolean isUndefined = false;

	public abstract JavaScriptFunction getFunction();

	public abstract String[] getReturnTypes();

	public abstract boolean hasReturnType(Set<String> returnTypeSet);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JavaScriptContext getContext() {
		return context;
	}

	public void setContext(JavaScriptContext block) {
		this.context = block;
	}

	public String getDisplayString() {
		return name;
	}

	public String getReplaceString() {
		return name.replaceAll("\\$", "\\$\\$");
	}

	public String toString() {
		return getDisplayString();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String[] getTypes() {
		return getTypeList().toArray(new String[getTypeList().size()]);
	}

	public void addTypes(String[] types) {
		for (String type : types) {
			if (type != null && !getTypeList().contains(type)) {
				getTypeList().add(type);
			}
		}
	}

	public boolean hasType(Set<String> typeSet) {
		for (String type : typeSet) {
			if (getTypeList().contains(type)) {
				return true;
			}
		}
		return false;
	}

	public JavaScriptPrototype createPrototype() {
		if (prototype != null) {
			return prototype;
		}

		if (context != null) {
			prototype = new JavaScriptPrototype(this);
		}
		return prototype;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public JavaScriptElement getParent() {
		return parent;
	}

	public void setParent(JavaScriptElement parent) {
		this.parent = parent;
	}

	/**
	 * @return the isStatic
	 */
	public boolean isStatic() {
		return isStatic;
	}

	/**
	 * @param isStatic
	 *            the isStatic to set
	 */
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	protected List<String> getTypeList() {
		return typeList;
	}

	/**
	 * @return the isTemporary
	 */
	public boolean isTemporary() {
		return isTemporary;
	}

	/**
	 * @param isTemporary
	 *            the isTemporary to set
	 */
	public void setTemporary(boolean isTemporary) {
		this.isTemporary = isTemporary;
	}

	public String getFromSource() {
		if (getContext() != null) {
			JavaScriptModel model = getContext().getModel();
			if (model != null) {
				Object file = model.getJavaScriptFile();
				if (file instanceof IFile) {
					return ((IFile) file).getName();
				} else if (file instanceof File) {
					return ((File) file).getName();
				}
			}
		}
		return null;
	}

	public boolean isUndefined() {
		return isUndefined;
	}

	public void setUndefined(boolean isUndefined) {
		this.isUndefined = isUndefined;
	}
}
