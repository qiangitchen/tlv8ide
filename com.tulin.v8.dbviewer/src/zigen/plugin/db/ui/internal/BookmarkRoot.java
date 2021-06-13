/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import java.io.Serializable;

public class BookmarkRoot extends BookmarkFolder implements Serializable {

	private static final long serialVersionUID = 1L;

	public BookmarkRoot() {
		super();
	}

	public BookmarkRoot(String name) {
		super(name);
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != getClass()) {
			return false;
		}

		BookmarkRoot castedObj = (BookmarkRoot) o;
		return ((this.name == null ? castedObj.name == null : this.name.equals(castedObj.name)));
	}
}
