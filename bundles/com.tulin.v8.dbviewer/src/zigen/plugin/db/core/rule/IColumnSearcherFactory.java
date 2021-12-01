/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import zigen.plugin.db.core.TableColumn;

public interface IColumnSearcherFactory {

	public static final int COLUMN_NAME = 4;

	public static final int DATA_TYPE = 5;

	public static final int TYPE_NAME = 6;

	public static final int COLUMN_SIZE = 7;

	public static final int DECIMAL_DIGITS = 9;

	public static final int NULLABLE = 11;

	public static final int REMARKS = 12;

	public static final int IS_NULLABLE = 18;

	abstract TableColumn[] execute(Connection con, String schemaPattern, String tableName) throws Exception;

	abstract int getDatabaseMajorVersion();

	abstract void setConvertUnicode(boolean b);

	abstract void setDatabaseMetaData(DatabaseMetaData meta);

}
