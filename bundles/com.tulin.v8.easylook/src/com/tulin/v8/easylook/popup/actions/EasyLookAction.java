package com.tulin.v8.easylook.popup.actions;

import java.io.File;

//import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

@SuppressWarnings("restriction")
public class EasyLookAction implements IObjectActionDelegate {
	private Object selected = null;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	public void run(IAction action) {
		if (this.selected == null) {
			return;
		}
		File directory = null;
		if ((this.selected instanceof IResource)) {
			directory = new File(((IResource) this.selected).getLocation().toOSString());
		}
		//if ((this.selected instanceof IFile)) {
		//	directory = directory.getParentFile();
		//}
		if ((this.selected instanceof File)) {
			directory = (File) this.selected;
		}
		if (directory == null)
			return;
		String osname = getOSName();
		try {
			if (osname.contains("win")) {
				Runtime.getRuntime().exec("explorer.exe /n," + directory.toString());
			} else if (osname.contains("mac")) {
				if (java.awt.Desktop.isDesktopSupported()) {
					java.awt.Desktop.getDesktop().open(directory);
				} else {
					Runtime.getRuntime().exec("nautilus " + directory.toString());
				}
			} else {
				openLinuxExplorer(directory.toString());
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	public void openLinuxExplorer(String path) throws Exception {
		// Unix or Linux的打开方式
		String[] explorers = { "dde-file-manager", "peony", "nautilus" };
		String explorer = null;
		for (int count = 0; count < explorers.length && explorer == null; count++)
			// 执行代码，在brower有值后跳出，
			// 这里是如果进程创建成功了，==0是表示正常结束。
			if (Runtime.getRuntime().exec(new String[] { "which", explorers[count] }).waitFor() == 0)
				explorer = explorers[count];
		if (explorer == null)
			throw new Exception("Could not find explorer");
		else
			// 这个值在上面已经成功的得到了一个进程。
			Runtime.getRuntime().exec(new String[] { explorer, path });
	}

	public String getOSName() {
		return System.getProperty("os.name").toLowerCase();
	}

	public void selectionChanged(IAction action, ISelection selection) {
		try {
			IAdaptable adaptable = null;
			this.selected = "unknown";
			if ((selection instanceof IStructuredSelection)) {
				adaptable = (IAdaptable) ((IStructuredSelection) selection).getFirstElement();
				if ((adaptable instanceof IResource))
					this.selected = ((IResource) adaptable);
				else if (((adaptable instanceof PackageFragment))
						&& ((((PackageFragment) adaptable).getPackageFragmentRoot() instanceof JarPackageFragmentRoot)))
					this.selected = getJarFile(((PackageFragment) adaptable).getPackageFragmentRoot());
				else if ((adaptable instanceof JarPackageFragmentRoot))
					this.selected = getJarFile(adaptable);
				else
					this.selected = ((IResource) adaptable.getAdapter(IResource.class));
			}
		} catch (Throwable localThrowable) {
		}
	}

	protected File getJarFile(IAdaptable adaptable) {
		JarPackageFragmentRoot jpfr = (JarPackageFragmentRoot) adaptable;
		File selected = jpfr.getPath().makeAbsolute().toFile();
		if (!selected.exists()) {
			File projectFile = new File(jpfr.getJavaProject().getProject().getLocation().toOSString());
			selected = new File(projectFile.getParent() + selected.toString());
		}
		return selected;
	}
}
