package com.tulin.v8.swt.chromium;

import java.util.Map;

import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefContextMenuParams;
import org.cef.callback.CefMenuModel;
import org.cef.handler.CefContextMenuHandlerAdapter;
import org.cef.handler.CefDisplayHandler;
import org.cef.handler.CefKeyboardHandler;
import org.cef.network.CefPostData;
import org.eclipse.swt.*;
import org.eclipse.swt.browser.AuthenticationListener;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

/**
 * 浏览器组件{为了保持与SWT内置浏览器一样的使用习惯而定义的}
 * 
 * @author chenqian
 */
public class Browser extends Composite {
	private org.eclipse.swt.browser.Browser swtBrowser;

	Chromiu webBrowser;
	int userStyle;
	boolean isClosing;

	static int DefaultType = SWT.DEFAULT;

	static final String NO_INPUT_METHOD = "org.eclipse.swt.internal.gtk.noInputMethod"; //$NON-NLS-1$

	public Browser(Composite parent, int style) {
		this(parent, style, "about:blank");
	}

	public Browser(Composite parent, String startUrl) {
		this(parent, SWT.NONE, startUrl);
	}

	public Browser(Composite parent, int style, String startUrl) {
		super(parent, style);
		userStyle = style;

		String platform = SWT.getPlatform();
		if ("gtk".equals(platform)) { //$NON-NLS-1$
			parent.getDisplay().setData(NO_INPUT_METHOD, null);
		}

		try {
			webBrowser = new Chromiu(startUrl);
			webBrowser.setBrowser(this);
			webBrowser.create(this, style);
		} catch (Exception | Error e) {
			setLayout(new FillLayout());
			swtBrowser = new org.eclipse.swt.browser.Browser(this, SWT.NONE);
		}
	}

	public CefClient getCefClient() {
		return webBrowser.getCefClient();
	}

	public CefBrowser getCefBrowser() {
		return webBrowser.getCefBrowser();
	}

	public org.eclipse.swt.browser.Browser getSWTBrowser() {
		return swtBrowser;
	}

	@Override
	protected void checkWidget() {

	}

	public static void clearSessions() {
		WebBrowser.clearSessions();
	}

	public static String getCookie(String name, String url) {
		if (name == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (url == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		return WebBrowser.GetCookie(name, url);
	}

	public static boolean setCookie(String value, String url) {
		if (value == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (url == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		return WebBrowser.SetCookie(value, url, true);
	}

	public void addAuthenticationListener(AuthenticationListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (swtBrowser != null) {
			swtBrowser.addAuthenticationListener(listener);
		}
		webBrowser.addAuthenticationListener(listener);
	}

	public void addCloseWindowListener(CloseWindowListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (swtBrowser != null) {
			swtBrowser.addCloseWindowListener(listener);
		}
		webBrowser.addCloseWindowListener(listener);
	}

	public void addLocationListener(LocationListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		webBrowser.addLocationListener(listener);
	}

	public void addOpenWindowListener(OpenWindowListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (swtBrowser != null) {
			swtBrowser.addOpenWindowListener(listener);
		}
		webBrowser.addOpenWindowListener(listener);
	}

	public void addProgressListener(ProgressListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (swtBrowser != null) {
			swtBrowser.addProgressListener(listener);
		}
		webBrowser.addProgressListener(listener);
	}

	public void addStatusTextListener(StatusTextListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (swtBrowser != null) {
			swtBrowser.addStatusTextListener(listener);
		}
		webBrowser.addStatusTextListener(listener);
	}

	public void addTitleListener(TitleListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (swtBrowser != null) {
			swtBrowser.addTitleListener(listener);
		}
		webBrowser.addTitleListener(listener);
	}

	public void addLoadListener(LoadListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		webBrowser.addLoadListener(listener);
	}

	public void addVisibilityWindowListener(VisibilityWindowListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (swtBrowser != null) {
			swtBrowser.addVisibilityWindowListener(listener);
		}
		webBrowser.addVisibilityWindowListener(listener);
	}

	public void addCefDisplayHandler(CefDisplayHandler cefDisplayHandler) {
		checkWidget();
		if (cefDisplayHandler == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		webBrowser.addCefDisplayHandler(cefDisplayHandler);
	}

	public void addCefKeyboardHandler(CefKeyboardHandler cefKeyboardHandler) {
		checkWidget();
		if (cefKeyboardHandler == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		webBrowser.addCefKeyboardHandler(cefKeyboardHandler);
	}

	public void removeCefKeyboardHandler(CefKeyboardHandler cefKeyboardHandler) {
		webBrowser.removeCefKeyboardHandler(cefKeyboardHandler);
	}

	public boolean back() {
		checkWidget();
		if (swtBrowser != null) {
			return swtBrowser.back();
		}
		return webBrowser.back();
	}

	@Override
	protected void checkSubclass() {

	}

	public boolean execute(String script) {
		checkWidget();
		if (script == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (swtBrowser != null) {
			return swtBrowser.execute(script);
		}
		return webBrowser.execute(script);
	}

	public boolean close() {
		checkWidget();
		if (swtBrowser != null) {
			return swtBrowser.close();
		}
		if (webBrowser.close()) {
			isClosing = true;
			dispose();
			isClosing = false;
			return true;
		}
		return false;
	}

	public Object evaluate(String script) throws SWTException {
		checkWidget();
		if (swtBrowser != null) {
			return swtBrowser.evaluate(script);
		}
		return evaluate(script, false);
	}

	public Object evaluate(String script, boolean trusted) throws SWTException {
		checkWidget();
		if (script == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (swtBrowser != null) {
			return swtBrowser.evaluate(script, trusted);
		}
		return webBrowser.evaluate(script, trusted);
	}

	public boolean forward() {
		checkWidget();
		if (swtBrowser != null) {
			return swtBrowser.forward();
		}
		return webBrowser.forward();
	}

	public String getBrowserType() {
		checkWidget();
		if (swtBrowser != null) {
			return swtBrowser.getBrowserType();
		}
		return webBrowser.getBrowserType();
	}

	public boolean getJavascriptEnabled() {
		checkWidget();
		if (swtBrowser != null) {
			return swtBrowser.getJavascriptEnabled();
		}
		return webBrowser.jsEnabledOnNextPage;
	}

	@Override
	public int getStyle() {
		/*
		 * If SWT.BORDER was specified at creation time then getStyle() should answer it
		 * even though it is removed for IE on win32 in checkStyle().
		 */
		return super.getStyle() | (userStyle & SWT.BORDER);
	}

	public String getText() {
		checkWidget();
		if (swtBrowser != null) {
			return swtBrowser.getText();
		}
		return webBrowser.getText();
	}

	public String getUrl() {
		checkWidget();
		if (swtBrowser != null) {
			return swtBrowser.getUrl();
		}
		return webBrowser.getUrl();
	}

	@Deprecated
	public Object getWebBrowser() {
		checkWidget();
		if (swtBrowser != null) {
			return swtBrowser.getWebBrowser();
		}
		return webBrowser.getWebBrowser();
	}

	public boolean isBackEnabled() {
		checkWidget();
		if (swtBrowser != null) {
			return swtBrowser.isBackEnabled();
		}
		return webBrowser.isBackEnabled();
	}

	@Override
	public boolean isFocusControl() {
		checkWidget();
		if (swtBrowser != null) {
			return swtBrowser.isFocusControl();
		}
		if (webBrowser.isFocusControl())
			return true;
		return super.isFocusControl();
	}

	public boolean isForwardEnabled() {
		checkWidget();
		if (swtBrowser != null) {
			return swtBrowser.isForwardEnabled();
		}
		return webBrowser.isForwardEnabled();
	}

	public void refresh() {
		checkWidget();
		if (swtBrowser != null) {
			swtBrowser.refresh();
		}
		webBrowser.refresh();
	}

	public void removeAuthenticationListener(AuthenticationListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (swtBrowser != null) {
			swtBrowser.removeAuthenticationListener(listener);
		}
		webBrowser.removeAuthenticationListener(listener);
	}

	public void removeCloseWindowListener(CloseWindowListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (swtBrowser != null) {
			swtBrowser.removeCloseWindowListener(listener);
		}
		webBrowser.removeCloseWindowListener(listener);
	}

	public void removeLocationListener(LocationListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (swtBrowser != null) {
			swtBrowser.removeLocationListener(listener);
		}
		webBrowser.removeLocationListener(listener);
	}

	public void removeOpenWindowListener(OpenWindowListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (swtBrowser != null) {
			swtBrowser.removeOpenWindowListener(listener);
		}
		webBrowser.removeOpenWindowListener(listener);
	}

	public void removeProgressListener(ProgressListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (swtBrowser != null) {
			swtBrowser.removeProgressListener(listener);
		}
		webBrowser.removeProgressListener(listener);
	}

	public void removeStatusTextListener(StatusTextListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (swtBrowser != null) {
			swtBrowser.removeStatusTextListener(listener);
		}
		webBrowser.removeStatusTextListener(listener);
	}

	public void removeTitleListener(TitleListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (swtBrowser != null) {
			swtBrowser.removeTitleListener(listener);
		}
		webBrowser.removeTitleListener(listener);
	}

	public void removeVisibilityWindowListener(VisibilityWindowListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (swtBrowser != null) {
			swtBrowser.removeVisibilityWindowListener(listener);
		}
		webBrowser.removeVisibilityWindowListener(listener);
	}

	public void removeCefDisplayHandler(CefDisplayHandler cefDisplayHandler) {
		checkWidget();
		if (cefDisplayHandler == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		webBrowser.removeCefDisplayHandler(cefDisplayHandler);
	}

	public void setJavascriptEnabled(boolean enabled) {
		checkWidget();
		if (swtBrowser != null) {
			swtBrowser.setJavascriptEnabled(enabled);
		}
		webBrowser.jsEnabledOnNextPage = enabled;
	}

	public boolean setText(String html) {
		checkWidget();
		if (swtBrowser != null) {
			return swtBrowser.setText(html);
		}
		return setText(html, true);
	}

	public boolean setText(String html, boolean trusted) {
		checkWidget();
		if (html == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (swtBrowser != null) {
			return swtBrowser.setText(html, trusted);
		}
		return webBrowser.setText(html, trusted);
	}

	public boolean setUrl(String url) {
		checkWidget();
		if (swtBrowser != null) {
			return swtBrowser.setUrl(url);
		}
		return webBrowser.setUrl(url);
	}

	@Deprecated
	public boolean setUrl(String url, String postData, String[] headers) {
		checkWidget();
		if (url == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (swtBrowser != null) {
			return swtBrowser.setUrl(url, postData, headers);
		}
		return webBrowser.setUrl(url, postData, headers);
	}

	public boolean setUrl(String url, CefPostData postData, Map<String, String> headerMap) {
		checkWidget();
		return webBrowser.setUrl(url, postData, headerMap);
	}

	@Override
	public void setMenu(Menu menu) {
		super.setMenu(menu);
		if (swtBrowser != null) {
			swtBrowser.setMenu(menu);
			return;
		}
		try {
			webBrowser.getCefClient().removeContextMenuHandler();
			webBrowser.getCefClient().addContextMenuHandler(new CefContextMenuHandlerAdapter() {
				@Override
				public void onBeforeContextMenu(CefBrowser browser, CefFrame frame, CefContextMenuParams params,
						CefMenuModel model) {
					// 清除菜单项
					model.clear();
					if (getMenu() != null) {
						getDisplay().asyncExec(() -> {
							getMenu().setVisible(true);
						});
					}
				}
			});
		} catch (Exception e) {
		}
	}

	public void stop() {
		checkWidget();
		if (swtBrowser != null) {
			swtBrowser.stop();
			return;
		}
		webBrowser.stop();
	}

	@Override
	public void dispose() {
		try {
//			webBrowser.stop();
			webBrowser.getCefClient().dispose();
		} catch (Exception e) {
		}
		super.dispose();
	}
}
