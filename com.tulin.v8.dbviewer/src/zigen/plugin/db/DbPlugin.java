/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.util.BundleUtility;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import kry.sql.format.SqlFormatRule;
import zigen.plugin.db.core.ConditionManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLHistoryManager;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.preference.SQLFormatPreferencePage;
import zigen.plugin.db.ui.bookmark.BookmarkManager;
import zigen.plugin.db.ui.dialogs.DBDialogSettings;
import zigen.plugin.db.ui.dialogs.IDBDialogSettings;
import zigen.plugin.db.ui.editors.ITableViewEditor;
import zigen.plugin.db.ui.views.TreeContentProvider;
import zigen.plugin.db.ui.views.TreeView;

@SuppressWarnings({ "restriction", "unchecked", "rawtypes" })
public class DbPlugin extends AbstractUIPlugin {

	private static DbPlugin plugin;

	private ResourceBundle resourceBundle;

	private IDBDialogSettings dBDialogSettings;

	private XmlController xmlController;

	private String defaultProject = null;

	private boolean configischange = false;

	public DbPlugin() {
		super();
		plugin = this;

		try {
			resourceBundle = ResourceBundle.getBundle("ResourceBundleMessages"); //$NON-NLS-1$
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	private ImageCacher imageCacher;

	public void start(BundleContext context) throws Exception {
		super.start(context);
		xmlController = new XmlController(getStateLocation());
		imageCacher = ImageCacher.getInstance();
		try {
			initDBConfig(); // 初始化数据源配置
		} catch (Exception e) {
		}
	}

	private void initDBConfig() {
		String readWritePath = getStateLocation().append(DbPluginConstant.FN_DB_SETTINGS).toOSString();
		Connection.ConnectionTranse(readWritePath); // 自动配置
	}

	public static final String getPluginVersion() {
		// String pluginId = getPluginId();
		// IPluginRegistry registry = Platform.getPluginRegistry();
		// IPluginDescriptor descriptor =
		// registry.getPluginDescriptor(pluginId);
		// PluginVersionIdentifier v = descriptor.getVersionIdentifier();
		// int major = v.getMajorComponent();
		// int minor = v.getMinorComponent();
		// int service = v.getServiceComponent();
		// String date = v.getQualifierComponent();
		// return major + "." + minor + "." + service + " release " + date;
		return (String) getDefault().getBundle().getHeaders().get("Bundle-Version");
	}

	public void stop(BundleContext context) throws Exception {
		try {
			imageCacher.clear();

			saveDBDialogSettings();
			xmlController.save();

		} finally {
			super.stop(context);
		}
	}

	public static DbPlugin getDefault() {
		return plugin;
	}

	public static String getResourceString(String key) {
		ResourceBundle bundle = DbPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public SQLHistoryManager getHistoryManager() {
		return xmlController.getHistoryManager();
	}

	public ConditionManager getConditionManager() {
		return xmlController.getConditionManager();
	}

	public BookmarkManager getBookmarkManager() {
		return xmlController.getBookmarkManager();
	}

	public PluginSettingsManager getPluginSettingsManager() {
		return xmlController.getPluginSettingsManager();
	}

	public synchronized IDBDialogSettings getDBDialogSettings() {
		if (configischange) {
			initDBConfig();
			loadDBDialogSettings();
		} else if (dBDialogSettings == null)
			loadDBDialogSettings();
		return dBDialogSettings;
	}

	protected void loadDBDialogSettings() {
		dBDialogSettings = new DBDialogSettings("Workbench"); //$NON-NLS-1$
		String readWritePath = getStateLocation().append(DbPluginConstant.FN_DB_SETTINGS).toOSString();
		File settingsFile = new File(readWritePath);
		if (settingsFile.exists()) {
			try {
				dBDialogSettings.load(readWritePath);
			} catch (IOException e) {
				dBDialogSettings = new DBDialogSettings("Workbench"); //$NON-NLS-1$
			}
		} else {
			URL dsURL = BundleUtility.find(getBundle(), DbPluginConstant.FN_DB_SETTINGS);
			if (dsURL == null)
				return;
			InputStream is = null;
			try {
				is = dsURL.openStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8")); //$NON-NLS-1$
				dBDialogSettings.load(reader);
			} catch (IOException e) {
				dBDialogSettings = new DBDialogSettings("Workbench"); //$NON-NLS-1$
			} finally {
				try {
					if (is != null)
						is.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}
	}

	public void saveDBDialogSettings() {
		if (dBDialogSettings == null) {
			return;
		}
		try {
			String readWritePath = getStateLocation().append(DbPluginConstant.FN_DB_SETTINGS).toOSString();
			dBDialogSettings.save(readWritePath);
		} catch (IOException e) {
			DbPlugin.log(e);
		}
	}

	public static IViewPart findView(String viewId) {
		IWorkbenchPage page = getDefault().getPage();
		if (page != null) {
			return page.findView(viewId);
		} else {
			return null;
		}
	}

	public static IViewPart findView(String viewId, String secondaryId) throws PartInitException {
		IWorkbenchPage page = getDefault().getPage();
		if (page != null) {
			return page.showView(viewId, secondaryId, IWorkbenchPage.VIEW_VISIBLE);
		} else {
			return null;
		}
	}

	public static IViewPart showView(String viewId) throws PartInitException {
		IWorkbenchPage page = getDefault().getPage();
		if (page != null) {
			return page.showView(viewId);
		} else {
			return null;
		}
	}

	public static IViewPart showView(String viewId, String secondaryId) throws PartInitException {
		IWorkbenchPage page = getDefault().getPage();
		if (page != null) {
			return page.showView(viewId, secondaryId, IWorkbenchPage.VIEW_ACTIVATE);
		} else {
			return null;
		}
	}

	public static void hideView(IViewPart view) {
		IWorkbenchPage page = getDefault().getPage();
		if (page != null && view != null) {
			getDefault().getPage().hideView(view);
		}

	}

	public IWorkbenchPage getPage() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		return window.getActivePage();
	}

	public Shell getShell() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		return window.getShell();
	}

	public static ITableViewEditor getActiveTableViewEditor() {
		IEditorPart editor = DbPlugin.getDefault().getPage().getActiveEditor();
		if (editor instanceof ITableViewEditor) {
			return (ITableViewEditor) editor;
		} else {
			return null;
		}
	}

	public static void getCloseEditors(IEditorReference[] editorRefs) {
		DbPlugin.getDefault().getPage().closeEditors(editorRefs, true);
	}

	public void showInformationMessage(String message) {
		MessageDialog.openInformation(getShell(), DbPluginConstant.TITLE, message);
	}

	public void showWarningMessage(String message) {
		MessageDialog.openWarning(getShell(), DbPluginConstant.TITLE, message);
	}

	public boolean confirmDialog(String message) {
		return MessageDialog.openConfirm(getShell(), DbPluginConstant.TITLE, message);
	}

	public MessageDialogWithToggle confirmDialogWithToggle(String message, String toggleMessage, boolean toggleStatus) {
		return MessageDialogWithToggle.openYesNoQuestion(getShell(), DbPluginConstant.TITLE, message, toggleMessage,
				toggleStatus, null, null);
	}

	public String getDefaultProject() {
		return defaultProject;
	}

	public void setDefaultProject(String defaultProject) {
		this.defaultProject = defaultProject;
	}

	private static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}

	public static void log(String message, Throwable e) {
		IStatus status = new Status(IStatus.ERROR, getPluginId(), IStatus.ERROR, message, e);
		getDefault().getLog().log(status);
	}

	public static void log(String message) {
		log(message, null);
	}

	public static void log(Throwable e) {
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		String message = stringWriter.getBuffer().toString();
		log(message, e);

		// for debug
		if (e != null)
			e.printStackTrace();
	}

	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}

	private static String getErrorMessage(Throwable throwable) {
		if (throwable != null) {
			String msg = throwable.getMessage();
			if (msg == null) {
				return getErrorMessage(throwable.getCause());
			} else {
				return msg;
			}
		} else {
			return Messages.getString("DbPlugin.ConfrimStatckTraceMessage"); //$NON-NLS-1$
		}
	}

	private static String getFirstLineMessage(String message) {
		StringUtil.convertLineSep(message);
		int pos = message.indexOf("\n"); //$NON-NLS-1$
		if (pos > 0) {
			return message.substring(0, pos);
		} else {
			return message;
		}

	}

	static IStatus createErrorStatus(Throwable throwable) {
		String msg = getErrorMessage(throwable);
		String lineMsg = getFirstLineMessage(msg);

		// MultiStatus mStatus = new MultiStatus(getPluginId(), IStatus.ERROR,
		// lineMsg, new Exception(Messages.getString("DbPlugin.1")));
		// //$NON-NLS-1$
		// MultiStatus mStatus = new MultiStatus(getPluginId(), IStatus.ERROR,
		// lineMsg, null); //$NON-NLS-1$
		// mStatus.add(new Status(IStatus.ERROR, getPluginId(), IStatus.OK, msg,
		// null));
		// return mStatus;
		return new Status(IStatus.ERROR, getPluginId(), IStatus.OK, lineMsg, throwable);

	}

	public static IStatus createWarningStatus(Throwable throwable) {
		return createWarningStatus(-1, throwable);
	}

	static IStatus createWarningStatus(int errorCode, Throwable throwable) {
		String msg = getErrorMessage(throwable);
		msg = getFirstLineMessage(msg);
		return new Status(IStatus.WARNING, getPluginId(), errorCode, msg, null);
	}

	public void showErrorDialog(Throwable throwable) {
		// for Debug
		if (throwable != null) {
			throwable.printStackTrace();
		}

		String message = Messages.getString("DbPlugin.ErrorMessage"); //$NON-NLS-1$
		IStatus status = null;

		if (throwable instanceof JobException) {
			JobException je = (JobException) throwable;
			Throwable cause = je.getCause();
			if (cause instanceof SQLException) {
				status = createErrorStatus(cause);
			} else {
				status = createErrorStatus(cause);

			}
		} else {

			if (throwable instanceof SQLException) {
				status = createErrorStatus(throwable);
			} else {
				status = createErrorStatus(throwable);
			}
		}
		ErrorDialog.openError(getShell(), DbPluginConstant.TITLE, message, status);
	}

	private String defaultSaveDir = null;

	public String getDefaultSaveDir() {
		return defaultSaveDir;
	}

	public void setDefaultSaveDir(String defaultSaveDir) {
		this.defaultSaveDir = defaultSaveDir;
	}

	public ImageDescriptor getImageDescriptor(String imageKey) {
		return getImageRegistry().getDescriptor(imageKey);
	}

	public Image getImage(String imageCode) {
		return imageCacher.getImage(imageCode);
	}

	public static Font getDefaultFont() {
		Font font = JFaceResources.getTextFont();
		return font;
	}

	public static Font getSmallFont() {
		Font newFont = null;
		Font font = getDefaultFont();
		FontData[] datas = font.getFontData();
		if (datas.length > 0) {
			FontData data = datas[0];
			int currnetHeight = data.getHeight();
			data.setHeight(currnetHeight - 1);
			newFont = new Font(Display.getDefault(), data);
		}
		return newFont;
	}

	public static FontData getDefaultFontData() {
		Font font = getDefaultFont();
		FontData[] datas = font.getFontData();
		if (datas.length > 0) {
			return datas[0];
		}
		return null;
	}

	public static SqlFormatRule getSqlFormatRult() {
		SqlFormatRule rule = DbPluginFormatRule.getInstance().getRule();
		IPreferenceStore st = getDefault().getPreferenceStore();
		int tabSize = st.getInt(SQLFormatPreferencePage.P_FORMAT_OPTION_TABSIZE);
		boolean optDecode = st.getBoolean(SQLFormatPreferencePage.P_FORMAT_OPTION_DECODE);
		boolean optIn = st.getBoolean(SQLFormatPreferencePage.P_FORMAT_OPTION_IN);

		rule.setIndentString(StringUtil.padding("", tabSize)); //$NON-NLS-1$
		rule.setDecodeSpecialFormat(!optDecode);
		rule.setInSpecialFormat(optIn);
		return rule;
	}

	public static IDBConfig[] getDBConfigs() {
		TreeView tv = (TreeView) findView(DbPluginConstant.VIEW_ID_TreeView);
		if (tv != null) {
			TreeContentProvider tcp = (TreeContentProvider) tv.getContentProvider();
			return tcp.getDBConfigs();
		} else {
			return null;
		}
	}

	private static List listeners = new ArrayList();

	public static void addStatusChangeListener(IStatusChangeListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	public static void removeStatusChangeListener(IStatusChangeListener listener) {
		listeners.remove(listener);
	}

	public static void fireStatusChangeListener(Object obj, int status) {
		for (Iterator iter = listeners.iterator(); iter.hasNext();) {
			IStatusChangeListener element = (IStatusChangeListener) iter.next();
			if (element != null) {
				element.statusChanged(obj, status);
			}
		}

	}

	public static final String IMG_CODE_BACK = "back.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_BOOKMARK = "bookmark.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_COLUMN = "column.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_CLOCK = "clock.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_DB_ADD = "db_add.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_DB_EDIT = "db_edit.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_DB_COPY = "db_copy.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_DB_DELETE = "db_delete.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_DB = "db.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_CONNECTED_DB = "connecteddb.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_FORWARD = "forward.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_DB_NEW = "new_db.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_NOTNULL_COLUMN = "notNull.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_PK_COLUMN = "primarykey.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_REFRESH = "refresh.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_SCHEMA = "schema.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_SQL = "sql.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_TABLE = "table.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_DISABLED_TABLE = "disabledtable.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_DISABLED_SYNONYM = "disabledsynonym.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_UNIQUEKEY = "uniquekey.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_ASC = "asc.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_DESC = "desc.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_BLANK = "blank.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_SAVE = "save.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_UNKNOWN = "unknown.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_CHECKED = "checked.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_UNCHECKED = "unchecked.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_DISABLED_CHECKED = "disabledchecked.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_CHECKED_CENTER = "checkedcenter.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_UNCHECKED_CENTER = "uncheckedcenter.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_DISABLED_CHECKED_CENTER = "disabledcheckedcenter.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_TEMPLATE = "template.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_VIEW = "view.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_FUNCTION = "function.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_SYNONYM = "synonym.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_SEQUENCE = "sequence.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_EXECUTE = "execute.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_CLEAR = "clear.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_COMMIT = "commit.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_ROLLBACK = "rollback.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_IMPORT = "import.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_EXPORT = "export.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_FORMAT = "format.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_FORMAT2 = "format2.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_VERTICALLAYOUT = "verticallayout.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_HORIZONTALLAYOUT = "horizontallayout.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_REMOVE = "remove_exc.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_REMOVE_ALL = "removea_exc.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_PIN = "pin.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_OPEN = "open.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_SYNCED = "synced.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_COLUMN_ADD = "column_add.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_COLUMN_EDIT = "column_edit.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_COLUMN_DEL = "column_del.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_COLUMN_DUPLICATE = "column_duplicate.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_SCRIPT = "script.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_ERROR_ROOT = "error_root.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_ERROR = "error.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_AUTO = "auto_commit.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_MANUAL = "manual_commit.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_DUMMY = "dummy.gif"; //$NON-NLS-1$

	public static final String IMG_CODE_FILTER = "filter.gif";//$NON-NLS-1$

	public static final String IMG_CODE_PIN_COLUMN = "pin_column.gif";//$NON-NLS-1$

	public static final String IMG_CODE_ALIAS = "alias.gif";//$NON-NLS-1$

	public static final String IMG_CODE_RESULT = "result.gif";//$NON-NLS-1$

	public static final String IMG_CODE_ADD = "add.gif";//$NON-NLS-1$

	public static final String IMG_CODE_TOP = "top.gif";//$NON-NLS-1$

	public static final String IMG_CODE_END = "end.gif";//$NON-NLS-1$

	public static final String IMG_CODE_PREVIOUS = "previous.gif";//$NON-NLS-1$

	public static final String IMG_CODE_NEXT = "next.gif";//$NON-NLS-1$

	public static final String IMG_CODE_EXPAND_ALL = "expandAll.gif";//$NON-NLS-1$

	public static final String IMG_CODE_COLLAPSE_ALL = "collapseAll.gif";//$NON-NLS-1$

	public static final String IMG_CODE_WARNING = "warning.gif";//$NON-NLS-1$

	public static final String IMG_CODE_FOLDER_ERR = "folder_err.gif";//$NON-NLS-1$

	public static final String IMG_CODE_FUNCTION_ERR = "function_err.gif";//$NON-NLS-1$

	public static final String IMG_CODE_FILE_ERR = "file_err.gif";//$NON-NLS-1$

	protected void initializeImageRegistry(ImageRegistry registry) {

		registerImage(registry, IMG_CODE_SQL);
		registerImage(registry, IMG_CODE_SCHEMA);
		registerImage(registry, IMG_CODE_TABLE);
		registerImage(registry, IMG_CODE_DISABLED_TABLE);
		registerImage(registry, IMG_CODE_DISABLED_SYNONYM);
		registerImage(registry, IMG_CODE_PK_COLUMN);
		registerImage(registry, IMG_CODE_COLUMN);
		registerImage(registry, IMG_CODE_PK_COLUMN);
		registerImage(registry, IMG_CODE_NOTNULL_COLUMN);

		registerImage(registry, IMG_CODE_EXECUTE);
		registerImage(registry, IMG_CODE_CLEAR);

		registerImage(registry, IMG_CODE_FORWARD);
		registerImage(registry, IMG_CODE_BACK);

		registerImage(registry, IMG_CODE_REFRESH);
		registerImage(registry, IMG_CODE_COMMIT);
		registerImage(registry, IMG_CODE_ROLLBACK);

		registerImage(registry, IMG_CODE_DB_ADD);
		registerImage(registry, IMG_CODE_DB_EDIT);
		registerImage(registry, IMG_CODE_DB_DELETE);

		registerImage(registry, IMG_CODE_BOOKMARK);

		registerImage(registry, IMG_CODE_CLOCK);
		registerImage(registry, IMG_CODE_DB);
		registerImage(registry, IMG_CODE_CONNECTED_DB);

		registerImage(registry, IMG_CODE_DB_NEW);
		registerImage(registry, IMG_CODE_DB_COPY);

		registerImage(registry, IMG_CODE_ASC);
		registerImage(registry, IMG_CODE_DESC);
		registerImage(registry, IMG_CODE_BLANK);
		registerImage(registry, IMG_CODE_SAVE);
		registerImage(registry, IMG_CODE_UNKNOWN);

		registerImage(registry, IMG_CODE_CHECKED);
		registerImage(registry, IMG_CODE_UNCHECKED);
		registerImage(registry, IMG_CODE_DISABLED_CHECKED);

		registerImage(registry, IMG_CODE_CHECKED_CENTER);
		registerImage(registry, IMG_CODE_UNCHECKED_CENTER);
		registerImage(registry, IMG_CODE_DISABLED_CHECKED_CENTER);

		registerImage(registry, IMG_CODE_TEMPLATE);

		registerImage(registry, IMG_CODE_VIEW);
		registerImage(registry, IMG_CODE_FUNCTION);
		registerImage(registry, IMG_CODE_SYNONYM);
		registerImage(registry, IMG_CODE_SEQUENCE);
		registerImage(registry, IMG_CODE_IMPORT);
		registerImage(registry, IMG_CODE_EXPORT);

		registerImage(registry, IMG_CODE_FORMAT);
		registerImage(registry, IMG_CODE_FORMAT2);
		registerImage(registry, IMG_CODE_VERTICALLAYOUT);
		registerImage(registry, IMG_CODE_HORIZONTALLAYOUT);

		registerImage(registry, IMG_CODE_REMOVE);
		registerImage(registry, IMG_CODE_REMOVE_ALL);

		registerImage(registry, IMG_CODE_PIN);
		registerImage(registry, IMG_CODE_OPEN);
		registerImage(registry, IMG_CODE_SYNCED);

		registerImage(registry, IMG_CODE_COLUMN_ADD);
		registerImage(registry, IMG_CODE_COLUMN_EDIT);
		registerImage(registry, IMG_CODE_COLUMN_DEL);
		registerImage(registry, IMG_CODE_COLUMN_DUPLICATE);
		registerImage(registry, IMG_CODE_SCRIPT);

		registerImage(registry, IMG_CODE_ERROR_ROOT);
		registerImage(registry, IMG_CODE_ERROR);

		registerImage(registry, IMG_CODE_AUTO);
		registerImage(registry, IMG_CODE_MANUAL);

		registerImage(registry, IMG_CODE_DUMMY);
		registerImage(registry, IMG_CODE_FILTER);

		registerImage(registry, IMG_CODE_PIN_COLUMN);
		registerImage(registry, IMG_CODE_ALIAS);
		registerImage(registry, IMG_CODE_RESULT);
		registerImage(registry, IMG_CODE_ADD);

		registerImage(registry, IMG_CODE_TOP);
		registerImage(registry, IMG_CODE_END);
		registerImage(registry, IMG_CODE_PREVIOUS);
		registerImage(registry, IMG_CODE_NEXT);
		registerImage(registry, IMG_CODE_EXPAND_ALL);
		registerImage(registry, IMG_CODE_COLLAPSE_ALL);

		registerImage(registry, IMG_CODE_WARNING);
		registerImage(registry, IMG_CODE_FOLDER_ERR);
		registerImage(registry, IMG_CODE_FUNCTION_ERR);
		registerImage(registry, IMG_CODE_FILE_ERR);

	}

	@SuppressWarnings("deprecation")
	private void registerImage(ImageRegistry registry, String fileName) {
		try {
			IPath path = new Path("icons/" + fileName); //$NON-NLS-1$
			URL url = find(path);
			if (url != null) {
				ImageDescriptor desc = ImageDescriptor.createFromURL(url);
				registry.put(fileName, desc);
			}
		} catch (Exception e) {
			DbPlugin.log(e);
		}
	}

	static String secondarlyId = null;

	public static void setSecondarlyId(String _secondarlyId) {
		secondarlyId = _secondarlyId;
	}

	public static String getSecondarlyId() {
		return secondarlyId;
	}

	public void setConfigischange(boolean configischange) {
		this.configischange = configischange;
	}

	public boolean getConfigischange() {
		return configischange;
	}
}
