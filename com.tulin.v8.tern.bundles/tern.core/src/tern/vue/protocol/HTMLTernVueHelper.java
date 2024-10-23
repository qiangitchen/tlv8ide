package tern.vue.protocol;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import tern.vue.modules.Directive;
import tern.vue.modules.IDirectiveProvider;
import tern.vue.VueType;

public class HTMLTernVueHelper {

	private HTMLTernVueHelper() {
	}

	public static void populateScope(Node element, IDirectiveProvider provider, 
			Object project, TernVueQuery query) {
		TernVueScope scope = query.getScope();
		populateScope(element, scope, provider, project,
				query.getFirstVueType());
	}

	public static void populateScope(Node element, TernVueScope scope,
			IDirectiveProvider provider, Object project, VueType vueType) {
		switch (vueType) {
		case module:
			// do nothing;
			break;
		case controller:
			// find controller
			populateScope(scope, element, provider, project, false);
			break;
		case unknown:
		case model:
		case directive:
		case repeat_expression:
			// find model
			populateScope(scope, element, provider, project, true);
			break;
		default:
			break;
		}

	}

	private static void populateScope(TernVueScope scope, Node element,
			IDirectiveProvider provider, Object project,
			boolean populateController) {
		if (element == null || element.getNodeType() == Node.DOCUMENT_NODE) {
			return;
		}
		NamedNodeMap attributes = element.getAttributes();
		if (attributes != null) {
			Attr node = null;
			for (int i = 0; i < attributes.getLength(); i++) {
				node = (Attr) attributes.item(i);
				Directive directive = provider.getVueDirective(project,
						node);
				if (directive != null) {
					switch (directive.getDirectiveType()) {
					case module:
						String module = ((Attr) node).getValue();
						scope.setModule(module);
						return;
					case controller:
						if (populateController) {
							String controller = ((Attr) node).getValue();
							scope.addController(controller);
						}
						break;
					case model:
						String model = ((Attr) node).getValue();
						scope.addModel(model);
						break;
					case repeat_expression:
						String expression = ((Attr) node).getValue();
						scope.addRepeat(expression);
						break;
					default:
						break;
					}
				}
			}
		}
		Node parent = element.getPreviousSibling();
		if (parent == null)
			parent = element.getParentNode();
		populateScope(scope, parent, provider, project, populateController);
	}

}
