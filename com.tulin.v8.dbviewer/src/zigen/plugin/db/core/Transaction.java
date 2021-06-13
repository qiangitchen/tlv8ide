/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;

import zigen.plugin.db.ui.editors.exceptions.NotFoundDBConfigException;

public class Transaction {

	private class TransactionElement {

		IDBConfig config = null;

		Connection con = null;

		int count = 0;

		private TransactionElement(IDBConfig config) {
			this.config = config;
		}

		private boolean isConnecting() {
			return !(con == null);
		}
	}

	private static Hashtable map = new Hashtable();

	private static Transaction instance;

	private IDBConfig config;

	public synchronized static Transaction getInstance(IDBConfig config) {
		if (instance == null) {
			instance = new Transaction();
		}
		instance.config = config;
		instance.create();
		return instance;

	}

	private Transaction() {}

	private void create() {
		if (config == null)
			throw new NotFoundDBConfigException("There is no Data Base definition information. "); //$NON-NLS-1$

		TransactionElement elem = null;
		if (!map.containsKey(config.getDbName())) {
			map.put(config.getDbName(), new TransactionElement(config));

		} else {
			TransactionElement element = (TransactionElement) map.get(config.getDbName());

			if (element.con == null || !config.getUserId().equals(element.config.getUserId())) {
				ConnectionManager.closeConnection(element.con);
				element.con = null;
				element.config = config;

			}
		}
	}

	public Connection getConnection() throws Exception {
		TransactionElement element = (TransactionElement) map.get(config.getDbName());
		if (element.con == null) {
			element.con = ConnectionManager.getConnection(config);
			element.con.setAutoCommit(false);
			element.count = 0;
		}

		if (config.isAutoCommit()) {
			if (!element.con.getAutoCommit()) {
				element.con.setAutoCommit(true);
			}
		} else {
			if (element.con.getAutoCommit()) {
				element.con.setAutoCommit(false);
			}
		}

		return element.con;

	}

	public void cloesConnection() {
		TransactionElement element = (TransactionElement) map.get(config.getDbName());
		ConnectionManager.closeConnection(element.con);
		element.con = null;
	}

	public boolean isConneting() {
		TransactionElement element = (TransactionElement) map.get(config.getDbName());
		return element.isConnecting();
	}

	public void addCount(int rowAffected) throws Exception {
		TransactionElement element = (TransactionElement) map.get(config.getDbName());
		element.count = element.count + rowAffected;
	}

	public void resetCount() throws Exception {
		TransactionElement element = (TransactionElement) map.get(config.getDbName());
		element.count = 0;
	}

	public int getTransactionCount() {
		TransactionElement element = (TransactionElement) map.get(config.getDbName());
		return element.count;
	}

	public void commit() throws Exception {
		TransactionElement element = (TransactionElement) map.get(config.getDbName());
		if (element.con != null) {
			element.con.commit();
			element.count = 0;
		}
	}

	public void rollback() throws Exception {
		TransactionElement element = (TransactionElement) map.get(config.getDbName());
		if (element.con != null) {
			element.con.rollback();
			element.count = 0;
		}
	}

	public IDBConfig getConfig() {
		return this.config;
	}

	public synchronized static void destroy() {
		Enumeration e = map.keys();
		while (e.hasMoreElements()) {
			Object k = e.nextElement();
			TransactionElement elem = (TransactionElement) map.get(k);
			ConnectionManager.closeConnection(elem.con);
			elem.con = null;
		}
	}
}
