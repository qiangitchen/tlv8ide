package com.tulin.v8.swt.chromium;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * 浏览器对话框
 */
public class BrowserDialog extends Dialog {

	private Display display;
	private Shell shell;

	protected String startUrl;
	protected Browser browser;

	protected int returnOption = SWT.CANCEL;

	public BrowserDialog(Shell parent, int style, String title, String startUrl) {
		super(parent, style);
		setText(title);
		this.startUrl = startUrl;
		shell = new Shell(parent, style);
		shell.setText(title);
		if (parent != null) {
			display = parent.getDisplay();
			shell.setImage(parent.getImage());
		} else {
			display = Display.getDefault();
		}
	}

	public BrowserDialog(Shell parent, String title, String startUrl) {
		this(parent, SWT.MODELESS | SWT.MAX | SWT.MIN | SWT.RESIZE | SWT.CLOSE, title, startUrl);
	}

	public void setSize(int w, int h) {
		shell.setSize(w, h);
	}

	public Display getDisplay() {
		return display;
	}

	public Shell getShell() {
		return shell;
	}

	/**
	 * 初始化界面
	 */
	protected void initView(Composite parent) {
		parent.setLayout(new FillLayout());
		browser = new Browser(parent, startUrl);
		browser.addTitleListener(new TitleListener() {
			@Override
			public void changed(TitleEvent event) {
				display.asyncExec(()->{
					shell.setText(event.title);
				});
			}
		});
	}

	/**
	 * 打开
	 * 
	 * @return JOptionPane OPTION
	 */
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

	/**
	 * 消息提示
	 * 
	 * @param message
	 */
	protected void alert(String message) {
		try {
			display.syncExec(new Runnable() {
				@Override
				public void run() {
					MessageBox alerts = new MessageBox(shell, SWT.ICON_INFORMATION);
					alerts.setText("提示");
					alerts.setMessage(message);
					alerts.open();
				}
			});
		} catch (Exception e) {
		}
	}

	/**
	 * 延时关闭窗口
	 * 
	 * @param s 延时时间，单位：毫秒
	 */
	public void closeDelay(long s) {
		new Thread(() -> {
			try {
				Thread.sleep(s);
			} catch (InterruptedException ignored) {
			}
			dispose();
		}).start();
	}

	/**
	 * 销毁
	 */
	public void dispose() {
		display.syncExec(() -> {
			shell.dispose();
		});
	}

	public boolean isClosed() {
		return shell.isDisposed();
	}

	/**
	 * 关闭
	 */
	public void close() {
		dispose();
	}

}
