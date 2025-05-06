package com.tulin.v8.fuzzyxml.util;

import java.util.ArrayList;
import java.util.List;

import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.fuzzyxml.FuzzyXMLNode;

public class NodeSelectUtil {

	public static FuzzyXMLNode[] getChildren(FuzzyXMLElement element, NodeFilter filter) {
		List<FuzzyXMLNode> result = new ArrayList<>();
		FuzzyXMLNode[] children = element.getChildren();
		for (int i = 0; i < children.length; i++) {
			if (filter.filter((FuzzyXMLNode) children[i])) {
				result.add((FuzzyXMLNode) children[i]);
			}
		}
		return (FuzzyXMLNode[]) result.toArray(new FuzzyXMLNode[result.size()]);
	}

	public static FuzzyXMLNode[] getNodeByFilter(FuzzyXMLElement element, NodeFilter filter) {
		List<FuzzyXMLNode> result = new ArrayList<>();
		if (filter.filter(element)) {
			result.add(element);
		}
		searchNodeByFilter(element, filter, result);
		return (FuzzyXMLElement[]) result.toArray(new FuzzyXMLElement[result.size()]);
	}

	private static void searchNodeByFilter(FuzzyXMLElement element, NodeFilter filter, List<FuzzyXMLNode> result) {
		FuzzyXMLNode[] children = element.getChildren();
		for (int i = 0; i < children.length; i++) {
			if (filter.filter(children[i])) {
				result.add(children[i]);
			}
			if (children[i] instanceof FuzzyXMLElement) {
				searchNodeByFilter((FuzzyXMLElement) children[i], filter, result);
			}
		}
	}

	public static FuzzyXMLElement getElementById(FuzzyXMLElement element, String id) {
		FuzzyXMLElement[] elements = getElementByAttribute(element, "id", id);
		if (elements.length == 0) {
			return null;
		} else {
			return elements[0];
		}
	}

	public static FuzzyXMLElement[] getElementByAttribute(FuzzyXMLElement element, String name, String value) {
		List<FuzzyXMLNode> result = new ArrayList<>();
		searchElementByAttribute(element, name, value, result);
		return (FuzzyXMLElement[]) result.toArray(new FuzzyXMLElement[result.size()]);
	}

	private static void searchElementByAttribute(FuzzyXMLElement element, String name, String value,
			List<FuzzyXMLNode> result) {
		if (value.equals(element.getAttributeValue(name))) {
			result.add(element);
		}
		FuzzyXMLNode[] children = element.getChildren();
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof FuzzyXMLElement) {
				searchElementByAttribute(element, name, value, result);
			}
		}
	}

	public static FuzzyXMLElement[] getElementByTagName(FuzzyXMLElement element, String name) {
		List<FuzzyXMLNode> result = new ArrayList<>();
		searchElementByTagName(element, name, result);
		return (FuzzyXMLElement[]) result.toArray(new FuzzyXMLElement[result.size()]);
	}

	private static void searchElementByTagName(FuzzyXMLElement element, String name, List<FuzzyXMLNode> result) {
		if (element.getName().equals(name)) {
			result.add(element);
		}
		FuzzyXMLNode[] children = element.getChildren();
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof FuzzyXMLElement) {
				searchElementByTagName(element, name, result);
			}
		}
	}

}
