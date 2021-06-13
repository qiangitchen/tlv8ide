/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ext.oracle.internal.OracleSourceDetailInfo;
import zigen.plugin.db.ext.oracle.internal.OracleSourceDetailSearcher;
import zigen.plugin.db.ext.oracle.internal.OracleSourceInfo;
import zigen.plugin.db.ui.internal.OracleSource;

public class SourceDDL implements IDDL, Serializable {

	private static final long serialVersionUID = 1L;

	IDBConfig config = null;

	OracleSource oracleSource;

	OracleSourceInfo oracleSourceInfo;

	OracleSourceDetailInfo oracleSourceDetailInfo;

	String SqlSouceName;

	public SourceDDL() {

	}

	public SourceDDL(OracleSource oracleSource) {
		setOracleSource(oracleSource);
	}

	protected final OracleSourceDetailInfo getOracleSourceDetailInfo() {
		OracleSourceDetailInfo info = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			String owner = oracleSourceInfo.getOwner();
			String name = oracleSourceInfo.getName();
			String type = oracleSourceInfo.getType();
			info = OracleSourceDetailSearcher.execute(con, owner, name, type, false);

		} catch (Exception e) {
			DbPlugin.log(e);
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}
		return info;

	}


	public String getType() {
		return oracleSourceInfo.getType();
	}


	public String getDdl() {
		return (oracleSourceDetailInfo != null) ? oracleSourceDetailInfo.getText() : "";
	}

	public String getDbName() {
		return config.getDbName();
	}

	public String getDisplayedName() {
		return getSchemaName() + "." + getTargetName() + "[" + getType() + "]";
	}

	public String getSchemaName() {
		return oracleSourceInfo.getOwner();
	}

	public String getTargetName() {
		return oracleSourceInfo.getName();
	}

	public boolean isSchemaSupport() {
		return true;
	}

	public IDBConfig getConfig() {
		return config;
	}

	public void setConfig(IDBConfig config) {
		this.config = config;
	}

	public OracleSourceInfo getOracleSourceInfo() {
		return oracleSourceInfo;
	}

	public void setOracleSourceInfo(OracleSourceInfo oracleSourceInfo) {
		this.oracleSourceInfo = oracleSourceInfo;

	}

	public String getSqlSouceName() {
		return SqlSouceName;
	}

	public void setSqlSouceName(String sqlSouceName) {
		SqlSouceName = sqlSouceName;
	}

	public void setOracleSourceDetailInfo(OracleSourceDetailInfo oracleSourceDetailInfo) {
		this.oracleSourceDetailInfo = oracleSourceDetailInfo;
	}

	public OracleSource getOracleSource() {
		return oracleSource;
	}

	public void setOracleSource(OracleSource oracleSource) {
		this.oracleSource = oracleSource;
		this.config = oracleSource.getDbConfig();
		this.oracleSourceInfo = oracleSource.getOracleSourceInfo();
		this.oracleSourceDetailInfo = getOracleSourceDetailInfo();
	}


}
