package tern.eclipse.ide.ui.viewers;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import tern.repository.ITernRepository;
import tern.server.ITernModule;

/**
 * Label provider for {@link ITernRepository}.
 * 
 */
public class TernRepositoryLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	private static final ITableLabelProvider INSTANCE = new TernRepositoryLabelProvider();

	public static ITableLabelProvider getInstance() {
		return INSTANCE;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof ITernRepository) {
			ITernRepository repository = (ITernRepository) element;
			switch (columnIndex) {
			case 0:
				return repository.getName();
			case 1:
				return repository.getTernBaseDirAsString();
			}

		} else if (element instanceof ITernModule) {
			ITernModule module = (ITernModule) element;
			switch (columnIndex) {
			case 0:
				return module.getName();
			}
		}
		return null;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (element instanceof ITernModule) {
			ITernModule module = (ITernModule) element;
			switch (columnIndex) {
			case 0:
				return TernModuleLabelProvider.getImageModule(module);
			}
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		return getColumnText(element, 0);
	}
}
