package tern.eclipse.ide.internal.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.util.NLS;
import org.osgi.framework.Bundle;

public class TernModuleInstall {

	private static final String ID_ATTR = "id";
	private static final String NAME_ATTR = "name";
	private static final String SRC_ATTR = "src";

	private final String name;
	private final String id;
	private final File src;

	/**
	 * GeneratorType constructor comment.
	 *
	 * @param element
	 *            a configuration element
	 * @throws IOException
	 */
	public TernModuleInstall(IConfigurationElement element) throws IOException {
		this.name = element.getAttribute(NAME_ATTR);
		this.id = element.getAttribute(ID_ATTR);
		String pluginId = element.getNamespaceIdentifier();
		this.src = getFile(element.getAttribute(SRC_ATTR), pluginId);
	}

	private File getFile(String src, String pluginId) throws IOException {
		// Obtain a file: URL for the bundle entry containing the Tern module contributions.
		Bundle bundle = Platform.getBundle(pluginId);
		URL moduleUrl = FileLocator.toFileURL(bundle.getEntry(src));
		if (moduleUrl == null || !"file".equals(moduleUrl.getProtocol())) {
			throw new IOException(NLS.bind("Unable to obtain a file URL for {0} in plug-in {1}", src, pluginId));
		}
		return new File(moduleUrl.getPath()).getCanonicalFile();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public File getSrc() {
		return src;
	}

	public void dispose() {

	}
}
