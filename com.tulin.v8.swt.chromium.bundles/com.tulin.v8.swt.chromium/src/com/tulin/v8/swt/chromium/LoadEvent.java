package com.tulin.v8.swt.chromium;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefLoadHandler.ErrorCode;
import org.cef.network.CefRequest.TransitionType;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Widget;

public class LoadEvent extends TypedEvent {
	private static final long serialVersionUID = -3656078130463828576L;
	
	public CefBrowser browser;
	public boolean isLoading;
	public boolean canGoBack;
	public boolean canGoForward;
	public CefFrame frame;
	public TransitionType transitionType;
	public int httpStatusCode;
	public ErrorCode errorCode;
	public String errorText;
	public String failedUrl;

	public LoadEvent(Widget widget) {
		super(widget);
	}

}
