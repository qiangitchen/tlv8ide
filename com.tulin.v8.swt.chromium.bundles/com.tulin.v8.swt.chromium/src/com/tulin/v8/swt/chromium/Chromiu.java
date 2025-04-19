package com.tulin.v8.swt.chromium;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.swing.SwingUtilities;

import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.callback.CefAuthCallback;
import org.cef.callback.CefJSDialogCallback;
import org.cef.callback.CefQueryCallback;
import org.cef.callback.CefStringVisitor;
import org.cef.handler.CefDisplayHandler;
import org.cef.handler.CefDisplayHandlerAdapter;
import org.cef.handler.CefJSDialogHandlerAdapter;
import org.cef.handler.CefKeyboardHandler;
import org.cef.handler.CefKeyboardHandlerAdapter;
import org.cef.handler.CefLifeSpanHandlerAdapter;
import org.cef.handler.CefLoadHandlerAdapter;
import org.cef.handler.CefMessageRouterHandlerAdapter;
import org.cef.handler.CefRequestHandlerAdapter;
import org.cef.misc.BoolRef;
import org.cef.network.CefPostData;
import org.cef.network.CefPostDataElement;
import org.cef.network.CefRequest;
import org.cef.network.CefRequest.TransitionType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.AuthenticationEvent;
import org.eclipse.swt.browser.AuthenticationListener;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;

import com.tulin.v8.cef.CefBrowserManager;
import com.tulin.v8.web.utils.WebappManager;

/**
 * CefBrowser的嵌套和调用类
 * 
 * @author chenqian
 */
public class Chromiu extends WebBrowser {
	private Frame awtframe;
	private CefClient client;
	private CefBrowser cefbrowser;
	private Component browerUI;

	protected String startUrl;

	private Composite parent = null;
	private ProgressBar progressBar;

	public Chromiu(String startUrl) {
		this.startUrl = startUrl;
	}

	private void createCefbrowser(String url) {
		if (cefbrowser != null) {
			cefbrowser.close(true);
		}
		this.cefbrowser = CefBrowserManager.createCefBrowser(url, false, false);
		this.client = cefbrowser.getClient();
		this.browerUI = cefbrowser.getUIComponent();
		client.removeLoadHandler();
		client.addLoadHandler(new CefLoadHandlerAdapter() {
			@Override
			public void onLoadingStateChange(CefBrowser browser, boolean isLoading, boolean canGoBack,
					boolean canGoForward) {
				showProgressBar(isLoading);
				LoadEvent levent = new LoadEvent(parent);
				levent.browser = browser;
				levent.isLoading = isLoading;
				levent.canGoBack = canGoBack;
				levent.canGoForward = canGoForward;
				for (LoadListener loadListener : loadListeners) {
					loadListener.onLoadingStateChange(levent);
				}
			}

			@Override
			public void onLoadStart(CefBrowser browser, CefFrame frame, TransitionType transitionType) {
				ProgressEvent pevent = new ProgressEvent(parent);
				pevent.current = 0;
				pevent.total = 100;
				for (ProgressListener progressListener : progressListeners) {
					progressListener.changed(pevent);
				}
				LoadEvent levent = new LoadEvent(parent);
				levent.browser = browser;
				levent.frame = frame;
				levent.transitionType = transitionType;
				for (LoadListener loadListener : loadListeners) {
					loadListener.onLoadStart(levent);
				}
			}

			@Override
			public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) {
				ProgressEvent pevent = new ProgressEvent(parent);
				pevent.current = 100;
				pevent.total = 100;
				for (ProgressListener progressListener : progressListeners) {
					progressListener.completed(pevent);
				}
				LoadEvent levent = new LoadEvent(parent);
				levent.browser = browser;
				levent.frame = frame;
				levent.httpStatusCode = httpStatusCode;
				for (LoadListener loadListener : loadListeners) {
					loadListener.onLoadEnd(levent);
				}
			}

			@Override
			public void onLoadError(CefBrowser browser, CefFrame frame, ErrorCode errorCode, String errorText,
					String failedUrl) {
				LoadEvent levent = new LoadEvent(parent);
				levent.browser = browser;
				levent.frame = frame;
				levent.errorCode = errorCode;
				levent.errorText = errorText;
				levent.failedUrl = failedUrl;
				for (LoadListener loadListener : loadListeners) {
					loadListener.onLoadError(levent);
				}
			}
		});
		client.removeLifeSpanHandler();
		client.addLifeSpanHandler(new CefLifeSpanHandlerAdapter() {
			@Override
			public boolean onBeforePopup(CefBrowser browser, CefFrame frame, String target_url,
					String target_frame_name) {
				if (openWindowListeners.length > 0) {
					WindowEvent wevent = new WindowEvent(parent);
					wevent.required = false;
					wevent.cefBrowser = browser;
					wevent.cefFrame = frame;
					wevent.url = target_url;
					wevent.name = target_frame_name;
					wevent.browser = Chromiu.this.browser;
					for (OpenWindowListener openWindowListener : openWindowListeners) {
						openWindowListener.open(wevent);
					}
					return wevent.required;
				} else {
					try {
						Chromiu.this.browser.getDisplay().asyncExec(() -> {
							new BrowserDialog(Chromiu.this.browser.getShell(), target_frame_name, target_url).open();
						});
						return true;// 返回true表示取消弹出窗口
					} catch (Exception ignored) {
					}
				}
				return false;
			}

			@Override
			public boolean doClose(CefBrowser browser) {
				WindowEvent wevent = new WindowEvent(parent);
				wevent.required = false;
				wevent.cefBrowser = browser;
				wevent.url = browser.getURL();
				wevent.browser = Chromiu.this.browser;
				for (CloseWindowListener closeWindowListener : closeWindowListeners) {
					closeWindowListener.close(wevent);
				}
				return wevent.required;
			}
		});
		// 处理键盘事件
		client.removeKeyboardHandler();
		// addCefKeyboardHandler(new KeyboardHandler());
		client.addKeyboardHandler(new CefKeyboardHandlerAdapter() {
			@Override
			public boolean onPreKeyEvent(CefBrowser browser, CefKeyEvent event, BoolRef is_keyboard_shortcut) {
				boolean res = false;
				for (CefKeyboardHandler cefKeyboardHandler : cefKeyboardHandlers) {
					boolean r = cefKeyboardHandler.onPreKeyEvent(browser, event, is_keyboard_shortcut);
					if (r) {
						res = r;
					}
				}
				return res;
			}

			public boolean onKeyEvent(CefBrowser browser, CefKeyEvent event) {
				boolean res = false;
				if (event.type == CefKeyEvent.EventType.KEYEVENT_KEYUP) {
					// System.out.println("windows_key_code:" + cefKeyEvent.windows_key_code);
					switch (event.windows_key_code) {
					// F5 刷新
					case 116:
						browser.reload();
						break;
					// F12 开发者工具
					case 123:
						devToolsShow(browser);
						break;
					default:
						res = false;
					}
				}
				for (CefKeyboardHandler cefKeyboardHandler : cefKeyboardHandlers) {
					boolean r = cefKeyboardHandler.onKeyEvent(browser, event);
					if (r) {
						res = r;
					}
				}
				return res;
			}
		});
		client.removeDisplayHandler();
		client.addDisplayHandler(new CefDisplayHandlerAdapter() {
			@Override
			public void onTitleChange(CefBrowser browser, String title) {
				TitleEvent tevent = new TitleEvent(parent);
				tevent.title = title;
				Chromiu.this.browser.getDisplay().asyncExec(() -> {
					for (TitleListener titleListener : titleListeners) {
						titleListener.changed(tevent);
					}
				});
				for (CefDisplayHandler cefDisplayHandlers : cefDisplayHandlers) {
					cefDisplayHandlers.onTitleChange(browser, title);
				}
			}

			@Override
			public void onAddressChange(CefBrowser browser, CefFrame frame, String url) {
				LocationEvent levent = new LocationEvent(parent);
				levent.location = url;
				Chromiu.this.browser.getDisplay().asyncExec(() -> {
					for (LocationListener locationListener : locationListeners) {
						locationListener.changing(levent);
						locationListener.changed(levent);
					}
				});
				for (CefDisplayHandler cefDisplayHandlers : cefDisplayHandlers) {
					cefDisplayHandlers.onAddressChange(browser, frame, url);
				}
			}

			@Override
			public boolean onTooltip(CefBrowser browser, String text) {
				boolean res = false;
				for (CefDisplayHandler cefDisplayHandlers : cefDisplayHandlers) {
					if (cefDisplayHandlers.onTooltip(browser, text)) {
						res = true;
					}
				}
				return res;
			}

			@Override
			public void onStatusMessage(CefBrowser browser, String value) {
				StatusTextEvent sevent = new StatusTextEvent(parent);
				sevent.text = value;
				Chromiu.this.browser.getDisplay().asyncExec(() -> {
					for (StatusTextListener statusTextListener : statusTextListeners) {
						statusTextListener.changed(sevent);
					}
				});
				for (CefDisplayHandler cefDisplayHandlers : cefDisplayHandlers) {
					cefDisplayHandlers.onStatusMessage(browser, value);
				}
			}

			@Override
			public boolean onConsoleMessage(CefBrowser browser, CefSettings.LogSeverity level, String message,
					String source, int line) {
				boolean res = false;
				for (CefDisplayHandler cefDisplayHandlers : cefDisplayHandlers) {
					if (cefDisplayHandlers.onConsoleMessage(browser, level, message, source, line)) {
						res = true;
					}
				}
				return res;
			}

			@Override
			public boolean onCursorChange(CefBrowser browser, int cursorType) {
				boolean res = false;
				for (CefDisplayHandler cefDisplayHandlers : cefDisplayHandlers) {
					if (cefDisplayHandlers.onCursorChange(browser, cursorType)) {
						res = true;
					}
				}
				return res;
			}

//			@Override
//			public void onFullscreenModeChange(CefBrowser browser, boolean fullscreen) {
//				for (CefDisplayHandler cefDisplayHandlers : cefDisplayHandlers) {
//					cefDisplayHandlers.onFullscreenModeChange(browser, fullscreen);
//				}
//			}
		});
		client.removeRequestHandler();
		client.addRequestHandler(new CefRequestHandlerAdapter() {
			@Override
			public boolean getAuthCredentials(CefBrowser browser, String origin_url, boolean isProxy, String host,
					int port, String realm, String scheme, CefAuthCallback callback) {
				if (authenticationListeners.length > 0) {
					AuthenticationEvent aevent = new AuthenticationEvent(parent);
					aevent.location = origin_url;
					for (AuthenticationListener authenticationListeners : authenticationListeners) {
						authenticationListeners.authenticate(aevent);
					}
					if (aevent.doit) {
						callback.Continue(aevent.user, aevent.password);
					} else {
						callback.cancel();
					}
					return true;
				}
				return super.getAuthCredentials(browser, origin_url, isProxy, host, port, realm, scheme, callback);
			}
		});
		// 处理JS消息提示框
		client.removeJSDialogHandler();
		client.addJSDialogHandler(new CefJSDialogHandlerAdapter() {
			@Override
			public boolean onJSDialog(CefBrowser browser, String origin_url, JSDialogType dialog_type,
					String message_text, String default_prompt_text, CefJSDialogCallback callback,
					BoolRef suppress_message) {
				if (dialog_type == JSDialogType.JSDIALOGTYPE_ALERT) {
					parent.getDisplay().asyncExec(() -> {
						MessageBox alerts = new MessageBox(parent.getShell(), SWT.ICON_INFORMATION);
						alerts.setText("提示");
						alerts.setMessage(message_text);
						alerts.open();
						callback.Continue(true, null);
					});
				} else if (dialog_type == JSDialogType.JSDIALOGTYPE_CONFIRM) {
					parent.getDisplay().asyncExec(() -> {
						MessageBox confirms = new MessageBox(parent.getShell(),
								SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
						confirms.setText("确认");
						confirms.setMessage(message_text);
						int c = confirms.open();
						callback.Continue(c == SWT.OK, null);
					});
				} else {
					return false;
				}
				return true;
			}
		});
		// 注册通用JS回调函数
		CefMessageRouter.CefMessageRouterConfig config = new CefMessageRouter.CefMessageRouterConfig();
		config.jsQueryFunction = EXECUTE_ID;
		config.jsCancelFunction = "Cancel_" + EXECUTE_ID;
		CefMessageRouter msgRouter = CefMessageRouter.create(config);
		msgRouter.addHandler(new CefMessageRouterHandlerAdapter() {
			@Override
			public boolean onQuery(CefBrowser browser, CefFrame frame, long queryId, String request, boolean persistent,
					CefQueryCallback callback) {
				evaluateResult = request;
				return true;
			}
		}, true);
		client.addMessageRouter(msgRouter);
		if (awtframe != null) {
			awtframe.removeAll();
			awtframe.add(browerUI, BorderLayout.CENTER);
			startUrl = url;
		}
		if (browser != null) {// 如果浏览器对象已经存在，需要重新注册右键菜单.
			browser.setMenu(browser.getMenu());
		}
	}

	private DevToolsDialog devToolsDialog;

	private void devToolsShow(CefBrowser cefBrowser) {
		if (devToolsDialog != null && !devToolsDialog.isClosed()) {
			devToolsDialog.setActivte();
			return;
		}
		parent.getDisplay().asyncExec(() -> {
			devToolsDialog = new DevToolsDialog(parent.getShell(), cefBrowser);
			devToolsDialog.open();
		});
	}

	@Override
	public void create(Composite parent, int style) {
		this.parent = parent;
		GridLayout mlay = new GridLayout();
		mlay.horizontalSpacing = 0;
		mlay.verticalSpacing = 0;
		mlay.marginLeft = 0;
		mlay.marginTop = 0;
		mlay.marginRight = 0;
		mlay.marginBottom = 0;
		mlay.marginWidth = 0;
		mlay.marginHeight = 0;
		parent.setLayout(mlay);
		GridData ply = new GridData(GridData.FILL_HORIZONTAL);
		ply.heightHint = 2;
		progressBar = new ProgressBar(parent, SWT.HORIZONTAL | SWT.SMOOTH | SWT.INDETERMINATE);
		progressBar.setLayoutData(ply);
		progressBar.setVisible(false);
		Composite composite = new Composite(parent, SWT.EMBEDDED);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new FillLayout());
		createCefbrowser(startUrl);
		awtframe = SWT_AWT.new_Frame(composite);
		awtframe.setLayout(new BorderLayout());
		awtframe.add(browerUI, BorderLayout.CENTER);
	}

	public void showProgressBar(boolean isShow) {
		parent.getDisplay().asyncExec(() -> {
			progressBar.setVisible(isShow);
		});
	}

	protected CefClient getCefClient() {
		return client;
	}

	protected CefBrowser getCefBrowser() {
		return cefbrowser;
	}

	@Override
	public boolean back() {
		boolean b = cefbrowser.canGoBack();
		cefbrowser.goBack();
		return b;
	}

	@Override
	public boolean execute(String script) {
		cefbrowser.executeJavaScript(script, "", 1);
		return false;
	}

	@Override
	public boolean forward() {
		boolean f = cefbrowser.canGoForward();
		cefbrowser.goForward();
		return f;
	}

	@Override
	public String getBrowserType() {
		return null;
	}

	@Override
	public String getText() {
		return getText(3000);
	}

	private String text = null;

	/**
	 * 获取浏览的HTML内容
	 * 
	 * @param timeOut 等待超时时间（单位：毫秒）
	 * @return
	 */
	public String getText(long timeOut) {
		text = null;
		long time = new Date().getTime();
		try {
			while ((new Date().getTime() - time < timeOut) && !cefbrowser.hasDocument()) {
				Thread.sleep(1);
			}
		} catch (Exception e) {
		}
		cefbrowser.getSource(new CefStringVisitor() {
			@Override
			public void visit(String string) {
				text = string;
			}
		});
		time = new Date().getTime();
		try {
			while (text == null && (new Date().getTime() - time < timeOut)) {
				Thread.sleep(1);
			}
		} catch (Exception e) {
		}
		return text;
	}

	@Override
	public String getUrl() {
		return cefbrowser.getURL();
	}

	@Override
	public boolean isBackEnabled() {
		return cefbrowser.canGoBack();
	}

	@Override
	public boolean isForwardEnabled() {
		return cefbrowser.canGoForward();
	}

	@Override
	public void refresh() {
		cefbrowser.reload();
	}

	private String textTemp = null;

	/**
	 * 清理临时文件
	 */
	private void clearTemp() {
		if (textTemp != null) {
			try {
				File file = new File(textTemp);
				if (file.exists()) {
					file.delete();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 加载HTML源码
	 */
	@Override
	public boolean setText(String html, boolean trusted) {
		if (trusted) {
			try {
				clearTemp();
				String userHomeDirectory = System.getProperty("user.home");
				String installDir = userHomeDirectory + File.separator + ".browser";
				File thtml = new File(installDir + File.separator + UUID.randomUUID().toString() + ".html");
				textTemp = thtml.getAbsolutePath();
				String url = WebappManager.filePathToURL(textTemp);
				setUrl(url);
				return true;
			} catch (Exception e) {
			}
		}
		return false;
	}

	/**
	 * 根据地址加载网页
	 * 
	 * @param url
	 * @return
	 */
	public synchronized boolean setUrl(String url) {
		SwingUtilities.invokeLater(() -> {
			if (url != null) {
				if (!url.equals(startUrl)) {
					createCefbrowser(url);
				} else if (cefbrowser != null) {
					cefbrowser.loadURL(url);
				}
			}
		});
		return true;
	}

	/**
	 * 访问指定网址并提交数据
	 */
	@Override
	public boolean setUrl(String url, String postData, String[] headers) {
		try {
			CefPostDataElement element = CefPostDataElement.create();
			if (postData == null) {
				element.setToEmpty();
			} else {
				byte[] p = postData.getBytes();
				element.setToBytes(p.length, p);
			}
			CefPostData pData = CefPostData.create();
			pData.addElement(element);
			Map<String, String> headerMap = new HashMap<>();
			if (headers != null) {
				for (String h : headers) {
					String key = h.substring(0, h.indexOf(":"));
					headerMap.put(key, h.substring(h.indexOf(":") + 1));
				}
			}
			setUrl(url, pData, headerMap);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 访问指定网址并提交数据
	 * 
	 * @param url
	 * @param postData
	 * @param headerMap
	 * @return
	 */
	public boolean setUrl(String url, CefPostData postData, Map<String, String> headerMap) {
		cefbrowser.getClient().addRequestHandler(new CefRequestHandlerAdapter() {
			@Override
			public boolean onBeforeBrowse(CefBrowser browser, CefFrame frame, CefRequest request, boolean user_gesture,
					boolean is_redirect) {
				request.setPostData(postData);
				request.setHeaderMap(headerMap);
				return super.onBeforeBrowse(browser, frame, request, user_gesture, is_redirect);
			}
		});
		setUrl(url);
		return true;
	}

	/**
	 * 停止加载
	 */
	@Override
	public void stop() {
		cefbrowser.stopLoad();
		clearTemp();
	}

}
