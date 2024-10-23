package tern.vue.modules;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 * Directive provider.
 * 
 */
public interface IDirectiveProvider {

	/**
	 * Returns the vue directive from the given attribute and null
	 * otherwise.
	 * 
	 * @param attr
	 * @return
	 */
	Directive getVueDirective(Object project, Attr attr);

	/**
	 * Returns the vue directive parameter from the given attribute and null
	 * otherwise.
	 * 
	 * @param attr
	 * @return
	 */
	DirectiveParameter getVueDirectiveParameter(Object project, Attr attr);

	/**
	 * Returns the vue directive from the given element and null otherwise.
	 * 
	 * @param element
	 * @return
	 */
	Directive getVueDirective(Object project, Element element);
}
