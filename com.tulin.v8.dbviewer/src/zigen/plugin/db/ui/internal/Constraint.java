/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import zigen.plugin.db.core.TableConstraintColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TablePKColumn;

public class Constraint extends TreeNode {

	private static final long serialVersionUID = 1L;

	public static final String PRIMARY_KEY = "PRIMARY KEY";

	public static final String FOREGIN_KEY = "FOREGIN KEY";

	private String name;

	private String type = PRIMARY_KEY;

	private String paramater;

	public Constraint(TablePKColumn[] pks) {
		if (pks instanceof TableFKColumn[]) {
			type = FOREGIN_KEY;
			configure((TableFKColumn[]) pks);
		} else {
			type = PRIMARY_KEY;
			configure(pks);
		}
	}

	public Constraint(TableConstraintColumn[] cons) {
		configure(cons);
	}

	private void configure(TablePKColumn[] pks) {
		if (pks != null && pks.length > 0) {

			StringBuffer sb = new StringBuffer();
			int i = 0;
			for (i = 0; i < pks.length; i++) {
				TablePKColumn pkc = pks[i];
				if (i == 0) {
					name = pkc.getName();
					sb.append("(");
					sb.append(pkc.getColumnName());
				} else {
					sb.append(", " + pkc.getColumnName());
				}
			}

			sb.append(")");
			this.paramater = sb.toString();
		}

	}

	private void configure(TableFKColumn[] fks) {
		if (fks != null && fks.length > 0) {
			boolean cascade = false;
			StringBuffer sb = new StringBuffer();
			StringBuffer sb2 = new StringBuffer();
			int i = 0;
			for (i = 0; i < fks.length; i++) {
				TableFKColumn column = fks[i];
				cascade = column.isCasucade();

				if (i == 0) {
					name = column.getName();
					sb.append("(");
					sb.append(column.getColumnName());

					// Reference
					sb2.append(" REFERENCES ");
					if (column.getPkSchema() != null) {
						sb2.append(column.getPkSchema());
						sb2.append(".");
					}
					sb2.append(column.getPkTableName());
					sb2.append(" ");
					sb2.append("(");
					sb2.append(column.getPkColumnName());

				} else {

					sb.append(", " + column.getColumnName());
					sb2.append(", " + column.getColumnName());
				}
			}
			sb.append(")");
			sb2.append(")");
			if (cascade) {
				sb2.append(" ON DELETE CASCADE");
			}

			this.paramater = sb.toString() + sb2.toString();
		}

	}

	private void configure(TableConstraintColumn[] cons) {
		if (cons != null && cons.length > 0) {

			StringBuffer sb = new StringBuffer();
			int i = 0;
			for (i = 0; i < cons.length; i++) {
				TableConstraintColumn column = cons[i];

				if (i == 0) {

					if (column.getColumnName() == null && !"".equals(column.getSearch_condition())) {
						this.name = column.getName();
						this.type = "CHECK";
						this.paramater = "(" + column.getSearch_condition() + ")";
						return;

					}

					this.name = column.getName();
					if (column.isNonUnique()) {
						this.type = "NONUNIQUE";
					} else {
						this.type = "UNIQUE";
					}
					sb.append("(");
					sb.append(column.getColumnName());

				} else {
					sb.append(", " + column.getColumnName());
				}
			}

			sb.append(")");
			this.paramater = sb.toString();

		}

	}

	public String getName() {
		return name;
	}

	public String getParamater() {
		return paramater;
	}

	public String getType() {
		return type;
	}

}
