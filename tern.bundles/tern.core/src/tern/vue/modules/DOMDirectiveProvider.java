package tern.vue.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/**
 * DOM directive provider.
 * 
 */
public class DOMDirectiveProvider implements IDirectiveProvider {

	private static final DOMDirectiveProvider INSTANCE = new DOMDirectiveProvider();

	public static DOMDirectiveProvider getInstance() {
		return INSTANCE;
	}

	@Override
	public Directive getVueDirective(Object project, Attr attr) {
		if (attr == null) {
			return null;
		}
		return VueModulesManager.getInstance().getDirective(project,
				attr.getOwnerElement().getNodeName(), attr.getName(),
				Restriction.A);
	}

	@Override
	public Directive getVueDirective(Object project, Element element) {
		if (element == null) {
			return null;
		}
		return VueModulesManager.getInstance().getDirective(project, null,
				element.getTagName(), Restriction.E);
	}

	@Override
	public DirectiveParameter getVueDirectiveParameter(Object project,
			Attr attr) {
		Element element = attr.getOwnerElement();
		// check if owner element is a directive?
		Directive elementDirective = getVueDirective(project, element);
		if (elementDirective != null) {
			return elementDirective.getParameter(attr.getName());
		} else {
			// retrieve directives from other attributes.
			List<Directive> directives = getVueDirectives(project,
					attr.getOwnerElement(), attr);
			DirectiveParameter parameter = null;
			for (Directive directive : directives) {
				parameter = directive.getParameter(attr.getName());
				if (parameter != null) {
					return parameter;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Directive> getVueDirectives(Object project,
			Element element, Attr selectedAttr) {
		if (element == null) {
			return Collections.emptyList();
		}
		List<Directive> names = null;
		NamedNodeMap attributes = element.getAttributes();
		int length = attributes.getLength();
		Attr attr = null;
		for (int i = 0; i < length; i++) {
			attr = (Attr) attributes.item(i);
			if (selectedAttr == null || !selectedAttr.equals(attr)) {
				Directive directive = getVueDirective(project, attr);
				if (directive != null) {
					if (names == null) {
						names = new ArrayList<Directive>();
					}
					names.add(directive);
				}
			}
		}
		return (List<Directive>) (names != null ? names : Collections
				.emptyList());
	}

}
