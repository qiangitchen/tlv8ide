package tern.angular.modules;

import java.util.List;

/**
 * Module registry
 * 
 */
public interface IAngularModulesRegistry {

	/**
	 * Returns the module from the given name and null otherwise.
	 * 
	 * @param name
	 *            of the module.
	 * @return the module from the given name and null otherwise.
	 */
	Module getModule(String name);

	/**
	 * 
	 * @param tagName
	 * @param name
	 * @return
	 */
	Directive getDirective(String tagName, String name, Restriction restriction);

	/**
	 * Collect directives with several criteria.
	 * 
	 * @param tagName
	 *            the tag name of the directive or null.
	 * @param directiveName
	 *            starts with directive name.
	 * @param syntax
	 *            directive syntax or null.
	 * @param existingDirectives
	 *            list of directives which must be excluded or null otherwise.
	 * @param restriction
	 *            the angular restriction.
	 * @param collector
	 *            the collector to collect directives.
	 */
	void collectDirectives(String tagName, String directiveName,
			IDirectiveSyntax syntax, List<Directive> existingDirectives,
			Restriction restriction, IDirectiveCollector collector);

}
