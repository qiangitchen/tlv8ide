package com.tulin.v8.ide.wizards.directDetail;

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
import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.ide.wizards.Messages;
import com.tulin.v8.ide.wizards.templet.DirectDetailTemplet;

public class WriteDirectDetail {
	private String dbkey = null;
	private String tableName = null;
	private String subtable = null;
	private String subdirect = null;

	private List<String> columns = null;
	private Map<String, String> labels = null;
	private Map<String, String> widths = null;
	private Map<String, String> datatypes = null;

	private List<String> subcolumns = null;
	private Map<String, String> sublabels = null;
	private Map<String, String> subwidths = null;
	private Map<String, String> subdatatypes = null;

	private String containername = null;
	private String filename = null;

	public WriteDirectDetail(DirectDetailPermision setter, DirectDetailPageEnd endpage) {
		dbkey = setter.dbkey;
		tableName = setter.table;
		subtable = setter.subtable;
		subdirect = setter.subdirect;

		columns = setter.columns;
		labels = setter.labels;
		widths = setter.widths;
		datatypes = setter.datatypes;

		subcolumns = setter.subcolumns;
		sublabels = setter.sublabels;
		subwidths = setter.subwidths;
		subdatatypes = setter.subdatatypes;

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
		StringArray subcolumnsText = new StringArray();
		StringArray sublabelsText = new StringArray();
		StringArray subwidthsText = new StringArray();
		StringArray subdatatypesText = new StringArray();
		for (int i = 0; i < subcolumns.size(); i++) {
			String subcolumn = subcolumns.get(i);
			subcolumnsText.push(subcolumn);
			sublabelsText.push((null == sublabels.get(subcolumn) || "".equals(sublabels.get(subcolumn))) ? subcolumn
					: sublabels.get(subcolumn));
			subwidthsText.push(subwidths.get(subcolumn));
			subdatatypesText.push(subdatatypes.get(subcolumn));
		}
		String pageText = DirectDetailTemplet.getPageContext("simpleDirct", filename, columnsText, labelsText,
				datatypesText);
		String jsText = DirectDetailTemplet.getJsContext("simpleDirct", dbkey, tableName, subtable, subdirect,
				columnsText.join(","), labelsText.join(","), widthsText.join(","), datatypesText.join(","),
				subcolumnsText.join(","), sublabelsText.join(","), subwidthsText.join(","), subdatatypesText.join(","));

		if (filename.indexOf(".") < 0) {
			filename = filename + ".html";
		}

		String PHANTOM_PROJECT_NAME = TuLinPlugin.getCurrentProjectName();
		String PROJECT_WEB_FOLDER = TuLinPlugin.getCurrentProjectWebFolderName();
		String containerPath = containername
				.substring(containername.indexOf("/" + PHANTOM_PROJECT_NAME + "/" + PROJECT_WEB_FOLDER + "/")
						+ PHANTOM_PROJECT_NAME.length() + PROJECT_WEB_FOLDER.length() + 3);
		String rootpath = "";
		if (!"".equals(containerPath)) {
			String[] dotns = containerPath.split("/");
			for (int i = 0; i < dotns.length; i++) {
				rootpath += "../";
			}
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
