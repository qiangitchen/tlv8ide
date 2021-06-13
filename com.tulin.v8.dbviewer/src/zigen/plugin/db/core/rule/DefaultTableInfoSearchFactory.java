package zigen.plugin.db.core.rule;

import java.sql.DatabaseMetaData;


public class DefaultTableInfoSearchFactory extends AbstractTableInfoSearchFactory{
	protected DatabaseMetaData meta;

	protected DefaultTableInfoSearchFactory(DatabaseMetaData meta) {
		this.meta = meta;
	}

	public String getTableInfoAllSql(String schema, String[] types) {
		return null;
	}

	public String getTableInfoSql(String schema, String tableName, String type) {
		return null;
	}

	public String getDbName() {
		return null;
	}

	public void setDatabaseMetaData(DatabaseMetaData meta){
		this.meta = meta;
	}


}
