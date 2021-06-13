package com.tulin.v8.core.utils;

/**
 * 本地浏览器
 * 
 * @author chenqian
 *
 */
public class LocalBrowser {
	public static boolean openUrl(String url) {
		String os = System.getProperty("os.name").toLowerCase();
		Runtime rt = Runtime.getRuntime();
		try {
			if (os.indexOf("win") >= 0) {
				// this doesn't support showing urls in the form of "page.html#nameLink"
				rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
			} else if (os.indexOf("mac") >= 0) {
				rt.exec("open " + url);
			} else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
				// Do a best guess on unix until we get a platform independent way
				// Build a list of browsers to try, in this order.
				String[] browsers = { "epiphany", "firefox", "mozilla", "konqueror", "netscape", "opera", "links",
						"lynx" };
				// Build a command string which looks like "browser1 "url" || browser2 "url"
				// ||..."
				StringBuffer cmd = new StringBuffer();
				for (int i = 0; i < browsers.length; i++)
					cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \"" + url + "\" ");
				rt.exec(new String[] { "sh", "-c", cmd.toString() });
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
