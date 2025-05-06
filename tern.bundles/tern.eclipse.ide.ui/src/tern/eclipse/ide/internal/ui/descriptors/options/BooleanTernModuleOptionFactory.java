package tern.eclipse.ide.internal.ui.descriptors.options;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import tern.eclipse.ide.ui.descriptors.options.ITernModuleOptionFactory;
import tern.metadata.TernModuleMetadataOption;
import tern.server.protocol.JsonHelper;

import com.eclipsesource.json.JsonObject;

/**
 * Boolean tern module option.
 *
 */
public class BooleanTernModuleOptionFactory implements ITernModuleOptionFactory {

	@Override
	public void createOption(Composite parent, IProject project,
			TernModuleMetadataOption metadata, final JsonObject options) {
		final String name = metadata.getName();
		// create UI
		final Button checkbox = new Button(parent, SWT.CHECK);
		checkbox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// init UI value
		boolean value = JsonHelper.getBoolean(options, name, false);
		checkbox.setSelection(value);
		// Synchronize UI & JSON
		checkbox.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				options.set(name, checkbox.getSelection());
			}
		});
	}

}
