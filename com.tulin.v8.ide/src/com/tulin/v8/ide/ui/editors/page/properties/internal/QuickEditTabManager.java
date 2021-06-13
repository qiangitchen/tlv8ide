package com.tulin.v8.ide.ui.editors.page.properties.internal;

import javax.xml.namespace.QName;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jst.jsf.common.metadata.Entity;
import org.eclipse.jst.jsf.common.metadata.Trait;
import org.eclipse.jst.jsf.common.metadata.internal.IMetaDataDomainContext;
import org.eclipse.jst.jsf.common.metadata.query.internal.MetaDataQueryContextFactory;
import org.eclipse.jst.jsf.common.metadata.query.internal.MetaDataQueryFactory;
import org.eclipse.jst.jsf.common.metadata.query.internal.taglib.ITaglibDomainMetaDataQuery;
import org.eclipse.jst.jsf.context.resolver.structureddocument.IStructuredDocumentContextResolverFactory;
import org.eclipse.jst.jsf.context.resolver.structureddocument.ITaglibContextResolver;
import org.eclipse.jst.jsf.context.structureddocument.IStructuredDocumentContext;
import org.eclipse.jst.jsf.context.structureddocument.IStructuredDocumentContextFactory;
import org.eclipse.jst.pagedesigner.editors.HTMLEditor;
import org.eclipse.jst.pagedesigner.editors.properties.quickedittabsections.QuickEditTabSections;
import org.eclipse.jst.pagedesigner.properties.DesignerPropertyTool;
import org.eclipse.jst.pagedesigner.properties.internal.NullQuickEditTabGroupDescriptor;
import org.eclipse.jst.pagedesigner.properties.internal.QuickEditTabSectionsDescriptor;
import org.eclipse.jst.pagedesigner.properties.internal.QuickEditTabSectionsManager;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.FileEditorInput;
import org.w3c.dom.Element;

import com.tulin.v8.ide.ui.editors.page.properties.WPETabbedPropertySheetPage;

/**
 * One-to-one with {@link WPETabbedPropertySheetPage} that manages the current
 * sections for the current selection.
 * <p>
 * The QuickEditTabManager has a shared instance of a
 * {@link QuickEditTabSectionsManager} for the project.
 * <p>
 * When a WPETabbedPropertySheetPage is created, it must acquire a
 * QuickEditTabManager using the static acquireInstance method, and then release
 * the instance when it is disposed. This ensures that the
 * QuickEditTabSectionsManager shared instance (per project) is released
 * appropriately.
 * <p>
 * selectionChanged method must be called prior to calling createControls on the
 * sections for this tab group call.
 */
@SuppressWarnings("restriction")
public class QuickEditTabManager {
	private QuickEditTabSectionsManager _groupsManager;
	private QuickEditTabSections _quickEditTabSections;
	private Entity _tagEntity;
	private QName _lastTagID;
	private QuickEditTabSectionsDescriptor _lastQuickEditTabGroup;
	private QuickEditTabSectionsDescriptor _nullQuickEditTabGroup;
	private Element _lastElement;

	private QuickEditTabSectionsManager getRegistry() {
		return _groupsManager;
	}

	/**
	 * Must only be called once per tabbed property sheet as ref count is kept.
	 * Callers must call releaseInstance when the page is disposed
	 * 
	 * @param page
	 * @return instance for this property sheet
	 */
	public static synchronized QuickEditTabManager acquireInstance(WPETabbedPropertySheetPage page) {
		IProject project = getProject(page);
		QuickEditTabManager instance = new QuickEditTabManager();
		instance._groupsManager = QuickEditTabSectionsManager.acquireInstance(project);
		return instance;
	}

	/**
	 * Releases this instance, but does not dispose. Ensures that the
	 * {@link QuickEditTabSectionsManager} is released.
	 */
	public synchronized void releaseInstance() {
		this._groupsManager.releaseInstance();
	}

	/**
	 * Private constructor
	 */
	private QuickEditTabManager() {
		//
	}

	private static IProject getProject(WPETabbedPropertySheetPage page) {
		IProject proj = null;
		IDocument doc = ((HTMLEditor) page.getEditor()).getDocument();
		IStructuredDocumentContext context = IStructuredDocumentContextFactory.INSTANCE.getContext(doc, 0);
		if (context != null) {
			proj = IStructuredDocumentContextResolverFactory.INSTANCE.getWorkspaceContextResolver(context).getProject();
		}
		return proj;

	}

	/**
	 * Must be called so that the sections for the input can be calculated.
	 * 
	 * @param part
	 * @param selection
	 * @return true if current selection is different than during the last call
	 */
	public boolean selectionChanged(IWorkbenchPart part, ISelection selection) {
		boolean hasChanged = false;
		QuickEditTabSections qets = getQuickTabSectionsMetaData(part, selection);
		if (qets == null) {// use null quick edit tab
			hasChanged = (_quickEditTabSections != null);
			_quickEditTabSections = qets;
			_lastQuickEditTabGroup = getNullQuickEditTab();
			_lastTagID = null;
		} else {
			QName tagId = getTagId();
			if (_lastTagID == null || !_lastTagID.equals(tagId)) {
				_quickEditTabSections = qets;
				QuickEditTabSectionsDescriptor group = getRegistry().getQuickEditTabSectionsFor(tagId);
				if (group == null) {
					group = createQuickEditTabGroup(tagId, _quickEditTabSections);
					if (group != null) {
						group.calculateSections();
						getRegistry().addQuickEditTabGroupDescriptor(group);
					}
				}
				_lastQuickEditTabGroup = group;
				_lastTagID = tagId;
			}
		}

		return hasChanged;
	}

	/**
	 * @return current QuickEditTabSectionsDescriptor
	 */
	public QuickEditTabSectionsDescriptor getCurrentTabGroupDescriptor() {
		return _lastQuickEditTabGroup;
	}

	private QuickEditTabSections getQuickTabSectionsMetaData(IWorkbenchPart part, ISelection selection) {

		Element node = DesignerPropertyTool.getElement(part, selection);
		if (node == null) {
			return null;
		}
		if (_lastElement != node) {
			_lastElement = node;
			_quickEditTabSections = null;
			String uri = getTagURIForNodeName(part, selection, node);
			if (uri != null) {
				final ITaglibDomainMetaDataQuery query = getQuery(part, node);
				if (query != null) {
					_tagEntity = query.getQueryHelper().getEntity(uri, node.getLocalName());
					if (_tagEntity != null) {
						Trait pds = query.findTrait(_tagEntity, QuickEditTabSections.TRAIT_ID);
						if (pds != null) {
							_quickEditTabSections = (QuickEditTabSections) pds.getValue();
						}
					}
				}
			}
		}
		return _quickEditTabSections;
	}

	private QuickEditTabSectionsDescriptor createQuickEditTabGroup(QName tagId, QuickEditTabSections tabSections) {
		return new QuickEditTabSectionsDescriptor(_tagEntity, tagId, tabSections);
	}

	private QName getTagId() {
		return new QName(_tagEntity.getModel().getId(), _tagEntity.getId());
	}

	/**
	 * Dispose
	 */
	public void dispose() {
		_lastQuickEditTabGroup = null;
		_lastElement = null;
		_lastTagID = null;
		_nullQuickEditTabGroup = null;
		_quickEditTabSections = null;
		_groupsManager = null;
	}

	private String getTagURIForNodeName(IWorkbenchPart part, ISelection selection, Element node) {
		HTMLEditor ed = null;
		if (part instanceof HTMLEditor)
			ed = (HTMLEditor) part;
		if (ed == null)
			return null;

		if (ed.getEditorInput() instanceof FileEditorInput) {
			IStructuredDocumentContext context = IStructuredDocumentContextFactory.INSTANCE.getContext(ed.getDocument(),
					node);
			if (context != null) {
				ITaglibContextResolver resolver = IStructuredDocumentContextResolverFactory.INSTANCE
						.getTaglibContextResolver(context);
				if (resolver != null) {
					return resolver.getTagURIForNodeName(node);
				}
			}
		}
		return null;
	}

	private ITaglibDomainMetaDataQuery getQuery(IWorkbenchPart part, Element node) {
		HTMLEditor ed = null;
		if (part instanceof HTMLEditor)
			ed = (HTMLEditor) part;
		if (ed == null)
			return null;

		if (ed.getEditorInput() instanceof FileEditorInput) {
			final FileEditorInput input = (FileEditorInput) ed.getEditorInput();
			final IMetaDataDomainContext mdContext = MetaDataQueryContextFactory.getInstance()
					.createTaglibDomainModelContext(input.getFile());
			return MetaDataQueryFactory.getInstance().createQuery(mdContext);
		}
		return null;
	}

	private QuickEditTabSectionsDescriptor getNullQuickEditTab() {
		if (_nullQuickEditTabGroup == null) {
			_nullQuickEditTabGroup = new NullQuickEditTabGroupDescriptor();
			_nullQuickEditTabGroup.calculateSections();
		}
		return _nullQuickEditTabGroup;
	}

	/**
	 * NOT API - for JUnit testing only
	 * 
	 * @return {@link QuickEditTabSectionsManager}
	 */
	public QuickEditTabSectionsManager getQuickEditTabSectionsManager() {
		return _groupsManager;
	}

}
