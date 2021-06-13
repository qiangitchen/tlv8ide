/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import java.io.Serializable;

public class SynonymInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String synonym_name;

	private String table_owner;

	private String table_name;

	private String db_link; // UnSupport

	private String comments;

	public String getDb_link() {
		return db_link;
	}

	public void setDb_link(String db_link) {
		this.db_link = db_link;
	}

	public String getSynonym_name() {
		return synonym_name;
	}

	public void setSynonym_name(String synonym_name) {
		this.synonym_name = synonym_name;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getTable_owner() {
		return table_owner;
	}

	public void setTable_owner(String table_owner) {
		this.table_owner = table_owner;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
