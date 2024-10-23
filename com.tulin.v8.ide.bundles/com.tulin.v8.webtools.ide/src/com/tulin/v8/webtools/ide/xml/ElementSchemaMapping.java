package com.tulin.v8.webtools.ide.xml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

/**
 * The model object for the mapping between the root element and the schema.
 * <p>
 * This class also provides two static methods which load and save models from /
 * to the preference store.
 * 
 */
public class ElementSchemaMapping {

	private String rootElement;
	private String filePath;

	public ElementSchemaMapping(String rootElement, String filePath) {
		setRootElement(rootElement);
		setFilePath(filePath);
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getRootElement() {
		return rootElement;
	}

	public void setRootElement(String rootElement) {
		this.rootElement = rootElement;
	}

	/**
	 * Save models to the preference store.
	 * 
	 * @param list the list which contans models
	 */
	public static void saveToPreference(List<ElementSchemaMapping> list) {
		IPreferenceStore store = WebToolsPlugin.getDefault().getPreferenceStore();

		StringBuffer sb = new StringBuffer();
		for (ElementSchemaMapping mapping : list) {
			sb.append(mapping.getRootElement());
			sb.append("\t");
			sb.append(mapping.getFilePath());
			sb.append("\n");
		}
		store.setValue(WebToolsPlugin.PREF_SCHEMA_MAPPINGS, sb.toString());
	}

	/**
	 * Load models from the preference store as the <code>java.util.List</code>
	 * which contains <code>ElementSchemaMapping</code>.
	 * 
	 * @return the list which contans loaded models
	 */
	public static List<ElementSchemaMapping> loadFromPreference() {
		IPreferenceStore store = WebToolsPlugin.getDefault().getPreferenceStore();
		List<ElementSchemaMapping> list = new ArrayList<ElementSchemaMapping>();

		String customMappings = store.getString(WebToolsPlugin.PREF_SCHEMA_MAPPINGS);
		String[] dim = customMappings.split("\n");
		for (int i = 0; i < dim.length; i++) {
			if (dim[i].length() > 0) {
				String[] elemAndPath = dim[i].split("\t");
				ElementSchemaMapping mapping = new ElementSchemaMapping(elemAndPath[0], elemAndPath[1]);
				list.add(mapping);
			}
		}

		return list;
	}

}
