/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule.sqlite;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TablePKColumn;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.preference.SQLFormatPreferencePage;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public class SqliteSQLCreatorFactory extends DefaultSQLCreatorFactory {

	public SqliteSQLCreatorFactory(ITable table) {
		super(table);
	}

	public String VisibleColumnSizePattern() {
		return ""; //$NON-NLS-1$
	}
	protected String getConstraintPKStr() {
		StringBuffer sb = new StringBuffer();
		if (pks == null || pks.length == 0)
			return null;

		int i = 0;
		for (i = 0; i < pks.length; i++) {
			TablePKColumn pkc = pks[i];
			if (i == 0) {
				primaryKeyName = pkc.getName();
				sb.append("PRIMARY KEY ");
				sb.append("(");
				sb.append(pkc.getColumnName());
			} else {
				sb.append(", " + pkc.getColumnName());
			}

		}
		sb.append(")");
		return sb.toString();
	}
}
