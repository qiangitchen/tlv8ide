/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import zigen.plugin.db.core.TableIDXColumn;

public class Index extends TreeNode {

	private static final long serialVersionUID = 1L;

	private String name;

	private String type;

	private String paramater;

	private String indexType;

	public Index(TableIDXColumn[] idxs) {
		configure(idxs);
	}

	private void configure(TableIDXColumn[] idxs) {
		if (idxs != null && idxs.length > 0) {
			StringBuffer sb = new StringBuffer();
			StringBuffer sb2 = new StringBuffer();
			int i = 0;
			for (i = 0; i < idxs.length; i++) {
				TableIDXColumn column = idxs[i];

				if (i == 0) {
					if (column.isNonUnique()) {
						type = "NONUNIQUE INDEX";
					} else {
						type = "UNIQUE INDEX";
					}

					this.name = column.getName();
					this.indexType = column.getIndexType();

					// sb.append("(");
					sb.append(column.getColumnName());

				} else {
					sb.append(", " + column.getColumnName());
				}
			}

			// sb.append(")");
			this.paramater = sb.toString() + sb2.toString();
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

	public String getIndexType() {
		return indexType;
	}

}
