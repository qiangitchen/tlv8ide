package tern.vue.modules;

/**
 * Directive collector.
 * 
 */
public interface IDirectiveCollector extends IDirectiveParameterCollector {

	/**
	 * Collect the given directive.
	 * 
	 * @param directive
	 * @param nameWhichMatch
	 */
	void add(Directive directive, String nameWhichMatch);

}
