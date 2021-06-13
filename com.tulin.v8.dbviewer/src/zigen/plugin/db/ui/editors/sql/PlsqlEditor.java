/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.sql;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.texteditor.MarkerUtilities;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ext.oracle.internal.OracleSourceErrorInfo;
import zigen.plugin.db.ui.views.internal.ColorManager;
import zigen.plugin.db.ui.views.internal.PLSQLCodeConfiguration;
import zigen.plugin.db.ui.views.internal.PLSQLSourceViewer;
import zigen.plugin.db.ui.views.internal.SQLToolBarForPlsqlEditor;

public class PlsqlEditor extends SqlEditor2 implements IPlsqlEditor, IPropertyChangeListener, IDocumentListener {

	public void documentAboutToBeChanged(DocumentEvent event) {
	}

	public void documentChanged(DocumentEvent event) {
		setDirty(true);
	}


	IResource resource = null;

	public PlsqlEditor() {
		super();
		colorManager = new ColorManager();
		sqlConfiguration = new PLSQLCodeConfiguration(colorManager);
		setSourceViewerConfiguration(sqlConfiguration);
		this.store = DbPlugin.getDefault().getPreferenceStore();
		this.store.addPropertyChangeListener(this);
	}

	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		super.init(site, editorInput);
		resource = (IResource) getEditorInput().getAdapter(IResource.class);
	}

	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		FormLayout layout = new FormLayout();
		parent.setLayout(layout);
		toolBar = new SQLToolBarForPlsqlEditor(this);
		toolBar.createPartControl(parent);

		Composite sqlComposite = new Composite(parent, SWT.NONE);
		sqlComposite.setLayout(new FillLayout());
		FormData data = new FormData();
		data.top = new FormAttachment(toolBar.getCoolBar(), 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		sqlComposite.setLayoutData(data);

		fAnnotationAccess = getAnnotationAccess();
		fOverviewRuler = createOverviewRuler(getSharedColors());
		// ISourceViewer viewer = new SQLSourceViewer(parent, ruler,
		// getOverviewRuler(), isOverviewRulerVisible(), styles);
		PLSQLSourceViewer viewer = new PLSQLSourceViewer(sqlComposite, ruler, getOverviewRuler(), true, styles);
		getSourceViewerDecorationSupport(viewer);
		viewer.setPlsqlEditor(this);

		return viewer;

	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				getContributor().fillContextMenu(manager);
			}
		});
		StyledText text = sqlViewer.getTextWidget();
		Menu menu = menuMgr.createContextMenu(text);
		text.setMenu(menu);
		getSite().registerContextMenu(menuMgr, sqlViewer);
	}

	private PlsqlEditorContributor getContributor() {
		IEditorActionBarContributor contributor = getEditorSite().getActionBarContributor();
		if (contributor instanceof PlsqlEditorContributor) {
			return (PlsqlEditorContributor) contributor;
		} else {
			return null;
		}
	}

	// public void selectionChanged(IWorkbenchPart part, ISelection selection) {
	// IEditorInput input = getEditorInput();
	// if (input instanceof FileEditorInput) {
	// FileEditorInput fi = (FileEditorInput) input;
	// file = fi.getFile();
	// this.config = ResourceUtil.getDBConfig(file);
	// this.sqlViewer.setDbConfig(this.config);
	// getContributor().setActivePage(this);
	// }
	// }

	public void doSave(IProgressMonitor progressMonitor) {
		try {
			super.doSave(progressMonitor);
			clearError();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	public void doSaveAs() {
		try {
			super.doSaveAs();
			clearError();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	protected void editorContextMenuAboutToShow(IMenuManager menu) {
		super.editorContextMenuAboutToShow(menu);
		menu.add(new Separator());
	}

	public PLSQLSourceViewer getPLSQLSourceViewer() {
		return (PLSQLSourceViewer) sqlViewer;
	}

	public void clearError() {
		try {
			resource.deleteMarkers(null, true, IResource.DEPTH_ZERO);
		} catch (CoreException e) {
			DbPlugin.getDefault().getLog().log(e.getStatus());
		}
	}

	public void setError(OracleSourceErrorInfo[] errors) {
		if (resource != null) {
			try {
				StyledText text = getSourceViewer().getTextWidget();
				IDocument doc = getSourceViewer().getDocument();

				for (int i = 0; i < errors.length; i++) {
					OracleSourceErrorInfo info = errors[i];

					int errorLine = info.getLine() > 0 ? info.getLine() - 1 : 0;
					int errorPosition = info.getPosition() > 0 ? info.getPosition() - 1 : 0;

					try {
						IRegion region = doc.getLineInformation(errorLine);
						int len = region.getLength() > 0 ? region.getLength() - 1 : 0;
						String str = doc.get(region.getOffset() + errorPosition, len);

						String target = str.replaceAll("\\p{Space}.*", "");

						ITextSelection selection = new TextSelection(doc, region.getOffset() + errorPosition, target.length());

						if (!selection.isEmpty()) {
							int start = selection.getOffset();
							int length = selection.getLength();

							if (length < 0) {
								length = -length;
								start -= length;
							}
							Map attributes = new HashMap();
							MarkerUtilities.setCharStart(attributes, start);
							MarkerUtilities.setCharEnd(attributes, start + length);
							int line = selection.getStartLine();

							// MarkerUtilities.setLineNumber(attributes, line ==
							// -1 ? -1 : errorLine+1);
							MarkerUtilities.setLineNumber(attributes, line == -1 ? -1 : errorLine);

							MarkerUtilities.setMessage(attributes, info.getErrorText());

							attributes.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_ERROR));
							attributes.put("selection", selection.getText() == null ? "" : selection.getText());

							MarkerUtilities.createMarker(resource, attributes, "zigen.plugin.db.markers.myProblem");
						}
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}

			} catch (CoreException e) {
				DbPlugin.getDefault().getLog().log(e.getStatus());
			}
		}
	}

}
