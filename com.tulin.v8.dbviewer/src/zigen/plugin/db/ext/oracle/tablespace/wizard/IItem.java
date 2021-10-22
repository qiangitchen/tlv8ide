/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.tablespace.wizard;

import java.math.BigDecimal;

public interface IItem {

	public String getTableName();

	public String getIndexName();

	// public int getDbBlockSize();

	public int getPctFree();

	public long getRecordSize();

	public BigDecimal getTableSpaceSize();

	public double getSafeCoefficient();

	public BigDecimal getTableSpaceSafeSize();

	// public void setDbBlockSize(int dbBlockSize);

	public boolean isChecked();
}
