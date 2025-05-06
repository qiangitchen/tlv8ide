package tern.vue.modules;

/**
 * Directive parameter collector.
 * 
 */
public interface IDirectiveParameterCollector {

	/**
	 * Collect the given directive parameter.
	 * 
	 * @param parameter
	 */
	void add(DirectiveParameter parameter);

}
