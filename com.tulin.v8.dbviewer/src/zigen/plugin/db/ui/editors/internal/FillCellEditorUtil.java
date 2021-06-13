/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ConnectionManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.InputStreamUtil;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.SQLCreator;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.UpdateSQLInvoker;
import zigen.plugin.db.ui.editors.exceptions.ZeroUpdateException;

public class FillCellEditorUtil {

	public static int update(Connection con, TableElement tableElement, int colIndex, Object bytes) throws Exception {
		int rowAffected = 0;
		tableElement.updateItems(colIndex - 1, bytes);
		rowAffected = UpdateSQLInvoker.invoke(con, tableElement.getTable(), tableElement.getModifiedColumns(), tableElement.getModifiedItems(), tableElement.getUniqueColumns(),
				tableElement.getUniqueItems());
		if (rowAffected > 0) {
			return rowAffected;
		} else {
			throw new ZeroUpdateException("Update Failed.");
		}
	}

	public static int delete(Connection con, TableElement tableElement, int colIndex) throws Exception {
		int rowAffected = 0;
		tableElement.updateItems(colIndex - 1, null);
		rowAffected = UpdateSQLInvoker.invoke(con, tableElement.getTable(), tableElement.getModifiedColumns(), tableElement.getModifiedItems(), tableElement.getUniqueColumns(),
				tableElement.getUniqueItems());
		// if(rowAffected > 0){
		// return rowAffected;
		// }else{
		// throw new ZeroUpdateException("Update Failed.");
		// }
		return rowAffected;
	}

	public static void saveAsFile(TableElement tableElement, int colIndex, int dataType, File saveFile) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			IDBConfig config = tableElement.getTable().getDbConfig();

			con = ConnectionManager.getConnection(config);
			String sql = SQLCreator.createSelectSql(tableElement, false);
			st = con.createStatement();
			rs = st.executeQuery(sql);

			if (rs.next()) {

				switch (dataType) {
				case Types.BINARY: // -2
				case Types.VARBINARY: // -3
				case Types.LONGVARBINARY: // -4
					InputStream is = rs.getBinaryStream(colIndex);
					if (rs.wasNull())
						return;
					InputStreamUtil.save(saveFile, is);
					break;

				case Types.BLOB:
					Blob blob = rs.getBlob(colIndex);
					if (rs.wasNull())
						return;
					InputStreamUtil.save(saveFile, new BufferedInputStream(blob.getBinaryStream()));
					break;
				case Types.CLOB:
					Clob clob = rs.getClob(colIndex);
					if (rs.wasNull())
						return;
					InputStreamUtil.save(saveFile, clob.getCharacterStream());
					break;

				default:
					break;
				}

			}

			DbPlugin.getDefault().showInformationMessage(Messages.getString("FillCellEditorUtil.0")); //$NON-NLS-1$
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
			ConnectionManager.closeConnection(con);
		}
	}

	public static Object getObject(TableElement tableElement, int colIndex, int dataType) {
		Object obj = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			IDBConfig config = tableElement.getTable().getDbConfig();
			con = ConnectionManager.getConnection(config);
			String sql = SQLCreator.createSelectSql(tableElement, false);

			st = con.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {

				switch (dataType) {
				case Types.BINARY: // -2
				case Types.VARBINARY: // -3
				case Types.LONGVARBINARY: // -4
					InputStream is = rs.getBinaryStream(colIndex);
					if (rs.wasNull())
						return null;
					obj = InputStreamUtil.toByteArray(is);
					break;
				case Types.BLOB:
					Blob blob = rs.getBlob(colIndex);
					if (rs.wasNull())
						return null;
					obj = InputStreamUtil.toByteArray(blob.getBinaryStream());
					break;
				case Types.CLOB:
					Clob clob = rs.getClob(colIndex);
					if (rs.wasNull())
						return null;
					obj = InputStreamUtil.toString(clob.getCharacterStream()); // String
					break;

				default:
					break;
				}
			}
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
			ConnectionManager.closeConnection(con);
		}
		return obj;
	}
}
