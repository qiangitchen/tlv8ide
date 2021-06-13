package com.tulin.v8.ide.navigator.views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.ui.internal.FlowDraw;
import com.tulin.v8.ide.utils.StudioUtil;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.diff.DDLDiff;
import zigen.plugin.db.diff.IDDLDiff;
import zigen.plugin.db.preference.DBTreeViewPreferencePage;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.BookmarkRoot;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.History;
import zigen.plugin.db.ui.internal.HistoryFolder;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.OracleFunction;
import zigen.plugin.db.ui.internal.OracleSequence;
import zigen.plugin.db.ui.internal.OracleSource;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.Synonym;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.internal.View;

public class TreeLabelProvider extends LabelProvider {
	ImageCacher ic = ImageCacher.getInstance();

	public String getText(Object obj) {
		if (obj instanceof Column) {
			Column column = (Column) obj;
			TableColumn _column = column.getColumn();
			if (_column.getTypeName() != null) {
				return ((Column) obj).getColumnLabel();
			} else {
				return obj.toString();
			}

		} else if (obj instanceof ITable) {
			ITable table = (ITable) obj;
			if (DbPlugin.getDefault().getPreferenceStore()
					.getBoolean(DBTreeViewPreferencePage.P_DISPLAY_TBL_COMMENT)) {
				return table.getLabel();
			} else {
				return table.getName();
			}

		} else if (obj instanceof Schema) {
			Schema schema = (Schema) obj;
			return schema.getName();

		} else if (obj instanceof History) {
			History history = (History) obj;
			return history.getName();

		} else if (obj instanceof DDLDiff) {
			IDDLDiff diff = (IDDLDiff) obj;
			return diff.getName();

		} else {
			return obj.toString();
		}
	}

	public Image getImage(Object obj) {
		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
		if (obj instanceof Root) {
			imageKey = ISharedImages.IMG_OBJ_FOLDER;
		} else if (obj instanceof FlowDraw) {
			return StudioPlugin.getIcon("M.gif");
		} else if (getText(obj).toLowerCase().endsWith("struts.xml")) {
			return StudioPlugin.getIcon("struts.gif");
		} else if (getText(obj).toLowerCase()
				.endsWith("applicationcontext.xml")) {
			return StudioPlugin.getIcon("spring.gif");
		} else if (getText(obj).toLowerCase().endsWith(".fun.xml")
				|| getText(obj).toLowerCase().endsWith(".fun")) {
			return StudioPlugin.getIcon("F.gif");
		} else if (getText(obj).toLowerCase().endsWith(".fn.xml")
				|| getText(obj).toLowerCase().endsWith(".fn")) {
			return StudioPlugin.getIcon("fun.gif");
		} else if (getText(obj).toLowerCase().endsWith(".xml")) {
			return StudioPlugin.getIcon("xml.gif");
		} else if (getText(obj).toLowerCase().endsWith(".java")) {
			return StudioPlugin.getIcon("java.gif");
		} else if (StudioUtil.isJSP(getText(obj).toLowerCase())) {
			return StudioPlugin.getIcon("jsp.gif");
		} else if (StudioUtil.isHTML(getText(obj).toLowerCase())) {
			return StudioPlugin.getIcon("html.gif");
		} else if (getText(obj).toLowerCase().endsWith(".swf")) {
			return StudioPlugin.getIcon("swf.gif");
		} else if (getText(obj).toLowerCase().indexOf(".php") > 0) {
			return StudioPlugin.getIcon("php.gif");
		} else if (getText(obj).toLowerCase().endsWith(".js")) {
			return StudioPlugin.getIcon("js.gif");
		} else if (StudioUtil.isImage(getText(obj).toLowerCase())) {
			return StudioPlugin.getIcon("image.gif");
		} else if (getText(obj).toLowerCase().endsWith(".css")) {
			return StudioPlugin.getIcon("css.gif");
		} else if (StudioUtil.isZip(getText(obj).toLowerCase())) {
			return StudioPlugin.getIcon("yasuo.gif");
		} else if (getText(obj).toLowerCase().endsWith(".prop")) {
			return StudioPlugin.getIcon("prop.gif");
		}
		if (obj instanceof DataBase) {
			DataBase db = (DataBase) obj;
			if (db.isConnected()) {
				return ic.getImage(DbPlugin.IMG_CODE_CONNECTED_DB);
			} else {
				return ic.getImage(DbPlugin.IMG_CODE_DB);
			}

		} else if (obj instanceof Schema) {
			return ic.getImage(DbPlugin.IMG_CODE_SCHEMA);

		} else if (obj instanceof BookmarkRoot) {
			return ic.getImage(DbPlugin.IMG_CODE_BOOKMARK);

		} else if (obj instanceof Synonym) {
			Synonym synonym = (Synonym) obj;
			if (synonym.isEnabled()) {
				return ic.getImage(DbPlugin.IMG_CODE_SYNONYM);
			} else {
				return ic.getImage(DbPlugin.IMG_CODE_DISABLED_SYNONYM);
			}
		} else if (obj instanceof View) {
			return ic.getImage(DbPlugin.IMG_CODE_VIEW);

		} else if (obj instanceof Bookmark) {
			Bookmark bm = (Bookmark) obj;
			if (Bookmark.TYPE_TABLE == bm.getType()) {
				return ic.getImage(DbPlugin.IMG_CODE_TABLE);

			} else if (Bookmark.TYPE_VIEW == bm.getType()) {
				return ic.getImage(DbPlugin.IMG_CODE_VIEW);

			} else if (Bookmark.TYPE_SYNONYM == bm.getType()) {
				return ic.getImage(DbPlugin.IMG_CODE_SYNONYM);

			} else {
				imageKey = ISharedImages.IMG_OBJ_FILE;
			}
		} else if (obj instanceof ITable) {
			ITable table = (ITable) obj;
			if (table.isEnabled()) {
				return ic.getImage(DbPlugin.IMG_CODE_TABLE);
			} else {
				return ic.getImage(DbPlugin.IMG_CODE_DISABLED_TABLE);
			}
		} else if (obj instanceof Column) {
			Column col = (Column) obj;
			if (col.hasPrimaryKey()) {
				return ic.getImage(DbPlugin.IMG_CODE_PK_COLUMN);
			} else {
				if (col.isNotNull()) {
					return ic.getImage(DbPlugin.IMG_CODE_NOTNULL_COLUMN);
				} else {
					return ic.getImage(DbPlugin.IMG_CODE_COLUMN);
				}
			}
		} else if (obj instanceof History) {
			return ic.getImage(DbPlugin.IMG_CODE_CLOCK);

		} else if (obj instanceof HistoryFolder) {
			imageKey = ISharedImages.IMG_OBJ_FOLDER;

		} else if (obj instanceof DDLDiff) {
			return ic.getImage(DbPlugin.IMG_CODE_TABLE);

		} else if (obj instanceof OracleFunction) {
			OracleFunction function = (OracleFunction) obj;
			if (function.hasError()) {
				return ic.getImage(DbPlugin.IMG_CODE_FUNCTION_ERR);
			} else {
				return ic.getImage(DbPlugin.IMG_CODE_FUNCTION);
			}

		} else if (obj instanceof OracleSource) {
			OracleSource source = (OracleSource) obj;
			if (source.hasError()) {
				return ic.getImage(DbPlugin.IMG_CODE_FILE_ERR);
			} else {
				imageKey = ISharedImages.IMG_OBJ_FILE;
			}

		} else if (obj instanceof OracleSequence) {
			return ic.getImage(DbPlugin.IMG_CODE_SEQUENCE);

		} else if (obj instanceof Folder) {
			Folder folder = (Folder) obj;
			if (folder.hasError()) {
				return ic.getImage(DbPlugin.IMG_CODE_FOLDER_ERR);
			} else {
				imageKey = ISharedImages.IMG_OBJ_FOLDER;
			}
		} else if (obj instanceof TreeNode) {
			TreeNode node = (TreeNode) obj;
			if ("file".equals(node.getTvtype())) {
				imageKey = ISharedImages.IMG_OBJ_FILE;
			} else {
				imageKey = ISharedImages.IMG_OBJ_FOLDER;
			}
		}
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);

	}
}
