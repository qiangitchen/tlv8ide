package com.tulin.v8.swt.chromium;

import java.awt.BorderLayout;
import java.awt.Frame;

import org.cef.browser.CefBrowser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DevToolsDialog extends Dialog {
	private Display display;
	private Shell shell;

	protected final int returnOption = SWT.CANCEL;
	protected final CefBrowser browser;

	public DevToolsDialog(Shell parent, CefBrowser browser) {
		super(parent, SWT.MODELESS | SWT.MAX | SWT.MIN | SWT.RESIZE | SWT.CLOSE);
		setText("Developer Tools");
		shell = new Shell(getStyle());
		shell.setText(getText());
		if (parent != null) {
			display = parent.getDisplay();
			shell.setImage(parent.getImage());
			parent.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					dispose();
				}
			});
		} else {
			display = Display.getDefault();
		}
		this.browser = browser;
	}

	public Display getDisplay() {
		return display;
	}

	public Shell getShell() {
		return shell;
	}

	protected void initView(Composite parent) {
		parent.setLayout(new FillLayout());
		Composite composite = new Composite(parent, SWT.EMBEDDED);
		composite.setLayout(new FillLayout());
		Frame awtframe = SWT_AWT.new_Frame(composite);
		awtframe.setLayout(new BorderLayout());
		awtframe.add(browser.getDevTools().getUIComponent(), BorderLayout.CENTER);
		shell.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				awtframe.removeAll();
			}
		});
	}

	public int open() {
		shell.open();
		initView(shell);
		shell.layout();
		runEventLoop(shell);
		return returnOption;
	}

	protected void runEventLoop(Shell loopShell) {
		// Use the display provided by the shell if possible
		Display display;
		if (shell == null) {
			display = Display.getCurrent();
		} else {
			display = loopShell.getDisplay();
		}

		while (loopShell != null && !loopShell.isDisposed()) {
			try {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		if (!display.isDisposed())
			display.update();
	}

	public void setActivte() {
		if (shell != null && !shell.isDisposed()) {
			shell.getDisplay().asyncExec(() -> {
				shell.setActive();
			});
		}
	}

	public boolean isClosed() {
		return shell == null || shell.isDisposed();
	}

	public void dispose() {
		if (shell != null && !shell.isDisposed()) {
			shell.getDisplay().asyncExec(() -> {
				shell.dispose();
			});
		}
	}

}
