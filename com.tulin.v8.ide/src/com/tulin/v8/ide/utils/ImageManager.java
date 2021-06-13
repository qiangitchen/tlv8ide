package com.tulin.v8.ide.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.tulin.v8.ide.StudioPlugin;

@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public class ImageManager {
	static Map<String, Image> a = new HashMap();
	static Map<String, ImageDescriptor> b = new HashMap();

	public static Image getIconByFileName(String paramString) {
		Image localImage = (Image) a.get(paramString);
		if (localImage == null)
			try {
				URL localURL = null;
				File localFile = new File(paramString);
				if (localFile.isAbsolute())
					localURL = localFile.toURL();
				else if (paramString.indexOf("/") != -1)
					localURL = Platform.getBundle(StudioPlugin.PLUGIN_ID).getEntry(paramString);
				else
					localURL = Platform.getBundle(StudioPlugin.PLUGIN_ID).getEntry("icons/xui_icons/" + paramString);
				ImageDescriptor localImageDescriptor = ImageDescriptor.createFromURL(localURL);
				localImage = localImageDescriptor.createImage();
				a.put(paramString, localImage);
			} catch (Exception localException) {
				try {
					return new Image(null, new FileInputStream(
							"F:/eclipse-SDK-3.4.2-win32/eclipse/workspace/Plugin5.1/icons/xui_icons/" + paramString));
				} catch (FileNotFoundException localFileNotFoundException) {
					localFileNotFoundException.printStackTrace();
				}
			}
		return localImage;
	}

	public static ImageDescriptor getImageDescriptor(String paramString) {
		ImageDescriptor localImageDescriptor1 = (ImageDescriptor) b.get(paramString);
		if (localImageDescriptor1 == null)
			try {
				URL localURL = null;
				File localFile = new File(paramString);
				if (localFile.isAbsolute())
					localURL = localFile.toURL();
				else if (paramString.indexOf("/") != -1)
					localURL = Platform.getBundle(StudioPlugin.PLUGIN_ID).getEntry(paramString);
				else
					localURL = Platform.getBundle(StudioPlugin.PLUGIN_ID).getEntry("icons/xui_icons/" + paramString);
				ImageDescriptor localImageDescriptor2 = ImageDescriptor.createFromURL(localURL);
				b.put(paramString, localImageDescriptor2);
				return localImageDescriptor2;
			} catch (Exception localException) {
			}
		return localImageDescriptor1;
	}
}
