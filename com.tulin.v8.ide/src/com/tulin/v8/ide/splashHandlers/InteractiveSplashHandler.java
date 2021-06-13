package com.tulin.v8.ide.splashHandlers;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.splash.BasicSplashHandler;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import com.tulin.v8.ide.StudioPlugin;

public class InteractiveSplashHandler extends BasicSplashHandler {

	public void init(Shell splash) {
		super.init(splash);
		String progressRectString = null;
		String messageRectString = null;
		String foregroundColorString = null;
		String versionColorString = null;
		String buildIdLocationString = null;
		IProduct product = Platform.getProduct();
		if (product != null) {
			progressRectString = product.getProperty("startupProgressRect");
			messageRectString = product.getProperty("startupMessageRect");
			foregroundColorString = product.getProperty("startupForegroundColor");
			versionColorString = product.getProperty("versionForegroundColor");
			buildIdLocationString = product.getProperty("versionRect");
		}
		Rectangle progressRect = StringConverter.asRectangle(progressRectString, new Rectangle(10, 10, 300, 15));
		setProgressRect(progressRect);

		Rectangle messageRect = StringConverter.asRectangle(messageRectString, new Rectangle(10, 35, 300, 15));
		setMessageRect(messageRect);
		int foregroundColorInteger;
		int versionColorInteger;
		try {
			foregroundColorInteger = Integer.parseInt(foregroundColorString, 16);
			versionColorInteger = Integer.parseInt(versionColorString, 16);
		} catch (Exception localException) {
			foregroundColorInteger = 13817855;
			versionColorInteger = 13817855;
		}

		setForeground(new RGB((foregroundColorInteger & 0xFF0000) >> 16, (foregroundColorInteger & 0xFF00) >> 8,
				foregroundColorInteger & 0xFF));
		final Color versionColor = new Color(getSplash().getShell().getDisplay(),
				new RGB((versionColorInteger & 0xFF0000) >> 16, (versionColorInteger & 0xFF00) >> 8,
						versionColorInteger & 0xFF));

		final String version = getVersionString();
		final Point versionPoint = StringConverter.asPoint(buildIdLocationString, new Point(322, 190));
		getContent().addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				e.gc.setForeground(versionColor);
				Point p = e.gc.textExtent(version);
				e.gc.drawText(version, versionPoint.x - p.x, versionPoint.y, true);
			}
		});
	}

	private String getVersionString() {
		try {
			File installLocation = new File(Platform.getInstallLocation().getURL().getFile());
			File coursewarePropsFile = new File(installLocation, "courseware.properties");
			if ((coursewarePropsFile.exists()) && (coursewarePropsFile.canRead())) {
				Properties coursewareProperties = new Properties();
				coursewareProperties.load(new FileInputStream(coursewarePropsFile));
				String version = coursewareProperties.getProperty("course.name");
				if ((version != null) && (version.length() > 0)) {
					return version;
				}
			}

		} catch (Exception localException) {
			Bundle bundle = Platform.getBundle(StudioPlugin.PLUGIN_ID);

			String versionString = (String) bundle.getHeaders().get("Bundle-Version");
			if (versionString != null) {
				Version version = new Version(versionString);
				String qualifier = version.getQualifier();
				int ix = qualifier.indexOf('-');
				if (ix > 0) {
					qualifier = qualifier.substring(ix + 1);
				}
				return version.getMajor() + "." + version.getMinor() + "." + version.getMicro() + "."
						+ qualifier.toUpperCase();
			}
		}
		return "";
	}

}
