/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.contentassist;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Display;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.SchemaInfo;
import zigen.plugin.db.core.SchemaSearcher;
import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.core.TableSearcher;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.AbstractStatementFactory;
import zigen.plugin.db.ui.actions.ConfirmConnectDBAction;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ContentAssistTable;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.views.TreeView;

public class ContentInfo {

	IDBConfig config;

	Transaction trans;

	boolean isConnected = false;

	String currentSchema = null;

	Map schemaInfoMap = null;

	char encloseChar;

	public ContentInfo(IDBConfig config) {
		this.config = config;
		if (config != null) {
			configure();
		} else {
			DbPlugin.log("There is no data base definition.");
		}
	}

	private void configure() {
		try {
			trans = Transaction.getInstance(config);
			if (trans.isConneting()) {
				isConnected = true;
				this.encloseChar = AbstractStatementFactory.getFactory(config).getEncloseChar();
				this.schemaInfoMap = getSchemas();
				this.currentSchema = findCurrentSchema();

				DbPlugin.fireStatusChangeListener(config, IStatusChangeListener.EVT_ChangeDataBase);

			} else {
				Display.getDefault().syncExec(new ConfirmConnectDBAction(trans));
				if (trans.isConneting()) {
					configure();
				} else {
					isConnected = false;
				}
			}


		} catch (Exception e) {
			DbPlugin.log(e);
		}
	}

	private String findCurrentSchema() throws Exception {
		TreeView tw = (TreeView) DbPlugin.getDefault().findView(DbPluginConstant.VIEW_ID_TreeView);
		if (tw != null) {
			DataBase db = tw.getContentProvider().findDataBase(config);
			if (SchemaSearcher.isSupport(trans.getConnection())) {
				List list = db.getChildren();
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					TreeNode node = (TreeNode) iterator.next();
					if (node instanceof Schema) {
						Schema schema = (Schema) node;
						if (schema.getName().equalsIgnoreCase(config.getSchema())) {
							return schema.getName();
						}
					} else {
//						return null;
						throw new IllegalStateException("Schema is not found.");
					}
				}
				throw new IllegalStateException("schema name is wrong.");
			}else{
				return null;
			}
		} else {
			return findCorrectSchema(config.getSchema().toUpperCase());
		}

	}


	private Map getSchemas() throws Exception {
		Map map = null;
		ObjectCacher holder = ObjectCacher.getInstance("@" + config.getDbName());

		synchronized (holder) {
			map = (Map) holder.get();
			if (map == null) {
				map = new HashMap();
				try {
					String[] result = SchemaSearcher.execute(trans.getConnection());
					for (int i = 0; i < result.length; i++) {
						String schema = result[i];
						schema = SQLUtil.enclose(schema, encloseChar);
						SchemaInfo info = new SchemaInfo(config, schema);
						map.put(schema.toUpperCase(), info);
					}
				} catch (Exception e) {
					DbPlugin.log(e);
				}
				holder.put(map);
			} else {
			}
		}

		return map;

	}

	public TableInfo[] getTableInfo() throws Exception {
		return getTableInfo(currentSchema);

	}

	public TableInfo[] getTableInfo(String schemaName) throws Exception {

		if (config == null)
			return null;

		schemaName = SQLUtil.removeEnclosedChar(schemaName, encloseChar);

		String[] tableTypes = null;
		switch (config.getDbType()) {
		case DBType.DB_TYPE_ORACLE:
			tableTypes = new String[] {"TABLE", "VIEW", "SYNONYM"}; //$NON-NLS-1$ //$NON-NLS-2$
			break;
		default:
			tableTypes = new String[] {"TABLE", "VIEW"}; //$NON-NLS-1$ //$NON-NLS-2$
			break;
		}
		TableInfo[] result = null;

		String keySchemaName = (schemaName != null) ? schemaName : config.getDbName();
		ObjectCacher holder = ObjectCacher.getInstance(keySchemaName + "@" + config.getDbName());

		synchronized (holder) {
			result = (TableInfo[]) holder.get();
			if (result == null) {
				try {
					if (schemaName != null) {
//						result = TableSearcher.execute(trans.getConnection(), schemaName, tableTypes);
						result = TableSearcher.execute(trans.getConnection(), schemaName, tableTypes, new Character(encloseChar));
					} else {
//						result = TableSearcher.execute(trans.getConnection(), null, tableTypes);
						result = TableSearcher.execute(trans.getConnection(), null, tableTypes, new Character(encloseChar));
					}

				} catch (Exception e) {
					DbPlugin.log(e);
				}
				holder.put(result);
			} else {
			}
		}
		return result;

	}

	public Column[] getColumns(String tableName) {
		return getColumns(currentSchema, tableName);
	}

	public Column[] getColumns(String schemaName, String tableName) {

		schemaName = SQLUtil.removeEnclosedChar(schemaName, encloseChar);
		tableName = SQLUtil.removeEnclosedChar(tableName, encloseChar);

		Column[] result = null;
		ObjectCacher holder = ObjectCacher.getInstance(tableName + "@" + schemaName + "@" + config.getDbName());
		synchronized (holder) {
			result = (Column[]) holder.get();
			if (result == null) {
				ContentAssistTable table = ContentAssistUtil.createContentAssistTable(schemaName, tableName);
				result = table.getColumns();
				holder.put(result);
			} else {
			}
		}
		return result;
	}

	public String findCorrectSchema(String target) {
		SchemaInfo info = (SchemaInfo) schemaInfoMap.get(target.toUpperCase());
		if (info != null) {
			return info.getName();
		}
		return null;
	}

	public String getCurrentSchema() {
		return currentSchema;
	}

	public IDBConfig getConfig() {
		return config;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public SchemaInfo[] getSchemaInfos() {
		if(schemaInfoMap == null) return null;

		SchemaInfo[] infos = new SchemaInfo[schemaInfoMap.size()];
		int i = 0;
		for (Iterator iterator = schemaInfoMap.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			infos[i++] = (SchemaInfo) schemaInfoMap.get(key);
		}
		return infos;
	}

}
