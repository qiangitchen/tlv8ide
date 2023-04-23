package com.tulin.v8.vue.wizards.gridCheckDialog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.json.JSONArray;
import org.json.JSONObject;

import com.tulin.v8.vue.wizards.Messages;
import com.tulin.v8.vue.wizards.WritePage;
import com.tulin.v8.vue.wizards.templet.GridCheckDialogTemplet;

public class WriteGridCheckDialog extends WritePage {
	private String dbkey = null;
	private String tableName = null;
	private String keyField = "fid";
	private String dataOrder = "";
	private List<String> columns = null;
	private Map<String, String> labels = null;
	private Map<String, String> widths = null;
	private String containername = null;
	private String filename = null;

	public WriteGridCheckDialog(GridCheckDialogLayoutPage setter, GridCheckDialogEndPage endpage) {
		dbkey = setter.getDbkey();
		tableName = setter.getTvName();
		keyField = setter.getKeyField();
		columns = setter.getColumns();
		labels = setter.getLabels();
		widths = setter.getWidths();
		containername = endpage.getContainerName();
		filename = endpage.getFileName();
	}

	public IFile writePage() throws Exception {
		JSONArray jsona = new JSONArray();
		JSONArray searchColumns = new JSONArray();
		for (int i = 0; i < columns.size(); i++) {
			JSONObject json = new JSONObject();
			String column = columns.get(i);
			json.put("dataIndex", column);
			json.put("key", column);
			String title = (null == labels.get(column) || "".equals(labels.get(column))) ? column : labels.get(column);
			json.put("title", title);
			json.put("width", widths.get(column));
			jsona.put(json);
			searchColumns.put(column);
		}
		String pageText = GridCheckDialogTemplet.getPageContext(dbkey, tableName, keyField, dataOrder, jsona,
				searchColumns);

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
