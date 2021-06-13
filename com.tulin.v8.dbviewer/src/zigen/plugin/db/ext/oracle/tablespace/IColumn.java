/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.tablespace;

public interface IColumn {

	public abstract int getColumn_length();

	public abstract String getColumn_type();

	public abstract String getColumn_name();
}
