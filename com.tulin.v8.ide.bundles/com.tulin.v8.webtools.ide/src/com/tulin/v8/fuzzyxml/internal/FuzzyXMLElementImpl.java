package com.tulin.v8.fuzzyxml.internal;

import java.util.ArrayList;
import java.util.List;

import com.tulin.v8.fuzzyxml.FuzzyXMLAttribute;
import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.fuzzyxml.FuzzyXMLException;
import com.tulin.v8.fuzzyxml.FuzzyXMLNode;
import com.tulin.v8.fuzzyxml.FuzzyXMLParser;
import com.tulin.v8.fuzzyxml.FuzzyXMLText;

public class FuzzyXMLElementImpl extends AbstractFuzzyXMLNode implements FuzzyXMLElement {
	private List<FuzzyXMLNode> children = new ArrayList<>();
	private List<FuzzyXMLAttribute> attributes = new ArrayList<>();
	private String name;
	// private HashMap namespace = new HashMap();

	public FuzzyXMLElementImpl(String name) {
		this(null, name, -1, -1);
	}

	public FuzzyXMLElementImpl(FuzzyXMLNode parent, String name, int offset, int length) {
		super(parent, offset, length);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void appendChildrenFromText(String text) {
		if (text.length() == 0) {
			return;
		}
		FuzzyXMLElement test = new FuzzyXMLElementImpl("test");
		appendChild(test);
		int offset = test.getOffset();
		removeChild(test);

		String parseText = "<root>" + text + "</root>";

		FuzzyXMLElement root = new FuzzyXMLParser().parse(parseText).getDocumentElement();
		((AbstractFuzzyXMLNode) root).appendOffset(root, 0, -6);
		((AbstractFuzzyXMLNode) root).appendOffset(root, 0, offset);
		FuzzyXMLNode[] nodes = ((FuzzyXMLElement) root.getChildren()[0]).getChildren();

		appendOffset(this, offset, text.length());

		for (int i = 0; i < nodes.length; i++) {
			appendChild(nodes[i], false, false);
		}

		fireModifyEvent(text, offset, 0);
	}

	public void appendChild(FuzzyXMLNode node) {
		appendChild(node, true, true);
	}

	public void appendChildWithNoCheck(FuzzyXMLNode node) {
		appendChild(node, true, false);
	}

	private void appendChild(FuzzyXMLNode node, boolean fireEvent, boolean check) {
		if (check) {
			if (((AbstractFuzzyXMLNode) node).getDocument() != null) {
				throw new FuzzyXMLException("Appended node already has a parent.");
			}

			if (node instanceof FuzzyXMLElement) {
				if (((FuzzyXMLElement) node).getChildren().length != 0) {
					throw new FuzzyXMLException("Appended node has chidlren.");
				}
			}
		}

		AbstractFuzzyXMLNode nodeImpl = (AbstractFuzzyXMLNode) node;
		nodeImpl.setParentNode(this);
		nodeImpl.setDocument(getDocument());
		if (node instanceof FuzzyXMLAttribute) {
			setAttribute((FuzzyXMLAttribute) node);
		} else {
			if (children.contains(node)) {
				return;
			}
			if (getDocument() == null) {
				children.add(node);
				return;
			}
			FuzzyXMLNode[] nodes = getChildren();
			int offset = 0;
			if (nodes.length == 0) {
				int length = getLength();
				FuzzyXMLAttribute[] attrs = getAttributes();
				offset = getOffset() + getName().length();
				for (int i = 0; i < attrs.length; i++) {
					offset = offset + attrs[i].toXMLString().length();
				}
				offset = offset + 2;

				nodeImpl.setOffset(offset);
				if (fireEvent) {
					nodeImpl.setLength(node.toXMLString().length());
				}

				children.add(node);
				String xml = toXMLString();
				children.remove(node);

				if (fireEvent) {
					fireModifyEvent(xml, getOffset(), getLength());
					appendOffset(this, offset, xml.length() - length);
				}

				children.add(node);

			} else {
				for (int i = 0; i < nodes.length; i++) {
					offset = nodes[i].getOffset() + nodes[i].getLength();
				}
				if (fireEvent) {
					fireModifyEvent(nodeImpl.toXMLString(), offset, 0);
					appendOffset(this, offset, node.toXMLString().length());
				}

				nodeImpl.setOffset(offset);
				if (fireEvent) {
					nodeImpl.setLength(node.toXMLString().length());
				}

				children.add(node);
			}
		}
	}

	public FuzzyXMLAttribute[] getAttributes() {
		return (FuzzyXMLAttribute[]) attributes.toArray(new FuzzyXMLAttribute[attributes.size()]);
	}

	public FuzzyXMLNode[] getChildren() {
		return (FuzzyXMLNode[]) children.toArray(new FuzzyXMLNode[children.size()]);
	}

	public boolean hasChildren() {
		if (children.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public void insertAfter(FuzzyXMLNode newChild, FuzzyXMLNode refChild) {
		if (newChild instanceof FuzzyXMLAttribute || refChild instanceof FuzzyXMLAttribute) {
			return;
		}
		FuzzyXMLNode[] children = getChildren();
		FuzzyXMLNode targetNode = null;
		boolean flag = false;
		for (int i = 0; i < children.length; i++) {
			if (flag) {
				targetNode = children[i];
			}
			if (children[i] == refChild) {
				flag = true;
			}
		}
		if (targetNode == null && flag) {
			appendChild(newChild);
		} else {
			insertBefore(newChild, targetNode);
		}
	}

	public void insertBefore(FuzzyXMLNode newChild, FuzzyXMLNode refChild) {
		if (newChild instanceof FuzzyXMLAttribute || refChild instanceof FuzzyXMLAttribute) {
			return;
		}
		FuzzyXMLNode target = null;
		int index = -1;
		FuzzyXMLNode[] children = getChildren();
		for (int i = 0; i < children.length; i++) {
			if (children[i] == refChild) {
				target = children[i];
				index = i;
				break;
			}
		}
		if (target == null) {
			return;
		}
		int offset = target.getOffset();
		fireModifyEvent(newChild.toXMLString(), offset, 0);

		AbstractFuzzyXMLNode nodeImpl = (AbstractFuzzyXMLNode) newChild;
		nodeImpl.setParentNode(this);
		nodeImpl.setDocument(getDocument());
		nodeImpl.setOffset(offset);
		nodeImpl.setLength(newChild.toXMLString().length());

		appendOffset(this, offset, nodeImpl.toXMLString().length());

		this.children.add(index, nodeImpl);
	}

	public void replaceChild(FuzzyXMLNode newChild, FuzzyXMLNode refChild) {
		if (newChild instanceof FuzzyXMLAttribute || refChild instanceof FuzzyXMLAttribute) {
			return;
		}
		int index = -1;
		for (int i = 0; i < children.size(); i++) {
			if (refChild == children.get(i)) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			return;
		}
		children.remove(index);

		AbstractFuzzyXMLNode nodeImpl = (AbstractFuzzyXMLNode) newChild;
		nodeImpl.setParentNode(this);
		nodeImpl.setDocument(getDocument());
		nodeImpl.setOffset(refChild.getOffset());
		nodeImpl.setLength(newChild.toXMLString().length());

		fireModifyEvent(newChild.toXMLString(), refChild.getOffset(), refChild.getLength());
		appendOffset(this, refChild.getOffset(), newChild.getLength() - refChild.getLength());

		children.add(index, newChild);
	}

	public void removeChild(FuzzyXMLNode oldChild) {
		if (oldChild instanceof FuzzyXMLAttribute) {
			removeAttributeNode((FuzzyXMLAttribute) oldChild);
			return;
		}
		if (children.contains(oldChild)) {
			((AbstractFuzzyXMLNode) oldChild).setParentNode(null);
			((AbstractFuzzyXMLNode) oldChild).setDocument(null);
			children.remove(oldChild);
			fireModifyEvent("", oldChild.getOffset(), oldChild.getLength());
			appendOffset(this, oldChild.getOffset(), oldChild.getLength() * -1);
		}
	}

	public void setAttribute(FuzzyXMLAttribute attr) {
		FuzzyXMLAttribute attrNode = getAttributeNode(attr.getName());
		if (attrNode == null) {
			if (attributes.contains(attr)) {
				return;
			}
			if (getDocument() == null) {
				attributes.add(attr);
				return;
			}
			FuzzyXMLAttributeImpl attrImpl = (FuzzyXMLAttributeImpl) attr;
			attrImpl.setDocument(getDocument());
			attrImpl.setParentNode(this);
			FuzzyXMLAttribute[] attrs = getAttributes();
			int offset = getOffset() + getName().length() + 1;
			for (int i = 0; i < attrs.length; i++) {
				offset = offset + attrs[i].toXMLString().length();
			}
			fireModifyEvent(attr.toXMLString(), offset, 0);
			appendOffset(this, offset, attr.toXMLString().length());
			attrImpl.setOffset(offset);
			attrImpl.setLength(attrImpl.toXMLString().length());
			attributes.add(attrImpl);
		} else {
			FuzzyXMLAttributeImpl attrImpl = (FuzzyXMLAttributeImpl) attrNode;
			attrImpl.setValue(attr.getValue());
		}
	}

	public FuzzyXMLAttribute getAttributeNode(String name) {
		FuzzyXMLAttribute[] attrs = getAttributes();
		for (int i = 0; i < attrs.length; i++) {
			if (attrs[i].getName().equals(name)) {
				return attrs[i];
			}
		}
		return null;
	}

	public boolean hasAttribute(String name) {
		return getAttributeNode(name) != null;
	}

	public void removeAttributeNode(FuzzyXMLAttribute attr) {
		if (attributes.contains(attr)) {
			((AbstractFuzzyXMLNode) attr).setParentNode(null);
			((AbstractFuzzyXMLNode) attr).setDocument(null);
			attributes.remove(attr);
			fireModifyEvent("", attr.getOffset(), attr.getLength());
			appendOffset(this, attr.getOffset(), attr.getLength() * -1);
		}
	}

	public String getValue() {
		StringBuffer sb = new StringBuffer();
		FuzzyXMLNode[] children = getChildren();
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof FuzzyXMLText) {
				sb.append(((FuzzyXMLText) children[i]).getValue());
			}
		}
		return sb.toString();
	}

	public String toXMLString() {
		boolean isHTML = false;
		if (getDocument() != null) {
			isHTML = getDocument().isHTML();
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<").append(FuzzyXMLUtil.escape(getName(), isHTML));
		FuzzyXMLAttribute[] attrs = getAttributes();
		for (int i = 0; i < attrs.length; i++) {
			sb.append(attrs[i].toXMLString());
		}
		FuzzyXMLNode[] children = getChildren();
		if (children.length == 0) {
			sb.append("/>");
		} else {
			sb.append(">");
			for (int i = 0; i < children.length; i++) {
				sb.append(children[i].toXMLString());
			}
			sb.append("</").append(FuzzyXMLUtil.escape(getName(), isHTML)).append(">");
		}
		return sb.toString();
	}

	public boolean equals(Object obj) {
		if (obj instanceof FuzzyXMLElement) {
			FuzzyXMLElement element = (FuzzyXMLElement) obj;

			if (!element.getName().equals(getName())) {
				return false;
			}

			FuzzyXMLNode parent = element.getParentNode();
			if (parent == null) {
				if (getParentNode() == null) {
					return true;
				}
				return false;
			}

			if (element.getOffset() == getOffset()) {
				return true;
			}

		}
		return false;
	}

	public String getAttributeValue(String name) {
		FuzzyXMLAttribute attr = getAttributeNode(name);
		if (attr != null) {
			return attr.getValue();
		}
		return null;
	}

	public void setAttribute(String name, String value) {
		FuzzyXMLAttribute attr = new FuzzyXMLAttributeImpl(name, value);
		setAttribute(attr);
	}

	public void removeAttribute(String name) {
		FuzzyXMLAttribute attr = getAttributeNode(name);
		if (attr != null) {
			removeAttributeNode(attr);
		}
	}

	public void setDocument(FuzzyXMLDocumentImpl doc) {
		super.setDocument(doc);
		FuzzyXMLNode[] nodes = getChildren();
		for (int i = 0; i < nodes.length; i++) {
			((AbstractFuzzyXMLNode) nodes[i]).setDocument(doc);
		}
		FuzzyXMLAttribute[] attrs = getAttributes();
		for (int i = 0; i < attrs.length; i++) {
			((AbstractFuzzyXMLNode) attrs[i]).setDocument(doc);
		}
	}

	public String toString() {
		return "element: " + getName();
	}

	public void removeAllChildren() {
		FuzzyXMLNode[] children = getChildren();
		for (int i = 0; i < children.length; i++) {
			removeChild(children[i]);
		}
	}
}
