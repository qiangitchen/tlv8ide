package com.tulin.v8.webtools.ide.js.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.internal.texteditor.EditPosition;
import org.eclipse.ui.internal.texteditor.TextEditorPlugin;

import com.tulin.v8.webtools.ide.ProjectParams;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.html.HTMLUtil;
import com.tulin.v8.webtools.ide.js.additional.AdditionalJavaScriptCompleterManager;
import com.tulin.v8.webtools.ide.js.additional.IAdditionalJavaScriptCompleter;
import com.tulin.v8.webtools.ide.js.launch.JavaScriptLibraryTable;
import com.tulin.v8.webtools.ide.utils.IOUtil;

public class ModelManager {

	private static ModelManager instance;
	private Map<IFile, ModelInfo> modelCache = new HashMap<IFile, ModelManager.ModelInfo>();
	private Map<IProject, Map<Object, ModelInfo>> libModelMapCache = new HashMap<IProject, Map<Object, ModelInfo>>();
	private Map<IProject, List<IAdditionalJavaScriptCompleter>> completerMapCache = new HashMap<IProject, List<IAdditionalJavaScriptCompleter>>();
	private Map<IProject, String[]> libPathMapCache = new HashMap<IProject, String[]>();

	private ModelManager() {
	}

	public static ModelManager getInstance() {
		if (instance == null) {
			instance = new ModelManager();
		}
		return instance;
	}

	private JavaScriptModel getModel(IFile file, List<JavaScriptModel> modelList) {
		try {
			InputStream in = file.getContents(true);
			String source = new String(IOUtil.readStream(in), file.getCharset());
			JavaScriptModel model = new JavaScriptModel(file, source, modelList);

			return model;
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}
		return null;
	}

	private JavaScriptModel getModel(File file, List<JavaScriptModel> modelList) {
		try {
			InputStream in = new FileInputStream(file);
			String source = new String(IOUtil.readStream(in));
			JavaScriptModel model = new JavaScriptModel(file, source, modelList);

			return model;
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}
		return null;
	}

	public Object getResourceByModel(JavaScriptModel model) {
		if (model != null) {
			return model.getJavaScriptFile();
		}
		return null;
	}

	public JavaScriptModel getCachedModel(IFile file, String source) {
		ModelInfo modelInfo = modelCache.get(file);
		int lastEditPosition = getEditPosition(source);
		IProject project = file.getProject();
		Map<Object, ModelInfo> libModelMap = libModelMapCache.get(project);
		// check if project js files are updated
		if (modelInfo == null || modelInfo.lastModified != file.getModificationStamp()
				|| lastEditPosition != modelInfo.editPosition || libModelMap == null) {
			if (libModelMap == null) {
				libModelMap = new HashMap<Object, ModelInfo>();
				libModelMapCache.put(project, libModelMap);
			}
			boolean isUpdate = false;
			List<JavaScriptModel> libModelList = new ArrayList<JavaScriptModel>();
			List<IAdditionalJavaScriptCompleter> completers = getCompleterList(project);
			if (completers != null) {
				for (IAdditionalJavaScriptCompleter completer : completers) {
					if (isUpdate) {
						List<JavaScriptModel> modelList = completer.loadModel(libModelList);
						if (modelList != null) {
							libModelMap.put(completer, new ModelInfo(modelList));
						}
					} else {
						ModelInfo modelHolder = libModelMap.get(completer);
						if (modelHolder == null || isUpdateModelList(modelHolder.modelList)) {
							List<JavaScriptModel> modelList = completer.loadModel(libModelList);
							if (modelList != null) {
								libModelMap.put(completer, new ModelInfo(modelList));
							}
							isUpdate = true;
						} else {
							libModelList.addAll(modelHolder.modelList);
						}
					}
				}
			}
			try {
				ProjectParams params = new ProjectParams(project);
				String[] jsLibPaths = params.getJavaScripts();
				if (jsLibPaths != null && jsLibPaths.length > 0) {
					IWorkspaceRoot wsroot = ResourcesPlugin.getWorkspace().getRoot();
					for (String jsLibPath : jsLibPaths) {
						if (isUpdate) {
							updateModelForJsLibFile(libModelMap, libModelList, wsroot, jsLibPath, 0);
						} else {
							ModelInfo mInfo = libModelMap.get(jsLibPath);
							if (mInfo == null || isUpdateModelList(mInfo.modelList)) {
								isUpdate = updateModelForJsLibFile(libModelMap, libModelList, wsroot, jsLibPath,
										mInfo == null ? 0 : mInfo.lastModified);
								if (!isUpdate) {
									libModelList.addAll(mInfo.modelList);
								}
							} else {
								libModelList.addAll(mInfo.modelList);
							}
						}
					}
				}
			} catch (Exception e) {
				WebToolsPlugin.logException(e);
			}
			if (isUpdate) {
				for (ModelInfo modelHolder : libModelMap.values()) {
					for (JavaScriptModel model : modelHolder.modelList) {
						model.setUpdate(false);
					}
				}
			}
			modelInfo = new ModelInfo(new JavaScriptModel(file, source, libModelList));
			modelInfo.lastModified = file.getModificationStamp();
			modelInfo.editPosition = lastEditPosition;
			modelCache.put(file, modelInfo);
		}
		return modelInfo.model;
	}

	private int getEditPosition(String source) {
		try {
			EditPosition editPosition = TextEditorPlugin.getDefault().getLastEditPosition();
			int lastEditPosition = editPosition != null ? editPosition.hashCode() : 0;
			return lastEditPosition;
		} catch (Exception e) {
			// TODO is there a more proper value?
			return source.length();
		}
	}

	private boolean updateModelForJsLibFile(Map<Object, ModelInfo> libModelMap, List<JavaScriptModel> libModelList,
			IWorkspaceRoot wsroot, String jsLibPath, long lastModified) {
		if (jsLibPath.startsWith(JavaScriptLibraryTable.PREFIX)) {
			IResource resource = wsroot.findMember(jsLibPath.substring(JavaScriptLibraryTable.PREFIX.length()));
			if (resource != null && resource instanceof IFile && resource.exists()) {
				IFile tfile = (IFile) resource;
				if (tfile.getModificationStamp() > lastModified) {
					JavaScriptModel model = getModel(tfile, libModelList);
					if (model != null) {
						libModelList.add(model);
						ModelInfo modelInfo = new ModelInfo(model);
						modelInfo.lastModified = tfile.getModificationStamp();
						libModelMap.put(jsLibPath, modelInfo);
						return true;
					}
				}
			}
		} else {
			File tfile = new File(jsLibPath);
			if (tfile.lastModified() > lastModified) {
				JavaScriptModel model = getModel(tfile, libModelList);
				if (model != null) {
					libModelList.add(model);
					ModelInfo modelInfo = new ModelInfo(model);
					modelInfo.lastModified = tfile.lastModified();
					libModelMap.put(jsLibPath, modelInfo);
					return true;
				}
			}
		}
		return false;
	}

	private boolean isUpdateModelList(List<JavaScriptModel> modelList) {
		for (JavaScriptModel model : modelList) {
			if (model.isUpdate()) {
				return true;
			}
		}
		return false;
	}

	public void clearCache(IProject project) {
		libModelMapCache.remove(project);
		completerMapCache.remove(project);
		libPathMapCache.remove(project);
		initCache(project);
	}

	public List<IAdditionalJavaScriptCompleter> getCompleterList(IProject project) {
		List<IAdditionalJavaScriptCompleter> completerList = completerMapCache.get(project);
		if (completerList == null) {
			initCache(project);
			completerList = completerMapCache.get(project);
		}
		return completerList;
	}

	private void initCache(IProject project) {
		List<IAdditionalJavaScriptCompleter> completerList = new ArrayList<IAdditionalJavaScriptCompleter>();
		String[] paths = null;
		try {
			ProjectParams params = new ProjectParams(project);

			String[] names = params.getJavaScriptCompleters();
			for (int i = 0; i < names.length; i++) {
				IAdditionalJavaScriptCompleter completer = AdditionalJavaScriptCompleterManager
						.getAdditionalJavaSCriptCompleter(names[i]);
				if (completer != null) {
					completerList.add(completer);
				}
			}

			paths = params.getJavaScriptLibPaths();
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}

		completerMapCache.put(project, completerList);
		libPathMapCache.put(project, paths == null ? new String[0] : paths);
	}

	public String[] getLibPaths(Object resource) {
		IProject project = null;
		if (resource instanceof IResource) {
			project = ((IResource) resource).getProject();
		}
		if (project == null) {
			try {
				project = HTMLUtil.getActiveFile().getProject();
			} catch (Exception e) {
				WebToolsPlugin.logException(e);
			}
		}
		if (project != null) {
			String[] paths = libPathMapCache.get(project);
			if (paths == null) {
				initCache(project);
				paths = libPathMapCache.get(project);
			}
			return paths;
		}
		return new String[0];
	}

	private static class ModelInfo {
		JavaScriptModel model;
		List<JavaScriptModel> modelList;
		long lastModified;
		int editPosition;

		public ModelInfo(JavaScriptModel model) {
			this.model = model;
			modelList = new ArrayList<JavaScriptModel>();
			modelList.add(model);
		}

		public ModelInfo(List<JavaScriptModel> modelList) {
			this.modelList = modelList;
		}
	}

}
