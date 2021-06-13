/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import java.sql.Connection;

import zigen.plugin.db.core.TableConstraintColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TableIDXColumn;
import zigen.plugin.db.core.TablePKColumn;

public interface IConstraintSearcherFactory {

	public static final int TABLE_INDEX_STATISTIC = 0;

	public static final int TABLE_INDEX_CLSTERED = 1;

	public static final int TABLE_INDEX_HASHED = 2;

	public static final int TABLE_INDEX_OTHER = 3;

	TablePKColumn[] getPKColumns(Connection con, String schemaPattern, String tableName) throws Exception;

	TableFKColumn[] getFKColumns(Connection con, String schemaPattern, String tableName) throws Exception;

	TableIDXColumn[] getUniqueIDXColumns(Connection con, String schemaPattern, String tableName, boolean unique) throws Exception;

	TableConstraintColumn[] getConstraintColumns(Connection con, String schemaPattern, String tableName) throws Exception;

}
