package com.tulin.v8.webtools.ide.js.additional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

public class AdditionalJavaScriptCompleterManager {
	private static Map<String, IAdditionalJavaScriptCompleter> completers = null;

	private static void init() {
		completers = new LinkedHashMap<String, IAdditionalJavaScriptCompleter>();

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(WebToolsPlugin.getPluginId() + ".javaScriptCompleter");
		IExtension[] extensions = point.getExtensions();

		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] elements = extensions[i].getConfigurationElements();
			for (int j = 0; j < elements.length; j++) {
				if ("completer".equals(elements[j].getName())) {
					try {
						String name = elements[j].getAttribute("name");
						IAdditionalJavaScriptCompleter completer = (IAdditionalJavaScriptCompleter) elements[j]
								.createExecutableExtension("class");
						completers.put(name, completer);
					} catch (CoreException ex) {
						WebToolsPlugin.logException(ex);
					}
				}
			}
		}

//		completers.put("prototype.js", new PrototypeCompleter());
//		completers.put("script.aculo.us", new ScriptaculousCompleter());
	}

	public static String[] getAdditionalJavaScriptCompleterNames() {
		if (completers == null) {
			init();
		}
		return completers.keySet().toArray(new String[0]);
	}

	public static List<IAdditionalJavaScriptCompleter> getAdditionalJavaScriptCompleters() {
		if (completers == null) {
			init();
		}
		return new ArrayList<IAdditionalJavaScriptCompleter>(completers.values());
	}

	public static IAdditionalJavaScriptCompleter getAdditionalJavaSCriptCompleter(String name) {
		if (completers == null) {
			init();
		}
		return completers.get(name);
	}

}
