package com.tulin.v8.webtools.ide.wizards;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.utils.IOUtil;

public class JavaScriptLibrariesManager {

	private static Map<String, JSLibrary> libs = new HashMap<String, JSLibrary>();
	static {
		libs.put("prototype.js 1.6.0", new JSLibrary("js/prototype/1.6.0/prototype-1.6.0.2.js"));

		libs.put("script.aculo.us 1.8.1",
				new JSLibrary("js/scriptaculous/1.8.1/builder.js", "js/scriptaculous/1.8.1/controls.js",
						"js/scriptaculous/1.8.1/dragdrop.js", "js/scriptaculous/1.8.1/effects.js",
						"js/scriptaculous/1.8.1/scriptaculous.js", "js/scriptaculous/1.8.1/slider.js",
						"js/scriptaculous/1.8.1/sound.js", "js/scriptaculous/1.8.1/unittest.js"));
	}

	public static String[] getLibraryNames() {
		return libs.keySet().toArray(new String[libs.size()]);
	}

	public static void copyLibrary(String name, IContainer container) {
		JSLibrary lib = libs.get(name);
		if (lib != null) {
			lib.copy(container);
		}
	}

	private static class JSLibrary {
		private String[] files;

		public JSLibrary(String... files) {
			this.files = files;
		}

		public void copy(IContainer container) {
			for (String filePath : files) {
				try {
					URL url = WebToolsPlugin.getDefault().getBundle().getEntry(filePath);
					byte[] buf = IOUtil.readStream(url.openStream());

					int index = filePath.lastIndexOf('/');
					if (index >= 0) {
						filePath = filePath.substring(index + 1);
					}

					IFile file = container.getFile(new Path(filePath));
					file.create(new ByteArrayInputStream(buf), true, null);
				} catch (Exception ex) {
					WebToolsPlugin.logException(ex);
				}
			}
		}
	}

}
