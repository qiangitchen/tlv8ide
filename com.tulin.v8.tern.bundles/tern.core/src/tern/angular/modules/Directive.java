package tern.angular.modules;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tern.angular.AngularType;
import tern.server.protocol.outline.IJSNode;
import tern.utils.StringUtils;

/**
 * Angular directive.
 */
public class Directive extends AngularElement {

	private final String url;
	private final String restrict;
	private final Module module;
	private final Collection<String> tagNames;
	private String description;
	private final DirectiveValue directiveValue;
	private Map<String, DirectiveParameter> parameters;
	private final boolean custom;
	private final AngularType type;

	public Directive(String name, AngularType type, String url, Collection<String> tagNames, String restrict,
			DirectiveValue directiveValue, IJSNode parent) {
		this(name, type, url, tagNames, restrict, directiveValue, null, null, null, parent);
	}

	public Directive(String name, AngularType type, String url, Collection<String> tagNames, String restrict,
			DirectiveValue directiveValue, Long start, Long end, String file, IJSNode parent) {
		this(name, type, url, tagNames, restrict, directiveValue, true, start, end, file, parent);
	}

	public Directive(String name, AngularType type, String url, Collection<String> tagNames, String restrict,
			DirectiveValue directiveValue, boolean custom, IJSNode parent) {
		this(name, type, url, tagNames, restrict, directiveValue, custom, null, null, null, parent);
	}

	public Directive(String name, AngularType type, String url, Collection<String> tagNames, String restrict,
			DirectiveValue directiveValue, boolean custom, Long start, Long end, String file, IJSNode parent) {
		super(name, AngularType.directive, start, end, file, parent);
		this.type = type;
		this.url = url;
		this.restrict = StringUtils.isEmpty(restrict) ? Restriction.A.name() : restrict;
		this.directiveValue = directiveValue;
		Module module = (Module) parent;
		this.module = module;
		this.tagNames = tagNames;
		if (module != null) {
			module.addDirective(this);
		}
		this.custom = custom;
	}

	public Collection<String> getTagNames() {
		return tagNames;
	}

	public List<String> getDirectiveNames() {
		return DirectiveHelper.getDirectiveNames(getName());
	}

	public Module getModule() {
		return module;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String getURL() {
		return url;
	}

	public DirectiveValue getDirectiveValue() {
		return directiveValue;
	}

	public Collection<DirectiveParameter> getParameters() {
		if (parameters != null) {
			return parameters.values();
		}
		return Collections.emptyList();
	}

	public Collection<String> getParameterNames() {
		if (parameters != null) {
			return parameters.keySet();
		}
		return Collections.emptyList();
	}

	public void addParameter(DirectiveParameter parameter) {
		if (parameters == null) {
			parameters = new HashMap<String, DirectiveParameter>();
		}
		parameters.put(parameter.getName(), parameter);
	}

	public boolean hasParameters() {
		if (parameters != null) {
			return !parameters.isEmpty();
		}
		return false;
	}

	public DirectiveParameter getParameter(String name) {
		if (parameters != null) {
			return parameters.get(name);
		}
		return null;
	}

	public boolean isCustom() {
		return custom;
	}

	public boolean isMatch(Restriction restriction) {
		if (restriction == null) {
			return true;
		}
		return restriction.isMatch(restrict);
	}

	public String getRestrict() {
		return restrict;
	}
	
	public AngularType getDirectiveType() {
		return type;
	}
}
