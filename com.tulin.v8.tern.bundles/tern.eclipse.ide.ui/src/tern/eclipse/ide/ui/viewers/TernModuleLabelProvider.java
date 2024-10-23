package tern.eclipse.ide.ui.viewers;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import tern.eclipse.ide.ui.ImageResource;
import tern.eclipse.ide.ui.TernUIPlugin;
import tern.server.ITernModule;
import tern.server.ITernModuleConfigurable;
import tern.server.ITernPlugin;
import tern.utils.TernModuleHelper;

/**
 * Label provider for {@link ITernPlugin} and {@link ITernModule}.
 * 
 */
public class TernModuleLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	private static final ITableLabelProvider INSTANCE = new TernModuleLabelProvider();

	public static ITableLabelProvider getInstance() {
		return INSTANCE;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof ITernModule) {
			ITernModule module = (ITernModule) element;
			switch (columnIndex) {
			case 0:
				return TernModuleHelper.getLabel(module);
			case 1:
				String version = module.getVersion();
				return version != null ? version : "";
			}
		}
		return element.toString();
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if (element instanceof ITernModule) {
				return getImageModule((ITernModule) element);
			}
		}
		return null;
	}

	/**
	 * Returns the image of the given module.
	 * 
	 * @param module
	 * @return the image of the given module.
	 */
	public static Image getImageModule(ITernModule module) {
		Image image = TernUIPlugin.getTernDescriptorManager().getImage(
				module);
		if (image != null) {
			return image;
		}
		return getDefaultImage(module);
	}

	private static Image getDefaultImage(ITernModule module) {
		switch (module.getModuleType()) {
		case Def:
			return ImageResource.getImage(ImageResource.IMG_TYPE_DEF);
		case Plugin:
			return ImageResource.getImage(ImageResource.IMG_PLUGIN);
		case Configurable:
			return getDefaultImage(((ITernModuleConfigurable) module)
					.getWrappedModule());
		}
		return null;
	}

}
