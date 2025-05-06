package com.tulin.v8.webtools.ide.jsp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jface.preference.IPreferenceStore;

import com.tulin.v8.fuzzyxml.FuzzyXMLDocument;
import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.fuzzyxml.FuzzyXMLNode;
import com.tulin.v8.fuzzyxml.FuzzyXMLParser;
import com.tulin.v8.fuzzyxml.XPath;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.gefutils.IJarVisitor;
import com.tulin.v8.webtools.ide.gefutils.JarAcceptor;
import com.tulin.v8.webtools.ide.html.HTMLUtil;
import com.tulin.v8.webtools.ide.utils.IOUtil;

/**
 * Provides one static method to get TLD files.
 */
public class TLDLoader {

	private static Map<String, byte[]> cache = new HashMap<String, byte[]>();

	/**
	 * Returns the TLD file as <code>InputStream</code>.
	 * <p>
	 * If the URL starts <code>http://</code>, this method tries to get the TLD file
	 * from the remote server. Otherwise, tries to get from the local path using
	 * given URI as the relative path from the base directory.
	 *
	 * @param project the java project
	 * @param basedir the base directory
	 * @param uri     the URI of the TLD file
	 * @return <code>InputStream</code> of the TLD file
	 */
	public static InputStream get(IJavaProject project, File basedir, String uri) throws Exception {
		if (cache.get(uri) == null) {
			// Default internal TLDs
			Map<String, String> innerTLD = WebToolsPlugin.getInnerTLD();
			if (innerTLD.get(uri) != null) {
				InputStream in = TLDLoader.class.getResourceAsStream(innerTLD.get(uri));
				byte[] bytes = IOUtil.readStream(in);
				cache.put(uri, bytes);
				return new ByteArrayInputStream(bytes);
			}
			// Contributed TLDs
			ITLDLocator[] locators = WebToolsPlugin.getDefault().getTLDLocatorContributions();
			for (int i = 0; i < locators.length; i++) {
				InputStream in = locators[i].locateTLD(uri);
				if (in != null) {
					byte[] bytes = IOUtil.readStream(in);
					cache.put(uri, bytes);
					return new ByteArrayInputStream(bytes);
				}
			}
			// from PreferenceStore
			Map<String, String> pref = getPreferenceTLD();
			if (pref.get(uri) != null) {
				InputStream in = new FileInputStream(new File(pref.get(uri)));
				byte[] bytes = IOUtil.readStream(in);
				cache.put(uri, bytes);
				return new ByteArrayInputStream(bytes);
			}
			// Check web.xml
			byte[] bytes = getTLDFromWebXML(basedir, uri);
			if (bytes != null) {
				cache.put(uri, bytes);
				return new ByteArrayInputStream(bytes);
			}

			// Search from project classpath
			TLDJarVisitor visitor = new TLDJarVisitor(uri);
			for (IPackageFragmentRoot root : project.getPackageFragmentRoots()) {
				if (root instanceof JarPackageFragmentRoot) {
					ZipFile zipFile = ((JarPackageFragmentRoot) root).getJar();
					Enumeration<? extends ZipEntry> e = zipFile.entries();
					while (e.hasMoreElements()) {
						ZipEntry entry = e.nextElement();
						Object result = visitor.visit(zipFile, entry);
						if (result != null) {
							bytes = (byte[]) result;
							if (bytes != null) {
								cache.put(uri, bytes);
								return new ByteArrayInputStream(bytes);
							}
						}
					}
				}
			}

			if (uri.startsWith("http://") || uri.startsWith("https://")) {
				// Search META-INF in jar files
				bytes = getTLDFromJars(basedir, uri);
				if (bytes != null) {
					cache.put(uri, bytes);
					return new ByteArrayInputStream(bytes);
				}
				// from the URL
				URL url = new URL(uri);
				InputStream in = url.openStream();
				cache.put(uri, IOUtil.readStream(in));
			} else {
				// from the local file
				File file = new File(basedir, uri);
				InputStream in = new FileInputStream(file);
				cache.put(uri, IOUtil.readStream(in));
			}
		}

		return new ByteArrayInputStream(cache.get(uri));
	}

	/** Load configurations from <code>IPreferenceStore</code>. */
	private static Map<String, String> getPreferenceTLD() {
		Map<String, String> map = new HashMap<String, String>();

		IPreferenceStore store = WebToolsPlugin.getDefault().getPreferenceStore();
		String[] uri = store.getString(WebToolsPlugin.PREF_TLD_URI).split("\n");
		String[] path = store.getString(WebToolsPlugin.PREF_TLD_PATH).split("\n");
		for (int i = 0; i < uri.length; i++) {
			if (!uri[i].trim().equals("") && !path[i].trim().equals("")) {
				map.put(uri[i].trim(), path[i].trim());
			}
		}

		return map;
	}

	/** Load from web.xml */
	private static byte[] getTLDFromWebXML(File basedir, String uri) {
		File webXML = new File(basedir, "/WEB-INF/web.xml");

		if (webXML.exists() && webXML.isFile()) {
			try {
				FuzzyXMLDocument doc = new FuzzyXMLParser().parse(new FileInputStream(webXML));
				FuzzyXMLNode[] nodes = XPath.selectNodes(doc.getDocumentElement(),
						"/web-app/taglib|/web-app/jsp-config/taglib");

				for (int i = 0; i < nodes.length; i++) {
					FuzzyXMLElement element = (FuzzyXMLElement) nodes[i];
					String taglibUri = HTMLUtil.getXPathValue(element, "/taglib-uri/child::text()");
					String taglibLoc = HTMLUtil.getXPathValue(element, "/taglib-location/child::text()");
					if (uri.equals(taglibUri)) {
						if (taglibLoc != null && taglibLoc.endsWith(".tld")) {
							File file = new File(basedir, taglibLoc);
							return IOUtil.readStream(new FileInputStream(file));
						}
						break;
					}
				}
			} catch (Exception ex) {
				WebToolsPlugin.logException(ex);
			}
		}
		return null;
	}

	/** Load from META-INF in the jar file */
	private static byte[] getTLDFromJars(File basedir, final String uri) {
		return (byte[]) JarAcceptor.accept(basedir, new TLDJarVisitor(uri));
	}

	private static class TLDJarVisitor implements IJarVisitor {

		private String uri;

		public TLDJarVisitor(String uri) {
			this.uri = uri;
		}

		public Object visit(ZipFile file, ZipEntry entry) throws Exception {
			if (entry.getName().endsWith(".tld")) {
				byte[] bytes = IOUtil.readStream(file.getInputStream(entry));
				try {
					FuzzyXMLDocument doc = new FuzzyXMLParser().parse(new ByteArrayInputStream(bytes));
					String nodeURI = HTMLUtil.getXPathValue(doc.getDocumentElement(), "/taglib/uri/child::text()");
					if (nodeURI != null && uri.equals(nodeURI)) {
						return bytes;
					}
				} catch (Exception ex) {
					WebToolsPlugin.logException(ex);
				}
			}
			return null;
		}

	}
}
