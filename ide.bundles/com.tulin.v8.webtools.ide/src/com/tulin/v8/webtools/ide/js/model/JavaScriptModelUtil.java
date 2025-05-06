package com.tulin.v8.webtools.ide.js.model;

import java.io.Writer;
import java.util.Set;

import com.tulin.v8.webtools.ide.rhino.javascript.Node;
import com.tulin.v8.webtools.ide.rhino.javascript.Token;

public class JavaScriptModelUtil {

	public static JavaScriptModel getRoot(JavaScriptContext context) {
		while (context.getParent() != null) {
			context = context.getParent();
		}
		return (JavaScriptModel) context;
	}

	public static String cutLastSegment(String name) {
		int index = name.lastIndexOf('.');
		if (index >= 0) {
			return name.substring(0, index);
		}
		return name;
	}

	public static String getLastSegment(String name) {
		int index = name.lastIndexOf('.');
		if (index >= 0) {
			return name.substring(index + 1);
		}
		return name;
	}

	public static void print(Writer writer, Node node, String source, int depth) {
		try {
			StringBuilder buf = new StringBuilder();
			for (int i = 0; i < depth; i++) {
				buf.append(' ');
			}
			String str = node.toString();
			String type = Token.name(node.getType());
			writer.write(buf.toString() + str + ":" + type + " (" + node.sourceStart + "," + node.sourceEnd + ")");
			writer.write(System.getProperty("line.separator"));
			if (node.getType() == Token.FUNCTION && node.fnNode != null) {
				print(writer, node.fnNode, source, depth);
			}
			Node firstChild = node.getFirstChild();
			if (firstChild != null) {
				print(writer, firstChild, source, depth + 1);
			}
			Node next = node.getNext();
			if (next != null) {
				print(writer, next, source, depth);
			}
		} catch (Exception e) {
		}
	}

	public static void print(Writer writer, Set<JavaScriptContext> set, JavaScriptContext context, int depth) {
		try {
			if (context == null) {
				return;
			}
			StringBuilder buf = new StringBuilder();
			for (int i = 0; i < depth; i++) {
				buf.append(' ');
			}
			if (context instanceof JavaScriptModel) {
				writer.write("ROOT");
			}
			for (JavaScriptElement jsElement : context.getElements()) {
				if (!(jsElement instanceof JavaScriptAlias)) {
					System.out.println(buf.toString() + " " + jsElement.getName() + ": " + jsElement);
					if (jsElement.getContext() != null && !set.contains(jsElement.getContext())) {
						set.add(jsElement.getContext());
						print(writer, set, jsElement.getContext(), depth + 1);
					}
				}
			}
		} catch (Exception e) {
		}
	}
}
