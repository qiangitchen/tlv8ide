package com.tulin.v8.ide.widgets;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.tulin.v8.ide.utils.ImageManager;

class h extends LabelProvider implements ITableLabelProvider {
	h(JSLibraryConfigComposite paramJSLibraryConfigComposite) {
	}

	public Image getColumnImage(Object paramObject, int paramInt) {
		JSLibraryEntity localJSLibraryEntity = (JSLibraryEntity) paramObject;
		if (paramInt == 0) {
			if (localJSLibraryEntity.getName().endsWith(".js"))
				return ImageManager.getIconByFileName("icons/js.gif");
			return ImageManager.getIconByFileName("icons/jsDoc.gif");
		}
		return null;
	}

	public String getColumnText(Object paramObject, int paramInt) {
		JSLibraryEntity localJSLibraryEntity = (JSLibraryEntity) paramObject;
		if (paramInt == 0)
			return localJSLibraryEntity.getName() == null ? ""
					: localJSLibraryEntity.getName();
		if (paramInt == 1)
			return localJSLibraryEntity.getLocalPath() == null ? ""
					: localJSLibraryEntity.getLocalPath();
		return null;
	}
}