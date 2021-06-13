/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.sql;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ext.oracle.internal.OracleSourceErrorInfo;
import zigen.plugin.db.ui.views.internal.PLSQLSourceViewer;

public interface IPlsqlEditor extends ISqlEditor {

	public IDBConfig getConfig();

	public PLSQLSourceViewer getPLSQLSourceViewer();

	public void clearError();

	public void setError(OracleSourceErrorInfo[] OracleSourceErrorInfos);
}
