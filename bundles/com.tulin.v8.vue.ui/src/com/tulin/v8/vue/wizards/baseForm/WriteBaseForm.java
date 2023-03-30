package com.tulin.v8.vue.wizards.baseForm;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
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
import org.json.JSONObject;

import com.tulin.v8.core.StringArray;
import com.tulin.v8.vue.wizards.Messages;
import com.tulin.v8.vue.wizards.templet.BaseFormTemplet;

public class WriteBaseForm {
	private String dbkey = null;
	private String tableName = null;
	private String keyField = "fid";
	private List<String> columns = null;
	private Map<String, String> labels = null;
	private Map<String, String> dedatatypes = new HashMap<String, String>();
	private Map<String, String> expands = new HashMap<String, String>();
	private String containername = null;
	private String filename = null;

	public WriteBaseForm(BaseFormPage setter, BaseFormEndPage endpage) {
		dbkey = setter.getDbkey();
		tableName = setter.getTvName();
		columns = setter.getColumns();
		labels = setter.getLabels();
		dedatatypes = setter.getDedatatypes();
		expands = setter.getExpands();
		containername = endpage.getContainerName();
		filename = endpage.getFileName();
	}

	public IFile writePage() throws Exception {
		StringArray formInfo = new StringArray();
		JSONObject formColumns = new JSONObject();
		for (int i = 0; i < columns.size(); i++) {
			String column = columns.get(i);
			formColumns.put(column, "");

			String dataType = dedatatypes.get(column);
			String label = labels.get(column);
			String expand = expands.get(column);

			formInfo.push("<a-form-item ref=\"" + column + "\" label=\"" + label + "\" name=\"" + column + "\">\n");
			if ("select".equals(dataType)) {
				formInfo.push("<a-select v-model:value=\"form." + column + "\" placeholder=\"\">\n");
				if (expand != null && !"".equals(expand)) {
					String[] options = expand.split(",");
					for (String option : options) {
						formInfo.push("<a-select-option value=\"" + option + "\">" + option + "</a-select-option>\n");
					}
				}
				formInfo.push("</a-select>");
			} else if ("datetime".equals(dataType)) {
				formInfo.push("<a-date-picker v-model:value=\"form." + column + "\""
						+ " show-time type=\"date\" placeholder=\"Pick a date\" style=\"width: 100%\"/>");
			} else if ("date".equals(dataType)) {
				formInfo.push("<a-date-picker v-model:value=\"form." + column + "\""
						+ " type=\"date\" placeholder=\"Pick a date\" style=\"width: 100%\"/>");
			} else if ("switch".equals(dataType)) {
				formInfo.push("<a-switch v-model:checked=\"form." + column + "\"/>");
			} else if ("checkbox".equals(dataType)) {
				formInfo.push("<a-checkbox-group v-model:value=\"form." + column + "\">");
				if (expand != null && !"".equals(expand)) {
					String[] options = expand.split(",");
					for (String option : options) {
						formInfo.push("<a-checkbox value=\"" + option + "\" name=\"" + column + "\">" + option
								+ "</a-checkbox>\n");
					}
				}
				formInfo.push("</a-checkbox-group>");
			} else if ("radio".equals(dataType)) {
				formInfo.push("<a-radio-group v-model:value=\"form." + column + "\">");
				if (expand != null && !"".equals(expand)) {
					String[] options = expand.split(",");
					for (String option : options) {
						formInfo.push("<a-radio value=\"" + option + "\">" + option + "</a-radio>\n");
					}
				}
				formInfo.push("</a-radio-group>");
			} else if ("textarea".equals(dataType)) {
				formInfo.push("<a-textarea v-model:value=\"form." + column + "\"/>");
			} else {
				formInfo.push("<a-input v-model:value=\"form." + column + "\"/>");
			}
			formInfo.push("</a-form-item>");
		}
		String pageText = BaseFormTemplet.getPageContext(dbkey, tableName, keyField, formInfo, formColumns);

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

	void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "TuLin Studio", IStatus.OK, message, null);
		throw new CoreException(status);
	}
}
