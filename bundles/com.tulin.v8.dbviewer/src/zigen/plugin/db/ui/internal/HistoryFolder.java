/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryFolder extends TreeNode {

	private static final long serialVersionUID = 1L;

	protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private Date date;

	public HistoryFolder(Date date) {
		super();
		this.date = date;
	}

	public String getName() {
		if (date != null) {
			return dateFormat.format(date);
		} else {
			return null;
		}
	}

}
