package com.tulin.v8.swt.chromium;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class WindowEvent extends TypedEvent {
	private static final long serialVersionUID = 3699087430791947715L;

	/**
	 * Specifies whether the platform requires the user to provide a
	 * <code>Browser</code> to handle the new window.
	 *
	 * @since 3.1
	 */
	public boolean required;

	/**
	 * <code>Browser</code> provided by the application.
	 */
	public Browser browser;

	/**
	 * Requested location for the <code>Shell</code> hosting the
	 * <code>Browser</code>. It is <code>null</code> if no location has been
	 * requested.
	 */
	public Point location;

	/**
	 * Requested <code>Browser</code> size. The client area of the
	 * <code>Shell</code> hosting the <code>Browser</code> should be large enough to
	 * accommodate that size. It is <code>null</code> if no size has been requested.
	 */
	public Point size;

	/**
	 * Specifies whether the <code>Shell</code> hosting the <code>Browser</code>
	 * should display an address bar.
	 *
	 * @since 3.1
	 */
	public boolean addressBar;

	/**
	 * Specifies whether the <code>Shell</code> hosting the <code>Browser</code>
	 * should display a menu bar. Note that this is always <code>true</code> on OS
	 * X.
	 *
	 * @since 3.1
	 */
	public boolean menuBar;

	/**
	 * Specifies whether the <code>Shell</code> hosting the <code>Browser</code>
	 * should display a status bar.
	 *
	 * @since 3.1
	 */
	public boolean statusBar;

	/**
	 * Specifies whether the <code>Shell</code> hosting the <code>Browser</code>
	 * should display a tool bar.
	 *
	 * @since 3.1
	 */
	public boolean toolBar;

	public CefBrowser cefBrowser;

	public CefFrame cefFrame;

	/**
	 * open url
	 */
	public String url;

	/**
	 * open page name
	 */
	public String name;

	public WindowEvent(Widget widget) {
		super(widget);
	}

	@Override
	public String toString() {
		String string = super.toString();
		return string.substring(0, string.length() - 1) // remove trailing '}'
				+ " required=" + required + " browser=" + browser + " location=" + location + " size=" + size
				+ " addressBar=" + addressBar + " menuBar=" + menuBar + " statusBar=" + statusBar + " toolBar="
				+ toolBar + " cefBrowser=" + cefBrowser + " cefFrame=" + cefFrame + " url=" + url + " name=" + name
				+ "}";
	}
}
