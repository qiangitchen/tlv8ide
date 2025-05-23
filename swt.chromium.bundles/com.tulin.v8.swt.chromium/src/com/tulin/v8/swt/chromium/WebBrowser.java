package com.tulin.v8.swt.chromium;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.cef.handler.CefDisplayHandler;
import org.cef.handler.CefKeyboardHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.AuthenticationListener;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

abstract class WebBrowser {
	Browser browser;
	AuthenticationListener[] authenticationListeners = new AuthenticationListener[0];
	CloseWindowListener[] closeWindowListeners = new CloseWindowListener[0];
	LocationListener[] locationListeners = new LocationListener[0];
	OpenWindowListener[] openWindowListeners = new OpenWindowListener[0];
	ProgressListener[] progressListeners = new ProgressListener[0];
	StatusTextListener[] statusTextListeners = new StatusTextListener[0];
	TitleListener[] titleListeners = new TitleListener[0];
	LoadListener[] loadListeners = new LoadListener[0];
	VisibilityWindowListener[] visibilityWindowListeners = new VisibilityWindowListener[0];
	List<CefDisplayHandler> cefDisplayHandlers = new ArrayList<>();
	List<CefKeyboardHandler> cefKeyboardHandlers = new ArrayList<>();
	boolean jsEnabledOnNextPage = true, jsEnabled = true;
	Object evaluateResult;

	static final String ERROR_ID = "com.tulin.v8.swt.chromium.error"; // $NON-NLS-1$
	static final String EXECUTE_ID = "swtExecuteTemporary"; // $NON-NLS-1$

	static List<String[]> NativePendingCookies = new ArrayList<>();
	static String CookieName, CookieValue, CookieUrl;
	static boolean CookieResult;
	static Runnable NativeClearSessions;
	static Runnable NativeGetCookie;
	static Runnable NativeSetCookie;

	/* Key Mappings */
	static final int[][] KeyTable = {
			/* Keyboard and Mouse Masks */
			{ 18, SWT.ALT }, { 16, SWT.SHIFT }, { 17, SWT.CONTROL }, { 224, SWT.COMMAND },

			/* Literal Keys */
			{ 65, 'a' }, { 66, 'b' }, { 67, 'c' }, { 68, 'd' }, { 69, 'e' }, { 70, 'f' }, { 71, 'g' }, { 72, 'h' },
			{ 73, 'i' }, { 74, 'j' }, { 75, 'k' }, { 76, 'l' }, { 77, 'm' }, { 78, 'n' }, { 79, 'o' }, { 80, 'p' },
			{ 81, 'q' }, { 82, 'r' }, { 83, 's' }, { 84, 't' }, { 85, 'u' }, { 86, 'v' }, { 87, 'w' }, { 88, 'x' },
			{ 89, 'y' }, { 90, 'z' }, { 48, '0' }, { 49, '1' }, { 50, '2' }, { 51, '3' }, { 52, '4' }, { 53, '5' },
			{ 54, '6' }, { 55, '7' }, { 56, '8' }, { 57, '9' }, { 32, ' ' }, { 59, ';' }, { 61, '=' }, { 188, ',' },
			{ 190, '.' }, { 191, '/' }, { 219, '[' }, { 221, ']' }, { 222, '\'' }, { 192, '`' }, { 220, '\\' },
			{ 108, '|' }, { 226, '<' },

			/* Non-Numeric Keypad Keys */
			{ 37, SWT.ARROW_LEFT }, { 39, SWT.ARROW_RIGHT }, { 38, SWT.ARROW_UP }, { 40, SWT.ARROW_DOWN },
			{ 45, SWT.INSERT }, { 36, SWT.HOME }, { 35, SWT.END }, { 46, SWT.DEL }, { 33, SWT.PAGE_UP },
			{ 34, SWT.PAGE_DOWN },

			/* Virtual and Ascii Keys */
			{ 8, SWT.BS }, { 13, SWT.CR }, { 9, SWT.TAB }, { 27, SWT.ESC }, { 12, SWT.DEL },

			/* Functions Keys */
			{ 112, SWT.F1 }, { 113, SWT.F2 }, { 114, SWT.F3 }, { 115, SWT.F4 }, { 116, SWT.F5 }, { 117, SWT.F6 },
			{ 118, SWT.F7 }, { 119, SWT.F8 }, { 120, SWT.F9 }, { 121, SWT.F10 }, { 122, SWT.F11 }, { 123, SWT.F12 },
			{ 124, SWT.F13 }, { 125, SWT.F14 }, { 126, SWT.F15 }, { 127, 0 }, { 128, 0 }, { 129, 0 }, { 130, 0 },
			{ 131, 0 }, { 132, 0 }, { 133, 0 }, { 134, 0 }, { 135, 0 },

			/* Numeric Keypad Keys */
			{ 96, SWT.KEYPAD_0 }, { 97, SWT.KEYPAD_1 }, { 98, SWT.KEYPAD_2 }, { 99, SWT.KEYPAD_3 },
			{ 100, SWT.KEYPAD_4 }, { 101, SWT.KEYPAD_5 }, { 102, SWT.KEYPAD_6 }, { 103, SWT.KEYPAD_7 },
			{ 104, SWT.KEYPAD_8 }, { 105, SWT.KEYPAD_9 }, { 14, SWT.KEYPAD_CR }, { 107, SWT.KEYPAD_ADD },
			{ 109, SWT.KEYPAD_SUBTRACT }, { 106, SWT.KEYPAD_MULTIPLY }, { 111, SWT.KEYPAD_DIVIDE },
			{ 110, SWT.KEYPAD_DECIMAL },

			/* Other keys */
			{ 20, SWT.CAPS_LOCK }, { 144, SWT.NUM_LOCK }, { 145, SWT.SCROLL_LOCK }, { 44, SWT.PRINT_SCREEN },
			{ 6, SWT.HELP }, { 19, SWT.PAUSE }, { 3, SWT.BREAK },

			/* WebKit-specific */
			{ 186, ';' }, { 187, '=' }, { 189, '-' }, };

	public void addAuthenticationListener(AuthenticationListener listener) {
		AuthenticationListener[] newAuthenticationListeners = new AuthenticationListener[authenticationListeners.length
				+ 1];
		System.arraycopy(authenticationListeners, 0, newAuthenticationListeners, 0, authenticationListeners.length);
		authenticationListeners = newAuthenticationListeners;
		authenticationListeners[authenticationListeners.length - 1] = listener;
	}

	public void addCloseWindowListener(CloseWindowListener listener) {
		CloseWindowListener[] newCloseWindowListeners = new CloseWindowListener[closeWindowListeners.length + 1];
		System.arraycopy(closeWindowListeners, 0, newCloseWindowListeners, 0, closeWindowListeners.length);
		closeWindowListeners = newCloseWindowListeners;
		closeWindowListeners[closeWindowListeners.length - 1] = listener;
	}

	public void addLocationListener(LocationListener listener) {
		LocationListener[] newLocationListeners = new LocationListener[locationListeners.length + 1];
		System.arraycopy(locationListeners, 0, newLocationListeners, 0, locationListeners.length);
		locationListeners = newLocationListeners;
		locationListeners[locationListeners.length - 1] = listener;
	}

	public void addOpenWindowListener(OpenWindowListener listener) {
		OpenWindowListener[] newOpenWindowListeners = new OpenWindowListener[openWindowListeners.length + 1];
		System.arraycopy(openWindowListeners, 0, newOpenWindowListeners, 0, openWindowListeners.length);
		openWindowListeners = newOpenWindowListeners;
		openWindowListeners[openWindowListeners.length - 1] = listener;
	}

	public void addProgressListener(ProgressListener listener) {
		ProgressListener[] newProgressListeners = new ProgressListener[progressListeners.length + 1];
		System.arraycopy(progressListeners, 0, newProgressListeners, 0, progressListeners.length);
		progressListeners = newProgressListeners;
		progressListeners[progressListeners.length - 1] = listener;
	}

	public void addStatusTextListener(StatusTextListener listener) {
		StatusTextListener[] newStatusTextListeners = new StatusTextListener[statusTextListeners.length + 1];
		System.arraycopy(statusTextListeners, 0, newStatusTextListeners, 0, statusTextListeners.length);
		statusTextListeners = newStatusTextListeners;
		statusTextListeners[statusTextListeners.length - 1] = listener;
	}

	public void addTitleListener(TitleListener listener) {
		TitleListener[] newTitleListeners = new TitleListener[titleListeners.length + 1];
		System.arraycopy(titleListeners, 0, newTitleListeners, 0, titleListeners.length);
		titleListeners = newTitleListeners;
		titleListeners[titleListeners.length - 1] = listener;
	}

	public void addLoadListener(LoadListener listener) {
		LoadListener[] newLoadListeners = new LoadListener[loadListeners.length + 1];
		System.arraycopy(loadListeners, 0, newLoadListeners, 0, loadListeners.length);
		loadListeners = newLoadListeners;
		loadListeners[loadListeners.length - 1] = listener;
	}

	public void addVisibilityWindowListener(VisibilityWindowListener listener) {
		VisibilityWindowListener[] newVisibilityWindowListeners = new VisibilityWindowListener[visibilityWindowListeners.length
				+ 1];
		System.arraycopy(visibilityWindowListeners, 0, newVisibilityWindowListeners, 0,
				visibilityWindowListeners.length);
		visibilityWindowListeners = newVisibilityWindowListeners;
		visibilityWindowListeners[visibilityWindowListeners.length - 1] = listener;
	}

	public void addCefDisplayHandler(CefDisplayHandler cefDisplayHandler) {
		removeCefDisplayHandler(cefDisplayHandler);
		cefDisplayHandlers.add(cefDisplayHandler);
	}

	public void removeCefDisplayHandler(CefDisplayHandler cefDisplayHandler) {
		cefDisplayHandlers.remove(cefDisplayHandler);
	}

	public void addCefKeyboardHandler(CefKeyboardHandler cefKeyboardHandler) {
		removeCefKeyboardHandler(cefKeyboardHandler);
		cefKeyboardHandlers.add(cefKeyboardHandler);
	}

	public void removeCefKeyboardHandler(CefKeyboardHandler cefKeyboardHandler) {
		cefKeyboardHandlers.remove(cefKeyboardHandler);
	}

	public abstract boolean back();

	public static void clearSessions() {
		if (NativeClearSessions != null)
			NativeClearSessions.run();
	}

	public static String GetCookie(String name, String url) {
		CookieName = name;
		CookieUrl = url;
		CookieValue = null;
		if (NativeGetCookie != null)
			NativeGetCookie.run();
		String result = CookieValue;
		CookieName = CookieValue = CookieUrl = null;
		return result;
	}

	public static boolean SetCookie(String value, String url, boolean addToPending) {
		CookieValue = value;
		CookieUrl = url;
		CookieResult = false;
		if (NativeSetCookie != null) {
			NativeSetCookie.run();
		} else {
			if (addToPending && NativePendingCookies != null) {
				NativePendingCookies.add(new String[] { value, url });
			}
		}
		CookieValue = CookieUrl = null;
		return CookieResult;
	}

	static void SetPendingCookies(List<String[]> pendingCookies) {
		for (String[] current : pendingCookies) {
			SetCookie(current[0], current[1], false);
		}
	}

	public abstract void create(Composite parent, int style) throws Exception;

	static String CreateErrorString(String error) {
		return ERROR_ID + error;
	}

	static String ExtractError(String error) {
		return error.substring(ERROR_ID.length());
	}

	public boolean close() {
		return true;
	}

	public void createFunction(BrowserFunction function) {
		registerFunction(function);

		StringBuilder functionBuffer = new StringBuilder();
		functionBuffer.append("window." + function.name);
		functionBuffer.append(" = function(){");
		functionBuffer.append("	try{");
		functionBuffer.append(" 	window.top." + function.name + function.token + "(arguments[0]);");
		functionBuffer.append("	}catch(e){");
		functionBuffer.append(" 	var result = '" + ERROR_ID + "';");
		functionBuffer.append(" 	var args = [];	");
		functionBuffer.append(" 	for(var i=0; i<arguments.length; i++){");
		functionBuffer.append(" 		var arg = arguments[i];	");
		functionBuffer.append(" 		if(arg instanceof Object){	");
		functionBuffer.append(" 			args.push(JSON.stringify(arg));	");
		functionBuffer.append(" 		}else{				");
		functionBuffer.append(" 			args.push('\"'+arg+'\"');	");
		functionBuffer.append(" 		}					");
		functionBuffer.append(" 	}						");
		functionBuffer.append(" 	window.top." + function.name + function.token + "({");
		functionBuffer.append("			request: '['+args.join(',')+']',");
		functionBuffer.append("			persistent: false,");
		functionBuffer.append(" 		onSuccess: function(response) {");
		functionBuffer.append("    			result=response;");
		functionBuffer.append(" 		},");
		functionBuffer.append(" 		onFailure: function(error_code, error_message) {");
		functionBuffer.append("    			result = '';  ");
		functionBuffer.append("    			throw new Error(error_message);");
		functionBuffer.append("  		} ");
		functionBuffer.append(" 	}); ");
		functionBuffer.append(" 	return result;");
		functionBuffer.append(" }");
		functionBuffer.append("};");

		StringBuilder buffer = new StringBuilder();
		if (function.top) {
			buffer.append(functionBuffer.toString());
		}

		function.functionString = buffer.toString();
		nonBlockingExecute(function.functionString);
	}

	void deregisterFunction(BrowserFunction function) {
	}

	public void destroyFunction(BrowserFunction function) {
		deregisterFunction(function);
	}

	// Designed to be overriden by platform implementations, used for optimization
	// and avoiding deadlocks.
	// Webkit2 is async, we often don't need to bother waiting for a return type if
	// we never use it.
	void nonBlockingExecute(String script) {
		// System.out.println(script);
		execute(script);
	}

	public abstract boolean execute(String script);

	public Object evaluate(String script, boolean trusted) throws SWTException {
		return evaluate(script);
	}

	public Object evaluate(String script) throws SWTException {
		evaluateResult = null;
		StringBuilder buffer = new StringBuilder("window.top."); // $NON-NLS-1$
		buffer.append(EXECUTE_ID);
		buffer.append("({"); // $NON-NLS-1$
		buffer.append("request:function");
		buffer.append("() {"); // $NON-NLS-1$
		buffer.append(script);
		buffer.append("}(),");
		buffer.append("persistent:false");
		buffer.append("});"); // $NON-NLS-1$
		nonBlockingExecute(buffer.toString());
		long time = new Date().getTime();
		try {
			while (evaluateResult == null && (new Date().getTime() - time < 5000)) {
				Thread.sleep(10);
			}
		} catch (Exception e) {
		}
		Object result = evaluateResult;
		evaluateResult = null;
		if (result instanceof SWTException)
			throw (SWTException) result;
		return result;
	}

	public abstract boolean forward();

	public abstract String getBrowserType();

	String getDeleteFunctionString(String functionName) {
		return "delete window." + functionName; //$NON-NLS-1$
	}

	public abstract String getText();

	public abstract String getUrl();

	public Object getWebBrowser() {
		return null;
	}

	public abstract boolean isBackEnabled();

	public boolean isFocusControl() {
		return false;
	}

	public abstract boolean isForwardEnabled();

	public abstract void refresh();

	void registerFunction(BrowserFunction function) {

	}

	public void removeAuthenticationListener(AuthenticationListener listener) {
		if (authenticationListeners.length == 0)
			return;
		int index = -1;
		for (int i = 0; i < authenticationListeners.length; i++) {
			if (listener == authenticationListeners[i]) {
				index = i;
				break;
			}
		}
		if (index == -1)
			return;
		if (authenticationListeners.length == 1) {
			authenticationListeners = new AuthenticationListener[0];
			return;
		}
		AuthenticationListener[] newAuthenticationListeners = new AuthenticationListener[authenticationListeners.length
				- 1];
		System.arraycopy(authenticationListeners, 0, newAuthenticationListeners, 0, index);
		System.arraycopy(authenticationListeners, index + 1, newAuthenticationListeners, index,
				authenticationListeners.length - index - 1);
		authenticationListeners = newAuthenticationListeners;
	}

	public void removeCloseWindowListener(CloseWindowListener listener) {
		if (closeWindowListeners.length == 0)
			return;
		int index = -1;
		for (int i = 0; i < closeWindowListeners.length; i++) {
			if (listener == closeWindowListeners[i]) {
				index = i;
				break;
			}
		}
		if (index == -1)
			return;
		if (closeWindowListeners.length == 1) {
			closeWindowListeners = new CloseWindowListener[0];
			return;
		}
		CloseWindowListener[] newCloseWindowListeners = new CloseWindowListener[closeWindowListeners.length - 1];
		System.arraycopy(closeWindowListeners, 0, newCloseWindowListeners, 0, index);
		System.arraycopy(closeWindowListeners, index + 1, newCloseWindowListeners, index,
				closeWindowListeners.length - index - 1);
		closeWindowListeners = newCloseWindowListeners;
	}

	public void removeLocationListener(LocationListener listener) {
		if (locationListeners.length == 0)
			return;
		int index = -1;
		for (int i = 0; i < locationListeners.length; i++) {
			if (listener == locationListeners[i]) {
				index = i;
				break;
			}
		}
		if (index == -1)
			return;
		if (locationListeners.length == 1) {
			locationListeners = new LocationListener[0];
			return;
		}
		LocationListener[] newLocationListeners = new LocationListener[locationListeners.length - 1];
		System.arraycopy(locationListeners, 0, newLocationListeners, 0, index);
		System.arraycopy(locationListeners, index + 1, newLocationListeners, index,
				locationListeners.length - index - 1);
		locationListeners = newLocationListeners;
	}

	public void removeOpenWindowListener(OpenWindowListener listener) {
		if (openWindowListeners.length == 0)
			return;
		int index = -1;
		for (int i = 0; i < openWindowListeners.length; i++) {
			if (listener == openWindowListeners[i]) {
				index = i;
				break;
			}
		}
		if (index == -1)
			return;
		if (openWindowListeners.length == 1) {
			openWindowListeners = new OpenWindowListener[0];
			return;
		}
		OpenWindowListener[] newOpenWindowListeners = new OpenWindowListener[openWindowListeners.length - 1];
		System.arraycopy(openWindowListeners, 0, newOpenWindowListeners, 0, index);
		System.arraycopy(openWindowListeners, index + 1, newOpenWindowListeners, index,
				openWindowListeners.length - index - 1);
		openWindowListeners = newOpenWindowListeners;
	}

	public void removeProgressListener(ProgressListener listener) {
		if (progressListeners.length == 0)
			return;
		int index = -1;
		for (int i = 0; i < progressListeners.length; i++) {
			if (listener == progressListeners[i]) {
				index = i;
				break;
			}
		}
		if (index == -1)
			return;
		if (progressListeners.length == 1) {
			progressListeners = new ProgressListener[0];
			return;
		}
		ProgressListener[] newProgressListeners = new ProgressListener[progressListeners.length - 1];
		System.arraycopy(progressListeners, 0, newProgressListeners, 0, index);
		System.arraycopy(progressListeners, index + 1, newProgressListeners, index,
				progressListeners.length - index - 1);
		progressListeners = newProgressListeners;
	}

	public void removeStatusTextListener(StatusTextListener listener) {
		if (statusTextListeners.length == 0)
			return;
		int index = -1;
		for (int i = 0; i < statusTextListeners.length; i++) {
			if (listener == statusTextListeners[i]) {
				index = i;
				break;
			}
		}
		if (index == -1)
			return;
		if (statusTextListeners.length == 1) {
			statusTextListeners = new StatusTextListener[0];
			return;
		}
		StatusTextListener[] newStatusTextListeners = new StatusTextListener[statusTextListeners.length - 1];
		System.arraycopy(statusTextListeners, 0, newStatusTextListeners, 0, index);
		System.arraycopy(statusTextListeners, index + 1, newStatusTextListeners, index,
				statusTextListeners.length - index - 1);
		statusTextListeners = newStatusTextListeners;
	}

	public void removeTitleListener(TitleListener listener) {
		if (titleListeners.length == 0)
			return;
		int index = -1;
		for (int i = 0; i < titleListeners.length; i++) {
			if (listener == titleListeners[i]) {
				index = i;
				break;
			}
		}
		if (index == -1)
			return;
		if (titleListeners.length == 1) {
			titleListeners = new TitleListener[0];
			return;
		}
		TitleListener[] newTitleListeners = new TitleListener[titleListeners.length - 1];
		System.arraycopy(titleListeners, 0, newTitleListeners, 0, index);
		System.arraycopy(titleListeners, index + 1, newTitleListeners, index, titleListeners.length - index - 1);
		titleListeners = newTitleListeners;
	}

	public void removeLoadListener(LoadListener listener) {
		if (loadListeners.length == 0)
			return;
		int index = -1;
		for (int i = 0; i < loadListeners.length; i++) {
			if (listener == loadListeners[i]) {
				index = i;
				break;
			}
		}
		if (index == -1)
			return;
		if (loadListeners.length == 1) {
			loadListeners = new LoadListener[0];
			return;
		}
		LoadListener[] newLoadListeners = new LoadListener[loadListeners.length - 1];
		System.arraycopy(loadListeners, 0, newLoadListeners, 0, index);
		System.arraycopy(loadListeners, index + 1, newLoadListeners, index, loadListeners.length - index - 1);
		loadListeners = newLoadListeners;
	}

	public void removeVisibilityWindowListener(VisibilityWindowListener listener) {
		if (visibilityWindowListeners.length == 0)
			return;
		int index = -1;
		for (int i = 0; i < visibilityWindowListeners.length; i++) {
			if (listener == visibilityWindowListeners[i]) {
				index = i;
				break;
			}
		}
		if (index == -1)
			return;
		if (visibilityWindowListeners.length == 1) {
			visibilityWindowListeners = new VisibilityWindowListener[0];
			return;
		}
		VisibilityWindowListener[] newVisibilityWindowListeners = new VisibilityWindowListener[visibilityWindowListeners.length
				- 1];
		System.arraycopy(visibilityWindowListeners, 0, newVisibilityWindowListeners, 0, index);
		System.arraycopy(visibilityWindowListeners, index + 1, newVisibilityWindowListeners, index,
				visibilityWindowListeners.length - index - 1);
		visibilityWindowListeners = newVisibilityWindowListeners;
	}

	boolean sendKeyEvent(Event event) {
		int traversal = SWT.TRAVERSE_NONE;
		boolean traverseDoit = true;
		switch (event.keyCode) {
		case SWT.ESC: {
			traversal = SWT.TRAVERSE_ESCAPE;
			traverseDoit = true;
			break;
		}
		case SWT.CR: {
			traversal = SWT.TRAVERSE_RETURN;
			traverseDoit = false;
			break;
		}
		case SWT.ARROW_DOWN:
		case SWT.ARROW_RIGHT: {
			traversal = SWT.TRAVERSE_ARROW_NEXT;
			traverseDoit = false;
			break;
		}
		case SWT.ARROW_UP:
		case SWT.ARROW_LEFT: {
			traversal = SWT.TRAVERSE_ARROW_PREVIOUS;
			traverseDoit = false;
			break;
		}
		case SWT.TAB: {
			traversal = (event.stateMask & SWT.SHIFT) != 0 ? SWT.TRAVERSE_TAB_PREVIOUS : SWT.TRAVERSE_TAB_NEXT;
			traverseDoit = (event.stateMask & SWT.CTRL) != 0;
			break;
		}
		case SWT.PAGE_DOWN: {
			if ((event.stateMask & SWT.CTRL) != 0) {
				traversal = SWT.TRAVERSE_PAGE_NEXT;
				traverseDoit = true;
			}
			break;
		}
		case SWT.PAGE_UP: {
			if ((event.stateMask & SWT.CTRL) != 0) {
				traversal = SWT.TRAVERSE_PAGE_PREVIOUS;
				traverseDoit = true;
			}
			break;
		}
		default: {
			if (translateMnemonics()) {
				if (event.character != 0 && (event.stateMask & (SWT.ALT | SWT.CTRL)) == SWT.ALT) {
					traversal = SWT.TRAVERSE_MNEMONIC;
					traverseDoit = true;
				}
			}
			break;
		}
		}

		boolean doit = true;
		if (traversal != SWT.TRAVERSE_NONE) {
			boolean oldEventDoit = event.doit;
			event.doit = traverseDoit;
			doit = !browser.traverse(traversal, event);
			event.doit = oldEventDoit;
		}
		if (doit) {
			browser.notifyListeners(event.type, event);
			doit = event.doit;
		}
		return doit;
	}

	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	public abstract boolean setText(String html, boolean trusted);

	public abstract boolean setUrl(String url, String postData, String[] headers);

	public abstract void stop();

	int translateKey(int key) {
		for (int[] element : KeyTable) {
			if (element[0] == key)
				return element[1];
		}
		return 0;
	}

	boolean translateMnemonics() {
		return true;
	}

}
