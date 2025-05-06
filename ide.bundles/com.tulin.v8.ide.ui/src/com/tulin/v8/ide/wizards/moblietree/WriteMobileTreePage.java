package com.tulin.v8.ide.wizards.moblietree;

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
import com.tulin.v8.ide.wizards.templet.TreeTemplet;
import com.tulin.v8.ide.wizards.templet.utils.TempletsReader;
import com.tulin.v8.ide.wizards.utils.FilePathUtils;

public class WriteMobileTreePage {
	private String dbkey = null;
	private String tableName = null;
	private String ID = null;
	private String parent = null;
	private String name = null;
	private String columns = null;
	private String rootFilter = null;
	private String level = null;
	private String quckpath = null;
	private String containername = null;
	private String filename = null;

	public WriteMobileTreePage(MobileTreedataPermision per, MobileTreePageEnd end) {
		dbkey = per.dbkey;
		tableName = per.table;
		ID = per.ID;
		parent = per.parent;
		name = per.name;
		columns = per.cells;
		rootFilter = per.rootFilter;
		level = per.level;
		quckpath = per.quckpath;
		containername = end.getContainerName();
		filename = end.getFileName();
	}

	public IFile writePage() throws Exception {
		String pageText = TreeTemplet.getPageContext("mobileTree", filename);
		String jsText = TreeTemplet.getJsContext("mobileTree", dbkey, tableName, ID, parent, name, columns, rootFilter,
				level, quckpath);

		if (filename.indexOf(".") < 0) {
			filename = filename + ".html";
		}
		String rootpath = FilePathUtils.getRootPath(containername);
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
