package tern.angular.modules;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractAngularModulesRegistry implements
		IAngularModulesRegistry {

	private final Map<String, Module> modules;

	public AbstractAngularModulesRegistry() {
		this.modules = new HashMap<String, Module>();
	}

	public void addModule(Module module) {
		modules.put(module.getName(), module);
	}

	public Collection<Module> getModules() {
		return modules.values();
	}

	public Module getModule(String name) {
		return modules.get(name);
	}

	public void collectDirectives(String tagName, String directiveName,
			IDirectiveSyntax syntax, List<Directive> existingDirectives,
			Restriction restriction, IDirectiveCollector collector) {
		// collect directives of each modules.
		Collection<Module> modules = getModules();
		for (Module module : modules) {
			module.collectDirectives(tagName, directiveName, syntax,
					existingDirectives, restriction, collector);
		}
	}

	public Directive getDirective(String tagName, String name,
			Restriction restriction) {
		Collection<Module> modules = getModules();
		for (Module module : modules) {
			Directive directive = module.getDirective(tagName, name, restriction);
			if (directive != null) {
				return directive;
			}
		}
		return null;
	}

	public void clear() {
		this.modules.clear();
	}
}
