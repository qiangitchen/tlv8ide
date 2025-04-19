package com.tulin.v8.swt.chromium;

import java.util.EventListener;

public interface LoadListener extends EventListener{
	public void onLoadingStateChange(LoadEvent event);
	public void onLoadStart(LoadEvent event);
	public void onLoadEnd(LoadEvent event);
	public void onLoadError(LoadEvent event);
}
