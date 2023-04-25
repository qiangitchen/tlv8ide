package com.tulin.v8.editors.ini.editors;

import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.IShowInTargetList;

import com.tulin.v8.editors.ini.Activator;

public class InitializationFileEditor extends TextEditor {
	public InitializationFileEditor() {
		setDocumentProvider(new DocumentProvider());

		IPreferenceStore store = Activator.getDefault().getCombinedPreferenceStore();
		setPreferenceStore(store);

		JavaTextTools textTools = Activator.getDefault().getJavaTextTools();
		setSourceViewerConfiguration(
				new InitializationConfiguration(this, store, textTools.getColorManager(), "___pf_partitioning"));
	}

	protected String[] collectContextMenuPreferencePages() {
		String[] ids = super.collectContextMenuPreferencePages();
		String[] more = new String[ids.length + 1];
		more[0] = "org.eclipse.jdt.ui.preferences.PropertiesFileEditorPreferencePage";
		System.arraycopy(ids, 0, more, 1, ids.length);
		return more;
	}

	protected void handlePreferenceStoreChanged(PropertyChangeEvent event) {
		try {
			ISourceViewer sourceViewer = getSourceViewer();
			if (sourceViewer == null)
				return;
			((InitializationConfiguration) getSourceViewerConfiguration()).handlePropertyChangeEvent(event);
		} finally {
			super.handlePreferenceStoreChanged(event);
		}
		super.handlePreferenceStoreChanged(event);
	}

	protected boolean affectsTextPresentation(PropertyChangeEvent event) {
		return (((InitializationConfiguration) getSourceViewerConfiguration()).affectsTextPresentation(event))
				|| (super.affectsTextPresentation(event));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object getAdapter(Class adapter) {
		if (adapter == IShowInTargetList.class) {
			return new IShowInTargetList() {
				public String[] getShowInTargetIds() {
					return new String[] { "org.eclipse.jdt.ui.PackageExplorer",
							"org.eclipse.ui.views.ResourceNavigator" };
				}
			};
		}
		return super.getAdapter(adapter);
	}

	public int getOrientation() {
		return 33554432;
	}

	protected void updateStatusField(String category) {
		super.updateStatusField(category);
		if (getEditorSite() != null) {
			getEditorSite().getActionBars().getStatusLineManager().setMessage(null);
			getEditorSite().getActionBars().getStatusLineManager().setErrorMessage(null);
		}
	}
}