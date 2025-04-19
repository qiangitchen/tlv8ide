package com.tulin.v8.swt.chromium;

import java.util.Random;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import com.alibaba.fastjson.JSONArray;

/**
 * 浏览器与JS交互函数定义
 * 
 * @author 陈乾
 */
public class BrowserFunction {
	Browser browser;
	String name;
	String functionString;
	boolean isEvaluate, top;
	String token;

	public BrowserFunction(Browser browser, String name) {
		this(browser, name, true, null, true, false);
	}

	public BrowserFunction(Browser browser, String name, boolean top, String[] frameNames) {
		this(browser, name, top, frameNames, true, false);
	}

	public BrowserFunction(Browser browser, String name, boolean request) {
		this(browser, name, true, null, true, request);
	}

	public BrowserFunction(Browser browser, String name, boolean top, String[] frameNames, boolean create,
			boolean request) {
		this.browser = browser;
		this.name = name;
		this.top = top;
		if (request) {
			CefMessageRouter.CefMessageRouterConfig config = new CefMessageRouter.CefMessageRouterConfig();
			config.jsQueryFunction = name;
			config.jsCancelFunction = "Cancel_" + name;
			CefMessageRouter msgRouter = CefMessageRouter.create(config);
			msgRouter.addHandler(new CefMessageRouterHandlerAdapter() {
				@Override
				public boolean onQuery(CefBrowser browser, CefFrame frame, long queryId, String request,
						boolean persistent, CefQueryCallback callback) {
					try {
						Object re = function(new Object[] { request });
						if (re != null) {
							callback.success(String.valueOf(re));
							return true;
						}
						function(new Object[] { request }, callback);
					} catch (Exception e) {
						callback.failure(e.hashCode(), e.getMessage());
					}
					return true;
				}
			}, false);
			browser.getCefClient().addMessageRouter(msgRouter);
		} else {
			Random random = new Random();
			byte[] bytes = new byte[8];
			random.nextBytes(bytes);
			StringBuilder buffer = new StringBuilder();
			for (byte b : bytes) {
				buffer.append(Integer.toHexString(b & 0xff));
			}
			token = buffer.toString();
			if (create) {
				CefMessageRouter.CefMessageRouterConfig config = new CefMessageRouter.CefMessageRouterConfig();
				config.jsQueryFunction = name + token;
				config.jsCancelFunction = "Cancel_" + name + token;
				CefMessageRouter msgRouter = CefMessageRouter.create(config);
				msgRouter.addHandler(new CefMessageRouterHandlerAdapter() {
					@Override
					public boolean onQuery(CefBrowser browser, CefFrame frame, long queryId, String request,
							boolean persistent, CefQueryCallback callback) {
						try {
							Object[] arguments = new Object[0];
							try {
								JSONArray params = JSONArray.parseArray(request);
								arguments = new Object[params.size()];
								for (int i = 0; i < params.size(); i++) {
									arguments[i] = params.get(i);
								}
							} catch (Exception e) {
								arguments = new Object[] { request };
							}
							Object re = function(arguments);
							if (re != null) {
								callback.success(String.valueOf(re));
								return true;
							}
							function(arguments, callback);
						} catch (Exception e) {
							callback.failure(e.hashCode(), e.getMessage());
						}
						return true;
					}
				}, false);
				browser.getCefClient().addMessageRouter(msgRouter);
				browser.addLoadListener(new LoadListenerAdapter() {
					@Override
					public void onLoadStart(LoadEvent event) {
						try {
							browser.webBrowser.createFunction(BrowserFunction.this);
						} catch (Exception e) {
							System.err.println("函数：" + name + ", 注册失败!");
						}
					}
				});
			}
		}
	}

	public Browser getBrowser() {
		return browser;
	}

	public String getName() {
		return name;
	}

	public void function(Object[] arguments, CefQueryCallback callback) {

	}

	public Object function(Object[] arguments) {
		return null;
	}

	public void dispose() {
		dispose(true);
	}

	void dispose(boolean remove) {
		browser = null;
		name = functionString = null;
	}

	public boolean isDisposed() {
		return true;
	}
	
	protected void alert(String message) {
		getBrowser().getDisplay().asyncExec(() -> {
			MessageBox alerts = new MessageBox(getBrowser().getShell(), SWT.ICON_INFORMATION);
			alerts.setText("提示");
			alerts.setMessage(message);
			alerts.open();
		});
	}

}
