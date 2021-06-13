/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.math.BigDecimal;
import java.util.Date;

public class TimeWatcher {

	// private static TimeWatcher instance;

	private Date startTime = null;

	private Date stopTime = null;

	private boolean isStart = false;

	public TimeWatcher() {}

	public void reset() {
		isStart = false;
		startTime = null;
		stopTime = null;
	}

	public void start() {
		if (isStart) {
			throw new IllegalStateException("The response is being measured now."); //$NON-NLS-1$
		} else {
			isStart = true;
		}
		startTime = new Date();
	}

	public void stop() {
		if (!isStart) {
			throw new IllegalStateException("Please begin measuring."); //$NON-NLS-1$
		} else {
			isStart = false;
		}
		stopTime = new Date();
	}

	public String getTotalTime() {
		if (startTime == null) {
			throw new IllegalStateException("Please begin measuring. "); //$NON-NLS-1$
		}
		if (stopTime == null) {
			throw new IllegalStateException("Please end measuring. "); //$NON-NLS-1$
		}

		return getString((stopTime.getTime() - startTime.getTime()) / 1000.0);
	}

	private String getString(double second) {
		StringBuffer sb = new StringBuffer();
		if ((int) (second / 3600) > 0) {
			sb.append((int) (second / 3600) + "h"); //$NON-NLS-1$
		}
		if ((int) ((second % 3600) / 60) > 0) {
			sb.append((int) (second % 3600) / 60 + "min"); //$NON-NLS-1$
		}
		sb.append(formart(second % 60) + "sec"); //$NON-NLS-1$
		return sb.toString();
	}

	private static String formart(double time) {
		BigDecimal decimal = new BigDecimal(time);
		// decimal = decimal.movePointLeft(4);
		decimal = decimal.setScale(1, BigDecimal.ROUND_UP);
		// decimal = decimal.setScale(2, BigDecimal.ROUND_UP);
		// decimal = decimal.setScale(1, BigDecimal.ROUND_DOWN);
		// decimal = decimal.setScale(1, BigDecimal.ROUND_UP);
		return decimal.toString();
	}

	public boolean isStart() {
		return isStart;
	}

}
