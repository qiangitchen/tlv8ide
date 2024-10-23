package tern.angular.modules;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 * Directive provider.
 * 
 */
public interface IDirectiveProvider {

	/**
	 * Returns the angular directive from the given attribute and null
	 * otherwise.
	 * 
	 * @param attr
	 * @return
	 */
	Directive getAngularDirective(Object project, Attr attr);

	/**
	 * Returns the angular directive parameter from the given attribute and null
	 * otherwise.
	 * 
	 * @param attr
	 * @return
	 */
	DirectiveParameter getAngularDirectiveParameter(Object project, Attr attr);

	/**
	 * Returns the angular directive from the given element and null otherwise.
	 * 
	 * @param element
	 * @return
	 */
	Directive getAngularDirective(Object project, Element element);
}
