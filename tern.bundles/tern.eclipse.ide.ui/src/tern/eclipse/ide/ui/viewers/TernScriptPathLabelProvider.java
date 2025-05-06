package tern.eclipse.ide.ui.viewers;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import tern.eclipse.ide.ui.ImageResource;
import tern.scriptpath.ITernScriptPath;
import tern.scriptpath.ITernScriptResource;

/**
 * Label provider for tern script path {@link ITernScriptPath}.
 *
 */
public class TernScriptPathLabelProvider extends LabelProvider {

	private static final TernScriptPathLabelProvider INSTANCE = new TernScriptPathLabelProvider();

	public static TernScriptPathLabelProvider getInstance() {
		return INSTANCE;
	}

	private final WorkbenchLabelProvider provider = new WorkbenchLabelProvider();

	@Override
	public String getText(Object element) {
		if (element instanceof ITernScriptResource) {
			return ((ITernScriptResource) element).getLabel();
		}
		if (element instanceof ITernScriptPath) {
			return ((ITernScriptPath) element).getLabel();
		}

		return super.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof ITernScriptPath) {
			IResource res = (IResource) ((ITernScriptPath) element)
					.getAdapter(IResource.class);
			return provider.getImage(res);
		}
		if (element instanceof ITernScriptResource) {
			return ImageResource.getImage(ImageResource.IMG_SCRIPT);
		}
		return super.getImage(element);
	}

}
