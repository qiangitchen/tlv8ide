/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.dialogs;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;

public class DriverLabelProvider extends LabelProvider {

	ImageCacher ic = ImageCacher.getInstance();

	public String getText(Object obj) {
		return obj.toString();
	}

	public Image getImage(Object obj) {

		String imageKey = ISharedImages.IMG_OBJ_FILE;

		if (obj instanceof DataBase) {
			return ic.getImage(DbPlugin.IMG_CODE_DB);

		} else if (obj instanceof Folder) {
			imageKey = ISharedImages.IMG_OBJ_FOLDER;
		}

		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
}
