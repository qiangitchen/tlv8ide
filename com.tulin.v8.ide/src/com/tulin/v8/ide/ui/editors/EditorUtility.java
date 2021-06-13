package com.tulin.v8.ide.ui.editors;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.compare.JavaCompareUtilities;
import org.eclipse.jdt.internal.ui.javaeditor.IClassFileEditorInput;
import org.eclipse.jdt.internal.ui.javaeditor.InternalClassFileEditorInput;
import org.eclipse.jdt.internal.ui.javaeditor.JarEntryEditorInput;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.ui.editors.process.ProcessEditorInput;
import com.tulin.v8.ide.ui.internal.FlowDraw;

import zigen.plugin.db.ui.internal.TreeNode;

@SuppressWarnings({ "rawtypes", "unchecked", "restriction" })
public class EditorUtility {
	public static IEditorPart isOpenInEditor(Object inputElement) {
		IEditorPart editor = findEditor(inputElement, false);
		if (editor != null) {
			return editor;
		}
		IEditorInput input = getEditorInput(inputElement);
		if (input != null) {
			IWorkbenchPage p = StudioPlugin.getActivePage();
			if (p != null) {
				IEditorPart part = p.findEditor(input);
				if (part == null) {
					if (inputElement instanceof FlowDraw) {
						FlowDraw treeobj = (FlowDraw) inputElement;
						ProcessEditorInput localFileEditorInput2 = new ProcessEditorInput(
								treeobj.getElement());
						part = p.findEditor(localFileEditorInput2);
					} else if (inputElement instanceof TreeNode) {
						IFileStore fileStore = EFS.getLocalFileSystem()
								.getStore(
										new Path(((TreeNode) inputElement)
												.getPath()));
						FileStoreEditorInput filestoreeditorinput = new FileStoreEditorInput(
								fileStore);
						part = p.findEditor(filestoreeditorinput);
					} else if (inputElement instanceof File) {
						IFileStore fileStore = EFS.getLocalFileSystem()
								.getStore(
										new Path(((File) inputElement)
												.getAbsolutePath()));
						FileStoreEditorInput filestoreeditorinput = new FileStoreEditorInput(
								fileStore);
						part = p.findEditor(filestoreeditorinput);
					}
				}
				return part;
			}
		}
		return null;
	}

	public static IEditorPart openInEditor(Object inputElement)
			throws PartInitException {
		return openInEditor(inputElement, true);
	}

	public static IEditorPart openInEditor(Object inputElement, boolean activate)
			throws PartInitException {
		if ((inputElement instanceof IFile)) {
			return openInEditor((IFile) inputElement, activate);
		}
		IEditorPart editor = findEditor(inputElement, activate);
		if (editor != null) {
			return editor;
		}
		IEditorInput input = getEditorInput(inputElement);
		if (input == null) {
			throwPartInitException("input为空!", 10008);
		}
		return openInEditor(input, getEditorID(input), activate);
	}

	private static IEditorPart findEditor(Object inputElement, boolean activate) {
		if (inputElement instanceof TreeNode) {
			IWorkbenchPage page = StudioPlugin.getActivePage();
			IEditorPart editor = page != null ? (editor = page
					.getActiveEditor()) : null;
			if (editor != null) {
				IEditorInput editorInput;
				boolean isCompareEditorInput = isCompareEditorInput(editor
						.getEditorInput());
				if (isCompareEditorInput) {
					editorInput = (IEditorInput) editor
							.getAdapter(IEditorInput.class);
				} else {
					editorInput = editor.getEditorInput();
				}
				if (inputElement instanceof FlowDraw) {
					ProcessEditorInput localFileEditorInput2 = new ProcessEditorInput(
							((FlowDraw) inputElement).getElement());
					if (localFileEditorInput2.equals(editorInput)) {
						if ((activate) && (page.getActivePart() != editor))
							page.activate(editor);
						return editor;
					}
				}
			}
			String path = ((TreeNode) inputElement).getPath();
			if (path == null)
				return null;
			File file = new File(path);
			if (editor != null) {
				boolean isCompareEditorInput = isCompareEditorInput(editor
						.getEditorInput());
				IEditorInput editorInput;
				if (isCompareEditorInput) {
					editorInput = (IEditorInput) editor
							.getAdapter(IEditorInput.class);
				} else {
					editorInput = editor.getEditorInput();
				}
				IFile iFile = StudioPlugin.getWorkspace().getRoot()
						.getFileForLocation(new Path(file.getAbsolutePath()));
				if (iFile != null && iFile.exists()) {
					FileEditorInput localFileEditorInput2 = new FileEditorInput(
							iFile);
					if (localFileEditorInput2.getFile().getFullPath()
							.equals(editorInput.getToolTipText())) {
						if ((activate) && (page.getActivePart() != editor))
							page.activate(editor);
						return editor;
					}
				} else {
					if (file.getAbsolutePath().equals(
							editorInput.getToolTipText())) {
						if ((activate) && (page.getActivePart() != editor))
							page.activate(editor);
						return editor;
					}
				}
			}
		} else if ((inputElement instanceof IJavaElement)) {
			ICompilationUnit cu = (ICompilationUnit) ((IJavaElement) inputElement)
					.getAncestor(5);
			if (cu != null) {
				IWorkbenchPage page = StudioPlugin.getActivePage();
				IEditorPart editor = page != null ? (editor = page
						.getActiveEditor()) : null;
				if (editor != null) {
					boolean isCompareEditorInput = isCompareEditorInput(editor
							.getEditorInput());
					if ((isCompareEditorInput)
							|| (!JavaModelUtil.isPrimary(cu))) {
						IEditorInput editorInput;
						if (isCompareEditorInput)
							editorInput = (IEditorInput) editor
									.getAdapter(IEditorInput.class);
						else
							editorInput = editor.getEditorInput();
						IJavaElement editorCU = getEditorInputJavaElement(
								editorInput, false);
						if (cu.equals(editorCU)) {
							if ((activate) && (page.getActivePart() != editor))
								page.activate(editor);
							return editor;
						}
					}
				}
			}
		}
		return null;
	}

	private static boolean isComparePlugInActivated() {
		return Platform.getBundle("org.eclipse.compare").getState() == 32;
	}

	private static boolean isCompareEditorInput(IEditorInput input) {
		return (isComparePlugInActivated())
				&& (JavaCompareUtilities.isCompareEditorInput(input));
	}

	public static void revealInEditor(IEditorPart part, IJavaElement element) {
		if (element == null) {
			return;
		}
		if ((part instanceof JavaEditor)) {
			((JavaEditor) part).setSelection(element);
			return;
		}
	}

	public static void revealInEditor(IEditorPart part, IRegion region) {

	}

	private static IEditorPart openInEditor(IFile file, boolean activate)
			throws PartInitException {
		if (file == null) {
			throwPartInitException("IFile为空!");
		}
		IWorkbenchPage p = StudioPlugin.getActivePage();
		if (p == null) {
			throwPartInitException("page为空!");
		}
		IEditorPart editorPart = IDE.openEditor(p, file, activate);
		initializeHighlightRange(editorPart);
		return editorPart;
	}

	private static IEditorPart openInEditor(IEditorInput input,
			String editorID, boolean activate) throws PartInitException {
		Assert.isNotNull(input);
		Assert.isNotNull(editorID);

		IWorkbenchPage p = StudioPlugin.getActivePage();
		if (p == null) {
			throwPartInitException("page为空!");
		}
		IEditorPart editorPart = p.openEditor(input, editorID, activate);
		initializeHighlightRange(editorPart);
		return editorPart;
	}

	private static void throwPartInitException(String message, int code)
			throws PartInitException {
		IStatus status = new Status(4, "org.eclipse.jdt.ui", code, message,
				null);
		throw new PartInitException(status);
	}

	private static void throwPartInitException(String message)
			throws PartInitException {
		throwPartInitException(message, 0);
	}

	private static void initializeHighlightRange(IEditorPart editorPart) {
		if ((editorPart instanceof ITextEditor)) {
			IAction toggleAction = editorPart
					.getEditorSite()
					.getActionBars()
					.getGlobalActionHandler(
							"org.eclipse.ui.edit.text.toggleShowSelectedElementOnly");
			boolean enable = toggleAction != null;
			if ((enable) && ((editorPart instanceof JavaEditor)))
				enable = StudioPlugin.getDefault().getPreferenceStore()
						.getBoolean("org.eclipse.jdt.ui.editor.showSegments");
			else
				enable = (enable) && (toggleAction.isEnabled())
						&& (toggleAction.isChecked());
			if (enable)
				if ((toggleAction instanceof TextEditorAction)) {
					((TextEditorAction) toggleAction).setEditor(null);

					((TextEditorAction) toggleAction)
							.setEditor((ITextEditor) editorPart);
				} else {
					toggleAction.run();

					toggleAction.run();
				}
		}
	}

	@SuppressWarnings("deprecation")
	public static String getEditorID(IEditorInput input)
			throws PartInitException {
		Assert.isNotNull(input);
		IEditorDescriptor editorDescriptor;
		if ((input instanceof IFileEditorInput)) {
			editorDescriptor = IDE
					.getEditorDescriptor(((IFileEditorInput) input).getFile());
		}else {
			editorDescriptor = IDE.getEditorDescriptor(input.getName());
		}
		return editorDescriptor.getId();
	}

	public static ITypeRoot getEditorInputJavaElement(IEditorPart editor,
			boolean primaryOnly) {
		Assert.isNotNull(editor);
		return getEditorInputJavaElement(editor.getEditorInput(), primaryOnly);
	}

	private static ITypeRoot getEditorInputJavaElement(
			IEditorInput editorInput, boolean primaryOnly) {
		if (editorInput == null) {
			return null;
		}
		ITypeRoot je = JavaUI.getEditorInputTypeRoot(editorInput);
		if ((je != null) || (primaryOnly)) {
			return je;
		}
		return JavaPlugin.getDefault().getWorkingCopyManager()
				.getWorkingCopy(editorInput, false);
	}

	private static IEditorInput getEditorInput(IJavaElement element) {
		while (element != null) {
			if ((element instanceof ICompilationUnit)) {
				ICompilationUnit unit = ((ICompilationUnit) element)
						.getPrimary();
				IResource resource = unit.getResource();
				if ((resource instanceof IFile)) {
					return new FileEditorInput((IFile) resource);
				}
			}
			if ((element instanceof IClassFile)) {
				return new InternalClassFileEditorInput((IClassFile) element);
			}
			element = element.getParent();
		}

		return null;
	}

	public static IEditorInput getEditorInput(Object input) {
		if (input instanceof FlowDraw) {
			FlowDraw treeobj = (FlowDraw) input;
			ProcessEditorInput localFileEditorInput2 = new ProcessEditorInput(
					treeobj.getElement());
			return localFileEditorInput2;
		}
		if ((input instanceof TreeNode)) {
			String path = ((TreeNode) input).getPath();
			if (path == null)
				return null;
			IFile iFile = StudioPlugin.getWorkspace().getRoot()
					.getFileForLocation(new Path(path));
			if (iFile != null) {
				return new FileEditorInput(iFile);
			}
		}
		if ((input instanceof File)) {
			String path = ((File) input).getAbsolutePath();
			if (path == null)
				return null;
			IFile iFile = StudioPlugin.getWorkspace().getRoot()
					.getFileForLocation(new Path(path));
			return new FileEditorInput(iFile);
		}
		if ((input instanceof IJavaElement)) {
			return getEditorInput((IJavaElement) input);
		}
		if ((input instanceof IFile)) {
			return new FileEditorInput((IFile) input);
		}
		if (JavaModelUtil.isOpenableStorage(input)) {
			return new JarEntryEditorInput((IStorage) input);
		}
		return null;
	}

	public static IJavaElement getActiveEditorJavaInput() {
		IWorkbenchPage page = JavaPlugin.getActivePage();
		if (page != null) {
			IEditorPart part = page.getActiveEditor();
			if (part != null) {
				IEditorInput editorInput = part.getEditorInput();
				if (editorInput != null) {
					return JavaUI.getEditorInputJavaElement(editorInput);
				}
			}
		}
		return null;
	}

	public static int findLocalizedModifier(String modifierName) {
		if (modifierName == null) {
			return 0;
		}
		if (modifierName.equalsIgnoreCase(Action.findModifierString(262144)))
			return 262144;
		if (modifierName.equalsIgnoreCase(Action.findModifierString(131072)))
			return 131072;
		if (modifierName.equalsIgnoreCase(Action.findModifierString(65536)))
			return 65536;
		if (modifierName.equalsIgnoreCase(Action.findModifierString(4194304))) {
			return 4194304;
		}
		return 0;
	}

	public static String getModifierString(int stateMask) {
		String modifierString = "";
		if ((stateMask & 0x40000) == 262144)
			modifierString = appendModifierString(modifierString, 262144);
		if ((stateMask & 0x10000) == 65536)
			modifierString = appendModifierString(modifierString, 65536);
		if ((stateMask & 0x20000) == 131072)
			modifierString = appendModifierString(modifierString, 131072);
		if ((stateMask & 0x400000) == 4194304) {
			modifierString = appendModifierString(modifierString, 4194304);
		}
		return modifierString;
	}

	private static String appendModifierString(String modifierString,
			int modifier) {
		if (modifierString == null)
			modifierString = "";
		String newModifierString = Action.findModifierString(modifier);
		if (modifierString.length() == 0)
			return newModifierString;
		return Messages.format("", new String[] { modifierString,
				newModifierString });
	}

	public static IJavaProject getJavaProject(IEditorInput input) {
		IJavaProject jProject = null;
		if ((input instanceof IFileEditorInput)) {
			IProject project = ((IFileEditorInput) input).getFile()
					.getProject();
			if (project != null) {
				jProject = JavaCore.create(project);
				if (!jProject.exists())
					jProject = null;
			}
		} else if ((input instanceof IClassFileEditorInput)) {
			jProject = ((IClassFileEditorInput) input).getClassFile()
					.getJavaProject();
		}
		return jProject;
	}

	public static IEditorPart[] getDirtyEditors(boolean skipNonResourceEditors) {
		Set inputs = new HashSet();
		List result = new ArrayList(0);
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
		for (int i = 0; i < windows.length; i++) {
			IWorkbenchPage[] pages = windows[i].getPages();
			for (int x = 0; x < pages.length; x++) {
				IEditorPart[] editors = pages[x].getDirtyEditors();
				for (int z = 0; z < editors.length; z++) {
					IEditorPart ep = editors[z];
					IEditorInput input = ep.getEditorInput();
					if ((!inputs.add(input))
							|| ((skipNonResourceEditors) && (!isResourceEditorInput(input))))
						continue;
					result.add(ep);
				}
			}

		}

		return (IEditorPart[]) result.toArray(new IEditorPart[result.size()]);
	}

	private static boolean isResourceEditorInput(IEditorInput input) {
		if ((input instanceof MultiEditorInput)) {
			IEditorInput[] inputs = ((MultiEditorInput) input).getInput();
			for (int i = 0; i < inputs.length; i++) {
				if (inputs[i].getAdapter(IResource.class) != null)
					return true;
			}
		} else if (input.getAdapter(IResource.class) != null) {
			return true;
		}
		return false;
	}

	public static IEditorPart[] getDirtyEditorsToSave(boolean saveUnknownEditors) {
		Set inputs = new HashSet();
		List result = new ArrayList(0);
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
		for (int i = 0; i < windows.length; i++) {
			IWorkbenchPage[] pages = windows[i].getPages();
			for (int x = 0; x < pages.length; x++) {
				IEditorPart[] editors = pages[x].getDirtyEditors();
				for (int z = 0; z < editors.length; z++) {
					IEditorPart ep = editors[z];
					IEditorInput input = ep.getEditorInput();
					if (!mustSaveDirtyEditor(ep, input, saveUnknownEditors)) {
						continue;
					}
					if (inputs.add(input))
						result.add(ep);
				}
			}
		}
		return (IEditorPart[]) result.toArray(new IEditorPart[result.size()]);
	}

	private static boolean mustSaveDirtyEditor(IEditorPart ep,
			IEditorInput input, boolean saveUnknownEditors) {
		IResource resource = (IResource) input.getAdapter(IResource.class);
		if (resource == null) {
			return saveUnknownEditors;
		}
		IJavaElement javaElement = JavaCore.create(resource);
		if ((javaElement instanceof ICompilationUnit)) {
			ICompilationUnit cu = (ICompilationUnit) javaElement;
			if (!cu.isWorkingCopy()) {
				return true;
			}
		}

		if (!(ep instanceof ITextEditor)) {
			return saveUnknownEditors;
		}
		ITextEditor textEditor = (ITextEditor) ep;
		IDocumentProvider documentProvider = textEditor.getDocumentProvider();
		if (!(documentProvider instanceof TextFileDocumentProvider)) {
			return saveUnknownEditors;
		}
		return false;
	}

}