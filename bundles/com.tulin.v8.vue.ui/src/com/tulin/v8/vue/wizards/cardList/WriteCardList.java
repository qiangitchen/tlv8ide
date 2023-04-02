package com.tulin.v8.vue.wizards.cardList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;

import com.tulin.v8.vue.wizards.Messages;
import com.tulin.v8.vue.wizards.WritePage;
import com.tulin.v8.vue.wizards.templet.CardListTemplet;

public class WriteCardList extends WritePage {
	private String dbkey = null;
	private String tableName = null;
	private String keyField = "fid";

	private String title;
	private String description;
	private String previewImage;
	private String smallIcon;
	private String dataOrder = "";

	private String containername = null;
	private String filename = null;

	public WriteCardList(CardListPage page, CardListEndPage endpage) {
		dbkey = page.getDbkey();
		tableName = page.getTvName();
		keyField = page.getKeyField();

		title = page.getTitle();
		description = page.getDescription();
		previewImage = page.getPreviewImage();
		smallIcon = page.getSmallIcon();

		dataOrder = page.getDataOrder();

		containername = endpage.getContainerName();
		filename = endpage.getFileName();
	}

	public IFile writePage() throws Exception {
		String pageText = CardListTemplet.getPageContext(dbkey, tableName, keyField, dataOrder, title, description,
				previewImage, smallIcon);

		if (filename.indexOf(".") < 0) {
			filename = filename + ".vue";
		}

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containername));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException(
					Messages.getString("wizards.dataselect.message.datatdesc").replace("{1}", containername));
		}
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(filename));
		try {
			InputStream stream = new ByteArrayInputStream(pageText.getBytes("UTF-8"));
			if (file.exists()) {
				file.setContents(stream, true, true, null);
			} else {
				file.create(stream, true, null);
			}
			stream.close();
		} catch (Exception e) {
		}
		return file;
	}

}
