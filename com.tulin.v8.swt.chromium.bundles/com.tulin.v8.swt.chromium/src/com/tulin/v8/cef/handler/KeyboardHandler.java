package com.tulin.v8.cef.handler;

import javax.swing.JFrame;

import org.cef.browser.CefBrowser;
import org.cef.handler.CefKeyboardHandlerAdapter;

import com.tulin.v8.cef.dialog.DevToolsDialog;

public class KeyboardHandler extends CefKeyboardHandlerAdapter {
	private DevToolsDialog devToolsDialog_ = null;

	@Override
	public boolean onKeyEvent(CefBrowser cefBrowser, CefKeyEvent cefKeyEvent) {
		if (cefKeyEvent.type == CefKeyEvent.EventType.KEYEVENT_KEYUP) {
			//System.out.println("windows_key_code:" + cefKeyEvent.windows_key_code);
			switch (cefKeyEvent.windows_key_code) {
			// F5 刷新
			case 116:
				cefBrowser.reload();
				break;
			// F12 开发者工具
			case 123:
				devToolsShow(cefBrowser);
				break;
			default:
				return false;
			}
		}
		return true;
	}

	/**
	 * 开发者工具显示或隐藏
	 * 
	 * @param cefBrowser 显示开发者工具的浏览器
	 */
	private void devToolsShow(CefBrowser cefBrowser) {
		if (devToolsDialog_ != null) {
			if (devToolsDialog_.isActive()) {
				devToolsDialog_.setVisible(false);
			} else {
				devToolsDialog_.setVisible(true);
			}
		} else {
			// 因为是开发者工具，不能影响内容页面的显示，所以单独新建一个窗体显示
			devToolsDialog_ = new DevToolsDialog(new JFrame(), "开发者工具", cefBrowser);
			devToolsDialog_.setVisible(true);
		}
	}
}
