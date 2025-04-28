package com.tulin.v8.cef;

import java.lang.reflect.Method;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefBeforeDownloadCallback;
import org.cef.callback.CefDownloadItem;
import org.cef.callback.CefJSDialogCallback;
import org.cef.handler.CefDownloadHandlerAdapter;
import org.cef.handler.CefJSDialogHandlerAdapter;
import org.cef.handler.CefLoadHandlerAdapter;
import org.cef.misc.BoolRef;

import com.tulin.v8.OSSelect;
import com.tulin.v8.cef.handler.KeyboardHandler;

/**
 * CefBrowser管理
 * 
 * @author 陈乾
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CefBrowserManager {
	static String className = "com.tulin.v8.cef.JCefAppBuilder";

	private static boolean initting = false;
	private static CefApp cefApp;

	public static CefApp getCefApp() {
		return cefApp;
	}

	public static synchronized void init() throws Exception {
		if (cefApp != null || initting) {
			return;
		}
//		 && OSSelect.isARM() && OSSelect.osVersion() <= 5.19
//		if (OSSelect.isLinux() && OSSelect.getGCCVersion() < 9.0) {
		if (!OSSelect.isWindows()) {
			throw new Exception("系统不支持~");
		}
		if (cefApp == null) {
			initting = true;
			try {
				Class clazz = Class.forName(className);
				Method method = clazz.getMethod("getCefApp");
				cefApp = (CefApp) method.invoke(className);
			} catch (Exception | Error e) {
				throw new Exception(e);
			} finally {
				initting = false;
			}
		}
	}

	/**
	 * 创建新的浏览器
	 * 
	 * @param startURL
	 * @param useOSR
	 * @param isTransparent
	 * @return
	 */
	public static CefBrowser createCefBrowser(String startURL, boolean useOSR, boolean isTransparent) throws Exception {
		CefClient client = cefApp.createClient();
		// 处理键盘事件
		client.addKeyboardHandler(new KeyboardHandler());
		// 处理下载事件
		client.addDownloadHandler(new CefDownloadHandlerAdapter() {
			@Override
			public void onBeforeDownload(CefBrowser browser, CefDownloadItem downloadItem, String suggestedName,
					CefBeforeDownloadCallback callback) {
				callback.Continue(null, true);
//				return true;// 返回true表示取消弹出窗口
			}
		});
		// 处理JS消息提示框
		client.addJSDialogHandler(new CefJSDialogHandlerAdapter() {
			@Override
			public boolean onJSDialog(CefBrowser browser, String origin_url, JSDialogType dialog_type,
					String message_text, String default_prompt_text, CefJSDialogCallback callback,
					BoolRef suppress_message) {
				if (dialog_type == JSDialogType.JSDIALOGTYPE_ALERT) {
					SwingUtilities.invokeLater(() -> {
						JOptionPane.showMessageDialog(browser.getUIComponent(), message_text, "提示",
								JOptionPane.INFORMATION_MESSAGE);
						callback.Continue(true, null);
					});
				} else if (dialog_type == JSDialogType.JSDIALOGTYPE_CONFIRM) {
					SwingUtilities.invokeLater(() -> {
						int c = JOptionPane.showConfirmDialog(browser.getUIComponent(), message_text, "确认",
								JOptionPane.OK_CANCEL_OPTION);
						callback.Continue(c == JOptionPane.OK_OPTION, null);
					});
				} else {
					return false;
				}
				return true;
			}
		});
		// 处理浏览器页面加载状态的变化
		client.addLoadHandler(new CefLoadHandlerAdapter() {
			@Override
			public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) {
				// System.out.println("httpStatusCode:" + httpStatusCode);
			}

			@Override
			public void onLoadError(CefBrowser browser, CefFrame frame, ErrorCode errorCode, String errorText,
					String failedUrl) {
				// System.err.println("errorCode:" + errorCode.getCode());
				// System.err.println("errorText:" + errorText);
				// System.err.println("failedUrl:" + failedUrl);
				// String p404 = "file://" + SourceUtils.getSourceAbsPath("/pages/404.html");
				// browser.loadURL(p404);
			}
		});
		return client.createBrowser(startURL, useOSR, isTransparent);
	}

	public static void release() {
		try {
			Class clazz = Class.forName(className);
			Method method = clazz.getMethod("release");
			method.invoke(className);
		} catch (Exception e) {
		} finally {
			cefApp = null;
		}
	}

}
