/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import java.util.List;

import zigen.plugin.db.core.TableConstraintColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TableIDXColumn;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public interface ISQLCreatorFactory {

	public static final int TYPE_NONUNIQUE_INDEX = 0;

	public static final int TYPE_UNIQUE_INDEX = 1;

	public static final int TYPE_BITMAP_INDEX = 2;

	public boolean isVisibleColumnSize(String typeName);

	public boolean supportsRemarks();

	public boolean supportsModifyColumnType();

	public boolean supportsModifyColumnSize(String columnType);

	public boolean supportsDropColumnCascadeConstraints();

	public boolean supportsRollbackDDL();

	public String createSelect(String condition, int limit);

	public String createSelectForPager(String _condition, int offset, int limit);

	public String createCountAll(String condition);

	public String createCountForQuery(String query);

	public String[] getSupportColumnType();

	public String createDDL();

	public String createRenameTableDDL(String newTableName);

	public String createCommentOnTableDDL(String commnets);

	public String createRenameColumnDDL(Column from, Column to);

	public String createCommentOnColumnDDL(Column column);

	public String[] createAddColumnDDL(Column column);

	public String[] createModifyColumnDDL(Column from, Column to);

	public String[] createDropColumnDDL(Column column, boolean cascadeConstraints);

	public List convertTableIDXColumn(TableIDXColumn[] idxs);

	public List convertTableConstraintColumn(TableConstraintColumn[] cons);

	public List convertTableFKColumn(TableFKColumn[] fks);

	public String createCreateIndexDDL(String indexName, Column[] columns, int indexType);

	public String createDropIndexDDL(String indexName);

	public String createCreateConstraintPKDDL(String constraintName, Column[] columns);

	public String createCreateConstraintUKDDL(String constraintName, Column[] columns);

	public String createCreateConstraintFKDDL(String constraintName, Column[] columns, ITable refTable, Column[] refColumns, boolean onDeleteCascade);

	public String createCreateConstraintCheckDDL(String constraintName, String check);

	public String createDropConstraintDDL(String constraintName, String type);

	public void setVisibleSchemaName(boolean b);

	public String getTableComment();

	public String getColumnComment();

	public boolean isSupportPager();
}
