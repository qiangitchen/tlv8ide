package com.tulin.v8.ide.ui.editors.page;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.JavaOutlinePage;
import org.eclipse.wst.sse.ui.internal.contentoutline.ConfigurableContentOutlinePage;

@SuppressWarnings("restriction")
public class MutiPageContentOutlinePage extends Page implements
		IContentOutlinePage {
	private PageBook jdField_a_of_type_OrgEclipseUiPartPageBook;
	private IContentOutlinePage jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage;
	private boolean jdField_a_of_type_Boolean;

	public MutiPageContentOutlinePage() {
	}

	public void createControl(Composite paramComposite) {
		this.jdField_a_of_type_OrgEclipseUiPartPageBook = new PageBook(
				paramComposite, 0);
		if (this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage != null)
			setPageActive(this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage);
	}

	public void dispose() {
		if ((this.jdField_a_of_type_OrgEclipseUiPartPageBook != null)
				&& (!this.jdField_a_of_type_OrgEclipseUiPartPageBook
						.isDisposed()))
			this.jdField_a_of_type_OrgEclipseUiPartPageBook.dispose();
		this.jdField_a_of_type_OrgEclipseUiPartPageBook = null;
		this.jdField_a_of_type_Boolean = true;
	}

	public boolean isDisposed() {
		return this.jdField_a_of_type_Boolean;
	}

	public Control getControl() {
		return this.jdField_a_of_type_OrgEclipseUiPartPageBook;
	}

	public void setActionBars(IActionBars paramIActionBars) {
	}

	public void setFocus() {
		if (this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage != null)
			this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage
					.setFocus();
	}

	public void addSelectionChangedListener(
			ISelectionChangedListener paramISelectionChangedListener) {
		if (this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage != null)
			this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage
					.addSelectionChangedListener(paramISelectionChangedListener);
	}

	public ISelection getSelection() {
		return this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage != null ? this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage
				.getSelection() : null;
	}

	public void removeSelectionChangedListener(
			ISelectionChangedListener paramISelectionChangedListener) {
		if (this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage != null)
			this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage
					.removeSelectionChangedListener(paramISelectionChangedListener);
	}

	public void setSelection(ISelection paramISelection) {
		if (this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage != null)
			this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage
					.setSelection(paramISelection);
	}

	public void setPageActive(IContentOutlinePage paramIContentOutlinePage) {
		this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage = paramIContentOutlinePage;
		// getSite().getActionBars().getToolBarManager().removeAll();
		// getSite().getActionBars().getMenuManager().removeAll();
		if (getSite() != null) {
			if ((paramIContentOutlinePage instanceof JavaOutlinePage)) {
				((JavaOutlinePage) paramIContentOutlinePage).init(getSite());
			} else if ((paramIContentOutlinePage instanceof ConfigurableContentOutlinePage)) {
				((ConfigurableContentOutlinePage) paramIContentOutlinePage)
						.init(getSite());
			}
			Control localControl = paramIContentOutlinePage.getControl();
			if ((localControl == null) || (localControl.isDisposed())) {
				paramIContentOutlinePage
						.createControl(this.jdField_a_of_type_OrgEclipseUiPartPageBook);
				localControl = paramIContentOutlinePage.getControl();
			}
			getSite().getActionBars().updateActionBars();
			this.jdField_a_of_type_OrgEclipseUiPartPageBook
					.showPage(localControl);
		}
	}

	public PageBook getPagebook() {
		return this.jdField_a_of_type_OrgEclipseUiPartPageBook;
	}

	public void init(IPageSite paramIPageSite) {
		super.init(paramIPageSite);
	}
}