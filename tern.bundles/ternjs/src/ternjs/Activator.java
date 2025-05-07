package ternjs;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import tern.utils.ZipUtils;

/**
 * OSGi Activator for ternjs bundle.
 * 
 * @author chenqian
 * @update 2025-05-07
 */
public class Activator implements BundleActivator {

	public static final String PLUGIN_ID = "ternjs";

	private static final File ternRepositoryBaseDir = new File(
			System.getProperty("user.home") + File.separator + ".ternjs");

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext )
	 */
	@SuppressWarnings("deprecation")
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		File path = new File(ternRepositoryBaseDir, "node_modules");
		if (!path.exists()) {
			File bundleDir = FileLocator.getBundleFile(bundleContext.getBundle());
			File zipFile = new File(bundleDir, "/node_modules.zip");
			System.out.println("zipFile: " + zipFile.getAbsolutePath());
			if (zipFile.exists()) {
				ZipUtils.extract(zipFile, ternRepositoryBaseDir);
				System.out.println("extract zip file to: " + ternRepositoryBaseDir);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	public static File getTernRepositoryBaseDir() throws IOException {
		return ternRepositoryBaseDir;
	}

}
