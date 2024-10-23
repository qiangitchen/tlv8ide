package tern.internal.resources;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import tern.ITernFileSynchronizer;
import tern.ITernFile;
import tern.ITernProject;
import tern.ITernResourcesManagerDelegate;
import tern.resources.TernFileSynchronizer;
import tern.resources.FilesystemTernFile;
import tern.resources.TernProject;
import tern.utils.ExtensionUtils;

public class DefaultTernResourcesManager implements
		ITernResourcesManagerDelegate {

	private Map<String, ITernProject> projectCache = new HashMap<String, ITernProject>();

	@Override
	public ITernFileSynchronizer createTernFileSynchronizer(ITernProject project) {
		return new TernFileSynchronizer(project);
	}

	@Override
	public ITernFile getTernFile(ITernProject project, String name) {
		File file;
		if (name.startsWith(ITernFile.EXTERNAL_PROTOCOL)) {
			file = new File(
					name.substring(ITernFile.EXTERNAL_PROTOCOL.length()));
		} else {
			file = new File(project.getProjectDir(), name);
		}
		return new FilesystemTernFile(file);
	}

	@Override
	public ITernFile getTernFile(Object fileObject) {
		if (fileObject instanceof File) {
			return new FilesystemTernFile((File) fileObject);
		}
		return null;
	}

	@Override
	public ITernProject getTernProject(Object project, boolean force)
			throws IOException {
		if (!(project instanceof File)) {
			return null;
		}
		File projectDir = (File) project;
		if (!projectDir.exists()) {
			return null;
		}
		String path = projectDir.toString();
		try {
			path = projectDir.getCanonicalPath();
		} catch (Exception e) {
			// ignore
		}
		// cache projects for the particular path
		ITernProject result = projectCache.get(path);
		if (result == null) {
			result = new TernProject(projectDir);
			projectCache.put(path, result);
		}
		return result;
	}

	protected String getExtension(Object fileObject) {
		if (fileObject instanceof ITernFile) {
			return ((ITernFile) fileObject).getFileExtension();
		} else if (fileObject instanceof File) {
			return ExtensionUtils.getFileExtension(((File) fileObject)
					.getName());
		} else if (fileObject instanceof String) {
			return ExtensionUtils.getFileExtension((String) fileObject);
		}
		return null;
	}

	@Override
	public boolean isHTMLFile(Object fileObject) {
		String ext = getExtension(fileObject);
		return ext != null
				&& ExtensionUtils.HTML_EXTENSIONS.contains(ext.toLowerCase());
	}

	@Override
	public boolean isJSFile(Object fileObject) {
		String ext = getExtension(fileObject);
		return ext != null
				&& ExtensionUtils.JS_EXTENSION.equals(ext.toLowerCase());
	}

}
