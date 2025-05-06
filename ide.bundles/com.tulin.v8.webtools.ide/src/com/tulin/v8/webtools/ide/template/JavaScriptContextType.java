package com.tulin.v8.webtools.ide.template;

import org.eclipse.jface.text.templates.GlobalTemplateVariables;
import org.eclipse.jface.text.templates.TemplateContextType;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

public class JavaScriptContextType extends TemplateContextType {

	public static final String CONTEXT_TYPE = WebToolsPlugin.getPluginId() + ".templateContextType.javascript";

	public JavaScriptContextType() {
		addResolver(new GlobalTemplateVariables.Cursor());
		addResolver(new GlobalTemplateVariables.WordSelection());
		addResolver(new GlobalTemplateVariables.LineSelection());
		addResolver(new GlobalTemplateVariables.User());
	}

}
