package com.tulin.v8.ide.wizards.listDetail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;

import com.tulin.v8.core.StringArray;
import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.wizards.Messages;
import com.tulin.v8.ide.wizards.templet.ListDetailTemplet;

public class WriteListDetailTag {
	private String dbkey = null;
	private String tableName = null;
	private List<String> columns = null;
	private Map<String, String> labels = null;
	private Map<String, String> widths = null;
	private Map<String, String> datatypes = null;

	public List<String> decolumns = null;
	public Map<String, String> delabels = null;
	public Map<String, String> dedatatypes = null;

	private String containername = null;
	private String filename = null;

	public WriteListDetailTag(ListDetailPermision setter, ListDetailPageEnd endpage) {
		dbkey = setter.dbkey;
		tableName = setter.table;
		columns = setter.columns;
		labels = setter.labels;
		widths = setter.widths;
		datatypes = setter.datatypes;

		decolumns = setter.decolumns;
		delabels = setter.delabels;
		dedatatypes = setter.dedatatypes;

		containername = endpage.getContainerName();
		filename = endpage.getFileName();
	}

	public IFile writePage() throws Exception {
		StringArray columnsText = new StringArray();
		StringArray labelsText = new StringArray();
		StringArray widthsText = new StringArray();
		StringArray datatypesText = new StringArray();
		for (int i = 0; i < columns.size(); i++) {
			String column = columns.get(i);
			columnsText.push(column);
			labelsText
					.push((null == labels.get(column) || "".equals(labels.get(column))) ? column : labels.get(column));
			widthsText.push(widths.get(column));
			datatypesText.push(datatypes.get(column));
		}
		StringArray decolumnsText = new StringArray();
		StringArray delabelsText = new StringArray();
		StringArray dedatatypesText = new StringArray();
		for (int i = 0; i < decolumns.size(); i++) {
			String column = decolumns.get(i);
			decolumnsText.push(column);
			delabelsText.push(
					(null == delabels.get(column) || "".equals(delabels.get(column))) ? column : delabels.get(column));
			dedatatypesText.push(dedatatypes.get(column));
		}
		String pageText = ListDetailTemplet.getPageContext("listdetailTag", filename, decolumnsText, delabelsText,
				dedatatypesText);
		String jsText = ListDetailTemplet.getJsContext("listdetailTag", dbkey, tableName, columnsText.join(","),
				labelsText.join(","), widthsText.join(","), datatypesText.join(","));

		if (filename.indexOf(".") < 0) {
			filename = filename + ".html";
		}

		String PHANTOM_PROJECT_NAME = StudioPlugin.getCurrentProjectName();
		String PROJECT_WEB_FOLDER = StudioPlugin.getCurrentProjectWebFolder();
		String containerPath = containername
				.substring(containername.indexOf("/" + PROJECT_WEB_FOLDER + "/") + PROJECT_WEB_FOLDER.length() + 2);
		String[] dotns = containerPath.split("/");
		String rootpath = "";
		for (int i = 0; i < dotns.length; i++) {
			rootpath += "../";
		}
		pageText = pageText.replace("/" + PHANTOM_PROJECT_NAME + "/", rootpath);

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
