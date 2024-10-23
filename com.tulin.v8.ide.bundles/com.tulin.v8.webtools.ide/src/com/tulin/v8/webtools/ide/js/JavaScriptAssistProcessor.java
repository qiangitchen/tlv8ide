package com.tulin.v8.webtools.ide.js;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ContextInformationValidator;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.swt.graphics.Image;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.html.HTMLUtil;
import com.tulin.v8.webtools.ide.js.launch.JavaScriptLibPathTable;
import com.tulin.v8.webtools.ide.js.model.JavaScriptContext;
import com.tulin.v8.webtools.ide.js.model.JavaScriptElement;
import com.tulin.v8.webtools.ide.js.model.JavaScriptFunction;
import com.tulin.v8.webtools.ide.js.model.JavaScriptModel;
import com.tulin.v8.webtools.ide.js.model.JavaScriptPrototype;
import com.tulin.v8.webtools.ide.js.model.JavaScriptVariable;
import com.tulin.v8.webtools.ide.js.model.ModelManager;
import com.tulin.v8.webtools.ide.js.model.JavaScriptFunction.JavaScriptArgument;
import com.tulin.v8.webtools.ide.template.HTMLTemplateManager;
import com.tulin.v8.webtools.ide.template.JavaScriptContextType;
import com.tulin.v8.webtools.ide.utils.IOUtil;

/**
 * IContentAssistProcessor implementation for JavaScriptEditor.
 */
public class JavaScriptAssistProcessor implements IContentAssistProcessor {

	private static final String[] DEFAULT_OBJECT_TYPES = new String[] { "Object", "String", "Number", "Array" };

	/**
	 * Returns source code to parse from <code>ITextViewer</code>.
	 * <p>
	 * If you want to use this class with the your own editor which supports the
	 * document contains JavaScript such as HTML/JSP, override this method as
	 * returns only JavaScript code.
	 * 
	 * @param viewer <code>ITextViewer</code>
	 * @return JavaScript source code
	 */
	protected String getSource(ITextViewer viewer) {
		return viewer.getDocument().get();
	}

	private JavaScriptModel createModel(IFile iFile, String source) {
		return ModelManager.getInstance().getCachedModel(iFile, source);
	}

	private static class CompletionProposalHolder {
		List<ICompletionProposal> highList = new ArrayList<ICompletionProposal>(1000);
		List<ICompletionProposal> middleList = new ArrayList<ICompletionProposal>(500);
		List<ICompletionProposal> lowList = new ArrayList<ICompletionProposal>(500);
		Set<String> replaceStringSet = new HashSet<String>(500);

		void add(ICompletionProposal proposal, int priority) {
			switch (priority) {
			case 1:
				highList.add(proposal);
				break;
			case 2:
				middleList.add(proposal);
				break;
			case 3:
				lowList.add(proposal);
				break;
			default:
				break;
			}
		}

		void add(JavaScriptElement jsElement, Region region, int priority) {
			add(jsElement, region, priority, true);
		}

		void add(JavaScriptElement jsElement, Region region, int priority, boolean hasArgs) {
			String replaceStr = jsElement.getReplaceString();
			if (replaceStringSet.contains(replaceStr)) {
				return; // nothing
			}
			replaceStringSet.add(replaceStr);
			StringBuilder buf = new StringBuilder(50);
			buf.append(jsElement.getDisplayString());
			JavaScriptElement parentElement = jsElement.getParent();
			if (parentElement != null && parentElement.getName() != null && parentElement.getName().length() > 0) {
				buf.append(" : ");
				buf.append(parentElement.getName());
			}
			StringBuffer res = new StringBuffer();
//			res.append("<b>"+jsElement.getDisplayString()+"</b><br><hr><br>");
			res.append(jsElement.getDescription());
//			res.append("<br>");
//			if(jsElement.getFunction()!=null && jsElement.getFunction().getArguments().length>0) {
//				res.append("<br><b>参数</b><br>");
//				for(JavaScriptArgument a:jsElement.getFunction().getArguments()) {
//					res.append(a.toString()+"<br>");
//				}
//			}
//			if(jsElement.getFunction()!=null && jsElement.getFunction().getReturnTypes().length>0) {
//				res.append("<br><b>返回</b><br>");
//				for(String r:jsElement.getFunction().getReturnTypes()) {
//					res.append(r+"<br>");
//				}
//			}
//			if(jsElement.getFromSource()!=null) {
//				res.append("<br><b>起源</b><br>");
//				res.append("<a>"+jsElement.getFromSource()+"</a><br>");
//			}
			addInternal(buf.toString(), hasArgs ? replaceStr : jsElement.getName(), jsElement.getFromSource(),
					res.toString(), getImageFromType(jsElement), region, priority);
		}

		void add(String displayString, String replaceString, String fromSource, String description, Image image,
				Region region, int priority) {
			if (replaceStringSet.contains(replaceString)) {
				return; // nothing
			}
			addInternal(displayString, replaceString, fromSource, description, image, region, priority);
			replaceStringSet.add(replaceString);
		}

		void addInternal(String displayString, String replaceString, String fromSource, String description, Image image,
				Region region, int priority) {
			ICompletionProposal proposal = JavaScriptUtil.createTemplateCompletionProposal(displayString, replaceString,
					fromSource, description, image, region);
			add(proposal, priority);
		}

		ICompletionProposal[] getCompletionProposals() {
			// sort
			sortCompilationProposal(highList);
			sortCompilationProposal(middleList);
			sortCompilationProposal(lowList);
			highList.addAll(middleList);
			highList.addAll(lowList);

			return highList.toArray(new ICompletionProposal[highList.size()]);
		}

		void sortCompilationProposal(List<ICompletionProposal> prop) {
			Collections.sort(prop, new Comparator<ICompletionProposal>() {
				public int compare(ICompletionProposal o1, ICompletionProposal o2) {
					return o1.getDisplayString().compareToIgnoreCase(o2.getDisplayString());
				}
			});
		}

	}

	private boolean isTargetElement(JavaScriptElement jsElement, String lowerWord) {
		if (jsElement.isPrivate()) {
			return false;
		}
		if (jsElement instanceof JavaScriptFunction && ((JavaScriptFunction) jsElement).isAnonymous()) {
			return false;
		}
		if (lowerWord.length() == 0) {
			return true;
		}
		return jsElement.getName().toLowerCase().startsWith(lowerWord);
	}

	private Map<String, Map<String, Object>> getModuleNameMap(String path) {
		int pos = path.lastIndexOf('/');
		final String basePath;
		final String prefixPath;
		if (pos >= 0) {
			basePath = path.substring(0, pos) + "/";
			prefixPath = pos < path.length() ? path.substring(pos + 1) : "";
		} else {
			basePath = "";
			prefixPath = path;
		}
		Map<String, Map<String, Object>> moduleMap = new LinkedHashMap<String, Map<String, Object>>();
		if (basePath.startsWith(".")) {
			IFile parentFile = HTMLUtil.getActiveFile();
			IContainer parent = parentFile.getParent();
			if (parent instanceof IResource) {
				IWorkspaceRoot wsroot = ResourcesPlugin.getWorkspace().getRoot();
				IResource resource = wsroot.findMember(((IResource) parent).getFullPath().toString() + "/" + basePath);
				if (resource != null && resource instanceof IResource && resource.exists()) {
					try {
						for (IResource child : ((IContainer) resource).members()) {
							putRequireModuleInfo(moduleMap, child, prefixPath, basePath);
						}
					} catch (Exception e) {
						WebToolsPlugin.logException(e);
					}
				}
			}
		} else {
			String[] libPaths = ModelManager.getInstance().getLibPaths(HTMLUtil.getActiveFile());
			IWorkspaceRoot wsroot = ResourcesPlugin.getWorkspace().getRoot();
			for (String dir : libPaths) {
				if (dir.startsWith(JavaScriptLibPathTable.PREFIX)) {
					IResource resource = wsroot
							.findMember(dir.substring(JavaScriptLibPathTable.PREFIX.length()) + "/" + basePath);
					if (resource != null && resource instanceof IContainer && resource.exists()) {
						try {
							for (IResource child : ((IContainer) resource).members()) {
								putRequireModuleInfo(moduleMap, child, prefixPath, basePath);
							}
						} catch (Exception e) {
							WebToolsPlugin.logException(e);
						}
					}
				} else {
					File file = new File(dir + "/" + basePath);
					if (file.isDirectory()) {
						try {
							for (File child : file.listFiles(new FileFilter() {
								public boolean accept(File pathname) {
									if (pathname.isDirectory()) {
										return true;
									}
									String name = pathname.getName();
									return name.endsWith(".js") && name.startsWith(prefixPath);
								}
							})) {
								putRequireModuleInfo(moduleMap, child, prefixPath, basePath);
							}
						} catch (Exception e) {
							WebToolsPlugin.logException(e);
						}
					}
				}
			}
		}
		return moduleMap;
	}

	private void putRequireModuleInfo(Map<String, Map<String, Object>> moduleMap, Object resource, String prefixPath,
			String basePath) {
		if (resource instanceof IFile) {
			IFile file = (IFile) resource;
			String name = file.getName();
			if (name != null && name.endsWith(".js") && name.startsWith(prefixPath)) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("sourcePath", file.getFullPath().toString());
				map.put("icon", WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_FILE));
				moduleMap.put(basePath + name.substring(0, name.length() - 3), map);
			}
		} else if (resource instanceof IFolder) {
			IFolder folder = (IFolder) resource;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sourcePath", folder.getFullPath().toString());
			map.put("icon", WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_FOLDER));
			moduleMap.put(basePath + folder.getName(), map);
		} else if (resource instanceof File) {
			File file = (File) resource;
			if (file.isDirectory()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("sourcePath", file.getAbsolutePath());
				map.put("icon", WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_FILE));
				moduleMap.put(basePath + file.getName(), map);
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("sourcePath", file.getAbsolutePath());
				map.put("icon", WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_FOLDER));
				moduleMap.put(basePath + file.getName().substring(0, file.getName().length() - 3), map);
			}
		}

	}

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		CompletionProposalHolder cpHolder = new CompletionProposalHolder();

		String source = getSource(viewer);

		String[] words = getLastWord(source, offset);
		String target = words[0];
		String last = words[1];
		String word = words[2];
		String requirePath = words[3];
		String selectedWord = "";
//		if (word.length() == 0) {
//			Point range = viewer.getSelectedRange();
//			if (range.y != 0) {
//				selectedWord = source.substring(range.x, range.x + range.y);
//			}
//		}

		// a require path complement
		if (requirePath.startsWith("require(")) {
			Pattern pattern = Pattern.compile("require\\([\"']([^)]*)");
			Matcher matcher = pattern.matcher(requirePath);
			if (matcher.matches()) {
				String pathPrefix = matcher.group(1);
				Map<String, Map<String, Object>> moduleMap = getModuleNameMap(pathPrefix);
				List<ICompletionProposal> proposalList = new ArrayList<ICompletionProposal>();
				for (Map.Entry<String, Map<String, Object>> entry : moduleMap.entrySet()) {
					String path = entry.getKey();
					Map<String, Object> map = entry.getValue();
					ICompletionProposal proposal = JavaScriptUtil.createTemplateCompletionProposal(path, path,
							(String) map.get("sourcePath"), null, (Image) map.get("icon"),
							new Region(offset - pathPrefix.length(), pathPrefix.length() + selectedWord.length()));
					proposalList.add(proposal);
				}
				return proposalList.toArray(new ICompletionProposal[proposalList.size()]);
			}
		}

		IFile iFile = HTMLUtil.getActiveFile();

		// exports element complement
		if (last.startsWith("require.")) {
			Pattern pattern = Pattern.compile("require\\([\"']([^\"']+)[\"']\\)\\.(.*)");
			Matcher matcher = pattern.matcher(target);
			if (matcher.matches()) {
				JavaScriptModel model = createModel(iFile, source);
				String path = matcher.group(1);
				JavaScriptElement exportsElement = null;
				if (path.startsWith(".")) {
					IWorkspaceRoot wsroot = ResourcesPlugin.getWorkspace().getRoot();
					IResource childFile = wsroot
							.findMember(iFile.getParent().getFullPath().toString() + "/" + path + ".js");
					if (childFile != null && childFile.exists() && childFile instanceof IFile) {
						JavaScriptModel includedModel = model.getIncludedModel(childFile);
						if (includedModel == null) {
							includedModel = createModel(childFile);
						}
						if (includedModel != null) {
							exportsElement = includedModel.getElementByName("exports", false);
						}
					}
				} else {
					String[] libPaths = ModelManager.getInstance().getLibPaths(iFile);
					IWorkspaceRoot wsroot = ResourcesPlugin.getWorkspace().getRoot();
					for (String dir : libPaths) {
						if (dir.startsWith(JavaScriptLibPathTable.PREFIX)) {
							IResource resource = wsroot.findMember(
									dir.substring(JavaScriptLibPathTable.PREFIX.length()) + "/" + path + ".js");
							if (resource != null && resource instanceof IFile && resource.exists()) {
								JavaScriptModel includedModel = model.getIncludedModel(resource);
								if (includedModel == null) {
									includedModel = createModel(resource);
								}
								if (includedModel != null) {
									exportsElement = includedModel.getElementByName("exports", false);
									break;
								}
							}
						} else {
							File file = new File(dir, path + ".js");
							if (file.isFile()) {
								JavaScriptModel includedModel = model.getIncludedModel(file);
								if (includedModel == null) {
									IFile iFile2 = IOUtil.getIFile(file);
									if (iFile2 != null) {
										includedModel = createModel(iFile2);
									}
								}
								if (includedModel != null) {
									exportsElement = includedModel.getElementByName("exports", false);
									break;
								}
							}
						}
					}
				}
				if (exportsElement != null && exportsElement.getContext() != null) {
					String name = last.replaceFirst("require\\.", "");
					JavaScriptElement element = name.length() > 0
							? exportsElement.getContext().getElementByName(name, false)
							: exportsElement;
					String lowerWord = word.toLowerCase();
					if (element != null && element.getContext() != null) {
						for (JavaScriptElement child : element.getContext().getClassObjectElements()) {
							if (!child.isStatic() && isTargetElement(child, lowerWord)) {
								// 属性没有jsDoc但是有指向函数时，属性、方法的jsDoc取对应函数的jsDoc 2021-7-24 陈乾
								if (child.getDescription() == null && child.getFunction() != null) {
									child.setDescription(child.getFunction().getDescription());
								}
								cpHolder.add(child,
										new Region(offset - word.length(), word.length() + selectedWord.length()),
										1/* high */);
							}
						}
					}
				}
				return cpHolder.getCompletionProposals();
			}
		}

		List<String> addedStrings = new ArrayList<String>();

		String lowerWord = word.toLowerCase();
		Set<String> objTypeSet = new HashSet<String>();
		if (last.endsWith(".")) {
			String objName = last.substring(0, last.length() - 1);

			if (objName.length() > 0) {
				char c = objName.charAt(0);
				if (c == '\"' || c == '\'') {
					objTypeSet.add("String");
				} else if (c >= '0' && c <= '9') {
					objTypeSet.add("Number");
				} else if (c == '/') {
					objTypeSet.add("RegExp");
				}
			}

			String targetSource;
			int pos = offset - 1;
			if (pos >= 0 && pos < source.length()) {
				char[] carray = source.toCharArray();
				carray[pos] = ' ';
				targetSource = String.valueOf(carray);
			} else {
				targetSource = source;
			}
			JavaScriptModel model = createModel(iFile, targetSource);
			JavaScriptContext context = model.getContextFromOffset(offset);

			JavaScriptContext clsContext = model.getObjectTypeContext(objName);
			if (clsContext != null && clsContext.getTarget() != null) {
				// static
				for (JavaScriptElement jsElement : clsContext.getElements()) {
					if (jsElement.isStatic() && isTargetElement(jsElement, lowerWord)) {
						// 属性没有jsDoc但是有指向函数时，属性、方法的jsDoc取对应函数的jsDoc 2021-7-24 陈乾
						if (jsElement.getDescription() == null && jsElement.getFunction() != null) {
							jsElement.setDescription(jsElement.getFunction().getDescription());
						}
						cpHolder.add(jsElement,
								new Region(offset - word.length(), word.length() + selectedWord.length()), 1/* high */);
					}
				}
			} else {
				// instance properties and functions
				if (objTypeSet.isEmpty() && context != null) {
					JavaScriptElement jsElement = context.getElementByName(objName, false);
					if (jsElement != null) {
						for (String returnType : jsElement.getReturnTypes()) {
							if (!"*".equals(returnType)) {
								objTypeSet.add(returnType);
							}
						}
						// instance elements
						if (jsElement.getContext() != null) {
							JavaScriptElement[] jsElements = jsElement.getContext().getClassObjectElements();
							for (JavaScriptElement jsChildElement : jsElements) {
								if (isTargetElement(jsChildElement, lowerWord)) {
									// 属性没有jsDoc但是有指向函数时，属性、方法的jsDoc取对应函数的jsDoc 2021-7-24 陈乾
									if (jsChildElement.getDescription() == null
											&& jsChildElement.getFunction() != null) {
										jsChildElement.setDescription(jsChildElement.getFunction().getDescription());
									}
									cpHolder.add(jsChildElement,
											new Region(offset - word.length(), word.length() + selectedWord.length()),
											1/* high */, lowerWord.equals(jsChildElement.getName().toLowerCase()));
								}
							}
							if (objTypeSet.isEmpty() && jsElements.length != 0) {
								objTypeSet.add("*");
							}
						}
					}
				}

				// instance
				boolean typeFound = false;
				if (!objTypeSet.isEmpty()) {
					for (String objType : objTypeSet) {
						JavaScriptContext jsBlock = model.getObjectTypeContext(objType);
						if (jsBlock != null) {
							for (JavaScriptElement jsElement : jsBlock.getClassObjectElements()) {
								if (!jsElement.isStatic() && isTargetElement(jsElement, lowerWord)) {
									// 属性没有jsDoc但是有指向函数时，属性、方法的jsDoc取对应函数的jsDoc 2021-7-24 陈乾
									if (jsElement.getDescription() == null && jsElement.getFunction() != null) {
										jsElement.setDescription(jsElement.getFunction().getDescription());
									}
									cpHolder.add(jsElement,
											new Region(offset - word.length(), word.length() + selectedWord.length()),
											1/* high */);
								}
							}
							typeFound = true;
						}
					}
				}

				if (!typeFound) {
					// unknown type
					for (String type : DEFAULT_OBJECT_TYPES) {
						JavaScriptContext jsContext = model.getObjectTypeContext(type);
						if (jsContext != null) {
							for (JavaScriptElement jsElement : jsContext.getClassObjectElements()) {
								if (!jsElement.isStatic() && isTargetElement(jsElement, lowerWord)
										&& !(jsElement instanceof JavaScriptPrototype)) {
									// 属性没有jsDoc但是有指向函数时，属性、方法的jsDoc取对应函数的jsDoc 2021-7-24 陈乾
									if (jsElement.getDescription() == null && jsElement.getFunction() != null) {
										jsElement.setDescription(jsElement.getFunction().getDescription());
									}
									cpHolder.add(jsElement,
											new Region(offset - word.length(), word.length() + selectedWord.length()),
											2/* middle */);
								}
							}
						}
					}
				}
			}
		} else {
			JavaScriptModel model = createModel(iFile, source);
			JavaScriptContext context = model.getContextFromOffset(offset);

			if (context != null) {
				String subSource = source.substring(0, offset);

				boolean funcFlag = false;
				int count = 0;
				int paramPos = 0;
				StringBuilder funcBuf = new StringBuilder();
				boolean isCompleted = false;
				for (int i = subSource.length() - 1; i >= 0 && !isCompleted; i--) {
					char c = subSource.charAt(i);
					switch (c) {
					case ',':
						paramPos++;
						break;
					case '(':
						count--;
						if (count < 0) {
							funcFlag = true;
						}
						break;
					case ')':
						count++;
						break;
					default:
						if (funcFlag) {
							if ((c >= '0' && c <= '9') || //
									(c >= 'a' && c <= 'z') || //
									(c >= 'A' && c <= 'Z') || //
									(c == '_') || //
									(c == '$') //
							) {
								funcBuf.insert(0, c);
							} else if (funcBuf.length() != 0) {
								isCompleted = true;
							}
						}
						break;
					}
				}

				String targetFunc;
				if (funcBuf.length() == 0) {
					targetFunc = null;
				} else {
					targetFunc = funcBuf.toString();
				}

				if (targetFunc != null) {
					JavaScriptElement jsElement = context.getElementByName(targetFunc, false);
					if (jsElement != null && jsElement.getFunction() != null) {
						JavaScriptArgument[] jsArgs = jsElement.getFunction().getArguments();
						if (paramPos >= 0 && paramPos < jsArgs.length) {
							String[] types = jsArgs[paramPos].getTypes();
							for (String t : types) {
								objTypeSet.add(t);
							}
						}
					}
					objTypeSet.remove("*");
				}

				JavaScriptElement[] children = context.getVisibleElements();
				for (JavaScriptElement element : children) {
					if (element.getName().toLowerCase().startsWith(lowerWord)) {
						JavaScriptFunction func = element.getFunction();
						if (func != null && !func.isAnonymous() && element == func) {
							String replace = func.getReplaceString();
							if (!addedStrings.contains(func.getReplaceString())) {
								int p = func.hasReturnType(objTypeSet) ? 1/* high */
										: 2/* middle */;
								Region region = new Region(offset - word.length(),
										word.length() + selectedWord.length());
								if (func.isClass()) {
									if (subSource.endsWith("new ")) {
										cpHolder.add(func, region, 1/* high */);
									}
									if (func.hasStaticProperties()) {
										cpHolder.add(func.getName(), func.getName(), func.getFromSource(),
												func.getDescription(), WebToolsPlugin.getDefault().getImageRegistry()
														.get(WebToolsPlugin.ICON_FUNCTION),
												region, p);
									}
								} else {
									cpHolder.add(func, region, p, lowerWord.equals(func.getName().toLowerCase()));
								}
								addedStrings.add(replace);
							}

						} else if (element instanceof JavaScriptVariable) {
							String replace = element.getReplaceString();
							if (!addedStrings.contains(replace)) {
								// 对象没有jsDoc但是有指向函数时，jsDoc取对应函数的jsDoc 2021-7-24 陈乾
								if (element.getDescription() == null && element.getFunction() != null) {
									element.setDescription(element.getFunction().getDescription());
								}
								cpHolder.add(element,
										new Region(offset - word.length(), word.length() + selectedWord.length()),
										element.hasReturnType(objTypeSet) ? 1/* high */
												: 2/* middle */,
										lowerWord.equals(element.getName().toLowerCase()));
								addedStrings.add(replace);
							}
						}
					}
				}
			}

//			for (ICompletionProposal template : super.computeCompletionProposals(viewer, offset)) {
//				cpHolder.add(template, 2/* middle */);
//			}

			for (String keyword : JavaScriptScanner.KEYWORDS) {
				if (keyword.toLowerCase().startsWith(lowerWord)) {
					cpHolder.add(keyword, keyword, "JavaScript", null, null,
							new Region(offset - word.length(), word.length() + selectedWord.length()), 3);
				}
			}
		}

		return cpHolder.getCompletionProposals();
	}

	private JavaScriptModel createModel(IResource childFile) {
		InputStream in = null;
		try {
			in = ((IFile) childFile).getContents();
			String source2 = new String(IOUtil.readStream(in));
			return createModel((IFile) childFile, source2);
		} catch (Exception e) {
			WebToolsPlugin.logException(e);
		} finally {
			IOUtil.closeQuietly(in);
		}
		return null;
	}

	private static Image getImageFromType(JavaScriptElement jsElement) {
		JavaScriptFunction jsFunc = jsElement.getFunction();
		if (jsFunc != null) {
			if (jsFunc.isClass()) {
				return WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_CLASS);
			} else {
				return WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_FUNCTION);
			}
		}

		if (jsElement instanceof JavaScriptVariable || jsElement instanceof JavaScriptPrototype) {
			return WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_VARIABLE);
		}
		return null;
	}

	/**
	 * Cuts out the last word of caret position.
	 * 
	 * @param viewer  ITextViewer
	 * @param current the caret offset
	 * @return the last word of caret position
	 */
	public static String[] getLastWord(String source, int offset) {
		StringBuilder buf = new StringBuilder();
		int current = offset - 1;
		int b1 = 0; // ()
		int b2 = 0; // []
		while (current >= 0) {
			char c = source.charAt(current);
			if (c >= 'a' && c <= 'z') {
				if (b1 == 0 && b2 == 0) {
					buf.insert(0, c);
				}
			} else if (c >= 'A' && c <= 'Z') {
				if (b1 == 0 && b2 == 0) {
					buf.insert(0, c);
				}
			} else if (c >= '0' && c <= '9') {
				if (b1 == 0 && b2 == 0) {
					buf.insert(0, c);
				}
			} else if (c == '_' || c == '$' || c == '.') {
				if (b1 == 0 && b2 == 0) {
					buf.insert(0, c);
				}
			} else if (c == '(' && b1 > 0) {
				b1--;
			} else if (c == ')') {
				b1++;
			} else if (c == '[' && b2 > 0) {
				b2--;
			} else if (c == ']') {
				b2++;
			} else if (c == ' ' && buf.length() > 0 && b1 == 0 && b2 == 0) {
				break;
			} else if (b1 == 0 && b2 == 0) {
				break;
			}
			current--;
		}

		String target = source.substring(current + 1, offset);

		String word = buf.toString().trim();
		int pos = word.lastIndexOf('.');
		if (pos >= 0) {
			return new String[] { target, word.substring(0, pos + 1), word.substring(pos + 1), "" };
		} else {
			StringBuilder requirePathBuf = new StringBuilder();
			int i = offset - 1;
			while (i >= 0) {
				char c = source.charAt(i);
				if (c >= 'a' && c <= 'z') {
					requirePathBuf.insert(0, c);
				} else if (c >= 'A' && c <= 'Z') {
					requirePathBuf.insert(0, c);
				} else if (c >= '0' && c <= '9') {
					requirePathBuf.insert(0, c);
				} else if (c == '.' || c == '/' || c == '(' || c == '"' || c == '\'' || c == '_' || c == '$') {
					requirePathBuf.insert(0, c);
				} else {
					break;
				}
				i--;
			}
			return new String[] { target, "", word, requirePathBuf.toString() };
		}
	}

	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return new ContextInformation[0];
	}

	// 自动激活提示 关键字[]
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { '.' };
	}

	public char[] getContextInformationAutoActivationCharacters() {
		return new char[0];
	}

	public String getErrorMessage() {
		return "error";
	}

	public IContextInformationValidator getContextInformationValidator() {
		return new ContextInformationValidator(this);
	}

	@SuppressWarnings("deprecation")
	protected TemplateContextType getContextType(ITextViewer viewer, IRegion region) {
		HTMLTemplateManager manager = HTMLTemplateManager.getInstance();
		return manager.getContextTypeRegistry().getContextType(JavaScriptContextType.CONTEXT_TYPE);
	}

}
