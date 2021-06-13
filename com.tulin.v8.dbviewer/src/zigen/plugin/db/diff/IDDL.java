/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

public interface IDDL {

	public abstract String getDisplayedName();

	public abstract String getDbName();

	public abstract String getDdl();

	public abstract String getSchemaName();

	public abstract String getTargetName();

	public abstract boolean isSchemaSupport();

	public String getType();
}
