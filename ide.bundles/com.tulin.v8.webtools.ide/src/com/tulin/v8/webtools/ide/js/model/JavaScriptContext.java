package com.tulin.v8.webtools.ide.js.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tulin.v8.webtools.ide.js.model.JavaScriptFunction.JavaScriptArgument;

public class JavaScriptContext {
	// child blocks
	protected List<JavaScriptContext> contextList = new ArrayList<JavaScriptContext>();

	// child elements
	protected Map<String, JavaScriptElement> elementMap = new LinkedHashMap<String, JavaScriptElement>();

	protected JavaScriptModel model;
	private JavaScriptContext parentContext;
	private JavaScriptElement targetElement;
	private JavaScriptElement returnElement;

	private int start;

	protected int end;

	public JavaScriptContext(JavaScriptModel model, JavaScriptContext parent, int start, int end) {
		this.model = model;
		this.parentContext = parent;
		this.start = start;
		this.end = end;
	}

	public void clear() {
		contextList.clear();
		elementMap.clear();
		parentContext = null;
		targetElement = null;
		returnElement = null;
		start = 0;
		end = 0;
	}

	public void addContext(JavaScriptContext context) {
		contextList.add(context);
		if (!model.isUpdate()) {
			model.setUpdate(true);
		}
	}

	public void addAllContext(JavaScriptContext[] contexts) {
		for (JavaScriptContext context : contexts) {
			contextList.add(context);
		}
		if (!model.isUpdate()) {
			model.setUpdate(true);
		}
	}

	public void addElement(JavaScriptElement element) {
		addElement(element, true, new HashSet<JavaScriptContext>());
	}

	private void addElement(JavaScriptElement element, boolean addToReturnElement, Set<JavaScriptContext> contextSet) {
		JavaScriptElement oldElement = elementMap.get(element.getName());
		if (oldElement != null) {
			if (oldElement.isTemporary()) {
				transferElements(oldElement, element, contextSet);
				elementMap.put(element.getName(), element);
			} else if (oldElement != element) {
				elementMap.put(element.getName(), element);
			}
		} else {
			elementMap.put(element.getName(), element);
		}

		elementMap.put(element.getName(), element);

		if (!model.isUpdate()) {
			model.setUpdate(true);
		}

		if (returnElement != null && addToReturnElement) {
			JavaScriptContext jsContext = returnElement.getContext();
			if (jsContext == null) {
				JavaScriptModel jsRoot = JavaScriptModelUtil.getRoot(this);
				String baseType = "Object:" + element.getName();
				String returnType = baseType;
				int count = 0;
				while (jsRoot.objectTypeMap.containsKey(returnType)) {
					count++;
					returnType = baseType + "_" + count;
				}
				returnElement.addTypes(new String[] { returnType });
				jsContext = new JavaScriptContext(this.model, this, 0, 0);
				returnElement.setContext(jsContext);
			}
			jsContext.addElement(element, false, contextSet);
		}
	}

	private void transferElements(JavaScriptElement fromElement, JavaScriptElement toElement,
			Set<JavaScriptContext> contextSet) {
		JavaScriptContext fromContext = fromElement.getContext();
		if (fromContext == null) {
			return;
		}

		JavaScriptContext toContext = toElement.getContext();
		if (contextSet.contains(toContext)) {
			return;
		} else {
			contextSet.add(toContext);
		}

		if (toContext == null) {
			toElement.setContext(fromContext);
		} else {
			for (JavaScriptElement child : fromContext.getElements()) {
				addChildElement(toElement, child, contextSet);
			}
		}
	}

	private void addChildElement(JavaScriptElement element, JavaScriptElement child,
			Set<JavaScriptContext> contextSet) {
		if (child instanceof JavaScriptPrototype) {
			JavaScriptPrototype prototype = element.createPrototype();
			for (JavaScriptElement gchild : child.getContext().getElements()) {
				addChildElement(prototype, gchild, contextSet);
			}
		} else {
			element.getContext().addElement(child);
		}
	}

	public JavaScriptContext getParent() {
		return parentContext;
	}

	public void setParent(JavaScriptContext parent) {
		this.parentContext = parent;
	}

	public JavaScriptContext getContextFromOffset(int offset) {
		if (offset < start || end < offset) {
			return null;
		}
		for (JavaScriptContext jsBlock : contextList) {
			JavaScriptContext block = jsBlock.getContextFromOffset(offset);
			if (block != null) {
				return block;
			}
		}
		return this;
	}

	private List<JavaScriptElement> getVisibleElementListFromModel(JavaScriptModel model) {
		List<JavaScriptElement> list = new ArrayList<JavaScriptElement>();
		for (JavaScriptModel jsModel : model.modelList) {
			list.addAll(jsModel.elementMap.values());
		}

		// global element
		for (JavaScriptModel libModel : model.modelList) {
			list.addAll(getElementListFromGlobal(model, libModel));
		}
		list.addAll(getElementListFromGlobal(model, model));

		return list;
	}

	private List<JavaScriptElement> getElementListFromGlobal(JavaScriptModel rootModel, JavaScriptModel libModel) {
		List<JavaScriptElement> list = new ArrayList<JavaScriptElement>();
		JavaScriptElement globalObj = libModel.getGlobalObject();
		if (globalObj != null) {
			JavaScriptContext globalContext = globalObj.getContext();
			for (JavaScriptElement jsElement : globalContext.getElements()) {
				list.add(jsElement);
			}
			for (String objectType : globalObj.getTypes()) {
				JavaScriptContext objTypeContext = rootModel.getObjectTypeContext(objectType);
				if (objTypeContext != null) {
					for (JavaScriptElement jsElement : objTypeContext.getElements()) {
						if (jsElement instanceof JavaScriptPrototype && jsElement.getContext() != null) {
							for (JavaScriptElement child : jsElement.getContext().getElements()) {
								if (!(child instanceof JavaScriptPrototype)) {
									list.add(child);
								}
							}
						} else if (!"arguments".equals(jsElement.getName())) {
							list.add(jsElement);
						}
					}
				}
			}
		}
		return list;
	}

	public JavaScriptElement[] getVisibleElements() {
		List<JavaScriptElement> list = new ArrayList<JavaScriptElement>();

		list.addAll(elementMap.values());
		if (this instanceof JavaScriptModel) {
			list.addAll(getVisibleElementListFromModel((JavaScriptModel) this));
		}

		JavaScriptContext parent = parentContext;
		while (parent != null) {
			list.addAll(parent.elementMap.values());
			if (parent instanceof JavaScriptModel) {
				list.addAll(getVisibleElementListFromModel((JavaScriptModel) parent));
			}
			parent = parent.getParent();
		}

		return list.toArray(new JavaScriptElement[list.size()]);
	}

	public JavaScriptElement[] getClassObjectElements() {
		List<JavaScriptElement> list = new ArrayList<JavaScriptElement>();

		boolean hasPrototype = false;
		for (JavaScriptElement element : elementMap.values()) {
			if (element instanceof JavaScriptPrototype) {
				for (JavaScriptElement child : element.getContext().getClassObjectElements()) {
					if (!(child instanceof JavaScriptPrototype)) {
						list.add(child);
					}
				}
				hasPrototype = true;
			}
			// TODO check if it's a private method
			list.add(element);
		}

		if (!hasPrototype) {
			// Object class
			JavaScriptModel jsModel = JavaScriptModelUtil.getRoot(this);
			if (jsModel != null && jsModel.objectTypeMap.containsKey("Object")) {
				for (JavaScriptElement jsElement : jsModel.objectTypeMap.get("Object").getElements()) {
					list.add(jsElement);
					if (jsElement instanceof JavaScriptPrototype && jsElement.getContext() != null) {
						for (JavaScriptElement child : jsElement.getContext().getElements()) {
							if (!(child instanceof JavaScriptPrototype)) {
								list.add(child);
							}
						}
					}
				}
			}
		}
		return list.toArray(new JavaScriptElement[list.size()]);
	}

	public JavaScriptElement getElementByName(String name) {
		return getElementByName(name, true);
	}

	public JavaScriptElement getElementByName(String name, boolean autoCreate) {
		if (name == null || "undefined".equals(name)) {
			return null;
		}
		String[] names = name.split("\\.");
		if (names.length == 0) {
			return null;
		}
		JavaScriptElement element = getElementByNameInternal(this, names, 0, autoCreate);
		if (element != null) {
			return element;
		}
		return null;
	}

	private JavaScriptElement getElementByNameInternal(JavaScriptContext context, String[] names, int depth,
			boolean autoCreate) {
		String str = names[depth];
		JavaScriptModel jsRoot = JavaScriptModelUtil.getRoot(context);
		for (JavaScriptElement element : context.getVisibleElements()) {
			if (str.length() > 0 && str.equals(element.getName())) {
				if (depth == names.length - 1) {
					return element;
				}

				for (String returnType : element.getReturnTypes()) {
					JavaScriptContext jsContext = jsRoot.getObjectTypeContext(returnType);
					if (jsContext != null) {
						JavaScriptElement childElement = getElementByNameInternal(jsContext, names, depth + 1,
								autoCreate);
						if (childElement != null) {
							return childElement;
						}
					}
				}
				if (element.getContext() != null) {
					JavaScriptElement childElement = getElementByNameInternal(element.getContext(), names, depth + 1,
							autoCreate);
					if (childElement != null) {
						return childElement;
					}
				}
				return null;
			} else if (element instanceof JavaScriptPrototype && element.getContext() != null
					&& element.getContext() != context) {
				JavaScriptElement childElement = getElementByNameInternal(element.getContext(), names, depth, false);
				if (childElement != null) {
					return childElement;
				}
			}
		}

		if (autoCreate) {
			if (model.getObjectTypeContext(str) != null) {
				return null;
			}

			// create dummy
			JavaScriptVariable jsVar = jsRoot.createTemporaryVariable(context, str);
			return jsVar;
		}
		return null;
	}

	public JavaScriptElement[] getElements() {
		return elementMap.values().toArray(new JavaScriptElement[elementMap.size()]);
	}

	public JavaScriptContext[] getContexts() {
		return contextList.toArray(new JavaScriptContext[contextList.size()]);
	}

	public JavaScriptElement getTarget() {
		return targetElement;
	}

	public void setTarget(JavaScriptElement targetElement) {
		this.targetElement = targetElement;
		if (targetElement != null && targetElement.getFunction() != null) {
			for (JavaScriptArgument jsArg : targetElement.getFunction().getArguments()) {
				if (jsArg.getJavaScriptElement() != null) {
					addElement(jsArg.getJavaScriptElement());
				} else if (jsArg.hasType("Function")) {
					JavaScriptFunction jsFunc = new JavaScriptFunction(jsArg.getName());
					jsFunc.setParent(targetElement);
					jsFunc.addReturnTypes(jsArg.getTypes());
					jsFunc.setStart(jsArg.getSourceStart());
					// TODO desc
					addElement(jsFunc);
				} else if (!"undefined".equals(jsArg.getName())) {
					JavaScriptVariable jsVar = new JavaScriptVariable(jsArg.getName(), jsArg.getTypes());
					jsVar.setParent(targetElement);
					jsVar.setStart(jsArg.getSourceStart());
					// TODO desc
					addElement(jsVar);
				}
			}
		}
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	/**
	 * @return the returnElement
	 */
	public JavaScriptElement getReturnElement() {
		return returnElement;
	}

	/**
	 * @param returnElement the returnElement to set
	 */
	public void setReturnElement(JavaScriptElement returnElement) {
		this.returnElement = returnElement;
	}

	public JavaScriptModel getModel() {
		return model;
	}
}
