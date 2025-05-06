package tern.eclipse.ide.linter.ui.properties;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import tern.TernException;
import tern.eclipse.ide.core.IWorkingCopy;
import tern.eclipse.ide.linter.core.ITernLinterConfig;
import tern.eclipse.ide.linter.core.TernLinterCorePlugin;
import tern.eclipse.ide.linter.internal.ui.TernLinterUIPlugin;
import tern.eclipse.ide.linter.internal.ui.Trace;
import tern.eclipse.ide.ui.TernUIPlugin;
import tern.eclipse.ide.ui.properties.AbstractTernPropertyPage;
import tern.server.ITernModule;

/**
 * Abstract class for Linter property page.
 *
 */
public abstract class TernLinterPropertyPage extends AbstractTernPropertyPage implements IWorkbenchPreferencePage {

	private final String linterId;
	private TernLinterOptionsBlock linterConfigBlock;

	public TernLinterPropertyPage(String linterId) {
		super();
		this.linterId = linterId;
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(TernLinterUIPlugin.getDefault().getPreferenceStore());
	}

	@Override
	protected Control createContents(Composite ancestor) {
		Composite parent = new Composite(ancestor, SWT.NONE);
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));

		initializeDialogUnits(parent);
		noDefaultAndApplyButton();

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		parent.setLayout(layout);

		IWorkingCopy workingCopy = getWorkingCopy();
		// create UI linter config
		linterConfigBlock = new TernLinterOptionsBlock(linterId, workingCopy, this);
		Control control = linterConfigBlock.createControl(parent);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 1;
		control.setLayoutData(data);

		// load linter config
		loadLinterConfig();

		// Display icon of linter
		try {
			ITernModule module = workingCopy.getTernModule(linterId);
			ImageDescriptor desc = TernUIPlugin.getTernDescriptorManager().getImageDescriptor(
					module);
			if (desc != null) {
				super.setImageDescriptor(desc);
			}
		} catch (TernException e) {
			
		}
		applyDialogFont(parent);
		return parent;
	}

	@Override
	protected void doPerformOk() throws Exception {
		linterConfigBlock.saveColumnSettings();
		// create options and store it .tern-project or config file
		// name.
		linterConfigBlock.updateTenProject();
	}

	/**
	 * Load linter config.
	 */
	private void loadLinterConfig() {
		try {
			// Create instance of linter config
			ITernLinterConfig config = TernLinterCorePlugin.getDefault().getTernLinterConfigurationsManager()
					.createLinterConfig(linterId);
			// refresh the tree options
			linterConfigBlock.setLinterConfig(config);
		} catch (Throwable e) {
			Trace.trace(Trace.SEVERE, "Error while loading linter config.", e);
		}
	}

}
