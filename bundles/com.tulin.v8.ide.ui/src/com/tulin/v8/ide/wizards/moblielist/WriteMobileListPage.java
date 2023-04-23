package com.tulin.v8.ide.wizards.moblielist;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;

import com.tulin.v8.ide.wizards.Messages;
import com.tulin.v8.ide.wizards.templet.MobileListTemplet;
import com.tulin.v8.ide.wizards.templet.utils.TempletsReader;
import com.tulin.v8.ide.wizards.utils.FilePathUtils;

public class WriteMobileListPage {
	private String dbkey = null;
	private String tableName = null;
	private String ID = null;
	private String title = null;
	private String text = null;
	private String ellip = null;
	private String filter = null;
	private String containername = null;
	private String filename = null;

	public WriteMobileListPage(MobileListdataPermision per, MobileListPageEnd end) {
		dbkey = per.dbkey;
		tableName = per.table;
		ID = per.ID;
		title = per.title;
		text = per.text;
		ellip = per.ellip;
		filter = per.filter;
		containername = end.getContainerName();
		filename = end.getFileName();
	}

	public IFile writePage() throws Exception {
		String pageText = MobileListTemplet.getPageContext("mobileList", filename);
		String jsText = MobileListTemplet.getJsContext("mobileList", dbkey, tableName, ID, title, text, ellip, filter);

		if (filename.indexOf(".") < 0) {
			filename = filename + ".html";
		}

		String containerPath = FilePathUtils.getContainerPath(containername);
		String rootpath = "";
		if (!"".equals(containerPath)) {
			String[] dotns = containerPath.split("/");
			for (int i = 0; i < dotns.length; i++) {
				rootpath += "../";
			}
		}
		pageText = pageText.replace("/" + TempletsReader.PHANTOM_PROJECT_NAME + "/", rootpath);

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containername));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException(
					Messages.getString("wizards.dataselect.message.datatdesc").replace("{1}", containername));
		}
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(filename));
		final IFile JSfile = container.getFile(new Path(filename.substring(0, filename.lastIndexOf(".")) + ".js"));
		try {
			InputStream stream = new ByteArrayInputStream(pageText.getBytes("UTF-8"));
			InputStream jsstream = new ByteArrayInputStream(jsText.getBytes("UTF-8"));
			if (JSfile.exists()) {
				JSfile.setContents(jsstream, true, true, null);
			} else {
				JSfile.create(jsstream, true, null);
			}
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

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "TuLin Studio", IStatus.OK, message, null);
		throw new CoreException(status);
	}
}
