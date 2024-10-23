package tern.vue.modules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VueModulesManager {

	private static final VueModulesManager INSTANCE = new VueModulesManager();

	public static VueModulesManager getInstance() {
		return INSTANCE;
	}

	private final List<IVueModulesRegistry> defaultRegistries;

	private final Map<Object, IVueModulesRegistry> customRegistries;

	private VueModulesManager() {
		this.defaultRegistries = new ArrayList<IVueModulesRegistry>();
		this.customRegistries = new HashMap<Object, IVueModulesRegistry>();
		defaultRegistries.add(new XMLVueModulesRegistry());
	}

	public void addRegistry(IVueModulesRegistry registry) {
		defaultRegistries.add(registry);
	}

	public void addRegistry(Object project, IVueModulesRegistry registry) {
		customRegistries.put(project, registry);
	}

	public void collectDirectives(Object project, String tagName,
			String directiveName, IDirectiveSyntax syntax,
			List<Directive> existingDirectives, Restriction restriction,
			IDirectiveCollector collector) {
		// collect directives of each modules.
		collectDefaultDirectives(tagName, directiveName, syntax,
				existingDirectives, restriction, collector);
		if (project != null) {
			IVueModulesRegistry registry = customRegistries.get(project);
			if (registry != null) {
				registry.collectDirectives(tagName, directiveName, syntax,
						existingDirectives, restriction, collector);
			}
		}
		// collect directives parameters of directive to ignore
		if (existingDirectives != null) {
			for (Directive directive : existingDirectives) {
				collectDirectiveParameters(directive, directiveName, collector);
			}
		}
	}

	public void collectDirectiveParameters(Directive directive, String name,
			IDirectiveParameterCollector collector) {
		Collection<DirectiveParameter> parameters = directive.getParameters();
		for (DirectiveParameter parameter : parameters) {
			if (parameter.getName().startsWith(name)) {
				collector.add(parameter);
			}
		}
	}

	private void collectDefaultDirectives(String tagName, String directiveName,
			IDirectiveSyntax syntax, List<Directive> existingDirectives,
			Restriction restriction, IDirectiveCollector collector) {
		for (IVueModulesRegistry registry : defaultRegistries) {
			registry.collectDirectives(tagName, directiveName, syntax,
					existingDirectives, restriction, collector);
		}
	}

	public Directive getDirective(String tagName, String name,
			Restriction restriction) {
		Directive directive = null;
		for (IVueModulesRegistry registry : defaultRegistries) {
			directive = registry.getDirective(tagName, name, restriction);
			if (directive != null) {
				return directive;
			}
		}
		return null;
	}

	public Module getModule(String name) {
		Module module = null;
		for (IVueModulesRegistry registry : defaultRegistries) {
			module = registry.getModule(name);
			if (module != null) {
				return module;
			}
		}
		return null;
	}

	public Directive getDirective(Object project, String tagName, String name,
			Restriction restriction) {
		Directive directive = getDirective(tagName, name, restriction);
		if (directive != null) {
			return directive;
		}
		if (project != null) {
			IVueModulesRegistry registry = customRegistries.get(project);
			if (registry != null) {
				return registry.getDirective(tagName, name, restriction);
			}
		}
		return null;
	}

}
