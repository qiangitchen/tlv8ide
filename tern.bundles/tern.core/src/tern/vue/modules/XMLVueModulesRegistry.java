package tern.vue.modules;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.SAXException;

public class XMLVueModulesRegistry extends AbstractVueModulesRegistry {

	public XMLVueModulesRegistry() {
		super();
		try {
			loadModule(XMLVueModulesRegistry.class
					.getResourceAsStream("ng.xml"));
			loadModule(XMLVueModulesRegistry.class
					.getResourceAsStream("ngRoute.xml"));
			loadModule(XMLVueModulesRegistry.class
					.getResourceAsStream("ngTouch.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadModule(InputStream in) throws IOException, SAXException {
		addModule(new SAXModuleHandler().load(in));
	}

}
