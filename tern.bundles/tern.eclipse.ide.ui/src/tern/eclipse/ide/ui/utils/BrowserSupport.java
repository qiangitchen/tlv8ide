package tern.eclipse.ide.ui.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

import tern.eclipse.ide.internal.ui.Trace;

public class BrowserSupport {

	private BrowserSupport() {
	}

	/**
	 * Opens the given url in the browser as choosen in the preferences.
	 * 
	 * @param url
	 *            the URL
	 * @param display
	 *            the display
	 * @since 3.6
	 */
	public static void open(final URL url, Display display) {
		display.syncExec(new Runnable() {
			public void run() {
				internalOpen(url, false);
			}
		});
	}

	/**
	 * Opens the given URL in an external browser.
	 * 
	 * @param url
	 *            the URL
	 * @param display
	 *            the display
	 * @since 3.6
	 */
	public static void openExternal(final URL url, Display display) {
		display.syncExec(new Runnable() {
			public void run() {
				internalOpen(url, true);
			}
		});
	}

	private static void internalOpen(final URL url,
			final boolean useExternalBrowser) {
		BusyIndicator.showWhile(null, new Runnable() {
			public void run() {
				URL helpSystemUrl = PlatformUI.getWorkbench().getHelpSystem()
						.resolve(url.toExternalForm(), true);
				if (helpSystemUrl == null) { // can happen if
												// org.eclipse.help.ui is not
												// available
					return; // the resolve() method already wrote
							// "Unable to instantiate help UI" to the log
				}
				try {
					IWorkbenchBrowserSupport browserSupport = PlatformUI
							.getWorkbench().getBrowserSupport();
					IWebBrowser browser;
					if (useExternalBrowser)
						browser = browserSupport.getExternalBrowser();
					else
						browser = browserSupport.createBrowser(null);
					browser.openURL(helpSystemUrl);
				} catch (PartInitException ex) {
					// XXX: show dialog?
					Trace.trace(Trace.SEVERE, "Opening Javadoc failed", ex); //$NON-NLS-1$
				}
			}
		});
	}

	public static void setLinkTarget(Link link, final String target,
			final String url) {
		link.setText(new StringBuilder("<a>").append(target).append("</a>")
				.toString());
		link.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				try {
					BrowserSupport.open(new URL(url), event.display);
				} catch (MalformedURLException e) {
					Trace.trace(Trace.SEVERE, "Error while opening browser", e);
				}
			}
		});
	}
}
