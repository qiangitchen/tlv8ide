package tern.eclipse.ide.internal.ui.preferences;

import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import tern.eclipse.ide.core.ITernServerType;
import tern.eclipse.ide.core.TernCorePlugin;
import tern.eclipse.ide.core.preferences.TernCorePreferenceConstants;
import tern.eclipse.ide.internal.ui.TernUIMessages;
import tern.eclipse.ide.ui.ImageResource;

/**
 * Tern Server preferences page.
 * 
 */
public class TernServerPreferencesPage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	public TernServerPreferencesPage() {
		super(GRID);
		setDescription(TernUIMessages.TernServerPreferencesPage_desc);
		setImageDescriptor(ImageResource
				.getImageDescriptor(ImageResource.IMG_LOGO));
	}

	@Override
	protected void createFieldEditors() {

		// Tern Server type combo
		ITernServerType[] serverTypes = TernCorePlugin
				.getTernServerTypeManager().getTernServerTypes();
		String[][] types = new String[serverTypes.length + 1][2];
		types[0][0] = " -- Choose your server type --"; //$NON-NLS-1$
		types[0][1] = ""; //$NON-NLS-1$

		for (int i = 0; i < serverTypes.length; i++) {
			types[i + 1][0] = serverTypes[i].getName();
			types[i + 1][1] = serverTypes[i].getId();
		}

		ComboFieldEditor ternServerEditor = new ComboFieldEditor(
				TernCorePreferenceConstants.TERN_SERVER_TYPE,
				TernUIMessages.TernServerPreferencesPage_serverType, types,
				getFieldEditorParent());
		addField(ternServerEditor);
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@SuppressWarnings("deprecation")
	@Override
	protected IPreferenceStore doGetPreferenceStore() {
		// IProject project = getProject();
		// ScopedPreferenceStore store;
		// if (project == null) {
		// // workspace settings
		// IScopeContext scope = new InstanceScope();
		// return new ScopedPreferenceStore(scope, TernCorePlugin.PLUGIN_ID);
		// } else {
		// // project settings
		// IScopeContext projectScope = new ProjectScope(project);
		// preferences = projectScope.getNode(TernCorePlugin.PLUGIN_ID);
		// store = new ScopedPreferenceStore(projectScope,
		// TernCorePlugin.PLUGIN_ID);
		// }
		// return store;
		IScopeContext scope = new InstanceScope();
		return new ScopedPreferenceStore(scope, TernCorePlugin.PLUGIN_ID);

	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		TernCorePlugin.getTernServerTypeManager().refresh();
	}

}
