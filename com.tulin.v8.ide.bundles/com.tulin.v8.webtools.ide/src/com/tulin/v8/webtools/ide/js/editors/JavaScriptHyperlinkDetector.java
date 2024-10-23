package com.tulin.v8.webtools.ide.js.editors;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IGotoMarker;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.html.HTMLUtil;
import com.tulin.v8.webtools.ide.js.launch.JavaScriptLibPathTable;
import com.tulin.v8.webtools.ide.js.model.JavaScriptContext;
import com.tulin.v8.webtools.ide.js.model.JavaScriptElement;
import com.tulin.v8.webtools.ide.js.model.JavaScriptModel;
import com.tulin.v8.webtools.ide.js.model.JavaScriptVariable;
import com.tulin.v8.webtools.ide.js.model.ModelManager;
import com.tulin.v8.webtools.ide.js.model.JavaScriptModel.RequirePathData;
import com.tulin.v8.webtools.ide.utils.IOUtil;

/**
 * <code>IHyperlinkDetector</code> for JavaScript.
 * <p>
 * This detector detects available functions and variables at the caret
 * position.
 */
public class JavaScriptHyperlinkDetector implements IHyperlinkDetector {

	/**
	 * Returns hyperlinks or null.
	 */
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {

		String source = textViewer.getDocument().get();
		IFile iFile = HTMLUtil.getActiveFile();
		JavaScriptModel model = ModelManager.getInstance().getCachedModel(iFile, source);
		int offset = region.getOffset();
		if (offset >= source.length()) {
			offset = source.length() - 1;
		}

		// extracts the word at the caret position
		StringBuilder buf = new StringBuilder();
		StringBuilder buf2 = new StringBuilder();
		buf.append(source.charAt(offset));
		buf2.append(source.charAt(offset));
		int current = offset - 1;
		int b1 = 0; // ()
		int b2 = 0; // []
		while (current >= 0) {
			char c = source.charAt(current);
			if (c >= 'a' && c <= 'z') {
				if (b1 == 0 && b2 == 0) {
					buf.insert(0, c);
				}
				buf2.insert(0, c);
			} else if (c >= 'A' && c <= 'Z') {
				if (b1 == 0 && b2 == 0) {
					buf.insert(0, c);
				}
				buf2.insert(0, c);
			} else if (c >= '0' && c <= '9') {
				if (b1 == 0 && b2 == 0) {
					buf.insert(0, c);
				}
				buf2.insert(0, c);
			} else if (c == '_' || c == '$' || c == '.') {
				if (b1 == 0 && b2 == 0) {
					buf.insert(0, c);
				}
				buf2.insert(0, c);
			} else if (c == '(' && b1 > 0) {
				b1--;
				buf2.insert(0, c);
			} else if (c == ')') {
				b1++;
				buf2.insert(0, c);
			} else if (c == '[' && b2 > 0) {
				b2--;
				buf2.insert(0, c);
			} else if (c == ']') {
				b2++;
				buf2.insert(0, c);
			} else if (c == ' ' && buf.length() > 0 && b1 == 0 && b2 == 0) {
				break;
			} else if (b1 == 0 && b2 == 0) {
				break;
			} else {
				buf2.insert(0, c);
			}
			current--;
		}

		int pos2 = offset + 1;
		offset = offset - buf2.length() + 1;
		while (source.length() > pos2) {
			char c = source.charAt(pos2);
			if (Character.isJavaIdentifierPart(c)) {
				buf.append(c);
				buf2.append(c);
				pos2++;
			} else if (c == '(' || c == ')' || c == ';' || c == ':' || c == ' ' || c == '.' || c == ',' || c == '+') {
				break;
			} else {
				return detectHyperlinksForRequirePaths(model, region.getOffset());
			}
		}

		String word = buf.toString();
		String target = buf2.toString();

		if (word.startsWith("require.")) {
			Pattern pattern = Pattern.compile("require\\([\"']([^\"']+)[\"']\\)\\.(.*)");
			Matcher matcher = pattern.matcher(target);
			if (matcher.matches()) {
				String path = matcher.group(1);
				JavaScriptElement exportsElement = null;
				if (path.startsWith(".")) {
					IWorkspaceRoot wsroot = ResourcesPlugin.getWorkspace().getRoot();
					IResource childFile = wsroot
							.findMember(iFile.getParent().getFullPath().toString() + "/" + path + ".js");
					if (childFile != null && childFile.exists() && childFile instanceof IFile) {
						JavaScriptModel includedModel = model.getIncludedModel(childFile);
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
								if (includedModel != null) {
									exportsElement = includedModel.getElementByName("exports", false);
									break;
								}
							}
						} else {
							File file = new File(dir, path + ".js");
							if (file.isFile()) {
								JavaScriptModel includedModel = model.getIncludedModel(file);
								if (includedModel != null) {
									exportsElement = includedModel.getElementByName("exports", false);
									break;
								}
							}
						}
					}
				}
				if (exportsElement != null && exportsElement.getContext() != null) {
					String name = word.replaceFirst("require\\.", "");
					JavaScriptElement element = name.length() > 0
							? exportsElement.getContext().getElementByName(name, false)
							: exportsElement;
					IHyperlink[] links = createLinks(iFile, element, offset, target);
					if (links != null) {
						return links;
					}
				}
			}
		} else if (!word.startsWith(".")) {
			JavaScriptContext context = model.getContextFromOffset(offset);
			if (context != null) {
				JavaScriptElement element = context.getElementByName(word, false);
				IHyperlink[] links = createLinks(iFile, element, offset, target);
				if (links != null) {
					return links;
				}
			}
		}

		return detectHyperlinksForRequirePaths(model, offset);
	}

	private IHyperlink[] createLinks(IFile iFile, JavaScriptElement element, int offset, String target) {
		if (element != null && !element.isTemporary() && (element.getStart() >= 0 || element.getParent() != null)) {
			IRegion hyperlinkRegion = new Region(offset, target.length());
			int pos = -1;

			IFile file = null;
			// from parent's type
			if (element.getParent() != null && element.getParent().getContext() != null
					&& element.getParent().getContext().getModel() != null) {
				file = IOUtil.getIFile(element.getParent().getContext().getModel().getJavaScriptFile());
				pos = element.getStart();
			}
			if (file == null) {
				if (element.getContext() != null && element.getContext().getModel() != null
						&& element.getContext().getModel().getJavaScriptFile() != null) {
					file = IOUtil.getIFile(element.getContext().getModel().getJavaScriptFile());
				} else {
					file = iFile;
				}
				pos = element.getStart();
			}

			if (file != null && offset != pos) {
				if (element instanceof JavaScriptVariable) {
					return new IHyperlink[] { new JavaScriptHyperlink(hyperlinkRegion, file, element.getStart()) };
				} else if (element.getFunction() != null) {
					return new IHyperlink[] {
							new JavaScriptHyperlink(hyperlinkRegion, file, element.getFunction().getStart()) };
				}
			}
		}
		return null;
	}

	private IHyperlink[] detectHyperlinksForRequirePaths(JavaScriptModel model, int offset) {
		RequirePathData requirePathData = model.getRequirePathData(offset);
		if (requirePathData != null) {
			IRegion hyperlinkRegion = new Region(requirePathData.getStart(),
					requirePathData.getEnd() - requirePathData.getStart());
			IFile ifile = IOUtil.getIFile(requirePathData.getFile());
			if (ifile != null) {
				return new IHyperlink[] { new JavaScriptHyperlink(hyperlinkRegion, ifile, 0) };
			}
		}
		return null;
	}

	/**
	 * <code>IHyperlink</code> implementation to jump the target resource.
	 */
	private class JavaScriptHyperlink implements IHyperlink {

		private IRegion region;
		private IFile resource;
		private int beginOffset;

		/**
		 * The constructor.
		 * 
		 * @param region      the hyperlink region
		 * @param resource    the target resource (<code>IFile</code>,
		 *                    <code>ITextViewer</code> or <code>java.io.File</code>)
		 * @param beginOffset the begin offset
		 */
		public JavaScriptHyperlink(IRegion region, IFile resource, int beginOffset) {
			this.region = region;
			this.resource = resource;
			this.beginOffset = beginOffset;
		}

		public IRegion getHyperlinkRegion() {
			return region;
		}

		public String getTypeLabel() {
			return null;
		}

		public String getHyperlinkText() {
			return null;
		}

		public void open() {
			// IFile
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IFile file = (IFile) resource;
			if (!file.exists()) {
				Shell parentShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				String caption = WebToolsPlugin.getResourceString("ErrorDialog.Caption");
				String message = WebToolsPlugin.createMessage(WebToolsPlugin.getResourceString("Error.FileNotFound"),
						new String[] { file.getFullPath().toString() });
				String details = WebToolsPlugin.getResourceString("Error.FileNotFoundDetails");
				IStatus status = new Status(IStatus.ERROR, WebToolsPlugin.getPluginId(), 0,
						WebToolsPlugin.createMessage(details, new String[] { file.getFullPath().toString() }), null);
				ErrorDialog.openError(parentShell, caption, message, status);
			} else {
				try {
					IEditorPart editor = IDE.openEditor(page, file, true);
					IGotoMarker gotoMarker = (IGotoMarker) editor.getAdapter(IGotoMarker.class);
					if (gotoMarker != null) {
						IMarker marker = file.createMarker(IMarker.TEXT);
						marker.setAttribute(IMarker.CHAR_START, beginOffset);
						marker.setAttribute(IMarker.CHAR_END, beginOffset);
						gotoMarker.gotoMarker(marker);
						marker.delete();
					}
				} catch (Exception ex) {
					WebToolsPlugin.logException(ex);
				}
			}
		}
	}

}
