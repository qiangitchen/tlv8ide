/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;


public interface IDDLDiff {

	public static final int TYPE_INCLUDE_ONLY_ORIGN = 10;

	public static final int TYPE_INCLUDE_ONLY_TARGET = 20;

	public static final int TYPE_BOTH_DIFFERENCE = 30;

	public static final int TYPE_BOTH_SAME = 40;

	public static final int TYPE_NOTHING = 99;

	public abstract int getResultType();

	public abstract String getName();

	public abstract String getLeftDDLString();

	public abstract String getRightDDLString();

	public abstract String getLeftDisplayedName();

	public abstract String getRightDisplayedName();

	public abstract String getLeftDBName();

	public abstract String getRightDBName();

	public abstract boolean isComparisonFailure();

	public abstract IDDL getLeftDDL();

	public abstract IDDL getRightDDL();

	public abstract String getType();

}
