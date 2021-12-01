/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views;

public interface ISQLOperationTarget {

	static final int FORMAT = 1001;

	static final int UNFORMAT = 1002;

	static final int ALL_EXECUTE = 1003;

	static final int CURRENT_EXECUTE = 1004;

	static final int SELECTED_EXECUTE = 1005;

	static final int NEXT_SQL = 1006;

	static final int BACK_SQL = 1007;

	static final int LINE_DEL = 1008;

	static final int COMMENT = 1009;

	static final int COMMIT = 1010;

	static final int ROLLBACK = 1011;

	static final int ALL_CLEAR = 1012;

	static final int SCRIPT_EXECUTE = 1013;
}
