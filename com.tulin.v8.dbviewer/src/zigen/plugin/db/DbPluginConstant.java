/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db;


public class DbPluginConstant {

//	public static String VERSION = "1.1.0 release 2009/01/08"; //$NON-NLS-1$

	public static String LINE_SEP = System.getProperty("line.separator"); //$NON-NLS-1$

	public static String FILE_ENCODING = System.getProperty("file.encoding"); //$NON-NLS-1$

	public static final String TREE_LEAF_LOADING = Messages.getString("DbPluginConstant.1"); //$NON-NLS-1$

	public static final String TITLE = "ZIGEN's DBViewer Plugin"; //$NON-NLS-1$

	public static final String VIEW_ID_TreeView = "zigen.plugin.db.ui.views.TreeView"; //$NON-NLS-1$

	public static final String VIEW_ID_HistoryView = "zigen.plugin.db.ui.views.HistoryView"; //$NON-NLS-1$

	public static final String VIEW_ID_SQLExecute = "zigen.plugin.db.ui.views.SQLExecuteView"; //$NON-NLS-1$

	public static final String VIEW_ID_OUTLINE = "zigen.plugin.db.ui.views.ContentOutline";

	public static final String EDITOR_ID_TableView = "zigen.plugin.db.ui.editors.TableViewEditor"; //$NON-NLS-1$

	public static final String EDITOR_ID_TableEditor = "zigen.plugin.db.ui.editors.TableEditor"; //$NON-NLS-1$

	public static final String EDITOR_ID_QueryView = "zigen.plugin.db.ui.editors.QueryViewEditor"; //$NON-NLS-1$

	public static final String EDITOR_ID_QueryView2 = "zigen.plugin.db.ui.editors.QueryViewEditor2"; //$NON-NLS-1$

	public static final String EDITOR_ID_SQL = "zigen.plugin.db.ui.editors.sql.SqlEditor"; //$NON-NLS-1$

	public static final String EDITOR_ID_SOURCE = "zigen.plugin.db.ui.editors.sql.SourceEditor"; //$NON-NLS-1$

	public static final String EDITOR_ID_SEQUENCE = "zigen.plugin.db.ui.editors.sql.SequenceEditor"; //$NON-NLS-1$

	public static final String FN_DB_SETTINGS = "db_settings.xml"; //$NON-NLS-1$

	public static final String FN_SQL_HISTORY = "sql_history.xml"; //$NON-NLS-1$

	public static final String FN_CONDITION_HISTORY = "condition_history.xml"; //$NON-NLS-1$

	public static final String FN_BOOKMARK = "bookmark.xml"; //$NON-NLS-1$

	public static final String FN_PLUGIN = "plugin_settings.xml"; //$NON-NLS-1$

	public static final String MSG_NO_CONNECTED_DB = Messages.getString("DbPluginConstant.19"); //$NON-NLS-1$

}
