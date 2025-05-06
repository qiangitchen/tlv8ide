package tern.angular.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tern.utils.StringUtils;

class DirectivesByTagName {

	private final Map<String, DirectiveAndSyntax> directivesMap;
	private final List<Directive> directives;

	public DirectivesByTagName() {
		this.directivesMap = new HashMap<String, DirectiveAndSyntax>();
		this.directives = new ArrayList<Directive>();
	}

	public void addDirective(Directive directive) {
		if (StringUtils.isEmpty(directive.getName())) {
			// don't add directive if name is empty.
			return;
		}
		directives.add(directive);
		List<String> names = directive.getDirectiveNames();
		for (int i = 0; i < names.size(); i++) {
			directivesMap.put(names.get(i), new DirectiveAndSyntax(directive, i));
		}
	}

	public void collectDirectives(String directiveName, IDirectiveSyntax syntax, List<Directive> ignoreDirectives,
			Restriction restriction, IDirectiveCollector collector) {
		String name = null;
		for (Directive directive : directives) {
			if (Module.isMatch(ignoreDirectives, restriction, directive)) {
				List<String> names = directive.getDirectiveNames();
				for (int i = 0; i < names.size(); i++) {
					if (DirectiveHelper.isSupport(syntax, i)) {
						name = names.get(i);
						if (name.startsWith(directiveName)) {
							collector.add(directive, name);
						}
					}
				}
			}
		}
	}

	public Directive getDirective(String name, Restriction restriction) {
		DirectiveAndSyntax directiveAndSyntax = directivesMap.get(name);
		if (directiveAndSyntax != null && directiveAndSyntax.getDirective().isMatch(restriction)) {
			return directiveAndSyntax.getDirective();
		}
		return null;
	}
}
