/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.sql;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Display;

abstract public class AutoDelayListener implements KeyListener, MouseListener, Runnable {

	private Thread fThread;

	private boolean fIsReset = false;

	private Object fMutex = new Object();

	int fAutoActivationDelay;


	public AutoDelayListener() {
		this.fAutoActivationDelay = 500;
	}

	public AutoDelayListener(int autoActivationDelay) {
		this.fAutoActivationDelay = autoActivationDelay;
	}

	protected void start(int showStyle) {
		fThread = new Thread(this, "DUMMY"); //$NON-NLS-1$
		fThread.start();
	}

	abstract Runnable createExecutAction();

	public void run() {
		try {
			while (true) {
				synchronized (fMutex) {
					if (fAutoActivationDelay != 0)
						fMutex.wait(fAutoActivationDelay);
					if (fIsReset) {
						fIsReset = false;
						continue;
					}
				}
				Display.getDefault().asyncExec(createExecutAction());

				break;
			}
		} catch (InterruptedException e) {
		}
		fThread = null;
	}

	protected void reset(int showStyle) {
		synchronized (fMutex) {
			fIsReset = true;
			fMutex.notifyAll();
		}
	}

	protected void stop() {
		Thread threadToStop = fThread;
		if (threadToStop != null && threadToStop.isAlive())
			threadToStop.interrupt();
	}

	private void fireEvent() {
		if (fThread != null && fThread.isAlive()) {
			reset(1);
			// stop();
		} else {
			start(1);
		}
	}

	public void keyPressed(KeyEvent e) {
		fireEvent();
	}

	public void mouseUp(MouseEvent e) {
		fireEvent();
	}

	public void keyReleased(KeyEvent e) {}

	public void mouseDown(MouseEvent e) {}

	public void mouseDoubleClick(MouseEvent e) {}


}
